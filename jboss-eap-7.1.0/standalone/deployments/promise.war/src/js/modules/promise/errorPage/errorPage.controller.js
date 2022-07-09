/**
 * ========================================================= Module:
 * errorPage.controller.js Author: Reinhard
 * =========================================================
 */
(function() {
	'use strict';

	angular.module('naut').controller('ErrorPageController',
			ErrorPageController);

	function ErrorPageController($scope, $resource, $location,$stateParams, RequestService) {
		var form = this;
		
		form.errCode = $stateParams.errorCode;
		if (form.errCode == 'PRMS-ERR-001'){
			form.errMsg = "Login Required!";
		}else if(form.errCode == 'PRMS-ERR-002'){
			form.errMsg = "Insufficient Privillage!";
		}else{
			form.errMsg = "Your Session has Timed Out!";
		}
		
		$scope.gotoHome = function () {
			/*KAI 09/17/2021 lack of Authorization*/
			RequestService.doSignOut();
			//$location.path('/page/portal');
		}
		
	}

	ErrorPageController.$inject = [ '$scope', '$resource', '$location','$stateParams', 'RequestService'];

})();