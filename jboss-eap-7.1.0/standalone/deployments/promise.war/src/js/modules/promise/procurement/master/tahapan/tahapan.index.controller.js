/**=========================================================
 * Module: TahapanController
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('TahapanController', TahapanController);

    function TahapanController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $state, $stateParams, $log) {
        var vm = this;

        $scope.tahapanList = function () {
            vm.loading = true;
           
            RequestService.doGET('/procurement/master/tahapan/get-list').then(function success(data) {
	        	vm.tahapanList = data;
		        vm.loading = false;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
        
       $scope.tahapanList();

       $scope.add = function () {
    	   $state.go('app.promise.procurement-master-tahapan-view',{
    		   toDo: "Add"
       		}); 
    	  }

        $scope.edit = function (tahapan) {
        	 $state.go('app.promise.procurement-master-tahapan-view',{
        		 tahapan: tahapan,
        		 toDo:"Edit"
             });
        }

        $scope.del = function(tahapanId) {
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading = true;
				var url = '/procurement/master/tahapan/delete/';
				var data = {};
				data.id = tahapanId;
				RequestService.doPOST(url, data)
					.success(function(data) {
						RequestService.informDeleteSuccess();
						vm.loading = false;
						$scope.tahapanList();
					}).error(function(data, status, headers, config) {
	        			RequestService.informInternalError();
	        			vm.loading = false;
					})

			});
		}
    }

    TahapanController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state','$stateParams', '$log'];

})();
