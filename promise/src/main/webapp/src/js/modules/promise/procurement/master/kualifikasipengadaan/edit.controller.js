(function () {
    'use strict';

    angular.module('naut').controller('KualifikasiPengadaanUbahController', KualifikasiPengadaanUbahController);

    function KualifikasiPengadaanUbahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        var dataKualifikasiPengadaan;
        
        if(typeof $rootScope.dataIndexKualifikasiPengadaan !== 'undefined'){
            dataKualifikasiPengadaan = $rootScope.dataIndexKualifikasiPengadaan;
        }
        form.id = dataKualifikasiPengadaan.id;
        form.nama = dataKualifikasiPengadaan.nama;

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
                    url: $rootScope.backendAddress + '/procurement/master/KualifikasiPengadaanServices/updateKualifikasiPengadaan',
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
    KualifikasiPengadaanUbahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
