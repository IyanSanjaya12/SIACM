/**=========================================================
 * Module: DashboardController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DashboardPController', DashboardPController);

    DashboardPController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter', '$state', 'RequestService', '$q'];

    function DashboardPController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter, $state, RequestService, $q) {

        var ds = this;

        $scope.sortType = 'namaPengadaan'; // set the default sort type
        $scope.sortReverse = false; // set the default sort order
        $scope.cariNamaPengadaan = '';
        $scope.rowPerPage = 5;
        $scope.currentPage = 1;
        $scope.cariTahapanPengadaan = 0;

        $scope.getPengadaanList = function (params) {
            $scope.loading = true;
            RequestService.doPOST('/procurement/inisialisasi/getPengadaanListByUserWithPagination', params)
                .then(function successCallback(reponse, index, array) {
                    $scope.pengadaanList = reponse.data.pengadaanList;
                    $scope.totalItems = reponse.data.jmlData;
                    $scope.loading = false;
                });
        };

        $scope.pageChanged = function (rowPerPage) {
            $scope.rowPerPage = rowPerPage;
            $scope.getPage(1);
        };

        $scope.getPage = function (page) {
            var params = {
                pageNumber: page,
                numberOfRowPerPage: $scope.rowPerPage,
                searchingKeyWord: '{filter:[{key:namaPengadaan, value:"' + $scope.cariNamaPengadaan + '"}], sort:tanggalNotaDinas, tahapanPengadaan:' + $scope.cariTahapanPengadaan + '}'
            };

            $scope.getPengadaanList(params);
        }
        $scope.getPage(1);

        $scope.getSearch = function () {
            $scope.getPage(1);
        }

        $scope.dateNow = new Date();

        //function check jadwal
        function asyncJadwalPengadaan(pengadaanId, tahapanId) {
            return $q(function (resolve, reject) {
                setTimeout(function () {
                    RequestService.doGET('/procurement/jadwalPengadaanServices/getIsJadwalPengadaanValid/' + pengadaanId + '/' + tahapanId)
                        .then(function (result) {
                            if (typeof result !== 'undefined') {
                                console.log("isValidJadwal = " + result);
                                resolve(result);
                            } else {
                                reject(false);
                            }
                        })
                }, 1000);
            })
        }

        $scope.view = function (pp) {
            $scope.loading = true;
            $rootScope.listPengadaan = pp;
            var promise = asyncJadwalPengadaan(pp.id, pp.tahapanPengadaan.tahapan.id);
            promise.then(function (result) {
                //jika kriteria evaluasi/kualifikasi bypassf
                if(pp.tahapanPengadaan.tahapan.id==7 || pp.tahapanPengadaan.tahapan.id==10080000|| pp.tahapanPengadaan.tahapan.id==19000001){
                    result = true;
                }
                    
                if (!result) {
                    $scope.loading = false;
                    RequestService.modalConfirmation("Tahapan Pengadaan saat ini tidak sesuai Jadwal Pengadaan, apakah Anda ingin merubah Jadwal Pengadaan?")
                        .then(function () {
                            $rootScope.pengadaan = pp;
                            $location.path("/app/promise/procurement/jadwalpengadaan/detil2");
                        });
                } else {
                    $scope.loading = false;
                    if (pp.tahapanPengadaan.tahapan.id == 1) {
                        $location.path('/app/promise/procurement/inisialisasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10010000) {
                        $location.path('/app/promise/procurement/inisialisasi/index04');
                    } else if (pp.tahapanPengadaan.tahapan.id == 2) {
                        $location.path('/app/promise/procurement/datapenawaran/vendor/dua/sampul');
                    } else if (pp.tahapanPengadaan.tahapan.id == 3) {
                        $location.path('/app/promise/procurement/datapenawaran/vendor/tahap/satu');
                    } else if (pp.tahapanPengadaan.tahapan.id == 4) {
                        $location.path('/app/promise/procurement/datapenawaran/vendor/tahap/dua');
                    } else if (pp.tahapanPengadaan.tahapan.id == 5) {
                        $location.path('/app/promise/procurement/jadwalpengadaan');
                    } else if (pp.tahapanPengadaan.tahapan.id == 6) {
                        $location.path('/app/promise/procurement/pengambilandokumen');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10060000) {
                        $location.path('/app/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 7  || pp.tahapanPengadaan.tahapan.id == 19000001) {
                        //Kriteria Evaluasi total / satuan 
                        $location.path('/app/promise/procurement/kriteriaEvaluasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10070000) {
                        $location.path('/app/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10080000) {
                        $location.path('/app/promise/procurement/prakualifikasi/kriteriakualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10090000) {
                        $location.path('/app/promise/procurement/prakualifikasi/seleksikualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10100000) {
                        $location.path('/app/promise/procurement/prakualifikasi/hasilkualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10110000) {
                        $location.path('/app/promise/procurement/prakualifikasi/sanggahanprakualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10120000) {
                        $location.path('/app/promise/procurement/prakualifikasi/persetujuankualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 8) {
                        $location.path('/app/promise/procurement/penjelasan');
                    } else if (pp.tahapanPengadaan.tahapan.id == 9 || pp.tahapanPengadaan.tahapan.id == 19000002) {
                        $location.path('/app/promise/procurement/pemasukanPenawaran');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10 || pp.tahapanPengadaan.tahapan.id == 19000003) {
                        $location.path('/app/promise/procurement/pembukaaanPenawaran');
                    } else if (pp.tahapanPengadaan.tahapan.id == 11) {
                        $location.path('/app/promise/procurement/evaluasiAdministrasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 12) {
                        $location.path('/app/promise/procurement/evaluasiteknis');
                    } else if (pp.tahapanPengadaan.tahapan.id == 13) {
                        $location.path('/app/promise/procurement/evaluasiharga/bidding');
                    } else if (pp.tahapanPengadaan.tahapan.id == 13000001) {
                        $location.path('/app/promise/procurement/evaluasihargasimple/bidding');
                    } else if (pp.tahapanPengadaan.tahapan.id == 14 || pp.tahapanPengadaan.tahapan.id == 14000001) {
                        $location.path('/app/promise/procurement/evaluasiharga/auction');
                    } else if (pp.tahapanPengadaan.tahapan.id == 15) {
                        $location.path('/app/promise/procurement/negosiasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 16) {
                        $location.path('/app/promise/procurement/usulancalonpemenang');
                    } else if (pp.tahapanPengadaan.tahapan.id == 17) {
                        $rootScope.pengadaanPenetapanPemenang = pp;
                        var sistemEvaluasiPenawaranId = pp.sistemEvaluasiPenawaran.id;
                        var jenisPenawaranId = pp.jenisPenawaran.id;
                        if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 1) {
                            $location.path('app/promise/procurement/penetapanpemenang/totalSistemGugur');
                        };
                        if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 2) {
                            $location.path('app/promise/procurement/penetapanpemenang/totalMeritPoint');
                        };
                    } else if (pp.tahapanPengadaan.tahapan.id == 18) {
                        $location.path('/app/promise/procurement/sanggahan');
                    } else if (pp.tahapanPengadaan.tahapan.id == 19) {
                        $location.path('/app/promise/procurement/penunjukanpemenang');
                    } else if (pp.tahapanPengadaan.tahapan.id == 20) {
                        $location.path('/app/promise/procurement/spk');
                    } else if (pp.tahapanPengadaan.tahapan.id == 21) {
                        $location.path('/app/promise/procurement/kontrak/indexdatapengadaan');
                    } else if (pp.tahapanPengadaan.tahapan.id == 22) {
                        $location.path('/app/promise/procurement/pengadaangagal');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10170200) {
                        $state.go('app.promise.procurement-panitia-pemasukanPenawaran', {
                            tahapanId: pp.tahapanPengadaan.tahapan.id
                        });
                    } else if (pp.tahapanPengadaan.tahapan.id == 10180200) {
                        $state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-tahapsatu', {
                            tahapanId: pp.tahapanPengadaan.tahapan.id
                        });
                    } else if (pp.tahapanPengadaan.tahapan.id == 10230000) {
                        $location.path('/app/promise/procurement/pembuktiankualifikasi');
                    } else if (pp.tahapanPengadaan.tahapan.id == 10170300) {
                        $state.go('app.promise.procurement-panitia-pemasukanPenawaran', {
                            tahapanId: pp.tahapanPengadaan.tahapan.id
                        });
                    } else if (pp.tahapanPengadaan.tahapan.id == 10180300) {
                        $state.go('app.promise.procurement-panitia-pembukaanPenawaran', {
                            tahapanId: pp.tahapanPengadaan.tahapan.id
                        });
                    } else if (pp.tahapanPengadaan.tahapan.id == 10250000) {
                        $state.go('app.promise.procurement-panitia-klarifikasiharga', {
                            tahapanId: pp.tahapanPengadaan.tahapan.id
                        });
                    } else if (pp.tahapanPengadaan.tahapan.id == 10040000) {
                        $location.path('/app/promise/procurement/persetujuanpengadaan');
                    } else {
                        alert('Halaman tidak tersedia');
                    }
                }
            }, function (reson) {
                //reject
                console.log("reject no data");
            });

        };
        //special
        $scope.viewSanggahan = function () {
            $location.path('/app/promise/procurement/prakualifikasi/sanggahanprakualifikasi');
        }
        $scope.viewKlarifikasiHarga = function () {
            $location.path('/app/promise/procurement/klarifikasiharga');
        }



        // Some numbers for demo
        $scope.loadProgressValues = function () {
            $scope.progressVal = [0, 0, 0, 0];
            // helps to show animation when values change
            $timeout(function () {
                $scope.progressVal[0] = 60;
                $scope.progressVal[1] = 34;
                $scope.progressVal[2] = 22;
                $scope.progressVal[3] = 76;
            });
        };
        //jumlah pengadaan
        $scope.totalPengadaan = 0;
        $scope.spkProsentase = 0;
        $scope.getCountAllPengadaan = function () {
            $scope.totalPengadaan = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getCountAllPengadaan')
                .success(function (data, status, headers, config) {
                    $scope.totalPengadaan = data;
                    $scope.spkProsentase = 'loading...';
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/countPengadaanByTahapan/21/21')
                        .success(function (data, status, headers, config) {
                            $scope.spkProsentase = data * 100 / $scope.totalPengadaan;
                        });
                })
                .error(function (data, status, headers, config) {});
        }
        $scope.getCountAllPengadaan();
        //jumlahVendor
        $scope.totalVendor = 0;
        $scope.getCountVendor = function () {
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getCountVendor')
                .success(
                    function (data, status, headers, config) {
                        $scope.totalVendor = data;
                    })
                .error(function (data, status, headers, config) {});
        }
        $scope.getCountVendor();
        //jumlahVendorBaru
        $scope.totalVendorBaru = 0;
        $scope.totalVendorBaruForRepeat = [];
        $scope.getCountVendor = function () {
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getCountVendor/0')
                .success(
                    function (data, status, headers, config) {
                        $scope.totalVendorBaru = data;
                        var loop = data;
                        if (loop > 5)
                            loop = 5;
                        for (var i = 0; i < loop; i++) {
                            $scope.totalVendorBaruForRepeat.push(i);
                        }
                    })
                .error(function (data, status, headers, config) {});
        }
        $scope.getCountVendor();
        //jumlah user Online
        $scope.totalUserOnline = 0;
        $scope.countUserOnline = function () {
            $http.get($rootScope.backendAddress + '/procurement/user/get-online-user')
                .success(
                    function (data, status, headers, config) {
                        $scope.totalUserOnline = data;
                    })
                .error(function (data, status, headers, config) {});
        }
        $scope.countUserOnline();

        //ForPieChart
        $scope.backend = $rootScope.backendAddress;

        //$scope.prosesPengadaan = 2;
        $scope.pieOptions = {
            animate: {
                duration: 700,
                enabled: true
            },
            barColor: colors.byName('success'),
            // trackColor: colors.byName('inverse'),
            scaleColor: false,
            lineWidth: 10,
            lineCap: 'circle'
        };

        // Dashboard charts
        // ----------------------------------- 

        // Spline chart
        /*
        $scope.splineChartOpts = angular.extend({}, flotOptions['default'], {
            series: {
                lines: {
                    show: false
                },
                splines: {
                    show: true,
                    tension: 0.4,
                    lineWidth: 2,
                    fill: 0.5
                },
            },
            yaxis: {
                max: 50
            }
        });*/
        $scope.splineChartOpts = angular.extend({}, flotOptions['line'], {
            yaxis: {
                max: 50
            }
        });
        $scope.splineData = getSplineData();

        function getSplineData() {
            var object = [];
            //dikomen karena hardcode
            /*var vendorReg = {
                'label': 'Vendor Registrasi',
                'color': colors.byName('info'),
                'data': [
					[$filter('date')(new Date('2015-12-01'), 'dd/MM'), 10], [$filter('date')(new Date('2015-12-10'), 'dd/MM'), 20], [$filter('date')(new Date('2015-12-04'), 'dd/MM'), 17]
          		]
            };*/
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/countPengadaanByDate')
                .success(
                    function (data, status, headers, config) {
                        var pengadaanStatistik = {
                            'label': 'Pengadaan',
                            'color': colors.byName('success')
                        };
                        var dataPengadaan = [];
                        for (var i = 0; i < data.length; i++) {
                            var dataTemp = [$filter('date')(new Date(data[i].tgl), 'dd/MM'), data[i].pengadaan];
                            dataPengadaan.push(dataTemp);
                        }
                        pengadaanStatistik.data = dataPengadaan;

                        //object.push(vendorReg);
                        object.push(pengadaanStatistik);
                    })
                .error(function (data, status, headers, config) {});
            //Fungsinya udah di bikinin tngl sesuain aja.
            /*$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/countVendorByDate')
            .success(
                function (data, status, headers, config) {
                    var vendorStatistik = {
                        'label': 'Vendor',
                        'color': colors.byName('success')
                    };
                    var dataVendor = [];
                    for (var i = 0; i < data.length; i++) {
                        var dataTemp = [$filter('date')(new Date(data[i].tgl), 'dd/MM'), data[i].vendor];
                        dataVendor.push(dataTemp);
                    }
                    vendorStatistik.data = dataVendor;

                    //object.push(vendorReg);
                    object.push(dataVendor);
                })
            .error(function (data, status, headers, config) {});*/

            return object;
        }

        //		$scope.$watch('app.theme.name', function (val) {
        //			$scope.splineData = getSplineData();
        //		});

        //		$scope.chartPieFlotChart = flotOptions['pie'];
        $scope.chartPieFlotChart = angular.extend({}, flotOptions['pie'], {
            series: {
                pie: {
                    show: true,
                    innerRadius: 0,
                    label: {
                        show: true,
                        radius: 0.8,
                        formatter: function (label, series) {
                            return '<div class="flot-pie-label">' + series.data[0][1] + '</div>';
                        },
                        background: {
                            opacity: 0.8,
                            color: '#222'
                        }
                    }
                }
            }
        });

        function update() {
            RequestService.doGET('/procurement/inisialisasi/dashboardPengadaan')
                .then(function (data) {
                    $scope.pengadaanSelesai = data.PengadaanSelesai;
                    $scope.realTimeChartData = [];
                    if (data.PengadaanBaru != undefined && data.PengadaanBaru > 0) {
                        var dataDashboard = {
                            "color": "#39C558",
                            "data": data.PengadaanBaru,
                            "label": "Pengadaan Baru"
                        };
                        $scope.realTimeChartData.push(dataDashboard);
                    }
                    if (data.PengadaanBerjalan != undefined && data.PengadaanBerjalan > 0) {
                        var dataDashboard = {
                            "color": "#00b4ff",
                            "data": data.PengadaanBerjalan,
                            "label": "Pengadaan Sedang Berjalan"
                        };
                        $scope.realTimeChartData.push(dataDashboard);
                    }
                    if (data.vPengadaanSelesai != undefined && data.vPengadaanSelesai > 0) {
                        var dataDashboard = {
                            "color": "#ff3e43",
                            "data": data.vPengadaanSelesai,
                            "label": "Pengadaan Selesai"
                        };
                        $scope.realTimeChartData.push(dataDashboard);
                    }

                    $rootScope.dashboard = $timeout(update, 10000);
                });
        }
        update();

        $scope.moveToHistoryPengadaan = function () {
            $state.go("app.promise.procurement-panitia-historypengadaan");
        }

    }
})();