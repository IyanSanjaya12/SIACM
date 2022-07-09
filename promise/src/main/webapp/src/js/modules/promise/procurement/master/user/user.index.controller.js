(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UserIndexController', UserIndexController);

    function UserIndexController($scope, $state, RequestService) {
        var vm= this;
       
        $scope.getUserList = function () {
           vm.loading = true;
            RequestService.doGET('/procurement/user/get-detail-user-list')
            .then(function success(data) {
				vm.userListDTO = data;
				vm.loading = false;
			},function error(response) {
				RequestService.informError("Terjadi Kesalahan Pada System");
				vm.loading = false;
			});
            }
            
        $scope.getUserList();
        
       
        $scope.add = function () {
            $state.go('app.promise.procurement-master-user-view',{
            	toDo:'add'
            });
        }

        $scope.edit = function (userDTO) {
        	$state.go('app.promise.procurement-master-user-view', {
        		user : userDTO.user,
        		roleUser:userDTO.roleUser,
        		toDo : 'edit'
        	});
       }
       
        $scope.del = function (dataDTO) {
        	var userMasterDTO = {
        		user : dataDTO.user
        	}
        	vm.url = "/procurement/user/delete";
        	RequestService.deleteModalConfirmation().then(function(result) {
        		RequestService.doPOSTJSON(vm.url,userMasterDTO)
        		.then(function success(data) {
        			RequestService.informDeleteSuccess();
    				$scope.getUserList();
		        }, 
		        function error(response) {
		        	$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
		        })	
        	});
	    }
     
    }

    UserIndexController.$inject = ['$scope','$state', 'RequestService'];

})();
