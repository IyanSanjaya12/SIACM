/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataPenawaranVendorSatuSampulController', DataPenawaranVendorSatuSampulController);

    function DataPenawaranVendorSatuSampulController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader,RequestService) {
        var pptc = this;
        
        if (typeof $rootScope.pengadaanVendor !== 'undefined') {
            $scope.pengadaanVendor = $rootScope.pengadaanVendor;
            $scope.pengadaanVendorTemp = $rootScope.pengadaanVendor;
            //console.log(JSON.stringify($scope.pengadaanVendor));
        } else {
            //console.log('INFO Error');
        }
        $scope.pengadaanId = $scope.pengadaanVendor.id;
        
        //uploader admin
        $scope.btnSimpanDokAdminStatus = true;
        $scope.btnKembaliIndexStatus = false;
        var uploader = $scope.uploader = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
            });
        
      //uploader Teknis
        $scope.btnSimpanDokTeknisStatus = true;
        $scope.btnKembaliIndexStatus = false;
        var uploader2 = $scope.uploader2 = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
       
        
      //uploader Penawaran
        $scope.btnSimpanDokPenawaranStatus = true;
        $scope.btnKembaliIndexStatus = false;
        var uploader3 = $scope.uploader3 = new FileUploader({
            /* Masih hardcode */
            url: $rootScope.uploadBackendAddress,
            /*url: $rootScope.uploadBackendAddress,*/
            method: 'POST'
        });
        
        $scope.ppDokAdministrasi = {};
        $scope.ppDokAdministrasi.fileUpload = [];
        $scope.ppDokTeknis = {};
        $scope.ppDokTeknis.fileUpload = [];
        $scope.ppDokPenawaran = {};
        $scope.ppDokPenawaran.fileUpload = [];

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
                for (var y = 0; y < pptc.dokAdminList.length; y++) {
                    $scope.idAdmin = pptc.dokAdminList[y].id;
                    var fileAdmin = {
                        fileName: pptc.dokAdminList[y].fileName,
                        realFileName: pptc.dokAdminList[y].realFileName,
                        name: pptc.dokAdminList[y].fileName,
                        size: 10000 /*sesuaikan*/
                    };
                    uploader.addToQueue(fileAdmin, undefined, undefined);
                    var item = uploader.queue[uploader.queue.length - 1];

                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;

                    //push ke object sebelumnya
                    var fileObj = {
                        dokAdminId: pptc.dokAdminList[y].id,
                        fileName: pptc.dokAdminList[y].fileName,
                        realFileName: pptc.dokAdminList[y].realFileName
                    };
                    $scope.ppDokAdministrasi.fileUpload.push(fileObj);
                }
                $scope.ppDokAdministrasi.uploader = $scope.uploader;
                pptc.loading = false;
                $scope.uploader.progress = 100;
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
                        fileName: pptc.dokTeknisList[y].fileName,
                        realFileName: pptc.dokTeknisList[y].realFileName,
                        name: pptc.dokTeknisList[y].fileName,
                        size: 10000 /*sesuaikan*/
                        
                    };
                    uploader2.addToQueue(fileTeknis, undefined, undefined);
                    var item2 = uploader2.queue[uploader2.queue.length - 1];

                    item2.isUploading = false;
                    item2.isReady = false;
                    item2.isSuccess = true;
                    item2.isUploaded = true;
                    item2.progress = 100;

                    //push ke object sebelumnya
                    var fileObj = {
                        dokTeknisId: pptc.dokTeknisList[y].id,
                        fileName: pptc.dokTeknisList[y].fileName,
                        realFileName: pptc.dokTeknisList[y].realFileName
                    };
                    $scope.ppDokTeknis.fileUpload.push(fileObj);
                }
                $scope.ppDokTeknis.uploader2 = $scope.uploader2;
                pptc.loading = false;
                $scope.uploader2.progress = 100;
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
                
                for (var y = 0; y < pptc.dokPenawaranList.length; y++) {
                    $scope.idDokPenawaran = pptc.dokPenawaranList[y].id;
                    var filePenawaran = {
                    	realFileName: pptc.dokPenawaranList[y].realFileName,
                        fileName: pptc.dokPenawaranList[y].fileName,
                        name: pptc.dokPenawaranList[y].fileName,
                        size: 10000 /*sesuaikan*/
                        
                    };
                    uploader3.addToQueue(filePenawaran, undefined, undefined);
                    var item3 = uploader3.queue[uploader3.queue.length - 1];

                    item3.isUploading = false;
                    item3.isReady = false;
                    item3.isSuccess = true;
                    item3.isUploaded = true;
                    item3.progress = 100;

                    //push ke object sebelumnya
                    var fileObj = {
                        dokPenawaranId: pptc.dokPenawaranList[y].id,
                        fileName: pptc.dokPenawaranList[y].fileName,
                        realFileName: pptc.dokPenawaranList[y].realFileName
                    };
                    $scope.ppDokPenawaran.fileUpload.push(fileObj);
                }
                $scope.ppDokPenawaran.uploader3 = $scope.uploader3;
                $scope.uploader3.progress = 100;
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
        
        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
            	return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
            }
        });
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            if (typeof $scope.ppDokAdministrasi.fileUpload === 'undefined') {
                $scope.ppDokAdministrasi.fileUpload = [];
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokAdministrasi.fileUpload = $scope.ppDokAdministrasi.fileUpload.concat(fileObj);
                $scope.ppDokAdministrasi.uploader = $scope.uploader;
            } else {
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokAdministrasi.fileUpload = $scope.ppDokAdministrasi.fileUpload.concat(fileObj);
                $scope.uploader = $scope.ppDokAdministrasi.uploader;
            }
            //console.info($scope.ppDokAdministrasi.fileUpload);
        };
        //remove
        $scope.ppDokAdministrasi.fireRemove = [];
        uploader.removeFileItem = function (index) {
            uploader.queue.splice(index, 1);
            $scope.ppDokAdministrasi.fireRemove.push($scope.ppDokAdministrasi.fileUpload[index]);
            $scope.ppDokAdministrasi.fileUpload.splice(index, 1);
        }
        uploader.removeAll = function () {
        	var i = uploader.queue.length;
        	for (var y = 0; y < i; y++) {
        		uploader.removeFileItem(i -1 -y);
        	}
        	uploader.progress = 0;
        }
        
        // FILTERS
        uploader2.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
            	return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
            }
        });

        uploader2.onCompleteItem = function (fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            //console.log('INFO File : ' + JSON.stringify(response));
            if (typeof $scope.ppDokTeknis.fileUpload === 'undefined') {
                $scope.ppDokTeknis.fileUpload = [];
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokTeknis.fileUpload = $scope.ppDokTeknis.fileUpload.concat(fileObj);
                $scope.ppDokTeknis.uploader2 = $scope.uploader2;
            } else {
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokTeknis.fileUpload = $scope.ppDokTeknis.fileUpload.concat(fileObj);
                $scope.uploader2 = $scope.ppDokTeknis.uploader2;
            }
        };
        //remove
        $scope.ppDokTeknis.fireRemove = [];
        uploader2.removeFileItem = function (index) {
            uploader2.queue.splice(index, 1);
            $scope.ppDokTeknis.fireRemove.push($scope.ppDokTeknis.fileUpload[index]);
            $scope.ppDokTeknis.fileUpload.splice(index, 1);
        }
        uploader2.removeAll = function () {
        	var i = uploader2.queue.length;
        	for (var y = 0; y < i; y++) {
        		uploader2.removeFileItem(i -1 -y);
        	}
        	uploader2.progress = 0;
        }
        
        // FILTERS
        uploader3.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
            	return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
            }
        });

        uploader3.onCompleteItem = function (fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            //console.log('INFO File : ' + JSON.stringify(response));
            if (typeof $scope.ppDokPenawaran.fileUpload === 'undefined') {
                $scope.ppDokPenawaran.fileUpload = [];
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokPenawaran.fileUpload = $scope.ppDokPenawaran.fileUpload.concat(fileObj);
                $scope.ppDokPenawaran.uploader3 = $scope.uploader3;
            } else {
                var fileObj = {
                	realFileName: response.fileName
                };
                $scope.ppDokPenawaran.fileUpload = $scope.ppDokPenawaran.fileUpload.concat(fileObj);
                $scope.uploader3 = $scope.ppDokPenawaran.uploader3;
            }
        };
        //remove
        $scope.ppDokPenawaran.fireRemove = [];
        uploader3.removeFileItem = function (index) {
                uploader3.queue.splice(index, 1);
                $scope.ppDokPenawaran.fireRemove.push($scope.ppDokPenawaran.fileUpload[index]);
                $scope.ppDokPenawaran.fileUpload.splice(index, 1);
            }
        uploader3.removeAll = function () {
        	var i = uploader3.queue.length;
        	for (var y = 0; y < i; y++) {
        		uploader3.removeFileItem(i -1 -y);
        	}
        	uploader3.progress = 0;
        }
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

        $scope.btnSimpanDisable = false;
        
        $scope.checkIsAllUploaded =function(){
        	var valid = true;
        	 angular.forEach(uploader.queue, function (file, index) {
	        	 if(!file.isUploaded){
	        		 valid = false;
	        	 }
        	 });
        	 angular.forEach(uploader2.queue, function (file, index) {
	        	 if(!file.isUploaded){
	        		 valid = false;
	        	 }
        	 });
        	 angular.forEach(uploader3.queue, function (file, index) {
	        	 if(!file.isUploaded){
	        		 valid = false;
	        	 }
        	 });
        	 return valid;
        }
        
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
            } else if (typeof pptc.suratPenawaran.mataUang === 'undefined' || pptc.suratPenawaran.mataUangI == '') {
                // alert("Tanggal Batas Surat Penawaran belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Mata Uang belum disi...');
            } else if (typeof pptc.suratPenawaran.nilaiJaminan === 'undefined' || pptc.suratPenawaran.nilaiJaminan == '') {
                //alert("Nilai Jaminan belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Nilai Jaminan belum disi...');
            } else if (typeof pptc.suratPenawaran.tanggalBatasJaminan === 'undefined' || pptc.suratPenawaran.tanggalBatasJaminan == '') {
                //alert("Tanggal Batas Jaminan belum disi.");
                toaster.pop('warning', 'Kesalahan', 'Tanggal Batas Jaminan belum disi..');
            } else if (uploader.queue.length == 0) {
                toaster.pop('warning', 'Kesalahan', 'Dokumen Administrasi harus diisi');
            } else if (uploader2.queue.length == 0) {
                toaster.pop('warning', 'Kesalahan', 'Dokumen Teknis harus diisi');
            } else if (uploader3.queue.length == 0) {
                toaster.pop('warning', 'Kesalahan', 'Dokumen Penawaran harus diisi');
            }else if(!$scope.checkIsAllUploaded()){
            	toaster.pop('warning', 'Kesalahan', 'Semua Dokumen harus ter upload');
            } else {
                ModalService.showModalConfirmation('Apakah anda yakin untuk menyimpan data penawaran ini?').then(function (result) {
                    $scope.btnSimpanDisable = true;
                    $scope.loading = true;

                    simpanSuratPenawaran();
                });
            }

        }


        function simpanSuratPenawaran() {
            pptc.suratPenawaran.tanggalAwal = $filter('date')(pptc.suratPenawaran.tanggalAwal, 'yyyy-MM-dd');
            pptc.suratPenawaran.tanggalBatas = $filter('date')(pptc.suratPenawaran.tanggalBatas, 'yyyy-MM-dd');
            pptc.suratPenawaran.tanggalBatasJaminan = $filter('date')(pptc.suratPenawaran.tanggalBatasJaminan, 'yyyy-MM-dd');
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
                    pptc.btnSimpanPenawaran = false;
                    simpanDokAdmin(data.id);
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
        }


        function simpanDokAdmin(suratPenawaranId) {
            //remove
            angular.forEach($scope.ppDokAdministrasi.fireRemove, function (item, index) {
            	if(typeof item.dokAdminId != 'undefined'){
            		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/delete/' + item.dokAdminId)
                    .success(function (data) {
                        //console.info(data);
                    });	
            	}
                
            });

            //insert&update
            angular.forEach(uploader.queue, function (item, index) {
                var formDokAdministrasi = {
                    suratPenawaran: suratPenawaranId,
                    realFileName: $scope.ppDokAdministrasi.fileUpload[index].realFileName,
                    fileName: item.file.name
                }
                $scope.ppDokAdministrasi.fileUpload
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/';
                if (typeof $scope.ppDokAdministrasi.fileUpload[index].dokAdminId !== 'undefined') {
                    uri = uri + 'update';
                    formDokAdministrasi.id = $scope.ppDokAdministrasi.fileUpload[index].dokAdminId;
                } else {
                    uri = uri + 'insert';
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
                        //console.info(data);
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            });
            simpanDokTeknis(suratPenawaranId);

        }

        function simpanDokTeknis(suratPenawaranId) {
            //remove
            angular.forEach($scope.ppDokTeknis.fireRemove, function (item, index) {
            	if(typeof item.dokTeknisId != 'undefined'){
            		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/delete/' + item.dokTeknisId)
                    .success(function (data) {
                        //console.info(data);
                    });
            	}
            });

            angular.forEach(uploader2.queue, function (item2, index) {
                var formDokTeknis = {
                    suratPenawaran: suratPenawaranId,
                    realFileName: $scope.ppDokTeknis.fileUpload[index].realFileName,
                    fileName: item2.file.name
                }
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/';
                if (typeof $scope.ppDokTeknis.fileUpload[index].dokTeknisId !== 'undefined') {
                    uri = uri + 'update';
                    formDokTeknis.id = $scope.ppDokTeknis.fileUpload[index].dokTeknisId;
                } else {
                    uri = uri + 'insert';
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
                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            });
            simpanDokDataPenawaran(suratPenawaranId);
        }

        function simpanDokDataPenawaran(suratPenawaranId) {
            //remove
            angular.forEach($scope.ppDokPenawaran.fireRemove, function (item, index) {
            	if(typeof item.dokPenawaranId != 'undefined'){
            		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/delete/' + item.dokPenawaranId)
                    .success(function (data) {
                        //console.info(data);
                    });
            	}
            });

            angular.forEach(uploader3.queue, function (item3, index) {
                var formDokPenawaran = {
                    suratPenawaran: suratPenawaranId,
                    fileName: item3.file.name,
                    realFileName: $scope.ppDokPenawaran.fileUpload[index].realFileName

                }
                var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/';
                if (typeof $scope.ppDokPenawaran.fileUpload[index].dokPenawaranId !== 'undefined') {
                    uri = uri + 'update';
                    formDokPenawaran.id = $scope.ppDokPenawaran.fileUpload[index].dokPenawaranId;
                } else {
                    uri = uri + 'insert';
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

                    })
                    .error(function (data, status, headers, config) {
                        $scope.messageModalError = "penyimpanan gagal";
                        $scope.loading = false;
                    });
            });

            $scope.showSuccess();
        }



        $scope.showSuccess = function () {
            $scope.loading = false;
            toaster.pop('success', 'Sukses', 'Data surat penawaran berhasil disimpan.');
            $scope.loading = true;
            $scope.konfirmasi();
        }

        $scope.konfirmasi = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/confirm.html',
                controller: ModalKonfirmasiCtrl,
                size: size
            });
            modalInstance.result.then(function () { //ok
                $rootScope.pengadaanVendor = {
                    pengadaan: {
                        id: $scope.pengadaanVendor.id
                    }
                };
                
                //console.log("test : "+JSON.stringify($scope.pengadaanVendor));
                if ($scope.pengadaanVendor.jenisPenawaran.id == 1) {
                    $location.path('/appvendor/promise/procurement/penawaranharga/vendor/harga/total');
                } else if ($scope.pengadaanVendor.jenisPenawaran.id == 2) {
                    $location.path('/appvendor/promise/procurement/penawaranharga/vendor/harga/satuan');
                }
            }, function () {});
        };
        var ModalKonfirmasiCtrl = function ($scope, $modalInstance, $rootScope) {
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalKonfirmasiCtrl.$inject = ['$scope', '$modalInstance', '$rootScope'];


    }

    DataPenawaranVendorSatuSampulController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader','RequestService'];


})();