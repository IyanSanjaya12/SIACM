/**=========================================================
 * Module: EvaluasiHargaSatuanAuctionViewController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaSatuanAuctionViewController', EvaluasiHargaSatuanAuctionViewController);

    function EvaluasiHargaSatuanAuctionViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
    
    
		/* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
		$rootScope.itemBarangJasaList = [];
		/* ----------------------------------------------------------------------------------------------- */
		
		
        /* ======================== START Data Pengadaan ================================================= */
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $rootScope.pengadaanId = $scope.detilPengadaan.id;
        $rootScope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        if (typeof $rootScope.view !== 'undefined' && $rootScope.view == true) {
            $scope.tahapanId = 14;
        } else {
            $scope.tahapanId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;
        }
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
            /*console.log("Hasil TANGGAL AKHIR JADWAL PENGADAAN = " + new Date($rootScope.tglAkhirJadwalP));
            console.log("Hasil WAKTU AKHIR JADWAL PENGADAAN = " + new Date($rootScope.waktuAkhirJadwalP));*/
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* ---------------------------- END Rincian Jadwal Pengadaan  ----------------------------------- */
		
		
		/* ============================ START Sesi Auction View Detail ================================== */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/getByPengadaan/' + $scope.pengadaanId, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $rootScope.sesiAuctionDetailList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });     
        /* ---------------------------- END Rincian Sesi Auction View ----------------------------------- */
        
        
        /* ============================ START CONTROLLER DETAIL ========================================= */
        // flag untuk view saja
        if($rootScope.flagView == 1)
            $scope.viewAja = 1;
            
		// fungsi untuk pengurutan nilai
        var sortNilai = function(nilai, type) {
            var angka = nilai;
            for (var x = 0; x <= angka.length; x++) {
                for (var y = x+1; y<= angka.length+1; y++) {
                    if(type == 'dsc'){
                        if(angka[x] < angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }
                    } else {
                        if(angka[x] > angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }    
                    }
                }
            }
            return angka[0];
        }
		
        // fungsi ke halaman detail
        $scope.clickDetail = function(data) {
            $rootScope.dataLemparan = data;
			angular.forEach($rootScope.itemMaterialList,function(value,index){
				$rootScope.itemBarangJasaList.push($rootScope.itemMaterialList[index]);
			});
			angular.forEach($rootScope.itemJasaList,function(value,index){
				$rootScope.itemBarangJasaList.push($rootScope.itemJasaList[index]);
			});
            //console.log("DATA ITEM LEMPARAN = "+JSON.stringify($rootScope.dataLemparan));
            $scope.go('/app/promise/procurement/evaluasiharga/satuan/auction/addview');
        }
        
        // tombol kembali ke index.html
        $scope.back = function() {
        if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $scope.go('/app/promise/procurement/evaluasiharga/auction');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/auction');
            }
        }
        
        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").hide();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            var printContents = document.getElementById(divName).innerHTML;
            $(".ng-table-pager").show();
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
        
        // lanjut tahapan
        $scope.validasiAuction = function(){
            if($scope.sesiAuctionDetailList.length > 0) {
				var dataAuction = $scope.sesiAuctionDetailList;
                var listSesiStatus = [];
                angular.forEach(dataAuction, function(value, index) {
                    listSesiStatus.push(dataAuction[index].status);
                });
				
				var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                //console.log("sortSesiStatus = " + sortSesiStatus);
				
				if(sortSesiStatus == 1) {
                    lanjutTahapan();
                    $scope.back();
                } else {
                    var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, anda tidak dapat melanjutkan ke tahapan berikutnya!');
                    if(jawaban){
                        $scope.back();
                    }
                }
			} else {
				toaster.pop('error', 'Auction Kosong', 'Belum pernah terdapat sesi Auction, Silahkan buat terlebih dahulu');
			}
        }
		
		//change tahapan
        /* get Next Tahapan */
		$scope.getNextTahapan = function () {
				$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
					.success(function (data, status, headers, config) {
						$scope.nextTahapan = data;
					})
					.error(function (data, status, headers, config) {});
			}
			/* END get Next Tahapan */
		$scope.getNextTahapan();

		//update pengadaan
		var lanjutTahapan = function () {
			$scope.loading = true;
			$http({
				method: 'POST',
				url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				transformRequest: function (obj) {
					var str = [];
					for (var p in obj)
						str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					return str.join("&");
				},
				data: {
					pengadaanId: $scope.pengadaanId,
					tahapanPengadaanId: $scope.nextTahapan
				}
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				//$location.path('app/promise/procurement/evaluasiteknis');
                $location.path('/app/promise/procurement/evaluasiharga/auction');
			});
		}
        /* --------------------------- END Rincian Controller -------------------------------------------- */
    
    
    }
    
    EvaluasiHargaSatuanAuctionViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();