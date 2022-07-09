(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BarangAddEditController', BarangAddEditController);

    function BarangAddEditController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.barang = ($stateParams.barang != undefined) ? $stateParams.barang : null;
        if (angular.equals(vm.todo, 'add')){
        	vm.barang = {
        			nama : '',
        			kode: '',
        			harga:'',
        			jumlah:'',
        			stokMinimal:'',
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
					RequestService.doPOSTJSON('/master/barang/save', vm.barang).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-master-barang-index');
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
        	if(!RequestService.validasiEmpty(vm.barang.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barang.kode)){
        		vm.errorMessageKode = error;
        		valid = false;
        	}else{
        		vm.errorMessageKode = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barang.hargaBeli)){
        		vm.errorMessageHargaBeli = error;
        		valid = false;
        	}else{
        		vm.errorMessageHargaBeli = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barang.harga)){
        		vm.errorMessageHarga = error;
        		valid = false;
        	}else{
        		vm.errorMessageHarga = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barang.jumlah)){
        		vm.errorMessageStok = error;
        		valid = false;
        	}else{
        		vm.errorMessageStok = '';
        	}
        		
        	if(!RequestService.validasiEmpty(vm.barang.stokMinimal)){
        		vm.errorMessageStokMinimal = error;
        		valid = false;
        	}else{
        		vm.errorMessageStokMinimal = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barang.mobil)){
        		vm.errorMessageMobil = error;
        		valid = false;
        	}else{
        		vm.errorMessageMobil = '';
        	}
        	return valid;
        }	
        
        $scope.getMobilList();
        
        $scope.back = function (){
        	$state.go ('app.promise.procurement-master-barang-index');
        }
    }

    BarangAddEditController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
