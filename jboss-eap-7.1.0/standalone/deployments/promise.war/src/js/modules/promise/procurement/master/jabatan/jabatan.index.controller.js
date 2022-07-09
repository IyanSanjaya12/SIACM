/**=========================================================
 * Module: JabatanController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('JabatanController', JabatanController);

	function JabatanController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
		var vm = this;
		
		$scope.getJabatanList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/jabatan/get-list')
				.then(function success(data) {
					vm.jabatanList = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
			});
		}
		$scope.getJabatanList();

		$scope.add = function () {
			$state.go('app.promise.procurement-master-jabatan-view', {
				toDo: 'Add'
        	});
		};
		
		$scope.edit = function (jabatan) {
			$state.go('app.promise.procurement-master-jabatan-view',{
				jabatan : jabatan,
				toDo : 'Edit'
			});
		};
		
		$scope.del = function (jabatanId) {
			var url = '/procurement/master/jabatan/delete';
			var data = {};
			
			data.id = jabatanId;
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading=true;
	            RequestService.doPOST(url,data)
	            	.success(function (data) {
	            		RequestService.informDeleteSuccess();
	            		vm.loading = false;
	               		$scope.getJabatanList();
	               }).error(function (data, status, headers, config) {
	            	   RequestService.informInternalError();
	            	   vm.loading = false;
	               });
	        });
		}
	}

	JabatanController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();