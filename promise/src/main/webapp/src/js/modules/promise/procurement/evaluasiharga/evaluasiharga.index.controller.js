/**=========================================================
 * Module: EvaluasiHargaController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiHargaController', EvaluasiHargaController);

    function EvaluasiHargaController($http, $rootScope, $resource, $location, $window, toaster, $scope) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $rootScope.vendorList = [];
        $rootScope.listHarga = [];
        $rootScope.flagView = 0;
        $rootScope.mataUangCode = "";
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Pengadaan ========================================= */
        var tahapan=14;//masih hardcode
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $scope.pengadaanList = data;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + 14000001)
	            //pemasukan harga simple auction
	            .success(function (data, status, headers, config) {
	                angular.forEach(data, function (value, index) {
	                		$scope.pengadaanList.push(value);
	                    });
	            
	                    //console.log('isinya data pengadaanList : '+JSON.stringify($scope.pengadaanList));
	                $scope.loading = false;
	            }).error(function (data, status, headers, config) {
	            $scope.loading = false;
            });
        }).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
        /* --------------------------------- END Rincian Data Pengadaan ----------------------------------- */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        // fungsi tombol edit untuk id = 14
        $scope.edit = function(ppvt) {
            $rootScope.detilPengadaan = ppvt;
            view(ppvt);
            //view(1); // hanya untuk uji coba
            
        };
        
        // fungsi tombol view untuk id = 14000001
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
            	if(ppvt.tahapanPengadaan.tahapan.id==14000001){
            		//belumada//$location.path('/app/promise/procurement/evaluasihargasimple/satuan/auction/view');
            	}
            	else if(ppvt.tahapanPengadaan.tahapan.id==14){
            		$location.path('/app/promise/procurement/evaluasiharga/satuan/auction/view');
            	}
                
            }
        }
        /* -------------------------------- END Rincian Controller --------------------------------------- */
        
    }

    EvaluasiHargaController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope'];

})();