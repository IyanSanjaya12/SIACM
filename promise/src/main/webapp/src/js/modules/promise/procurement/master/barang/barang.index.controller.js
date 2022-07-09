(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BarangIndexController', BarangIndexController);

    function BarangIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getBarangList = function () {
            vm.loading = true;
            RequestService.doGET('/master/barang/getBarangList')
            .then(function success(data) {
				vm.barangList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-barang-add-edit', {
				todo : 'add',
				barang : null
			});
        }
        $scope.edit = function(barang){
        	$state.go('app.promise.procurement-master-barang-add-edit', {
				todo : 'edit',
				barang : barang
			});
        }
        $scope.deleteBarang = function(barang){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/barang/delete', barang).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getBarangList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getBarangList();
    }

    BarangIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
