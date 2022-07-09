/**=========================================================
 * Module: UndanganPengadaanDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UndanganPengadaanDetilController', UndanganPengadaanDetilController);

    function UndanganPengadaanDetilController($scope, $http, $rootScope, $resource, $location, ngTableParams, $modal) {
        $scope.pengadaan = $rootScope.pengadaan;
        $scope.isMaterial = false;
        $scope.isJasa = false;
        //console.log('info pengadaan '+JSON.stringify($scope.pengadaan));

        $scope.getDaftarPengadaanStatus = function (pengadaanId, userId) {
            $scope.loadingValidasiPendaftaran = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorUserIdAndPengadaanId/' + userId + '/' + pengadaanId)
                .success(function (data,
                    status, headers, config) {
                    if (typeof data.vendor !== 'undefined') {
                        //console.log('INFO data : ' + JSON.stringify(data));
                        if (data.vendor.user == userId) {
                            $scope.loadingValidasiPendaftaran = false;
                            $scope.btnDaftarPengadaanStatus = false;
                        } else {
                            $scope.loadingValidasiPendaftaran = false;
                            $scope.btnDaftarPengadaanStatus = true;
                        }
                    } else {
                        $scope.loadingValidasiPendaftaran = false;
                        $scope.btnDaftarPengadaanStatus = true;
                    }
                }).error(function (data, status, headers, config) {
                    $scope.loadingValidasiPendaftaran = false;
                });
        };
        $scope.getDaftarPengadaanStatus($scope.pengadaan.id, $rootScope.userLogin.user.id);
        
        $scope.getDokumenPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + $rootScope.pengadaan.id)
                .success(function (data, status, headers, config) {
                    $scope.dokumenList = data;
                })
                .error(function (data, status, headers, config) {});
        };
        $scope.getDokumenPengadaan();

        //Bidang Usaha Pengadaan relasi Pembukaaan Penawaran
        $scope.getBidangPengadaan = function (pengadaanId) {
            $scope.loadingSubidang = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + pengadaanId)
                .success(function (data,
                    status, headers, config) {
                    $scope.subBidangUsahaByPengadaanIdList = data;
                    $scope.loadingSubidang = false;
                }).error(function (data, status, headers, config) {
                    $scope.loadingSubidang = false;
                });
        }
        $scope.getBidangPengadaan($scope.pengadaan.id);

        //Vendor Requirement
        $scope.getVendorRequirement = function (pengadaanId) {
            $scope.loadingVendorRequirement = true;
            $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementListByPengadaanId/' + pengadaanId)
                .success(function (data,
                    status, headers, config) {
                    $scope.vendorRequirement = data;
                    $scope.loadingVendorRequirement = false;
                }).error(function (data, status, headers, config) {
                    $scope.loadingVendorRequirement = false;
                });
        };
        $scope.getVendorRequirement($scope.pengadaan.id);

        //Material Pengadaan
        $scope.getMaterialPengadaan = function (pengadaanId) {
            $scope.loadingMaterialPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    if (data.length > 0)
                        $scope.isMaterial = true;

                    for (var i = 0; i < data.length; i++) {
                        data[i].index = i + 1;
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

                    $scope.loadingMaterialPengadaan = false;
                })
                .error(function (data, status, headers, config) {
                    //console.log('Error loading jenis pengadaan');
                    $scope.loadingMaterialPengadaan = false;
                });
        };
        $scope.getMaterialPengadaan($scope.pengadaan.id);


        //Jasa Pengadaan
        $scope.getJasaPengadaan = function (pengadaanId) {
            $scope.loadingJasaPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    if (data.length > 0)
                        $scope.isJasa = true;

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

                    $scope.loadingJasaPengadaan = false;
                })
                .error(function (data, status, headers, config) {
                    //console.log('Error loading jenis pengadaan');
                    $scope.loadingJasaPengadaan = false;
                });
        };
        $scope.getJasaPengadaan($scope.pengadaan.id);

        //Jadwal Pengadaan	
        $scope.getJadwalPengadaan = function (pengadaanId) {
            $scope.loadingJadwalPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanId/' + pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.jadwalPengadaanList = data;
                    //console.log('info pengadaan : ' + JSON.stringify(data));
                    $scope.loadingJadwalPengadaan = false;
                })
                .error(function (data, status, headers, config) {
                    //console.log('Error loading jenis pengadaan');
                    $scope.loadingJadwalPengadaan = false;
                });
        };
        $scope.getJadwalPengadaan($scope.pengadaan.id);

        //hasilpendaftaran
        $scope.btnDaftarPengadaan = function (size) {
            $scope.loadingDaftarPengadaan = true;
            $scope.btnSimpanDisable = true;
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    if (typeof data.id !== 'undefined') {
                        var vendor = data;
                        if (vendor.statusBlacklist != 0) {
                            $scope.messageError = "Anda tidak bisa mendaftar pengadaan. Status Anda saat ini masuk dalam Daftar Hitam!";
                            $scope.loadingDaftarPengadaan = false;
                        } else {
                            if (vendor.status == 0) {
                                $scope.messageError = "Anda tidak bisa mendaftar pengadaan. Status Anda saat ini belum diterima sebagai vendor Aktif!";
                                $scope.loadingDaftarPengadaan = false;
                            } else {
                                
                                $http({
                                        method: 'POST',
                                        url: $rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/insertPendaftaranVendor',
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
                                            tanggalDaftar: "",
                                            nomorPendaftaran: "",
                                            pengadaanId: $scope.pengadaan.id,
                                            vendorId: vendor.id
                                        }
                                    }).success(function (data, status, headers, config) {
                                        $rootScope.vendor = vendor;
                                        var modalInstance = $modal.open({
                                            templateUrl: '/hasilpendaftaran.html',
                                            controller: ModalInstanceUndanganVendorCtrl,
                                            size: size
                                        });
                                        $scope.loadingDaftarPengadaan = false;
                                    })
                                    .error(function (data, status, headers, config) {
                                        $scope.messageError = "Daftar Pengadaan Gagal";
                                        $scope.loadingDaftarPengadaan = false;
                                    });
                            }

                        }
                    } else {
                        $scope.loadingDaftarPengadaan = false;
                    }
                })
                .error(function (data, status, headers, config) {
                    //console.log('Error loading loadingDaftarPengadaan');
                    $scope.loadingDaftarPengadaan = false;
                    $scope.btnSimpanDisable = false;
                });
        };

        var ModalInstanceUndanganVendorCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, $location, $filter) {
            $scope.pengadaan = $rootScope.pengadaan;
            $scope.vendor = $rootScope.vendor;
            $scope.token = $rootScope.userLogin.token;

            $scope.dokumenPengadaan = function () {
                $http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + $rootScope.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.download = data;
                    })
                    .error(function (data, status, headers, config) {});
            };
            $scope.dokumenPengadaan();

            $scope.downloadFile = function (file) {
                $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/insert',
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
                            tglAmbil: $filter('date')(new Date(), "dd-MM-yyyy"),
                            pengadaan: $scope.pengadaan.id,
                            vendor: $scope.vendor.id
                        }
                    })
                    .success(function (data, status, headers, config) {
                        //window.location.assign($rootScope.backendAddressIP+'/promise/files/'+file.nama);
                        window.open($rootScope.viewUploadBackendAddress + "/" + file.path, '_blank');
                        //console.log('Download File : ' + file.nama+', Path : '+file.path);
                    });
            };

            $scope.printDiv = function (divName) {
            	$(".btn-cancel").hide();
            	$(".btn-print").hide();
            	$(".close").hide();
                var printContents = document.getElementById(divName).innerHTML;
                $(".btn-cancel").show();
                $(".btn-print").show();
                $(".close").show();
                var popupWin = window.open('', '_blank', 'width=800,height=600');
                popupWin.document.open()
                popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</html>');
                popupWin.document.close();
            }

            $scope.cancel = function () {
                $location.path("/appvendor/promise/procurement/undanganvendor");
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceUndanganVendorCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', '$location', '$filter'];

        $scope.btnKembali = function () {
            $location.path('/appvendor/promise/procurement/undanganvendor');
        };
    }

    UndanganPengadaanDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'ngTableParams', '$modal'];

})();