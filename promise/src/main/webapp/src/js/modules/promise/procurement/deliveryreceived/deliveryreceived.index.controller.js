
(function() {
	'use strict';

	angular.module('naut').controller('DeliveryReceivedController',
			DeliveryReceivedController);

	function DeliveryReceivedController($scope, $http, $rootScope, $resource,
			$location, RequestService, $state, $stateParams, $filter, $modal,
			ModalService, $window, $log) {

		var vm = this;

		vm.param = {
			search : '',
			pageNo : 1,
			pageSize : 5,
			jmlData : {},
			dataList : {},
			orderKeyword : '',
			vendorName : ''

		};
		vm.sortDrList = [ {
			name : 'Tanggal PO Terlama',
			value : 'po.updated ASC'
		}, {
			name : 'Tanggal PO Terbaru',
			value : 'po.updated DESC'
		}, {
			name : 'Nomor PO A-Z',
			value : 'po.poNumber ASC'
		}, {
			name : 'Nomor PO Z-A',
			value : 'po.poNumber DESC'
		}, {
			name : 'Nama Penyedia A-Z',
			value : 'po.vendorName ASC'
		}, {
			name : 'Nama Penyedia Z-A',
			value : 'po.vendorName DESC'
		}, {
			name : 'Nama Pemesan A-Z',
			value : 'po.purchaseRequest.organisasi.nama ASC'
		}, {
			name : 'Nama Pemesan Z-A',
			value : 'po.purchaseRequest.organisasi.nama DESC'
		}, {
			name : 'Nomor DR A-Z',
			value : 'dr.deliveryReceiptNum ASC'
		}, {
			name : 'Nomor DR Z-A',
			value : 'dr.deliveryReceiptNum DESC'
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
	  
	  $scope.printDr = function(dr){
		  RequestService.doPrint({reportFileName:'PrintDeliveryReceipt', drId:dr.drId});
		}

		$scope.DeliveryReceivedList = function(id) {
			vm.loading = true;

			RequestService
					.doPOST(
							'/procurement/deliveryreceived/DeliveryReceivedServices/get-list',
							vm.param).then(
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

		$scope.DeliveryReceivedList();
		
		$scope.resend = function(dr) {
			ModalService
			.showModalConfirmation(
					'Apakah anda yakin ingin resend DR ini?')
			.then(function(result) {
				ModalService.showModalInformationBlock();
				$scope.resendToEbs(dr);
			});
		}
		
		$scope.resendToEbs = function(dr) {
			var button = document.getElementById("Btn");
			button.disabled = true;
			var post = {
					pk : dr.drId,
			};
			RequestService.doPOST(
					'/catalog/interfacing/deliveryReceiptInterfacingService/send-data-dr', post).then(
					function successCallback(data) {
						if(angular.equals(data.data.status,"ERROR")){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Response error from EBS : "+data.data.statusText +" Please click Resend button");
							$state.go('app.promise.procurement-deliveryreceived');
						}else{
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Success send data DR");
							$scope.DeliveryReceivedList();
							$state.go('app.promise.procurement-deliveryreceived');	
						}
						button.disabled = false;
		             }, function errorCallback(response) {
		            	 ModalService.closeModalInformation();
		            	 RequestService.informInternalError();
					});
		}

		
		$scope.getSearchAndSort = function(data) {
			$scope.DeliveryReceivedList();
		}

		$scope.setPage = function() {
			$scope.DeliveryReceivedList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.DeliveryReceivedList();
		}
		
		 $scope.received = function (deliveryReceived) {
			 $state.go('app.promise.procurement-deliveryReceived-detail', {
				 deliveryReceived: deliveryReceived,
        		 toDo:"received"
             });
        }
		 $scope.process = function (deliveryReceived) {
			 $state.go('app.promise.procurement-deliveryReceived-detail', {
				 deliveryReceived: deliveryReceived,
        		 toDo:"process"
             });
        }
	}

	DeliveryReceivedController.$inject = [ '$scope', '$http', '$rootScope',
		'$resource', '$location', 'RequestService', '$state',
		'$stateParams', '$filter', '$modal', 'ModalService', '$window',
		'$log' ];

})();