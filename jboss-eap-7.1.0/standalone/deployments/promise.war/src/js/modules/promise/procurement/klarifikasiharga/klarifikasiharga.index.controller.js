/**=========================================================
 * Module: KlarifikasiHargaController.js
 * Author: Zie.Zat
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('KlarifikasiHargaController', KlarifikasiHargaController);

    function KlarifikasiHargaController($http, $rootScope, $resource, $location, $window, toaster, $scope, $state) {
        
        /* ================================= START Data Pengadaan ========================================= */
    	$scope.getList = function () {
            var tahapan= 10250000 //500 //masih hard code
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();
        /* --------------------------------- END Rincian Data Pengadaan ----------------------------------- */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        // fungsi tombol edit
        $scope.edit = function(data) {
            $rootScope.detilPengadaan = data;
            view(data.jenisPenawaran.id);
        };
        
        // fungsi tombol view
        $scope.view = function(data) {
            $rootScope.detilPengadaan = data;
            view(data.jenisPenawaran.id);
        }
        
        // fungsi validasi pilihan dari jenis penawaran
        var view = function(jenisPenawaranId) {
            if (jenisPenawaranId == 1) { // 1 = total, 2 = satuan
            	$state.go('app.promise.procurement-panitia-klarifikasiharga-total');
            } else if (jenisPenawaranId == 2) {
            	$state.go('app.promise.procurement-panitia-klarifikasiharga-satuan');
            }
        }
        /* -------------------------------- END Rincian Controller --------------------------------------- */
        
    }
  
    KlarifikasiHargaController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$state'];

})();