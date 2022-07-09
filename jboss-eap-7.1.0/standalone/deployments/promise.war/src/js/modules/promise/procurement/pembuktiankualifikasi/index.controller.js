/**=========================================================
 * Module: PembuktianKualifikasiController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembuktianKualifikasiController', PembuktianKualifikasiController);

    function PembuktianKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=10230000 //hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();
        
        $scope.edit = function(data) {
            $rootScope.detilPengadaan = data;
            view(data.pengadaan.id);
        };

        $scope.view = function (data) {
            $rootScope.detilPengadaan = data;
            $location.path('/app/promise/procurement/pembuktiankualifikasi/detil');
        };

    }

    PembuktianKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();