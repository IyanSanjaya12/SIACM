(function () {
    'use strict';
    angular.module('naut')
    .controller('RoleIndexController', RoleIndexController);
    function RoleIndexController($scope, $http, $rootScope, $resource, $location, $filter, $compile, $modal, RequestService,$state,ModalService) {
    	
    	var vm = this;
    	
    	/** load role list **/
    	$scope.roleList = function () {
	        vm.loading = true;	        	        
	        RequestService.doGET('/procurement/master/role/get-list')
	        	.then(function success(data) {
	        		vm.roleList = data;
		            vm.loading = false;
		        }, function error(response) {
        			RequestService.informInternalError();
		        	vm.loading = false;
		        });
        }
        $scope.roleList();
                        
        /** function to add role **/
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-role-view', {
        		toDo: "Add"
        	})
        };
        
        /** function for edit role **/
        $scope.edit = function(role){
        	$state.go('app.promise.procurement-master-role-view',{ 
        		role: role,
        		toDo: "Edit"
        	})
        };
        
        /** function for delete role **/
        $scope.del = function(roleId){
        	vm.loading = true;
        	vm.url = '/procurement/master/role/delete';
        	var data = {
        		id : roleId
        	};
        	
        	RequestService.deleteModalConfirmation().then(function (result) {
        		RequestService.doPOST(vm.url, data).success(function (data){
    				RequestService.informDeleteSuccess();
                 	$scope.roleList();
                 	vm.loading = false;
        		},function error(response) {
        			RequestService.informInternalError();
        			vm.loading = false;
        		})
        	})
        };
    }
    RoleIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$compile', '$modal', 'RequestService','$state','ModalService'];
})();