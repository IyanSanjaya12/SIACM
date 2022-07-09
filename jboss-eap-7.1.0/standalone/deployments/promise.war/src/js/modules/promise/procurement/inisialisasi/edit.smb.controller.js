(function () {
    'use strict';

    angular
        .module('naut')
        .controller('InisialisasiPengadaanSMBEditController', InisialisasiPengadaanSMBEditController)
        .filter('propsFilter', propsFilter);

    function InisialisasiPengadaanSMBEditController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader, $q, $stateParams) {
        var vm = this;
        $scope.loading = true;
        vm.dataPengadaan = $rootScope.pengadaan.pengadaan;
        vm.kategoriPengadaan = $rootScope.pengadaan.kategoriPengadaan;
        vm.metodePengadaan = vm.dataPengadaan.metodePengadaan;
        vm.kualifikasiPengadaan = vm.dataPengadaan.kualifikasiPengadaan;
        vm.flowPengadaan = $rootScope.pengadaan.flowPengadaan;

        vm.tabDisableStatus = {
            tabGeneral: false,
            tabRules: true,
            tabHPS: true,
            tabVendor: true,
            tabDokumen: true
        }

        vm.tabActiveStatus = {
            tabGeneral: true,
            tabRules: false,
            tabHPS: false,
            tabVendor: false,
            tabDokumen: false
        }

        vm.goToTabGeneral = function () {
            vm.tabActiveStatus.tabGeneral = true;
            vm.tabActiveStatus.tabRules = false;
            vm.tabActiveStatus.tabHPS = false;
            vm.tabActiveStatus.tabVendor = false;
            vm.tabActiveStatus.tabDokumen = false;
        }

        vm.goToTabRules = function () {
            var validateFormGeneral = false;
            //nomorNotaPengadaan
            if (vm.dataPengadaan.nomorNotaDinas !== undefined && vm.dataPengadaan.nomorNotaDinas !== null && vm.dataPengadaan.nomorNotaDinas.length > 0) {
                vm.dataPengadaan.nomorNotaPengadaanError = false;
            } else {
                vm.dataPengadaan.nomorNotaPengadaanError = true;
                validateFormGeneral = true;
            }
            //dokumenTanggal
            if (vm.dataPengadaan.tanggalNotaDinas !== undefined && vm.dataPengadaan.tanggalNotaDinas !== null) {
                vm.dataPengadaan.dokumenTanggalError = false;
            } else {
                vm.dataPengadaan.dokumenTanggalError = true;
                validateFormGeneral = true;
            }
            //namaPengadaan
            if (vm.dataPengadaan.namaPengadaan !== undefined && vm.dataPengadaan.namaPengadaan !== null && vm.dataPengadaan.namaPengadaan.length > 0) {
                vm.dataPengadaan.namaPengadaanError = false;
            } else {
                vm.dataPengadaan.namaPengadaanError = true;
                validateFormGeneral = true;
            }

            if (validateFormGeneral == false) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = true;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            } else {
                vm.tabActiveStatus.tabGeneral = true;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            }
        }

        vm.goToTabHPS = function () {
            var validateTabRule = false;

            if (typeof vm.flowPengadaan === 'undefined') {
                vm.flowPengadaanError = true;
                validateTabRule = true;
            } else {
                vm.flowPengadaanError = false;
            }

            if (typeof vm.dataPengadaan.metodePenawaranHarga === 'undefined') {
                vm.metodePenawaranHargaError = true;
                validateTabRule = true;
            } else {
                vm.metodePenawaranHargaError = false;
            }

            if (typeof vm.dataPengadaan.jenisPenawaran === 'undefined') {
                vm.jenisPenawaranError = true;
                validateTabRule = true;
            } else {
                vm.jenisPenawaranError = false;
            }

            if (typeof vm.dataPengadaan.sistemEvaluasiPenawaran === 'undefined') {
                vm.sistemEvaluasiPenawaranError = true;
                validateTabRule = true;
            } else {
                vm.sistemEvaluasiPenawaranError = false;
            }

            if (validateTabRule == false) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = true;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            } else {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = true;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            }
        }

        vm.goToTabVendor = function () {
            var validateTabHPS = false;
            if (vm.dataPengadaan.materialPengadaanList.length == 0 && vm.dataPengadaan.jasaPengadaanList.length == 0) {
                validateTabHPS = true;
            }
            if (validateTabHPS) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = true;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            } else {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = true;
                vm.tabActiveStatus.tabDokumen = false;

            }
        }

        vm.goToTabDokumen = function () {
            vm.tabActiveStatus.tabGeneral = false;
            vm.tabActiveStatus.tabRules = false;
            vm.tabActiveStatus.tabHPS = false;
            vm.tabActiveStatus.tabVendor = false;
            vm.tabActiveStatus.tabDokumen = true;
        }

        //Tab GENERAL
        if (vm.dataPengadaan.nomorIzinPrinsip !== undefined && vm.dataPengadaan.nomorIzinPrinsip !== null &&
            vm.dataPengadaan.tanggalIzinPrinsip !== undefined && vm.dataPengadaan.tanggalIzinPrinsip !== null) {
            vm.dataPengadaan.izinPrinsipStatus = true;
        } else {
            vm.dataPengadaan.izinPrinsipStatus = false;
        }

        // datepicker for tanggal nota dinas
        vm.dokumenTanggalOpened = false;
        vm.dokumenTanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.dokumenTanggalOpened = true;
        };

        // datepicker for tanggal izin prinsip
        vm.izinPrinsipTanggalOpened = false;
        vm.izinPrinsipTanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.izinPrinsipTanggalOpened = true;
        };

        //Tab Rules
        $http.get($rootScope.backendAddress + '/procurement/master/flowPengadaanServices/getListByKategori/' + vm.kategoriPengadaan.id)
            .success(function (data, status, headers, config) {
                $scope.flowPengadaanList = data;
            });

        $http.get($rootScope.backendAddress + '/procurement/master/MetodePenawaranHargaServices/getMetodePenawaranHargaList')
            .success(function (data, status, headers, config) {
                $scope.metodePenawaranHargaList = data;
            });

        $http.get($rootScope.backendAddress + '/procurement/master/JenisPenawaranServices/getJenisPenawaranList')
            .success(function (data, status, headers, config) {
                $scope.jenisPenawaranList = data;
            });

        $http.get($rootScope.backendAddress + '/procurement/master/SistemEvaluasiPenawaranServices/getSistemEvaluasiPenawaranList')
            .success(function (data, status, headers, config) {
                $scope.sistemEvaluasiPenawaranList = data;
            });

        //Tab HPS Form
        $scope.pilihPerencanaanList = [{
            id: 1,
            nama: "Tanpa Perencanaan"
		}, {
            id: 2,
            nama: "Dengan Perencanaan"
		}];
        vm.dataPengadaan.pilihPerencanaan = $scope.pilihPerencanaanList[0];

        $scope.tambahBarangJasaList = [{
            id: 1,
            nama: "Tambah Barang"
		}, {
            id: 2,
            nama: "Tambah Jasa"
		}];
        vm.dataPengadaan.tambahBarangJasa = $scope.tambahBarangJasaList[0];

        //generate Form Barang/Jasa
        vm.prosesTambahBarangJasa = function () {
            var validationProses = false;
            if (typeof vm.dataPengadaan.pilihPerencanaan === 'undefined') {
                vm.pilihPerencanaanError = true;
            } else {
                vm.pilihPerencanaanError = false;
                if (typeof vm.dataPengadaan.tambahBarangJasa === 'undefined') {
                    vm.tambahBarangJasaError = true;
                } else {
                    vm.tambahBarangJasaError = false;
                    validationProses = true;
                }
            }

            if (validationProses) {
                vm.tambahJasaFormStatus = false;
                vm.tambahMaterialFormStatus = false;

                if (vm.dataPengadaan.tambahBarangJasa.id == 1 && vm.dataPengadaan.pilihPerencanaan.id == 1) {
                    vm.tambahMaterialFormStatus = true;
                    vm.tambahJasaFormStatus = false;
                }
                if (vm.dataPengadaan.tambahBarangJasa.id == 2 && vm.dataPengadaan.pilihPerencanaan.id == 1) {
                    vm.tambahJasaFormStatus = true;
                    vm.tambahMaterialFormStatus = false;
                }
            }
        }

        //Material
        $scope.autoLoadMaterial = function () {
            if (vm.dataPengadaan.materialPengadaanList !== undefined && vm.dataPengadaan.materialPengadaanList !== null && vm.dataPengadaan.materialPengadaanList.length > 0) {

            } else {
                // download data from itempengadaan based on pengadaan
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + vm.dataPengadaan.id)
                    .success(function (data, status, headers, config) {
                        vm.dataPengadaan.materialPengadaanList = data;

                        //List Material		
                        $scope.tableMaterialList = new ngTableParams({
                            page: 1, // show first page
                            count: 5 // count per page
                        }, {
                            total: vm.dataPengadaan.materialPengadaanList.length, // length of data4
                            getData: function ($defer, params) {
                                $defer.resolve(vm.dataPengadaan.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });

                        // footer data
                        //vm.dataPengadaan.prosentasePajakMaterial = 10;
                        vm.dataPengadaan.jumlahMaterialTotalHPS = 0;
                        if (vm.dataPengadaan.materialPengadaanList !== undefined && vm.dataPengadaan.materialPengadaanList !== null && vm.dataPengadaan.materialPengadaanList.length > 0) {
                            if (vm.dataPengadaan.prosentasePajakMaterial === undefined) {
                                vm.dataPengadaan.prosentasePajakMaterial = 0;
                            }
                            angular.forEach(vm.dataPengadaan.materialPengadaanList, function (value, key) {
                                vm.dataPengadaan.jumlahMaterialTotalHPS = vm.dataPengadaan.jumlahMaterialTotalHPS + value.totalHPS;
                                // nilai pajak untuk setiap record 
                                value.nilaiPajak = vm.dataPengadaan.prosentasePajakMaterial * value.nilaiHPS / 100;
                            });
                        }
                        vm.dataPengadaan.jumlahMaterialNilaiHPSPPN = vm.dataPengadaan.jumlahMaterialTotalHPS * vm.dataPengadaan.prosentasePajakMaterial / 100;
                        vm.dataPengadaan.jumlahMaterialNilaiHPSDenganPPN = vm.dataPengadaan.jumlahMaterialTotalHPS + vm.dataPengadaan.jumlahMaterialNilaiHPSPPN;
                    });
            }
        };
        $scope.autoLoadMaterial();

        $scope.updateJumlahMaterialNilaiHPSPPN = function () {
            if (vm.dataPengadaan.prosentasePajakMaterial !== undefined && vm.dataPengadaan.prosentasePajakMaterial !== null && vm.dataPengadaan.prosentasePajakMaterial > 0) {

            } else {
                vm.dataPengadaan.prosentasePajakMaterial = vm.dataPengadaan.materialPengadaanList[0].jenisPajak.prosentase;
            }
            vm.dataPengadaan.jumlahMaterialTotalHPS = 0;
            if (vm.dataPengadaan.materialPengadaanList !== undefined && vm.dataPengadaan.materialPengadaanList !== null && vm.dataPengadaan.materialPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.materialPengadaanList, function (value, key) {
                    vm.dataPengadaan.jumlahMaterialTotalHPS = vm.dataPengadaan.jumlahMaterialTotalHPS + value.totalHPS;
                    // nilai pajak untuk setiap record 
                    value.nilaiPajak = vm.dataPengadaan.prosentasePajakMaterial * value.nilaiHPS / 100;
                });
            }
            vm.dataPengadaan.jumlahMaterialNilaiHPSPPN = vm.dataPengadaan.jumlahMaterialTotalHPS * vm.dataPengadaan.prosentasePajakMaterial / 100;
            vm.dataPengadaan.jumlahMaterialNilaiHPSDenganPPN = vm.dataPengadaan.jumlahMaterialTotalHPS + vm.dataPengadaan.jumlahMaterialNilaiHPSPPN;
        }

        // tambah data material pengadaan
        vm.btnTambahMaterial = function () {
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                var modalItemMaterialTotal = $modal.open({
                    templateUrl: '/kebutuhan.material.total.html',
                    controller: 'ModalItemMaterialTotalCtrl',
                    size: 'lg',
                    resolve: {
                        materialPengadaan: function () {
                            return {};
                        }
                    }
                });
                modalItemMaterialTotal.result.then(function (materialPengadaan) {
                    if (materialPengadaan != undefined && materialPengadaan !== null) {
                        vm.dataPengadaan.materialPengadaanList.push(materialPengadaan);
                        $scope.tableMaterialList.reload();
                        $scope.updateJumlahMaterialNilaiHPSPPN();
                    }
                });
            } else {
                var modalItemMaterialSatuan = $modal.open({
                    templateUrl: '/kebutuhan.material.satuan.html',
                    controller: 'ModalItemMaterialSatuanCtrl',
                    size: 'lg',
                    resolve: {
                        materialPengadaan: function () {
                            return {};
                        }
                    }
                });
                modalItemMaterialSatuan.result.then(function (materialPengadaan) {
                    if (materialPengadaan != undefined && materialPengadaan !== null) {
                        vm.dataPengadaan.materialPengadaanList.push(materialPengadaan);
                        $scope.tableMaterialList.reload();
                        $scope.updateJumlahMaterialNilaiHPSPPN();
                    }
                });
            }
        }

        // edit mode material pengadaan
        $scope.editMaterial = function (index, itemPengadaan) {
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                var modalItemMaterialTotal = $modal.open({
                    templateUrl: '/kebutuhan.material.total.html',
                    controller: 'ModalItemMaterialTotalCtrl',
                    size: 'lg',
                    resolve: {
                        materialPengadaan: function () {
                            return itemPengadaan;
                        }
                    }
                });
                modalItemMaterialTotal.result.then(function (materialPengadaan) {
                    if (materialPengadaan != undefined && materialPengadaan !== null) {
                        vm.dataPengadaan.materialPengadaanList.splice(index, 1, materialPengadaan);
                        $scope.tableMaterialList.reload();
                        $scope.updateJumlahMaterialNilaiHPSPPN();
                    }
                });
            } else {
                var modalItemMaterialSatuan = $modal.open({
                    templateUrl: '/kebutuhan.material.satuan.html',
                    controller: 'ModalItemMaterialSatuanCtrl',
                    size: 'lg',
                    resolve: {
                        materialPengadaan: function () {
                            return itemPengadaan;
                        }
                    }
                });
                modalItemMaterialSatuan.result.then(function (materialPengadaan) {
                    if (materialPengadaan != undefined && materialPengadaan !== null) {
                        $scope.tableMaterialList.reload();
                        $scope.updateJumlahMaterialNilaiHPSPPN();
                    }
                });
            }
        };

        //Material Delete
        $scope.removeMaterial = function (index, materialObj) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: ModalInstanceCtrl,
                resolve: {
                    materialName: function () {
                        return materialObj.item.nama;
                    }
                }
            });
            modalInstance.result.then(function () {
                vm.dataPengadaan.materialPengadaanList.splice(index, 1);
                if (materialObj.id !== undefined && materialObj.id !== null && materialObj.id > 0) {
                    if (vm.dataPengadaan.deleteMaterialPengadaanList === undefined || vm.dataPengadaan.deleteMaterialPengadaanList === null) {
                        vm.dataPengadaan.deleteMaterialPengadaanList = [];
                    }
                    vm.dataPengadaan.deleteMaterialPengadaanList.push(materialObj);
                }
                $scope.tableMaterialList.reload();
                $scope.updateJumlahMaterialNilaiHPSPPN();
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

        //Jasa
        $scope.autoLoadJasa = function () {
            if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {

            } else {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + vm.dataPengadaan.id)
                    .success(function (data, status, headers, config) {
                        vm.dataPengadaan.jasaPengadaanList = data;
                        //List Jasa
                        $scope.tableJasaList = new ngTableParams({
                            page: 1, // show first page
                            count: 5 // count per page
                        }, {
                            total: vm.dataPengadaan.jasaPengadaanList.length, // length of data4
                            getData: function ($defer, params) {
                                $defer.resolve(vm.dataPengadaan.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });

                        // footer data
                        //vm.dataPengadaan.prosentasePajakJasa = 10;
                        vm.dataPengadaan.jumlahJasaTotalHPS = 0;
                        if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {
                            if (vm.dataPengadaan.prosentasePajakJasa === undefined) {
                                vm.dataPengadaan.prosentasePajakJasa = 0;
                            }
                            angular.forEach(vm.dataPengadaan.jasaPengadaanList, function (value, key) {
                                vm.dataPengadaan.jumlahJasaTotalHPS = vm.dataPengadaan.jumlahJasaTotalHPS + value.totalHPS;
                                // nilai pajak untuk setiap record 
                                value.nilaiPajak = vm.dataPengadaan.prosentasePajakJasa * value.nilaiHPS / 100;
                            });
                        }
                        vm.dataPengadaan.jumlahJasaNilaiHPSPPN = vm.dataPengadaan.jumlahJasaTotalHPS * vm.dataPengadaan.prosentasePajakJasa / 100;
                        vm.dataPengadaan.jumlahJasaNilaiHPSDenganPPN = vm.dataPengadaan.jumlahJasaTotalHPS + vm.dataPengadaan.jumlahJasaNilaiHPSPPN;
                    });
            }
        };
        $scope.autoLoadJasa();

        $scope.updateJumlahJasaNilaiHPSPPN = function () {
            if (vm.dataPengadaan.prosentasePajakJasa !== undefined && vm.dataPengadaan.prosentasePajakJasa !== null && vm.dataPengadaan.prosentasePajakJasa > 0) {} else {
                vm.dataPengadaan.prosentasePajakJasa = vm.dataPengadaan.jasaPengadaanList[0].jenisPajak.prosentase;
            }
            vm.dataPengadaan.jumlahJasaTotalHPS = 0;
            if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {

                angular.forEach(vm.dataPengadaan.jasaPengadaanList, function (value, key) {
                    vm.dataPengadaan.jumlahJasaTotalHPS = vm.dataPengadaan.jumlahJasaTotalHPS + value.totalHPS;
                    // nilai pajak untuk setiap record 
                    value.nilaiPajak = vm.dataPengadaan.prosentasePajakJasa * value.nilaiHPS / 100;
                });
            }
            vm.dataPengadaan.jumlahJasaNilaiHPSPPN = vm.dataPengadaan.jumlahJasaTotalHPS * vm.dataPengadaan.prosentasePajakJasa / 100;
            vm.dataPengadaan.jumlahJasaNilaiHPSDenganPPN = vm.dataPengadaan.jumlahJasaTotalHPS + vm.dataPengadaan.jumlahJasaNilaiHPSPPN;
        }

        // tambah data jasa pengadaan
        vm.btnTambahJasa = function () {
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                var modalJasaMaterialTotal = $modal.open({
                    templateUrl: '/kebutuhan.jasa.total.html',
                    controller: 'ModalJasaMaterialTotalCtrl',
                    size: 'lg',
                    resolve: {
                        jasaPengadaan: function () {
                            return {};
                        }
                    }
                });
                modalJasaMaterialTotal.result.then(function (jasaPengadaan) {
                    if (jasaPengadaan != undefined && jasaPengadaan !== null) {
                        vm.dataPengadaan.jasaPengadaanList.push(jasaPengadaan);
                        $scope.tableJasaList.reload();
                        $scope.updateJumlahJasaNilaiHPSPPN();
                    }
                });
            } else {
                var modalJasaMaterialSatuan = $modal.open({
                    templateUrl: '/kebutuhan.jasa.satuan.html',
                    controller: 'ModalJasaMaterialSatuanCtrl',
                    size: 'lg',
                    resolve: {
                        jasaPengadaan: function () {
                            return {};
                        }
                    }
                });
                modalJasaMaterialSatuan.result.then(function (jasaPengadaan) {
                    if (jasaPengadaan != undefined && jasaPengadaan !== null) {
                        vm.dataPengadaan.jasaPengadaanList.push(jasaPengadaan);
                        $scope.tableJasaList.reload();
                        $scope.updateJumlahJasaNilaiHPSPPN();
                    }
                });
            }
        }

        // edit jasa
        $scope.editJasa = function (index, jasaPengadaan) {
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                var modalJasaMaterialTotal = $modal.open({
                    templateUrl: '/kebutuhan.jasa.total.html',
                    controller: 'ModalJasaMaterialTotalCtrl',
                    size: 'lg',
                    resolve: {
                        jasaPengadaan: function () {
                            return jasaPengadaan;
                        }
                    }
                });
                modalJasaMaterialTotal.result.then(function (jasaPengadaan) {
                    if (jasaPengadaan != undefined && jasaPengadaan !== null) {
                        vm.dataPengadaan.jasaPengadaanList.splice(index, 1, jasaPengadaan);
                        $scope.tableJasaList.reload();
                        $scope.updateJumlahJasaNilaiHPSPPN();
                    }
                });
            } else {
                var modalJasaMaterialSatuan = $modal.open({
                    templateUrl: '/kebutuhan.jasa.satuan.html',
                    controller: 'ModalJasaMaterialSatuanCtrl',
                    size: 'lg',
                    resolve: {
                        jasaPengadaan: function () {
                            return jasaPengadaan;
                        }
                    }
                });
                modalJasaMaterialSatuan.result.then(function (jasaPengadaan) {
                    if (jasaPengadaan != undefined && jasaPengadaan !== null) {
                        $scope.tableJasaList.reload();
                        $scope.updateJumlahJasaNilaiHPSPPN();
                    }
                });
            }
        }

        //Jasa Delete
        $scope.removeJasa = function (index, jasaObject) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTableJasa2.html',
                controller: ModalInstanceJasaCtrl,
                resolve: {
                    jasaName: function () {
                        return jasaObject.item.nama;
                    }
                }
            });
            modalInstance.result.then(function () {
                vm.dataPengadaan.jasaPengadaanList.splice(index, 1);
                if (jasaObject.id !== undefined && jasaObject.id !== null && jasaObject.id > 0) {
                    if (vm.dataPengadaan.deleteJasaPengadaanList === undefined || vm.dataPengadaan.deleteJasaPengadaanList === null) {
                        vm.dataPengadaan.deleteJasaPengadaanList = [];
                    }
                    vm.dataPengadaan.deleteJasaPengadaanList.push(jasaObject);
                }
                $scope.tableJasaList.reload();
                $scope.updateJumlahJasaNilaiHPSPPN();
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

        //Tab 3 Vendor
        // kualifikasi vendor
        $http.get($rootScope.backendAddress + '/procurement/master/kualifikasi-vendor/get-list')
            .success(function (data, status, headers, config) {
                $scope.kualifikasiVendorList = data;
            });

        // Bidang usaha vendor
        if (vm.dataPengadaan.subBidangUsahaList !== undefined && vm.dataPengadaan.subBidangUsahaList !== null && vm.dataPengadaan.subBidangUsahaList.length > 0) {

        } else {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + vm.dataPengadaan.id)
                .success(function (data, status, headers, config) {
                    if (data !== undefined && data !== null && data.length > 0) {
                        vm.dataPengadaan.segmentasiVendorStatus = true;
                        vm.dataPengadaan.subBidangUsahaList = [];
                        angular.forEach(data, function (value, key) {
                            value.subBidangUsaha.bidangUsahaId = value.id;
                            vm.dataPengadaan.subBidangUsahaList.push(value.subBidangUsaha);
                        });
                        $scope.tableBidangUsahaVendor = new ngTableParams({
                            page: 1,
                            count: 5
                        }, {
                            total: vm.dataPengadaan.subBidangUsahaList.length,
                            getData: function ($defer, params) {
                                $defer.resolve(vm.dataPengadaan.subBidangUsahaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    }
                });
        }

        $scope.btnTambahBidang = function () {
            var modalTambahSubBidangUsaha = $modal.open({
                templateUrl: '/bidangusaha.html',
                controller: 'BidangUsahaPengadaanCtlr',
                size: 'lg',
                resolve: {
                    dataBidangUsahaList: function () {
                        return vm.dataPengadaan.subBidangUsahaList;
                    }
                }
            });
            modalTambahSubBidangUsaha.result.then(function (dataBidangUsahaList) {
                vm.dataPengadaan.subBidangUsahaList = dataBidangUsahaList;
                $scope.tableBidangUsahaVendor.reload();
            });
        }

        $scope.removeBidangUsaha = function (subBidangUsaha, index) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable3.html',
                controller: ModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    materialName: function () {
                        return subBidangUsaha.bidangUsaha.nama + ' - ' + subBidangUsaha.nama;
                    }
                }
            });
            modalInstance.result.then(function () {
                vm.dataPengadaan.subBidangUsahaList.splice(index, 1);
                $scope.tableBidangUsahaVendor.reload();
            });
        }

        // pengalaman vendor
        $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementListByPengadaanId/' + vm.dataPengadaan.id)
            .success(function (data, status, headers, config) {
                if (data !== undefined && data !== null) {
                    vm.dataPengadaan.pengalamanVendorStatus = true;
                    vm.dataPengadaan.pengalamanVendor = data;
                }
            });

        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.pengalamanMataUangList = data;
            }).error(function (data, status, headers, config) {});

        // jika metode pengadaan adalah penunjukan langsung request undangan vendor
        if (vm.dataPengadaan.metodePengadaan.id === 5) {
            if (vm.dataPengadaan.undanganVendorList !== undefined && vm.dataPengadaan.undanganVendorList !== null && vm.dataPengadaan.undanganVendorList.length > 0) {

            } else {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/getUndanganVendorListByPengadaanId/' + vm.dataPengadaan.id)
                    .success(function (data, status, headers, config) {
                        vm.dataPengadaan.undanganVendorList = [];
                        if (data !== undefined && data !== null && data.length > 0) {
                            angular.forEach(data, function (vendor, index) {
                                vendor.vendor.undanganId = vendor.id;
                                vm.dataPengadaan.undanganVendorList.push(vendor.vendor);
                            });

                            $scope.tableUndanganVendor = new ngTableParams({
                                page: 1,
                                count: 5
                            }, {
                                total: vm.dataPengadaan.undanganVendorList.length,
                                getData: function ($defer, params) {
                                    $defer.resolve(vm.dataPengadaan.undanganVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                                }
                            });
                        }
                    });
            }
        }

        $scope.btnUndangVendor = function () {
            var modalTambahUndangVendor = $modal.open({
                templateUrl: '/vendorpengadaan.html',
                controller: 'UndangVendorPengadaanCtlr',
                size: 'lg',
                resolve: {
                    undanganVendorList: function () {
                        return vm.dataPengadaan.undanganVendorList;
                    }
                }
            });
            modalTambahUndangVendor.result.then(function (undanganVendorList) {
                vm.dataPengadaan.undanganVendorList = undanganVendorList;
                $scope.tableUndanganVendor.reload();
            });
        }





        $scope.removeVendor = function (vendorObj, size) {
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
                vm.dataPengadaan.undanganVendorList.splice(index, 1);
                if (vendorObj.undanganVendorId !== undefined && vendorObj.undanganVendorId !== null) {
                    if (vm.dataPengadaan.deleteUndanganVendorList === undefined || vm.dataPengadaan.deleteUndanganVendorList === null) {
                        vm.dataPengadaan.deleteUndanganVendorList = [];
                    }
                    vm.dataPengadaan.deleteUndanganVendorList.push(vendorObj);
                }
                $scope.tableUndanganVendor.reload();
            });
        };


        $scope.loadDokumen = function (upload) {
                vm.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + vm.dataPengadaan.id, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    //vm.dokumenPenjelasanList = data;
                    /*Pindahkan ke list uploader file quee to show*/
                    for (var y = 0; y < data.length; y++) {
                        var file = {
                            id: data[y].id,
                            name: data[y].nama,
                            size: 10000
                        };
                        upload.addToQueue(file, undefined, undefined);
                        var item = upload.queue[upload.queue.length - 1];
                        item.isUploading = false;
                        item.isReady = false;
                        item.isSuccess = true;
                        item.isUploaded = true;
                        item.progress = 100;
                        /*added new attribute & properties to store realFileName*/
                        //                        item.realFileName = vm.dokumenPenjelasanList[y].realFileName;
                    }

                    vm.loading = false;
                }).error(function (data, status, headers, config) {
                    vm.loading = false;
                });
            }
            //Tab 4 Uplaod file
        $scope.btnSimpanInisialisasiStatus = true;
        $scope.btnKembaliIndexStatus = false;
        if (typeof $rootScope.inisialisasiForm.uploader === 'undefined') {
            var uploader = $scope.uploader = new FileUploader({
                url: $rootScope.uploadBackendAddress,
                //url: 'http://localhost:8080/promisews/procurement/dokumenUploadPengadaan/uploadFile',
                method: 'POST'
            });

            $scope.loadDokumen($scope.uploader);
        } else {
            var uploader = $scope.uploader = $rootScope.inisialisasiForm.uploader;

            $scope.loadDokumen($scope.uploader);

        }
        //        // FILTERS
        //        uploader.filters.push({
        //            name: 'customFilter',
        //            fn: function (item /*{File|FileLikeObject}*/ , options) {
        //                return this.queue.length < 10;
        //            }
        //        });



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

        $scope.insertItemJasaSynchronus = function (index, pengadaanId) {
            var jasaList = vm.dataPengadaan.jasaPengadaanList[index];
            var itemDto = {
                id: jasaList.item.id,
                nama: jasaList.item.nama,
                satuanId: 5,
                itemTypeId: 2,
                itemGroupId: 2
            }
            var uri = '';
            if (typeof itemDto.id !== 'undefined') {
                uri = $rootScope.backendAddress + '/procurement/master/item/update-item';
            } else {
                uri = $rootScope.backendAddress + '/procurement/master/item/insert-item';
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
            for (var i = 0; i < vm.dataPengadaan.jasaPengadaanList.length; i++) {
                $scope.insertItemJasaSynchronus(i, pengadaanId);
            }
        }

        $scope.insertItemPengadaanJasa = function (itemId, pengadaanId, index) {
            //jasa
            var jasaList = vm.dataPengadaan.jasaPengadaanList[index];
            var total = 0;
            var pajak = 0;
            var jenisPajak;
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                total = jasaList.kuantitas * jasaList.nilaiHPS;
                pajak = total * vm.dataPengadaan.prosentasePajakJasa; //pajak ???? fix this
                jenisPajak = null;
            } else {
                total = jasaList.totalHPS;
                pajak = total - (jasaList.kuantitas * jasaList.nilaiHPS);
                jenisPajak = jasaList.jenisPajak.id;
            }
            var prosentasePajak = 0;
            if (typeof jasaList.prosentasePajak !== 'undefined') {
                prosentasePajak = jasaList.prosentasePajak;
            }
            var formItemPengadaan = {
                id: jasaList.id,
                pengadaan: pengadaanId,
                item: itemId,
                mataUang: jasaList.mataUang.id,
                kuantitas: jasaList.kuantitas,
                nilaiHPS: jasaList.nilaiHPS,
                totalHPS: total,
                nilaiPajak: pajak,
                jenisPajak: jenisPajak,
                prosentasePajak: prosentasePajak
            };
            var uri = '';
            if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                if (typeof formItemPengadaan.id !== 'undefined') {
                    uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaan';
                } else {
                    uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanTotal';
                }
            } else {
                if (typeof formItemPengadaan.id !== 'undefined') {
                    uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaanSatuan';
                } else {
                    uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
                }
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

            // delete data pengadaan jasa
            if (vm.dataPengadaan.deleteJasaPengadaanList !== undefined && vm.dataPengadaan.deleteJasaPengadaanList !== null && vm.dataPengadaan.deleteJasaPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.deleteJasaPengadaanList, function (deleteJasa, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/deleteRowItemPengadaan/' + deleteJasa.id;
                    $http.get(targetURI, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {});
                });
            }
        }

        $scope.insertItemPengadaanMaterial = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data material pengadaan...');
            //material
            for (var i = 0; i < vm.dataPengadaan.materialPengadaanList.length; i++) {
                var prosentasePajak = 0;
                if (typeof vm.dataPengadaan.materialPengadaanList[i].prosentasePajak !== 'undefined') {
                    prosentasePajak = vm.dataPengadaan.materialPengadaanList[i].prosentasePajak;
                }
                var jenisPajak = null;
                if (vm.dataPengadaan.jenisPenawaran.id != 1) {
                    jenisPajak = vm.dataPengadaan.materialPengadaanList[i].jenisPajak.id;
                }
                var formItemPengadaan = {
                    id: vm.dataPengadaan.materialPengadaanList[i].id,
                    pengadaan: pengadaanId,
                    item: vm.dataPengadaan.materialPengadaanList[i].item.id,
                    mataUang: vm.dataPengadaan.materialPengadaanList[i].mataUang.id,
                    kuantitas: vm.dataPengadaan.materialPengadaanList[i].kuantitas,
                    nilaiHPS: vm.dataPengadaan.materialPengadaanList[i].nilaiHPS,
                    totalHPS: vm.dataPengadaan.materialPengadaanList[i].totalHPS,
                    nilaiPajak: (vm.dataPengadaan.materialPengadaanList[i].totalHPS - vm.dataPengadaan.materialPengadaanList[i].nilaiHPS),
                    jenisPajak: jenisPajak,
                    prosentasePajak: prosentasePajak
                };
                var uri = '';
                if (vm.dataPengadaan.jenisPenawaran.id == 1) {
                    if (typeof formItemPengadaan.id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaan';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanTotal';
                    }
                } else {
                    if (typeof formItemPengadaan.id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaanSatuan';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
                    }
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
            // delete data material
            if (vm.dataPengadaan.deleteMaterialPengadaanList !== undefined && vm.dataPengadaan.deleteMaterialPengadaanList !== null && vm.dataPengadaan.deleteMaterialPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.deleteMaterialPengadaanList, function (deleteMaterial, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/deleteRowItemPengadaan/' + deleteMaterial.id;
                    $http.get(targetURI, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {});
                });
            }
        }

        $scope.insertVendorRequirement = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data vendor requirement pengadaan...');
            //vendor requirement
            var formVendorRequirement = {
                id: vm.dataPengadaan.pengalamanVendor.id,
                pengadaan: pengadaanId,
                minimalPengalaman: vm.dataPengadaan.pengalamanVendor.minimalPengalaman,
                tahunPengalamanMulai: vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai,
                tahunPengalamanAkhir: vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir,
                mataUang: vm.dataPengadaan.pengalamanVendor.mataUang.id,
                nilaiKontrak: vm.dataPengadaan.pengalamanVendor.nilaiKontrak
            };
            var uri = '';
            if (typeof formVendorRequirement.id !== 'undefined') {
                uri = $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/updateVendorRequirement';
            } else {
                uri = $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/insertVendorRequirement';
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
                data: formVendorRequirement
            }).success(function (data, status, headers, config) {
                $scope.message = ('Proses penyimpanan data vendor requirement pengadaan... BERHASIL');
            });
        }

        $scope.insertBidang = function (pengadaanId) {
            //bidang
            $scope.message = ('Proses penyimpanan data bidang pengadaan...');
            if (typeof vm.dataPengadaan.subBidangUsahaList !== 'undefined') {
                for (var i = 0; i < vm.dataPengadaan.subBidangUsahaList.length; i++) {
                    var formBidangPengadaan = {
                        id: vm.dataPengadaan.subBidangUsahaList[i].bidangUsahaId,
                        pengadaanId: pengadaanId,
                        subBidangId: vm.dataPengadaan.subBidangUsahaList[i].id
                    };
                    var uri = '';
                    if (typeof formBidangPengadaan.id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/updateBidangUsahaPengadaan';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/insertBidangUsahaPengadaan';
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
            if (typeof vm.dataPengadaan.undanganVendorList !== 'undefined') {
                for (var i = 0; i < vm.dataPengadaan.undanganVendorList.length; i++) {
                    var formVendorUndangan = {
                        id: vm.dataPengadaan.undanganVendorList[i].undanganId,
                        pengadaan: pengadaanId,
                        vendor: vm.dataPengadaan.undanganVendorList[i].id
                    }
                    var uri = '';
                    if (typeof formVendorUndangan.id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/updateUndanganVendor';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/insertUndanganVendor';
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
                        data: formVendorUndangan
                    }).success(function (data, status, headers, config) {
                        $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ' + (((i + 1) / 100) * 100) + '%');
                    });
                }
                // delete data vendor that exist in database
                if (vm.dataPengadaan.deleteUndanganVendorList !== undefined && vm.dataPengadaan.deleteUndanganVendorList !== null && vm.dataPengadaan.deleteUndanganVendorList.length > 0) {
                    angular.forEach(vm.dataPengadaan.deleteUndanganVendorList, function (deleteUndanganVendor, index) {
                        var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/deleteRowUndanganVendor/' + deleteUndanganVendor.undanganVendorId;
                        $http.get(targetURI, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {});
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
                        id: $rootScope.inisialisasiForm.fileUpload[i].file.id,
                        pengadaan: pengadaanId,
                        nama: $rootScope.inisialisasiForm.fileUpload[i].file.name.substr(11, $rootScope.inisialisasiForm.fileUpload[i].file.name.length),
                        path: '/pengadaan/' + $rootScope.inisialisasiForm.fileUpload[i].file.name,
                        fileSize: 10000
                    }
                    var uri = '';
                    if (typeof formDokumenPengadaan.id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/updateDokumenPengadaan';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/insertDokumenPengadaan';
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

        $scope.insertPengadaanSMB = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data pengadaan SMB...');
            //vendor requirement
            var formPengadaanSMB = {
                id:$rootScope.pengadaan.id,
                pengadaan: pengadaanId,
                kategoriPengadaan: vm.kategoriPengadaan.id,
                flowPengadaan: vm.flowPengadaan.id
            };
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/updatePengadaanSMB',
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
                id: vm.dataPengadaan.id,
                nomorNotaDinas: vm.dataPengadaan.nomorNotaDinas,
                tanggalNotaDinas: vm.dataPengadaan.tanggalNotaDinas,
                namaPengadaan: vm.dataPengadaan.namaPengadaan,
                nomorIzinPrinsip: vm.dataPengadaan.nomorIzinPrinsip,
                tanggalIzinPrinsip: vm.dataPengadaan.tanggalIzinPrinsip,
                mataUang: 1,
                kualifikasiPengadaan: null,
                jenisPengadaan: null,
                metodePengadaan: vm.dataPengadaan.metodePengadaan.id,
                metodePenyampaianDokumen: null,
                metodePenawaranHarga: vm.dataPengadaan.metodePenawaranHarga.id,
                jenisPenawaran: vm.dataPengadaan.jenisPenawaran.id,
                sistemEvaluasiPenawaran: vm.dataPengadaan.sistemEvaluasiPenawaran.id,
                kualifikasiVendor: vm.dataPengadaan.kualifikasiVendor.id,
                alurPengadaan: 1,
                tahapanPengadaan: vm.dataPengadaan.tahapanPengadaan.id,
                prosentasePajakMaterial: vm.dataPengadaan.prosentasePajakMaterial,
                prosentasePajakJasa: vm.dataPengadaan.prosentasePajakJasa
            };

            $scope.loading = true;
            $scope.message = ('Proses penyimpanan data pengadaan...');

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/updatePengadaan',
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
                    $scope.insertBidang(pengadaanId);
                    $scope.insertVendorRequirement(pengadaanId);
                    $scope.insertVendor(pengadaanId);
                    $scope.insertDokumenUpload(pengadaanId);
                    $scope.insertPengadaanSMB(pengadaanId);
                }
                $scope.loading = false;
                $location.path('/app/promise/procurement/inisialisasi/smb');
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
            }
            //URL Link
        vm.go = function (path) {
            $location.path(path);
        };
        $scope.loading = false;
    }

    InisialisasiPengadaanSMBEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader', '$q', '$stateParams'];

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
        }
    }
})();

angular.module('naut')
    .controller('ModalItemMaterialTotalCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, materialPengadaan) {

        $scope.materialPengadaan = {};

        if (materialPengadaan !== undefined && materialPengadaan !== null) {
            $scope.materialPengadaan = materialPengadaan;

            if ($scope.materialPengadaan.item !== undefined && $scope.materialPengadaan.item !== null &&
                $scope.materialPengadaan.item.deskripsi !== undefined && $scope.materialPengadaan.item.deskripsi !== null && $scope.materialPengadaan.item.deskripsi.length > 0
            ) {
                $scope.materialPengadaan.item.deskripsi.push($scope.materialPengadaan.item.deskripsi);
            }
        }

        // material item list
        $scope.refreshkodeMaterialList = function (kodeMaterial) {
            if (kodeMaterial !== undefined && kodeMaterial !== null && kodeMaterial.length > 0) {
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial)
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            } else {
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list')
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            }
        };

        //Currency
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                $scope.materialPengadaan.mataUang = data[0];
            }).error(function (data, status, headers, config) {});

        //calculate total HPS
        $scope.calculateTotalHPSItem = function () {
            if (typeof $scope.materialPengadaan.kuantitas !== 'undefined' && typeof $scope.materialPengadaan.nilaiHPS !== 'undefined') {
                $scope.materialPengadaan.totalHPS = $scope.materialPengadaan.kuantitas * $scope.materialPengadaan.nilaiHPS;
                if ($scope.materialPengadaan.mataUang.id == 1) { //Rupiah Default
                    $scope.materialPengadaan.totalHpsTxt = $filter('formatMoney')($scope.materialPengadaan.totalHPS, 2, ',', '.');
                } else {
                    $scope.materialPengadaan.totalHpsTxt = $filter('formatMoney')($scope.materialPengadaan.totalHPS, 2, '.', ',');
                }
            }
        }
        $scope.calculateTotalHPSItem();

        $scope.ok = function () {
            $modalInstance.close($scope.materialPengadaan);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

angular.module('naut')
    .controller('ModalItemMaterialSatuanCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, materialPengadaan) {

        $scope.materialPengadaan = {};

        if (materialPengadaan !== undefined && materialPengadaan !== null) {
            $scope.materialPengadaan = materialPengadaan;

            if ($scope.materialPengadaan.item !== undefined && $scope.materialPengadaan.item !== null &&
                $scope.materialPengadaan.item.deskripsi !== undefined && $scope.materialPengadaan.item.deskripsi !== null && $scope.materialPengadaan.item.deskripsi.length > 0
            ) {
                $scope.materialPengadaan.item.deskripsi.push($scope.materialPengadaan.item.deskripsi);
            }
        }

        // material item list
        $scope.refreshkodeMaterialList = function (kodeMaterial) {
            if (kodeMaterial !== undefined && kodeMaterial !== null && kodeMaterial.length > 0) {
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial)
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            } else {
                return $http.get($rootScope.backendAddress + '/procurement/master/itemmaster/item/get-list')
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            }
        };

        //Currency
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                $scope.materialPengadaan.mataUang = data[0];
            }).error(function (data, status, headers, config) {});

        //calculate total HPS
        $scope.calculateTotalHPSItem = function () {
            if (typeof $scope.materialPengadaan.kuantitas !== 'undefined' && typeof $scope.materialPengadaan.nilaiHPS !== 'undefined') {
                var total = 0;
                if ($scope.materialPengadaan.checked) {
                    total = ($scope.materialPengadaan.nilaiHPS * $scope.materialPengadaan.kuantitas) + (($scope.materialPengadaan.nilaiHPS * $scope.materialPengadaan.kuantitas) * $scope.materialPengadaan.prosentasePajak / 100);
                } else {
                    if (typeof $scope.materialPengadaan.jenisPajak !== 'undefined' && $scope.materialPengadaan.jenisPajak != null) {
                        total = ($scope.materialPengadaan.nilaiHPS * $scope.materialPengadaan.kuantitas) + (($scope.materialPengadaan.nilaiHPS * $scope.materialPengadaan.kuantitas) * $scope.materialPengadaan.jenisPajak.prosentase / 100);
                    } else {
                        total = $scope.materialPengadaan.nilaiHPS * $scope.materialPengadaan.kuantitas;
                    }
                }
                $scope.materialPengadaan.totalHPS = total;
                if ($scope.materialPengadaan.mataUang.id == 1) { //Rupiah Default
                    $scope.materialPengadaan.totalHpsTxt = $filter('formatMoney')(total, 2, ',', '.');
                } else {
                    $scope.materialPengadaan.totalHpsTxt = $filter('formatMoney')(total, 2, '.', ',');
                }
            }
        }
        $scope.calculateTotalHPSItem();

        $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
            .success(function (data, status, headers, config) {
                $scope.pajakList = data;
                $scope.materialPengadaan.jenisPajak = data[0];
            }).error(function (data, status, headers, config) {});

        $scope.checkPajak = function () {
            $scope.calculateTotalHPSItem();
        }

        $scope.checkTax = function () {
            if ($scope.materialPengadaan.checked == false) {
                $scope.materialPengadaan.checked = true;
                $scope.materialPengadaan.disabled = true;
            } else {
                $scope.materialPengadaan.checked = false;
                $scope.materialPengadaan.disabled = false;
            }
        }

        $scope.ok = function () {
            if (!$scope.materialPengadaan.checked) {
                $scope.materialPengadaan.prosentasePajak = 0;
            }
            $modalInstance.close($scope.materialPengadaan);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

angular.module('naut')
    .controller('ModalJasaMaterialTotalCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, jasaPengadaan) {

        $scope.jasaPengadaan = {};

        if (jasaPengadaan !== undefined && jasaPengadaan !== null) {
            $scope.jasaPengadaan = jasaPengadaan;

            if ($scope.jasaPengadaan.item !== undefined && $scope.jasaPengadaan.item !== null &&
                $scope.jasaPengadaan.item.deskripsi !== undefined && $scope.jasaPengadaan.item.deskripsi !== null && $scope.jasaPengadaan.item.deskripsi.length > 0
            ) {
                $scope.jasaPengadaan.item.deskripsi.push($scope.jasaPengadaan.item.deskripsi);
            }
        }

        // material item list
        $scope.refreshkodeMaterialList = function (kodeMaterial) {
            if (kodeMaterial !== undefined && kodeMaterial !== null && kodeMaterial.length > 0) {
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial)
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            } else {
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list')
                    .then(function (response) {
                        $scope.kodeMaterialList = response.data;
                    });
            }
        };

        //Currency
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                $scope.jasaPengadaan.mataUang = data[0];
            }).error(function (data, status, headers, config) {});

        //calculate total HPS
        $scope.calculateTotalHPSJasa = function () {
            if (typeof $scope.jasaPengadaan.kuantitas !== 'undefined' && typeof $scope.jasaPengadaan.nilaiHPS !== 'undefined') {
                $scope.jasaPengadaan.totalHPS = $scope.jasaPengadaan.kuantitas * $scope.jasaPengadaan.nilaiHPS;
                if ($scope.jasaPengadaan.mataUang.id == 1) { //Rupiah Default
                    $scope.jasaPengadaan.totalHpsTxt = $filter('formatMoney')($scope.jasaPengadaan.totalHPS, 2, ',', '.');
                } else {
                    $scope.jasaPengadaan.totalHpsTxt = $filter('formatMoney')($scope.jasaPengadaan.totalHPS, 2, '.', ',');
                }
            }
        }
        $scope.calculateTotalHPSJasa();

        $scope.ok = function () {
            $modalInstance.close($scope.jasaPengadaan);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

angular.module('naut')
    .controller('ModalJasaMaterialSatuanCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, jasaPengadaan) {

        $scope.jasaPengadaan = {};

        if (jasaPengadaan !== undefined && jasaPengadaan !== null) {
            $scope.jasaPengadaan = jasaPengadaan;
            if ($scope.jasaPengadaan.prosentasePajak > 0) {
                $scope.jasaPengadaan.checked = true;
                $scope.jasaPengadaan.disabled = true;
            } else {
                $scope.jasaPengadaan.checked = false;
                $scope.jasaPengadaan.disabled = false;
            }

            if ($scope.jasaPengadaan.item !== undefined && $scope.jasaPengadaan.item !== null &&
                $scope.jasaPengadaan.item.deskripsi !== undefined && $scope.jasaPengadaan.item.deskripsi !== null && $scope.jasaPengadaan.item.deskripsi.length > 0
            ) {
                $scope.jasaPengadaan.item.deskripsi.push($scope.jasaPengadaan.item.deskripsi);
            }
        }

        //Currency
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                $scope.jasaPengadaan.mataUang = data[0];
            }).error(function (data, status, headers, config) {});

        //calculate total HPS
        $scope.calculateTotalHPSJasa = function () {
            if (typeof $scope.jasaPengadaan.kuantitas !== 'undefined' && typeof $scope.jasaPengadaan.nilaiHPS !== 'undefined') {
                var total = 0;
                if ($scope.jasaPengadaan.checked) {
                    total = ($scope.jasaPengadaan.nilaiHPS * $scope.jasaPengadaan.kuantitas) + (($scope.jasaPengadaan.nilaiHPS * $scope.jasaPengadaan.kuantitas) * $scope.jasaPengadaan.prosentasePajak / 100);
                } else {
                    if (typeof $scope.jasaPengadaan.jenisPajak !== 'undefined' && $scope.jasaPengadaan.jenisPajak != null) {
                        total = ($scope.jasaPengadaan.nilaiHPS * $scope.jasaPengadaan.kuantitas) + (($scope.jasaPengadaan.nilaiHPS * $scope.jasaPengadaan.kuantitas) * $scope.jasaPengadaan.jenisPajak.prosentase / 100);
                    } else {
                        total = $scope.jasaPengadaan.nilaiHPS * $scope.jasaPengadaan.kuantitas;
                    }
                }
                $scope.jasaPengadaan.totalHPS = total;
                if ($scope.jasaPengadaan.mataUang.id == 1) { //Rupiah Default
                    $scope.jasaPengadaan.totalHpsTxt = $filter('formatMoney')(total, 2, ',', '.');
                } else {
                    $scope.jasaPengadaan.totalHpsTxt = $filter('formatMoney')(total, 2, '.', ',');
                }
            }
        }
        $scope.calculateTotalHPSJasa();

        $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
            .success(function (data, status, headers, config) {
                $scope.pajakList = data;
                $scope.jasaPengadaan.jenisPajak = data[0];
            }).error(function (data, status, headers, config) {});

        $scope.checkPajak = function () {
            $scope.calculateTotalHPSJasa();
        }

        $scope.checkTax = function () {
            if ($scope.jasaPengadaan.checked == false) {
                $scope.jasaPengadaan.checked = true;
                $scope.jasaPengadaan.disabled = true;
            } else {
                $scope.jasaPengadaan.checked = false;
                $scope.jasaPengadaan.disabled = false;
            }
        }
        $scope.ok = function () {
            $scope.namaError = false;
            $scope.kuantitasError = false;
            $scope.kuantitasError01 = false;
            $scope.hpsError = false;
            $scope.hpsError01 = false;
            if (typeof $scope.jasaPengadaan.item.nama === 'undefined' || $scope.jasaPengadaan.item.nama.length == 0) {
                $scope.namaError = true;
            } else {
                if (typeof $scope.jasaPengadaan.kuantitas === 'undefined') {
                    $scope.kuantitasError = true;
                } else {
                    if ($scope.jasaPengadaan.kuantitas <= 0) {
                        $scope.kuantitasError01 = true;
                    } else {
                        if (typeof $scope.jasaPengadaan.nilaiHPS === 'undefined') {
                            $scope.hpsError = true;
                        } else {
                            if ($scope.jasaPengadaan.nilaiHPS <= 0) {
                                $scope.hpsError01 = true;
                            } else {
                                $modalInstance.close($scope.jasaPengadaan);
                            }
                        }
                    }
                }
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
