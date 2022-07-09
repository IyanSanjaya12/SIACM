/**=========================================================
 * Module: DetilPerencanaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DetailPerencanaanController', DetailPerencanaanController);

    function DetailPerencanaanController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader) {

        var dpr = this;
        dpr.itemPerencanaanList = [];
        dpr.jasaPerencanaanList = [];
        dpr.itemPerencanaanDelete = [];
        dpr.jasaPerencanaanDelete = [];
        $scope.editDataMaterial = false;
        $scope.editDataJasa = false;

        $scope.LoadData = function () {
            if (typeof $rootScope.anggaranPerencanaan !== 'undefined') {
                //console.log('$rootScope.anggaranPerencanaan = ' + JSON.stringify($rootScope.anggaranPerencanaan));
                $scope.anggaranPerencanaan = $rootScope.anggaranPerencanaan;
                $scope.anggaranId = $scope.anggaranPerencanaan.id;
                $scope.nomorMemo = $scope.anggaranPerencanaan.nomorDraft;
                $scope.tanggalMemo = $scope.anggaranPerencanaan.tanggalDraft;
                $scope.tahunAnggaran = $scope.anggaranPerencanaan.tahunAnggaran;
                $scope.nomorDraft = $scope.anggaranPerencanaan.nomorDraft;
                $scope.tanggalDraft = $scope.anggaranPerencanaan.tanggalDraft;
                $scope.jumlah = $scope.anggaranPerencanaan.jumlah;
                $scope.sisaAnggaran = $scope.anggaranPerencanaan.sisaAnggaran;
                $scope.mataUang = $scope.anggaranPerencanaan.mataUang.kode;
                if ($scope.anggaranPerencanaan.sisaAnggaran != $scope.anggaranPerencanaan.jumlah) {
                    $scope.btnSimpanPerencanaanDisable = true;
                    $scope.editDataMaterial = true;
                    $scope.editDataJasa = true;
                }
            } else {
                console.log('INFO Error');
            }
        };
        $scope.LoadData();

        // Datepicker
        dpr.disabled = function (date, mode) {
            return false;
        };
        dpr.toggleMin = function () {
            dpr.minDate = dpr.minDate ? null : new Date();
        };
        dpr.toggleMin();
        dpr.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        dpr.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        dpr.format = dpr.formats[4];

        dpr.tanggalMemoOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            dpr.tanggalMemoOpened = true;
        };

        //Upload file
        /*$scope.btnSimpanDokPeralatanStatus = true;
        $scope.btnKembaliIndexStatus = false;*/
        if (typeof $rootScope.ppDokPekerjaan.uploader === 'undefined') {
            var uploader = $scope.uploader = new FileUploader({
                url: $rootScope.uploadBackendAddress,
                method: 'POST'
            });
        } else {
            var uploader = $scope.uploader = $rootScope.ppDokPekerjaan.uploader;
        }

        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item, options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS
        uploader.onWhenAddingFileFailed = function (item, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            //console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            //console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            //console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            //console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            //console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            fileItem.realFileName = response.fileName;
        };
        uploader.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        
        $scope.getListAnggaranPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/getListByAlokasiAnggaranId/' + $scope.anggaranId).success(
                function (data, status, headers, config) {
                    dpr.anggaranPerencanaan = data;
                    //console.log('iNFO ANGG =  ' + JSON.stringify(dpr.anggaranPerencanaan));
                    $scope.jumlahTotalHps = 0;
                    angular.forEach(dpr.anggaranPerencanaan, function (anggaranPerencanaan) {
                        dpr.anggaranPerencanaanId = anggaranPerencanaan.id;
                        //dokumen
                        $http.get($rootScope.backendAddress + '/procurement/perencanaan/dokumenPekerjaanServices/getByPerencanaan/' + anggaranPerencanaan.perencanaan.id).success(function (data, status, headers, config) {
                            dpr.dokumenPekerjaanList = data;
                            for (var y = 0; y < dpr.dokumenPekerjaanList.length; y++) {
                                $scope.dokumenId = dpr.dokumenPekerjaanList[y].id;
                                var file = {
                                    name: dpr.dokumenPekerjaanList[y].fileName,
                                    size: dpr.dokumenPekerjaanList[y].fileSize
                                };
                                uploader.addToQueue(file, undefined, undefined);
                                var item = uploader.queue[uploader.queue.length - 1];
                                item.isUploading = false;
                                item.isReady = false;
                                item.isSuccess = true;
                                item.isUploaded = true;
                                item.progress = 100;
                                item.realFileName = dpr.dokumenPekerjaanList[y].realFileName;
                            }


                            $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanServices/getPerencanaanId/' + anggaranPerencanaan.perencanaan.id).success(function (data, status, headers, config) {
                                dpr.perencanaan = data;
                                dpr.perencanaanDataId = dpr.perencanaan.id;


                                //Material
                                $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/getListByPerencanaanIdAndItemType/' + anggaranPerencanaan.perencanaan.id + '/1').success(
                                    function (data, status, headers, config) {
                                        dpr.itemPerencanaanList = data;
                                        $scope.itemPerencanaanList = dpr.itemPerencanaanList;
                                        $scope.jumlahTotalHPSMaterial = 0;
                                        for (var i = 0; i < $scope.itemPerencanaanList.length; i++) {
                                            $scope.itemPerencanaanList[i].index = i + 1;
                                            $scope.itemPerencanaanList[i].totalHPS = 0;
                                            $scope.itemPerencanaanList[i].totalHPS = $scope.itemPerencanaanList[i].nilai * $scope.itemPerencanaanList[i].jumlah;
                                            $scope.jumlahTotalHPSMaterial += $scope.itemPerencanaanList[i].totalHPS;
                                            if ($scope.itemPerencanaanList[i].sisah != $scope.itemPerencanaanList[i].jumlah) {
                                                //$scope.btnSimpanPerencanaan = false;
                                                //$scope.editDataMaterial = true;
                                            }
                                        }
                                        $scope.tableMaterialSession = new ngTableParams({
                                            page: 1,
                                            count: 5
                                        }, {
                                            total: $scope.itemPerencanaanList.length,
                                            getData: function ($defer, params) {
                                                $defer.resolve($scope.itemPerencanaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                            }
                                        });
                                        $scope.loading = false;
                                        //Jasa
                                        $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/getListByPerencanaanIdAndItemType/' + anggaranPerencanaan.perencanaan.id + '/2').success(
                                            function (data, status, headers, config) {
                                                dpr.jasaPerencanaanList = data;
                                                $scope.perencanaanJasa = dpr.jasaPerencanaanList;
                                                $scope.jumlahTotalHPSJasa = 0;
                                                for (var i = 0; i < $scope.perencanaanJasa.length; i++) {
                                                    $scope.perencanaanJasa[i].index = i + 1;
                                                    $scope.perencanaanJasa[i].totalHPS = 0;
                                                    $scope.perencanaanJasa[i].totalHPS = $scope.perencanaanJasa[i].nilai * $scope.perencanaanJasa[i].jumlah;
                                                    $scope.jumlahTotalHPSJasa += $scope.perencanaanJasa[i].totalHPS;
                                                    if ($scope.perencanaanJasa[i].sisah != $scope.perencanaanJasa[i].jumlah) {
                                                        //$scope.btnSimpanPerencanaan = false;
                                                        $scope.editDataJasa = true;
                                                    }

                                                }
                                                $scope.jumlahTotalHps = $scope.jumlahTotalHPSMaterial + $scope.jumlahTotalHPSJasa;
                                                //console.log('JUMLAH TOTAL HPS = ' + JSON.stringify($scope.jumlahTotalHps));
                                                $scope.tableJasaSession = new ngTableParams({
                                                    page: 1,
                                                    count: 5
                                                }, {
                                                    total: $scope.perencanaanJasa.length,
                                                    getData: function ($defer, params) {
                                                        $defer.resolve($scope.perencanaanJasa.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                                    }
                                                });
                                                $scope.loading = false;
                                            }).error(function (data, status, headers, config) {
                                            $scope.loading = false;
                                        });
                                    }).error(function (data, status, headers, config) {
                                    $scope.loading = false;
                                });
                            }).error(function (data, status, headers, config) {
                                $scope.loading = false;
                            });
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });
                    });
                }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
            $scope.loading = false;
        };
        $scope.getListAnggaranPengadaan();

        $scope.updateMaterialANdJasa = function () {
            if (dpr.itemPerencanaanList !== undefined && dpr.itemPerencanaanList !== null && dpr.itemPerencanaanList.length > 0) {
                $scope.jumlahTotalHPSMaterial = 0;
                for (var i = 0; i < dpr.itemPerencanaanList.length; i++) {
                    dpr.itemPerencanaanList[i].totalHPS = 0;
                    dpr.itemPerencanaanList[i].totalHPS = dpr.itemPerencanaanList[i].nilai * dpr.itemPerencanaanList[i].jumlah;
                    $scope.jumlahTotalHPSMaterial += dpr.itemPerencanaanList[i].totalHPS;
                    //console.log('TOTAL HPS = ' + JSON.stringify($rootScope.itemPerencanaanList));
                }
            }
            $scope.jumlahTotalHPSJasa = 0;
            if (dpr.jasaPerencanaanList !== undefined && dpr.jasaPerencanaanList !== null && dpr.jasaPerencanaanList.length > 0) {
                $scope.jumlahTotalHPSJasa = 0;
                for (var i = 0; i < dpr.jasaPerencanaanList.length; i++) {
                    dpr.jasaPerencanaanList[i].totalHPS = 0;
                    dpr.jasaPerencanaanList[i].totalHPS = dpr.jasaPerencanaanList[i].nilai * dpr.jasaPerencanaanList[i].jumlah;
                    $scope.jumlahTotalHPSJasa += dpr.jasaPerencanaanList[i].totalHPS;
                    //console.log('TOTAL HPS = ' + JSON.stringify($rootScope.itemPerencanaanList));
                }
            }

            $scope.jumlahTotalHps = $scope.jumlahTotalHPSMaterial + $scope.jumlahTotalHPSJasa;
            //console.log('JUMLAH TOTAL HPS = ' + JSON.stringify($scope.jumlahTotalHps));
        }

        //Add Material Perencanaan
        $scope.addMaterial = function (size) {
            var materialItemPerencanaan = {};
            var addModalItemMaterial = $modal.open({
                templateUrl: '/addMaterial.html',
                controller: 'ModalInstanceAddMaterialCtrl',
                size: 'lg',
                resolve: {
                    itemMaterialPerencanaan: function () {
                        return materialItemPerencanaan;
                    }
                }
            });
            addModalItemMaterial.result.then(function (itemMaterialPerencanaan) {
                if (itemMaterialPerencanaan != undefined && itemMaterialPerencanaan !== null) {
                    dpr.itemPerencanaanList.push(itemMaterialPerencanaan);
                    //console.log('HASIL PUSH = ' + JSON.stringify(dpr.itemPerencanaanList));
                    $scope.tableMaterialSession.reload();
                    $scope.updateMaterialANdJasa();
                }
            });
        };

        //Edit Material Perencanaan
        $scope.editMaterial = function (index, materialItemPerencanaan) {
            var editModalItemMaterial = $modal.open({
                templateUrl: '/addMaterial.html',
                controller: 'ModalInstanceAddMaterialCtrl',
                size: 'lg',
                resolve: {
                    itemMaterialPerencanaan: function () {
                        return materialItemPerencanaan;
                    }
                }
            });
            editModalItemMaterial.result.then(function (itemMaterialPerencanaan) {
                if (itemMaterialPerencanaan != undefined && itemMaterialPerencanaan !== null) {
                    dpr.itemPerencanaanList.splice(index, 1, itemMaterialPerencanaan);
                    $scope.tableMaterialSession.reload();
                    $scope.updateMaterialANdJasa();
                }
            });
        };

        //Material Delete
        $scope.removeMaterial = function (index, materialItemPerencanaan) {
            var modalDeleteMaterialInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: ModalMaterialInstanceCtrl,
                size: 'md',
                resolve: {
                    materialName: function () {
                        return materialItemPerencanaan.item.nama;
                    }
                }
            });
            modalDeleteMaterialInstance.result.then(function () {
                dpr.itemPerencanaanList.splice(index, 1);
                if (typeof materialItemPerencanaan.id !== undefined && materialItemPerencanaan.id !== null && materialItemPerencanaan.id > 0) {
                    if (typeof dpr.itemPerencanaanDelete === undefined || dpr.itemPerencanaanDelete === null) {
                        dpr.itemPerencanaanDelete = [];
                    }
                    dpr.itemPerencanaanDelete.push(materialItemPerencanaan);
                    //console.log('dpr.itemPerencanaanDelete = ' + JSON.stringify(dpr.itemPerencanaanDelete));
                }
                $scope.tableMaterialSession.reload();
                $scope.updateMaterialANdJasa();
            });
        };
        var ModalMaterialInstanceCtrl = function ($scope, $modalInstance, materialName) {
            $scope.materialName = materialName;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalMaterialInstanceCtrl.$inject = ['$scope', '$modalInstance', 'materialName'];



        //Add Jasa Perencanaan
        $scope.addJasa = function (size) {
            var jasaItemPerencanaan = {};
            var addModalItemJasa = $modal.open({
                templateUrl: '/addJasa.html',
                controller: 'ModalInstanceAddJasaCtrl',
                size: 'lg',
                resolve: {
                    itemJasaPerencanaan: function () {
                        return jasaItemPerencanaan;
                    }
                }
            });
            addModalItemJasa.result.then(function (itemJasaPerencanaan) {
                if (itemJasaPerencanaan != undefined && itemJasaPerencanaan !== null) {
                    dpr.jasaPerencanaanList.push(itemJasaPerencanaan);
                    //console.log('IJAL = ' + JSON.stringify(dpr.jasaPerencanaanList));
                    $scope.tableJasaSession.reload();
                    $scope.updateMaterialANdJasa();
                }
            });
        };

        //Edit Jasa
        $scope.editJasa = function (index, jasaItemPerencanaan) {
            var editModalItemJasa = $modal.open({
                templateUrl: '/addJasa.html',
                controller: 'ModalInstanceAddJasaCtrl',
                size: 'lg',
                resolve: {
                    itemJasaPerencanaan: function () {
                        return jasaItemPerencanaan;
                    }
                }
            });
            editModalItemJasa.result.then(function (itemJasaPerencanaan) {
                if (itemJasaPerencanaan != undefined && itemJasaPerencanaan !== null) {
                    dpr.jasaPerencanaanList.splice(index, 1, itemJasaPerencanaan);
                    $scope.tableJasaSession.reload();
                    $scope.updateMaterialANdJasa();
                }
            });
        };

        //Jasa Delete
        $scope.removeJasa = function (index, jasaItemPerencanaan) {
            var modalDeleteJasaInstance = $modal.open({
                templateUrl: '/alertTable1.html',
                controller: ModalJasaInstanceCtrl,
                size: 'md',
                resolve: {
                    jasaName: function () {
                        return jasaItemPerencanaan.item.nama;
                    }
                }
            });
            modalDeleteJasaInstance.result.then(function () {
                dpr.jasaPerencanaanList.splice(index, 1);
                if (typeof jasaItemPerencanaan.id !== undefined && jasaItemPerencanaan.id !== null && jasaItemPerencanaan.id > 0) {
                    if (typeof dpr.jasaPerencanaanDelete === undefined || dpr.jasaPerencanaanDelete === null) {
                        dpr.jasaPerencanaanDelete = [];
                    }
                    dpr.jasaPerencanaanDelete.push(jasaItemPerencanaan);
                }
                $scope.tableJasaSession.reload();
                $scope.updateMaterialANdJasa();
            });
        };
        var ModalJasaInstanceCtrl = function ($scope, $modalInstance, jasaName) {
            $scope.jasaName = jasaName;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalJasaInstanceCtrl.$inject = ['$scope', '$modalInstance', 'jasaName'];

        $scope.btnSimpanPerencanaan = function () {
            simpanPerencanaan();
        }

        function simpanPerencanaan() {
            $scope.tanggalMemo = $filter('date')($scope.tanggalMemo, 'dd-MM-yyyy');
            //angular.forEach(dpr.perencanaan, function (perencanaan, index) {
            var formPerencanaan = {
                id: dpr.perencanaanDataId,
                //nama: 'perencanaan 1',
                nomor: dpr.perencanaan.nomor,
                tanggalMemo: dpr.perencanaan.tanggalMemo
                    //keterangan: 'item dari perencanaan'
            }
            var uri = $rootScope.backendAddress + '/procurement/perencanaan/perencanaanServices/';
            if (typeof dpr.perencanaanDataId !== 'undefined' && dpr.perencanaanDataId > 0) {
                uri = uri + 'update';
                formPerencanaan;
            } else {
                uri = uri + 'insert';
                formPerencanaan;
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
                    data: formPerencanaan
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    simpanAnggaranPerencanaan(data.id);
                    $scope.btnSimpanPerencanaan = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            //})
        }

        function simpanAnggaranPerencanaan(perencanaanId) {
            //angular.forEach(dpr.anggaranPerencanaan, function (anggaranPerencanaan) {
            var formAnggaranPerencanaan = {
                id: dpr.anggaranPerencanaanId,
                alokasiAnggaran: $scope.anggaranId,
                perencanaan: perencanaanId
            }
            var uri = $rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/';
            if (typeof dpr.anggaranPerencanaanId != 'undefined' && dpr.anggaranPerencanaanId > 0) {
                uri = uri + 'update';
                formAnggaranPerencanaan;
            } else {
                uri = uri + 'insert';
                formAnggaranPerencanaan;
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
                    data: formAnggaranPerencanaan
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    simpanDokPekerjaan(data.perencanaan.id);
                    $scope.btnSimpanPerencanaan = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            //})
        }

        function simpanDokPekerjaan(perencanaanId) {
            angular.forEach(uploader.queue, function (item) {
                var formDokPekerjaan = {
                    id: $scope.dokumenId,
                    perencanaan: perencanaanId,
                    fileName: item.file.name,
                    realFileName: item.realFileName,
                    fileSize: 100000

                }
                var uri = $rootScope.backendAddress + '/procurement/perencanaan/dokumenPekerjaanServices/';
                if (typeof $scope.dokumenId !== 'undefined') {
                    uri = uri + 'update';
                    formDokPekerjaan;
                } else {
                    uri = uri + 'insert';
                    formDokPekerjaan;
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
                        data: formDokPekerjaan
                    })
                    .success(function (data, status, headers, config) {
                        $scope.loading = false;
                        simpanItemPerencanaan(data.perencanaan.id);
                        $scope.btnSimpanPerencanaan = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            })

        }


        function simpanItemPerencanaan(perencanaanId) {
            angular.forEach(dpr.itemPerencanaanList, function (itemPerencanaan) {
                var formMaterial = {
                    id: itemPerencanaan.id,
                    perencanaan: perencanaanId,
                    item: itemPerencanaan.item.id,
                    jumlah: itemPerencanaan.jumlah,
                    sisah: itemPerencanaan.sisah,
                    nilai: itemPerencanaan.nilai,
                    mataUang: itemPerencanaan.mataUang.id
                }
                if (typeof itemPerencanaan.id !== 'undefined' && itemPerencanaan.id > 0) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/update',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formMaterial
                    }).success(function (data, status, headers, config) {
                        console.log('Material Berhasil Diupdate');
                        toaster.pop('success', 'Item Material', 'Update ' + data.item.nama + ', berhasil.');
                        $scope.btnSimpanPerencanaan = false;
                    });
                } else if (typeof itemPerencanaan.id === 'undefined' || itemPerencanaan.id == null) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formMaterial
                    }).success(function (data, status, headers, config) {
                        console.log('Matrial Berhasil Disimpan');
                        toaster.pop('success', 'Item Material', 'Simpan ' + data.item.nama + ', berhasil.');
                        $scope.btnSimpanPerencanaan = false;
                    });
                }
            })
            angular.forEach(dpr.jasaPerencanaanList, function (jasaPerencanaan) {
                var formJasa = {
                    id: jasaPerencanaan.id,
                    perencanaan: perencanaanId,
                    item: jasaPerencanaan.item.id,
                    jumlah: jasaPerencanaan.jumlah,
                    sisah: jasaPerencanaan.sisah,
                    nilai: jasaPerencanaan.nilai,
                    mataUang: jasaPerencanaan.mataUang.id
                }
                if (jasaPerencanaan.id != 'undefined' && jasaPerencanaan.id > 0) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/update',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formJasa
                    }).success(function (data, status, headers, config) {
                        console.log('Jasa Berhasil Diupdate');
                        toaster.pop('success', 'Item Jasa', 'Update ' + data.item.nama + ', berhasil.');
                        $scope.btnSimpanPerencanaan = false;
                    });
                } else if (typeof jasaPerencanaan.id === 'undefined' && jasaPerencanaan.id == null) {
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formJasa
                    }).success(function (data, status, headers, config) {
                        console.log('Jasa Berhasil Disimpan');
                        toaster.pop('success', 'Item Jasa', 'Simpan ' + data.item.nama + ', berhasil.');
                        $scope.btnSimpanPerencanaan = false;
                    });
                }
            })

            // delete data material
            if (typeof dpr.itemPerencanaanDelete !== undefined && dpr.itemPerencanaanDelete !== null) {
                angular.forEach(dpr.itemPerencanaanDelete, function (deleteMaterial, index) {

                    $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/delete/' + deleteMaterial.id)
                        .success(function (data, status, headers, config) {
                            console.log('Material Berhasil Dihapus');
                        }).error(function (data, status, headers, config) {})

                });
            }

            // delete data Jasa
            if (typeof dpr.jasaPerencanaanDelete !== undefined && dpr.jasaPerencanaanDelete !== null) {
                angular.forEach(dpr.jasaPerencanaanDelete, function (deleteJasa, index) {

                    $http.get($rootScope.backendAddress + '/procurement/perencanaan/perencanaanItemServices/delete/' + deleteJasa.id)
                        .success(function (data, status, headers, config) {
                            console.log('Jasa Berhasil Dihapus');
                        }).error(function (data, status, headers, config) {})

                });
            }

        }

        $scope.showSuccess = function () {
            toaster.pop('success', 'Sukses', 'Data berhasil diubah.');
        }

        $scope.backToDetail = function () {
            $location.path('/app/promise/procurement/perencanaan/detail');
        };

        $scope.back = function () {
            $location.path('/app/promise/procurement/perencanaan');
        };

    }

    DetailPerencanaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader'];


})();

angular.module('naut')
    .controller('ModalInstanceAddMaterialCtrl', function ($rootScope, $scope, $http, ngTableParams, $modal, $filter, $modalInstance, itemMaterialPerencanaan) {
        var dpr = this;
        $rootScope.materialList = [];

        if (typeof itemMaterialPerencanaan !== undefined && itemMaterialPerencanaan !== null) {
            $scope.materialList = itemMaterialPerencanaan;
            $rootScope.materialList.push(itemMaterialPerencanaan);
        }

        if (typeof $rootScope.anggaranPerencanaan !== 'undefined') {
            $scope.anggaranPerencanaan = $rootScope.anggaranPerencanaan;
        } else {
            console.log('INFO Error');
        }
        $scope.panitia = $scope.anggaranPerencanaan.biroPengadaan.nama;
        $scope.tahunAnggaran = $scope.anggaranPerencanaan.tahunAnggaran;
        $scope.periodeAnggaran = $scope.anggaranPerencanaan.periodeAnggaran.nama;
        $scope.nomorDraft = $scope.anggaranPerencanaan.nomorDraft;
        $scope.tanggalDraft = $scope.anggaranPerencanaan.tanggalDraft;
        $scope.jumlah = $scope.anggaranPerencanaan.jumlah;
        $scope.mataUang = $scope.anggaranPerencanaan.mataUang.kode;

        $rootScope.LoadDataMaterial = function () {
            if (typeof $rootScope.materialList !== 'undefined') {
                $scope.materialList.item = $rootScope.materialList;
            }
        };

        $scope.copyJumlahKeSisah = function () {
            $scope.materialList.sisah = $scope.materialList.jumlah;
        }

        //Modal Cari Material
        $scope.cariMaterial = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/cariMaterial.html',
                controller: ModalInstanceCariMaterialCtrl,
                size: 'md'
            });
        };
        var ModalInstanceCariMaterialCtrl = function ($scope, $modalInstance) {
            $http.get($rootScope.backendAddress + '/procurement/master/item/get-by-item-type/1')
                .success(function (data, status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].index = i + 1;
                    }
                    $scope.tableMaterial = new ngTableParams({
                        page: 1, // show first page
                        count: 3 // count per page
                    }, {
                        total: data.length, // length of data4
                        getData: function ($defer, params) {
                            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                        }
                    });
                })

            //pencarian
            $scope.query = {}
            $scope.queryBy = '$'

            $scope.pilihMaterial = function (material) {
                $rootScope.materialList = material;
            };
            $scope.ok = function () {
                $scope.LoadDataMaterial();
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceCariMaterialCtrl.$inject = ['$scope', '$modalInstance'];


        /*$http.get($rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/getSingleByAlokasiAnggaranId/' + $rootScope.anggaranPerencanaan.id).success(function (data, status, headers, config) {
            $rootScope.anggaranPerencananData = data;
        }).error(function (data, status, headers, config) {})*/



        $scope.simpanKeSession = function () {
            if (typeof $rootScope.materialList === 'undefined' || $rootScope.materialList.length == 0) {
                alert('Material Harus dipilih');
            } else if (typeof $scope.materialList.jumlah === 'undefined' || $scope.materialList.jumlah == '') {
                alert('Jumlah Material Harus Diisi');
            } else if (typeof $scope.materialList.nilai === 'undefined' || $scope.materialList.nilai == '') {
                alert('Satuan Harus Diisi');
            } else {
                $scope.itemMaterialPerencanaan = {
                        id: $scope.materialList.id,
                        item: $scope.materialList.item,
                        //perencanaan: $rootScope.anggaranPerencananData.perencanaan,
                        mataUang: $rootScope.anggaranPerencanaan.mataUang,
                        jumlah: $scope.materialList.jumlah,
                        sisah: $scope.materialList.sisah,
                        nilai: $scope.materialList.nilai,
                        keterangan: $scope.materialList.keterangan
                    }
                    //console.log('TEST = ' + JSON.stringify($scope.itemMaterialPerencanaan));
                $modalInstance.close($scope.itemMaterialPerencanaan);
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });


angular.module('naut')
    .controller('ModalInstanceAddJasaCtrl', function ($rootScope, $scope, $http, ngTableParams, $modal, $filter, $modalInstance, itemJasaPerencanaan) {

        $scope.itemJasaPerencanaan = {};
        var dpr = this;
        $rootScope.jasaList = [];
        if (typeof itemJasaPerencanaan !== undefined && itemJasaPerencanaan !== null) {
            $scope.jasaList = itemJasaPerencanaan;
            $rootScope.jasaList.push(itemJasaPerencanaan);
        }
        if (typeof $rootScope.anggaranPerencanaan !== 'undefined') {
            $rootScope.anggaranPerencanaan = $scope.anggaranPerencanaan;
        } else {
            console.log('INFO Error');
        }
        $scope.panitia = $scope.anggaranPerencanaan.biroPengadaan.nama;
        $scope.tahunAnggaran = $scope.anggaranPerencanaan.tahunAnggaran;
        $scope.periodeAnggaran = $scope.anggaranPerencanaan.periodeAnggaran.nama;
        $scope.nomorDraft = $scope.anggaranPerencanaan.nomorDraft;
        $scope.tanggalDraft = $scope.anggaranPerencanaan.tanggalDraft;
        $scope.jumlah = $scope.anggaranPerencanaan.jumlah;
        $scope.mataUang = $scope.anggaranPerencanaan.mataUang.kode;

        $rootScope.LoadDataJasa = function () {
            if (typeof $rootScope.jasaList !== 'undefined') {
                $scope.jasaList.item = $rootScope.jasaList;
            }
        };

        //Modal Cari Jasa
        $scope.cariJasa = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/cariJasa.html',
                controller: ModalInstanceCariJasaCtrl,
                size: 'md'
            });
        };
        var ModalInstanceCariJasaCtrl = function ($scope, $modalInstance) {
            $http.get($rootScope.backendAddress + '/procurement/master/item/get-by-item-type/2')
                .success(function (data, status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].index = i + 1;
                    }
                    $scope.tableJasa = new ngTableParams({
                        page: 1, // show first page
                        count: 3 // count per page
                    }, {
                        total: data.length, // length of data4
                        getData: function ($defer, params) {
                            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                        }
                    });
                })

            //pencarian
            $scope.query = {}
            $scope.queryBy = '$'

            $scope.cariJasaList = [];
            $scope.loadCariJasa = function () {
                if (typeof $scope.cariJasaList[0] !== 'undefined') {
                    $scope.nama = $scope.cariJasaList[0].nama;
                    $scope.kode = $scope.cariJasaList[0].kode;
                    if (typeof $scope.cariJasaList[0].satuanId !== 'undefined') {
                        $scope.satuan.satuanId = $scope.cariJasaList[0].satuanId;
                    }
                }
            };


            $scope.satuan = {};
            $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data, status, headers, config) {
                $scope.satuanList = data;
            }).error(function (data, status, headers, config) {})

            $scope.simpanJasa = function () {
                if (typeof $scope.nama === 'undefined' || $scope.nama == '' || $scope.nama == null) {
                    alert('Nama Jasa Harus Diisi');
                } else if (typeof $scope.kode === 'undefined' || $scope.kode == '') {
                    alert('Kode Jasa Harus Diisi');
                } else if (typeof $scope.satuan.satuanId === 'undefined' || $scope.satuan.satuanId == '') {
                    alert('Satuan Harus Diisi');
                } else if (typeof $scope.cariJasaList[0] === 'undefined' || $scope.cariJasaList[0].lenght == 0) {
                    $scope.simpanJasaToTable();
                }
            };

            $scope.simpanJasaToTable = function () {
                var simpanItemKeTable = {
                    nama: $scope.nama,
                    kode: $scope.kode,
                    deskripsi: 'Item di tambah dari perencanaan',
                    satuanId: $scope.satuan.satuanId.id,
                    itemTypeId: 2,
                    itemGroupId: 2
                }
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/item/insert-item',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: simpanItemKeTable
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        $rootScope.jasaList = data;
                        console.log('$rootScope.jasaList = ' + JSON.stringify($rootScope.jasaList));
                        $rootScope.LoadDataJasa();
                        $modalInstance.close('closed');
                    }
                });
            };
            $scope.limit = 1;
            $scope.checked = 0;
            $scope.pilihJasa = function (jasa) {
                if (jasa.active) {
                    $scope.cariJasaList.push(jasa);
                    $rootScope.jasaList = jasa;
                    $scope.checked++;
                    console.log(JSON.stringify($scope.cariJasaList));
                    $scope.loadCariJasa();
                } else {
                    $scope.cariJasaList.splice($scope.cariJasaList.indexOf(jasa), 1);
                    console.log(JSON.stringify($scope.cariJasaList));
                    $scope.nama = '';
                    $scope.kode = '';
                    $scope.satuan.satuanId = '';
                    $scope.checked--;
                }
            };
            $scope.ok = function () {
                $scope.simpanJasa();
                $scope.LoadDataJasa();
                if (typeof $scope.cariJasaList[0] !== "undefined") {
                    $modalInstance.close('closed');
                }
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceCariJasaCtrl.$inject = ['$scope', '$modalInstance'];


        /*$http.get($rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/getSingleByAlokasiAnggaranId/' + $rootScope.anggaranPerencanaan.id).success(function (data, status, headers, config) {
            $rootScope.anggaranPerencananData = data;
        }).error(function (data, status, headers, config) {})*/


        $scope.simpanKeSession = function () {
            if (typeof $rootScope.jasaList === 'undefined' || $rootScope.jasaList.length == 0) {
                alert('Jasa Harus dipilih');
            } else if (typeof $scope.jasaList.jumlah === 'undefined' || $scope.jasaList.jumlah == '') {
                alert('Jumlah Jasa Harus Diisi');
            } else if (typeof $scope.jasaList.nilai === 'undefined' || $scope.jasaList.nilai == '') {
                alert('Satuan Harus Diisi');
            } else {
                $scope.itemJasaPerencanaan = {
                    id: $scope.jasaList.id,
                    item: $scope.jasaList.item,
                    //perencanaan: $rootScope.anggaranPerencananData.perencanaan,
                    mataUang: $rootScope.anggaranPerencanaan.mataUang,
                    jumlah: $scope.jasaList.jumlah,
                    sisah: $scope.jasaList.sisah,
                    nilai: $scope.jasaList.nilai,
                    keterangan: $scope.jasaList.keterangan
                }
                $modalInstance.close($scope.itemJasaPerencanaan);
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });