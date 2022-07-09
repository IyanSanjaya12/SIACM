(function () {
    'use strict';

    angular.module('naut').controller('PersetujuanPengadaanDetailController',
        PersetujuanPengadaanDetailController);

    function PersetujuanPengadaanDetailController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $filter, FileUploader, ngTableParams, $state,RequestService) {
        var persetujuanDetil = this;
        persetujuanDetil.persetujuanTahapanId = $rootScope.detilPengadaan.tahapanPengadaan.tahapan.id;
        $scope.detilPengadaan = $rootScope.detilPengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;

        persetujuanDetil.persetujuan = {};
        persetujuanDetil.persetujuan.pengadaanId = $scope.pengadaanId;

        //cek existing data
        var isExist = false;

        function getPersetujuanPengadaan() {
            persetujuanDetil.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/persetujuan/PersetujuanPengadaanServices/find/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                if (typeof data.id !== 'undefined') {
                    persetujuanDetil.persetujuan = data;
                    persetujuanDetil.persetujuan.pengadaanId = $scope.pengadaanId;
                    isExist = true;
                    //console.log("DATA PERSETUJUAN : " + JSON.stringify(data));
                }
                persetujuanDetil.loading = false;
                loadDokumen();
            }).
            error(function (data, status, headers, config) {

            });
        }
        getPersetujuanPengadaan();

        var uploader = $scope.uploader = new FileUploader({
            /* Masih hardcode */
            /*url: 'http://10.10.10.22:32770/promise/upload.php',*/
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });


        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
            	return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
            }
        });

        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            fileItem.realFileName = response.fileName;
            /*alert('realFileName '+fileItem.realFileName); */
        };

        // Datepicker
        persetujuanDetil.toggleMin = function () {
            persetujuanDetil.minDate = persetujuanDetil.minDate ? null : new Date();
        };
        persetujuanDetil.toggleMin();
        persetujuanDetil.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        persetujuanDetil.format = persetujuanDetil.formats[4];

        persetujuanDetil.tanggalPengumumanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            persetujuanDetil.tanggalPengumumanOpened = true;
        };

        function loadDokumen() {
            persetujuanDetil.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/persetujuan/PersetujuanPengadaanServices/getListByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                persetujuanDetil.dokumenPenjelasanList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < persetujuanDetil.dokumenPenjelasanList.length; y++) {
                    var file = {
                        name: persetujuanDetil.dokumenPenjelasanList[y].fileName,
                        size: persetujuanDetil.dokumenPenjelasanList[y].fileSize
                    };
                    uploader.addToQueue(file, undefined, undefined);
                    var item = uploader.queue[uploader.queue.length - 1];
                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                    /*added new attribute & properties to store realFileName*/
                    item.realFileName = persetujuanDetil.dokumenPenjelasanList[y].realFileName;
                }
                persetujuanDetil.loading = false;
            }).error(function (data, status, headers, config) {
                persetujuanDetil.loading = false;
            });
        }
		
		/* get Next Tahapan */
        $scope.getNextTahapan = function (pengadaanId) {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.nextTahapan = data;
                    $scope.updateTahapanPengadaan(pengadaanId, data);
                })
                .error(function (data, status, headers, config) {});
        }
		//update pengadaan
        $scope.updateTahapanPengadaan = function (pengadaanId, nextTahapan) {
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
                    pengadaanId: pengadaanId,
                    tahapanPengadaanId: nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $state.go('app.promise.procurement-panitia-persetujuanpengadaan');
            });
        };

        function saveDokumen(persetujuanPengadaanId) {
            /*first, delete all by pengadaan*/
            $http.get($rootScope.backendAddress + '/procurement/persetujuan/PersetujuanPengadaanServices/deleteDokumenByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: false
            }).success(function (data, status, headers, config) {

                /* insert new */
                angular.forEach(uploader.queue, function (item) {
                    var formPostPengadaan = {
                        persetujuanPengadaanId: persetujuanPengadaanId,
                        fileName: item.file.name,
                        realFileName: item.realFileName,
                        fileSize: 100000
                    }
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/persetujuan/PersetujuanPengadaanServices/createDokumen',
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
                });
            }).error(function (data, status, headers, config) {});
        }
        $scope.btnSimpanDisable=false;
        $scope.saveData = function () {

			ModalService.showModalConfirmation().then(function (result) {
	            $scope.btnSimpanDisable = true;
	            persetujuanDetil.loading = true;
	            var url = $rootScope.backendAddress + '/procurement/persetujuan/PersetujuanPengadaanServices/';
	            if (isExist) {
	                url = url + 'updatePersetujuanPengadaan';
	            } else {
	                url = url + 'createPersetujuanPengadaan';
	            }
	            persetujuanDetil.persetujuan.tanggalPengumuman = $filter('date')(persetujuanDetil.persetujuan.tanggalPengumuman, 'dd-MM-yyyy');
	            //console.info('Data Persetujuan : ' + JSON.stringify(persetujuanDetil.persetujuan));
	            $http({
	                method: 'POST',
	                url: url,
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                transformRequest: function (obj) {
	                    var str = [];
	                    for (var p in obj)
	                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	                    return str.join("&");
	                },
	                data: persetujuanDetil.persetujuan
	            }).success(function (data, status, headers, config) {
	                saveDokumen(data.id);
					$scope.getNextTahapan(persetujuanDetil.persetujuan.pengadaanId);
					persetujuanDetil.loading = false;
	            })
	            return false; 
			}); 
			
        }
		
        $scope.btnBack = function(){
			$location.path('/app/promise/procurement/persetujuanpengadaan');
		}
			
    }

    PersetujuanPengadaanDetailController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'FileUploader', 'ngTableParams', '$state','RequestService'];

})();