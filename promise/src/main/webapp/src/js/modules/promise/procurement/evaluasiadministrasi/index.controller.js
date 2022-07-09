/**=========================================================
 * Module: EvaluasiAdministrasiController.js
 * Author: H.R
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiAdministrasiController', EvaluasiAdministrasiController);

    function EvaluasiAdministrasiController($http, $scope, $rootScope, $resource, $location, $state) {
        var eac = this;
        $scope.loading = true;
        eac.getEvaluasiAdministrasiList = function() {
            var tahapan=11;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(
                function(data, status, headers, config) {
                    eac.evaluasiAdministrasiList = data;
                    $scope.loading = false;
                }).error(function(data, status, headers, config) {});
        };

        eac.view = function(ea) {
            if (ea.jenisPenawaran.id === 1) {
                $rootScope.detilPengadaan = ea;
            	$location.path('/app/promise/procurement/evaluasiAdministrasi/edit');
            } else {
            	$state.go('app.promise.procurement-panitia-evaluasiAdministrasi-edit-satuan', {dataPengadaan: ea});
            }
        };
        eac.getEvaluasiAdministrasiList();

    }

    EvaluasiAdministrasiController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$state'];

})();