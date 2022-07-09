'use strict';

/* Controllers */

angular
.module('naut')
.controller('ecatalogCompareCtrl', ['$scope','RequestService','$state','$stateParams','ModalService','$rootScope',                 
                                    function($scope,RequestService,$state,$stateParams,ModalService, $rootScope) {
	
	$rootScope.catalogFront = null;
	$rootScope.compareList = [];
                                        
    var vm = this;
	if ($stateParams.catalogList != undefined) {
		$scope.catalogList = $stateParams.catalogList;
		
		$scope.compareView = function (catalogList) {
			vm.catalogAttribute = [];
			
			var i =0;
			angular.forEach($scope.catalogList[0].catalogAttributeList, function (catalogAttribute) {
						  
				var attribute = {}
				attribute = {
					attributeCatalog : catalogAttribute.attributePanelGroup.attribute.translateInd,
					nilai1 : catalogAttribute.nilai,
		            
		        }
				
				if($scope.catalogList[1] != undefined) {
					attribute.nilai2 = $scope.catalogList[1].catalogAttributeList[i].nilai;
				}
				
				if($scope.catalogList[2] != undefined) {
					attribute.nilai3 = $scope.catalogList[2].catalogAttributeList[i].nilai;
				}
			  
			  	vm.catalogAttribute.push(attribute);
				
				i++;
	        });
		}
		
		$scope.compareView($scope.catalogList);
		
		/** remove catalog from list **/
		$scope.removeCompareInView = function(compare) {
			var indexCompare = $scope.catalogList.indexOf(compare);
	    	if($scope.catalogList.length == 1){
	    		ModalService.showModalInformation('info : anda tidak menghapus catalog');
	    	}else{
	    		$scope.catalogList.splice(indexCompare, 1);
	    		$scope.compareView($scope.catalogList);
	    	}
	    }
		
		
	}
    
    vm.kembali = function(){
        $state.go('app.promise.procurement-ecatalog2');
    }
    
}]);