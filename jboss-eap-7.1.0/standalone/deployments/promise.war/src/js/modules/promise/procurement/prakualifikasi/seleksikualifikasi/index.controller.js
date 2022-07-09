/**=========================================================
 * Module: SeleksiKualifikasiController.js
 * Author: F.H.K
 =========================================================*/


(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiKualifikasiController', SeleksiKualifikasiController);

    function SeleksiKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=10090000 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanPraKualifikasiList/'+tahapan+'/'+$rootScope.userLogin.user.id, {
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
            $location.path('/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiadministrasi');
        };

    }

    SeleksiKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();