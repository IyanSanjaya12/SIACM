/**
 * ========================================================= Module:
 * LaporanBudgetdDanRealisasiController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanBudgetDanRealisasiController',
			LaporanBudgetDanRealisasiController);

	function LaporanBudgetDanRealisasiController(RequestService, ModalService, $scope,
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
			type : 0,
			sort : '',
			startDate : '',
			endDate : ''

		};

		vm.sortHeaderList = [ {
			name : 'PR',
			value : 1
		}, {
			name : 'PO',
			value : 2
		} ];
		
		vm.sortAscDesc = [ {
			name : 'ASC',
			value : 'ASC'
		}, {
			name : 'DESC',
			value : 'DESC'
		} ];

		
		$scope.budgetDanRealisasiList = function() {

			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}		
			vm.loading = true;
			RequestService.doPOST('/procurement/laporan/get-budget-dan-realisasi', vm.param)
					.then(
							function success(data) {
								//console.log(data);
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

		$scope.budgetDanRealisasiList();

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-budget-dan-realisasi';
			RequestService.doPostDownloadExcel(targetURL, vm.param);
		}

		$scope.setPage = function() {
			$scope.budgetDanRealisasiList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.budgetDanRealisasiList();
		}

	}

	LaporanBudgetDanRealisasiController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
