/**=========================================================
 * Module: PenawaranHargaSatuanDetailTertutupAddController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PenawaranHargaSatuanDetailTertutupAddController', PenawaranHargaSatuanDetailTertutupAddController);

    function PenawaranHargaSatuanDetailTertutupAddController($http, $rootScope, $resource, $location, $window, toaster, $scope, $filter, ngTableParams) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
		var dataLemparan = $rootScope.dataLemparan;
        var itemId = dataLemparan.item.id;
		var itemPengadaanId = dataLemparan.id
		var nilaiTerbaik1 = 0;
        var nilaiTerbaik2 = 0;
        var nilaiTerbaik3 = 0;
		
		$rootScope.itemPId = itemPengadaanId;
		$scope.vendorId = $rootScope.vendorIdLogin;
		$scope.nilaiTerbaikList = [];
		/* ----------------------------------------------------------------------------------------------- */
		
		
		/* ================================= START Data Kriteria Ambang ================================== */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $rootScope.kriteriaAmbangBatasList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* --------------------------------- END Rincian Data Kriteria Ambang ---------------------------- */
		
		
		/* ================================= START Sesi Auction View Detail ============================== */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/getByPengadaanItemPengadaan/' + $scope.pengadaanId + '/' + itemPengadaanId, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
			var statusData = 0;
            angular.forEach(data,function(value,index){
                if(data[index].status != 1) {
                    $scope.sesiAuction = data[index];
                    $scope.noSesiAuction = data[index].noSesi;
                    $scope.tanggalAwalSesi = $filter('date')(data[index].waktuAwal, 'dd-MMM-yyyy');
                    $scope.waktuAwalSesi = $filter('date')(data[index].waktuAwal, 'HH:mm');
                    $scope.tanggalAkhirSesi = $filter('date')(data[index].waktuAkhir, 'dd-MMM-yyyy');
                    $scope.waktuAkhirSesi = $filter('date')(data[index].waktuAkhir, 'HH:mm');
                    $scope.hargaAwal = data[index].hargaAwal;
                    $scope.selisihPenawaran = data[index].selisihPenawaran;
                    $scope.mataUang = data[index].pengadaan.mataUang.kode;
                    $rootScope.tglAkhirSesiAuction = data[index].waktuAkhir;
                    $rootScope.sesiAuctionDetail = data[index];
                } else {
                    statusData = statusData + 1;
                }
            });
			
            if(statusData == data.length) {
                toaster.pop('warning', 'Auction', 'Tidak ada Sesi Auction yang berlangsung');
				$scope.hideTombol = 1;
            } else {
                historyDataPenawaran($scope.sesiAuction.id);
            }
            //console.log("Hasil Auction = " + JSON.stringify($rootScope.sesiAuctionDetail));
			
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });     
        /* --------------------------------- END Rincian Sesi Auction View ------------------------------- */
		
		
		/* ================================= START Data Penawaran Harga ================================== */
        var historyDataPenawaran = function(auctionId) {
            var nilaiPenawaranList = [];
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByAuction/' + auctionId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                //console.log("Hasil Penawaran Auction = " + JSON.stringify(data));
                angular.forEach(data,function(value,index) {
                    nilaiPenawaranList.push(data[index].nilaiPenawaranOri);
                });
                $scope.loading = false;
                nilaiTerbaik1 = sortNilai(nilaiPenawaranList, 'asc', 1);
                nilaiTerbaik2 = sortNilai(nilaiPenawaranList, 'asc', 2);
                nilaiTerbaik3 = sortNilai(nilaiPenawaranList, 'asc', 3);
                vendorInput(auctionId, nilaiTerbaik1, nilaiTerbaik2, nilaiTerbaik3); 
                
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
            });
        }
        /* --------------------------------- END Data Penawaran Harga ------------------------------------ */
		
		
		/* ================================= START Data Penawaran Harga Per Vendor ======================= */
		var vendorInput = function(auctionId, nilaiTerbaik1, nilaiTerbaik2, nilaiTerbaik3) {
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PesertaAuctionServices/getByAuctionVendor/' + auctionId + '/' + $scope.vendorId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                //console.log("Hasil Penawaran Auction Per Vendor = " + JSON.stringify(data));
                if(data.length > 0){
                    cariVendorInput(data[0].id, nilaiTerbaik1, nilaiTerbaik2, nilaiTerbaik3);
                } else {
                    var peringatan = confirm("Maaf, Anda tidak dapat mengikuti Sesi ini karena nilai Harga Anda di Penawaran Pertama Tidak Memenuhi Batas Ambang Kami");
                    if(peringatan)
                        $scope.viewAja = 1;
                    else
                        $scope.viewAja = 1;
                }
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
            });
        }
		
		var cariVendorInput = function(pesertaAuctionId, nilaiTerbaik1, nilaiTerbaik2, nilaiTerbaik3) {
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByPesertaAuction/' + pesertaAuctionId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                //console.log("Hasil Penawaran Auction Per Vendor = " + JSON.stringify(data));
                $scope.penawaran = data[0].nilaiPenawaranOri;
                $scope.waktuPenawaran = data[0].created;
                $scope.mataUang = data[0].suratPenawaran.mataUang.kode;
                if(nilaiTerbaik1 == $scope.penawaran) {
                    $scope.rankPenawaran = 1;
                } else if(nilaiTerbaik2 == $scope.penawaran) {
                    $scope.rankPenawaran = 2;
                } else if(nilaiTerbaik3 == $scope.penawaran) {
                    $scope.rankPenawaran = 3;
                } else {
                    $scope.rankPenawaran = 'Tidak termasuk 3 Besar';
                }
				
				angular.forEach(data,function(value,index){ 
                    $scope.nilaiTerbaikList.push(data[index].nilaiPenawaranOri);
                });
                $rootScope.nilaiTerbaik = sortNilai($scope.nilaiTerbaikList, 'asc', 1);
				
                $rootScope.penawaranVendorList = data; 
				
				
				// paging manual dari Bootstrap
                $scope.penawaranHargaVendorList = new ngTableParams({
                    page: 1,
                    count: 3
                }, {
                    total: data.length,
                    getData: function($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
            });
        }
		/* --------------------------------- END Data Penawaran Harga Per Vendor ------------------------- */
		
		
		/* ================================= START CONTROLLER DETAIL ===================================== */
        // fungsi untuk pengurutan nilai
        var sortNilai = function(nilai, type, rank) {
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
            return angka[rank-1];
        }
        
        // tombol kembali ke index.html
        $scope.back = function() {
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/auction/tertutup');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
		
		// mulai Auction Terbuka
        $scope.mulaiAuction = function() {
            var tanggalAkhirSesi = new Date($rootScope.tglAkhirSesiAuction);
            var tanggalInput = new Date();
            if($scope.hideTombol != 1) {
                if(tanggalInput > tanggalAkhirSesi) {
                    var peringatan = confirm("Waktu sesi sudah habis, silahkan menunggu sesi berikutnya");
                    if(peringatan) {
                        updateDataAuction();
                    } else {
                        updateDataAuction();
                    }
                } else {
                    $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/pemasukanpenawaran/tertutup');
                }
            } else {
                alert("Tidak ada Sesi Auction yang berlangsung");
            }
        }
		
		// fungsi update data Auction
        var updateDataAuction = function() {
            var data = $rootScope.sesiAuctionDetail;
            if(data.status == 0){
                //console.log("HASIL DARI SUMBER = " + JSON.stringify(data));
                var dataSesiAuction = {
                    id: data.id,
                    noSesi: data.noSesi,
                    waktuAwal: new Date(data.waktuAwal),
                    waktuAkhir: new Date(data.waktuAkhir),
                    keterangan: data.keterangan,
                    hargaAwal: data.hargaAwal,
                    selisihPenawaran: data.selisihPenawaran,
                    pengadaan: data.pengadaan.id,
                    itemPengadaan: data.itemPengadaan.id,
                    status: 1
                }
                //console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify(dataSesiAuction));
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function(obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataSesiAuction
                }).success(function(data, status, headers, config) {
                    console.log("UPDATE AUCTION SESSION OK : " + JSON.stringify(data));
                }).error(function(data, status, headers, config) {
                    $scope.loading = true;
                    alert("DATA ERORR UNTUK MENYIMPAN!!");
                });
            }
			$scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
        }
		/* --------------------------------- END Rincian Controller -------------------------------------- */
	}
	
	PenawaranHargaSatuanDetailTertutupAddController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$filter', 'ngTableParams'];

})();