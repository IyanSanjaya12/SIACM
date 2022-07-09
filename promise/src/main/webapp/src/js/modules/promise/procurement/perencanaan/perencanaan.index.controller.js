/**=========================================================
 * Module: PerencanaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PerencanaanController', PerencanaanController);

    function PerencanaanController($scope, $http, $rootScope, $resource, $location) {
        var pr = this;

        $rootScope.ppDokPekerjaan = {};

        //$rootScope.perencanaanDataList = [];

        pr.go = function (path) {
            $location.path(path);
        };

        $scope.getList = function () {
            $scope.loading = true;
            console.log(JSON.stringify($rootScope.userRole));
            $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/findByBiroPengadaan')
                .success(
                function (data, status, headers, config) {
                    pr.perencanaanList = data;
                    //console.log('iNFO : '+JSON.stringify(data ));
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };
        $scope.getList();


        pr.btnDetil = function (alokasiPemilikAnggaran) {
            //$rootScope.anggaranPerencanaan = alokasiAnggaran;
            $http.get($rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/getSingleByAlokasiAnggaranId/' + alokasiPemilikAnggaran.id).success(function (data, status, headers, config) {
                pr.alokasiAnggaranData = data;
                if (typeof pr.alokasiAnggaranData.perencanaan === 'undefined') {
                    //console.log('>> cek : '+JSON.stringify(alokasiPemilikAnggaran));
                    $rootScope.anggaranPerencanaan = {};
                    $rootScope.anggaranPerencanaan.alokasiAnggaran = alokasiPemilikAnggaran;
                    $location.path('/app/promise/procurement/perencanaan/detail/add');
                } else if (pr.alokasiAnggaranData.perencanaan.id > 0) {
                    $rootScope.anggaranPerencanaan = alokasiPemilikAnggaran;                    
                    $rootScope.anggaranPerencanaan.alokasiAnggaran = alokasiPemilikAnggaran;
                    $location.path('/app/promise/procurement/perencanaan/detail');
                }
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });

            //console.log('iNFO : '+JSON.stringify($rootScope.anggaranPerencanaan ));
            //$location.path('/app/promise/procurement/perencanaan/detail');
        };

    }

    PerencanaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();