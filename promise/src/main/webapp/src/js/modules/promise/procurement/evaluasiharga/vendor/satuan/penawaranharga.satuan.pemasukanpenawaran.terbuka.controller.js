/**=========================================================
 * Module: PemasukanPenawaranSatuanTerbukaAddController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanPenawaranSatuanTerbukaAddController', PemasukanPenawaranSatuanTerbukaAddController);

    function PemasukanPenawaranSatuanTerbukaAddController($http, $rootScope, $resource, $location, $window, toaster, $scope, $filter, ngTableParams) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
		var dataLemparan = $rootScope.dataLemparan;
        var itemId = dataLemparan.item.id;
		var itemPengadaanId = dataLemparan.id;
		var nilaiTerbaik = $rootScope.nilaiTerbaik;
        var hitung = 0;
		
		$rootScope.itemPId = itemPengadaanId;
		$rootScope.ambilDataMaterialJasa = [];
		$scope.vendorId = $rootScope.vendorIdLogin;
		
		$scope.penawaranAwal = $rootScope.sesiAuctionDetail.hargaAwal;
        $scope.selisihPenawaran = $rootScope.sesiAuctionDetail.selisihPenawaran;
        $scope.mataUang = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
        
        // HAPUS JIKA TIDAK MENGGUNAKAN KURS ASING -------------------------
        $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran;
		
        if(nilaiTerbaik != undefined) {
			$scope.penawaranAwalDenganKurs = nilaiTerbaik;
			$scope.hargaPerbandingan = nilaiTerbaik - $scope.selisihPenawaran;
		} else {
			$scope.penawaranAwalDenganKurs = $scope.penawaranAwal;
			$scope.hargaPerbandingan = $scope.penawaranAwal - $scope.selisihPenawaran;
		}
		
        $rootScope.nilaiValue = 'NaN';
		/* ----------------------------------------------------------------------------------------------- */
		
		
		/* ============================ START Data Peserta Auction ======================================= */
		 $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PesertaAuctionServices/getByAuctionVendor/' + $rootScope.sesiAuctionDetail.id + '/' + $scope.vendorId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
			 $scope.dataPesertaAuctionId = data[0].id;
			 //console.log("PESERTA AUCTION ID = "+ $scope.dataPesertaAuctionId);
		}).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
		/* ---------------------------- END Data Peserta Auction ----------------------------------------- */
		
		
		/* ============================ START Data Surat Penawaran ======================================= */
		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendorId + '/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
			 $scope.dataSuratPenawaranId = data[0].id;
			 //console.log("SURAT PENAWARAN ID = "+ $scope.dataSuratPenawaranId);
		}).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
		/* ---------------------------- END Data Surat Penawaran ----------------------------------------- */
		
		
		/* ============================ START Rincian Kurs Pengadaan ===================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list/', {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            $scope.kursList = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading
        });
        
        $scope.pilih = function() {
            ambilKursSource('IDR', $scope.kursPA);
        }
        
        // Ambil Nilai Kurs setiap mata uang dari Source (ONLINE, bukan DataBase) terkini dengan BASE USD
        var ambilKursSource = function(mu, cur) {
            $scope.loading = true;
            $http.jsonp('http://api.fixer.io/latest?base=' + cur + '&callback=JSON_CALLBACK', {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                for(var key in data.rates) {
                    var value = data.rates[key];
                    if(mu == key){
                        $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran / value;
                        $scope.penawaranAwalDenganKurs = $scope.penawaranAwal / value;
                        $scope.totalPenawaranDenganKurs = $scope.totalPenawaran / value;
                        $scope.mataUang = $scope.kursPA;
                        $rootScope.nilaiValue = value;
                    } else if(mu == cur) {
                        $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran;
                        $scope.penawaranAwalDenganKurs = $scope.penawaranAwal;
                        $scope.totalPenawaranDenganKurs = $scope.totalPenawaran;
                        $scope.mataUang = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
                        $rootScope.nilaiValue = 'NaN';
                    }
                        
                }
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading
            });
            
        }
        /* ---------------------------- END Rincian Kurs Pengadaan --------------------------------------- */
		
		
		/* ============================ START Rincian Kebutuhan Material/Jasa ============================ */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaan/' + itemPengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            
			data.item.totHPS = 0;
            data.item.itemNilaiOri = 0;
            data.item.itemNilaiAfter = 0;
            data.item.totHPSAfter = 0;
          
            $rootScope.ambilDataMaterialJasa.push(data);
			//console.log("DATA ITEM PENGADAAN = "+JSON.stringify(data));
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Rincian Kebutuhan Materrial/Jasa ----------------------------- */
		
		
		/* ============================ START Rincian Conditional Price ================================== */
		$scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByPengadaanAndItemPengadaan/' + $scope.pengadaanId + '/' + itemPengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            angular.forEach(data,function(value,index){
                if(data[index].conditionalPrice.isPersentage == 1)
                    data[index].logo = '%';
                else
                    data[index].logo = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
            });
            $rootScope.hasilItem = data;
            //console.log("ITEM CONDITIONAL PRICE " + JSON.stringify($rootScope.hasilItem));
            
            // paging untuk Item CP 
            $scope.ItemConList = new ngTableParams({
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
            $scope.loading
        });
		/* ---------------------------- END Rincian Conditional Price ------------------------------------ */
       
		
		
		/* ============================ START CONTROLLER DETAIL ========================================== */
		// fungsi Untuk Format Number
        Number.prototype.formatMoney = function(c, d, t){
            var n = this, 
                c = isNaN(c = Math.abs(c)) ? 2 : c, 
                d = d == undefined ? "." : d, 
                t = t == undefined ? "," : t, 
                s = n < 0 ? "-" : "", 
                i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
                j = (j = i.length) > 3 ? j % 3 : 0;
            return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
        };
		
		// tombol kembali ke index.html
        $scope.back = function() {
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/satuan/auction/terbuka');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
		
		// set Nilai Total HPS di kanan tabel
        $scope.setTotalHPS = function(data, nilaiOri) {
            var dataItem = $rootScope.hasilItem;
			//console.log("DATA JASA YANG AKAN DISIMPAN = "+JSON.stringify($rootScope.hasilItem));
			
			var hasilPenjumlahanItem = 0;
            var nilaiAfter = 0;
            
            angular.forEach(dataItem,function(value,index){
                if(dataItem[index].conditionalPrice.isPersentage == 1) {
                    if(dataItem[index].itemPengadaan.id == data.id) {
                        hasilPenjumlahanItem = hasilPenjumlahanItem + ((nilaiOri * dataItem[index].nilai/100) * dataItem[index].conditionalPrice.fungsi);
                    }
                } else {
                    if(dataItem[index].itemPengadaan.id == data.id) {
                        hasilPenjumlahanItem = hasilPenjumlahanItem + (dataItem[index].nilai * dataItem[index].conditionalPrice.fungsi);
                    }
                }
            });
            
            nilaiAfter = nilaiOri + hasilPenjumlahanItem;
            //data.item.totHPS = nilaiOri * data.kuantitas;
			data.item.totHPS = nilaiAfter * data.kuantitas;
            var totHPSAfter = nilaiAfter * data.kuantitas;
            
            // simpan ke dalam data penawaran
            data.item.itemNilaiOri = nilaiOri;
            data.item.itemNilaiAfter = nilaiAfter;
            data.item.totHPSAfter = totHPSAfter;
			
			hitungPenawaran($rootScope.ambilDataMaterialJasa);
		}
		
		var hitungPenawaran = function(dataMaterialJasa) {
            $scope.totalPenawaran = 0;
			
			angular.forEach(dataMaterialJasa,function(value,index){
                $scope.totalPenawaran = $scope.totalPenawaran + dataMaterialJasa[index].item.totHPS;
            });
			
			//HAPUS JIKA TIDAK MENGGUNAKAN METODE KURS ASING -------------------------------------
            //console.log("KURS ASING = "+$rootScope.nilaiValue);
            $scope.totalPenawaranDenganKurs = $scope.totalPenawaran;
            if($rootScope.nilaiValue != 'NaN')
                $scope.totalPenawaranDenganKurs = $scope.totalPenawaran / $rootScope.nilaiValue; 
            //-------------------------------------------------------------------------------------
            
            if($scope.totalPenawaran >= $scope.hargaPerbandingan) {
                alert("Maaf, Harga Penawaran tidak boleh lebih dari IDR." + ($scope.hargaPerbandingan).formatMoney(0, '.', ','));
                document.getElementsByName("kursPA")[0].focus();
            } 
		}
		
		// simpan Penawaran Vendor total
        $scope.simpanPenawaran = function() {
			var dataMaterialJasa = $rootScope.ambilDataMaterialJasa[0];
			var totalPenawaranAfter = dataMaterialJasa.item.totHPSAfter;
			
			if($scope.totalPenawaran != 'undefined') {
				if($scope.totalPenawaran >= $scope.hargaPerbandingan) {
                    alert("Maaf, Harga Penawaran tidak boleh lebih dari IDR." + ($scope.hargaPerbandingan).formatMoney(0, '.', ','));
                    document.getElementsByName("hps")[0].focus();
                } else {
					if($rootScope.kriteriaAmbangBatasList.length > 0){
                        var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
						var totalHpsBarangJasa = dataMaterialJasa.totalHPS;
						var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah)/100);
						console.log("AMBANG BAWAH = "+itungBatasBawah);
						if($scope.totalPenawaran < itungBatasBawah){
                            alert("Maaf, Harga Penawaran tidak boleh kurang dari IDR." + (itungBatasBawah).formatMoney(0, '.', ','));
                            document.getElementsByName("kursPA")[0].focus();
                        } else {
                            $scope.simpanPenawaranTotal(totalPenawaranAfter);
                            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
                        }
					} else {
                        $scope.simpanPenawaranTotal(totalPenawaranAfter);
                        $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
                    }  
				}
			}
		}
		
		// simpan Penawaran Vendor Total
        $scope.simpanPenawaranTotal = function(totalPenawaranAfter){
            var dataPenawaran = {
                nilaiPenawaran: totalPenawaranAfter,
                nilaiPenawaranOri: $scope.totalPenawaran,
                diskualifikasiHarga: 0,
                pesertaAuction: $scope.dataPesertaAuctionId,
                suratPenawaran: $scope.dataSuratPenawaranId
            }
            //console.log("DATA YANG AKAN DISIMPAN = "+JSON.stringify(dataPenawaran));
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataPenawaran
            }).success(function(data, status, headers, config) {
                console.log("INSERT PENAWARAN HARGA VENDOR OK : " + JSON.stringify(data));
				var penawaranAuctionId = data.id;
                $scope.simpanPenawaranPerItem(penawaranAuctionId);
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
                alert("DATA ERORR UNTUK MENYIMPAN!!");
            });
        }
		
		// simpan Penawaran Vendor per Item
        $scope.simpanPenawaranPerItem = function(penawaranAuctionId) {
            var data = $rootScope.ambilDataMaterialJasa[0];
            //console.log("DATA JASA YANG AKAN DISIMPAN = "+JSON.stringify(dataMaterialJasa));
			
			if($scope.totalPenawaran != 'undefined') {
                var dataPenawaranPerItem = {
                    hargaSatuanAfterCondition: data.item.itemNilaiAfter,
                    hargaTotalAfterCondition: data.item.totHPSAfter,
                    hargaSatuanOri: data.item.itemNilaiOri,
                    hargaTotalOri: data.item.totHPS,
                    penawaranAuction: penawaranAuctionId,
                    itemPengadaan: data.id
                }
                saveItem(dataPenawaranPerItem);
                console.log("DATA MATERIAL YANG AKAN DISIMPAN = "+JSON.stringify(dataPenawaranPerItem));
			}
		}		
		var saveItem = function(dataItem) {
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/create',
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                  var str = [];
                  for (var p in obj)
                      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                  return str.join("&");
                },
                data: dataItem
            }).success(function(data, status, headers, config) {
              console.log("INSERT PENAWARAN HARGA PER ITEM VENDOR OK : " + JSON.stringify(data));
            }).error(function(data, status, headers, config) {
              $scope.loading = true;
              alert("DATA ERORR UNTUK MENYIMPAN!!");
            });
        }
        /* ---------------------------- END Rincian Controller ------------------------------------------- */
		
		
	}
	
	
	PemasukanPenawaranSatuanTerbukaAddController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$filter', 'ngTableParams'];

})();