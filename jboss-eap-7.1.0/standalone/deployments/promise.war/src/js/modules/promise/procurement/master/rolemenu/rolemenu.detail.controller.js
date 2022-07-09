/**=========================================================
 * Module: MasterRoleMenuController.js
 * Controller for Master Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DetailRoleMenuController', DetailRoleMenuController);

    function DetailRoleMenuController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
        
    	var vm = this;
        
        if (typeof $rootScope.roleMenuDetail !== 'undefined') {
            $scope.role = $rootScope.roleMenuDetail;
            //console.log('Data  =' +JSON.stringify($scope.roleMenu));
        } else {
            console.log('INFO Error');
        }
        
        $scope.getRoleMenuList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/menu/role-menu/get-list-by-role/' + $scope.role.id)
				.then(function success(data) {
					vm.roleMenuList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informInternalError();
					vm.loading = false;
			});
		}
		$scope.getRoleMenuList();
		
		$scope.edit = function (roleMenu) {
			$state.go('app.promise.procurement-master-rolemenu-view',{
				roleMenu : roleMenu,
				toDo : 'Edit'
			});
		};
		
		$scope.del = function (roleMenuId) {
			vm.loading = true;
			var url = '/procurement/menu/role-menu/delete';
			var data = {};
			
			data.id = roleMenuId;
			RequestService.deleteModalConfirmation().then(function(result) {
	            RequestService.doPOST(url,data).success(function (data) {
	               RequestService.informDeleteSuccess();
	               		$scope.getRoleMenuList();
	               		$scope.loading = false;
	               }).error(function (data, status, headers, config) {
	            	   RequestService.informInternalError();
	            	   vm.loading = false;
	               });
	        });
		}
		
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-rolemenu');
        }
        
       
    }

    DetailRoleMenuController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();