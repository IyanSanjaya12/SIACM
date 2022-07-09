'use strict';

angular
.module('naut')
.controller('masterCategoryCtrl', ['$scope', 'RequestService', '$state', function($scope, RequestService, $state) {
	
	
	$scope.initData = function() {
		RequestService.doGET('/procurement/catalog/CategoryServices/findAll')
		.then(function (data) {
			$scope.categoryList = data;
		});
	}
	$scope.initData();
	
	$scope.add = function() {
		$state.go('app.promise.procurement-category-view', {
			toDo : "add"
		});
	}

	$scope.edit = function(categoryNew) {
		$state.go('app.promise.procurement-category-view', {
			category : categoryNew,
			toDo : "edit"
		});
	}
	
	$scope.addSubRoot = function(categoryNew) {
		$state.go('app.promise.procurement-category-view', {
			category : categoryNew,
			toDo : "addSub"
		});
	}
	
	$scope.del = function(categoryId) {
		var url = "/procurement/catalog/CategoryServices/delete";
    	
		var data = {};
    	
    	data.id = categoryId;
    	RequestService.modalConfirmation(
		'Apakah anda yakin ingin menghapus Kategori?').then(
		function(result) {
			RequestService.doPOST(url, data).success(
					function(data) {
						RequestService.informDeleteSuccess();
						$scope.initData();
						
					}).error(
					function(data, status, headers, config) {
						RequestService.informError("Terjadi Kesalahan Pada System");
					})
		});
	}
	
	$scope.importData = function() {
    	$state.go("app.promise.procurement-category-import");
    }
	
}]);