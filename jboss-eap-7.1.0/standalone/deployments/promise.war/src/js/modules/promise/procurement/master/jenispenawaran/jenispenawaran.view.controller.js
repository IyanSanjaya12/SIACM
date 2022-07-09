/**=========================================================
 * Module: JenisPenawaranViewController
 =========================================================*/

(function () {
    'use strict';

    angular
    	.module('naut')
    	.controller('JenisPenawaranViewController', JenisPenawaranViewController);

    function JenisPenawaranViewController($scope, $stateParams, RequestService, $state) {
        var vm = this;
        vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.jenisPenawaran = ($stateParams.jenisPenawaran != undefined) ? $stateParams.jenisPenawaran : {};
				
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

			if (typeof vm.jenisPenawaran.nama === 'undefined' || vm.jenisPenawaran.nama == '') {
				vm.errorMessageNama = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}
		
		$scope.saveData = function() {
			var url = '';
			if (vm.toDo == "Add") {
				url = '/procurement/master/JenisPenawaranServices/insert'
			} else if (vm.toDo == "Edit") {
				url = '/procurement/master/JenisPenawaranServices/update'
			}
			
			RequestService.doPOSTJSON(url, vm.jenisPenawaran)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorMessageNama != undefined) {
								vm.errorMessageNama = "promise.procurement.master.jenispenawaran.error.nama_sama";
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-jenispenawaran');
						}

					}
				},function error(response) {
					$log.info("proses gagal");
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
		}

        $scope.back = function () {
            $state.go('app.promise.procurement-master-jenispenawaran');
        }
    }
    
    JenisPenawaranViewController.$inject = ['$scope','$stateParams','RequestService', '$state'];

})();
