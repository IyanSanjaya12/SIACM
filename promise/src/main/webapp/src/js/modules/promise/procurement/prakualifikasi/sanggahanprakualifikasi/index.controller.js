/**=========================================================
 * Module: SanggahanPrakualifikasiController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SanggahanPrakualifikasiController', SanggahanPrakualifikasiController);

    function SanggahanPrakualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            $scope.pengadaanList = [];
            var tahapan = 10100000 //hasil kualifikasi
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/' + tahapan)
                .success(
                    function (data, status, headers, config) {
                        angular.forEach(data, function (value, index) {
                            $scope.pengadaanList.push(value);
                        })
                        tahapan = 10110000 //sanggahan
                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/' + tahapan)
                            .success(function (data, status, headers, config) {
                                angular.forEach(data, function (value, index) {
                                    $scope.pengadaanList.push(value);
                                })

                                $scope.loading = false;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.loading = false;
                            });
                    })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.pengadaan = data;
            $location.path('/app/promise/procurement/prakualifikasi/sanggahanprakualifikasi-detil');
        };

    }

    SanggahanPrakualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();