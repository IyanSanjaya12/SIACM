(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HasilKualifikasiController', HasilKualifikasiController);

    function HasilKualifikasiController($scope, $http, $rootScope, $resource, $location) {
        $scope.getIndexPengadaan = function () {
            $scope.loading = true;
            //console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListByUser')
                .success(function (data, status, headers, config) {
                    $scope.pengadaanList = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    console.log('Error loading jenis pengadaan');
                    $scope.loading = false;
                });
        };
        $scope.getIndexPengadaan();

        $rootScope.pengadaan = {};

        $scope.getViewDetil = function (pengadaan) {
            $rootScope.pengadaan = pengadaan;
           // alert('hello'+"/app/promise/procurement/prakualifikasi/hasilkualifikasi/detil");
            if ($rootScope.userRoleName == 'VENDOR'){
                 $location.path("/appvendor/promise/procurement/prakualifikasi/hasilkualifikasi/detil");
            } else {
                $location.path("/app/promise/procurement/prakualifikasi/hasilkualifikasi/detil");
            }
        };
    }

    HasilKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();