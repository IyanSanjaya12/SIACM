(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BlacklistIndexController', BlacklistIndexController);

    function BlacklistIndexController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $state) {
    	
        var vm = this;
        
        $scope.getBlacklistVendorList = function () { 
			RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendorList')
			.then(function (data, status, headers, config) { 
				vm.blacklistVendorList = data;
				vm.loading = false;
			},function error(response) {
				vm.loading = false;
			})
		}
        $scope.getBlacklistVendorList();

		$scope.add = function () {
			$state.go('app.promise.procurement-manajemenvendor-blacklistvendor-add', {
				toDo : "Add"
			});
		};
		
		$scope.view = function (bv) { 
        	$rootScope.blacklistVendor = bv;
        	$state.go('app.promise.procurement-manajemenvendor-blacklistvendor-view',{
        		toDo:'View'
        	});
        }
    }
    
    BlacklistIndexController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state'];

})();