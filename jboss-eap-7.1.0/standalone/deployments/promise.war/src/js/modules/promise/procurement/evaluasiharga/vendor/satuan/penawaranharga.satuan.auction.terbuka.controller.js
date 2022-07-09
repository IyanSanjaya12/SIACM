/**=========================================================
 * Module: PenawaranHargaSatuanTerbukaAddController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PenawaranHargaSatuanTerbukaAddController', PenawaranHargaSatuanTerbukaAddController);

    function PenawaranHargaSatuanTerbukaAddController($http, $rootScope, $resource, $location, $window, toaster, $scope, $filter, ngTableParams) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $rootScope.pengadaanId = $scope.detilPengadaan.id;
		$scope.tahapanId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;
        $scope.vendorId = $rootScope.vendorIdLogin;
        /* ----------------------------------------------------------------------------------------------- */
		
		
		/* ======================== START Data Pengadaan ================================================= */
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $rootScope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        /* ------------------------ END Rincian Data Pengadaan ------------------------------------------ */
        
       
        /* ======================== START Bidang Usaha Pengadaan ======================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            $scope.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
        /* ------------------------ END Bidang Usaha Pengadaan ------------------------------------------ */
         
        
        /* ======================== START Rincian Kebutuhan Material ==================================== */
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	$rootScope.itemMaterialList = data;
				
				
                // Paging di Bootstrap
                $scope.itemPengadaanMaterialByPengadaanIdList = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    total: data.length,
                    getData: function($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
        /* --------------------------- END Rincian Kebutuhan Material ----------------------------------- */
        

        /* =========================== START Rincian Kebutuhan Jasa ===================================== */
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	$rootScope.itemJasaList = data;
				
				
                // Paging di Bootstrap
                $scope.itemPengadaanJasaByPengadaanIdList = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    total: data.length,
                    getData: function($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                      }
                });
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
        /* ---------------------------- END Rincian Kebutuhan Jasa -------------------------------------- */
		
		
		/* ============================ START Jadwal Pengadaan ========================================== */
        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanAndTahapan/' + $scope.pengadaanId + '/' + $scope.tahapanId, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $rootScope.tglAkhirJadwalP = data[0].tglAkhir;
            $rootScope.waktuAkhirJadwalP = data[0].waktuAkhir;
			var today = new Date();
			var jadwalP = new Date(data[0].waktuAkhir);
			if(today > jadwalP){
				var waktuAkhirPengadaan = $filter('date')(jadwalP, 'dd-MMM-yyyy HH:mm');
				var peringatan = confirm("JADWAL PENGADAAN ("+waktuAkhirPengadaan+") MELEWATI BATAS HARI INI");
				if(peringatan)
					$scope.back();
				else
					$scope.back();
			}
            //console.log("Hasil TANGGAL AKHIR JADWAL PENGADAAN = " + new Date($rootScope.tglAkhirJadwalP));
            //console.log("Hasil WAKTU AKHIR JADWAL PENGADAAN = " + new Date($rootScope.waktuAkhirJadwalP));
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* ---------------------------- END Rincian Jadwal Pengadaan  ----------------------------------- */
		
		
		/* ============================ START CONTROLLER DETAIL ========================================= */
		// tombol kembali ke index.html
        $scope.back = function() {
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
		
		// fungsi ke halaman detail
        $scope.clickDetail = function(data) {
            $rootScope.dataLemparan = data;
            //console.log("DATA ITEM LEMPARAN = "+JSON.stringify($rootScope.dataLemparan));
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/auction/detail/terbuka');
        }
		/* ---------------------------- END Rincian Controller ------------------------------------------ */
		
	}
	
	
	PenawaranHargaSatuanTerbukaAddController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$filter', 'ngTableParams'];

})();