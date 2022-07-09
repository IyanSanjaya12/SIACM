(function () {
    'use strict';

    angular.module('naut').controller('PemasukanPenawaranViewSatuanController', PemasukanPenawaranViewSatuanController);

    function PemasukanPenawaranViewSatuanController($scope, $http, $rootScope, $resource, $location, toaster, RequestService) {
        var pPengadaan = this;
        pPengadaan.detilPengadaan = {};

        /* START Detail Pengadaan */
        if (typeof $rootScope.detilPengadaan === 'undefined') {
            console.log('$rootScope.detilPengaaddan No Data');
        } else {
            pPengadaan.detilPengadaan = $rootScope.detilPengadaan;
            //console.log('Info Detil Pengadaan pPengadaan.detilPengadaan: '+JSON.stringify(pPengadaan.detilPengadaan));
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        /*---END Detail Pengadaan----*/

        /* START Bidang Usaha Pengadaan*/
        $scope.getBidangPengadaan = function () {
            $scope.loading = true;
            if (typeof $rootScope.subBidangUsahaByPengadaanIdList === 'undefined') {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    pPengadaan.subBidangUsahaByPengadaanIdList = data;
                    $rootScope.subBidangUsahaByPengadaanIdList = pPengadaan.subBidangUsahaByPengadaanIdList;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            } else {
                pPengadaan.subBidangUsahaByPengadaanIdList = $rootScope.subBidangUsahaByPengadaanIdList;
            }
        }
        $scope.getBidangPengadaan();
        /*---END Bidang Usaha Pengadaan---*/

        /* START Rincian Kebutuhan Material*/
        $scope.getMaterialPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pPengadaan.itemPengadaanMaterialByPengadaanIdList = data;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        $scope.getMaterialPengadaan();
        $scope.itemId = $rootScope.detilPengadaan.id;
        /*---END Rincian Kebutuhan Materrial---*/

        /* START Rincian Kebutuhan Jasa*/
        $scope.getJasaPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pPengadaan.itemPengadaanJasaByPengadaanIdList = data;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        $scope.getJasaPengadaan();
        /*---END Rincian Kebutuhan Jasa---*/


        /*--------------------------------------------------------------------------------------*/
        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/pemasukanPenawaran');
                }
            } else {
                $location.path('/app/promise/procurement/pemasukanPenawaran');
            }
        };

        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").remove();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        /*--------------------------------------------------------------------------------------*/

        $scope.pemasukanPenawaran = function (itemId, pengadaanId) {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranItemServices/getListPenawaranItemByItemAndPengadaan/' + $scope.itemId + '/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    pPengadaan.detilItemPengadaan = data;
                })
                .error(function (data, status, headers, config) {
                    console.log('No Data Penawaran Item');
                    $scope.loading = false;
                });

        };

        pPengadaan.detailPengadaan = function (dp) {
            $rootScope.detilItemPengadaan = {
                pengadaanId: dp.pengadaan.id,
                itemId: dp.item.id
            };

            //console.log(JSON.stringify(dp));
            $location.path('/app/promise/procurement/pemasukanPenawaran/detailSatuan');
        };

        $scope.savePembukaanPenawaran = function () {
            $scope.loadingSaveToPembukaan = true;
            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/create',
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
                        pengadaan: $scope.pengadaanId
                    }
                })
                .success(function (data, status, headers, config) {
                    $scope.loadingSaveToPembukaan = false;
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Pemasukan Penawaran', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                    }
                })
                .error(function (data, status, headers, config) {
                    $scope.loadingSaveToPembukaan = false;
                });
        }

        /*----------------------------------------------SIMPAN TAHAPAN------------------------------------------------*/
        $scope.btnSimpan = function () {
                RequestService.modalConfirmation("Anda yakin ingin menyimpan data Tahapan Pemasukan Penawaran?")
                    .then(function (resp) {
                        $scope.loading = true;
                        $scope.savePembukaanPenawaran();

                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
                            .success(function (data, status, headers, config) {
                                var nextTahapanId = data;
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
                                        tahapanPengadaanId: nextTahapanId
                                    }
                                }).success(function (data, status, headers, config) {
                                    $scope.loading = false;
                                    if (typeof data !== 'undefined') {
                                        toaster.pop('success', 'Pemasukan Penawaran', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                                    }
                                    //console.log('INFO update tahapan sukses');
                                    $scope.loading = true;
                                    $scope.statusSimpan = true;
                                    $scope.loading = false;
                                    $scope.btnSimpan = false;
                                    $scope.backStatus = true;
                                });
                            })
                            .error(function (data, status, headers, config) {
                                console.log('ERROR : get next tahapan');
                                $scope.loading = false;
                            });
                    });
            }
            /*----------------------------------------------END SIMPAN TAHAPAN---------------------------------------------*/

    }

    PemasukanPenawaranViewSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService'];

})();