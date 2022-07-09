(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KategoriPengadaanController', KategoriPengadaanController);

    function KategoriPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getKategoriPengadaanList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/kategoriPengadaanServices/getList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.kategoriPengadaanList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getKategoriPengadaanList();

        form.add = function () {
            $location.path('app/promise/procurement/master/kategoripengadaan/tambah');
        }

        form.edit = function (data) {
            $rootScope.dataIndexKategoriPengadaan = data;
            $location.path('app/promise/procurement/master/kategoripengadaan/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Kategori Pengadaan ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/master/kategoriPengadaanServices/delete/' + id).success(function (data, status, headers, config) {
                    getKategoriPengadaanList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    KategoriPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
