/**=========================================================
 * Module: JabatanViewController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('JabatanViewController', JabatanViewController);

	function JabatanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log, $parse) {
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.jabatan = ($stateParams.jabatan != undefined) ? $stateParams.jabatan : {};
		vm.submitted = false;
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if(formValid) {
				RequestService.modalConfirmation().then(function(result) {
					$scope.savedata();
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formJabatan[$event.target.name].$setValidity($event.target.name, true);
		}

		$scope.savedata = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/master/jabatan/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/jabatan/update';
             }
			RequestService.doPOSTJSON(url, vm.jabatan)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								$scope.formJabatan.namaJabatan.$setValidity('namaJabatan', false);
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-jabatan');
						}
					}
					vm.loading = false;
				},
				function error(response) {
					$log.info("proses gagal");
					RequestService.informInternalError();
				});
        }
		
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-jabatan');
        }
		
	}
	JabatanViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$log', '$parse'];
})();
		