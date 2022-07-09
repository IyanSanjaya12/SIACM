/**=========================================================
 * Module: JadwalPengadaanDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('JadwalPengadaanDetilController', JadwalPengadaanDetilController);

    function JadwalPengadaanDetilController(ModalService, $scope, $http, $rootScope, $resource, $location, ngTableParams, $modal, $filter) {
        $scope.pengadaan = $rootScope.pengadaan;
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

        //Material Pengadaan
        $scope.getMaterialPengadaan = function (pengadaanId) {
            $scope.loadingMaterialPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + pengadaanId)
                .success(function (data, status, headers, config) {
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
                    $scope.loadingMaterialPengadaan = false;
                });
        };
        $scope.getMaterialPengadaan($scope.pengadaan.id);

        //Jasa Pengadaan
        $scope.getJasaPengadaan = function (pengadaanId) {
            $scope.loadingJasaPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + pengadaanId)
                .success(function (data, status, headers, config) {
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
                    $scope.loadingJasaPengadaan = false;
                });
        };
        $scope.getJasaPengadaan($scope.pengadaan.id);

        //Jadwal Pengadaan
        $scope.getJadwalPengadaan = function (pengadaanObj) {
            $scope.loadingJadwalPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanId/' + pengadaanObj.id)
                .success(function (data, status, headers, config) {
                    if (data.length == 0) {
                        //get jadwal master by alur
                        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getRegisterJadwalByAlurPengadaanId/' + pengadaanObj.alurPengadaan.id)
                            .success(function (data, status, headers, config) {
                                $scope.statusJadwal = false;
                                $scope.list = [];
                                for (var i = 0; i < data.length; i++) {
                                    $scope.list.push($scope.generateObject(data[i]));
                                    $scope.proccessData(i, data[i], false);
                                }
                                $scope.jadwalPengadaanList = data;
                                $scope.loadingJadwalPengadaan = false;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.loadingJadwalPengadaan = false;
                            });
                    } else {

                        $scope.statusJadwal = true;
                        $scope.list = [];

                        $scope.jadwalPengadaanList = data;
                        for (var i = 0; i < data.length; i++) {
                            $scope.list.push($scope.generateObject(data[i]));
                            $scope.proccessData(i, data[i], true);
                        }
                        $scope.loadingJadwalPengadaan = false;
                    }


                })
                .error(function (data, status, headers, config) {
                    $scope.loadingJadwalPengadaan = false;
                });
        }
        $scope.getJadwalPengadaan($scope.pengadaan);

        // Datepicker
        var form = this;

        form.clear = function () {
            form.tglAwalPengumuman = null;
            form.tglAkhirPengumuman = null;

            form.tglAwalPengambilanDokumen = null;
            form.tglAkhirPengambilanDokumen = null;

            form.tglAwalPenjelasan = null;
            form.tglAkhirPenjelasan = null;

            form.tglAwal1SPemasukanPenawaran = null;
            form.tglAkhir1SPemasukanPenawaran = null;

            form.tglAwal1SPembukaanPenawaran = null;
            form.tglAkhir1SPembukaanPenawaran = null;

            form.tglAwalEvlAdmin = null;
            form.tglAkhirEvlAdmin = null;

            form.tglAwalEvlTeknis = null;
            form.tglAkhirEvlTeknis = null;

            form.tglAwalEvlHarga = null;
            form.tglAkhirEvlHarga = null;

            form.tglAwalNegosiasi = null;
            form.tglAkhirNegosiasi = null;

            form.tglAwalPenetapan = null;
            form.tglAkhirPenetapan = null;

        };
        form.disabled = function (date, mode) {
            return false;
            //return ( mode === 'day' && ( date.getDay() === 0 ||date.getDay() === 6 ) );
        };
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        //Pengumuman
        form.tglAwalPengumumanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalPengumumanOpened = true;
        };
        form.tglAkhirPengumumanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirPengumumanOpened = true;
        };

        //Pengambilan Dokumen
        form.tglAwalPengambilanDokumenOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalPengambilanDokumenOpened = true;
        };
        form.tglAkhirPengambilanDokumenOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirPengambilanDokumenOpened = true;
        };

        //Penjelasan
        form.tglAwalPenjelasanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalPenjelasanOpened = true;
        };
        form.tglAkhirPenjelasanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirPenjelasanOpened = true;
        };

        //1 Sampul - Pemasukan Penawaran
        form.tglAwal1SPemasukanPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwal1SPemasukanPenawaranOpened = true;
        };
        form.tglAkhir1SPemasukanPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhir1SPemasukanPenawaranOpened = true;
        };

        //1 Sampul - Pembukaan Penawaran
        form.tglAwal1SPembukaanPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwal1SPembukaanPenawaranOpened = true;
        };
        form.tglAkhir1SPembukaanPenawaranOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhir1SPembukaanPenawaranOpened = true;
        };

        //Evaluasi Administrasi
        form.tglAwalEvlAdminOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalEvlAdminOpened = true;
        };
        form.tglAkhirEvlAdminOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirEvlAdminOpened = true;
        };

        //Evaluasi Teknis
        form.tglAwalEvlTeknisOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalEvlTeknisOpened = true;
        };
        form.tglAkhirEvlTeknisOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirEvlTeknisOpened = true;
        };

        //Evaluasi Harga
        form.tglAwalEvlHargaOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalEvlHargaOpened = true;
        };
        form.tglAkhirEvlHargaOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirEvlHargaOpened = true;
        };

        //Negosiasi
        form.tglAwalNegosiasiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalNegosiasiOpened = true;
        };
        form.tglAkhirNegosiasiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirNegosiasiOpened = true;
        };

        //Penetapan
        form.tglAwalPenetapanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalPenetapanOpened = true;
        };
        form.tglAkhirPenetapanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirPenetapanOpened = true;
        };


        $scope.generateDataPengumuman = function (data, status) {
            if (status) {
                form.tglAwalPengumuman = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirPengumuman = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalPengumuman = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalPengumuman = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirPengumuman = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirPengumuman = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwalPengumuman = 8;
                form.waktuMenitAwalPengumuman = 0;
                form.waktuJamAkhirPengumuman = 17;
                form.waktuMenitAkhirPengumuman = 0;
            }
        }

        $scope.generateDataPengambilanDokumen = function (data, status) {
            if (status) {
                form.tglAwalPengambilanDokumen = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirPengambilanDokumen = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalPengambilanDokumen = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalPengambilanDokumen = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirPengambilanDokumen = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirPengambilanDokumen = parseInt($filter('date')(data.waktuAkhir, 'mm'));
                form.lokasiPengambilanDokumen = data.lokasi;
            } else {
                form.waktuJamAwalPengambilanDokumen = 8;
                form.waktuMenitAwalPengambilanDokumen = 0;
                form.waktuJamAkhirPengambilanDokumen = 17;
                form.waktuMenitAkhirPengambilanDokumen = 0;
            }
        }

        $scope.generateDataPenjelasan = function (data, status) {
            if (status) {
                form.tglAwalPenjelasan = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirPenjelasan = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalPenjelasan = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalPenjelasan = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirPenjelasan = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirPenjelasan = parseInt($filter('date')(data.waktuAkhir, 'mm'));
                form.lokasiPenjelasan = data.lokasi;
            } else {
                form.waktuJamAwalPenjelasan = 8;
                form.waktuMenitAwalPenjelasan = 0;
                form.waktuJamAkhirPenjelasan = 17;
                form.waktuMenitAkhirPenjelasan = 0;
            }
        }

        $scope.generateData1SPemasukanPenawaran = function (data, status) {
            if (status) {
                form.tglAwal1SPemasukanPenawaran = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhir1SPemasukanPenawaran = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwal1SPemasukanPenawaran = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwal1SPemasukanPenawaran = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhir1SPemasukanPenawaran = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhir1SPemasukanPenawaran = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwal1SPemasukanPenawaran = 8;
                form.waktuMenitAwal1SPemasukanPenawaran = 0;
                form.waktuJamAkhir1SPemasukanPenawaran = 17;
                form.waktuMenitAkhir1SPemasukanPenawaran = 0;
            }
        }

        $scope.generateData1SPembukaanPenawaran = function (data, status) {
            if (status) {
                form.tglAwal1SPembukaanPenawaran = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhir1SPembukaanPenawaran = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwal1SPembukaanPenawaran = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwal1SPembukaanPenawaran = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhir1SPembukaanPenawaran = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhir1SPembukaanPenawaran = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwal1SPembukaanPenawaran = 8;
                form.waktuMenitAwal1SPembukaanPenawaran = 0;
                form.waktuJamAkhir1SPembukaanPenawaran = 17;
                form.waktuMenitAkhir1SPembukaanPenawaran = 0;
            }
        }

        $scope.generateDataEvaluasiAdmin = function (data, status) {
            if (status) {
                form.tglAwalEvlAdmin = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirEvlAdmin = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalEvlAdmin = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalEvlAdmin = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirEvlAdmin = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirEvlAdmin = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwalEvlAdmin = 8;
                form.waktuMenitAwalEvlAdmin = 0;
                form.waktuJamAkhirEvlAdmin = 17;
                form.waktuMenitAkhirEvlAdmin = 0;
            }
        }

        $scope.generateDataEvaluasiTeknis = function (data, status) {
            if (status) {
                form.tglAwalEvlTeknis = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirEvlTeknis = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalEvlTeknis = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalEvlTeknis = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirEvlTeknis = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirEvlTeknis = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwalEvlTeknis = 8;
                form.waktuMenitAwalEvlTeknis = 0;
                form.waktuJamAkhirEvlTeknis = 17;
                form.waktuMenitAkhirEvlTeknis = 0;
            }
        }

        $scope.generateDataEvaluasiHarga = function (data, status) {
            if (status) {
                form.tglAwalEvlHarga = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirEvlHarga = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalEvlHarga = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalEvlHarga = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirEvlHarga = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirEvlHarga = parseInt($filter('date')(data.waktuAkhir, 'mm'));
                form.lokasiEvlHarga = data.lokasi;
            } else {
                form.waktuJamAwalEvlHarga = 8;
                form.waktuMenitAwalEvlHarga = 0;
                form.waktuJamAkhirEvlHarga = 17;
                form.waktuMenitAkhirEvlHarga = 0;
            }
        }

        $scope.generateDataNegosiasi = function (data, status) {
            if (status) {
                form.tglAwalNegosiasi = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirNegosiasi = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalNegosiasi = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalNegosiasi = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirNegosiasi = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirNegosiasi = parseInt($filter('date')(data.waktuAkhir, 'mm'));
                form.lokasiNegosiasi = data.lokasi;
            } else {
                form.waktuJamAwalNegosiasi = 8;
                form.waktuMenitAwalNegosiasi = 0;
                form.waktuJamAkhirNegosiasi = 17;
                form.waktuMenitAkhirNegosiasi = 0;
            }
        }

        $scope.generateDataPenetapanPemenang = function (data, status) {
            if (status) {
                form.tglAwalPenetapan = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                form.tglAkhirPenetapan = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                form.waktuJamAwalPenetapan = parseInt($filter('date')(data.waktuAwal, 'HH'));
                form.waktuMenitAwalPenetapan = parseInt($filter('date')(data.waktuAwal, 'mm'));
                form.waktuJamAkhirPenetapan = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                form.waktuMenitAkhirPenetapan = parseInt($filter('date')(data.waktuAkhir, 'mm'));
            } else {
                form.waktuJamAwalPenetapan = 8;
                form.waktuMenitAwalPenetapan = 0;
                form.waktuJamAkhirPenetapan = 17;
                form.waktuMenitAkhirPenetapan = 0;
            }
        }

        /*control the array*/
        $scope.proccessData = function (index, data, status) {
            switch (index) {
                case 0:
                    $scope.generateDataPengumuman(data, status);
                    break;
                case 1:
                    $scope.generateDataPengambilanDokumen(data, status);
                    break;
                case 2:
                    $scope.generateDataPenjelasan(data, status);
                    break;
                case 3:
                    $scope.generateData1SPemasukanPenawaran(data, status);
                    break;
                case 4:
                    $scope.generateData1SPembukaanPenawaran(data, status);
                    break;
                case 5:
                    $scope.generateDataEvaluasiAdmin(data, status);
                    break;
                case 6:
                    $scope.generateDataEvaluasiTeknis(data, status);
                    break;
                case 7:
                    $scope.generateDataEvaluasiHarga(data, status);
                    break;
                case 8:
                    $scope.generateDataNegosiasi(data, status);
                    break;
                case 9:
                    $scope.generateDataPenetapanPemenang(data, status);
                    break;
            }
        }

        /*formatting the date*/
        $scope.formatDate = function (tgl, jam, menit) {
            var date = null;
            if (typeof tgl === 'string') {
                date = new Date(tgl);
            } else {
                date = tgl;
            }
            date.setHours(jam);
            date.setMinutes(menit);
            return date;
        }

        /*define the object to post*/
        $scope.generateObject = function (data) {
            var object = {
                id: data.id,
                pengadaanId: $scope.pengadaan.id,
                tahapanPengadaanId: data.tahapanPengadaan.id,
                tanggalAwal: data.tglAwal,
                waktuAwal: data.waktuAwal,
                tanggalAkhir: data.tglAkhir,
                waktuAkhir: data.waktuAkhir,
                lokasi: data.lokasi
            };
            return object;
        }

        /*update the object of pengumuman*/
        $scope.updatePengumuman = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalPengumuman, form.waktuJamAwalPengumuman, form.waktuMenitAwalPengumuman);
            var dateAkhir = $scope.formatDate(form.tglAkhirPengumuman, form.waktuJamAkhirPengumuman, form.waktuMenitAkhirPengumuman);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*update the object of pengambilan dokumen*/
        $scope.updatePengambilanDokumen = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalPengambilanDokumen, form.waktuJamAwalPengambilanDokumen, form.waktuMenitAwalPengambilanDokumen);
            var dateAkhir = $scope.formatDate(form.tglAkhirPengambilanDokumen, form.waktuJamAkhirPengambilanDokumen, form.waktuMenitAkhirPengambilanDokumen);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
            object.lokasi = form.lokasiPengambilanDokumen;
        }

        /*update the object of penjelasan*/
        $scope.updatePenjelasan = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalPenjelasan, form.waktuJamAwalPenjelasan, form.waktuMenitAwalPenjelasan);
            var dateAkhir = $scope.formatDate(form.tglAkhirPenjelasan, form.waktuJamAkhirPenjelasan, form.waktuMenitAkhirPenjelasan);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
            object.lokasi = form.lokasiPenjelasan;
        }

        /*update the object of Satu Sampul/Pemasukan Penawaran*/
        $scope.update1SPemasukanPenawaran = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwal1SPemasukanPenawaran, form.waktuJamAwal1SPemasukanPenawaran, form.waktuMenitAwal1SPemasukanPenawaran);
            var dateAkhir = $scope.formatDate(form.tglAkhir1SPemasukanPenawaran, form.waktuJamAkhir1SPemasukanPenawaran, form.waktuMenitAkhir1SPemasukanPenawaran);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*update the object of Satu Sampul/Pembukaan Penawaran*/
        $scope.update1SPembukaanPenawaran = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwal1SPembukaanPenawaran, form.waktuJamAwal1SPembukaanPenawaran, form.waktuMenitAwal1SPembukaanPenawaran);
            var dateAkhir = $scope.formatDate(form.tglAkhir1SPembukaanPenawaran, form.waktuJamAkhir1SPembukaanPenawaran, form.waktuMenitAkhir1SPembukaanPenawaran);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*update the object of Evaluasi Administrasi*/
        $scope.updateEvaluasiAdministrasi = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalEvlAdmin, form.waktuJamAwalEvlAdmin, form.waktuMenitAwalEvlAdmin);
            var dateAkhir = $scope.formatDate(form.tglAkhirEvlAdmin, form.waktuJamAkhirEvlAdmin, form.waktuMenitAkhirEvlAdmin);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*update the object of Evaluasi Teknis*/
        $scope.updateEvaluasiTeknis = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalEvlTeknis, form.waktuJamAwalEvlTeknis, form.waktuMenitAwalEvlTeknis);
            var dateAkhir = $scope.formatDate(form.tglAkhirEvlTeknis, form.waktuJamAkhirEvlTeknis, form.waktuMenitAkhirEvlTeknis);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*update the object of Evaluasi Harga*/
        $scope.updateEvaluasiHarga = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalEvlHarga, form.waktuJamAwalEvlHarga, form.waktuMenitAwalEvlHarga);
            var dateAkhir = $scope.formatDate(form.tglAkhirEvlHarga, form.waktuJamAkhirEvlHarga, form.waktuMenitAkhirEvlHarga);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
            object.lokasi = form.lokasiEvlHarga;
        }

        /*update the object of Negosiasi*/
        $scope.updateNegoisasi = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalNegosiasi, form.waktuJamAwalNegosiasi, form.waktuMenitAwalNegosiasi);
            var dateAkhir = $scope.formatDate(form.tglAkhirNegosiasi, form.waktuJamAkhirNegosiasi, form.waktuMenitAkhirNegosiasi);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
            object.lokasi = form.lokasiNegosiasi;
        }

        /*update the object of Penetapan Pemenang*/
        $scope.updatePenetapanPemenang = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate(form.tglAwalPenetapan, form.waktuJamAwalPenetapan, form.waktuMenitAwalPenetapan);
            var dateAkhir = $scope.formatDate(form.tglAkhirPenetapan, form.waktuJamAkhirPenetapan, form.waktuMenitAkhirPenetapan);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
        }

        /*control the array*/
        $scope.proccessing = function (index) {
            switch (index) {
                case 0:
                    $scope.updatePengumuman(index);
                    break;
                case 1:
                    $scope.updatePengambilanDokumen(index);
                    break;
                case 2:
                    $scope.updatePenjelasan(index);
                    break;
                case 3:
                    $scope.update1SPemasukanPenawaran(index);
                    break;
                case 4:
                    $scope.update1SPembukaanPenawaran(index);
                    break;
                case 5:
                    $scope.updateEvaluasiAdministrasi(index);
                    break;
                case 6:
                    $scope.updateEvaluasiTeknis(index);
                    break;
                case 7:
                    $scope.updateEvaluasiHarga(index);
                    break;
                case 8:
                    $scope.updateNegoisasi(index);
                    break;
                case 9:
                    $scope.updatePenetapanPemenang(index);
                    break;
            }
        }

        $scope.insertToJadwalPengadaan = function (index) {
            $scope.loading = true;
            var uri = "";
            if (!$scope.statusJadwal) {
                uri = $rootScope.backendAddress + '/procurement/jadwalPengadaanServices/insertJadwalPengadaan';
            } else {
                uri = $rootScope.backendAddress + '/procurement/jadwalPengadaanServices/updateJadwalPengadaan';
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
                data: $scope.list[index]
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }

        $scope.btnTest = function () {
			ModalService.showModalConfirmation().then(function (result) {
	            for (var i = 0; i < $scope.list.length; i++) {
	                $scope.proccessing(i);
	                $scope.insertToJadwalPengadaan(i);
	            }
			});
            $location.path("/app/promise/procurement/jadwalpengadaan");
        }

        $scope.btnKembali = function () {
            $location.path("/app/promise/procurement/jadwalpengadaan");
        }
    }

    JadwalPengadaanDetilController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'ngTableParams', '$modal', '$filter'];

})();
