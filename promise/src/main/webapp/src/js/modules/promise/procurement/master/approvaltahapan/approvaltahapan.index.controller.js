(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalTahapanIndexController', ApprovalTahapanIndexController);

    function ApprovalTahapanIndexController(RequestService, $scope, $state) {
    	
        var vm = this;

        /*$scope.approvalTahapanList = function () {
	        $scope.loading = true;
	        $http.get($rootScope.backendAddress + '/procurement/approval/approvaltahapanservices/getapprovaltahapanlist', {
	            ignoreLoadingBar: true
	        }).success(function (data, status, headers, config) {
	            vm.approvalTahapanList = data;
	            $scope.loading = false;
	            
	        }).error(function (data, status, headers, config) {
	            $scope.loading = false;
	        })    
        }
        $scope.approvalTahapanList();*/
        
        $scope.getApprovalTahapanList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/approval/approval-tahapan/get-list')
				.then(function success(data) {
					vm.approvalTahapanDTOList = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
			});
		}
		$scope.getApprovalTahapanList();
	        
		$scope.add = function () {
        	$state.go('app.promise.procurement-master-approvaltahapan-view', {
				toDo: 'Add'
        	});
        }

		$scope.edit = function (approvalTahapan) {
            $state.go('app.promise.procurement-master-approvaltahapan-view', {
            	approvalTahapan: approvalTahapan,
				toDo : 'Edit'
            });
        }
        
		$scope.del = function (approvalTahapan) {
        	
        	var url = "/procurement/approval/approval-tahapan/delete";
        		
            RequestService.deleteModalConfirmation().then(function (result) {
            	vm.loading = true;
                RequestService.doPOST(url, approvalTahapan)
                    .success(function (data) {
                    	RequestService.informDeleteSuccess();
                    	vm.loading = false;
                    	$scope.getApprovalTahapanList();
                    }).error(function(data, status, headers, config) {
	        			RequestService.informInternalError();
	        			vm.loading = false;
					})
            });
        }
        
    }
    
    ApprovalTahapanIndexController.$inject = ['RequestService', '$scope', '$state'];

})();