(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MobilIndexController', MobilIndexController);

    function MobilIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getMobilList = function () {
            vm.loading = true;
            RequestService.doGET('/master/mobil/getMobilList')
            .then(function success(data) {
				vm.mobilList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-mobil-add-edit', {
				todo : 'add',
				mobil : null
			});
        }
        $scope.edit = function(mobil){
        	$state.go('app.promise.procurement-master-mobil-add-edit', {
				todo : 'edit',
				mobil : mobil
			});
        }
        $scope.deleteMobil = function(mobil){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/mobil/delete', mobil).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getMobilList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getMobilList();
    }

    MobilIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
