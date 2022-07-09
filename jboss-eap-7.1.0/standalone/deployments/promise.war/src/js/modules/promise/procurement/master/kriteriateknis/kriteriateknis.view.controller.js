/**=========================================================
 * Module: MasterKriteriaTeknisViewController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaTeknisViewController', KriteriaTeknisViewController);

    function KriteriaTeknisViewController($scope, $state, RequestService, $log, $stateParams) {
        
    	var vm = this;
		vm.toDo =($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.kriteriaTeknis= ($stateParams.kriteriaTeknis != undefined) ? $stateParams.kriteriaTeknis : {};
        
		$scope.save = function(){
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		};
		
		$scope.validateForm = function () {
			vm.isValid = true;
	        vm.errorNamaKriteriaTeknis= '';
	        vm.errorKeteranganKriteriaTeknis= '';
	        if (typeof vm.kriteriaTeknis.nama === 'undefined' || vm.kriteriaTeknis.nama == '') {
	        	vm.errorNamaKriteriaTeknis = 'template.error.field_kosong';
	        	vm.isValid =  false;
         	}
	        if (typeof vm.kriteriaTeknis.keterangan === 'undefined' || vm.kriteriaTeknis.keterangan == '') {
                vm.errorKeteranganKriteriaTeknis = 'template.error.field_kosong';
                vm.isValid =  false;
         	}
	        return vm.isValid;
	    }
		
		$scope.saveData = function(){
			var url='';
			if (vm.toDo=="add") {
				url = '/procurement/master/kriteriaTeknisServices/insert';
			} 
			if (vm.toDo=="edit") {
				url = '/procurement/master/kriteriaTeknisServices/update';
			}
			
			RequestService.doPOSTJSON(url, vm.kriteriaTeknis).then(function success(data) {
	        	if(data != undefined) {
	                if(data.isValid != undefined) {
	                	if(data.errorNama != undefined) {
	                		vm.errorNamaKriteriaTeknis = data.errorNama;
	                	}
	                } else {
	                	RequestService.informSaveSuccess();
			        	$state.go('app.promise.procurement-master-kriteriaTeknis');
	                }
	        	}  
	        }, 
	        function error(response) {
	        	$log.info("proses gagal");
	        	RequestService.informError("Terjadi Kesalahan Pada System");
	        });			
			
		}
		
		$scope.back = function(){
			$state.go('app.promise.procurement-master-kriteriaTeknis');
		};
    }
    
    KriteriaTeknisViewController.$inject = ['$scope','$state','RequestService','$log','$stateParams'];

})();