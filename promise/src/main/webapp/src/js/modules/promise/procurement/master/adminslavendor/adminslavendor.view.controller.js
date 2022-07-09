(function () {
	'use strict';

	angular.module('naut').controller('MasterAdminSLAVendorViewController', MasterAdminSLAVendorViewController);

	function MasterAdminSLAVendorViewController(RequestService,$scope, $state, $stateParams,$log) {
		var vm = this;
		vm.loading = true;
		vm.slaVendorProses = ($stateParams.slaVendorProses != undefined) ? $stateParams.slaVendorProses : {};
		
		
		$scope.save = function(){
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
			
		};
			
		$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorJumlahHari= '';
	         
	         if (typeof vm.slaVendorProses.jmlHari == 'undefined' || vm.slaVendorProses.jmlHari == '') {
                vm.errorJumlahHari = 'template.error.field_kosong';
                vm.isValid =  false;
	         } 
	         
	         if (typeof vm.slaVendorProses.jmlHari == '0' ){
        	 	vm.errorJumlahHari = 'template.error.field_kosong';
                vm.isValid =  false;
	         }
	         return vm.isValid;
	    }
		
		$scope.saveData = function(){	
			RequestService.doPOSTJSON('/procurement/master/SLAVendorProsesServices/update', vm.slaVendorProses)
		        .then(function success(data) {
		        	RequestService.informSaveSuccess();
				    $state.go('app.promise.procurement-master-adminslavendor');
		        }, function error(response) {
		        	$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
		        });					
		}
		
		$scope.back = function(){
			$state.go('app.promise.procurement-master-adminslavendor');
		};
		
	}

	MasterAdminSLAVendorViewController.$inject = ['RequestService', '$scope','$state','$stateParams','$log'];

})();