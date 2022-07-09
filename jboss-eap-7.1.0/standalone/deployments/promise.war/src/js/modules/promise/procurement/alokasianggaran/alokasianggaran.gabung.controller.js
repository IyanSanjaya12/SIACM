/**=========================================================
 * Module: AlokasiAnggaranViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlokasiAnggaranGabungController', AlokasiAnggaranGabungController);

    function AlokasiAnggaranGabungController($scope, $http, $rootScope, $resource, $location, $filter) {
        var form = this;
        form.data = {};
        if (typeof $rootScope.detilAlokasiAnggaran !== 'undefined') {
            form.data = $rootScope.detilAlokasiAnggaran;
        }
        $rootScope.tableGabungAnggaran=[];

        $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getJenisList')
            .success(function (data, status, headers, config) {
                $scope.jenisList = [];
                var obj = {
                    id: 0,
                    nama: 'semua'
                }
                $scope.jenisList.push(obj);
                for (var i = 0; i < data.length; i++) {
                    $scope.jenisList.push(data[i]);
                }
            });

        $scope.back = function () {
            $location.path("/app/promise/procurement/alokasianggaran");
        }

        $scope.getList = function () {
            var jenisAnggaran = null;
            var nomor = null;
            var tahun = null;
            if (typeof form.data.jenisAnggaran !== 'undefined') {
                if (form.data.jenisAnggaran.id == 0) {
                    jenisAnggaran = null;
                } else {
                    jenisAnggaran = form.data.jenisAnggaran.id;
                }
            }
            if (typeof form.data.nomor !== 'undefined') {
                if (form.data.nomor.length == 0) {
                    nomor = null;
                } else {
                    nomor = form.data.nomor.replace('/', '%252F');
                }
            }
            if (typeof form.data.tahun !== 'undefined') {
                tahun = form.data.tahun;
            }
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getListByJenisNomorTahun/' + jenisAnggaran + '/' + nomor + '/' + tahun, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    form.list = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }

        $scope.search = function () {
            $scope.getList();
            $rootScope.tableGabungAnggaran = [];
        }

        $scope.checkedAlokasiAnggaran = function (data) {
            var index = form.list.indexOf(data);
            if (form.list[index].statusCheck == true) {
                $rootScope.tableGabungAnggaran.push(data);
            } else {
                $rootScope.tableGabungAnggaran.splice(index, 1);
            }
        };

        $scope.gabungAnggaran = function () {
            if ($rootScope.tableGabungAnggaran.length > 0) {
                $location.path("/app/promise/procurement/alokasianggaran/gabung/list");
            } else {
                alert('pilih anggaran terlebih dahulu');
            }
        }


    }

    AlokasiAnggaranGabungController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();
