/**=========================================================
 * Module: RequestForQuotationController.js
 * Author: Tiyan, Mamat
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RequestForQuotationController', RequestForQuotationController);

    function RequestForQuotationController($scope, RequestService, $modal, $state) {    	
        var form = this;

    	$scope.maxSize = 5; //Panjang halaman yang tampil dibawah
    	$scope.currentPage = 1;
    	$scope.maxRecord = $scope.maxSize;
        
        $scope.sortRFQList = [
            {
                name: 'RFQ Number',
                value: 'noRFQ'
            }, {
                name: 'PR Number',
                value: 'purchaseRequest.prnumber'
            }, {
                name: 'Status',
                value: 'requestQuotationStatus'
            }
        ];
        form.sortRFQ = $scope.sortRFQList[0];
        
        $scope.getDataList = function() {        	
    		var params = {
    				startRecord: $scope.currentPage,
    				pageSize: $scope.maxRecord,
    				orderKeyword: form.sortRFQ.value
    		}
    		
    		if (form.searchRFQ != undefined) {
    			params.keywordSearch = form.searchRFQ;
    		}
    		
    		RequestService.requestServer('/procurement/RequestQuotationServices/initSearch', params)
    		.then(function (data) {
    			if (data.Process != undefined) {
    				form.rfqOnProcess = data.Process;
    			} else {
    				form.rfqOnProcess = 0;
    			}
    			if (data.Received != undefined) {
    				form.rfqOnReceived = data.Received;
    			} else {
    				form.rfqOnReceived = 0;
    			}
    			form.rfqAllData = form.rfqOnProcess + form.rfqOnReceived;
    			form.totalItems = data.jmlData;
    			form.requestQuotationList = data.requestQuotationList;
    		});
    	}
    	
    	$scope.setPage = function (pageNo) {
    		$scope.currentPage = pageNo;
    		$scope.getDataList();
    	}
    	
    	$scope.pageChanged = function(maxRecord) {
          $scope.maxRecord = maxRecord;
          $scope.getDataList();
        }
    	
    	$scope.$watch('form.searchRFQ', function(newValue, oldValue) {
    		$scope.getDataList();
    	});
    	
    	form.getSortRFQ = function() {
    		$scope.getDataList();
    	}

        //add rfq
        form.addRFQ = function () {
            $state.go('app.promise.procurement-requestforquotation-add');
        }
        
        form.editRFQ = function(rfq) {
        	delete rfq.showDetail;
        	$state.go('app.promise.procurement-requestforquotation-add', {dataRequest: rfq});
        }
        
        form.deleteRFQ = function(rfq) {
        	
        }
        
        form.priceComparison = function (rfq) {
            var priceComparisonModalInstance = $modal.open({
                templateUrl: '/pricecomparison.html',
                controller: 'PriceComparisonModalInstance',
                size: 'lg',
                resolve: {
                    requestData: function () {
                        return rfq;
                    }
                }
            });

            priceComparisonModalInstance.result.then(function () {
                $scope.getRDQList();
            }, function () {
                console.log('cancel');
            });
        }

        //input quoted
        form.inputQuotation = function (vendor) {
            var inputQuotationModalInstance = $modal.open({
                templateUrl: '/inputquotation.html',
                controller: 'InputQuotationModalInstance',
                size: 'lg',
                resolve: {
                    requestData: function () {
                        return vendor;
                    }
                }
            });

            inputQuotationModalInstance.result.then(function () {
                $scope.getRDQList();
            });
        }
    }
    RequestForQuotationController.$inject = ['$scope', 'RequestService', '$modal', '$state'];
})();

angular
.module('naut')
.controller('PriceComparisonModalInstance', function ($scope, $modalInstance, requestData) {
    // Datepicker
    $scope.form = {};

    $scope.itemComparisonList = [{
        nama: 'Komputer Desktop i5 16G',
        supplierList: [{
            nama: 'PT. Global Aksara Informatika',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 10000000,
                description: 'Komputer Programmer'
            }
        }, {
            nama: 'PT. Code Bean',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12000000,
                description: 'Komputer Programmer'
            }
        }]
    }, {
        nama: 'Laptop i5 8G',
        supplierList: [{
            nama: 'PT. Bhineka Indonesia',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12000000,
                description: 'Komputer Programmer'
            }
        }, {
            nama: 'PT. Jakarta Notebook',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12050000,
                description: 'Komputer Programmer'
            }
        }]
    }];

    $scope.ok = function () {       
        $modalInstance.close('close');
    }
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});

angular
.module('naut')
.controller('InputQuotationModalInstance', function ($scope, $modalInstance, requestData) {    
	$scope.sendQuoteDateStatus = false;
	$scope.sendQuoteDateEvent = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.sendQuoteDateStatus = true;
    }

    //item
    $scope.itemList = [{
        kode: 'ITEM01',
        nama: 'Komputer Desktop i5 Ram 16G',
        qty: 0,
        uom: 'unit',
        price: 0,
        description: ''
    }, {
        kode: 'ITEM02',
        nama: 'Laptop Bussiness i5 Ram 8GB Disk 500GB',
        qty: 0,
        uom: 'unit',
        price: 0,
        description: ''
    }]

    $scope.ok = function () {
        $modalInstance.close('close');
    }
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});