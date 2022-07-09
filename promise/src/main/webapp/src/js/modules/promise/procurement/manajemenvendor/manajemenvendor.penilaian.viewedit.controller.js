
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenilaianVendorViewEditController', PenilaianVendorViewEditController);

	function PenilaianVendorViewEditController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state, $q) {
		var vm = this;
		vm.dataVendor = $rootScope.penilaianDataVendor; 
		vm.penilaianVendor = $rootScope.penilaianVendor; 
		
		vm.getMataUang = function () { 
			RequestService.doGET('/procurement/master/mata-uang/get-list')
			.then(function (data, status, headers, config) { 
				vm.mataUangList = data; 
			}); 
		}
		
		vm.getMataUang();
		vm.indikatorPenilaianList = [];
		vm.formulaPenilaianList = [];
		
		vm.getPenilaianVendorIndikator = function () { 
			RequestService.doGET('/procurement/vendormanagement/PenilaianVendorIndikatorServices/getPenilaianVendorIndikatorGetByPenilaian/' + vm.penilaianVendor.vPerfId)
			.then(function (data, status, headers, config) { 
				vm.penilaianVendorIndikatorList = data;
				
				vm.penilaianVendorIndikatorList.forEach(function(penilaianVendorIndikator, index, array) {

					vm.indikatorPenilaianList.push(penilaianVendorIndikator.indikatorPenilaian);
					
					RequestService.doGET('/procurement/vendormanagement/PenilaianVendorIndikatorDetailServices/getFormulaPenilaianByPenilaianIndikator/' + penilaianVendorIndikator.vPerfAspId)
					.then(function (data, status, headers, config) {
						vm.formulaPenilaianList = [];
						vm.formulaPenilaianList = data;
						vm.loading = false;
					});
				});
			}); 
		}
		
		vm.getPenilaianVendorIndikator();
		
		
		vm.penilaianBack = function(){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
		}
		
		//get deskripsi penilaian
		vm.getDeskripsiPenilaian = function (score) { 
			RequestService.doGET('/procurement/master/FormulaPenilaianServices/getDeskripsiPenilaian/'+score)
			.then(function (data, status, headers, config) { 
				vm.penilaianVendor.vPerfKesimpulanPenilaian = data.deskripsi;
				vm.loading = false;
			}); 
		}
		
		//looping dipindah ke beckend
		/*vm.getFinalNote = function(finalScore){
			vm.formulaPenilaianList.forEach(function(formula, index, array) {
				if(formula.fPPersamaanKiri == 'ge'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}
				}else if(formula.fPPersamaanKiri == 'le'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}
				}else if(formula.fPPersamaanKiri == 'gt'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}
				}else if(formula.fPPersamaanKiri == 'lt'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; } 
					}
				}
			});
		};*/
		
		vm.formulaPenilaian = function(fp){
			var result = '';
			if(fp != null && fp != ''){
				result = fp.fPBatasKiri + ' ';

				if(fp.fPPersamaanKiri == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKiri == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKiri == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKiri == 'lt'){ result = result + ' <';}

				result = result + ' x ';
								
				if(fp.fPPersamaanKanan == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKanan == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKanan == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKanan == 'lt'){ result = result + ' <';}

				result = result + ' ' + fp.fPBatasKanan;
				
			}
			return result;
		}
		
		vm.calculateValues = function(indikatorId, formulaId, scoreInput){
  
			vm.penilaianVendor.vPerfNilaiAkhir = 0
			var nilaiAkhir = 0;
			vm.penilaianVendorIndikatorList.forEach(function(penilaianVendor, index, array) {
				if(penilaianVendor.indikatorPenilaian.iPId == indikatorId){
					
					var finalScoreDetail = (Number(penilaianVendor.indikatorPenilaian.iPBobot)  * Number(scoreInput)) / 100;
					penilaianVendor.vPerfAspNilaiAkhir = finalScoreDetail.toFixed(2);
					
					penilaianVendor.formulaPenilaian.fPId = formulaId;
				}

				nilaiAkhir = (Number(nilaiAkhir) + (penilaianVendor.vPerfAspNilaiAkhir == null ? Number(0) : Number(penilaianVendor.vPerfAspNilaiAkhir))).toFixed(2);
			});
			vm.penilaianVendor.vPerfNilaiAkhir = nilaiAkhir;
			if(vm.penilaianVendor.vPerfNilaiAkhir > 0){ 
				//vm.getFinalNote(vm.penilaianVendor.vPerfNilaiAkhir);
				vm.getDeskripsiPenilaian(vm.penilaianVendor.vPerfNilaiAkhir);
			}
		}
		
		vm.penilaianSave = function(){
			vm.mdlName = 'Data Pengadaan';
			/** validasi **/
			var next = true;
			if(vm.penilaianVendor.vPerfTglPenilaian == null || vm.penilaianVendor.vPerfTglPenilaian == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal penilaian tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.purchaseOrder.poNumber == null || vm.penilaianVendor.purchaseOrder.poNumber == ''){
				toaster.pop('error', vm.mdlname, 'Nomor Purchase Order tidak boleh kosong!');
				next = false;
			}/*else if(vm.penilaianVendor.vPerfTglKontrak == null || vm.penilaianVendor.vPerfTglKontrak == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal kontrak tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfNomorPengadaan == null || vm.penilaianVendor.vPerfNomorPengadaan == ''){
				toaster.pop('error', vm.mdlname, 'Nomor pengadaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfPaketPekerjaan == null || vm.penilaianVendor.vPerfPaketPekerjaan == ''){
				toaster.pop('error', vm.mdlname, 'Paket pekerjaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfPenggunaHasilPekerjaan == null || vm.penilaianVendor.vPerfPenggunaHasilPekerjaan == ''){
				toaster.pop('error', vm.mdlname, 'Pengguna hasil pekerjaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.mataUang  == null || vm.penilaianVendor.mataUang == ''){
				toaster.pop('error', vm.mdlname, 'Mata uang harus dipilih!');
				next = false;
			}else if(vm.penilaianVendor.vPerfNilaiKontrak == null || vm.penilaianVendor.vPerfNilaiKontrak == ''){
				toaster.pop('error', vm.mdlname, 'Nilai kontrak tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfKontrakTglAwalKerja == null || vm.penilaianVendor.vPerfKontrakTglAwalKerja == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal awal kerja tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfKontrakTglAkhirKerja == null || vm.penilaianVendor.vPerfKontrakTglAkhirKerja == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal akhir kerja tidak boleh kosong!');
				next = false;
			}*/else if(vm.penilaianVendor.vPerfCatatanUser == null || vm.penilaianVendor.vPerfCatatanUser == ''){
				toaster.pop('error', vm.mdlname, 'Catatan user tidak boleh kosong!');
				next = false;
			}
			
			if(next == true){
				var data = {}
				data.penilaianVendor = vm.penilaianVendor;
				data.penilaianVendorIndikatorList = vm.penilaianVendorIndikatorList;
				 
				ModalService.showModalConfirmation().then(function (result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/procurement/vendormanagement/PenilaianVendorServices/updatePenilaianVendorAll', data)
					.then(function successCallback(response) {
						$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
						ModalService.closeModalInformation();
					 }, function errorCallback(response) {
						ModalService.closeModalInformation();
						ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
				});
			}			
		}
	}

	PenilaianVendorViewEditController.$inject = ['ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state', '$q'];

})();

