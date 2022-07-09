(function () {
    'use strict';

    angular.module('naut').controller('AlurPengadaan02TambahTahapanController', AlurPengadaan02TambahTahapanController);

    function AlurPengadaan02TambahTahapanController($scope, $http, $rootScope, $resource, $location, toaster, $state, $stateParams, RequestService, $modal) {
        var form = this;
        $scope.alur = $rootScope.alurPengadaanData;

        $scope.tahapanPengadaanList = [];
        //get tahpan by alur
        $scope.getTahapanPengadaanByAlur = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/getAllByAlur/' + $scope.alur.id)
                .success(function (data) {
                    $scope.tahapanPengadaanList = data;
                    $scope.loading = false;
                })
                .error(function (data) {
                    console.error("error data");
                    $scope.loading = false;
                });
        };
        $scope.getTahapanPengadaanByAlur();

        //tambah tahapan
        $scope.btnTambahTahapan = function () {
            $rootScope.tahapanPengadaanList = $scope.tahapanPengadaanList;
            var modalInstance = $modal.open({
                templateUrl: 'tambah_tahapan.html',
                controller: ModalTambahTahapanController,
                size: 'lg'
            });
            modalInstance.result.then(function () {
                $scope.getTahapanPengadaanByAlur();
            });
        };
        var ModalTambahTahapanController = function ($scope, $rootScope, $modalInstance, $filter, RequestService) {
            $scope.tahapanPengadaan = 0;
            //lookup tahapan
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/tahapan/get-list')
                .success(function (data) {
                    $scope.tahapanPengadaanList = data;
                    $scope.loading = false;
                })
                .error(function (data) {
                    $scope.loading = false;
                });

            $scope.simpan = function () {
                $scope.loading = true;
                var isExist = false;
                angular.forEach($rootScope.tahapanPengadaanList, function (value, index) {
                    if (value.tahapan.id == $scope.tahapanPengadaan) {
                        isExist = true;
                    }
                });
                if (isExist) {
                    RequestService.modalInformation('Tahapan Pengadaan sudah ada!', 'danger')
                        .then(function (result) {
                            $scope.loading = false;
                        });

                } else {
                    var postForm = {
                        kode: $scope.kode,
                        tahapanId: $scope.tahapanPengadaan,
                        alurPengadaanId: $rootScope.alurPengadaanData.id,
                        squence: $scope.sequence
                    };
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/create',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: postForm
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                            $modalInstance.close('closed');
                        })
                        .error(function (data) {
                            $scope.loading = false;
                            console.error(data);
                        });
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalTambahTahapanController.$inject = ['$scope', '$rootScope', '$modalInstance', '$filter', 'RequestService'];

        //edit tahapan
        $scope.btnEditTahapan = function (tahapanPengadaan) {
            $rootScope.tahapanPengadaanList = $scope.tahapanPengadaanList;
            $rootScope.tahapanPengadaan = tahapanPengadaan;
            var modalInstance = $modal.open({
                templateUrl: 'tambah_tahapan.html',
                controller: ModalEditTahapanController,
                size: 'lg'
            });
            modalInstance.result.then(function () {
                $scope.getTahapanPengadaanByAlur();
            });
        };
        var ModalEditTahapanController = function ($scope, $rootScope, $modalInstance, $filter, RequestService) {
            $scope.tahapanPengadaan = $rootScope.tahapanPengadaan.tahapan.id;
            $scope.kode = $rootScope.tahapanPengadaan.kode;
            $scope.sequence = $rootScope.tahapanPengadaan.squence;
            $scope.id = $rootScope.tahapanPengadaan.id;

            //lookup tahapan
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/tahapan/get-list')
                .success(function (data) {
                    $scope.tahapanPengadaanList = data;
                    $scope.loading = false;
                })
                .error(function (data) {
                    $scope.loading = false;
                });

            $scope.simpan = function () {
                $scope.loading = true;
                var isExist = false;
                angular.forEach($rootScope.tahapanPengadaanList, function (value, index) {
                    if (value.tahapan.id == $scope.tahapanPengadaan) {
                        isExist = true;
                    }
                });

                RequestService.modalConfirmation('Anda yakin ingin merubah Tahapan Pengadaan?')
                    .then(function (result) {
                        var postForm = {
                            kode: $scope.kode,
                            tahapanId: $scope.tahapanPengadaan,
                            alurPengadaanId: $rootScope.alurPengadaanData.id,
                            squence: $scope.sequence,
                            tahapanPengadaanId: $scope.id
                        };
                        $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/update',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: postForm
                            }).success(function (data, status, headers, config) {
                                $scope.loading = false;
                                $modalInstance.close('closed');
                            })
                            .error(function (data) {
                                $scope.loading = false;
                                console.error(data);
                            });
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalEditTahapanController.$inject = ['$scope', '$rootScope', '$modalInstance', '$filter', 'RequestService'];

        //delete tahapan
        $scope.btnDeleteTahapanPengadaan = function (tahapanPengadaan) {
            RequestService.modalConfirmation('Anda yakin ingin menghapus "' + tahapanPengadaan.tahapan.nama + '" ini ?')
                .then(function () {
                    $http.get($rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/delete/' + tahapanPengadaan.id)
                        .success(function (data) {
                            $scope.getTahapanPengadaanByAlur();
                        })
                        .error(function (error) {
                            console.error(error);
                            $scope.loading = false;
                        })
                })
        };


        //Jadwal Tahapan Pengadaan
        $scope.getJadwalPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/getRegisterJadwalByAlurPengadaanId/' + $scope.alur.id)
                .success(function (data) {
                    $scope.dataJadwalPengadaanList = data;
                })
                .error(function (data) {
                    console.log("Error : " + JSON.stringify(data));
                });
        };
        $scope.getJadwalPengadaan();
        //Add - jadwal Tahapan Pengadaan
        $scope.btnTambahJadwalPengadaan = function () {
            $rootScope.dataJadwalPengadaanList = $scope.dataJadwalPengadaanList;
            var modalInstance = $modal.open({
                templateUrl: 'tambah_jadwal_tahapan_pengadaan.html',
                controller: ModalTambahJadwalPengadaanController,
                size: 'lg'
            });
            modalInstance.result.then(function () {
                $scope.getJadwalPengadaan();
            });
        };
        var ModalTambahJadwalPengadaanController = function ($scope, $rootScope, $modalInstance, $filter, RequestService) {
            $scope.form = {};
            $scope.form.tahapanPengadaan = {};
            $scope.form.isPrakualifikasi = 0;
            var refresh = function () {
                $scope.form.isJadwal = false;
                $scope.form.isLokasi = false;
                $scope.form.isNoUndangan = false;
                $scope.form.isTglAwal = false;
                $scope.form.isTglAkhir = false;
                $scope.form.isWaktuAwal = false;
                $scope.form.isWaktuAkhir = false;
            }
            refresh();

            $http.get($rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/getAllByAlur/' + $rootScope.alurPengadaanData.id)
                .success(function (data, status, headers, config) {
                    $scope.tahapanPengadaanFilterList = [];
                    angular.forEach(data, function (value, index) {
                        var isExist = false;
                        for (var i = 0; i < $rootScope.dataJadwalPengadaanList.length; i++) {
                            if (value.id == $rootScope.dataJadwalPengadaanList[i].tahapanPengadaan.id) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            $scope.tahapanPengadaanFilterList.push(value);
                        }
                    });
                })
                .error(function (data, status, headers, config) {})

            $scope.form.kembali = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.form.simpan = function () {
                var jadwal, lokasi, undangan, tglAwal, tglAkhir, waktuAwal, waktuAkhir;
                if ($scope.form.isJadwal == true) {
                    jadwal = 1;
                } else {
                    jadwal = 0;
                }
                if ($scope.form.isLokasi == true) {
                    lokasi = 1;
                } else {
                    lokasi = 0;
                }
                if ($scope.form.isNoUndangan == true) {
                    undangan = 1;
                } else {
                    undangan = 0;
                }
                if ($scope.form.isTglAwal == true) {
                    tglAwal = 1;
                } else {
                    tglAwal = 0;
                }
                if ($scope.form.isTglAkhir == true) {
                    tglAkhir = 1;
                } else {
                    tglAkhir = 0;
                }
                if ($scope.form.isWaktuAwal == true) {
                    waktuAwal = 1;
                } else {
                    waktuAwal = 0;
                }
                if ($scope.form.isWaktuAkhir == true) {
                    waktuAkhir = 1;
                } else {
                    waktuAkhir = 0;
                }

                if (typeof $scope.form.tahapanPengadaan.selected === 'undefined') {
                    alert("Pilih Tahapan Pengadaan.");
                } else {
                    //console.log(JSON.stringify(form));
                    var postForm = {
                        tahapanPengadaan: $scope.form.tahapanPengadaan.selected.id,
                        isJadwal: jadwal,
                        isLokasi: lokasi,
                        isNoUndangan: undangan,
                        isTglAwal: tglAwal,
                        isTglAkhir: tglAkhir,
                        isWaktuAwal: waktuAwal,
                        isWaktuAkhir: waktuAkhir,
                        isPrakualifikasi : $scope.form.isPrakualifikasi
                    }
                    console.log(JSON.stringify(postForm));
                    $scope.loading = true;
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/create',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: postForm
                    }).success(function (data, status, headers, config) {
                        if (typeof data !== 'undefined') {
                            toaster.pop('success', 'Registrasi Jadwal Tahapan Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                            $modalInstance.close('closed');
                        }
                        $scope.loading = false;
                        $scope.form.tahapanPengadaan.selected = undefined;
                        refresh();
                    });
                }
            };
        }
        ModalTambahJadwalPengadaanController.$inject = ['$scope', '$rootScope', '$modalInstance', '$filter', 'RequestService'];
        //Del - Tahapan Pengadaan
        $scope.btnDeleteJadwalTahapanPengadaan = function (id) {
            RequestService.modalConfirmation('Apakah anda yakin ingin menghapus data Registrasi Jadwal Tahapan Pengadaan?')
                .then(function () {
                    $http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/delete/' + id)
                        .success(function (data, status, headers, config) {
                            $scope.getJadwalPengadaan();
                        })
                        .error(function (data, status, headers, config) {});
                });
        };

        //edit jadwal pengadaan
        $scope.btnEditJadwalPengadaan = function (indexRegistrasiJadwalTahapanPengadaan) {
            $rootScope.indexRegistrasiJadwalTahapanPengadaan = indexRegistrasiJadwalTahapanPengadaan;
            var modalInstance = $modal.open({
                templateUrl: 'edit_jadwal_tahapan_pengadaan.html',
                controller: ModalEditJadwalPengadaanController,
                size: 'lg'
            });
            modalInstance.result.then(function () {
                $scope.getJadwalPengadaan();
            });
        };
        var ModalEditJadwalPengadaanController = function ($scope, $rootScope, $modalInstance, $filter, RequestService) {
            $scope.form = {};
            var dataRegistrasiJadwalTahapanPengadaan = [];
            $scope.form.tahapanPengadaan = {};

            if (typeof $rootScope.indexRegistrasiJadwalTahapanPengadaan !== 'undefined') {
                dataRegistrasiJadwalTahapanPengadaan = $rootScope.indexRegistrasiJadwalTahapanPengadaan;
            }
            $scope.form.id = dataRegistrasiJadwalTahapanPengadaan.id;
            $scope.form.tahapanPengadaan.selected = dataRegistrasiJadwalTahapanPengadaan.tahapanPengadaan;
            $scope.form.isJadwal = dataRegistrasiJadwalTahapanPengadaan.isJadwal;
            $scope.form.isLokasi = dataRegistrasiJadwalTahapanPengadaan.isLokasi;
            $scope.form.isNoUndangan = dataRegistrasiJadwalTahapanPengadaan.isNoUndangan;
            $scope.form.isTglAwal = dataRegistrasiJadwalTahapanPengadaan.isTglAwal;
            $scope.form.isTglAkhir = dataRegistrasiJadwalTahapanPengadaan.isTglAkhir;
            $scope.form.isWaktuAwal = dataRegistrasiJadwalTahapanPengadaan.isWaktuAwal;
            $scope.form.isWaktuAkhir = dataRegistrasiJadwalTahapanPengadaan.isWaktuAkhir;
            $scope.form.isPrakualifikasi = dataRegistrasiJadwalTahapanPengadaan.isPrakualifikasi; 

            $http.get( $rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/getAllByAlur/' + $rootScope.alurPengadaanData.id )
                .success(function (data, status, headers, config) {
                    $scope.tahapanPengadaanList = data;
                })
                .error(function (data, status, headers, config) {})

            $scope.form.kembali = function () {
                $modalInstance.dismiss('cancel');
            };
            
            $scope.form.simpan = function () {
                var jadwal, lokasi, undangan, tglAwal, tglAkhir, waktuAwal, waktuAkhir,isPrakualifikasi;
                if ($scope.form.isJadwal == true) {
                    jadwal = 1;
                } else {
                    jadwal = 0;
                }
                if ($scope.form.isLokasi == true) {
                    lokasi = 1;
                } else {
                    lokasi = 0;
                }
                if ($scope.form.isNoUndangan == true) {
                    undangan = 1;
                } else {
                    undangan = 0;
                }
                if ($scope.form.isTglAwal == true) {
                    tglAwal = 1;
                } else {
                    tglAwal = 0;
                }
                if ($scope.form.isTglAkhir == true) {
                    tglAkhir = 1;
                } else {
                    tglAkhir = 0;
                }
                if ($scope.form.isWaktuAwal == true) {
                    waktuAwal = 1;
                } else {
                    waktuAwal = 0;
                }
                if ($scope.form.isWaktuAkhir == true) {
                    waktuAkhir = 1;
                } else {
                    waktuAkhir = 0;
                }
                if (typeof $scope.form.tahapanPengadaan.selected === 'undefined') {
                    alert("Pilih Tahapan Pengadaan.");
                } else {
                    //console.log(JSON.stringify(form));
                    var postForm = {
                            id: $scope.form.id,
                            tahapanPengadaan: $scope.form.tahapanPengadaan.selected.id,
                            isJadwal: jadwal,
                            isLokasi: lokasi,
                            isNoUndangan: undangan,
                            isTglAwal: tglAwal,
                            isTglAkhir: tglAkhir,
                            isWaktuAwal: waktuAwal,
                            isWaktuAkhir: waktuAkhir,
                            isPrakualifikasi: $scope.form.isPrakualifikasi
                        }
                        //console.log(JSON.stringify(postForm));
                    $scope.loading = true;
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/update',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: postForm
                    }).success(function (data, status, headers, config) {
                        if (typeof data !== 'undefined') {
                            toaster.pop('success', 'Registrasi Jadwal Tahapan Pengadaan', 'Simpan ' + data.nama + ', berhasil.');
                            $modalInstance.close('closed');
                        }
                        $scope.loading = false;
                        $scope.form.tahapanPengadaan.selected = undefined;
                        $scope.form.isJadwal = false;
                        $scope.form.isLokasi = false;
                        $scope.form.isNoUndangan = false;
                        $scope.form.isTglAwal = false;
                        $scope.form.isTglAkhir = false;
                        $scope.form.isWaktuAwal = false;
                        $scope.form.isWaktuAkhir = false;
                    });
                };
            };
        };
        ModalTambahJadwalPengadaanController.$inject = ['$scope', '$rootScope', '$modalInstance', '$filter', 'RequestService'];
    }

    AlurPengadaan02TambahTahapanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', '$stateParams', 'RequestService', '$modal'];

})();