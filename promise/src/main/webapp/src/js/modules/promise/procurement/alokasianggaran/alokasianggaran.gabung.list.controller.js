/**=========================================================
 * Module: AlokasiAnggaranController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlokasiAnggaranGabungListController', AlokasiAnggaranGabungListController);

    function AlokasiAnggaranGabungListController($scope, $http, $rootScope, $resource, $location,ngTableParams) {
        var form = this;

        $scope.view = function (data) {
            $rootScope.detilAlokasiAnggaran = data.anggaranPengadaan;
            $location.path('/app/promise/procurement/alokasianggaran/gabung/view');
        };

        $scope.back = function () {
            $location.path('/app/promise/procurement/alokasianggaran/gabung');
        }
        $scope.tableGabungAnggaranDetail = [];

        if ($rootScope.tableGabungAnggaran.length > 0 && typeof $rootScope.tableGabungAnggaran !== 'undefined') {
            var listGabungAnggaran = $rootScope.tableGabungAnggaran;
            var count = 0;
            angular.forEach(listGabungAnggaran, function (value, indeksA) {
                $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AnggaranPengadaanServices/getListByAnggaran/' + value.id)
                    .success(function (data, status, headers, config) {
                        count = count + 1;
                        var object = {};
                        if (data.length > 0) {
                            angular.forEach(data, function (value, indeks) {
                                var object = {};
                                object.nomorDraft = value.alokasiAnggaran.nomorDraft;
                                object.nomorNotaDinas = value.pengadaan.nomorNotaDinas;
                                object.sisaAnggaran = value.sisaAnggaran;
                                object.anggaranPengadaan = value;
                                $scope.tableGabungAnggaranDetail.push(object);
                            })
                        } else {
                            object.nomorDraft = value.nomorDraft;
                            object.sisaAnggaran = value.sisaAnggaran;
                            object.nomorNotaDinas = null;
                            object.anggaranPengadaan = null;
                            $scope.tableGabungAnggaranDetail.push(object);
                        }
                        if (count == listGabungAnggaran.length) {
                            $scope.tableList = new ngTableParams({
                                page: 1, // show first page
                                count: 5 // count per page
                            }, {
                                total: $scope.tableGabungAnggaranDetail.length, // length of data4
                                getData: function ($defer, params) {
                                    $defer.resolve($scope.tableGabungAnggaranDetail.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                                }
                            });
                        }

                    });
            });
        }


        $scope.save = function () {
            if ($rootScope.tableGabungAnggaran.length > 0) {
                var jumlah = 0;
                for (var i = 0; i < $rootScope.tableGabungAnggaran.length; i++) {
                    jumlah = jumlah + $rootScope.tableGabungAnggaran[i].sisaAnggaran;
                }
                $rootScope.detilAlokasiAnggaran = {};
                $rootScope.detilAlokasiAnggaran.jumlah = jumlah;
                $rootScope.gabungAnggaran = true;
                $location.path('/app/promise/procurement/alokasianggaran/view');
            }

        }
    }

    AlokasiAnggaranGabungListController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location' , 'ngTableParams'];

})();
