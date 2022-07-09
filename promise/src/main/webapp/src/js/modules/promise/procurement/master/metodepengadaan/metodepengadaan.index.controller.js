/**=========================================================
 * Module: MetodePengadaanController
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('MetodePengadaanIndexController', MetodePengadaanIndexController);

    function MetodePengadaanIndexController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $state, $stateParams) {
        var vm = this;
        
        $scope.metodePengadaanList = function () {
	        vm.loading = true;
	        
	        RequestService.doGET('/procurement/master/metodePengadaanServices/getMetodePengadaanList')
	        	.then(function success(data) {
	        		vm.metodePengadaanList = data;
		            vm.loading = false;
	        }, function error(response) {
	        		RequestService.informError("Terjadi Kesalahan Pada System");
	        	 	vm.loading = false;
	        });
        }
        $scope.metodePengadaanList();
		
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-metodePengadaan-view',{
        		toDo: "Add"
        	});
        }
		
        $scope.edit = function(metodePengadaan){
			 $state.go('app.promise.procurement-master-metodePengadaan-view',{
				 metodePengadaan: metodePengadaan,
				 toDo:"Edit"
			 });
		 }
        
        $scope.del = function(metodePengadaanId){
			vm.url = '/procurement/master/metodePengadaanServices/delete';
			var data = {
				id : metodePengadaanId
			};
			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data metode pengadaan?").then(function (result) {
				RequestService.doPOST(vm.url, data)
					.success(function (data){
	                	RequestService.informDeleteSuccess();
	                	$scope.metodePengadaanList();
	                }).error(function (data, status, headers, config) {
	    				RequestService.informError("Terjadi Kesalahan Pada System");
		            })
			});
        }
    }

    MetodePengadaanIndexController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state','$stateParams'];

})();