(function () {
    'use strict';

    angular.module('naut').controller('SistemEvaluasiPenawaranViewController', SistemEvaluasiPenawaranViewController);

    function SistemEvaluasiPenawaranViewController($scope, $http, $rootScope, $resource, $location, toaster, $stateParams, $state, RequestService) {
        
    	var vm = this;
        
        vm.sistemEvaluasiPenawaran = ($stateParams.sistemEvaluasiPenawaran != undefined) ? $stateParams.sistemEvaluasiPenawaran : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		
		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		};
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.SistemEvaluasiPenawaran = "";

			if (typeof vm.sistemEvaluasiPenawaran.nama === 'undefined' || vm.sistemEvaluasiPenawaran.nama == '') {
				vm.errorNamaSistemEvaluasiPenawaran = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}
		
        $scope.saveData = function () {
        	vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/master/SistemEvaluasiPenawaranServices/insert';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/master/SistemEvaluasiPenawaranServices/update';
			}

			RequestService.doPOSTJSON(vm.url, vm.sistemEvaluasiPenawaran)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								vm.errorNamaSistemEvaluasiPenawaran = 'promise.procurement.master.sistemevaluasipenawaran.error.nama_sama';
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-sistemevaluasipenawaran');
						}
					}
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
				});

        };
        
        $scope.back = function() {
			$state.go('app.promise.procurement-master-sistemevaluasipenawaran');
		};
    }
    SistemEvaluasiPenawaranViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster','$stateParams','$state','RequestService'];

})();
