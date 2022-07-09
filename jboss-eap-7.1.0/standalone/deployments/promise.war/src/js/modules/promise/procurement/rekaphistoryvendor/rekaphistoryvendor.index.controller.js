(function () {
    'use strict';
    angular
        .module('naut')
        .controller('RekapHistoryVendorIndexControllerController', RekapHistoryVendorIndexControllerController);

    function RekapHistoryVendorIndexControllerController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $state, $translate, $interval) {

		//$ocLazyLoad.load('src/js/modules/promise/procurement/rekaphistoryvendor/rekaphistoryvendor.view.controller.js');
		
    	var vm = this;
		
		var dtInstance;
		vm.purchaseRequestItemList = [];
		vm.Math = window.Math;
		
		vm.paramDataTable= [];
		
		function callback(json) {
		    console.log(json);
		}
		
        $scope.getDataList = function () {
        	vm.tempObj = [];
        	
            vm.dtOptions = RequestService.requestServerPaggingRekapHistory(DTOptionsBuilder.newOptions(), '/procurement/vendor/vendorServices/getVendorPaggingList');
            /** untuk custom field **/
            vm.dtOptions.withOption('fnRowCallback', function (nRow, data, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
                
                var start = this.fnSettings()._iDisplayStart;
                
                $("td:first", nRow).html(start + iDisplayIndex + 1).attr("align","center");
                $("td", nRow).attr("align","center");
                $("td:eq(1), td:eq(2)", nRow).attr("align","left");
            })
            .withOption('headerCallback',function( thead, data, start, end, display ) {
                $compile(angular.element(thead).contents())($scope);
            })

            /** untuk menampilkan colom data **/
            vm.dtColumns = [
				DTColumnBuilder.newColumn('id').withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable(), 
				DTColumnBuilder.newColumn('nama').withTitle('<div style="text-align:center"><span translate="promise.procurement.manajemenvendor.rekaphistoryvendor.list.nama_vendor"></span></div>'),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center">Rating</div>').renderWith(function(data){
					return '<span class="stars2 " tooltip-placement="top" tooltip="' + $filter('currency')((data.performanceAvg == null ? 0 : data.performanceAvg),'',2) + '">' + 
			          '<span style="width:' + $scope.getStars(data.performanceAvg) + '"></span>' + 
			        '</span>';
				}),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.manajemenvendor.rekaphistoryvendor.list.pernah_mengikuti_pengadaan"></span></div>').renderWith(function(data){
					return '<span align="center">' + $filter('currency')(data.pengadaanCount,'',0);
				}),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.manajemenvendor.rekaphistoryvendor.list.sedang_mengikuti_pengadaan"></span></div>').renderWith(function(data){
					return '<span align="center">' + $filter('currency')(data.pengadaanRunningCount,'',0);
				}),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.manajemenvendor.rekaphistoryvendor.list.menang_pengadaan"></span></div>').renderWith(function(data){
					return '<span align="center">' + $filter('currency')(data.winnerCount,'',0);
				}),
		        DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(
		            function (data, type, full, meta) {
		            	vm.paramDataTable = data.paramDataTable;
			            vm.tempObj[data] = data;			            
						return '<button id="join_' + data.id + '" class="btn btn-info btn-sm " ng-click="viewDetail(' + data.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >';
					}
                )
            ];
        }
        $scope.getDataList();

        DTInstances.getLast().then(function (instance) {
            dtInstance = instance;
        }); 
        
        $scope.getStars = function(rating) {
        	if(rating != null) {
                var val = parseFloat(rating);
                var size = val/5*100;
                return size + '%';
        	} else {
        		return 0 + '%';
        	}

        }        
        
        $scope.viewDetail = function(vendor){
			$rootScope.rekapDataRekanan = vendor;
			$state.go('app.promise.procurement-manajemenvendor-rekaphistoryvendor-view');
        }
        
        $scope.exportExcel = function() {
        	if($rootScope.dtInstanceRekapHistory.json.recordsTotal == 0) {
    	   		alert("Mohon maaf tidak ada data untuk di export");
    	   	} else {
	        	if(vm.paramDataTable[0] == "") {
	        		vm.paramDataTable[0] = "\"\"";
	        	}
	        	
	    		$http.get($rootScope.backendAddress+'/procurement/report/vendor/print/'+ vm.paramDataTable[0] + '/' + vm.paramDataTable[1] + '/' + vm.paramDataTable[2] , {
	                responseType: 'arraybuffer'
	            })
	            .success(function (data) {
	                //$log.info('typeof data = ' + typeof data);
	
	                if (typeof data == 'object') {
	
	                }
	                //posibilty error
	                else if (typeof data == 'undefined') {
	                    //get error code
	                    $http.get(url)
	                        .success(function (data) {
	                            var errorCode = data.substr(data.indexOf("PRMS-ERR"), 12);
	                            $location.path('/page/errorPage/' + errorCode);
	                        });
	                    return;
	                }
	                
	                
	                var file = new Blob([data], {
	                    type: 'application/vnd.ms-excel'
	                });
	                //var fileURL = URL.createObjectURL(file);
	                //indow.open(fileURL, 'C-Sharpcorner');
	                
	                var blobURL = (window.URL || window.webkitURL).createObjectURL(file);
	                var anchor = document.createElement("a");
	                anchor.download = "Rekap-History-Vendor.xls";
	                anchor.href = blobURL;
	                anchor.click();
	                
	                $scope.loading = false;
	                
	            });
    	   	}
        	
    		return true;
    	}
        
    }

    RekapHistoryVendorIndexControllerController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$state', '$translate', '$interval'];

})();