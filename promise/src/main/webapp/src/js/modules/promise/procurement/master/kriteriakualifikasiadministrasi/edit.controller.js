(function () {
    'use strict';

    angular.module('naut').controller('MasterKriteriaKualifikasiAdministrasiEditController', MasterKriteriaKualifikasiAdministrasiEditController);

    function MasterKriteriaKualifikasiAdministrasiEditController($scope, $http, $rootScope, $resource, $location, toaster) {
        var masterKriteriaKualifikasiAdministrasi = this;
        masterKriteriaKualifikasiAdministrasi.id = $rootScope.masterKriteriaKualifikasiAdministrasiEdit.id;
        masterKriteriaKualifikasiAdministrasi.nama = $rootScope.masterKriteriaKualifikasiAdministrasiEdit.nama;
        masterKriteriaKualifikasiAdministrasi.keterangan = $rootScope.masterKriteriaKualifikasiAdministrasiEdit.keterangan;

        masterKriteriaKualifikasiAdministrasi.save = function () {
            $scope.loading = true;
            if (typeof masterKriteriaKualifikasiAdministrasi.nama === 'undefined' || masterKriteriaKualifikasiAdministrasi.nama == '') {
                alert("Nama Kriteria belum disi.");
            } else if (typeof masterKriteriaKualifikasiAdministrasi.keterangan === 'undefined' || masterKriteriaKualifikasiAdministrasi.keterangan == '') {
                alert("Keterangan belum disi.");
            } else {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/update',
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
                        toaster.pop('success', 'Kriteria', 'Ubah ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    masterKriteriaKualifikasiAdministrasi.nama = '';
                    masterKriteriaKualifikasiAdministrasi.keterangan='';
                });
            }
        };
        masterKriteriaKualifikasiAdministrasi.back = function () {
            $location.path('/app/promise/procurement/master/kriteriaKualifikasiAdministrasi');
        };
    }

    MasterKriteriaKualifikasiAdministrasiEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
