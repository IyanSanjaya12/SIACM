(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelangganIndexController', PelangganIndexController);

    function PelangganIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getPelangganList = function () {
            vm.loading = true;
            RequestService.doGET('/master/pelanggan/getPelangganList')
            .then(function success(data) {
				vm.pelangganList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-pelanggan-add-edit', {
				todo : 'add',
				pelanggan : null
			});
        }
        $scope.edit = function(pelanggan){
        	$state.go('app.promise.procurement-master-pelanggan-add-edit', {
				todo : 'edit',
				pelanggan : pelanggan
			});
        }
        $scope.deletePelanggan = function(pelanggan){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/pelanggan/delete', pelanggan).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getPelangganList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getPelangganList();
    }

    PelangganIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
