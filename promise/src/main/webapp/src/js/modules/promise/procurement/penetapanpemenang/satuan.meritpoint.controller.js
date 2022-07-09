(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SatuanMeritPointController', SatuanMeritPointController);

    function SatuanMeritPointController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams) {
        var form = this;

        if (typeof $rootScope.pengadaanPenetapanPemenang !== 'undefined') {
            form.detailPengadaan = $rootScope.pengadaanPenetapanPemenang;
        } else {
            toaster.pop('error', 'Data Pengadaan Tidak Ada', '');
        }

        $scope.pengadaanId = form.detailPengadaan.id;

        //bidang-sub bidang
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        })

        //material
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.itemPengadaanMaterial = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        })

        //jasa
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.itemPengadaanJasa = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            })
        }).error(function (data, status, headers, config) {})

        form.goDetail = function (tipeTabel, list, large) {
            if (form.detailPengadaan.metodePenawaranHarga.id == 1) {
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + list.id).success(function (negosiasi, status, headers, config) {
                    angular.forEach(negosiasi, function (value, indeks) {
                        negosiasi[indeks].pengadaanId = $scope.pengadaanId;
                        //nilai Evaluasi
                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.evaluasiHargaVendor.vendor.id).success(function (nilai, status, headers, config) {
                            if (nilai.length > 0) {
                                negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                            } else {
                                negosiasi[indeks].harga = 0;
                            }
                        })
                        $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangperitem/getByItemByVendorByPengadaan/' + list.id + '/' + value.evaluasiHargaVendor.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {
                            if (penetapan.length > 0) {
                                $rootScope.edit = true;
                                $rootScope.pemenang = penetapan[0].id;
                                negosiasi[indeks].status = true;
                                negosiasi[indeks].nilaiKuantitas = penetapan[0].nilaiKualitas;
                            }
                        })
                    })
                    var modalInstance = $modal.open({
                        templateUrl: '/detail.html',
                        controller: modalInstanceController,
                        size: large,
                        resolve: {
                            data1: function () {
                                return list;
                            },
                            data2: function () {
                                return negosiasi;
                            }
                        }
                    });
                })
            } else {
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + list.id).success(function (auction, status, headers, config) {
                    angular.forEach(auction, function (value, indeks) {
                        auction[indeks].pengadaanId = $scope.pengadaanId;
                        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByVendorAndItemPengadaan/' + value.evaluasiHargaVendor.vendor.id + '/' + list.id).success(function (nilai, status, headers, config) {
                            auction[indeks].harga = nilai[0].hargaTotalAfterCondition;
                        })
                        $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangperitem/getByItemByVendorByPengadaan/' + list.id + '/' + value.evaluasiHargaVendor.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {
                            if (penetapan.length > 0) {
                                $rootScope.edit = true;
                                $rootScope.pemenang = penetapan[0].id;
                                auction[indeks].status = true;
                                auction[indeks].nilaiKuantitas = penetapan[0].nilaiKualitas;
                            }
                        })
                    })
                    var modalInstance = $modal.open({
                        templateUrl: '/detail.html',
                        controller: modalInstanceController,
                        size: large,
                        resolve: {
                            data1: function () {
                                return list;
                            },
                            data2: function () {
                                return auction;
                            }
                        }
                    });
                })
            };
        };

        var modalInstanceController = function ($scope, $http, $modalInstance, $resource, $rootScope, data1, data2) {
            $scope.data = data1;
            $scope.data2 = data2;
            $scope.data.sisa = $scope.data.kuantitas;
            var checkPemenang = {};

            $scope.kuantitasChange = function () {
                var totalKuantitas = 0;
                angular.forEach($scope.data2, function (value, indeks) {
                    if (typeof $scope.data2[indeks].nilaiKuantitas !== 'undefined') {
                        totalKuantitas = totalKuantitas + parseInt($scope.data2[indeks].nilaiKuantitas);
                    }
                })
                $scope.data.sisa = $scope.data.kuantitas - totalKuantitas;
            }

            $scope.pemenangCheck = function (selected) {
                checkPemenang = selected;
                //console.log(JSON.stringify(list));
            }

            var simpanPemenang = function (indeks) {
                //console.log('pengadaan = ' + JSON.stringify(checkPemenang.pengadaan.id));
                //console.log('item = ' + JSON.stringify(checkPemenang.item.id));
                //console.log('vendor = ' + JSON.stringify(checkPemenang.vendor.id));

                var uri = $rootScope.backendAddress + '/procurement/penetapanpemenangperitem/';
                var postFormPemenang = {};
                if ($rootScope.edit) {
                    uri = uri + 'update';
                    postFormPemenang = {
                        id: $rootScope.pemenang,
                        vendorId: checkPemenang.evaluasiHargaVendor.vendor.id,
                        itemId: checkPemenang.itemPengadaan.id,
                        pengadaanId: checkPemenang.pengadaanId,
                        nilaiKualitas: checkPemenang.nilaiKuantitas
                    };
                    //console.log(0);
                } else {
                    uri = uri + 'create';
                    postFormPemenang = {
                        vendorId: checkPemenang.evaluasiHargaVendor.vendor.id,
                        itemId: checkPemenang.itemPengadaan.id,
                        pengadaanId: checkPemenang.pengadaanId,
                        nilaiKualitas: checkPemenang.nilaiKuantitas
                    };
                    //console.log(1);
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
                    })
                    .success(function (data, status, headers, config) {
                        if (typeof data !== 'undefined') {
                            toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
                        }
                        $modalInstance.dismiss('cancel');
                    })
                    .error(function (data, status, headers, config) {

                    });
            }

            var getPenetapanPemenangList = function (itemId, vendorId, pengadaanId) {
                $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangperitem/getByItemByVendorByPengadaan/' + itemId + '/' + vendorId + '/' + pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    var id = 0;
                    if (data.length > 0) {
                        id = data[0].id;
                        //console.log('idFlag = ' + JSON.stringify(id));
                        simpanPemenang(id);
                    } else {
                        //console.log('id = ' + JSON.stringify(id));
                        simpanPemenang(id);
                    }
                }).error(function (data, status, headers, config) {})
            }

            $scope.ok = function () {
                //console.log(checkPemenang.nilaiKualitas);
                if (checkPemenang.nilaiKualitas > $scope.data.kuantitas) {
                    $scope.messageModalError = undefined;
                    $scope.messageModalError = "GAGAL DISIMPAN, Stock Barang melebihi permintaaan";
                } else if (typeof checkPemenang.nilaiKuantitas === 'undefined') {
                    $scope.messageModalError = undefined;
                    $scope.messageModalError = "GAGAL DISIMPAN, Isi kolom kuantitas pada Vendor Pemenang yang dipilih";
                } else if (Object.keys(checkPemenang).length > 0 && checkPemenang.nilaiKuantitas >= 1) {
                    $scope.messageModalError = undefined
                    getPenetapanPemenangList(checkPemenang.itemPengadaan.id, checkPemenang.evaluasiHargaVendor.vendor.id, checkPemenang.pengadaanId);
                } else {
                    $scope.messageModalError = undefined;
                    $scope.messageModalError = "GAGAL DISIMPAN, Pilih salah satu pemenang Vendor pada Tabel Penyedia Barang dan Jasa";
                }
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }
        }

        modalInstanceController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'data1', 'data2'];

        form.nextTahapanPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.nextTahapan = data;
            }).error(function (data, status, headers, config) {})
        }
        form.nextTahapanPengadaan();

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
            $scope.isLoading = false;
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        form.btnSimpan = function () {
            $scope.loading = true;

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
                    toaster.pop('success', 'Penetapan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                }
                $scope.loading = false;
            });
        }

    }
    SatuanMeritPointController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams'];
})();