(function () {
    'use strict';

    angular
        .module('naut')
        .controller('InisialisasiPengadaanBumnTambahController', InisialisasiPengadaanBumnTambahController)
        .filter('propsFilter', propsFilter);

    function InisialisasiPengadaanBumnTambahController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader, $q) {
        var vm = this;
        $scope.loading = true;
        vm.jenisPengadaan = $rootScope.inisialisasiForm.jenisPengadaan;
        vm.metodaPengadaan = $rootScope.inisialisasiForm.metodaPengadaan;
        vm.kualifikasiPenyediaBarangJasa = $rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa;
        //console.log('info : InisialisasiForm '+JSON.stringify($rootScope.inisialisasiForm));
        //Tab 0
        vm.izinPrinsipOnClick = function () {
                //console.log('INFO Izin :' + vm.izinPrinsipStatus);
                if (vm.izinPrinsipStatus == true) {
                    vm.izinPrinsipFormStatus = true;
                } else {
                    vm.izinPrinsipFormStatus = false;
                }
            }
            //nomorNotaPengadaan
        if (typeof $rootScope.inisialisasiForm.nomorNotaPengadaan !== 'undefined') {
            vm.nomorNotaPengadaan = $rootScope.inisialisasiForm.nomorNotaPengadaan;
        }
        $scope.updateNomorNotaPengadaan = function () {
            if (typeof vm.nomorNotaPengadaan !== 'undefined') {
                $rootScope.inisialisasiForm.nomorNotaPengadaan = vm.nomorNotaPengadaan;
            }
        };

        //namaPengadaan
        if (typeof $rootScope.inisialisasiForm.namaPengadaan !== 'undefined') {
            vm.namaPengadaan = $rootScope.inisialisasiForm.namaPengadaan;
        }
        $scope.updateNamaPengadaan = function () {
            if (typeof vm.namaPengadaan !== 'undefined') {
                $rootScope.inisialisasiForm.namaPengadaan = vm.namaPengadaan;
            }
        };

        //nomorIzinPrinsip		
        if (typeof $rootScope.inisialisasiForm.nomorIzinPrinsip !== 'undefined') {
            vm.nomorIzinPrinsip = $rootScope.inisialisasiForm.nomorIzinPrinsip;
        }
        $scope.updateNomorIzinPrinsip = function () {
            if (typeof vm.nomorIzinPrinsip !== 'undefined') {
                $rootScope.inisialisasiForm.nomorIzinPrinsip = vm.nomorIzinPrinsip;
            }
        };

        //status izin prinsip
        if (typeof $rootScope.inisialisasiForm.izinPrinsipStatus !== 'undefined') {
            vm.izinPrinsipStatus = $rootScope.inisialisasiForm.izinPrinsipStatus;
            //console.log('INFO Izin XX:' + vm.izinPrinsipStatus);
            if (vm.izinPrinsipStatus == true) {
                vm.izinPrinsipFormStatus = true;
            } else {
                vm.izinPrinsipFormStatus = false;
            }
        }
        $scope.updateIzinPrinsipStatus = function () {
            if (typeof vm.izinPrinsipStatus !== 'undefined') {
                $rootScope.inisialisasiForm.izinPrinsipStatus = vm.izinPrinsipStatus;
            }
        };
        vm.izinPrinsipOnClick = function () {
            //console.log('INFO Izin :' + vm.izinPrinsipStatus);
            if (vm.izinPrinsipStatus == true) {
                vm.izinPrinsipFormStatus = true;
            } else {
                vm.izinPrinsipFormStatus = false;
            }
        }

        // Datepicker
        vm.clear = function () {
            vm.tanggalDokumen = null;
            vm.tanggalIzinPrinsip = null;
        };
        vm.disabled = function (date, mode) {
            return false; // ( mode === 'day' && ( date.getDay() === 0 ||
            // date.getDay() === 6 ) );
        };
        vm.toggleMin = function () {
            vm.minDate = vm.minDate ? null : new Date();
        };
        vm.toggleMin();
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        vm.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        vm.format = vm.formats[4];

        vm.dokumenTanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.dokumenTanggalOpened = true;
        };
        if (typeof $rootScope.inisialisasiForm.dokumenTanggal !== 'undefined') {
            vm.dokumenTanggal = $rootScope.inisialisasiForm.dokumenTanggal;
        }
        $scope.updateDokumenTanggal = function () {
            if (typeof vm.dokumenTanggal !== 'undefined') {
                $rootScope.inisialisasiForm.dokumenTanggal = $filter('date')(vm.dokumenTanggal, 'dd-MM-yyyy');
            }
        };

        vm.izinPrinsipTanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.izinPrinsipTanggalOpened = true;
        };
        if (typeof $rootScope.inisialisasiForm.tanggalIzinPrinsip !== 'undefined') {
            vm.tanggalIzinPrinsip = $rootScope.inisialisasiForm.tanggalIzinPrinsip;
        }
        $scope.updateTanggalIzinPrinsip = function () {
            if (typeof vm.tanggalIzinPrinsip !== 'undefined') {
                $rootScope.inisialisasiForm.tanggalIzinPrinsip = $filter('date')(vm.tanggalIzinPrinsip, 'dd-MM-yyyy');
            }
        };
        //PPK		
        if (typeof $rootScope.inisialisasiForm.ppk !== 'undefined') {
            vm.ppk = $rootScope.inisialisasiForm.ppk;
        }
        $scope.updatePPK = function () {
            if (typeof vm.ppk !== 'undefined') {
                $rootScope.inisialisasiForm.ppk = vm.ppk;
            }
        };
        //PP		
        if (typeof $rootScope.inisialisasiForm.pp !== 'undefined') {
            vm.pp = $rootScope.inisialisasiForm.pp;
        }
        $scope.updatePP = function () {
            if (typeof vm.pp !== 'undefined') {
                $rootScope.inisialisasiForm.pp = vm.pp;
            }
        };
        $scope.getTab01Validation = function () {
                var validationTab01 = false;
                //nomorNotaPengadaan
                if (typeof vm.nomorNotaPengadaan === 'undefined' || vm.nomorNotaPengadaan == ' ') {
                    vm.nomorNotaPengadaanError = true;
                } else {
                    vm.nomorNotaPengadaanError = false;
                    //dokumenTanggal
                    if (typeof vm.dokumenTanggal === 'undefined' || vm.dokumenTanggal == ' ') {
                        vm.dokumenTanggalError = true;
                    } else {
                        vm.dokumenTanggalError = false;
                        //namaPengadaan
                        if (typeof vm.namaPengadaan === 'undefined' || vm.namaPengadaan == ' ') {
                            vm.namaPengadaanError = true;
                        } else {
                            vm.namaPengadaanError = false;
                            //ppk
                            if (typeof vm.ppk === 'undefined' || vm.ppk == ' ') {
                                vm.ppkError = true;
                            } else {
                                vm.ppkError = false;
                                validationTab01 = true;
                            }
                        }
                    }
                }
                return validationTab01;
            }
            //Tab 1

        $scope.metodePenyampaianDokumen = {};
        $http.get($rootScope.backendAddress + '/procurement/master/metodePenyampaianDokumenServices/listMetodePenyampaianDokumenPengadaan')
            .success(function (data, status, headers, config) {
                $scope.metodePenyampaianDokumenList = data;
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.metodePenyampaianDokumen !== 'undefined') {
            $scope.metodePenyampaianDokumen.selected = {
                id: $rootScope.inisialisasiForm.metodePenyampaianDokumen.id,
                nama: $rootScope.inisialisasiForm.metodePenyampaianDokumen.nama
            };
        }
        $scope.updateMetodePenyampaianDokumen = function () {
            if (typeof $scope.metodePenyampaianDokumen.selected !== 'undefined') {
                $rootScope.inisialisasiForm.metodePenyampaianDokumen = $scope.metodePenyampaianDokumen.selected;
            }
            $scope.getAlurPengadaanList();
        };

        $scope.metodePenawaranHarga = {};
        $http.get($rootScope.backendAddress + '/procurement/master/MetodePenawaranHargaServices/getMetodePenawaranHargaList').success(
            function (data, status, headers, config) {
                var dataValidate = [];
                for (var i = 0; i < data.length; i++) {
                    if (vm.metodaPengadaan == 1) { //lelang
                        if (data[i].id == 2 || data[i].id == 3) { //auction terbuka & tertutup
                            dataValidate.push(data[i]);
                        }
                    } else if (vm.metodaPengadaan == 5) { //penunjukan langsung
                        if (data[i].id == 1) { //biding
                            dataValidate.push(data[i]);
                            break;
                        }
                    }
                }
                $scope.metodePenawaranHargaList = dataValidate;
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.metodePenawaranHarga !== 'undefined') {
            $scope.metodePenawaranHarga.selected = {
                id: $rootScope.inisialisasiForm.metodePenawaranHarga.id,
                nama: $rootScope.inisialisasiForm.metodePenawaranHarga.nama
            };
        }
        $scope.updateMetodePenawaranHarga = function () {
            if (typeof $scope.metodePenawaranHarga.selected !== 'undefined') {
                $rootScope.inisialisasiForm.metodePenawaranHarga = $scope.metodePenawaranHarga.selected;
            }
        };

        $scope.jenisPenawaran = {};
        $http.get($rootScope.backendAddress + '/procurement/master/JenisPenawaranServices/getJenisPenawaranList')
            .success(function (data, status, headers, config) {
                $scope.jenisPenawaranList = data;
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.jenisPenawaran !== 'undefined') {
            $scope.jenisPenawaran.selected = {
                id: $rootScope.inisialisasiForm.jenisPenawaran.id,
                nama: $rootScope.inisialisasiForm.jenisPenawaran.nama
            };
        }
        $scope.updateJenisPenawaran = function () {
            if (typeof $scope.jenisPenawaran.selected !== 'undefined') {
                $rootScope.inisialisasiForm.jenisPenawaran = $scope.jenisPenawaran.selected;
            }
        };

        $scope.sistemEvaluasiPenawaran = {};
        $http.get($rootScope.backendAddress + '/procurement/master/SistemEvaluasiPenawaranServices/getSistemEvaluasiPenawaranList').success(
            function (data, status, headers, config) {
                $scope.sistemEvaluasiPenawaranList = data;
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.sistemEvaluasiPenawaran !== 'undefined') {
            $scope.sistemEvaluasiPenawaran.selected = {
                id: $rootScope.inisialisasiForm.sistemEvaluasiPenawaran.id,
                nama: $rootScope.inisialisasiForm.sistemEvaluasiPenawaran.nama
            };
        }
        $scope.updateSistemEvaluasiPenawaran = function () {
            if (typeof $scope.metodePenawaranHarga.selected !== 'undefined') {
                $rootScope.inisialisasiForm.sistemEvaluasiPenawaran = $scope.sistemEvaluasiPenawaran.selected;
            }
        };
        $scope.alurPengadaanList = [];
        $scope.kondisiPengadaan = {};
        $scope.getAlurPengadaanList = function () {
            $scope.loading = true;
            if (typeof $rootScope.inisialisasiForm.jenisPengadaan !== 'undefined')
                $scope.kondisiPengadaan.jenisPengadaan = $rootScope.inisialisasiForm.jenisPengadaan;

            if (typeof $rootScope.inisialisasiForm.metodaPengadaan !== 'undefined')
                $scope.kondisiPengadaan.metodePengadaan = $rootScope.inisialisasiForm.metodaPengadaan;

            if (typeof $scope.metodePenyampaianDokumen.selected !== 'undefined')
                $scope.kondisiPengadaan.metodePenyampaianDokumenPengadaan = $scope.metodePenyampaianDokumen.selected.id;

            if (typeof $scope.metodePenawaranHarga.selected !== 'undefined')
                $scope.kondisiPengadaan.metodePenawaranHarga = $scope.metodePenawaranHarga.selected.id;

            if (typeof $scope.jenisPenawaran.selected !== 'undefined')
                $scope.kondisiPengadaan.jenisPenawaran = $scope.jenisPenawaran.selected.id;

            if (typeof $scope.sistemEvaluasiPenawaran.selected !== 'undefined')
                $scope.kondisiPengadaan.sistemEvaluasiPenawaran = $scope.sistemEvaluasiPenawaran.selected.id;

            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/AlurPengadaanServices/getListByKondisiPengadaan',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: $scope.kondisiPengadaan
                }).success(function (data, status, headers, config) {
                    $scope.alurPengadaanList = data;
                    $scope.loading = false;
                })
                .error(function () {
                    $scope.loading = false;
                });
        };
        $scope.getAlurPengadaanList();
        $scope.pilihAlur = function (alur) {
            console.log(">>> " + JSON.stringify(alur));
            if (typeof alur.kondisiPengadaan.metodePenyampaianDokumenPengadaan !== 'undefined' && alur.kondisiPengadaan.metodePenyampaianDokumenPengadaan !== null) {
                $scope.metodePenyampaianDokumen.selected = {
                    id: alur.kondisiPengadaan.metodePenyampaianDokumenPengadaan.id,
                    nama: alur.kondisiPengadaan.metodePenyampaianDokumenPengadaan.nama
                };
                if (typeof $scope.metodePenyampaianDokumen.selected !== 'undefined') {
                    $rootScope.inisialisasiForm.metodePenyampaianDokumen = $scope.metodePenyampaianDokumen.selected;
                }
            }
            if (typeof alur.kondisiPengadaan.metodePenawaranHarga !== 'undefined') {
                $scope.metodePenawaranHarga.selected = {
                    id: alur.kondisiPengadaan.metodePenawaranHarga.id,
                    nama: alur.kondisiPengadaan.metodePenawaranHarga.nama
                };
                if (typeof $scope.metodePenawaranHarga.selected !== 'undefined') {
                    $rootScope.inisialisasiForm.metodePenawaranHarga = $scope.metodePenawaranHarga.selected;
                }
            }
            if (typeof alur.kondisiPengadaan.jenisPenawaran !== 'undefined') {
                $scope.jenisPenawaran.selected = {
                    id: alur.kondisiPengadaan.jenisPenawaran.id,
                    nama: alur.kondisiPengadaan.jenisPenawaran.nama
                };
                if (typeof $scope.jenisPenawaran.selected !== 'undefined') {
                    $rootScope.inisialisasiForm.jenisPenawaran = $scope.jenisPenawaran.selected;
                }
            }
            if (typeof alur.kondisiPengadaan.sistemEvaluasiPenawaran !== 'undefined') {
                $scope.sistemEvaluasiPenawaran.selected = {
                    id: alur.kondisiPengadaan.sistemEvaluasiPenawaran.id,
                    nama: alur.kondisiPengadaan.sistemEvaluasiPenawaran.nama
                };
                if (typeof $scope.metodePenawaranHarga.selected !== 'undefined') {
                    $rootScope.inisialisasiForm.sistemEvaluasiPenawaran = $scope.sistemEvaluasiPenawaran.selected;
                }
            }
            $rootScope.inisialisasiForm.alurPengadaan = alur.id;
        }
        $scope.getTab02Validation = function () {
            var validationTab02 = false;
            if (typeof $scope.metodePenyampaianDokumen.selected === 'undefined') {
                vm.metodePenyampaianDokumenError = true;
            } else {
                vm.metodePenyampaianDokumenError = false;
                if (typeof $scope.metodePenawaranHarga.selected === 'undefined') {
                    vm.metodePenawaranHargaError = true;
                } else {
                    vm.metodePenawaranHargaError = false;
                    if (typeof $scope.jenisPenawaran.selected === 'undefined') {
                        vm.jenisPenawaranError = true;
                    } else {
                        vm.jenisPenawaranError = false;
                        if (typeof $scope.sistemEvaluasiPenawaran.selected === 'undefined') {
                            vm.sistemEvaluasiPenawaranError = true;
                        } else {
                            if (typeof $rootScope.inisialisasiForm.alurPengadaan === 'undefined') {
                                vm.alurPengadaanError = true;
                            } else {
                                vm.alurPengadaanError = false;
                                validationTab02 = true;
                            }
                        }

                    }
                }
            }
            return validationTab02;
        }


        //Tab 2
        //HPS Form
        $scope.pilihPerencanaan = {};
        $scope.pilihPerencanaanList = [{
            id: 1,
            nama: "Tanpa Perencanaan"
		}, {
            id: 2,
            nama: "Dengan Perencanaan"
		}];
        $scope.hideProses = true;
        $scope.hidePerencanaan = true;
        if (typeof $rootScope.HPSForm.pilihPerencanaan !== 'undefined') {
            $scope.pilihPerencanaan.selected = {
                id: $rootScope.HPSForm.pilihPerencanaan.id,
                nama: $rootScope.HPSForm.pilihPerencanaan.nama
            };
        }
        $scope.updatePilihPerencanaan = function () {
            if (typeof $scope.pilihPerencanaan.selected !== 'undefined') {
                $rootScope.HPSForm.pilihPerencanaan = $scope.pilihPerencanaan.selected;
            }
            //console.log("cek >> " + JSON.stringify($scope.pilihPerencanaan));
            if ($scope.pilihPerencanaan.selected.id == 2) { //Dengan Perencanaan
                $scope.hideProses = true;
                $scope.hidePerencanaan = false;
            } else {
                $scope.hideProses = false;
                $scope.hidePerencanaan = true;
            }
        };

        $scope.tambahBarangJasa = {};
        $scope.tambahBarangJasaList = [{
            id: 1,
            nama: "Tambah Barang"
		}, {
            id: 2,
            nama: "Tambah Jasa"
		}];
        if (typeof $rootScope.HPSForm.tambahBarangJasa !== 'undefined') {
            $scope.tambahBarangJasa.selected = {
                id: $rootScope.HPSForm.tambahBarangJasa.id,
                nama: $rootScope.HPSForm.tambahBarangJasa.nama
            };
        }
        $scope.updateTambahBarangJasa = function () {
            if (typeof $scope.tambahBarangJasa.selected !== 'undefined') {
                $rootScope.HPSForm.tambahBarangJasa = $scope.tambahBarangJasa.selected;
            }
        };

        //generate Form Barang/Jasa
        vm.prosesTambahBarangJasa = function () {
                var validationProses = false;
                if (typeof $scope.pilihPerencanaan.selected === 'undefined') {
                    vm.pilihPerencanaanError = true;
                } else {
                    vm.pilihPerencanaanError = false;
                    if (typeof $scope.tambahBarangJasa.selected === 'undefined') {
                        vm.tambahBarangJasaError = true;
                    } else {
                        vm.tambahBarangJasaError = false;
                        validationProses = true;
                    }
                }
                if (validationProses) {
                    vm.tambahJasaFormStatus = false;
                    vm.tambahMaterialFormStatus = false;
                    if ($scope.tambahBarangJasa.selected.id == 1 && $scope.pilihPerencanaan.selected.id == 1) {
                        vm.tambahMaterialFormStatus = true;
                        vm.tambahJasaFormStatus = false;
                    }
                    if ($scope.tambahBarangJasa.selected.id == 2 && $scope.pilihPerencanaan.selected.id == 1) {
                        vm.tambahJasaFormStatus = true;
                        vm.tambahMaterialFormStatus = false;
                    }
                    $rootScope.HPSForm.tambahJasaFormStatus = vm.tambahJasaFormStatus;
                    $rootScope.HPSForm.tambahMaterialFormStatus = vm.tambahMaterialFormStatus;
                }
            }
            //MaterialForm
        if (typeof $rootScope.HPSForm.tambahMaterialFormStatus === 'undefined') {
            $rootScope.HPSForm.tambahMaterialFormStatus = vm.tambahMaterialFormStatus;
        } else {
            vm.tambahMaterialFormStatus = $rootScope.HPSForm.tambahMaterialFormStatus;
        }
        //Jasa Form
        if (typeof $rootScope.HPSForm.tambahJasaFormStatus === 'undefined') {
            $rootScope.HPSForm.tambahJasaFormStatus = vm.tambahJasaFormStatus;
        } else {
            vm.tambahJasaFormStatus = $rootScope.HPSForm.tambahJasaFormStatus;
        }

        //Pajak
        if (typeof $rootScope.inisialisasiForm.prosentasePajakMaterial === 'undefined') {
            vm.prosentasePajakMaterial = 10;
            $rootScope.inisialisasiForm.prosentasePajakMaterial = vm.prosentasePajakMaterial;
        } else {
            vm.prosentasePajakMaterial = $rootScope.inisialisasiForm.prosentasePajakMaterial;
        }
        $scope.updateJumlahMaterilaNilaiHPSPPN = function () {
            if (vm.prosentasePajakMaterial < 0)
                vm.prosentasePajakMaterial = 0;

            $rootScope.inisialisasiForm.prosentasePajakMaterial = vm.prosentasePajakMaterial;
            if (typeof vm.jumlahMaterialNilaiHPSPPN !== 'undefined') {
                vm.jumlahMaterialNilaiHPSPPN = vm.jumlahMaterialNilaiHPS * (vm.prosentasePajakMaterial / 100);
                vm.jumlahMaterialNilaiHPSDenganPPN = vm.jumlahMaterialNilaiHPS + vm.jumlahMaterialNilaiHPSPPN;
            }
        }

        //Pajak
        if (typeof $rootScope.inisialisasiForm.prosentasePajakJasa === 'undefined') {
            vm.prosentasePajakJasa = 10;
            $rootScope.inisialisasiForm.prosentasePajakJasa = vm.prosentasePajakJasa;
        } else {
            vm.prosentasePajakJasa = $rootScope.inisialisasiForm.prosentasePajakJasa;
        }
        $scope.updateJumlahJasaNilaiHPSPPN = function () {
                if (vm.prosentasePajakJasa < 0)
                    vm.prosentasePajakJasa = 0;

                $rootScope.inisialisasiForm.prosentasePajakJasa = vm.prosentasePajakJasa;
                if (typeof vm.jumlahJasaNilaiHPSPPN !== 'undefined') {
                    vm.jumlahJasaNilaiHPSPPN = vm.jumlahJasaNilaiHPS * (vm.prosentasePajakJasa / 100);
                    vm.jumlahJasaNilaiHPSDenganPPN = vm.jumlahJasaNilaiHPS + vm.jumlahJasaNilaiHPSPPN;
                }
            }
            //Material
        $scope.autoLoadMaterial = function () {
            if (typeof $rootScope.inisialisasiForm.materialList === 'undefined') {
                $rootScope.inisialisasiForm.materialList = [];
                vm.materialList = [];
            } else {
                vm.materialList = $rootScope.inisialisasiForm.materialList;
                console.log(">> materialList : " + JSON.stringify(vm.materialList));
                vm.jumlahMaterialNilaiHPS = 0;
                if ($rootScope.inisialisasiForm.jenisPenawaran.id === 1) {
                    for (var i = 0; i < vm.materialList.length; i++) {
                        vm.jumlahMaterialNilaiHPS = vm.jumlahMaterialNilaiHPS + (vm.materialList[i].kuantitas * vm.materialList[i].hps);
                        vm.materialList[i]["nomor"] = i + 1;
                    }
                    vm.jumlahMaterialNilaiHPSPPN = vm.jumlahMaterialNilaiHPS * (vm.prosentasePajakMaterial / 100);
                    vm.jumlahMaterialNilaiHPSDenganPPN = vm.jumlahMaterialNilaiHPS + vm.jumlahMaterialNilaiHPSPPN;
                }

                //List Material		
                vm.tableMaterialList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.materialList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.materialList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }
            if (vm.materialList.length > 0)
                vm.tambahMaterialFormStatus = true;
            console.log(">>load material new :" + JSON.stringify(vm.materialList));
        };
        $scope.autoLoadMaterial();

        //Jasa
        $scope.autoLoadJasa = function () {
            if (typeof $rootScope.inisialisasiForm.jasaList === 'undefined') {
                $rootScope.inisialisasiForm.jasaList = [];
                vm.jasaList = [];
            } else {
                vm.jasaList = $rootScope.inisialisasiForm.jasaList;
                vm.jumlahJasaNilaiHPS = 0;
                if ($rootScope.inisialisasiForm.jenisPenawaran.id === 1) {
                    for (var i = 0; i < vm.jasaList.length; i++) {
                        vm.jumlahJasaNilaiHPS = vm.jumlahJasaNilaiHPS + (vm.jasaList[i].kuantitas * vm.jasaList[i].hps);
                        vm.jasaList[i]["nomor"] = i + 1;
                    }
                    vm.jumlahJasaNilaiHPSPPN = vm.jumlahJasaNilaiHPS * (vm.prosentasePajakJasa / 100);
                    vm.jumlahJasaNilaiHPSDenganPPN = vm.jumlahJasaNilaiHPS + vm.jumlahJasaNilaiHPSPPN;
                }
                //List Material		
                vm.tableJasaList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.jasaList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.jasaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }
            if (vm.jasaList.length > 0)
                vm.tambahJasaFormStatus = true;
            console.log(">>load jasa new : " + JSON.stringify(vm.jasaList));
        };
        $scope.autoLoadJasa();

        //Pencarian perencanaa
        $scope.getPerencanaanPengadaan = function () {
            var perencanaanModalInstance = $modal.open({
                templateUrl: '/pencarian_perencanaan.html',
                controller: PerencanaanModalInstance,
                size: 'lg'
            });
            perencanaanModalInstance.result.then(function () {
                $scope.getPerencanaanPengadaanDetil($rootScope.pilihPerencanaan);
            }, function () {
            });
        }
        var PerencanaanModalInstance = function ($scope, $modalInstance, $rootScope) {
            $scope.getList = function () {
                $scope.loading = true;
                $rootScope.pilihPerencanaan = {};
                console.log(JSON.stringify($rootScope.userRole));
                $http.get($rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/getList')
                    .success(function (data, status, headers, config) {
                        $scope.perencanaanList = data;
                        //console.log('iNFO : '+JSON.stringify(data ));
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            };
            $scope.getList();
            $scope.pilihPerencanaan = function (perencanaan) {
                $rootScope.pilihPerencanaan = perencanaan;
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        PerencanaanModalInstance.$inject = ['$scope', '$modalInstance', '$rootScope'];

        $scope.getPerencanaanPengadaanDetil = function (perencanaan) {
            $rootScope.importPerencanaan = {};
            var perencanaanModalInstance = $modal.open({
                templateUrl: '/detil_perencanaan.html',
                controller: DetilPerencanaanModalInstance,
                size: 'lg',
                resolve: {
                    perencanaan: function () {
                        return perencanaan;
                    }
                }
            });
            perencanaanModalInstance.result.then(function () {
                $scope.autoLoadMaterial();
                $scope.autoLoadJasa();
            }, function () {
                $scope.getPerencanaanPengadaan();
            });
        };
        
        var DetilPerencanaanModalInstance = function ($scope, $modalInstance, perencanaan, $rootScope) {
            var dpr = this;
            $scope.detilPerencanaan = perencanaan;
            $scope.loading = true;
            //Material
            $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/getListByPerencanaanIdAndItemType/' + $scope.detilPerencanaan.perencanaan.id + '/1')
                .success(function (data, status, headers, config) {
                    dpr.itemPerencanaanList = data;
                    $scope.showMaterial = false;
                    $scope.showJasa = false;

                    if (data.length > 0)
                        $scope.showMaterial = true;

                    $scope.itemPerencanaanList = dpr.itemPerencanaanList;
                    $scope.jumlahTotalHPSMaterial = 0;
                    for (var i = 0; i < $scope.itemPerencanaanList.length; i++) {
                        $scope.itemPerencanaanList[i].index = i + 1;
                        $scope.itemPerencanaanList[i].totalHPS = 0;
                        $scope.itemPerencanaanList[i].totalHPS = $scope.itemPerencanaanList[i].nilai * $scope.itemPerencanaanList[i].jumlah;
                        $scope.jumlahTotalHPSMaterial += $scope.itemPerencanaanList[i].totalHPS;
                        /*
                        if($scope.itemPerencanaanList[i].sisah != $scope.itemPerencanaanList[i].jumlah) {
                            //$scope.btnSimpanPerencanaan = false;
                            //$scope.editDataMaterial = true;
                        }*/
                    }
                    $scope.tableMaterialSession = new ngTableParams({
                        page: 1,
                        count: 5
                    }, {
                        total: $scope.itemPerencanaanList.length,
                        getData: function ($defer, params) {
                            $defer.resolve($scope.itemPerencanaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                        }
                    });
                    $scope.loading = false;
                    //Jasa
                    $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/getListByPerencanaanIdAndItemType/' + $scope.detilPerencanaan.perencanaan.id + '/2').success(
                        function (data, status, headers, config) {
                            if (data.length)
                                $scope.showJasa = true;

                            dpr.jasaPerencanaanList = data;
                            $scope.perencanaanJasa = dpr.jasaPerencanaanList;
                            $scope.jumlahTotalHPSJasa = 0;
                            for (var i = 0; i < $scope.perencanaanJasa.length; i++) {
                                $scope.perencanaanJasa[i].index = i + 1;
                                $scope.perencanaanJasa[i].totalHPS = 0;
                                $scope.perencanaanJasa[i].totalHPS = $scope.perencanaanJasa[i].nilai * $scope.perencanaanJasa[i].jumlah;
                                $scope.jumlahTotalHPSJasa += $scope.perencanaanJasa[i].totalHPS;
                                if ($scope.perencanaanJasa[i].sisa != $scope.perencanaanJasa[i].jumlah) {
                                    //$scope.btnSimpanPerencanaan = false;
                                    $scope.editDataJasa = true;
                                }

                            }
                            $scope.jumlahTotalHps = $scope.jumlahTotalHPSMaterial + $scope.jumlahTotalHPSJasa;
                            //console.log('JUMLAH TOTAL HPS = ' + JSON.stringify($scope.jumlahTotalHps));
                            $scope.tableJasaSession = new ngTableParams({
                                page: 1,
                                count: 5
                            }, {
                                total: $scope.perencanaanJasa.length,
                                getData: function ($defer, params) {
                                    $defer.resolve($scope.perencanaanJasa.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                }
                            });
                            $scope.loading = false;
                        }).error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            
            $scope.btnImport = function () {
                $rootScope.importPerencanaan = perencanaan;
                $rootScope.perencanaanId = $scope.detilPerencanaan.perencanaan.id;
                if (dpr.itemPerencanaanList.length > 0) {
                    var materialList = [];
                    angular.forEach(dpr.itemPerencanaanList, function (value, key) {
                        var materialObject = {
                            material: value.item,
                            kuantitas: value.jumlah,
                            hps: value.nilai,
                            mataUang: value.mataUang,
                            keteranga: '-'
                        };
                        materialList.push(materialObject);
                    });
                    $rootScope.inisialisasiForm.materialList = materialList;
                }
                if (dpr.jasaPerencanaanList.length > 0) {
                    var jasaList = [];
                    angular.forEach(dpr.jasaPerencanaanList, function (value, key) {
                        var jasaObject = {
                            jasa: value.item,
                            kuantitas: value.jumlah,
                            hps: value.nilai,
                            mataUang: value.mataUang,
                            keteranga: '-'
                        };
                        jasaList.push(jasaObject);
                    });
                    $rootScope.inisialisasiForm.jasaList = jasaList;
                }
                $modalInstance.close('closed');
            }
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        DetilPerencanaanModalInstance.$inject = ['$scope', '$modalInstance', 'perencanaan', '$rootScope'];

        //Material Delete
        $scope.removeMaterial = function (obj, size) {
            //console.log('INFO obj : ' + JSON.stringify(obj));
            var modalInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: ModalInstanceCtrl,
                size: size,
                resolve: {
                    materialName: function () {
                        return obj.material.nama;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                var index = $rootScope.inisialisasiForm.materialList.indexOf(obj);
                $rootScope.inisialisasiForm.materialList.splice(index, 1);
                $scope.autoLoadMaterial();
            }, function () { //cancel 
                //console.log('INFO cancel delete material');
            });
        };
        var ModalInstanceCtrl = function ($scope, $modalInstance, materialName) {
            $scope.materialName = materialName;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceCtrl.$inject = ['$scope', '$modalInstance', 'materialName'];

        //Jasa Delete
        $scope.removeJasa = function (obj, size) {
            //console.log('INFO obj : ' + JSON.stringify(obj));
            var modalInstance = $modal.open({
                templateUrl: '/alertTableJasa2.html',
                controller: ModalInstanceJasaCtrl,
                size: size,
                resolve: {
                    jasaName: function () {
                        return obj.nama;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                var index = $rootScope.inisialisasiForm.jasaList.indexOf(obj);
                $rootScope.inisialisasiForm.jasaList.splice(index, 1);
                $scope.autoLoadJasa();
            }, function () { //cancel 
                //console.log('INFO cancel delete material');
            });
        };
        var ModalInstanceJasaCtrl = function ($scope, $modalInstance, jasaName) {
            $scope.jasaName = jasaName;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceJasaCtrl.$inject = ['$scope', '$modalInstance', 'jasaName'];

        //Material Edit
        if (typeof $rootScope.materialEditId !== 'undefined') {
            $rootScope.materialEditId = undefined;
        }
        $scope.editMaterial = function (index) {
            $rootScope.materialEditId = index;
            $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
            $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanmaterialtotal');
            //            if ($rootScope.inisialisasiForm.jenisPenawaran.id === 2) {
            //            	$location.path('/app/promise/procurement/inisialisasi/add/kebutuhanmaterialsatuan');
            //            } else {
            //            	$location.path('/app/promise/procurement/inisialisasi/add/kebutuhanmaterialtotal');
            //            }
        };

        //Jasa Edit
        if (typeof $rootScope.jasaEditId !== 'undefined') {
            $rootScope.jasaEditId = undefined;
        }
        $scope.editJasa = function (index) {
            $rootScope.jasaEditId = index;
            $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanjasatotal');
            } else {
                $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanjasasatuan');
            }
        };

        vm.btnTambahMaterial = function () {
            $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
            $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanmaterialtotal');
        }

        vm.btnTambahJasa = function () {
            $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanjasatotal');
            } else {
                $location.path('/app/promise/procurement/inisialisasi/add/kebutuhanjasasatuan');
            }
        }

        //Tab 3 Vendor
        $scope.kualifikasiVendor = {};
        $http.get($rootScope.backendAddress + '/procurement/master/kualifikasi-vendor/get-list')
            .success(
                function (data, status, headers, config) {
                    $scope.kualifikasiVendorList = data;
                }).error(function (data, status, headers, config) {});

        if (typeof $rootScope.inisialisasiForm.kualifikasiVendor !== 'undefined') {
            $scope.kualifikasiVendor.selected = {
                id: $rootScope.inisialisasiForm.kualifikasiVendor.id,
                nama: $rootScope.inisialisasiForm.kualifikasiVendor.nama
            };
        };
        $scope.updateKualifikasiVendor = function () {
            if (typeof $scope.kualifikasiVendor.selected !== 'undefined') {
                $rootScope.inisialisasiForm.kualifikasiVendor = $scope.kualifikasiVendor.selected;
            }
        };
        //segmentasi bidang/subbidang
        $scope.segmentasiVendorStatusOnChange = function () {
            //console.log('INFO Segmentasi Vendor :' + vm.segmentasiVendorStatus);
            $rootScope.inisialisasiForm.segmentasiVendorStatus = vm.segmentasiVendorStatus;
        }
        if (typeof $rootScope.inisialisasiForm.segmentasiVendorStatus === 'undefined') {
            $rootScope.inisialisasiForm.segmentasiVendorStatus = vm.segmentasiVendorStatus;
        } else {
            vm.segmentasiVendorStatus = $rootScope.inisialisasiForm.segmentasiVendorStatus;
        }

        $scope.autoLoadSubBidang = function () {
            if (typeof $rootScope.inisialisasiForm.subBidangUsahaList === 'undefined') {
                $rootScope.inisialisasiForm.subBidangUsahaList = [];
            } else {
                //console.log('INFO ROOT Sub Bidang : ' + JSON.stringify($rootScope.inisialisasiForm.subBidangUsahaList));
                vm.subBidangUsahaList = $rootScope.inisialisasiForm.subBidangUsahaList;
            }
        }
        $scope.autoLoadSubBidang();

        $scope.btnTambahBidang = function () {
            $rootScope.inisialisasiForm.tabActiveFrom = 'tab3';
            $location.path('/app/promise/procurement/inisialisasi/add/bidang');
        }
        $scope.removeBiddangUsaha = function (bidangObj, size) {
            //console.log('INFO obj : ' + JSON.stringify(bidangObj));
            var modalInstance = $modal.open({
                templateUrl: '/alertTable3.html',
                controller: ModalInstanceCtrl,
                size: size,
                resolve: {
                    materialName: function () {
                        return bidangObj.bidangUsaha.nama + ' - ' + bidangObj.nama;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                var index = $rootScope.inisialisasiForm.subBidangUsahaList.indexOf(bidangObj);
                $rootScope.inisialisasiForm.subBidangUsahaList.splice(index, 1);
                $scope.autoLoadSubBidang();
            }, function () { //cancel 
                //console.log('INFO cancel delete Sub Bidang');
            });
        }

        //Pengaalaman
        $scope.pengalamanVendorStatusOnChange = function () {
            $rootScope.inisialisasiForm.pengalamanVendorStatus = vm.pengalamanVendorStatus;
            if (vm.pengalamanVendorStatus) {
                var d = new Date();
                vm.pengalamanTahunAkhir = parseInt(d.getFullYear());
                vm.pengalamanTahunMulai = vm.pengalamanTahunAkhir - 1;
                if (typeof $rootScope.inisialisasiForm.pengalamanTahunAkhir === 'undefined') {
                    $rootScope.inisialisasiForm.pengalamanTahunAkhir = vm.pengalamanTahunAkhir;
                } else {
                    vm.pengalamanTahunAkhir = $rootScope.inisialisasiForm.pengalamanTahunAkhir;
                }
                if (typeof $rootScope.inisialisasiForm.pengalamanTahunMulai === 'undefined') {
                    $rootScope.inisialisasiForm.pengalamanTahunMulai = vm.pengalamanTahunMulai;
                } else {
                    vm.pengalamanTahunMulai = $rootScope.inisialisasiForm.pengalamanTahunMulai;
                }
            }
        }

        if (typeof $rootScope.inisialisasiForm.pengalamanVendorStatus === 'undefined') {
            $rootScope.inisialisasiForm.pengalamanVendorStatus = vm.pengalamanVendorStatus;
        } else {
            vm.pengalamanVendorStatus = $rootScope.inisialisasiForm.pengalamanVendorStatus;
        }
        //tahun - akhir
        $scope.pengalamanTahunAkhirOnChange = function () {
            $rootScope.inisialisasiForm.pengalamanTahunAkhir = vm.pengalamanTahunAkhir;
        }
        if (typeof $rootScope.inisialisasiForm.pengalamanTahunAkhir === 'undefined') {
            $rootScope.inisialisasiForm.pengalamanTahunAkhir = vm.pengalamanTahunAkhir;
        } else {
            vm.pengalamanTahunAkhir = $rootScope.inisialisasiForm.pengalamanTahunAkhir;
        }
        //tahun - mulai		
        $scope.pengalamanTahunMulaiOnChange = function () {
            $rootScope.inisialisasiForm.pengalamanTahunMulai = vm.pengalamanTahunMulai;
        }
        if (typeof $rootScope.inisialisasiForm.pengalamanTahunMulai === 'undefined') {
            $rootScope.inisialisasiForm.pengalamanTahunMulai = vm.pengalamanTahunMulai;
        } else {
            vm.pengalamanTahunMulai = $rootScope.inisialisasiForm.pengalamanTahunMulai;
        }

        //validasi tahun mulai akhir
        $scope.invalidTahun = function () {
                if (typeof vm.pengalamanTahunMulai !== 'undefined' && typeof vm.pengalamanTahunAkhir !== 'undefined') {
                    if (vm.pengalamanTahunMulai > vm.pengalamanTahunAkhir) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            //jumlahPengalaman
        $scope.jumlahPengalamanVendorOnChange = function () {
            $rootScope.jumlahPengalamanVendor = vm.jumlahPengalamanVendor;
        }
        if (typeof $rootScope.jumlahPengalamanVendor === 'undefined') {
            $rootScope.jumlahPengalamanVendor = vm.jumlahPengalamanVendor;
        } else {
            vm.jumlahPengalamanVendor = $rootScope.jumlahPengalamanVendor;
        }
        //minimalNilaiKontrak
        $scope.minimalNilaiKontrakOnChange = function () {
            $rootScope.minimalNilaiKontrak = vm.minimalNilaiKontrak;
        }
        if (typeof $rootScope.minimalNilaiKontrak === 'undefined') {
            $rootScope.minimalNilaiKontrak = vm.minimalNilaiKontrak;
        } else {
            vm.minimalNilaiKontrak = $rootScope.minimalNilaiKontrak;
        }
        //Currency
        $scope.pengalamanMataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.pengalamanMataUangList = data;
                if ($rootScope.inisialisasiForm.pengalamanMataUang !== undefined) {
                    $scope.pengalamanMataUang.selected = $rootScope.inisialisasiForm.pengalamanMataUang;
                } else {
                    $scope.pengalamanMataUang.selected = data[0];
                    $rootScope.inisialisasiForm.pengalamanMataUang = data[0];
                }
            }).error(function (data, status, headers, config) {});

        $scope.updatePengalamanMataUang = function () {
            if (typeof $scope.pengalamanMataUang !== 'undefined') {
                $rootScope.inisialisasiForm.pengalamanMataUang = $scope.pengalamanMataUang.selected;
            }
        }

        $scope.getTab03Validation = function () {
            var validationTab03 = false;
            if ($rootScope.inisialisasiForm.materialList.length > 0 || $rootScope.inisialisasiForm.jasaList.length > 0) { //or jasa = 0 (*next
                validationTab03 = true;
            } else {
                vm.materialJasaError = true;
            }
            return validationTab03;
        };

        //Undang Vendor
        $scope.autoLoadVendor = function () {
            if (typeof $rootScope.inisialisasiForm.vendorList === 'undefined') {
                $rootScope.inisialisasiForm.vendorList = [];
            } else {
                vm.vendorList = $rootScope.inisialisasiForm.vendorList;
            }
        }
        $scope.autoLoadVendor();

        $scope.btnUndangVendor = function () {
            $rootScope.inisialisasiForm.tabActiveFrom = 'tab3';
            $location.path('/app/promise/procurement/inisialisasi/add/vendor');
        }
        $scope.removeVendor = function (vendorObj, size) {
            //console.log('INFO obj : ' + JSON.stringify(bidangObj));
            var modalInstance = $modal.open({
                templateUrl: '/alertTable3.html',
                controller: ModalInstanceCtrl,
                size: size,
                resolve: {
                    materialName: function () {
                        return vendorObj.nama + ', ' + vendorObj.alamat;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                var index = $rootScope.inisialisasiForm.vendorList.indexOf(vendorObj);
                $rootScope.inisialisasiForm.vendorList.splice(index, 1);
                $scope.autoLoadVendor();
            }, function () { //cancel 
                //console.log('INFO cancel delete Sub Bidang');
            });
        };

        $scope.getTab04Validation = function () {
            var validationTab04 = false;
            if (vm.metodaPengadaan == 5) {
                if (typeof $rootScope.inisialisasiForm.vendorList === 'undefined' || $rootScope.inisialisasiForm.vendorList.length == 0) {
                    vm.vendorListError = true
                } else {
                    validationTab04 = true;
                }
            } else {
                validationTab04 = true;
            }
            return validationTab04;
        };


        //Tab 4 Uplaod file
        $scope.btnSimpanInisialisasiStatus = true;
        $scope.btnKembaliIndexStatus = false;
        if (typeof $rootScope.inisialisasiForm.uploader === 'undefined') {
            var uploader = $scope.uploader = new FileUploader({
                url: $rootScope.uploadBackendAddress,
                //url: 'http://localhost:8080/promisews/procurement/dokumenUploadPengadaan/uploadFile',
                method: 'POST'
            });
        } else {
            var uploader = $scope.uploader = $rootScope.inisialisasiForm.uploader;
        }
        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });
        // CALLBACKS
        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            //console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            //console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            //console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            //console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            //console.log('INFO File : '+JSON.stringify(response));
            if (typeof $rootScope.inisialisasiForm.fileUpload === 'undefined') {
                $rootScope.inisialisasiForm.fileUpload = [];
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.inisialisasiForm.fileUpload = $rootScope.inisialisasiForm.fileUpload.concat(fileObj);
                $rootScope.inisialisasiForm.uploader = $scope.uploader;
            } else {
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.inisialisasiForm.fileUpload = $rootScope.inisialisasiForm.fileUpload.concat(fileObj);
                $scope.uploader = $rootScope.inisialisasiForm.uploader;
            }
        };
        uploader.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        $scope.insertItemJasaSynchronus = function (index, pengadaanId) {
            var jasaList = $rootScope.inisialisasiForm.jasaList[index];
            var itemDto = {
                nama: jasaList.nama,
                satuanId: 5,
                itemTypeId: 2,
                itemGroupId: 2
            }
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/item/insert-item',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: itemDto
            }).success(function (data, status, headers, config) {
                if (typeof data.id !== 'undefined') {
                    $scope.insertItemPengadaanJasa(data.id, pengadaanId, index);
                    $scope.message = ('Proses penyimpanan item jasa... ' + (((index + 1) / 100) * 100) + '%');
                }
            });
        }

        $scope.insertItemJasa = function (pengadaanId) {
            //insert to item
            for (var i = 0; i < $rootScope.inisialisasiForm.jasaList.length; i++) {
                $scope.insertItemJasaSynchronus(i, pengadaanId);
            }
        }

        $scope.insertItemPengadaanJasa = function (itemId, pengadaanId, index) {
            //jasa
            var jasaList = $rootScope.inisialisasiForm.jasaList[index];
            var total = 0;
            var pajak = 0;
            var jenisPajak;
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                total = jasaList.kuantitas * jasaList.hps;
                pajak = total * 0.1; //pajak ???? fix this
                jenisPajak = null;
            } else {
                total = jasaList.totalHps;
                pajak = total - (jasaList.kuantitas * jasaList.hps);
                jenisPajak = jasaList.jenisPajak.id;
            }
            var formItemPengadaan = {
                pengadaan: pengadaanId,
                item: itemId,
                mataUang: jasaList.mataUang.id,
                kuantitas: jasaList.kuantitas,
                nilaiHPS: jasaList.hps,
                totalHPS: total,
                nilaiPajak: pajak,
                jenisPajak: jenisPajak
            };
            var uri = '';
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanTotal';
            } else {
                uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
            }
            $http({
                method: 'POST',
                url: uri,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: formItemPengadaan
            }).success(function (data, status, headers, config) {
                $scope.message = ('Proses penyimpanan data jasa pengadaan... ' + (((index + 1) / 100) * 100) + '%');
            });
            $scope.message = ('Proses penyimpanan data material&jasa pengadaan... BERHASIL!');
        }

        $scope.insertItemPengadaanMaterial = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data material pengadaan...');
            //material
            for (var i = 0; i < $rootScope.inisialisasiForm.materialList.length; i++) {
                var formItemPengadaan = {
                    pengadaan: pengadaanId,
                    item: $rootScope.inisialisasiForm.materialList[i].material.id,
                    mataUang: $rootScope.inisialisasiForm.materialList[i].mataUang.id,
                    kuantitas: $rootScope.inisialisasiForm.materialList[i].kuantitas,
                    nilaiHPS: $rootScope.inisialisasiForm.materialList[i].hps,
                    totalHPS: ($rootScope.inisialisasiForm.materialList[i].kuantitas * $rootScope.inisialisasiForm.materialList[i].hps)
                };
                var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanTotal';
                if ($rootScope.inisialisasiForm.jenisPenawaran.id === 2) {
                    targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
                    formItemPengadaan.jenisPajak = $rootScope.inisialisasiForm.materialList[i].jenisPajak.id;
                    if ($rootScope.inisialisasiForm.materialList[i].prosentasePajak !== undefined && $rootScope.inisialisasiForm.materialList[i].prosentasePajak !== null && $rootScope.inisialisasiForm.materialList[i].prosentasePajak > 0) {
                        formItemPengadaan.prosentasePajak = $rootScope.inisialisasiForm.materialList[i].prosentasePajak;
                        formItemPengadaan.nilaiPajak = $rootScope.inisialisasiForm.materialList[i].nilaiPajak;
                    }
                }
                $http({
                    method: 'POST',
                    url: targetURI,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: formItemPengadaan
                }).success(function (data, status, headers, config) {
                    $scope.message = ('Proses penyimpanan data material pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                });
            }
        }

        $scope.insertVendorRequirement = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data vendor requirement pengadaan...');
            //vendor requirement
            var formVendorRequirement = {
                pengadaan: pengadaanId,
                tahunPengalamanMulai: $rootScope.inisialisasiForm.pengalamanTahunMulai,
                tahunPengalamanAkhir: $rootScope.inisialisasiForm.pengalamanTahunAkhir,
                mataUang: $rootScope.inisialisasiForm.pengalamanMataUang.id
            };
            if ($rootScope.jumlahPengalamanVendor !== undefined && $rootScope.jumlahPengalamanVendor !== null && $rootScope.jumlahPengalamanVendor > 0) {
                formVendorRequirement.minimalPengalaman = $rootScope.jumlahPengalamanVendor;
            }
            if ($rootScope.minimalNilaiKontrak !== undefined && $rootScope.minimalNilaiKontrak !== null && $rootScope.minimalNilaiKontrak > 0) {
                formVendorRequirement.nilaiKontrak = $rootScope.minimalNilaiKontrak;
            }
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/insertVendorRequirement',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: formVendorRequirement
            }).success(function (data, status, headers, config) {
                $scope.message = ('Proses penyimpanan data vendor requirement pengadaan... BERHASIL');
            });
        }

        $scope.insertBidang = function (pengadaanId) {
            //bidang
            $scope.message = ('Proses penyimpanan data bidang pengadaan...');
            if (typeof $rootScope.inisialisasiForm.subBidangUsahaList !== 'undefined') {
                for (var i = 0; i < $rootScope.inisialisasiForm.subBidangUsahaList.length; i++) {
                    var formBidangPengadaan = {
                        pengadaanId: pengadaanId,
                        subBidangId: $rootScope.inisialisasiForm.subBidangUsahaList[i].id
                    };
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/insertBidangUsahaPengadaan',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formBidangPengadaan
                    }).success(function (data, status, headers, config) {
                        $scope.message = ('Proses penyimpanan data bidang pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                    });
                }
                $scope.message = ('Proses penyimpanan data bidang pengadaan... BERHASIL!');
            }
        }

        $scope.insertVendor = function (pengadaanId) {
            //vendor 
            $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ');
            if (typeof $rootScope.inisialisasiForm.vendorList !== 'undefined') {
                for (var i = 0; i < $rootScope.inisialisasiForm.vendorList.length; i++) {
                    var formVendorUndangan = {
                        pengadaan: pengadaanId,
                        vendor: $rootScope.inisialisasiForm.vendorList[i].id
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/insertUndanganVendor',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formVendorUndangan
                    }).success(function (data, status, headers, config) {
                        $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                    });
                }
                $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... BERHASIL');
            }
        }

        $scope.insertVendorKualifikasi = function (pengadaanId) {
            //vendor 
            $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ');
            if (typeof $rootScope.inisialisasiForm.vendorList !== 'undefined') {
                for (var i = 0; i < $rootScope.inisialisasiForm.vendorList.length; i++) {
                    var formVendorUndangan = {
                        pengadaan: pengadaanId,
                        vendor: $rootScope.inisialisasiForm.vendorList[i].id
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/undanganVendorKualifikasiServices/insertUndanganVendorKualifikasi',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formVendorUndangan
                    }).success(function (data, status, headers, config) {
                        $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                    });
                }
                $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... BERHASIL');
            }
        }

        $scope.insertDokumenUpload = function (pengadaanId) {
            //dokumen upload
            var today = new Date();
            if (typeof $rootScope.inisialisasiForm.fileUpload !== 'undefined') {
                for (var i = 0; i < $rootScope.inisialisasiForm.fileUpload.length; i++) {
                    var formDokumenPengadaan = {
                        pengadaan: pengadaanId,
                        nama: $rootScope.inisialisasiForm.fileUpload[i].fileName.substr(11, $rootScope.inisialisasiForm.fileUpload[i].fileName.length),
                        path: $rootScope.inisialisasiForm.fileUpload[i].fileName,
                        fileSize: 10000
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/insertDokumenPengadaan',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formDokumenPengadaan
                    }).success(function (data, status, headers, config) {
                        $scope.message = ('Proses penyimpanan data dokumen pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                    });
                }
                $scope.message = ('Proses penyimpanan data dokumen pengadaan... BERHASIL');
            }
        }

        /* get Next Tahapan */
        $scope.getNextTahapan = function (pengadaanId) {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.nextTahapan = data;
                    $scope.updateTahapanPengadaan(pengadaanId, data);
                })
                .error(function (data, status, headers, config) {});
        }

        //update pengadaan
        $scope.updateTahapanPengadaan = function (pengadaanId, nextTahapan) {
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
                    pengadaanId: pengadaanId,
                    tahapanPengadaanId: nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };

        $scope.cratePerencanaanPengadaan = function (pengadaanId, perencanaanId) {
            var formPostPerencanaanPengadaan = {
                pengadaan: pengadaanId,
                perencanaan: perencanaanId
            };

            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/inisialisasi/perencanaanpengadaan/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: formPostPerencanaanPengadaan
                })
                .success(function (data, status, headers, config) {
                    if(typeof data !== 'undefined'){
                        console.log("Simpan Relation PR berhasil");
                    } else {
                        console.error("Simpan Relation PR");
                    }
                });
        };

        $scope.insertPengadaan = function () {
            //pengadaan
            var formPostPengadaan = {
                nomorNotaDinas: $rootScope.inisialisasiForm.nomorNotaPengadaan,
                tanggalNotaDinas: $rootScope.inisialisasiForm.dokumenTanggal,
                namaPengadaan: $rootScope.inisialisasiForm.namaPengadaan,
                mataUang: 1,
                kualifikasiPengadaan: $rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa,
                jenisPengadaan: $rootScope.inisialisasiForm.jenisPengadaan,
                metodePengadaan: $rootScope.inisialisasiForm.metodaPengadaan,
                metodePenyampaianDokumen: $rootScope.inisialisasiForm.metodePenyampaianDokumen.id,
                metodePenawaranHarga: $rootScope.inisialisasiForm.metodePenawaranHarga.id,
                jenisPenawaran: $rootScope.inisialisasiForm.jenisPenawaran.id,
                sistemEvaluasiPenawaran: $rootScope.inisialisasiForm.sistemEvaluasiPenawaran.id,
                alurPengadaan: $rootScope.inisialisasiForm.alurPengadaan,
                tahapanPengadaan: 1,
                ppk: $rootScope.inisialisasiForm.ppk
            };
            if ($rootScope.userLogin !== undefined && $rootScope.userLogin !== null) {
                formPostPengadaan.userId = $rootScope.userLogin.user.id;
            }

            if ($rootScope.inisialisasiForm.kualifikasiVendor !== undefined && $rootScope.inisialisasiForm.kualifikasiVendor !== null) {
                formPostPengadaan.kualifikasiVendor = $rootScope.inisialisasiForm.kualifikasiVendor.id;
            }

            if ($rootScope.inisialisasiForm.pp !== undefined) {
                formPostPengadaan.pp = $rootScope.inisialisasiForm.pp;
            }

            if ($rootScope.inisialisasiForm.nomorIzinPrinsip !== undefined) {
                formPostPengadaan.nomorIzinPrinsip = $rootScope.inisialisasiForm.nomorIzinPrinsip;
            }

            if ($rootScope.inisialisasiForm.tanggalIzinPrinsip !== undefined) {
                formPostPengadaan.tanggalIzinPrinsip = $rootScope.inisialisasiForm.tanggalIzinPrinsip;
            }
            if ($rootScope.inisialisasiForm.jenisPenawaran.id === 1) {
                if (vm.prosentasePajakMaterial !== undefined && vm.prosentasePajakMaterial !== null && vm.prosentasePajakMaterial > 0) {
                    formPostPengadaan.prosentasePajakMaterial = vm.prosentasePajakMaterial;
                }
                if (vm.prosentasePajakJasa !== undefined && vm.prosentasePajakJasa !== null && vm.prosentasePajakJasa > 0) {
                    formPostPengadaan.prosentasePajakJasa = vm.prosentasePajakJasa;
                }
            }

            $scope.loading = true;
            $scope.message = ('Proses penyimpanan data pengadaan...');

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/createPengadaan',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: formPostPengadaan
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    var pengadaanId = data.id;
                    $scope.message = ('Proses penyimpanan data pengadaan... BERHASIL');
                    //proses update tahapan-->
                    //$scope.getNextTahapan(pengadaanId);
                    //end of update tahapan-->
                    //proses simpan item material-->
                    $scope.insertItemPengadaanMaterial(pengadaanId);
                    //end of proses simpan item material-->
                    //proses simpan item jasa-->
                    $scope.insertItemJasa(pengadaanId);
                    //end of proses simpan item jasa-->
                    //proses simpan bidang-->
                    if (vm.segmentasiVendorStatus !== undefined && vm.segmentasiVendorStatus !== null && vm.segmentasiVendorStatus === true) {
                        $scope.insertBidang(pengadaanId);
                    }
                    //end of proses simpan bidang-->
                    //proses simpan vendor requirement-->
                    if (vm.pengalamanVendorStatus !== undefined && vm.pengalamanVendorStatus !== null && vm.pengalamanVendorStatus === true) {
                        $scope.insertVendorRequirement(pengadaanId);
                    }
                    //end of proses simpan vendor requirement-->
                    //proses simpan vendor-->
                    //if (vm.metodaPengadaan == 5) { //comment aja, selama ada vendor yang diundang simpan
                    $scope.insertVendor(pengadaanId);
                    if ($rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa == 1) {
                        $scope.insertVendorKualifikasi(pengadaanId);
                    }
                    //}
                    //end of proses simpan vendor-->
                    //proses simpan DokumenUpload-->
                    $scope.insertDokumenUpload(pengadaanId);
                    //end of proses simpan DokumenUpload-->
                    
                    //perencanaan pengadaan
                    if(typeof $rootScope.perencanaanId !=='undefined'){
                        console.log("with PR");
                        $scope.cratePerencanaanPengadaan(pengadaanId, $rootScope.perencanaanId);
                    }
                }
                $scope.loading = false;
                $scope.btnSimpanInisialisasiStatus = false;
                $scope.btnKembaliIndexStatus = true;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.message = "Error simpan pengadaan";
            });
        }

        $scope.btnSimpanInisialisasi = function () {
            $scope.insertPengadaan();
        }

        $scope.btnKembaliIndex = function () {
            $location.path('/app/promise/procurement/inisialisasi');
        }

        //class utils
        //Auto Tab
        $scope.getAutoTab = function () {
            var status = "tab0";
            if (typeof $rootScope.inisialisasiForm.tabActiveFrom !== 'undefined') {
                status = $rootScope.inisialisasiForm.tabActiveFrom;
            }
            //console.log('TAB : '+status);
            return status;
        };
        //URL Link
        vm.go = function (path) {
            $location.path(path);
        };
        $scope.loading = false;
    }

    InisialisasiPengadaanBumnTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader', '$q'];

    function propsFilter() {
        return function (items, props) {
            var out = [];

            if (angular.isArray(items)) {
                items.forEach(function (item) {
                    var itemMatches = false;

                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }

            return out;
        };
    }
})();