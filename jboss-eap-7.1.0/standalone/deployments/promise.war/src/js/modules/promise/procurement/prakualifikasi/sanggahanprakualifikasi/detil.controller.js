/**=========================================================
 * Module: SanggahanPrakualifikasiDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SanggahanPrakualifikasiDetilController', SanggahanPrakualifikasiDetilController);

    function SanggahanPrakualifikasiDetilController($scope, $http, $rootScope, $resource, $location, $filter) {
        var sanggahanDetil = this;
        if($rootScope.pengadaan.tahapanPengadaan.tahapan.id==10110000)
            $scope.statusSimpan = true;

        $scope.getSanggahanPrakualifikasi = function(){
            $scope.loading = true;
            
            $http.get($rootScope.backendAddress + '/procurement/sanggahan/sanggahanPrakualifikasiServices/getListByPengadaan/' + $rootScope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                if(data.length > 0){
                    sanggahanDetil.tanggalSanggahan = data[0].tanggal;
                    $scope.keterangan = data[0].keterangan;
                    $scope.statusSanggahan = data[0].status;
                }
                    
                $scope.loading = false;
            })
            .error(function (data,
                    status, headers, config){
                $scope.loading = false;
            });
        }
        $scope.getSanggahanPrakualifikasi();
        
        $scope.getHasilKualifikasi = function () {
            $scope.loadingHasilKualifikasi = true;
            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/hasilkualifikasi/findByPengadaanId/' + $rootScope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                    if (data.length > 0) {
                        $scope.hasilKualifikasiList = data;
                        $scope.loadingHasilKualifikasi = false;
                    } else {
                        if ($rootScope.pengadaan.jenisPengadaan.id == 1 || $rootScope.pengadaan.jenisPengadaan.id == 4) { //Barang atau Jasa Lainnya
                            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/hasilkualifikasi/initHasilKualifikasi/' + $rootScope.pengadaan.id)
                                .success(function (data, status, headers, config) {
                                    $scope.hasilKualifikasiList = data;
                                    $scope.loadingHasilKualifikasi = false;
                                })
                                .error(function (data, status, headers, config) {
                                    $scope.loadingHasilKualifikasi = false;
                                });
                        }
                    }

                })
                .error(function (data, status, headers, config) {
                    $scope.loadingHasilKualifikasi = false;
                });
        }
        $scope.getHasilKualifikasi();

        // Datepicker
        sanggahanDetil.toggleMin = function () {
            sanggahanDetil.minDate = sanggahanDetil.minDate ? null : new Date();
        };
        sanggahanDetil.toggleMin();
        sanggahanDetil.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        sanggahanDetil.format = sanggahanDetil.formats[4];

        sanggahanDetil.tanggalSanggahanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            sanggahanDetil.tanggalSanggahanOpened = true;
        };

        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $rootScope.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.nextTahapan = data;
                    })
                    .error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        //update pengadaan
        $scope.updatePengadaan = function (extValue) {
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
                    pengadaanId: $rootScope.pengadaan.id,
                    tahapanPengadaanId: ($scope.nextTahapan + extValue)
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        };

        $scope.btnSimpan = function () {
            $scope.loadingSimpanSanggahan = true;
            var konfirmasi = confirm("Apakah anda yakin ingin menyimpan data Sanggahan Prakualifkasi?");
            if (konfirmasi) {
                var data = {
                    pengadaan: $rootScope.pengadaan.id,
                    tanggal: $filter('date')(sanggahanDetil.tanggalSanggahan, 'dd-MM-yyyy'),
                    keterangan: $scope.keterangan,
                    status: $scope.statusSanggahan
                };

                //console.log("Cek Data : "+JSON.stringify(data));
                $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/sanggahan/sanggahanPrakualifikasiServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: data
                    })
                    .success(function (data, status, headers, config) {
                        if ($scope.statusSanggahan == 0 || $scope.statusSanggahan == 2) {
                            $scope.updatePengadaan(1);
                        } else {
                            $scope.updatePengadaan(0);
                        }
                        $scope.statusSimpan = true;
                        $scope.messageSimpan = "Penyimpan Data Sanggahan Prakualifikasi Berhasil";
                        $scope.loadingSimpanSanggahan = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loadingSimpanSanggahan = false;
                    });
            } else {
                return false;
            }

        };

        $scope.btnKembali = function () {
            $location.path("/app/promise/procurement/prakualifikasi/sanggahanprakualifikasi");
        }

    }

    SanggahanPrakualifikasiDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();