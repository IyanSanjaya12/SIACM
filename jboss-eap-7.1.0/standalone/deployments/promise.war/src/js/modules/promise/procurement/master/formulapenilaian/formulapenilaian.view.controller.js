(function() {
	'use strict';

	angular.module('naut').controller('FormulaPenilaianEditController',
			FormulaPenilaianViewController);

	function FormulaPenilaianViewController(RequestService, ModalService,
			$scope, $http, $rootScope, $resource, $location, toaster, $state,
			$log) {

		var vm = this;
		$scope.formulaPenilaianList = [];
		vm.loading = true;
		vm.mdlname = 'Formula Penilaian';

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {

				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		}

		$scope.validateForm = function() {
			vm.isValid = true;
			/*value.errorMessageKode = '';
			value.errorMessageBatasKiri = '';
			value.errorMessagePersamaanKiri = '';
			value.errorMessagePersamaanKanan = '';
			value.errorMessageBatasKanan = '';
			value.errorMessageScore = '';
			value.errorMessageDescription = '';
			value.errorMessageNote = '';*/
			
			for(var i=0; i < $scope.formulaPenilaianList.length; i++) {

				var value = $scope.formulaPenilaianList[i];
				
				value.errorMessageKode = '';
				
				if (value.fPNote == null
						|| value.fPNote == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.catatan_kosong';
				}
				
				if (value.fPDescription == null
						|| value.fPDescription == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.deskripsi_kosong';
				}
				if (value.fPPoint == null
						|| value.fPPoint == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.score_nol';
				}
				if (value.fPBatasKanan == null
						|| value.fPBatasKanan == ''|| value.fPBatasKanan == '0') {
						vm.isValid = false;
						value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.batas_kanan_nol';
					
				}
				if (value.fPPersamaanKanan == null
						|| value.fPPersamaanKanan == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.persamaan_kanan_kosong';
				}
				if (value.fPPersamaanKiri == null
						|| value.fPPersamaanKiri == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.persamaan_kiri_kosong';
				} 
				if (value.fPBatasKiri == null|| value.fPBatasKiri == '') {
					if (value.fPBatasKiri != '0') {
						vm.isValid = false;
						value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.batas_kiri_nol';
					}
				}
				if (value.fPCode == null || value.fPCode == '') {
					vm.isValid = false;
					value.errorMessageKode = 'promise.procurement.master.formulapenilaian.error.kode_kosong';
				}
				
				
			}
			
			return vm.isValid;

		};

		$scope.saveData = function() {

			var data = {};
			vm.errorCode=[];
			
			var penilaianList = [];
			for(var i=0; i < $scope.formulaPenilaianList.length; i++) 
			{
				var value = $scope.formulaPenilaianList[i];
				
//				 Cara 1 
				var dataPenilaian = {
					fPCode:value.fPCode,
					fPBatasKiri : value.fPBatasKiri,
					fPBatasKanan : value.fPBatasKanan,
					fPPersamaanKiri : value.fPPersamaanKiri,
					fPPersamaanKanan : value.fPPersamaanKanan,
					fPPoint: value.fPPoint,
					fPNote: value.fPNote,
					fPDescription: value.fPDescription					
				}
				
				penilaianList.push(dataPenilaian);
				
				/* Cara 2 : Bikin fungsi buat menghilangkan attribute errorMEssage*/
				
			}
			
			data.formulaPenilaianList = penilaianList;
			RequestService
					.doPOSTJSON(
							'/procurement/master/FormulaPenilaianServices/insert',
							data)
					.then(
							function success(data) {
								
								if (data != undefined) {
									if (data.isSave != undefined) {
										
										if (data.errorScore != undefined) {
											for(var i=0; i < data.errorScore.length; i++) {
												var value = $scope.formulaPenilaianList[data.errorScore[i]];
												value.errorMessageKode= 'promise.procurement.master.formulapenilaian.error.score_sama';
											}
										}
										
										if (data.errorKiriKanan != undefined) {
											for(var i=0; i < data.errorKiriKanan.length; i++) {
												var value = $scope.formulaPenilaianList[data.errorKiriKanan[i]];
												value.errorMessageKode= 'promise.procurement.master.formulapenilaian.error.nilai_sama';
											}
										}
										
										if (data.errorBatasKiriKanan != undefined) {
											for(var g=0; g < data.errorBatasKiriKanan.length; g++) {
												var value = $scope.formulaPenilaianList[data.errorBatasKiriKanan[g]];
												value.errorMessageKode= 'promise.procurement.master.formulapenilaian.error.batas_nilai_sama';
											}
										}
										
										if (data.errorCode != undefined) {
											for(var m=0; m < data.errorCode.length; m++) {
												var value = $scope.formulaPenilaianList[data.errorCode[m]];
												value.errorMessageKode= 'promise.procurement.master.formulapenilaian.error.kode_sama';
											}
										}
										
									} else {
										RequestService.informSaveSuccess();
										$state
												.go('app.promise.procurement-master-formulapenilaian');
									}
								}

								/*
								 * console.log(data);
								 * RequestService.informSaveSuccess();
								 * $state.go('app.promise.procurement-master-formulapenilaian');
								 */
							},
							function error(response) {
								$log.info("proses gagal");
								RequestService
										.informError("Terjadi Kesalahan Pada System");
							});

		}

		$scope.getFormulaPenilaian = function() {
			RequestService
					.doGET(
							'/procurement/master/FormulaPenilaianServices/getFormulaPenilaianList')
					.then(function(data, status, headers, config) {
						$scope.formulaPenilaianList = data;
						vm.loading = false;
					});

			if ($scope.formulaPenilaianList.length == 0) {
				$scope.formulaPenilaianList.push({});
			}
		}

		$scope.getFormulaPenilaian();

		$scope.back = function() {
			$state.go('app.promise.procurement-master-formulapenilaian');
		};

		vm.formulaPenilaianAddRow = function() {
			$scope.formulaPenilaianList.push({});
		};

		/*
		 * vm.formulaPenilaianRemoveRow = function (index) {
		 * vm.formulaPenilaianList.splice(vm.formulaPenilaianList.length-1, 1); };
		 */

		// edit suapaya bisa remove sesuai index
		vm.formulaPenilaianRemoveRow = function(index) {
			vm.formulaPenilaianList.splice(index, 1);
		};

	}

	FormulaPenilaianViewController.$inject = [ 'RequestService',
			'ModalService', '$scope', '$http', '$rootScope', '$resource',
			'$location', 'toaster', '$state', '$log' ];

})();