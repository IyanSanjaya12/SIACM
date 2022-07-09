(function () {
    'use strict';

    angular.module('naut').controller('RegistrasiJadwalTahapanPengadaanTambahController', RegistrasiJadwalTahapanPengadaanTambahController);

    function RegistrasiJadwalTahapanPengadaanTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        form.tahapanPengadaan = {};
        var refresh = function () {
            form.isJadwal = false;
            form.isLokasi = false;
            form.isNoUndangan = false;
            form.isTglAwal = false;
            form.isTglAkhir = false;
            form.isWaktuAwal = false;
            form.isWaktuAkhir = false;
        }
        refresh();

        $http.get($rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/getList').success(function (data, status, headers, config) {
            $scope.tahapanPengadaanList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/registrasijadwaltahapanpengadaan');
        }

        form.simpan = function () {
            var jadwal, lokasi, undangan, tglAwal, tglAkhir, waktuAwal, waktuAkhir;
            if (form.isJadwal == true) {
                jadwal = 1;
            } else {
                jadwal = 0;
            }
            if (form.isLokasi == true) {
                lokasi = 1;
            } else {
                lokasi = 0;
            }
            if (form.isNoUndangan == true) {
                undangan = 1;
            } else {
                undangan = 0;
            }
            if (form.isTglAwal == true) {
                tglAwal = 1;
            } else {
                tglAwal = 0;
            }
            if (form.isTglAkhir == true) {
                tglAkhir = 1;
            } else {
                tglAkhir = 0;
            }
            if (form.isWaktuAwal == true) {
                waktuAwal = 1;
            } else {
                waktuAwal = 0;
            }
            if (form.isWaktuAkhir == true) {
                waktuAkhir = 1;
            } else {
                waktuAkhir = 0;
            }

            if (typeof form.tahapanPengadaan.selected === 'undefined') {
                alert("Pilih Tahapan Pengadaan.");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                    tahapanPengadaan: form.tahapanPengadaan.selected.id,
                    isJadwal: jadwal,
                    isLokasi: lokasi,
                    isNoUndangan: undangan,
                    isTglAwal: tglAwal,
                    isTglAkhir: tglAkhir,
                    isWaktuAwal: waktuAwal,
                    isWaktuAkhir: waktuAkhir
                }
                console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: postForm
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Registrasi Jadwal Tahapan Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.tahapanPengadaan.selected = undefined;
                    refresh();
                });
            }
        };
    }
    RegistrasiJadwalTahapanPengadaanTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
