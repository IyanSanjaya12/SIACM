/**=========================================================
 * Module: HistoryPengadaanViewController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HistoryPengadaanViewController', HistoryPengadaanViewController);

    function HistoryPengadaanViewController($scope, $http, $rootScope, $resource, $location, $modal, $filter) {
        $scope.pengadaan = $rootScope.pengadaanView;
        $rootScope.view = undefined;
        $rootScope.detilPengadaan = undefined;
        $rootScope.pengadaanPenetapanPemenang = undefined;
        $rootScope.pengadaan = undefined;
        $rootScope.isEditable = undefined;
        $rootScope.ppEdit = undefined;
        $rootScope.flagView = undefined;
        $rootScope.mataUangCode = undefined;
        $rootScope.listHarga = undefined;
        $rootScope.vendorList = undefined;

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

        $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanAndTahapanBefore/' + $scope.pengadaan.id + '/' + $scope.pengadaan.tahapanPengadaan.tahapan.id)
            .success(function (data, status, headers, config) {
                $scope.jadwalList = data;
            });

        /*membuat navigasi manual*/
        $scope.navigate = function (tahapan) {
            $rootScope.view = true;
            if (tahapan == 6) {
                $rootScope.pengadaan = $scope.pengadaan;
                $location.path("/app/promise/procurement/pengambilandokumen/viewPengambilan");
            } else if (tahapan == 8) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                $rootScope.isEditable = false;
                $location.path("/app/promise/procurement/penjelasan/detail");
            } else if (tahapan == 9) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                if ($scope.pengadaan.jenisPenawaran.id == 1) {
                    $location.path("/app/promise/procurement/pemasukanPenawaran/viewTotal");
                } else {
                    $location.path("/app/promise/procurement/pemasukanPenawaran/viewSatuan");
                }
            } else if (tahapan == 10) {
                $http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/getList/' + $scope.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $rootScope.ppEdit = data[0];

                        if ($scope.pengadaan.jenisPenawaran.id == 1) {
                            $location.path('/app/promise/procurement/viewPembukaanPenawaranTotal');
                        } else {
                            $location.path('/app/promise/procurement/pembukaanPenawaranSatuan');
                        }
                    });
            } else if (tahapan == 11) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                $location.path('/app/promise/procurement/evaluasiAdministrasi/viewEvaluasiAdministrasi');
            } else if (tahapan == 12) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                if ($scope.pengadaan.jenisPenawaran.id == 1) {
                    $location.path('/app/promise/procurement/evaluasiteknis/view/viewTotal');
                } else {
                    $location.path('/app/promise/procurement/evaluasiteknis/view/satuan');
                }
            } else if (tahapan == 13) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                //$rootScope.detilPengadaan.tahapanPengadaan.tahapan.id = tahapan;
                $rootScope.flagView = 1;
                $rootScope.listHarga = [];
                $rootScope.vendorList = [];
                $rootScope.mataUangCode = "";
                if ($scope.pengadaan.jenisPenawaran.id == 1) {
                    $location.path('/app/promise/procurement/evaluasiharga/total/bidding/view');
                } else {
                    $location.path('/app/promise/procurement/evaluasiharga/satuan/bidding/view');
                }
            } else if (tahapan == 14) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                //$rootScope.detilPengadaan.tahapanPengadaan.tahapan.id = tahapan;
                $rootScope.flagView = 1;
                $rootScope.listHarga = [];
                $rootScope.vendorList = [];
                $rootScope.mataUangCode = "";
                if ($scope.pengadaan.jenisPenawaran.id == 1) {
                    $location.path('/app/promise/procurement/evaluasiharga/total/auction/viewEvaluasiHarga');
                } else {
                    $location.path('/app/promise/procurement/evaluasiharga/satuan/auction/view');
                }
            } else if (tahapan == 15) {
                $rootScope.detilPengadaan = $scope.pengadaan;
                if ($scope.pengadaan.jenisPenawaran.id == 1) {
                    $location.path('/app/promise/procurement/negosiasi/total/detail/cetak/' + $scope.pengadaan.id);
                } else {
                    $location.path('/app/promise/procurement/negosiasi/satuan/detail/' + $scope.pengadaan.id);
                }
            } else if (tahapan == 16) {
                $rootScope.pengadaanUsulanCalonPemenang= $scope.pengadaan;
                if ($scope.pengadaan.jenisPenawaran.id == 1 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 1) {
                    $location.path('app/promise/procurement/usulancalonpemenang/totalsistemgugur');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 1 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 2) {
                    $location.path('app/promise/procurement/usulancalonpemenang/viewtotalmeritpoint');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 2 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 1) {
                    $location.path('app/promise/procurement/usulancalonpemenang/satuansistemgugur');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 2 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 2) {
                    $location.path('app/promise/procurement/usulancalonpemenang/satuanmeritpoint');
                };

            } else if (tahapan == 17) {
                $rootScope.pengadaanPenetapanPemenang = $scope.pengadaan;
                if ($scope.pengadaan.jenisPenawaran.id == 1 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 1) {
                    $location.path('app/promise/procurement/penetapanpemenang/viewTotalSistemGugur');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 1 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 2) {
                    $location.path('app/promise/procurement/penetapanpemenang/viewTotalMeritPoint');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 2 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 1) {
                    $location.path('app/promise/procurement/penetapanpemenang/satuanSistemGugur');
                };
                if ($scope.pengadaan.jenisPenawaran.id == 2 && $scope.pengadaan.sistemEvaluasiPenawaran.id == 2) {
                    $location.path('app/promise/procurement/penetapanpemenang/satuanMeritPoint');
                };
            }
        }

        $rootScope.backToView = function () {
            $location.path("/app/promise/procurement/historypengadaan/view");
        }

        $scope.back = function () {
            $location.path("/app/promise/procurement/historypengadaan");
        }

        /*modal print*/
        $scope.goModalPrint = function (data) {
            var modalInstance = $modal.open({
                templateUrl: '/ModalPrint.html',
                controller: ModalInstancePrintCtrl,
                size: 'lg'
            });
        };

        /*---Modal Confirmation---*/
        var ModalInstancePrintCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {

            $scope.ok = function () {
                $modalInstance.dismiss('close');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstancePrintCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/

    }

    HistoryPengadaanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$filter'];

})();