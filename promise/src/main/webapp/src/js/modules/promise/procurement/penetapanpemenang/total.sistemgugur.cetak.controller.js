(function () {
    'use strict';

    angular
        .module('naut')
        .controller('TotalSistemGugurCetakController', TotalSistemGugurCetakController);

    function TotalSistemGugurCetakController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams, $filter, RequestService) {
        var form = this;

        if (typeof $rootScope.pengadaanPenetapanPemenang !== 'undefined') {
            form.detailPengadaan = $rootScope.pengadaanPenetapanPemenang;
        } else {
            console.log('Error');
        }
        $scope.pengadaanId = form.detailPengadaan.id;

        //jeniskontrak
        form.jenisKontrak = 0; //Normal default
        $http.get($rootScope.backendAddress + '/procurement/master/JenisKontrakServices/findAll')
            .success(function (data) {
                $scope.jenisKontrakList = data;
            })
            .error(function (data) {
                console.error("No data");
            });

        $scope.showJenisKontrak = function () {
            console.log("jenis kontrak : " + JSON.stringify(form.jenisKontrak));
        }

        //get has PR
        $scope.isJoinPR = false;
        $http.get($rootScope.backendAddress + "/procurement/inisialisasi/purchaserequestpengadaan/findByPengadaanId/" + $scope.pengadaanId)
            .success(function (data) {
                if (data.length > 0) {
                    if (data[0].purchaseRequest.isJoin == 2) {
                        $scope.isJoinPR = true;
                    }
                }

            });

        form.nextTahapanPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.nextTahapan = data;
            }).error(function (data, status, headers, config) {})
        }
        form.nextTahapanPengadaan();
        $scope.getUpdateTahapan = function () {
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
                    tahapanPengadaanId: form.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.saveBtnStatus = true;
                $location.path('/app/promise/procurement/penetapanpemenang');
            });
        }

        var checkPemenangVendor = {};
        $scope.loading = true;
        //get nilai harga
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId)
            .success(function (negosiasi, status, headers, config) {
                negosiasi = $filter('filter')(negosiasi, {
                    nilaiTotal: ('!' + null)
                });
                angular.forEach(negosiasi, function (value, indeks) {
                    negosiasi[indeks].pengadaanId = $scope.pengadaanId;
                    negosiasi[indeks].nilaiHarga = value.nilaiHarga;
                    //nilai Evaluasi
                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id)
                        .success(function (nilai, status, headers, config) {
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
                                        if (typeof dataPenawaranPertama.nilaiPenawaran !== 'undefined' && dataPenawaranPertama.nilaiPenawaran !== null) {
                                            negosiasi[indeks].harga = dataPenawaranPertama.nilaiPenawaran;
                                            negosiasi[indeks].mataUang = dataPenawaranPertama.mataUang;
                                            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasi[indeks].mataUang.id)
                                                .success(function (data) {
                                                    negosiasi[indeks].hargaWithKurs = data.nilai * negosiasi[indeks].harga;
                                                });
                                        } else {
                                            //jika tidak ada penawaran pertama = simple auction
                                            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getDistinctPenawaranAuctionAndLastAuctionWithKursByPengadaan/' + $scope.pengadaanId + '/' + value.vendor.id)
                                                .success(function (dataAuction) {
                                                    negosiasi[indeks].harga = dataAuction.nilaiPenawaran;
                                                    negosiasi[indeks].mataUang = dataAuction.mataUang;
                                                    negosiasi[indeks].hargaWithKurs = dataAuction.totalPenawaranKonfersi;
                                                });
                                        }

                                    });
                            }
                        });
                    $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {

                        if (penetapan.length > 0) {
                            $scope.edit = true;
                            $scope.pemenang = penetapan[0].id;
                            negosiasi[indeks].status = true;
                        }
                    });
                    if ((indeks + 1) == negosiasi.length) {
                        $scope.loading = false;
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
                })
            });

        //vendor-surat penawaran
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getSuratPenawaranByPengadaanIdNilaiJaminan/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.vendorList = data;
            for (var i = 0; i < data.length; i++) {
                $scope.asynchronousNilai(i, data[i].vendor.id);
                //console.log(i);
            }
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        })

        $scope.asynchronousNilai = function (x, id) {
            var nilai = $scope.getEvaluasiHargaVendor(id);
            nilai.then(function (nilaiEvaluasi) {
                //console.log('resolve ' + nilaiEvaluasi);
                if (typeof form.vendorList[x].nilaiAdmin !== 'undefined' && typeof form.vendorList[x].nilaiTeknis !== 'undefined') {
                    form.vendorList[x]['admin'] = nilaiEvaluasi.admin;
                    form.vendorList[x]['teknis'] = nilaiEvaluasi.teknis;
                    form.vendorList[x]['status'] = false;
                } else {
                    form.vendorList[x].admin = nilaiEvaluasi.admin;
                    form.vendorList[x].teknis = nilaiEvaluasi.teknis;
                    form.vendorList[x]['status'] = false;
                }
                if (x == 0) {
                    //console.log(x);
                    form.vendorList[x].status = true;
                    checkPemenangVendor = form.vendorList[x];
                    //console.log('check 1 = '+JSON.stringify($scope.checkPemenangVendor));
                }
                //console.log('index ' + x + '' + JSON.stringify(form.vendorList));
                if ((x + 1) == form.vendorList.length) {
                    form.evaluasiHargaVendor = new ngTableParams({
                        page: 1,
                        count: 10
                    }, {
                        total: form.vendorList.length,
                        getData: function ($defer, params) {
                            $defer.resolve(form.vendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                        }
                    });
                }
            }, function (reject) {});
        }

        form.pemenangCheck = function (list) {
            checkPemenangVendor = {};
            checkPemenangVendor = list;
        }

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
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getByPengadaanVendor/' + $scope.pengadaanId + '/' + vendorId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        nilaiEvaluasi.admin = data[0].nilaiAdmin;
                        nilaiEvaluasi.teknis = data[0].nilaiTeknis;
                        //console.log('nilai admin ' + nilaiEvaluasi.admin);
                        //console.log('nilai teknis ' + nilaiEvaluasi.teknis);
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

        form.isSimple = false;

        $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/isSimpleByPengadaanId/' + $scope.pengadaanId).success(function (simple, status, headers, config) {

            form.isSimple = simple;
        });


        var simpanPemenang = function (indeks) {
            var uri = $rootScope.backendAddress + '/procurement/penetapanpemenangtotal/';
            var postFormPemenang = {};
            if ($scope.edit) {
                uri = uri + 'update';
                postFormPemenang = {
                    id: $scope.pemenang,
                    vendorId: checkPemenangVendor.vendor.id,
                    pengadaanId: $scope.pengadaanId
                };
            } else {
                uri = uri + 'createwithjeniskontrak';
                postFormPemenang = {
                    vendorId: checkPemenangVendor.vendor.id,
                    pengadaanId: $scope.pengadaanId,
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

            }).error(function (data, status, headers, config) {})

        }

        var getPenetapanPemenangList = function (vendorId, pengadaanId) {
            $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + vendorId + '/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    var id = 0;
                    if (data.length > 0) {
                        id = data[0].id;
                        simpanPemenang(id);
                    } else {
                        simpanPemenang(id);
                    }
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Penetapan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                    }
                })
                .error(function (data, status, headers, config) {})
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
        };

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

        
        $scope.errorMsg = "";
        form.btnSimpan = function () {
            if (form.jenisKontrak == 0) {
                $scope.errorMsg = "Jenis Kontrak Harus dipilih!";
                RequestService.informError($scope.errorMsg);
                return false;
            } else if (typeof checkPemenangVendor.vendor === 'undefined') {
                $scope.errorMsg = "Pemenang harus dipilih!";
                RequestService.informError($scope.errorMsg);
                return false;
            } else {
                $scope.loading = true;
                getPenetapanPemenangList(checkPemenangVendor.vendor.id, $scope.pengadaanId);
                $scope.loading = false;
                $scope.getUpdateTahapan();
            }
        };

        $rootScope.switchBack = "/app/promise/procurement/penetapanpemenang/totalSistemGugur";
        $rootScope.switchMultiwinner = "/app/promise/procurement/penetapanpemenang/multiwinner";
        $scope.btnSwitchMultiwinner = function () {
            $location.path($rootScope.switchMultiwinner);
        }
    }
    TotalSistemGugurCetakController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams', '$filter', 'RequestService'];
})();