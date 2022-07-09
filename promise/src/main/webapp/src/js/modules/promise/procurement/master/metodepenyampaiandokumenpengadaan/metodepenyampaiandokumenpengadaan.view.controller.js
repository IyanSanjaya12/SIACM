(function () {
	'use strict';

	angular
		.module('naut')
		.controller('MetodePenyampaianDokumenPengadaanViewController', MetodePenyampaianDokumenPengadaanViewController);

	function MetodePenyampaianDokumenPengadaanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.metodePenyampaianDokumenPengadaan = ($stateParams.metodePenyampaianDokumenPengadaan != undefined) ? $stateParams.metodePenyampaianDokumenPengadaan : {};
		
		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if($scope.validateForm()){
					$scope.savedata();
				}
			});
		}
		
		$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorNamaMetodePenyampaianDokumenPengadaan = '';
	         
	         if (typeof vm.metodePenyampaianDokumenPengadaan.nama === 'undefined' || vm.metodePenyampaianDokumenPengadaan.nama == '') {
                vm.errorNamaMetodePenyampaianDokumenPengadaan = 'template.error.field_kosong';
                vm.isValid = false;
	         }
	         
	         return vm.isValid;
	    }
		
		$scope.savedata = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/master/metodePenyampaianDokumenServices/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/metodePenyampaianDokumenServices/update';
            }
			RequestService.doPOSTJSON(url, vm.metodePenyampaianDokumenPengadaan)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								vm.errorNamaMetodePenyampaianDokumenPengadaan = 'promise.procurement.master.metode_penyampaian_dokumen_pengadaan.error.nama_sama';
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-metodePenyampaianDokumenPengadaan');
						}
					}
				},function error(response) {
					$log.info("proses gagal");
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
        }
		
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-metodePenyampaianDokumenPengadaan');
        }
	}
	MetodePenyampaianDokumenPengadaanViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$log'];
})();