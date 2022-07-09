/**=========================================================
 * Module: negosiasi.vendor.total.detail.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/


(function () {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiVendorTotalDetailController', NegosiasiTotalDetailController);

    function NegosiasiTotalDetailController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams, editableOptions, editableThemes, $anchorScroll, RequestService) {

        $scope.pengadaanId = $stateParams.pengadaanId

        var negosiasiDetail = this;
        negosiasiDetail.negosiasiTahapId = 15;
        negosiasiDetail.nextTahapId = 16;

        // set mode
        $scope.isEditable = $rootScope.isEditable;

        $scope.loadingSaving = false;
        $scope.loadingNegosiasiLastFromPanitia = false;
        $scope.loading = true;

        $scope.CPReady = false; /*Flag when CP is not in editing mode*/

        $scope.isLastOffer = false;

        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/

        // Load data peserta hasil kelulusan evaluasi
        // * Negosiasi Total, daftar vendor peserta diambil dari  promise_t5_evaluasi_harga_vendor
        // * Salin harga terakhir ke nego

        function loadVendorNegosiasi() {

            $scope.loading = true;

            /*Ambil data vendor by userId*/
            //console.log('INFO : ' + JSON.stringify($rootScope.userLogin));
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    negosiasiDetail.vendor = data;

                    /*Ambil informasi surat Penawaran, juga untuk mengambil mata uang vendor*/
                    $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + negosiasiDetail.vendor.id + '/' + $scope.pengadaanId)
                        .success(function (data, status, headers, config) {
                            if (data.length > 0) {
                                negosiasiDetail.suratPenawaran = data[0];

                                /*Ambil Penawaran Terakhir dari panitia */
                                $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromPanitiaByPengadaanAndVendor/' + $scope.pengadaanId + '/' + negosiasiDetail.vendor.id)
                                    .success(function (data, status, headers, config) {

                                        /*default value*/
                                        negosiasiDetail.negosiasiLastFromPanitia = {
                                            "nilaiPenawaranAfterCondition": 0
                                        };

                                        if (typeof data[0] != 'undefined') {
                                            negosiasiDetail.negosiasiLastFromPanitia = data[0];
                                        }


                                        /*Penawaran akhir vendor*/
                                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + negosiasiDetail.vendor.id, {
                                            ignoreLoadingBar: true
                                        }).success(function (data, status, headers, config) {
                                            //cek sudah ada init dari panitia?
                                            if (data.length == 0) {
                                                $scope.loading = false;
                                                RequestService.modalInformation('Panitia belum memberikan penawaran Negosiasi!', 'danger')
                                                    .then(function (data) {
                                                        $scope.gotoIndex();
                                                    });
                                            } else {
                                                negosiasiDetail.negosiasiLastFromVendor = data[0];
                                                /*Kurs mata uang pengadaan*/
                                                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + negosiasiDetail.negosiasiLastFromVendor.mataUang.id, {
                                                    ignoreLoadingBar: true
                                                }).success(function (data, status, headers, config) {
                                                    negosiasiDetail.kursPengadaan = data;

                                                    /*Detil nego */
                                                    $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/getNegosiasiDetailListByNegosiasi/' + negosiasiDetail.negosiasiLastFromVendor.id, {
                                                        ignoreLoadingBar: true
                                                    }).success(function (data, status, headers, config) {
                                                        negosiasiDetail.negosiasiDetailList = data;

                                                        /*Nego CP*/
                                                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiConditionalPriceServices/getListByNegosiasi/' + negosiasiDetail.negosiasiLastFromVendor.id, {
                                                            ignoreLoadingBar: true
                                                        }).success(function (data, status, headers, config) {
                                                            negosiasiDetail.negoConditionList = data;

                                                            /*Master CP*/
                                                            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getList', {
                                                                ignoreLoadingBar: true
                                                            }).success(function (data, status, headers, config) {
                                                                $scope.masterCP = data;


                                                                /*------------ main code -----------------------*/

                                                                /*OPTIMIZE - Create another list from available data*/

                                                                /*Create list of negosiasiDetailMaterialList & negosiasiDetailJasaList*/
                                                                negosiasiDetail.negosiasiDetailMaterialList = [];
                                                                negosiasiDetail.negosiasiDetailJasaList = [];
                                                                angular.forEach(negosiasiDetail.negosiasiDetailList, function (n) {
                                                                    n.itemPengadaan.item.itemType.id == 1 ? negosiasiDetail.negosiasiDetailMaterialList.push(n) : negosiasiDetail.negosiasiDetailJasaList.push(n);
                                                                })

                                                                /*Create list of headerConditionList & itemConditionList*/
                                                                negosiasiDetail.headerConditionList = [];
                                                                negosiasiDetail.itemConditionList = [];
                                                                angular.forEach(negosiasiDetail.negoConditionList, function (n) {
                                                                    n.isNew = false;
                                                                    n.conditionalPrice.tipe == 1 ? negosiasiDetail.headerConditionList.push(n) : negosiasiDetail.itemConditionList.push(n);
                                                                })

                                                                /*Create list of masterItemCP & masterHeaderCP*/
                                                                $scope.masterItemCP = [];
                                                                $scope.masterHeaderCP = [];
                                                                angular.forEach($scope.masterCP, function (n) {
                                                                    n.tipe == 1 ? $scope.masterHeaderCP.push(n) : $scope.masterItemCP.push(n);
                                                                })

                                                                /*Create list of itemPengadaan*/
                                                                $scope.itemPengadaanList = [];
                                                                angular.forEach(negosiasiDetail.negosiasiDetailJasaList, function (n) {
                                                                    $scope.itemPengadaanList.push(n.itemPengadaan);
                                                                })
                                                                angular.forEach(negosiasiDetail.negosiasiDetailMaterialList, function (n) {
                                                                    $scope.itemPengadaanList.push(n.itemPengadaan);
                                                                })


                                                                $scope.loading = false;
                                                                $scope.CPReady = true;
                                                                $scope.hitung();
                                                                $scope.updateLastOfferStatus()
                                                                    /*------------ end main  -----------------------*/



                                                            }).error(function (data, status, headers, config) {
                                                                $scope.loading = false;
                                                                console.log("***ERROR** - /procurement/master/conditionalPriceServices/getList");
                                                            });
                                                        }).error(function (data, status, headers, config) {
                                                            $scope.loading = false;
                                                            console.log("***ERROR** - /procurement/negosiasi/NegosiasiConditionalPriceServices/getListByNegosiasi/");
                                                        });
                                                    }).error(function (data, status, headers, config) {
                                                        $scope.loading = false;
                                                        console.log("***ERROR** - /procurement/negosiasi/NegosiasiDetailServices/getNegosiasiDetailListByNegosiasi/")
                                                    });

                                                }).error(function (data, status, headers, config) {
                                                    $scope.loading = false;
                                                    console.log("***ERROR** - /procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanByPengadaanAndMataUang/")
                                                });
                                            }

                                        }).error(function (data, status, headers, config) {
                                            $scope.loading = false;
                                            console.log("***ERROR** - /procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/")
                                        });

                                    }).error(function (data, status, headers, config) {
                                        $scope.loading = false;
                                        console.log("***ERROR** - /procurement/negosiasi/NegosiasiServices/getLastFromPanitiaByPengadaanAndVendor/")
                                    });
                            } else {
                                RequestService.modalInformation('Anda tidak bisa melakukan Negosiasi, karena tidak mengikuti Pemasukan Penawaran sebelumnya!', 'danger')
                                    .then(function (data) {
                                        $scope.gotoIndex();
                                    });
                            }
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                            console.log("***ERROR** - /procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/")
                        });
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                    console.log("***ERROR** - /procurement/vendor/vendorServices/getVendorByUserId/")
                });

        }

        $scope.hitung = function () {
            /*Menghitung item, header cp dan total value*/

            /*Keep It Simple*/

            /*Reset nilai total = 0 */
            negosiasiDetail.hargaTotalAfterCondition = 0; /*Nilai total + All CP*/

            /*reset all afterConditional value into original value*/
            angular.forEach(negosiasiDetail.negosiasiDetailMaterialList, function (negosiasi) {
                negosiasi.hargaSatuanAfterCondition = negosiasi.hargaSatuanOri;
                negosiasi.hargaTotalOri = negosiasi.itemPengadaan.kuantitas * negosiasi.hargaSatuanOri;
                negosiasi.hargaTotalAfterCondition = negosiasi.hargaTotalOri;
                negosiasi.itemCP = 0;
            })

            angular.forEach(negosiasiDetail.negosiasiDetailJasaList, function (negosiasi) {
                negosiasi.hargaSatuanAfterCondition = negosiasi.hargaSatuanOri;
                negosiasi.hargaTotalOri = negosiasi.itemPengadaan.kuantitas * negosiasi.hargaSatuanOri;
                negosiasi.hargaTotalAfterCondition = negosiasi.hargaTotalOri;
                negosiasi.itemCP = 0;
            })

            //hitung masing2 item CP
            angular.forEach(negosiasiDetail.itemConditionList, function (cp) {
                /*Cari item pengadaan by cp.itemPengadaan.id*/
                angular.forEach(negosiasiDetail.negosiasiDetailMaterialList, function (negosiasi) {
                    if (negosiasi.itemPengadaan.id == cp.itemPengadaan.id) {
                        var valCP = hitungItemCp(negosiasi.hargaSatuanAfterCondition, cp);
                        negosiasi.itemCP = parseFloat(negosiasi.itemCP) + parseFloat(valCP);
                        negosiasi.hargaSatuanAfterCondition = parseFloat(negosiasi.hargaSatuanAfterCondition) + parseFloat(valCP);
                        negosiasi.hargaTotalAfterCondition = negosiasi.hargaSatuanAfterCondition * negosiasi.itemPengadaan.kuantitas;
                    }
                })

                angular.forEach(negosiasiDetail.negosiasiDetailJasaList, function (negosiasi) {
                    if (negosiasi.itemPengadaan.id == cp.itemPengadaan.id) {
                        var valCP = hitungItemCp(negosiasi.hargaSatuanAfterCondition, cp);
                        negosiasi.itemCP = parseFloat(negosiasi.itemCP) + parseFloat(valCP);
                        negosiasi.hargaSatuanAfterCondition = parseFloat(negosiasi.hargaSatuanAfterCondition) + parseFloat(valCP);
                        negosiasi.hargaTotalAfterCondition = negosiasi.hargaSatuanAfterCondition * negosiasi.itemPengadaan.kuantitas;
                    }
                })
            })

            /*Hitung dulu total hargaTotalAfterCondition dan Ori*/
            negosiasiDetail.hargaTotalAfterCondition = 0;
            negosiasiDetail.hargaTotalOri = 0;

            angular.forEach(negosiasiDetail.negosiasiDetailMaterialList, function (negosiasi) {
                negosiasiDetail.hargaTotalAfterCondition = parseFloat(negosiasiDetail.hargaTotalAfterCondition) + parseFloat(negosiasi.hargaTotalAfterCondition);
                negosiasiDetail.hargaTotalOri = parseFloat(negosiasiDetail.hargaTotalOri) + parseFloat(negosiasi.hargaTotalOri);
            })
            angular.forEach(negosiasiDetail.negosiasiDetailJasaList, function (negosiasi) {
                negosiasiDetail.hargaTotalAfterCondition = parseFloat(negosiasiDetail.hargaTotalAfterCondition) + parseFloat(negosiasi.hargaTotalAfterCondition);
                negosiasiDetail.hargaTotalOri = parseFloat(negosiasiDetail.hargaTotalOri) + parseFloat(negosiasi.hargaTotalOri);
            })


            /*Hitung header condition*/
            negosiasiDetail.headerCP = 0;
            angular.forEach(negosiasiDetail.headerConditionList, function (cp) {
                var valCP = hitungItemCp(negosiasiDetail.hargaTotalAfterCondition, cp);
                negosiasiDetail.headerCP = parseFloat(negosiasiDetail.headerCP) + parseFloat(valCP);
                negosiasiDetail.hargaTotalAfterCondition += valCP;
            })
        }


        var hitungItemCp = function (val, cp) {
            return (cp.conditionalPrice.isPersentage ? (val * (cp.nilai / 100)) : cp.nilai) * cp.conditionalPrice.fungsi;
        }


        /*XEDITABLE ITEM CP Function ------------------------------------------------------------------------------------*/

        $scope.showItemCP = function (cp) {
            var selected = [];
            if (cp.conditionalPrice) {
                selected = $filter('filter')($scope.masterItemCP, {
                    id: cp.conditionalPrice.id
                });
            }
            return selected.length ? selected[0].nama : 'Not set';
        };

        $scope.showItemPengadaan = function (cp) {
            var selected = [];
            if (cp.itemPengadaan) {
                selected = $filter('filter')($scope.itemPengadaanList, {
                    id: cp.itemPengadaan.id
                });
            }
            return selected.length ? selected[0].item.kode : 'Not set';
        };

        $scope.saveItemCP = function (data) {
            data.isNew = false;
            $scope.CPReady = true;
            $scope.hitung();

        };

        $scope.removeItemCP = function (index) {
            negosiasiDetail.itemConditionList.splice(index, 1);
            $scope.hitung();
        };

        $scope.addItemCP = function () {
            $scope.inserted = {
                isNew: true
            };
            negosiasiDetail.itemConditionList.push($scope.inserted);
            $scope.CPReady = false;
        };

        $scope.checkBeforeSaveItemCP_ConditionalPrice = function (data) {
            if (typeof data == 'undefined') {
                return 'Please select Conditional Price!';
            }
        };

        $scope.checkBeforeSaveItemCP_ItemPengadaan = function (data) {
            if (typeof data == 'undefined') {
                return 'Please select Item!';
            }
        };

        $scope.checkBeforeSaveItemCP_Amount = function (data) {
            if (typeof data == 'undefined') {
                return 'Please input amount!';
            }
        };

        $scope.cancelItem = function (rowform, index, cp) {
            if (cp.isNew) {
                $scope.removeItemCP(index);
            }

            rowform.$cancel();
            $scope.CPReady = true;
        }

        $scope.editItem = function (rowform) {
            rowform.$show();
            $scope.CPReady = false;
        }


        /*XEDITABLE HEADER CP Function ------------------------------------------------------------------------------------*/
        $scope.showHeaderCP = function (cp) {
            var selected = [];
            if (cp.conditionalPrice) {
                selected = $filter('filter')($scope.masterHeaderCP, {
                    id: cp.conditionalPrice.id
                });
            }
            return selected.length ? selected[0].nama : 'Not set';
        };

        $scope.saveHeaderCP = function (data) {
            data.isNew = false;
            $scope.CPReady = true;
            $scope.hitung();
        };

        $scope.addHeaderCP = function () {
            $scope.inserted = {
                isNew: true
            };
            negosiasiDetail.headerConditionList.push($scope.inserted);
            $scope.CPReady = false;
        };

        $scope.removeHeaderCP = function (index) {
            negosiasiDetail.headerConditionList.splice(index, 1);
            $scope.hitung();
        };

        $scope.checkBeforeSaveHeaderCP_CP = function (data) {
            if (typeof data == 'undefined') {
                return 'Please select Conditional Price!';
            }
        };

        $scope.checkBeforeSaveHeaderCP_Amount = function (data) {
            if (typeof data == 'undefined') {
                return 'Please input amount!';
            }
        };

        $scope.cancelHeader = function (rowform, index, cp) {
            if (cp.isNew) {
                $scope.removeHeaderCP(index)
            }
            $scope.CPReady = true;
            rowform.$cancel();

        }

        $scope.editHeader = function (rowform) {
            rowform.$show();
            $scope.CPReady = false;
        }




        /* BOTTOM MENU ------------------------------------------------------------------------------------*/


        /*tombol aksi back to index*/
        $scope.gotoIndex = function () {
            $location.path('/appvendor/promise/procurement/vendor/negosiasi');
        };



        /*tombol aksi back to index*/
        $scope.sendPriceToBuyer = function () {
            ModalService.showModalConfirmation().then(function (result) {
                $scope.loadingSaving = true;
                $scope.savingIntoNegosiasi(false);
            });
        };

        /*tombol aksi last Offer*/
        $scope.sendLastOffer = function () {
            ModalService.showModalConfirmation().then(function (result) {
                $scope.loadingSaving = true;
                $scope.savingIntoNegosiasi(true);
            });
        };



        $scope.savingIntoNegosiasi = function (lastOffer) {

            var postData = {
                "suratPenawaran": negosiasiDetail.suratPenawaran.id,
                "pengadaan": negosiasiDetail.suratPenawaran.pengadaan.id,
                "vendor": negosiasiDetail.vendor.id,
                "nilaiPenawaranOri": negosiasiDetail.hargaTotalOri,
                "nilaiPenawaranAfterCondition": negosiasiDetail.hargaTotalAfterCondition,
                "userId": $rootScope.userLogin.user.id,
                "isVendor": 1,
                "isPanitia": 0,
                "isDeal": (lastOffer == true ? 1 : 0),
                "isDelete": 0,
                "mataUang": negosiasiDetail.kursPengadaan.mataUang.id
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
                $scope.isLastOffer = lastOffer;
                $scope.savingIntoNegosiasiDetail(data);
            })
        }


        $scope.savingIntoNegosiasiDetail = function (negosiasi) {
            console.log(">>> savingIntoNegosiasiDetail");
            $scope.recordSaveCtr = 0;
            $scope.recordToSave = (negosiasiDetail.negosiasiDetailMaterialList.length + negosiasiDetail.negosiasiDetailJasaList.length);

            /*Simpan Material*/
            angular.forEach(negosiasiDetail.negosiasiDetailMaterialList, function (negosiasiDetail) {
                    saveDetail(negosiasi, negosiasiDetail);
                })
                /*Simpan Jasa*/
            angular.forEach(negosiasiDetail.negosiasiDetailJasaList, function (negosiasiDetail) {
                saveDetail(negosiasi, negosiasiDetail);
            })
        }


        /* Called from savingIntoNegosiasiDetail*/
        var saveDetail = function (negosiasi, negosiasiDetail) {
            console.log(">>> saveDetail");
            var postData = {
                "negosiasi": negosiasi.id,
                "itemPengadaan": negosiasiDetail.itemPengadaan.id,
                "hargaSatuanAfterCondition": negosiasiDetail.hargaSatuanAfterCondition,
                "hargaTotalAfterCondition": negosiasiDetail.hargaTotalAfterCondition,
                "hargaSatuanOri": negosiasiDetail.hargaSatuanOri,
                "hargaTotalOri": negosiasiDetail.hargaTotalOri,
                "deleted": 0,
                "userId": $rootScope.userLogin.user.id,
                "isDelete": 0,
                "mataUang": negosiasiDetail.mataUang.id
            }

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/create',
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
                $scope.saveDetailSuccessHandler(negosiasi);
            })

        }


        $scope.saveDetailSuccessHandler = function (negosiasi) {
            console.log(">>> saveDetailSuccessHandler");
            $scope.recordSaveCtr++;
            if ($scope.recordSaveCtr == $scope.recordToSave) {
                saveNegosiasiConditionalPriceList(negosiasi);
            }
        }


        /* Called from saveDetailSuccessHandler*/
        var saveNegosiasiConditionalPriceList = function (negosiasi) {
            console.log(">>> saveNegosiasiConditionalPriceList 1");
            $scope.recordSaveCtr = 0;
            $scope.recordToSave = (negosiasiDetail.headerConditionList.length + negosiasiDetail.itemConditionList.length);

            /*Untuk mejaga aja ketika tidak ada conditional price*/
            if ($scope.recordToSave == 0) {
                $scope.loadingSaving = false;
                toaster.pop('success', 'Sukses', 'Data penawaran negosiasi berhasil dikirim.');
            } else {

                /*Simpan Header*/
                angular.forEach(negosiasiDetail.headerConditionList, function (cp) {
                        saveNegosiasiConditionalPrice(negosiasi, cp);
                    })
                    /*Simpan Item*/
                angular.forEach(negosiasiDetail.itemConditionList, function (cp) {

                    saveNegosiasiConditionalPrice(negosiasi, cp);
                })
            }
        }

        /* Called from saveConditionalPriceList*/
        var saveNegosiasiConditionalPrice = function (negosiasi, cp) {

            var postData = {
                "negosiasi": negosiasi.id,
                "conditionalPrice": cp.conditionalPrice.id,
                "nilai": cp.nilai,
                "userId": $rootScope.userLogin.user.id,
                "isDelete": 0
            }

            if (cp.itemPengadaan != null) postData.itemPengadaan = cp.itemPengadaan.id;

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiConditionalPriceServices/insert',
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
                $scope.saveNegosiasiConditionalPriceSuccessHandler();
            })
        }


        $scope.saveNegosiasiConditionalPriceSuccessHandler = function (negosiasi) {
            console.log(">>> saveNegosiasiConditionalPriceSuccessHandler");
            $scope.recordSaveCtr++;
            if ($scope.recordSaveCtr == $scope.recordToSave) {
                $scope.loadingSaving = false;
                toaster.pop('success', 'Sukses', 'Data penawaran negosiasi berhasil dikirim.');
            }
        }

        $scope.updateLastOfferStatus = function () {

            $scope.isLastOffer = (negosiasiDetail.negosiasiLastFromPanitia.isDeal || negosiasiDetail.negosiasiLastFromVendor.isDeal);
        }

        /*tombol aksi refresh*/
        $scope.refreshPage = function () {
            $scope.loadingNegosiasiLastFromPanitia = true;
            $location.hash("negosiasiLastFromPanitia");
            $anchorScroll();

            /*Ambil Penawaran Terakhir dari panitia */
            $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromPanitiaByPengadaanAndVendor/' + $scope.pengadaanId + '/' + negosiasiDetail.vendor.id, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {

                if (typeof data[0] != 'undefined') {
                    negosiasiDetail.negosiasiLastFromPanitia = data[0];
                }
                $scope.loadingNegosiasiLastFromPanitia = false;
                toaster.pop('success', 'Sukses', 'Data penawaran panitia telah diperbaharui');
                $scope.updateLastOfferStatus();

            }).error(function (data, status, headers, config) {
                $scope.loadingNegosiasiLastFromPanitia = false;
                console.log("***ERROR** - /procurement/master/conditionalPriceServices/getList");
            });
        };

        $scope.showHistory = function (negosiasi) {

            $rootScope.myBackurl = '/appvendor/promise/procurement/negosiasi/vendor/total/detail/' + $scope.pengadaanId;
            $location.path('/app/promise/procurement/negosiasi/total/detail/history/' + $scope.pengadaanId + '/' + negosiasiDetail.vendor.id);


        };

        loadVendorNegosiasi();

    }

    NegosiasiTotalDetailController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams', 'editableOptions', 'editableThemes', '$anchorScroll', 'RequestService'];

})();