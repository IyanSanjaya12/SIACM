/**=========================================================
 * Module: PengambilanDokumenKualifikasiViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengambilanDokumenKualifikasiViewController', PengambilanDokumenKualifikasiViewController);

    function PengambilanDokumenKualifikasiViewController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal) {
        var form = this;
        $rootScope.disableSave = false;

        $scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId=$scope.detilPengadaan.id;
       
        /* START List Vendor*/
        $scope.getListVendor = function () {
            $scope.loading = true;
            $scope.list = [];
            var uri = $rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pendaftaranVendorPraKualifikasiServices/getPendaftaranVendorPraKualifikasiByPengadaan/' + $scope.detilPengadaan.id;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                for (var i = 0; i < data.length; i++) {
                    data[i]['tglOpened'] = false;
                    $scope.getTglAmbilDokumen(data[i], data[i].vendor.id, $scope.detilPengadaan.id);
                }
                $scope.listVendors = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();

        $scope.getTglAmbilDokumen = function (itemList, vendor, pengadaan) {
            $scope.loading = true;
            $scope.list = [];
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/pengambilanDokumenKualifikasiServices/getByPengadaanVendor/' + pengadaan + '/' + vendor;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                form = new Object();
                form.pengadaan = pengadaan;
                form.vendor = vendor;
                if (data.length > 0) {
                    itemList['tglAmbil'] = data[0].tglAmbil;
                    form.tglAmbil = data[0].tglAmbil;
                    form.id = data[0].id;
                }
                $scope.list.push(form);
            }).error(function (data, status, headers, config) {

            });
        }

        // Datepicker
        var toggleMax = function (tgl) {
            $scope.maxDate = new Date(tgl);
        }

        $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/jadwalKualifikasiServices/getByPengadaanAndTahapan/' + $scope.detilPengadaan.id + '/' + 1006).success(function (data, status, headers, config) {
            toggleMax(data[0].tglAkhir);
        }).error(function (data, status, headers, config) {})

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
        $scope.toggleMin();

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[4];

        $scope.tglOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.listVendors[index].tglOpened = true;
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
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        };

        $scope.save = function () {
            for (var i = 0; i < $scope.listVendors.length; i++) {
                $scope.list[i].tglAmbil = $filter('date')($scope.listVendors[i].tglAmbil, 'dd-MM-yyyy');
                $scope.loading = true;
                var uri = '';
                if (typeof $scope.list[i].id !== 'undefined') {
                    uri = $rootScope.backendAddress + '/procurement/prakualifikasi/pengambilanDokumenKualifikasiServices/update';
                } else {
                    uri = $rootScope.backendAddress + '/procurement/prakualifikasi/pengambilanDokumenKualifikasiServices/insert';
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
                    data: $scope.list[i]
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.updatePengadaan();
                }).error(function (data, status, headers, config) {
                    alert('error');
                });
            }
        }

        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path("/app/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi");
                }
            } else {
                $location.path("/app/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi");
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
                $rootScope.disableSave = true;
                $modalInstance.dismiss('close');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceConfirmationCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/

    }

    PengambilanDokumenKualifikasiViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal'];

})();
