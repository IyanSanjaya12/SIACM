/**=========================================================
 * Module: PemasukanDokumenController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanDokumenKualifikasiController', PemasukanDokumenKualifikasiController);

    function PemasukanDokumenKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=10070000 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanPraKualifikasiList/'+tahapan+'/'+$rootScope.userLogin.user.id, {
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
            $location.path('/app/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi/view');
        };

    }

    PemasukanDokumenKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();