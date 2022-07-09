(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KualifikasiPengadaanController', KualifikasiPengadaanController);

    function KualifikasiPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getKualifikasiPengadaanList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/KualifikasiPengadaanServices/getKualifikasiPengadaanList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.kualifikasiPengadaanList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getKualifikasiPengadaanList();

        form.add = function () {
            $location.path('app/promise/procurement/master/kualifikasipengadaan/tambah');
        }

        form.edit = function (data) {
            $rootScope.dataIndexKualifikasiPengadaan = data;
            $location.path('app/promise/procurement/master/kualifikasipengadaan/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Kualifikasi Pengadaan ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/master/KualifikasiPengadaanServices/deleteKualifikasiPengadaan/' + id).success(function (data, status, headers, config) {
                    getKualifikasiPengadaanList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    KualifikasiPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
