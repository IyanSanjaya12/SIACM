/**=========================================================
 * Module: DetilPerencanaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DetailAddPerencanaanController', DetailAddPerencanaanController);

    function DetailAddPerencanaanController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader) {

        var dpr = this;
        dpr.itemPerencanaanList = [];
        dpr.jasaPerencanaanList = [];
        dpr.itemPerencanaanDelete = [];
        dpr.jasaPerencanaanDelete = [];

        $scope.LoadData = function () {
            if (typeof $rootScope.anggaranPerencanaan !== 'undefined') {
                $rootScope.anggaranPerencanaan = $scope.anggaranPerencanaan;
                $scope.anggaranId = $scope.anggaranPerencanaan.alokasiAnggaran.id;
                $scope.nomorMemo = $scope.anggaranPerencanaan.alokasiAnggaran.nomorDraft;
                $scope.tanggalMemo = $scope.anggaranPerencanaan.alokasiAnggaran.tanggalDraft;
                $scope.tahunAnggaran = $scope.anggaranPerencanaan.alokasiAnggaran.tahunAnggaran;
                $scope.nomorDraft = $scope.anggaranPerencanaan.alokasiAnggaran.nomorDraft;
                $scope.tanggalDraft = $scope.anggaranPerencanaan.alokasiAnggaran.tanggalDraft;
                $scope.jumlah = $scope.anggaranPerencanaan.alokasiAnggaran.jumlah;
                $scope.sisaAnggaran = $scope.anggaranPerencanaan.alokasiAnggaran.sisaAnggaran;
                $scope.mataUang = $scope.anggaranPerencanaan.alokasiAnggaran.mataUang.kode;
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
        
        $scope.loadData = function (){
            if (typeof dpr.itemPerencanaanList === 'undefined'){
                dpr.itemPerencanaanList=[];
                $scope.itemPerencanaanList=[];
            }else{
                //console.log('dpr.itemPerencanaanList = ' + JSON.stringify(dpr.itemPerencanaanList));
                $scope.itemPerencanaanList = dpr.itemPerencanaanList;
                $scope.jumlahTotalHPSMaterial = 0;
                for (var i = 0; i < $scope.itemPerencanaanList.length; i++) {
                    $scope.itemPerencanaanList[i].index = i + 1;
                    $scope.itemPerencanaanList[i].totalHPS = 0;
                    $scope.itemPerencanaanList[i].totalHPS = $scope.itemPerencanaanList[i].nilai * $scope.itemPerencanaanList[i].jumlah;
                    $scope.jumlahTotalHPSMaterial += $scope.itemPerencanaanList[i].totalHPS;
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
            }
            
            //jasa
            if (typeof dpr.jasaPerencanaanList === 'undefined'){
                dpr.jasaPerencanaanList=[];
                $scope.jasaPerencanaanList=[];
            }else{
                //console.log('dpr.jasaPerencanaanList = ' + JSON.stringify(dpr.jasaPerencanaanList));
                $scope.jasaPerencanaanList = dpr.jasaPerencanaanList;
                $scope.jumlahTotalHPSMaterial = 0;
                for (var i = 0; i < $scope.jasaPerencanaanList.length; i++) {
                    $scope.jasaPerencanaanList[i].index = i + 1;
                    $scope.jasaPerencanaanList[i].totalHPS = 0;
                    $scope.jasaPerencanaanList[i].totalHPS = $scope.jasaPerencanaanList[i].nilai * $scope.jasaPerencanaanList[i].jumlah;
                    $scope.jumlahTotalHPSMaterial += $scope.jasaPerencanaanList[i].totalHPS;
                }
                $scope.tableJasaSession = new ngTableParams({
                    page: 1,
                    count: 5
                }, {
                    total: $scope.jasaPerencanaanList.length,
                    getData: function ($defer, params) {
                        $defer.resolve($scope.jasaPerencanaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
            }
        };
        $scope.loadData();


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
                if (itemMaterialPerencanaan != 'undefined' && itemMaterialPerencanaan !== null) {
                    dpr.itemPerencanaanList.push(itemMaterialPerencanaan);
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
            if (typeof uploader.queue==='undefined' || uploader.queue.length==0){
                toaster.pop('warning', 'upload dokumen masih kosong', 'Simpan ', ', Warning.');
            }else{
                simpanPerencanaan();
            }
        }

        function simpanPerencanaan() {
            $scope.tanggalMemo = $filter('date')($scope.tanggalMemo, 'dd-MM-yyyy');
            var formPerencanaan = {
                //nama: 'perencanaan 1',
                nomor: $scope.nomorMemo,
                tanggalMemo: $scope.tanggalMemo
                //keterangan: 'item dari perencanaan'
            }
            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/perencanaan/perencanaanServices/insert',
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
        }

        function simpanAnggaranPerencanaan(perencanaanId) {
                var formAnggaranPerencanaan = {
                    alokasiAnggaran: $scope.anggaranId,
                    perencanaan: perencanaanId
                }
                $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/anggaranPerencanaanServices/insert',
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
        }

        function simpanDokPekerjaan(perencanaanId) {
            angular.forEach(uploader.queue, function (item) {
                var formDokPekerjaan = {
                    perencanaan: perencanaanId,
                    fileName: item.file.name,
                    realFileName: item.realFileName,
                    fileSize: 100000

                }
                $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/perencanaan/dokumenPekerjaanServices/insert',
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
                console.log(">> : "+JSON.stringify(itemPerencanaan));
                if(typeof itemPerencanaan.mataUang == 'undefined'){
                     itemPerencanaan.mataUang = {};
                    itemPerencanaan.mataUang.id = 1; //Rupiah
                }
                   
                var formMaterial = {
                    perencanaan: perencanaanId,
                    item: itemPerencanaan.item.id,
                    jumlah: itemPerencanaan.jumlah,
                    sisah: itemPerencanaan.sisah,
                    nilai: itemPerencanaan.nilai,
                    mataUang: itemPerencanaan.mataUang.id
                }
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
            })
            angular.forEach(dpr.jasaPerencanaanList, function (jasaPerencanaan) {
                var formJasa = {
                    perencanaan: perencanaanId,
                    item: jasaPerencanaan.item.id,
                    jumlah: jasaPerencanaan.jumlah,
                    sisah: jasaPerencanaan.sisah,
                    nilai: jasaPerencanaan.nilai,
                    mataUang: jasaPerencanaan.mataUang.id
                }
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
            })

        }
        
        $scope.showSuccess = function () {
			$scope.loading = false;
			toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
		}

        $scope.backToDetail = function () {
            $location.path('/app/promise/procurement/perencanaan/detail');
        };

        $scope.back = function () {
            $location.path('/app/promise/procurement/perencanaan');
        };

    }

    DetailAddPerencanaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader'];


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
        $scope.panitia = $scope.anggaranPerencanaan.alokasiAnggaran.biroPengadaan.nama;
        $scope.tahunAnggaran = $scope.anggaranPerencanaan.alokasiAnggaran.tahunAnggaran;
        /*$scope.periodeAnggaran = $scope.anggaranPerencanaan.periodeAnggaran.nama;*/
        $scope.nomorDraft = $scope.anggaranPerencanaan.alokasiAnggaran.nomorDraft;
        $scope.tanggalDraft = $scope.anggaranPerencanaan.alokasiAnggaran.tanggalDraft;
        $scope.jumlah = $scope.anggaranPerencanaan.alokasiAnggaran.jumlah;
        $scope.mataUang = $scope.anggaranPerencanaan.alokasiAnggaran.mataUang.kode;

        $rootScope.LoadDataMaterial = function () {
            if (typeof $rootScope.materialList !== 'undefined') {
                $scope.materialList.item = $rootScope.materialList;
                //console.log('$rootScope.materialList = ' + JSON.stringify($rootScope.materialList));
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


        $scope.simpanKeSession = function () {
            if (typeof $scope.materialList.item === 'undefined' || $scope.materialList.item.length == 0) {
                alert('Material Harus dipilih');
            } else if (typeof $scope.materialList.jumlah === 'undefined' || $scope.materialList.jumlah == '') {
                alert('Jumlah Material Harus Diisi');
            } else if (typeof $scope.materialList.nilai === 'undefined' || $scope.materialList.nilai == '') {
                alert('Satuan Harus Diisi');
            } else {
                $scope.itemMaterialPerencanaan = {
                        id: $scope.materialList.id,
                        item: $scope.materialList.item,
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
        /*$scope.periodeAnggaran = $scope.anggaranPerencanaan.periodeAnggaran.nama;*/
        $scope.nomorDraft = $scope.anggaranPerencanaan.alokasiAnggaran.nomorDraft;
        $scope.tanggalDraft = $scope.anggaranPerencanaan.alokasiAnggaran.tanggalDraft;
        $scope.jumlah = $scope.anggaranPerencanaan.jumlah;
        $scope.mataUang = $scope.anggaranPerencanaan.mataUang.kode;

        $rootScope.LoadDataJasa = function () {
            if (typeof $rootScope.jasaList !== 'undefined') {
                $scope.jasaList.item = $rootScope.jasaList;
            }
        };
    
        $scope.copyJumlahKeSisah = function () {
            $scope.jasaList.sisah = $scope.jasaList.jumlah;
        }

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
    
        

        $scope.simpanKeSession = function () {
            if (typeof $scope.jasaList.item === 'undefined' || $scope.jasaList.item.length == 0) {
                alert('Jasa Harus dipilih');
            } else if (typeof $scope.jasaList.jumlah === 'undefined' || $scope.jasaList.jumlah == '') {
                alert('Jumlah Jasa Harus Diisi');
            } else if (typeof $scope.jasaList.nilai === 'undefined' || $scope.jasaList.nilai == '') {
                alert('Satuan Harus Diisi');
            } else {
                $scope.itemJasaPerencanaan = {
                    id: $scope.jasaList.id,
                    item: $scope.jasaList.item,
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