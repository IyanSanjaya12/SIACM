/**=========================================================
 * Module: Sidebar.menu.controller.js.js
 * Author: Reinhard
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SidebarMenuController', SidebarMenuController);

	function SidebarMenuController($scope, $http, $rootScope, $resource, $state,RequestService) {
		
		var vm = this;
		
		/*$rootScope.userRole.id berisi id yg aktif*/
		/*nilai didapatkan di login.controller.js*/
   	
        vm.userRoleId = $rootScope.userRole.id;
		vm.dashboardSref = $rootScope.userRole.dashboard.state;//state untuk dashboard per role
		
		
		$scope.toggleMenu = function (menuId)
		{
			if ($rootScope.activeModul== menuId)
				{
					$rootScope.activeModul = 0;
				}
			else
				{
				$rootScope.activeModul = menuId;
				}
		}
		$scope.toggleActiveMenu = function (menuId)
		{
			$rootScope.activeMenuId = menuId;
		}
		
		$scope.getMenuListByRole = function () {
			
			$http.get($rootScope.backendAddress + '/procurement/menu/menu/get-tree-by-token', {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.menuList = data;
				$scope.listRoleUser = $rootScope.userRoleList
				$scope.hasPM = false;
				if ($scope.listRoleUser.length > 0) {
                    angular.forEach($scope.listRoleUser, function (value) {       

                        if (value.role.appCode == "PM") {
                            $scope.hasPM = true;
                        }
                    })
                }
			}).error(function (data, status, headers, config) {
			});
		}
		$scope.getMenuListByRole();
		
	}

	SidebarMenuController.$inject = ['$scope', '$http', '$rootScope','$state','RequestService'];

})();