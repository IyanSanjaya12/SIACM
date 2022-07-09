(function () {
	'use strict';

	angular
		.module('naut')
		.controller('ParameterViewController', ParameterViewController);

	function ParameterViewController($scope, $http, $rootScope, $resource,$stateParams,$state,RequestService) {
		var vm = this;
		
		vm.parameter = ($stateParams.parameter != undefined) ? $stateParams.parameter : {};
		
		vm.booleanValues = ['TRUE', 'FALSE'];
		vm.submitted = false;
		function isNumeric(n) {
			  return !isNaN(parseFloat(n)) && isFinite(n);
		}
		
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
			$scope.formParameter[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.saveData = function () {
			var url = '/procurement/master/parameter/update'
			RequestService.doPOSTJSON(url, vm.parameter)
				.then(function success(data){
					if (data.errorNama != undefined) {
						$scope.formParameter.nilai.$setValidity('nilai', false);
						vm.loading = false;
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-parameter');
					}
					
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
		}
	
		$scope.back = function () {
			$state.go('app.promise.procurement-master-parameter');
		};
	
	}

	ParameterViewController.$inject = ['$scope', '$http', '$rootScope', '$resource','$stateParams','$state','RequestService'];

})();