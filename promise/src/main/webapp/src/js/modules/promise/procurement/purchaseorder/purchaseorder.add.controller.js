/**
 * ========================================================= Module:
 * PurchaseOrderAddController.js Author: Mamat, <n>
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('PurchaseOrderAddController',
			PurchaseOrderAddController);

	function PurchaseOrderAddController(RequestService, ModalService, $scope, $http,
			$rootScope, $resource, $location, $filter, toaster, $state, $stateParams, $window) {
		var vm = this;
		vm.po = {};
		vm.purchaseOrder = {};
		$scope.termin = {};
		vm.toDo = $stateParams.toDo;
		// BIlling TO
		vm.po.billTo = {};
		vm.po.shipToList = [];
		$scope.bookingOrder = ($stateParams.bookingOrder != undefined) ? $stateParams.bookingOrder : {};
		vm.prItemListPo = [];
		if(vm.toDo == "detail"){
			vm.poId = $scope.bookingOrder.id;
			$scope.bookingOrder.id = ($scope.bookingOrder.purchaseRequest.id);
			$scope.bookingOrder.prnumber = $scope.bookingOrder.purchaseRequest.prnumber;
			vm.po.billTo.podate = $scope.bookingOrder.purchaseOrderDate ;
			vm.po.billTo.deliveryTime = $scope.bookingOrder.deliveryTime ;		
			vm.po.ponumber = $scope.bookingOrder.poNumber;
		}
		vm.deliveryTimeList=[];
		$rootScope.grandTotal = 0;
		$scope.subTotal = 0;
		$scope.ppn = 0;
		$scope.ongkir = 0;
		
		$scope.taxCodeList = [{id:1, code:'V0'}, {id:2, code:'V1'}];
		$scope.errormessagetaxcode = [];
		
		$scope.createPO = function (){
			var poItemList = [];
			vm.subTotalList = [];
			vm.ppnList = [];
			RequestService.doGET('/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/'+ $scope.bookingOrder.id)
			.then(function successCallback(data) {	
				vm.prItemListPo = data;
//				$scope.estimated = $filter('date')(vm.prItemListPo[0].estimatedDeliveryTime, "dd-MM-yyyy");
				angular.forEach(vm.prItemListPo, function(value, key) {
					$scope.ongkir += value.ongkosKirim;
					$scope.ppn = $scope.subTotal * 10 / 100;
					$rootScope.grandTotal = $scope.subTotal + $scope.ppn;
					vm.subTotalList.push( (value.quantity * value.price) + value.ongkosKirim);
					var tampTotal = (value.quantity * value.price) + value.ongkosKirim;
					vm.ppnList.push(tampTotal * 0.1)
					$scope.subTotal = $scope.subTotal + (value.quantity * value.price) + value.ongkosKirim;
				});
				
			}, function errorCallback(response) {				 
				
			});
		}
		
		$scope.createPO();

		// Datepicker
	
		vm.disabled = function(date, mode) {
			return false; // ( mode === 'day' && ( date.getDay() === 0 ||
			// date.getDay() === 6 ) );
		};
		vm.toggleMin = function() {
			vm.minDate = vm.minDate ? null : new Date();
		};
		vm.toggleMin();
		vm.dateOptions = {
			formatYear : 'yy',
			startingDay : 1
		};

		vm.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate',
				'dd-MM-yyyy', 'yyyy-MM-dd' ];
		vm.format = vm.formats[4];

		if(vm.toDo == "add"){
		vm.poDateOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.poDateOpened = true;
		};
		}
		$scope.getListAddressBook = function(prId){
			vm.addresBookList = [];
			RequestService.doGET('/procurement/master/AddressBookServices/getAddressBookByPurchaseRequestId/'+ prId +'/'+vm.toDo).then(function success(data) {
				vm.addresBookList = data;
				if(angular.equals(vm.toDo,"detail") ){
					angular.forEach(vm.addresBookList, function(value){
						vm.deliveryTimeList.push(value.deliveryTime);					
					});
				}
			});
		}
		$scope.getListAddressBook($scope.bookingOrder.id);
		if(vm.toDo == "add"){
		vm.poDateOpenDynamic = function(indexInput, $event) {
			$event.preventDefault();
			$event.stopPropagation();

			vm.addresBookList.forEach(function(shipTo, index, array) {
				if (index == indexInput) {
					shipTo.poDateOpened = true;
				} else {
					shipTo.poDateOpened = false;
				}
			});
		};
		}
		
		$scope.btnAddShipping = function() {
			var poItemList = [];
			angular.forEach(vm.prItemListPo, function(value, key) {
				var poItem = {
					pritemid : value.id,
					name : value.item.deskripsi,
					quantity : value.quantity,
					shipquantity : value.shipquantity,
					price : value.price,
					costcenter : value.costcenter,
					unit : value.unit,
					matauang : value.mataUang.kode,
					ongkosKirim : value.ongkosKirim

				};
				poItemList.push(poItem);
			});

			var company = {
				itemPOList : poItemList,

				companyAddressList : []
			};

			vm.po.shipToList[0] = company;
			
		};

	
		
		$scope.nextDataShipto = function() {
			var valid = true;			
			if (typeof vm.po.billTo.podate === 'undefined') {
				valid = false;
				$scope.errormessagePoDate = 'Field tidak boleh kosong!';
			}
//			var estimasi = new Date(vm.po.billTo.podate);
//			estimasi.setDate(estimasi.getDate() + vm.prItemListPo[0].purchaserequest.slaDeliveryTime); 
//			$scope.estimated = $filter('date')(estimasi, "dd-MM-yyyy");
			vm.estimatedList = [];
			angular.forEach(vm.prItemListPo, function(data){
				var estimasi = new Date(vm.po.billTo.podate);
				estimasi.setDate(estimasi.getDate() + data.slaDeliveryTime);
				vm.estimatedList.push($filter('date')(estimasi, "dd-MM-yyyy"))
			});
			$scope.btnAddShipping();
			return valid;
		}

		$scope.delValidation = function($event) {
			$scope.poForm[$event.target.name].$setValidity($event.target.name,
					true);
		}
		
		 $scope.validateForm = function () {
			 var isValid = true;
			 vm.lengthprItem = 0;
			 vm.lengthTaxCode = 0;
			 angular.forEach(vm.addresBookList, function(value, index) {
				 vm.lengthprItem += value.purchaseRequestItemList.length;
			 });
			 angular.forEach(vm.taxCode, function(value, index) {
				 angular.forEach(value, function(value2, index2) {
					 vm.lengthTaxCode +=1;					 
				 });
			 });
			 if (typeof vm.taxCode == 'undefined' || vm.taxCode == '') {
	    		isValid = false;
    		}else{
    			if(vm.lengthprItem == vm.lengthTaxCode){
    				angular.forEach(vm.addresBookList, function(value1, index1) {
    					angular.forEach(value1.purchaseRequestItemList, function(value2, index2) {
    						if (vm.taxCode[index1+1][index2] == undefined || vm.taxCode[index1+1][index2] == '' || vm.taxCode[index1+1][index2] == null ){
    							isValid = false;
    						} 	    					
    					});
    				});	    			    				
    			}else{
    				isValid = false;
    			}
    		}
			if(!isValid){
				toaster.pop('error','Kesalahan','Mohon Mengisi Tax Code !')
			}
			 return isValid;
		 }
		

		if(vm.toDo == "add"){
		$scope.btnSimpan = function(formValid) {
			vm.loading = true;
			vm.submitted = true;
			if(vm.deliveryTimeList.length < vm.addresBookList.length){
				toaster.pop('error','Kesalahan','Mohon isi Field Waktu Pengiriman yang Diusulkan !')
			}
			if (formValid) {
				vm.tempEstimatedDeliveryTime=$filter('date')(vm.po.billTo.podate, "yyyy-MM-dd");
				vm.tempEstDelTime		= new Date(vm.tempEstimatedDeliveryTime);
				var isValid = true;
				angular.forEach(vm.deliveryTimeList, function(value, index){
					if(isValid){
						vm.tempDeliveryTime	=$filter('date')(value, "yyyy-MM-dd");
						vm.tempDelTime = new Date(vm.tempDeliveryTime);
						if (vm.tempDelTime < vm.tempEstDelTime){
							isValid = false
							ModalService.showModalInformation('Tanggal Proposed Delivery time tidak boleh kurang dari Tanggal Estimated Delivery Time!');
						}						
					}
				});
				if(isValid){
					//RequestService.modalConfirmation().then(function(result) {
					var isvalid = $scope.validateForm();
					if(isvalid){
							ModalService
							.showModalConfirmation(
									'Apakah anda yakin ingin menyimpan PO ini?')
							.then(function(result) {
							ModalService.showModalInformationBlock();
							$scope.saveData();
						});	
					}
				}
			}
		}
		}
		$scope.saveData = function() {
			vm.taxCodeStringList = []
			/*angular.forEach(vm.taxCode, function(value, index) {
				vm.taxCodeStringList.push(value.code);
				
			})*/
			angular.forEach(vm.addresBookList, function(value1, index1) {
				angular.forEach(value1.purchaseRequestItemList, function(value2, index2) {
					vm.taxCodeStringList.push(vm.taxCode[index1+1][index2].code);
				});
			});
			angular.forEach(vm.deliveryTimeList, function(value, index){
				vm.addresBookList[index].deliveryTime = value;
			})
			var purchaseOrderAddEditDTO = {
				// prNumber : vm.po.billTo.pr.prnumber,
				prNumber : vm.prItemListPo[0].purchaserequest.id,
				poDate : vm.po.billTo.podate,
				vendorName : vm.prItemListPo[0].vendor.nama,
				addressBookDTOForCreatePOList : vm.addresBookList,
				grandTotal : vm.addresBookList[0].grandTotal,
				taxCodeList	: vm.taxCodeStringList
			};

			RequestService.doPOSTJSON(
					'/procurement/purchaseorder/PurchaseOrderServices/insert',
					purchaseOrderAddEditDTO ).then(function success(data) {
				if(data.response.includes != undefined){
					if(!data.response.includes("SUCCESS")){
						ModalService.closeModalInformation();
						ModalService.showModalInformation("Response Error from SAP: "+data.response +" Please click Resend button");
						vm.loading = false;
						$state.go('app.promise.procurement-purchaseorder');
					}else{
						//RequestService.informSaveSuccess();
						ModalService.closeModalInformation();
						ModalService.showModalInformation("Success send data PO");
						$state.go('app.promise.procurement-purchaseorder');
						
					}					
				}else{
					ModalService.closeModalInformation();		
					ModalService.showModalInformation("Success send data PO");
					$state.go('app.promise.procurement-purchaseorder');					
				}
						

			}, function error(response) {
				ModalService.closeModalInformation();
				RequestService.informInternalError();
				vm.loading = false;
			});
		}

		 $scope.cetakPO = function(){
			 RequestService.doPrint({reportFileName:'CetakPurchaseOrder', purchaseOrderId:vm.poId});
	 	}
		 
		$scope.btnKembaliIndex = function() {
			if(vm.toDo == "add"){
				$state.go('app.promise.procurement-booking-order-index');		
			} else {
				$state.go('app.promise.procurement-purchaseorder');
			}
		}
	}
	PurchaseOrderAddController.$inject = [ 'RequestService', 'ModalService', '$scope', '$http',
			'$rootScope', '$resource', '$location', '$filter', 'toaster', '$state', '$stateParams','$window' ];

})();