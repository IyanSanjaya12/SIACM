/**=========================================================
 * Module: RoleMenuController.js
 * Controller for Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MenuAksiViewController', MenuAksiViewController);
    
    function MenuAksiViewController($scope, $http, $rootScope, $resource, $location, toaster, $modal,RequestService,$stateParams,$state) {

        var vm = this;
        vm.menuAksi = ($stateParams.menuAksi != undefined) ? $stateParams.menuAksi : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.submitted = false;
  
        $scope.getMenu = function () {
        	vm.loading = false;
			RequestService.doGET('/procurement/menu/menu/get-list')
				.then(function success(data) {
					vm.menuList = data;
				}, function error(response) { 
					RequestService.informInternalError();
					vm.loading = false;
			});
		}
        $scope.getMenu();
        
  
        $scope.getAksi = function (search) {
        	vm.loading = true;
            if(search.length==0)
                search="a";
            
            RequestService.doGET('/procurement/menu/aksi/search-path/50/'+search)
				.then(function success(data) {
					vm.aksiList = data;
				}, function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
			});
        }
        
        $scope.save = function(formValid) {
        	vm.submitted = true;
        	if(formValid){
        		RequestService.modalConfirmation().then(function(result) {	
        				vm.loading = true;
    					$scope.saveData();
    				});
    			}
    		};
			
    		$scope.delValidation = function($event) {
    			$scope.formMenuaksi[$event.target.name].$setValidity($event.target.name, true);
    		}

		$scope.saveData = function() {
			vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/menu/menu-aksi/insert';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/menu/menu-aksi/update';
			}

			RequestService.doPOSTJSON(vm.url, vm.menuAksi)
				.then(function success(data) {
					if (data.errorNama != undefined) {
						vm.loading = false;
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-menuaksi');
					}
				
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});

		}
        

        $scope.back = function () {
        	$state.go('app.promise.procurement-master-menuaksi');
        };

    }
    MenuAksiViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal','RequestService','$stateParams','$state'];

})();