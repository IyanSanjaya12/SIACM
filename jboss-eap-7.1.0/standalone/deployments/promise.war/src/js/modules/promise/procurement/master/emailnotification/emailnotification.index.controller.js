/**=========================================================
 * Module: EmailNotificationController.js
 * Controller for Email Notifikasi
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('EmailNotificationController', EmailNotificationController);

	function EmailNotificationController($scope, $http, $rootScope, $resource, $location, toaster,$state,RequestService) {
		var vm = this;

		$scope.getEmailStatus = function () {
			
			RequestService.doGET('/procurement/email/email-notification-status/get-list')
				.then(function success(data) {
					var emailStatus = data;
					if (emailStatus.length == 0) {
						vm.emailStatus = {};
					} else {
						vm.emailStatus = emailStatus[0];
						
					}
				}, function error(response) { 
					RequestService.informInternalError();
			});
		}
		
		$scope.getEmailStatus();
		
		$scope.statusUpdate = function () {
			vm.loading = true;
			
			RequestService.doGET('/procurement/email/master-smtp-server/get-by-id/2')
				.then(function success(data) {
					var statusAktif;
					if (typeof data.host !== 'undefined') {
						if (vm.emailStatus.statusAktif == true) {
							statusAktif = 1;
						} else {
							statusAktif = 0;
						}
						vm.formStatusAktif = {
								id: vm.emailStatus.id,
								statusAktif: statusAktif
							};
						vm.url ='/procurement/email/email-notification-status/';
						
						if (typeof vm.emailStatus.id !== 'undefined') {
							vm.url = vm.url + 'update';
						} else {
							vm.url = vm.url + 'insert';
						}
						
						RequestService.doPOST(vm.url,vm.formStatusAktif)
						.then(function success(data) {
							if (typeof data !== 'undefined') {
								RequestService.informSuccess('Status Email Menjadi ' +data.data.statusAktif+ ', berhasil.');
								vm.loading = false;
							}
							vm.loading = false;
						}, function error(response) { 
							RequestService.informInternalError();
							vm.loading = false;
						});
						
					}
			}, function error(response) { 
				RequestService.informInternalError();
				vm.loading = false;
			});
			
		}

		$scope.getEmailNotification = function () {
			vm.loading = true;
			
			RequestService.doGET('/procurement/master/email-notification/get-list')
				.then(function success(data) {
					vm.emailNotificationList = data;
					vm.loading = false;
				}, function error(response) { 
					RequestService.informInternalError();
					vm.loading = false;
				});
		}
		
		$scope.getEmailNotification();
		
		$scope.add = function () {
			$state.go('app.promise.procurement-master-emailnotification-view',{
				toDo:'add'
			});
		};
		
		$scope.edit = function (emailNotification) {
			$state.go('app.promise.procurement-master-emailnotification-view',{
				emailNotification:emailNotification,
				toDo:'edit'
			});
		};

		$scope.del = function (emailNotification) {
			RequestService.deleteModalConfirmation().then(function (result) {
				vm.loading = true;
				
				RequestService.doPOST('/procurement/master/email-notification/delete',emailNotification)
				.then(function success(data) {
					RequestService.informDeleteSuccess();
					vm.loading = false;
					$scope.getEmailNotification();
				}, function error(response) { 
					RequestService.informInternalError();
					vm.loading = false;
				});
			});
		};
		
	}

	EmailNotificationController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state','RequestService'];

})();