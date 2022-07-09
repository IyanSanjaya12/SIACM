'use strict';

/* Controllers */

angular
.module('naut')
.controller('masterAttrSetAddCtrl', ['$scope', 'RequestService', '$state', '$modal','ModalService', function($scope, RequestService, $state, $modal, ModalService) {
	var vm = this;
	$scope.attributeGroup = {};
	$scope.attributePanelGroupList = [];
    
    RequestService.doGET('/procurement/catalog/AttributeServices/findAll')
	.then(function (data) {
		$scope.attributeList = data;
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
        			attributeSubGroupList: []
        			};
        	$scope.attributePanelGroupList.push(dataGroup);
        });
    }
    
    
    $scope.editGroup = function (attributeGroup) {
    	var indexGroup = $scope.attributePanelGroupList.indexOf(attributeGroup);
        var modalInstance = $modal.open({
            templateUrl: 'addGroup.html',
            controller: 'addGroupModalController',
            resolve: {
            	dataGroupName: function () {
                    return attributeGroup.name;
                }
            }
        });
        
        modalInstance.result.then(function (dataGroupName) {
        	attributeGroup.name = dataGroupName;
        	$scope.attributePanelGroupList.splice(indexGroup, 1, attributeGroup);
        });
    }
    
    $scope.removeGroup = function(attributeGroup) {
    	angular.forEach($scope.attributePanelGroupList.attributeSubGroupList, function(attributeSubGroup) {
    		$scope.attributeList.push(attributeSubGroup);
    	});
    	var indexGroup = $scope.attributePanelGroupList.indexOf(attributeGroup);
    	$scope.attributePanelGroupList.splice(indexGroup, 1);
    }
    $scope.saveData = function(){
    	$scope.attributeGroup.attributePanelGroupList = [];
		angular.forEach($scope.attributePanelGroupList, function(attributePanelGroup){
			angular.forEach(attributePanelGroup.attributeSubGroupList, function(attributeSubGroup, indexSubGroup){
				var tempAttributePanelGroup = {
						name: attributePanelGroup.name,
						attribute: attributeSubGroup,
						urutan: indexSubGroup+1
				};
				$scope.attributeGroup.attributePanelGroupList.push(tempAttributePanelGroup);
			});
		});
		RequestService.doPOSTJSON('/procurement/catalog/AttributeGroupServices/save', $scope.attributeGroup)
    	.then(function (data) {
    		if(data != undefined) {
    			RequestService.modalInformation('Data Berhasil di Simpan')
				.then(function (result) {
					$state.go("app.promise.procurement-attribute-group");
				});
    		}
    	});
    }
    $scope.cekValidSame = function(value){
    	RequestService.doGET("/procurement/catalog/AttributeGroupServices/getAttributeGroupByname/"+value).then(function success(data) {
    		if(data.length > 0){
    			vm.errorMessageName = "Name has been used";
    			return false;
    		}else{
    			vm.errorMessageName = "";
    			return true;
    		}
    	});
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
		if($scope.attributePanelGroupList.length > 0){
			angular.forEach($scope.attributePanelGroupList, function(data){
				if (data.attributeSubGroupList.length == 0){
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
    
    $scope.simpan = function() {
    	RequestService.doGET("/procurement/catalog/AttributeGroupServices/getAttributeGroupByname/"+$scope.attributeGroup.name).then(function success(data) {
    		if(data.length == 0){
    			if($scope.validateForm()){
    				RequestService.modalConfirmation().then(function (result) {
    					$scope.saveData();
    				});    		    			
    			}    			
    		}
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