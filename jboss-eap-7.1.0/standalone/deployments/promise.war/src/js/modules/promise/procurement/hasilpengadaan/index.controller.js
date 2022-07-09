/**=========================================================
 * Module: HasilPengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HasilPengadaanController', HasilPengadaanController);

    function HasilPengadaanController($scope, $http, $rootScope, $resource, $location, $state) {
        var form = this;
		$scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorUserIdTahapan/' + $rootScope.userLogin.user.id + '/20')
            .success(function (data, status, headers, config) {
                form.hasilPengadaanList = data;
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorUserIdTahapan/' + $rootScope.userLogin.user.id + '/18')
                    .success(function (data, status, headers, config) {
                        angular.forEach(data, function (value, index) {
                            form.hasilPengadaanList.push(value);
                        });
                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorUserIdTahapan/' + $rootScope.userLogin.user.id + '/21')
                            .success(function (data, status, headers, config) {
                                angular.forEach(data, function (value, index) {
                                    form.hasilPengadaanList.push(value);
									$scope.loading = false;
                                });
                            });
                    });
            }).error(function (data, status, headers, config) {
            });

        form.buttonView = function (dataPengadaan) {
            $state.go('appvendor.promise.procurement-hasilpengadaan-detail', {
                dataPengadaan: dataPengadaan
            });
        }
    }

    HasilPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();