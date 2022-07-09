(function() {
	'use strict';

	angular.module('naut').controller('MasterApprovalController',
			MasterApprovalController);

	function MasterApprovalController(RequestService, $scope, $state, $log) {

		var vm = this;
		vm.loading = true;
		$scope.getMasterApprovalList = function() {

			RequestService
					.doGET(
							'/procurement/approval/get-master-approval-list-detil')
					.then(
							function successCallback(data) {
								vm.masterApprovalList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		}

		$scope.getMasterApprovalList();

		$scope.add = function () {
			$state.go('app.promise.procurement-master-approval-view',{
				toDo:'add'
			});
		};
		
		$scope.edit = function (approval) {
			$state.go('app.promise.procurement-master-approval-view',{
				approval:approval,
				toDo:'edit'
			});
		};
		
		
		$scope.del = function (approval) {
			
			var url = '/procurement/approval/delete/';
			
			RequestService.deleteModalConfirmation().then(function (result) {
				vm.loading = true;
				RequestService.doPOST(url, approval)
					.success(function (data) {
	            		RequestService.informDeleteSuccess();
	            		vm.loading = false;
	            		$scope.getMasterApprovalList();
	               }).error(function(data, status, headers, config) {
	        			RequestService.informInternalError();
	        			vm.loading = false;
					})
				
			});
		};

		

		
		
	}

	MasterApprovalController.$inject = [ 'RequestService','$scope','$state', '$log'];

})();