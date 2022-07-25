(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BarangHistoryIndexController', BarangHistoryIndexController);

    function BarangHistoryIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;
        
        $scope.getBarangHistoryList = function () {
            vm.loading = true;
            RequestService.doGET('/master/barangHistory/getBarangHistoryList')
            .then(function success(data) {
				vm.barangHistoryList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.approval = function(barangHistory){
        	$state.go('app.promise.procurement-approval-barang-detail', {
				todo : 'edit',
				barangHistory : barangHistory
			});
        }
        $scope.deleteBarangHistory = function(barangHistory){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/barangHistory/delete', barangHistory).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getBarangHistoryList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getBarangHistoryList();
    }

    BarangHistoryIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
