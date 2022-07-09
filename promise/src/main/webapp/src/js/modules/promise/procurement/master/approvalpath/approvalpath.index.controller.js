/**=========================================================
 * Module: MasterRoleMenuController.js
 * Controller for Master Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalPathIndexController', ApprovalPathIndexController);

    function ApprovalPathIndexController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
        
    	var vm = this;
        
        $scope.getApprovalpathList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/approval-path/get-list')
				.then(function success(data) {
					vm.approvalPathList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informInternalError();
					vm.loading = false;
			});
		}
		$scope.getApprovalpathList();
		
		 $scope.add = function () {
	            $state.go('app.promise.procurement-master-approvalpath-view', {
					toDo : "add"
				});
	        }
		 
		$scope.edit = function (approvalPath) {
			$state.go('app.promise.procurement-master-approvalpath-view',{
				approvalPath : approvalPath,
				toDo : 'Edit'
			});
		};  
		
		$scope.del = function (pathId) {
        	var url = "/procurement/master/approval-path/delete" 
        	var data = {};
        	
        	data.id = pathId;
        	RequestService.deleteModalConfirmation().then(function(result) {
	            RequestService.doPOST(url,data).success(function (data) {
	               RequestService.informDeleteSuccess();
	               		$scope.getApprovalpathList();
	               		$scope.loading = false;
	               }).error(function (data, status, headers, config) {
	            	   RequestService.informInternalError();
	            	   vm.loading = false;
	               });
	        });
        };
    }

    ApprovalPathIndexController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();