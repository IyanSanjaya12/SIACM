(function () {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaTotalViewSimpleController', EvaluasiHargaTotalViewSimpleController);

    function EvaluasiHargaTotalViewSimpleController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, RequestService, $state) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $scope.pengadaanId = $rootScope.detilPengadaan.id;
        $scope.sistemEvaluasiPenawaranId = $rootScope.detilPengadaan.sistemEvaluasiPenawaran.id;
        if (typeof $rootScope.view !== 'undefined' && $rootScope.view == true) {
            $scope.tahapanId = 14000001;
        } else {
            $scope.tahapanId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;
        }

        var nilaiTerbaik = 0;
        var nilaiTerbaikSemuaVendor = 0;
        var urutan = 0;
        var totalHpsBarangJasa = 0;
        /* ----------------------------------------------------------------------------------------------- */


        /* ================================= START Jadwal Pengadaan ====================================== */
        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanAndTahapan/' + $scope.pengadaanId + '/' + $scope.tahapanId, {
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
            /*console.log("Hasil TANGGAL AKHIR JADWAL PENGADAAN = " + new Date($rootScope.tglAkhirJadwalP));
            console.log("Hasil WAKTU AKHIR JADWAL PENGADAAN = " + new Date($rootScope.waktuAkhirJadwalP));*/
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Jadwal Pengadaan  ------------------------------- */



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
            //$scope.loading = false;
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
        function getLastPenawaran () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getPenawaranAuctionAndLastAuctionByPengadaan/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    $rootScope.dataPenawaranAuction = data;
                    angular.forEach($rootScope.dataPenawaranAuction, function (value, index) {
                        $http.get($rootScope.backendAddress + "/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/" + value.suratPenawaran.pengadaan.id + "/" + value.mataUang.id)
                            .success(function (data) {
                                $rootScope.dataPenawaranAuction[index].nilaiKursPengadaan = data.nilai;
                                $rootScope.dataPenawaranAuction[index].nilaiPenawaranDenganKurs =  $rootScope.dataPenawaranAuction[index].nilaiPenawaran * data.nilai;
                            });
                    });
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };
        getLastPenawaran();
        $scope.getLastPenawaran = function(){
            getLastPenawaran();
        }

        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $scope.dataPenyediaList = data;
            $rootScope.listDataPenyedia = data;
            $scope.listNilaiTerbaik = [];
            $scope.totNilaiSemuaVendor = [];
            $scope.listDataPenawaranAuction = [];
            $scope.loading = false;
            //console.log("Hasil dataPenyediaList = " + JSON.stringify($rootScope.listDataPenyedia));
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------- END Rincian Data Penyedia Barang/Jasa --------------------------------- */


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

        // tombol sesi auction baru
        $scope.newSesiAuction = function () {
            if ($rootScope.sesiAuctionDetail.length > 0) {
                // Check waktu auction
				var params = {
						pengadaanId: $scope.pengadaanId
				}
				RequestService.doPOST('/procurement/evaluasiharga/AuctionServices/checkSesiAuctionByPengadaanId', params)
				.then(function (response) {
                    if(response.data != undefined) {
						var sesiAuctionDetail = response.data
						var listSesiStatus = [];
						
						angular.forEach(sesiAuctionDetail, function (value, index) {
							listSesiStatus.push(value.status);
						});
						var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
						if (sortSesiStatus == 1) {
							$state.go('app.promise.procurement-panitia-evaluasihargasimple-total-sesiauction');
						} else {
							var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, Apakah ingin menutup sesi sebelumnya?');
							if (jawaban) {
								updateDataAuction(sesiAuctionDetail);
								$state.go('app.promise.procurement-panitia-evaluasihargasimple-total-sesiauction');
							}
						}
					}
                });
                
            } else {
                $scope.go('/app/promise/procurement/evaluasihargasimple/sesiauction');
            }
        }

        // tombol lihat history auction
        $scope.historyAuction = function () {
            if ($rootScope.sesiAuctionDetail.length > 0) {
                $scope.go('/app/promise/procurement/evaluasihargasimplesimple/historyauction');
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
                $scope.go('/app/promise/procurement/evaluasihargasimple/diskualifikasi');
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
                    $scope.go('/app/promise/procurement/evaluasihargasimple/auction');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasihargasimple/auction');
            }
        }

        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }


        // fungsi target lokasi
        $scope.go = function (path) {
            $location.path(path);
        }

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

        // simpan evaluasi
        $scope.showBtnSaveData = true;
        $scope.saveData = function () {
            $scope.showBtnSaveData = false;
            if ($rootScope.sesiAuctionDetail.length > 0) {
                var listSesiStatus = [];
                angular.forEach($rootScope.sesiAuctionDetail, function (value, index) {
                    listSesiStatus.push($rootScope.sesiAuctionDetail[index].status);
                });
                var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                //console.log("sortSesiStatus = " + sortSesiStatus);
                if (sortSesiStatus == 1) {
                    simpanDataEvalHarga();
                    lanjutTahapan();
               
                } else {
                    var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, apakah anda yakin akan menutup sesi dan melanjutkan ke tahapan berikutnya??');
                    if (jawaban) {
                        updateDataAuction($rootScope.sesiAuctionDetail); // Change all Opened Auction Status with Integer 1
                        simpanDataEvalHarga();
                        lanjutTahapan();
                        //$scope.back();
                    } else {
                        //$scope.go('/app/promise/procurement/evaluasihargasimple/historyauction');
                        $scope.showBtnSaveData = true;
                    }
                }
            } else {
                alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                $scope.showBtnSaveData = true;
                $scope.newSesiAuction();
            }

        }

        // fungsi simpan data Evaluasi Harga
        var simpanDataEvalHarga = function () {
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getDistinctPenawaranAuctionAndLastAuctionWithKursByPengadaan/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    //console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify($rootScope.listSaveDataEval));
                    var hargaTerbaik = 0;
                    angular.forEach(data, function (value, index) {
                        // simpan                        
                        var isCalonPemenang = 0;
                        if (index == 0) {
                            hargaTerbaik = value.totalPenawaranKonfersi;
                            isCalonPemenang = 1;
                        }

                        var nilaiHarga = (hargaTerbaik / value.totalPenawaranKonfersi) * 100;

                        $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + value.suratPenawaran.vendor.id + '/' + value.suratPenawaran.pengadaan.id)
                            .success(function (nilaiEvaluasi) {
                                var dataEvaluasiHargaVendor = {
                                    id: 0,
                                    nilaiHarga: nilaiHarga,
                                    vendor: value.suratPenawaran.vendor.id,
                                    isCalonPemenang: isCalonPemenang,
                                    isValid: 1,
                                    suratPenawaran: value.suratPenawaran.id
                                }

                                //console.log(">>eva : "+JSON.stringify(nilaiEvaluasi));
                                if (nilaiEvaluasi.length > 0) {
                                    dataEvaluasiHargaVendor.nilaiAdmin = nilaiEvaluasi[0].nilaiEvaluasiAdministrasi;
                                    dataEvaluasiHargaVendor.nilaiTeknis = nilaiEvaluasi[0].nilaiEvaluasiTeknis;
                                } else {
                                    dataEvaluasiHargaVendor.nilaiAdmin = 0;
                                    dataEvaluasiHargaVendor.nilaiTeknis = 0;
                                }

                                //cek merit point / tidak
                                if (value.suratPenawaran.pengadaan.sistemEvaluasiPenawaran.id == 2) { //meritpoint
                                    //cari pembobotan dulu
                                    $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId)
                                        .success(function (dataAmbangBatas) {
                                            if (dataAmbangBatas.length > 0) {
                                                var nilaiAdminMeritPoint = (dataAmbangBatas[0].presentasiNilaiAdmin / 100) * dataEvaluasiHargaVendor.nilaiAdmin;
                                                var nilaiTeknisMeritPoint = (dataAmbangBatas[0].presentasiNilaiTeknis / 100) * dataEvaluasiHargaVendor.nilaiTeknis;
                                                var nilaiHargaMeritPoint = (dataAmbangBatas[0].presentasiNilaiHarga / 100) * dataEvaluasiHargaVendor.nilaiHarga;
                                                dataEvaluasiHargaVendor.nilaiTotal = nilaiAdminMeritPoint + nilaiTeknisMeritPoint + nilaiHargaMeritPoint;

                                                $http({
                                                    method: 'POST',
                                                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/create',
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
                                            } else {
                                                console.error('tidak ada ambang batas');
                                            }
                                        });

                                } else {
                                    dataEvaluasiHargaVendor.nilaiTotal = (dataEvaluasiHargaVendor.nilaiAdmin + dataEvaluasiHargaVendor.nilaiTeknis + dataEvaluasiHargaVendor.nilaiHarga) / 3;

                                    $http({
                                        method: 'POST',
                                        url: $rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/create',
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
                                }


                            })
                            .error(function (data) {
                                console.error("lookup nilai admin teknis error");
                            });


                    });
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.showBtnSaveData = true;
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

        /*modal Input Kurs*/
        $scope.goKurs = function () {
            $rootScope.btnSimpanStatus = $scope.btnSimpanStatus;
            $rootScope.pengadaanId = $scope.pengadaanId;
            var modalInstance = $modal.open({
                templateUrl: '/inputKurs.html',
                controller: ModalInstanceInputKursCtrl,
                size: 'lg'
            });
        };
        var ModalInstanceInputKursCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, $q) {
            $scope.btnSimpanStatus = $rootScope.btnSimpanStatus;

            $scope.ok = function () {
                var postInsertData = [];
                var postUpdateData = [];
                for (var i = 0; i < $scope.inputKursList.length; i++) {
                    if (typeof $scope.inputKursList[i].kursPengadaanId === 'undefined') {
                        postInsertData.push($scope.inputKursList[i]);
                    } else {
                        postUpdateData.push($scope.inputKursList[i]);
                    }
                }

                var ansyncInsertData = function (dataInsertKurs) {
                    return $q(function (resolve) {
                        var postData = {
                            data: []
                        };
                        for (var i = 0; i < dataInsertKurs.length; i++) {
                            postData.data.push({
                                pengadaanId: dataInsertKurs[i].pengadaanId,
                                mataUangId: dataInsertKurs[i].mataUangId,
                                nilai: dataInsertKurs[i].kurs
                            });
                        }
                        $scope.loading = true;
                        $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/insertKursPengadaanRaw',
                                data: postData
                            })
                            .success(function (data, status, headers, config) {
                                $scope.messageSuccess = "Penyimpanan berhasil";
                                $scope.btnSimpanStatus = false;
                                $scope.loading = false;
                                resolve(true);
                            })
                            .error(function (data, status, headers, config) {
                                $scope.messageError = "penyimpanan gagal";
                                $scope.loading = false;
                                resolve(false);
                            });
                    })
                }

                var promiseInsert = ansyncInsertData(postInsertData);
                promiseInsert.then(function (resolve) {
                    var postData = {
                        data: []
                    };
                    for (var i = 0; i < postUpdateData.length; i++) {
                        postUpdateData[i].nilai = postUpdateData[i].kurs;
                        postData.data.push(postUpdateData[i]);
                    }
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/updateKursPengadaanRaw',
                            data: postData
                        })
                        .success(function (data, status, headers, config) {
                            $scope.messageSuccess = "Penyimpanan berhasil";
                            getLastPenawaran();
                            $scope.btnSimpanStatus = false;
                        })
                        .error(function (data, status, headers, config) {
                            $scope.messageError = "penyimpanan gagal";
                        });
                    $scope.loading = false;
                });

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.getMataUang = function () {
                $scope.loading = true;
                $scope.inputKursList = [];
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + $rootScope.pengadaanId)
                    .success(function (data, status, headers, config) {
                        if (data.length > 0) {
                            angular.forEach(data, function (valueKursPengadaan, indexKursPengadaan) {
                                $scope.inputKursList[indexKursPengadaan] = {
                                    kursPengadaanId: valueKursPengadaan.id,
                                    pengadaanId: valueKursPengadaan.pengadaan.id,
                                    mataUangId: valueKursPengadaan.mataUang.id,
                                    nama: valueKursPengadaan.mataUang.nama,
                                    kode: valueKursPengadaan.mataUang.kode,
                                    kurs: valueKursPengadaan.nilai
                                }
                            });

                            $scope.loading = false;
                        } else {
                            $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
                                .success(function (masterKurs, status, headers, config) {
                                    angular.forEach(masterKurs, function (value, indeks) {
                                        $scope.inputKursList[indeks] = {
                                            id: value.id,
                                            nama: value.nama,
                                            kode: value.kode,
                                            kurs: value.kurs
                                        };
                                    })
                                    $scope.loading = false;
                                });
                        }
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            }
            $scope.getMataUang();

        };
        ModalInstanceInputKursCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', '$q'];
        /*END modal Input Kurs*/

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
                $scope.go('/app/promise/procurement/evaluasihargasimple/auction');
            });
        }

        /* --------------------------- END Rincian Controller --------------------------------------------- */

    }
    EvaluasiHargaTotalViewSimpleController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'RequestService', '$state'];

})();