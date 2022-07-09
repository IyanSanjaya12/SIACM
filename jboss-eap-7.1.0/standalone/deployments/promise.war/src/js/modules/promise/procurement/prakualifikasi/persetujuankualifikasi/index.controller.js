(function () {
    'use strict';

    angular.module('naut').controller('PersetujuanKualifikasiPengadaanController',
        PersetujuanKualifikasiPengadaanController);

    function PersetujuanKualifikasiPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var persetujuanIndex = this;
        persetujuanIndex.loading = true;
        var tahapan = 10120000;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/' + tahapan)
            .success(function (data, status, headers, config) {
                persetujuanIndex.pengadaanList = data;
                persetujuanIndex.loading = false;
            }).error(function (data, status, headers, config) {
                persetujuanIndex.loading = false;
            });

        persetujuanIndex.view = function (pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = false;
            $location.path('/app/promise/procurement/prakualifikasi/persetujuankualifikasi/detil');
        };

        persetujuanIndex.edit = function (pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = true;
            $location.path('/app/promise/procurement/prakualifikasi/persetujuankualifikasi/detil');
        };

    }

    PersetujuanKualifikasiPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location'
    ];

})();