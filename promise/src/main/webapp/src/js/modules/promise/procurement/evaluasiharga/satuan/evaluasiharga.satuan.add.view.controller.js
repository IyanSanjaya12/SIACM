/**=========================================================
 * Module: EvaluasiHargaSatuanAuctionAddViewController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaSatuanAuctionAddViewController', EvaluasiHargaSatuanAuctionAddViewController);

    function EvaluasiHargaSatuanAuctionAddViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
    
    
		/* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
		var dataLemparan = $rootScope.dataLemparan;
        var itemId = dataLemparan.item.id;
		var itemPengadaanId = dataLemparan.id
		$rootScope.itemPId = itemPengadaanId;
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
        
        
        /* ============================= START Data Evaluasi Admin Pembobotan =============================== */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
        	if(data.length > 0)
        		$rootScope.maksNilaiLulusAmin = data[0].nilaiMinimalKelulusanAdministrasi;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* ----------------------------- END Data Evaluasi Admin Pembobotan -------------------------------- */
        
        
        /* ============================= START Data Evaluasi Teknis Pembobotan ============================= */
        $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointPembobotanServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
        	if(data.length > 0)
        		$rootScope.maksNilaiLulusTeknis = data[0].nilaiMinimalKelulusanTeknis;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* ---------------------------- END Data Evaluasi Teknis Pembobotan --------------------------------- */
		
		
		/* ================================= START Sesi Auction View Detail ============================== */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/getByPengadaanItemPengadaan/' + $scope.pengadaanId + '/' + itemPengadaanId, {
			ignoreLoadingBar: true
		}).success(function(data, status, headers, config) {
            $rootScope.sesiAuctionDetail = data;
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });     
        /* --------------------------------- END Rincian Sesi Auction View ------------------------------- */
        
        
        /* ================================= START Data Penyedia Barang/Jasa Detail ====================== */
		// look up ke tabel Penawaran Auction
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByPengadaanAndItemPengadaan/' + $scope.pengadaanId + '/' + itemPengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
			console.log("DATA PENAWARAN AUCTION = "+JSON.stringify(data));
			$rootScope.dataPenawaranAuction = data;
			$scope.loading = false;
		}).error(function(data, status, headers, config) {
			$scope.loading = true;
		});		
		
        //console.log("ITEM ID YANG DILEMPAR = "+itemId);
        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByPengadaanAndItem/' + $scope.pengadaanId + '/' + itemId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            $scope.dataPenyediaList = [];
            $scope.listNilaiTerbaik = [];
            $scope.totNilaiSemuaVendor = [];
            $scope.listDataPenawaranAuction = [];
			
            angular.forEach(data,function(value,index){
                $scope.dataPenyediaList.push(value);
                $rootScope.listHarga.push(data[index].hargaTotalAfterCondition);
                $rootScope.mataUangCode = data[index].penawaranPertama.suratPenawaran.mataUang.kode;       
            });
			
            $rootScope.listDataPenyedia = data;
            $scope.loading = false;
            //console.log("Hasil dataPenyediaList = " + JSON.stringify($rootScope.listDataPenyedia));
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* -------------------------------- END Rincian Data Penyedia Barang/Jasa ----------------------- */
		
		
		/* ================================ START Nilai Evaluasi Detail ================================= */
        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + itemPengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
			//console.log("HASIL EVALUASI HARGA VENDOR DETAIL = " + JSON.stringify(data));
			hitungEvaluasiHarga(data);
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
                    $scope.loading = true;
        });
		/* -------------------------------- END Rincian Nilai Evaluasi ---------------------------------- */
		
		
		/* ================================ START Perhitungan Evaluasi Harga ============================ */
		//-----------> Perhitungan dengan atau tanpa AMBANG BATAS <-------------------
		var hitungEvaluasiHarga = function(dataEval) {
			$rootScope.dataEval = dataEval;
			
			$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByPengadaanAndItemPengadaan/' + $scope.pengadaanId + '/' + itemPengadaanId, {
				ignoreLoadingBar: true
			}).success(function(data, status, headers, config) {
				
				var dataEval = $rootScope.dataEval;
				var hargaTerbaik = 0;
				for(var i=0; i<dataEval.length; i++){
					var nilaiPenawaranListVendor = [];
					for(var j=0; j<data.length; j++){
						if(dataEval[i].evaluasiHargaVendor.vendor.id == data[j].penawaranAuction.pesertaAuction.vendor.id){
							nilaiPenawaranListVendor.push(data[j].hargaTotalAfterCondition);
						}
					}
					hargaTerbaik = sortNilai(nilaiPenawaranListVendor, 'asc');
					var hargaTerbaikVendor = { hargaTerbaik: hargaTerbaik };
					angular.extend(dataEval[i], hargaTerbaikVendor);
				}
				
				hitungAmbangBatas(dataEval);
				$scope.loading = false;
			}).error(function(data, status, headers, config) {
				$scope.loading = true;
				alert("DATA ERORR");
			});
		}
		
		//-----------> Perhitungan dengan atau tanpa BOBOT KRITERIA <-------------------
		var hitungAmbangBatas = function(dataEval) {
			
			var barangJasaList = $rootScope.itemBarangJasaList;
			var totalHpsBarangJasa = 0;
			var ambangBatas = $rootScope.kriteriaAmbangBatasList[0];
			$scope.listNilaiTerbaik = [];
			
			angular.forEach(barangJasaList,function(value,index){
				if(barangJasaList[index].id == $rootScope.itemPId){
					totalHpsBarangJasa = totalHpsBarangJasa + barangJasaList[index].totalHPS;
				}
			});
			
			angular.forEach(dataEval,function(value,index){
				if($rootScope.kriteriaAmbangBatasList.length > 0){ // Menggunakan Ambang Batas
					var itungBatasAtas = totalHpsBarangJasa + ((totalHpsBarangJasa * ambangBatas.atas)/100);
					var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBatas.bawah)/100);
					//console.log("AMBANG BATAS ATAS = " + itungBatasAtas + " DAN AMBANG BATAS BAWAH = " + itungBatasBawah);
					if(totalHpsBarangJasa < dataEval[index].hargaTerbaik) {
						if(dataEval[index].hargaTerbaik < itungBatasAtas && dataEval[index].hargaTerbaik > itungBatasBawah){
							$scope.totNilaiSemuaVendor.push(dataEval[index].hargaTerbaik);
						} else {
							dataEval[index].evaluasiHargaVendor.nilaiHarga = 0;
							dataEval[index].evaluasiHargaVendor.nilaiTotal = 0;
						}
					} else {
						if(dataEval[index].hargaTerbaik < itungBatasAtas && dataEval[index].hargaTerbaik > itungBatasBawah) {
							$scope.totNilaiSemuaVendor.push(dataEval[index].hargaTerbaik);
						} else {
							dataEval[index].evaluasiHargaVendor.nilaiHarga = 0;
							dataEval[index].evaluasiHargaVendor.nilaiTotal = 0;
						}
					}
				} else { // Tidak Menggunakan Ambang Batas
					if(totalHpsBarangJasa < dataEval[index].hargaTerbaik) {
						dataEval[index].evaluasiHargaVendor.nilaiHarga = 0;
						dataEval[index].evaluasiHargaVendor.nilaiTotal = 0;
					} else {
						$scope.totNilaiSemuaVendor.push(dataEval[index].hargaTerbaik);
					}
				}
			});
			
			var nilaiTerbaikSemuaVendor = sortNilai($scope.totNilaiSemuaVendor, 'asc');
			
			angular.forEach(dataEval,function(value,index){
				var nilaiAdmin = dataEval[index].evaluasiHargaVendor.nilaiAdmin;
				var nilaiTeknis = dataEval[index].evaluasiHargaVendor.nilaiTeknis;
				var nilaiHarga = 0;
				
				if(nilaiTerbaikSemuaVendor == dataEval[index].hargaTerbaik){
					dataEval[index].evaluasiHargaVendor.nilaiHarga = 100;
					nilaiHarga = dataEval[index].evaluasiHargaVendor.nilaiHarga;
					if($scope.sistemEvaluasiPenawaranId == 2) {
						dataEval[index].evaluasiHargaVendor.nilaiTotal = hitungTotalNilai(ambangBatas, dataEval[index]);
						if(dataEval[index].evaluasiHargaVendor.nilaiAdmin >= $rootScope.maksNilaiLulusAmin) {
							if(dataEval[index].evaluasiHargaVendor.nilaiTeknis >= $rootScope.maksNilaiLulusTeknis)
								dataEval[index].evaluasiHargaVendor.isValid = 1;
						}		
					} else {
                        dataEval[index].evaluasiHargaVendor.nilaiTotal = (nilaiAdmin + nilaiTeknis + nilaiHarga)/3;
                        dataEval[index].evaluasiHargaVendor.isValid = 1
					}
					$scope.listNilaiTerbaik.push(dataEval[index].evaluasiHargaVendor.nilaiTotal);
				} else {
					if(dataEval[index].evaluasiHargaVendor.nilaiHarga != 0){
						dataEval[index].evaluasiHargaVendor.nilaiHarga = (nilaiTerbaikSemuaVendor/dataEval[index].hargaTerbaik)*100
						nilaiHarga = dataEval[index].evaluasiHargaVendor.nilaiHarga;
                        if($scope.sistemEvaluasiPenawaranId == 2) {
							dataEval[index].evaluasiHargaVendor.nilaiTotal = hitungTotalNilai(ambangBatas, dataEval[index]);
							if(dataEval[index].evaluasiHargaVendor.nilaiAdmin >= $rootScope.maksNilaiLulusAmin) {
								if(dataEval[index].evaluasiHargaVendor.nilaiTeknis >= $rootScope.maksNilaiLulusTeknis)
									dataEval[index].evaluasiHargaVendor.isValid = 1;
							}
                        } else {
                            dataEval[index].evaluasiHargaVendor.nilaiTotal = (nilaiAdmin + nilaiTeknis + nilaiHarga)/3;
                            dataEval[index].evaluasiHargaVendor.isValid = 1
                        }
                        $scope.listNilaiTerbaik.push(dataEval[index].evaluasiHargaVendor.nilaiTotal);
                    }
                }
			});
			
			var nilaiFlag = sortNilai($scope.listNilaiTerbaik, 'dsc');
			
            var dataNilai = $rootScope.listDataPenyedia;
            angular.forEach(dataEval,function(value,index){
                if(nilaiFlag == dataEval[index].evaluasiHargaVendor.nilaiTotal) {
                    dataEval[index].flag = 1;
                    var id = dataEval[index].evaluasiHargaVendor.vendor.id;
                    angular.forEach(dataNilai,function(value,index){
                        if(dataNilai[index].penawaranPertama.suratPenawaran.vendor.id == id)
                            dataNilai[index].flag = 1;
                    });
                } else {
                    dataEval[index].flag = 0;
                    var id = dataEval[index].evaluasiHargaVendor.vendor.id;
                    angular.forEach(dataNilai,function(value,index){
                        if(dataNilai[index].penawaranPertama.suratPenawaran.vendor.id == id)
                            dataNilai[index].flag = 0;
                    });
                }
            });
			
			$scope.evaluasiHargaVendorList = dataEval;
			$rootScope.listSaveDataEval = $scope.evaluasiHargaVendorList;
			//console.log("HASIL PENAWARAN AUCTION DETAIL = " + JSON.stringify($scope.evaluasiHargaVendorList));
			
		}
		/* -------------------------------- END Perhitungan Evaluasi Harga ------------------------------ */
        
        
        /* ================================ START HISTORY PENAWARAN MODAL =============================== */
        $scope.clickHistoryPenawaran = function(vendorId, itemPengadaanId) {
            $rootScope.vendorId = vendorId;
            $rootScope.itemPengadaanId = itemPengadaanId;
            $scope.viewHistoryPenawaran();
        }
		
		$scope.viewHistoryPenawaran = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/history_penawaran_harga.html',
                controller: ModalInstanceCtrl,
                size: size,
            });
        }
		
		var ModalInstanceCtrl = function ($http, $scope, $rootScope, $modalInstance, ngTableParams) {
            
            // look up ke tabel Penawaran Auction
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByVendorAndItemPengadaan/' + $rootScope.vendorId + '/' + $rootScope.itemPengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                $scope.loading = false;
               
                if(data.length > 0) {
                    $scope.penawaranAuctionByVendor = data;
                    $scope.kodeVendor = data[0].penawaranAuction.pesertaAuction.vendor.id;
                    $scope.namaVendor = data[0].penawaranAuction.pesertaAuction.vendor.nama;
                    $scope.alamatVendor = data[0].penawaranAuction.pesertaAuction.vendor.alamat;
                    $scope.telVendor = data[0].penawaranAuction.pesertaAuction.vendor.nomorTelpon;
                } else {
					// look up ke table Penawaran Pertama
					$scope.penawaranAuctionByVendor = [];
					var dataPP = $rootScope.listDataPenyedia;
					angular.forEach(dataPP,function(value,index){
						if(dataPP[index].penawaranPertama.suratPenawaran.vendor.id == $rootScope.vendorId) {
							$scope.penawaranAuctionByVendor.push(dataPP[index]);
							$scope.kodeVendor = dataPP[index].penawaranPertama.suratPenawaran.vendor.id;
							$scope.namaVendor = dataPP[index].penawaranPertama.suratPenawaran.vendor.nama;
							$scope.alamatVendor = dataPP[index].penawaranPertama.suratPenawaran.vendor.alamat;
							$scope.telVendor = dataPP[index].penawaranPertama.suratPenawaran.vendor.nomorTelpon;
						}
					});
                }
				
				// paging untuk Penawaran List
				$scope.penawaranAuctionByVendorList = new ngTableParams({
					page: 1,
					count: 4
				}, {
					total: $scope.penawaranAuctionByVendor.length,
					getData: function($defer, params) {
						$defer.resolve($scope.penawaranAuctionByVendor.slice((params.page() - 1) * params.count(), params.page() * params.count()))
					}
				});
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
                alert("DATA ERORR");
            });
            
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        
        ModalInstanceCtrl.$inject = ['$http', '$scope', '$rootScope', '$modalInstance', 'ngTableParams'];
		/* -------------------------------- END HISTORY PENAWARAN MODAL --------------------------------- */
           
        
        /* ================================ START CONTROLLER DETAIL ===================================== */
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
        
		// fungsi hitung Total Nilai
        var hitungTotalNilai = function(presen, nilai){
            var hasil = ((nilai.nilaiAdmin * presen.presentasiNilaiAdmin) / 100) + ((nilai.nilaiTeknis * presen.presentasiNilaiTeknis) / 100) + ((nilai.nilaiHarga * presen.presentasiNilaiHarga) / 100);
            return hasil;
        }
		
        // tombol kembali ke add.html
        $scope.back = function() {
            $scope.go('/app/promise/procurement/evaluasiharga/satuan/auction/view');
        }
    
        // fungsi target lokasi
        $scope.go = function(path) {
            $location.path(path);
        }
		
		// tombol lihat history auction
        $scope.historyAuction = function() {
            if($rootScope.sesiAuctionDetail.length > 0){
                $scope.go('/app/promise/procurement/evaluasiharga/satuan/historyauction');
            } else {
                if($scope.viewAja == 1) {
                    alert('Maaf, Item ini belum pernah melalukan sesi Auction');
                    $scope.back();
                } else {
                    alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                    $scope.newSesiAuction();
                }
            }
        }
		
		// tombol lihat diskualifikasi harga
        $scope.diskualifikasi = function() {
            if($rootScope.sesiAuctionDetail.length > 0){
                $scope.go('/app/promise/procurement/evaluasiharga/satuan/diskualifikasi');
            } else {
                alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                $scope.newSesiAuction();
            }
        }
		
		// tombol sesi auction baru
        $scope.newSesiAuction = function() {
            if($rootScope.sesiAuctionDetail.length > 0) {
                var dataAuction = $rootScope.sesiAuctionDetail
                var listSesiStatus = [];
                angular.forEach(dataAuction, function(value, index) {
                    listSesiStatus.push(dataAuction[index].status);
                });
                var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                //console.log("sortSesiStatus = " + sortSesiStatus);
                if(sortSesiStatus == 1) {
                    $scope.go('/app/promise/procurement/evaluasiharga/satuan/sesiauction');
                } else {
                    var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, Apakah ingin menutup sesi sebelumnya?');
                    if(jawaban){
                        toaster.pop('error', 'SALAHKAN IMPLEMENTOR', 'Fungsi ini belum BISA, harap menghubungi IMPLEMENTOR PROMise terdekat......TERIMA KASIH');
                        
                        // Jika RULES Berubah, ketika buat sesi baru harus menutup sesi sebelumnya, maka buka
                        // Remark di bawah ini
                        // TODO: Change all Opened Auction Status with Integer 1
                        /*updateDataAuction();*/
                    } else {
                        $scope.go('/app/promise/procurement/evaluasiharga/satuan/historyauction');
                    }
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/satuan/sesiauction');
            }
        }
		
		// simpan evaluasi
		$scope.saveData = function() {
			if($rootScope.sesiAuctionDetail.length > 0) {
				var listSesiStatus = [];
                angular.forEach($rootScope.sesiAuctionDetail, function(value, index) {
                    listSesiStatus.push($rootScope.sesiAuctionDetail[index].status);
                });
				
				var sortSesiStatus = sortNilai(listSesiStatus, 'asc');
                //console.log("sortSesiStatus = " + sortSesiStatus);
				
				if(sortSesiStatus == 1) {
                    simpanDataEvalHarga();
                    $scope.back();
                } else {
                    var jawaban = confirm('Masih ada Sesi sebelumnya yang berlangsung!, apakah anda yakin akan munutup sesi dan melanjutkan ke tahapan berikutnya??');
                    if(jawaban){
                        simpanDataEvalHarga();
						updateDataAuction($rootScope.sesiAuctionDetail);// Change all Opened Auction Status with Integer 1
                        $scope.back();
                    } else {
                        $scope.go('/app/promise/procurement/evaluasiharga/satuan/historyauction');
                    }
                }
			}
			else {
                alert('Sesi auction belum ada, silahkan buat terlebih dahulu');
                $scope.newSesiAuction();
            }
		}
		
		// fungsi simpan data Evaluasi Harga
        var simpanDataEvalHarga = function() {
			//console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify($scope.evaluasiHargaVendorList));
            var data = $scope.evaluasiHargaVendorList;
			
			angular.forEach(data,function(value,index){
                // simpan
                var dataEvaluasiHargaVendor = {
                    id: data[index].evaluasiHargaVendor.id,
                    nilaiAdmin: data[index].evaluasiHargaVendor.nilaiAdmin,
                    nilaiTeknis: data[index].evaluasiHargaVendor.nilaiTeknis,
                    nilaiHarga: data[index].evaluasiHargaVendor.nilaiHarga,
                    nilaiTotal: data[index].evaluasiHargaVendor.nilaiTotal,
                    vendor: data[index].evaluasiHargaVendor.vendor.id,
                    isCalonPemenang: data[index].flag,
                    isValid: data[index].evaluasiHargaVendor.isValid,
                    suratPenawaran: data[index].evaluasiHargaVendor.suratPenawaran.id
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function(obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataEvaluasiHargaVendor
                }).success(function(data, status, headers, config) {
                    //console.log("UPDATE EVALUASI HARGA OK : " + JSON.stringify(data));
                }).error(function(data, status, headers, config) {
                    $scope.loading = true;
                    alert("DATA ERORR UNTUK MENYIMPAN!!");
                });
            });
		}
		
		// fungsi update data Auction
        var updateDataAuction = function(data) {
            for(var i=0; i<data.length; i++) {
                if(data[i].status == 0){
                    //console.log("HASIL YANG AKAN DISIMPAN = " + JSON.stringify(data));
                    var dataSesiAuction = {
                        id: data[i].id,
                        noSesi: data[i].noSesi,
                        waktuAwal: new Date(data[i].waktuAwal),
                        waktuAkhir: new Date(data[i].waktuAkhir),
                        keterangan: data[i].keterangan,
                        hargaAwal: data[i].hargaAwal,
                        selisihPenawaran: data[i].selisihPenawaran,
                        pengadaan: data[i].pengadaan.id,
						itemPengadaan: data[i].itemPengadaan.id,
                        status: 1
                    }
        
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
                        //console.log("UPDATE AUCTION SESSION OK : " + JSON.stringify(data));
                    }).error(function(data, status, headers, config) {
                        $scope.loading = true;
                        alert("DATA ERORR UNTUK MENYIMPAN!!");
                    });
                }
            }
        }
        /* -------------------------------- END Rincian Controller -------------------------------------- */    
    
    }
    
    EvaluasiHargaSatuanAuctionAddViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();