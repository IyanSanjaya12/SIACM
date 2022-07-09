/**=========================================================
 * Module: PengambilanDokumenController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengambilanDokumenController', PengambilanDokumenController);

    function PengambilanDokumenController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=6 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
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
            $location.path('/app/promise/procurement/pengambilandokumen/view');
        };

    }

    PengambilanDokumenController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();