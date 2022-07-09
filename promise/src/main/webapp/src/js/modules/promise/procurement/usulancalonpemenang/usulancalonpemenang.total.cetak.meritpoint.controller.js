(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UsulanCalonPemenangTotalMeritPointCetakController', UsulanCalonPemenangTotalMeritPointCetakController);

    function UsulanCalonPemenangTotalMeritPointCetakController(ModalService, $http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams, $filter) {

        var form = this;

        if (typeof $rootScope.pengadaanUsulanCalonPemenang !== 'undefined') {
            $scope.detailPengadaan = $rootScope.pengadaanUsulanCalonPemenang;
        } else {
            console.log('Error');
        }
        $scope.pengadaanId = $scope.detailPengadaan.id;

        $scope.list = [];
        $scope.loading = true;
        if ($scope.detailPengadaan.metodePenawaranHarga.id == 1) { //bidding
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId)
                .success(function (negosiasi, status, headers, config) {
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
                                negosiasi[indeks].harga = 0;
                            }
                        });
                        $http.get($rootScope.backendAddress + '/procurement/usulancalonpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (usulan, status, headers, config) {
                            if (usulan.length > 0) {
                                negosiasi[indeks].idUsulanCalonPemenang = usulan[0].id;
                                negosiasi[indeks].finalCheck = usulan[0].finalCheck;
                            } else {
                                negosiasi[indeks].finalCheck = false;
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
                        $scope.list.push(form);
                    })
                    $scope.listVendors = negosiasi;
                })
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

                    $http.get($rootScope.backendAddress + '/procurement/usulancalonpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (usulan, status, headers, config) {
                        if (usulan.length > 0) {
                            auction[indeks].idUsulanCalonPemenang = usulan[0].id;
                            auction[indeks].finalCheck = usulan[0].finalCheck;
                        } else {
                            auction[indeks].finalCheck = false;
                        }
                    })
                    if ((indeks + 1) == auction.length) {
                        $scope.loading = false;
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
                    $scope.list.push(form);
                })
                $scope.listVendors = auction;
            })
        }

        $scope.checked = function (data) {
            var obj = $scope.listVendors.indexOf(data);
            if ($scope.listVendors[obj].finalCheck == true) {
                $scope.list[obj].finalCheck = true;
            } else {
                $scope.list[obj].finalCheck = false;
            }
        }

        $scope.proses = function (indeks) {
            $scope.loading = true;
            form = new Object();
            form.finalCheck = $scope.listVendors[indeks].finalCheck;
            form.id = $scope.listVendors[indeks].idUsulanCalonPemenang;
            form.pengadaanId = $scope.detailPengadaan.id;
            form.vendorId = $scope.listVendors[indeks].vendor.id;
            console.log(JSON.stringify(form));
            var uri = '';
            if (typeof $scope.listVendors[indeks].idUsulanCalonPemenang !== 'undefined') {
                uri = $rootScope.backendAddress + '/procurement/usulancalonpemenangtotal/update';
            } else {
                uri = $rootScope.backendAddress + '/procurement/usulancalonpemenangtotal/create';
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
                data: form
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (($scope.list.length - 1) != indeks) {
                    $scope.proses(indeks + 1);
                } else {
                    $scope.showSuccess();
                    //$scope.updatePengadaan();
                }
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        }

        $scope.showSuccess = function () {
            $scope.loadingSaving = false;
            toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
            $scope.back();
        }

        //save
        $scope.saveData = function () {
            $scope.proses(0);
            $scope.updatePengadaan();
        }

        /* get Next Tahapan */
        $scope.updatePengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.nextTahapan = data;
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
                        //$location.path('app/promise/procurement/klarifikasiharga');
                    });
                })
                .error(function (data, status, headers, config) {
                    console.log('get next error');
                });
        }

        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/usulancalonpemenang');
                }
            } else {
                $location.path('/app/promise/procurement/usulancalonpemenang');
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

            /*modal Confirmation*/
        $scope.goModalConfirmation = function (data) {
            $rootScope.saveAfterConfirmation = $scope.saveData;
            var modalInstance = $modal.open({
                templateUrl: '/ModalConfirmation.html',
                controller: ModalInstanceConfirmationCtrl,
            });
        };

        /*---Modal Confirmation---*/
        var ModalInstanceConfirmationCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {

            $scope.ok = function () {
                $rootScope.saveAfterConfirmation();
                $modalInstance.dismiss('close');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceConfirmationCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/
    }
    UsulanCalonPemenangTotalMeritPointCetakController.$inject = ['ModalService', '$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams', '$filter'];
})();