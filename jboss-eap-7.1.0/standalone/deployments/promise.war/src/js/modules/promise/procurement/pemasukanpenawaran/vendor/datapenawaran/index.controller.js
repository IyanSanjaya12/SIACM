/**=========================================================
 * Module: PemasukanPenawaranController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataPenawaranVendorController', DataPenawaranVendorController);

    function DataPenawaranVendorController($scope, $http, $rootScope, $resource, $location, $state) {
        var pp = this;

        $rootScope.ppDokAdministrasi = {};
        $rootScope.ppDokTeknis = {};
        $rootScope.ppDokPenawaran = {};
        var tahapan = {};

        pp.go = function (path) {
            $location.path(path);
        };

        if (typeof $rootScope.listPengadaan !== 'undefined') {
            if ($rootScope.listPengadaan.tahapanPengadaan.tahapan.id == 10170300) {
                tahapan = 10170300; // Pemasukan Penawaran Tahap Ke-1 untuk Pengadaan 2 Tahap
            } else if ($rootScope.listPengadaan.tahapanPengadaan.tahapan.id == 10170200) {
                tahapan = 10170200; // Pemasukan Penawaran Tahap Ke-2 untuk Pengadaan 2 Tahap
            } else {
                tahapan = $rootScope.listPengadaan.tahapanPengadaan.tahapan.id;
            }
        } else {
            tahapan = 9; // Pemasukan Penawaran untuk Pengadaan 1 Sampul
        }

        $scope.getPengadaanList = function () {
            $scope.loading = true;
            if (tahapan == 10170200) {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                    .success(function (data, status, headers, config) {
                        angular.forEach(data, function (value, index) {
                            pp.pemasukanPenawaranList = data;
                        })
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {
                        $scope.loading = true;
                    });
            } else if (tahapan == 10170300) {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                    .success(function (data, status, headers, config) {
                        angular.forEach(data, function (value, index) {
                            pp.pemasukanPenawaranList = data;
                        })
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {
                        $scope.loading = true;
                    });
            } else {
                //simple auction & bidding tidak ditampilkan
                if (tahapan != 13000001 && tahapan != 14000001) {
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                        .success(function (data, status, headers, config) {
                            pp.pemasukanPenawaranList = data;
                            $scope.loading = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });
                }

            }


        };
        $scope.getPengadaanList();



        /*pp.btnSatuSampul = function(pengadaan){
            $rootScope.pengadaanVendor = pengadaan;
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor/satu/sampul')
        }

        pp.btnDuaSampul = function (pengadaan) {
            $rootScope.pengadaanVendor = pengadaan;
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor/dua/sampul');
        };

        pp.btnDetilTahap = function (pengadaan) {
            $rootScope.pengadaanVendor = pengadaan;
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor/tahap/satu');
        };

        pp.btnDetilTahapDua = function (pengadaan) {
            $rootScope.pengadaanVendor = pengadaan;
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor/tahap/dua');
        };*/

        pp.btnDetil = function (pengadaan) {

            $rootScope.pengadaanVendor = pengadaan;
            if (pengadaan.metodePenyampaianDokumenPengadaan.id == 1) {
                $location.path('/appvendor/promise/procurement/datapenawaran/vendor/satu/sampul');
            } else if (pengadaan.metodePenyampaianDokumenPengadaan.id == 2) {
                $location.path('/appvendor/promise/procurement/datapenawaran/vendor/dua/sampul');
            } else if (pengadaan.metodePenyampaianDokumenPengadaan.id == 3) {
                if (tahapan == 10170200) {
                    $state.go('appvendor.promise.procurement-vendor-datapenawaranvendortahapsatu', {
                        dataPengadaan: pengadaan
                    });
                } else if (tahapan == 10170300) {
                    $state.go('appvendor.promise.procurement-vendor-pemasukanpenawaranvendortahapdua', {
                        dataPengadaan: pengadaan
                    });
                }
            } else {
                alert('Halaman tidak tersedia');
            }
        }
    }

    DataPenawaranVendorController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();