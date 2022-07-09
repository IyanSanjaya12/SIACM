/**=========================================================
 * Module: KriteriaEvaluasiController.js
 * Author: F.H.K
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaEvaluasiController', KriteriaEvaluasiController);

    function KriteriaEvaluasiController($http, $rootScope, $resource, $location, $window) {
        var pp = this;

        // variable for all checklist kriteria
        // Total Type
        $rootScope.kriteriaEvaluasi = [];
        // Satuan Type
        $rootScope.kriteriaEvaluasiSatuan = [];
        // Additional for data from table
        $rootScope.isiListAmbangFromTable = []
        $rootScope.flagView = 0;

        pp.go = function (path) {
            $location.path(path);
        };
        pp.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/7', {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            pp.kriteriaEvaluasiList = data;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/19000001')
                //tahapan kriteria evaluasi satuan
                .success(function (data, status, headers, config) {
                    angular.forEach(data, function(value, index){
                        pp.kriteriaEvaluasiList.push(value);
                    })
                    pp.loading = false;
                })
                .error(function (err) {
                    pp.loading = false;
                });
        }).error(function (data, status, headers, config) {
            pp.loading = false;
        });

        pp.edit = function (ppvt) {
            $rootScope.detilPengadaan = ppvt;
            
            pp.go('/app/promise/procurement/kriteriaEvaluasi/view');
            
            /*
            if (ppvt.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                $location.path('/app/promise/procurement/kriteriaEvaluasi/view');
            } else if (ppvt.jenisPenawaran.id == 2) {
                $location.path('/app/promise/procurement/kriteriaEvaluasi/viewSatuan');
            } else {
                alert('tidak tersedia');
            }*/

        };

        pp.view = function (ppvt) {
            $rootScope.detilPengadaan = ppvt;
            $rootScope.flagView = 1;
            pp.go('/app/promise/procurement/kriteriaEvaluasi/view');
        }


    }

    KriteriaEvaluasiController.$inject = ['$http', '$rootScope', '$resource', '$location', '$window'];

})();