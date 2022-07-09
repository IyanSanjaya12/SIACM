(function () {
    'use strict';

    angular.module('naut').controller('SesiAuctionViewController', SesiAuctionViewController);

    function SesiAuctionViewController(ModalService, $scope, $http, $rootScope, $location, toaster, $resource, $filter, RequestService, $q) {
    	

        /* ============================ START Detail Pengadaan ================================================ */
        $scope.loading = true;
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            //console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;

        /* ============================ START Bidang Usaha Pengadaan ========================================= */
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId)
            .success(function (data, status, headers, config) {
                $scope.subBidangUsahaByPengadaanIdList = data;
                $scope.loading = false;
            })
            .error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        /* ---------------------------- END Bidang Usaha Pengadaan ------------------------------------------ */


        /* ============================= START Data Penyedia Barang/Jasa Detail ============================ */
        $scope.dataPenyediaList = $rootScope.listDataPenyedia;
        var dataPenawaranAuction = $rootScope.dataPenawaranAuction;
        /* ----------------------------- END Rincian Data Penyedia Barang/Jasa ----------------------------- */


        /* ============================ Mapping Sesi Auction from table ===================================== */
        var detailSesiAuction = $rootScope.sesiAuctionDetail;
        //$scope.noSesiAuction = detailSesiAuction.length + 1;
        var noSesiAuc = detailSesiAuction.length + 1;
        $scope.tahunAuction = $filter('date')(new Date(), 'MMM-yyyy');
        if ($scope.detilPengadaan.jenisPenawaran.id == 1)
            $scope.jenisPenawaran = $scope.detilPengadaan.jenisPenawaran.nama;
        else
            $scope.jenisPenawaran = "Item_Id";
        
        $scope.noSesiAuction = "Session-" + noSesiAuc + "/" + $scope.pengadaanId + "-" + $scope.jenisPenawaran + "/" + $scope.tahunAuction;
        /* ---------------------------- END Mapping Sesi Auction -------------------------------------------- */


        /* ============================ Sorting Harga Penawaran Awal ======================================== */
        //console.log("Hasil listHarga = " + JSON.stringify($rootScope.listHarga));
        var angka = $rootScope.listHarga;
        var hargaTerendahList = [];

        for (var x = 0; x <= angka.length; x++) {
            for (var y = x + 1; y <= angka.length + 1; y++) {
                if (angka[x] > angka[y]) {
                    var temp = angka[x];
                    angka[x] = angka[y];
                    angka[y] = temp;
                }
            }
        }

        function asyncGetLastAuction(pengadaanId) {
            return $q(function (resolve) {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + "/procurement/evaluasiharga/PenawaranAuctionServices/getDistinctPenawaranAuctionAndLastAuctionWithKursByPengadaan/" + $scope.pengadaanId)
                    .success(function (lastPenawaranAuction) {
                        $scope.loading = false;
                        resolve(lastPenawaranAuction);
                    });
            });
        }
        
        console.log('isi root scope data penyedia '+JSON.stringify($rootScope.listDataPenyedia));

        var promise = asyncGetLastAuction($scope.pengadaanId);
        promise.then(function (rsLastAuction) {
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + $scope.pengadaanId).success(function (kursPengadaan, status, headers, config) {
                if ($rootScope.kriteriaAmbangBatasList.length > 0) {
                    var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
                    var ambangAtas = $rootScope.kriteriaAmbangBatasList[0].atas;
                    var totalHpsBarangJasa = $rootScope.totalHpsMaterial + $rootScope.totalHpsJasa;
                    var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah) / 100);
                    var itungBatasAtas = totalHpsBarangJasa + ((totalHpsBarangJasa * ambangAtas) / 100);
                    var data = [];
                    var isNewAuction = true;
                    if (rsLastAuction.length > 0) {
                        //sudah ada auction
                        //console.log("data penyedia auction : "+JSON.stringify(rsLastAuction));
                        isNewAuction = false;
                        data = rsLastAuction
                    } else {
                        //belum ada auction
                        //console.log("data penyedia : "+JSON.stringify(data));
                        data = $scope.dataPenyediaList;
                    }
                    var dataIkutAuctionAmbang = [];
                    var maksBobotAdmin = $rootScope.maksNilaiLulusAmin;
                    var maksBobotTeknis = $rootScope.maksNilaiLulusTeknis;
                    var dataNilaiVendor = $rootScope.listSaveDataEval;
                    for (var i = 0; i < data.length; i++) {
                        if(!isNewAuction){
                            data[i].penawaranKonversi = data[i].totalPenawaranKonfersi; //backend nama beda
                        }
                        for (var j = 0; j < dataNilaiVendor.length; j++) {
                            if (data[i].suratPenawaran.vendor.id == dataNilaiVendor[j].vendor.id) {
                                //console.log("Cek " + data[i].suratPenawaran.vendor.nama + " 1: maksBobotAdmin: " + maksBobotAdmin + ", dataNilaiVendor[j].nilaiAdmin:" + dataNilaiVendor[j].nilaiAdmin);
                                if (typeof maksBobotAdmin !== 'undefined' && dataNilaiVendor[j].nilaiAdmin >= maksBobotAdmin) {
                                    //merit point
                                    if (dataNilaiVendor[j].nilaiTeknis >= maksBobotTeknis) {
                                        angular.forEach(kursPengadaan, function (value, indeks) {
                                            if (value.mataUang.id == data[i].mataUang.id) {
                                                data[i].nilaiPenawaran = data[i].penawaranKonversi;
                                            }
                                        });
                                        //console.log("penawaran : "+JSON.stringify(data[i]));
                                        //console.log("CEK " + data[i].suratPenawaran.vendor.nama + " 02 : data[i].nilaiPenawaran:" + data[i].nilaiPenawaran + ", itungBatasAtas: " + itungBatasAtas + ", " + "itungBatasBawah: " + itungBatasBawah);
                                        if (data[i].nilaiPenawaran >= itungBatasBawah && data[i].nilaiPenawaran <= itungBatasAtas) {
                                            hargaTerendahList.push(data[i].nilaiPenawaran);
                                            dataIkutAuctionAmbang.push(data[i]);
                                        }
                                    }
                                } else {
                                    //sistem gugur
                                    //console.log("sistem gugur!");
                                    if (data[i].mataUang.id != 1) {
                                        angular.forEach(kursPengadaan, function (value, indeks) {
                                            if (value.mataUang.id == data[i].mataUang.id) {
                                                data[i].nilaiPenawaran = data[i].penawaranKonversi;
                                            }
                                        });
                                        //console.log("CEK " + data[i].suratPenawaran.vendor.nama + " 02.1: data[i].nilaiPenawaran:" + data[i].nilaiPenawaran + ", itungBatasAtas: " + itungBatasAtas + ", " + "itungBatasBawah: " + itungBatasBawah);
                                    }
                                    if (data[i].nilaiPenawaran >= itungBatasBawah && data[i].nilaiPenawaran <= itungBatasAtas) {
                                        hargaTerendahList.push(data[i].nilaiPenawaran);
                                        dataIkutAuctionAmbang.push(data[i]);
                                    }
                                }
                                console.log('isi data ikut auction ambang '+JSON.stringify(dataIkutAuctionAmbang));
                            }
                        }
                    }
                    
                    var diLuarAmbangList = [];
                    for (var i = 0; i < $rootScope.listDataPenyedia.length; i++) {
                		var flag = true;
                    	for (var j = 0; j < dataIkutAuctionAmbang.length; j++){
                    
                    		if(flag){
	                    		if ($rootScope.listDataPenyedia[i].suratPenawaran.id == dataIkutAuctionAmbang[j].suratPenawaran.id){
	                    			flag = false;
	                           }
                    		}
                    	}
                    	
                    	if(flag){
                    		diLuarAmbangList.push($rootScope.listDataPenyedia[i]);
                    	}
                    }
                    	
                    	
                  
                    for (var x = 0; x <= hargaTerendahList.length; x++) {
                        for (var y = x + 1; y <= hargaTerendahList.length + 1; y++) {
                            if (hargaTerendahList[x] > hargaTerendahList[y]) {
                                var temp = hargaTerendahList[x];
                                hargaTerendahList[x] = hargaTerendahList[y];
                                hargaTerendahList[y] = temp;
                            }
                        }
                    }
                    
                   

                    $scope.hargaAwal = hargaTerendahList[0];
                    $scope.dataPenyediaList = dataIkutAuctionAmbang;
                    if ($scope.dataPenyediaList.length != $rootScope.listDataPenyedia.length) {
                        //$scope.messageWarningValidate = "Jumlah Peserta Auction tidak sama dengan jumlah Penyedia Barang/Jasa yang melakukan Penawaran! Penyedia Barang/Jasa divalidasi berdasarkan Kriteria Evaluasi.";
                    	$scope.messageWarningValidate = "Vendor "
                    	for(var i = 0; i<diLuarAmbangList.length; i++){
                    		
                    		if (diLuarAmbangList.length>1){
                    			$scope.messageWarningValidate += diLuarAmbangList[i].suratPenawaran.vendor.nama + ", ";
                    		}else{
                    			$scope.messageWarningValidate += diLuarAmbangList[i].suratPenawaran.vendor.nama;
                    		}
                    	}
                    	$scope.messageWarningValidate += " tidak memenuhi nilai ambang batas";
                    		
                    }


                } else {
                    if (dataPenawaranAuction.length > 0) {
                        $scope.hargaAwal = dataPenawaranAuction[0].nilaiPenawaran;
                    } else {
                        $scope.hargaAwal = angka[0];
                    }
                }
            });
        });
        //$scope.mataUang = $rootScope.mataUangCode;
        $scope.mataUang = 'IDR'; //semuanya dibandingkan dengan rupiah
        /* ----------------------------- END Sorting Harga Penawaran Awal ---------------------------------- */


        /* ============================= START Validasi Data Inputan ======================================= */
        $scope.validasiTimeAwal = function () {
            var maxTime = document.getElementsByName("input_awal")[0].max;
            var minTime = document.getElementsByName("input_awal")[0].min;
            var tanggalInputAwal = document.getElementsByName("input_awal")[0].value;
            //console.log(tanggalInputAwal + " vs " + maxTime);
            if (tanggalInputAwal > maxTime || tanggalInputAwal < minTime) {
                toaster.pop('error', 'Salah Waktu', 'Waktu berlaku antar 7.00AM sampai 10.00PM');
                //document.getElementsByName("input_awal")[0].focus();
            }
        }

        $scope.validasiSelisihHPS = function () {
            var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
            var totalHpsBarangJasa = $rootScope.totalHpsMaterial + $rootScope.totalHpsJasa;
            var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah) / 100);
            var totalSelisih = $scope.hargaAwal - $scope.selisihPenawaran;
            //console.log('itungBatasBawah ' + itungBatasBawah);
            //console.log('totalSelisih ' + totalSelisih);
            if (itungBatasBawah > totalSelisih) {
                toaster.pop('error', 'Salah Input Selisih Harga', 'Harga yang telah dikurangi berdekatan dengan Harga Ambang Bawah');
                return false;
                //document.getElementsByName("selisih_penawaran")[0].focus();
            }else{
            	return true;
            }
        }

        $scope.validasiTimeAkhir = function () {
            // NOTE: Bila terjadi perubahan terhadap inputan di database terhadap REGEX waktu di jadwal Pengadaan silahkan UBAH
            // Validasi Tanggal Inputan dengan Jadwal
            var tanggalAkhir = new Date($rootScope.tglAkhirJadwalP);
            var maxDate = $filter('date')(tanggalAkhir, 'dd-MMM-yyyy');
            var tanggalInputAkhir = document.getElementsByName("date_akhir")[0].value;
            var tglSekarang = $filter('date')(new Date(), 'dd-MMM-yyyy');
            //console.log(tanggalInputAkhir + " vs " + maxDate + " vs " + tglSekarang);

            // Validasi Waktu Inputan dengan Jadwal
            var waktuAkhir = new Date($rootScope.waktuAkhirJadwalP);
            var maxTime = $filter('date')(waktuAkhir, 'HH:mm');
            var waktuInputAkhir = document.getElementsByName("input_akhir")[0].value;
            var waktuSekarang = $filter('date')(new Date(), 'HH:mm');
            //console.log(waktuInputAkhir + " vs " + maxTime);
            //console.log(waktuInputAkhir + " vs " + waktuSekarang);

            if (tanggalInputAkhir > maxDate) {
                toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir melebihi dari Jadwal Pengadaan! (' + maxDate + ')');
                //document.getElementsByName("date_akhir")[0].focus();
            } else if (tanggalInputAkhir < tglSekarang) {
                toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir kurang dari Hari ini! (' + tglSekarang + ')');
                //document.getElementsByName("date_akhir")[0].focus();
            } else if (tanggalInputAkhir == tglSekarang && waktuInputAkhir > maxTime) {
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi dari Jadwal Pengadaan! (' + maxTime + ')');
                //document.getElementsByName("date_akhir")[0].focus();
            } else if (tanggalInputAkhir == tglSekarang && waktuInputAkhir < waktuSekarang) {
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi Waktu hari ini (' + waktuSekarang + ')');
                //document.getElementsByName("input_akhir")[0].focus();
            } 
        }

        // untuk peserta Auction dari vendor yang akan ikut
        var vendorCheck = [];
        $scope.pesertaAuction = function (vendor, ikut) {
            if (ikut) {
                vendorCheck.push(vendor);
            } else {
                vendorCheck.splice(vendorCheck.indexOf(vendor), 1);
            }
            //console.log("vendorCheck = " + JSON.stringify(vendorCheck));
        }

        $scope.validasiInput = function () {
                var tglAwalSesi = new Date($scope.tanggalSesiAwal);
                $scope.validasiTimeAkhir();
                if ($scope.tanggalSesiAkhir == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Tanggal Akhir Sesi belum terisi');
                    //document.getElementsByName("date_akhir")[0].focus();
                } else if (tglAwalSesi > $scope.tanggalSesiAkhir) {
                    toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir Sesi harus lebih besar dari Tanggal Awal Sesi');
                    //document.getElementsByName("date_akhir")[0].focus();
                } else if ($scope.akhirSessionTime == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Waktu Akhir Sesi belum terisi');
                    //document.getElementsByName("input_akhir")[0].focus();
                } else if ($scope.selisihPenawaran == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Selisih Penawaran belum terisi');
                    //document.getElementsByName("selisih_penawaran")[0].focus();
                } else if (vendorCheck.length == 0) {
                    toaster.pop('warning', 'Check-Box Kosong', 'Selisih Penawaran belum terisi');
                }
                else {
                	if($scope.validasiSelisihHPS()){
                		 ModalService.showModalConfirmation().then(function (result) {
                             $scope.save();
                             $scope.go('/app/promise/procurement/evaluasiharga/auction');
                         });	
                	}                 
                }
            }
            /* ----------------------------- END Validasi Data Inputan ------------------------------------------ */


        /* ============================= Datepicker ========================================================= */
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[0];
        $scope.tanggalAwalSesiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAwalSesiOpened = true;
        };
        $scope.tanggalAkhirSesiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAkhirSesiOpened = true;
        };
        $scope.tanggalSesiAwal = $filter('date')(new Date(), 'dd-MMM-yyyy');
        var tanggal = $filter('date')(new Date(), 'EEE MMM d y h:mm a Z');
        $scope.awalSessionTime = new Date(tanggal);
        /* ----------------------------- END Datepicker --------------------------------------------------- */


        /* ============================= START CONTROLLER DETAIL ========================================== */
        // fungsi untuk pengurutan nilai
        var sortNilai = function (nilai, type) {
            var angka = nilai;
            for (var x = 0; x <= angka.length; x++) {
                for (var y = x + 1; y <= angka.length + 1; y++) {
                    if (type == 'dsc') {
                        if (angka[x] < angka[y]) {
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }
                    } else {
                        if (angka[x] > angka[y]) {
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
        $scope.back = function () {
            $scope.go('/app/promise/procurement/evaluasiharga/total/auction/view');
        }

        // fungsi target lokasi
        $scope.go = function (path) {
            $location.path(path);
        }

        // ################### Simpan data-nya ke Table ######################
        // 1. Simpan data Sesi Auction
        $scope.save = function () {
                var tglAwalAuction = $scope.tanggalSesiAwal + " " + document.getElementsByName("input_awal")[0].value;
                var tglAwalSesi = $filter('date')($scope.tanggalSesiAkhir, 'dd-MMM-yyyy');
                var tglAkhirAuction = tglAwalSesi + " " + document.getElementsByName("input_akhir")[0].value;
                var dataSesiAuction = {
                    noSesi: $scope.noSesiAuction,
                    waktuAwal: new Date(tglAwalAuction),
                    waktuAkhir: new Date(tglAkhirAuction),
                    keterangan: $scope.keterangan,
                    hargaAwal: $scope.hargaAwal,
                    selisihPenawaran: $scope.selisihPenawaran,
                    pengadaan: $scope.pengadaanId,
                    itemPengadaan: 0,
                    status: 0
                }

                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataSesiAuction
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        var dataSesiAuctionId = data.id;
                        savePesertaAuction(dataSesiAuctionId);
                    }
                    //console.log("CREATE AUCTION SESSION OK : " + JSON.stringify(data));
                });

                // 2. Simpan data Peserta Auction
                var savePesertaAuction = function (dataSesiAuctionId) {
                    //console.log("vendorCheck = " + JSON.stringify(vendorCheck));
                    for (var i = 0; i < vendorCheck.length; i++) {
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
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: dataPesertaAuction
                        }).success(function (data, status, headers, config) {
                            //console.log("CREATE PESERTA AUCTION OK : " + JSON.stringify(data));
                        });
                    }
                }

                // 3. Simpan data Penawaran Auction
                var savePenawaranAuction = function (dataVendorAuctionId, nilaiPenawaranOri, nilaiPenawaran, suratPenawaranId) {
                    var dataPenawaranAuction = {
                        nilaiPenawaran: nilaiPenawaran,
                        nilaiPenawaranOri: nilaiPenawaranOri,
                        pesertaAuction: dataVendorAuctionId,
                        suratPenawaran: suratPenawaranId
                    }

                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/create',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: dataPenawaranAuction
                    }).success(function (data, status, headers, config) {
                        //console.log("CREATE PENAWARAN AUCTION OK : " + JSON.stringify(data));
                    });
                }
            }
            /* ----------------------------- END Rincian Controller --------------------------------------------------- */
    }


    SesiAuctionViewController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', 'RequestService', '$q'];
})();