/**=========================================================
 * Module: SPKViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SPKViewController', SPKViewController);

    function SPKViewController($scope, $http, $rootScope, $resource, $location, $filter) {
        var form = this;

        if ($rootScope.statusSPK == false) {
            $scope.detilPengadaan = $rootScope.penetapanPemenang;
            $scope.vendor = $rootScope.penetapanPemenang.vendor;
            $rootScope.spk = undefined;
            $scope.status = false;
        } else {
            $scope.detilPengadaan = $rootScope.spk.pengadaan;
            $scope.vendor = $rootScope.spk.vendor;
            form.id = $rootScope.spk.id;
            form.tanggal = $filter('date')($rootScope.spk.tanggal, 'yyyy-MM-dd');
            form.tanggalMulai = $filter('date')($rootScope.spk.tanggalMulai, 'yyyy-MM-dd');
            form.tanggalSelesai = $filter('date')($rootScope.spk.tanggalSelesai, 'yyyy-MM-dd');
            form.nomor = $rootScope.spk.nomor;
            $scope.status = true;
            $rootScope.penetapanPemenang = undefined;
        }

        /*generate Object to post*/
        form.pengadaan = $scope.detilPengadaan.id;
        form.vendor = $scope.vendor.id;

        /* START List Vendor*/
        $scope.getListVendor = function () {
            $scope.loading = true;
            var uri = "";
			console.log(">> "+JSON.stringify($scope.detilPengadaan));
            if ($scope.detilPengadaan.jenisPenawaran.id == 1) {
                uri = $rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getByPengadaanVendor/' + $scope.detilPengadaan.id  + '/' + $scope.vendor.id;
            } else {
                uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/getListByVendorByPengadaan/' + $scope.vendor.id + '/' + $scope.detilPengadaan.id;
            }
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.listVendors = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();

        // Datepicker
        $scope.disabled = function (date, mode) {
            //            var today = date.getTime();
            //            var valid = false;
            //            for (var i = 0; i < $rootScope.hariLiburList.length; i++) {
            //                if (today == $rootScope.hariLiburList[i].tanggal.getTime()) {
            //                    return true;
            //                }
            //
            //            }
            return false;
        };
        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.maxDate = new Date(2020, 5, 22);
        $scope.toggleMin();
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[4];

        $scope.tglOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglOpened = true;
        };

        $scope.tglMulaiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglMulaiOpened = true;
        };

        $scope.tglSelesaiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglSelesaiOpened = true;
        };

        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.detilPengadaan.id, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.nextTahapan = data;
                }).error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        //update pengadaan
        $scope.updatePengadaan = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: {
                    pengadaanId: $scope.detilPengadaan.id,
                    tahapanPengadaanId: $scope.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $location.path("/app/promise/procurement/spk");
            });
        };

        $scope.save = function () {
            form.tanggal = $filter('date')(form.tanggal, 'yyyy-MM-dd');
            form.tanggalMulai = $filter('date')(form.tanggalMulai, 'yyyy-MM-dd');
            form.tanggalSelesai = $filter('date')(form.tanggalSelesai, 'yyyy-MM-dd');
            $scope.loading = true;
            var uri = "";
            if (!$scope.status) {
                uri = $rootScope.backendAddress + '/procurement/spk/SPKServices/insert';
            } else {
                uri = $rootScope.backendAddress + '/procurement/spk/SPKServices/update';
            }
            $http({
                method: 'POST',
                url: uri,
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
                $scope.loading = false;
                if (!$scope.status) {
                    $scope.updatePengadaan();
                } else {
                    $location.path("/app/promise/procurement/spk-index");
                }
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        }

        $scope.back = function () {
            if (!$scope.status) {
                $location.path("/app/promise/procurement/spk");
            } else {
                $location.path("/app/promise/procurement/spk-index");
            }
        }

    }

    SPKViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();
