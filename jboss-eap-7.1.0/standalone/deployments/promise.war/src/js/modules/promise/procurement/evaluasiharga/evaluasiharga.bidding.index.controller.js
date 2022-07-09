/**=========================================================
 * Module: EvaluasiHargaBiddingController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiHargaBiddingController', EvaluasiHargaBiddingController);

    function EvaluasiHargaBiddingController($http, $rootScope, $resource, $location, $window, toaster, $scope) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $rootScope.vendorList = [];
        $rootScope.listHarga = [];
        $rootScope.flagView = 0;
        $rootScope.totalHpsMaterial = 0;
        $rootScope.totalHpsJasa = 0;
        $rootScope.mataUangCode = "";
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Pengadaan ========================================= */
        var tahapan=13;//masih hardcode
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $scope.pengadaanList = data;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + 13000001)
            //pemasukan harga simple auction
            .success(function (data, status, headers, config) {
                angular.forEach(data, function (value, index) {
                		$scope.pengadaanList.push(value);
                    });
            
                    //console.log('isinya data pengadaanList : '+JSON.stringify($scope.pengadaanList));
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
        });
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Data Pengadaan ----------------------------------- */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        // fungsi tombol edit
        $scope.edit = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            view(ppvt);
            //view(1); //hanya untuk uji coba
        };
        
        // fungsi tombol view
        $scope.view = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            $rootScope.flagView = 1;
            view(ppvt);
        }
        
        // fungsi validasi pilihan dari jenis penawaran
        var view = function(ppvt) {
            if (ppvt.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                //$location.path('/app/promise/procurement/evaluasiharga/total/bidding/view');
                if(ppvt.tahapanPengadaan.tahapan.id==13000001){
            		$location.path('/app/promise/procurement/evaluasihargasimple/total/bidding/view');
            	}
            	else if(ppvt.tahapanPengadaan.tahapan.id==13){
            		$location.path('/app/promise/procurement/evaluasiharga/total/bidding/view');
            	}
                
            } else if (ppvt.jenisPenawaran.id  == 2) {
                $location.path('/app/promise/procurement/evaluasiharga/satuan/bidding/view');
            }
        }
        /* -------------------------------- END Rincian Controller --------------------------------------- */
        
    }

    EvaluasiHargaBiddingController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope'];

})();