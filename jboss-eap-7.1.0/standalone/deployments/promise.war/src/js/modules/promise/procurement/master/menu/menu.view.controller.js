/**=========================================================
 * Module: MasterMenuViewController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('MenuViewController', MenuViewController);

	function MenuViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.menu = ($stateParams.menu != undefined) ? $stateParams.menu : {};
		vm.submitted = false;
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if(formValid){
				RequestService.modalConfirmation().then(function(result) {
					$scope.saveData();
					vm.loading = true;
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formMenu[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.saveData = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/menu/menu/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/menu/menu/update';
             }
			RequestService.doPOSTJSON(url, vm.menu).then(function success(data) {
				if (data != undefined) {
					if (data.isValid != undefined) {
						if (data.errorNama != undefined) {							
							$scope.formMenu.name.$setValidity('name', false);
						}
						vm.loading = false;
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-menu');
					}
				}
			},
			function error(response) {				
				vm.loading = false;
				RequestService.informInternalError();
			});
        }
		
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-menu');
        }
	}
	MenuViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$log'];
})();
		