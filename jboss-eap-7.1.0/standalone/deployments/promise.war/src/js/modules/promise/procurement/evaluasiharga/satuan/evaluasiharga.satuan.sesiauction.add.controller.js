/**=========================================================
 * Module: SesiAuctionSatuanViewController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular.module('naut').controller('SesiAuctionSatuanViewController', SesiAuctionSatuanViewController);

    function SesiAuctionSatuanViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
		
		/* ============================ START Detail Pengadaan ================================================ */
        $scope.loading = true;
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        /* ---------------------------- END Detail Pengadaan ------------------------------------------------- */
        
        
        /* ============================ START Bidang Usaha Pengadaan ========================================= */
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            $scope.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Bidang Usaha Pengadaan ------------------------------------------ */
        
        
        /* ============================= START Data Penyedia Barang/Jasa Detail ============================= */
        $scope.dataPenyediaList = $rootScope.listDataPenyedia;
		var dataPenawaranAuction = $rootScope.dataPenawaranAuction;
        /* ----------------------------- END Rincian Data Penyedia Barang/Jasa ------------------------------ */
		
		
		/* ============================ Mapping Sesi Auction from table ===================================== */
        var detailSesiAuction = $rootScope.sesiAuctionDetail;
        //$scope.noSesiAuction = detailSesiAuction.length + 1;
        var noSesiAuc = detailSesiAuction.length + 1;
        $scope.tahunAuction = $filter('date')(new Date(),'MMM-yyyy');
        $scope.jenisPenawaran = $scope.detilPengadaan.jenisPenawaran.nama;
        $scope.noSesiAuction = "Session-" + noSesiAuc + "/" + $scope.pengadaanId + "-" + $scope.jenisPenawaran + "/NoItem-" + $rootScope.itemPId + "/" + $scope.tahunAuction;
        /* ---------------------------- END Mapping Sesi Auction -------------------------------------------- */
		
		
		/* ============================ Sorting Harga Penawaran Awal ======================================== */
        //console.log("Hasil listHarga = " + JSON.stringify($rootScope.listHarga));
        var angka = $rootScope.listHarga;
        var hargaTerendahList = [];
        
        for (var x = 0; x <= angka.length; x++) {
            for (var y = x+1; y<= angka.length+1; y++) {
                if(angka[x] > angka[y]){
                    var temp = angka[x];
                    angka[x] = angka[y];
                    angka[y] = temp;
                }
            }
        }
		
		//console.log("KRITERIA AMBANG BATAS = " + JSON.stringify($rootScope.kriteriaAmbangBatasList));
        if($rootScope.kriteriaAmbangBatasList.length > 0){
            var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
			var barangJasaList = $rootScope.itemBarangJasaList;
			var totalHpsBarangJasa = 0;
			//console.log("BARANG JASA LIST = "+JSON.stringify(barangJasaList));
			angular.forEach(barangJasaList,function(value,index){
				if(barangJasaList[index].id == $rootScope.itemPId){
					totalHpsBarangJasa = totalHpsBarangJasa + barangJasaList[index].totalHPS;
				}
			});
            
            var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah)/100);
            var data = $scope.dataPenyediaList;
			//console.log("Hasil dataPenyediaList = " + JSON.stringify(data));
            var dataIkutAuctionAmbang = [];
            var maksBobotAdmin = $rootScope.maksNilaiLulusAmin;
            var maksBobotTeknis = $rootScope.maksNilaiLulusTeknis;
            var dataNilaiVendor = $rootScope.listSaveDataEval;
            
            for(var i = 0; i < data.length; i++) {
            	for(var j = 0; j < dataNilaiVendor.length; j++) {
            		if(data[i].penawaranPertama.suratPenawaran.vendor.id == dataNilaiVendor[j].evaluasiHargaVendor.vendor.id){
            			if(dataNilaiVendor[j].evaluasiHargaVendor.nilaiAdmin >= maksBobotAdmin) {
            				if(dataNilaiVendor[j].evaluasiHargaVendor.nilaiTeknis >= maksBobotTeknis) {
            					if(data[i].hargaTotalAfterCondition >= itungBatasBawah){
            						hargaTerendahList.push(data[i].hargaTotalAfterCondition);
            	                    dataIkutAuctionAmbang.push(data[i]);
            	                }
            				}
            			}
            		}
            	}
            }
            
            for (var x = 0; x <= hargaTerendahList.length; x++) {
                for (var y = x+1; y<= hargaTerendahList.length+1; y++) {
                    if(hargaTerendahList[x] > hargaTerendahList[y]){
                        var temp = hargaTerendahList[x];
                        hargaTerendahList[x] = hargaTerendahList[y];
                        hargaTerendahList[y] = temp;
                    }
                }
            }
            
			if(dataPenawaranAuction.length > 0)
				$scope.hargaAwal = dataPenawaranAuction[0].penawaranAuction.nilaiPenawaran;
			else
            	$scope.hargaAwal = hargaTerendahList[0];
            
            $scope.dataPenyediaList = dataIkutAuctionAmbang;
        } else {
            $scope.hargaAwal = angka[0];
        }
        $scope.mataUang = $rootScope.mataUangCode;
		/* ----------------------------- END Sorting Harga Penawaran Awal ---------------------------------- */
		
		
		/* ============================= Datepicker ======================================================== */
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[0];
        $scope.tanggalAwalSesiOpen = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAwalSesiOpened = true;
        };
        $scope.tanggalAkhirSesiOpen = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAkhirSesiOpened = true;
        };
        $scope.tanggalSesiAwal = $filter('date')(new Date(),'dd-MMM-yyyy');
        var tanggal = $filter('date')(new Date(), 'EEE MMM d y h:mm a Z');
        $scope.awalSessionTime = new Date(tanggal);
        /* ---------------------------- END Datepicker --------------------------------------------------- */
		
		
		/* ============================ START Validasi Data Inputan ====================================== */
		$scope.validasiTimeAkhir = function() {
            // NOTE: Bila terjadi perubahan terhadap inputan di database terhadap REGEX waktu di jadwal Pengadaan silahkan UBAH
            // Validasi Tanggal Inputan dengan Jadwal
            var tanggalAkhir = new Date($rootScope.tglAkhirJadwalP);
            var maxDate = $filter('date')(tanggalAkhir, 'dd-MMM-yyyy');
            var tanggalInputAkhir = document.getElementsByName("date_akhir")[0].value;
            var tglSekarang = $filter('date')(new Date(), 'dd-MMM-yyyy');
            
            // Validasi Waktu Inputan dengan Jadwal
            var waktuAkhir = new Date($rootScope.waktuAkhirJadwalP);
            var maxTime = $filter('date')(waktuAkhir, 'HH:mm');
            var waktuInputAkhir = document.getElementsByName("input_akhir")[0].value;
            var waktuSekarang = $filter('date')(new Date(), 'HH:mm');
            
            if(tanggalInputAkhir > maxDate){
                toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir melebihi dari Jadwal Pengadaan! ('+maxDate+')');
                document.getElementsByName("date_akhir")[0].focus();
            } else if(tanggalInputAkhir == maxDate && waktuInputAkhir > maxTime){
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi dari Jadwal Pengadaan! ('+maxTime+')');
                document.getElementsByName("date_akhir")[0].focus();
            } else if(tanggalInputAkhir == tglSekarang && waktuInputAkhir < waktuSekarang){
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi Waktu hari ini ('+waktuSekarang+')');
                document.getElementsByName("input_akhir")[0].focus();
            }
        }
		
		// untuk peserta Auction dari vendor yang akan ikut
        var vendorCheck = [];
        $scope.pesertaAuction = function(vendor, ikut) {
            if(ikut) {
                vendorCheck.push(vendor);
            } else {
                vendorCheck.splice(vendorCheck.indexOf(vendor), 1);
            }
            //console.log("vendorCheck = " + JSON.stringify(vendorCheck));
        }
		
		$scope.validasiInput = function() {
            var tglAwalSesi = new Date($scope.tanggalSesiAwal);
            $scope.validasiTimeAkhir();
            if($scope.tanggalSesiAkhir == undefined) {
                toaster.pop('warning', 'Field Kosong', 'Tanggal Akhir Sesi belum terisi');
                document.getElementsByName("date_akhir")[0].focus();
            } else if(tglAwalSesi > $scope.tanggalSesiAkhir) { 
                toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir Sesi harus lebih besar dari Tanggal Awal Sesi');
                document.getElementsByName("date_akhir")[0].focus();
            } else if($scope.akhirSessionTime == undefined) { 
                toaster.pop('warning', 'Field Kosong', 'Waktu Akhir Sesi belum terisi');
                document.getElementsByName("input_akhir")[0].focus();
            } else if($scope.selisihPenawaran == undefined) { 
                toaster.pop('warning', 'Field Kosong', 'Selisih Penawaran belum terisi');
                document.getElementsByName("selisih_penawaran")[0].focus();
            } else if(vendorCheck.length == 0) {
                toaster.pop('warning', 'Check-Box Kosong', 'Selisih Penawaran belum terisi');
            } else {
                $scope.save();
                $scope.go('/app/promise/procurement/evaluasiharga/satuan/auction/view');
            }
        }
		/* ---------------------------- END Validasi Data Inputan ---------------------------------------- */
        
        
        /* ============================ START CONTROLLER DETAIL ========================================== */
        // fungsi untuk pengurutan nilai
        var sortNilai = function(nilai, type) {
            var angka = nilai;
            for (var x = 0; x <= angka.length; x++) {
                for (var y = x+1; y<= angka.length+1; y++) {
                    if(type == 'dsc'){
                        if(angka[x] < angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }
                    } else {
                        if(angka[x] > angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }    
                    }
                }
            }
            return angka[0];
        }
        
        // fungsi tombol kembali
        $scope.back = function() {
            $scope.go('/app/promise/procurement/evaluasiharga/satuan/auction/addview');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
		
		// ################### Simpan data-nya ke Table ######################
		$scope.save = function() {
			simpanSesi();
		}
		
        // 1. Simpan data Sesi Auction
		var simpanSesi = function() {
			var tglAwalAuction = $scope.tanggalSesiAwal + " " + document.getElementsByName("input_awal")[0].value;
            var tglAwalSesi = $filter('date')($scope.tanggalSesiAkhir,'dd-MMM-yyyy');
            var tglAkhirAuction = tglAwalSesi + " " + document.getElementsByName("input_akhir")[0].value;
            var dataSesiAuction = {
                noSesi: $scope.noSesiAuction,
                waktuAwal: new Date(tglAwalAuction),
                waktuAkhir: new Date(tglAkhirAuction),
                keterangan: $scope.keterangan,
                hargaAwal: $scope.hargaAwal,
                selisihPenawaran: $scope.selisihPenawaran,
                pengadaan: $scope.pengadaanId,
				itemPengadaan: $rootScope.itemPId,
                status: 0
            }
			
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataSesiAuction
            }).success(function(data, status, headers, config) {
                if (typeof data !== 'undefined'){
                    var dataSesiAuctionId = data.id;
                    savePesertaAuction(dataSesiAuctionId);
                }
                //console.log("CREATE AUCTION SESSION OK : " + JSON.stringify(data));
            });
		}
		
		// 2. Simpan data Peserta Auction
		var savePesertaAuction = function(dataSesiAuctionId) {
			console.log("vendorCheck = " + JSON.stringify(vendorCheck));
            for(var i=0; i<vendorCheck.length; i++) {
				var dataPesertaAuction = {
					vendor: vendorCheck[i].id,
                    auction: dataSesiAuctionId
                }
                    
                $http({
					method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/PesertaAuctionServices/create',
                    headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function(obj) {
                       var str = [];
                       for (var p in obj)
                          str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                       return str.join("&");
                    },
                    data: dataPesertaAuction
				}).success(function(data, status, headers, config) {
                   //console.log("CREATE PESERTA AUCTION OK : " + JSON.stringify(data));
				});
			}
		}
		
		// 3. Simpan data Penawaran Auction
        var savePenawaranAuction = function(dataVendorAuctionId, nilaiPenawaranOri, nilaiPenawaran, nilaiPenawaranSatuan, nilaiPenawaranSatuanOri, suratPenawaranId) {
            var dataPenawaranAuction = {
                nilaiPenawaran: nilaiPenawaran,
                nilaiPenawaranOri: nilaiPenawaranOri,
                pesertaAuction: dataVendorAuctionId,
                suratPenawaran: suratPenawaranId
            }
                    
			$rootScope.hargaSatuanAfterCondition = nilaiPenawaranSatuan;
			$rootScope.hargaSatuanOri = nilaiPenawaranSatuanOri;
			
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataPenawaranAuction
            }).success(function(data, status, headers, config) {
                //console.log("CREATE PENAWARAN AUCTION OK : " + JSON.stringify(data));
				if (typeof data !== 'undefined'){
					var dataPenawaranAuctionId = data.id;
					var hargaSatuanOri = $rootScope.hargaSatuanOri;
					var hargaSatuanAfterCondition = $rootScope.hargaSatuanAfterCondition;
					var hargaTotalOri = data.nilaiPenawaranOri;
					var hargaTotalAfterCondition = data.nilaiPenawaran;
					savePenawaranAuctionDetail(dataPenawaranAuctionId, hargaSatuanOri, hargaSatuanAfterCondition, hargaTotalOri, hargaTotalAfterCondition);
				}
            });
		}
		
		var savePenawaranAuctionDetail = function(dataPenawaranAuctionId, hargaSatuanOri, hargaSatuanAfterCondition, hargaTotalOri, hargaTotalAfterCondition) {
			var itemPengadaanId = $rootScope.itemPId;
			var dataPenawaranAuctionDetail = {
                penawaranAuction: dataPenawaranAuctionId,
				itemPengadaan: itemPengadaanId,
				hargaSatuanOri: hargaSatuanOri,
				hargaSatuanAfterCondition: hargaSatuanAfterCondition,
				hargaTotalOri: hargaTotalOri,
				hargaTotalAfterCondition: hargaTotalAfterCondition
            }
			
			$http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataPenawaranAuctionDetail
            }).success(function(data, status, headers, config) {
				console.log("CREATE PENAWARAN AUCTION DETAIL OK : " + JSON.stringify(data));
			});
		}
		/* ---------------------------- END Rincian Controller ------------------------------------------- */
	}
    
    SesiAuctionSatuanViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();