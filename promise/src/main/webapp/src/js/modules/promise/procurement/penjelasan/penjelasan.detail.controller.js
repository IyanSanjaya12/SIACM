/**=========================================================
 * Module: PenjelasanDetail.js
 * Modul/Tahapan ID: 8
 * Author: Reinhard
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenjelasanDetailController', PenjelasanDetailController);

    function PenjelasanDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, FileUploader, ngTableParams) {
        var penjelasanDetail = this;
        penjelasanDetail.penjelasanTahapId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;

        // set mode
        $scope.isEditable = $rootScope.isEditable;
        $scope.loading = true;
        $scope.loadingSaving = false;

        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/


        $scope.detilPengadaan = $rootScope.detilPengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;

        /*Start load*/
        loadBidangUsahaPengadaan();


        function loadBidangUsahaPengadaan() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                penjelasanDetail.subBidangUsahaByPengadaanIdList = data;
                penjelasanDetail.loading = false;
                loadKebutuhanMaterial();
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

        function loadKebutuhanMaterial() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var itemPengadaanMaterialByPengadaanIdList = data;
                penjelasanDetail.itemPengadaanMaterialByPengadaanIdList = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    total: itemPengadaanMaterialByPengadaanIdList.length,
                    getData: function ($defer, params) {
                        $defer.resolve(itemPengadaanMaterialByPengadaanIdList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
                loadKebutuhanJasa();
                penjelasanDetail.loading = false;
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }


        function loadKebutuhanJasa() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var itemPengadaanJasaByPengadaanIdList = data
                penjelasanDetail.itemPengadaanJasaByPengadaanIdList = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    total: itemPengadaanJasaByPengadaanIdList.length,
                    getData: function ($defer, params) {
                        $defer.resolve(itemPengadaanJasaByPengadaanIdList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
                loadJabatan();
                penjelasanDetail.loading = false;
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

        function loadJabatan() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
                .success(function (data, status, headers, config) {
                    $scope.jabatanList = data;
                    penjelasanDetail.loading = false;
                    loadPenjelasan();
                }).error(function (data, status, headers, config) {
                    penjelasanDetail.loading = false;
                });
        }


        /*-------------------------------------< LOAD EDITABLE MODEL >-------------------------------------------------*/

        /*Penjelasan, 
         * load list by pengadaan id, if empty then create new one */
        function loadPenjelasan() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/penjelasanServices/getListByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var penjelasanList = data;
                if (penjelasanList.length == 0) {
                    // if object penjelasan is empty create a new one;
                    penjelasanDetail.penjelasan = {};
                    penjelasanDetail.penjelasan.isOnline = 1;
                    penjelasanDetail.penjelasan.pengadaan = $scope.pengadaanId;
                } else {
                    penjelasanDetail.penjelasan = penjelasanList[0];
                }
                penjelasanDetail.loading = false;
                /*angular.forEach(penjelasanDetail.penjelasan, function(value, key) {
                    console.log("penjelasan = " + key + ': ' + value);
                });*/
                loadKehadiranVendor();
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }


        function loadKehadiranVendor() {
            penjelasanDetail.loading = true;
            /*merge suratPenawaranServices & vendorPenjelasanServices*/
            penjelasanDetail.kehadiranVendorListFromDB = {};
            penjelasanDetail.penjelasanVendorList = {};


            /*Get by surat Penawaran*/
            //$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + $scope.pengadaanId, { 

            /* updated*/
            /*Get by pendaftaran vendor*/
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                penjelasanDetail.penjelasanVendorList = data;
                $http.get($rootScope.backendAddress + '/procurement/penjelasan/vendorPenjelasanServices/getListByPengadaan/' + $scope.pengadaanId, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    penjelasanDetail.kehadiranVendorListFromDB = data;
                    // get and set kehadiran Vendor status from vendorPenjelasan
                    angular.forEach(penjelasanDetail.penjelasanVendorList, function (penjelasanVendor) {
                        penjelasanVendor.perwakilan = null;
                        penjelasanVendor.handphone = null;
                        penjelasanVendor.isHadirPenjelasan = false;
                        angular.forEach(penjelasanDetail.kehadiranVendorListFromDB, function (kehadiranVendor) {
                            if (penjelasanVendor.vendor.id == kehadiranVendor.vendor.id) {
                                penjelasanVendor.perwakilan = kehadiranVendor.perwakilan;
                                penjelasanVendor.handphone = kehadiranVendor.handphone;
                                penjelasanVendor.isHadirPenjelasan = true;
                            }
                        });
                    });
                    penjelasanDetail.loading = false;
                }).error(function (data, status, headers, config) {});
                loadKehadiranPanitia();
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

        function loadKehadiranPanitia() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/panitiaPenjelasanServices/getListByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                penjelasanDetail.panitiaPenjelasanList = data;
                penjelasanDetail.loading = false;
                loadDokumen();
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

        function loadDokumen() {
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/dokumenPenjelasanServices/getListByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                penjelasanDetail.dokumenPenjelasanList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < penjelasanDetail.dokumenPenjelasanList.length; y++) {
                    var file = {
                        name: penjelasanDetail.dokumenPenjelasanList[y].fileName,
                        size: penjelasanDetail.dokumenPenjelasanList[y].fileSize
                    };
                    uploader.addToQueue(file, undefined, undefined);
                    var item = uploader.queue[uploader.queue.length - 1];
                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                    /*added new attribute & properties to store realFileName*/
                    item.realFileName = penjelasanDetail.dokumenPenjelasanList[y].realFileName;
                }
                penjelasanDetail.loading = false;
                loadBeritaAcara();
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

        function loadBeritaAcara() {
            penjelasanDetail.loading = true;
            penjelasanDetail.beritaAcara = {};
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/beritaAcaraServices/getListByPengadaanAndTahapan/' + $scope.pengadaanId + '/' + penjelasanDetail.penjelasanTahapId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var beritaAcaraList = data;
                if (beritaAcaraList.length == 0) {
                    penjelasanDetail.beritaAcara.pengadaan = $scope.pengadaanId;
                    penjelasanDetail.beritaAcara.tahapan = penjelasanDetail.penjelasanTahapId;
                } else {
                    penjelasanDetail.beritaAcara = beritaAcaraList[0];
                }
                penjelasanDetail.loading = false;
                /*angular.forEach(penjelasanDetail.beritaAcara, function(value, key) {
                     console.log("beritaAcara = " + key + ': ' + value);
                 });
     */
            }).error(function (data, status, headers, config) {
                penjelasanDetail.loading = false;
            });
        }

      
        
        var uploader = $scope.uploader = new FileUploader({
            /* Masih hardcode */
            /*url: 'http://10.10.10.22:32770/promise/upload.php',*/
        	url : $rootScope.uploadBackendAddress,
            method: 'POST'
        });


        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
            fileItem.realFileName = response.fileName;
            /*alert('realFileName '+fileItem.realFileName); */
        };

        // Datepicker
        penjelasanDetail.toggleMin = function () {
            penjelasanDetail.minDate = penjelasanDetail.minDate ? null : new Date();
        };
        penjelasanDetail.toggleMin();
        penjelasanDetail.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        penjelasanDetail.format = penjelasanDetail.formats[4];

        penjelasanDetail.tanggalBeritaAcaraOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            penjelasanDetail.tanggalBeritaAcaraOpened = true;
        };


        /*-------------------------------------< FORM EVENTS >-------------------------------------------------*/

        /*tombol aksi tambah/hapus panitia*/
        penjelasanDetail.addPanitia = function () {
            penjelasanDetail.inserted = {
                id: penjelasanDetail.panitiaPenjelasanList.length + 1,
                isNew: true
            };
            penjelasanDetail.panitiaPenjelasanList.push(penjelasanDetail.inserted);
        };


        penjelasanDetail.removePanitia = function (index) {
            penjelasanDetail.panitiaPenjelasanList.splice(index, 1);
        };


        /*tombol aksi back to index*/
        $scope.gotoIndex = function () {
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/penjelasan');
                }
            } else {
                $location.path('/app/promise/procurement/penjelasan');
            }
        };
        
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }


        var isFormValid = function () {
            /*vendorlist*/
            var isValid = true;
            var keepGoing = true;
            var idx = 0;
            angular.forEach(penjelasanDetail.penjelasanVendorList, function (penjelasanVendor) {
                if (keepGoing) {
                    if (penjelasanVendor.isHadirPenjelasan) {
                        if (penjelasanVendor.perwakilan == null || (typeof penjelasanVendor.perwakilan == 'undefined') || penjelasanVendor.perwakilan.length <= 0) {
                            document.getElementsByName('penjelasanvendor_perwakilan')[idx].focus();
                            isValid = false;
                            keepGoing = false;
                        } else if (penjelasanVendor.handphone == null || (typeof penjelasanVendor.handphone == 'undefined') || penjelasanVendor.handphone.length <= 0) {
                            document.getElementsByName('penjelasanvendor_handphone')[idx].focus();
                            isValid = false;
                            keepGoing = false;
                        }
                        idx++;
                    }
                }
            })

            /*panitia*/
            keepGoing = true;
            idx = 0;
            if (isValid) {
                var idx = 0;
                angular.forEach(penjelasanDetail.panitiaPenjelasanList, function (penjelasanPanitia) {
                    if (keepGoing) {
                        if (penjelasanPanitia.nama == null || (typeof penjelasanPanitia.nama == 'undefined') || penjelasanPanitia.nama.length <= 0) {
                            document.getElementsByName('panitia_nama')[idx].focus();
                            isValid = false;
                            keepGoing = false;
                        } else if (penjelasanPanitia.jabatan == null || (typeof penjelasanPanitia.jabatan == 'undefined')) {
                            document.getElementsByName('panitia_jabatan')[idx].focus();
                            isValid = false;
                            keepGoing = false;
                        }
                        idx++;
                    }
                })
            }


            /*panitia*/
            if (isValid) {
                if (penjelasanDetail.beritaAcara.nomor == null || (typeof penjelasanDetail.beritaAcara.nomor == 'undefined')) {
                    isValid = false;
                    document.getElementsByName('beritaacara_nomor')[0].focus();
                } else if (penjelasanDetail.beritaAcara.tanggal == null || (typeof penjelasanDetail.beritaAcara.tanggal == 'undefined')) {
                    isValid = false;
                    document.getElementsByName('beritaacara_tanggal')[0].focus();
                }
            }

            return isValid;

        }

        $scope.saveData = function () {
            if (isFormValid()) {
                $scope.isEditable = false;
                savePenjelasan();
            } else {
                toaster.pop('warning', 'Kesalahan', 'Silahkan periksa kembali inputan anda!');
            }
            return false;
        }

        function savePenjelasan() {
            $scope.loadingSaving = true;

            /*insert penjelasan only when penjelasan.id is undefined*/
            if (typeof penjelasanDetail.penjelasan.id == 'undefined') {

                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/penjelasan/penjelasanServices/insert',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: penjelasanDetail.penjelasan
                }).success(function (data, status, headers, config) {
                    saveKehadiranVendor(data.id);
                })
            } else {
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/penjelasan/penjelasanServices/update',
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
                        id: penjelasanDetail.penjelasan.id,
                        pengadaan: penjelasanDetail.penjelasan.pengadaan.id,
                        isOnline: 1
                    }
                }).success(function (data, status, headers, config) {
                    saveKehadiranVendor(penjelasanDetail.penjelasan.id);
                })
            }
        }


        function saveKehadiranVendor(penjelasanId) {

            /*first, delete all by pengadaan*/
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/vendorPenjelasanServices/deleteRowByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: false
            }).success(function (data, status, headers, config) {

                /*insert new*/
                angular.forEach(penjelasanDetail.penjelasanVendorList, function (penjelasanVendor) {
                    if (penjelasanVendor.isHadirPenjelasan) {
                        var formPostPengadaan = {
                            penjelasan: penjelasanId,
                            vendor: penjelasanVendor.vendor.id,
                            perwakilan: penjelasanVendor.perwakilan,
                            handphone: penjelasanVendor.handphone
                        }
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/penjelasan/vendorPenjelasanServices/insert',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: formPostPengadaan
                        }).success(function (data, status, headers, config) {

                        })
                    }
                })
                saveKehadiranPanitia(penjelasanId);
            }).error(function (data, status, headers, config) {});

        }

        function saveKehadiranPanitia(penjelasanId) {

            /*first, delete all by pengadaan*/
            penjelasanDetail.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/panitiaPenjelasanServices/deleteRowByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: false
            }).success(function (data, status, headers, config) {

                /* insert new */
                angular.forEach(penjelasanDetail.panitiaPenjelasanList, function (penjelasanPanitia) {
                    penjelasanDetail.loading = false;
                    var formPostPengadaan = {
                        penjelasan: penjelasanId,
                        nama: penjelasanPanitia.nama,
                        jabatan: penjelasanPanitia.jabatan.id
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/penjelasan/panitiaPenjelasanServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formPostPengadaan
                    }).success(function (data, status, headers, config) {


                    })
                })
                saveDokumen(penjelasanId);
            }).error(function (data, status, headers, config) {});
        }



        function saveDokumen(penjelasanId) {

            /*first, delete all by pengadaan*/
            $http.get($rootScope.backendAddress + '/procurement/penjelasan/dokumenPenjelasanServices/deleteRowByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: false
            }).success(function (data, status, headers, config) {

                /* insert new */
                angular.forEach(uploader.queue, function (item) {
                    var formPostPengadaan = {
                        penjelasan: penjelasanId,
                        fileName: item.file.name,
                        realFileName: item.realFileName,
                        fileSize: 100000
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/penjelasan/dokumenPenjelasanServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formPostPengadaan
                    }).success(function (data, status, headers, config) {})
                })
                saveBeritaAcara();
            }).error(function (data, status, headers, config) {});
        }



        function saveBeritaAcara() {
            /*insert BeritaAcara only when penjelasan.BeritaAcara.id is undefined else update it*/

            /*convert tanggal berita acara in to text formatted*/
            penjelasanDetail.beritaAcara.tanggal = $filter('date')(penjelasanDetail.beritaAcara.tanggal, 'dd-MM-yyyy');

            var beritaAcaraAction = 'update';
            if (typeof penjelasanDetail.beritaAcara.id == 'undefined') {
                beritaAcaraAction = 'insert';
            }

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/beritaAcaraServices/' + beritaAcaraAction,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        var val = obj[p];

                        /*extract id value when val is object and !null*/
                        if ((typeof val) == 'object') {
                            if (val != null)
                                val = val['id'];
                        }

                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(val));
                    }
                    return str.join("&");
                },
                data: penjelasanDetail.beritaAcara
            }).success(function (data, status, headers, config) {
                $scope.showSuccess();
            })


        }

        $scope.showSuccess = function () {
            $scope.loadingSaving = false;
            toaster.pop('success', 'Sukses', 'Data penjelasan berhasil disimpan.');
            /*$scope.gotoIndex();*/
        }

    }

    PenjelasanDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'FileUploader', 'ngTableParams'];

})();
