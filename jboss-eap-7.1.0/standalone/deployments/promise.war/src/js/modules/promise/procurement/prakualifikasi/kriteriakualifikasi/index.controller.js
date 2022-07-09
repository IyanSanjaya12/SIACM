/**=========================================================
 * Module: KriteriaKualifikasiController.js
 * Author: F.H.K
 =========================================================*/


(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaKualifikasiController', KriteriaKualifikasiController);

    function KriteriaKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=10080000 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/'+tahapan, {
            //$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/', {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.pengadaan = data;
            if(data.jenisPengadaan.id == 1)
                $location.path('/app/promise/procurement/prakualifikasi/kriteriakualifikasi/view');
            else
                $location.path('/app/promise/procurement/prakualifikasi/kriteriakualifikasi/viewjasa');
        };

    }

    KriteriaKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();