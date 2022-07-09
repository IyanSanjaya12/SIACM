/**=========================================================
 * Module: PengambilanDokuemViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengambilanDokumenCetakController', PengambilanDokumenCetakController);

    function PengambilanDokumenCetakController(ModalService, $scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, $q) {
        var form = this;
        $rootScope.disableSave = false;

        $scope.detilPengadaan = $rootScope.pengadaan;
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
                $scope.listVendors = data;
                angular.forEach($scope.listVendors, function (value, index) {
                    $scope.listVendors[index].tglOpened = false;
                    $scope.getTglAmbilDokumen(value.vendor.id, value.pengadaan.id)
                        .then(function (resolve) {
                            $scope.listVendors[index].tglAmbil = resolve.tglAmbil;
                            $scope.listVendors[index].pengambilanDokumenId = resolve.pengambilanDokumenId;
                        }, function (reject) {
                            $scope.listVendors[index].tglAmbil = null;
                            $scope.listVendors[index].pengambilanDokumenId = null;
                        });
                });

            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();

        $scope.getTglAmbilDokumen = function (vendor, pengadaan) {
            return $q(function (resolve, reject) {
                setTimeout(function () {
                    var uri = $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/getByPengadaanVendor/' + pengadaan + '/' + vendor;
                    var pengambilanDokumen = {
                        tglAmbil: null,
                        pengambilanDokumenId: null
                    };
                    $http.get(uri)
                        .success(function (data, status, headers, config) {
                            if (data.length > 0) {
                                pengambilanDokumen.tglAmbil = data[0].tglAmbil;
                                pengambilanDokumen.pengambilanDokumenId = data[0].id;
                                resolve(pengambilanDokumen);
                            } else {
                                reject(pengambilanDokumen);
                            }
                        }).error(function (data, status, headers, config) {
                            reject(pengambilanDokumen);
                        });
                }, 1000);
            });
        }

        // Datepicker
        var toggleMax = function (tgl) {
            $scope.maxDate = new Date(tgl);
        }

        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanAndTahapan/' + $scope.detilPengadaan.id + '/' + 6).success(function (data, status, headers, config) {
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
                $scope.back();
            }).error(function (data, status, headers, config) {
                //alert('error');
            });
        };
        $scope.btnSimpanDisable = false;
        $scope.save = function () {

            ModalService.showModalConfirmation().then(function (result) {
                $scope.btnSimpanDisable = true;
                for (var i = 0; i < $scope.listVendors.length; i++) {
                    //console.log("post : " + JSON.stringify($scope.listVendors[i]));
                    var formPost = {
                        pengadaan : $scope.listVendors[i].pengadaan.id,
                        vendor : $scope.listVendors[i].vendor.id,
                        id : $scope.listVendors[i].pengambilanDokumenId
                    };
                    if($scope.listVendors[i].tglAmbil!= null && typeof $scope.listVendors[i].tglAmbil !== 'undefined')
                        formPost.tglAmbil = $filter('date')($scope.listVendors[i].tglAmbil, 'dd-MM-yyyy');
                    
                    var uri = '';
                    if (typeof formPost.id !== 'undefined' && formPost.id != null) {
                        uri = $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/update';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/insert';
                    }
                    console.log("uri : " + uri);
                    console.log("formPost : "+JSON.stringify(formPost));
                    
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
                        data: formPost
                    }).success(function (data, status, headers, config) {
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });

                    if (i == ($scope.listVendors.length - 1)) {
                        $scope.updatePengadaan();
                    }
                    
                    /*
                    var formPost = {};
                    formPost.tglAmbil = $filter('date')($scope.listVendors[i].tglAmbil, 'dd-MM-yyyy');
                    $scope.loading = true;
                    var uri = '';
                    if (typeof $scope.list[i].id !== 'undefined') {
                        uri = $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/update';
                    } else {
                        uri = $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/insert';
                    }
                    console.log("uri : " + uri);
                    console.log("vendor : " + JSON.stringify($scope.list[i]));

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
                    }).error(function (data, status, headers, config) {
                        //alert('error');
                    });

                    if (i == ($scope.listVendors.length - 1)) {
                        $scope.updatePengadaan();
                    }
                    */
                }
            });
        }

        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path("/app/promise/procurement/pengambilandokumen");
                }
            } else {
                $location.path("/app/promise/procurement/pengambilandokumen");
            }
        }
        
        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").hide();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            var printContents = document.getElementById(divName).innerHTML;
            $(".ng-table-pager").show();
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

    PengambilanDokumenCetakController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', '$q'];

})();