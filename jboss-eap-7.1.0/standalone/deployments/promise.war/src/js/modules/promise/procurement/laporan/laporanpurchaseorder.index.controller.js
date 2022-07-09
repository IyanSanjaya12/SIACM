/**
 * ========================================================= Module:
 * LaporanCatalogController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanPurchaseOrderController',
			LaporanPurchaseOrderController);

	function LaporanPurchaseOrderController(RequestService, ModalService, $scope,
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
				noPo : '',
				pageNo : 1,
				pageSize : 5,
				dataList : {},
				jmlData : {},
				orderKeyword : '',
				vendorName : '',
				startDate : '' ,
				endDate : '' ,
				
			};

		
		vm.sortHeaderList = [ {
			name : 'No PO A-Z',
			value : 'purchaseOrder.poNumber ASC'
		}, {
			name : 'No PO Z-A',
			value : 'purchaseOrder.poNumber DESC'
		}, {
			name : 'Tanggal PO A-Z',
			value : 'purchaseOrder.approvedDate ASC'
		}, {
			name : 'Tanggal PO Z-A',
			value : 'purchaseOrder.approvedDate DESC'
		}, {
			name : 'Nama Item Katalog A-Z',
			value : 'purchaseRequestItem.catalog.catalogContractDetail.itemDesc ASC'
		}, {
			name : 'Nama Item Katalog Z-A',
			value : 'purchaseRequestItem.catalog.catalogContractDetail.itemDesc DESC'
		}, {
			name : 'Nama Item A-Z', // KAI 20201221 hapus EBS [19451]
			value : 'item.nama ASC'
		}, {
			name : 'Nama Item Z-A', // KAI 20201221 hapus EBS [19451]
			value : 'item.nama DESC'
		}, {
			name : 'Penyedia A-Z',
			value : 'vendor.nama ASC'
		}, {
			name : 'Penyedia Z-A',
			value : 'vendor.nama DESC'
		}, {
			name : 'Pengguna A-Z',
			value : 'purchaseOrder.purchaseRequest.organisasi.nama ASC'
		}, {
			name : 'Pengguna Z-A',
			value : 'purchaseOrder.purchaseRequest.organisasi.nama DESC'
		}, {
			name : 'Total Harga A-Z',
			value : 'totalUnitPrices ASC'
		}, {
			name : 'Total Harga Z-A',
			value : 'totalUnitPrices DESC'
		}];

		
		$scope.poList = function() {
			
			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}
			
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-list-purchase-order', data)
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

		$scope.poList();

		  $scope.vendorList = function () {
	       
	            RequestService.doGET('/procurement/laporan/get-vendor-list').then(function success(data) {
		        	vm.vendorList = data;
		        }, function error(response) {
		        	RequestService.informInternalError();
		        	vm.loading = false;
		        });
	       }
		  $scope.vendorList();
		  
		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-purchase-order';
			RequestService.doPostDownloadExcel(targetURL, vm.param, "Report Purchase Order");
		}

		$scope.setPage = function() {
			$scope.poList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.poList();
		}

	}

	LaporanPurchaseOrderController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
