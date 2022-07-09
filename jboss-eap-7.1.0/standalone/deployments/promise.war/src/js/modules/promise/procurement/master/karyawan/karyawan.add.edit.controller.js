(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KaryawanAddEditController', KaryawanAddEditController);

    function KaryawanAddEditController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.karyawan = ($stateParams.karyawan != undefined) ? $stateParams.karyawan : null;
        
        if (angular.equals(vm.todo, 'add')){
        	vm.karyawan = {
        			nama : '',
        			nik: '',
        			alamat:'',
        			telepon:'',
        			position:{}
        			
        	}
        }
        
        $scope.getJabatanList = function () {
            vm.loading = true;
            RequestService.doGET('/master/position/getPositionList')
            .then(function success(data) {
				vm.jabatanList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.getJabatanList();
        
        $scope.save = function (){
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/master/karyawan/save', vm.karyawan).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-master-karyawan-index');
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
        	if(!RequestService.validasiEmpty(vm.karyawan.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.karyawan.nik)){
        		vm.errorMessageNik = error;
        		valid = false;
        	}else{
        		vm.errorMessageNik = '';
        	}
        	if(!RequestService.validasiEmpty(vm.karyawan.alamat)){
        		vm.errorMessageAlamat = error;
        		valid = false;
        	}else{
        		vm.errorMessageAlamat = '';
        	}
        	if(!RequestService.validasiEmpty(vm.karyawan.telepon)){
        		vm.errorMessageTelepon = error;
        		valid = false;
        	}else{
        		vm.errorMessageTelepon = '';
        	}
        	if(!RequestService.validasiEmpty(vm.karyawan.position)){
        		vm.errorMessageJabatan = error;
        		valid = false;
        	}else{
        		vm.errorMessageJabatan = '';
        	}
        	return valid;
        }
        
        $scope.back = function (){
        	$state.go ('app.promise.procurement-master-karyawan-index');
        }
    }

    KaryawanAddEditController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
