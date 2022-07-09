(function(){
'use strict';
angular
    .module('naut')
    .controller('addressBookViewController', addressBookViewController);
    function addressBookViewController (RequestService, $state, $stateParams, $rootScope, $http, $scope, $log) {

            var vm = this;
            vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
    		/*vm.addressBook = ($stateParams.addressBook != undefined) ? $stateParams.addressBook : {};*/
    		
            vm.addressBook = {};
            vm.addressTemp = null;
            if ($stateParams.dataAddressBook != undefined) {
                vm.addressBook = $stateParams.dataAddressBook;
                vm.addressTemp = vm.addressBook.addressLabel;
            }
            
            $scope.getProvinsi = function(){
            	RequestService.doGET("/procurement/master/AddressBookServices/getAllProvinsi").then(function success(data) {
            		vm.provinsiList = data;
            	},function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
					
    			});
            }
            $scope.getProvinsi();
            
            $scope.getOrganisasiList = function () {
    			RequestService.doGET('/procurement/master/organisasi/get-organisasi-level2-list').then(function (data) {
    				vm.organisasiList=data;
    			});
    		}
            $scope.getOrganisasiList();
            
            /*$scope.getOrganisasi = function () {			
				RequestService.doGET('/procurement/master/organisasi/get-organisasi-afco-by-token')
				.then(function success(data) {
					vm.addressBook.organisasi = {
                            id: data.id
                        };
					
				}, function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
			
			});      
        } 
        $scope.getOrganisasi();*/
            
            $scope.save = function() {
            	if ($scope.validateForm()) {
            		if(vm.addressTemp != null ){
            			if(!angular.equals(vm.addressTemp, vm.addressBook.addressLabel)){
            				RequestService.doGET("/procurement/master/AddressBookServices/getAddressBookByname/"+vm.addressBook.addressLabel).then(function success(data) {
                    			if(data.length == 0){
                    				RequestService.modalConfirmation().then(function(result) {
                            			$scope.saveData();
                            		});
                    			}else{
                    				vm.errorMessageLabel = "Name has been used";
                    			}
                    		}); 
            			}else{
            				RequestService.modalConfirmation().then(function(result) {
                    			$scope.saveData();
                    		});
            			}
            		}else{
            			RequestService.doGET("/procurement/master/AddressBookServices/getAddressBookByname/"+vm.addressBook.addressLabel).then(function success(data) {
                			if(data.length == 0){
                				RequestService.modalConfirmation().then(function(result) {
                        			$scope.saveData();
                        		});
                			}else{
                				vm.errorMessageLabel = "Name has been used";
                			}
                		}); 
            		}
            	}
    		}
            
            $scope.validateForm = function () {
            	var isValid = true;
            	
            	vm.errorMessageLabel = '';
            	vm.errorMessageNamaLengkap = '';
            	vm.errorMessageTelepon = '';
            	vm.errorMessagefax = '';
            	vm.errorMessageJalan = '';
            	vm.errorMessageKota='';
            	vm.errorMessageDistrict='';
            	vm.errorMessageSubDistrict='';
            	vm.errorMessageKodePos='';
            	vm.errorMessageProvinsi = '';
            	vm.errorMessageOrg = '';
            	
            	if (typeof vm.addressBook.addressLabel == 'null' || vm.addressBook.addressLabel == '' || vm.addressBook.addressLabel == null) {
            		isValid = false;
            			vm.errorMessageLabel = "template.error.field_kosong";
            	}else {
            		isValid = $scope.cekValidSame(vm.addressBook.addressLabel);
            	}
            	if (typeof vm.addressBook.fullName == 'null'|| vm.addressBook.fullName == '' || vm.addressBook.fullName == null) {
            		isValid = false;
            			vm.errorMessageNamaLengkap = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.telephone == 'null' || vm.addressBook.telephone == '' || vm.addressBook.telephone == null) {
            		isValid = false;
            			vm.errorMessageTelepon = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.fax == 'null' || vm.addressBook.fax == '' || vm.addressBook.fax == null) {
            		isValid = false;
            			vm.errorMessageFax = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.streetAddress == 'null' || vm.addressBook.streetAddress == '' || vm.addressBook.streetAddress == null) {
            		isValid = false;
            			vm.errorMessageJalan = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.city == 'null' || vm.addressBook.city == '' || vm.addressBook.city == null) {
            		isValid = false;
            			vm.errorMessageKota = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.district== 'null' || vm.addressBook.district == '' || vm.addressBook.district == null) {
            		isValid = false;
            			vm.errorMessageDistrict = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.subDistrict== 'null' || vm.addressBook.subDistrict == '' || vm.addressBook.subDistrict == null) {
            		isValid = false;
            			vm.errorMessageSubDistrict = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.postalCode == 'null' || vm.addressBook.postalCode == '' || vm.addressBook.postalCode == null) {
            		isValid = false;
            			vm.errorMessageKodePos = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.provinsi == 'null' || vm.addressBook.provinsi == '' || vm.addressBook.provinsi == null) {
            		isValid = false;
            			vm.errorMessageProvinsi = "template.error.field_kosong";
            	}
            	if (typeof vm.addressBook.organisasi == 'null' || vm.addressBook.organisasi == '' || vm.addressBook.organisasi == null) {
            		isValid = false;
            			vm.errorMessageOrg = "template.error.field_kosong";
            	}
            	
            	return isValid;
            	
            	
            }

            $scope.saveData = function () {
            	if (vm.addressBook.provinsi.id == undefined){
            		var obj = JSON.parse(vm.addressBook.provinsi);
            		vm.addressBook.provinsi = obj;            		
            	}
            	if (vm.toDo == "Add") {
    				vm.url = "/procurement/master/AddressBookServices/insert"
    			} else if (vm.toDo == "Edit"){
    				vm.url = "/procurement/master/AddressBookServices/update"
    			}
	            	RequestService.doPOSTJSON(vm.url, vm.addressBook)
					.then(function success(data) {
						RequestService.informSaveSuccess();
						$state.go ('app.promise.procurement-addressbook-index');
					}, function error(response) {
						$log.info("proses gagal");
						RequestService.informError("Terjadi Kesalahan Pada System");
					});
            }
            
            $scope.cekValidSame = function(value){
            	var valid = true;
            	if(vm.toDo != "Edit"){
            		RequestService.doGET("/procurement/master/AddressBookServices/getAddressBookByname/"+value).then(function success(data) {
            			if(data.length > 0){
            				vm.errorMessageLabel = "Name has been used";
            				valid = false;
            			}else{
            				vm.errorMessageLabel = "";
            				valid = true;
            			}
            		});            		
            	}
            	return valid;
            }
            
            $scope.back = function () {
                $state.go("app.promise.procurement-addressbook-index");
            }
            
    }
  addressBookViewController.$inject = ['RequestService', '$state', '$stateParams', '$rootScope', '$http', '$scope', '$log'];
})();