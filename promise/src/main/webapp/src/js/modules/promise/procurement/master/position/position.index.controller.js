(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PositionIndexController', PositionIndexController);

    function PositionIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getPositionList = function () {
            vm.loading = true;
            RequestService.doGET('/master/position/getPositionList')
            .then(function success(data) {
				vm.positionList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.add = function(){
        	$state.go('app.promise.procurement-master-position-add-edit', {
				todo : 'add',
				position : null
			});
        }
        $scope.edit = function(position){
        	$state.go('app.promise.procurement-master-position-add-edit', {
				todo : 'edit',
				position : position
			});
        }
        $scope.deletePosition = function(position){
        	RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doPOSTJSON('/master/position/delete', position).then(function success(data) {
					ModalService.closeModalInformation();
					RequestService.informSaveSuccess();
					$scope.getPositionList();
				}, function error(response) {
					ModalService.closeModalInformation();
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
        
        $scope.getPositionList();
    }

    PositionIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
