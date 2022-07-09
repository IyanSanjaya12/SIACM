/**=========================================================
 * Module: RekapPengadaanController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RekapPengadaanViewController', RekapPengadaanViewController);

	function RekapPengadaanViewController($scope, $http, $rootScope, $resource, $location, $state, ngTableParams, $stateParams) {
		var form = this;
		
		form.dataPengadaan = $stateParams.dataPengadaan;
				
		// find anggaran pengadaan
		$http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AnggaranPengadaanServices/getAnggaranPengadaanListByPengadaanId/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.anggaranPengadaan = data[0];
	    });
				
		// find Bidang Usaha Pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.subBidangUsahaList = data;
	    });
		
		// Jadwal Prakualifikasi
		// proses kualifikasi
		
		// Jadwal pengadaan 
		$http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanId/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.jadwalPengadaanList = data;
	    	form.dataPengadaan.lastTahapan = 1;
	    	// registrasi jadwal tahapan pengadaan
	    	$http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/getRegisterJadwalByAlurPengadaanId/' + form.dataPengadaan.alurPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.jadwalPengadaanList !== undefined && form.dataPengadaan.jadwalPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.jadwalPengadaanList, function(jadwalPengadaan, index){
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(registrasiJadwal, index){
		    					if (jadwalPengadaan.tahapanPengadaan.id === registrasiJadwal.tahapanPengadaan.id) {
		    						jadwalPengadaan.statusLokasi = registrasiJadwal.isLokasi;
		    					}
		    					form.dataPengadaan.lastTahapan = registrasiJadwal.tahapanPengadaan.tahapan.id;
		    				});
		    			}
		    		});
		    	}
		    });
	    	// berita acara
	    	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/beritaAcaraServices/getListByPengadaanAndTahapan/' + form.dataPengadaan.id + '/' + form.dataPengadaan.lastTahapan )
		    .success(function (data, status, headers, config) {
		    	if (data !== undefined && data.length > 0 && data[0] !== undefined) {
		    		form.dataPengadaan.beritaAcara = data[0];
		    	}
		    });
	    });
		
		// vendor yang terdaftar di pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.vendorPengadaanList = data;
	    	
	    	// pengambilan dokumen
	    	$http.get($rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/getByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.statusPengambilanDokumen = 'T';
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(pengambilanDokumen, index){
		    					if (dataVendor.vendor.id === pengambilanDokumen.vendor.id) {
		    						dataVendor.statusPengambilanDokumen = 'Y';
								}
		    				});
				    	}
		    		});
		    	}		
		    });
	    	
	    	// penjelasan
	    	$http.get($rootScope.backendAddress + '/procurement/penjelasan/vendorPenjelasanServices/getListByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.statusPenjelasan = 'T';
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(penjelasan, index){
		    					if (dataVendor.vendor.id === penjelasan.vendor.id) {
		    						dataVendor.statusPenjelasan = 'Y';
								}
		    				});
				    	}
		    		});
		    	}		
		    });
	    	
	    	// pemasukan penawaran
	    	$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	form.dataPengadaan.suratPenawaranList = data;
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.statusPemasukan = 'T';
		    			dataVendor.evaluasiHarga = 0;
		    			dataVendor.hargaNegosiasi = 0;
		    			if (form.dataPengadaan.suratPenawaranList !== undefined && form.dataPengadaan.suratPenawaranList.length > 0) {
		    				angular.forEach(form.dataPengadaan.suratPenawaranList, function(pemasukan, index){
		    					if (dataVendor.vendor.id === pemasukan.vendor.id) {
		    						dataVendor.statusPemasukan = 'Y';
								}
		    				});
				    	}
    					// harga penawaran
    					$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + form.dataPengadaan.id + '/' + dataVendor.vendor.id)
    				    .success(function (data, status, headers, config) {
    				    	if (data !== undefined && data.length > 0) {
    				    		angular.forEach(data, function(penawaranPertama, index){
    				    			dataVendor.evaluasiHarga = penawaranPertama.nilaiPenawaran;
    				    		})
    				    	}
    				    });
    					// harga negosiasi
    					$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + form.dataPengadaan.id + '/' + dataVendor.vendor.id)
    				    .success(function (data, status, headers, config) {
    				    	if (data !== undefined && data.length > 0) {
    				    		dataVendor.hargaNegosiasi = data[0].nilaiPenawaranAfterCondition;
    				    	}
    				    });    					
		    		});
		    	}		
		    });
	    	
	    	// pembukaan penawaran
	    	$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirVendorServices/getDaftarHadirVendorListByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.statusPembukaan = 'T';
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(pembukaan, index){
		    					if (dataVendor.vendor.id === pembukaan.vendor.id) {
		    						dataVendor.statusPembukaan = 'Y';
								}
		    				});
				    	}
		    		});
		    	}		
		    });
	    	
	    	// pembukaan penawaran
	    	$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirVendorServices/getDaftarHadirVendorListByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.statusPembukaan = 'T';
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(pembukaan, index){
		    					if (dataVendor.vendor.id === pembukaan.vendor.id) {
		    						dataVendor.statusPembukaan = 'Y';
								}
		    				});
				    	}
		    		});
		    	}		
		    });
	    	
	    	// evaluasi admin, teknis, harga
	    	$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
		    			dataVendor.evaluasiAdmin = 0;
		    			dataVendor.evaluasiTeknis = 0;
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(evaluasi, index){
		    					if (dataVendor.vendor.id === evaluasi.vendor.id) {
		    						dataVendor.evaluasiAdmin = evaluasi.nilaiAdmin;
		    						dataVendor.evaluasiTeknis = evaluasi.nilaiTeknis;
								}
		    				});
				    	}
		    		});
		    	}		
		    });
	    	
	    	form.tableProsesPengadaan = new ngTableParams({
	            page: 1, // show first page
	            count: 5 // count per page
	        }, {
	            total: form.dataPengadaan.vendorPengadaanList.length, // length of data4
	            getData: function ($defer, params) {
	                $defer.resolve(form.dataPengadaan.vendorPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	            }
	        }); 				
	    });
		
		// penetapan pemenang
		$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getPenetapanPemenangTotalByPengadaan/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	if (data !== undefined && data.length > 0 && data[0] !== undefined) {
	    		form.dataPengadaan.penetapanPemenang = data[0];
	    	}
	    });
		
		// Material pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + form.dataPengadaan.id)
        .success(function(data, status, headers, config) {
            form.dataPengadaan.materialPengadaanList = [];
            form.dataPengadaan.jasaPengadaanList = [];
            form.dataPengadaan.totalNilaiMaterial = 0;
            form.dataPengadaan.totalNilaiJasa = 0;
            
            if (data !== undefined && data.length > 0) {
                angular.forEach(data, function(itemPengadaan, index){
            		if (itemPengadaan.item.itemType.id === 1) {
            			form.dataPengadaan.materialPengadaanList.push(itemPengadaan);
            			form.dataPengadaan.totalNilaiMaterial = form.dataPengadaan.totalNilaiMaterial + itemPengadaan.totalHPS;
            		} else {
            			form.dataPengadaan.jasaPengadaanList.push(itemPengadaan);
            			form.dataPengadaan.totalNilaiJasa = form.dataPengadaan.totalNilaiJasa + itemPengadaan.totalHPS;
            		}
            	});
            }
            
            form.dataPengadaan.nilaiPPNMaterial = form.dataPengadaan.prosentasePajakMaterial * form.dataPengadaan.totalNilaiMaterial / 100;
            form.dataPengadaan.nilaiPPNJasa = form.dataPengadaan.prosentasePajakJasa * form.dataPengadaan.totalNilaiJasa / 100;
            form.dataPengadaan.totalNilaiMaterialAfterPPN = form.dataPengadaan.totalNilaiMaterial - form.dataPengadaan.nilaiPPNMaterial;
            form.dataPengadaan.totalNilaiJasaAfterPPN = form.dataPengadaan.totalNilaiJasa - form.dataPengadaan.nilaiPPNJasa;
            
            form.tableMaterialPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.dataPengadaan.materialPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.dataPengadaan.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            
            form.tableJasaPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.dataPengadaan.jasaPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.dataPengadaan.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
        });
		
		form.btnKembali = function() {
			$state.go('app.promise.procurement-panitia-rekappengadaan-index');
		}
	}

	RekapPengadaanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource','$location', '$state', 'ngTableParams', '$stateParams'];

})();