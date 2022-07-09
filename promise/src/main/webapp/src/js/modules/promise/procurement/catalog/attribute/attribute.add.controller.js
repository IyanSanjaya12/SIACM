(function () {
	
	'use strict';

    angular
        .module('naut')
        .controller('AttributeAddController', AttributeAddController);

    function AttributeAddController(RequestService, $state, $stateParams, $scope) {
    	var vm = this;
    	var todo='';
    	vm.namaAttributeTamp = null; 
    	if ($stateParams.dataAttribute != undefined) {
    		vm.attribute = $stateParams.dataAttribute;
    		todo = 'edit';
    		vm.namaAttributeTamp = vm.attribute.name;
    	}
    	RequestService.doGET('/procurement/catalog/AttributeServices/findAllInputType')
    	.then(function (data) {
    		vm.inputTypeList = data;
    	});
    	
    	vm.changeInputType = function() {
    		if (vm.attribute.attributeOptionList != undefined) {
    			delete vm.attribute.attributeOptionList;
    		}
    	}
    	
    	vm.addOption = function() {
    		if (vm.attribute.attributeOptionList == undefined) {
    			vm.attribute.attributeOptionList = [];
    		}
    		vm.attribute.attributeOptionList.push({});
    	}
    	
    	vm.deleteOption = function(attributeOption) {
    		var indexData = vm.attribute.attributeOptionList.indexOf(attributeOption);
    		vm.attribute.attributeOptionList.splice(indexData, 1);
    	}
    	
    	$scope.cekValidSame = function(value){
        	var valid = true;
        	if(todo!='edit'){
        		RequestService.doGET("/procurement/catalog/AttributeServices/getAttributeByname/"+value).then(function success(data) {
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
    	
    	vm.saveData = function() {
        	var targetURL = '/procurement/catalog/AttributeServices/save';
			
			RequestService.doPOSTJSON(targetURL, vm.attribute)
			.then(function success(data) {
				if(data != undefined) {
					RequestService.modalInformation('Data Berhasil di Simpan')
					.then(function (result) {
						$state.go('app.promise.procurement-master-attribute');
					});
				}
			},function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
				
			});
        }
    	
    	vm.simpan = function() {
        	if($scope.validateForm()){
        		if(vm.namaAttributeTamp != null){//when edit
        			if(!angular.equals(vm.namaAttributeTamp,vm.attribute.name)){
        				RequestService.doGET("/procurement/catalog/AttributeServices/getAttributeByname/"+vm.attribute.name).then(function success(data) {
        					if(data.length == 0){
        						RequestService.modalConfirmation().then(function(result) {
        							vm.saveData();
        						});        				
        					}else{
        						vm.errorMessageLabel = "Name has been used";
        					}
        				});        				
        			}else{
        				RequestService.modalConfirmation().then(function(result) {
							vm.saveData();
						});
        			}
        		}else{//when add
        			RequestService.doGET("/procurement/catalog/AttributeServices/getAttributeByname/"+vm.attribute.name).then(function success(data) {
    					if(data.length == 0){
    						RequestService.modalConfirmation().then(function(result) {
    							vm.saveData();
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
        	vm.errorMessageInd = '';
        	vm.errorMessageEng = '';
        	vm.errorMessageInputType = '';
        	
        	
        	if (typeof vm.attribute.name == 'null' || vm.attribute.name == '' || vm.attribute.name == null) {
        		isValid = false;
        		vm.errorMessageLabel = "template.error.field_kosong";
        	}
        	if (typeof vm.attribute.translateInd == 'null'|| vm.attribute.translateInd == '' || vm.attribute.translateInd == null) {
        		isValid = false;
        		vm.errorMessageInd = "template.error.field_kosong";
        	}
        	if (typeof vm.attribute.translateEng == 'null' || vm.attribute.translateEng == '' || vm.attribute.translateEng == null) {
        		isValid = false;
        		vm.errorMessageEng = "template.error.field_kosong";
        	}
        	if (typeof vm.attribute.inputType == 'null' || vm.attribute.inputType == '' || vm.attribute.inputType == null) {
        		isValid = false;
        		vm.errorMessageInputType = "template.error.field_kosong";
        	}
        	return isValid;
        	
        	
        }
    	
        
        vm.kembali = function() {
        	$state.go('app.promise.procurement-master-attribute');
        }
    }
    
    AttributeAddController.$inject = ['RequestService', '$state', '$stateParams', '$scope'];
	
})();