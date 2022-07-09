(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MobilAddEditController', MobilAddEditController);

    function MobilAddEditController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.mobil = ($stateParams.mobil != undefined) ? $stateParams.mobil : null;
        if (angular.equals(vm.todo, 'add')){
        	vm.mobil = {
        			nama : '',
        			merk: ''
        			
        	}
        }
        
        $scope.save = function (){
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/master/mobil/save', vm.mobil).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-master-mobil-index');
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
        	if(!RequestService.validasiEmpty(vm.mobil.nama)){
        		vm.errorMessageNama = error;
        		valid = false;
        	}else{
        		vm.errorMessageNama = '';
        	}
        	if(!RequestService.validasiEmpty(vm.mobil.merk)){
        		vm.errorMessageMerk = error;
        		valid = false;
        	}
        	return valid;
        }
        
        $scope.back = function(){
        	$state.go('app.promise.procurement-master-mobil-index', {
				todo : 'add',
				mobil : null
			});
        }
        
    }

    MobilAddEditController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService'];

})();
