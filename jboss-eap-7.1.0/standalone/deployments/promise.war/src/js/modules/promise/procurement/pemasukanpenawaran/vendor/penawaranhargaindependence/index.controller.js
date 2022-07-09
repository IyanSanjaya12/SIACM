/**=========================================================
 * Module: PenawaranHargaIndependenceVendorController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenawaranHargaIndependenceVendorController', PenawaranHargaIndependenceVendorController);

    function PenawaranHargaIndependenceVendorController($scope, $http, $rootScope, $resource, $location, $state) {
        var vm = this;

        vm.getPengadaanList = function () {
            $scope.loading = true;
            var tahapan=13000001;//masih hardcode
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id+'/'+tahapan)
            /*$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorList' )*/
            .success(
                function (data, status, headers, config) {
                    vm.pemasukanPenawaranList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };
        vm.getPengadaanList();

        vm.btnDetil = function (pp) {
            $rootScope.pengadaanVendor = pp;
            if (pp.pengadaan.jenisPenawaran.id == 1) {
                $state.go('appvendor.promise.procurement-vendor-penawaranvendorindependencehargatotal');
            } else if (pp.pengadaan.jenisPenawaran.id == 2) {
                $state.go('appvendor.promise.procurement-vendor-penawaranvendorindependencehargasatuan');
            } else {
                alert('Halaman tidak tersedia');
            }
        };


    }

    PenawaranHargaIndependenceVendorController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();