(function () {
    'use strict';

    angular
        .module('naut')
        .controller('JenisPajakController', JenisPajakController);

    function JenisPajakController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getJenisPajakList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.jenisPajakList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getJenisPajakList();

        form.add = function () {
            $location.path('app/promise/procurement/master/jenispajak/tambah');
        }

        form.edit = function (data) {
            $rootScope.dataIndexJenisPajak = data;
            $location.path('app/promise/procurement/master/jenispajak/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Jenis Pajak ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/deleteJenisPajak/' + id).success(function (data, status, headers, config) {
                    getJenisPajakList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    JenisPajakController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
