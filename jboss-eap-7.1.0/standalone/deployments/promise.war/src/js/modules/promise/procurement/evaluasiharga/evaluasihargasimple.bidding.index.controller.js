/**=========================================================
 * Module: EvaluasiHargaBiddingController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiHargaBiddingSimpleController', EvaluasiHargaBiddingSimpleController);

    function EvaluasiHargaBiddingSimpleController($http, $rootScope, $resource, $location, $window, toaster, $scope) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $rootScope.vendorList = [];
        $rootScope.listHarga = [];
        $rootScope.flagView = 0;
        $rootScope.totalHpsMaterial = 0;
        $rootScope.totalHpsJasa = 0;
        $rootScope.mataUangCode = "";
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Pengadaan ========================================= */
        var tahapan=13000001;//masih hardcode
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $scope.pengadaanList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Data Pengadaan ----------------------------------- */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        // fungsi tombol edit
        $scope.edit = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            view(ppvt.jenisPenawaran.id);
            //view(1); //hanya untuk uji coba
        };
        
        // fungsi tombol view
        $scope.view = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            $rootScope.flagView = 1;
            view(ppvt.jenisPenawaran.id);
        }
        
        // fungsi validasi pilihan dari jenis penawaran
        var view = function(jenisPenawaranId) {
            if (jenisPenawaranId == 1) { // 1 = total, 2 = satuan
                $location.path('/app/promise/procurement/evaluasihargasimple/total/bidding/view');
            } else if (jenisPenawaranId == 2) {
                $location.path('/app/promise/procurement/evaluasiharga/satuan/bidding/view');
            }
        }
        /* -------------------------------- END Rincian Controller --------------------------------------- */
        
    }

    EvaluasiHargaBiddingSimpleController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope'];

})();