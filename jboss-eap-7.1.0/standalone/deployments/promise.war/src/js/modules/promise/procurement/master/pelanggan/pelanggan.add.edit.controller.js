(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelangganAddEditController', PelangganAddEditController);

    function PelangganAddEditController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.pelanggan = ($stateParams.pelanggan != undefined) ? $stateParams.pelanggan : null;
        if (angular.equals(vm.todo, 'add')){
        	vm.pelanggan = {
        			nama : '',
        			noPolisi: '',
        			alamat:'',
        			telepon:'',
        			mobil:{}
        			
        	}
        }
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
        $scope.save = function (){
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/master/pelanggan/save', vm.pelanggan).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-master-pelanggan-index');
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
        	if(!RequestService.validasiEmpty(vm.pelanggan.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.pelanggan.noPolisi)){
        		vm.errorMessageNik = error;
        		valid = false;
        	}else{
        		vm.errorMessageNik = '';
        	}
        	if(!RequestService.validasiEmpty(vm.pelanggan.alamat)){
        		vm.errorMessageAlamat = error;
        		valid = false;
        	}else{
        		vm.errorMessageAlamat = '';
        	}
        	if(!RequestService.validasiEmpty(vm.pelanggan.telepon)){
        		vm.errorMessageTelpon = error;
        		valid = false;
        	}else{
        		vm.errorMessageTelpon = '';
        	}
        	if(!RequestService.validasiEmpty(vm.pelanggan.mobil)){
        		vm.errorMessageMobil = error;
        		valid = false;
        	}else{
        		vm.errorMessageMobil = '';
        	}
        	return valid;
        }
        
        $scope.getMobilList();
        
        $scope.back = function (){
        	$state.go ('app.promise.procurement-master-pelanggan-index');
        }
    }

    PelangganAddEditController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
