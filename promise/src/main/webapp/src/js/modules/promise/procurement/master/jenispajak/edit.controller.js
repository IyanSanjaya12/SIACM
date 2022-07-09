(function () {
    'use strict';

    angular.module('naut').controller('JenisPajakUbahController', JenisPajakUbahController);

    function JenisPajakUbahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        var dataJenisPajak;

        if (typeof $rootScope.dataIndexJenisPajak !== 'undefined') {
            dataJenisPajak = $rootScope.dataIndexJenisPajak;
        }

        form.id = dataJenisPajak.id;
        form.nama = dataJenisPajak.nama;
        form.deskripsi = dataJenisPajak.deskripsi;
        form.prosentase = dataJenisPajak.prosentase;

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/jenispajak');
        }

        form.simpan = function () {
            if (typeof form.nama === 'undefined' || form.nama == '') {
                alert("Nama Jenis Pajak belum disi.");
            } else if (typeof form.deskripsi === 'undefined' || form.deskripsi == '' || form.deskripsi == null) {
                alert("Deksripsi Jenis Pajak belum disi.");
            } else if (typeof form.prosentase === 'undefined' || form.prosentase == '' || form.prosentase == null) {
                alert("Persentasi Jenis Pajak belum diisi");
            } else {
                //console.log(JSON.stringify(form));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/JenisPajakServices/updateJenisPajak',
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
                        toaster.pop('success', 'Jenis Pajak', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.nama = '';
                    form.deskripsi = '';
                    form.prosentase = '';
                });
            };
        };
    }
    JenisPajakUbahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
