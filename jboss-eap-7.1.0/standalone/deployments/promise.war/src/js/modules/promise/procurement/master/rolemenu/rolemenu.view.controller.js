/**=========================================================
 * Module: EditRoleMenuController.js
 * Controller for Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RoleMenuViewController', RoleMenuViewController);
    function RoleMenuViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {

        var vm = this;
        vm.roleMenu = ($stateParams.roleMenu != undefined) ? $stateParams.roleMenu : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};        
        vm.submitted = false;
        
        $scope.getRoleList = function () {
			RequestService.doGET('/procurement/master/role/get-list')
				.then(function success(data) {
					vm.roleList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getRoleList();
		
		$scope.getMenuList = function () {
			RequestService.doGET('/procurement/menu/menu/get-list')
				.then(function success(data) {
					vm.menuList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getMenuList();
		
        $scope.save = function(formValid) {
        	vm.submitted = true;      	
        	if(formValid){
        		RequestService.modalConfirmation().then(function(result) {
        				vm.loading = true;
    					$scope.savedata();
    				});
        		}
        	}
        
        $scope.delValidation = function($event) {
			$scope.formRoleMenu[$event.target.name].$setValidity($event.target.name, true);
		}
      
		$scope.savedata = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/menu/role-menu/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/menu/role-menu/update';
            }
			RequestService.doPOSTJSON(url, vm.roleMenu)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorMenuNama != undefined) {
								$scope.formRoleMenu.role.$setValidity('role', false);
								$scope.formRoleMenu.menu.$setValidity('menu', false);
							}
							vm.loading = false;
						} else {
							RequestService.informSaveSuccess();
							if(vm.toDo == "Add"){
								$state.go('app.promise.procurement-master-rolemenu');
				            }
							else if(vm.toDo == "Edit"){
								$state.go('app.promise.procurement-master-rolemenu-detail');
				             }
						}
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
		}
		
		$scope.back = function () {
			if(vm.toDo == "Add"){
				$state.go('app.promise.procurement-master-rolemenu');
            }
			else if(vm.toDo == "Edit"){
				$state.go('app.promise.procurement-master-rolemenu-detail');
            }
        }

    }
    RoleMenuViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state','$stateParams'];

})();