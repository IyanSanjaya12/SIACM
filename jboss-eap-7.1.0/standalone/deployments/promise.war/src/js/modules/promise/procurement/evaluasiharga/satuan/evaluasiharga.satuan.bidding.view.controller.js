/**=========================================================
 * Module: EvaluasiHargaSatuanBiddingViewController.js
 * Author: F.H.K
 =========================================================*/


(function() {
    'use strict';

    angular.module('naut').controller('EvaluasiHargaSatuanBiddingViewController', EvaluasiHargaSatuanBiddingViewController);

    function EvaluasiHargaSatuanBiddingViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $rootScope.dataPenyediaListForSave = [];
        $rootScope.nilaiTerbaik = [];
        
        var hasil = {};
        var hitung = 0;
        var nilaiHarga = 0;
        var nilaiTotal = 0;
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
        
        
        /* ======================== START Data Kriteria Ambang Batas ==================================== */
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            $rootScope.ambangBatas = data;
            $scope.material(data);
            $scope.loading = false;
        }).error(function(data, status, headers, config) {
            $scope.loading = true;
        });
        /* ------------------------ END Rincian Data Kriteria Ambang Batas ------------------------------ */
        
              
        /* ======================== START Rincian Kebutuhan Material ==================================== */
        $scope.material = function(ambangBatas) {
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            angular.forEach(data,function(value,index){
                hitungBarangJasa(data[index], ambangBatas);
            });
            
            $scope.jasa(ambangBatas);
            
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
        }
        /* ------------------------ END Rincian Kebutuhan Material -------------------------------------- */
        

        /* ======================== START Rincian Kebutuhan Jasa ======================================== */
        $scope.jasa = function(ambangBatas) {
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            angular.forEach(data,function(value,index){
                hitungBarangJasa(data[index], ambangBatas);
            });
            
            
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
        }
        /* ------------------------ END Rincian Kebutuhan Jasa ------------------------------------------ */
        
        
        /* ======================= START Data Penyedia Barang/Jasa Detail =============================== */  
        var hitungBarangJasa = function(dataPengadaan, ambangBatas) {
            
            // Look-up ke table Penawaran Pertama
             $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByPengadaanAndItem/' + $scope.pengadaanId + '/' + dataPengadaan.item.id, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                var totalHpsBarangJasa = dataPengadaan.totalHPS;
                $scope.listHarga = [];
            
                if(ambangBatas.length > 0){ // Menggunakan Ambang Batas
                    var itungBatasAtas = totalHpsBarangJasa + ((totalHpsBarangJasa * ambangBatas[0].atas)/100);
                    var itungBatasBawah = totalHpsBarangJasa - ((totalHpsBarangJasa * ambangBatas[0].bawah)/100);
                    
                    angular.forEach(data,function(value,index){
                        if(totalHpsBarangJasa < data[index].hargaTotalOri) {
                            if(data[index].hargaTotalOri < itungBatasAtas && data[index].hargaTotalOri > itungBatasBawah){
                                hasil = {hasil: 'Memenuhi'};
                                $scope.listHarga.push(data[index].hargaTotalOri);
                                angular.extend(data[index], hasil);
                            } else {
                                hasil = {hasil: 'Tidak Memenuhi'};
                                angular.extend(data[index], hasil);
                            }
                        } else {
                            if(data[index].hargaTotalOri < itungBatasAtas && data[index].hargaTotalOri > itungBatasBawah) {
                                hasil = {hasil: 'Memenuhi'};
                                $scope.listHarga.push(data[index].hargaTotalOri);
                                angular.extend(data[index], hasil);
                            } else {
                                hasil = {hasil: 'Tidak Memenuhi'};
                                angular.extend(data[index], hasil);
                            }
                        }
                    });
                } else { // Tidak Menggunakan Ambang Batas
                    angular.forEach(data,function(value,index){
                        if(totalHpsBarangJasa < data[index].hargaTotalOri) {
                            hasil = {hasil: 'Tidak Memenuhi'};
                            $scope.listHarga.push(data[index].hargaTotalOri);
                            angular.extend(data[index], hasil);
                        } else {
                            hasil = {hasil: 'Memenuhi'};
                            angular.extend(data[index], hasil);
                        }
                    });
                }
                $rootScope.nilaiTerbaik.push(sortNilai($scope.listHarga, 'asc'));
                $rootScope.dataPenyediaListForSave.push(data);
                
                $scope.loading = false;
                //console.log("Hasil dataPenyediaList = " + JSON.stringify($rootScope.dataPenyediaList));
            }).error(function(data, status, headers, config) {
                $scope.loading = true;
            });
        }
        /* ----------------------- END Rincian Data Penyedia Barang/Jasa -------------------------------- */
        
        
        /* ======================== START HISTORY PENAWARAN MODAL ======================================= */
        $scope.clickDetailNilai = function(data) {
            $rootScope.itemId = data.item.id;
            $scope.viewHistoryPenawaran('lg');
        }
        
        $scope.viewHistoryPenawaran = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/view_detail_nilai_bidding.html',
                controller: ModalInstanceCtrl,
                size: size,
            });
        };
        
        var ModalInstanceCtrl = function ($http, $scope, $rootScope, $modalInstance, ngTableParams, toaster) {
            
            var data = $rootScope.dataPenyediaListForSave;
            var itemId = $rootScope.itemId;
            $scope.dataPenyediaList = [];
            
            for(var i=0; i<data.length; i++){
                var dataPerVendor = data[i];
                for(var j=0; j<dataPerVendor.length; j++){
                    if(dataPerVendor[j].itemPengadaan.item.id == itemId) {
                        $scope.dataPenyediaList.push(dataPerVendor[j]);
                    }
                }
            }
            
            
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }
        ModalInstanceCtrl.$inject = ['$http', '$scope', '$rootScope', '$modalInstance', 'ngTableParams', 'toaster'];
        /* ------------------------ END HISTORY PENAWARAN MODAL ----------------------------------------- */
        
        
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
        
        // tombol kembali ke index.html
        $scope.back = function() {
             if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $scope.go('/app/promise/procurement/evaluasiharga/bidding');
                }
            } else {
                $scope.go('/app/promise/procurement/evaluasiharga/bidding');
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
        
        // simpan nilai vendor
        $scope.saveData = function() {           
            if($rootScope.dataPenyediaList != undefined) {
                var data = $rootScope.dataPenyediaListForSave;
                var nilaiTerbaikList = $rootScope.nilaiTerbaik;
                var hargaTerbaik = 0;
                //console.log("LIST NILAI TERBAIK = "+nilaiTerbaikList);
            
                for(var i=0; i<data.length; i++){
                    var dataPerVendor = data[i];
                
                    for(var j=0; j<dataPerVendor.length; j++){
                        for(var k=0; k<nilaiTerbaikList.length; k++){
                            if(nilaiTerbaikList[k] == dataPerVendor[j].hargaTotalOri){
                                hargaTerbaik = dataPerVendor[j].hargaTotalOri;
                            }
                        }
                    }
                
                    for(var j=0; j<dataPerVendor.length; j++){
                        //console.log("DATA KE - "+i+" => "+j+" = "+dataPerVendor[j].hasil);
                        var dataLemparan = [{
                            itemPengadaanId: dataPerVendor[j].itemPengadaan.id,
                            vendorId: dataPerVendor[j].penawaranPertama.suratPenawaran.vendor.id,
                            hasil: dataPerVendor[j].hasil,
                            nilaiPenawaranOri: dataPerVendor[j].nilaiPenawaranOri,
                            hargaTerbaik: hargaTerbaik
                        }];
                        cariEvaluasiDetail(dataLemparan);
                    }
                }
                lanjutTahapan();
                $scope.back;
            } else {
                alert("DATA ERROR/EMPTY");
            }
        }
        
        var cariEvaluasiDetail = function(dataLemparan) {
            $rootScope.dataAwal = dataLemparan;
            
            angular.forEach(dataLemparan,function(value,index){
                var dataL = dataLemparan[index];
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + dataL.itemPengadaanId, {
                    ignoreLoadingBar: true
                }).success(function(data, status, headers, config) {
                    $scope.loading = false;
                    
                    for(var i=0; i<data.length; i++){
                        var vendorIdData = data[i].evaluasiHargaVendor.vendor.id;
                        var itemPIdData = data[i].itemPengadaan.id;
                        if(dataL.itemPengadaanId == itemPIdData && dataL.vendorId == vendorIdData){
                            simpanDataDetail(data[i], dataL, i);
                        }
                    }              
                }).error(function(data, status, headers, config) {
                    $scope.loading = true;
                });
            });            
        }
        
        var simpanDataDetail = function(data, dataL, index) {
            var nilaiAdmin = data.evaluasiHargaVendor.nilaiAdmin;
            var nilaiTeknis = data.evaluasiHargaVendor.nilaiTeknis;
            
            if(dataL.hasil == 'Memenuhi') {
                //console.log("SISTEM EVALUASI = "+$scope.sistemEvaluasiPenawaranId);
                if($scope.sistemEvaluasiPenawaranId == 1){ // Sistem Gugur
                    nilaiHarga = 100;
                    nilaiTotal = (parseFloat(nilaiAdmin) + parseFloat(nilaiTeknis) + nilaiHarga)/3
                } else { // Merit Point
                    if(dataL.hargaTerbaik == dataL.nilaiPenawaranOri)
                        nilaiHarga = 100;
                    else
                        nilaiHarga = (dataL.hargaTerbaik / dataL.nilaiPenawaranOri) * 100;
                        nilaiTotal = ((nilaiAdmin * $rootScope.ambangBatas[0].presentasiNilaiAdmin) / 100) + ((nilaiTeknis * $rootScope.ambangBatas[0].presentasiNilaiTeknis) / 100) + ((nilaiHarga * $rootScope.ambangBatas[0].presentasiNilaiHarga) / 100);
                }
            } else {
                nilaiHarga = 0;
                nilaiTotal = 0;
            }
            
            
            // simpan ke Evaluasi HargaVendor
            var dataEvaluasiHargaVendor = {
                id: data.evaluasiHargaVendor.id,
                nilaiAdmin: nilaiAdmin,
                nilaiTeknis: nilaiTeknis,
                nilaiHarga: nilaiHarga,
                nilaiTotal: nilaiTotal,
                updated: new Date(),
                vendor: data.evaluasiHargaVendor.vendor.id,
                suratPenawaran: data.evaluasiHargaVendor.suratPenawaran.id
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
            }).success(function(data, status, headers, config) {});
        }
		
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
                    $location.path('/app/promise/procurement/evaluasiharga/bidding');
				});
			}
        /* --------------------------- END Rincian Controller -------------------------------------------- */
        
    }
    
    EvaluasiHargaSatuanBiddingViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();