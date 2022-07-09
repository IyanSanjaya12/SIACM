/**=========================================================
 * Module: PenawaranHargaVendorController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenawaranHargaVendorController', PenawaranHargaVendorController);

    function PenawaranHargaVendorController($scope, $http, $rootScope, $resource, $location) {
        var pp = this;

        pp.go = function (path) {
            $location.path(path);
        };

        $scope.getPengadaanList = function () {
            $scope.loading = true;
            var tahapan = 9; //masih hardcode
            if ($rootScope.tahapanId != undefined) {
                tahapan = $rootScope.tahapanId;
            }
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                .success(function (data, status, headers, config) {
                    pp.pemasukanPenawaranList = data;
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + 19000002)
                        .success(function (data, status, headers, config) {
                            angular.forEach(data, function(value, index){
                                pp.pemasukanPenawaranList.push(value);
                            });
                            $scope.loading = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };
        $scope.getPengadaanList();

        pp.btnDetil = function (pp) {
            $rootScope.pengadaanVendor = pp;
            if (pp.pengadaan.jenisPenawaran.id == 1) {
                $location.path('/appvendor/promise/procurement/penawaranharga/vendor/harga/total');
            } else if (pp.pengadaan.jenisPenawaran.id == 2) {
                $location.path('/appvendor/promise/procurement/penawaranharga/vendor/harga/satuan');
            } else {
                alert('Halaman tidak tersedia');
            }
        };


    }

    PenawaranHargaVendorController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();