(function () {
	
	'use strict';

    angular
        .module('naut')
        .controller('AttributeEditController', AttributeEditController);

    function AttributeEditController($scope, RequestService, $state, $stateParams) {
    	
    	$scope.dataEdit = $stateParams.dataAttribute;
    	
    	$scope.types = [];
    	$scope.dynamic_value = [];
    	$scope.isiValue = '';
    	
    	$scope.clone = function(){
            var dynamic_length = $scope.dynamic_value.length;
            $scope.dynamic_value.push({
                id: dynamic_length + 1,
                text: ''
            });
        };
        
        $scope.del_clone = function(val){
            $scope.dynamic_value.split[val];
        };
    	
        $scope.getDataAttributeType = function(attributeId){
        	var dataParam = {
        			attributeId: attributeId
        	}
        	
        	RequestService.requestServer('/procurement/catalog/AttributeServices/findAttributeTypeByAttributeId', dataParam)
        	.then(function (data, status, headers, config) {
        		if(data) {
        			angular.forEach(data,function(value, index){
        				$scope.dynamic_value.push({
        					id: index + 1,
        					text: value.value
        				});
        			})
        		}
        	});
        }
        
    	// Isi field dengan data
    	$scope.isiField = function() {
    		var value = $scope.dataEdit;
    		
    		if(value.unique == false)
    			var isiUnique = 'false';
    		else
    			var isiUnique = 'true';
    		
    		if(value.required == "NO")
    			var isiRequired = 'false';
    		else
    			var isiRequired = 'true';
    		
    		if(value.searchable == "NO")
    			var isiSearchable = 'false';
    		else
    			var isiSearchable = 'true';
    		
    		if(value.sortable == "NO")
    			var isiSortable = 'false';
    		else
    			var isiSortable = 'true';
    		
    		if(value.configurable == "NO")
    			var isiConfigurable = 'false';
    		else
    			var isiConfigurable = 'true';
    				
    				
    		$scope.data = {
    				label: value.codeName,
    				bahasa_admin: value.translateInd,
    				bahasa_ind: value.translateInd,
    				bahasa_eng: value.translateEng,
    				unique: isiUnique,
    				required: isiRequired,
    				searchable: isiSearchable,
    				sortable: isiSortable,
    				configurable: isiConfigurable,
    				inputType: value.inputType.name
    		}
    		
    		if($scope.data.inputType == 'Textarea' || $scope.data.inputType == 'Text_Field') 
        		$scope.showValueInputType = false;
        	else
        		$scope.showValueInputType = true;
    	}
    	
    	$scope.getDataInputType = function() {
    		var dataParam = {
    				flagDeleted: 0
    		}
    		
    		RequestService.doGET('/procurement/catalog/AttributeServices/findAllInputType')
        	.then(function (data, status, headers, config) {
        		
        		angular.forEach(data,function(value){
        			var isiData = {
        					id: value.name,
        					text: value.description
        			}
        			
        			$scope.types.push(isiData);
        		})
        	});
    		
    		$scope.isiField();
    		$scope.getDataAttributeType($scope.dataEdit.id);
    	}
    	$scope.getDataInputType();
    	
    	$scope.inputTypeChange = function(inputId) {
        	if(inputId == 'Textarea' || inputId == 'Text_Field') 
        		$scope.showValueInputType = false;
        	else
        		$scope.showValueInputType = true;
        }
    	
    	$scope.batal = function(data) {
    		$scope.dynamic_value.splice($scope.dynamic_value.indexOf(data), 1)
    	}
    	
    	// Fungsi Simpan
    	$scope.simpan = function() {
    		
    		var dataSimpan = {
    				attributeId: $scope.dataEdit.id,
        			required: $scope.data.required,
        			unique: $scope.data.unique,
        			searchable: $scope.data.searchable,
        			sortable: $scope.data.sortable,
        			configurable: $scope.data.configurable,
        			translateInd: $scope.data.bahasa_ind,
        			translateEng: $scope.data.bahasa_eng,
        			inputType: $scope.data.inputType
        	}
        	
        	var targetURL = '/procurement/catalog/AttributeServices/save';
        	
        	RequestService.requestServer(targetURL, dataSimpan)
        	.then(function (data, status, headers, config) {
        		if(data) {
        			var dataHapus = {
        					attribute: data.id
        			}
        			
        			RequestService.requestServer('/procurement/master/AttributeServices/deleteAttributeValue', dataHapus)
        	    	.then(function (data, status, headers, config) {
        	    		$scope.simpanAttributeTypeValue(dataHapus.attribute);
        	    	});
        		}
        	});
    	}
    	
    	$scope.simpanAttributeTypeValue = function(attributeId) {
    		var hitung = 0;
    		var panjangData = $scope.dynamic_value.length;
    		
    		angular.forEach($scope.dynamic_value,function(value){
    			var dataValueSimpan = {
    					attributeValue: value.text,
    					attribute: attributeId
    			}
    			
    			RequestService.requestServer('/procurement/master/AttributeServices/createAttributeValue', dataValueSimpan)
    	    	.then(function (data, status, headers, config) {});
    			
    			hitung = hitung + 1;
    		});
    		
    		if(hitung == panjangData) {
    			RequestService.modalInformation('Data Berhasil di Simpan')
    			.then(function (result) {
    				$state.go('app.promise.procurement-master-attribute');
    			});
    		}
    	}
    	
    	$scope.kembali = function() {
        	$state.go('app.promise.procurement-master-attribute');
        }
    }
    
    AttributeEditController.$inject = ['$scope', 'RequestService', '$state', '$stateParams'];
	
})();