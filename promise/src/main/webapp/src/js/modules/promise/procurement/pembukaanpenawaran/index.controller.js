/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembukaanPenawaranController', PembukaanPenawaranController);

    function PembukaanPenawaranController($scope, $http, $rootScope, $resource, $location, $stateParams) {
        var pp = this;

        pp.go = function (path) {
            $location.path(path);
        };
        $scope.getPenawaranPengadaan = function () {
            $scope.loading = true;
            var tahapan = 10;
            if ($stateParams.tahapanId != undefined) {
                tahapan = $stateParams.tahapanId;
            }

            var uri = $rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/getPembukaanPenawaranMaxByTahapanUserDate/' + tahapan + '/' + $rootScope.userLogin.user.id;

            /*list yang digunakan tinggal di switch*/
            pp.pembukaanPenawaranList = [];
            $http.get(uri)
                .success(function (data, status, headers, config) {
                    angular.forEach(data, function (value, index) {
                            var object = {
                                id: value[0],
                                pengadaan: value[1]
                            }
                            pp.pembukaanPenawaranList.push(object);
                        })
                        //data pembukaan pengadaan satuan
                    $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/getPembukaanPenawaranMaxByTahapanUserDate/' + 19000003 + '/' + $rootScope.userLogin.user.id)
                        .success(function (dataSatuan) {
                            angular.forEach(dataSatuan, function (value, index) {
                                var object = {
                                    id: value[0],
                                    pengadaan: value[1]
                                }
                                pp.pembukaanPenawaranList.push(object);
                            });
                        })
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };
        $scope.getPenawaranPengadaan();

        pp.btnEdit = function (ppe) {
            $rootScope.ppEdit = ppe;
            if (ppe.pengadaan.jenisPenawaran.id == 1) { // 1 = total, 2 = satuan
                $location.path('/app/promise/procurement/pembukaaanPenawaranTotal');
            } else if (ppe.pengadaan.jenisPenawaran.id == 2) {
                $location.path('/app/promise/procurement/pembukaanPenawaranSatuan');
            } else {
                alert('tidak tersedia');
            }
        };
    }

    PembukaanPenawaranController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$stateParams'];

})();