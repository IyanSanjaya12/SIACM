/**=========================================================
 * Module: SatuanController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SatuanController', SatuanController);

	function SatuanController($http, $rootScope, $resource, $location,$state,RequestService,$scope) {
		
		var vm = this;
		
		$scope.getSatuanList = function () {
			vm.loading = true;
			
			RequestService.doGET('/procurement/master/satuan/get-list')
				.then(function success(data) {
					vm.satuanList = data;
					vm.loading = false;
				}, function error(response) { 
					RequestService.informInternalError();
        			vm.loading = false;
			});
		}
		
		$scope.getSatuanList();
		
		$scope.add = function () {
			$state.go('app.promise.procurement-master-satuan-view',{
				toDo:'add'
			});
		};
		
		$scope.edit = function (satuan) {
			$state.go('app.promise.procurement-master-satuan-view',{
				satuan:satuan,
				toDo:'edit'
			});
		};

		$scope.del = function (satuan) {
			
			var url = '/procurement/master/satuan/delete';
			
			RequestService.deleteModalConfirmation().then(function (result) {
				vm.loading = true;
				RequestService.doPOST(url, satuan)
					.success(function (data) {
	            		RequestService.informDeleteSuccess();
	            		vm.loading = false;
	            		$scope.getSatuanList();
	               }).error(function (data, status, headers, config) {
	            	   RequestService.informInternalError();
	        			vm.loading = false;
	               });
				
			});
		};
		
	}

	SatuanController.$inject = ['$http', '$rootScope', '$resource', '$location','$state','RequestService','$scope'];

})();