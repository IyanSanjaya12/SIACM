(function () {
    'use strict';

    angular.module('naut').controller('KualifikasiPengadaanTambahController', KualifikasiPengadaanTambahController);

    function KualifikasiPengadaanTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/kualifikasipengadaan');
        }

        form.simpan = function () {
            if (typeof form.nama === 'undefined' || form.nama == '') {
                alert("Nama Kualifikasi Pengadaan belum disi.");
            } else {
                //console.log(JSON.stringify(form));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/KualifikasiPengadaanServices/insertKualifikasiPengadaan',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: form
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Kualifikasi Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.nama = '';
                });
            };
        };
    }
    KualifikasiPengadaanTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
