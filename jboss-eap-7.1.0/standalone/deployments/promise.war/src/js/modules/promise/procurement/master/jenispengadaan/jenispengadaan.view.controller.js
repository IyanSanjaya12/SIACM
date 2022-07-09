/**=========================================================
 * Module: JenisPengadaanViewController
 =========================================================*/

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('JenisPengadaanViewController', JenisPengadaanViewController);

	function JenisPengadaanViewController($scope, RequestService, $state, $stateParams, $log) {
		
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.jenisPengadaan = ($stateParams.jenisPengadaan != undefined) ? $stateParams.jenisPengadaan : {};

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

			if (typeof vm.jenisPengadaan.nama === 'undefined'
					|| vm.jenisPengadaan.nama == '') {
				vm.errorMessageNama = "template.error.field_kosong";
				vm.isValid = false;
			}
			return vm.isValid;
		}

		$scope.saveData = function() {
			var url = '';
			if (vm.toDo == "Add") {
				url = '/procurement/master/jenisPengadaanServices/insert'
			} else if (vm.toDo == "Edit") {
				url = '/procurement/master/jenisPengadaanServices/update'
			}
			
			RequestService.doPOSTJSON(url, vm.jenisPengadaan).then(function success(data) {
				if (data != undefined) {
					if (data.isValid != undefined) {
						if (data.errorMessageNama != undefined) {
							vm.errorMessageNama = "promise.procurement.master.jenispengadaan.error.nama_sama";
						}
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-jenisPengadaan');
					}

				}
			},
			function error(response) {
				$log.info("proses gagal");
				RequestService.informError("Terjadi Kesalahan Pada System");
			});
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-jenisPengadaan');
		};
	}

	JenisPengadaanViewController.$inject = [ '$scope', 'RequestService', '$state',
			'$stateParams', '$log' ];

})();