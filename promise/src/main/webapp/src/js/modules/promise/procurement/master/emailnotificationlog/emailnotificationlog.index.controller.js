/**=========================================================
 * Module: EmailNotificationLogController.js
 * Controller for Log Email Notifikasi
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('EmailNotificationLogController', EmailNotificationLogController);

	function EmailNotificationLogController($scope, $http, $rootScope, $resource, $location, toaster, $state, RequestService, ModalService) {
		var vm = this;

		$scope.getEmailNotification = function () {
			vm.loading = true;
			
			RequestService.doGET('/procurement/email/emailNotificationServices/getListEmailNotificationLog')
				.then(function success(data) {
					vm.emailNotificationLogList = data;
					vm.loading = false;
				}, function error(response) { 
					RequestService.informInternalError();
					vm.loading = false;
				});
		}
		
		$scope.getEmailNotification();
		
		$scope.resendEmail = function(emailNotification){
			RequestService.modalConfirmation('Anda yakin ingin mengirim ulang Email?').then(function(result) {
				RequestService.doPOSTJSON('/procurement/email/emailNotificationServices/resendEmailFromLog', emailNotification)
				.then(function success(data) {
					ModalService.showModalInformation('Sukses Mengirim Email');
				}, function error(response) { 
					RequestService.informError('Gagal Mengirim Email!');
				});				
			});
		}
	}

	EmailNotificationLogController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state','RequestService', 'ModalService'];

})();