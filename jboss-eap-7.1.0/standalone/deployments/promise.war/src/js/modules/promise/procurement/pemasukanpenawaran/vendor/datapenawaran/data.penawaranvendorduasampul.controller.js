/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataPenawaranVendorDuaSampulController', DataPenawaranVendorDuaSampulController);

    function DataPenawaranVendorDuaSampulController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader) {
        var pptc = this;
        
        //GET VENDOR
        var getVendor = function () {
            $scope.loadingVendor = true;
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    $scope.vendor = data;
                    loadSuratPenawaran();
                    $scope.loadingVendor = false;
                })
                .error(function (data, status, headers, config) {

                    $scope.loadingVendor = false;
                });
        };
        getVendor();
        
        /* START Detail Pengadaan */
        if (typeof $rootScope.pengadaanVendor !== 'undefined') {
            $scope.pengadaanVendor = $rootScope.pengadaanVendor;
            //console.log(JSON.stringify($scope.pengadaanVendor));
        } else {
            console.log('INFO Error');
        }
        $scope.pengadaanId = $scope.pengadaanVendor.id;

        /*---END Detail Pengadaan----*/

        function loadSuratPenawaran() {
            pptc.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                var suratPenawaranList = data;
                if (suratPenawaranList.length == 0) {
                    pptc.suratPenawaran = {};
                } else {
                    pptc.suratPenawaran = suratPenawaranList[0];
                }
                pptc.loading = false;
                loadDokumenAdmin();

            }).error(function (data, status, headers, config) {
                pptc.loading = false;
            });
        }


        /* START Bidang Usaha Pengadaan*/
        $scope.getBidangPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pptc.subBidangUsahaByPengadaanIdList = data;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        $scope.getBidangPengadaan();
        /*---END Bidang Usaha Pengadaan---*/


        function loadDokumenAdmin() {
            pptc.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/getListByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                pptc.dokAdminList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < pptc.dokAdminList.length; y++) {
                    $scope.idAdmin = pptc.dokAdminList[y].id;
                    var fileAdmin = {                        
                        name: pptc.dokAdminList[y].file,
                        size: 10000 /*sesuaikan*/
                    };
                    uploader.addToQueue(fileAdmin, undefined, undefined);
                    var item = uploader.queue[uploader.queue.length - 1];

                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                }
                pptc.loading = false;
                //console.log('ID idADMIN = ' + JSON.stringify(pptc.dokAdminList));
                loadDokumenTeknis();
            }).error(function (data, status, headers, config) {
                pptc.loading = false;
            });
        }
        

        function loadDokumenTeknis() {
            pptc.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/getListByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                pptc.dokTeknisList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < pptc.dokTeknisList.length; y++) {
                    $scope.idTeknis = pptc.dokTeknisList[y].id;
                    var fileTeknis = {
                        name: pptc.dokTeknisList[y].file,
                        size: 10000 /*sesuaikan*/
                    };
                    uploader2.addToQueue(fileTeknis, undefined, undefined);
                    var item2 = uploader2.queue[uploader2.queue.length - 1];

                    item2.isUploading = false;
                    item2.isReady = false;
                    item2.isSuccess = true;
                    item2.isUploaded = true;
                    item2.progress = 100;
                }
                pptc.loading = false;
                loadDokumenDataPenawaran();
            }).error(function (data, status, headers, config) {
                pptc.loading = false;
            });
        }

        function loadDokumenDataPenawaran() {
            pptc.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/getListByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                pptc.dokPenawaranList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < pptc.dokPenawaranList.length; y++) {
                    $scope.idDokPenawaran = pptc.dokPenawaranList[y].id;
                    var filePenawaran = {
                        name: pptc.dokPenawaranList[y].file,
                        size: 10000 /*sesuaikan*/
                    };
                    uploader3.addToQueue(filePenawaran, undefined, undefined);
                    var item3 = uploader3.queue[uploader3.queue.length - 1];

                    item3.isUploading = false;
                    item3.isReady = false;
                    item3.isSuccess = true;
                    item3.isUploaded = true;
                    item3.progress = 100;
                }
                pptc.loading = false;
            }).error(function (data, status, headers, config) {
                pptc.loading = false;
            });
        }


        //----------------MATA UANG------------------------
        $scope.mataUang = {};
        $scope.getMataUang = function () {
            $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
                .success(function (data, status, headers, config) {
                    $scope.mataUangList = data;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getMataUang();
        //----------------END MATA UANG-------------------


        //Upload file
        $scope.btnSimpanDokAdminStatus = true;
        $scope.btnKembaliIndexStatus = false;
        if (typeof $rootScope.ppDokAdministrasi.uploader === 'undefined') {
            var uploader = $scope.uploader = new FileUploader({
                /* Masih hardcode */
                url: 'http://10.10.10.22:49183/promise/upload.php',
                /*url: $rootScope.uploadBackendAddress,*/
                method: 'POST'
            });
        } else {
            var uploader = $scope.uploader = $rootScope.ppDokAdministrasi.uploader;
        }

        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS
        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
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
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
            console.log('INFO File : ' + JSON.stringify(response));
            if (typeof $rootScope.ppDokAdministrasi.fileUpload === 'undefined') {
                console.info('00001');
                $rootScope.ppDokAdministrasi.fileUpload = [];
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokAdministrasi.fileUpload = $rootScope.ppDokAdministrasi.fileUpload.concat(fileObj);
                $rootScope.ppDokAdministrasi.uploader = $scope.uploader;
            } else {
                console.info('00002');
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokAdministrasi.fileUpload = $rootScope.ppDokAdministrasi.fileUpload.concat(fileObj);
                $scope.uploader = $rootScope.ppDokAdministrasi.uploader;
            }
        };
        uploader.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        /*-------------------------------------< method >-------------------------------------------------*/


        //Upload file Teknis
        $scope.btnSimpanDokTeknisStatus = true;
        $scope.btnKembaliIndexStatus = false;
        if (typeof $rootScope.ppDokTeknis.uploader2 === 'undefined') {
            var uploader2 = $scope.uploader2 = new FileUploader({
                /* Masih hardcode */
                url: 'http://10.10.10.22:49183/promise/upload.php',
                /*url: $rootScope.uploadBackendAddress,*/
                method: 'POST'
            });
        } else {
            var uploader2 = $scope.uploader2 = $rootScope.ppDokTeknis.uploader2;
        }

        // FILTERS
        uploader2.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS
        uploader2.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader2.onAfterAddingFile = function (fileItem) {
            //console.info('onAfterAddingFile', fileItem);
        };
        uploader2.onAfterAddingAll = function (addedFileItems) {
            //console.info('onAfterAddingAll', addedFileItems);
        };
        uploader2.onBeforeUploadItem = function (item) {
            //console.info('onBeforeUploadItem', item);
        };
        uploader2.onProgressItem = function (fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader2.onProgressAll = function (progress) {
            //console.info('onProgressAll', progress);
        };
        uploader2.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader2.onErrorItem = function (fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader2.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader2.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
            console.log('INFO File : ' + JSON.stringify(response));
            if (typeof $rootScope.ppDokTeknis.fileUpload === 'undefined') {
                console.info('00001');
                $rootScope.ppDokTeknis.fileUpload = [];
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokTeknis.fileUpload = $rootScope.ppDokTeknis.fileUpload.concat(fileObj);
                $rootScope.ppDokTeknis.uploader2 = $scope.uploader2;
            } else {
                console.info('00002');
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokTeknis.fileUpload = $rootScope.ppDokTeknis.fileUpload.concat(fileObj);
                $scope.uploader2 = $rootScope.ppDokTeknis.uploader2;
            }
        };
        uploader2.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        /*-------------------------------------< method >-------------------------------------------------*/


        //Upload file Data Penawaran
        $scope.btnSimpanDokPenawaranStatus = true;
        $scope.btnKembaliIndexStatus = false;
        if (typeof $rootScope.ppDokPenawaran.uploader3 === 'undefined') {
            var uploader3 = $scope.uploader3 = new FileUploader({
                /* Masih hardcode */
                url: 'http://10.10.10.22:49183/promise/upload.php',
                /*url: $rootScope.uploadBackendAddress,*/
                method: 'POST'
            });
        } else {
            var uploader3 = $scope.uploader3 = $rootScope.ppDokPenawaran.uploader3;
        }

        // FILTERS
        uploader3.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS
        uploader3.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader3.onAfterAddingFile = function (fileItem) {
            //console.info('onAfterAddingFile', fileItem);
        };
        uploader3.onAfterAddingAll = function (addedFileItems) {
            //console.info('onAfterAddingAll', addedFileItems);
        };
        uploader3.onBeforeUploadItem = function (item) {
            //console.info('onBeforeUploadItem', item);
        };
        uploader3.onProgressItem = function (fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader3.onProgressAll = function (progress) {
            //console.info('onProgressAll', progress);
        };
        uploader3.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader3.onErrorItem = function (fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader3.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader3.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
            console.log('INFO File : ' + JSON.stringify(response));
            if (typeof $rootScope.ppDokPenawaran.fileUpload === 'undefined') {
                console.info('00001');
                $rootScope.ppDokPenawaran.fileUpload = [];
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokPenawaran.fileUpload = $rootScope.ppDokPenawaran.fileUpload.concat(fileObj);
                $rootScope.ppDokPenawaran.uploader3 = $scope.uploader3;
            } else {
                console.info('00002');
                var fileObj = {
                    fileName: response.fileName
                };
                $rootScope.ppDokPenawaran.fileUpload = $rootScope.ppDokPenawaran.fileUpload.concat(fileObj);
                $scope.uploader3 = $rootScope.ppDokPenawaran.uploader3;
            }
        };
        uploader3.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        /*-------------------------------------< method >-------------------------------------------------*/

        pptc.back = function () {
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor');
        };

        /*------*/


        // Datepicker Tanggal Syrat Penawaran
        pptc.disabled = function (date, mode) {
            return false; // ( mode === 'day' && ( date.getDay() === 0 ||
            // date.getDay() === 6 ) );
        };
        pptc.toggleMin = function () {
            pptc.minDate = pptc.minDate ? null : new Date();
        };
        pptc.toggleMin();
        pptc.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        pptc.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        pptc.format = pptc.formats[4];

        pptc.tglSuratPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            pptc.tglSuratPenawaranOpened = true;
        };

        pptc.tglBtsPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            pptc.tglBtsPenawaranOpened = true;
        };

        pptc.tglBtsJaminanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            pptc.tglBtsJaminanOpened = true;
        };


        pptc.btnSimpanPenawaran = function () {

            /*$scope.loading = true;
            $scope.message = ('Proses penyimpanan data Penawaran...');*/
            //           
            if (typeof pptc.suratPenawaran.nomor === 'undefined' || pptc.suratPenawaran.nomor == '') {
                //alert("Nama Nomor Surat Penawaran belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Nomor Surat Penawaran belum disi..!');
            } else if (typeof pptc.suratPenawaran.tanggalAwal === 'undefined' || pptc.suratPenawaran.tanggalAwal == '') {
                //alert("Tanggal Surat Penawaran belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Tanggal Surat Penawaran belum disi...');
            } else if (typeof pptc.suratPenawaran.tanggalBatas === 'undefined' || pptc.suratPenawaran.tanggalBatas == '') {
                // alert("Tanggal Batas Surat Penawaran belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Tanggal Batas Surat Penawaran belum disi...');
            } else if (typeof pptc.suratPenawaran.namaJaminan === 'undefined' || pptc.suratPenawaran.namaJaminan == '') {
                //alert("Nama Jaminan belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Nama Jaminan belum disi...');
            } else if (typeof pptc.suratPenawaran.mataUang === 'undefined' || pptc.suratPenawaran.mataUang == '') {
                // alert("Tanggal Batas Surat Penawaran belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Mata Uang belum disi...');
            } else if (typeof pptc.suratPenawaran.nilaiJaminan === 'undefined' || pptc.suratPenawaran.nilaiJaminan == '') {
                //alert("Nilai Jaminan belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Nilai Jaminan belum disi...');
            } else if (typeof pptc.suratPenawaran.tanggalBatasJaminan === 'undefined' || pptc.suratPenawaran.tanggalBatasJaminan == '') {
                //alert("Tanggal Batas Jaminan belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Tanggal Batas Jaminan belum disi..');
            } else {
                $scope.loading = true;
                simpanSuratPenawaran();
            }

        }


        function simpanSuratPenawaran() {
            pptc.suratPenawaran.tanggalAwal = $filter('date')(pptc.suratPenawaran.tanggalAwal, 'dd-MM-yyyy');
            pptc.suratPenawaran.tanggalBatas = $filter('date')(pptc.suratPenawaran.tanggalBatas, 'dd-MM-yyyy');
            pptc.suratPenawaran.tanggalBatasJaminan = $filter('date')(pptc.suratPenawaran.tanggalBatasJaminan, 'dd-MM-yyyy');
            var formSuratPenawaran = {
                id: pptc.suratPenawaran.id,
                pengadaan: $scope.pengadaanId,
                vendor: $scope.vendor.id,
                nomor: pptc.suratPenawaran.nomor,
                tanggalAwal: pptc.suratPenawaran.tanggalAwal,
                tanggalBatas: pptc.suratPenawaran.tanggalBatas,
                namaJaminan: pptc.suratPenawaran.namaJaminan,
                mataUang: pptc.suratPenawaran.mataUang.id,
                nilaiJaminan: pptc.suratPenawaran.nilaiJaminan,
                tanggalBatasJaminan: pptc.suratPenawaran.tanggalBatasJaminan,
            };
            var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/';
            if (typeof pptc.suratPenawaran.id !== 'undefined') {
                uri = uri + 'updateSuratPenawaran';
                formSuratPenawaran;
            } else {
                uri = uri + 'insertSuratPenawaran';
                formSuratPenawaran;
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
                    data: formSuratPenawaran
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    pptc.btnSimpanPenawaran = false;
                    simpanDokAdmin(data.id);
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
        }


        function simpanDokAdmin(suratPenawaranId) {
            angular.forEach(uploader.queue, function (item) {
                var formDokAdministrasi = {
                    id: $scope.idAdmin,
                    suratPenawaran: suratPenawaranId,
                    file: item.file.name,

                }
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/';
                if (typeof $scope.idAdmin !== 'undefined') {
                    uri = uri + 'update';
                    formDokAdministrasi;
                } else {
                    uri = uri + 'insert';
                    formDokAdministrasi;
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
                        data: formDokAdministrasi
                    })
                    .success(function (data, status, headers, config) {
                        $scope.loading = false;
                        simpanDokTeknis(suratPenawaranId)
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            })

        }

        function simpanDokTeknis(suratPenawaranId) {
            angular.forEach(uploader2.queue, function (item2) {
                var formDokTeknis = {
                    id: $scope.idTeknis,
                    suratPenawaran: suratPenawaranId,
                    file: item2.file.name,

                }
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/';
                if (typeof $scope.idTeknis !== 'undefined') {
                    uri = uri + 'update';
                    formDokTeknis;
                } else {
                    uri = uri + 'insert';
                    formDokTeknis;
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
                        data: formDokTeknis
                    })
                    .success(function (data, status, headers, config) {
                        $scope.loading = false;
                        simpanDokDataPenawaran(suratPenawaranId)
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            })
        }

        function simpanDokDataPenawaran(suratPenawaranId) {
            angular.forEach(uploader3.queue, function (item3) {
                var formDokPenawaran = {
                    id: $scope.idDokPenawaran,
                    suratPenawaran: suratPenawaranId,
                    file: item3.file.name,

                }
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/';
                if (typeof $scope.idDokPenawaran !== 'undefined') {
                    uri = uri + 'update';
                    formDokPenawaran;
                } else {
                    uri = uri + 'insert';
                    formDokPenawaran;
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
                        data: formDokPenawaran
                    })
                    .success(function (data, status, headers, config) {
                        $scope.loading = false;
                        pptc.btnSimpanPenawaran = false;
                        if (typeof data !== 'undefined') {
                            $scope.showSuccess();

                        }
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            })
        }



        $scope.showSuccess = function () {
            $scope.loading = false;
            toaster.pop('success', 'Sukses', 'Data surat penawaran berhasil disimpan.');
            $scope.loading = true;
            $scope.konfirmasi();
            /*alert('Proses selenjutnya');
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor');*/
        }
        
        
        $scope.konfirmasi = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/confirm.html',
                controller: ModalKonfirmasiCtrl,
                size: size
            });
            modalInstance.result.then(function () { //ok
                
            }, function () {});
        };
        var ModalKonfirmasiCtrl = function ($scope, $modalInstance) {
            $scope.ok = function () {
                $location.path('/appvendor/promise/procurement/penawaranharga/vendor');
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalKonfirmasiCtrl.$inject = ['$scope', '$modalInstance'];
        


    }



    DataPenawaranVendorDuaSampulController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader'];


})();