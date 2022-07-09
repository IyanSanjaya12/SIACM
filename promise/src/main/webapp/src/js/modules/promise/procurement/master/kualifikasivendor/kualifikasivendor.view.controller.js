/**=========================================================
 * Module: KualifikasiVendorViewController
 =========================================================*/

(function(){
	
	'use strict';
	angular
		.module('naut')
		.controller('KualifikasiVendorViewController', KualifikasiVendorViewController);
	
	function KualifikasiVendorViewController($scope,$log,$stateParams,$state,RequestService){
		
		var vm = this;
		vm.submitted = false;
		vm.toDo =($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.kualifikasiVendor= ($stateParams.kualifikasiVendor != undefined) ? $stateParams.kualifikasiVendor : {};
		
		$scope.save = function(formValid){
			vm.submitted= true;
			if(formValid){
				RequestService.modalConfirmation().then(function(result) {
					$scope.saveData();
					vm.loading = true;
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formKualifikasiVendor[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.saveData = function(){
			var url='';
			if (vm.toDo=="add") {
				url = '/procurement/master/kualifikasi-vendor/insert';
			} 
			if (vm.toDo=="edit") {
				url = '/procurement/master/kualifikasi-vendor/update';
			}
			
			RequestService.doPOSTJSON(url, vm.kualifikasiVendor)
	        .then(function success(data) {
	        	if(data != undefined) {
	        		if(data.isValid != undefined) {
	        			if(data.errorNama != undefined) {
	        				$scope.formKualifikasiVendor.name.$setValidity('name', false);
	        			}
	        			vm.loading = false;
	                } else {
	                	RequestService.informSaveSuccess();
			        	$state.go('app.promise.procurement-master-kualifikasivendor');
	                }
	        	}  
	         }, function error(response) {
	        	 RequestService.informInternalError();
	        	 vm.loading = false;
	         });				
			
		}
		
		$scope.back = function(){
			$state.go('app.promise.procurement-master-kualifikasivendor');
		};
		
	}
	
	KualifikasiVendorViewController.$inject =['$scope','$log','$stateParams','$state','RequestService'];
	
})();