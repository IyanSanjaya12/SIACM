(function () {
    'use strict';

    angular.module('naut').controller('SesiAuctionViewSimpleController', SesiAuctionViewSimpleController);

    function SesiAuctionViewSimpleController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $q) {
        $scope.selisihPenawaran = 0;
        /* ============================ START Detail Pengadaan ================================================ */
        $scope.loading = true;
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            //console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        /* ---------------------------- END Detail Pengadaan ------------------------------------------------- */


        /* ============================ START Bidang Usaha Pengadaan ========================================= */
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $scope.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
        /* ---------------------------- END Bidang Usaha Pengadaan ------------------------------------------ */


        /* ============================= START Data Penyedia Barang/Jasa Detail ============================ */
        $scope.dataPenyediaList = $rootScope.listDataPenyedia;
        //console.log('$scope.dataPenyediaList1 '+JSON.stringify($scope.dataPenyediaList));


        /* ============================ Mapping Sesi Auction from table ===================================== */
        var detailSesiAuction = $rootScope.sesiAuctionDetail;
        //$scope.noSesiAuction = detailSesiAuction.length + 1;
        var noSesiAuc = detailSesiAuction.length + 1;
        $scope.tahunAuction = $filter('date')(new Date(), 'MMM-yyyy');
        if ($scope.detilPengadaan.jenisPenawaran.id == 1)
            $scope.jenisPenawaran = $scope.detilPengadaan.jenisPenawaran.nama;
        else
            $scope.jenisPenawaran = "Item_Id";
        $scope.noSesiAuction = "Session-" + noSesiAuc + "/" + $scope.pengadaanId + "-" + $scope.jenisPenawaran + "/" + $scope.tahunAuction;
        /* ---------------------------- END Mapping Sesi Auction -------------------------------------------- */


        /* ============================= START Validasi Data Inputan ======================================= */
        $scope.validasiTimeAwal = function () {
            var maxTime = document.getElementsByName("input_awal")[0].max;
            var minTime = document.getElementsByName("input_awal")[0].min;
            var tanggalInputAwal = document.getElementsByName("input_awal")[0].value;
            //console.log(tanggalInputAwal + " vs " + maxTime);
            if (tanggalInputAwal > maxTime || tanggalInputAwal < minTime) {
                toaster.pop('error', 'Salah Waktu', 'Waktu berlaku antar 7.00AM sampai 10.00PM');
                //document.getElementsByName("input_awal")[0].focus();
            }
        }

        $scope.validasiTimeAkhir = function () {
            // NOTE: Bila terjadi perubahan terhadap inputan di database terhadap REGEX waktu di jadwal Pengadaan silahkan UBAH
            // Validasi Tanggal Inputan dengan Jadwal
            var tanggalAkhir = new Date($rootScope.tglAkhirJadwalP);
            var maxDate = $filter('date')(tanggalAkhir, 'dd-MMM-yyyy');
            var tanggalInputAkhir = document.getElementsByName("date_akhir")[0].value;
            var tglSekarang = $filter('date')(new Date(), 'dd-MMM-yyyy');
            //console.log(tanggalInputAkhir + " vs " + maxDate);
            
            // Validasi Waktu Inputan dengan Jadwal
            var waktuAkhir = new Date($rootScope.waktuAkhirJadwalP);
            var maxTime = $filter('date')(waktuAkhir, 'HH:mm');
            var waktuInputAkhir = document.getElementsByName("input_akhir")[0].value;
            var waktuSekarang = $filter('date')(new Date(), 'HH:mm');
            //console.log(waktuInputAkhir + " vs " + maxTime);
            //console.log(waktuInputAkhir + " vs " + waktuSekarang);
                       
            if ( tanggalInputAkhir > maxDate ) {
            	toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir melebihi dari Jadwal Pengadaan! (' + maxDate + ')');
                //document.getElementsByName("date_akhir")[0].focus();
            } else if (tanggalInputAkhir < tglSekarang) {
                toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir kurang dari Hari ini! (' + tglSekarang + ')');
                //document.getElementsByName("date_akhir")[0].focus();
            } else if (tanggalInputAkhir == tglSekarang && waktuInputAkhir > maxTime) {
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi dari Jadwal Pengadaan! (' + maxTime + ')');
                //document.getElementsByName("input_akhir")[0].focus();
            } else if (tanggalInputAkhir == tglSekarang && waktuInputAkhir < waktuSekarang) {
                toaster.pop('error', 'Salah Waktu', 'Waktu Akhir melebihi Waktu hari ini (' + waktuSekarang + ')');
                //document.getElementsByName("input_akhir")[0].focus();
            }
        }

        // untuk peserta Auction dari vendor yang akan ikut
        var vendorCheck = [];
        $scope.pesertaAuction = function (vendor, ikut) {
            if (ikut) {
                vendorCheck.push(vendor);
            } else {
                vendorCheck.splice(vendorCheck.indexOf(vendor), 1);
            }
            //console.log("vendorCheck = " + JSON.stringify(vendorCheck));
        }

        $scope.onChangeSelisihPenawaran = function () {
            $scope.selisihPenawaran = document.getElementById("selisihPenawaran").value;
            //console.log(">> "+document.getElementById("selisihPenawaran").value);
        }

        $scope.validasiInput = function () {
                var tglAwalSesi = new Date($scope.tanggalSesiAwal);
                $scope.validasiTimeAkhir();
                if ($scope.tanggalSesiAkhir == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Tanggal Akhir Sesi belum terisi');
                    //document.getElementsByName("date_akhir")[0].focus();
                } else if (tglAwalSesi > $scope.tanggalSesiAkhir) {
                    toaster.pop('error', 'Salah Tanggal', 'Tanggal Akhir Sesi harus lebih besar dari Tanggal Awal Sesi');
                    //document.getElementsByName("date_akhir")[0].focus();
                } else if ($scope.akhirSessionTime == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Waktu Akhir Sesi belum terisi');
                    //document.getElementsByName("input_akhir")[0].focus();
                } else if ($scope.selisihPenawaran == undefined) {
                    toaster.pop('warning', 'Field Kosong', 'Selisih Penawaran belum terisi');
                    //document.getElementsByName("selisihPenawaran")[0].focus();
                } else if (vendorCheck.length == 0) {
                    toaster.pop('warning', 'Check-Box Kosong', 'Selisih Penawaran belum terisi');
                } else {
                    $scope.save();
                    $rootScope.dataPenawaranAuction = [];
                    $scope.back();
                }
            }
            /* ----------------------------- END Validasi Data Inputan ------------------------------------------ */


        /* ============================= Datepicker ========================================================= */
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[0];
        $scope.tanggalAwalSesiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAwalSesiOpened = true;
        };
        $scope.tanggalAkhirSesiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tanggalAkhirSesiOpened = true;
        };
        $scope.tanggalSesiAwal = $filter('date')(new Date(), 'dd-MMM-yyyy');
        var tanggal = $filter('date')(new Date(), 'EEE MMM d y h:mm a Z');
        $scope.awalSessionTime = new Date(tanggal);
        /* ----------------------------- END Datepicker --------------------------------------------------- */



        // fungsi tombol kembali
        $scope.back = function () {
            $scope.go('/app/promise/procurement/evaluasihargasimple/total/auction/view');
        }

        // fungsi target lokasi
        $scope.go = function (path) {
            $location.path(path);
        }

        //cek terlebih dahulu nilai session seblumnya ada atau tidak
        function asyncGetLastAuction(pengadaanId) {
            return $q(function (resolve) {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + "/procurement/evaluasiharga/PenawaranAuctionServices/getDistinctPenawaranAuctionAndLastAuctionWithKursByPengadaan/" + $scope.pengadaanId)
                    .success(function (lastPenawaranAuction) {
                        $scope.loading = false;
                        resolve(lastPenawaranAuction);
                    });
            });
        }

        //get nilai HPS
        $scope.nilaiHPS = 0;

        var lastPenawaranAuction = asyncGetLastAuction($scope.pengadaanId);
        lastPenawaranAuction.then(function (dataPenwaranList) {
            //console.log("test JSON : " + JSON.stringify(resolve));
            if (dataPenwaranList.length > 0) {
                //sudah ada session sebelumnya
                //sorting
                for (var x = 0; x <= dataPenwaranList.length; x++) {
                    //console.log("DATA X : " + JSON.stringify(dataPenwaranList[x]));
                    for (var y = x + 1; y <= dataPenwaranList.length + 1; y++) {
                        //console.log("DATA Y : " + JSON.stringify(dataPenwaranList[y]));
                        if (typeof dataPenwaranList[y] !== 'undefined') {
                            if (dataPenwaranList[x].totalPenawaranKonfersi > dataPenwaranList[y].totalPenawaranKonfersi) {
                                var temp = dataPenwaranList[x];
                                dataPenwaranList[x] = dataPenwaranList[y];
                                dataPenwaranList[y] = temp;
                            }
                        }

                    }
                }

                $scope.hargaAwal = dataPenwaranList[0].totalPenawaranKonfersi;
                $scope.mataUang = 'IDR';

            } else {
                ///belum ada session
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getTotalNilaiHPSByPengadaan/' + $scope.pengadaanId)
                    .success(function (data, status, headers, config) {
                        $scope.hargaAwal = data;
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });
            }
        });



        //insert kurs, agar bisa dibuka di negosiasi
        $scope.insertKursPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanListByPengadaan/' + $scope.pengadaanId)
                .success(function (data) {
                    if (data.length == 0) {
                        $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/insertKursPengadaanFromMaster/' + $scope.pengadaanId)
                            .success(function (data) {
                                console.log(">>" + JSON.stringify(data));
                                $scope.loading = false;
                            });

                    } else {
                        $scope.loading = false;
                    }
                });
        };

        // ################### Simpan data-nya ke Table ######################
        // 1. Simpan data Sesi Auction
        $scope.save = function () {
            var tglAwalAuction = $scope.tanggalSesiAwal + " " + document.getElementsByName("input_awal")[0].value;
            var tglAwalSesi = $filter('date')($scope.tanggalSesiAkhir, 'dd-MMM-yyyy');
            var tglAkhirAuction = tglAwalSesi + " " + document.getElementsByName("input_akhir")[0].value;
            var dataSesiAuction = {
                noSesi: $scope.noSesiAuction,
                waktuAwal: new Date(tglAwalAuction),
                waktuAkhir: new Date(tglAkhirAuction),
                keterangan: $scope.keterangan,
                hargaAwal: $scope.hargaAwal,
                selisihPenawaran: $scope.selisihPenawaran.replace(/\,/g, ""),
                pengadaan: $scope.pengadaanId,
                itemPengadaan: 0,
                status: 0
            }

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiharga/AuctionServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataSesiAuction
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    var dataSesiAuctionId = data.id;
                    savePesertaAuction(dataSesiAuctionId);
                    $scope.insertKursPengadaan();
                }
                //console.log("CREATE AUCTION SESSION OK : " + JSON.stringify(data));
            });
            //                console.log(">> Simpan Data Sesi Auction : " + JSON.stringify(dataSesiAuction));

            // 2. Simpan data Peserta Auction
            var savePesertaAuction = function (dataSesiAuctionId) {
                //console.log("vendorCheck = " + JSON.stringify(vendorCheck));
                for (var i = 0; i < vendorCheck.length; i++) {
                    var dataPesertaAuction = {
                        vendor: vendorCheck[i].id,
                        auction: dataSesiAuctionId
                    }

                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/evaluasiharga/PesertaAuctionServices/create',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: dataPesertaAuction
                    }).success(function (data, status, headers, config) {
                        //console.log("CREATE PESERTA AUCTION OK : " + JSON.stringify(data));
                    });
                }
            }


        }
    }


    SesiAuctionViewSimpleController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$q'];
})();