

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RoleJabatanIndexController', RoleJabatanIndexController);

    function RoleJabatanIndexController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
    	
        var vm = this;
        
        $scope.getRoleJabatanList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/role-jabatan/get-list')
				.then(function success(data) {
					vm.roleJabatanList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
					vm.loading = false;
			});
		}
		$scope.getRoleJabatanList();
        
        $scope.add = function () {
			$state.go('app.promise.procurement-master-rolejabatan-view', {
				toDo: 'Add'
        	});
		};
		
		$scope.edit = function (roleJabatan) {
			$state.go('app.promise.procurement-master-rolejabatan-view',{
				roleJabatan : roleJabatan,
				toDo : 'Edit'
			});
		};
		$scope.del = function (roleJabatanId) {
			vm.loading = true;
			var url = '/procurement/master/role-jabatan/delete';
			var data = {};
			
			data.id = roleJabatanId;
			RequestService.deleteModalConfirmation().then(function(result) {
	            RequestService.doPOST(url,data).success(function (data) {
	               RequestService.informDeleteSuccess();
	               		$scope.getRoleJabatanList();
	               		$scope.loading = false;
	               }).error(function (data, status, headers, config) {
	            	   RequestService.informInternalError();
	            	   vm.loading = false;
	               });
	        });
		}
		
    }

    RoleJabatanIndexController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();