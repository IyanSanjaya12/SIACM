/**=========================================================
 * Module: MasterRoleMenuController.js
 * Controller for Master Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RoleMenuController', RoleMenuController);

    function RoleMenuController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
    	
        var vm = this;
        
        $scope.getRoleMenuList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/role/get-list')
				.then(function success(data) {
					vm.roleMenuList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
					vm.loading = false;
			});
		}
		$scope.getRoleMenuList();
        
        $scope.add = function () {
			$state.go('app.promise.procurement-master-rolemenu-view', {
				toDo: 'Add'
        	});
		};
		
		$scope.detail = function (roleMenu) {
			$rootScope.roleMenuDetail = roleMenu;
			$state.go('app.promise.procurement-master-rolemenu-detail',{
				roleMenu : roleMenu,
				toDo : 'Detail'
			});
		};
		
    }

    RoleMenuController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();