/**=========================================================
 * Module: MetodePengadaanViewController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('MetodePengadaanViewController', MetodePengadaanViewController);

	function MetodePengadaanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		
		var vm = this;
		vm.toDo= ($stateParams.toDo != undefined)? $stateParams.toDo : {};
		vm.metodePengadaan = ($stateParams.metodePengadaan != undefined) ? $stateParams.metodePengadaan : {};
		
		$scope.save = function () {
			RequestService.modalConfirmation().then(function (result){
				if ($scope.validateForm()){
					$scope.saveData();                     
				}
            });
    	}
	        	
       $scope.validateForm = function () {
        	vm.isValid = true;
        	vm.errorMessageMetodePengadaan = '';
			
            if (typeof vm.metodePengadaan.nama === 'undefined' || vm.metodePengadaan.nama == '') {
                	vm.errorMessageMetodePengadaan = 'template.error.field_kosong';
                    vm.isValid = false;
                }
            return vm.isValid;
        }
        
       	$scope.saveData = function(){
	        var url='';
	        if (vm.toDo == "Add" ){
	        	url = '/procurement/master/metodePengadaanServices/insert'			
	        }
	        else if(vm.toDo=="Edit"){
	        	url = '/procurement/master/metodePengadaanServices/update'
	        }	
	        RequestService.doPOSTJSON(url, vm.metodePengadaan ).then(function success(data) {
	        	if(data != undefined) {
	        		if(data.isValid != undefined) {
	        			if(data.errorNama != undefined) {
	        				vm.errorMessageMetodePengadaan = 'promise.procurement.master.metodepengadaan.error.nama_sama';
	                  	}
	                 }
	                 else {
	                	 RequestService.informSaveSuccess();
	                	 $state.go('app.promise.procurement-master-metodePengadaan');
	                 }
	        	}
        	}, function error(response) {
        		$log.info("proses gagal");
        		RequestService.informError("Terjadi Kesalahan Pada System");
        	});
        }
 
        $scope.back = function () {
        	$state.go('app.promise.procurement-master-metodePengadaan');
        }
	}

	MetodePengadaanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter','$log'];

})();