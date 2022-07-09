(function () {
	'use strict';

	angular
		.module('naut')
		.controller('AlokasiAnggaran02Controller', AlokasiAnggaran02Controller);

	function AlokasiAnggaran02Controller($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state ) {
		var form = this;
		
		$scope.inputDisable = false;
		$scope.btnDisable = false;
		
		$scope.errormessageAccNum = '';
		
		form.dtInstance = {};	

    	form.tempObj = [];
		
		function callback(json) {
		}

		form.reloadData = function () {
			
		    var resetPaging = false;
		    
		    if(form.dtInstance != null){
			    form.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

        form.getDataList = function () {
	    	form.dtOptions = DTOptionsBuilder.newOptions()
	        
	        	form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/alokasianggaran/AlokasiAnggaranServices/getAlokasiAnggaranByPagination', {});
	         	form.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	                $compile(nRow)($scope);
	                var start = this.fnSettings()._iDisplayStart;
	                $("td:first", nRow).html(start + iDisplayIndex + 1);
	            })
	            .withDataProp('data')
                //.withOption('scrollY', '410px')
                .withOption('scrollX', '100%')
	            .withOption('processing', true)
	            .withOption('serverSide', true)
	            .withPaginationType('full_numbers')
	            .withDisplayLength(10)
                .withOption('responsive', true);
	
	        	form.dtColumns = [
		                DTColumnBuilder.newColumn(null).withTitle('No').notSortable(),
	
		                /*DTColumnBuilder.newColumn(null).withTitle('Account Code')
		                .renderWith(function (data, type, full, meta) {
		                    return data.nomorDraft;
		                }),*/
		                DTColumnBuilder.newColumn(null).withTitle('Account Name')
		                .renderWith(function (data, type, full, meta) {
		                    return data.accountName;
		                }),
		                DTColumnBuilder.newColumn(null).withTitle('Type')
		                .renderWith(function (data, type, full, meta) {
		                	if(data.jenisAnggaran != null){
		                        return data.jenisAnggaran.nama;
		                	}else{
		                		return '';
		                	}		                	
		                }),
		                DTColumnBuilder.newColumn(null).withTitle('Department')
		                .renderWith(function (data, type, full, meta) {
		                	if(data.biroPengadaan != null){
		                        return data.biroPengadaan.nama;
		                	}else{
		                		return '';
		                	}
		                }),
                    
		               DTColumnBuilder.newColumn(null).withTitle('Periode')
		                .renderWith(function (data, type, full, meta) {
		                    return data.tahunAnggaran;
		                }),
                        
		                DTColumnBuilder.newColumn(null).withTitle('Start Budget').withOption('width', '15%')
		                .renderWith(function (data, type, full, meta) {
		                    return ($filter('currency')(data.jumlah,'',2) + ' ' + data.mataUang.kode) ;
		                }).withClass("number"),
		                
		                DTColumnBuilder.newColumn(null).withTitle('Booked Budget').withOption('width', '15%')
		                .renderWith(function (data, type, full, meta) {
		                    return ($filter('currency')(data.bookingAnggaran,'',2) + ' ' + data.mataUang.kode);
		                }).withClass("number"),
		                
		                DTColumnBuilder.newColumn(null).withTitle('Used Budget').withOption('width', '15%').notSortable()
		                .renderWith(function (data, type, full, meta) {
		                    return ($filter('currency')((data.jumlah - (data.bookingAnggaran + data.sisaAnggaran)) ,'',2)  + ' ' + data.mataUang.kode);
		                }).withClass("number"),
		                
		                DTColumnBuilder.newColumn(null).withTitle('Available').withOption('width', '15%')
		                .renderWith(function (data, type, full, meta) {
		                    return ($filter('currency')(data.sisaAnggaran,'',2)  + ' ' + data.mataUang.kode);
		                }).withClass("number"),

		                DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {   
		                	form.tempObj[data.id] = data;
		                	
	                return  '<a class="view-order badge bg-primary" tooltip-placement="top" tooltip="Edit" ng-click="btnEditAnggaran(form.tempObj['+data.id+'])">'+
							'<em class="glyphicon glyphicon-pencil"></em></a>'+
		                    '<a class="view-order badge bg-danger" ng-show="data.bookingAnggaran != 0" ng-click="btnDeleteAnggaran(form.tempObj['+data.id+'])" tooltip-placement="top" tooltip="Delete">'+
							'<em class="glyphicon glyphicon-trash"></em></a>'
				
	            })
	            ];
	        	
                
        }

        form.getDataList();

        form.alokasianggaranIndex = function (instance) {
            form.dtInstance = instance.alokasianggaranIndex;
        }
    		
		//Delete Anggaran
		$scope.btnDeleteAnggaran = function (anggaran) {
			var usedBudget = (anggaran.jumlah - (anggaran.bookingAnggaran + anggaran.sisaAnggaran));
			
			$scope.loading = true;
			if(anggaran.bookingAnggaran != 0 || usedBudget != 0) {
				RequestService.informError('Mohon maaf, saat ini anggaran sedang dipakai');
			} else {
				RequestService.modalConfirmation('Anda yakin ingin menghapus data ini ?')
				.then(function () {
					$http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/delete/' + anggaran.id)
						.success(function (data) {
							$scope.loading = false;
							form.reloadData();
						})
						.error(function (error) {
							console.error(error);
							$scope.loading = false;
						})
				})
				
			}
			

		}

		//Add Anggaran
		$scope.btnAddAnggaran = function (id, size) {
			$scope.loading = false;
			
			$rootScope.anggaranSelected = {};
			var modalInstance = $modal.open({
				templateUrl: id,
				controller: ModalAnggaran02Controller,
				size: size
			});
			modalInstance.result.then(function () {
				form.reloadData();
			});

		};
		//Edit Anggaran
		
		$rootScope.anggaranSelected = {};
		$scope.btnEditAnggaran = function (anggaranId) {
			
			$rootScope.anggaranSelected = anggaranId;
			$scope.inputDisable = true;
			$scope.btnDisable = true;
			
			var modalInstance = $modal.open({
				templateUrl: 'addCost.html',
				controller: ModalAnggaran02Controller,
				size: 'lg'
			});

			modalInstance.result.then(function () {			
				form.reloadData();
			});

		};
		
		$scope.importData = function() {
	    	$state.go("app.promise.procurement-alokasianggaran-index02-import");
	    }
		
		var ModalAnggaran02Controller = function ($scope, $modalInstance, $filter, DTRendererFactory, DTRendererService, ModalService) {
			$scope.form = {};
			$scope.loading = true;
			var isNew = true;
			if (typeof $rootScope.anggaranSelected.id !== 'undefined') {
				$scope.form.anggaranId = $rootScope.anggaranSelected.id;
				$scope.form.accountCode = $rootScope.anggaranSelected.nomorDraft;
                $scope.form.tahunAnggaran= $rootScope.anggaranSelected.tahunAnggaran;
				$scope.form.accountName = $rootScope.anggaranSelected.accountName;
				$scope.form.jenisAnggaranId = $rootScope.anggaranSelected.jenisAnggaran.id;
				$scope.form.start = $rootScope.anggaranSelected.jumlah;
				/*$scope.form.booked = $rootScope.anggaranSelected.bookingAnggaran;
				$scope.form.available = $rootScope.anggaranSelected.jumlah;*/
				isNew = false;
				$scope.btnDisable = true;
				$scope.inputDisable = true;
				
			}
			
			$scope.generateCode = function () {
				$http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getcoanumber')
				.success(function(data, status, headers, config) {
					$scope.form.accountCode = data.coa;
				})
				
			};

			$scope.departementList = [];
			$scope.getDepartementList = function () {
				$http.get($rootScope.backendAddress + '/procurement/master/panitiaServices/getPanitiaByUserId/' + $rootScope.userLogin.user.id)
					.success(function (data, status, headers, config) {
						$scope.panitia = data;
						if (data.length == 0) {
							$http.get($rootScope.backendAddress + '/procurement/user/get-role-user/' + $rootScope.userLogin.user.id)
								.success(function (dataRoleUser, status, headers, config) {
									$scope.panitia = {};
									$scope.panitia.cabang = dataRoleUser[0].organisasi;
									$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + dataRoleUser[0].organisasi.id)
										.success(function (data, status, headers, config) {
											$scope.departementList = data;

											if (typeof $rootScope.anggaranSelected !== 'undefined') {
												angular.forEach($scope.departementList, function (value, index) {
													if (typeof $rootScope.anggaranSelected.biroPengadaan !== 'undefined') {
														if ($rootScope.anggaranSelected.biroPengadaan.id == value.id) {
															$scope.form.departementSelected = value;
														}
													}
												});
											}
											$scope.loading = false;
										}).error(function (data, status, headers, config) {
											console.error(data);
											scope.loading = false;
										});
								});
						} else {
							$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + data.cabang.id)
								.success(function (data, status, headers, config) {
									$scope.departementList = data;
									if (typeof $rootScope.anggaranSelected !== 'undefined') {
										angular.forEach($scope.departementList, function (value, index) {
											if (typeof $rootScope.anggaranSelected.biroPengadaan !== 'undefined') {
												if ($rootScope.anggaranSelected.biroPengadaan.id == value.id) {
													$scope.form.departementSelected = value;
												}
											}
										});
									}
									$scope.loading = false;
								}).error(function (data, status, headers, config) {
									console.error(data);
									scope.loading = false;
								});
						}

					});
			};

			//jenis anggaran
			$http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getJenisList')
				.success(function (data, status, headers, config) {
					$scope.jenisAnggaranList = data;
					$scope.getDepartementList();
				});
			
			
			/*procurement/alokasianggaran/AlokasiAnggaranServices/getAlokasiAnggaranByNomorDraft/ */
			
			// validate
			
			$scope.post= function(){
				$scope.loading = true;
				
				var anggaranPost = {
						nomorDraft: $scope.form.accountCode,
						accountName: $scope.form.accountName,
						jenisAnggaran: $scope.form.jenisAnggaranId,
						biroPengadaan: $scope.form.departementSelected.id,
                        tahunAnggaran: $scope.form.tahunAnggaran,
						jumlah: $scope.form.start,
						sisaAnggaran: $scope.form.start,
						bookingAnggaran: 0, 
						status: 0, //0 : belum digunakan, 1 : sudah digunakan
						mataUang: 1, //IDR
//						tahunAnggaran: $filter('date')((new Date()).getTime(), 'yyyy'),
						periodeAnggaran: 0
					};
				
				var url = $rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/create';
				
				if (!isNew) { //jika update
					anggaranPost.id = $scope.form.anggaranId;
					url = $rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/update';
				}

				//console.info(anggaranPost);
				$http({
					method: 'POST',
					url: url,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					transformRequest: function (obj) {
						var str = [];
						for (var p in obj)
							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data: anggaranPost
				}).success(function (data, status, headers, config) {
					$scope.loading = false; 
					$modalInstance.close('closed');
					form.reloadData();
				})
				.error(function (data) {
					$scope.loading = false;
					console.error(data);
					form.reloadData();
				});
			}
			
			$scope.checkAccountCode = function() {
				
            	$http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getAlokasiAnggaranByNomorDraft/' + $scope.form.accountCode , {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data.nomorDraft == $scope.form.accountCode && isNew){
                    	$scope.errormessageAccNum = "Account Code tidak boleh sama.";
                    } else {
                    	var alokasiAnggaran = {
                    		accountName : $scope.form.accountName
                		}
                			
            			RequestService.doPOST('/procurement/alokasianggaran/AlokasiAnggaranServices/getAlokasiAnggaranByAccountName', alokasiAnggaran)
            			.then(function successCallback(response) {
            				
            				if(response.data.accountName == $scope.form.accountName && isNew) {
            					$scope.errormessageAccName = "Account Name tidak boleh sama.";
            				} else {
            					$scope.post();
            				}
            				 
            			}); 
                    	
                    }
                    
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                }) 
			}
					
			$scope.btnSimpan = function () {
				var valText = /[^a-zA-Z0-9/\s/g\-]/i;
				var isValid = 'true';
 				$scope.errormessageAccNum = '';

				if (typeof $scope.form.accountCode === 'undefined' || $scope.form.accountCode == '') {
					isValid = false;
					$scope.errormessageAccNum = 'Account Code harus diisi!';
				}
				if ($scope.form.accountCode.match(valText)) {
					isValid = false;
					$scope.errormessageAccNum = 'Account Code berisi simbol!';
				}
				$scope.errormessageAccName = '';
				if (typeof $scope.form.accountName === 'undefined' || $scope.form.accountName == '') {
					isValid = false;
					$scope.errormessageAccName = 'Account Name harus diisi!';
				}
				if ($scope.form.accountName.match(valText)) {
					isValid = false;
					$scope.errormessageAccName = 'Account Name berisi simbol!';
				}
				$scope.errormessageJnsAgrn = '';
				if (typeof $scope.form.jenisAnggaranId === 'undefined' || $scope.form.jenisAnggaranId == '') {
					isValid = false;
					$scope.errormessageJnsAgrn = 'Jenis Anggaran harus diisi!';
				}
				$scope.errormessageDep = '';
				if (typeof $scope.form.departementSelected === 'undefined' || $scope.form.departementSelected == '') {
					isValid = false;
					$scope.errormessageDep = 'Departemen harus diisi!';
				}
				$scope.errormessageBugStart = '';
				if (typeof $scope.form.start === 'undefined' || $scope.form.start == '') {
					isValid = false;
					$scope.errormessageBugStart = 'Budget Start harus diisi!';
				}
				else {
					$scope.checkAccountCode();
				}
				
			}
			$scope.cancel = function () {
				$modalInstance.close('closed');
			}

		};
		ModalAnggaran02Controller.$inject = ['$scope', '$modalInstance', '$filter', 'DTRendererFactory', 'DTRendererService', 'ModalService']

	}

	AlokasiAnggaran02Controller.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile','$filter', 'DTRendererFactory', 'DTRendererService', '$state'];

})();