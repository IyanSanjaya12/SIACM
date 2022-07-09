/**=========================================================
 * Module: PembukaanPenawaranTahapDuaIndexController.js
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembukaanPenawaranTahapDuaIndexController', PembukaanPenawaranTahapDuaIndexController);

    function PembukaanPenawaranTahapSatuIndexController($http, $scope, $rootScope, $state, $stateParams) {
        var tahapan = 10180300; // masih hard code
        if ($stateParams.tahapanId != undefined) {
            tahapan = $stateParams.tahapanId;
        }

        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + tahapan + '/' + $rootScope.userLogin.user.id)
            .success(function (data, status, headers, config) {
                $scope.pengadaanList = data;
            });

        $scope.btnEdit = function (ppe) {
            if (ppe.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                $state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-total-tahapdua', {
                    dataPengadaan: ppe
                });
            } else if (ppe.jenisPenawaran.id == 2) {
                $state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-satuan-tahapdua', {
                    dataPengadaan: ppe
                });
            } else {
                alert('tidak tersedia');
            }
        };
    }

    PembukaanPenawaranTahapDuaIndexController.$inject = ['$http', '$scope', '$rootScope', '$state', '$stateParams'];

})();