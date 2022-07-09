(function () {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaTotalBiddingViewController', EvaluasiHargaTotalBiddingViewController);

    function EvaluasiHargaTotalBiddingViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $rootScope.nilaiTerbaik = 0;
        $rootScope.dataHasilHitunganSimpan = [];

        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        //$scope.sistemEvaluasiPenawaranId == 2;
        $scope.listHarga = [];
        $scope.dataPenyediaList = [];

        $rootScope.totalHpsMaterial = 0;
        $rootScope.totalHpsJasa = 0;
        $scope.totalHpsBarangJasa = 0;
        var nilaiHarga = 0;
        var nilaiTotal = 0;
        var hasil = {};
        /* ----------------------------------------------------------------------------------------------- */


        /* ======================== START Data Kriteria Ambang Batas ===================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.ambangBatas = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ------------------------ END Rincian Data Kriteria Ambang Batas ------------------------------- */


        /* ======================== START Data Kebutuhan Material ======================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            angular.forEach(data, function (value, index) {
                //console.log(">> value : " + value.totalHPS);
                $rootScope.totalHpsMaterial = $rootScope.totalHpsMaterial + value.totalHPS;
                //console.log("Total HPS Kebutuhan Material = " + $rootScope.totalHpsMaterial);
            });
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
                    angular.forEach(data, function (value, index) {
                        $rootScope.totalHpsJasa = $rootScope.totalHpsJasa + value.totalHPS;
                        //console.log("Total HPS Kebutuhan Jasa = " + $rootScope.totalHpsJasa);
                    });
                    hitungBarangJasa();
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = true;
                });
            }
            /* ----------------------- END Rincian Data Kebutuhan Jasa --------------------------------------- */


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


        /* ======================= START Data Penyedia Barang/Jasa Detail ================================ */
        var hitungBarangJasa = function () {
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getWithDeviasiByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.totalHpsBarangJasa = $rootScope.totalHpsMaterial + $rootScope.totalHpsJasa;
                //console.log("Total HPS Kebutuhan = " + $scope.totalHpsBarangJasa);
                
                //console.log("$rootScope.ambangBatas[0].atas :"+$rootScope.ambangBatas[0].atas );
                //console.log("$rootScope.ambangBatas[0].bawah:"+$rootScope.ambangBatas[0].bawah);

                if ($rootScope.ambangBatas.length > 0) { // Menggunakan Ambang Batas      
                    var itungBatasAtas = $scope.totalHpsBarangJasa + ( ($rootScope.ambangBatas[0].atas / 100) * $scope.totalHpsBarangJasa );
                    var itungBatasBawah = $scope.totalHpsBarangJasa - ( ($rootScope.ambangBatas[0].atas / 100) * $scope.totalHpsBarangJasa );
                    //console.log("AMBANG BATAS PENILAIAN ANTARA (" + itungBatasAtas + " dan " + itungBatasBawah + ")");
                    angular.forEach(data, function (value, index) {
                        if (data[index].nilaiPenawaranKurs <= itungBatasAtas && data[index].nilaiPenawaranKurs >= itungBatasBawah) {
                            hasil = {
                                hasil: 'Memenuhi'
                            };
                            $scope.listHarga.push(data[index].nilaiPenawaranKurs);
                            angular.extend(data[index], hasil);
                        } else {
                            hasil = {
                                hasil: 'Tidak Memenuhi'
                            };
                            angular.extend(data[index], hasil);
                        }

                    });
                } else { // Tidak Menggunakan Ambang Batas
                    //console.log(">> tidak menggunakan ambang batas");
                    angular.forEach(data, function (value, index) {
                        if ($scope.totalHpsBarangJasa <= data[index].nilaiPenawaranKurs) {
                            hasil = {
                                hasil: 'Tidak Memenuhi'
                            };
                            $scope.listHarga.push(data[index].nilaiPenawaranKurs);
                            angular.extend(data[index], hasil);
                        } else {
                            hasil = {
                                hasil: 'Memenuhi'
                            };
                            angular.extend(data[index], hasil);
                        }
                    });
                }
                $rootScope.nilaiTerbaik = sortNilai($scope.listHarga, 'asc');
                //cosole.log(">>nilai terbaik : "+$rootScope.nilaiTerbaik);
                hitungEvaluasi(data, $rootScope.nilaiTerbaik);
                $scope.loading = false;
                //console.log("Hasil dataPenyediaList = " + JSON.stringify($scope.dataPenyediaList));
            }).error(function (data, status, headers, config) {
                $scope.loading = true;
            });
        }

        var hitungEvaluasi = function (data, peringkat) {
                var dataPenyediaList = data;
                var hargaTerbaik = peringkat;

                if (dataPenyediaList.length > 0) {
                    angular.forEach(dataPenyediaList, function (value, index) {
                        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getByPengadaanVendor/' + $scope.pengadaanId + '/' + dataPenyediaList[index].suratPenawaran.vendor.id)
                            .success(function (data, status, headers, config) {
                                $scope.loading = false;
                                //console.log("Hasil EvaluasiHargaVendorList = " + JSON.stringify(data));
                                if (dataPenyediaList[index].hasil == 'Memenuhi') {
                                    if ($scope.sistemEvaluasiPenawaranId == 1) { // Sistem Gugur
                                        data[0].nilaiHarga = 100;
                                        data[0].nilaiTotal = (parseFloat(data[0].nilaiAdmin) + parseFloat(data[0].nilaiTeknis) + nilaiHarga) / 3
                                        data[0].isCalonPemenang = 1;
                                        data[0].isValid = 1;
                                    } else { // Merit Point                                      
                                        if (typeof data[0] !== 'undefined') {
                                            data[0].penawaranPertama = value.nilaiPenawaran;
                                            data[0].penawaranPertamaMataUang = value.mataUang;
                                            data[0].nilaiPenawaranOri = value.nilaiPenawaranKurs;
                                            if (hargaTerbaik == data[0].nilaiPenawaranOri) {
                                                data[0].nilaiHarga = 100;
                                                data[0].isCalonPemenang = 1;
                                            } else {
                                                data[0].nilaiHarga = (hargaTerbaik / value.nilaiPenawaranKurs) * 100;
                                                data[0].isCalonPemenang = 2;
                                            }
                                            if (data[0].nilaiAdmin < $rootScope.maksNilaiLulusAmin) {
                                                data[0].nilaiTotal = 0;
                                                dataPenyediaList[index].hasil = 'Gagal Evaluasi Administrasi'
                                            } else if (data[0].nilaiTeknis < $rootScope.maksNilaiLulusTeknis) {
                                                data[0].nilaiTotal = 0;
                                                dataPenyediaList[index].hasil = 'Gagal Evaluasi Teknis'
                                            } else {
                                                data[0].nilaiTotal = ((data[0].nilaiAdmin * $rootScope.ambangBatas[0].presentasiNilaiAdmin) / 100) + ((data[0].nilaiTeknis * $rootScope.ambangBatas[0].presentasiNilaiTeknis) / 100) + ((data[0].nilaiHarga * $rootScope.ambangBatas[0].presentasiNilaiHarga) / 100);
                                                data[0].isValid = 1;
                                            }
                                        }

                                    }
                                } else {
                                    data[0].nilaiHarga = 0;
                                    data[0].nilaiTotal = 0;
                                }
                                if (typeof data[0] !== 'undefined') {
                                    $rootScope.dataHasilHitunganSimpan.push(data[0]);
                                    $scope.dataPenyediaList.push(dataPenyediaList[index]);
                                    //console.log(">> "+JSON.stringify($rootScope.dataHasilHitunganSimpan));
                                }

                            }).error(function (data, status, headers, config) {
                                $scope.loading = true;
                            });
                    });
                }
            }
            /* ----------------------- END Rincian Data Penyedia Barang/Jasa --------------------------------- */


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

        // tombol kembali ke index.html
        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $scope.go('/app/promise/procurement/evaluasiharga/bidding');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/bidding');
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

        // Simpan Nilai Harga ke tabel
        $scope.saveData = function () {
            var data = $rootScope.dataHasilHitunganSimpan;

            if (data.length > 0) {
                angular.forEach(data, function (value, index) {
                    // simpan
                    var dataEvaluasiHargaVendor = {
                        id: data[index].id,
                        nilaiAdmin: data[index].nilaiAdmin,
                        nilaiTeknis: data[index].nilaiTeknis,
                        nilaiHarga: data[index].nilaiHarga,
                        nilaiTotal: data[index].nilaiTotal,
                        isCalonPemenang: data[index].isCalonPemenang,
                        isValid: data[index].isValid,
                        updated: new Date(),
                        vendor: data[index].vendor.id,
                        suratPenawaran: data[index].suratPenawaran.id
                    }
                    //console.log("Post : " + JSON.stringify(dataEvaluasiHargaVendor));

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
                    }).success(function (data, status, headers, config) {});
                });

                lanjutTahapan();
            } else {
                alert("DATA ERROR/EMPTY");
            }
        }

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
                    //$location.path('app/promise/procurement/evaluasiteknis');
                    $location.path('/app/promise/procurement/evaluasiharga/bidding');
                    
                });
            }
            /* --------------------------- END Rincian Controller -------------------------------------------- */
    }

    EvaluasiHargaTotalBiddingViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal'];

})();