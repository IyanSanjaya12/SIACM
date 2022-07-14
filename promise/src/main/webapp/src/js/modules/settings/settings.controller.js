/**=========================================================
 * Module: SettingsController.js
 * Handles app setting
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SettingsController', SettingsController);
	
	function SettingsController($scope, settings, $rootScope) {

		// Restore/Save layout settings
		settings.loadAndWatch();

		// Set scope for panel settings
		$scope.themes = settings.availableThemes();
		$scope.setTheme = settings.setTheme;
		
		$rootScope.backendAddress = "http://localhost:8181/promisews";
		$rootScope.backendAddressIP = 'http://localhost:8181';
		$rootScope.uploadBackendAddress      = 'http://localhost:8181/promisews/procurement/file/upload';		
		$rootScope.viewUploadBackendAddress  = 'http://localhost:8181/promisews/file/view';	
		$rootScope.cmAppLink  = 'http://localhost:8181/promisecmclient/dashboard.promise'; 
        $rootScope.forumAppLink="http://localhost:8181/forum/";  
        $rootScope.pmAppLink  = ''; 
/*
        $rootScope.backendAddress = "http://promise.local/promisews";
		$rootScope.backendAddressIP = 'http://promise.local';
		$rootScope.uploadBackendAddress      = 'http://promise.local/promisews/procurement/file/upload';		
		$rootScope.viewUploadBackendAddress  = 'http://promise.local/promisews/file/view';	
		 
		$rootScope.cmAppLink  = 'http://pm.dev.promise.co.id/promisecmclient/';
		$rootScope.forumAppLink="http://promise.local/forum/";  	 	    
		*/
        // Vendor Update Status Constanta
        $rootScope.vendorStatus = {
            daftar: 0,
            approve: 1,
            blackList: 2
        }
        
	}
	SettingsController.$inject = ['$scope', 'settings', '$rootScope'];
})();