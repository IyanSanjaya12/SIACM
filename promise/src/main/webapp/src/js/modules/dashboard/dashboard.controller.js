/**=========================================================
 * Module: DashboardController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter', 'RequestService', '$state'];

    function DashboardController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter, RequestService, $state) {
        //go
        $scope.go = function (path) {
            $location.path(path);
        };

        $scope.view = function (pp) {
            $rootScope.listPengadaan = pp;
            if (pp.tahapanPengadaan.tahapan.id == 13000001) {
                $location.path('/appvendor/promise/procurement/penawaranhargaindependence/vendor');
            } else if (pp.tahapanPengadaan.tahapan.id == 14000001 || pp.tahapanPengadaan.tahapan.id == 14) {
                $location.path('/appvendor/promise/procurement/evaluasiharga/vendor/auction');
            } else if (pp.tahapanPengadaan.tahapan.id == 3) {
                //$location.path('/app/promise/procurement/datapenawaran/vendor/tahap/satu');
            } else if (pp.tahapanPengadaan.tahapan.id == 10170200 || pp.tahapanPengadaan.tahapan.id == 10170300 || pp.tahapanPengadaan.tahapan.id == 9 || pp.tahapanPengadaan.tahapan.id == 19000002) {
            	$rootScope.pengadaanVendor = pp;
                $location.path('/appvendor/promise/procurement/datapenawaran/vendor/satu/sampul');
            } else if (pp.tahapanPengadaan.tahapan.id == 10060000) {
                if (pp.metodePengadaan.id != 4 && pp.metodePengadaan.id != 5) {
                    $location.path('/appvendor/promise/procurement/prakualifikasi/pengumumankualifikasi');
                } else if (pp.metodePengadaan.id == 4 || pp.metodePengadaan.id == 5) {
                    $location.path('/appvendor/promise/procurement/prakualifikasi/undanganvendorkualifikasi');
                }

            } else if (pp.tahapanPengadaan.tahapan.id == 10070000) {
                $location.path('/appvendor/promise/procurement/prakualifikasi/datakualifikasi');
            } else if (pp.tahapanPengadaan.tahapan.id == 10100000) {
                $location.path('/appvendor/promise/procurement/prakualifikasi/hasilkualifikasi');
            } else if (pp.tahapanPengadaan.tahapan.id == 15) {
                $location.path('/appvendor/promise/procurement/vendor/negosiasi');
            } else if (pp.tahapanPengadaan.tahapan.id == 18) {
                $location.path('/appvendor/promise/procurement/vendor/sanggahan');
            } else if (pp.tahapanPengadaan.tahapan.id == 20 || pp.tahapanPengadaan.tahapan.id == 21) { //kontrak or SPK
                $state.go('appvendor.promise.procurement-hasilpengadaan-detail', {
                    dataPengadaan: {
                        pengadaan : pp 
                    }
                });
            } else {
                alert('Halaman tidak tersedia');
            }
        };

        $scope.getValidationButtonView = function (pp) {
            var isHasView = false;
            if (pp.tahapanPengadaan.tahapan.id == 13000001) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 14000001 || pp.tahapanPengadaan.tahapan.id == 14) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 10170200 || pp.tahapanPengadaan.tahapan.id == 10170300 || pp.tahapanPengadaan.tahapan.id == 9 || pp.tahapanPengadaan.tahapan.id == 19000002) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 10060000) {
                if (pp.metodePengadaan.id != 4 && pp.metodePengadaan.id != 5) {
                    isHasView = true;
                } else if (pp.metodePengadaan.id == 4 || pp.metodePengadaan.id == 5) {
                    isHasView = true;
                }

            } else if (pp.tahapanPengadaan.tahapan.id == 10070000) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 10100000) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 15) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 18) {
                isHasView = true;
            } else if (pp.tahapanPengadaan.tahapan.id == 20 || pp.tahapanPengadaan.tahapan.id == 21) { //kontrak or SPK
                isHasView = true;
            }

            return isHasView;
        }

        $scope.jumlahPengadaan = 0;

        $scope.cariNamaPengadaan = '';
        $scope.rowPerPage = 5;
        $scope.currentPage = 1;
        $scope.cariTahapanPengadaan = 0;

        $scope.getPengadaanList = function (params) {
            $scope.loading = true;

            RequestService.doPOST('/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorWithPagination', params)
                .then(function successCallback(reponse, index, array) {
                    $scope.pendaftaranVendorList = reponse.data.pendaftaranVendorList;
                    $scope.totalItems = reponse.data.jmlData;
                    $scope.jumlahPengadaan = $scope.totalItems;
                    angular.forEach($scope.pendaftaranVendorList, function (value, index) {
                        $scope.pendaftaranVendorList[index].isHasView = $scope.getValidationButtonView(value.pengadaan);
                    });
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
                userId: $rootScope.userLogin.user.id,
                searchingKeyWord: '{filter:[{key:pengadaan.namaPengadaan, value:"' + $scope.cariNamaPengadaan + '"}], sort:pengadaan.tanggalNotaDinas, pengadaan.tahapanPengadaan:' + $scope.cariTahapanPengadaan + '}'
            };

            $scope.getPengadaanList(params);
        }
        $scope.getPage(1);

        $scope.getSearch = function () {
            $scope.getPage(1);
        }


        //jumlah Menang
        $scope.totalMenang = 0;
        $scope.getMenang = function () {
            $scope.totalMenang = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/countPenetapanByUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    $scope.totalMenang = data;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    $scope.totalMenang = 0;
                });
        };
        $scope.getMenang();

        //nilai Pengadaan
        $scope.nilaiPengadaan = 0;
        $scope.getNilaiTotalPengadaan = function () {
            $scope.nilaiPengadaan = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/DashboardVendorServices/getNilaiTotalPengadaan')
                .success(function (data) {
                    $scope.nilaiPengadaan = data;
                    var number = $scope.nilaiPengadaan;
                    var text = "";
                    if (number) {
                        var abs = Math.abs(number);
                        if (abs >= Math.pow(10, 12)) {
                            // trillion
                            number = (number / Math.pow(10, 12)).toFixed(1);
                            text = "TRILIUN";
                        } else if (abs < Math.pow(10, 12) && abs >= Math.pow(10, 9)) {
                            // billion
                            number = (number / Math.pow(10, 9)).toFixed(1);
                            text = "MILIAR";
                        } else if (abs < Math.pow(10, 9) && abs >= Math.pow(10, 6)) {
                            // million
                            number = (number / Math.pow(10, 6)).toFixed(1);
                            text = "JUTA";
                        } else if (abs < Math.pow(10, 6) && abs >= Math.pow(10, 3)) {
                            // thousand
                            number = (number / Math.pow(10, 3)).toFixed(1);
                            text = "RIBU";
                        }
                        $scope.shortNumberPengadaan = {
                            number: number,
                            text: text
                        }
                    }
                })
                .error(function (data) {
                    $scope.nilaiPengadaan = 0;
                })
        }
        $scope.getNilaiTotalPengadaan();

        //jumlah pengadaan
        $scope.totalPengadaan = 0;
        $scope.totalProses = 0;
        $scope.getCountAllPengadaan = function () {
            $scope.totalPengadaan = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getCountAllPengadaan')
                .success(function (data, status, headers, config) {
                    $scope.totalPengadaan = data;
                    $scope.totalProses = 'loading...';
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/countPengadaanByTahapan/20/20')
                        .success(function (data, status, headers, config) {
                            $scope.totalProses = $scope.totalPengadaan - data;
                        });
                })
                .error(function (data, status, headers, config) {});
        }
        $scope.getCountAllPengadaan();
        //jumlahUndanga		
        $scope.jumlahUndangan = 0;
        $scope.getJumlahUndangan = function () {
            $scope.jumlahUndangan = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/undanganVendorServices/countByVendorUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    $scope.jumlahUndangan = data;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    $scope.jumlahUndangan = 0;
                });
        };
        $scope.getJumlahUndangan();
        //jumlah Pengumuman		
        $scope.jumlahPengumuman = 0;
        $scope.getPengumuman = function () {
            $scope.jumlahPengumuman = 'loading...';
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/PengumumanPengadaanServices/countPengumumanPengadaanNow')
                .success(function (data, status, headers, config) {
                    $scope.jumlahPengumuman = data;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    $scope.jumlahPengumuman = 0;
                });
        };
        $scope.getPengumuman();


    }
})();