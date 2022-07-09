/**=========================================================
 * Module: EditEmailNotifikasiController.js
 * Controller for Email Notifikasi
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('EmailNotificationViewController', EmailNotificationViewController);
    
    function EmailNotificationViewController($scope, $http, $rootScope, $resource, $location, toaster, $modal,$state,RequestService,$stateParams) {

        var vm = this;   
        vm.emailNotification = ($stateParams.emailNotification != undefined) ? $stateParams.emailNotification : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.submitted = false;
		
        $scope.getTahapan = function () {
            $http.get($rootScope.backendAddress + '/procurement/master/tahapan/get-list')
                .success(function (data, status, headers, config) {
                    $scope.tahapanList = data;
                }).error(function (data, status, headers, config) {
                	RequestService.informInternalError();
                });
        }
        $scope.getTahapan();

        $scope.getRole = function () {
            $http.get($rootScope.backendAddress + '/procurement/master/role/get-list')
                .success(function (data, status, headers, config) {
                    $scope.roleList = data;
                }).error(function (data, status, headers, config) {
                	RequestService.informInternalError();
                });
        }
        $scope.getRole();
        
        $scope.save = function(formValid) {
        	vm.submitted = true;
        	if(formValid) {
				RequestService.modalConfirmation().then(function(result) {
					$scope.saveData();
					vm.loading = true;
				});
			}
		}

        $scope.delValidation = function($event) {
			$scope.formNotifEmail[$event.target.name].$setValidity($event.target.name, true);
		}
        
        $scope.saveData = function () {
           
        	vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/master/email-notification/insert';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/master/email-notification/update';
			}

			RequestService.doPOSTJSON(vm.url, vm.emailNotification)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								$scope.formNotifEmail.namaNotifEmail.$setValidity('namaNotifEmail', false);
								vm.loading = false;
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-emailnotification');
						}

					}
				}, function error(response) {
					vm.loading = false;
				});
            
        };

        $scope.back = function () {
        	$state.go('app.promise.procurement-master-emailnotification');
        }

    }
    EmailNotificationViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal','$state','RequestService','$stateParams'];

})();