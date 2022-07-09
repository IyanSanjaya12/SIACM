/**=========================================================

 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalPathViewController', ApprovalPathViewController);
    function ApprovalPathViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams, $filter) {

        var vm = this;
        vm.approvalPath = ($stateParams.approvalPath != undefined) ? $stateParams.approvalPath : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};        
        vm.submitted = false;
        
        $scope.getOrganisasiList = function () {
			RequestService.doGET('/procurement/master/approval-path/get-list-organisai-parent')
				.then(function success(data) {
					vm.organisasiParentList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getOrganisasiList();
		
        $scope.save = function(formValid) {
        	vm.submitted = true;      	
        	if(formValid){
        		RequestService.modalConfirmation().then(function(result) {
        				vm.loading = true;
    					$scope.savedata();
    				});
        		}
        	}
        
        $scope.savedata = function () {
			var url = '';
			if(vm.toDo == "add"){
				url = '/procurement/master/approval-path/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/approval-path/update';
            }
			RequestService.doPOSTJSON(url, vm.approvalPath)
				.then(function success(data) {
					if (data != undefined) {	
						if(data.isValidId != undefined) {
							if(data.errorPathId != undefined) {
								$scope.formApprovalPath.approvalPathId.$setValidity('approvalPathId', false);
							}
						} 
						if (data.isValidName != undefined){
							if(data.errorPathName != undefined) {
								$scope.formApprovalPath.approvalPathName.$setValidity('approvalPathName', false);
							}
						}
						vm.loading = false;
					} if (data.errorPathName == undefined && data.errorPathId == undefined) {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-approval-path');		
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
		}
        
        $scope.back = function () {
			$state.go('app.promise.procurement-master-approval-path');
        }
        
        $scope.delValidation = function($event) {
			$scope.formApprovalPath[$event.target.name].$setValidity($event.target.name, true);
		}

    }
    ApprovalPathViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state','$stateParams', '$filter'];

})();