(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SistemEvaluasiPenawaranController', SistemEvaluasiPenawaranController);

    function SistemEvaluasiPenawaranController($scope, $http, $rootScope, $resource, $location,RequestService,$state) {
        var vm = this;
        
        $scope.getSistemEvaluasiPenawaranList = function () {
			vm.loading = true;
			
			RequestService.doGET('/procurement/master/SistemEvaluasiPenawaranServices/getSistemEvaluasiPenawaranList')
				.then(function success(data) {
					vm.sistemEvaluasiPenawaranList = data;
					vm.loading = false;
				}, function error(response) { 
					vm.loading = false;
			});
		}
        $scope.getSistemEvaluasiPenawaranList();

        $scope.add = function () {
        	$state.go('app.promise.procurement-master-sistemevaluasipenawaran-view',{
				toDo:'add'
			});
        }

        $scope.edit = function (sistemEvaluasiPenawaran) {
        	$state.go('app.promise.procurement-master-sistemevaluasipenawaran-view',{
        		sistemEvaluasiPenawaran:sistemEvaluasiPenawaran,
				toDo:'edit'
			});
        }

        $scope.del = function (sistemEvaluasiPenawaran) {
            RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data sistem evaluasi penawaran ?").then(function (result) {
				RequestService.doPOST('/procurement/master/SistemEvaluasiPenawaranServices/delete/',sistemEvaluasiPenawaran)
					.then(function success(data) {
						RequestService.informDeleteSuccess();
						$scope.getSistemEvaluasiPenawaranList();
					}, function error(response) { 
					    RequestService.informError("Terjadi Kesalahan Pada System");
					});
			});
        }
    }

    SistemEvaluasiPenawaranController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location','RequestService','$state'];

})();
