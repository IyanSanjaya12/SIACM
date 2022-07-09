(function () {
    'use strict';
    angular
        .module('naut')
        .controller('TotalMeritPointController', TotalMeritPointController);

    function TotalMeritPointController(ModalService, $http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, $filter, ngTableParams,RequestService) {

        var form = this;

        if (typeof $rootScope.pengadaanPenetapanPemenang !== 'undefined') {
            form.detailPengadaan = $rootScope.pengadaanPenetapanPemenang;
        } else {
            console.log('Error');
        }
        $scope.pengadaanId = form.detailPengadaan.id;

        form.checkPemenangVendor = {};

        //jeniskontrak
        form.jenisKontrak = 0; //Normal default
        $http.get($rootScope.backendAddress + '/procurement/master/JenisKontrakServices/findAll')
            .success(function (data) {
                $scope.jenisKontrakList = data;
            })
            .error(function (data) {
                console.error("No data");
            });
        
        $scope.showJenisKontrak = function(){
            console.log("jenis kontrak : "+form.jenisKontrak);
        }

        //get has PR
        $scope.isJoinPR = false;
        $http.get($rootScope.backendAddress + "/procurement/inisialisasi/purchaserequestpengadaan/findByPengadaanId/" + $scope.pengadaanId)
            .success(function (data) {
                if (data.length > 0) {
                    if (data[0].purchaseRequest.isJoin != 0) {
                        $scope.isJoinPR = true;
                    }
                }

            });

        //vendor-surat penawaran
        if (form.detailPengadaan.metodePenawaranHarga.id == 1) { //bidding
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListCalonPemenangByPengadaan/' + $scope.pengadaanId).success(function (negosiasi, status, headers, config) {
                negosiasi = $filter('filter')(negosiasi, {
                    nilaiTotal: ('!' + null)
                });
                angular.forEach(negosiasi, function (value, indeks) {
                    negosiasi[indeks].pengadaanId = $scope.pengadaanId;
                    //nilai Evaluasi
                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id).success(function (nilai, status, headers, config) {
                        if (nilai.length > 0) {
                            negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                            negosiasi[indeks].mataUang = nilai[0].mataUang;
                            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasi[indeks].mataUang.id)
                                .success(function (data) {
                                    negosiasi[indeks].hargaWithKurs = data.nilai * negosiasi[indeks].harga;
                                });
                        } else {
                            //jika data nego tidak ada ambil penawaran pertama
                            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id)
                                .success(function (dataPenawaranPertama) {
                                    negosiasi[indeks].harga = dataPenawaranPertama.nilaiPenawaran;
                                    negosiasi[indeks].mataUang = nilai[0].mataUang;
                                    $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasi[indeks].mataUang.id)
                                        .success(function (data) {
                                            negosiasi[indeks].hargaWithKurs = data.nilai * negosiasi[indeks].harga;
                                        });
                                });
                        }
                    })
                    $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {

                        if (penetapan.length > 0) {
                            $scope.edit = true;
                            $scope.pemenang = penetapan[0].id;
                            negosiasi[indeks].status = true;
                        }
                    });
                    if ((indeks + 1) == negosiasi.length) {
                        form.evaluasiHargaVendor = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: negosiasi.length,
                            getData: function ($defer, params) {
                                $defer.resolve(negosiasi.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    }
                });
            });
        } else { //auction
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (auction, status, headers, config) {
                auction = $filter('filter')(auction, {
                    nilaiTotal: ('!' + null)
                });
                angular.forEach(auction, function (value, indeks) {
                    auction[indeks].pengadaanId = $scope.pengadaanId;
                    //cek dulu ada nilai nego apa tidak
                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id)
                        .success(function (nilai, status, headers, config) {
                            if (nilai.length > 0) {
                                auction[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                                auction[indeks].mataUang = nilai[0].mataUang;
                                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + auction[indeks].mataUang.id)
                                    .success(function (data) {
                                        auction[indeks].hargaWithKurs = data.nilai * auction[indeks].harga;
                                    });
                            } else {
                                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByVendorPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (nilai, status, headers, config) {
                                    if (nilai.length > 0) {
                                        auction[indeks].harga = nilai[0].nilaiPenawaran;
                                        auction[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                                        auction[indeks].mataUang = nilai[0].mataUang;
                                        auction[indeks].hargaWithKurs = data.nilai * auction[indeks].harga;
                                    } else {
                                        auction[indeks].harga = null;
                                    }
                                })
                            }
                        });

                    $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {
                        if (penetapan.length > 0) {
                            $scope.edit = true;
                            $scope.pemenang = penetapan[0].id;
                            auction[indeks].status = true;
                        }
                    })
                    if ((indeks + 1) == auction.length) {
                        form.evaluasiHargaVendor = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: auction.length,
                            getData: function ($defer, params) {
                                $defer.resolve(auction.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    }
                })
            })
        }

        form.pemenangCheck = function (list) {
        	form.checkPemenangVendor = {};
        	form.checkPemenangVendor = list;
        }
        
        //evaluasi harga vendor
        var nilaiEvaluasi = {
            admin: 0,
            teknis: 0,
            harga: 0
        };
        $scope.getEvaluasiHargaVendor = function (vendorId) {
            $scope.loading = true;
            var defered = $q.defer();
            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + vendorId + '/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        //nilaiEvaluasi.metodePenawaranHargaid = data[0].pengadaan.metodePenawaranHarga.id;
                        nilaiEvaluasi.admin = data[0].nilaiEvaluasiAdministrasi;
                        nilaiEvaluasi.teknis = data[0].nilaiEvaluasiTeknis;
                        nilaiEvaluasi.harga = data[0].nilaiEvaluasiHarga;
                        defered.resolve(nilaiEvaluasi);
                    } else {
                        defered.resolve(0);
                    }
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            }, 1000);
            return defered.promise;
        };
        
        form.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/penetapanpemenang');
                }
            } else {
                $location.path('/app/promise/procurement/penetapanpemenang');
            }
        }

        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").remove();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        form.btnSimpan = function () {
            if (form.jenisKontrak == 0) {
                $scope.errorMsg = "Jenis Kontrak Harus dipilih!";
                RequestService.informError($scope.errorMsg);
                return false;
            } else if (typeof form.checkPemenangVendor.vendor === 'undefined') {
                $scope.errorMsg = "Pemenang harus dipilih!";
                RequestService.informError($scope.errorMsg);
                return false;
            } else {
                $scope.loading = true;
                $scope.saveData();
            }
        };
        
        $scope.saveData = function() {
        	RequestService.modalConfirmation().then(function(result) {
            	var penetapanPemenangTotalDTO = {
            			vendorId: form.checkPemenangVendor.vendor.id,
                        pengadaanId: $scope.pengadaanId,
                        jenisKontrakId: form.jenisKontrak
                }
                if ($scope.edit) {
                	penetapanPemenangTotalDTO.penetapanPemenangTotalId= $scope.pemenang;
                }
            	if ($scope.isPublish == true) {
            		penetapanPemenangTotalDTO.pengumumanPengadaan={         
                            isiPengumuman: form.detailPengadaan.namaPengadaan
                    }
            	}
            	form.url = '/procurement/penetapanpemenangtotal/insert';
            	RequestService.doPOSTJSON(form.url,penetapanPemenangTotalDTO)
				.then(function success(data) {
					toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
		            $timeout(function () {
		                $scope.loading = false;
		                $location.path('/app/promise/procurement/penetapanpemenang');
		            }, 2000);
		        
				}, function error(response) {
					$log.info("proses gagal");
					 toaster.pop('error', 'GAGAL DISIMPAN', 'Terjadi Kesalahan Pada System');
				});
            	
            });
        }
        $scope.change = function() {
        	$scope.isPublish = !$scope.isPublish;
        }

    }
    TotalMeritPointController.$inject = ['ModalService', '$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', '$filter', 'ngTableParams','RequestService'];
})();