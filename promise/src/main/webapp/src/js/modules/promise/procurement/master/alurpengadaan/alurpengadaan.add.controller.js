(function () {
    'use strict';

    angular.module('naut').controller('AlurPengadaanAddController', AlurPengadaanAddController);

    function AlurPengadaanAddController($scope, $http, $rootScope, $resource, $location, toaster, $state, $stateParams, RequestService) {
        var form = this;

        form.alurPengadaan = ($stateParams.alurPengadaan != undefined || $stateParams.alurPengadaan != null) ? $stateParams.alurPengadaan : {};

        //jenis pengadaan
        $http.get($rootScope.backendAddress + '/procurement/master/jenisPengadaanServices/getJenisPengadaanList').success(function (data, status, headers, config) {
            $scope.jenisPengadaanList = data;
        }).error(function (data, status, headers, config) {})

        //metode pengadaan
        $http.get($rootScope.backendAddress + '/procurement/master/metodePengadaanServices/getMetodePengadaanList').success(function (data, status, headers, config) {
            $scope.metodePengadaanList = data;
        }).error(function (data, status, headers, config) {})

        //metode penyampaian dokumen pengadaan
        $http.get($rootScope.backendAddress + '/procurement/master/metodePenyampaianDokumenServices/listMetodePenyampaianDokumenPengadaan').success(function (data, status, headers, config) {
            $scope.metodePenyampaianDokumenPengadaanList = data;
        }).error(function (data, status, headers, config) {})

        //metode penawaran harga
        $http.get($rootScope.backendAddress + '/procurement/master/MetodePenawaranHargaServices/getMetodePenawaranHargaList').success(function (data, status, headers, config) {
            $scope.metodePenawaranHargaList = data;
        }).error(function (data, status, headers, config) {})

        //sistem evaluasi penawaran
        $http.get($rootScope.backendAddress + '/procurement/master/SistemEvaluasiPenawaranServices/getSistemEvaluasiPenawaranList').success(function (data, status, headers, config) {
            $scope.sistemEvaluasiPenawaranList = data;
        }).error(function (data, status, headers, config) {})

        form.back = function () {
            $state.go('app.promise.procurement-master-alurpengadaan');
        }

        form.save = function () {
            if (typeof form.alurPengadaan.nama === 'undefined' || form.alurPengadaan.nama == '') {
                alert("Nama Alur Pengadaan belum disi.");
            } else if (typeof form.alurPengadaan.jenisPengadaan === 'undefined') {
                alert("Jenis Pengadaan belum dipilih.");
            } else if (typeof form.alurPengadaan.metodePengadaan === 'undefined') {
                alert("Metode Pengadaan belum dipilih.");
            } else if (typeof form.alurPengadaan.metodePenyampaianDokumenPengadaan === 'undefined') {
                alert("Metode Penyampaian Dokumen Pengadaan belum dipilih");
            } else if (typeof form.alurPengadaan.metodePenawaranHarga === 'undefined') {
                alert("Metode Penawaran Harga belum dipilih");
            } else if (typeof form.alurPengadaan.sistemEvaluasiPenawaran === 'undefined') {
                alert("Sistem Evaluasi Penawaran belum dipilih");
            } else {
                RequestService.modalConfirmation().then(function (result) {

                    /*get alur by kondisi*/
                    $scope.getAlurByKondisi = function (jenis, mp, mpd, mph, jp, sep) {
                        $http.get($rootScope.backendAddress + '/procurement/master/AlurPengadaanServices/getListByAllKondisi/' + jenis + '/' + mp + '/' + mpd + '/' + mph + '/' + jp + '/' + sep).success(function (data, status, headers, config) {
                            if (data.length == 0) {

                                var postForm = {
                                    nama: form.alurPengadaan.nama,
                                    keterangan: form.alurPengadaan.keterangan,
                                    jenisPengadaan: form.alurPengadaan.jenisPengadaan.id,
                                    metodePengadaan: form.alurPengadaan.metodePengadaan.id,
                                    metodePenyampaianDokumenPengadaan: form.alurPengadaan.metodePenyampaianDokumenPengadaan.id,
                                    metodePenawaranHarga: form.alurPengadaan.metodePenawaranHarga.id,
                                    sistemEvaluasiPenawaran: form.alurPengadaan.sistemEvaluasiPenawaran.id,
                                    metodeLelang: form.alurPengadaan.metodeLelang
                                }
                                $scope.loading = true;
                                $http({
                                    method: 'POST',
                                    url: $rootScope.backendAddress + '/procurement/master/AlurPengadaanServices/create',
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

                                    $scope.loading = false;
                                    $state.go('app.promise.procurement-master-alurpengadaan-add-tahapan', {
                                        alurPengadaan: data
                                    });

                                });


                            } else {
                                $state.go('app.promise.procurement-master-alurpengadaan-add-tahapan', {
                                    alurPengadaan: data[0]
                                });
                            }
                        })
                    }

                    $scope.getAlurByKondisi(form.alurPengadaan.jenisPengadaan.id, form.alurPengadaan.metodePengadaan.id, form.alurPengadaan.metodePenyampaianDokumenPengadaan.id, form.alurPengadaan.metodePenawaranHarga.id, 0, form.alurPengadaan.sistemEvaluasiPenawaran.id);

                });
            };
        };

        form.edit = function () {
            if (typeof form.alurPengadaan.nama === 'undefined' || form.alurPengadaan.nama == '') {
                alert("Nama Alur Pengadaan belum disi.");
            } else if (typeof form.alurPengadaan.jenisPengadaan === 'undefined') {
                alert("Jenis Pengadaan belum dipilih.");
            } else if (typeof form.alurPengadaan.metodePengadaan === 'undefined') {
                alert("Metode Pengadaan belum dipilih.");
            } else if (typeof form.alurPengadaan.metodePenyampaianDokumenPengadaan === 'undefined') {
                alert("Metode Penyampaian Dokumen Pengadaan belum dipilih");
            } else if (typeof form.alurPengadaan.metodePenawaranHarga === 'undefined') {
                alert("Metode Penawaran Harga belum dipilih");
            } else if (typeof form.alurPengadaan.sistemEvaluasiPenawaran === 'undefined') {
                alert("Sistem Evaluasi Penawaran belum dipilih");
            } else {
                RequestService.modalConfirmation().then(function (result) {

                    var postForm = {
                        id: form.alurPengadaan.id,
                        nama: form.alurPengadaan.nama,
                        keterangan: form.alurPengadaan.keterangan,
                        jenisPengadaan: form.alurPengadaan.jenisPengadaan.id,
                        metodePengadaan: form.alurPengadaan.metodePengadaan.id,
                        metodePenyampaianDokumenPengadaan: form.alurPengadaan.metodePenyampaianDokumenPengadaan.id,
                        metodePenawaranHarga: form.alurPengadaan.metodePenawaranHarga.id,
                        sistemEvaluasiPenawaran: form.alurPengadaan.sistemEvaluasiPenawaran.id,
                        metodeLelang: form.alurPengadaan.metodeLelang
                    }
                    $scope.loading = true;
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/master/AlurPengadaanServices/update',
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

                        $scope.loading = false;
                        $state.go('app.promise.procurement-master-alurpengadaan');

                    });

                });
            };
        };
    }

    AlurPengadaanAddController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', '$stateParams', 'RequestService'];

})();
