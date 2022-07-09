(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PositionAddEditController', PositionAddEditController);

    function PositionAddEditController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.position = ($stateParams.position != undefined) ? $stateParams.position : null;
        
        if (angular.equals(vm.todo, 'add')){
        	vm.position = {
        			nama : '',
        			kode: '',
        			keterangan:''
        			
        	}
        }
        
        $scope.save = function (){
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/master/position/save', vm.position).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-master-position-index');
					}, function error(response) {
						ModalService.closeModalInformation();
						$log.info("proses gagal");
						RequestService.informError("Terjadi Kesalahan Pada System");
					});
    			});
        	}
        }
        
        $scope.checkValidasi = function(){
        	var error = 'silahkan isi kolom ini!';
        	var valid = true;
        	if(!RequestService.validasiEmpty(vm.position.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.position.kode)){
        		vm.errorMessageKode = error;
        		valid = false;
        	}else{
        		vm.errorMessageKode = '';
        	}
        	if(!RequestService.validasiEmpty(vm.position.keterangan)){
        		vm.errorMessageKeterangan = error;
        		valid = false;
        	}else{
        		vm.errorMessageKeterangan = '';
        	}
        	return valid;
        }
        
        $scope.back = function (){
        	$state.go ('app.promise.procurement-master-position-index');
        }
        
    }

    PositionAddEditController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
