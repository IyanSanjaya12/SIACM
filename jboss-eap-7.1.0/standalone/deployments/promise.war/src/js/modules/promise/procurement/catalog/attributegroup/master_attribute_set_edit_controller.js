'use strict';

/* Controllers */

angular
.module('naut')
.controller('masterAttrSetEditCtrl', ['$scope', 'RequestService', '$state', '$modal', '$stateParams','ModalService', function($scope, RequestService, $state, $modal, $stateParams,ModalService) {
	var vm = this;
	if ($stateParams.dataAttributeGroup != undefined) {
		$scope.attributeGroup = $stateParams.dataAttributeGroup;
		vm.tampNama = $scope.attributeGroup.name;
	}
    
    RequestService.doPOSTJSON('/procurement/catalog/AttributeServices/findAllExcludeGroup', $scope.attributeGroup.attributePanelGroupList)
	.then(function (data) {
		$scope.attributeList = data.attributeList;
		$scope.attributeGroup.attributePanelGroupList = data.attributePanelGroupList;
	});
    
    
    $scope.addGroup = function () {
        var modalInstance = $modal.open({
            templateUrl: 'addGroup.html',
            controller: 'addGroupModalController',
            resolve: {
            	dataGroupName: function () {
                    return null;
                }
            }
        });
        
        modalInstance.result.then(function (groupText) {
        	var dataGroup = {
        			name: groupText,
        			attributeList: []
        			};
        	$scope.attributeGroup.attributePanelGroupList.push(dataGroup);
        });
    }
    
    
    $scope.editGroup = function (attributePanelGroup) {
    	var indexGroup = $scope.attributeGroup.attributePanelGroupList.indexOf(attributePanelGroup);
        var modalInstance = $modal.open({
            templateUrl: 'addGroup.html',
            controller: 'addGroupModalController',
            resolve: {
            	dataGroupName: function () {
                    return attributePanelGroup.name;
                }
            }
        });
        
        modalInstance.result.then(function (dataGroupName) {
        	attributePanelGroup.name = dataGroupName;
        	$scope.attributeGroup.attributePanelGroupList.splice(indexGroup, 1, attributePanelGroup);
        });
    }
    
    $scope.removeGroup = function(attributePanelGroup) {
    	angular.forEach(attributePanelGroup.attributeList, function(attribute) {
    		$scope.attributeList.push(attribute);
    	});
    	delete attributePanelGroup.attributeList;
    	var indexGroup = $scope.attributeGroup.attributePanelGroupList.indexOf(attributePanelGroup);
    	$scope.attributeGroup.attributePanelGroupList.splice(indexGroup, 1);
    }
    $scope.cekValidSame = function(value){
    	vm.errorMessageName = "";
    	if(!angular.equals(vm.tampNama, $scope.attributeGroup.name)){
    		RequestService.doGET("/procurement/catalog/AttributeGroupServices/getAttributeGroupByname/"+value).then(function success(data) {
    			if(data.length > 0){
    				vm.errorMessageName = "Name has been used";
    			}else{
    				vm.errorMessageName = "";
    			}
    		});    		
    	}
    }
    $scope.validateForm = function () {
    	var isValid = true;
    	
    	vm.errorMessageName = '';
    	vm.errorMessageDesc = '';
    	
    	
    	if (typeof $scope.attributeGroup.name == 'null' || $scope.attributeGroup.name == '' || $scope.attributeGroup.name == null) {
    		isValid = false;
    		vm.errorMessageName = "template.error.field_kosong";
    	}
    	if (typeof $scope.attributeGroup.description == 'null'|| $scope.attributeGroup.description == '' || $scope.attributeGroup.description == null) {
    		isValid = false;
    		vm.errorMessageDesc = "template.error.field_kosong";
    	}
    	if($scope.attributeGroup.attributePanelGroupList.length > 0){
			angular.forEach($scope.attributeGroup.attributePanelGroupList, function(data){
				if (data.attributeList.length == 0){
					ModalService.showModalInformation('Mohon lengkapi Attribute Group!');
					isValid = false;
				}
			});
		}else{
			ModalService.showModalInformation('Mohon isi Attribute Group! Minimal satu');
			isValid = false;			
		} 
    	
    	return isValid;
    	
    	
    }
    $scope.simpan = function(variable) {
    	if(!angular.equals(vm.tampNama, $scope.attributeGroup.name)){
    		RequestService.doGET("/procurement/catalog/AttributeGroupServices/getAttributeGroupByname/"+$scope.attributeGroup.name).then(function success(data) {
    			if(data.length == 0){
    				if($scope.validateForm()){
    					$scope.saveData();
    				}    			   			
    			}
    		});
    	}else{
    		if($scope.validateForm()){
				$scope.saveData();
			}
    	}
    }
    
    $scope.saveData = function(){
    	RequestService.modalConfirmation('Yakin menyimpan data ?')
		.then(function (result) {
			var tempAttributePanelGroupList = [];
			angular.forEach($scope.attributeGroup.attributePanelGroupList, function(attributePanelGroup){
				angular.forEach(attributePanelGroup.attributeList, function(attribute, indexSubGroup){
					var tempAttributePanelGroup = {
							name: attributePanelGroup.name,
							attribute: attribute,
							urutan: indexSubGroup+1
					};
					tempAttributePanelGroupList.push(tempAttributePanelGroup);
				});
			});
			$scope.attributeGroup.attributePanelGroupList = tempAttributePanelGroupList;
			RequestService.doPOSTJSON('/procurement/catalog/AttributeGroupServices/save', $scope.attributeGroup)
			.then(function (data) {
				if(data != undefined) {
					RequestService.modalInformation('Data Berhasil di Simpan')
					.then(function (result) {
						$state.go("app.promise.procurement-attribute-group");
					});
				}
			});
		});
    }
    
    $scope.kembali = function() {
    	$state.go("app.promise.procurement-attribute-group");
    }
}]);

angular
.module('naut')
.controller('addGroupModalController', ['$scope', '$modalInstance', 'dataGroupName', function($scope, $modalInstance, dataGroupName) {
	$scope.groupName = dataGroupName;
	$scope.errorMessageGroupName ='';
    $scope.ok = function () {
    	if($scope.groupName == '' || $scope.groupName == null){
    		$scope.errorMessageGroupName ='template.error.field_kosong';
    	}else{
    		$modalInstance.close($scope.groupName);    		
    	}
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);