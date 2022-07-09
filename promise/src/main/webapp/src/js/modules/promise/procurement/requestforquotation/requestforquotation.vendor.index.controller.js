/**=========================================================
 * Module: RequestForQuotationController.js
 * Author: Tiyan, Mamat
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RequestForQuotationVendorController', RequestForQuotationVendorController);

    function RequestForQuotationVendorController($scope, RequestService, $modal, $state) {    	
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
    				vendorId: RequestService.getUserLogin().user.id,
    				startRecord: $scope.currentPage,
    				pageSize: $scope.maxRecord,
    				orderKeyword: form.sortRFQ.value
    		}
    		
    		if (form.searchRFQ != undefined) {
    			params.keywordSearch = form.searchRFQ;
    		}
    		
    		RequestService.requestServer('/procurement/RequestQuotationServices/initSearchVendor', params)
    		.then(function (data) {
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

        //input quoted
        form.inputQuotation = function (requestQuotation) {
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
                		var vendorRFQ = {};
                		angular.forEach(requestQuotation.requestQuotationVendorList, function(vendor) {
                			if (vendor.vendor.user == RequestService.getUserLogin().user.id) {
                				vendorRFQ = vendor;
                			}
                		});
                		return vendorRFQ;
                	}
                }
            });

            inputQuotationModalInstance.result.then(function () {
                $scope.getDataList();
            });
        }
    }
    RequestForQuotationVendorController.$inject = ['$scope', 'RequestService', '$modal', '$state'];
})();