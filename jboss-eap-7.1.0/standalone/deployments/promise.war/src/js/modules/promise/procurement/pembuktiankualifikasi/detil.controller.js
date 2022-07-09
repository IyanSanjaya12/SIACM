/**=========================================================
 * Module: PembuktianKualifikasiDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembuktianKualifikasiDetilController', PembuktianKualifikasiDetilController);

    function PembuktianKualifikasiDetilController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal) {
        var form = this;

        $scope.detilPengadaan = $rootScope.detilPengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;

        /* START List Vendor*/
        $scope.getListVendor = function () {
            $scope.loading = true;
            $scope.list = [];
            var uri = $rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $scope.detilPengadaan.id;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                angular.forEach(data, function (value, indeks) {
                    //console.log(value);
                    data[indeks]['finalCheck'] = false;
                    $scope.getPembuktianKualifikasi(data[indeks], data[indeks].vendor.id, $scope.detilPengadaan.id);
                })
                $scope.listVendors = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();

        $scope.getPembuktianKualifikasi = function (itemList, vendor, pengadaan) {
            $scope.loading = true;
            $scope.list = [];
            //console.log(vendor);
            var uri = $rootScope.backendAddress + '/procurement/pembuktiankualifikasi/pembuktianKualifikasiServices/getListByPengadaanAndVendor/' + pengadaan + '/' + vendor;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
               // console.log('setelah sukses : ' + data[0].vendor.id);
                if (data.length > 0) {
                    itemList['finalCheck'] = data[0].finalCheck;
                    itemList['idKualifikasi']=data[0].id;
                } else {
                    form.finalCheck = false;
                }
                $scope.list.push(form);
            }).error(function (data, status, headers, config) {

            });
        }

        $scope.checked = function (data) {
            var obj = $scope.listVendors.indexOf(data);
            if ($scope.listVendors[obj].finalCheck == true) {
                $scope.list[obj].finalCheck = true;

            } else {
                $scope.list[obj].finalCheck = false;
               // console.log($scope.list[obj]);
            }
        }


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
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        };

        $scope.proses = function (indeks) {
            $scope.loading = true;
            form = new Object();
            form.finalCheck = $scope.listVendors[indeks].finalCheck;
            form.id = $scope.listVendors[indeks].idKualifikasi;
            form.pengadaan = $scope.detilPengadaan.id;
            form.vendor = $scope.listVendors[indeks].vendor.id;
            var uri = '';
            if (typeof $scope.listVendors[indeks].idKualifikasi !== 'undefined') {
                uri = $rootScope.backendAddress + '/procurement/pembuktiankualifikasi/pembuktianKualifikasiServices/update';
            } else {
                uri = $rootScope.backendAddress + '/procurement/pembuktiankualifikasi/pembuktianKualifikasiServices/create';
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
                if (($scope.list.length - 1) != indeks) {
                    $scope.proses(indeks + 1);
                }
                $scope.updatePengadaan();
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        }
        $scope.save = function () {
            $scope.proses(0);
        }

        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path("/app/promise/procurement/pengambilandokumen");
                }
            } else {
                $location.path("/app/promise/procurement/pembuktiankualifikasi");
            }
        }

        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        /*modal Confirmation*/
        $scope.goModalConfirmation = function (data) {
            $rootScope.saveAfterConfirmation = $scope.save;
            var modalInstance = $modal.open({
                templateUrl: '/ModalConfirmation.html',
                controller: ModalInstanceConfirmationCtrl,
            });
        };

        /*---Modal Confirmation---*/
        var ModalInstanceConfirmationCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {

            $scope.ok = function () {
                $rootScope.saveAfterConfirmation();
                $modalInstance.dismiss('close');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceConfirmationCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/

    }

    PembuktianKualifikasiDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal'];

})();
