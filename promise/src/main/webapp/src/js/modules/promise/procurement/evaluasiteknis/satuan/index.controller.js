(function () {
    'use strict';

    angular.module('naut')
        .controller('EvaluasiTeknisSatuanViewController', EvaluasiTeknisSatuanViewController);

    function EvaluasiTeknisSatuanViewController($scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $q, $timeout, ngTableParams) {
        var form = this;
        $scope.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('app/promise/procurement/evaluasiteknis');
                }
            } else {
                $location.path('app/promise/procurement/evaluasiteknis');
            }
        }

        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        $scope.viewKriteriaTeknis = function (data) {
            $location.path('app/promise/procurement/evaluasiteknis/view/total/sistemgugur');
            $rootScope.kriteriaTeknisData = data;
        }

        /* START Detail Pengadaan */
        if (typeof $rootScope.detilPengadaan !== 'undefined') {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {}
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
        /*---END Detail Pengadaan----*/


        /* START Bidang Usaha Pengadaan*/
        $scope.getListBidangUsaha = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.subBidangUsahaByPengadaanIdList = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListBidangUsaha();
        /*---END Bidang Usaha Pengadaan---*/

        /* START Rincian Kebutuhan Material*/
        $scope.getListItemPengadaan = function () {
            var count = 0;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.listMaterials = data;
                for (var i = 0; i < data.length; i++) {
                    count = count + 1;
                    data[i].index = i + 1;
                    $scope.getListSuratPenawaran(data[i].item.id);
                }
                $scope.tableMaterialPengadaanList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: data.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {});
        }
        $scope.getListItemPengadaan();
        /*---END Rincian Kebutuhan Materrial---*/

        /* START Rincian Kebutuhan Jasa*/
        $scope.getListRincianKebutuhanJasa = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                for (var i = 0; i < data.length; i++) {
                    data[i].index = i + 1;
                }

                $scope.tableJasaPengadaanList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: data.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListRincianKebutuhanJasa();
        /*---END Rincian Kebutuhan Jasa---*/

        /* START GET NilaiEvaluasi*/
        $scope.getNilaiEvaluasi = function (vendorId, itemId) {
                $scope.loading = true;
                $scope.unvalid = false;
                var deferred = $q.defer();
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/getListByVendorPengadaanItem/' + vendorId + '/' + $rootScope.detilPengadaan.id + '/' + itemId;
                $timeout(function () {
                    $http.get(uri)
                        .success(function (data, status, headers, config) {
                            if (data.length > 0) {
                                deferred.resolve(data[0].nilaiEvaluasiTeknis);
                            } else {
                                $scope.unvalid = true;
                                deferred.resolve(null);
                            }
                            $scope.loading = false;
                        });
                }, 1000);
                return deferred.promise;
            }
            /*---END GET NilaiEvaluasi---*/

        /* START ASynchronus merge table */
        $scope.callXhrAsynchronous = function (x, vendorId, itemId) {
                var myPromise = $scope.getNilaiEvaluasi(vendorId, itemId);
                // wait until the promise return resolve or eject
                //"then" has 2 functions (resolveFunction, rejectFunction)
                myPromise.then(function (resolve) {
                    if (typeof $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi !== 'undefined') {
                        $scope.suratPenawaranByPengadaanList[x]['nilaiEvaluasi'] = Math.round(resolve);
                    } else {
                        if (resolve == null) {
                            $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi = null;
                        } else {
                            $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi = Math.round(resolve);
                        }
                    }
                }, function (reject) {});

            }
            /*---END ASynchronus merge table---*/

        /* START List Vendor */
        $scope.getListSuratPenawaran = function (itemId) {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $rootScope.detilPengadaan.id, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    var x;
                    $scope.suratPenawaranByPengadaanList = data;
                    for (x = 0; x < data.length; x++) {
                        $scope.callXhrAsynchronous(x, data[x].vendor.id, itemId);
                    }
                }).error(function (data, status, headers, config) {});
            }
            /*---END List Vendor---*/

        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
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
                    pengadaanId: $scope.pengadaanId,
                    tahapanPengadaanId: $scope.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (typeof data !== 'undefined') {
                    toaster.pop('success', 'Evaluasi Teknis', 'Simpan ' + 'Evaluasi Teknis' + ', berhasil.');
                }
                $location.path('app/promise/procurement/evaluasiteknis');
            });
        };


        $scope.saveData = function () {
            if ($scope.unvalid == false) {
                $scope.updatePengadaan();
            } else {
                alert('Masih ada daftar Vendor yang belum dilakukan Evaluasi Teknis')
            }
        }
        $rootScope.saveAfterConfirmation = $scope.saveData;

        /*modal Vendor*/
        $scope.goModalVendor = function (itemId, size) {
            $rootScope.validate = $scope.getListItemPengadaan;
            $rootScope.itemPengadaan = itemId;
            var modalInstance = $modal.open({
                templateUrl: '/EvaluasiTeknisSatuanVendor.html',
                controller: ModalVendorCtrl,
                size: size,
            });
        };

        /*modal Confirmation*/
        $scope.goModalConfirmation = function (data) {
            var modalInstance = $modal.open({
                templateUrl: '/ModalConfirmation.html',
                controller: ModalInstanceConfirmationCtrl,
            });
        };


        var ModalVendorCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, toaster) {
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.chooseModal = function (data, size) {
                if ($scope.detilPengadaan.sistemEvaluasiPenawaran.id == 1) {
                    $scope.goModalSistemGugur(data, size);
                } else {
                    $scope.goModalMeritPoint(data, size);
                }
            }

            /*modal Sistem Gugur*/
            $scope.goModalSistemGugur = function (data, size) {
                $rootScope.kriteriaTeknisData = data;
                var modalInstance = $modal.open({
                    templateUrl: '/EvaluasiTeknisTotalSistemGugur.html',
                    controller: ModalInstanceEvaluasiTeknisSatuanSistemGugurCtrl,
                    size: size
                });
                $modalInstance.dismiss('cancel');
            };

            /*modal Merit Point*/
            $scope.goModalMeritPoint = function (data, size) {
                $rootScope.kriteriaTeknisData = data;
                var modalInstance = $modal.open({
                    templateUrl: '/EvaluasiTeknisTotalMeritPoint.html',
                    controller: ModalInstanceEvaluasiTeknisSatuanMeritPoinCtrl,
                    size: size
                });
                $modalInstance.dismiss('cancel');
            };

            /* START GET NilaiEvaluasi*/
            $scope.getNilaiEvaluasi = function (vendorId) {
                    $scope.loading = true;
                    var deferred = $q.defer();
                    var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/getListByVendorPengadaanItem/' + vendorId + '/' + $rootScope.detilPengadaan.id + '/' + $rootScope.itemPengadaan;
                    $timeout(function () {
                        $http.get(uri)
                            .success(function (data, status, headers, config) {
                                if (data.length > 0) {
                                    deferred.resolve(data[0].nilaiEvaluasiTeknis);
                                } else {
                                    deferred.resolve(null);
                                }
                                $scope.loading = false;
                            });
                    }, 1000);
                    return deferred.promise;
                }
                /*---END GET NilaiEvaluasi---*/

            /* START ASynchronus merge table */
            $scope.callXhrAsynchronous = function (x, vendorId) {
                    var myPromise = $scope.getNilaiEvaluasi(vendorId);
                    // wait until the promise return resolve or eject
                    //"then" has 2 functions (resolveFunction, rejectFunction)
                    myPromise.then(function (resolve) {
                        if (typeof $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi !== 'undefined') {
                            $scope.suratPenawaranByPengadaanList[x]['nilaiEvaluasi'] = Math.round(resolve);
                        } else {
                            if (resolve == null) {
                                $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi = null;
                            } else {
                                $scope.suratPenawaranByPengadaanList[x].nilaiEvaluasi = Math.round(resolve);
                            }
                        }
                    }, function (reject) {});

                }
                /*---END ASynchronus merge table---*/

            /* START List Vendor */
            $scope.getListSuratPenawaran = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $rootScope.detilPengadaan.id, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    var x;
                    $scope.suratPenawaranByPengadaanList = data;
                    for (x = 0; x < data.length; x++) {
                        $scope.callXhrAsynchronous(x, data[x].vendor.id);
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getListSuratPenawaran();
            /*---END List Vendor---*/

        };
        ModalVendorCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'toaster'];
        /*------*/

        var ModalInstanceEvaluasiTeknisSatuanSistemGugurCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, toaster) {
            var formModal = this;
            $scope.itemPengadaan = $rootScope.itemPengadaan;
            $scope.sistemGugurList = [];
            var evaluasiTeknis = [];

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            /* START Vendor */
            if (typeof $rootScope.kriteriaTeknisData !== 'undefined') {
                $scope.kriteriaTeknisData = $rootScope.kriteriaTeknisData;
            } else {}
            $scope.pengadaanId = $rootScope.detilPengadaan.id;
            formModal.vendorId = $scope.kriteriaTeknisData.id;
            /*---END Vendor----*/


            /* GET Evaluasi Teknik List*/
            $scope.evaluasiTeknisList = [];
            $scope.getEvaluasiTeknisList = function () {
                $scope.checked1 = true;
                $scope.checked2 = false;
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/evaluasiteknis/EvaluasiTeknisSatuanServices/getByPengadaanVendorItem/' + $scope.pengadaanId + '/' + formModal.vendorId + '/' + $scope.itemPengadaan, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    if (data.length > 0) {
                        $scope.watchTeknisList = true;
                        $scope.kriteriaTeknisByPengadaanList = data;
                        var i;
                        for (i = 0; i < data.length; i++) {
                            formModal = new Object();
                            formModal.kriteriaTeknisId = data[i].kriteriaTeknis.id;
                            formModal.id = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.itemId = data[i].item.id;
                            if (data[i].status == true) {
                                formModal.status = 1;
                            } else {
                                $scope.checked1 = false;
                                $scope.checked2 = true;
                                formModal.status = 0;
                            }
                            evaluasiTeknis.push(formModal);
                        }
                    } else {
                        $scope.getListKriteriaTeknis();
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getEvaluasiTeknisList();
            /*---END Evaluasi Teknik List---*/

            /* LIST Kriteria Teknis*/
            $scope.getListKriteriaTeknis = function () {
                    $scope.checked2 = true;
                    $scope.checked1 = false;
                    $scope.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisServices/getByPengadaanIdList/' + $scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        $scope.kriteriaTeknisByPengadaanList = data;
                        var i;
                        for (i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                            $scope.kriteriaTeknisByPengadaanList[i]["status"] = false;
                            formModal = new Object();
                            formModal.kriteriaTeknisId = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.status = 0;
                            formModal.itemId = $scope.itemPengadaan;
                            evaluasiTeknis.push(formModal);
                        }
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {

                    });

                }
                /*---END Kriteria Teknis---*/

            $scope.changeStatus = function (status) {
                for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                    evaluasiTeknis[i].status = 1;
                    $scope.kriteriaTeknisByPengadaanList[i].status = status;
                }
            }
            $scope.checkedKriteriaTeknis = function (data) {
                var kriteria = $scope.kriteriaTeknisByPengadaanList.indexOf(data);
                if ($scope.kriteriaTeknisByPengadaanList[kriteria].status == true) {
                    evaluasiTeknis[kriteria].status = 1;
                } else {
                    evaluasiTeknis[kriteria].status = 0;
                }

                //change status to the summary
                var checkSumTrue = 0;
                for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                    if ($scope.kriteriaTeknisByPengadaanList[i].status) {
                        checkSumTrue += 1;
                    }
                }
                if (checkSumTrue == $scope.kriteriaTeknisByPengadaanList.length) {
                    $scope.checked1 = true;
                    $scope.checked2 = false;
                } else {
                    $scope.checked1 = false;
                    $scope.checked2 = true;
                }
                //end proses change status summary
            };

            $scope.checkSummary1 = function () {
                $scope.checked1 = true;
                $scope.checked2 = false;
                $scope.changeStatus(true);
            }

            $scope.checkSummary2 = function () {
                $scope.checked1 = false;
                $scope.checked2 = true;
                $scope.changeStatus(false);
            }

            var formNilaiEvaluasi = {
                id: 0,
                vendorId: 0,
                nilaiEvaluasiTeknis: 0,
                nilaiEvaluasiAdministrasi: 0,
                nilaiEvaluasiHarga: 0,
                itemId: 0
            };

            /* GET Nilai Evaluasi Vendor*/
            $scope.getNilaiEvaluasiVendor = function (param) {
                $scope.loading = true;
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/getListByVendorPengadaanItem/' + formModal.vendorId + '/' + $scope.pengadaanId + '/' + $scope.itemPengadaan;
                $http.get(uri, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.nilaiEvaluasiVendor = data;
                    if (data.length > 0) {
                        formNilaiEvaluasi.id = $scope.nilaiEvaluasiVendor[0].id;
                        formNilaiEvaluasi.nilaiEvaluasiHarga = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiHarga;
                        formNilaiEvaluasi.nilaiEvaluasiAdministrasi = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiAdministrasi;
                        formNilaiEvaluasi.itemId = $scope.nilaiEvaluasiVendor[0].item.id;
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getNilaiEvaluasiVendor();
            /*---END Nilai Evaluasi Vendor---*/

            $scope.calculate = function () {
                var x;
                var checked = 0;
                for (x = 0; x < $scope.kriteriaTeknisByPengadaanList.length; x++) {
                    if ($scope.kriteriaTeknisByPengadaanList[x].status == true) {
                        checked = checked + 1;
                    }
                }
                return checked / $scope.kriteriaTeknisByPengadaanList.length * 100;
            }

            formNilaiEvaluasi.vendorId = formModal.vendorId;
            formNilaiEvaluasi.pengadaanId = $scope.pengadaanId;
            formNilaiEvaluasi.itemId = $scope.itemPengadaan;
            $scope.save = function () {
                $scope.loading = true;
                var ix;
                for (ix = 0; ix < evaluasiTeknis.length; ix++) {
                    //insert ke evaluasiteknistotal
                    var uri = $rootScope.backendAddress + '/procurement/evaluasiteknis/EvaluasiTeknisSatuanServices/';
                    if (typeof evaluasiTeknis[ix].id !== 'undefined') {
                        uri = uri + 'update';
                    } else {
                        uri = uri + 'create';
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
                        data: evaluasiTeknis[ix]
                    }).success(function (data, status, headers, config) {
                        toaster.pop('success', 'Evaluasi Teknis', 'Simpan ' + 'data' + ', berhasil.');
                        $scope.loading = false;
                    });
                }
                //SIMPAN nilai evaluasi Teknis
                formNilaiEvaluasi.nilaiEvaluasiTeknis = $scope.calculate();
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/';
                if (formNilaiEvaluasi.id != 0) {
                    uri = uri + 'update';
                } else {
                    uri = uri + 'create';
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
                    data: formNilaiEvaluasi
                }).success(function (data, status, headers, config) {
                    toaster.pop('success', 'Evaluasi Teknis', 'Simpan ' + 'data' + ', berhasil.');
                    $scope.loading = false;
                });
                $modalInstance.dismiss('cancel');
                $location.path('/app/promise/procurement/evaluasiteknis/view/satuan');
                $rootScope.validate();
            };

        };
        ModalInstanceEvaluasiTeknisSatuanSistemGugurCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'toaster'];
        /*------*/

        var ModalInstanceEvaluasiTeknisSatuanMeritPoinCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, toaster) {
            var formModal = this;
            var evaluasiTeknis = [];
            $scope.itemPengadaan = $rootScope.itemPengadaan;

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            /* START Vendor */
            if (typeof $rootScope.kriteriaTeknisData !== 'undefined') {
                $scope.kriteriaTeknisData = $rootScope.kriteriaTeknisData;
            } else {}
            $scope.pengadaanId = $rootScope.detilPengadaan.id;
            formModal.vendorId = $scope.kriteriaTeknisData.id;
            /*---END Vendor----*/


            /* GET Evaluasi Teknik List*/
            $scope.evaluasiTeknisList = [];
            $scope.getEvaluasiTeknisList = function () {
                $scope.loading = true;
                $scope.watchTeknisList = false;
                var uri = $rootScope.backendAddress + '/procurement/evaluasiteknis/EvaluasiTeknisSatuanMeritPointServices/getByPengadaanVendorItem/' + $scope.pengadaanId + '/' + formModal.vendorId + '/' + $scope.itemPengadaan;
                $http.get(uri, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    if (data.length > 0) {
                        $scope.watchTeknisList = true;
                        $scope.kriteriaTeknisByPengadaanList = data;
                        $scope.total = 0;
                        $scope.totalInput = 0;
                        for (var i = 0; i < data.length; i++) {
                            formModal = new Object();
                            formModal.kriteriaTeknisMeritPointId = data[i].kriteriaTeknisMeritPoint.id;
                            formModal.id = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.nilai = data[i].nilai;
                            formModal.itemId = data[i].item.id;
                            evaluasiTeknis.push(formModal);
                            $scope.total = $scope.total + data[i].kriteriaTeknisMeritPoint.nilai;
                            $scope.totalInput = $scope.totalInput + data[i].nilai;
                        }
                    } else {
                        $scope.getListKriteriaTeknis();
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getEvaluasiTeknisList();
            /*---END Evaluasi Teknik List---*/

            /* LIST Kriteria Teknis*/
            $scope.getListKriteriaTeknis = function () {
                    $scope.total = 0;
                    $scope.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointServices/getByPengadaanIdList/' + $scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        $scope.kriteriaTeknisByPengadaanList = data;
                        var i;
                        for (i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                            $scope.kriteriaTeknisByPengadaanList[i]["nilaiInput"] = 0;
                            formModal = new Object();
                            formModal.kriteriaTeknisMeritPointId = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.nilai = 0;
                            formModal.itemId = $scope.itemPengadaan;
                            evaluasiTeknis.push(formModal);
                            $scope.total = $scope.total + data[i].nilai;
                            $scope.totalInput = 0;
                        }
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {

                    });

                }
                /*---END Kriteria Teknis---*/
            $scope.accumulate = function (data) {
                var kriteria = $scope.kriteriaTeknisByPengadaanList.indexOf(data);
                if ($scope.watchTeknisList == false) {
                    if (data.nilaiInput > 100) {
                        data.nilaiInput = $scope.kriteriaTeknisByPengadaanList[kriteria].nilai;
                        alert("masukkan nilai antara 0-100");
                    }
                    evaluasiTeknis[kriteria].nilai = data.nilaiInput;
                    $scope.totalInput = new Number();
                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilaiInput);
                    }
                } else {
                    if (data.nilai > 100) {
                        data.nilai = $scope.kriteriaTeknisByPengadaanList[kriteria].kriteriaTeknisMeritPoint.nilai;
                        alert("masukkan nilai antara 0-100");
                    }
                    evaluasiTeknis[kriteria].nilai = data.nilai;
                    $scope.totalInput = new Number();
                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilai);
                    }
                }
            }

            $scope.checkedKriteriaTeknis = function (data) {
                var kriteria = $scope.kriteriaTeknisByPengadaanList.indexOf(data);
                if ($scope.kriteriaTeknisByPengadaanList[kriteria].status == true) {
                    evaluasiTeknis[kriteria].status = 1;
                } else {
                    evaluasiTeknis[kriteria].status = 0;
                }
            };

            var formNilaiEvaluasi = {
                id: 0,
                vendorId: 0,
                nilaiEvaluasiTeknis: 0,
                nilaiEvaluasiAdministrasi: 0,
                nilaiEvaluasiHarga: 0,
                itemId: 0
            };

            /* GET Nilai Evaluasi Vendor*/
            $scope.getNilaiEvaluasiVendor = function (param) {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/getListByVendorPengadaanItem/' + formModal.vendorId + '/' + $scope.pengadaanId + '/' + $scope.itemPengadaan, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.nilaiEvaluasiVendor = data;
                    if (data.length > 0) {
                        formNilaiEvaluasi.id = $scope.nilaiEvaluasiVendor[0].id;
                        formNilaiEvaluasi.nilaiEvaluasiHarga = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiHarga;
                        formNilaiEvaluasi.nilaiEvaluasiAdministrasi = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiAdministrasi;
                        $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiTeknis = Math.round(data[0].nilaiEvaluasiTeknis);
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getNilaiEvaluasiVendor();
            /*---END Nilai Evaluasi Vendor---*/

            $scope.calculate = function () {
                var total = 0;
                for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                    var nilai1 = 0;
                    var nilai2 = 0;
                    var constant = 100;
                    if ($scope.watchTeknisList == false) {
                        nilai1 = $scope.kriteriaTeknisByPengadaanList[i].nilai;
                        nilai2 = $scope.kriteriaTeknisByPengadaanList[i].nilaiInput;
                    } else {
                        nilai1 = $scope.kriteriaTeknisByPengadaanList[i].kriteriaTeknisMeritPoint.nilai;
                        nilai2 = $scope.kriteriaTeknisByPengadaanList[i].nilai;
                    }
                    total = total + ((nilai1 * nilai2) / constant);
                }
                return total;
            }

            formNilaiEvaluasi.vendorId = formModal.vendorId;
            formNilaiEvaluasi.pengadaanId = $scope.pengadaanId;
            formNilaiEvaluasi.itemId = $scope.itemPengadaan;
            $scope.save = function () {
                $scope.loading = true;
                var ix;
                for (ix = 0; ix < evaluasiTeknis.length; ix++) {
                    //insert ke evaluasiteknistotal
                    var uri = $rootScope.backendAddress + '/procurement/evaluasiteknis/EvaluasiTeknisSatuanMeritPointServices/';
                    if (typeof evaluasiTeknis[ix].id !== 'undefined') {
                        uri = uri + 'update';
                    } else {
                        uri = uri + 'create';
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
                        data: evaluasiTeknis[ix]
                    }).success(function (data, status, headers, config) {
                        toaster.pop('success', 'Evaluasi Teknis', 'Simpan ' + 'data' + ', berhasil.');
                        $scope.loading = false;
                    });
                }
                //SIMPAN nilai evaluasi Teknis
                formNilaiEvaluasi.nilaiEvaluasiTeknis = $scope.calculate();
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/';
                if (formNilaiEvaluasi.id != 0) {
                    uri = uri + 'update';
                } else {
                    uri = uri + 'create';
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
                    data: formNilaiEvaluasi
                }).success(function (data, status, headers, config) {
                    toaster.pop('success', 'Evaluasi Teknis', 'Simpan ' + 'data' + ', berhasil.');
                    $scope.loading = false;
                });
                $modalInstance.dismiss('cancel');
                $location.path('/app/promise/procurement/evaluasiteknis/view/satuan');
                $rootScope.validate();
            };

        };
        ModalInstanceEvaluasiTeknisSatuanMeritPoinCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'toaster'];
        /*------*/

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


    EvaluasiTeknisSatuanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$q', '$timeout', 'ngTableParams'];

})();
