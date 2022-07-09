/**
 * ========================================================= Module:
 * PurchaseOrderController.js Author: Lia, Mamat
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('PurchaseOrderController',
			PurchaseOrderController);

	function PurchaseOrderController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $modal,
			ModalService, $window, $log, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile) {

		var vm = this;
		vm.tampObj=[];
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
			jmlData : {},
			dataList : {},
			orderKeyword : '',
			vendorName : '',
			startDate : '',
			endDate : ''

		};
		vm.sortDRList = [ {
			name : 'PO Number',
			value : 'poNumber'
		}, {
			name : 'Purchased by',
			value : 'department'
		}, {
			name : 'Status',
			value : 'status'
		}, {
			name : 'PO Date',
			value : 'purchaseOrderDate'
		} ];
		
		/*$scope.vendorList = function () {
		       
            RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/get-vendor-list').then(function success(data) {
	        	vm.vendorList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
	  $scope.vendorList();*/

		$scope.PurchaseOrderList = function(id) {
			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}		
			vm.loading = true;

			$rootScope.grandTotal = 0;
			$scope.subTotal = 0;
			$scope.ppn = 0;
			$scope.ongkir = 0;
			RequestService.doPOST('/procurement/purchaseorder/PurchaseOrderServices/get-list',vm.param).then(function success(data) {
				if (data.data.dataList != undefined && data.data.dataList.length > 0) {
					vm.dataList = data.data.dataList;
					vm.totalItems = data.data.jmlData;
					/*KAI - 20201130 - #20665*/
					angular.forEach(vm.dataList, function(value, key) {
						$scope.ongkir += value.purchaseRequest.totalHargaOngkir;
						$scope.total = value.totalCost + value.purchaseRequest.totalHargaOngkir;
						$scope.ppn = $scope.total * 10 / 100;
						vm.dataList[key].subTotal = $scope.total + $scope.ppn;
					})
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

		//$scope.PurchaseOrderList();
		
		$scope.vendorList = function () {
		       
			RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/get-vendor-list-for-purchase-order-manager').then(function success(data) {
	        	vm.vendorList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
	  $scope.vendorList();
		
		$scope.sync = function(purchaseOrder) {
			ModalService
			.showModalConfirmation(
					'Apakah anda yakin ingin resend PO ini?')
			.then(function(result) {
				ModalService.showModalInformationBlock();
				$scope.sync1(purchaseOrder);
			});
		}
		
		$scope.sync1 = function(purchaseOrder) {
			var button = document.getElementById("Btn");
			button.disabled = true;
			var targetURL = '/catalog/interfacing/purchaseOrderInterfacingService/send-data-po/'+purchaseOrder.id;
			RequestService.doGET(targetURL).then(
					function success(data){
					//KAI - 20201125 - SYNC
						if(data.statusText != "SUCCESS"){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Response error from SAP : "+data.statusText +" Please click Resend button");
							$state.go('app.promise.procurement-purchaseorder');
						}else{
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Success send data PO");
							//$scope.PurchaseOrderList();
							$state.go('app.promise.procurement-purchaseorder');	
						}
						button.disabled = false;
					},
					function error(response) {
			        	RequestService.informInternalError();
					});
		}
		
		$scope.cetakPO = function(purchaseOrder){
			RequestService.doPrint({reportFileName:'CetakPurchaseOrder', purchaseOrderId:purchaseOrder.id});
		}
		
		 $scope.detail = function (bookingOrder) {
			 $state.go('app.promise.procurement-purchaseorder-add', {
				 bookingOrder: bookingOrder,
        		 toDo:"detail"
             });
        }
		 
		$scope.initTable = function(postData) {
			vm.dtOptions = DTOptionsBuilder.newOptions()
	    	.withOption('ajax', {
	    		url: $rootScope.backendAddress + '/procurement/purchaseorder/PurchaseOrderServices/getListPagination',
	    		type: 'POST',
	    		data: postData,
	    		 /*PROMISE_SECURITY*/
				"beforeSend": function(xhr) {
			     	xhr.setRequestHeader("Authorization", $rootScope.userToken);
			     },
			     error: function (xhr, error, thrown) {
			 	 	var errorCode = xhr.responseText.substr(xhr.responseText.indexOf("PRMS-ERR"), 12);
	             	$location.path('/page/errorPage/' + errorCode);
	       	     }
			     /*END PROMISE_SECURITY*/
	        })
	        .withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	        	$compile(nRow)($scope);
	        	var start = this.fnSettings()._iDisplayStart;
	        	$("td:first", nRow).html(start + iDisplayIndex + 1);
	        })
	        .withOption('headerCallback', function(thead, data, start, end, display) {
	            $compile(angular.element(thead).contents())($scope);
	        }) 
	        .withDataProp('data')
	        .withOption('processing', true)
	        .withOption('serverSide', true)
	        .withOption('scrollX', '100%')
	        .withPaginationType('full_numbers')
	        .withDisplayLength(10)
	
			vm.dtColumns = [
					DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable()
					.renderWith(function(data, type, full, meta) {
						return meta.row + 1;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.boNumber"></span>')
					.renderWith(function(data) {
						return data.purchaseRequest.boNumber;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.prNumber"></span>')
					.renderWith(function(data) {
						return data.purchaseRequest.prnumber;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.poNumber"></span>')
					.renderWith(function(data, type, full, meta) {
						return data.poNumber;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.posDate"></span>')
					.renderWith(function(data, type, full, meta) {
						return $filter('date')(data.purchaseOrderDate, 'dd/MM/yyyy');
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.department"></span>')
					.renderWith(function(data, type, full, meta) {
						return data.department;
					}),
					/*DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.vendorName"></span>')
					.renderWith(function(data) {
						var namaVendor = "-";
						if(angular.equals($rootScope.userRole.nama, 'PENGGUNADVP') || angular.equals($rootScope.userRole.nama, 'PENGGUNASPV') || angular.equals($rootScope.userRole.nama, 'PENGGUNASTAFF') ||
								angular.equals($rootScope.userRole.nama, 'DIREKTURPENGGUNA') || angular.equals($rootScope.userRole.nama, 'PENYELENGGARAPEMILIHAN')){
							namaVendor = data.vendorName;
						}
						return namaVendor;
					}),*/
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.totalCost"></span>')
					.renderWith(function(data) {
						
						var total = data.totalCost + data.purchaseRequest.totalHargaOngkir;
						var ppn = total * 10 / 100;
						data.subTotal = total + ppn;
						return $filter('currency')(data.subTotal, ' ', 2);
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.purchaseOrder.table.status"></span>')
					.renderWith(function(data) {
						var status = "-";
						if(data.purchaseRequest.status == 10){
							status ="PO Received";
						}else {
							status = data.status;
						}
						return status;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="template.tabel.aksi"></span>')
					.renderWith(function(data) {
						vm.tampObj[data.id] = data;
						var strAksi = '<center><button type="button" ng-class="bg- + app.theme.name"'+
											'class="btn btn-primary" ng-click="detail(vm.tampObj['+data.id+'])" title="Detail PO"><em class="fa fa-search"></em>'+
										'</button>&nbsp;&nbsp;';
						if((!angular.equals($rootScope.userRole.code, "VE")) && angular.equals(data.status, "Failed Sending PO to SAP") && angular.equals(data.poNumber, "-") && data.isAvailable){
							strAksi +='<button type="button" id="Btn" class="btn btn-warning" ng-click="sync(vm.tampObj['+data.id+'])" title="Re-Send PO">'+
											'<em class="fa fa-send"></em>'+
										'</button>&nbsp;&nbsp;';
						}
						strAksi +='<button type="button" id="cetak" data-toggle="tooltip" data-placement="bottom" title="Cetak" class="btn btn-primary" ng-click="cetakPO(vm.tampObj['+data.id+'])">'+
										'<em class="fa fa-print"></em>'+
									'</button>&nbsp;&nbsp;</center>';
						return strAksi;
					})
				];
	
		}
		
		$scope.getListPaging = function() {
			
			vm.loading = true;
			var postData = {
					refresh: '',
					startDate:  vm.param.startDate==undefined?"":$filter('date')(vm.param.startDate, "yyyy-MM-dd"),
					endDate: vm.param.endDate==undefined?"":$filter('date')(vm.param.endDate, "yyyy-MM-dd"),
					vendorName: vm.param.vendorName,
					orderKeyword: vm.param.orderKeyword
			};
			
			$scope.initTable(postData);
			vm.loading = false;
		}
		$scope.getListPaging();
	}
	
	PurchaseOrderController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$modal', 'ModalService', '$window',
		'$log', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile' ];

})();