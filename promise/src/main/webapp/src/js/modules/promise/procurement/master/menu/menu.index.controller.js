/**=========================================================
 * Module: MasterMenuController
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MenuIndexController', MenuIndexController);

    function MenuIndexController($scope, $http, $rootScope, $resource, $location, $modal, $state, RequestService) {
        var vm = this;

        /*vm.go = function (path) {
            $location.path(path);
        };*/
        
        $scope.getMenuList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/menu/menu/get-list')
				.then(function success(data) {
					vm.menuList = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
			});
		}
		$scope.getMenuList();
        
		$scope.add = function () {
			$state.go('app.promise.procurement-master-menu-view', {
				toDo: 'Add'
        	});
		};
		
		$scope.edit = function (menu) {
			$state.go('app.promise.procurement-master-menu-view',{
				menu : menu,
				toDo : 'Edit'
			});
		};
		
		$scope.del = function (menuId) {
			vm.loading = true;
			var url = '/procurement/menu/menu/delete';
			var data = {};
			
			data.id = menuId;
			RequestService.deleteModalConfirmation().then(function(result) {
				RequestService.doPOST(url,data)
	            	.success(function (data) {
	            		RequestService.informDeleteSuccess();
	            		vm.loading = false;
	            		$scope.getMenuList();
	            	}).error(function (data, status, headers, config) {
	            		RequestService.informInternalError();
	            		vm.loading = false;
	            	});
	        });
		}
		
    }

    MenuIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state', 'RequestService'];

})();