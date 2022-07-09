/**=========================================================
 * Module: UndanganKualifikasiController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UndanganKualifikasiController', UndanganKualifikasiController);

    function UndanganKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        $rootScope.pengadaan = {};
        $rootScope.vendor = {};

        $scope.getViewDetil = function (pengadaan) {
            $rootScope.pengadaan = pengadaan;
            $location.path("/appvendor/promise/procurement/prakualifikasi/undanganvendorkualifikasi/detil");
        };

        $scope.getIndexPengadaan = function () {
            $scope.loading = true;
            var tahapan = 10050000 // ini masih hardcode
                //console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/undanganVendorKualifikasiServices/getUndanganVendorKualifikasiListByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                .success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        $scope.udanganPengadaanList = data;
                        $scope.loading = false;
                    } else {
                        tahapan = 10060000;
                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/undanganVendorKualifikasiServices/getUndanganVendorKualifikasiListByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                            .success(function (data, status, headers, config) {
                                $scope.udanganPengadaanList = data;
                                $scope.loading = false;
                            })
                            .error(function (data, status, headers, config) {
                                console.log('Error loading jenis pengadaan');
                                $scope.loading = false;
                            });
                    }
                })
                .error(function (data, status, headers, config) {
                    console.log('Error loading jenis pengadaan');
                    $scope.loading = false;
                });

        };
        $scope.getIndexPengadaan();

    }

    UndanganKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
