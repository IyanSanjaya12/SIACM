(function () {
    'use strict';

    angular.module('naut').controller('VendorRequirementUbahController', VendorRequirementUbahController);

    function VendorRequirementUbahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        var dataVendorRequirement;
        form.pengadaan = {};
        form.mataUang = {};

        if (typeof $rootScope.indexVendorRequirement !== 'undefined') {
            dataVendorRequirement = $rootScope.indexVendorRequirement;
        }
        form.id = dataVendorRequirement.id;
        form.minimalPengalaman = dataVendorRequirement.minimalPengalaman;
        form.tahunPengalamanMulai = dataVendorRequirement.tahunPengalamanMulai;
        form.tahunPengalamanAkhir = dataVendorRequirement.tahunPengalamanAkhir;
        form.nilaiKontrak = dataVendorRequirement.nilaiKontrak;
        form.mataUang.selected = dataVendorRequirement.mataUang;
        form.pengadaan.selected = dataVendorRequirement.pengadaan;

        //pengadaan
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList').success(function (data, status, headers, config) {
            $scope.pengadaanList = data;
        }).error(function (data, status, headers, config) {})

        //mata uang
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list').success(function (data, status, headers, config) {
            $scope.mataUangList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/vendorrequirement');
        }

        form.simpan = function () {
            if (typeof form.minimalPengalaman === 'undefined' || form.minimalPengalaman == '') {
                alert("Minimal Pengalaman belum diisi.");
            } else if (typeof form.tahunPengalamanMulai === 'undefined' || form.tahunPengalamanMulai == '') {
                alert("Mulai Tahun Pengalaman belum diisi.");
            } else if (typeof form.tahunPengalamanAkhir === 'undefined' || form.tahunPengalamanAkhir == '') {
                alert("Akhir Tahun Pengalaman belum diisi.");
            } else if (typeof form.nilaiKontrak === 'undefined' || form.nilaiKontrak == '') {
                alert("Nilai Kontrak belum diisi.");
            } else if (typeof form.mataUang.selected === 'undefined') {
                alert("Mata Uang belum dipilih.");
            } else if (typeof form.pengadaan.selected == 'undefined') {
                alert("Pengadaan belum dipilih");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                        id: form.id,
                        minimalPengalaman: form.minimalPengalaman,
                        tahunPengalamanMulai: form.tahunPengalamanMulai,
                        tahunPengalamanAkhir: form.tahunPengalamanAkhir,
                        nilaiKontrak: form.nilaiKontrak,
                        mataUang: form.mataUang.selected.id,
                        pengadaan: form.pengadaan.selected.id
                    }
                    //console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/updateVendorRequirement',
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
                        toaster.pop('success', 'Vendor Requirement', 'Simpan ' + data.pengadaan.namaPengadaan + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.minimalPengalaman = '';
                    form.tahunPengalamanMulai = '';
                    form.tahunPengalamanAkhir = '';
                    form.nilaiKontrak = '';
                    form.mataUang.selected = undefined;
                    form.pengadaan.selected = undefined;
                });
            };
        };
    }
    VendorRequirementUbahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
