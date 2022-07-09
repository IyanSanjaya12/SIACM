(function () {
    'use strict';

    angular
        .module('naut')
        .controller('InisialisasiPengadaanBumnEditController', InisialisasiPengadaanBumnEditController)
        .filter('propsFilter', propsFilter);

    function InisialisasiPengadaanBumnEditController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader, $q, $stateParams) {
        var vm = this;
        $scope.loading = true;
        vm.dataPengadaan = $stateParams.pengadaan;

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
            //ppk
            if (vm.dataPengadaan.ppk !== undefined && vm.dataPengadaan.ppk !== null && vm.dataPengadaan.ppk.length > 0) {
                vm.dataPengadaan.ppkError = false;
            } else {
                vm.dataPengadaan.ppkError = true;
                validateFormGeneral = true;
            }

            if (validateFormGeneral === false) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = true;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            }
        }

        vm.goToTabHPS = function () {
            var validateTabHPS = false;

            if (typeof vm.dataPengadaan.metodePenyampaianDokumenPengadaan === 'undefined') {
                vm.metodePenyampaianDokumenError = true;
                validateTabHPS = true;
            } else {
                vm.metodePenyampaianDokumenError = false;
            }

            if (typeof vm.dataPengadaan.metodePenawaranHarga === 'undefined') {
                vm.metodePenawaranHargaError = true;
                validateTabHPS = true;
            } else {
                vm.metodePenawaranHargaError = false;
            }

            if (typeof vm.dataPengadaan.jenisPenawaran === 'undefined') {
                vm.jenisPenawaranError = true;
                validateTabHPS = true;
            } else {
                vm.jenisPenawaranError = false;
            }

            if (typeof vm.dataPengadaan.sistemEvaluasiPenawaran === 'undefined') {
                vm.sistemEvaluasiPenawaranError = true;
                validateTabHPS = true;
            } else {
                vm.sistemEvaluasiPenawaranError = false;
            }

            if (validateTabHPS === false) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = true;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = false;
            }
        }

        vm.goToTabVendor = function () {
            var validateTabHPS = false;
            if (vm.dataPengadaan.materialPengadaanList !== undefined && vm.dataPengadaan.materialPengadaanList !== null && vm.dataPengadaan.materialPengadaanList.length > 0) {
                // material exist            	
            } else {
                if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {
                    // jasa exist
                } else {
                    validateTabHPS = true;
                }
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
            var validateTabVendor = false;
            if(typeof $scope.tableUndanganVendor === 'undefined' || $scope.tableUndanganVendor.length == 0) {
                validateTabVendor = true;
            }
            //            var validateSubBidangUsaha = false;
            //        	if (vm.dataPengadaan.segmentasiVendorStatus !== undefined && vm.dataPengadaan.segmentasiVendorStatus !== null && vm.dataPengadaan.segmentasiVendorStatus === true) {
            //        		if (vm.dataPengadaan.subBidangUsahaList !== undefined && vm.dataPengadaan.subBidangUsahaList !== null && vm.dataPengadaan.subBidangUsahaList.length > 0) {
            //        			
            //        		} else {
            //        			validateSubBidangUsaha = true;
            //        		}
            //        	} else {
            //    			validateTabVendor = true;
            //    		}
            //        	if (validateTabVendor === true) {
            //        		if (vm.dataPengadaan.pengalamanVendorStatus !== undefined && vm.dataPengadaan.pengalamanVendorStatus !== null && vm.dataPengadaan.pengalamanVendorStatus === true) {
            //                    if (vm.dataPengadaan.pengalamanVendor.minimalPengalaman !== undefined && vm.dataPengadaan.pengalamanVendor.minimalPengalaman !== null && vm.dataPengadaan.pengalamanVendor.minimalPengalaman > 0) {
            //                    	
            //                    } else {
            //            			validateTabVendor = true;
            //            		}
            //                    
            //                    if (vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai !== undefined && vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai !== null && vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai > 0) {
            //                    	
            //                    } else {
            //            			validateTabVendor = true;
            //            		}
            //                    
            //                    if (vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir !== undefined && vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir !== null && vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir > 0) {
            //                    	
            //                    } else {
            //            			validateTabVendor = true;
            //            		}
            //                    
            //                    if (vm.dataPengadaan.pengalamanVendor.mataUang !== undefined && vm.dataPengadaan.pengalamanVendor.mataUang !== null && vm.dataPengadaan.pengalamanVendor.mataUang.id > 0) {
            //                    	
            //                    } else {
            //            			validateTabVendor = true;
            //            		}
            //                    
            //                    if (vm.dataPengadaan.pengalamanVendor.nilaiKontrak !== undefined && vm.dataPengadaan.pengalamanVendor.nilaiKontrak !== null && vm.dataPengadaan.pengalamanVendor.nilaiKontrak > 0) {
            //                    	
            //                    } else {
            //            			validateTabVendor = true;
            //            		}
            //            	} else {
            //        			validateTabVendor = true;
            //        		}
            //        	}

            if (validateTabVendor) {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = true;
                vm.tabActiveStatus.tabDokumen = false;
            } else {
                vm.tabActiveStatus.tabGeneral = false;
                vm.tabActiveStatus.tabRules = false;
                vm.tabActiveStatus.tabHPS = false;
                vm.tabActiveStatus.tabVendor = false;
                vm.tabActiveStatus.tabDokumen = true;
            }
        }

        //Tab GENERAL
        vm.toggleMin = function () {
            vm.minDate = vm.minDate ? null : new Date();
        };
        vm.toggleMin();

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
        $http.get($rootScope.backendAddress + '/procurement/master/metodePenyampaianDokumenServices/listMetodePenyampaianDokumenPengadaan')
            .success(function (data, status, headers, config) {
                $scope.metodePenyampaianDokumenList = data;
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
                        if (vm.dataPengadaan.jenisPenawaran.id === 1) {
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
                    });
            }
        };
        $scope.autoLoadMaterial();

        $scope.updateJumlahMaterialNilaiHPSPPN = function () {
            if (vm.dataPengadaan.jenisPenawaran.id === 1) {
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
        }

        // tambah data material pengadaan
        vm.btnTambahMaterial = function () {
            var itemPengadaan = {};
            itemPengadaan.jenisPenawaran = vm.dataPengadaan.jenisPenawaran;
            var addModalItemMaterialTotal = $modal.open({
                templateUrl: '/kebutuhan.bumn.material.html',
                controller: 'ModalBUMNItemMaterialTotalCtrl',
                size: 'lg',
                resolve: {
                    materialPengadaan: function () {
                        return itemPengadaan;
                    }
                }
            });
            addModalItemMaterialTotal.result.then(function (materialPengadaan) {
                if (materialPengadaan != undefined && materialPengadaan !== null) {
                    vm.dataPengadaan.materialPengadaanList.push(materialPengadaan);
                    $scope.tableMaterialList.reload();
                    $scope.updateJumlahMaterialNilaiHPSPPN();
                }
            });
        }

        // edit mode material pengadaan
        $scope.editMaterial = function (index, itemPengadaan) {
            itemPengadaan.jenisPenawaran = vm.dataPengadaan.jenisPenawaran;
            var editModalItemMaterialTotal = $modal.open({
                templateUrl: '/kebutuhan.bumn.material.html',
                controller: 'ModalBUMNItemMaterialTotalCtrl',
                size: 'lg',
                resolve: {
                    materialPengadaan: function () {
                        return itemPengadaan;
                    }
                }
            });
            editModalItemMaterialTotal.result.then(function (materialPengadaan) {
                if (materialPengadaan != undefined && materialPengadaan !== null) {
                    vm.dataPengadaan.materialPengadaanList.splice(index, 1, materialPengadaan);
                    $scope.tableMaterialList.reload();
                    $scope.updateJumlahMaterialNilaiHPSPPN();
                }
            });
        };

        //Material Delete
        $scope.removeMaterial = function (index, materialObj) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: ModalInstanceCtrl,
                size: 'sm',
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
                        if (vm.dataPengadaan.jenisPenawaran.id === 1) {
                            vm.dataPengadaan.prosentasePajakJasa = 10;
                            vm.dataPengadaan.jumlahJasaTotalHPS = 0;
                            if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {
                                vm.dataPengadaan.prosentasePajakJasa = vm.dataPengadaan.jasaPengadaanList[0].jenisPajak.prosentase;
                                angular.forEach(vm.dataPengadaan.jasaPengadaanList, function (value, key) {
                                    vm.dataPengadaan.jumlahJasaTotalHPS = vm.dataPengadaan.jumlahJasaTotalHPS + value.totalHPS;
                                    // nilai pajak untuk setiap record 
                                    value.nilaiPajak = vm.dataPengadaan.prosentasePajakJasa * value.nilaiHPS / 100;
                                });
                            }
                            vm.dataPengadaan.jumlahJasaNilaiHPSPPN = vm.dataPengadaan.jumlahJasaTotalHPS * vm.dataPengadaan.prosentasePajakJasa / 100;
                            vm.dataPengadaan.jumlahJasaNilaiHPSDenganPPN = vm.dataPengadaan.jumlahJasaTotalHPS + vm.dataPengadaan.jumlahJasaNilaiHPSPPN;
                        }
                    });
            }
        };
        $scope.autoLoadJasa();

        $scope.updateJumlahJasaNilaiHPSPPN = function () {
            if (vm.dataPengadaan.jenisPenawaran.id === 1) {
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
        }

        // tambah data jasa pengadaan
        vm.btnTambahJasa = function () {
            var jasaPengadaan = {};
            jasaPengadaan.jenisPenawaran = vm.dataPengadaan.jenisPenawaran;
            var modalJasaMaterialTotal = $modal.open({
                templateUrl: '/kebutuhan.bumn.jasa.html',
                controller: 'ModalBUMNJasaMaterialTotalCtrl',
                size: 'lg',
                resolve: {
                    jasaPengadaan: function () {
                        return jasaPengadaan;
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
        }

        // edit jasa
        $scope.editJasa = function (index, jasaPengadaan) {
            jasaPengadaan.jenisPenawaran = vm.dataPengadaan.jenisPenawaran;
            var modalJasaMaterialTotal = $modal.open({
                templateUrl: '/kebutuhan.bumn.jasa.html',
                controller: 'ModalBUMNJasaMaterialTotalCtrl',
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
        }

        //Jasa Delete
        $scope.removeJasa = function (index, jasaObject) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTableJasa2.html',
                controller: ModalInstanceJasaCtrl,
                size: 'sm',
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
                            value.subBidangUsaha.bidangUsahaPengadaanId = value.id;
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
                templateUrl: '/bidangusahabumn.html',
                controller: 'BidangUsahaPengadaanBUMNCtlr',
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
                size: 'sm',
                resolve: {
                    materialName: function () {
                        return subBidangUsaha.bidangUsaha.nama + ' - ' + subBidangUsaha.nama;
                    }
                }
            });
            modalInstance.result.then(function () {
                vm.dataPengadaan.subBidangUsahaList.splice(index, 1);
                if (subBidangUsaha.bidangUsahaPengadaanId !== undefined && subBidangUsaha.bidangUsahaPengadaanId !== null) {
                    if (vm.dataPengadaan.deleteSubBidangUsahaList === undefined || vm.dataPengadaan.deleteSubBidangUsahaList === null) {
                        vm.dataPengadaan.deleteSubBidangUsahaList = [];
                    }
                    vm.dataPengadaan.deleteSubBidangUsahaList.push(subBidangUsaha);
                }
                $scope.tableBidangUsahaVendor.reload();
            });
        }

        // pengalaman vendor
        $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementListByPengadaanId/' + vm.dataPengadaan.id)
            .success(function (data, status, headers, config) {
                if (data !== undefined && data !== null && data.id !== undefined && data.id !== null && data.id > 0) {
                    vm.dataPengadaan.pengalamanVendorStatus = true;
                    vm.dataPengadaan.pengalamanVendor = data;
                }
            });

        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.pengalamanMataUangList = data;
            }).error(function (data, status, headers, config) {});

        // jika metode pengadaan adalah penunjukan langsung request undangan vendor
        vm.dataPengadaan.undanganVendorStatus = false;
        if (vm.dataPengadaan.metodePengadaan.id === 5) {
            vm.dataPengadaan.undanganVendorStatus = true;
            if (vm.dataPengadaan.undanganVendorList !== undefined && vm.dataPengadaan.undanganVendorList !== null && vm.dataPengadaan.undanganVendorList.length > 0) {

            } else {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/getUndanganVendorListByPengadaanId/' + vm.dataPengadaan.id)
                    .success(function (data, status, headers, config) {
                        vm.dataPengadaan.undanganVendorList = [];
                        if (data !== undefined && data !== null && data.length > 0) {
                            angular.forEach(data, function (undanganVendor, index) {
                                undanganVendor.vendor.undanganVendorId = undanganVendor.id;
                                vm.dataPengadaan.undanganVendorList.push(undanganVendor.vendor);
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
                templateUrl: '/vendorpengadaanbumn.html',
                controller: 'UndangVendorPengadaanBUMNCtlr',
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

        $scope.removeVendor = function (vendorObj, index) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable3.html',
                controller: ModalInstanceCtrl,
                size: 'sm',
                resolve: {
                    materialName: function () {
                        return vendorObj.nama + ', ' + vendorObj.alamat;
                    }
                }
            });
            modalInstance.result.then(function () {
                vm.dataPengadaan.undanganVendorList.splice(index, 1);
                if (vendorObj.undanganVendorId !== undefined && vendorObj.undanganVendorId !== null) {
                    if (vm.dataPengadaan.deleteUndanganVendorList === undefined || vm.dataPengadaan.deleteUndanganVendorList === null) {
                        vm.dataPengadaan.deleteUndanganVendorList = [];
                    }
                    vm.dataPengadaan.deleteUndanganVendorList.push(vendorObj);
                }
                $scope.tableUndanganVendor.reload();
            });
        }

        //Tab 4 Upload file        
        $scope.btnSimpanInisialisasiStatus = true;
        $scope.btnKembaliIndexStatus = false;

        var uploader = $scope.uploader = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });

        // request dokumen pengadaan        
        $http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + vm.dataPengadaan.id)
            .success(function (data, status, headers, config) {
                vm.dataPengadaan.dokumenPengadaanList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < vm.dataPengadaan.dokumenPengadaanList.length; y++) {
                    var file = {
                        name: vm.dataPengadaan.dokumenPengadaanList[y].nama,
                        size: vm.dataPengadaan.dokumenPengadaanList[y].fileSize
                    };
                    uploader.addToQueue(file, undefined, undefined);
                    var item = uploader.queue[uploader.queue.length - 1];
                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                    item.dokumenPengadaanId = vm.dataPengadaan.dokumenPengadaanList[y].id;
                    /*added new attribute & properties to store realFileName*/
                    item.realFileName = vm.dataPengadaan.dokumenPengadaanList[y].realFileName;
                }
            });

        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item, options) {
                return this.queue.length < 10;
            }
        });
        // CALLBACKS
        //        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
        //            console.info('onWhenAddingFileFailed', item, filter, options);
        //        };
        //        uploader.onAfterAddingFile = function (fileItem) {
        //            //console.info('onAfterAddingFile', fileItem);
        //        };
        //        uploader.onAfterAddingAll = function (addedFileItems) {
        //            //console.info('onAfterAddingAll', addedFileItems);
        //        };
        //        uploader.onBeforeUploadItem = function (item) {
        //            //console.info('onBeforeUploadItem', item);
        //        };
        //        uploader.onProgressItem = function (fileItem, progress) {
        //            //console.info('onProgressItem', fileItem, progress);
        //        };
        //        uploader.onProgressAll = function (progress) {
        //            //console.info('onProgressAll', progress);
        //        };
        //        uploader.onSuccessItem = function (fileItem, response, status, headers) {
        //            //console.info('onSuccessItem', fileItem, response, status, headers);
        //        };
        //        uploader.onErrorItem = function (fileItem, response, status, headers) {
        //            //console.info('onErrorItem', fileItem, response, status, headers);
        //        };
        //        uploader.onCancelItem = function (fileItem, response, status, headers) {
        //            console.info('onCancelItem', fileItem, response, status, headers);
        //        };
        //        uploader.onCompleteItem = function (fileItem, response, status, headers) {
        //            if (typeof vm.dataPengadaan.fileUpload === 'undefined') {
        //                vm.dataPengadaan.fileUpload = [];
        //                var fileObj = {
        //                    fileName: response.fileName
        //                };
        //                vm.dataPengadaan.fileUpload = vm.dataPengadaan.fileUpload.concat(fileObj);
        //                vm.dataPengadaan.uploader = $scope.uploader;
        //            } else {
        //                var fileObj = {
        //                    fileName: response.fileName
        //                };
        //                vm.dataPengadaan.fileUpload = vm.dataPengadaan.fileUpload.concat(fileObj);
        //                $scope.uploader = vm.dataPengadaan.uploader;
        //            }
        //        };
        //validasi tahun mulai akhir


        // save all data
        $scope.btnUpdateInisialisasi = function () {
            //pengadaan
            var formPostPengadaan = {
                id: vm.dataPengadaan.id,
                nomorNotaDinas: vm.dataPengadaan.nomorNotaDinas,
                tanggalNotaDinas: $filter('date')(vm.dataPengadaan.dokumenTanggal, 'dd-MM-yyyy'),
                namaPengadaan: vm.dataPengadaan.namaPengadaan,
                ppk: vm.dataPengadaan.ppk,
                mataUang: 1,
                kualifikasiPengadaan: vm.dataPengadaan.kualifikasiPengadaan.id,
                jenisPengadaan: vm.dataPengadaan.jenisPengadaan.id,
                metodePengadaan: vm.dataPengadaan.metodePengadaan.id,
                metodePenyampaianDokumen: vm.dataPengadaan.metodePenyampaianDokumenPengadaan.id,
                metodePenawaranHarga: vm.dataPengadaan.metodePenawaranHarga.id,
                jenisPenawaran: vm.dataPengadaan.jenisPenawaran.id,
                sistemEvaluasiPenawaran: vm.dataPengadaan.sistemEvaluasiPenawaran.id,
                alurPengadaan: 1,
                tahapanPengadaan: vm.dataPengadaan.tahapanPengadaan.id
            };
            if (vm.dataPengadaan.pp !== undefined && vm.dataPengadaan.pp !== null) {
                formPostPengadaan.pp = vm.dataPengadaan.pp;
            }
            if (vm.dataPengadaan.nomorIzinPrinsip !== undefined && vm.dataPengadaan.nomorIzinPrinsip !== null) {
                formPostPengadaan.nomorIzinPrinsip = vm.dataPengadaan.nomorIzinPrinsip;
            }
            if (vm.dataPengadaan.tanggalIzinPrinsip !== undefined && vm.dataPengadaan.tanggalIzinPrinsip !== null) {
                formPostPengadaan.tanggalIzinPrinsip = vm.dataPengadaan.tanggalIzinPrinsip;
            }
            if (vm.dataPengadaan.jenisPenawaran.id === 1) {
                formPostPengadaan.prosentasePajakMaterial = vm.dataPengadaan.prosentasePajakMaterial;
                formPostPengadaan.prosentasePajakJasa = vm.dataPengadaan.prosentasePajakJasa;
            }
            if (vm.dataPengadaan.kualifikasiVendor !== undefined && vm.dataPengadaan.kualifikasiVendor !== null) {
                formPostPengadaan.kualifikasiVendor = vm.dataPengadaan.kualifikasiVendor.id;
            }

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
                    $scope.message = ('Proses penyimpanan data pengadaan... BERHASIL');
                    //proses simpan item material
                    $scope.insertItemPengadaanMaterial(vm.dataPengadaan.id);
                    // proses simpan item jasa
                    $scope.insertJasaIntoItem(vm.dataPengadaan.id);
                    // proses simpan bidang
                    if (vm.dataPengadaan.segmentasiVendorStatus !== undefined && vm.dataPengadaan.segmentasiVendorStatus !== null && vm.dataPengadaan.segmentasiVendorStatus === true) {
                        $scope.insertBidang(vm.dataPengadaan.id);
                    }
                    // proses simpan vendor requirement
                    if (vm.dataPengadaan.pengalamanVendorStatus !== undefined && vm.dataPengadaan.pengalamanVendorStatus !== null && vm.dataPengadaan.pengalamanVendorStatus === true) {
                        $scope.insertVendorRequirement(vm.dataPengadaan.id);
                    }
                    // proses simpan vendor
                    if (vm.dataPengadaan.metodePengadaan.id === 5) {
                        $scope.insertVendor(vm.dataPengadaan.id);
                    }
                    // proses simpan DokumenUpload
                    $scope.insertDokumenUpload(vm.dataPengadaan.id);
                }
                $scope.loading = false;

                $scope.btnSimpanInisialisasiStatus = false;
                $scope.btnKembaliIndexStatus = true;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.message = "Error simpan pengadaan";
            });
        }

        var dataService = function (targetURI, paramData) {
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
                data: paramData
            }).success(function (data, status, headers, config) {
                $scope.message = 'Proses Penyimpanan data gagal';
            });
        }

        $scope.insertItemPengadaanMaterial = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data material pengadaan...');
            //material pengadaan
            angular.forEach(vm.dataPengadaan.materialPengadaanList, function (dataMaterial, index) {
                var formItemPengadaan = {
                    id: dataMaterial.id,
                    pengadaan: pengadaanId,
                    item: dataMaterial.item.id,
                    mataUang: dataMaterial.mataUang.id,
                    kuantitas: dataMaterial.kuantitas,
                    nilaiHPS: dataMaterial.nilaiHPS,
                    totalHPS: dataMaterial.totalHPS
                };

                if (vm.dataPengadaan.jenisPenawaran.id === 2) {
                    formItemPengadaan.jenisPajak = dataMaterial.jenisPajak.id;
                    if (dataMaterial.prosentasePajak !== undefined && dataMaterial.prosentasePajak !== null && dataMaterial.prosentasePajak > 0) {
                        formItemPengadaan.prosentasePajak = dataMaterial.prosentasePajak;
                        formItemPengadaan.nilaiPajak = dataMaterial.prosentasePajak * dataMaterial.totalHPS;
                    } else {
                        formItemPengadaan.nilaiPajak = dataMaterial.jenisPajak.prosentase * dataMaterial.totalHPS;
                    }
                }

                var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanTotal';
                if (dataMaterial.id !== undefined && dataMaterial.id !== null && dataMaterial.id > 0) {
                    targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaan';
                    if (vm.dataPengadaan.jenisPenawaran.id === 2) {
                        targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaanSatuan';
                    }
                } else {
                    if (vm.dataPengadaan.jenisPenawaran.id === 2) {
                        targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
                    }
                }


                dataService(targetURI, formItemPengadaan);
            });

            // delete data material
            if (vm.dataPengadaan.deleteMaterialPengadaanList !== undefined && vm.dataPengadaan.deleteMaterialPengadaanList !== null && vm.dataPengadaan.deleteMaterialPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.deleteMaterialPengadaanList, function (deleteMaterial, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/deleteRowItemPengadaan/' + deleteMaterial.id;

                    dataService(targetURI);
                });
            }
        }

        $scope.insertJasaIntoItem = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data jasa pengadaan...');
            //jasa pengadaan
            if (vm.dataPengadaan.jasaPengadaanList !== undefined && vm.dataPengadaan.jasaPengadaanList !== null && vm.dataPengadaan.jasaPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.jasaPengadaanList, function (jasaPengadaan, index) {
                    if (jasaPengadaan.item.id !== undefined && jasaPengadaan.item.id !== null && jasaPengadaan.item.id > 0) {
                        $scope.insertJasaPengadaan(pengadaanId, jasaPengadaan, jasaPengadaan.item.id);
                    } else {
                        // insert into item
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
                            data: {
                                nama: jasaPengadaan.item.nama,
                                satuanId: 5,
                                itemTypeId: 2,
                                itemGroupId: 2,
                                deskripsi: jasaPengadaan.item.deskripsi
                            }
                        }).success(function (data, status, headers, config) {
                            if (typeof data.id !== 'undefined') {
                                $scope.insertJasaPengadaan(pengadaanId, jasaPengadaan, data.id);
                                $scope.message = ('Proses penyimpanan item jasa... ' + (((index + 1) / 100) * 100) + '%');
                            }
                        });
                    }
                });
            }

            // delete data pengadaan jasa
            if (vm.dataPengadaan.deleteJasaPengadaanList !== undefined && vm.dataPengadaan.deleteJasaPengadaanList !== null && vm.dataPengadaan.deleteJasaPengadaanList.length > 0) {
                angular.forEach(vm.dataPengadaan.deleteJasaPengadaanList, function (deleteJasa, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/deleteRowItemPengadaan/' + deleteJasa.id;
                    dataService(targetURI);
                });
            }
        }

        $scope.insertJasaPengadaan = function (pengadaanId, jasaPengadaan, itemId) {
            //insert to item pengadaan untuk jasa
            var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/insertItemPengadaanSatuan';
            if (jasaPengadaan.id !== undefined && jasaPengadaan.id !== null && jasaPengadaan.id > 0) {
                targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/updateItemPengadaan';
            }

            var formJasaPengadaan = {
                id: jasaPengadaan.id,
                pengadaan: pengadaanId,
                item: itemId,
                mataUang: jasaPengadaan.mataUang.id,
                kuantitas: jasaPengadaan.kuantitas,
                nilaiHPS: jasaPengadaan.nilaiHPS,
                totalHPS: jasaPengadaan.totalHPS,
                nilaiPajak: jasaPengadaan.nilaiPajak,
                jenisPajak: jasaPengadaan.jenisPajak.id
            };

            if (jasaPengadaan.jenisPenawaran.id === 2) {
                formJasaPengadaan.jenisPajak = jasaPengadaan.jenisPajak.id;
                formJasaPengadaan.nilaiPajak = jasaPengadaan.nilaiPajak;
                if (jasaPengadaan.prosentasePajak !== undefined && jasaPengadaan.prosentasePajak !== null && jasaPengadaan.prosentasePajak > 0) {
                    formJasaPengadaan.prosentasePajak = jasaPengadaan.prosentasePajak;
                }
            }

            dataService(targetURI, formJasaPengadaan);
        }

        $scope.insertBidang = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data bidang pengadaan...');
            //bidang
            if (vm.dataPengadaan.subBidangUsahaList !== undefined && vm.dataPengadaan.subBidangUsahaList !== null && vm.dataPengadaan.subBidangUsahaList.length > 0) {
                angular.forEach(vm.dataPengadaan.subBidangUsahaList, function (dataSubBidangUsaha, index) {
                    var formSubBidangPengadaan = {
                        id: dataSubBidangUsaha.bidangUsahaPengadaanId,
                        pengadaanId: pengadaanId,
                        subBidangId: dataSubBidangUsaha.id
                    }

                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/insertBidangUsahaPengadaan';
                    if (dataSubBidangUsaha.bidangUsahaPengadaanId !== undefined && dataSubBidangUsaha.bidangUsahaPengadaanId !== null && dataSubBidangUsaha.bidangUsahaPengadaanId > 0) {
                        targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/updateBidangUsahaPengadaan';
                    }

                    dataService(targetURI, formSubBidangPengadaan);
                });
            }

            // delete jika ada data sub bidang yang di delete
            if (vm.dataPengadaan.deleteSubBidangUsahaList !== undefined && vm.dataPengadaan.deleteSubBidangUsahaList !== null && vm.dataPengadaan.deleteSubBidangUsahaList.length > 0) {
                angular.forEach(vm.dataPengadaan.deleteSubBidangUsahaList, function (deleteSubBidangUsaha, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/deleteRowBidangUsahaPengadaan/' + bidangUsahaPengadaanId;
                    dataService(targetURI);
                });
            }
        }

        $scope.insertVendorRequirement = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data vendor requirement pengadaan...');
            //vendor requirement
            if (vm.dataPengadaan.pengalamanVendor !== undefined && vm.dataPengadaan.pengalamanVendor !== null) {
                var targetURI = $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/insertVendorRequirement';
                if (vm.dataPengadaan.pengalamanVendor.id !== undefined && vm.dataPengadaan.pengalamanVendor.id !== null && vm.dataPengadaan.pengalamanVendor.id > 0) {
                    targetURI = $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/updateVendorRequirement';
                }
                var formVendorRequirement = {
                    id: vm.dataPengadaan.pengalamanVendor.id,
                    pengadaan: pengadaanId
                };

                if (vm.dataPengadaan.pengalamanVendor.minimalPengalaman !== undefined && vm.dataPengadaan.pengalamanVendor.minimalPengalaman !== null && vm.dataPengadaan.pengalamanVendor.minimalPengalaman > 0) {
                    formVendorRequirement.minimalPengalaman = vm.dataPengadaan.pengalamanVendor.minimalPengalaman;
                }

                if (vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai !== undefined && vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai !== null && vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai > 0) {
                    formVendorRequirement.tahunPengalamanMulai = vm.dataPengadaan.pengalamanVendor.tahunPengalamanMulai;
                }

                if (vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir !== undefined && vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir !== null && vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir > 0) {
                    formVendorRequirement.tahunPengalamanAkhir = vm.dataPengadaan.pengalamanVendor.tahunPengalamanAkhir;
                }
                if (vm.dataPengadaan.pengalamanVendor.mataUang !== undefined && vm.dataPengadaan.pengalamanVendor.mataUang !== null) {
                    formVendorRequirement.mataUang = vm.dataPengadaan.pengalamanVendor.mataUang.id;
                }

                if (vm.dataPengadaan.pengalamanVendor.nilaiKontrak !== undefined && vm.dataPengadaan.pengalamanVendor.nilaiKontrak !== null && vm.dataPengadaan.pengalamanVendor.nilaiKontrak > 0) {
                    formVendorRequirement.nilaiKontrak = vm.dataPengadaan.pengalamanVendor.nilaiKontrak;
                }
                dataService(targetURI, formVendorRequirement);
            }
        }

        $scope.invalidTahun = function (x,y) {
            if (x > y) {
                alert('Tahun Pengalaman Awal lebih besar dari Tahun Pengalaman Akhir');
            } else {
                vm.goToTabDokumen(); 
            }
        }

        $scope.insertVendor = function (pengadaanId) {
            $scope.message = ('Proses penyimpanan data undangan vendor pengadaan... ');
            // vendor
            if (vm.dataPengadaan.undanganVendorList !== undefined && vm.dataPengadaan.undanganVendorList !== null && vm.dataPengadaan.undanganVendorList.length > 0) {
                angular.forEach(vm.dataPengadaan.undanganVendorList, function (dataVendor, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/insertUndanganVendor';
                    if (dataVendor.undanganVendorId !== undefined && dataVendor.undanganVendorId !== null && dataVendor.undanganVendorId > 0) {
                        targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/updateUndanganVendor';
                    }
                    var formVendorUndangan = {
                        id: dataVendor.undanganVendorId,
                        pengadaan: pengadaanId,
                        vendor: dataVendor.id
                    }
                    dataService(targetURI, formVendorUndangan);
                });

                // delete data vendor that exist in database
                if (vm.dataPengadaan.deleteUndanganVendorList !== undefined && vm.dataPengadaan.deleteUndanganVendorList !== null && vm.dataPengadaan.deleteUndanganVendorList.length > 0) {
                    angular.forEach(vm.dataPengadaan.deleteUndanganVendorList, function (deleteUndanganVendor, index) {
                        var targetURI = $rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/deleteRowUndanganVendor/' + deleteUndanganVendor.undanganVendorId;
                        dataService(targetURI);
                    });
                }
            }
        }

        $scope.insertDokumenUpload = function (pengadaanId) {
            //dokumen upload
            angular.forEach(uploader.queue, function (item) {
                var formDokumenUpload = {
                    id: item.dokumenPengadaanId,
                    nama: item.file.name,
                    pengadaan: pengadaanId,
                    path: '/pengadaan/' + item.file.name,
                    fileSize: 10000
                }
                var targetURI = $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/insertDokumenPengadaan';
                if (item.dokumenPengadaanId !== undefined && item.dokumenPengadaanId !== null && item.dokumenPengadaanId > 0) {
                    targetURI = $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/updateDokumenPengadaan';
                }
                dataService(targetURI, formDokumenUpload);
            });

            if (vm.dataPengadaan.deleteDokumenPengadaanList !== undefined) {
                angular.forEach(vm.dataPengadaan.deleteDokumenPengadaanList, function (dataDokumenPengadaan, index) {
                    var targetURI = $rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/deleteRowDokumenPengadaan/' + dataDokumenPengadaan.dokumenPengadaanId;
                    dataService(targetURI);
                });
            }
        }

        $scope.removeItemDokumen = function (item) {
            item.remove();
            if (item.dokumenPengadaanId !== undefined && item.dokumenPengadaanId !== null) {
                if (vm.dataPengadaan.deleteDokumenPengadaanList === undefined || vm.dataPengadaan.deleteDokumenPengadaanList === null) {
                    vm.dataPengadaan.deleteDokumenPengadaanList = [];
                }
                vm.dataPengadaan.deleteDokumenPengadaanList.push(item);
            }
        }

        $scope.btnKembaliIndex = function () {
            $location.path('/app/promise/procurement/inisialisasi');
        }

        //class utils
        //Auto Tab
        //        $scope.getAutoTab = function () {
        //            var status = "tab0";
        //            if (typeof vm.dataPengadaan.tabActiveFrom !== 'undefined') {
        //                status = vm.dataPengadaan.tabActiveFrom;
        //            }
        //            //console.log('TAB : '+status);
        //            return status;
        //        }
        //URL Link
        vm.go = function (path) {
            $location.path(path);
        };
        $scope.loading = false;
    }

    InisialisasiPengadaanBumnEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader', '$q', '$stateParams'];

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
    .controller('ModalBUMNItemMaterialTotalCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, materialPengadaan) {

        $scope.materialPengadaan = {};

        if (materialPengadaan !== undefined && materialPengadaan !== null) {
            $scope.materialPengadaan = materialPengadaan;

            if ($scope.materialPengadaan.item !== undefined && $scope.materialPengadaan.item !== null &&
                $scope.materialPengadaan.item.deskripsi !== undefined && $scope.materialPengadaan.item.deskripsi !== null && $scope.materialPengadaan.item.deskripsi.length > 0
            ) {
                $scope.materialPengadaan.item.deskripsi.push($scope.materialPengadaan.item.deskripsi);
            }

            if ($scope.materialPengadaan.prosentasePajak !== undefined && $scope.materialPengadaan.prosentasePajak !== null && $scope.materialPengadaan.prosentasePajak > 0) {
                $scope.materialPengadaan.checkStatusPajak = true;
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
            });

        if ($scope.materialPengadaan.jenisPenawaran.id === 2) {
            $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
                .success(function (data, status, headers, config) {
                    $scope.pajakList = data;
                }).error(function (data, status, headers, config) {});
        }

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
    .controller('ModalBUMNJasaMaterialTotalCtrl', function ($rootScope, $scope, $http, $filter, $modalInstance, jasaPengadaan) {

        $scope.jasaPengadaan = {};

        if (jasaPengadaan !== undefined && jasaPengadaan !== null) {
            $scope.jasaPengadaan = jasaPengadaan;

            if ($scope.jasaPengadaan.item !== undefined && $scope.jasaPengadaan.item !== null &&
                $scope.jasaPengadaan.item.deskripsi !== undefined && $scope.jasaPengadaan.item.deskripsi !== null && $scope.jasaPengadaan.item.deskripsi.length > 0
            ) {
                $scope.jasaPengadaan.item.deskripsi.push($scope.jasaPengadaan.item.deskripsi);
            }

            if ($scope.jasaPengadaan.prosentasePajak !== undefined && $scope.jasaPengadaan.prosentasePajak !== null && $scope.jasaPengadaan.prosentasePajak > 0) {
                $scope.jasaPengadaan.checkStatusPajak = true;
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

        if ($scope.jasaPengadaan.jenisPenawaran.id === 2) {
            $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
                .success(function (data, status, headers, config) {
                    $scope.pajakList = data;
                });
        }

        $scope.ok = function () {
            $modalInstance.close($scope.jasaPengadaan);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

angular.module('naut')
    .controller('BidangUsahaPengadaanBUMNCtlr', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, dataBidangUsahaList) {
        $scope.dataBidangUsahaList = dataBidangUsahaList;

        $http.get($rootScope.backendAddress + '/procurement/master/bidang-usaha/get-bidang-usaha-by-parent-id/0')
            .success(function (data, status, headers, config) {
                $scope.bidangUsahaList = data;
            });

        $scope.dataSubBidangUsahaList = [];
        $scope.tableSubBidangUsahaList = new ngTableParams({
            page: 1, // show first page
            count: 10 // count per page
        }, {
            total: $scope.dataSubBidangUsahaList.length, // length of data4
            getData: function ($defer, params) {
                $defer.resolve($scope.dataSubBidangUsahaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });

        $scope.getSubBidangUsaha = function (item) {
            $http.get($rootScope.backendAddress + '/procurement/master/sub-bidang-usaha/get-by-bidang-usaha-id/' + item.id)
                .success(function (data, status, headers, config) {
                    $scope.dataSubBidangUsahaList = data;
                    angular.forEach($scope.dataSubBidangUsahaList, function (dataSubBidang, indexSubBidang) {
                        angular.forEach($scope.dataBidangUsahaList, function (dataBidang, indexBidang) {
                            if (dataSubBidang.id === dataBidang.id) {
                                dataSubBidang.checkSubBidangStatus = true;
                            }
                        });
                    });

                    $scope.tableSubBidangUsahaList.reload();
                });
        }

        $scope.btnSimpanBidang = function () {
            var outSubBidangUsahaList = [];
            angular.forEach($scope.dataSubBidangUsahaList, function (subBidangUsaha, index) {
                if (subBidangUsaha.checkSubBidangStatus === true) {
                    outSubBidangUsahaList.push(subBidangUsaha);
                }
            });
            $modalInstance.close(outSubBidangUsahaList);
        }
        $scope.btnBatal = function () {
            $modalInstance.dismiss('cancel');
        }
    });

angular.module('naut')
    .controller('UndangVendorPengadaanBUMNCtlr', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, undanganVendorList) {
        $scope.undanganVendorList = undanganVendorList;

        $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorList')
            .success(function (data, status, headers, config) {
                $scope.dataVendorList = data;
                angular.forEach($scope.dataVendorList, function (dataVendor, indexVendor) {
                    angular.forEach($scope.undanganVendorList, function (dataUndanganVendor, indexUndanganVendor) {
                        if (dataVendor.id === dataUndanganVendor.id) {
                            dataVendor.checkUndanganVendor = true;
                        }
                    });
                });

                $scope.tableVendorList = new ngTableParams({
                    page: 1, // show first page
                    count: 10 // count per page
                }, {
                    total: $scope.dataVendorList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve($scope.dataVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });

        $scope.btnSimpanVendor = function () {
            var outUndanganVendorList = [];
            angular.forEach($scope.dataVendorList, function (value, index) {
                if (value.checkUndanganVendor === true) {
                    outUndanganVendorList.push(value);
                }
            });
            $modalInstance.close(outUndanganVendorList);
        }
        $scope.btnBatal = function () {
            $modalInstance.dismiss('cancel');
        }
    });
