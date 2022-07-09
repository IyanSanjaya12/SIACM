(function () {
    'use strict';

    angular.module('naut').controller('KriteriaEvaluasiViewController', KriteriaEvaluasiViewController);

    function KriteriaEvaluasiViewController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $window, $route, $templateCache, ngTableParams, RequestService, $state) {
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        var vm = this;

        vm.flagView = $rootScope.flagView;

        vm.kriteriaAminId = 0;
        vm.kriteriaTeknisId = 0;
        vm.kriteriaAmbangId = 0;
        vm.pembobotanId = 0;
        vm.adaListAdmin = true;
        vm.adaListTeknis = true;
        vm.adaListAmbang = true;

        /* START Detail Pengadaan */
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        vm.pengadaanId = $scope.detilPengadaan.id;
        vm.sistemEvaluasi = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        //vm.sistemEvaluasi = 2; //Untuk Uji Coba
        /*---END Detail Pengadaan----*/

        /* START Bidang Usaha Pengadaan*/
        vm.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + vm.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            vm.subBidangUsahaByPengadaanIdList = data;
            vm.loading = false;
        }).error(function (data, status, headers, config) {
            vm.loading = false;
        });
        /*---END Bidang Usaha Pengadaan---*/

        /* START Rincian Kebutuhan Material*/
        vm.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + vm.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isMaterialNotEmpty = true;

            var itemPengadaanMaterialByPengadaanIdList = data
            vm.itemPengadaanMaterialByPengadaanIdList = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: itemPengadaanMaterialByPengadaanIdList.length,
                getData: function ($defer, params) {
                    $defer.resolve(itemPengadaanMaterialByPengadaanIdList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            vm.loading = false;
        }).error(function (data, status, headers, config) {
            vm.loading = false;
        });
        /*---END Rincian Kebutuhan Materrial---*/

        /* START Rincian Kebutuhan Jasa*/
        vm.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + vm.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isJasaNotEmpty = true;

            var itemPengadaanJasaByPengadaanIdList = data;
            vm.itemPengadaanJasaByPengadaanIdList = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: itemPengadaanJasaByPengadaanIdList.length,
                getData: function ($defer, params) {
                    $defer.resolve(itemPengadaanJasaByPengadaanIdList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            vm.loading = false;
        }).error(function (data, status, headers, config) {
            vm.loading = false;
        });
        /*---END Rincian Kebutuhan Jasa---*/

        /* START get from table Kriteria Evaluasi */
        //Administrasi
        vm.loading = true;
        vm.listAdminFromTable = function () {
                if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/getByPengadaanIdList/' + vm.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        if (data != undefined) {
                            vm.listAdmin = data;
                            vm.adaListAdmin = false;
                        }

                        vm.listAdminList = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: vm.listAdmin.length,
                            getData: function ($defer, params) {
                                $defer.resolve(vm.listAdmin.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        });
                        vm.loading = false;
                    }).error(function (data, status, headers, config) {
                        vm.loading = false;
                    });
                } else if (vm.sistemEvaluasi == 2) {
                    //Teknis Merit Point
                    vm.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointServices/getByPengadaanIdList/' + vm.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        if (data != undefined) {
                            vm.listAdmin = data
                            vm.adaListAdmin = false;
                        }

                        vm.listAdminList = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: vm.listAdmin.length,
                            getData: function ($defer, params) {
                                $defer.resolve(vm.listAdmin.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        });
                        vm.loading = false;
                    }).error(function (data, status, headers, config) {
                        vm.loading = false;
                    });
                }
            }
            //Teknis
        vm.loading = true;
        vm.listTeknisFromTable = function () {
                if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisServices/getByPengadaanIdList/' + vm.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        if (data != undefined) {
                            vm.listTeknis = data;
                            vm.adaListTeknis = false;
                        }

                        vm.listTeknisList = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: vm.listTeknis.length,
                            getData: function ($defer, params) {
                                $defer.resolve(vm.listTeknis.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        });
                        vm.loading = false;
                    }).error(function (data, status, headers, config) {
                        vm.loading = false;
                    });
                } else if (vm.sistemEvaluasi == 2) {
                    //Teknis Merit Point
                    vm.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointServices/getByPengadaanIdList/' + vm.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        if (data != undefined) {
                            vm.listTeknis = data;
                            vm.adaListTeknis = false;
                        }

                        vm.listTeknisList = new ngTableParams({
                            page: 1,
                            count: 10
                        }, {
                            total: vm.listTeknis.length,
                            getData: function ($defer, params) {
                                $defer.resolve(vm.listTeknis.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        });
                        vm.loading = false;
                    }).error(function (data, status, headers, config) {
                        vm.loading = false;
                    });
                }
            }
            //Ambang Batas
        vm.loading = true;
        vm.listAmbangFromTable = function () {
                $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + vm.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data != undefined) {
                        vm.adaListAmbang = false;
                        angular.forEach(data, function (value, index) {
                            $rootScope.isiListAmbangFromTable.push(value);
                        });
                        for (var i = 0; i < $rootScope.isiListAmbangFromTable.length; i++) {
                            vm.batasAtas = $rootScope.isiListAmbangFromTable[i].atas;
                            vm.batasBawah = $rootScope.isiListAmbangFromTable[i].bawah;
                            vm.persenAdmin = $rootScope.isiListAmbangFromTable[i].presentasiNilaiAdmin;
                            vm.persenTeknis = $rootScope.isiListAmbangFromTable[i].presentasiNilaiTeknis;
                            vm.persenHarga = $rootScope.isiListAmbangFromTable[i].presentasiNilaiHarga;
                        }
                    }
                    vm.loading = false;
                }).error(function (data, status, headers, config) {
                    vm.loading = true;
                });
            }
            /*---END get from kriteria Evaluasi---*/

        /*--------------------------------------------------------------------------------------*/
        vm.back = function () {
            $location.path('/app/promise/procurement/kriteriaEvaluasi');
        };

        vm.backView = function () {
            $location.path('/app/promise/procurement/kriteriaEvaluasi/view');
        };

        // TODO check the sistemEvaluasiPengadaan from detailPengadaan for change view Detail Kriteria
        vm.addKriteria = function () {
            if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addView');
            } else if (vm.sistemEvaluasi == 2) {
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addMeritView');
            } else {
                alert('tidak tersedia');
            }
        };

        vm.addKriteriaHarga = function () {
            $rootScope.flagKriteriaHarga = 1;
            if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addView');
            } else if (vm.sistemEvaluasi == 2) {
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addMeritView');
            } else {
                alert('tidak tersedia');
            }
        };

        //get for kriteria when ADDED mode
        if ($rootScope.kriteriaEvaluasi.length > 0) {
            vm.listAdmin = $rootScope.kriteriaEvaluasi[0].kriteriaAdmin;
            vm.listTeknis = $rootScope.kriteriaEvaluasi[0].kriteriaTeknis;
            vm.batasAtas = $rootScope.kriteriaEvaluasi[0].batasAtas;
            vm.batasBawah = $rootScope.kriteriaEvaluasi[0].batasBawah;
            vm.persenAdmin = $rootScope.kriteriaEvaluasi[0].persenAdmin;
            vm.persenTeknis = $rootScope.kriteriaEvaluasi[0].persenTeknis;
            vm.persenHarga = $rootScope.kriteriaEvaluasi[0].persenHarga;
            vm.valueMaks = $rootScope.kriteriaEvaluasi[0].valueMaks;

            //untuk paging ulang 
            vm.listAdminList = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: vm.listAdmin.length,
                getData: function ($defer, params) {
                    $defer.resolve(vm.listAdmin.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            vm.listTeknisList = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: vm.listTeknis.length,
                getData: function ($defer, params) {
                    $defer.resolve(vm.listTeknis.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
        } else {
            vm.listAdminFromTable();
            vm.listTeknisFromTable();
            vm.listAmbangFromTable()
            //console.log('INFO listAdminFromTable : ' + JSON.stringify(vm.listAdmin));
        }

        // remove kriteria from table
        vm.removeKriteriaAdmin = function (kriteriaAdmin) {
            RequestService.modalConfirmation('Anda yakin ingin menghapus Kriteria Admin "' + (typeof kriteriaAdmin.nama === 'undefined' ? kriteriaAdmin.masterKriteriaAdministrasi.nama : kriteriaAdmin.nama) + '" ?')
                .then(function (result) {
                    var index = $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.indexOf(kriteriaAdmin);
                    $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.splice(index, 1);
                    vm.listAdminList.reload();
                });

        }

        vm.removeKriteriaTeknis = function (kriteriaTeknis) {
            RequestService.modalConfirmation('Anda yakin ingin menghapus Kriteria Teknis "' + (typeof kriteriaTeknis.nama === 'undefined' ? kriteriaTeknis.masterKriteriaAdministrasi.nama : kriteriaTeknis.nama) + '" ?')
                .then(function (result) {
                    var index = $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.indexOf(kriteriaTeknis);
                    $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.splice(index, 1);
                    vm.listTeknisList.reload();
                });
        }

        // ----------------------------------------------------------------------

        //save data Kriteria Administrasi, Teknis and Harga
        vm.saveData = function () {
            //check data

        	$scope.loading = true;
            if ($rootScope.kriteriaEvaluasi.length > 0) {
                if ($rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length <= 0) {
                    alert("Kriteria Administrasi belum diisi!");
                } else if ($rootScope.kriteriaEvaluasi[0].kriteriaTeknis.length <= 0) {
                    alert("Kriteria Teknis belum diisi!");
                } else {


                    ModalService.showModalConfirmation().then(function (result) {

                        //save data k.Administrasi
                        
                    	//console.log("DAta yg akan di simpan : " + JSON.stringify($rootScope.kriteriaEvaluasi[0]));
                        var urlTeknis = '';
                        var dataKriteriaAdministrasi = {};
                        var indexAdmin = 0;
                        for (var i = 0; i < $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length; i++) {
                            if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                                urlTeknis = '/procurement/kriteriaAdministrasiServices';
                                dataKriteriaAdministrasi = {
                                    id: vm.kriteriaAminId,
                                    nama: $rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].nama,
                                    pengadaan: vm.pengadaanId,
                                    masterKriteriaAdministrasi: $rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].id
                                };
                            } else if (vm.sistemEvaluasi == 2) {
                                urlTeknis = '/procurement/kriteriaAdminMeritPointServices';
                                if ($rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].nilai != undefined) {
                                    dataKriteriaAdministrasi = {
                                        id: vm.kriteriaAminId,
                                        nilai: $rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].nilai,
                                        pengadaan: vm.pengadaanId,
                                        masterKriteriaAdministrasi: $rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].id
                                    };
                                }
                            }

                            if (vm.kriteriaAminId > 0) {
                                $http({
                                    method: 'POST',
                                    url: $rootScope.backendAddress + urlTeknis + '/update',
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded'
                                    },
                                    transformRequest: function (obj) {
                                        var str = [];
                                        for (var p in obj)
                                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                        return str.join("&");
                                    },
                                    data: dataKriteriaAdministrasi
                                }).success(function (data, status, headers, config) {
                                    //console.log("UPDATE KRITERIA ADMIN OK : " + JSON.stringify(data));
                                });
                            } else {
                                $http({
                                    method: 'POST',
                                    url: $rootScope.backendAddress + urlTeknis + '/create',
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded'
                                    },
                                    transformRequest: function (obj) {
                                        var str = [];
                                        for (var p in obj)
                                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                        return str.join("&");
                                    },
                                    data: dataKriteriaAdministrasi
                                }).success(function (data, status, headers, config) {
                                    if (typeof data !== 'undefined')
                                        vm.kriteriaAminId = data.id;
                                    //console.log("CREATE KRITERIA ADMIN OK : " + JSON.stringify(data));
                                });
                            }
                            
                            indexAdmin++;
                        }
                        
                        if(indexAdmin == $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length) {
                        	simpanKriteriaTeknis();
                        }
                        
                    });
                }
            } else {
                ModalService.showModalInformation('Silahkan masukkan data kriteria evaluasi!');
            }
        }

        //save data k.Teknis
        var simpanKriteriaTeknis = function () {
            var urlTeknis = '';
            var dataKriteriaTeknis = {};
            var indexTeknis = 0;
            
            for (var j = 0; j < $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.length; j++) {
                if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                    urlTeknis = '/procurement/kriteriaTeknisServices';
                    dataKriteriaTeknis = {
                        id: vm.kriteriaTeknisId,
                        nama: $rootScope.kriteriaEvaluasi[0].kriteriaTeknis[j].nama,
                        pengadaan: vm.pengadaanId,
                        masterKriteriaTeknis: $rootScope.kriteriaEvaluasi[0].kriteriaTeknis[j].id
                    };
                } else if (vm.sistemEvaluasi == 2) {
                    urlTeknis = '/procurement/kriteriaTeknisMeritPointServices';
                    if ($rootScope.kriteriaEvaluasi[0].kriteriaTeknis[j].nilai != undefined) {
                        dataKriteriaTeknis = {
                            id: vm.kriteriaTeknisId,
                            nilai: $rootScope.kriteriaEvaluasi[0].kriteriaTeknis[j].nilai,
                            pengadaan: vm.pengadaanId,
                            masterKriteriaTeknis: $rootScope.kriteriaEvaluasi[0].kriteriaTeknis[j].id
                        };
                    }
                }

                if (vm.kriteriaTeknisId > 0) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + urlTeknis + '/update',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: dataKriteriaTeknis
                    }).success(function (data, status, headers, config) {
                        //console.log("UPDATE KRITERIA TEKNIS OK : " + JSON.stringify(data));
                    });
                } else {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + urlTeknis + '/create',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: dataKriteriaTeknis
                    }).success(function (data, status, headers, config) {
                        if (typeof data !== 'undefined')
                            vm.kriteriaTeknisId = data.id;
                        //console.log("CREATE KRITERIA TEKNIS OK : " + JSON.stringify(data));
                    });
                }
                
                indexTeknis++;
            }
            
            if(indexTeknis == $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.length) {
            	if(vm.sistemEvaluasi == 1) {
            		simpanKriteriaAmbang();
            	} else {
            		simpanPembobotanAdmin();
            	}
            	
            }
            
        }


        //save data k.Admin pembobotan nilai merit point
        var simpanPembobotanAdmin = function () {
            var dataPembobotanAdmin = {
                id: vm.pembobotanId,
                totalNilaiPembobotanAdmin: $rootScope.kriteriaEvaluasi[0].valueTotalAdmin,
                nilaiMinimalKelulusanAdmin: $rootScope.kriteriaEvaluasi[0].valueMaksAdmin,
                pengadaan: vm.pengadaanId
            };
            
            //console.log(dataPembobotanAdmin);

            if (vm.pembobotanId > 0) {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataPembobotanAdmin
                }).success(function (data, status, headers, config) {
                    //console.log("UPDATE PEMBOBOTAN ADMIN OK : " + JSON.stringify(data));
                });
            } else {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataPembobotanAdmin
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined')
                        vm.pembobotanId = data.id;
                    //console.log("CREATE PEMBOBOTAN ADMIN OK : " + JSON.stringify(data));
                });
            }
            simpanPembobotanTeknis();
        }


        //save data k.Teknis pembobotan nilai merit point
        var simpanPembobotanTeknis = function () {
            var dataPembobotanTeknis = {
                id: vm.pembobotanId,
                totalNilaiPembobotanTeknis: $rootScope.kriteriaEvaluasi[0].valueTotalTeknis,
                nilaiMinimalKelulusanTeknis: $rootScope.kriteriaEvaluasi[0].valueMaksTeknis,
                pengadaan: vm.pengadaanId
            };
            
            //console.log(dataPembobotanTeknis);

            if (vm.pembobotanId > 0) {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointPembobotanServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataPembobotanTeknis
                }).success(function (data, status, headers, config) {
                    //console.log("UPDATE PEMBOBOTAN OK : " + JSON.stringify(data));
                });
            } else {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointPembobotanServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataPembobotanTeknis
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined')
                        vm.pembobotanId = data.id;
                    //console.log("CREATE PEMBOBOTAN TEKNIS OK : " + JSON.stringify(data));
                });
            }
            simpanKriteriaAmbang();
        }


        //save data k.AmbangBatas sistem gugur
        var simpanKriteriaAmbang = function () {
            var dataKriteriaAmbang = {};
            if (vm.sistemEvaluasi == 1) { // 1 = sistem gugur, 2 = merit point
                dataKriteriaAmbang = {
                    id: vm.kriteriaAmbangId,
                    atas: $rootScope.kriteriaEvaluasi[0].batasAtas,
                    bawah: $rootScope.kriteriaEvaluasi[0].batasBawah,
                    pengadaan: vm.pengadaanId
                };
            } else if (vm.sistemEvaluasi == 2) {
                dataKriteriaAmbang = {
                    id: vm.kriteriaAmbangId,
                    atas: $rootScope.kriteriaEvaluasi[0].batasAtas,
                    bawah: $rootScope.kriteriaEvaluasi[0].batasBawah,
                    presentasiNilaiAdmin: $rootScope.kriteriaEvaluasi[0].persenAdmin,
                    presentasiNilaiTeknis: $rootScope.kriteriaEvaluasi[0].persenTeknis,
                    presentasiNilaiHarga: $rootScope.kriteriaEvaluasi[0].persenHarga,
                    pengadaan: vm.pengadaanId
                };
            }
            
            //console.log(dataKriteriaAmbang);

            if (vm.kriteriaAmbangId > 0) {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataKriteriaAmbang
                }).success(function (data, status, headers, config) {
                    //console.log("UPDATE KRITERIA AMBANG OK : " + JSON.stringify(data));
                });
            } else {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataKriteriaAmbang
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined')
                        vm.kriteriaAmbangId = data.id;
                    //console.log("CREATE KRITERIA AMBANG OK : " + JSON.stringify(data));
                });
            }
            noTahapanSelanjutnya();
        }


        //change tahapan
        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + vm.pengadaanId)
                    .success(function (data, status, headers, config) {
                        $scope.nextTahapan = data;
                    })
                    .error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        var noTahapanSelanjutnya = function () {
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
                    pengadaanId: vm.pengadaanId,
                    tahapanPengadaanId: $scope.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $state.go('app.promise.procurement-panitia-kriteriaEvaluasi');
            });
        }

        //URL Link
        vm.go = function (path) {
            $location.path(path);
        };
    }

    KriteriaEvaluasiViewController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$route', '$templateCache', 'ngTableParams', 'RequestService', '$state'];

})();