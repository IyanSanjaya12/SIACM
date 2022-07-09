/**
 * ========================================================= Module:
 * EmailSMTPController - index.controller.js Controller for Email SMTP Server
 * Configuration =========================================================
 */

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('EmailSMTPController', EmailSMTPController);

	function EmailSMTPController($scope, $http, $rootScope, $resource, $location, toaster, $modal, $stateParams, RequestService) {
		var vm = this;
		/*vm.smtpServer = ($stateParams.smtpServer != undefined) ? $stateParams.smtpServer : {};*/
		
		vm.loading = true;
		vm.isSMTPServer = false;
		vm.submitted =false;
		
		$http.get($rootScope.backendAddress + '/procurement/email/master-smtp-server/get-list')
			.success(function (data, status, headers, config) {
				if (typeof data[0].id !== 'undefined') {
					vm.smtpServer = data[0];
					vm.isSMTPServer = true;
					/*
					 * $scope.smtp = data; $scope.smtp.isSSL = data.ssl;
					 * $scope.isSMTPServer = true;
					 */
					
				}
				vm.loading = false;
			})
			.error(function (data, status, headers, config) {
				vm.loading = false;
				RequestService.informInternalError();
			});
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if(formValid){
				RequestService.modalConfirmation().then(function(result) {
					$scope.saveData();
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formSMTPServer[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.saveData = function() { 
			vm.url = '/procurement/email/master-smtp-server/insert';
			if (vm.isSMTPServer) {
				vm.url = '/procurement/email/master-smtp-server/update';
			}
		
			vm.loading = true;
			
			RequestService.doPOSTJSON(vm.url, vm.smtpServer)
				.then(function success(data) {
					vm.loading = false;
					RequestService.informSaveSuccess();
				},
				function error(response) {
					vm.loading = false;
					RequestService.informInternalError();
				}
			);
		
		}
	
	}
	EmailSMTPController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', '$stateParams', 'RequestService'];

})();