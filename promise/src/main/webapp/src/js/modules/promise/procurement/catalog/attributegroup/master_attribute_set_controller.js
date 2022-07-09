'use strict';

/* Controllers */

angular
.module('naut')
.controller('masterAttrSetCtrl', ['$scope', 'RequestService', '$state', '$modal', function($scope, RequestService, $state, $modal) {
	
	$scope.initData = function() {
		RequestService.doGET('/procurement/catalog/AttributeGroupServices/findAll')
		.then(function (data) {
			$scope.attributeGroupList = data;
		})
	}
	$scope.initData();
	
	$scope.deleteData = function(data) {
		RequestService.modalConfirmation('Yakin menghapus data Attribute Group ('+data.name+') ?')
    	.then(function (result) {
    		RequestService.doPOSTJSON('/procurement/catalog/AttributeGroupServices/delete', data)
        	.then(function (data) {
        		RequestService.modalInformation('Data Berhasil di Hapus')
				.then(function (result) {
					$scope.initData();
				});
        	})
    	});
	}
	
	$scope.addData = function() {
		$state.go('app.promise.procurement-attribute-group-add');
	}
	
	$scope.editData = function(data) {
		$state.go('app.promise.procurement-attribute-group-edit', { dataAttributeGroup: data });
	}
	
	$scope.importData = function() {
    	$state.go("app.promise.procurement-attribute-group-import");
    }

}]);