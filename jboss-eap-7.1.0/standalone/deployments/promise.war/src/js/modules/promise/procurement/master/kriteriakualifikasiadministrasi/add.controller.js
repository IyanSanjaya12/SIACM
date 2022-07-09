(function () {
    'use strict';

    angular.module('naut').controller('MasterKriteriaKualifikasiAdministrasiTambahController', MasterKriteriaKualifikasiAdministrasiTambahController);

    function MasterKriteriaKualifikasiAdministrasiTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var masterKriteriaKualifikasiAdministrasi = this;

        masterKriteriaKualifikasiAdministrasi.save = function () {
            if (typeof masterKriteriaKualifikasiAdministrasi.nama === 'undefined' || masterKriteriaKualifikasiAdministrasi.nama == '') {
                alert("Nama Kriteria belum disi.");
            } else if (typeof masterKriteriaKualifikasiAdministrasi.keterangan === 'undefined' || masterKriteriaKualifikasiAdministrasi.keterangan == '') {
                alert("Keterangan belum disi.");
            } else {
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/create',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: masterKriteriaKualifikasiAdministrasi
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Kriteria', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    masterKriteriaKualifikasiAdministrasi.nama = '';
                    masterKriteriaKualifikasiAdministrasi.keterangan = '';
                });
            }
        };

        masterKriteriaKualifikasiAdministrasi.back = function () {
            $location.path('/app/promise/procurement/master/kriteriaKualifikasiAdministrasi');
        };
    }

    MasterKriteriaKualifikasiAdministrasiTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
