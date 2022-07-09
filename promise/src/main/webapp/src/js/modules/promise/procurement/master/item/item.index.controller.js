(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ItemIndexController', ItemIndexController);

    function ItemIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state) {
        var vm = this;

        $scope.getItemList = function () {
            vm.loading = true;
            RequestService.doGET('/procurement/master/item/get-list')
            .then(function success(data) {
				vm.itemList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.getItemList();
        
        $scope.syncronize = function () {
        	RequestService.informError('belum ada fungsi ya');
        };

       /* $scope.add = function () {
        	$state.go('app.promise.procurement-master-item-view',{
        		toDo: 'Add'
        	})		
        };

        $scope.edit = function (item) {
            $rootScope.dataIndexItem = data;
        	$state.go('app.promise.procurement-master-item-view',{
				item : item,
				toDo : 'Edit'
			})
		};
		
		$scope.del = function (itemId) {
			vm.url = '/procurement/master/item/delete';
			var data ={
        			id : itemId
        			  };
			RequestService.deleteModalConfirmation().then(function(result) {
	            RequestService.doPOST(vm.url,data).success(function (data) {
	            	RequestService.informDeleteSuccess();
	            	vm.loading = false;
               		$scope.getItemList();
	            	}, function error(response) {
	            		RequestService.informInternalError();
		        	 	vm.loading = false;
	                 })
               });
		}*/
		
    }

    ItemIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();
