(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BarangHistoryDetailController', BarangHistoryDetailController);

    function BarangHistoryDetailController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.barangHistory = ($stateParams.barangHistory != undefined) ? $stateParams.barangHistory : null;
        
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
        $scope.btnSimpan = function(status){
        	vm.barangHistory.status = status;
        	$scope.save();
        }
        $scope.save = function (){
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/master/barangHistory/save', vm.barangHistory).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-approval-barang-index');
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
        	if(!RequestService.validasiEmpty(vm.barangHistory.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barangHistory.kode)){
        		vm.errorMessageKode = error;
        		valid = false;
        	}else{
        		vm.errorMessageKode = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barangHistory.hargaBeli)){
        		vm.errorMessageHargaBeli = error;
        		valid = false;
        	}else{
        		vm.errorMessageHargaBeli = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barangHistory.harga)){
        		vm.errorMessageHarga = error;
        		valid = false;
        	}else{
        		vm.errorMessageHarga = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barangHistory.jumlah)){
        		vm.errorMessageStok = error;
        		valid = false;
        	}else{
        		vm.errorMessageStok = '';
        	}
        		
        	if(!RequestService.validasiEmpty(vm.barangHistory.stokMinimal)){
        		vm.errorMessageStokMinimal = error;
        		valid = false;
        	}else{
        		vm.errorMessageStokMinimal = '';
        	}
        	if(!RequestService.validasiEmpty(vm.barangHistory.mobil)){
        		vm.errorMessageMobil = error;
        		valid = false;
        	}else{
        		vm.errorMessageMobil = '';
        	}
        	return valid;
        }	
        
        $scope.getMobilList();
        
        $scope.back = function (){
        	$state.go ('app.promise.procurement-master-barangHistory-index');
        }
    }

    BarangHistoryDetailController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
