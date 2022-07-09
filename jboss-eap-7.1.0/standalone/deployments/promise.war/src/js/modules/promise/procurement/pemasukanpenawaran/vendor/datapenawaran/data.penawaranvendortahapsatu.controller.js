/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataPenawaranVendorTahapSatuController', DataPenawaranVendorTahapSatuController);

    function DataPenawaranVendorTahapSatuController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $state, $stateParams, ngTableParams, $modal, FileUploader) {
        var form = this;
        form.dataPengadaan = $stateParams.dataPengadaan;
        
        var getBidangPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.dataPengadaan.id)
            .success(function (data, status, headers, config) {
                $scope.loading = false;
                form.subBidangUsahaList = data;
            });
        }
        getBidangPengadaan();
        
        var uploader = $scope.uploader = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
        
        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            fileItem.realFileName = response.fileName;
        };
        
        var uploadDokumenTeknis = $scope.uploadDokumenTeknis = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
        
        uploadDokumenTeknis.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        uploadDokumenTeknis.onCompleteItem = function (fileItem, response, status, headers) {
            fileItem.realFileName = response.fileName;
        };

        var loadDokumenAdmin = function() {
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/getListByVendorAndPengadaan/' + form.vendor.id + '/' + form.dataPengadaan.id, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                if (data !== undefined && data.length > 0) {
                	form.dokAdminList = data;
                	var fileAdmin = {
                        name: data[0].file,
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
            });
        }

        var loadDokumenTeknis = function() {
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/getListByVendorAndPengadaan/' + form.vendor.id + '/' + form.dataPengadaan.id)
            .success(function (data, status, headers, config) {                
                if (data !== undefined && data.length > 0) {
                	form.dokTeknisList = data;
                	var fileTeknis = {
                        name: data[0].file,
                        size: 10000 /*sesuaikan*/
                    };
                    uploadDokumenTeknis.addToQueue(fileTeknis, undefined, undefined);
                    var item = uploadDokumenTeknis.queue[uploadDokumenTeknis.queue.length - 1];

                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                }                
            });
        }

        $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
        .success(function (data, status, headers, config) {
                form.vendor = data;
                
                loadDokumenAdmin();
                loadDokumenTeknis();
        });

        form.back = function () {
            $location.path('/appvendor/promise/procurement/datapenawaran/vendor');
        }
        
        form.btnSimpanDataPenawaran = function () {
        	var validateInput = true;
            if (uploader !== undefined && uploader.queue.length > 0) {
            } else {
            	validateInput = false;
            	toaster.pop('warning', 'Kesalahan', 'Data dokumen administrasi belum diupload..!');
            }
        	
        	if (uploadDokumenTeknis !== undefined && uploadDokumenTeknis.queue.length > 0) {        		
            } else {
            	validateInput = false;
            	toaster.pop('warning', 'Kesalahan', 'Data dokumen teknis belum diupload..!');
            }
        	if (validateInput) {
        		simpanSuratPenawaran();
        	}
        }
        
        function simpanSuratPenawaran() {
            var modalInstance = $modal.open({
                templateUrl: '/confirm.html',
                controller: 'ModalKonfirmasiCtrl',
                size: 'sm'
            });
            modalInstance.result.then(function () { 
            	var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/';
            	var formSuratPenawaran = {
    	                pengadaan: form.dataPengadaan.id,
    	                vendor: form.vendor.id
        		}
            	if (form.dokAdminList !== undefined && form.dokAdminList.length > 0) {
            		uri = uri + 'updateSuratPenawaran';
        			formSuratPenawaran.id = form.dokAdminList[0].suratPenawaran.id;
            	} else {
            		uri = uri + 'insertSuratPenawaran';
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
                    simpanDokAdmin(data.id);
                    simpanDokTeknis(data.id);
                    //nextTahapan();
                    $state.go('appvendor.promise.procurement-vendor-datapenawaran');
                });
            });
        }

        function simpanDokAdmin(suratPenawaranId) {
        	var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranAdminServices/';
            var formDokAdministrasi = {
                suratPenawaran: suratPenawaranId
            }
        	if (form.dokAdminList !== undefined && form.dokAdminList.length > 0) {
        		uri = uri + 'update';
        		var fileName = form.dokAdminList[0].file;
        		formDokAdministrasi.id = form.dokAdminList[0].id;
        	} else {
        		uri = uri + 'insert';
        	}
            
            angular.forEach(uploader.queue, function (item) {
    			if ((fileName == undefined) || (fileName !== undefined && item.file.name != fileName)) {
    				formDokAdministrasi.file = item.file.name;
    				if (item.isUploaded == false) {
    					item.upload();
    				}
    			}
            })
            
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
            });
        }

        function simpanDokTeknis(suratPenawaranId) {
        	var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenPenawaranTeknisServices/';
        	var formDokTeknis = {
                    suratPenawaran: suratPenawaranId
            }
        	
        	if (form.dokTeknisList !== undefined && form.dokTeknisList.length > 0) {
        		uri = uri + 'update';
        		var fileName = form.dokTeknisList[0].file;
        		formDokTeknis.id = form.dokTeknisList[0].id;
        	} else {
        		uri = uri + 'insert';
        	}
        	
    		angular.forEach(uploadDokumenTeknis.queue, function (item) {
    			if ((fileName == undefined) || (fileName !== undefined && item.file.name != fileName)) {
    				formDokTeknis.file = item.file.name;
    				if (item.isUploaded == false) {
    					item.upload();
    				}
    			}
    		});
    		
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
            });
        }
        
        function nextTahapan() {
        	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + form.dataPengadaan.id)
            .success(function (data, status, headers, config) {
            	form.nextTahapan = data;
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
                        pengadaanId: form.dataPengadaan.id,
                        tahapanPengadaanId: form.nextTahapan
                    }
                })
                .success(function (data, status, headers, config) {
                    $state.go('appvendor.promise.procurement-vendor-datapenawaran');
                });
            });
        }

    }
    
    DataPenawaranVendorTahapSatuController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$state', '$stateParams', 'ngTableParams', '$modal', 'FileUploader'];

})();

angular.module('naut')
.controller('ModalKonfirmasiCtrl', function ($scope, $modalInstance) {
	$scope.ok = function () {
        $modalInstance.close('closed');
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});