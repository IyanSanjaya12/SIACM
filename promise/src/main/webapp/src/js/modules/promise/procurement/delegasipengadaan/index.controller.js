/**=========================================================
 * Module: DelegasiPengadaanController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DelegasiPengadaanController', DelegasiPengadaanController);

    function DelegasiPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            //var tahapan=6 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanServices/getList', {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.perencanaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.perencanaan = data;
            $location.path('/app/promise/procurement/delegasipengadaan/detil');
        };

    }

    DelegasiPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();