(function () {
    'use strict';

    angular
        .module('naut')
        .controller('InisialisasiPengadaanSMBTambahController', InisialisasiPengadaanSMBTambahController)
        .filter('propsFilter', propsFilter);

    function InisialisasiPengadaanSMBTambahController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader, $q) {
        var vm = this;
        $scope.loading = true;
        vm.jenisPengadaan = $rootScope.inisialisasiForm.jenisPengadaan;
        vm.metodaPengadaan = $rootScope.inisialisasiForm.metodePengadaan;
        
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
                            validationTab01 = true;

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
        };

        $scope.flowPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/master/flowPengadaanServices/getListByKategori/' + $rootScope.inisialisasiForm.kategoriPengadaan.id)
            .success(function (data, status, headers, config) {
                $scope.flowPengadaanList = data;
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.flowPengadaan !== 'undefined') {
            $scope.flowPengadaan.selected = {
                id: $rootScope.inisialisasiForm.flowPengadaan.id,
                nama: $rootScope.inisialisasiForm.flowPengadaan.nama
            };
        }
        $scope.updateFlowPengadaan = function () {
            if (typeof $scope.flowPengadaan.selected !== 'undefined') {
                $rootScope.inisialisasiForm.flowPengadaan = $scope.flowPengadaan.selected;
            }
        };

        $scope.metodePenawaranHarga = {};
        $http.get($rootScope.backendAddress + '/procurement/master/MetodePenawaranHargaServices/getMetodePenawaranHargaList').success(
            function (data, status, headers, config) {
                $scope.metodePenawaranHargaList = data;
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
        $scope.getTab02Validation = function () {
            var validationTab02 = false;
            if (typeof $scope.flowPengadaan.selected === 'undefined') {
                vm.flowPengadaanError = true;
            } else {
                vm.flowPengadaanError = false;
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
                            vm.sistemEvaluasiPenawaranError = false;
                            validationTab02 = true;
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
                vm.jumlahMaterialNilaiHPS = 0;
                for (var i = 0; i < vm.materialList.length; i++) {
                    vm.jumlahMaterialNilaiHPS = vm.jumlahMaterialNilaiHPS + (vm.materialList[i].kuantitas * vm.materialList[i].hps);
                    vm.materialList[i]["nomor"] = i + 1;
                }
                vm.jumlahMaterialNilaiHPSPPN = vm.jumlahMaterialNilaiHPS * (vm.prosentasePajakMaterial / 100);
                vm.jumlahMaterialNilaiHPSDenganPPN = vm.jumlahMaterialNilaiHPS + vm.jumlahMaterialNilaiHPSPPN;
                //console.log('INFO materialList length:'+ vm.materialList.length);
                //console.log('INFO materialList :'+JSON.stringify(vm.materialList));
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
                for (var i = 0; i < vm.jasaList.length; i++) {
                    vm.jumlahJasaNilaiHPS = vm.jumlahJasaNilaiHPS + (vm.jasaList[i].kuantitas * vm.jasaList[i].hps);
                    vm.jasaList[i]["nomor"] = i + 1;
                }
                vm.jumlahJasaNilaiHPSPPN = vm.jumlahJasaNilaiHPS * (vm.prosentasePajakJasa / 100);
                vm.jumlahJasaNilaiHPSDenganPPN = vm.jumlahJasaNilaiHPS + vm.jumlahJasaNilaiHPSPPN;
                //console.log('INFO materialList length:'+ vm.materialList.length);
                //console.log('INFO materialList :'+JSON.stringify(vm.materialList));
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
        };
        $scope.autoLoadJasa();

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
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $rootScope.materialEditId = index;
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanmaterialtotal');
            } else {
                $rootScope.materialEditId = index;
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanmaterialsatuan');
            }
        };

        //Jasa Edit
        if (typeof $rootScope.jasaEditId !== 'undefined') {
            $rootScope.jasaEditId = undefined;
        }
        $scope.editJasa = function (index) {
            $rootScope.jasaEditId = index;
            $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanjasatotal');
            } else {
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanjasasatuan');
            }
        };

        vm.btnTambahMaterial = function () {
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanmaterialtotal');
            } else {
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanmaterialsatuan');
            }
        }

        vm.btnTambahJasa = function () {
            if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanjasatotal');
            } else {
                $rootScope.inisialisasiForm.tabActiveFrom = "tab2";
                $location.path('/app/promise/procurement/inisialisasi/add/smb/kebutuhanjasasatuan');
            }
        }

        //Tab 3 Vendor
        $scope.kualifikasiVendor = {};
        $http.get($rootScope.backendAddress + '/procurement/master/kualifikasi-vendor/get-list').success(
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
            $location.path('/app/promise/procurement/inisialisasi/add/smb/bidang');
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
                if (data.length > 0) {
                    $scope.pengalamanMataUang.selected = {
                        id: data[0].id,
                        nama: data[0].nama
                    };
                    $rootScope.inisialisasiForm.pengalamanMataUang = $scope.pengalamanMataUang.selected;
                }
            }).error(function (data, status, headers, config) {});
        if (typeof $rootScope.inisialisasiForm.pengalamanMataUang !== 'undefined') {
            //console.log('INFO : '+JSON.stringify($rootScope.inisialisasiForm.pengalamanMataUang));
            $scope.pengalamanMataUang.selected = {
                id: $rootScope.inisialisasiForm.pengalamanMataUang.id,
                nama: $rootScope.inisialisasiForm.pengalamanMataUang.nama
            };
        } else {

        }
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

        $scope.getTab04Validation = function () {
            var validationTab04 = false;
            if (vm.metodaPengadaan.id == 5) {
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
            $location.path('/app/promise/procurement/inisialisasi/add/smb/vendor');
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
            var prosentasePajak = 0;
            if (typeof jasaList.prosentasePajak !== 'undefined') {
                prosentasePajak = jasaList.prosentasePajak;
            }
            var formItemPengadaan = {
                pengadaan: pengadaanId,
                item: itemId,
                mataUang: jasaList.mataUang.id,
                kuantitas: jasaList.kuantitas,
                nilaiHPS: jasaList.hps,
                totalHPS: total,
                nilaiPajak: pajak,
                jenisPajak: jenisPajak,
                prosentasePajak: prosentasePajak
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
                var total = 0;
                var pajak = 0;
                var jenisPajak;
                if ($rootScope.inisialisasiForm.jenisPenawaran.id == 1) {
                    total = $rootScope.inisialisasiForm.materialList[i].kuantitas * $rootScope.inisialisasiForm.materialList[i].hps;
                    pajak = total * 0.1; //pajak ???? fix this
                    jenisPajak = null;
                } else {
                    total = $rootScope.inisialisasiForm.materialList[i].totalHps;
                    pajak = total - ($rootScope.inisialisasiForm.materialList[i].kuantitas * $rootScope.inisialisasiForm.materialList[i].hps);
                    jenisPajak = $rootScope.inisialisasiForm.materialList[i].jenisPajak.id;

                }
                var prosentasePajak = 0;
                if (typeof $rootScope.inisialisasiForm.materialList[i].prosentasePajak !== 'undefined') {
                    prosentasePajak = $rootScope.inisialisasiForm.materialList[i].prosentasePajak;
                }
                var formItemPengadaan = {
                    pengadaan: pengadaanId,
                    item: $rootScope.inisialisasiForm.materialList[i].material.id,
                    mataUang: $rootScope.inisialisasiForm.materialList[i].mataUang.id,
                    kuantitas: $rootScope.inisialisasiForm.materialList[i].kuantitas,
                    nilaiHPS: $rootScope.inisialisasiForm.materialList[i].hps,
                    totalHPS: total,
                    nilaiPajak: pajak,
                    jenisPajak: jenisPajak,
                    prosentasePajak: prosentasePajak
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
                    $scope.message = ('Proses penyimpanan data material pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                });
            }
        }

        $scope.insertVendorRequirement = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data vendor requirement pengadaan...');
            //vendor requirement
            var formVendorRequirement = {
                pengadaan: pengadaanId,
                minimalPengalaman: $rootScope.jumlahPengalamanVendor,
                tahunPengalamanMulai: $rootScope.inisialisasiForm.pengalamanTahunMulai,
                tahunPengalamanAkhir: $rootScope.inisialisasiForm.pengalamanTahunAkhir,
                nilaiKontrak: $rootScope.minimalNilaiKontrak
            };
            if (typeof $rootScope.inisialisasiForm.pengalamanMataUang !== 'undefined') {
                formVendorRequirement.mataUang = $rootScope.inisialisasiForm.pengalamanMataUang.id;
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

        $scope.insertDokumenUpload = function (pengadaanId) {
            //dokumen upload
            var today = new Date();
            if (typeof $rootScope.inisialisasiForm.fileUpload !== 'undefined') {
                for (var i = 0; i < $rootScope.inisialisasiForm.fileUpload.length; i++) {
                    var formDokumenPengadaan = {
                        pengadaan: pengadaanId,
                        nama: $rootScope.inisialisasiForm.fileUpload[i].fileName.substr(11, $rootScope.inisialisasiForm.fileUpload[i].fileName.length),
                        path: '/pengadaan/' + $rootScope.inisialisasiForm.fileUpload[i].fileName,
                        fileSize:10000
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
                        $scope.updateTahapanPengadaan(pengadaanId,data);
                    })
                    .error(function (data, status, headers, config) {});
            }

        //update pengadaan
        $scope.updateTahapanPengadaan = function (pengadaanId,nextTahapan) {
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

        $scope.insertPengadaanSMB = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data pengadaan SMB...');
            //vendor requirement
            var formPengadaanSMB = {
                pengadaan: pengadaanId,
                kategoriPengadaan: $rootScope.inisialisasiForm.kategoriPengadaan.id,
                flowPengadaan: $rootScope.inisialisasiForm.flowPengadaan.id
            };
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/createPengadaanSMB',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: formPengadaanSMB
            }).success(function (data, status, headers, config) {
                $scope.message = ('Proses penyimpanan data pengadaan SMB... BERHASIL');
            });
        }


        $scope.insertPengadaan = function () {
            //pengadaan
            var formPostPengadaan = {
                nomorNotaDinas: $rootScope.inisialisasiForm.nomorNotaPengadaan,
                tanggalNotaDinas: $rootScope.inisialisasiForm.dokumenTanggal,
                namaPengadaan: $rootScope.inisialisasiForm.namaPengadaan,
                nomorIzinPrinsip: $rootScope.inisialisasiForm.nomorIzinPrinsip,
                tanggalIzinPrinsip: $rootScope.inisialisasiForm.tanggalIzinPrinsip,
                mataUang: 1,
                kualifikasiPengadaan: null,
                jenisPengadaan: null,
                metodePengadaan: $rootScope.inisialisasiForm.metodePengadaan.id,
                metodePenyampaianDokumen: null,
                metodePenawaranHarga: $rootScope.inisialisasiForm.metodePenawaranHarga.id,
                jenisPenawaran: $rootScope.inisialisasiForm.jenisPenawaran.id,
                sistemEvaluasiPenawaran: $rootScope.inisialisasiForm.sistemEvaluasiPenawaran.id,
                kualifikasiVendor: $rootScope.inisialisasiForm.kualifikasiVendor.id,
                alurPengadaan: 1,
                tahapanPengadaan: 1,
                prosentasePajakMaterial: vm.prosentasePajakMaterial,
                prosentasePajakJasa: vm.prosentasePajakJasa
            };

            if ($rootScope.userLogin !== undefined && $rootScope.userLogin !== null) {
                formPostPengadaan.userId = $rootScope.userLogin.user.id;
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
                    $scope.message = 'Proses penyimpanan data pengadaan... BERHASIL';
                    $scope.insertItemPengadaanMaterial(pengadaanId);
                    $scope.insertItemJasa(pengadaanId);
                    $scope.insertVendorRequirement(pengadaanId);
                    $scope.insertVendor(pengadaanId);
                    $scope.insertDokumenUpload(pengadaanId);
                    $scope.insertPengadaanSMB(pengadaanId);
                    $rootScope.inisialisasiForm.uploader = undefined;
                }
                $scope.loading = false;
                $scope.btnSimpanInisialisasiStatus = false;
                $scope.btnKembaliIndexStatus = true;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.message = "Error simpan pengadaan";
                console.error($scope.message);
            });
        }

        $scope.btnSimpanInisialisasi = function () {
            $scope.insertPengadaan();
        }

        $scope.btnKembaliIndex = function () {
            $location.path('/app/promise/procurement/inisialisasi/smb');
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

    InisialisasiPengadaanSMBTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader', '$q'];

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
