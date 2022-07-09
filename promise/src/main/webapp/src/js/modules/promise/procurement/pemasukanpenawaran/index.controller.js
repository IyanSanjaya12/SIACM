/**=========================================================
 * Module: PemasukanPenawaranController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanPenawaranController', PemasukanPenawaranController);

    function PemasukanPenawaranController($scope, $http, $rootScope, $resource, $location, $stateParams) {
        var pp = this;

        var tahapan = 9 //masih hard code
        if ($stateParams.tahapanId != undefined) {
            tahapan = $stateParams.tahapanId;
        }

        pp.go = function (path) {
            $location.path(path);
        };

        $scope.getPemasukanPenawaran = function () {
            $scope.loading = true;

            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + tahapan).success(
                function (data, status, headers, config) {
                    pp.pemasukanPenawaranList = data;
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + 19000002)
                        //pemasukan penwawaran satuan
                        .success(function (data, status, headers, config) {
                            angular.forEach(data, function (value, index) {
                                    pp.pemasukanPenawaranList.push(value);
                                });
                        
                                //console.log('iNFO : '+JSON.stringify(data));
                            $scope.loading = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });
                }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };
        $scope.getPemasukanPenawaran();


        pp.btnDetil = function (ppvt) {
            $rootScope.detilPengadaan = ppvt;
            if (ppvt.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                $location.path('/app/promise/procurement/pemasukanPenawaran/viewTotal');
            } else if (ppvt.jenisPenawaran.id == 2) {
                //alert('penawaran satuan');
                $location.path('/app/promise/procurement/pemasukanPenawaran/viewSatuan');

            } else {
                alert('tidak tersedia');
            }
        };


    }

    PemasukanPenawaranController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$stateParams'];

})();