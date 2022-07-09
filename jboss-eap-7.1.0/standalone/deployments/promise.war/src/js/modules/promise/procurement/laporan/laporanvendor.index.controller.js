/**
 * ========================================================= Module:
 * LaporanCatalogController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanVendorController',
			LaporanVendorController);

	function LaporanVendorController(RequestService, ModalService, $scope,
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
				pageSize : 5,
				dataList : {},
				jmlData : {},
				orderKeyword : '',
				vendorName : '',
				asc : '',
				startDate : '',
				endDate : ''

			};

		vm.sortDRList = [ {
			name : 'Penyedia',
			value : 'vendor.nama'
		}, {
			name : 'Bidang Usaha ',
			value : 'subBidangUsaha.bidangUsaha.nama'
		} ];

		vm.sortAscDesc = [ {
			name : 'ASC',
			value : 'ASC'
		}, {
			name : 'DESC',
			value : 'DESC'
		} ];

		$scope.bidangUsahaList = function () {
		       
            RequestService.doGET('/procurement/laporan/get-bidang-usaha-list').then(function success(data) {
	        	vm.bidangUsahaList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
	  $scope.bidangUsahaList();
		
		$scope.vendorList = function() {
			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}	
			vm.loading = true;

			RequestService.doPOST('/procurement/laporan/get-list-vendor', vm.param)
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

		$scope.vendorList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-vendor';
			RequestService.doPostDownloadExcel(targetURL, vm.param);
		}

		$scope.setPage = function() {
			$scope.vendorList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.vendorList();
		}

	}

	LaporanVendorController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
