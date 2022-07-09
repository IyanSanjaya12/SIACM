/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembukaanPenawaranTotalController', PembukaanPenawaranTotalController);

    function PembukaanPenawaranTotalController(ModalService, $http, $scope, $rootScope, $resource, $location, $modal, $q, $timeout, ngTableParams, toaster) {
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        $scope.pengadaanId = $rootScope.ppEdit.pengadaan.id;
        $scope.inputKursList = [];
        var pptc = this;
        pptc.pengadaanId = $rootScope.ppEdit.pengadaan.id;
        pptc.namaPengadaan = $rootScope.ppEdit.pengadaan.namaPengadaan;
        pptc.jenisPengadaan = $rootScope.ppEdit.pengadaan.jenisPengadaan;
        pptc.metodePengadaan = $rootScope.ppEdit.pengadaan.metodePengadaan;
        pptc.kualifikasiPengadaan = $rootScope.ppEdit.pengadaan.kualifikasiPengadaan;
        pptc.metodePenawaranHarga = $rootScope.ppEdit.pengadaan.metodePenawaranHarga;
        pptc.sistemEvaluasiPenawaran = $rootScope.ppEdit.pengadaan.sistemEvaluasiPenawaran;
        pptc.mataUang = $rootScope.ppEdit.pengadaan.mataUang;
        pptc.kualifikasiVendor = $rootScope.ppEdit.pengadaan.kualifikasiVendor;
        if ((typeof $rootScope.ppEdit.pengadaan.tahapanPengadaan === 'undefined') || $rootScope.ppEdit.pengadaan.tahapanPengadaan == null) {
            pptc.tahapanPengadaan = 0;
        } else {
            pptc.tahapanPengadaan = $rootScope.ppEdit.pengadaan.tahapanPengadaan.tahapan.id;
        }
        pptc.pembukaanPenawaranId = $rootScope.ppEdit.id;

        $scope.btnSimpanStatus = true;
        //Comment supaya Button Tambah Panitia muncul
        /*if (pptc.tahapanPengadaan != 10 ) {
            $scope.btnSimpanStatus = false;
        }*/
        //gettahapannext
        $scope.getTahapanNext = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + pptc.pengadaanId)
                .success(function (data, status, headers, config) {
                    pptc.tahapanPengadaanNext = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };
        $scope.getTahapanNext();
        
         $scope.btnsimpanhilang = true;

        //list Daftar Hadir Vendor
        function loadDaftarHadir() {
            pptc.daftarHadirVendorList = new ngTableParams();
            $scope.daftarHadirVendorList = [];
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getWithDeviasiByPengadaan/' + pptc.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.loading = true;
                    $scope.daftarHadirVendorList = data;
                    if ($scope.daftarHadirVendorList.length > 0) {
                        for (var i = 0; i < $scope.daftarHadirVendorList.length; i++) {
                            if ((i + 1) == $scope.daftarHadirVendorList.length) {
                                pptc.daftarHadirVendorList = new ngTableParams({
                                    page: 1,
                                    count: 10
                                }, {
                                    total: $scope.daftarHadirVendorList.length,
                                    getData: function ($defer, params) {
                                        $defer.resolve($scope.daftarHadirVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                    }
                                });
                            }
                        }
                        $scope.loading = false;
                    }
                    
                   
                    $scope.validasiSimpan = function () {
                        $scope.btnsimpanhilang = true;
                    	//lepas validasi
                        //if ($scope.checkedKehadiranVendor.length == $scope.daftarHadirVendorList.length) {

                            ModalService.showModalConfirmation().then(function (result) {
                                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + pptc.pengadaanId).success(function (data, status, headers, config) {
                                    var nodeKurs = false;
                                    angular.forEach(data, function (value, indeks) {
                                            if (value.mataUang.id != 1) {
                                                nodeKurs = true;
                                            }
                                        })
                                        //console.log($rootScope.nodeKurs);
                                    if (nodeKurs) {
                                        //console.log('flag1');
                                        pptc.btnSimpan();
                                        //$scope.btnsimpanhilang = false;
                                    } else {
                                        $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + pptc.pengadaanId)
                                            .success(function (kursPengadaan, status, headers, config) {
                                                if (kursPengadaan.length > 0) {
                                                    pptc.btnSimpan();                                                                                                    
                                                } else {
                                                    if (pptc.kehadiranPanitiaList.length > 0) {
                                                        console.log('flag2');
                                                        $scope.loading = true;
                                                        $http({
                                                                method: 'POST',
                                                                url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/insertKursPengadaan',
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
                                                                    pengadaanId: pptc.pengadaanId,
                                                                    mataUangId: 1,
                                                                    nilai: 1
                                                                }
                                                            })
                                                            .success(function (data, status, headers, config) {
                                                                $scope.loading = false;
                                                                console.log("All Rupiah berhasil");
                                                                pptc.btnSimpan();
                                                                


                                                            })
                                                            .error(function (data, status, headers, config) {
                                                                $scope.loading = false;
                                                            });
                                                    } else {
                                                        toaster.pop('warning', 'Kesalahan', 'Absensi Panitia belum ada !!');
                                                    }
                                                }
                                            })
                                    }
                                })
                            });
                        //} else {
                        //    toaster.pop('warning', 'Kesalahan', 'Masih ada daftar Vendor yang belum melakukan konfirmasi Pembukaan Penawaran !!');
                        //}
                    }
                    
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.btnsimpanhilang = true;
                });
        }
        loadDaftarHadir();

        //absen Vendor
        $scope.checkedKehadiranVendor = [];
        $scope.getAbsenVendor = function () {
            $scope.loading = true;

            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirVendorServices/getListByPembukaanPenawaran/' + pptc.pembukaanPenawaranId)
                .success(function (data, status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        $scope.checkedKehadiranVendor[i] = data[i].vendor;
                    }
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getAbsenVendor();
        $scope.getVendorId = function (index) {
            if (typeof $scope.checkedKehadiranVendor[index] !== 'undefined')
                return $scope.checkedKehadiranVendor[index].id;
        }

        $scope.togleCheckKehadiranVendor = function (vendor) {
            if ($scope.checkedKehadiranVendor.indexOf(vendor) === -1) {
                $scope.checkedKehadiranVendor.push(vendor);
                console.log('isi checkedKehadiranVendor= '+JSON.stringify($scope.checkedKehadiranVendor) );
            } else {
                $scope.checkedKehadiranVendor.splice($scope.checkedKehadiranVendor.indexOf(vendor), 1);
                console.log('isi checkedKehadiranVendor sliced= '+JSON.stringify($scope.checkedKehadiranVendor) );
            }
        };

        //absen Panitia
        //select Jabatan
        $scope.getJabatan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
                .success(function (data, status, headers, config) {
                    $scope.optionJabatan = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getJabatan();

        pptc.kehadiranPanitiaList = [];
        $scope.getAbsenPanitia = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirPanitiaServices/getListByPembukaanPenawaran/' + pptc.pembukaanPenawaranId)
                .success(function (data, status, headers, config) {
                    pptc.kehadiranPanitiaList = data;
                    //console.log('getAbsenPanitia '+data.length);
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        $scope.getAbsenPanitia();

        pptc.addHadir = function () {
            pptc.inserted = {
                id: pptc.kehadiranPanitiaList.length + 1,
                isNew: true
            };
            pptc.kehadiranPanitiaList.push(pptc.inserted);
        };
        pptc.removeHadir = function (index) {
            pptc.kehadiranPanitiaList.splice(index, 1);
        };

        //simpan
        pptc.btnSimpan = function () {
            $scope.messageError = "";
            $scope.postFormKehadiranVendor = $scope.checkedKehadiranVendor;
            $scope.getKursPengadaanList = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + pptc.pengadaanId)
                    .success(function (data, status, headers, config) {
                        var validate = true;
                        var message = "";
                        if ($scope.postFormKehadiranVendor.length == 0) {
                            message += "<strong>Data Kehadiran Vendor</strong> belum ada, silahkan check pada Vendor yang menghadiri Pembukaan.<br/>";
                            validate = false;
                        }
                        if (data.length == 0) {
                            message += "<strong>Kurs Mata Uang</strong> belum dimasukan <br/>";
                            validate = false;
                        }
                        if (pptc.kehadiranPanitiaList.length == 0) {
                            message += "<strong>Absensi Panitia</strong> belum ada.<br/>";
                            validate = false;
                        }
                        if (validate) {
                            console.log('info postFormKehadiranVendor : ' + JSON.stringify($scope.postFormKehadiranVendor));
                            console.log('info postFormKehadiranPanitia : ' + JSON.stringify(pptc.kehadiranPanitiaList));
                            $scope.simpanPembukaanPenawaran = function () {
                                $scope.loading = true;

                                var pembukaanPenawaranId = pptc.pembukaanPenawaranId;
                                //insert vendor
                                for (var i = 0; i < $scope.postFormKehadiranVendor.length; i++) {
                                    $scope.loading = true;
                                    $http({
                                            method: 'POST',
                                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirVendorServices/create',
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
                                                checks: 1,
                                                pembukaanPenawaran: pembukaanPenawaranId,
                                                vendor: $scope.postFormKehadiranVendor[i].id
                                            }
                                        })
                                        .success(function (data, status, headers, config) {
                                            $scope.loading = false;
                                         
                                        });
                                }
                                //insert panitia
                                for (var i = 0; i < pptc.kehadiranPanitiaList.length; i++) {
                                    $scope.loading = true;
                                    $http({
                                            method: 'POST',
                                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirPanitiaServices/create',
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
                                                nama: pptc.kehadiranPanitiaList[i].nama,
                                                pembukaanPenawaran: pembukaanPenawaranId,
                                                jabatan: pptc.kehadiranPanitiaList[i].jabatan
                                            }
                                        })
                                        .success(function (data, status, headers, config) {
                                            //pptc.kehadiranPanitiaList = data;
                                            $scope.getAbsenPanitia();
                                            $scope.loading = false;
                                            $scope.btnsimpanhilang = false;
                                        });
                                }
                                //update pengadaan
                                $scope.updatePengadaan = function () {
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
                                            pengadaanId: pptc.pengadaanId,
                                            tahapanPengadaanId: pptc.tahapanPengadaanNext
                                        }
                                    }).success(function (data, status, headers, config) {
                                        $scope.loading = false;
                                    });
                                };
                                $scope.updatePengadaan();
                                $scope.messageSuccess = "Simpan Pembukaan Penawaran Berhasil";
                                $scope.btnSimpanStatus = false;
                                $scope.btnsimpanhilang = false;

                            }
                            $scope.simpanPembukaanPenawaran();
                        } else {
                            $scope.messageError = message;
                            $scope.btnsimpanhilang = true;
                        }
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                        $scope.btnsimpanhilang = true;
                    });
            };
            $scope.getKursPengadaanList();
        };

        $rootScope.kondisiAllRupiah = function () {
            console.log('flagKurs');
        }

        /*modal Input Kurs*/
        pptc.goKurs = function (size) {
            $rootScope.btnSimpanStatus = $scope.btnSimpanStatus;
            $rootScope.pengadaanId = pptc.pengadaanId;
            var modalInstance = $modal.open({
                templateUrl: '/inputKurs.html',
                controller: ModalInstanceInputKursCtrl,
                size: size
            });
        };

        var ModalInstanceInputKursCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {
            $scope.inputKursList = [];
            $scope.btnSimpanStatus = $rootScope.btnSimpanStatus;

            $scope.ok = function () {
                if (typeof $scope.inputKursList[0].pengadaanId === 'undefined') {
                    $scope.loading = true;

                    var postData = {
                        data: []
                    };
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
                        postData.data.push({
                            pengadaanId: $rootScope.pengadaanId,
                            mataUangId: $scope.inputKursList[i].id,
                            nilai: $scope.inputKursList[i].kurs
                        });
                    }
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/insertKursPengadaanRaw',
                            data: postData
                        })
                        .success(function (data, status, headers, config) {
                            $scope.messageSuccess = "Penyimpanan berhasil";
                            loadDaftarHadir();
                            $scope.btnSimpanStatus = false;
                            $scope.loading = false;
                        })
                        .error(function (data, status, headers, config) {
                            $scope.messageError = "penyimpanan gagal";
                            $scope.loading = false;
                        });
                } else {
                    $scope.loading = true;
                    var postData = {
                        data: []
                    };
                    for (var i = 0; i < $scope.inputKursList.length; i++) {
                        $scope.inputKursList[i]["nilai"] = $scope.inputKursList[i].kurs;
                        postData.data.push($scope.inputKursList[i]);
                    }
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/updateKursPengadaanRaw',
                            data: postData
                        })
                        .success(function (data, status, headers, config) {
                            $scope.messageSuccess = "Penyimpanan berhasil";
                            loadDaftarHadir();
                            $scope.btnSimpanStatus = false;

                            $scope.loading = false;
                        })
                        .error(function (data, status, headers, config) {
                            $scope.messageError = "penyimpanan gagal";

                            $scope.loading = false;
                        });
                }
                //console.log('INFO KURS : ' + JSON.stringify($scope.inputKursList));
                //$modalInstance.close('closed');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.getMataUang = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + pptc.pengadaanId)
                    .success(function (data, status, headers, config) {
                        if (data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                $scope.inputKursList[i] = {
                                    kursPengadaanId: data[i].id,
                                    pengadaanId: data[i].pengadaan.id,
                                    mataUangId: data[i].mataUang.id,
                                    nama: data[i].mataUang.nama,
                                    kode: data[i].mataUang.kode,
                                    kurs: data[i].nilai
                                };
                            }
                            $scope.loading = false;
                        } else {
                            $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
                                .success(function (masterKurs, status, headers, config) {
                                    $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/' + pptc.pengadaanId)
                                        .success(function (data, status, headers, config) {
                                            angular.forEach(masterKurs, function (value, indeks) {
                                                for (var i = 0; i < data.length; i++) {
                                                    if (value.id == data[i].mataUang.id) {
                                                        var tempObj = {
                                                            id: value.id,
                                                            nama: value.nama,
                                                            kode: value.kode,
                                                            kurs: value.kurs
                                                        };
                                                        $scope.inputKursList.push(tempObj);
                                                        break;
                                                    }
                                                }
                                            });
                                        });

                                    $scope.loading = false;
                                });
                        }
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            }
            $scope.getMataUang();

        };
        ModalInstanceInputKursCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/

        //view dokumen
        $scope.viewDocument = function (suratPenawaran) {
            $rootScope.suratPenawaranSelected = suratPenawaran;
            var modalInstance = $modal.open({
                templateUrl: '/view_document.html',
                controller: ModalInstanceViewDocumentCtrl,
                size: 'lg'
            });
        }
        var ModalInstanceViewDocumentCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, $window) {
            //list file
            console.info($rootScope.suratPenawaranSelected);
            //load Dokumen Admin
            $scope.loadingDokAdmin = false;
            $scope.loadDokAdmin = function () {
                $scope.loadingDokAdmin = true;
                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/getListByVendorAndPengadaan/' + $rootScope.suratPenawaranSelected.vendor.id + '/' + $rootScope.suratPenawaranSelected.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.fileAdminList = data;
                        $scope.loadingDokAdmin = false;
                    })
                    .error(function (data) {
                        $scope.loadingDokAdmin = false;
                    });
            };
            $scope.loadDokAdmin();

            //load Dokumen Teknis
            $scope.loadingDokTeknis = false;
            $scope.loadDokTeknis = function () {
                $scope.loadingDokTeknis = true;
                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/getListByVendorAndPengadaan/' + $rootScope.suratPenawaranSelected.vendor.id + '/' + $rootScope.suratPenawaranSelected.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.fileTeknisList = data;
                        $scope.loadingDokTeknis = false;
                    })
                    .error(function (data) {
                        $scope.loadingDokTeknis = false;
                    });
            };
            $scope.loadDokTeknis();

            //load Dokumen Data Penawaran
            $scope.loadingDokDataPenawaran = false;
            $scope.loadDokDataPenawaran = function () {
                $scope.loadingDokDataPenawaran = true;
                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/getListByVendorAndPengadaan/' + $rootScope.suratPenawaranSelected.vendor.id + '/' + $rootScope.suratPenawaranSelected.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.fileDataPenawaranList = data;
                        $scope.loadingDokDataPenawaran = false;
                    })
                    .error(function (data) {
                        $scope.loadingDokDataPenawaran = false;
                    });
            };
            $scope.loadDokDataPenawaran();

            $scope.downloadFile = function (file) {
                var url = $rootScope.viewUploadBackendAddress + '/' + file;
                console.info(url);
                $window.open(url, '_blank');
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        };
        ModalInstanceViewDocumentCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', '$window'];

        pptc.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/pembukaaanPenawaran');
                }
            } else {
                $location.path('/app/promise/procurement/pembukaaanPenawaran');
            }

        };

        $scope.printDiv = function (divName) {
            $( ".loading" ).remove();  
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

    }

    PembukaanPenawaranTotalController.$inject = ['ModalService', '$http', '$scope', '$rootScope', '$resource', '$location', '$modal', '$q', '$timeout', 'ngTableParams', 'toaster'];
})();