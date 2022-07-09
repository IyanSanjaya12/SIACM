/**=========================================================
 * Module: RequestForQuotationController.js
 * Author: Tiyan, Mamat
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RequestForQuotationAddController', RequestForQuotationAddController);

    function RequestForQuotationAddController($scope, RequestService, $state, $stateParams, $modal) {
        var form = this;
        
        form.dataRequest = $stateParams.dataRequest;
        
        if (form.dataRequest != undefined) {
        	if (form.dataRequest.requestQuotationVendorList != undefined && form.dataRequest.requestQuotationVendorList.length > 0) {
        		if (form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList != undefined && form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList.length > 0) {
        			form.quotationItemList = form.dataRequest.requestQuotationVendorList[0].requestQuotationItemList;
        		}
        	}
        }
        
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
				RequestService.doPOSTJSON(targetURL, form.dataRequest)
	        	.then(function (data) {
	        		$state.go("app.promise.procurement-requestforquotation");
	    		});			
			});
        }
        
        
        
        form.info = {};
        $scope.vendorList = [];
        
        $scope.findPRNumber = function (prNumber) {
            if (typeof prNumber !== 'undefined') {
                if (prNumber.length > 2) {
                	RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseRequestByNumber/' + prNumber)
                        .then(function (data) {
                            $scope.prNumberList = data;
                            $scope.loading = false;
                        });
                }
            }

        }
        $scope.prSelected = function (pr) {
            $scope.loadingPRItem = true;
            RequestService.doGET('/procurement/order/purchaseRequestItemServices/getListByPurchaseRequestId/' + pr.id)
            .then(function(data) {
            	$scope.itemList = data;
                $scope.loadingPRItem = false;
                $scope.getVendorByPr(pr.id);
            });
        };
        $scope.getVendorByPr = function (prId) {
            $scope.loadingVendor = true;
            RequestService.requestServer('/procurement/vendor/vendorServices/getVendorByPR' , {prId: prId})
            .then(function(data) {
            	$scope.vendorList = data;
                $scope.loadingVendor = false;
            });
        }

        // Datepicker
        form.clear = function () {
            form.info.issuanceDate = null;
            form.info.quoteDeliveryDate = null;
        };
        form.disabled = function (date, mode) {
            return false; // ( mode === 'day' && ( date.getDay() === 0 ||
            // date.getDay() === 6 ) );
        };
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        form.info.issuanceDateOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.info.issuanceDateOpened = true;
        };
        form.info.quoteDeliveryDateOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.info.quoteDeliveryDateOpened = true;
        }

        //Items
        $scope.deleteItem = function (item) {
            console.log('>> delete : ' + JSON.stringify(item));

            var index = $scope.itemList.indexOf(item);
            if (index > -1) {
                var konfirmasi = confirm('Apakah Anda yakin ingin menghapus Item "' + item.itemname + '"');
                if (konfirmasi) {
                    $scope.itemList.splice(index, 1);
                }
            }
        }

        //Vendor
        $scope.deleteVendor = function (vendor) {
            var index = $rootScope.vendorList.indexOf(vendor);
            if (index > -1) {
                var konfirmasi = confirm('Apakah Anda yakin ingin menghapus Vendor "' + vendor.nama + '"');
                if (konfirmasi) {
                    $rootScope.vendorList.splice(index, 1);
                }
            }
        }
        $scope.btnAddVendor = function () {
            var addVendorModalInstance = $modal.open({
                templateUrl: '/addvendor.html',
                controller: AddVendorModalInstance,
                size: 'md'
            });

            addVendorModalInstance.result.then(function () {
                console.log(">>save after action");
            }, function () {
                console.log('cancel');
            });
        }
        var AddVendorModalInstance = function ($scope, $modalInstance, $rootScope) {
            $scope.form = {};
            $scope.form.vendor = {};
            $scope.findVendor = function (vendor) {
                if (vendor.length > 0) {
                    $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByName/' + vendor)
                        .then(function (response) {
                            $scope.vendorList = response.data;
                        });
                }
            }

            $scope.ok = function () {
                console.log(">>>" + JSON.stringify($scope.form.vendor.selected));
                var index = $rootScope.vendorList.indexOf($scope.form.vendor.selected);
                if(index == -1){
                    $rootScope.vendorList.push($scope.form.vendor.selected);
                } else {
                    alert('Vendor '+$scope.form.vendor.selected.nama+' sudah Ada!');
                }
                $modalInstance.close('closed');
            }
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        AddVendorModalInstance.$inject = ['$scope', '$modalInstance', '$rootScope'];
    }
    RequestForQuotationAddController.$inject = ['$scope', 'RequestService', '$state', '$stateParams', '$modal'];
})();