/**=========================================================
 * Module: MasterKriteriaAdministrasiViewController
 =========================================================*/

(function() {
	'use strict';

	angular.module('naut').controller('KriteriaAdministrasiViewController',
			KriteriaAdministrasiViewController);
	function KriteriaAdministrasiViewController($scope, $state, $stateParams, $log, RequestService) {
		var vm = this;
		vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.kriteriaAdministrasi = ($stateParams.kriteriaAdministrasi != undefined) ? $stateParams.kriteriaAdministrasi
				: {};
				
		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.simpan();
				}
			});
		}
				
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageNama = '';
			vm.errorMessageKeterangan = '';

			if (typeof vm.kriteriaAdministrasi.nama === 'undefined' || vm.kriteriaAdministrasi.nama == '') {
				vm.errorMessageNama = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.kriteriaAdministrasi.keterangan == 'undefined' || vm.kriteriaAdministrasi.keterangan == '') {
				vm.errorMessageKeterangan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}

		$scope.simpan = function() {
			var url = '';
			if (vm.toDo == "Add") {
				url = '/procurement/master/kriteriaAdministrasiServices/insert'
			}
			else if (vm.toDo == "Edit") {
				url = '/procurement/master/kriteriaAdministrasiServices/update'
			}
			
			RequestService.doPOSTJSON(url, vm.kriteriaAdministrasi)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorMessageNama != undefined) {
								vm.errorMessageNama = "promise.procurement.master.kriteriaadministrasi.error.nama_sama";
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-kriteriaAdministrasi');
						}

					}
				},
				function error(response) {
					$log.info("proses gagal");
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
		}
	

		$scope.back = function() {
			$state.go('app.promise.procurement-master-kriteriaAdministrasi');
		}
	}

	KriteriaAdministrasiViewController.$inject = [ '$scope', '$state',
			'$stateParams', '$log', 'RequestService' ];

})();
