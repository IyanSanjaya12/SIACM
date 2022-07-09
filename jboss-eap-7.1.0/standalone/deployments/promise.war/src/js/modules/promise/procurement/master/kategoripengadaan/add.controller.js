(function () {
    'use strict';

    angular.module('naut').controller('KategoriPengadaanTambahController', KategoriPengadaanTambahController);

    function KategoriPengadaanTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        form.kp = {};

        $http.get($rootScope.backendAddress + '/procurement/master/kondisiPengadaanServices/getList').success(function (data, status, headers, config) {
            $scope.getKondisiPengadaanList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/kategoripengadaan');
        }

        form.simpan = function () {
            if (typeof form.nama === 'undefined' || form.nama == '') {
                alert("Nama Kategori Pengadaan belum disi.");
            } else if (typeof form.kp.selected === 'undefined') {
                alert("Pilih Kondisi Pengadaan.");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                    nama: form.nama,
                    kondisiPengadaan: form.kp.selected.id
                }
                console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/kategoriPengadaanServices/create',
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
                        toaster.pop('success', 'Kategori Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.nama = '';
                    form.kp.selected = undefined;
                });
            };
        };
    }
    KategoriPengadaanTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
