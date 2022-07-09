/**=========================================================
 * Module: SatuanViewController
 =========================================================*/

(function() {
	'use strict';

	angular.module('naut').controller('SatuanViewController',
			SatuanViewController);

	function SatuanViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {
		
		var vm = this;
		vm.satuan = ($stateParams.satuan != undefined) ? $stateParams.satuan : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		
		vm.submitted = false;
		
		$scope.delValidation = function($event) {
			$scope.formSatuan[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if (formValid){
				RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();		
				});																		
			}
		}

		$scope.saveData = function() {
			vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/master/satuan/insert';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/master/satuan/update';
			}

			RequestService.doPOSTJSON(vm.url, vm.satuan)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if(data.errorNama != undefined) {
	            				$scope.formSatuan.namaSatuan.$setValidity('namaSatuan', false);
		            		}
							vm.loading=false;
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-satuan');
						}
	
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});

		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-satuan');
		};
		
	}
	
	SatuanViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state', '$stateParams' ];

})();