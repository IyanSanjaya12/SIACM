/**=========================================================
 * Module: MetodePenawaranHargaViewController
 =========================================================*/

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('MetodePenawaranHargaViewController', MetodePenawaranHargaViewController);


	function MetodePenawaranHargaViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams, $log) {

		var vm = this;
		
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.metodePenawaranHarga = ($stateParams.metodePenawaranHarga != undefined) ? $stateParams.metodePenawaranHarga : {};

		$scope.save = function() {

			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		}
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageNama = '';

			if (typeof vm.metodePenawaranHarga.nama == 'undefined'
					|| vm.metodePenawaranHarga.nama == '') {
				vm.errorMessageNama = 'template.error.field_kosong';
				vm.isValid = false;

			}
			return vm.isValid;
		}

		$scope.saveData = function() {

			var url = ""
			if (vm.toDo == "add") {
				url = "/procurement/master/MetodePenawaranHargaServices/insert";

			} else if (vm.toDo == "edit") {

				url = "/procurement/master/MetodePenawaranHargaServices/update";

			}
			RequestService.doPOSTJSON(url, vm.metodePenawaranHarga) .then(function success(data) {
				if(data != undefined) {
					if(data.isValid != undefined) {
						if(data.errorNama != undefined) {
							vm.errorMessageNama = 'promise.procurement.master.metodepenawaranharga.error.nama_sama';
						}
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-metodepenawaranharga');
					}
              
				}
            }, function error(response) {
            	$log.info("proses gagal");
            	RequestService.informError("Terjadi Kesalahan Pada System");
            });
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-metodepenawaranharga');
		}

	}
	MetodePenawaranHargaViewController.$inject = [ '$scope', '$http', '$rootScope',
			'$resource', '$location', 'toaster', 'RequestService', '$state',
			'$stateParams', '$log' ];

})();
