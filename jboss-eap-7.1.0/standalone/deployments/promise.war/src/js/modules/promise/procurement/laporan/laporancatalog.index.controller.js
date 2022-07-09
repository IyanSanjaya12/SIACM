/**
 * ========================================================= Module:
 * LaporanCatalogController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanCatalogController',
			LaporanCatalogController);

	function LaporanCatalogController(RequestService, ModalService, $scope,
			$http, $rootScope, $resource, $location, $modal, $state, $filter,
			$stateParams, $log) {
		var vm = this;
		
		vm.param = {
			pageNo : 1,
			pageSize : 5,
			dataList : {},
			jmlData : {},
			orderKeyword : '',
			vendorName : ''

		};

		vm.sortDRList = [ {
			name : 'Nama Item Katalog A-Z',
			value : 'catalogContractDetail.itemDesc ASC'
		}, {
			name : 'Nama Item Katalog Z-A',
			value : 'catalogContractDetail.itemDesc DESC'
		}, {
			name : 'Item A-Z', // KAI 20201221 hapus EBS [19451]
			value : 'item.nama ASC'
		}, {
			name : 'Item Z-A', // KAI 20201221 hapus EBS [19451]
			value : 'item.nama DESC'
		}, {
			name : 'Penyedia A-Z',
			value : 'vendor.nama ASC'
		}, {
			name : 'Penyedia Z-A',
			value : 'vendor.nama DESC'
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
		
		$scope.catalogList = function() {
			vm.loading = true;

			RequestService.doPOST('/procurement/laporan/get-list', vm.param)
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

		$scope.catalogList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-item-katalog';
			RequestService.doPostDownloadExcel(targetURL, vm.param, "Report Item Catalog");
		}

		$scope.setPage = function() {
			$scope.catalogList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.catalogList();
		}

	}

	LaporanCatalogController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
