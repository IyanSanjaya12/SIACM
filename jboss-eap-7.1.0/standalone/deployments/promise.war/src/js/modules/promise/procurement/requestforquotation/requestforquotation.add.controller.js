/**=========================================================
 * Module: RequestForQuotationController.js
 * Author: Tiyan, Mamat
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RequestForQuotationAddController', RequestForQuotationAddController);

    function RequestForQuotationAddController($scope, RequestService, $state, $stateParams, $modal, ngTableParams) {
        var form = this;
        
        if ($stateParams.dataRequest != undefined) {
        	form.dataRequest = $stateParams.dataRequest;
        } else {
        	form.dataRequest = {};
        	form.dataRequest.requestQuotationVendorList = [];
        	form.quotationItemList = [];
        }
        
        if (form.dataRequest != undefined) {
        	if (form.dataRequest.requestQuotationVendorList != undefined && form.dataRequest.requestQuotationVendorList.length > 0) {
        		if (form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList != undefined && form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList.length > 0) {
        			form.quotationItemList = form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList;
        		}
        	}
        }
        
        form.lookupPR = function() {
        	var lookupPRModalInstance = $modal.open({
                templateUrl: 'app/views/promise/procurement/master/LookupPage/PurchaseRequestLOV.html',
                controller: 'PurchaseRequestModal',
                size: 'lg',
                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                	return $ocLazyLoad.load('src/js/modules/promise/procurement/master/LookupPage/PurchaseRequestLOVController.js');
                }]
            });

        	lookupPRModalInstance.result.then(function (dataPR) {
        		form.dataRequest.purchaseRequest = dataPR;
        		
        		RequestService.doGET('/procurement/order/purchaseRequestItemServices/getListByPurchaseRequestId/' + dataPR.id)
                .then(function(data) {
                	if (data != undefined && data.length > 0) {
                		angular.forEach(data, function(dataItem) {
                			var requestQuotationItem = {
                					item: dataItem.item,
                					quantity: dataItem.quantity,
                					price: dataItem.price,
                					satuan: dataItem.item.satuanId,
                					description: dataItem.specification
                			};
                			form.quotationItemList.push(requestQuotationItem);
                		});
                    	form.tableItemList.reload();
                	}
                });
        		
//        		RequestService.requestServer('/procurement/vendor/vendorServices/getVendorByPR' , {prId: dataPR.id})
//                .then(function(data) {
//                	form.dataRequest.requestQuotationVendorList = data;
//                });
            });
        }
        
        form.tableVendorList = new ngTableParams({
            page: 1, // show first page
            count: 5 // count per page
        }, {
            total: form.dataRequest.requestQuotationVendorList.length, 
            getData: function ($defer, params) {
                $defer.resolve(form.dataRequest.requestQuotationVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
        
    	form.tableItemList = new ngTableParams({
            page: 1, // show first page
            count: 5 // count per page
        }, {
            total: form.quotationItemList.length, 
            getData: function ($defer, params) {
                $defer.resolve(form.quotationItemList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
        
        form.postDateStatus = false;
        form.postDateEvent = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.postDateStatus = true;
        }
        
        form.deliveryQuoteDateStatus = false;
        form.deliveryQuoteDateEvent = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.deliveryQuoteDateStatus = true;
        }
        
        form.backToIndex = function() {
        	$state.go("app.promise.procurement-requestforquotation");
        }
        
        form.saveData = function() {
        	var targetURL = "/procurement/RequestQuotationServices/insert";
        	if (form.dataRequest.id != undefined) {
        		targetURL = "/procurement/RequestQuotationServices/update";
        	}
        	RequestService.modalConfirmation()
			.then(function (result) {
				angular.forEach(form.dataRequest.requestQuotationVendorList, function(dataVendor, index){
					dataVendor.requestQuotationStatus = 'Received';
					dataVendor.requestQuotationItemList = form.quotationItemList;
				});
				RequestService.doPOSTJSON(targetURL, form.dataRequest)
	        	.then(function (data) {
	        		$state.go("app.promise.procurement-requestforquotation");
	    		});			
			});
        }
        
        form.btnSendToVendor = function() {
        	
        }
        
        form.AddVendor = function() {
        	var addVendorModalInstance = $modal.open({
                templateUrl: 'app/views/promise/procurement/master/LookupPage/VendorLOV.html',
                controller: 'VendorModal',
                size: 'lg',
                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                	return $ocLazyLoad.load('src/js/modules/promise/procurement/master/LookupPage/VendorLOVController.js');
                }]
            });

            addVendorModalInstance.result.then(function (dataVendor) {
            	var requestQuotationVendor = {
            			vendor: dataVendor
            	}
            	form.dataRequest.requestQuotationVendorList.push(requestQuotationVendor);
            	form.tableVendorList.reload();
            });
        }
        
        form.findPRNumber = function () {
        	if (form.dataRequest.purchaseRequest.prnumber != undefined && form.dataRequest.purchaseRequest.prnumber.length > 2) {
        		RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseRequestByNumber/' + form.dataRequest.purchaseRequest.prnumber)
                .then(function (data) {
                	if (data != undefined && data.length > 0) {
                		form.dataRequest.purchaseRequest = data[0];
                		
                		RequestService.doGET('/procurement/order/purchaseRequestItemServices/getListByPurchaseRequestId/' + data[0].id)
                        .then(function(data) {
                        	if (data != undefined && data.length > 0) {
                        		angular.forEach(data, function(dataItem) {
                        			var requestQuotationItem = {
                        					item: dataItem.item,
                        					quantity: dataItem.quantity,
                        					price: dataItem.price,
                        					satuan: dataItem.item.satuanId,
                        					description: dataItem.specification
                        			};
                        			form.quotationItemList.push(requestQuotationItem);
                        		});
                            	form.tableItemList.reload();
                        	}
                        });
                	}
                });
        	}
        }

        //Items
        form.deleteItem = function (item) {
            var index = $scope.itemList.indexOf(item);
            if (index > -1) {
                var konfirmasi = confirm('Apakah Anda yakin ingin menghapus Item "' + item.itemname + '"');
                if (konfirmasi) {
                    $scope.itemList.splice(index, 1);
                }
            }
        }

        //Vendor
        form.deleteVendor = function (vendorQuotation) {
        	RequestService.modalConfirmation("Apakah Anda yakin ingin menghapus Vendor "+ vendorQuotation.vendor.nama)
        	.then(function(){
        		var indexRecord = form.dataRequest.requestQuotationVendorList.indexOf(vendorQuotation);
        		form.dataRequest.requestQuotationVendorList.splice(indexRecord, 1);
        		form.tableVendorList.reload();
        	});
        }
        
    }
    RequestForQuotationAddController.$inject = ['$scope', 'RequestService', '$state', '$stateParams', '$modal', 'ngTableParams'];
})();