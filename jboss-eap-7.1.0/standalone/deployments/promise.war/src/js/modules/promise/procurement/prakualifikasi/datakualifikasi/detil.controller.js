/**=========================================================
 * Module: DataKualifikasiDetilController.js
 * Author: Musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataKualifikasiDetilController', DataKualifikasiDetilController);

    function DataKualifikasiDetilController($http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, toaster, $location) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        var form = this;

        form.id = $rootScope.userLogin.user.id;
        form.namaPengguna = $rootScope.userLogin.user.namaPengguna;
        form.userId = $rootScope.userLogin.user.username;
        form.dataKeuanganList = [];
        form.dataKualifikasi = {};
        form.dataKualifikasiPost={};
        form.dataKualifikasiPost.pengadaanId = $rootScope.detilPengadaan.id;

        /* ------------------------------------------------------------------------------------------------ */


        /* =============================== START Data Kualifikasi =========================================== */
        var getDataKualifikasi = function (vendorId) {
                $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/dataKualifikasiVendorServices/getListByPengadaanAndVendor/' + form.dataKualifikasiPost.pengadaanId + '/' + vendorId)
                    .success(function (data, status, headers, config) {
                        if (data != undefined && data.length > 0) {
                            form.dataKualifikasi = data[0];
                        } else {
                            getKeuanganVendor(vendorId);
                        }
                    });
            }
            /* --------------------------------- END Data Kualifikasi ------------------------------------------- */

        /* ================================= START Data Vendor ============================================ */
        $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + form.id)
            .success(function (data, status, headers, config) {
                if (data != undefined) {
                    $scope.vendorId = data.id;
                    getDataKualifikasi($scope.vendorId);
                }
            });
        /* --------------------------------- END Data Vendor ---------------------------------------------- */


        /* ================================= START Data Keuangan ========================================== */
        var getKeuanganVendor = function (vendorId) {
                $http.get($rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/getKeuanganVendorByVendorId/' + vendorId)
                    .success(function (data, status, headers, config) {
                        if (data.length > 0) {
                            angular.forEach(data, function (value, index) {
                                if ((index + 1) == data.length) {
                                    form.dataKualifikasi = value;
                                    form.dataKualifikasi.id=undefined;
                                }
                                form.dataKeuanganList.push(value);
                            })
                        }

                        $scope.tableKeuanganVendor = new ngTableParams({
                            page: 1, // show first page
                            count: 5 // count per page
                        }, {
                            total: form.dataKeuanganList.length, // length of data4
                            getData: function ($defer, params) {
                                $defer.resolve(form.dataKeuanganList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                            }
                        });
                    });
            }
            /* ------------------------------------------------------------------------------------------------ */


        /* ================================= START CONTROLLER DETAIL ====================================== */

        // Kembali ke Dashboard
        $scope.back = function () {
            $location.path('/appvendor/promise/procurement/prakualifikasi/datakualifikasi');
        }

        // Kembali ke Dashboard
        $scope.backToDashboard = function () {
            $location.path('/appvendor/promise/dashboard');
        }

        // Simpan Data
        $scope.btnSimpan = function () {
            if (form.dataKualifikasi !== undefined) {
                if ($scope.validate(form.dataKualifikasi)) {
                    $scope.saveDataKualifikasi(form.dataKualifikasi);
                }
            } else {
                toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Keuangan');
            }
        }

        $scope.saveDataKualifikasi = function (dataKeuanganId) {
            form.dataKualifikasiPost.tanggalKeuanganUpdate = $filter('date')(form.dataKualifikasi.tanggalKeuanganUpdate, 'dd-MM-yyyy');
            form.dataKualifikasiPost.vendorId=form.dataKualifikasi.vendor.id;
            form.dataKualifikasiPost.nomorAudit=form.dataKualifikasi.nomorAudit;
            form.dataKualifikasiPost.tanggalAudit=$filter('date')(form.dataKualifikasi.tanggalAudit, 'dd-MM-yyyy');;
            form.dataKualifikasiPost.namaAudit = form.dataKualifikasi.namaAudit;
            form.dataKualifikasiPost.tahunKeuangan = form.dataKualifikasi.tahunKeuangan;
            form.dataKualifikasiPost.kas = form.dataKualifikasi.kas;
            form.dataKualifikasiPost.bank = form.dataKualifikasi.bank;
            form.dataKualifikasiPost.totalPiutang = form.dataKualifikasi.totalPiutang;
            form.dataKualifikasiPost.persediaanBarang = form.dataKualifikasi.persediaanBarang;
            form.dataKualifikasiPost.pekerjaanDalamProses = form.dataKualifikasi.pekerjaanDalamProses;
            form.dataKualifikasiPost.totalAktivaLancar = form.dataKualifikasi.totalAktivaLancar;
            form.dataKualifikasiPost.peralatanDanMesin = form.dataKualifikasi.peralatanDanMesin;
            form.dataKualifikasiPost.inventaris = form.dataKualifikasi.inventaris;
            form.dataKualifikasiPost.gedungGedung = form.dataKualifikasi.gedungGedung;
            form.dataKualifikasiPost.totalAktivaTetap = form.dataKualifikasi.totalAktivaTetap;
            form.dataKualifikasiPost.aktivaLainnya = form.dataKualifikasi.aktivaLainnya;
            form.dataKualifikasiPost.totalAktiva = form.dataKualifikasi.totalAktiva;
            form.dataKualifikasiPost.hutangDagang = form.dataKualifikasi.hutangDagang;
            form.dataKualifikasiPost.hutangPajak = form.dataKualifikasi.hutangPajak;
            form.dataKualifikasiPost.hutangLainnya = form.dataKualifikasi.hutangLainnya;
            form.dataKualifikasiPost.totalHutangJangkaPendek = form.dataKualifikasi.totalHutangJangkaPendek;
            form.dataKualifikasiPost.hutangJangkaPanjang = form.dataKualifikasi.hutangJangkaPanjang;
            form.dataKualifikasiPost.kekayaanBersih = form.dataKualifikasi.kekayaanBersih;
            form.dataKualifikasiPost.totalPasiva = form.dataKualifikasi.totalPasiva;
            if (form.dataKualifikasi.id == undefined) {
                var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/dataKualifikasiVendorServices/create';
            } else {
                form.dataKualifikasiPost.id = form.dataKualifikasi.id;
                var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/dataKualifikasiVendorServices/update';
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
                data: form.dataKualifikasiPost
            }).success(function (data, status, headers, config) {
                toaster.pop('success', 'Pemberitahuan', 'Berhasil merubah data Keuangan Vendor');
                $scope.backToDashboard();
            }).error(function (data, status, headers, config) {
                toaster.pop('error', 'Kesalahan', 'Berhasil merubah data Keuangan Vendor');
            });
        }

        var saveDataKeuangan = function (dataKeuangan) {
            if ($scope.validate(dataKeuangan)) {
                if (dataKeuangan !== undefined) {
                    //                angular.forEach(form.dataKeuanganList, function (dataList, index) {
                    dataKeuangan.vendor = $scope.vendorId;
                    dataKeuangan.tanggalAudit = new Date(dataKeuangan.tanggalAudit);

                    if (dataKeuangan.id == undefined) {
                        var targetURI = $rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/createKeuanganVendor';
                    } else {
                        var targetURI = $rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/editKeuanganVendor';
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
                        data: dataKeuangan
                    }).success(function (data, status, headers, config) {
                        //toaster.pop('success', 'Pemberitahuan', 'Berhasil merubah data Keuangan Vendor');
                        //$scope.backToDashboard();
                        $scope.saveDataKualifikasi(data.id);
                    }).error(function (data, status, headers, config) {
                        //toaster.pop('error', 'Kesalahan', 'Berhasil merubah data Keuangan Vendor');
                    });
                    //                });
                }
            }
        }

        // --------------------------------- Start Perhitungan Neraca per Lajur Buku Besar nya ----------------------------------- >

        //Aktiva
        $scope.tambahAktivaLancar = function () {
            form.dataKualifikasi.totalAktivaLancar = form.dataKualifikasi.kas + form.dataKualifikasi.bank + form.dataKualifikasi.totalPiutang + form.dataKualifikasi.persediaanBarang + form.dataKualifikasi.pekerjaanDalamProses;
        }

        $scope.tambahAktivaTetap = function () {
            form.dataKualifikasi.totalAktivaTetap = form.dataKualifikasi.peralatanDanMesin + form.dataKualifikasi.inventaris + form.dataKualifikasi.gedungGedung;
        }

        $scope.totalAktiva = function () {
            form.dataKualifikasi.totalAktiva = form.dataKualifikasi.totalAktivaLancar + form.dataKualifikasi.totalAktivaTetap + form.dataKualifikasi.aktivaLainnya;
        }

        //Pasiva
        $scope.tambahPasiva = function () {
            form.dataKualifikasi.totalHutangJangkaPendek = form.dataKualifikasi.hutangDagang + form.dataKualifikasi.hutangPajak + form.dataKualifikasi.hutangLainnya;
        }

        $scope.rugiLaba = function () {
            form.dataKualifikasi.kekayaanBersih = (form.dataKualifikasi.kas + form.dataKualifikasi.bank + form.dataKualifikasi.totalPiutang + form.dataKualifikasi.persediaanBarang + form.dataKualifikasi.pekerjaanDalamProses + form.dataKualifikasi.peralatanDanMesin + form.dataKualifikasi.inventaris + form.dataKualifikasi.gedungGedung + form.dataKualifikasi.aktivaLainnya) - (form.dataKualifikasi.hutangDagang + form.dataKualifikasi.hutangPajak + form.dataKualifikasi.hutangLainnya + form.dataKualifikasi.hutangJangkaPanjang);
        }

        $scope.totalPasiva = function () {
                form.dataKualifikasi.totalPasiva = ((form.dataKualifikasi.kas + form.dataKualifikasi.bank + form.dataKualifikasi.totalPiutang + form.dataKualifikasi.persediaanBarang + form.dataKualifikasi.pekerjaanDalamProses + form.dataKualifikasi.peralatanDanMesin + form.dataKualifikasi.inventaris + form.dataKualifikasi.gedungGedung + form.dataKualifikasi.aktivaLainnya) - (form.dataKualifikasi.hutangDagang + form.dataKualifikasi.hutangPajak + form.dataKualifikasi.hutangLainnya + form.dataKualifikasi.hutangJangkaPanjang)) + (form.dataKualifikasi.hutangDagang + form.dataKualifikasi.hutangPajak + form.dataKualifikasi.hutangLainnya + form.dataKualifikasi.hutangJangkaPanjang);
            }
            // --------------------------------- End Perhitungan Neraca per Lajur Buku Besar nya -------------------------------------- >

        $scope.tanggalAuditStatus = false;
        $scope.tanggalAuditOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAuditStatus = true;
        };

        $scope.tanggalUpdateStatus = false;
        $scope.tanggalUpdateOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalUpdateStatus = true;
        };

        $scope.validasiTahun = function (tahun) {
            var lanjut = true;
            if (tahun < 1950 || tahun > 2020) {
                toaster.pop('error', 'Kesalahan', 'Tahun melebihi batas (1950 s/d 2020)');
                document.getElementsByName("tahunKeuangan")[0].focus();
                lanjut = false;
            }

            return lanjut;
        }

        $scope.validate = function (dataKeuangan) {

            if (dataKeuangan.nomorAudit == undefined || dataKeuangan.nomorAudit == "") {
                dataKeuangan.nomorAuditError = true;
                document.getElementsByName("nomorAudit")[0].focus();
            } else {
                dataKeuangan.nomorAuditError = false;
            }

            if (dataKeuangan.tanggalAudit == undefined || dataKeuangan.tanggalAudit == "") {
                dataKeuangan.tanggalAuditError = true;
                document.getElementsByName("tanggalAudit")[0].focus();
            } else {
                dataKeuangan.tanggalAuditError = false;
            }

            if (dataKeuangan.namaAudit == undefined || dataKeuangan.namaAudit == "") {
                dataKeuangan.namaAuditError = true;
                document.getElementsByName("namaAudit")[0].focus();
            } else {
                dataKeuangan.namaAuditError = false;
            }

            if (dataKeuangan.tahunKeuangan == undefined || dataKeuangan.tahunKeuangan == "") {
                dataKeuangan.tahunKeuanganError = true;
                document.getElementsByName("tahunKeuangan")[0].focus();
            } else {
                dataKeuangan.tahunKeuanganError = false;
            }

            var totalAktivaError = false;
            if (dataKeuangan.totalAktiva == undefined || dataKeuangan.totalAktiva == "") {
                totalAktivaError = true;
                toaster.pop('warning', 'Kesalahan', 'Data Neraca Harus Lengkap');
            }


            if (dataKeuangan.nomorAuditError == false && dataKeuangan.tanggalAuditError == false && dataKeuangan.namaAuditError == false && dataKeuangan.tahunKeuanganError == false && totalAktivaError == false) {
                return true;
            } else {
                return false;
            }

        }

        /* ------------------------------------------------------------------------------------------------ */

    }

    DataKualifikasiDetilController.$inject = ['$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'toaster', '$location'];

})();
