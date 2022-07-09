/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiAdministrasiCetakController', EvaluasiAdministrasiCetakController);

    function EvaluasiAdministrasiCetakController(ModalService, $http, $scope, $rootScope, $resource, $location, $modal, toaster, ngTableParams, $timeout, $q) {
        var eaec = this;
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        if (typeof $rootScope.detilPengadaan !== 'undefined') {
            eaec.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
        }
        eaec.pengadaanId = $scope.detilPengadaan.id;
        
        //gettahapannext
        $scope.getTahapanNext = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + eaec.pengadaanId)
                .success(function (data, status, headers, config) {
                    eaec.tahapanPengadaanNext = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };
        $scope.getTahapanNext();

        /*Bidang Usaha Pengadaan relasi Pembukaaan Penawaran*/
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + eaec.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data,
            status, headers, config) {
            eaec.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {

        });
        /*------*/

        eaec.go = function (path) {
            $location.path(path);
        };

        /*list Material*/
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + eaec.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isMaterialNotEmpty = true;
            var itemPengadaanMaterial = data
            $scope.loading = false;
            eaec.itemPengadaanMaterial = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: itemPengadaanMaterial.length,
                getData: function ($defer, params) {
                    $defer.resolve(itemPengadaanMaterial.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /*------*/

        /*list Jasa*/
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + eaec.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            if (data.length > 0)
                $scope.isJasaNotEmpty = true;
            var itemPengadaanJasa = data;
            $scope.loading = false;
            eaec.itemPengadaanJasa = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: itemPengadaanJasa.length,
                getData: function ($defer, params) {
                    $defer.resolve(itemPengadaanJasa.slice((params.page() - 1) * params.count, params.page() * params.count()));
                }
            });
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /*------*/

        /*list Pendaftaran vendor*/
      /*  $scope.listVendor = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + eaec.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                eaec.pendaftaranVendorList = data;
                console.log('data pendaftaran vendor = '+JSON.stringify(eaec.pendaftaranVendorList));
                for (var a = 0; a < data.length; a++) {
                    $scope.asynchronousNilaiAdmin(a, data[a].vendor.id);
                }

            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }*/
        
        // vendor list baru
        $scope.listVendor = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getWithDeviasiByPengadaanFiltered/' + eaec.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                eaec.pendaftaranVendorList = data;
                //console.log('data pendaftaran vendor = '+JSON.stringify(eaec.pendaftaranVendorList));
                for (var a = 0; a < data.length; a++) {
                    $scope.asynchronousNilaiAdmin(a, data[a].suratPenawaran.vendor.id);
                }

            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        
        $scope.listVendor();
        $rootScope.listVendor = $scope.listVendor;
        /*------*/

        /* START GET NilaiEvaluasi*/
        $scope.getNilaliEvaluasi = function (x, vendorId) {
                $scope.loading = true;
                var deferred = $q.defer();
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + vendorId + '/' + $rootScope.detilPengadaan.id;
                $timeout(function () {
                    $http.get(uri)
                        .success(function (data, status, headers, config) {
                            if (data.length > 0) {
                                deferred.resolve(data[0].nilaiEvaluasiAdministrasi);
                                eaec.pendaftaranVendorList[x]['nilaiAdministrasi'] = data[0].nilaiEvaluasiAdministrasi;
                            } else {
                                deferred.resolve(null);
                                eaec.pendaftaranVendorList[x]['nilaiAdministrasi'] = null;
                            }
                            //console.log(eaec.pendaftaranVendorList[x].nilaiAdministrasi);
                            $http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/getByPengadaanIdList/' + eaec.pengadaanId, {
                                ignoreLoadingBar: true
                            }).success(function (dataLulus, status, headers, config) {
                                eaec.pendaftaranVendorList[x].statusLulus = 'Tidak Lulus';
                                if (eaec.detilPengadaan.sistemEvaluasiPenawaran.id == 2) {
                                    if (dataLulus.length > 0) {
                                        if (eaec.pendaftaranVendorList[x].nilaiAdministrasi >= dataLulus[0].nilaiMinimalKelulusanAdministrasi) {
                                            eaec.pendaftaranVendorList[x].statusLulus = 'Lulus';
                                            //console.log('INFO Nilai : ' + eaec.pendaftaranVendorList[x].nilaiAdministrasi);
                                        }
                                    }
                                } else {
                                    if (eaec.pendaftaranVendorList[x].nilaiAdministrasi == 100) {
                                        eaec.pendaftaranVendorList[x].statusLulus = 'Lulus';
                                    }
                                }
                            });
                            $scope.loading = false;
                        });
                }, 1000);
                return deferred.promise;
            }
            /*---END GET NilaiEvaluasi---*/

        $scope.asynchronousNilaiAdmin = function (x, vendorId) {
            var nilaiAdmin = $scope.getNilaliEvaluasi(x, vendorId);
            nilaiAdmin.then(function (resolve) {
                    //                    if (typeof eaec.pendaftaranVendorList[x].nilaiAdministrasi === 'undefined') {
                    //                        if (resolve == null) {
                    //                            eaec.pendaftaranVendorList[x]['nilaiAdministrasi'] = null;
                    //                        } else {
                    //                            eaec.pendaftaranVendorList[x]['nilaiAdministrasi'] = Math.round(resolve);
                    //                        }
                    //                    } else {
                    //                        if (resolve == null) {
                    //                            eaec.pendaftaranVendorList[x].nilaiAdministrasi = null;
                    //                        } else {
                    //                            eaec.pendaftaranVendorList[x].nilaiAdministrasi = Math.round(resolve);
                    //                        }
                    //                    }


                },
                function (reject) {});
        }
        $scope.btnSimpanDisable = false;
        eaec.btnSimpan = function () { 

			ModalService.showModalConfirmation().then(function (result) {
	            $scope.loading = true;
	            $scope.btnSimpanDisable = true;
	            $http({
	                method: 'POST',
	                url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan/',
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
	                    pengadaanId: eaec.pengadaanId,
	                    tahapanPengadaanId: eaec.tahapanPengadaanNext
	                }
	            }).success(function (data, status, headers, config) {
	                if (typeof data !== 'undefined') {
	                    toaster.pop('success', 'Evaluasi Administrasi', 'Simpan ' + data.namaPengadaan + ', berhasil.');
	                    $scope.loading = false;
	                    $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id = eaec.tahapanPengadaanNext;
	                }
	            });
			});
        }

        eaec.back = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/evaluasiAdministrasi');
                }
            } else {
                $location.path('/app/promise/procurement/evaluasiAdministrasi');
            }
        };

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

        /*modal*/
        eaec.goModalKriteria = function (vl, size) {
            $rootScope.vendorEa = vl;
            var modalInstance = $modal.open({
                templateUrl: '/modalKriteria.html',
                controller: ModalInstanceDetailCtrl,
                size: size
            });
        };

        var ModalInstanceDetailCtrl = function ($scope, $rootScope, $modalInstance, $resource) {

            var eaecModal = this;
            if (typeof $rootScope.detilPengadaan !== 'undefined') {
                $scope.detailVendor = $rootScope.vendorEa;
            } else {
                console.log('INFO Error');
            }
            eaec.pendaftaranVendorId = $rootScope.vendorEa.vendor.id;

            var evaluasiVendor = [];

            eaec.evaluasiAdministrasiServices = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/EvaluasiAdministrasiServices/getEvaluasiAdministrasiByVendorAndPengadaan/' + eaec.pendaftaranVendorId + '/' + eaec.pengadaanId).success(function (data, status, headers, config) {
                    if (data.length > 0) {
                        $scope.chk1 = true;
                        $scope.chk2 = false;
                        $scope.adminLook = true;
                        $scope.kriteriaAdministrasiList = data;
                        $scope.loading = false;
                        for (var i = 0; i < data.length; i++) {
                            eaecModal = new Object();
                            eaecModal.kriteriaAdministrasiId = data[i].kriteriaAdministrasi.id;
                            eaecModal.evaluasiAdministrasiId = data[i].id;
                            eaecModal.vendorId = $rootScope.vendorEa.vendor.id;
                            if (data[i].status == true) {
                                eaecModal.status = 1;
                            } else {
                                $scope.chk1 = false;
                                $scope.chk2 = true;
                                eaecModal.status = 0;
                            }
                            evaluasiVendor.push(eaecModal);
                        }
                    } else {
                        eaec.kriteriaAdministrasiServices();
                    }
                }).error(function (data, status, headers, config) {});
            }
            eaec.evaluasiAdministrasiServices();
            /*------*/

            eaec.kriteriaAdministrasiServices = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/getByPengadaanIdList/' + eaec.pengadaanId).success(function (data, status, headers, config) {
                    $scope.chk1 = false;
                    $scope.chk2 = true;
                    $scope.kriteriaAdministrasiList = data;
                    $scope.loading = false;
                    for (var i = 0; i < $scope.kriteriaAdministrasiList.length; i++) {
                        $scope.kriteriaAdministrasiList[i]["status"] = false;
                        eaecModal = new Object();
                        eaecModal.kriteriaAdministrasiId = data[i].id;
                        eaecModal.vendorId = $rootScope.vendorEa.vendor.id;
                        eaecModal.status = 0;
                        evaluasiVendor.push(eaecModal);
                    }
                }).error(function (data, status, headers, config) {});
            }

            $scope.changeStatus = function (nilai) {
                for (var i = 0; i < $scope.kriteriaAdministrasiList.length; i++) {
                    if (nilai) {
                        evaluasiVendor[i].status = 1;
                    } else {
                        evaluasiVendor[i].status = 0;
                    }
                    $scope.kriteriaAdministrasiList[i].status = nilai;
                }
            }

            /*Checklist Nilai Evaluasi Vendor*/
            $scope.statusCheck = function (list) {
                //console.log(JSON.stringify(evaluasiVendor));
                var centang = $scope.kriteriaAdministrasiList.indexOf(list);
                if ($scope.kriteriaAdministrasiList[centang].status == true) {
                    evaluasiVendor[centang].status = 1;
                } else {
                    evaluasiVendor[centang].status = 0;
                }

                var sumCheckTrue = 0;
                //console.log(JSON.stringify($scope.kriteriaAdministrasiList.length));
                for (var i = 0; i < $scope.kriteriaAdministrasiList.length; i++) {
                    if ($scope.kriteriaAdministrasiList[i].status) {
                        sumCheckTrue += 1;
                    }
                }

                if (sumCheckTrue == $scope.kriteriaAdministrasiList.length) {
                    $scope.chk1 = true;
                    $scope.chk2 = false;
                } else {
                    $scope.chk1 = false;
                    $scope.chk2 = true;;
                }
            };


            $scope.checkAll = function () {
                $scope.chk1 = true;
                $scope.chk2 = false;
                $scope.changeStatus(true);
            };

            $scope.uncheckAll = function () {
                $scope.chk1 = false;
                $scope.chk2 = true;
                $scope.changeStatus(false);
            };

            var nilaiEvaluasiVendor = {
                id: 0,
                vendorId: 0,
                pengadaanId: 0,
                nilaiEvaluasiAdministrasi: 0
            };

            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + eaec.pendaftaranVendorId + '/' + eaec.pengadaanId).success(function (data, status, headers, config) {
                $scope.nilaiEvaluasiVendorList = data;
                $scope.loading = false;
                if (data.length > 0) {
                    nilaiEvaluasiVendor.id = $scope.nilaiEvaluasiVendorList[0].id;
                    nilaiEvaluasiVendor.vendorId = eaec.pendaftaranVendorId;
                    nilaiEvaluasiVendor.pengadaanId = eaec.pengadaanId;
                    nilaiEvaluasiVendor.nilaiEvaluasiAdministrasi = $scope.nilaiEvaluasiVendorList[0].nilaiEvaluasiAdministrasi;
                }
            }).error(function (data, status, headers, config) {});
            /*------*/

            $scope.nilaiChecklist = function () {
                var checked = 0;
                for (var i = 0; i < $scope.kriteriaAdministrasiList.length; i++) {
                    if ($scope.kriteriaAdministrasiList[i].status == true) {
                        checked = checked + 1;
                    }
                }
                return (checked / $scope.kriteriaAdministrasiList.length) * 100;
            }
            nilaiEvaluasiVendor.nilaiEvaluasiAdministrasi = $scope.nilaiChecklist;
            $scope.ok = function () {
                var nilaiAdmin = 0;
                var statusCheckAdmin = 0;
                var vendorId = 0;
                for (var i = 0; i < evaluasiVendor.length; i++) {
                    if (evaluasiVendor[i].status == 1)
                        statusCheckAdmin++;
                    vendorId = evaluasiVendor[i].vendorId;
                    var uri = $rootScope.backendAddress + '/procurement/EvaluasiAdministrasiServices/';
                    var postFormEvaluasiVendorUpdate = {
                        evaluasiAdministrasiId: evaluasiVendor[i].evaluasiAdministrasiId,
                        vendorId: evaluasiVendor[i].vendorId,
                        kriteriaAdministrasiId: evaluasiVendor[i].kriteriaAdministrasiId,
                        status: evaluasiVendor[i].status
                    };
                    var postFormEvaluasiVendorCreate = {
                        vendorId: evaluasiVendor[i].vendorId,
                        kriteriaAdministrasiId: evaluasiVendor[i].kriteriaAdministrasiId,
                        status: evaluasiVendor[i].status
                    };
                    if (typeof evaluasiVendor[i].evaluasiAdministrasiId !== 'undefined') {
                        uri = uri + 'update';
                        eaecModal.postFormEvaluasiVendor = postFormEvaluasiVendorUpdate;
                    } else {
                        uri = uri + 'create';
                        eaecModal.postFormEvaluasiVendor = postFormEvaluasiVendorCreate;
                    }
                    if (evaluasiVendor.length > 0) {
                        $scope.loading = true;
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
                                data: eaecModal.postFormEvaluasiVendor
                            })
                            .success(function (data, status, headers, config) {
                                //$scope.messageSuccess = "Penyimpanan berhasil";
                                $scope.loading = false;
                            })
                            .error(function (data, status, headers, config) {
                                //$scope.messageError = "penyimpanan gagal";
                                $scope.loading = false;
                            });
                    }
                }

                nilaiAdmin = (statusCheckAdmin / evaluasiVendor.length) * 100;

                var postFormNilaiEvaluasiVendorUpdate = {
                    id: nilaiEvaluasiVendor.id,
                    vendorId: nilaiEvaluasiVendor.vendorId,
                    pengadaanId: nilaiEvaluasiVendor.pengadaanId,
                    nilaiEvaluasiAdministrasi: nilaiEvaluasiVendor.nilaiEvaluasiAdministrasi,
                };
                var postFormNilaiEvaluasiVendorCreate = {
                    id: 0,
                    vendorId: vendorId,
                    pengadaanId: eaec.pengadaanId,
                    nilaiEvaluasiAdministrasi: nilaiAdmin
                };
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/';
                if (nilaiEvaluasiVendor.id != 0) {
                    uri = uri + 'update';
                    eaecModal.postFormNilaiEvaluasiVendor = postFormNilaiEvaluasiVendorUpdate;
                    postFormNilaiEvaluasiVendorCreate.id = nilaiEvaluasiVendor.id;
                } else {
                    uri = uri + 'create';
                    eaecModal.postFormNilaiEvaluasiVendor = postFormNilaiEvaluasiVendorCreate;
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
                        data: postFormNilaiEvaluasiVendorCreate
                    })
                    .success(function (data, status, headers, config) {
                        $scope.messageModalSuccess = "Penyimpanan berhasil";
                        $scope.loading = false;

                        $modalInstance.dismiss('cancel');
                        $rootScope.listVendor();
                        $location.path('/app/promise/procurement/evaluasiAdministrasi/edit');
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        };
        ModalInstanceDetailCtrl.$inject = ['$scope', '$rootScope', '$modalInstance', '$resource'];
        /*------*/

        //choosemodal
        $scope.chooseModal = function (data, size) {
            if (eaec.detilPengadaan.sistemEvaluasiPenawaran.id == 1) {
                eaec.goModalKriteria(data, size);
            } else {
                $scope.goModalMeritPoint(data, size);
            }
        }

        /*modal Merit Point*/
        $scope.goModalMeritPoint = function (data, size) {
            $rootScope.kriteriaTeknisData = data;
            var modalInstance = $modal.open({
                templateUrl: '/EvaluasiAdminMeritPoint.html',
                controller: ModalInstanceEvaluasiAdminTotalMeritPoinCtrl,
                size: size
            });
        };
        
        var ModalInstanceEvaluasiAdminTotalMeritPoinCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, toaster) {
            var formModal = this;
            var evaluasiTeknis = [];

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            /* START Vendor */
            if (typeof $rootScope.kriteriaTeknisData !== 'undefined') {
                $scope.kriteriaTeknisData = $rootScope.kriteriaTeknisData.suratPenawaran.vendor;
            } else {
                console.log('INFO Error');
            }
            $scope.pengadaanId = $rootScope.detilPengadaan.id;
            formModal.vendorId = $scope.kriteriaTeknisData.id;
            /*---END Vendor----*/


            /* GET Evaluasi Teknik List*/
            $scope.evaluasiTeknisList = [];
            $scope.getEvaluasiTeknisList = function () {
                $scope.loading = true;
                $scope.watchTeknisList = false;
                $http.get($rootScope.backendAddress + '/procurement/EvaluasiAdministrasiMeritPoint/getEvaluasiAdministrasiMeritPointByVendorAndPengadaan/' + formModal.vendorId + '/' + $scope.pengadaanId, {
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
                            formModal.kriteriaAdministrasiMeritPointId = data[i].kriteriaAdministrasiMeritPoint.id;
                            formModal.id = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.nilai = data[i].nilai;
                            evaluasiTeknis.push(formModal);
                            $scope.total = $scope.total + data[i].kriteriaAdministrasiMeritPoint.nilai;
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
                    $http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointServices/getByPengadaanIdList/' + $scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        $scope.kriteriaTeknisByPengadaanList = data;
                        var i;
                        for (i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                            $scope.kriteriaTeknisByPengadaanList[i]["nilaiInput"] = 0;
                            formModal = new Object();
                            formModal.kriteriaAdministrasiMeritPointId = data[i].id;
                            formModal.vendorId = $scope.kriteriaTeknisData.id;
                            formModal.nilai = 0;
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
                        data.nilaiInput = 100;
                        alert("masukkan nilai antara 0-100");
                    }
                    evaluasiTeknis[kriteria].nilai = data.nilaiInput;
                    $scope.totalInput = new Number();
                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilaiInput);
                    }
                } else {
                    if (data.nilai > 100) {
                        data.nilai = 100;
                        alert("masukkan nilai antara 0-100");
                    }
                    evaluasiTeknis[kriteria].nilai = data.nilai;
                    $scope.totalInput = new Number();
                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilai);
                    }
                }

            }

            // -------------- Ada Perubahan Instruksi ---------------------------------------------------->
            //            $scope.accumulate = function (data) {
            //                var kriteria = $scope.kriteriaTeknisByPengadaanList.indexOf(data);
            //                if ($scope.watchTeknisList == false) {
            //                    if (data.nilaiInput > $scope.kriteriaTeknisByPengadaanList[kriteria].nilai) {
            //                        data.nilaiInput = $scope.kriteriaTeknisByPengadaanList[kriteria].nilai;
            //                        alert("nilai maks = " + $scope.kriteriaTeknisByPengadaanList[kriteria].nilai)
            //                    }
            //                    evaluasiTeknis[kriteria].nilai = data.nilaiInput;
            //                    $scope.totalInput = new Number();
            //                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
            //                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilaiInput);
            //                    }
            //                } else {
            //                    if (data.nilai > $scope.kriteriaTeknisByPengadaanList[kriteria].kriteriaAdministrasiMeritPoint.nilai) {
            //                        data.nilai = $scope.kriteriaTeknisByPengadaanList[kriteria].kriteriaAdministrasiMeritPoint.nilai;
            //                        alert("nilai maks = " + $scope.kriteriaTeknisByPengadaanList[kriteria].nilai);
            //                    }
            //                    evaluasiTeknis[kriteria].nilai = data.nilai;
            //                    $scope.totalInput = new Number();
            //                    for (var i = 0; i < $scope.kriteriaTeknisByPengadaanList.length; i++) {
            //                        $scope.totalInput = parseInt($scope.totalInput) + parseInt($scope.kriteriaTeknisByPengadaanList[i].nilai);
            //                    }
            //                }
            //
            //            }

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
                nilaiEvaluasiHarga: 0
            };

            /* GET Nilai Evaluasi Vendor*/
            $scope.getNilaiEvaluasiVendor = function (param) {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + formModal.vendorId + '/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    $scope.nilaiEvaluasiVendor = data;
                    if (data.length > 0) {
                        formNilaiEvaluasi.id = $scope.nilaiEvaluasiVendor[0].id;
                        formNilaiEvaluasi.nilaiEvaluasiHarga = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiHarga;
                        formNilaiEvaluasi.nilaiEvaluasiTeknis = $scope.nilaiEvaluasiVendor[0].nilaiEvaluasiTeknis;
                    }
                }).error(function (data, status, headers, config) {});
            }
            $scope.getNilaiEvaluasiVendor();
            /*---END Nilai Evaluasi Vendor---*/

            //            $scope.calculate = function () {
            //                return ($scope.total * $scope.totalInput) / 100;
            //            }

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
                        nilai1 = $scope.kriteriaTeknisByPengadaanList[i].kriteriaAdministrasiMeritPoint.nilai;
                        nilai2 = $scope.kriteriaTeknisByPengadaanList[i].nilai;
                    }
                    total = total + ((nilai1 * nilai2) / constant);
                }
                return total;
            }

            formNilaiEvaluasi.vendorId = formModal.vendorId;
            formNilaiEvaluasi.pengadaanId = $scope.pengadaanId;
            $scope.save = function () {
                $scope.loading = true;
                var ix;
                for (ix = 0; ix < evaluasiTeknis.length; ix++) {
                    //insert ke evaluasiteknistotal
                    var uri = $rootScope.backendAddress + '/procurement/EvaluasiAdministrasiMeritPoint/';
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
                        $scope.loading = false;
                    });
                }
                //SIMPAN nilai evaluasi Teknis
                formNilaiEvaluasi.nilaiEvaluasiAdministrasi = $scope.calculate();
                formNilaiEvaluasi.nilaiEvaluasiHarga = 0.0;
                var uri = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/';
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
                    toaster.pop('success', 'Evaluasi Administrasi', 'Simpan ' + 'data' + ', berhasil.');
                    $scope.loading = false;
                });
                $modalInstance.dismiss('cancel');
                $rootScope.listVendor();
                $location.path('/app/promise/procurement/evaluasiAdministrasi/edit');
            };

        };
        ModalInstanceEvaluasiAdminTotalMeritPoinCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'toaster'];
        /*------*/
    }


    EvaluasiAdministrasiCetakController.$inject = ['ModalService', '$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', 'ngTableParams', '$timeout', '$q'];

})();
