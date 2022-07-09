/**=========================================================
 * Module: AddAksiController.js
 * Controller for Aksi
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AksiViewController', AksiViewController);

    function AksiViewController($scope, $modal, $state, $stateParams, RequestService, $log) {

        var vm = this;
        
        vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.aksi = ($stateParams.aksi != undefined) ? $stateParams.aksi : {};
		vm.submitted = false;
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if (formValid){
				RequestService.modalConfirmation().then(function(result) {
						vm.loading = true;
						$scope.saveData();
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formAksi[$event.target.name].$setValidity($event.target.name, true);
		}
			 
        $scope.saveData = function () {
        	
        	if (vm.toDo == "Add") {
				vm.url = "/procurement/menu/aksi/insert"
			} else if (vm.toDo == "Edit"){
				vm.url = "/procurement/menu/aksi/update"
			}
        	
            RequestService.doPOSTJSON(vm.url, vm.aksi)
				.then(function success(data) {
					if (data.errorNama != undefined) {
						$scope.formAksi.path.$setValidity('path', false);
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-aksi');
					}
					vm.loading = false;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
        };

        $scope.back = function () {
            $state.go('app.promise.procurement-master-aksi');
        };

    }
    
    AksiViewController.$inject = ['$scope', '$modal','$state', '$stateParams', 'RequestService', '$log'];

})();