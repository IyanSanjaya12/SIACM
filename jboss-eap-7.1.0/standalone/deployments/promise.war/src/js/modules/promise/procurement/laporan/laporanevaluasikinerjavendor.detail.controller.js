(function() {
	'use strict';

	angular.module('naut').controller('LaporanDetailEvaluasiKinerjaVendorController',
			LaporanDetailEvaluasiKinerjaVendorController);

	function LaporanDetailEvaluasiKinerjaVendorController(RequestService, ModalService, $scope,
			$http, $rootScope, $resource, $location, $modal, $state, $filter,
			$stateParams, $log) {
		var vm = this;
		
		vm.evaluasiKinerjaVendor = ($state.params.evaluasiKinerjaVendor != undefined) ? $state.params.evaluasiKinerjaVendor : {};
		vm.submitted = false;
		
		
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
			pageNo : 1,
			pageSize : 5,
			dataList : {},
			jmlData : {},
			poNumber : '',
			prnumber : '',
			search : '',
			id : vm.evaluasiKinerjaVendor.id,
			periode : 0

		};
			
			vm.sortPeriode = [ {
				name : 'Per 1 Bulan',
				value : '1'
			}, {
				name : 'Per 3 Bulan',
				value : '2'
			},{
				name : 'Per 6 Bulan',
				value : '3'
			}, {
				name : 'Per 12 Bulan',
				value : '4'
			}];
			
			
		$scope.evaluasiKinerjaVendorList = function() {
			var data = vm.param;
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-list-detail-evaluasi', vm.param)
					.then(
							function success(data) {
								if (data.data.dataList != undefined
										&& data.data.dataList.length > 0) {
									
									/*vm.param = data.config.data;*/
									vm.dataList = data.data.dataList;
									vm.totalItems = data.data.jmlData;
									vm.param.jmlData = data.data.jmlData;
									vm.param.dataList = data.data.dataList;
									
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

		$scope.evaluasiKinerjaVendorList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-detail-evaluasi-kinerja-vendor';
			RequestService.doPostDownloadExcel(targetURL, vm.param);
		}

		$scope.setPage = function() {
			$scope.evaluasiKinerjaVendorList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.evaluasiKinerjaVendorList();
		}
		
		
		$scope.back = function (){
			$state.go('app.promise.procurement-laporan-evaluasi-kinerja-vendor-index', {});
		}

	}

	LaporanDetailEvaluasiKinerjaVendorController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();