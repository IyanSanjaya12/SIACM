/**=========================================================
 * Module: PembuatKeputusanViewController
 =========================================================*/

(function(){
	
	'use strict';
	angular.module('naut').controller('PembuatKeputusanViewController', PembuatKeputusanViewController);
	
	function PembuatKeputusanViewController($http,$scope,$rootScope,$log,$stateParams,$state,RequestService){
		var vm=this;
		$scope.loading = false;
		vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.pembuatKeputusan= ($stateParams.pembuatKeputusan != undefined) ? $stateParams.pembuatKeputusan : {};
				
		$scope.save = function(){
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
			
		};
		
		$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorNamaPembuatKeputusan= '';
	         if (typeof vm.pembuatKeputusan.nama === 'undefined' || vm.pembuatKeputusan.nama == '') {
	                vm.errorNamaPembuatKeputusan = 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         
	         return vm.isValid;
	    }
		
		$scope.saveData = function(){
			var url='';
			if (vm.toDo=="add") {
				url = '/procurement/master/pembuatKeputusanServices/insert';
			} 
			if (vm.toDo=="edit") {
				url = '/procurement/master/pembuatKeputusanServices/update';
			}
			
			RequestService.doPOSTJSON(url, vm.pembuatKeputusan)
		    	.then(function success(data) {
			        if(data != undefined) {
		        		if(data.isValid != undefined) {
		        			if(data.errorNama != undefined) {
		        				vm.errorNamaPembuatKeputusan = data.errorNama;
		        			}
		                } else {
		                	RequestService.informSaveSuccess();
				        	$state.go('app.promise.procurement-master-pembuatkeputusan');
		                }
		        	}  
		        }, 
		        function error(response) {
		        	$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
		        });				
			
		}
		
		$scope.back = function(){
			$state.go('app.promise.procurement-master-pembuatkeputusan');
		};

		
	}
	PembuatKeputusanViewController.$inject =['$http','$rootScope','$scope','$log','$stateParams','$state','RequestService'];
	
})();