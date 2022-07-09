(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BlacklistController', BlacklistController);

    function BlacklistController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $ocLazyLoad, $state) {
    	
    	/** load js sub modul **/
		$ocLazyLoad.load('src/js/modules/promise/procurement/blacklistvendor/blacklistvendor.add.controller.js');
		$ocLazyLoad.load('src/js/modules/promise/procurement/blacklistvendor/blacklistvendor.view.controller.js');
		
        var vm = this;
        
        vm.getBlacklistVendorList = function () { 
			RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendorList')
			.then(function (data, status, headers, config) { 
				vm.blacklistVendorList = data;
			}); 
		}
        vm.getBlacklistVendorList();

		vm.add = function () {
			$location.path('/app/promise/procurement/manajemenvendor/blacklistvendor/add');
		};
	
        $scope.view = function (bv) { 
        	$rootScope.blacklistVendor = bv;
			$location.path('/app/promise/procurement/manajemenvendor/blacklistvendor/view');
        }
    }
    
    BlacklistController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$ocLazyLoad', '$state	'];

})();