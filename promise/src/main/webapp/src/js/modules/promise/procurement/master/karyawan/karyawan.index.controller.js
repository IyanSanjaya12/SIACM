(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KaryawanIndexController', KaryawanIndexController);

    function KaryawanIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getKaryawanList = function () {
            vm.loading = true;
            RequestService.doGET('/master/karyawan/getKaryawanList')
            .then(function success(data) {
				vm.karyawanList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-karyawan-add-edit', {
				todo : 'add',
				karyawan : null
			});
        }
        $scope.edit = function(karyawan){
        	$state.go('app.promise.procurement-master-karyawan-add-edit', {
				todo : 'edit',
				karyawan : karyawan
			});
        }
        $scope.deleteKaryawan = function(karyawan){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/karyawan/delete', karyawan).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getKaryawanList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getKaryawanList();
    }

    KaryawanIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
