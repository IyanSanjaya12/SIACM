(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenjualanIndexController', PenjualanIndexController);

    function PenjualanIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getPenjualanList = function () {
            vm.loading = true;
            RequestService.doGET('/transaction/penjualan/getPenjualanList')
            .then(function success(data) {
				vm.penjualanList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-transaction-penjualan-detail', {
				todo : 'add',
				penjualan : null
			});
        }
        $scope.edit = function(penjualan){
        	$state.go('app.promise.procurement-transaction-penjualan-detail', {
				todo : 'edit',
				penjualan : penjualan
			});
        }
        $scope.deletePenjualan = function(penjualan){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/penjualan/delete', penjualan).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getPenjualanList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getPenjualanList();
    }

    PenjualanIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
