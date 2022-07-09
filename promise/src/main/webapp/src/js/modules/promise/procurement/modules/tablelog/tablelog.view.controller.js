/**
 * ========================================================= 
 * Module: tablelog.view.controller.js
 * =========================================================
 */
 (function () {
	'use strict';
			
	angular
		.module('naut')
		.controller('TableLogViewController', TableLogViewController);

    function TableLogViewController($rootScope, $scope, $stateParams, $filter, $compile, $state, $location, RequestService, DTOptionsBuilder, DTColumnBuilder, $modal) {
        var vm = this;
        vm.tempObj = [];
        vm.userId = ($stateParams.userId != undefined ) ? $stateParams.userId : '';
        
        var today = new Date();
        var year = today.getFullYear();
        var month = (today.getMonth()+1)
        var tableLogForm = {};
        vm.listYear = [];

        // Untuk mendapatkan list tahun 10 tahun kebelakang 
        for(var i=today.getFullYear(); 10<i; i--) {
            vm.listYear.push(i);
            if(i === today.getFullYear() - 10){
                break;
            }
        }

        vm.listMonth = [ 
            {
                "id":1,
                "bulan": "Januari"
            },
            {
                "id":2,
                "bulan": "Februari"
            },
            {
                "id":3,
                "bulan": "Maret"
            },
            {
                "id":4,
                "bulan": "April"
            },
            {
                "id":5,
                "bulan": "Mei"
            },
            {
                "id":6,
                "bulan": "Juni"
            },
            {
                "id":7,
                "bulan": "Juli"
            },
            {
                "id":8,
                "bulan": "Agustus"
            },
            {
                "id":9,
                "bulan": "September"
            },
            {
                "id":10,
                "bulan": "Oktober"
            },
            {
                "id":11,
                "bulan": "November"
            },
            {
                "id":12,
                "bulan": "Desember"
            }
        ]

        tableLogForm = {
            month: 1,
            year: year,
            userid: vm.userId
        }

        var uri = '/procurement/modules/tablelog/tableLogModulesServices/getListTableLogFromFileCsv';
        vm.getListDetailTableLog = function (postData) {
        	
        	vm.dtOptionTableLog = DTOptionsBuilder.newOptions()
        		.withOption('ajax', {
        			url: $rootScope.backendAddress + '/procurement/modules/tablelog/tableLogModulesServices/getListTableLogFromFileCsv',
        			type: 'POST',
        			data: postData,
                    /*PROMISE_SECURITY*/
                   "beforeSend": function(xhr) {
                           xhr.setRequestHeader("Authorization", $rootScope.userToken);
                    },
                    error: function (xhr, error, thrown) {
                              var errorCode = xhr.responseText.substr(xhr.responseText.indexOf("PRMS-ERR"), 12);
                            $location.path('/page/errorPage/' + errorCode);
                       }
                    /*END PROMISE_SECURITY*/
        		})
        		.withOption('fnRowCallback', function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			        $compile(nRow)($scope);
			        var start = this.fnSettings()._iDisplayStart;
		            $("td:first", nRow).html(start + iDisplayIndex + 1);
			    })
                .withOption('headerCallback', function(thead, data, start, end, display) {
                    $compile(angular.element(thead).contents())($scope);
                })
		        .withDataProp('data')
		        .withOption('processing', true)
		        .withOption('serverSide', true)
		        .withPaginationType('full_numbers')
		        .withDisplayLength(10)
		        
		        vm.dtColumnsTableLog = [
                    DTColumnBuilder.newColumn(null).withTitle('No').notSortable(),
                    DTColumnBuilder.newColumn(null).withTitle('Nama Kolom')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.namaKolom == null) ? '-' : data.namaKolom;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Nama Table')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.namaTable == null) ? '-' : data.namaTable;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Nilai Awal').withOption('width', '8%')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.nilaiAwal == null) ? '-' : 
		                	(data.nilaiAwal.length > 50) ? '<plaintext> '+data.nilaiAwal.substring(0,25)+' (...)' : data.nilaiAwal;

		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Nilai Baru').withOption('width', '8%')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.nilaiBaru == null) ? '-' : 
		                	(data.nilaiBaru.length > 50) ? '<plaintext> '+data.nilaiBaru.substring(0,25)+' (...)' : data.nilaiBaru;

		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Nama User')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.user == null) ? '-' : data.user;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Waktu')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.waktu == null) ? '-' : data.waktu;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Action Type')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.actionType == null) ? '-' : data.actionType;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('Detail')
		                .renderWith(function(data, type, full, meta) {
		                	vm.tempObj[data.id] = data;
                            var detailButton;
                            if(data.nilaiAwal != '' && data.nilaiBaru != '') {
                            	if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
                                    detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
								} else {
									detailButton = ''
								}
                            } else {
                            	if(data.nilaiAwal == '' && data.nilaiBaru != '') {
                            		if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
										detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
									} else {
										detailButton = ''
									}
                            	}
                                if(data.nilaiBaru == '' && data.nilaiAwal != '') {
                            		if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
										detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
									} else {
										detailButton = ''
									}
                            	}
                            
                            } 
                            
		                	return detailButton
		            })
                ]
        }
        vm.getListDetailTableLog(tableLogForm);

        vm.param = {
            monthParam: tableLogForm.month,
            yearParam: tableLogForm.year
        }  

        vm.getListDetailTableLogFromYearAndMonth = function () {
            var paramForSelectedMonthAndYear = {};
            if (typeof vm.param.monthParam === 'undefined' || vm.param.monthParam == null) {
                alert('Bulan belum diisi !');
            } else if(typeof vm.param.yearParam === 'undefined' || vm.param.yearParam == null) {
            	alert('Tahun belum diisi !');
            } else {
            	paramForSelectedMonthAndYear = {
					month: vm.param.monthParam,
					year: vm.param.yearParam,
					userid: vm.userId
                }
				vm.dtOptionTableLog = DTOptionsBuilder.newOptions()
        		.withOption('ajax', {
        			url: $rootScope.backendAddress + '/procurement/modules/tablelog/tableLogModulesServices/getListTableLogFromFileCsv',
        			type: 'POST',
        			data: paramForSelectedMonthAndYear,
                    /*PROMISE_SECURITY*/
                   "beforeSend": function(xhr) {
                           xhr.setRequestHeader("Authorization", $rootScope.userToken);
                    },
                    error: function (xhr, error, thrown) {
                              var errorCode = xhr.responseText.substr(xhr.responseText.indexOf("PRMS-ERR"), 12);
                            $location.path('/page/errorPage/' + errorCode);
                       }
                    /*END PROMISE_SECURITY*/
        		})
        		.withOption('fnRowCallback', function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			        $compile(nRow)($scope);
			        var start = this.fnSettings()._iDisplayStart;
		            $("td:first", nRow).html(start + iDisplayIndex + 1);
			    })
		        .withDataProp('data')
		        .withOption('processing', true)
		        .withOption('serverSide', true)
		        .withPaginationType('full_numbers')
		        .withDisplayLength(10)
		        
		        vm.dtColumnsTableLog = [
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="general.no"></span>').notSortable(),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.nama_kolom"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.namaKolom == null) ? '-' : data.namaKolom;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.nama_tabel"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.namaTable == null) ? '-' : data.namaTable;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.nilai_awal"></span>').withOption('width', '8%')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.nilaiAwal == null) ? '-' : 
		                	(data.nilaiAwal.length > 50) ? '<plaintext> '+data.nilaiAwal.substring(0,25)+' (...)' : data.nilaiAwal;

		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.nilai_baru"></span>').withOption('width', '8%')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.nilaiBaru == null) ? '-' : 
		                	(data.nilaiBaru.length > 50) ? '<plaintext> '+data.nilaiBaru.substring(0,25)+' (...)' : data.nilaiBaru;

		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.nama_user"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.user == null) ? '-' : data.user;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.waktu"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.waktu == null) ? '-' : data.waktu;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.action_type"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	return (data.actionType == null) ? '-' : data.actionType;
		            }),
                    DTColumnBuilder.newColumn(null).withTitle('<span class="text-nowrap" translate="tablelog.viewlist.detil"></span>')
		                .renderWith(function(data, type, full, meta) {
		                	vm.tempObj[data.id] = data;
                            var detailButton;
                            if(data.nilaiAwal != '' && data.nilaiBaru != '') {
                            	if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
                                    detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
								} else {
									detailButton = ''
								}
                            } else {
                            	if(data.nilaiAwal == '' && data.nilaiBaru != '') {
                            		if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
										detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
									} else {
										detailButton = ''
									}
                            	}
                                if(data.nilaiBaru == '' && data.nilaiAwal != '') {
                            		if(data.nilaiAwal.length > 50 || data.nilaiBaru.length > 50) {
										detailButton = '<button type="button" class="mr btn btn-info btn-circle" ng-click="vm.detailModalNilaiTable(vm.tempObj['+data.id+'])" ng-disabled="vm.isLoading"><span title="Nilai Awal & Nilai Baru"> <i class="fa fa-search"></i> </span></button>'
									} else {
										detailButton = ''
									}
                            	}
                            
                            } 
                            
		                	return detailButton
		            })
                ]
            }
            
        }

        vm.backIndexTableLog = function() {
            $state.go('app.promise.procurement-tablelog-index')
        }

        vm.detailModalNilaiTable = function (tableLog) {
        	console.log(tableLog);
        	var nilaiAwalParam = tableLog.nilaiAwal;
        	var nilaiBaruParam = tableLog.nilaiBaru;
        	
            return $modal.open({
                templateUrl: '/modalDetailTableLog.html',
                controller: 'modalDetailTableLogController',
                size: 'lg',
                resolve: {
                    nilaiAwal: function () {
                        return nilaiAwalParam;
                    },
                    nilaiBaru: function () {
                        return nilaiBaruParam;
                    }
                }
            }).result;
        }

        angular.module('naut')
        .controller('modalDetailTableLogController', function ($rootScope, $scope, $http, $modalInstance, RequestService, nilaiAwal, nilaiBaru) {
        	
            $scope.nilaiBaruDetail = typeof nilaiBaru == 'undefined' ? '-' : nilaiBaru;

            $scope.nilaiAwalDetail = typeof nilaiAwal == 'undefined' ? '-' : nilaiAwal;


            $scope.ok = function () {
                $modalInstance.dismiss('cancel');
            };
            
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        })

    }

    TableLogViewController.$inject = ['$rootScope', '$scope', '$stateParams', '$filter', '$compile', '$state', '$location', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', '$modal']

})();