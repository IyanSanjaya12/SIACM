/**=========================================================
 * Module: UndanganPengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UndanganPengadaanController', UndanganPengadaanController);

    function UndanganPengadaanController($scope, $http, $rootScope, $resource, $location) {
        $rootScope.pengadaan = {};
        $rootScope.vendor = {};

        $scope.getViewDetil = function (pengadaan) {
            $rootScope.pengadaan = pengadaan;
            $location.path("/appvendor/promise/procurement/undanganvendor/detil");
        };

        $scope.getIndexPengadaan = function () {
            $scope.loading = true;
            var tahapan = 5 // ini masih hardcode
                //console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/getUndanganVendorListByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                .success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        $scope.udanganPengadaanList = data;
                        $scope.loading = false;
                    } else {
                        tahapan = 6;
                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/getUndanganVendorListByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
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

    UndanganPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
