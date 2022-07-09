(function () {
    'use strict';

    angular
        .module('naut')
        .controller('CreateNewKontrakController', CreateNewKontrakController);

    function CreateNewKontrakController($scope, $http, $rootScope, $resource, $location, $stateParams, ngTableParams, $state, $filter, $modal) {
        var vm = this;

        vm.pengadaan = $stateParams.dataPengadaan;
        vm.pengadaan.kursdollar = 0;

        //list Material
        vm.getItemPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/perolehan/PerolehanPengadaanSatuanServices/findByPengadaan/' + vm.pengadaan.id)
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    vm.pengadaan.materialPengadaanList = [];
                    vm.pengadaan.jasaPengadaanList = [];
                    vm.pengadaan.totalNilaiMaterial = 0;
                    vm.pengadaan.totalNilaiJasa = 0;

                    if (data !== undefined && data.length > 0) {
                        vm.pengadaan.valutaItemPengadaan = data[0].mataUang;
                        angular.forEach(data, function (perolehan, index) {
                            if (perolehan.itemPengadaan.item.itemType.id === 1) {
                                vm.pengadaan.materialPengadaanList.push(perolehan);
                                vm.pengadaan.totalNilaiMaterial = vm.pengadaan.totalNilaiMaterial + perolehan.nilaiTotal;
                            } else {
                                vm.pengadaan.jasaPengadaanList.push(perolehan);
                                vm.pengadaan.totalNilaiJasa = vm.pengadaan.totalNilaiJasa + perolehan.nilaiTotal;
                            }
                        });
                    }

                    $scope.tableMaterialPengadaan = new ngTableParams({
                        page: 1,
                        count: 10
                    }, {
                        total: vm.pengadaan.materialPengadaanList.length,
                        getData: function ($defer, params) {
                            $defer.resolve(vm.pengadaan.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                        }
                    });

                    $scope.tableJasaPengadaan = new ngTableParams({
                        page: 1,
                        count: 10
                    }, {
                        total: vm.pengadaan.jasaPengadaanList.length,
                        getData: function ($defer, params) {
                            $defer.resolve(vm.pengadaan.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                        }
                    });
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        vm.getItemPengadaan();

        // surat penawaran
        vm.getSuratPenawaran = function () {
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + vm.pengadaan.id)
                .success(function (data, status, headers, config) {
                    vm.pengadaan.suratpenawaran = data[0];
                });
        }
        vm.getSuratPenawaran();

        // nilai perolehan untuk setiap item
        vm.getNilaiPerolehan = function () {
            $http.get($rootScope.backendAddress + '/procurement/perolehan/PerolehanPengadaanTotalServices/findByPengadaanAndVendor/' + vm.pengadaan.id + '/' + vm.pengadaan.vendor.id)
                .success(function (data, status, headers, config) {
                    vm.pengadaan.perolehan = data;
                });

        }
        vm.getNilaiPerolehan();

        // setting datepicker
        $scope.status = {
            tglSPKOpened: false,
            tglMulaiOpened: false,
            tglSelesaiOpened: false,
            tglBatasKirimOpened: false,
            tglBatasJaminanOpened: false
        };

        $scope.openTglSPK = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.tglSPKOpened = true;
        };

        $scope.openTglMulai = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.tglMulaiOpened = true;
        };

        $scope.openTglSelesai = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.tglSelesaiOpened = true;
        };

        $scope.openTglBatasKirim = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.tglBatasKirimOpened = true;
        };

        $scope.openTglBatasJaminan = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.status.tglBatasJaminanOpened = true;
        };

        vm.pengadaan.tipeTerminList = [{
            id: "DP",
            nama: "Down Payment"
		}, {
            id: "FP",
            nama: "Full Payment"
		}, {
            id: "TP",
            nama: "Term Payment"
		}];

        $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list')
            .success(function (data, status, headers, config) {
                vm.pengadaan.satuanList = data;
            });

        // add Termin
        vm.pengadaan.terminList = [];
        $scope.tableTerminList = new ngTableParams({
            page: 1, // show first page
            count: 5 // count per page
        }, {
            total: vm.pengadaan.terminList.length, // length of data4
            getData: function ($defer, params) {
                $defer.resolve(vm.pengadaan.terminList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });

        $scope.addDataTermin = function () {
            var terminmodalinstance = $modal.open({
                templateUrl: '/termin.html',
                controller: 'TerminModalController',
                size: 'lg',
                resolve: {
                    tipeList: function () {
                        return vm.pengadaan.tipeTerminList;
                    },
                    satuanList: function () {
                        return vm.pengadaan.satuanList;
                    },
                    dataTermin: function () {
                        return {};
                    }
                }
            });
            terminmodalinstance.result.then(function (dataTermin) {
                if (dataTermin != undefined && dataTermin !== null) {
                    vm.pengadaan.terminList.push(dataTermin);
                    $scope.tableTerminList.reload();
                }
            });
        }

        $scope.editDataTermin = function (index, dataTermin) {
            var terminmodalinstance = $modal.open({
                templateUrl: '/termin.html',
                controller: 'TerminModalController',
                size: 'lg',
                resolve: {
                    tipeList: function () {
                        return vm.pengadaan.tipeTerminList;
                    },
                    satuanList: function () {
                        return vm.pengadaan.satuanList;
                    },
                    dataTermin: function () {
                        return dataTermin;
                    }
                }
            });
            terminmodalinstance.result.then(function (dataTermin) {
                if (dataTermin != undefined && dataTermin !== null) {
                    vm.pengadaan.terminList.splice(index, 1, dataTermin);
                    $scope.tableTerminList.reload();
                }
            });
        }

        $scope.removeDataTermin = function (index, dataTermin) {
            var terminmodalinstance = $modal.open({
                templateUrl: '/alertModal.html',
                controller: 'DeleteDataModalController',
                size: 'sm',
                resolve: {
                    nameData: function () {
                        return $filter('date')(dataTermin.tanggal, 'dd/MM/yyyy');
                    }
                }
            });
            terminmodalinstance.result.then(function () {
                vm.pengadaan.terminList.splice(index, 1);
                $scope.tableTerminList.reload();
            });
        }

        // event on click button kembali
        $scope.btnKembali = function () {
            $state.go("app.promise.procurement-manajemenkontrak-viewdatapengadaan");
        }

        $scope.btnSimpan = function () {
            if (vm.pengadaan.nilaiPenawaranAfterCondition !== undefined && vm.pengadaan.nilaiPenawaranAfterCondition !== null) {

            } else {
                vm.pengadaan.nilaiPenawaranAfterCondition = 0;
            }
            if (vm.pengadaan.kurs !== undefined && vm.pengadaan.kurs !== null) {

            } else {
                vm.pengadaan.kurs = 0;
            }
            if (vm.pengadaan.nilaiJaminanPelaksana !== undefined && vm.pengadaan.nilaiJaminanPelaksana !== null) {

            } else {
                vm.pengadaan.nilaiJaminanPelaksana = 0;
            }
            // format tanggal menjadi string
            // insert into kontrak
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/KontrakManajemenServices/create',
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
                    nomor: vm.pengadaan.nomor,
                    nomorSuratPenawaran: vm.pengadaan.suratpenawaran.nomor,
                    tglSuratPenawaran: $filter('date')(vm.pengadaan.suratpenawaran.tanggalAwal, 'dd-MM-yyyy'),
                    nilai: vm.pengadaan.perolehan.nilai,
                    mataUang: vm.pengadaan.perolehan.mataUang.id,
                    tglMulaiKontrak: $filter('date')(vm.pengadaan.tglMulaiKontrak, 'dd-MM-yyyy'),
                    tglSelesaiKontrak: $filter('date')(vm.pengadaan.tglSelesaiKontrak, 'dd-MM-yyyy'),
                    nilaiKursUSD: vm.pengadaan.kursdollar,
                    tglSPK: $filter('date')(vm.pengadaan.tglSPK, 'dd-MM-yyyy'),
                    tglBatasPengiriman: $filter('date')(vm.pengadaan.tglBatasPengiriman, 'dd-MM-yyyy'),
                    namaJaminanPelaksana: vm.pengadaan.namaJaminanPelaksana,
                    nilaiJaminanPelaksana: vm.pengadaan.nilaiJaminanPelaksana,
                    tglBatasJaminan: $filter('date')(vm.pengadaan.tglBatasJaminan, 'dd-MM-yyyy'),
                    pinalty: vm.pengadaan.pinalty,
                    pinaltyType: vm.pengadaan.pinaltyType,
                    pengadaan: vm.pengadaan.id,
                    vendor: vm.pengadaan.perolehan.vendor.id
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                // insert jenis termin
                insertJenisTermin(data.id);

                $state.go("app.promise.procurement-manajemenkontrak-viewdatapengadaan");
            });
        }

        var insertJenisTermin = function (kontrakId) {
            if (vm.pengadaan.terminList !== undefined && vm.pengadaan.terminList.length > 0) {
                var targetURI = $rootScope.backendAddress + '/procurement/master/jenisTerminServices/created';
                angular.forEach(vm.pengadaan.terminList, function (dataTermin, index) {
                    var paramData = {
                        kontrak: kontrakId
                    };
                    if (dataTermin.tanggal !== undefined && dataTermin.tanggal.length > 0) {
                        paramData.tanggal = $filter('date')(dataTermin.tanggal, 'dd/MM/yyyy');
                    }
                    if (dataTermin.tipe !== undefined) {
                        paramData.tipe = dataTermin.tipe.id;
                    }
                    if (dataTermin.keterangan !== undefined) {
                        paramData.keterangan = dataTermin.keterangan;
                    }
                    if (dataTermin.satuan !== undefined && dataTermin.satuan.id > 0) {
                        paramData.satuan = dataTermin.satuan.id;
                    }
                    if (dataTermin.nilaiTermin !== undefined) {
                        paramData.nilaiTermin = dataTermin.nilaiTermin;
                    }
                    if (dataTermin.exchangeRate !== undefined) {
                        paramData.exchangeRate = dataTermin.exchangeRate;
                    }

                    dataService(targetURI, paramData);
                });
            }
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

    }

    CreateNewKontrakController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$stateParams', 'ngTableParams', '$state', '$filter', '$modal'];

})();

angular.module('naut')
    .controller('DeleteDataModalController', function ($scope, $modalInstance, nameData) {
        $scope.nameData = nameData

        $scope.ok = function () {
            $modalInstance.close('closed');
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

angular.module('naut')
    .controller('TerminModalController', function ($rootScope, $scope, $http, $modalInstance, $filter, tipeList, satuanList, dataTermin) {
        $scope.tipeList = tipeList;
        $scope.satuanList = satuanList;
        $scope.dataTermin = dataTermin;

        $scope.tanggalTerminStatus = false;
        $scope.openTanggalTermin = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalTerminStatus = true;
        }

        $scope.btnSimpan = function () {
            $modalInstance.close($scope.dataTermin);
        }

        $scope.btnCancel = function () {
            $modalInstance.dismiss('cancel');
        }
    });