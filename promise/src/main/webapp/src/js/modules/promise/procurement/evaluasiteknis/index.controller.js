/**=========================================================
 * Module: EvaluasiTeknisController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiTeknisController', EvaluasiTeknisController);

    function EvaluasiTeknisController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            var tahapan=12;//masih hardcode
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    form.evaluasiTeknisList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.detilPengadaan = data;
            if (data.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                $location.path('/app/promise/procurement/evaluasiteknis/view/total', {
                    id: data.id
                });
            } else if (data.jenisPenawaran.id == 2) {
                $location.path('/app/promise/procurement/evaluasiteknis/view/satuan');

            } else {
                alert('tidak tersedia');
            }
        };

    }

    EvaluasiTeknisController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();