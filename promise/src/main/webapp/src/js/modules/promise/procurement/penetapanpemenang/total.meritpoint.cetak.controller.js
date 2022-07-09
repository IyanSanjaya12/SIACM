(function () {
    'use strict';
    angular
        .module('naut')
        .controller('TotalMeritPointCetakController', TotalMeritPointCetakController);

    function TotalMeritPointCetakController(ModalService, $http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, $filter, ngTableParams) {

        var form = this;

        if (typeof $rootScope.pengadaanPenetapanPemenang !== 'undefined') {
            form.detailPengadaan = $rootScope.pengadaanPenetapanPemenang;
        } else {
            console.log('Error');
        }
        $scope.pengadaanId = form.detailPengadaan.id;

        var checkPemenangVendor = {};

        //jeniskontrak
        form.jenisKontrak = 0; //Normal default
        $http.get($rootScope.backendAddress + '/procurement/master/JenisKontrakServices/findAll')
            .success(function (data) {
                $scope.jenisKontrakList = data;
            })
            .error(function (data) {
                console.error("No data");
            });
        
        $scope.showJenisKontrak = function(){
            console.log("jenis kontrak : "+form.jenisKontrak);
        }

        //get has PR
        $scope.isJoinPR = false;
        $http.get($rootScope.backendAddress + "/procurement/inisialisasi/purchaserequestpengadaan/findByPengadaanId/" + $scope.pengadaanId)
            .success(function (data) {
                if (data.length > 0) {
                    if (data[0].purchaseRequest.isJoin != 0) {
                        $scope.isJoinPR = true;
                    }
                }

            });

        //vendor-surat penawaran
        if (form.detailPengadaan.metodePenawaranHarga.id == 1) { //bidding
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (negosiasi, status, headers, config) {
                negosiasi = $filter('filter')(negosiasi, {
                    nilaiTotal: ('!' + null)
                });
                angular.forEach(negosiasi, function (value, indeks) {
                    negosiasi[indeks].pengadaanId = $scope.pengadaanId;
                    //nilai Evaluasi
                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id).success(function (nilai, status, headers, config) {
                        if (nilai.length > 0) {
                            negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                            negosiasi[indeks].mataUang = nilai[0].mataUang;
                            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasi[indeks].mataUang.id)
                                .success(function (data) {
                                    negosiasi[indeks].hargaWithKurs = data.nilai * negosiasi[indeks].harga;
                                });
                        } else {
                            //jika data nego tidak ada ambil penawaran pertama
                            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id)
                                .success(function (dataPenawaranPertama) {
                                    negosiasi[indeks].harga = dataPenawaranPertama.nilaiPenawaran;
                                    negosiasi[indeks].mataUang = nilai[0].mataUang;
                                    $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasi[indeks].mataUang.id)
                                        .success(function (data) {
                                            negosiasi[indeks].hargaWithKurs = data.nilai * negosiasi[indeks].harga;
                                        });
                                });
                        }
                    })
                    $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {

                        if (penetapan.length > 0) {
                            $scope.edit = true;
                            $scope.pemenang = penetapan[0].id;
                            negosiasi[indeks].status = true;
                        }
                    });
                    if ((indeks + 1) == negosiasi.length) {
                        form.evaluasiHargaVendor = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: negosiasi.length,
                            getData: function ($defer, params) {
                                $defer.resolve(negosiasi.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    }
                });
            });
        } else { //auction
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (auction, status, headers, config) {
                auction = $filter('filter')(auction, {
                    nilaiTotal: ('!' + null)
                });
                angular.forEach(auction, function (value, indeks) {
                    auction[indeks].pengadaanId = $scope.pengadaanId;
                    //cek dulu ada nilai nego apa tidak
                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id)
                        .success(function (nilai, status, headers, config) {
                            if (nilai.length > 0) {
                                auction[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                                auction[indeks].mataUang = nilai[0].mataUang;
                                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + auction[indeks].mataUang.id)
                                    .success(function (data) {
                                        auction[indeks].hargaWithKurs = data.nilai * auction[indeks].harga;
                                    });
                            } else {
                                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByVendorPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (nilai, status, headers, config) {
                                    if (nilai.length > 0) {
                                        auction[indeks].harga = nilai[0].nilaiPenawaran;
                                        auction[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                                        auction[indeks].mataUang = nilai[0].mataUang;
                                        auction[indeks].hargaWithKurs = data.nilai * auction[indeks].harga;
                                    } else {
                                        auction[indeks].harga = null;
                                    }
                                })
                            }
                        });

                    $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {
                        if (penetapan.length > 0) {
                            $scope.edit = true;
                            $scope.pemenang = penetapan[0].id;
                            auction[indeks].status = true;
                        }
                    })
                    if ((indeks + 1) == auction.length) {
                        form.evaluasiHargaVendor = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: auction.length,
                            getData: function ($defer, params) {
                                $defer.resolve(auction.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    }
                })
            })
        }

        form.pemenangCheck = function (list) {
            checkPemenangVendor = {};
            checkPemenangVendor = list;
        }

        /*var getKriteriaAmbangBatasList = function () {
            var defered = $q.defer();
            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    var persentasiNilaiHarga = data[0].presentasiNilaiHarga;
                    defered.resolve(persentasiNilaiHarga);
                    //console.log('$scope.persentasiNilaiHarga 1 : ' + $scope.persentasiNilaiHarga);
                }).error(function (data, status, headers, config) {})
            }, 1000);
            return defered.promise;
        };

        var dataList = {
            totalHPS: 0,
            nilaiPersentasi: 0
        };
        var getItemPengadaanList = function (data) {
            var defered = $q.defer();
            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        dataList.totalHPS = dataList.totalHPS + data[i].totalHPS;
                    }

                    getKriteriaAmbangBatasList().then(function (data) {
                        dataList.nilaiPersentasi = data;
                        //console.log('dataNilai ' + data);
                        defered.resolve(dataList);
                    });
                }).error(function (data, status, headers, config) {})
            }, 1000);
            return defered.promise;
        };*/

        //evaluasi harga vendor
        var nilaiEvaluasi = {
            admin: 0,
            teknis: 0,
            harga: 0
        };
        $scope.getEvaluasiHargaVendor = function (vendorId) {
            $scope.loading = true;
            var defered = $q.defer();
            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + vendorId + '/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        //nilaiEvaluasi.metodePenawaranHargaid = data[0].pengadaan.metodePenawaranHarga.id;
                        nilaiEvaluasi.admin = data[0].nilaiEvaluasiAdministrasi;
                        nilaiEvaluasi.teknis = data[0].nilaiEvaluasiTeknis;
                        nilaiEvaluasi.harga = data[0].nilaiEvaluasiHarga;
                        defered.resolve(nilaiEvaluasi);
                    } else {
                        defered.resolve(0);
                    }
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            }, 1000);
            return defered.promise;
        };

        form.nextTahapanPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.nextTahapan = data;
            }).error(function (data, status, headers, config) {})
        }
        form.nextTahapanPengadaan();

        var simpanPemenang = function (indeks) {
            //console.log('pengadaan = ' + JSON.stringify(checkPemenangVendor.pengadaan.id));
            //console.log('vendor = ' + JSON.stringify(checkPemenangVendor.vendor.id));

            var uri = $rootScope.backendAddress + '/procurement/penetapanpemenangtotal/';
            var postFormPemenang = {};
            if ($scope.edit) {
                uri = uri + 'update';
                postFormPemenang = {
                    id: $scope.pemenang,
                    vendorId: checkPemenangVendor.vendor.id,
                    pengadaanId: checkPemenangVendor.pengadaanId
                };
            } else {
                uri = uri + 'createwithjeniskontrak';
                postFormPemenang = {
                    vendorId: checkPemenangVendor.vendor.id,
                    pengadaanId: checkPemenangVendor.pengadaanId,
                    jenisKontrakId: form.jenisKontrak
                };
            }
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
                data: postFormPemenang
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
                }

                // Simpan ke Catalog (Untuk sementara)
                //    			var dataSimpan = {
                //    					pengadaanId: checkPemenangVendor.pengadaanId
                //    			}
                //    			RequestService.doPOST('/procurement/catalog/CatalogServices/simpanDariItemKontrak', dataSimpan)
                //            	.success(function (data, status, headers, config) {});

            }).error(function (data, status, headers, config) {})
        }

        var getPenetapanPemenangList = function (vendorId, pengadaanId) {
            $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + vendorId + '/' + pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var id = 0;
                if (data.length > 0) {
                    id = data[0].id;
                    //console.log('id = ' + JSON.stringify(id));
                    simpanPemenang(id);
                } else {
                    //console.log('id = ' + JSON.stringify(id));
                    simpanPemenang(id);
                }
            }).error(function (data, status, headers, config) {})
        }

        form.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/penetapanpemenang');
                }
            } else {
                $location.path('/app/promise/procurement/penetapanpemenang');
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


        form.btnSimpan = function () {
            $scope.loading = true;
            if (form.jenisKontrak == 0) {
                $scope.errorMsg = "Jenis Kontrak Harus dipilih!";
                toaster.pop('error', 'GAGAL DISIMPAN', $scope.errorMsg);
                $scope.loading = false;
                return false;
            } else if (Object.keys(checkPemenangVendor).length > 0) {
                ModalService.showModalConfirmation().then(function (result) {
                    getPenetapanPemenangList(checkPemenangVendor.vendor.id, checkPemenangVendor.pengadaanId);
                    //tahapan pengadaan
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
                            tahapanPengadaanId: form.nextTahapan
                        }
                    }).success(function (data, status, headers, config) {
                        if (typeof data !== 'undefined') {
                            if ($scope.isPublish == true) {
                                var postFormPengumuman = {
                                    pengadaan: form.detailPengadaan.id,
                                    tglMulai: $filter('date')((new Date()), "dd-MM-yyyy"),
                                    tglSelesai: $filter('date')((new Date()), "dd-MM-yyyy"),
                                    tipe: 2,
                                    isiPengumuman: form.detailPengadaan.namaPengadaan
                                };
                                var url = $rootScope.backendAddress + "/procurement/inisialisasi/PengumumanPengadaanServices/insert";
                                $http({
                                    method: 'POST',
                                    url: url,
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded'
                                    },
                                    transformRequest: function (obj) {
                                        var str = [];
                                        for (var p in obj)
                                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                        return str.join("&");
                                    },
                                    data: postFormPengumuman
                                }).success(function (data, status, headers, config) {
                                    toaster.pop('success', 'Penetapan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                                    $timeout(function () {
                                        $scope.loading = false;
                                        $location.path('/app/promise/procurement/penetapanpemenang');
                                    }, 2000);
                                });

                            } else {
                                toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
                                $timeout(function () {
                                    $scope.loading = false;
                                    $location.path('/app/promise/procurement/penetapanpemenang');
                                }, 2000);
                            }
                        }
                    });
                });
            } else {
                toaster.pop('error', 'GAGAL DISIMPAN', 'Pilih salah satu pemenang Vendor pada Tabel Hasil Evaluasi Administrasi');
                $scope.loading = false;
            }
        }

    }
    TotalMeritPointCetakController.$inject = ['ModalService', '$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', '$filter', 'ngTableParams'];
})();