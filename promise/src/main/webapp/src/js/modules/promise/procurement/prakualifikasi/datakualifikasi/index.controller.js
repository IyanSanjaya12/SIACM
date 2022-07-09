/**=========================================================
 * Module: DataKualifikasiController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataKualifikasiController', DataKualifikasiController);

    function DataKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;
        var tahapan = 10070000 //ini masih hard code

        $scope.getList = function () {
            var tahapan=10070000 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanPraKualifikasiListVendor/'+tahapan+'/'+$rootScope.userLogin.user.id, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }

        $scope.getList();

        $scope.view = function (data) {
            $rootScope.detilPengadaan = data;
            $location.path('/appvendor/promise/procurement/prakualifikasi/datakualifikasi/detil');
        };
    }

    DataKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
