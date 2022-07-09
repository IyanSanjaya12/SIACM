/**=========================================================
 * Module: RegistrasiDataKeuanganController.js
 * Author: F.H.K
 =========================================================*/


(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataKeuanganViewController', RegistrasiDataKeuanganViewController);

	function RegistrasiDataKeuanganViewController( $scope, $rootScope, $filter, ngTableParams, $state,RequestService,$stateParams,$log) {
		
		var vm = this;
		vm.dataKeuangan = ($stateParams.dataKeuangan != undefined) ? $stateParams.dataKeuangan : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
        vm.loginId = $rootScope.userLogin.user.id;
        vm.status = ($stateParams.status != undefined) ? $stateParams.status : {};
        vm.temp={};
        vm.tanggalAuditStatus = false;
        $scope.tanggalAuditOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.tanggalAuditStatus = true;
        };
        
    if(vm.toDo=='edit'){
    	vm.temp.tahunKeuangan={tahun:vm.dataKeuangan.tahunKeuangan};
    }    
    
    if(vm.toDo == 'add'){
    	
    	vm.dataKeuangan.kas = 0;
    	vm.dataKeuangan.bank = 0;
    	vm.dataKeuangan.totalPiutang = 0;
		vm.dataKeuangan.persediaanBarang = 0;
		vm.dataKeuangan.pekerjaanDalamProses = 0;
		vm.dataKeuangan.peralatanDanMesin = 0;
		vm.dataKeuangan.inventaris = 0;
		vm.dataKeuangan.gedungGedung = 0;
		vm.dataKeuangan.aktivaLainnya = 0;
		vm.dataKeuangan.hutangDagang = 0;
		
		vm.dataKeuangan.hutangPajak = 0;
		vm.dataKeuangan.hutangLainnya = 0;
		vm.dataKeuangan.kekayaanBersih = 0;
		vm.dataKeuangan.hutangJangkaPanjang = 0;
		vm.dataKeuangan.totalAktivaLancar = 0;
		vm.dataKeuangan.totalAktivaTetap = 0;
		vm.dataKeuangan.totalHutangJangkaPendek = 0;
		vm.dataKeuangan.kekayaanBersih = 0;	
	}
    
     vm.tahunKeuanganList = [];
        var yearNow = $filter('date')(new Date(), 'yyyy');
        var k=0;
        for(var i=yearNow; i > (yearNow-5);i-- ){
            vm.tahunKeuanganList.push({
                
                tahun:i
            });
            k++;
        }
      //Aktiva
        $scope.tambahAktivaLancar = function() {  
            vm.dataKeuangan.totalAktivaLancar = vm.dataKeuangan.kas + vm.dataKeuangan.bank + vm.dataKeuangan.totalPiutang 
            + vm.dataKeuangan.persediaanBarang + vm.dataKeuangan.pekerjaanDalamProses;
        }
        
        $scope.tambahAktivaTetap = function() { 
        	vm.dataKeuangan.totalAktivaTetap = vm.dataKeuangan.peralatanDanMesin + vm.dataKeuangan.inventaris + vm.dataKeuangan.gedungGedung;
        }
        
        $scope.totalAktiva = function() {
        	vm.dataKeuangan.totalAktiva = vm.dataKeuangan.totalAktivaLancar + vm.dataKeuangan.totalAktivaTetap + vm.dataKeuangan.aktivaLainnya;
        }
        
        //Pasiva
        $scope.tambahPasiva = function() {
        	vm.dataKeuangan.totalHutangJangkaPendek = vm.dataKeuangan.hutangDagang + vm.dataKeuangan.hutangPajak + vm.dataKeuangan.hutangLainnya;
        }
        
        $scope.rugiLaba = function() {
        	vm.dataKeuangan.kekayaanBersih = (vm.dataKeuangan.kas + vm.dataKeuangan.bank + vm.dataKeuangan.totalPiutang 
        	        + vm.dataKeuangan.persediaanBarang + vm.dataKeuangan.pekerjaanDalamProses 
        	        + vm.dataKeuangan.peralatanDanMesin + vm.dataKeuangan.inventaris + vm.dataKeuangan.gedungGedung + vm.dataKeuangan.aktivaLainnya) 
        	        - (vm.dataKeuangan.hutangDagang + vm.dataKeuangan.hutangPajak + vm.dataKeuangan.hutangLainnya + vm.dataKeuangan.hutangJangkaPanjang);
        }
        
        $scope.totalPasiva = function() {
        	vm.dataKeuangan.totalPasiva = ((vm.dataKeuangan.kas + vm.dataKeuangan.bank + vm.dataKeuangan.totalPiutang 
        	        + vm.dataKeuangan.persediaanBarang + vm.dataKeuangan.pekerjaanDalamProses 
        	        + vm.dataKeuangan.peralatanDanMesin + vm.dataKeuangan.inventaris + vm.dataKeuangan.gedungGedung + vm.dataKeuangan.aktivaLainnya) 
        	        - (vm.dataKeuangan.hutangDagang + vm.dataKeuangan.hutangPajak + vm.dataKeuangan.hutangLainnya + vm.dataKeuangan.hutangJangkaPanjang)) 
        	        + (vm.dataKeuangan.hutangDagang + vm.dataKeuangan.hutangPajak + vm.dataKeuangan.hutangLainnya + vm.dataKeuangan.hutangJangkaPanjang);
        }
        
        
		
		$scope.save = function() {
			
			RequestService.modalConfirmation().then(function(result) {
				if($scope.validateForm()){
				vm.dataKeuangan.tahunKeuangan=vm.temp.tahunKeuangan.tahun;
				
				$scope.saveData();
				}
			});
		};

		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorNomorAudit = "";
			vm.errorTanggalAudit = "";
			vm.errorNamaAudit  = "";
			vm.errorTahunKeuangan = "";
			vm.errorAktiva="";
			vm.errorPasiva = "";

			if (typeof vm.dataKeuangan.nomorAudit === 'undefined' || vm.dataKeuangan.nomorAudit == '') {
				vm.errorNomorAudit = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataKeuangan.tanggalAudit === 'undefined' || vm.dataKeuangan.tanggalAudit == '') {
				vm.errorTanggalAudit = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.dataKeuangan.namaAudit === 'undefined' || vm.dataKeuangan.namaAudit == '') {
				vm.errorNamaAudit = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.temp.tahunKeuangan === 'undefined' || vm.temp.tahunKeuangan == '') {
				vm.errorTahunKeuangan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			
			if(vm.dataKeuangan.totalAktiva == undefined || vm.dataKeuangan.totalAktiva == "") {
				vm.isValid = false;
				vm.errorAktiva = 'promise.procurement.RegistrasiVendor.DataKeuangan.error.aktiva_blum_lengkap';
	        }
			if (vm.dataKeuangan.totalPasiva == undefined || vm.dataKeuangan.totalPasiva == '') {
				vm.errorPasiva = 'promise.procurement.RegistrasiVendor.DataKeuangan.error.pasiva_blum_lengkap';
			
				vm.isValid = false;
			}
			
			return vm.isValid;
		}

		$scope.saveData = function() {
			vm.url = '';
			var data = {
					status : vm.status
				}
			
			vm.keuanganVendorDraft= {
					id:vm.dataKeuangan.id,
					aktivaLainnya 	:vm.dataKeuangan.aktivaLainnya,
					bank			: vm.dataKeuangan.bank,
					gedungGedung	:vm.dataKeuangan.gedungGedung,
					hutangDagang	:vm.dataKeuangan.hutangDagang,
					hutangJangkaPanjang :vm.dataKeuangan.hutangJangkaPanjang,
					hutangLainnya		:vm.dataKeuangan.hutangLainnya,
					hutangPajak			:vm.dataKeuangan.hutangPajak,
					inventaris			:vm.dataKeuangan.inventaris,
					kas					:vm.dataKeuangan.kas,
					kekayaanBersih		:vm.dataKeuangan.kekayaanBersih,
					namaAudit			:vm.dataKeuangan.namaAudit,
					nomorAudit			:vm.dataKeuangan.nomorAudit,
					pekerjaanDalamProses :vm.dataKeuangan.pekerjaanDalamProses,
					peralatanDanMesin	:vm.dataKeuangan.peralatanDanMesin,
					persediaanBarang	:vm.dataKeuangan.persediaanBarang,
					tahunKeuangan		:vm.dataKeuangan.tahunKeuangan,
					tanggalAudit		:vm.dataKeuangan.tanggalAudit,
					totalAktiva			:vm.dataKeuangan.totalAktiva,
					totalAktivaLancar	:vm.dataKeuangan.totalAktivaLancar,
					totalAktivaTetap	:vm.dataKeuangan.totalAktivaTetap,
					totalHutangJangkaPendek	:vm.dataKeuangan.totalHutangJangkaPendek,
					totalPasiva				:vm.dataKeuangan.totalPasiva,
					totalPiutang		:vm.dataKeuangan.totalPiutang,
					userId 			:vm.dataKeuangan.userId,
					vendor 			:vm.dataKeuangan.vendor
					}		
			if(vm.status == 0 && vm.toDo=="edit"){
				data.keuanganVendor = vm.keuanganVendorDraft;
			}else
			{
				vm.keuanganVendorDraft.keuanganVendor=vm.dataKeuangan.keuanganVendor;
				data.keuanganVendorDraft=vm.keuanganVendorDraft;
			}
						
			if (vm.toDo == "add") {
				vm.url = '/procurement/vendor/KeuanganVendorServices/insert';
			}

			if (vm.toDo == "edit") {
				vm.keuanganVendorDraft.created = vm.dataKeuangan.created;
				vm.keuanganVendorDraft.isDelete = vm.dataKeuangan.isDelete;
				vm.url = '/procurement/vendor/KeuanganVendorServices/update';
			}
			
			data.loginId=vm.loginId;
			
			vm.keuanganVendorDTO=data;
			RequestService.doPOSTJSON(vm.url, vm.keuanganVendorDTO)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								vm.errorNamaSatuan = 'promise.procurement.master.satuan.error.nama_sama';
							}
						} else {
							
							RequestService.modalInformation("template.informasi.simpan_sukses",'success');
							
							$state.go('appvendor.promise.procurement-vendor-datakeuangan');
						}
	
					}
				}, function error(response) {
					$log.info("proses gagal");
		        	
		        	RequestService.modalInformation("template.informasi.gagal",'danger');
				});

		}
		
        
       
		 $scope.back = function() {
		        $state.go('appvendor.promise.procurement-vendor-datakeuangan');	      	
		        }
         
        /* ------------------------------------------------------------------------------------------------ */
		
	}
	
	RegistrasiDataKeuanganViewController.$inject = ['$scope', '$rootScope', '$filter', 'ngTableParams', '$state' ,'RequestService','$stateParams','$log'];

})();