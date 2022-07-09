(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UserAdditionalIndexController', UserAdditionalIndexController);

    function UserAdditionalIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state) {
        var vm = this;
        vm.param = {
    			nama : '',
    			pageNo : 1,
    			pageSize : 5,
    			jmlData : {},
    			dataList : {},
    			rolePlh : '',
    			status : ''

    		};
        
        vm.statusList = [ {
			name : 'Non-Aktif',
			value : 0
		}, {
			name : 'Aktif',
			value : 1
		} ];
        
        $scope.AdditionalUserList = function() {
			vm.loading = true;

			RequestService
					.doPOST(
							'/procurement/master/userAdditional/get-list', vm.param).then(
							function success(data) {
								if (data.data.dataList != undefined
										&& data.data.dataList.length > 0) {
									vm.dataList = data.data.dataList;
									vm.totalItems = data.data.jmlData;
								} else {
									vm.dataList = [];
									vm.totalItems = 0;
								}

								vm.loading = false;
							}, function error(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		}
		$scope.AdditionalUserList();

		$scope.roleListPM = function () {
			RequestService.doGET('/procurement/master/role/getRoleListPm').then(function success(data) {
	        	vm.roleListPM = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		$scope.roleListPM();

		$scope.add = function () {
			$state.go('app.promise.procurement-master-user-additional-view', {
			toDo: 'Add'
			});
		}

	    $scope.edit = function (additionalUser) {
	    $state.go('app.promise.procurement-master-user-additional-view', {
    	   additionalUser: additionalUser,
    	   toDo : 'Edit'
	       });
	    }
	    
	    $scope.setPage = function() {
			$scope.AdditionalUserList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.AdditionalUserList();
		}
    }
    UserAdditionalIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();
