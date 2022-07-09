/**=========================================================
 * Module: AlokasiAnggaranController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlokasiAnggaranController', AlokasiAnggaranController);

    function AlokasiAnggaranController($scope, $http, $rootScope, $resource, $location) {
        var form = this;
        $scope.loading = true;
        $rootScope.gabungAnggaran=undefined;

        $scope.getList = function () {
            $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getListByIsUsed', {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    form.list = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.detilAlokasiAnggaran = data;
            $location.path('/app/promise/procurement/alokasianggaran/view');
        };

        $scope.addAlokasiAnggaran = function () {
            $rootScope.detilAlokasiAnggaran = undefined;
            $location.path('/app/promise/procurement/alokasianggaran/view');
        }
        
        $scope.gabungAlokasiAnggaran = function () {
            $rootScope.detilAlokasiAnggaran = undefined;
            $location.path('/app/promise/procurement/alokasianggaran/gabung');
        }

    }

    AlokasiAnggaranController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
