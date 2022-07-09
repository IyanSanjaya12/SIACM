(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AfcoController', AfcoController);

    function AfcoController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state) {
        var vm = this;
        
        $scope.getAfcoList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/AfcoServices/getAfcoList')
				.then(function success(data) {
					vm.afcoList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
					vm.loading = false;
			});
		}
		$scope.getAfcoList();
		
		$scope.add = function () {
			$state.go('app.promise.procurement-master-afco-view', {
        	toDo: 'Add'
        	});
		};
		
		$scope.edit = function (afco) {
			$state.go('app.promise.procurement-master-afco-view',{
				afco : afco,
				toDo : 'Edit'
			});
		};
		
		$scope.del = function (afcoId) {
			var url = '/procurement/master/AfcoServices/delete';
			var data = {};
			
			data.id = afcoId;
			RequestService.modalConfirmation('Apakah anda yakin ingin menghapus data afco').then(function(result) {
	            RequestService.doPOST(url,data.id)
		            .success(function (data) {
		               RequestService.informDeleteSuccess();
		               		$scope.getAfcoList();
		               }).error(function (data, status, headers, config) {
		            	   	RequestService.informError("Terjadi Kesalahan Pada System");
		               });
	        });
		}
		
		
    }

    AfcoController.$inject = ['ModalService','RequestService','$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();
