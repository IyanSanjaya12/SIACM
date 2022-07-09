/**=========================================================
 * Module: MataUangViewController
 =========================================================*/


(function() {
	'use strict';

	angular
		.module('naut')
		.controller('MataUangViewController', MataUangViewController);

	function MataUangViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams, $log) {

		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined)? $stateParams.toDo : {};
		vm.mataUang = ($stateParams.mataUang != undefined) ? $stateParams.mataUang : {};
		vm.submitted = false;
		
		$scope.delValidation = function($event) {
			$scope.formMataUang[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			$scope.validateKurs();
			if (formValid && vm.valid){
				RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();		
				});																		
			}
		}
		
		$scope.validateKurs = function() {
			vm.valid = true;
			if(vm.mataUang.kurs === undefined || vm.mataUang.kurs == 0 || Number(vm.mataUang.kurs) <= 0 ){
				$scope.formMataUang.kursMataUang.$setValidity('kursMataUang', false);
				vm.valid = false;
			}else{
				$scope.formMataUang.kursMataUang.$setValidity('kursMataUang', true);
			}
		}

		
		
		$scope.saveData = function(){
			
			var url = ""
			
			if (vm.toDo == "add") {
				url = "/procurement/master/mata-uang/insert";
			} else if (vm.toDo == "edit") {
				url = "/procurement/master/mata-uang/update";
			}
				
			RequestService.doPOSTJSON(url, vm.mataUang).then(function success(data) {
            	if(data != undefined) {
            		if(data.isValid != undefined) {
            			if(data.errorKode != undefined) {
            				$scope.formMataUang.kodeMataUang.$setValidity('kodeMataUang', false);
	            		}
	            		if(data.errorNama != undefined) {
	            			$scope.formMataUang.namaMataUang.$setValidity('namaMataUang', false);
	            		}
	            		vm.loading = false;
            		} else {
            			RequestService.informSaveSuccess();
    	            	$state.go('app.promise.procurement-master-matauang');
            		}
            		
            	}
            }, 
            function error(response) {
            	RequestService.informInternalError();
            	vm.loading = false;
            });
		}
		
		$scope.back = function() {
			$state.go('app.promise.procurement-master-matauang');
		}

	}
	MataUangViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state', '$stateParams','$log' ];

})();
