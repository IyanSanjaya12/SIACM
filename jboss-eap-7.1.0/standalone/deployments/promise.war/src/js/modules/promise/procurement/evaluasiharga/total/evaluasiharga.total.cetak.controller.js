(function () {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaTotalCetakController', EvaluasiHargaTotalCetakController);

    function EvaluasiHargaTotalCetakController(ModalService, $scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, $interval, $state, RequestService, ngTableParams) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $scope.pengadaanId = $rootScope.detilPengadaan.id;
        $scope.mataUangPengadaan = $scope.detilPengadaan.mataUang.id;
        $scope.sistemEvaluasiPenawaranId = $rootScope.detilPengadaan.sistemEvaluasiPenawaran.id;
        if (typeof $rootScope.view !== 'undefined' && $rootScope.view == true) {
            $scope.tahapanId = 14;
        } else {
            $scope.tahapanId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;
        }

        var autoReload;
        var nilaiTerbaik = 0;
        var nilaiTerbaikSemuaVendor = 0;
        var urutan = 0;
        var totalHpsBarangJasa = 0;

        // Get Kurs List
        RequestService.doGET('/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanList02ByPengadaan/' + $scope.pengadaanId)
            .then(function (data) {
                $scope.kursList = data;
                $scope.loadNilaiEvalasiHarga();
            })
            /* ----------------------------------------------------------------------------------------------- */


        /* ================================= START Jadwal Pengadaan ====================================== */
/*        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanAndTahapan/' + $scope.pengadaanId + '/' + $scope.tahapanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.tglAkhirJadwalP = data[0].tglAkhir;
            $rootScope.waktuAkhirJadwalP = data[0].waktuAkhir;
            var today = new Date();
            var jadwalP = new Date(data[0].waktuAkhir);
            if (today > jadwalP) {
                var waktuAkhirPengadaan = $filter('date')(jadwalP, 'dd-MMM-yyyy HH:mm');
                var peringatan = confirm("JADWAL PENGADAAN (" + waktuAkhirPengadaan + ") MELEWATI BATAS HARI INI");
                if (peringatan)
                    $scope.back();
                else
                    $scope.back();
            }
            console.log("Hasil TANGGAL AKHIR JADWAL PENGADAAN = " + new Date($rootScope.tglAkhirJadwalP));
            //console.log("Hasil WAKTU AKHIR JADWAL PENGADAAN = " + new Date($rootScope.waktuAkhirJadwalP));
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });*/
        /* --------------------------------- END Rincian Jadwal Pengadaan  ------------------------------- */


        /* ================================= START Data Kriteria Ambang ================================== */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.kriteriaAmbangBatasList = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Data Kriteria Ambang ---------------------------- */


        /* ======================= START Data Evaluasi Admin Pembobotan ================================== */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $rootScope.maksNilaiLulusAmin = data[0].nilaiMinimalKelulusanAdministrasi;
            //console.log("Maksimal Nilai Lulus Admin = "+$rootScope.maksNilaiLulusAmin);
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------- END Data Evaluasi Admin Pembobotan ------------------------------------ */


        /* ======================= START Data Evaluasi Teknis Pembobotan ================================= */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointPembobotanServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $rootScope.maksNilaiLulusTeknis = data[0].nilaiMinimalKelulusanTeknis;
            //console.log("Maksimal Nilai Lulus Teknis = "+$rootScope.maksNilaiLulusTeknis);
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------- END Data Evaluasi Teknis Pembobotan ---------------------------------- */


        /* ======================== START Data Kebutuhan Material ======================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.totalHpsMaterial = 0;
            angular.forEach(data, function (value, index) {
                $rootScope.totalHpsMaterial = $rootScope.totalHpsMaterial + data[index].totalHPS;
            });
            //console.log("Total HPS Kebutuhan Material = " + $rootScope.totalHpsMaterial);
            $scope.loading = false;
            kebJasa();
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------- END Rincian Data Kebutuhan Material ----------------------------------- */


        /* ======================== START Data Kebutuhan Jasa ============================================ */
        var kebJasa = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $rootScope.totalHpsJasa = 0;
                    angular.forEach(data, function (value, index) {
                        $rootScope.totalHpsJasa = $rootScope.totalHpsJasa + data[index].totalHPS;
                    });
                    //console.log("Total HPS Kebutuhan Jasa = " + $rootScope.totalHpsJasa);
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = true;
                });
            }
            /* ----------------------- END Rincian Data Kebutuhan Jasa --------------------------------------- */


        /* ======================= START Data Penyedia Barang/Jasa Detail ================================ */
        // look up ke tabel Penawaran Auction
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByPengadaan/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.dataPenawaranAuction = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });

        $scope.loadNilaiEvalasiHarga = function () {
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getPenawaranPertamaListByGraduationEvaluasiTeknis/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                urutan = 0;
                $scope.dataPenyediaList = [];
                $scope.listNilaiTerbaik = [];
                $scope.totNilaiSemuaVendor = [];
                $scope.listDataPenawaranAuction = [];
                angular.forEach(data, function (value, index) {
                    angular.forEach($scope.kursList, function (kurs) {
                        if (data.length > 0) {
                            if (kurs.mataUang.id == value.mataUang.id) {
                                value.penawaranKonversi = parseFloat(value.nilaiPenawaran) * parseFloat(kurs.nilai);
                            }
                        }
                    })
                    $scope.dataPenyediaList.push(value);
                    //if($scope.sistemEvaluasiPenawaranId == 2)
                    evaluasiHargaVendor(data[index], data.length);
                    $rootScope.listHarga.push(data[index].nilaiPenawaran);
                    $rootScope.mataUangCode = data[index].mataUang.kode;

                });
                $rootScope.listDataPenyedia = data;
                $scope.loading = false;
                //console.log("Hasil dataPenyediaList = " + JSON.stringify($rootScope.listDataPenyedia));
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };

        /* ----------------------- END Rincian Data Penyedia Barang/Jasa --------------------------------- */


        /* ============================ START Nilai Evaluasi Detail ====================================== */
        var evaluasiHargaVendor = function (dataPP, panjangData) {
                var suratPenawaranId = dataPP.suratPenawaran.id;

                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/' + suratPenawaranId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    //console.log("Hasil EVALUASI HARGA = " + JSON.stringify(data));
                    perhitunganEvaluasiHarga(data[0], panjangData);
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = true;
                });
            }
            /* ---------------------------- END Rincian Nilai Evaluasi -------------------------------------- */


        /* ============================ START Perhitungan Evaluasi Harga ================================ */
        //-----------> Perhitungan dengan atau tanpa AMBANG BATAS <-------------------
        var perhitunganEvaluasiHarga = function (dataEval, panjangData) {
            var nilaiPenawaranListVendor = [];
            var hargaTerbaik = 0;
            var hargaTerbaikOri = 0;

            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByVendorPengadaan/' + dataEval.vendor.id + '/' + dataEval.suratPenawaran.pengadaan.id)
                .success(function (data, status, headers, config) {

                    $scope.loading = false;

                    angular.forEach(data, function (value, index) {
                        nilaiPenawaranListVendor.push(data[index].totalPenawaranKonfersi);
                    });

                    // Perhitungan Nilai Terbaik dari penawaran vendor
                    hargaTerbaik = sortNilai(nilaiPenawaranListVendor, 'asc');

                    var hargaTerbaikVendor = {
                        hargaTerbaik: hargaTerbaik
                    };
                    angular.forEach(data, function (value, index) {
                        if (value.totalPenawaranKonfersi == hargaTerbaik) {
                            hargaTerbaikVendor.nilaiPenawaran = value.nilaiPenawaran;
                            hargaTerbaikVendor.mataUang = value.mataUang;
                            hargaTerbaikVendor.nilaiKurs = value.nilaiKurs;
                        }
                    });
                    angular.extend(dataEval, hargaTerbaikVendor);
                    // perhitungan nilai evaluasi harga
                    totalHpsBarangJasa = $rootScope.totalHpsMaterial + $rootScope.totalHpsJasa;

                    if ($rootScope.kriteriaAmbangBatasList.length > 0) { // Menggunakan Ambang Batas
                        var itungBatasAtas = totalHpsBarangJasa + ((totalHpsBarangJasa * $rootScope.kriteriaAmbangBatasList[0].atas) / 100);
                        var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * $rootScope.kriteriaAmbangBatasList[0].bawah) / 100);
                        if (totalHpsBarangJasa < dataEval.hargaTerbaik) {
                            if (dataEval.hargaTerbaik < itungBatasAtas && dataEval.hargaTerbaik > itungBatasBawah) {
                                $scope.totNilaiSemuaVendor.push(dataEval.hargaTerbaik);
                            } else {
                                dataEval.nilaiHarga = 0;
                                dataEval.nilaiTotal = 0;
                            }
                        } else {
                            if (dataEval.hargaTerbaik < itungBatasAtas && dataEval.hargaTerbaik > itungBatasBawah) {
                                $scope.totNilaiSemuaVendor.push(dataEval.hargaTerbaik);
                            } else {
                                dataEval.nilaiHarga = 0;
                                dataEval.nilaiTotal = 0;
                            }
                        }
                    } else { // Tidak Menggunakan Ambang Batas
                        if (totalHpsBarangJasa < dataEval.hargaTerbaik) {
                            dataEval.nilaiHarga = 0;
                            dataEval.nilaiTotal = 0;
                        } else {
                            $scope.totNilaiSemuaVendor.push(dataEval.hargaTerbaik);
                        }
                    }

                    urutan = urutan + 1;
                    // kirim ke List hitung lanjutan Evaluasi Harga
                    $scope.listDataPenawaranAuction.push(dataEval);

                    hitungLanjutanEvaluasiHarga($scope.listDataPenawaranAuction, $scope.totNilaiSemuaVendor, urutan, panjangData);
                }).error(function (data, status, headers, config) {
                    $scope.loading = true;
                    alert("DATA ERORR");
                });
        }

        //-----------> Perhitungan dengan atau tanpa BOBOT KRITERIA <-------------------
        var hitungLanjutanEvaluasiHarga = function (dataPAuction, totNilaiSemuaVendor, urutan, panjangDataEval) {
                //console.log(">cek dataPAuction:"+JSON.stringify(dataPAuction));
                if (urutan == panjangDataEval) {
                    nilaiTerbaikSemuaVendor = sortNilai(totNilaiSemuaVendor, 'asc');

                    angular.forEach(dataPAuction, function (value, index) {
                        dataPAuction[index].isValid = 0;
                        if (nilaiTerbaikSemuaVendor == dataPAuction[index].hargaTerbaik) {
                            dataPAuction[index].nilaiHarga = 100;
                            if ($scope.sistemEvaluasiPenawaranId == 2) {
                                dataPAuction[index].nilaiTotal = hitungTotalNilai($rootScope.kriteriaAmbangBatasList[0], dataPAuction[index]);
                                if (dataPAuction[index].nilaiAdmin >= $rootScope.maksNilaiLulusAmin) {
                                    if (dataPAuction[index].nilaiTeknis >= $rootScope.maksNilaiLulusTeknis)
                                        dataPAuction[index].isValid = 1;
                                }
                            } else {
                                dataPAuction[index].nilaiTotal = (dataPAuction[index].nilaiAdmin + dataPAuction[index].nilaiTeknis + dataPAuction[index].nilaiHarga) / 3;
                                dataPAuction[index].isValid = 1;
                            }
                            $scope.listNilaiTerbaik.push(dataPAuction[index].nilaiTotal);
                        } else {
                            if (dataPAuction[index].nilaiHarga != 0) {
                                dataPAuction[index].nilaiHarga = (nilaiTerbaikSemuaVendor / dataPAuction[index].hargaTerbaik) * 100
                                if ($scope.sistemEvaluasiPenawaranId == 2) {
                                    dataPAuction[index].nilaiTotal = hitungTotalNilai($rootScope.kriteriaAmbangBatasList[0], dataPAuction[index]);
                                    if (dataPAuction[index].nilaiAdmin >= $rootScope.maksNilaiLulusAmin) {
                                        if (dataPAuction[index].nilaiTeknis >= $rootScope.maksNilaiLulusTeknis)
                                            dataPAuction[index].isValid = 1;
                                    }
                                } else {
                                    dataPAuction[index].nilaiTotal = (dataPAuction[index].nilaiAdmin + dataPAuction[index].nilaiTeknis + dataPAuction[index].nilaiHarga) / 3;
                                    dataPAuction[index].isValid = 1;
                                }
                                $scope.listNilaiTerbaik.push(dataPAuction[index].nilaiTotal);
                            }
                        }
                    });
                    var nilaiFlag = sortNilai($scope.listNilaiTerbaik, 'dsc');
                    var dataNilai = $rootScope.listDataPenyedia;
                    angular.forEach(dataPAuction, function (value, index) {
                        if (nilaiFlag == dataPAuction[index].nilaiTotal) {
                            dataPAuction[index].flag = 1;
                            var id = dataPAuction[index].vendor.id;
                            angular.forEach(dataNilai, function (value, index) {
                                if (dataNilai[index].suratPenawaran.vendor.id == id)
                                    dataNilai[index].flag = 1;
                            });
                        } else {
                            dataPAuction[index].flag = 0;
                            var id = dataPAuction[index].vendor.id;
                            angular.forEach(dataNilai, function (value, index) {
                                if (dataNilai[index].suratPenawaran.vendor.id == id)
                                    dataNilai[index].flag = 0;
                            });
                        }
                    });

                    // kirim data ke Html
                    $scope.evaluasiHargaVendorList = dataPAuction;
                    $rootScope.listSaveDataEval = $scope.evaluasiHargaVendorList;
                    $rootScope.dataNilaiList = dataNilai;
                }
            }
            /* ---------------------------- END Perhitungan Evaluasi Harga ---------------------------------- */


        /* ============================ START Sesi Auction View Detail ================================== */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/getByPengadaan/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.sesiAuctionDetail = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ---------------------------- END Rincian Sesi Auction View ----------------------------------- */


        /* =============================== START HISTORY PENAWARAN MODAL ================================ */
        $scope.clickHistoryPenawaran = function (vendorId, pengadaanId) {
            $rootScope.vendorId = vendorId;
            $rootScope.pengadaanId = pengadaanId;
            $scope.viewHistoryPenawaran();
        }

        $scope.viewHistoryPenawaran = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/history_penawaran_harga.html',
                controller: ModalInstanceCtrl,
                size: size,
            });
        };

        var ModalInstanceCtrl = function ($http, $scope, $rootScope, $modalInstance, ngTableParams) {

            // look up ke tabel Penawaran Auction
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByVendorPengadaan/' + $rootScope.vendorId + '/' + $rootScope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;

                if (data.length > 0) {
                    $scope.penawaranAuctionByVendor = data;
                    $scope.kodeVendor = data[0].pesertaAuction.vendor.id;
                    $scope.namaVendor = data[0].pesertaAuction.vendor.nama;
                    $scope.alamatVendor = data[0].pesertaAuction.vendor.alamat;
                    $scope.telVendor = data[0].pesertaAuction.vendor.nomorTelpon;
                } else {
                    // look up ke table Penawaran Pertama
                    $scope.penawaranAuctionByVendor = [];
                    var dataPP = $rootScope.listDataPenyedia;
                    angular.forEach(dataPP, function (value, index) {
                        if (dataPP[index].suratPenawaran.vendor.id == $rootScope.vendorId) {
                            $scope.penawaranAuctionByVendor.push(dataPP[index]);
                            $scope.kodeVendor = dataPP[index].suratPenawaran.vendor.id;
                            $scope.namaVendor = dataPP[index].suratPenawaran.vendor.nama;
                            $scope.alamatVendor = dataPP[index].suratPenawaran.vendor.alamat;
                            $scope.telVendor = dataPP[index].suratPenawaran.vendor.nomorTelpon;
                        }
                    });
                }

                // paging untuk Penawaran List
                $scope.penawaranAuctionByVendorList = new ngTableParams({
                    page: 1,
                    count: 4
                }, {
                    total: $scope.penawaranAuctionByVendor.length,
                    getData: function ($defer, params) {
                        $defer.resolve($scope.penawaranAuctionByVendor.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
            }).error(function (data, status, headers, config) {
                $scope.loading = true;
                alert("DATA ERORR");
            });


            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        ModalInstanceCtrl.$inject = ['$http', '$scope', '$rootScope', '$modalInstance', 'ngTableParams'];
        /* ------------------------------- END HISTORY PENAWARAN MODAL ---------------------------------- */


        /* ============================ START CONTROLLER DETAIL ========================================= */
        // flag untuk view saja
        if ($rootScope.flagView == 1)
            $scope.viewAja = 1;

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

        // fungsi hitung Total Nilai
        var hitungTotalNilai = function (presen, nilai) {
            var hasil = ((nilai.nilaiAdmin * presen.presentasiNilaiAdmin) / 100) + ((nilai.nilaiTeknis * presen.presentasiNilaiTeknis) / 100) + ((nilai.nilaiHarga * presen.presentasiNilaiHarga) / 100);
            return hasil;
        }

        // tombol sesi auction baru
        $scope.newSesiAuction = function () {

            if ($rootScope.sesiAuctionDetail.length > 0) {

                // Check waktu auction
                var params = {
                    pengadaanId: $scope.pengadaanId
                }
                RequestService.doPOST('/procurement/evaluasiharga/AuctionServices/checkSesiAuctionByPengadaanId', params)
                    .then(function (response) {
                        if (response.data != undefined) {
                            var sesiAuctionDetail = response.data
                            var listSesiStatus = [];

                            angular.forEach(sesiAuctionDetail, function (value, index) {
                                listSesiStatus.push(value.status);
                            });
                            var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                            if (sortSesiStatus == 1) {
                                $state.go('app.promise.procurement-panitia-evaluasiharga-total-sesiauction');
                            } else {
                                var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, Apakah ingin menutup sesi sebelumnya?');
                                if (jawaban) {
                                    updateDataAuction(sesiAuctionDetail);
                                    $state.go('app.promise.procurement-panitia-evaluasiharga-total-sesiauction');
                                }
                            }
                        }
                    })
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/sesiauction');
            }
        }

        // tombol lihat history auction
        $scope.historyAuction = function () {
            if ($rootScope.sesiAuctionDetail.length > 0) {
                $scope.go('/app/promise/procurement/evaluasiharga/historyauction');
            } else {
                if ($scope.viewAja == 1) {
                    alert('Maaf, Anda belum sampai ke Tahapan ini');
                    $scope.back();
                } else {
                    alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                    $scope.newSesiAuction();
                }
            }
        }

        // tombol lihat diskualifikasi harga
        $scope.diskualifikasi = function () {
            if ($rootScope.sesiAuctionDetail.length > 0) {
                $scope.go('/app/promise/procurement/evaluasiharga/diskualifikasi');
            } else {
                alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                $scope.newSesiAuction();
            }
        }

        // tombol kembali ke index.html
        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $scope.go('/app/promise/procurement/evaluasiharga/auction');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/auction');
            }
        }

        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").hide();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            var printContents = document.getElementById(divName).innerHTML;
            $(".ng-table-pager").show();
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }


        // fungsi target lokasi
        $scope.go = function (path) {
            $location.path(path);
        }

        // simpan evaluasi
        $scope.saveData = function () {
            if ($rootScope.sesiAuctionDetail.length > 0) {


                ModalService.showModalConfirmation().then(function (result) {
                    // Check waktu auction
                    var params = {
                        pengadaanId: $scope.pengadaanId
                    }
                    RequestService.doPOST('/procurement/evaluasiharga/AuctionServices/checkSesiAuctionByPengadaanId', params)
                        .then(function (response) {
                            if (response.data != undefined) {
                                var sesiAuctionDetail = response.data;
                                var listSesiStatus = [];
                                angular.forEach(sesiAuctionDetail, function (value, index) {
                                    listSesiStatus.push(value.status);
                                });
                                var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                                if (sortSesiStatus == 1) {
                                    simpanDataEvalHarga();
                                    lanjutTahapan();
                                    $scope.back();
                                } else {
                                    var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, apakah anda yakin akan munutup sesi dan melanjutkan ke tahapan berikutnya??');
                                    if (jawaban) {
                                        updateDataAuction(sesiAuctionDetail); // Change all Opened Auction Status with Integer 1
                                        simpanDataEvalHarga();
                                        lanjutTahapan();
                                        $scope.back();
                                    }
                                }
                            }
                        });
                });
            } else {
                alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                $scope.newSesiAuction();
            }

        }

        // fungsi simpan data Evaluasi Harga
        var simpanDataEvalHarga = function () {
            //console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify($rootScope.listSaveDataEval));
            var data = $rootScope.listSaveDataEval;
            angular.forEach(data, function (value, index) {
                // simpan
                var dataEvaluasiHargaVendor = {
                    id: data[index].id,
                    nilaiAdmin: data[index].nilaiAdmin,
                    nilaiTeknis: data[index].nilaiTeknis,
                    nilaiHarga: data[index].nilaiHarga,
                    nilaiTotal: data[index].nilaiTotal,
                    vendor: data[index].vendor.id,
                    isCalonPemenang: data[index].flag,
                    isValid: data[index].isValid,
                    suratPenawaran: data[index].suratPenawaran.id
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataEvaluasiHargaVendor
                }).success(function (data, status, headers, config) {
                    //console.log("UPDATE EVALUASI HARGA OK : " + JSON.stringify(data));
                }).error(function (data, status, headers, config) {
                    $scope.loading = true;
                    alert("DATA ERORR UNTUK MENYIMPAN!!");
                });
            });
        }

        // fungsi update data Auction
        var updateDataAuction = function (data) {
            for (var i = 0; i < data.length; i++) {
                if (data[i].status == 0) {
                    //console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify(data));
                    var dataSesiAuction = {
                        id: data[i].id,
                        noSesi: data[i].noSesi,
                        waktuAwal: new Date(data[i].waktuAwal),
                        waktuAkhir: new Date(data[i].waktuAkhir),
                        keterangan: data[i].keterangan,
                        hargaAwal: data[i].hargaAwal,
                        selisihPenawaran: data[i].selisihPenawaran,
                        pengadaan: data[i].pengadaan.id,
                        itemPengadaan: 0,
                        status: 1
                    }

                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/update',
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
                        //console.log("UPDATE AUCTION SESSION OK : " + JSON.stringify(data));
                    }).error(function (data, status, headers, config) {
                        $scope.loading = true;
                        alert("DATA ERORR UNTUK MENYIMPAN!!");
                    });
                }
            }
        }

        //change tahapan
        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
                    .success(function (data, status, headers, config) {
                        $scope.nextTahapan = data;
                    })
                    .error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        //update pengadaan
        var lanjutTahapan = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: {
                    pengadaanId: $scope.pengadaanId,
                    tahapanPengadaanId: $scope.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.back();
            });
        }

        // Refresh Page
        // 1. Manual
        $scope.manualRefreshAuction = function () {
            $scope.loading = true;
            $scope.loadNilaiEvalasiHarga();
        }

        // 2. Auto
        $scope.autoRefreshAuction = function () {
            $scope.stop();
            $scope.getRemainingTime();
            var urlActionView = 'app/promise/procurement/evaluasiharga/total/auction/view';
            var currentUrl = $location.absUrl().split('?')[0]
                //console.log('currentUrl = ' + currentUrl);

            // kill timer when currentUrl <> urlActionView
            if (currentUrl.indexOf(urlActionView) == -1) {
                $interval.cancel(autoReload);
            }
        }

        $scope.stop = function () {
            $interval.cancel(autoReload);
        };

        $scope.startAutoReload = function () {
            $scope.stop();
            autoReload = $interval($scope.refresh, 1000); // Auto Reload setiap 1 Detik ajah
        }

        // Get Auction Session Ongoing
        var getOngoingAuctionSesi = function () {
            var params = {
                pengadaanId: $scope.pengadaanId
            }
            RequestService.doPOST('/procurement/evaluasiharga/AuctionServices/getActiveSession', params)
                .then(function (response) {
                    if (response.data.length > 0) {
                        $scope.noSesi = response.data[0].pesertaAuction.auction.noSesi;
                        $scope.tglAwalSesi = response.data[0].pesertaAuction.auction.waktuAwal;
                        $scope.tglAkhirSesi = response.data[0].pesertaAuction.auction.waktuAkhir;
                        $scope.loading = false;

                        // paging untuk Penawaran List
                        $scope.penawaranAuctionList = new ngTableParams({
                            page: 1,
                            count: 4
                        }, {
                            counts: [], // hide page count
                            total: response.data.length,
                            getData: function ($defer, params) {
                                $defer.resolve(response.data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        });
                    }
                })
        }
        getOngoingAuctionSesi();


        /* --------------------------- END Rincian Controller --------------------------------------------- */

    }
    EvaluasiHargaTotalCetakController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', '$interval', '$state', 'RequestService', 'ngTableParams'];

})();