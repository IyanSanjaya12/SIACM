(function () {
    'use strict';

    angular.module('naut').controller('KondisiPengadaanTambahController', KondisiPengadaanTambahController);

    function KondisiPengadaanTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        form.jenisPengadaan = {};
        form.metodePengadaan = {};
        form.metodePenyampaianDokumenPengadaan = {};
        form.metodePenawaranHarga = {};
        form.jenisPenawaran = {};
        form.evaluasiPenawaranHarga = {};
        form.sistemEvaluasiPenawaran = {};
        form.kualifikasiVendor = {};
        form.vendorRequirement = {};

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

        //jenis penawaran
        $http.get($rootScope.backendAddress + '/procurement/master/JenisPenawaranServices/getJenisPenawaranList').success(function (data, status, headers, config) {
            $scope.jenisPenawaranList = data;
        }).error(function (data, status, headers, config) {})
        
        //evaluasi penawaran harga
        $http.get($rootScope.backendAddress + '/procurement/master/JenisPenawaranServices/getJenisPenawaranList').success(function (data, status, headers, config) {
            $scope.evaluasiPenawaranHargaList = data;
        }).error(function (data, status, headers, config) {})

        //sistem evaluasi penawaran
        $http.get($rootScope.backendAddress + '/procurement/master/SistemEvaluasiPenawaranServices/getSistemEvaluasiPenawaranList').success(function (data, status, headers, config) {
            $scope.sistemEvaluasiPenawaranList = data;
        }).error(function (data, status, headers, config) {})

        //kualifikasi vendor
        $http.get($rootScope.backendAddress + '/procurement/master/kualifikasi-vendor/get-list').success(function (data, status, headers, config) {
            $scope.kualifikasiVendorList = data;
        }).error(function (data, status, headers, config) {})

        //vendor requirement
        $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementList').success(function (data, status, headers, config) {
            $scope.vendorRequirementList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/kondisipengadaan');
        }

        form.simpan = function () {
            if (typeof form.jenisPengadaan.selected === 'undefined') {
                alert("Jenis Pengadaan belum dipilih.");
            } else if (typeof form.metodePengadaan.selected === 'undefined') {
                alert("Metode Pengadaan belum dipilih.");
            } else if (typeof form.metodePenyampaianDokumenPengadaan.selected == 'undefined') {
                alert("Metode Penyampaian Dokumen Pengadaan belum dipilih");
            } else if (typeof form.metodePenawaranHarga.selected == 'undefined'){
                alert("Metode Penawaran Harga belum dipilih");
            } else if (typeof form.jenisPenawaran.selected == 'undefined'){
                alert("Jenis Penawaran belum dipilih");
            } else if(typeof form.evaluasiPenawaranHarga.selected == 'undefined'){
                alert("Evaluasi Penawaran Harga belum dipilih");
            } else if (typeof form.sistemEvaluasiPenawaran.selected == 'undefined'){
                alert("Sistem Evaluasi Penawaran belum dipilih");
            } else if (typeof form.kualifikasiVendor.selected == 'undefined'){
                alert("Kualifikasi Vendor belum dipilih");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                        jenisPengadaan: form.jenisPengadaan.selected.id,
                        metodePengadaan: form.metodePengadaan.selected.id,
                        metodePenyampaianDokumenPengadaan: form.metodePenyampaianDokumenPengadaan.selected.id,
                        metodePenawaranHarga: form.metodePenawaranHarga.selected.id,
                        jenisPenawaran: form.jenisPenawaran.selected.id,
                        evaluasiPenawaranHarga: form.evaluasiPenawaranHarga.selected.id,
                        sistemEvaluasiPenawaran: form.sistemEvaluasiPenawaran.selected.id,
                        kualifikasiVendor: form.kualifikasiVendor.selected.id
                    }
                if(typeof form.vendorRequirement.selected !== 'undefined' && form.vendorRequirement.selected!=null)
                    postForm.vendorRequirement = form.vendorRequirement.selected.id;
                
                //console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/kondisiPengadaanServices/createKondisiPengadaan',
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
                        toaster.pop('success', 'Kondisi Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.jenisPengadaan.selected = undefined;
                    form.metodePengadaan.selected = undefined;
                    form.metodePenyampaianDokumenPengadaan.selected = undefined;
                    form.metodePenawaranHarga.selected = undefined;
                    form.jenisPenawaran.selected = undefined;
                    form.evaluasiPenawaranHarga.selected = undefined;
                    form.sistemEvaluasiPenawaran.selected = undefined;
                    form.kualifikasiVendor.selected = undefined;
                    form.vendorRequirement.selected = undefined;
                });
            };
        };
    }
    KondisiPengadaanTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
