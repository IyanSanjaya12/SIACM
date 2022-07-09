(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HasilKualifikasiDetilController', HasilKualifikasiDetilController);

    function HasilKualifikasiDetilController($scope, $http, $rootScope, $resource, $location) {
        //console.log('ROOT Pengadaan : ' + JSON.stringify($rootScope.pengadaan));
        $scope.getBidangPengadaan = function () {
            $scope.loadingSubidang = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $rootScope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                    $scope.subBidangUsahaByPengadaanIdList = data;
                    $scope.loadingSubidang = false;
                }).error(function (data, status, headers, config) {
                    $scope.loadingSubidang = false;
                });
        }
        $scope.getBidangPengadaan();

        $scope.getHasilKualifikasi = function () {
            $scope.loadingHasilKualifikasi = true;
            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/hasilkualifikasi/findByPengadaanId/' + $rootScope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                    if (data.length > 0) {
                        $scope.hasilKualifikasiList = data;
                        $scope.loadingHasilKualifikasi = false;
                    } else {
                        if ($rootScope.pengadaan.jenisPengadaan.id == 1 || $rootScope.pengadaan.jenisPengadaan.id == 4) { //Barang atau Jasa Lainnya
                            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/hasilkualifikasi/initHasilKualifikasi/' + $rootScope.pengadaan.id)
                                .success(function (data, status, headers, config) {
                                    $scope.hasilKualifikasiList = data;
                                    $scope.loadingHasilKualifikasi = false;
                                })
                                .error(function (data, status, headers, config) {
                                    $scope.loadingHasilKualifikasi = false;
                                });
                        }
                    }

                })
                .error(function (data, status, headers, config) {
                    $scope.loadingHasilKualifikasi = false;
                });
        }
        $scope.getHasilKualifikasi();

    }

    HasilKualifikasiDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();