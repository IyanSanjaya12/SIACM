/**=========================================================
 * Module: PenawaranHargaTerbukaController.js
 * Author: F.H.K
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenawaranHargaTerbukaController', PenawaranHargaTerbukaController);

    function PenawaranHargaTerbukaController($http, $rootScope, $resource, $location, $window, toaster, $scope) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        var userId = $rootScope.userLogin.user.id;
        $scope.pengadaanList = [];
        /* ------------------------------------------------------------------------------------------------ */


        /* ================================= START Data Vendor ============================================ */
        $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + userId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $rootScope.vendorIdLogin = data.id;
        }).error(function (data, status, headers, config) {
            //$scope.loading = false;
        });
        /* --------------------------------- END Rincian Data Vendor -------------------------------------- */


        /* ================================= START Data Pengadaan ========================================= */
        $scope.loading = true;
		var tahapan = 14; //masi hardcode evaluasi harga default
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {

            angular.forEach(data, function (value, index) {
                $scope.pengadaanList.push(data[index].pengadaan);
            });
            tahapan = 14000001;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id + '/' + tahapan)
                .success(function (data, status, headers, config) {
                    angular.forEach(data, function (value, index) {
                        $scope.pengadaanList.push(data[index].pengadaan);
                    });
                    // tampilan tombol
                    var dataPengadaan = $scope.pengadaanList;
                    angular.forEach(dataPengadaan, function (value, index) {
                        //dataPengadaan[index].metodePenawaranHarga.id = 3; // hanya untuk uji coba
                        if (dataPengadaan[index].metodePenawaranHarga.id == 1) {
                            $scope.tampil = 1;
                        } else {
                            $scope.tampil = 0;
                        }
                    });

                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });

        }).error(function (data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Data Pengadaan ----------------------------------- */


        /* ================================= START CONTROLLER DETAIL ====================================== */
        // fungsi tombol edit
        $scope.edit = function (ppvt) {
            $rootScope.detilPengadaan = ppvt;
            view(ppvt.jenisPenawaran.id, ppvt.metodePenawaranHarga.id);
            //view(2, 3);
        };

        // fungsi tombol view
        $scope.view = function (ppvt) {
            $rootScope.detilPengadaan = ppvt;
            $rootScope.flagView = 1;
            view(ppvt.jenisPenawaran.id, ppvt.metodePenawaranHarga.id);
            //view(2, 3);
        }

        // fungsi validasi pilihan dari jenis penawaran
        var view = function (jenisPenawaranId, metodePenawaranHargaId) {
                if (metodePenawaranHargaId == 2) { // 2 = auction Terbuka 
                    if (jenisPenawaranId == 1) { // 1 = total
                        $location.path('/appvendor/promise/procurement/evaluasiharga/vendor/total/auction/terbuka');
                    } else if (jenisPenawaranId == 2) { // 2 = satuan
                        $location.path('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/auction/terbuka');
                    };
                } else if (metodePenawaranHargaId == 3) { // 3 = auction Tertutup
                    if (jenisPenawaranId == 1) { // 1 = total
                        $location.path('/appvendor/promise/procurement/evaluasiharga/vendor/total/auction/tertutup');
                    } else if (jenisPenawaranId == 2) { // 2 = satuan
                        $location.path('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/auction/tertutup');
                    };
                }
            }
            /* -------------------------------- END Rincian Controller --------------------------------------- */

    }

    PenawaranHargaTerbukaController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope'];

})();