(function() {
	'use strict';

	angular.module('naut').controller('NegaraViewController',
			NegaraViewController);

	function NegaraViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.negara = ($stateParams.negara != undefined) ? $stateParams.negara : {};
		vm.submitted = false;
		
		$scope.delValidation = function($event) {
			$scope.formNegara[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if (formValid) {
				RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();	
				});
			}
		}

		$scope.saveData = function() {
			var url = '';
			if (vm.toDo == "Add") {
				url = '/procurement/master/negara/insert'
			} else if (vm.toDo == "Edit") {
				url = '/procurement/master/negara/update'
			}
			
			RequestService.doPOSTJSON(url, vm.negara)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
		            		if(data.errorNama != undefined) {
		            			$scope.formNegara.namaNegara.$setValidity('namaNegara', false);
		            		}
		            		
		                	vm.loading = false;
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-negara');
						}
					}
				},
				function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-negara');
		}
	}

	NegaraViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$log' ];

})();