/**=========================================================
 * Module: PurchaseOrderAddController.js
 * Author: Mamat, <n>
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PurchaseOrderEditVendorsController', PurchaseOrderEditVendorsController);

    function PurchaseOrderEditVendorsController(RequestService, $scope, $http, $rootScope, $resource, $location, $filter, toaster, ModalService) {
        var form = this;
        
        form.purchaseOrderForm = {};
        form.purchaseOrder = $rootScope.purchaseOrder; 
        form.vendorList = [];
        form.billingAddressList = [];
        form.shippingToList = [];
        form.purchaseOrder.vendor = {};
        form.purchaseRequestItemList = [];
       
        form.vendor = {};
        
        form.vendor = {};
        form.purchaseOrderTerm = {};
        
        form.isProcessing = false;
        form.btnKembaliIndex = true;
        form.statusSimpan = false;
        form.purchaseOrderTerm.poTermType = 2;
        form.doGenerateNumber = false;
        form.shipToThisAddress = false;
        
        $scope.btnDisable = false;

      
        
       
        
        RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseOrderDetailById/' + form.purchaseOrder.id)
		.then(function successCallback(data) {
			form.purchaseOrderForm = data;
			form.purchaseOrder = form.purchaseOrderForm.purchaseOrder;
				
			$scope.inputDisable = true;
			if(form.purchaseOrder.poNumber == ""){
				$scope.inputDisable = false;
			} else {
				$scope.btnDisable = true;
			}
			
			if(form.purchaseOrder.addressBook == null){
				form.purchaseOrder.addressBook = {};
				form.purchaseOrder.addressBook.organisasi = form.purchaseOrder.purchaseRequest.organisasi; 
			}
			
			form.purchaseRequestList.push(form.purchaseOrder.purchaseRequest);
			form.purchaseRequestItemList = form.purchaseOrderForm.purchaseRequestItemList;
			
			form.purchaseOrderTerm = form.purchaseOrderForm.purchaseOrderTerm;
			
			form.vendor = form.purchaseOrderForm.vendor;
			
			form.vendorList.push(form.vendor);
			 
			form.purchaseOrderForm.shippingToDtlList.forEach(function(shippingToDtl, indexShipping, array) {
				var shippingTo = shippingToDtl.shippingTo;
				shippingTo.purchaseOrderItemList = [];
				
				var purchaseOrderItemListNew = [];
				var purchaseOrderItemList = shippingToDtl.purchaseOrderItemList;

				var purchaseRequestItemList = shippingToDtl.purchaseRequestItemList;
				
				purchaseRequestItemList.forEach(function(purchaseRequestItem, indexPOI, array) {
					
					var purchaseOrderItem = {};
					var check = false;
					
					purchaseOrderItemList.forEach(function(purchaseOrderItemParam, index, array) {			
						if(purchaseOrderItemParam.purchaseRequestItem.id == purchaseRequestItem.id){
							purchaseOrderItemParam.selected = '1';
							purchaseOrderItem.shipquantityReadOnly = false;
							purchaseOrderItem = purchaseOrderItemParam;
							check = true
						}
					});
					
					if(check == false){ 
						purchaseOrderItem.item = {};
						purchaseOrderItem.item = purchaseRequestItem.item; 
						
						purchaseOrderItem.unitPrice = purchaseRequestItem.price; 

						purchaseOrderItem.selected = '';
						purchaseOrderItem.shipquantityReadOnly = true;
						
						purchaseOrderItem.purchaseRequestItem = {};
						purchaseOrderItem.purchaseRequestItem = purchaseRequestItem;
            			purchaseOrderItem.totalUnitPrices = 0;
            			purchaseOrderItem.quantitySend = 0;
					}
					purchaseOrderItemListNew.push(purchaseOrderItem);
				});
				
				
				shippingTo.purchaseOrderItemList = purchaseOrderItemListNew;
				
				form.shippingToList.push(shippingTo);
			});
 
	       
		}, function errorCallback(response) {				 
			ModalService.showModalInformation('Terjadi kesalahan pada system!');
		});
        
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        form.format = form.formats[4];

        form.poDateOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.poDateOpened = true;
        };
        
        form.poDateOpenDynamic = function(indexInput, $event){
            $event.preventDefault();
            $event.stopPropagation();
            
            form.shippingToList.forEach(function(shipTo, index, array) {
             	if(index == indexInput){
                 	shipTo.poDateOpened = true;
             	}else{
                 	shipTo.poDateOpened = false;
             	}
             });
        }

        form.btnKembaliIndex = function () {
            $location.path('/app/promise/procurement/purchaseorder/vendor');
        }
        
        form.generateNumber = function () {
			var date = new Date();
			form.purchaseOrder.poNumber = "PO/" + date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2) + "/AI/" + (1000 + Math.ceil(Math.random() * 9000));
		};
		
		
		
	
       
        
        form.doInput = function(indexShipping, indexPOI){
        	
        	form.shippingToList.forEach(function(shipTo, index, array) {
        		if(index == indexShipping){
        			shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
                		if(indexDua == indexPOI){
                			if(purchaseOrderItem.shipquantityReadOnly == null){
                				purchaseOrderItem.shipquantityReadOnly = true;
                				purchaseOrderItem.selected = "";
                    			purchaseOrderItem.quantitySend = 0;
                			}else{
                    			if(purchaseOrderItem.shipquantityReadOnly == true){
                    				purchaseOrderItem.shipquantityReadOnly = false;
                        			purchaseOrderItem.selected = "1";
                        			purchaseOrderItem.quantitySend = 0;
                    			}else{
                    				purchaseOrderItem.shipquantityReadOnly = true;
                        			purchaseOrderItem.selected = "";
                        			purchaseOrderItem.quantitySend = 0;
                    			}
                			}
                			purchaseOrderItem.totalUnitPrices = 0;
                		}
                	});
        		}
        	});
        };
        
        form.getTotalUnitPrice = function(indexShipping, indexPOI){
        	
        	/** validasi jumlah item **/
        	var maxQty = 0;
        	var sisaQty = 0;
        	var actualQty = 0;
        	var prId = 0;
        	var countQty = 0;
        	var itemName = '';
        	
        	form.shippingToList.forEach(function(shipTo, index, array) {
        		if(index == indexShipping){
	        		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
	        			if(indexDua == indexPOI){ 
	            			maxQty = purchaseOrderItem.purchaseRequestItem.quantity;
	            			prId = purchaseOrderItem.purchaseRequestItem.id;
	            			itemName = purchaseOrderItem.purchaseRequestItem.item.nama;
	                		actualQty += Number(purchaseOrderItem.quantitySend);
	            		}
	        		});
        		}
        	});
        	
        	form.shippingToList.forEach(function(shipTo, index, array) {
        		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
                	if(prId == purchaseOrderItem.purchaseRequestItem.id){
                		countQty += Number(purchaseOrderItem.quantitySend);
                	}
                });
        	});

        	if(Number(countQty) > Number(maxQty)){ 
        		
        		var check = true;
	        	while(check) {
            		
            		actualQty = actualQty.toString();
        			actualQty = actualQty.substring(0, actualQty.length-1);
        			
        			form.shippingToList.forEach(function(shipTo, index, array) {
                		if(index == indexShipping){
        	        		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
        	        			if(indexDua == indexPOI){ 
        	        				purchaseOrderItem.quantitySend = Number(actualQty);
        	            			purchaseOrderItem.totalUnitPrices = actualQty * purchaseOrderItem.unitPrice;
        	            		}
        	        		});
                		}
                	});
        			
        			var countCheck = 0;
        			form.shippingToList.forEach(function(shipTo, index, array) {
                		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
                        	if(prId == purchaseOrderItem.purchaseRequestItem.id){
                        		countCheck += Number(purchaseOrderItem.quantitySend);
                        	}
                        });
                	});

	        		if(Number(countCheck) <= Number(maxQty)){
	        			check = false;
	        			break;
	        		}
	        	}
				 
	        	toaster.pop('error', 'Purchase Order', 'Maksimal Quantity ' + itemName + ' ' +  maxQty +' unit!');
        	}

        	var subTotal = 0;
        	form.shippingToList.forEach(function(shipTo, index, array) {
        		if(index == indexShipping){
	        		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
	        			if(indexDua == indexPOI){ 
	        				purchaseOrderItem.quantitySend = Number(actualQty);
	            			purchaseOrderItem.totalUnitPrices = actualQty * purchaseOrderItem.unitPrice;
	            		}
	        		});
        		}
        	});
        	
        	/** jumlah harga **/
        	form.shippingToList.forEach(function(shipTo, index, array) {
        		shipTo.purchaseOrderItemList.forEach(function(purchaseOrderItem, indexDua, array) {
	        		subTotal += purchaseOrderItem.totalUnitPrices;
	        	});
        	});

        	form.purchaseOrder.subTotal = subTotal;
        }
        
    }
    PurchaseOrderEditVendorsController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'toaster', 'ModalService'];

})();