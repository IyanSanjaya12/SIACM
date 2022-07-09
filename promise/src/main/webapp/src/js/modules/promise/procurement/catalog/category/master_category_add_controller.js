'use strict';

/* Controllers */

angular
.module('naut')
.controller('masterCategoryAddCtrl', ['RequestService', '$state', '$stateParams', '$http', '$rootScope', '$scope', function(RequestService, $state, $stateParams, $http, $rootScope, $scope) {
	var vm = this;
	
	if ($stateParams.dataCategory != undefined) {
		vm.category = {};
		vm.category.parentCategory = $stateParams.dataCategory;
		
	}

	vm.kembali = function() {
		$state.go('app.promise.procurement-category');
	}
	
	vm.simpan = function() {
		
		$scope.messageErrorExist = '';
		
		var valid = validasiData(vm.category);

		if(valid == true) {
			
			$http.get($rootScope.backendAddress + '/procurement/catalog/CategoryServices/getCategoryByNama/'+ vm.category.translateInd, {
	            ignoreLoadingBar: true
	        }).success(function (data, status, headers, config) {
	        	
	        	if(data.translateInd != null || data.translateInd != undefined) {
	        		$scope.messageErrorExist = 'Nama Category Sudah Ada';
	        	} else {
	        		RequestService.doPOSTJSON('/procurement/catalog/CategoryServices/save', vm.category)
	    	    	.then(function (data) {
	    	    		if(data) {
	    	    			RequestService.modalInformation('Data Berhasil di Simpan')
	        				.then(function (result) {
	        					$state.go('app.promise.procurement-category');
	        				});
	    	    		}
	    	    	})
	        	}

	        }).error(function (data, status, headers, config) {
	            $scope.loading = false;
	        })	
			
  	
		} else {
			RequestService.modalInformation('Data Isian Belom Lengkap!')
			.then(function (result) {});
		}
	}
	
	function validasiData(data) {
		
		var ok = false;
		
		if(data.adminLabel == null || data.adminLabel == '')
			var bahasaAdminError = true;
		else
			var bahasaAdminError = false;
		if(data.translateInd == null || data.translateInd == '')
			var bahasaINDError = true;
		else
			var bahasaINDError = false;
		if(data.translateEng == null || data.translateEng == '')
			var bahasaENGError = true;
		else
			var bahasaENGError = false;
		
		if(bahasaAdminError == false && bahasaINDError == false && bahasaENGError == false) {
			ok = true;
		}
		
		return ok;
	}
}]);