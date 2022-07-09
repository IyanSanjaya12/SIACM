/**
 * ========================================================= Module:
 * PengadaanTambahController.js
 * =========================================================
 */

(function() {
    'use strict';

    angular.module('naut').controller('PengadaanTambahController',
        PengadaanTambahController);
    angular.module('naut').service();

    function PengadaanTambahController($scope, $modal, $filter, $location,
        $http, $rootScope, $resource, ngTableParams, editableOptions,
        editableThemes, $q, $timeout) {

        var vm = this;

        $scope.pengadaanId = 0;

        $scope.disabled = undefined;

        $scope.enable = function() {
            $scope.disabled = false;
        };

        $scope.disable = function() {
            $scope.disabled = true;
        };

        $scope.clear = function() {
            $scope.kategoriPengadaan.selected = undefined;
        };

        // Datepicker
        vm.clear = function() {
            vm.tanggalDokumen = null;
            vm.tanggalIzinPrinsip = null;
        };
        vm.disabled = function(date, mode) {
            return false; // ( mode === 'day' && ( date.getDay() === 0 ||
            // date.getDay() === 6 ) );
        };
        vm.toggleMin = function() {
            vm.minDate = vm.minDate ? null : new Date();
        };
        vm.toggleMin();
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        vm.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        vm.format = vm.formats[4];

        vm.dokumenTanggalOpen = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.dokumenTanggalOpened = true;
        };

        vm.izinPrinsipTanggalOpen = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.izinPrinsipTanggalOpened = true;
        };

        $scope.kategoriPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/kategoriPengadaan/list').success(
            function(data, status, headers, config) {
                $scope.kategoriPengadaanList = data;
            }).error(function(data, status, headers, config) {});

        $scope.alurPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/alurPengadaan/list').success(
            function(data, status, headers, config) {
                $scope.alurPengadaanList = data;
            }).error(function(data, status, headers, config) {});

        $scope.jenisPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/jenisPengadaan/list')
            .success(function(data, status, headers, config) {
                $scope.jenisPengadaanList = data;
            }).error(function(data, status, headers, config) {});

        $scope.metodePengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/metodePengadaan/list').success(
                function(data, status, headers, config) {
                    $scope.metodePengadaanList = data;
                }).error(function(data, status, headers, config) {});

        $scope.kualifikasiPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/kualifikasiPengadaan/list').success(
            function(data, status, headers, config) {
                $scope.kualifikasiPengadaanList = data;
            }).error(function(data, status, headers, config) {});

        $scope.metodePenyampaianDokumen = {};
        $http.get($rootScope.backendAddress + '/procurement/metodePenyampaianDokumenPengadaan/list')
            .success(function(data, status, headers, config) {
                $scope.metodePenyampaianDokumenList = data;
            }).error(function(data, status, headers, config) {});

        $scope.metodePenawaranHarga = {};
        $http.get($rootScope.backendAddress + '/procurement/metodePenawaranHarga/list').success(
            function(data, status, headers, config) {
                $scope.metodePenawaranHargaList = data;
            }).error(function(data, status, headers, config) {});
        vm.metodePenawaranHarga = $scope.metodePenawaranHarga;

        $scope.jenisPenawaran = {};
        $http.get($rootScope.backendAddress + '/procurement/jenisPenawaran/list')
            .success(function(data, status, headers, config) {
                $scope.jenisPenawaranList = data;
            }).error(function(data, status, headers, config) {});

        $scope.sistemEvaluasiPenawaran = {};
        $http.get($rootScope.backendAddress + '/procurement/evaluasiPenawaran/list').success(
            function(data, status, headers, config) {
                $scope.sistemEvaluasiPenawaranList = data;
            }).error(function(data, status, headers, config) {});

        $scope.kualifikasiVendor = {};
        $http.get($rootScope.backendAddress + '/procurement/kualifikasiVendor/list').success(
            function(data, status, headers, config) {
                $scope.kualifikasiVendorList = data;
            }).error(function(data, status, headers, config) {});

        $scope.mataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/mataUang/list')
            .success(function(data, status, headers, config) {
                $scope.mataUangList = data;
            }).error(function(data, status, headers, config) {});

        $scope.pengalamanMinimal = {};
        $scope.pengalamanMinimalList = [];
        for (var i = 1; i <= 10; i++) {
            $scope.pengalamanMinimalList.push({
                pengalam: i
            });
        }

        // save otomatis tiap 60 detik
        $scope.saveDataPengadaanAuto = function() {
            console.log('info : ' + JSON.stringify($scope.pengalamanMinimal));
            if (typeof vm.nomor !== 'undefined') {
                var kategoriPengadaanId = 0;
                if (typeof $scope.kategoriPengadaan.selected !== 'undefined')
                    kategoriPengadaanId = $scope.kategoriPengadaan.selected.id;

                var alurPengadaanId = 0;
                if (typeof $scope.alurPengadaan.selected !== 'undefined')
                    alurPengadaanId = $scope.alurPengadaan.selected.id;

                var jenisPengadaanId = 0;
                if (typeof $scope.jenisPengadaan.selected !== 'undefined')
                    jenisPengadaanId = $scope.alurPengadaan.selected.id;

                var metodePengadaanId = 0;
                if (typeof $scope.metodePengadaan.selected !== 'undefined')
                    metodePengadaanId = $scope.metodePengadaan.selected.id;

                var kualifikasiPengadaanId = 0;
                if (typeof $scope.kualifikasiPengadaan.selected !== 'undefined')
                    kualifikasiPengadaanId = $scope.kualifikasiPengadaan.selected.id;

                var metodePenyampaianDokumenId = 0;
                if (typeof $scope.metodePenyampaianDokumen.selected !== 'undefined')
                    metodePenyampaianDokumenId = $scope.metodePenyampaianDokumen.selected.id;

                var metodePenawaranHargaId = 0;
                if (typeof $scope.metodePenawaranHarga.selected !== 'undefined')
                    metodePenawaranHargaId = $scope.metodePenawaranHarga.selected.id;

                var jenisPenawaranId = 0;
                if (typeof $scope.jenisPenawaran.seleceted !== 'undefined')
                    jenisPenawaranId = $scope.jenisPenawaran.selected.id;

                var sistemEvaluasiPenawaranId = 0;
                if (typeof $scope.sistemEvaluasiPenawaran.selected !== 'undefined')
                    sistemEvaluasiPenawaranId = $scope.sistemEvaluasiPenawaran.selected.id;

                var kualifikasiVendorId = 0;
                if (typeof $scope.kualifikasiVendor.selected !== 'undefined')
                    kualifikasiVendorId = $scope.kualifikasiVendor.selected.id;

                var minimalPengalaman = 0;
                if (typeof $scope.pengalamanMinimal !== 'undefined')
                    minimalPengalaman = $scope.pengalamanMinimal.selected.pengalam;

                var matauangId = 0;
                if (typeof $scope.mataUang.selected !== 'undefined')
                    matauangId = $scope.mataUang.selected.id;

                var tahunPengalamanStart = 0;
                if (typeof vm.tahunPengalamanStart !== 'undefined')
                    tahunPengalamanStart = vm.tahunPengalamanStart;

                var tahunPengalamanEnd = 0;
                if (typeof vm.tahunPengalamanEnd !== 'undefined')
                    tahunPengalamanEnd = vm.tahunPengalamanEnd;

                var pengadaan = {
                    pengadaanId: $scope.pengadaanId,
                    nomor: vm.nomor,
                    nama: vm.nama,
                    dokumenNomor: vm.dokumenNomor,
                    dokumenTanggal: $filter('date')(vm.dokumenTanggal, 'yyyy-MM-dd'),
                    izinPrinsipNomor: vm.izinPrinsipNomor,
                    izinPrinsipTanggal: $filter('date')(vm.izinPrinsipTanggal, 'yyyy-MM-dd'),
                    kategoriPengadanId: kategoriPengadaanId,
                    alurPengadaanId: alurPengadaanId,
                    metodePengadaanId: metodePengadaanId,
                    kualifikasiPengadaanId: kualifikasiPengadaanId,
                    metodePenyampaianDokumenId: metodePenyampaianDokumenId,
                    metodePenawaranHargaId: metodePenawaranHargaId,
                    jenisPenawaranId: jenisPenawaranId,
                    sistemEvaluasiPenawaranId: sistemEvaluasiPenawaranId,
                    kualifikasiVendorId: kualifikasiVendorId,
                    identifikasiHPS: vm.identifikasiHPS,
                    minimalPengalaman: minimalPengalaman,
                    minimalNilai: vm.minimalNilai,
                    matauangId: matauangId,
                    tahunPengalamanStart: tahunPengalamanStart,
                    tahunPengalamanEnd: tahunPengalamanEnd,
                    keterangan: vm.keterangan
                };

                if ($scope.pengadaanId > 0) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/edit',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function(obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: pengadaan
                    }).success(function(data, status, headers, config) {
                        console.log("SAVE OK : " + JSON.stringify(data));
                    });
                } else {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/create',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function(obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: pengadaan
                    }).success(function(data, status, headers, config) {
                        if (typeof data !== 'undefined')
                            $scope.pengadaanId = data.id;
                        console.log("SAVE OK : " + JSON.stringify(data));
                    });
                }
                console.log("FORM for Upload : " + JSON.stringify(pengadaan));
            } else {
                console.log("No. Pengdaaan belum disi.");
            }

        };
        $scope.intervalFunction = function() {
            $timeout(function() {
                $scope.saveDataPengadaanAuto();
                $scope.intervalFunction();
            }, 60000)
        };
        $scope.intervalFunction();
    }

    PengadaanTambahController.$inject = ['$scope', '$modal', '$filter',
        '$location', '$http', '$rootScope', '$resource', 'ngTableParams',
        'editableOptions', 'editableThemes', '$q', '$timeout'
    ];

})();