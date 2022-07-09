/**=========================================================
 * Module: PendaftaranPengadaanController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengumumanKualifikasiController', PengumumanKualifikasiController);

    function PengumumanKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;
        var tahapan = 10050000 //ini masih hard code

        $scope.getList = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pengumumanKualifikasiServices/getPengadaanNow/' + tahapan)
                .success(
                    function (data, status, headers, config) {
                        if (data.length > 0) {
                            $scope.pengumumanPengadaanList = data;
                            $scope.loading = false;
                        } else {
                            tahapan = 10060000;
                            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pengumumanKualifikasiServices/getPengadaanNow/' + tahapan)
                                .success(
                                    function (data, status, headers, config) {
                                        $scope.pengumumanPengadaanList = data;
                                        $scope.loading = false;
                                    })
                                .error(function (data, status, headers, config) {});
                        }
                    })
                .error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.detilPengadaan = data;
            $location.path('/appvendor/promise/procurement/prakualifikasi/pengumumankualifikasi/detil');
        };
    }

    PengumumanKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
