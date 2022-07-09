/**=========================================================
 * Module: negosiasi.total.detail.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiTotalDetailController', NegosiasiTotalDetailController);

    function NegosiasiTotalDetailController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams) {

        $scope.pengadaanId = $stateParams.pengadaanId

        var negosiasiDetail = this;
        negosiasiDetail.negosiasiTahapId = $rootScope.detilPengadaan.tahapanPengadaan.id;
        $scope.getNextTahapan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    negosiasiDetail.nextTahapId = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    console.log('ERROR : get next tahapan');
                    $scope.loading = false;
                });
        };
        $scope.getNextTahapan();
        $scope.btnDisable = false;
        $scope.getUpdateTahapan = function () {
            ModalService.showModalConfirmation().then(function (result) {
                $scope.loading = true;
                $scope.btnDisable = true;
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
                        pengadaanId: $scope.pengadaanId,
                        tahapanPengadaanId: negosiasiDetail.nextTahapId
                    }
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    //$scope.btnDisable = true;
                    $scope.saveBtnStatus = true;
                    $scope.btnSendPrice = true;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.btnDisable = false;
                    $scope.message = "Error simpan tahapan pengadaan";
                });
            });
        }

        // set mode
        $scope.isEditable = $rootScope.isEditable;
        $scope.loading = true;
        $scope.loadingSaving = false;

        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/

        /*Strategy:*/

        /* Waktu pertama dibuka.. seharusnya T6_NEGOSIASI berisi penawaran terakhir vendor, kemudian dilist*/
        /* Daftar vendor yang ikut serta dalam nego diambil dari T6_NEGOSIASI*/

        function loadVendorNegosiasi(isShowSuccessMsg) {

            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (data, status, headers, config) {
            	console.log("a");
                /*CEK DATA EVALUASI*/
                if (data.length > 0) {
                    negosiasiDetail.vendorNegosiasiList = $filter('filter')(data, {
                        nilaiTotal: ('!' + null)
                    });
                    $scope.recordSaveCtr = 0;
                    $scope.recordToSave = negosiasiDetail.vendorNegosiasiList.length;

                    angular.forEach(negosiasiDetail.vendorNegosiasiList, function (vendorNegosiasi, vendorNegosiasiIndex) {
                        /*Ambil penawaran awal vendor*/
                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getFirstFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + vendorNegosiasi.vendor.id, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            if (data.length > 0) {
                                vendorNegosiasi.negosiasiFirstFromVendor = data[0];
                                /*Ambil dulu kurs mata uang Vendor*/
                                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + vendorNegosiasi.negosiasiFirstFromVendor.mataUang.id)
                                    .success(function (data, status, headers, config) {
                                        vendorNegosiasi.kursPengadaan = data;
                                        /*Ambil penawaran akhir vendor*/
                                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + vendorNegosiasi.vendor.id, {
                                            ignoreLoadingBar: true
                                        }).success(function (data, status, headers, config) {
                                            vendorNegosiasi.negosiasiLastFromVendor = data[0];
                                            /*Ambil penawaran akhir panitia*/
                                            $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromPanitiaByPengadaanAndVendor/' + $scope.pengadaanId + '/' + vendorNegosiasi.vendor.id, {
                                                ignoreLoadingBar: true
                                            }).success(function (data, status, headers, config) {

                                                /*---------------- main code ---------------------*/

                                                /*default value*/
                                                vendorNegosiasi.negosiasiLastFromPanitia = {
                                                    "nilaiPenawaranAfterCondition": 0,
                                                    "isDeal": 0
                                                };
                                                /*Untuk flag apakah panitia sudah melakukan penawaran*/

                                                negosiasiDetail.isPanitiaOffered = false;

                                                if (typeof data[0] != 'undefined') {
                                                    negosiasiDetail.isPanitiaOffered = true;
                                                    vendorNegosiasi.negosiasiLastFromPanitia = data[0];
                                                }

                                                $scope.loadDataSuccessHandler(isShowSuccessMsg);

                                                $scope.updateLastOfferStatus(vendorNegosiasi);
                                                /*---------------- main code ---------------------*/

                                                $scope.loading = false;

                                            }).error(function (data, status, headers, config) {
                                                console.log("** ERROR NegosiasiServices - getLastFromPanitiaByPengadaanAndVendor ***");
                                                $scope.loading = false;
                                            });

                                        }).error(function (data, status, headers, config) {
                                            console.log("** ERROR NegosiasiServices - getLastFromVendorByPengadaanAndVendor ***");
                                            $scope.loading = false;
                                        });

                                    }).error(function (data, status, headers, config) {
                                        console.log("** ERROR KursPengadaanServices - getKursPengadaanByPengadaanAndMataUang ***");
                                        $scope.loading = false;
                                    });
                            } else {
                                $scope.loading = false;
                                negosiasiDetail.vendorNegosiasiList.splice(vendorNegosiasiIndex,1);
                                $scope.recordToSave = $scope.recordToSave-1;
                            }
                        }).error(function (data, status, headers, config) {
                            console.log("** ERROR NegosiasiServices - getFirstFromVendorByPengadaanAndVendor ***");
                            $scope.loading = false;
                        });

                    });

                } /*CEK DATA EVALUASI*/
                else {
                    alert('Data evaluasi belum ada!');
                    $scope.gotoIndex();
                }

            }).error(function (data, status, headers, config) {
                console.log("** ERROR EvaluasiHargaVendorServices - getListByPengadaan ***");
                $scope.loading = false;
            });
        }


        $scope.loadDataSuccessHandler = function (isShowSuccessMsg) {
            $scope.recordSaveCtr++;
            if ($scope.recordSaveCtr == $scope.recordToSave) {
                $scope.loading = false;
                if (isShowSuccessMsg) {
                    toaster.pop('success', 'Sukses', 'Data penawaran berhasil diperbaharui.');
                }
            }
        }

        /*tombol aksi back to index*/
        $scope.gotoIndex = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/negosiasi');
                }
            } else {
                $location.path('/app/promise/procurement/negosiasi');
            }
        };

        $scope.printDiv = function (divName) {
            $( ".loading" ).remove();
            $("button").hide();
            $("input:text").hide();
            
            
            var b = angular.element($('#text1')).val();
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            $("button").show();
            $("input:text").show();
            $scope.model=b;
            
            for(var c=0; c.lenght; c++){
                
            }
            
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        /*tombol aksi send price to vendor*/
        $scope.sendPriceToVendor = function () {
            ModalService.showModalConfirmation().then(function (result) {

                $scope.recordToSave = negosiasiDetail.vendorNegosiasiList.length;
                $scope.recordSaveCtr = 0;
                $scope.loadingSaving = true;

                angular.forEach(negosiasiDetail.vendorNegosiasiList, function (vendorNegosiasi) {
                    /*INSERT INTO promise_t6_negosiasi*/
                    /*INSERT ONLY when deal_price != 1*/

                    if (vendorNegosiasi.isDeal != 1) {
                        //mata uang sendprice to vendor pakai IDR
                        var postData = {
                            "suratPenawaran": vendorNegosiasi.suratPenawaran.id,
                            "vendor": vendorNegosiasi.vendor.id,
                            "pengadaan": vendorNegosiasi.suratPenawaran.pengadaan.id,
                            "nilaiPenawaranAfterCondition": vendorNegosiasi.negosiasiLastFromPanitia.nilaiPenawaranAfterCondition,
                            "isVendor": 0,
                            "isPanitia": 1,
                            "isDeal": 0,
                            "isDelete": 0,
                            "mataUangId": 1
                        }

                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/create',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: postData
                        }).success(function (data, status, headers, config) {
                            $scope.sendPriceSuccessHandler();
                            $scope.updateLastOfferStatus(vendorNegosiasi);
                        })

                    } else {
                        console.log("price not send because it's already deal!")
                        $scope.recordToSave -= 1;
                        $scope.loadingSaving = false;
                    }
                });
            });
            /* $location.path('/app/promise/procurement/negosiasi');*/
        }

        $scope.sendPriceSuccessHandler = function () {
            $scope.recordSaveCtr++;
            if ($scope.recordSaveCtr == $scope.recordToSave) {
                $scope.loadingSaving = false;
                negosiasiDetail.isPanitiaOffered = true;
                toaster.pop('success', 'Sukses', 'Data penawaran negosiasi berhasil dikirim.');
            }
        }

        $scope.dealPrice = function (vendorNegosiasi) {

            var postData = {
                "pengadaanId": vendorNegosiasi.pengadaan.id,
                "vendorId": vendorNegosiasi.vendor.id
            }

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiPengadaanServices/panitiaDealPriceForTotalNego',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postData
            }).success(function (data, status, headers, config) {
                if (data) {
                    /*refresh*/
                    loadVendorNegosiasi(false);
                }
            })

        };

        $scope.refreshPage = function () {
            loadVendorNegosiasi(true);
        }

        $scope.showHistory = function (negosiasi) {
            $rootScope.myBackurl = '/app/promise/procurement/negosiasi/total/detail/' + negosiasi.pengadaan.id;
            $location.path('/app/promise/procurement/negosiasi/total/detail/history/' + negosiasi.pengadaan.id + '/' + negosiasi.vendor.id);

        };

        $scope.showDetail = function (negosiasi) {

            $location.path('/app/promise/procurement/negosiasi/total/detail/more/' + negosiasi.pengadaan.id + '/' + negosiasi.vendor.id + '/' + negosiasi.id);
        };

        $scope.updateLastOfferStatus = function (vendorNegosiasi) {
            if (typeof vendorNegosiasi.negosiasiLastFromVendor === 'undefined') {
                vendorNegosiasi.negosiasiLastFromVendor = {
                    isDeal: false
                };
            }

            vendorNegosiasi.isDeal = (vendorNegosiasi.negosiasiLastFromPanitia.isDeal || vendorNegosiasi.negosiasiLastFromVendor.isDeal);

        }



        /* MainCode*/
        $scope.loading = true;
        var postData = {
            "pengadaanId": $scope.pengadaanId
        }
        $http({
            method: 'POST',
            url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiPengadaanServices/initForTotalNego',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: postData
        }).success(function (data, status, headers, config) {

            if (data) {
                /*alert('sukses');*/
                loadVendorNegosiasi(false);
            } else {
                alert('error on init data!')
            }
        })

    }

    NegosiasiTotalDetailController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams'];

})();