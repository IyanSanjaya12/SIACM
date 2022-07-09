/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DataPenawaranVendorTahapDuaController', DataPenawaranVendorTahapDuaController);

    function DataPenawaranVendorTahapDuaController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, FileUploader, $state, $stateParams) {
        var form = this;
        
        form.dataPengadaan = $stateParams.dataPengadaan;
        
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.dataPengadaan.id)
        .success(function (data, status, headers, config) {
            form.subBidangUsahaList = data;
        });
        
        var uploadDokumenPenawaran = $scope.uploadDokumenPenawaran = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
        
        uploadDokumenPenawaran.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        uploadDokumenPenawaran.onCompleteItem = function (fileItem, response, status, headers) {
            fileItem.realFileName = response.fileName;
        }

        var loadDokumenPenawaran = function() {
        	$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/getListByVendorAndPengadaan/' + form.vendor.id + '/' + form.dataPengadaan.id)
            .success(function (data, status, headers, config) {                
                if (data !== undefined && data.length > 0) {
                	form.dokumenDataPenawaran = data[0];
                	var paramDokumenPenawaran = {
                        name: data[0].file,
                        size: 10000 /*sesuaikan*/
                    };
                	uploadDokumenPenawaran.addToQueue(paramDokumenPenawaran, undefined, undefined);
                    var item = uploadDokumenPenawaran.queue[uploadDokumenPenawaran.queue.length - 1];

                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                   
                    form.dataPengadaan.id = form.dokumenDataPenawaran.suratPenawaran.pengadaan.id;
	                form.vendor.id = form.dokumenDataPenawaran.suratPenawaran.vendor.id;
	                form.dataPengadaan.nomorSuratPenawaran = form.dokumenDataPenawaran.suratPenawaran.nomor;
	                form.dataPengadaan.tglSuratPenawaran = form.dokumenDataPenawaran.suratPenawaran.tanggalAwal;
	                form.dataPengadaan.tanggalBatas = form.dokumenDataPenawaran.suratPenawaran.tanggalBatas;
	                form.dataPengadaan.namaJaminan = form.dokumenDataPenawaran.suratPenawaran.namaJaminan;
	                form.dataPengadaan.mataUang.id = form.dokumenDataPenawaran.suratPenawaran.mataUang.id;
	                form.dataPengadaan.nilaiJaminan = form.dokumenDataPenawaran.suratPenawaran.nilaiJaminan;
	               form.dataPengadaan.tanggalBatasJaminan =form.dokumenDataPenawaran.suratPenawaran.tanggalBatasJaminan;
                } else {
                	$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + form.vendor.id + '/' + form.dataPengadaan.id)
                	.success(function (data, status, headers, config) {
                		if (data !== undefined && data.length > 0) {
                			form.suratPenawaran = data[0];
                		}
                	});
                }
            });
        }

        $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
        .success(function (data, status, headers, config) {
                form.vendor = data;
                
                loadDokumenPenawaran();
        });
        
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
        .success(function (data, status, headers, config) {
            form.mataUangList = data;
            form.dataPengadaan.mataUang = data[0];
        });

        form.back = function () {
            $state.go('appvendor.promise.procurement-vendor-datapenawarantahapdua-index');
        }

        form.tanggalSuratPenawaranStatus = false;
        form.tanggalSuratPenawaranOpened = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tanggalSuratPenawaranStatus = true;
        };
        
        form.tglBatasPenawaran = false;
        form.tglBtsPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglBatasPenawaran = true;
        };

        form.tglBtsJaminanOpened = false;
        form.tglBtsJaminanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglBtsJaminanOpened = true;
        };
        
        form.btnSimpanPenawaran = function() {
        	var validateInput = true;
            if (uploadDokumenPenawaran !== undefined && uploadDokumenPenawaran.queue.length > 0) {
            } else {
            	toaster.pop('warning', 'Kesalahan', 'Data dokumen penawaran belum diupload..!');
            	validateInput = false;
            }	
            	
            if (form.dataPengadaan.nomorSuratPenawaran === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Nomor Surat Penawaran belum disi..!');
            	validateInput = false;
            }
            
            if (form.dataPengadaan.tglSuratPenawaran === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Tanggal Surat Penawaran belum disi...');
            	validateInput = false;
            }
            
            if (form.dataPengadaan.tanggalBatas === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Tanggal Batas Surat Penawaran belum disi...');
            	validateInput = false;
            }
            
            if (form.dataPengadaan.namaJaminan === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Nama Jaminan belum disi...');
            	validateInput = false;
            }
            if (form.dataPengadaan.mataUang === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Mata Uang belum disi...');
            	validateInput = false;
            }
            if (form.dataPengadaan.nilaiJaminan === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Nilai Jaminan belum disi...');
            	validateInput = false;
            }
            if (form.dataPengadaan.tanggalBatasJaminan === undefined) {
            	toaster.pop('warning', 'Kesalahan', 'Tanggal Batas Jaminan belum disi..');
            	validateInput = false;
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
            	var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/updateSuratPenawaran';
            	form.dataPengadaan.tglSuratPenawaran = $filter('date')(form.dataPengadaan.tglSuratPenawaran, 'yyyy-MM-dd');
            	form.dataPengadaan.tanggalBatas = $filter('date')(form.dataPengadaan.tanggalBatas, 'yyyy-MM-dd');
	            form.dataPengadaan.tanggalBatasJaminan = $filter('date')(form.dataPengadaan.tanggalBatasJaminan, 'yyyy-MM-dd');
	            var formSuratPenawaran = {
	                pengadaan: form.dataPengadaan.id,
	                vendor: form.vendor.id,
	                nomor: form.dataPengadaan.nomorSuratPenawaran,
	                tanggalAwal: form.dataPengadaan.tglSuratPenawaran,
	                tanggalBatas: form.dataPengadaan.tanggalBatas,
	                namaJaminan: form.dataPengadaan.namaJaminan,
	                mataUang: form.dataPengadaan.mataUang.id,
	                nilaiJaminan: form.dataPengadaan.nilaiJaminan,
	                tanggalBatasJaminan: form.dataPengadaan.tanggalBatasJaminan,
	            };
	            
	            if (form.dokumenDataPenawaran !== undefined && form.dokumenDataPenawaran.suratPenawaran !== undefined) {
	            	formSuratPenawaran.id = form.dokumenDataPenawaran.suratPenawaran.id;
	            }
	            if (form.suratPenawaran !== undefined) {
	            	formSuratPenawaran.id = form.suratPenawaran.id; 
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
	                    simpanDokDataPenawaran(data.id);
                        $rootScope.tahapanId = form.dokumenDataPenawaran.suratPenawaran.pengadaan.tahapanPengadaan.tahapan.id;
	                    $location.path("/appvendor/promise/procurement/penawaranharga/vendor");
	            });
            });
        }

        function simpanDokDataPenawaran(suratPenawaranId) {
        	var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/dokumenDataPenawaranServices/';
        	var formDokPenawaran = {
                    suratPenawaran: suratPenawaranId
            }
        	
        	if (form.dokumenDataPenawaran !== undefined) {
        		uri = uri + 'update';
        		var fileName = form.dokumenDataPenawaran.file;
        		formDokPenawaran.id = form.dokumenDataPenawaran.id;
        	} else {
        		uri = uri + 'insert';
        	}
        	
    		angular.forEach(uploadDokumenPenawaran.queue, function (item) {
    			if ((fileName == undefined) || (fileName !== undefined && item.file.name != fileName)) {
    				formDokPenawaran.file = item.file.name;
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
                data: formDokPenawaran
            });
        }
        
    }

    DataPenawaranVendorTahapDuaController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', 'FileUploader', '$state', '$stateParams'];

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