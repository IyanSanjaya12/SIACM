(function () {
	'use strict';

	angular
		.module('naut')
		.controller('AlasanBlacklistViewController', AlasanBlacklistViewController);

	function AlasanBlacklistViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		
		var vm = this;
		vm.toDo= ($stateParams.toDo != undefined)? $stateParams.toDo : {};
		vm.alasanBlacklist = ($stateParams.alasanBlacklist != undefined) ? $stateParams.alasanBlacklist : {};
		
		$scope.save = function () {
			RequestService.modalConfirmation().then(function (result){
				if ($scope.validateForm()){
					$scope.saveData();                     
				}
            });
    	}
	        	
       $scope.validateForm = function () {
        	vm.isValid = true;
        	vm.errorMessageAlasanBlacklist = '';
	        vm.errorMessageJangkaWaktuAlasanBlacklist = '';
	        vm.errorMessageJumlahWaktuAlasanBlacklist = '';
			
            if (typeof vm.alasanBlacklist.alasanBlacklistKeterangan === 'undefined' || vm.alasanBlacklist.alasanBlacklistKeterangan == '' || vm.alasanBlacklist.alasanBlacklistKeterangan == null) {
            	vm.errorMessageAlasanBlacklist = 'template.error.field_kosong';
                vm.isValid = false;
            }
              
            if (typeof vm.alasanBlacklist.alasanBlacklistJmlWaktu == 'undefined' || vm.alasanBlacklist.alasanBlacklistJmlWaktu == '' || vm.alasanBlacklist.alasanBlacklistJmlWaktu == null) {
            	vm.errorMessageJumlahWaktuAlasanBlacklist = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            if (typeof vm.alasanBlacklist.alasanBlacklistJkWaktu  == 'undefined' || vm.alasanBlacklist.alasanBlacklistJkWaktu == '' || vm.alasanBlacklist.alasanBlacklistJkWaktu  == null) {
            	vm.errorMessageJangkaWaktuAlasanBlacklist = 'template.error.field_kosong';
                vm.isValid = false;
            }
            
            return vm.isValid;
        }
        
       	$scope.saveData = function(){
       		var url='';
       		if (vm.toDo == "Add" ){
        		url = '/procurement/master/AlasanBlacklistServices/insert'			
       		}
       		else if(vm.toDo=="Edit"){
        		url = '/procurement/master/AlasanBlacklistServices/update', vm.alasanBlacklist
       		}	
	        RequestService.doPOSTJSON(url, vm.alasanBlacklist)
	        	.then(function success(data) {
	        		RequestService.informSaveSuccess();
	        		$state.go('app.promise.procurement-master-alasanblacklist'); 
	        	}, function error(response) {
	        		$log.info("proses gagal");
	        		RequestService.informError("Terjadi Kesalahan Pada System");
	        	});
        }
 
        $scope.back = function () {
        	$state.go('app.promise.procurement-master-alasanblacklist');
        }
        
	}

	AlasanBlacklistViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter','$log'];

})();