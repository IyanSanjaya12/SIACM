/**=========================================================
 * Module: EvaluasiHargaController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiHargaSimpleController', EvaluasiHargaSimpleController);

    function EvaluasiHargaSimpleController($http, $rootScope, $resource, $location, $window, toaster, $scope) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $rootScope.vendorList = [];
        $rootScope.listHarga = [];
        $rootScope.flagView = 0;
        $rootScope.mataUangCode = "";
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Pengadaan ========================================= */
        var tahapan=14000001;//masih hardcode
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
            view(ppvt);
            //view(1); // hanya untuk uji coba
        };
        
        // fungsi tombol view
        $scope.view = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            $rootScope.flagView = 1;
            view(ppvt);
            
            
			//view(1); // hanya untuk uji coba
        }
        
        // fungsi validasi pilihan dari jenis penawaran
        var view = function(ppvt) {
            if (ppvt.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
            	if(ppvt.tahapanPengadaan.tahapan.id==14000001){
            		$location.path('/app/promise/procurement/evaluasihargasimple/total/auction/view');
            	}
            	else if(ppvt.tahapanPengadaan.tahapan.id==14){
            		$location.path('/app/promise/procurement/evaluasiharga/total/auction/view');
            	}
            } else if (jenisPenawaranId == 2) {
                //belumada//$location.path('/app/promise/procurement/evaluasihargasimple/satuan/auction/view');
            }
        }
        /* -------------------------------- END Rincian Controller --------------------------------------- */
        
    }

    EvaluasiHargaSimpleController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope'];

})();