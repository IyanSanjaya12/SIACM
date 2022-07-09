(function () {
    'use strict';

    angular.module('naut').controller('PenawaranVendorHargaSatuanController', PenawaranVendorHargaSatuanController);

    function PenawaranVendorHargaSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $modal) {
        var pptc = this;

        /* START Detail Pengadaan */
        if (typeof $rootScope.pengadaanVendor !== 'undefined') {
            $scope.pengadaanVendor = $rootScope.pengadaanVendor;
            //console.log('$scope.PenawaranHargaDetail = ' +JSON.stringify($scope.pengadaanVendor));
        } else {
            console.log('INFO Error');
        }
        $scope.pengadaanId = $scope.pengadaanVendor.pengadaan.id;
        /*---END Detail Pengadaan----*/

        $scope.loadSuratPenawaranHarga = function () {
            $scope.loading = true;

            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                var itemPengadaanList = data;
                if (itemPengadaanList.length == 0) {
                    pptc.itemPengadaanList = {};
                } else {
                    pptc.itemPengadaanList = itemPengadaanList[0];
                    $rootScope.itemPengadaanId = pptc.itemPengadaanList.id;
                }

                $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id).success(function (data, status, headers, config) {
                    $scope.vendor = data;
                    $rootScope.idVendor = $scope.vendor.id;
                    //loadVendorItemConditionalPrice();
                    $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        var suratPenawaranList = data;
                        if (suratPenawaranList.length == 0) {
                            pptc.suratPenawaran = {};
                        } else {
                            pptc.suratPenawaran = suratPenawaranList[0];
                        }
                        pptc.loading = false;
                        $scope.suratPenawaranId = pptc.suratPenawaran.id;

                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            pptc.itemPengadaanMaterialByPengadaanIdList = data;
                            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (itemPengadaanMaterial) {
                                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByVendorAndItemPengadaan/' + $scope.vendor.id + '/' + itemPengadaanMaterial.id, {
                                    ignoreLoadingBar: true
                                }).success(function (data, status, headers, config) {
                                    $scope.loading = false;
                                    itemPengadaanMaterial.penawaranDetail = {
                                        "hargaSatuanAfterCondition": 0
                                    };
                                    if (data != null) {
                                        itemPengadaanMaterial.penawaranDetail = data;
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.loading = false;
                                });
                            });
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            pptc.itemPengadaanJasaByPengadaanIdList = data;
                            angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (itemPengadaanJasa) {
                                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByVendorAndItemPengadaan/' + $scope.vendor.id + '/' + itemPengadaanJasa.id, {
                                    ignoreLoadingBar: true
                                }).success(function (data, status, headers, config) {
                                    $scope.loading = false;
                                    //console.log('data = '+ JSON.stringify(data));
                                    itemPengadaanJasa.penawaranDetail = {
                                        "hargaSatuanAfterCondition": 0
                                    };
                                    if (data != null) {
                                        itemPengadaanJasa.penawaranDetail = data;
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.loading = false;
                                });
                            });
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                        $scope.loading = true;
                        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + $scope.vendor.id, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                            pptc.penawaranPertamaList = data;

                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                    }).error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });

                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        $scope.loadSuratPenawaranHarga();

        /*--------------------------------------------------------------------------------------*/
        $scope.back = function () {
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor');
        };

        /*--------------------------------------------------------------------------------------*/

        $scope.back = function () {
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor');
        };

        $scope.addKondisi = function () {
            $location.path('/appvendor/promise/procurement/penawaran/vendor/add/head/condition');
        };

        $scope.btnDetail = function (mt) {
            $rootScope.itemPengadaanDetail = mt;
            console.log('itemPengadaanDetail' + JSON.stringify($rootScope.itemPengadaanDetail));
            if (mt.item.itemType.id == 1) {
                $location.path('/appvendor/promise/procurement/penawaranharga/vendor/satuan/detail/material');
            } else if (mt.item.itemType.id == 2) {
                $location.path('/appvendor/promise/procurement/penawaranharga/vendor/satuan/detail/jasa');
            }
        };


    }

    PenawaranVendorHargaSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

})();