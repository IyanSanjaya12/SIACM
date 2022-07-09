(function () {
    'use strict';

    angular.module('naut').controller('HistoryAuctionViewController', HistoryAuctionViewController);

    function HistoryAuctionViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {

        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        $scope.listHarga = [];
        $scope.penawaranAuctionTerbaikList = [];
        $rootScope.idAuction = 0;

        var isiList = {};
        var statusAuction = '';
        /* ------------------------------------------------------------------------------------------------ */

        /* =============================== START Detail Pengadaan ========================================= */
        $scope.loading = true;
        if ($rootScope.detilPengadaan != undefined) {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        } else {
            console.log('INFO Error');
            alert("Error cause : DetailPengadaan is Undefined!");
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        /* ------------------------------- END Detail Pengadaan ------------------------------------------ */


        /* =============================== Mapping Sesi Auction from table =============================== */
        var detailSesiAuction = $rootScope.sesiAuctionDetail;
        /* ------------------------------- END Mapping Sesi Auction -------------------------------------- */


        /* =============================== Mapping Penawaran Auction from table ========================== */
        for (var i = 0; i < detailSesiAuction.length; i++) {
            var noIdSesi = detailSesiAuction[i].id;

            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByAuction/' + noIdSesi)
                .success(function (data, status, headers, config) {
                    var hargaTerbaikSebelumnya = 0;
                    angular.forEach(data, function (value, index) {
                        $scope.listHarga.push(data[index].totalPenawaranKonfersi);
                    });
                    $scope.hargaTerbaik = sortHarga($scope.listHarga);
                    angular.forEach(data, function (value, index) {
                        if (data[index].totalPenawaranKonfersi == $scope.hargaTerbaik) {
                            if (hargaTerbaikSebelumnya != $scope.hargaTerbaik) {
                                if (data[index].pesertaAuction.auction.status == 0) {
                                    statusAuction = 'Berlangsung';
                                } else {
                                    statusAuction = 'Sesi Ditutup';
                                }
                                var isiList = {
                                    idSesi: data[index].pesertaAuction.auction.id,
                                    noSesi: data[index].pesertaAuction.auction.noSesi,
                                    tglSesi: $filter('date')(data[index].pesertaAuction.auction.waktuAwal, 'dd-MMM-yyyy'),
                                    idVendor: data[index].pesertaAuction.vendor.id,
                                    namaVendor: data[index].pesertaAuction.vendor.nama,
                                    hargaTerbaik: data[index].nilaiPenawaran,
                                    mataUang: data[index].mataUang.kode,
                                    status: statusAuction
                                }
                                hargaTerbaikSebelumnya = data[index].nilaiPenawaran;
                                $scope.penawaranAuctionTerbaikList.push(isiList);
                            }
                            //console.log("ISI LIST KE - " + index + " = " + JSON.stringify(isiList));
                        }
                    });
                    //console.log("ISI penawaranAuctionTerbaikList = " + JSON.stringify($scope.penawaranAuctionTerbaikList));
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        }
        /* ------------------------------- END Mapping Penawaran Auction --------------------------------- */


        /* =============================== Sorting Harga Penawaran Awal ================================== */
        var sortHarga = function (listHarga) {
                var angka = listHarga;
                for (var x = 0; x <= angka.length; x++) {
                    for (var y = x + 1; y <= angka.length + 1; y++) {
                        if (angka[x] > angka[y]) {
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }
                    }
                }
                return angka[0];
            }
            /* ------------------------------- END Sorting Harga Penawaran Awal ----------------------------- */


        /* =============================== START DETAIL AUCTION MODAL =================================== */
        $scope.clickDetailAuction = function (idAuction) {
            $rootScope.idAuction = idAuction;
            $scope.viewDetailAuction();
        }

        $scope.viewDetailAuction = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/view_detail_auction.html',
                controller: ModalInstanceCtrl,
                size: 'lg',
            });
        };

        var ModalInstanceCtrl = function ($http, $scope, $rootScope, $modalInstance, ngTableParams) {
            var penawaranAuctionList = [];
            $scope.mataUangPengadaan = $scope.detilPengadaan.mataUang.id;

            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByAuction/' + $rootScope.idAuction, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {

                angular.forEach(data, function (value, index) {
                    penawaranAuctionList.push(value);
                });

                //console.log("ISI penawaranAuctionList = " + JSON.stringify($scope.penawaranAuctionList));
                $scope.noSesi = penawaranAuctionList[0].pesertaAuction.auction.noSesi;
                $scope.tglAwalSesi = penawaranAuctionList[0].pesertaAuction.auction.waktuAwal;
                $scope.tglAkhirSesi = penawaranAuctionList[0].pesertaAuction.auction.waktuAkhir;
                $scope.loading = false;

                // paging untuk Penawaran List
                $scope.penawaranAuctionList = new ngTableParams({
                    page: 1,
                    count: 4
                }, {
                    total: data.length,
                    getData: function ($defer, params) {
                        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                });
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        ModalInstanceCtrl.$inject = ['$http', '$scope', '$rootScope', '$modalInstance', 'ngTableParams'];
        /* ------------------------------- END DETAIL AUCTION MODAL ------------------------------------- */


        /* =============================== START CONTROLLER DETAIL ====================================== */
        $scope.cetak = function () {
            toaster.pop('error', 'Cetak Auction', 'Maaf Belum bisa untuk mencetak Riwayat Auction');
        }

        $scope.back = function () {
            $scope.go('/app/promise/procurement/evaluasiharga/total/auction/view');
        }

        $scope.go = function (path) {
                $location.path(path);
            }
            /* ------------------------------- END Rincian Controller -------------------------------------- */
    }

    HistoryAuctionViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];

})();