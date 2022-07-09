/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .factory('TotalHPSPengadaan', TotalHPSPengadaan)
        .controller('PembukaanPenawaranSatuanController', PembukaanPenawaranSatuanController);

    function PembukaanPenawaranSatuanController($http, $scope, $rootScope, $resource, $location, $modal, $q, $timeout, TotalHPSPengadaan, ngTableParams) {
        var pptc = this;
        pptc.pengadaanId = $rootScope.ppEdit.pengadaan.id;
        pptc.namaPengadaan = $rootScope.ppEdit.pengadaan.namaPengadaan;
        pptc.jenisPengadaan = $rootScope.ppEdit.pengadaan.jenisPengadaan;
        pptc.metodePengadaan = $rootScope.ppEdit.pengadaan.metodePengadaan;
        pptc.kualifikasiPengadaan = $rootScope.ppEdit.pengadaan.kualifikasiPengadaan;
        pptc.metodePenawaranHarga = $rootScope.ppEdit.pengadaan.metodePenawaranHarga;
        pptc.sistemEvaluasiPenawaran = $rootScope.ppEdit.pengadaan.sistemEvaluasiPenawaran;
        pptc.mataUang = $rootScope.ppEdit.pengadaan.mataUang;
        pptc.kualifikasiVendor = $rootScope.ppEdit.pengadaan.kualifikasiVendor;
        if ((typeof $rootScope.ppEdit.pengadaan.tahapanPengadaan === 'undefined') || $rootScope.ppEdit.pengadaan.tahapanPengadaan == null) {
            pptc.tahapanPengadaan = 0;
        } else {
            pptc.tahapanPengadaan = $rootScope.ppEdit.pengadaan.tahapanPengadaan.id;
        }
        pptc.pembukaanPenawaranId = $rootScope.ppEdit.id;

        $scope.btnSimpanStatus = true;
        if (pptc.tahapanPengadaan == 10)
            $scope.btnSimpanStatus = false;

        //Bidang Usaha Pengadaan relasi Pembukaaan Penawaran
        $scope.getBidangPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + pptc.pengadaanId)
                .success(function (data,
                    status, headers, config) {
                    pptc.subBidangUsahaByPengadaanIdList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getBidangPengadaan();

        pptc.keterangan = $rootScope.ppEdit.pengadaan.keterangan;

        pptc.go = function (path) {
            $location.path(path);
        };

        //list Material
        pptc.materialPengadaan = {};
        $scope.getMaterialPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + pptc.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    pptc.materialPengadaan = data;
                    var itemPengadaanMaterial = data;
                    pptc.itemPengadaanMaterial = new ngTableParams({
                        page: 1,
                        count: 10
                    }, {
                        total: itemPengadaanMaterial.length,
                        getData: function ($defer, params) {
                            $defer.resolve(itemPengadaanMaterial.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                        }
                    });
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getMaterialPengadaan();

        //list Jasa
        $scope.getJasaPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + pptc.pengadaanId)
                .success(function (data, status, headers, config) {
                    var itemPengadaanJasa = data;
                    $scope.loading = false;
                    pptc.itemPengadaanJasa = new ngTableParams({
                        page: 1,
                        count: 10
                    }, {
                        total: itemPengadaanJasa.length,
                        getData: function ($defer, params) {
                            $defer.resolve(itemPengadaanJasa.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                        }
                    });
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getJasaPengadaan();


        //absen Panitia
        //select Jabatan
        $scope.getJabatan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
                .success(function (data, status, headers, config) {
                    $scope.optionJabatan = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getJabatan();

        pptc.kehadiranPanitiaList = [];
        $scope.getAbsenPanitia = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirPanitiaServices/getListByPembukaanPenawaran/' + pptc.pembukaanPenawaranId)
                .success(function (data, status, headers, config) {
                    pptc.kehadiranPanitiaList = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getAbsenPanitia();

        pptc.addHadir = function () {
            pptc.inserted = {
                id: pptc.kehadiranPanitiaList.length + 1,
                isNew: true
            };
            pptc.kehadiranPanitiaList.push(pptc.inserted);
        };
        pptc.removeHadir = function (index) {
            pptc.kehadiranPanitiaList.splice(index, 1);
        };

        // check jadwal pengadaan
        var jadwalPengadaan = {};

        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + pptc.pengadaanId)
                    .success(function (data, status, headers, config) {
                        $scope.nextTahapan = data;
                    })
                    .error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        //update pengadaan
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
                    pengadaanId: pptc.pengadaanId,
                    tahapanPengadaanId: $scope.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                //alert('error');
            });
        };

        //simpan pembukaan penawaran satuan
        pptc.btnSimpan = function () {
            $scope.messageError = "";
            //$scope.postFormKehadiranVendor = $scope.checkedKehadiranVendor;
            $scope.getKursPengadaanList = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + pptc.pengadaanId)
                    .success(function (data, status, headers, config) {
                        var validate = true;
                        var message = "";

                        if (data.length == 0) {
                            message += "<strong>Kurs Mata Uang</strong> belum dimasukan <br/>";
                            validate = false;
                        }
                        if (pptc.kehadiranPanitiaList.length == 0) {
                            message += "<strong>Absensi Panitia</strong> belum ada.<br/>";
                            validate = false;
                        }

                        if (validate) {
                            $scope.simpanPembukaanPenawaran = function () {
                                $scope.loading = true;
                                //insert panitia
                                for (var i = 0; i < pptc.kehadiranPanitiaList.length; i++) {
                                    $scope.loading = true;
                                    $http({
                                            method: 'POST',
                                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirPanitiaServices/create',
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
                                                nama: pptc.kehadiranPanitiaList[i].nama,
                                                pembukaanPenawaran: pptc.pembukaanPenawaranId,
                                                jabatan: pptc.kehadiranPanitiaList[i].jabatan
                                            }
                                        })
                                        .success(function (data, status, headers, config) {
                                            pptc.kehadiranPanitiaList = data;
                                            $scope.loading = false;
                                        });
                                }
                                
                                //update pengadaan                                
                                $scope.updatePengadaan();
                                
                                $scope.messageSuccess = "Simpan Pembukaan Penawaran Berhasil";
                                $scope.btnSimpanStatus = false;

                            }
                            $scope.simpanPembukaanPenawaran();
                        } else {
                            $scope.messageError = message;
                        }
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            };
            $scope.getKursPengadaanList();
        };

        /*modal Input Kurs*/
        pptc.goKurs = function (size) {
            $rootScope.btnSimpanStatus = $scope.btnSimpanStatus;
            var modalInstance = $modal.open({
                templateUrl: '/inputKurs.html',
                controller: ModalInstanceInputKursCtrl,
                size: size
            });
        };

        var ModalInstanceInputKursCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {
            $scope.inputKursList = [];
            $scope.btnSimpanStatus = $rootScope.btnSimpanStatus;

            $scope.ok = function () {
                if (typeof $scope.inputKursList[0].pengadaanId === 'undefined') {
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
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
                                    pengadaanId: pptc.pengadaanId,
                                    mataUangId: $scope.inputKursList[i].id,
                                    nilai: $scope.inputKursList[i].kurs
                                }
                            })
                            .success(function (data, status, headers, config) {
                                $scope.messageSuccess = "Penyimpanan berhasil";
                                $scope.btnSimpanStatus = false;
                                $scope.loading = false;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.messageError = "penyimpanan gagal";
                                $scope.loading = false;
                            });
                    }

                } else {
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
                        $scope.loading = true;
                        $scope.inputKursList[i]["nilai"] = $scope.inputKursList[i].kurs;
                        $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/updateKursPengadaan',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: $scope.inputKursList[i]
                            })
                            .success(function (data, status, headers, config) {
                                $scope.messageSuccess = "Penyimpanan berhasil";
                                $scope.btnSimpanStatus = false;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.messageError = "penyimpanan gagal";
                            });
                    }

                    $scope.loading = false;
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.getMataUang = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + pptc.pengadaanId)
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
                                .success(function (data, status, headers, config) {
                                    $scope.inputKursList = data;
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

        pptc.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/pembukaaanPenawaran');
                }
            } else {
                $location.path('/app/promise/procurement/pembukaaanPenawaran');
            }
        };

        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        /*
         * form pembukaan penawaran detail untuk setiap material atau jasa
         * di bedakan berdasarkan tipe dari material atau jasa
         */
        var dataPembukaanPenawaranSatuan = {};

        pptc.getPembukaanPenawaranDetil = function (itemId) {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaranSatuanServices/getList/' + pptc.pengadaanId + '/' + itemId.id)
                .success(function (data1, status, headers, config) {
                    $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/getSatuanWithDeviasiByPengadaan/' + pptc.pengadaanId + '/' + itemId.item.id)
                        .success(function (data2) {
                            $scope.loading = false;
                            for (var keySuratPenawaran in data2) {
                                data2[keySuratPenawaran].itemId = itemId.id;
                                data2[keySuratPenawaran].centang = false;
                                data2[keySuratPenawaran].pengadaan = $rootScope.ppEdit.pengadaan;
                                for (var keyDataSatuan in data1) {
                                    if (data2[keySuratPenawaran].vendor.id === data1[keyDataSatuan].vendor.id) {
                                        data2[keySuratPenawaran].centang = true;
                                        data2[keySuratPenawaran].pembukaanPenawaranSatuanId = data1[keyDataSatuan].id
                                    }
                                }
                            }
                            var modalDetailInstance = $modal.open({
                                templateUrl: '/pembukaanPenawaranDetail.html',
                                controller: modalDetailCtrl,
                                size: 'lg',
                                resolve: {
                                    data2: function () {
                                        return data2;
                                    }
                                }
                            });
                        })
                        .error(function (err) {
                            $scope.loading = false;
                            console.error(err);
                        });
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }

        var modalDetailCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, data2) {
            $scope.data2 = data2;
            $scope.ok = function () {
                var dataOutput = [];
                angular.forEach($scope.data2, function (value1, key1) {
                    if (value1.centang === true) {
                        var uri = $rootScope.backendAddress + '/procurement/pembukaanPenawaranSatuanServices/create';
                        if (value1.pembukaanPenawaranSatuanId !== undefined) {
                            uri = $rootScope.backendAddress + '/procurement/pembukaanPenawaranSatuanServices/update';
                        }
                        // insert into  data pembukaan penawaran satuan
                        $http({
                            method: 'POST',
                            url: uri,
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
                                id: value1.pembukaanPenawaranSatuanId,
                                pengadaanId: value1.pengadaan.id,
                                itemPengadaanId: value1.itemId,
                                vendorId: value1.vendor.id
                            }
                        });
                    }
                    if (value1.centang === false && value1.pembukaanPenawaranSatuanId > 0) {
                        // insert into  data pembukaan penawaran satuan
                        $http({
                            method: 'GET',
                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaranSatuanServices/delete/' + value1.pembukaanPenawaranSatuanId
                        });
                    }

                });
                //
                $modalInstance.dismiss('OK');
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }
        }
        modalDetailCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'data2'];

    }

    PembukaanPenawaranSatuanController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', '$q', '$timeout', 'TotalHPSPengadaan', 'ngTableParams'];

    function TotalHPSPengadaan($q, $timeout, $http, $rootScope) {
        var getTotalHPSPengadaan = function (pengadaanId) {
            var defered = $q.defer();
            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + pengadaanId)
                    .success(function (data, status, headers, config) {
                        var totalHPS = 0;
                        for (var i = 0; i < data.length; i++) {
                            totalHPS = totalHPS + data[i].totalHPS;
                        }
                        //console.log('total HPS x = ' + totalHPS);
                        defered.resolve(totalHPS);
                    });
            }, 1000);
            return defered.promise;
        };

        return {
            getTotalHPSPengadaan: getTotalHPSPengadaan
        }
    }
})();