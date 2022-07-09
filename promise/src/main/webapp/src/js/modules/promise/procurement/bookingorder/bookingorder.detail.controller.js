(function() {
	'use strict';

	angular.module('naut').controller('bookingOrderDtlCtrl',
			bookingOrderDtlCtrl);

	function bookingOrderDtlCtrl($rootScope, $scope, RequestService, $state,
			$stateParams, $location, $modal, ModalService, $http,
			$anchorScroll, ROLE_USER) {
		
		var vm = this;
		$scope.bookingOrder = ($stateParams.bookingOrder != undefined) ? $stateParams.bookingOrder
				: {};
		vm.role = true;
		if ($scope.userRole.nama == 'VENDOR') {
			vm.role = false;
			$location.path('/appvendor/promise/dashboard');
		}
		if ($scope.userRole.nama == 'PENYEDIA') {
			vm.role = false;
		}
		vm.isGenerated = false;
		vm.tempBulkPriceDiskon = [];
		vm.tempCatalogFeeAsuransi = [];
		vm.tempCatalogFeeOngkosKirim = [];
		vm.slaDeliveryTime=null;
		vm.tempSlaDeliveryTime=null;
		vm.tambah = 0;
		vm.grandTotal = 0;
		vm.asuransi = [];
		vm.total = 0;
		vm.isNew=false;
		vm.maxSlaDeliveryTime=null;
		vm.note = '';
		vm.hidenLogUser = false;
		vm.hidenApproveReject = false;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.bookingOrder = $stateParams.bookingOrder;
		vm.quantity=0;
		vm.totalOngkir=0;
		vm.totalOngkirAll=0;
		vm.totalAsuransi=0;
		vm.totalBarang=0;
		
		vm.shipping = [];
		
		$scope.errorNoteMessage = '';
		if ($stateParams.catalogList != undefined) {
			$scope.catalogList = $stateParams.catalogList;
			angular.forEach($scope.catalogList, function(value, index) {
				// if(value.catalogLocationList != undefined) {
				// if(value.catalogLocationList[0].supplyAbility != undefined) {
				value.qtyCartItem = 1;
				value.isQtyCart0 = false;
				if (value.harga_eproc == null || value.harga_eproc == 0) {
					$scope.grandTotal = $scope.grandTotal
							+ (value.qtyCartItem * value.harga);
					// $scope.useHargaEproc = false;

				} else {
					$scope.grandTotal = $scope.grandTotal
							+ (value.qtyCartItem * value.harga_eproc);
					// $scope.useHargaEproc = true;
				}
				$scope.grandTotalCurrency = value.mataUang.kode;
				// } else {
				// ModalService.showModalInformation('Item -
				// '+value.kodeProduk+' melebihi Supply Ability');
				// value.qtyCartItem = 1;
				// }
				// } else {
				// ModalService.showModalInformation('Item -
				// '+value.kodeProduk+' Tidak memiliki Stock');
				// $state.go('app.promise.procurement-ecatalog2');
				// }
			});
		}

		$scope.getApprovalProcessVendorList = function() {
			vm.loading = false;
			RequestService.doPOSTJSON(
					'/procurement/master/approvalProcessVendor/get-list', vm.bookingOrder.id).then(
					function success(data) {
						vm.approvalProcessVendorList = data;
						vm.loading = false;

						vm.loading = false;
					}, function error(response) {
						RequestService.informInternalError();
						vm.loading = false;
					});
		}
		$scope.getApprovalProcessVendorList();

		$scope.getApprovalProcessVendorStatus1List = function() {
			vm.loading = false;
			RequestService.doPOSTJSON(
					'/procurement/master/approvalProcessVendor/getStatus1List', vm.bookingOrder.id)
					.then(function success(data) {
						vm.approvalProcessVendorStatus1List = data;
						var checkListStatus = [];
						checkListStatus = vm.approvalProcessVendorStatus1List
						if(checkListStatus.length > 0){
							 vm.hidenApproveReject = true;
						 }
						vm.loading = false;
					}, function error(response) {
						RequestService.informInternalError();
						vm.loading = false;
					});
		}
		$scope.getApprovalProcessVendorStatus1List();

		$scope.getApprovalInternalLogUserList = function() {
			vm.loading = false;
			RequestService
					.doPOSTJSON('/procurement/approvalProcessServices/getLogUserList', vm.bookingOrder.id)
					.then(function success(data) {
						vm.approvalInternalLogUserList = data;
						var checkListUser = [];
						checkListUser = vm.approvalInternalLogUserList
						if(checkListUser.length > 0){
							 vm.hidenLogUser = true;
						 }
						vm.loading = false;
					}, function error(response) {
						RequestService.informInternalError();
						vm.loading = false;
					});
		}
		$scope.getApprovalInternalLogUserList();

		if (vm.approvalProcessVendorStatus1List == null) {
			vm.hideApproveAndReject = false;
		}

		$scope.btnSimpan = function(status) {
			$scope.errorNoteMessage = "";

			var checkNote = vm.note;
			var isNote = true;
			if (checkNote.length >= 255) {
				isNote = false;
				$scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
			} else if (checkNote.length == 0 || checkNote.length == ''){
				$scope.errorNoteMessage = "Note tidak boleh kosong";
				isNote = false;
			} else {
				if (status == 2) {
					ModalService
							.showModalConfirmation(
									'Apakah anda yakin ingin membatalkan Booking Order ini?')
							.then(function(result) {
								ModalService.showModalInformationBlock();
								$scope.simpan(status);
							});
				} else if (status == 3) {
					ModalService
							.showModalConfirmation(
									'Apakah anda yakin ingin menyetujui Booking Order ini?')
							.then(function(result) {
								ModalService.showModalInformationBlock();
								$scope.simpan(status);
							});
				}

			}
		}
		$scope.simpan = function(status) {
			if (vm.note === null || vm.note === '') {
				vm.note = '-';
			}
			var post = {
				status : status,
				note : vm.note,
				bookingOrderId: vm.bookingOrder.id,
				slaDeliveryTime : vm.slaDeliveryTime
			};
			RequestService.doPOST(
					'/procurement/master/approvalProcessVendor/doProcess', post).then(
					function successCallback(data) {
						if(angular.equals(data.data.status,"ERROR")){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Data Gagal di Simpan, Setting terlebih dahulu Approval Tahapan di organisasi "+data.data.statusText);
						} else {
								ModalService.closeModalInformation();
								$state.go('app.promise.procurement-booking-order-index');
								RequestService.informSaveSuccess();
								vm.hidenApproveReject = false;
							}
						
		             }, function errorCallback(response) {
		                 ModalService.closeModalInformation();
		                 ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
		}

		$scope.detail = function() {
			
			RequestService.doPOSTJSON('/procurement/purchaseRequestItemServices/getListByPurchaseRequestId', $scope.bookingOrder.id).then(function successCallback(data) {
				vm.prItemList = data;
				
				angular.forEach(vm.prItemList, function(prItem, index) {
					RequestService.doPOSTJSON('/procurment/catalog/catalog-bulk-price/get-list-by-catalog-id', prItem.catalog.id).then(function successCallback(data2) {
						angular.forEach(data2,function(value2, index2) {
							if (value2 != null) {
								if (prItem.quantity >= value2.minQuantity && prItem.quantity <= value2.maxQuantity || prItem.quantity > value2.maxQuantity) {
									vm.tempBulkPriceDiskon[index] = value2.diskon;
								}
							}
						});

						if (vm.tempBulkPriceDiskon[index] == null) {
							vm.tempBulkPriceDiskon[index] = 0;
						}
						vm.total += prItem.total;
						vm.grandTotal += prItem.total;
					});
					vm.asuransi[index] = false;
					if (prItem.asuransi > 0) {
						vm.asuransi[index] = true;
					}
				});
				angular.forEach(vm.prItemList,function(prItem, index) {
					vm.totalOngkirAll +=prItem.ongkosKirim;
					RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/get-list-catalg-fee-by-catalog-id',prItem.catalog.id).then(function successCallback(data2) {
						angular.forEach(data2, function(fee, index2) {
						vm.tempCatalogFeeAsuransi[index] = 0;
						
						vm.tempCatalogFeeOngkosKirim[index] = 0;
						if (data2 == undefined || data2 == null) {
							vm.tempCatalogFeeAsuransi[index] = 0;
							vm.tempCatalogFeeOngkosKirim[index] = 0;
						} else {
							
							vm.totalOngkir = prItem.ongkosKirim;
							if ((fee.organisasi != null || fee.organisasi != undefined) && (prItem.purchaserequest.rootParent != null)) {
								if (fee.organisasi != null) {
									if (fee.organisasi.id == prItem.purchaserequest.rootParent) {
										if (prItem.asuransi > 0){
											vm.tempCatalogFeeAsuransi[index] = fee.asuransi;
										}
										if (prItem.quantity >= fee.quantityBatch) {
											vm.quantity= prItem.quantity/fee.quantityBatch;
											vm.totalBarang = Math.ceil(vm.quantity);
											vm.tempCatalogFeeOngkosKirim[index] = fee.ongkosKirim * vm.totalBarang;
										} else {
											vm.tempCatalogFeeOngkosKirim[index] = fee.ongkosKirim;
										}
									
										//vm.totalOngkir =vm.tempCatalogFeeOngkosKirim[index];
										vm.totalAsuransi+=((vm.tempCatalogFeeAsuransi[index] / 100) * prItem.total);
									}
								}
							}
						}
					});
					RequestService.doPOSTJSON('/procurement/purchaseRequestServices/get-purchase-request-by-id', $scope.bookingOrder.id).then(function successCallback(data) {
						if(data.slaDeliveryTime != null){
							vm.tempSlaDeliveryTime= data.slaDeliveryTime;
							vm.slaDeliveryTime=data.slaDeliveryTime;							
						}else{
							vm.slaDeliveryTime = 0;
						}
						if(data.status == 11){
							vm.is =false;
						}else{
							vm.isNew =true;
						}
					})
					RequestService.doPOSTJSON('/procurement/purchaseRequestServices/slaDeliveryTimeListByPurchaseRequest', $scope.bookingOrder.id).then(function successCallback(data) {
						if(data.length > 0){
							vm.slaDeliveryTimeList=data;							
						}
					})
					if (vm.tempCatalogFeeOngkosKirim[index] == undefined
							|| vm.tempCatalogFeeOngkosKirim[index] == null) {
						vm.tempCatalogFeeOngkosKirim[index] = 0;
					}
					vm.grandTotal += vm.totalOngkir;
				});
					
				
					
					
			});
				
				$scope.shipping();	
				
			}, function errorCallback(response) {

			});

		}
		
		// KAI - 20210204 - #20867
		$scope.shipping = function() {
			angular.forEach(vm.prItemList,function(prItem, index) {
				
            	if (vm.shipping.length == 0){
            		vm.shipping.push(prItem.addressBook);
            	} else {
            		var isSame = false;
            		angular.forEach(vm.shipping, function (shipping, indexShipping) {
            			if (shipping.id == prItem.addressBook.id ){
            				isSame = true;
            			}
            		})
            		
            		if(!isSame){
            			vm.shipping.push(prItem.addressBook);
            		}	
            	}
				
			});
			
			vm.prItemListNew = [];
            vm.totalHargaShipTo = [];
            vm.totalOngkirShipTo = [];
            vm.subTotalShipTo = [];
            angular.forEach(vm.shipping, function (shipping, index) {
            	var prItemTemp = [];
            	var totalHargaShipTo = 0;
            	var totalOngkirShipTo = 0;
            	var subTotalShipTo = 0;
            	 angular.forEach(vm.prItemList, function (value, index2) {
            		 if (shipping.id == value.addressBook.id ){
            			 prItemTemp.push(value);
            			 totalHargaShipTo += value.total;
            			 totalOngkirShipTo += value.ongkosKirim;
            		 } 
            	 })
            	 vm.prItemListNew.push(prItemTemp);
            	 vm.totalHargaShipTo.push(totalHargaShipTo);
            	 vm.totalOngkirShipTo.push(totalOngkirShipTo);
            	 vm.subTotalShipTo.push(totalHargaShipTo+totalOngkirShipTo);
            });
            vm.priceIncludePPN = [];
    		angular.forEach(vm.prItemListNew, function(prItem,index){
    			var tamPriceIncludePPN =[];
    			angular.forEach(prItem, function(data, index2){
    				tamPriceIncludePPN.push(data.catalog.harga);
    				if(data.catalog.catalogKontrak.isPpn == 1){
    					tamPriceIncludePPN[index2] = data.catalog.harga * (10/11);
    				}        				
    			});
    			vm.priceIncludePPN.push(tamPriceIncludePPN);
    		});
			
		}
		
		$scope.detail();
		
//		$scope.checkValue=function(slaDeliveryTime){
//			if(slaDeliveryTime > vm.tempSlaDeliveryTime){
//				vm.slaDeliveryTime=vm.tempSlaDeliveryTime;
//			}
//		}
		$scope.back = function() {
			$state.go('app.promise.procurement-booking-order-index', {});
		}

	}

	bookingOrderDtlCtrl.$inject = [ '$rootScope', '$scope', 'RequestService',
			'$state', '$stateParams', '$location', '$modal', 'ModalService',
			'$http', '$anchorScroll', 'ROLE_USER' ];

})();