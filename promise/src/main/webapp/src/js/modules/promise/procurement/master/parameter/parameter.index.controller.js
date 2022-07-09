
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('ParameterController', ParameterController);

	function ParameterController($scope, $http, $rootScope, $resource, $location,RequestService,$state) {
		var vm = this;
		
		$scope.getParameter = function () {
			
			vm.loading = true;
			RequestService.doGET('/procurement/master/parameter/get-list')
			.then(function success(data) {
				vm.parameterList = data;
				vm.loading = false;
			}, function error(response) { 
				RequestService.informInternalError();
				vm.loading = false;
		});
		}
		$scope.getParameter();

		
		$scope.edit = function (parameter) {
			$state.go('app.promise.procurement-master-parameter-view',{
				parameter:parameter
			});
		};
	}

	ParameterController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location','RequestService','$state'];

})();