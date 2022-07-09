(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KondisiPengadaanController', KondisiPengadaanController);

    function KondisiPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getItemList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/kondisiPengadaanServices/getList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.kondisiPengadaanList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getItemList();

        form.add = function () {
            $location.path('app/promise/procurement/master/kondisipengadaan/tambah');
        }

        form.edit = function (data) {
            $rootScope.indexKondisiPengadaan = data;
            $location.path('app/promise/procurement/master/kondisipengadaan/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Kondisi Pengadaan ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/master/kondisiPengadaanServices/deleteKondisiPengadaan/' + id).success(function (data, status, headers, config) {
                    getItemList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    KondisiPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
