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
                templateUrl: 'app/views/promise/procurement/requestforquotation/ViewComparison.modal.html',
                controller: 'PriceComparisonModalInstance',
                size: 'lg',
                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                	return $ocLazyLoad.load('src/js/modules/promise/procurement/requestforquotation/ViewComparison.modal.controller.js');
                }]
            });

            priceComparisonModalInstance.result.then(function () {
                $scope.getRDQList();
            });
        }

        //input quoted
        form.inputQuotation = function (requestQuotation, vendorRFQ) {
            var inputQuotationModalInstance = $modal.open({
                templateUrl: 'app/views/promise/procurement/requestforquotation/inputquotation.modal.html',
                controller: 'InputQuotationModalInstance',
                size: 'lg',
                resolve: {
                	'$ocLazyLoad' : function ($ocLazyLoad) {
                    return $ocLazyLoad.load(['angularFileUpload'])
                        .then(function () {
                            return $ocLazyLoad.load('src/js/modules/promise/procurement/requestforquotation/inputquotation.modal.controller.js');
                        });
                	},
                	requestQuotation: function() {
                		return requestQuotation;
                	},
                	vendorRFQ: function() {
                		return vendorRFQ;
                	}
                }
            });

            inputQuotationModalInstance.result.then(function () {
                $scope.getDataList();
            });
        }
    }
    RequestForQuotationController.$inject = ['$scope', 'RequestService', '$modal', '$state'];
})();