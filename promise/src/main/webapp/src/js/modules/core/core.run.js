/**=========================================================
 * Module: ApplicationRun.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.run(appRun);


	appRun.$inject = ['$rootScope', '$state', '$stateParams', '$localStorage', 'translator', 'settings', '$window','$location'];

	function appRun($rootScope, $state, $stateParams, $localStorage, translator, settings, $window, $location) {
		
		// Set reference to access them from any scope
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
		$rootScope.$storage = $localStorage;

		translator.init();
		settings.init();
	}

})();