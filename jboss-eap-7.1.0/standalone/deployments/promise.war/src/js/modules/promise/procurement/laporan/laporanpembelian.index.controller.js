/**
 * ========================================================= Module:
 * LaporanPembelian
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('LaporanPembelian',
			LaporanPembelian);

	function LaporanPembelian(RequestService, ModalService, $scope,
			$http, $rootScope, $resource, $location, $modal, $state, $filter,
			$stateParams, $log, $compile) {
		var vm = this;
		vm.sortHeaderList = [ {
			name : 'No.PO A-Z',
			value : 'deliveryReceived.purchaseOrder.poNumber ASC'
		},{
			name : 'No.PO Z-A',
			value : 'deliveryReceived.purchaseOrder.poNumber DESC'
		},{
			name : 'Tanggal Receipt A-Z',
			value : 'deliveryReceived.dateReceived ASC'
		}, {
			name : 'Tanggal Receipt Z-A',
			value : 'deliveryReceived.dateReceived DESC'
		}];
		
		vm.sortAscDesc = [ {
			name : 'ASC',
			value : 'ASC'
		}, {
			name : 'DESC',
			value : 'DESC'
		} ];

		vm.status = [ {
			name : 'Not Received',
			value : '0'
		}, {
			name : 'Received',
			value : '1'
		} ];
		vm.param = {
				search : '',
				orderKeyword: '',
				sort : '',
				startDate : '',
				endDate : '',
				status: '',
				pageNo : 1,
				pageSize : 5,
			};
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
		$scope.pembelianList = function() {
			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.status ){
				delete data['status'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}	
		
			RequestService.doPOST('/procurement/laporan/get-laporan-proses-pembelian', vm.param)
					.then(
							function success(data) {
								if (data.data.dataList != undefined
										&& data.data.dataList.length > 0) {
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
		$scope.pembelianList();	
		
		$scope.getDetil = function(po, index) {
			var i = 0;
			angular.forEach(vm.dataList, function(value, key) {
				vm.dataList[i].showdetail = false;
				i++;
			});
			
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/laporan/getPoItemJoinCatalogKontrak/' + po).success(function(data, status, headers, config) {
				vm.poItem = data;
				var rowObject = document.getElementById('info_' + po);
				if ((rowObject.style.display == 'none')) {
					var cell = document.getElementById('cell_info_' + po);
					var content = '<table class="table-responsive"><tr>'; 
					content += '       <table class="table table-striped table-bordered table-hover">';
					content += '          <th scope="col"><strong>Nomor Kontrak</strong></th>';
					content += '          <th scope="col"><strong>Nama Pekerjaan</strong></th>';
					content += '          <th scope="col"><strong>Kode item (EBS)</strong></th>';
					content += '          <th scope="col"><strong>Nama item (Catalog)</strong></th>';
					content += '          <th scope="col"><strong>Qty</strong></th>';
					content += '     </tr>';

					var cellAction = document.getElementById('cell_action_' + 1);

					angular.forEach(vm.poItem, function(value, key) {
						content += '<tr>';
						content += '          <td>' + value[1].catalogKontrak.nomorKontrak + '</td>';
						content += '          <td>' + value[1].catalogKontrak.namaPekerjaan + '</td>';
						content += '          <td>' + value[0].item.kode + '</td>';
						content += '          <td>' + value[0].purchaseRequestItem.catalog.catalogContractDetail.itemDesc + '</td>';
						content += '          <td>' + value[0].purchaseRequestItem.quantity + '</td>';
						
						content += '</tr>';

					});
					content += '</table>';
					content += '</div>';
					cell.innerHTML = content;

					$compile(cell)($scope);

					rowObject.style.display = 'table-row';
					$scope.loading = false;
					vm.dataList[index].showdetail = true;
					return true;
				} else {
					rowObject.style.display = 'none';
					$scope.loading = false;
					vm.dataList[index].showdetail = false;
					return false;
				}
			}).error(function(data, status, headers, config) {
				$scope.loading = false;
			});
		};

		$scope.setPage = function() {
			$scope.pembelianList();
		}
		

		$scope.downLoad = function() {
			var targetURL = '/procurement/laporan/download-excel-proses-pembelian';
			RequestService.doPostDownloadExcel(targetURL, vm.param);
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.pembelianList();
		}

	}

	LaporanPembelian.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter', '$compile' ];

})();
