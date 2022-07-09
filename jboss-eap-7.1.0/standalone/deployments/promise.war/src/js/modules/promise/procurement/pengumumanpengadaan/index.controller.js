/**=========================================================
 * Module: PendaftaranPengadaanController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengumumanPengadaanController', PengumumanPengadaanController);

    function PengumumanPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;
        var tahapan = 5 //ini masih hard code

        $scope.getList = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/PengumumanPengadaanServices/getPengadaanNow/' + tahapan)
                .success(
                    function (data, status, headers, config) {
                        if (data.length > 0) {
                            $scope.pengumumanPengadaanList = data;
                            $scope.loading = false;
                        } else {
                            tahapan = 6;
                            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/PengumumanPengadaanServices/getPengadaanNow/' + tahapan)
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
            $location.path('/appvendor/promise/procurement/pengumumanpengadaan/view');
        };
    }

    PengumumanPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
