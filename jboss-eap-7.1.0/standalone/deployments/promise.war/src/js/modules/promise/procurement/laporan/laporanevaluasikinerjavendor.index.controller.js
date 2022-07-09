/**
 * ========================================================= Module:
 * LaporanEvaluasiKinerjaVendorController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanEvaluasiKinerjaVendorController',
			LaporanEvaluasiKinerjaVendorController);

	function LaporanEvaluasiKinerjaVendorController(RequestService, ModalService, $scope,
			$http, $rootScope, $resource, $location, $modal, $state, $filter,
			$stateParams, $log) {
		var vm = this;
		
		 $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
	        $scope.format = $scope.formats[0];
	        $scope.tanggalStartOpen = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.tanggalStartStatus = true;
			}
	        $scope.tanggalEndOpen = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.tanggalEndStatus = true;
			}
		
		vm.param = {
			vendorName : '',
			pageNo : 1,
			pageSize : 5,
			dataList : {},
			jmlData : {},
			orderKeyword : '',
			sort : ''

		};
		
		vm.sortHeaderList = [ {
		name : 'Nama Penyedia',
		value : 'vendor.nama'
		}];
		
		
		vm.sortAscDesc = [ {
			name : 'ASC',
			value : 'ASC'
		}, {
			name : 'DESC',
			value : 'DESC'
		} ];
		
		
		$scope.vendorList = function () {
		       
            RequestService.doGET('/procurement/laporan/get-vendor-list').then(function success(data) {
	        	vm.vendorList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
	  $scope.vendorList();

		$scope.evaluasiKinerjaVendorList = function() {
			var data = vm.param;
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-list-evaluasi-kinerja-vendor', vm.param)
					.then(
							function success(data) {
								if (data.data.dataList != undefined
										&& data.data.dataList.length > 0) {
									vm.dataList = data.data.dataList;
									vm.totalItems = data.data.jmlData;

								} else {
									vm.dataList = [];
									vm.totalItems = 0;
								}
								vm.loading = false;
							}, function error(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		}
		
		$scope.rattings = function(ratting){
			let rattings=['Sangat Kurang','Kurang', 'Cukup', 'Baik', 'Sangat Baik', 'Sangat Memuaskan']
			
			
			if (ratting != null){
				if (ratting <=1.00){
					ratting=0;
				}
				
				else if (ratting<=2.00){
					ratting=1;
				}
				else if (ratting<=3.00){
					ratting=2;
				}	
				else if (ratting<=4.00){
					ratting=3;
				}
				else if (ratting<=5.00){
					ratting=4;
				}
				else if (ratting<=6.00){
					ratting=5;
				}
			} else {
				return '-';
			}
			
			return rattings[ratting]
		}

		$scope.evaluasiKinerjaVendorList();

		$scope.detail =  function(evaluasiKinerjaVendor){
			$state.go('app.promise.procurement-laporan-evaluasi-kinerja-vendor-detail', {
				evaluasiKinerjaVendor: evaluasiKinerjaVendor
			});
		}
		
		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-evaluasi-kinerja-vendor';
			RequestService.doPostDownloadExcel(targetURL, vm.param);
		}

		$scope.setPage = function() {
			$scope.evaluasiKinerjaVendorList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.evaluasiKinerjaVendorList();
		}

	}

	LaporanEvaluasiKinerjaVendorController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();