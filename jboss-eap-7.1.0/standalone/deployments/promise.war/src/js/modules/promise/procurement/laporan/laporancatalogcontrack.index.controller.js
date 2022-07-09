/**
 * ========================================================= Module:
 * LaporanCatalogContrackController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanCatalogContrackController',
			LaporanCatalogContrackController);

	function LaporanCatalogContrackController(RequestService, ModalService, $scope,
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
			namaPekerjaan : '',
			pageNo : 1,
			pageSize : 5,
			dataList : {},
			jmlData : {},
			orderKeyword : '',
			vendorName : '',
			sort : '',
			startDate : '',
			endDate : ''

		};

		vm.sortHeaderList = [ {
			name : 'Nomor Kontrak',
			value : 'nomorKontrak'
		}, {
			name : 'Nama Pekerjaan',
			value : 'namaPekerjaan'
		}, {
			name : 'Nilai Kontrak',
			value : 'nilaiKontrak'
		}, {
			name : 'Penyedia',
			value : 'vendor.nama'
		}, {
			name : 'Tgl Akhir',
			value : 'tglAkhirKontrak'
		} ];
		
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
		
		$scope.catalogContrackList = function() {

			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}		
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-list-contrack-catalog', vm.param)
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

		$scope.catalogContrackList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-catalog-contract';
			RequestService.doPostDownloadExcel(targetURL, vm.param, "Report Catalog Contract");
		}

		$scope.setPage = function() {
			$scope.catalogContrackList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.catalogContrackList();
		}

	}

	LaporanCatalogContrackController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
