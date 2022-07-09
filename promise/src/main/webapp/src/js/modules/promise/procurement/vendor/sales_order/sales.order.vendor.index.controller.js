(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SalesOrderVendorIndexController', SalesOrderVendorIndexController);

	function SalesOrderVendorIndexController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $modal, ModalService) {
		var vm = this;
		$scope.loading = true;
		$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
        	.success(function (data, status, headers, config) {
	        	if(data != undefined) {
	        		$rootScope.vendorId = data.id;
	        		
	        		$http.get($rootScope.backendAddress+'/procurement/salesorderitem/SalesOrderItemServices/getSalesOrderItemByVendorId/'+ $rootScope.vendorId)
	                .success(function(val){
	                	vm.poList = val;
	                	$scope.loading = false;
	                	
	                	
	                })
	                
	        	} 
        });
	  
        
        $rootScope.status = 0;
		$rootScope.po = {};
		
		vm.view = function(po){
			$rootScope.po = po;
			$rootScope.status = 1;
			var modalInstance = $modal.open({
	            templateUrl: 'viewpo.html',
	            controller: modalInstanceViewPoController,
	            size : 'lg'
	        });
		}
		
		var modalInstanceViewPoController = function ($scope, $http, $modalInstance, $resource, $rootScope,$log, $location) {
			$scope.loading = true;
			$http.get($rootScope.backendAddress+'/procurement/salesorderitem/SalesOrderItemServices/getSalesOrderItemBySalesOrderIdAndByVendorId/'+$rootScope.po.salesOrder.id+'/' +$rootScope.vendorId)
	        .success(function(val){
	        	$scope.catalogList = val;
	        	 var total = 0;
	        	 
	    		 angular.forEach(val, function(val, index) {
	    			 total = total + val.harga;
	    		 })
	    		 
	    		 var totalWithQty = 0;
	    		 angular.forEach(val, function(valListCat, index) {
	    			 totalWithQty = totalWithQty + ((valListCat.harga)*(valListCat.qty));
	    		 })
    	
	    		 $scope.grandTotal = totalWithQty;
	    		 
	    		 $scope.boNumber = $scope.catalogList[0].soNumber;
	    		 
	    		 $scope.loading = false;
	    		 
	        })
	        
			$scope.cancel = function () {
		        $modalInstance.dismiss('cancel');
		    };
			
		}
		modalInstanceViewPoController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', '$log', '$location'];
	        	
		
		//vm.poList
		
	}

	SalesOrderVendorIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$modal', 'ModalService'];

})();