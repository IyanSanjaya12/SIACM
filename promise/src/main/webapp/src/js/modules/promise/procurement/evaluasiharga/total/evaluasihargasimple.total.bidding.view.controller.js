(function () {
    'use strict';
    angular.module('naut')

    .controller('EvaluasiHargaTotalBiddingViewSimpleController', EvaluasiHargaTotalBiddingViewSimpleController);

    function EvaluasiHargaTotalBiddingViewSimpleController(ModalService, $scope, $http, $rootScope,
        $location, toaster, $resource, $filter, $modal) {

        /* ========================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $rootScope.nilaiTerbaik = 0;
        $rootScope.dataHasilHitunganSimpan = [];
        $scope.btnSimpanStatus = true;

        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        // $scope.sistemEvaluasiPenawaranId == 2;
        $scope.listHarga = [];
        $scope.dataPenyediaList = [];

        var totalHpsBarangJasa = 0;
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
                    angular.forEach(data, function (value, index) {
                        $rootScope.totalHpsJasa = $rootScope.totalHpsJasa + data[index].totalHPS;
                    });
                    //console.log("Total HPS Kebutuhan Jasa = " + $rootScope.totalHpsJasa);
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
            //console.log("Maksimal Nilai Lulus Admin = " + $rootScope.maksNilaiLulusAmin);
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
        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------- END Data Evaluasi Teknis Pembobotan ---------------------------------- */



        /*
         * ======================= START Data Penyedia Barang/Jasa Detail ================================ */
        var hitungBarangJasa = function () {
            $scope.listHarga = [];
            $scope.dataPenyediaList = [];
            $rootScope.dataHasilHitunganSimpan = [];
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getPenawaranPertamaListMultiCurrencyByPengadaan/' + $scope.pengadaanId).success(function (data, status, headers, config) {
                totalHpsBarangJasa = $rootScope.totalHpsMaterial + $rootScope.totalHpsJasa;

                if ($rootScope.ambangBatas.length > 0) { // Menggunakan Ambang Batas
                    var itungBatasAtas = totalHpsBarangJasa + ((totalHpsBarangJasa * $rootScope.ambangBatas[0].atas) / 100);
                    var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * $rootScope.ambangBatas[0].bawah) / 100);
                    //console.log("AMBANG BATAS PENILAIAN ANTARA (" + itungBatasAtas + " dan " + itungBatasBawah + ")");

                    angular.forEach(data, function (value, index) {
                        if (totalHpsBarangJasa < data[index].penawaranPertama.nilaiPenawaranOri) {
                            if (data[index].penawaranPertama.nilaiPenawaranOri < itungBatasAtas && data[index].penawaranPertama.nilaiPenawaranOri > itungBatasBawah) {
                                hasil = {
                                    hasil: 'Memenuhi'
                                };
                                $scope.listHarga.push(data[index].penawaranPertama.nilaiPenawaranOri);
                                angular.extend(data[index], hasil);
                            } else {
                                hasil = {
                                    hasil: 'Tidak Memenuhi'
                                };
                                angular.extend(data[index], hasil);
                            }
                        } else {
                            if (data[index].penawaranPertama.nilaiPenawaranOri < itungBatasAtas && data[index].penawaranPertama.nilaiPenawaranOri > itungBatasBawah) {
                                hasil = {
                                    hasil: 'Memenuhi'
                                };
                                $scope.listHarga.push(data[index].penawaranPertama.nilaiPenawaranOri);
                                angular.extend(data[index], hasil);
                            } else {
                                hasil = {
                                    hasil: 'Tidak Memenuhi'
                                };
                                angular.extend(data[index], hasil);
                            }
                        }
                    });
                } else { // Tidak Menggunakan Ambang Batas
                    angular.forEach(data, function (value, index) {
                        //cek menggunakan mata uang asing atau tidak                            
                        if (totalHpsBarangJasa < data[index].nilaiDenganKurs) {
                            hasil = {
                                hasil: 'Tidak Memenuhi'
                            };
                            $scope.listHarga.push(data[index].nilaiDenganKurs);
                            angular.extend(data[index], hasil);
                        } else {
                            hasil = {
                                hasil: 'Memenuhi'
                            };
                            $scope.listHarga.push(data[index].nilaiDenganKurs);
                            angular.extend(data[index], hasil);
                        }
                    });
                }
                //console.log("listHarga : "+JSON.stringify($scope.listHarga));
                $rootScope.nilaiTerbaik = sortNilai($scope.listHarga, 'asc');
                // console.log("data : " + JSON.stringify(data));

                // console.log("nilai terbaik : "+$rootScope.nilaiTerbaik);

                hitungEvaluasi(data, $rootScope.nilaiTerbaik);
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = true;
            });
        }

        var hitungEvaluasi = function (data, peringkat) {
                var dataPenyediaList = data;
                var hargaTerbaik = peringkat;

                if (dataPenyediaList.length > 0) {
                    angular.forEach(dataPenyediaList, function (value, index) {
                        //getNilaiTeknis
                        $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + value.penawaranPertama.suratPenawaran.vendor.id + '/' + value.penawaranPertama.suratPenawaran.pengadaan.id)
                            .success(function (nilaiEvaluasi) {
                                data[0] = {};
                                //console.log(">>eva : "+JSON.stringify(nilaiEvaluasi));
                                if (nilaiEvaluasi.length > 0) {
                                    data[0].nilaiAdmin = nilaiEvaluasi[0].nilaiEvaluasiAdministrasi;
                                    data[0].nilaiTeknis = nilaiEvaluasi[0].nilaiEvaluasiTeknis;
                                } else {
                                    data[0].nilaiAdmin = 0;
                                    data[0].nilaiTeknis = 0;
                                }

                                if (dataPenyediaList[index].nilaiDenganKurs == hargaTerbaik) {
                                    value.isCalonPemenang = 1;
                                    data[0].isCalonPemenang = 1;
                                    data[0].nilaiHarga = 100;
                                } else {
                                    value.isCalonPemenang = 0;
                                    data[0].isCalonPemenang = 0;
                                    data[0].nilaiHarga = (hargaTerbaik / value.nilaiDenganKurs) * 100;
                                }
                                data[0].isValid = 1;
                                data[0].nilaiTotal = data[0].nilaiHarga;
                                data[0].suratPenawaran = value.penawaranPertama.suratPenawaran;
                                $rootScope.dataHasilHitunganSimpan.push(data[0]);
                                $scope.dataPenyediaList.push(value);
                            });
                        //console.log(">>" + JSON.stringify(dataPenyediaList));
                    });
                }
            }
            /*
             * ----------------------- END Rincian Data Penyedia Barang/Jasa --------------------------------- */

        /*
         * ============================ START CONTROLLER DETAIL ========================================= */
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
                    $scope.go('/app/promise/procurement/evaluasihargasimple/bidding');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasihargasimple/bidding');
            }
        }

        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document
                .write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        // fungsi target lokasi
        $scope.go = function (path) {
            $location.path(path);
        }

        // Simpan Nilai Harga ke tabel
        $scope.showBtnSaveData = true;
        $scope.saveData = function () {
            $scope.showBtnSaveData = false;
            var data = $rootScope.dataHasilHitunganSimpan;

            if (data.length > 0) {
                ModalService.showModalConfirmation('Apakah anda yakin menyimpan penawaran ini?').then(function (result) {
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
                                vendor: data[index].suratPenawaran.vendor.id,
                                suratPenawaran: data[index].suratPenawaran.id
                            }
                            //console.log(">> POST : " + JSON.stringify(dataEvaluasiHargaVendor));

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
                        }).success(function (data, status, headers, config) {});
                        

                        $scope.insertKursPengadaan();
                        $scope.updatePengadaan();
                
                    });

                });
            } else {
                alert("DATA ERROR/EMPTY");
                $scope.showBtnSaveData = true;
            }
        }

        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http
                    .get(
                        $rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId).success(
                        function (data, status, headers, config) {
                            $scope.nextTahapan = data;
                        }).error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        // update pengadaan
        $scope.updatePengadaan = function () {
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
                $scope.back();
            });
        }

        //insert kurs, agar bisa dibuka di negosiasi
        $scope.insertKursPengadaan = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/insertKursPengadaan',
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
                    mataUangId: 1, //IDR - Rupiah
                    nilai: 1
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };

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
        var ModalInstanceInputKursCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {
            $scope.btnSimpanStatus = $rootScope.btnSimpanStatus;

            $scope.ok = function () {
                if (typeof $scope.inputKursList[0].pengadaanId === 'undefined') {
                    var postData = {
                        data: []
                    };
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
                        postData.data.push({
                            pengadaanId: $rootScope.pengadaanId,
                            mataUangId: $scope.inputKursList[i].id,
                            nilai: $scope.inputKursList[i].kurs
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
                            hitungBarangJasa();
                            $scope.loading = false;
                        })
                        .error(function (data, status, headers, config) {
                            $scope.messageError = "penyimpanan gagal";
                            $scope.loading = false;
                        });
                } else {
                    //console.log('INFO inputKurs ' + JSON.stringify($scope.inputKursList));                    
                    $scope.loading = true;
                    var postData = {
                        data: []
                    };
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
                        $scope.inputKursList[i]["nilai"] = $scope.inputKursList[i].kurs;
                        postData.data.push($scope.inputKursList[i]);
                    }
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/updateKursPengadaanRaw',
                            data: postData
                        })
                        .success(function (data, status, headers, config) {
                            $scope.messageSuccess = "Penyimpanan berhasil";
                            hitungBarangJasa();
                            $scope.btnSimpanStatus = false;
                        })
                        .error(function (data, status, headers, config) {
                            $scope.messageError = "penyimpanan gagal";
                        });
                    $scope.loading = false;
                }
                //console.log('INFO KURS : ' + JSON.stringify($scope.inputKursList));
                //$modalInstance.close('closed');
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
                            for (var i = 0; i < data.length; i++) {
                                $scope.inputKursList[i] = {
                                    kursPengadaanId: data[i].id,
                                    pengadaanId: data[i].pengadaan.id,
                                    mataUangId: data[i].mataUang.id,
                                    nama: data[i].mataUang.nama,
                                    kode: data[i].mataUang.kode,
                                    kurs: data[i].nilai
                                };
                            }
                            $scope.loading = false;
                        } else {
                            $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
                                .success(function (masterKurs, status, headers, config) {

                                    $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + $rootScope.pengadaanId)
                                        .success(function (data, status, headers, config) {
                                            angular.forEach(masterKurs, function (value, indeks) {
                                                for (var i = 0; i < data.length; i++) {
                                                    if (value.id == data[i].mataUang.id) {
                                                        var tempObj = {
                                                            id: value.id,
                                                            nama: value.nama,
                                                            kode: value.kode,
                                                            kurs: value.kurs
                                                        };

                                                        $scope.inputKursList.push(tempObj);
                                                        break;
                                                    }
                                                }
                                            });
                                        });

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
        ModalInstanceInputKursCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*END modal Input Kurs*/
    }

    EvaluasiHargaTotalBiddingViewSimpleController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal'];

})();