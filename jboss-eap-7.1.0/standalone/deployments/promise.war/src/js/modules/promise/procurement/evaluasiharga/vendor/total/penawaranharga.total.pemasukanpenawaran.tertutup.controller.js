/**=========================================================
 * Module: PemasukanPenawaranTotalTertutupAddController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanPenawaranTotalTertutupAddController', PemasukanPenawaranTotalTertutupAddController);

    function PemasukanPenawaranTotalTertutupAddController($http, $rootScope, $resource, $location, $window, toaster, $scope, $filter, ngTableParams, RequestService) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
		var nilaiTerbaik = $rootScope.nilaiTerbaik;
		
		$scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        $scope.pengadaanId = $rootScope.detilPengadaan.id;
        $scope.penawaranAwal = $rootScope.hargaAwal;
        $scope.selisihPenawaran = $rootScope.sesiAuctionDetail.selisihPenawaran;
        $scope.mataUang = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
        $scope.vendorId = $rootScope.vendorIdLogin;
        
        // HAPUS JIKA TIDAK MENGGUNAKAN KURS ASING -------------------------
        $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran;
        
		if(nilaiTerbaik != undefined) {
			$scope.penawaranAwalDenganKurs = nilaiTerbaik;
			$scope.hargaPerbandingan = nilaiTerbaik - $scope.selisihPenawaran;
		} else if($scope.penawaranAwal != undefined) {
			$scope.penawaranAwalDenganKurs = $scope.penawaranAwal;
			$scope.hargaPerbandingan = $scope.penawaranAwal - $scope.selisihPenawaran;
		} else {
			$scope.penawaranAwalDenganKurs = $rootScope.hargaAwalPenawaranPertama;
			$scope.hargaPerbandingan = $rootScope.hargaAwalPenawaranPertama - $scope.selisihPenawaran;
		}
		
        $rootScope.nilaiValue = 'NaN';
        // -----------------------------------------------------------------
        
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
        
        
        /* ============================ START Rincian Kebutuhan Material ================================= */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
        	if (data.length > 0)
                $scope.isMaterialNotEmpty = true;
            angular.forEach(data,function(value,index){
                data[index].item.totHPS = 0;
            });
            $rootScope.ambilDataMaterial = data;
            
            // paging untuk material
            $scope.itemPengadaanMaterialByPengadaanIdList = new ngTableParams({
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
            $scope.loading = false;
        });
        /* ---------------------------- END Rincian Kebutuhan Materrial ---------------------------------- */
        

        /* ============================ START Rincian Kebutuhan Jasa ===================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
        	if (data.length > 0)
                $scope.isJasaNotEmpty = true;
            angular.forEach(data,function(value,index){
                data[index].item.totHPS = 0;
            });
            $rootScope.ambilDataJasa = data;
            
            // paging untuk jasa
            $scope.itemPengadaanJasaByPengadaanIdList = new ngTableParams({
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
            $scope.loading = false;
        });
        /* ---------------------------- END Rincian Kebutuhan Jasa --------------------------------------- */

        
        /* ============================ START Rincian Kurs Pengadaan ===================================== */
        $scope.loading = true;
        RequestService.doGET('/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/'+ $scope.pengadaanId + '/' + $scope.vendorId)
		.then(function (data) {
			if(data != undefined) {

				$scope.mataUang = data.mataUang.kode;
        		$scope.mataUangId = data.mataUang.id;
				var params = { 
		        		pengadaanId : $scope.pengadaanId,
		        		mataUangId : $scope.mataUangId
		        }
		        RequestService.doPOST('/procurement/master/mata-uang/getMataUangListByPengadaan', params)
				.then(function (response) {
					if(response.data != undefined) {
						$scope.kursList = response.data;
						$rootScope.nilaiValue = parseFloat(response.data[0].kurs);
						$scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran * $rootScope.nilaiValue;
			            $scope.penawaranAwalDenganKurs = $scope.penawaranAwalDenganKurs * $rootScope.nilaiValue;
			            $scope.hargaPerbandingan = $scope.hargaPerbandingan * $rootScope.nilaiValue;
					}
				});
			} else {
				$scope.loading = true;
		        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list/', {
		            ignoreLoadingBar: true
		        }).success(function (data, status, headers, config) {
		            $scope.kursList = data;
		            $scope.loading = false;
		        }).error(function (data, status, headers, config) {
		            $scope.loading
		        });
			}
		})
        
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
                    } else if(cur == mu) {
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
        
        
        /* ============================ START Rincian Conditional Price ================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $scope.vendorId + '/' + $scope.pengadaanId + '/' + 1, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            angular.forEach(data,function(value,index){
                if(data[index].conditionalPrice.isPersentage == 1)
                    data[index].logo = '%';
                else
                    data[index].logo = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
            });
            $rootScope.hasilHeader = data;
            
            // paging untuk jasa
            $scope.headerConList = new ngTableParams({
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
        
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $scope.vendorId + '/' + $scope.pengadaanId + '/' + 2, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            angular.forEach(data,function(value,index){
                if(data[index].conditionalPrice.isPersentage == 1)
                    data[index].logo = '%';
                else
                    data[index].logo = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
            });
            $rootScope.hasilItem = data;
            
            // paging untuk jasa
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
        
        // set Nilai Total HPS di kanan tabel
        $scope.setTotalHPS = function(data, nilaiOri) {
            var dataItem = $rootScope.hasilItem;
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
            
            hitungPenawaran($rootScope.ambilDataJasa, $rootScope.ambilDataMaterial);
        }      
        var hitungPenawaran = function(dataJasa, dataMaterial) {
			var hasilPenjumlahanHeader = 0;
            $scope.totalPenawaran = 0;
            
            angular.forEach(dataMaterial,function(value,index){
                $scope.totalPenawaran = $scope.totalPenawaran + dataMaterial[index].item.totHPS;
            });
            angular.forEach(dataJasa,function(value,index){
                $scope.totalPenawaran = $scope.totalPenawaran + dataJasa[index].item.totHPS;
            });
            
			var data = $rootScope.hasilHeader;
            angular.forEach(data,function(value,index){
                if(data[index].conditionalPrice.isPersentage == 1) {
                    hasilPenjumlahanHeader = hasilPenjumlahanHeader + (($scope.totalPenawaran * data[index].nilai/100) * data[index].conditionalPrice.fungsi);
                } else {
                    hasilPenjumlahanHeader = hasilPenjumlahanHeader + (data[index].nilai * data[index].conditionalPrice.fungsi);
                }
            });
            
			$scope.totalPenawaranAfter = $scope.totalPenawaran + hasilPenjumlahanHeader;
			
            //HAPUS JIKA TIDAK MENGGUNAKAN METODE KURS ASING -------------------------------------
            $scope.totalPenawaranDenganKurs = $scope.totalPenawaranAfter;
            //-------------------------------------------------------------------------------------
            
            if ($scope.totalPenawaranAfter > $scope.hargaPerbandingan) {
                alert("Maaf, Harga Penawaran tidak boleh lebih dari " + ($scope.hargaPerbandingan).formatMoney(0, '.', ',') + " " + $scope.mataUang);
            }
        }
        
        
        // simpan Penawaran Vendor
        $scope.simpanPenawaran = function() {
            
            if($scope.totalPenawaran != 'undefined') {
                if($scope.totalPenawaranAfter > $scope.hargaPerbandingan) {
                	alert("Maaf, Harga Penawaran tidak boleh lebih dari " + ($scope.hargaPerbandingan).formatMoney(0, '.', ',') + " " + $scope.mataUang);
                    document.getElementsByName("hps")[0].focus();
                } else {
                    if($rootScope.kriteriaAmbangBatasList.length > 0){
                        var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
                        var jumlahMaterial = 0;
                        var jumlahJasa = 0;
                        var totalHpsBarangJasa = 0;
                        var dataMaterial = $rootScope.ambilDataMaterial;
                        var dataJasa = $rootScope.ambilDataJasa;
                        angular.forEach(dataMaterial,function(value,index){
                            jumlahMaterial = jumlahMaterial + dataMaterial[index].totalHPS;
                        });
                        angular.forEach(dataJasa,function(value,index){
                            jumlahJasa = jumlahJasa + dataJasa[index].totalHPS;
                        });
                        totalHpsBarangJasa = jumlahMaterial + jumlahJasa;
                        var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah)/100);
                        if ($rootScope.nilaiValue != 'NaN') {
                        	itungBatasBawah = itungBatasBawah * $rootScope.nilaiValue;
                        }
                        
                        if($scope.totalPenawaran < itungBatasBawah){
                        	alert("Maaf, Harga Penawaran tidak boleh kurang dari " + (itungBatasBawah).formatMoney(0, '.', ',') + " " + $scope.mataUang);
                            document.getElementsByName("hps")[0].focus();
                        } else {
                            $scope.simpanPenawaranTotal($scope.totalPenawaranAfter);
                            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
                        }
                    } else {
                        $scope.simpanPenawaranTotal($scope.totalPenawaranAfter);
                        $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
                    }                   
                }
            }
        }
        
		// simpan Penawaran Vendor Total
        $scope.simpanPenawaranTotal = function(totalPenawaranAfter){
        	var dataPenawaran = {
                    nilaiPenawaranOri: $scope.totalPenawaran,
                    diskualifikasiHarga: 0,
                    pesertaAuction: $scope.dataPesertaAuctionId,
                    suratPenawaran: $scope.dataSuratPenawaranId,
                    mataUang: $scope.mataUangId
                }
            if ($rootScope.nilaiValue != 'NaN') {
            	var jumlah = totalPenawaranAfter / $rootScope.nilaiValue;
            	dataPenawaran.nilaiPenawaran = parseFloat(Math.round(jumlah * 100) / 100).toFixed(2);
            } else {
            	dataPenawaran.nilaiPenawaran = totalPenawaranAfter;
            }
            
            RequestService.doPOST('/procurement/evaluasiharga/PenawaranAuctionServices/create', dataPenawaran)
    		.then(function (response) {
    			var penawaranAuctionId = response.data.id;
    			$scope.simpanPenawaranPerItem(penawaranAuctionId);
    		});
        }
		
        // simpan Penawaran Vendor per Item
        $scope.simpanPenawaranPerItem = function(penawaranAuctionId) {
            var dataMat = $rootScope.ambilDataMaterial;
            var dataJas = $rootScope.ambilDataJasa;
            if($scope.totalPenawaran != 'undefined') {
                angular.forEach(dataMat,function(value,index){
                    var dataPenawaranPerItem = {
                		hargaSatuanOri: dataMat[index].item.itemNilaiOri,
                        hargaTotalOri: dataMat[index].item.totHPS,
                        hargaSatuanAfterCondition: dataMat[index].item.itemNilaiAfter,
                        hargaTotalAfterCondition: dataMat[index].item.totHPSAfter,
                        penawaranAuction: penawaranAuctionId,
                        itemPengadaan: dataMat[index].id,
                        mataUang: $scope.mataUangId
                    }
                    
                    if ($rootScope.nilaiValue != 'NaN') {
                    	var jumlahNilaiItem = dataMat[index].item.itemNilaiAfter / $rootScope.nilaiValue;
                    	var jumlahNilaiTotal = dataMat[index].item.totHPSAfter / $rootScope.nilaiValue;
                    	dataPenawaranPerItem.hargaSatuanKonversi = parseFloat(Math.round(jumlahNilaiItem * 100) / 100).toFixed(2);
                    	dataPenawaranPerItem.hargaTotalKonversi = parseFloat(Math.round(jumlahNilaiTotal * 100) / 100).toFixed(2);
                    } else {
                    	dataPenawaranPerItem.hargaSatuanKonversi = dataMat[index].item.itemNilaiAfter;
                    	dataPenawaranPerItem.hargaTotalKonversi = dataMat[index].item.totHPSAfter;
                    }
                    
                    saveItem(dataPenawaranPerItem);
                });
                  
                angular.forEach(dataJas,function(value,index){
                    var dataPenawaranPerItem = {
                        hargaSatuanAfterCondition: dataJas[index].item.itemNilaiAfter,
                        hargaTotalAfterCondition: dataJas[index].item.totHPSAfter,
                        hargaSatuanOri: dataJas[index].item.itemNilaiOri,
                        hargaTotalOri: dataJas[index].item.totHPS,
                        penawaranAuction: penawaranAuctionId,
                        itemPengadaan: dataJas[index].id,
                        mataUang: $scope.mataUangId
                    }
                    
                    if ($rootScope.nilaiValue != 'NaN') {
                    	var jumlahNilaiItem = dataJas[index].item.itemNilaiAfter / $rootScope.nilaiValue;
                    	var jumlahNilaiTotal = dataJas[index].item.totHPSAfter / $rootScope.nilaiValue;
                    	dataPenawaranPerItem.hargaSatuanAfterCondition = parseFloat(Math.round(jumlahNilaiItem * 100) / 100).toFixed(2);
                    	dataPenawaranPerItem.hargaTotalAfterCondition = parseFloat(Math.round(jumlahNilaiTotal * 100) / 100).toFixed(2);
                    } else {
                    	dataPenawaranPerItem.hargaSatuanAfterCondition = dataJas[index].item.itemNilaiAfter;
                    	dataPenawaranPerItem.hargaTotalAfterCondition = dataJas[index].item.totHPSAfter;
                    }
                    
                    saveItem(dataPenawaranPerItem);
                });
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
        
        // tombol kembali ke index.html
        $scope.back = function() {
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/total/auction/tertutup');
            //$scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
        /* ---------------------------- END Rincian Controller ------------------------------------------- */
    }
    
    PemasukanPenawaranTotalTertutupAddController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$filter', 'ngTableParams', 'RequestService'];

})();