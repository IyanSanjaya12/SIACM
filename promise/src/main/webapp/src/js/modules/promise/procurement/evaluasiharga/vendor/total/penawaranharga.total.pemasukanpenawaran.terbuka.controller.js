/**=========================================================
 * Module: PemasukanPenawaranTotalTerbukaAddController.js
 * Author: F.H.K
 =========================================================*/


(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanPenawaranTotalTerbukaAddController', PemasukanPenawaranTotalTerbukaAddController);

    function PemasukanPenawaranTotalTerbukaAddController($http, $rootScope, $resource, $location, $window, toaster, $scope, $filter, ngTableParams, RequestService) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        var nilaiTerbaik = $rootScope.nilaiTerbaik;
        var hitung = 0;

        $scope.pengadaanId = $rootScope.detilPengadaan.id;
        $scope.penawaranAwal = $rootScope.sesiAuctionDetail.hargaAwal;
        $scope.selisihPenawaran = $rootScope.sesiAuctionDetail.selisihPenawaran;
        $scope.mataUang = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
        $scope.vendorId = $rootScope.vendorIdLogin;

        // HAPUS JIKA TIDAK MENGGUNAKAN KURS ASING -------------------------
        $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran;

        if (typeof nilaiTerbaik !== "undefined") {
            $scope.penawaranAwalDenganKurs = nilaiTerbaik;
            $scope.hargaPerbandingan = nilaiTerbaik - $scope.selisihPenawaran;
        } else {
            $scope.penawaranAwalDenganKurs = $scope.penawaranAwal;
            $scope.hargaPerbandingan = $scope.penawaranAwal - $scope.selisihPenawaran;
        }

        $rootScope.nilaiValue = 'NaN';
        // -----------------------------------------------------------------

        /* ----------------------------------------------------------------------------------------------- */


        /* ============================ START Data Peserta Auction ======================================= */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PesertaAuctionServices/getByAuctionVendor/' + $rootScope.sesiAuctionDetail.id + '/' + $scope.vendorId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $scope.dataPesertaAuctionId = data[0].id;
            //console.log("PESERTA AUCTION ID = "+ $scope.dataPesertaAuctionId);
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Data Peserta Auction ----------------------------------------- */


        /* ============================ START Data Surat Penawaran ======================================= */
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendorId + '/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (typeof data[0] !== 'undefined') {
                $scope.dataSuratPenawaranId = data[0].id;
                if (data[0].pengadaan.tahapanPengadaan.tahapan.id == 14000001) {
                    $scope.mataUangId = data[0].mataUang.id;
                    $scope.mataUangPenawaranVendor = data[0].mataUang.id;
                    $scope.mataUangPenawaranVendorKode = data[0].mataUang.kode;
                }
            } else {
                $scope.mataUang = "IDR";
                $scope.mataUangId = 1;
                $scope.mataUangPenawaranVendor = $scope.mataUangId;
                var suratPenawaranData = {
                    pengadaan: $scope.pengadaanId,
                    vendor: $scope.vendorId,
                    mataUang: 1 //IDR
                };
                $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/insertSuratPenawaran',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: suratPenawaranData
                    }).success(function (data, status, headers, config) {
                        $scope.dataSuratPenawaranId = data.id;
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            }
            //console.log("SURAT PENAWARAN ID = "+ $scope.dataSuratPenawaranId);
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Data Surat Penawaran ----------------------------------------- */


        /* ============================ START Rincian Kebutuhan Material ================================= */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isMaterialNotEmpty = true;
            angular.forEach(data, function (value, index) {
                data[index].item.totHPS = 0;
                data[index].item.itemNilaiOri = 0;
                data[index].item.itemNilaiAfter = 0;
                data[index].item.totHPSAfter = 0;
            });
            $rootScope.ambilDataMaterial = data;

            // paging untuk material
            $scope.itemPengadaanMaterialByPengadaanIdList = new ngTableParams({
                page: 1,
                count: 3
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Rincian Kebutuhan Materrial ---------------------------------- */


        /* ============================ START Rincian Kebutuhan Jasa ===================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isJasaNotEmpty = true;
            angular.forEach(data, function (value, index) {
                data[index].item.totHPS = 0;
                data[index].item.itemNilaiOri = 0;
                data[index].item.itemNilaiAfter = 0;
                data[index].item.totHPSAfter = 0;
            });
            $rootScope.ambilDataJasa = data;

            // paging untuk jasa
            $scope.itemPengadaanJasaByPengadaanIdList = new ngTableParams({
                page: 1,
                count: 3
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Rincian Kebutuhan Jasa --------------------------------------- */


        /* ============================ START Rincian Kurs Pengadaan ===================================== */

        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + $scope.vendorId)
            .success(function (data) {
                if (typeof data.id !== 'undefined' && data.id !== null) {
                    console.log("Data : " + JSON.stringify(data));
                    $scope.mataUang = data.mataUang.kode;
                    $scope.mataUangId = data.mataUang.id;
                    
                   
                    $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + $scope.mataUangId)
                        .success(function (dataKurs) {
                        	$rootScope.nilaiValue = dataKurs.nilai;
                            $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran * $rootScope.nilaiValue;
                            $scope.penawaranAwalDenganKurs = $scope.penawaranAwalDenganKurs * $rootScope.nilaiValue;
                            $scope.hargaPerbandingan = $scope.hargaPerbandingan * $rootScope.nilaiValue;
                        })
                    $scope.mataUangPenawaranVendor = $scope.mataUangId;
                    var params = {
                        pengadaanId: $scope.pengadaanId,
                        mataUangId: $scope.mataUangId
                    }
                    RequestService.doPOST('/procurement/master/mata-uang/getMataUangListByPengadaan', params)
                        .then(function (response) {
                        	if (response.data != undefined) {
                                $scope.kursList = response.data;
                                
                            }
                        });
                } else {
                    $scope.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list/')
                        .success(function (data, status, headers, config) {
                            $scope.kursList = data;
                            $scope.loading = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loading
                        });
                }
            })
            .error(function (data) {
            });

        $scope.mataUangPenawaranVendor = $scope.mataUangId;
        $scope.mataUangPenawaranVendorKode = $scope.mataUang;
        $scope.changeMataUang = function () {
            angular.forEach($scope.kursList, function (value, index) {
                if (value.id == $scope.mataUangPenawaranVendor) {
                    $scope.mataUangPenawaranVendorKode = value.kode;
                    $scope.mataUangId = value.id;
                }
            });
            hitungPenawaran($rootScope.ambilDataJasa, $rootScope.ambilDataMaterial);
        }

        $scope.kursPA = 'IDR';
        $scope.pilih = function () {
            ambilKursSource('IDR', $scope.kursPA);
        }

        // Ambil Nilai Kurs setiap mata uang dari Source (ONLINE, bukan DataBase) terkini dengan BASE USD
        var ambilKursSource = function (mu, cur) {
                $scope.loading = true;
                $http.jsonp('http://api.fixer.io/latest?base=' + cur + '&callback=JSON_CALLBACK', {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    for (var key in data.rates) {
                        var value = data.rates[key];
                        if (mu == key) {
                            $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran / value;
                            $scope.penawaranAwalDenganKurs = $scope.penawaranAwal / value;
                            $scope.totalPenawaranDenganKurs = $scope.totalPenawaranAfter / value;
                            $scope.mataUang = $scope.kursPA;
                            $rootScope.nilaiValue = value;
                        } else if (mu == cur) {
                            $scope.selisihPenawaranDenganKurs = $scope.selisihPenawaran;
                            if (nilaiTerbaik != undefined) {
                                $scope.penawaranAwalDenganKurs = nilaiTerbaik;
                            } else {
                                $scope.penawaranAwalDenganKurs = $scope.penawaranAwal;
                            }
                            $scope.totalPenawaranDenganKurs = $scope.totalPenawaranAfter;
                            $scope.mataUang = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
                            $rootScope.nilaiValue = 'NaN';
                        }

                    }
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading
                });

            }
            /* ---------------------------- END Rincian Kurs Pengadaan --------------------------------------- */


        /* ============================ START Rincian Conditional Price ================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $scope.vendorId + '/' + $scope.pengadaanId + '/' + 1, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            angular.forEach(data, function (value, index) {
                if (data[index].conditionalPrice.isPersentage == 1)
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
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading
        });

        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $scope.vendorId + '/' + $scope.pengadaanId + '/' + 2, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            angular.forEach(data, function (value, index) {
                if (data[index].conditionalPrice.isPersentage == 1)
                    data[index].logo = '%';
                else
                    data[index].logo = $rootScope.sesiAuctionDetail.pengadaan.mataUang.kode;
            });
            $rootScope.hasilItem = data;
            //console.log("ITEM CONDITIONAL PRICE " + JSON.stringify($rootScope.hasilItem));

            // paging untuk jasa
            $scope.ItemConList = new ngTableParams({
                page: 1,
                count: 3
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading
        });
        /* ---------------------------- END Rincian Conditional Price ------------------------------------ */


        /* ============================ START CONTROLLER DETAIL ========================================== */
        // fungsi Untuk Format Number
        Number.prototype.formatMoney = function (c, d, t) {
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
        $scope.setTotalHPS = function (data, nilaiOri) {
            nilaiOri = nilaiOri * 1;
            var dataItem = $rootScope.hasilItem;
            //console.log("DATA JASA YANG AKAN DISIMPAN = "+JSON.stringify($rootScope.hasilItem));
            var hasilPenjumlahanItem = 0;
            var nilaiAfter = 0;

            angular.forEach(dataItem, function (value, index) {
                if (dataItem[index].conditionalPrice.isPersentage == 1) {
                    if (dataItem[index].itemPengadaan.id == data.id) {
                        hasilPenjumlahanItem = hasilPenjumlahanItem + ((nilaiOri * dataItem[index].nilai / 100) * dataItem[index].conditionalPrice.fungsi);
                    }
                } else {
                    if (dataItem[index].itemPengadaan.id == data.id) {
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
        var hitungPenawaran = function (dataJasa, dataMaterial) {
            $scope.totalPenawaran = 0;
            var hasilPenjumlahanHeader = 0;

            angular.forEach(dataMaterial, function (value, index) {
                $scope.totalPenawaran = $scope.totalPenawaran + dataMaterial[index].item.totHPS;
            });
            angular.forEach(dataJasa, function (value, index) {
                $scope.totalPenawaran = $scope.totalPenawaran + dataJasa[index].item.totHPS;
            });

            var data = $rootScope.hasilHeader;
            angular.forEach(data, function (value, index) {
                if (data[index].conditionalPrice.isPersentage == 1) {
                    hasilPenjumlahanHeader = hasilPenjumlahanHeader + (($scope.totalPenawaran * data[index].nilai / 100) * data[index].conditionalPrice.fungsi);
                } else {
                    hasilPenjumlahanHeader = hasilPenjumlahanHeader + (data[index].nilai * data[index].conditionalPrice.fungsi);
                }
            });
            $scope.totalPenawaranAfter = $scope.totalPenawaran + hasilPenjumlahanHeader;

            //HAPUS JIKA TIDAK MENGGUNAKAN METODE KURS ASING -------------------------------------
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + $scope.mataUangId)
                .success(function (data) {
                    $scope.totalPenawaranDenganKonfersi = $scope.totalPenawaranAfter * data.nilai;

                    if ($scope.totalPenawaranDenganKonfersi > $scope.hargaPerbandingan) {
                        alert("Maaf, Harga Penawaran tidak boleh lebih dari " + ($scope.hargaPerbandingan).formatMoney(0, '.', ',') + " IDR");
                    }
                });

            $scope.totalPenawaranDenganKurs = $scope.totalPenawaranAfter;
            //-------------------------------------------------------------------------------------

        }
        
        // simpan Penawaran Vendor total
        $scope.btnDisable = false;
        $scope.simpanPenawaran = function () {
        	
            if (typeof $scope.totalPenawaran != 'undefined') {
            	if ( $scope.totalPenawaran == 0 ) {
                	alert("Maaf, Harga Penawaran tidak boleh 0");
                } else {

                    if ($scope.totalPenawaranDenganKonfersi > $scope.hargaPerbandingan) {
                        alert("Maaf, Harga Penawaran tidak boleh lebih dari " + ($scope.hargaPerbandingan).formatMoney(0, '.', ',') + " IDR");
                        document.getElementsByName("hps")[0].focus();
                    } else {
                        if($scope.mataUangId!=null && typeof $scope.mataUangId !== 'undefined'){
                            if ($rootScope.kriteriaAmbangBatasList.length > 0) {
                                var ambangBawah = $rootScope.kriteriaAmbangBatasList[0].bawah;
                                var jumlahMaterial = 0;
                                var jumlahJasa = 0;
                                var totalHpsBarangJasa = 0;
                                var dataMaterial = $rootScope.ambilDataMaterial;
                                var dataJasa = $rootScope.ambilDataJasa;
                                angular.forEach(dataMaterial, function (value, index) {
                                    jumlahMaterial = jumlahMaterial + dataMaterial[index].totalHPS;
                                });
                                angular.forEach(dataJasa, function (value, index) {
                                    jumlahJasa = jumlahJasa + dataJasa[index].totalHPS;
                                });
                                totalHpsBarangJasa = jumlahMaterial + jumlahJasa;
                                var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBawah) / 100);
                                if ($rootScope.nilaiValue != 'NaN') {
                                    itungBatasBawah = itungBatasBawah * $rootScope.nilaiValue;
                                }
                                //console.log("AMBANG BAWAH = " + itungBatasBawah);
                                if ($scope.totalPenawaranDenganKonfersi < itungBatasBawah) {
                                    alert("Maaf, Harga Penawaran tidak boleh kurang dari " + (itungBatasBawah).formatMoney(0, '.', ',') + " IDR");
                                    //perhitungan utama menggunakan IDR
                                    document.getElementsByName("hps")[0].focus();
                                } else {
                                    $scope.btnDisable = true;
                                    $scope.simpanPenawaranTotal($scope.totalPenawaranAfter);
                                }
                            } else {
                            	$scope.btnDisable = true;
                                $scope.simpanPenawaranTotal($scope.totalPenawaranAfter);
                            }
                        } else {
                            alert("Mata Uang harus diisi!" );
                        }                   	
                    }
                	
                }
            	
            	
            //alert untuk memasukkan penawaran    
            } else {
            	alert("Mohon isi Penawaran terlebih dahulu" );
            }
        }


        // simpan Penawaran Vendor Total
        $scope.simpanPenawaranTotal = function (totalPenawaranAfter) {
        	RequestService.modalConfirmation().then(function (result) {
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
        	}, function(close){
               $scope.btnDisable = false;
            });
        	
        }

        // simpan Penawaran Vendor per Item
        $scope.simpanPenawaranPerItem = function (penawaranAuctionId) {
            var dataMat = $rootScope.ambilDataMaterial;
            var dataJas = $rootScope.ambilDataJasa;
            //console.log("DATA JASA YANG AKAN DISIMPAN = "+JSON.stringify(dataJas));
            if ($scope.totalPenawaran != 'undefined') {
                angular.forEach(dataMat, function (value, index) {
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

                    console.log("DATA MATERIAL YANG AKAN DISIMPAN = " + JSON.stringify(dataPenawaranPerItem));
                    saveItem(dataPenawaranPerItem);
                });

                angular.forEach(dataJas, function (value, index) {
                    var dataPenawaranPerItem = {
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

                    console.log("DATA JASA YANG AKAN DISIMPAN = " + JSON.stringify(dataPenawaranPerItem));
                    saveItem(dataPenawaranPerItem);
                });
            }
        }

        var saveItem = function (dataItem) {
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataItem
            }).success(function (data, status, headers, config) {
                $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
                //console.log("INSERT PENAWARAN HARGA PER ITEM VENDOR OK : " + JSON.stringify(data));
            }).error(function (data, status, headers, config) {
            	$scope.btnDisable = true;
                //$scope.loading = true;
                alert("DATA ERORR UNTUK MENYIMPAN!!");
            });
        }

        // tombol kembali ke index.html
        $scope.back = function () {
            $scope.go('/appvendor/promise/procurement/evaluasiharga/vendor/total/auction/terbuka');
        }

        // fungsi target lokasi
        $scope.go = function (path) {
                $location.path(path);
            }
            /* ---------------------------- END Rincian Controller ------------------------------------------- */
    }

    PemasukanPenawaranTotalTerbukaAddController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window', 'toaster', '$scope', '$filter', 'ngTableParams', 'RequestService'];

})();