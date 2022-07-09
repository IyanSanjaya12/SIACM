(function () {
	'use strict';

	angular
		.module('naut')
		.controller('BankViewController', BankViewController);

	function BankViewController($log, $scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, ModalService ) {
		var vm = this;
		vm.bank={};
		vm.list={};

		vm.submitted = false;
		$scope.delValidation = function($event) {
			$scope.formBank[$event.target.name].$setValidity($event.target.name, true);
		}
		
		vm.toDo = $stateParams.toDo;
		vm.bank.id = ($stateParams.bank != undefined) ? $stateParams.bank : {};		
		
		if (vm.toDo == "edit") {
        	RequestService.doGET(
					'/procurement/master/bank/get-edit-bank-wilayah-by-id/'+vm.bank.id)
					.then(function success(data) {
						vm.bank = data.bank;
						vm.list.propinsi = data.propinsi[0];
						vm.list.kota = data.kota[0];
						$scope.getKotaList(data.propinsi[0].lokasi_propinsi);
						vm.loading = false;
					}, function error(response) {
						vm.loading = false;
					});
		}

		$scope.getProvinsiList = function(){
			
			RequestService.doGET('/procurement/master/bank/get-propinsi-list')
			.then(function success(data) {
				vm.provinsiList  = data;
				vm.loading = false;
			}, function error(response) {
				vm.loading = false;
			});
			
		}
		$scope.getProvinsiList();
		
		$scope.getKotaList = function(lokasiId){
			
			RequestService.doGET('/procurement/master/bank/get-kota-list/'+lokasiId)
			.then(function success(data) {
				vm.kotaList  = data;
				vm.loading = false;
			}, function error(response) {
				vm.loading = false;
			});
			
		}
		
		
		$scope.save = function () {
			vm.submitted = true;
			if($scope.validateForm()) {   			
				RequestService
				.modalConfirmation()
				.then(
						function(result) {
						vm.bank.propinsi=vm.list.propinsi.lokasi_nama;
						vm.bank.kota=vm.list.kota.lokasi_nama;
		        		$scope.saveData();
					        	
					});
        	}
			
        }

		$scope.pilihKota = function () {
			vm.list.kota = null;
			$scope.getKotaList(vm.list.propinsi.lokasi_propinsi);
		}
		
        $scope.validateForm = function () {
        	vm.isValid = true;
        	vm.errorMessageNamaBank = '';
        	vm.errorMessageKodeBank= '';
        	vm.errorMessageNamaKantor= '';
        	vm.errorMessageAlamat= '';
        	vm.errorMessageKota= '';
        	vm.errorMessageProvinsi= '';
        	vm.errorMessageKodePos= '';
        	vm.errorMessageTelepon= '';
        	vm.errorMessageStatusKantor= '';
	          
            if (typeof vm.bank.namaBank == 'undefined' || vm.bank.namaBank == '') {
            	vm.errorMessageNamaBank = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.bank.kodeBank == 'undefined' || vm.bank.kodeBank == null) {
                vm.errorMessageKodeBank = 'template.error.field_kosong';
                  vm.isValid = false;
              }else{
                if(vm.bank.kodeBank.length !==3)
                {
                  vm.errorMessageKodeBank = 'promise.procurement.master.bank.error.kode_digit';
                      vm.isValid = false;
                }  
                
              }
           
            if (typeof vm.bank.namaKantor == 'undefined' || vm.bank.namaKantor == '') {
            	vm.errorMessageNamaKantor = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.bank.alamat == 'undefined' || vm.bank.alamat == '') {
            	vm.errorMessageAlamat = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.list.kota == 'undefined' || vm.list.kota =='') {
            	vm.errorMessageKota = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.list.propinsi == 'undefined' || vm.list.propinsi =='') {
            	vm.errorMessageProvinsi = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.bank.kodePos == 'undefined' || vm.bank.kodePos == '') {
            	vm.errorMessageKodePos = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.bank.telepon == 'undefined' || vm.bank.telepon == '') {
            	vm.errorMessageTelepon = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.bank.statusKantor == 'undefined') {
            	vm.errorMessageStatusKantor = 'template.error.field_kosong';
                vm.isValid = false;
            }else{
            	return vm.isValid;
            }
            
        }

        $scope.saveData = function () {
        	
        	var url = ""
    			
    			if (vm.toDo == "add") {
    				vm.bank.id=null;
    				
    				url = "/procurement/master/bank/insert";	
    			} else if (vm.toDo == "edit") {				
    				url = "/procurement/master/bank/update";
    			}
    			
    			RequestService.doPOSTJSON(url, vm.bank)
                .then(function success(data) {
                 if(data != undefined) {
                	 RequestService.informSaveSuccess();
                     $state.go('app.promise.procurement-master-bank');
                 }
                }, 
                function error(response) {
                	RequestService.informInternalError();
                	vm.loading = false;
                });
        }
        
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-bank');
        }
        
        
        
	}

	BankViewController.$inject = ['$log','$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', 'ModalService'];

})();