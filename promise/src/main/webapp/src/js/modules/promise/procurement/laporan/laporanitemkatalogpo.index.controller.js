/**
 * ========================================================= Module:
 * LaporanCatalogContrackController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanItemCatalogPoController',
			LaporanItemCatalogPoController);

	function LaporanItemCatalogPoController(RequestService, ModalService, $scope,
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
			search : '',
			pageNo : 1,
			pageSize : 10,
			dataList : {},
			jmlData : {},
			type : 0,
			vendorName : '',
			startDate : '',
			endDate : ''

		};

		vm.sortHeaderList = [ {
			name : 'Tanggal PO ASC',
			value : 1
		}, {
			name : 'Tanggal PO DESC',
			value : 2
		}, {
			name : 'Total ASC',
			value : 3
		}, {
			name : 'Total DESC',
			value : 4
		} ];

		$scope.vendorList = function () {
            RequestService.doGET('/procurement/laporan/get-vendor-list-catalog-contract').then(function success(data) {
	        	vm.vendorList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		$scope.vendorList();
		
		$scope.itemCatalogPoList = function() {

			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}
			
			
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-item-katalog-po', vm.param)
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

		$scope.itemCatalogPoList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-item-katalog-po';
			RequestService.doPostDownloadExcel(targetURL, vm.param, "Report Item Catalog PO");
		}

		$scope.setPage = function() {
			$scope.itemCatalogPoList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.itemCatalogPoList();
		}
		
		$scope.resetFilter = function () {
			vm.param = {
					search : '',
					pageNo : 1,
					pageSize : 10,
					dataList : {},
					jmlData : {},
					type : 0,
					vendorName : '',
					startDate : '',
					endDate : ''
			};
			$scope.itemCatalogPoList();
        }

	}

	LaporanItemCatalogPoController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
