(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlurPengadaan02Controller', AlurPengadaan02Controller);

    function AlurPengadaan02Controller(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal) {
        var form = this;

        var getAlurPengadaanList = function () {
            $scope.loading = true;
            RequestService.doGET('/procurement/master/AlurPengadaanServices/getList/')
                .then(function successCallback(data) {
                    form.alurPengadaanList = data;
                    form.loading = false;
                }, function errorCallback(response) {
                    $scope.loading = false;
                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                });
        }

        getAlurPengadaanList();

        form.add = function () {
            $rootScope.tahapanPengadaanList = $scope.tahapanPengadaanList;
            var modalInstance = $modal.open({
                templateUrl: 'tambah_alur_pengadaan.html',
                controller: ModalTambahAlurController,
                size: 'lg'
            });
            modalInstance.result.then(function () {
                //console.log("hello reload data");
                $location.path('app/promise/procurement/master/alurPengadaan02/tambahtahapan');
            });
        }
        var ModalTambahAlurController = function ($scope, $rootScope, $modalInstance, $filter, RequestService) {
            $scope.loading = true;
            $scope.kondisiPengadaan = 0;

            $http.get($rootScope.backendAddress + '/procurement/master/kondisiPengadaanServices/getList')
                .success(function (data) {
                    $scope.kondisiPengadaanList = data;
                    angular.forEach($scope.kondisiPengadaanList, function (value, index) {
                        var longName = value.jenisPengadaan.nama + ', ' + value.metodePengadaan.nama + ', ' + value.metodePenawaranHarga.nama + ', ' + value.jenisPenawaran.nama + ', ' + value.sistemEvaluasiPenawaran.nama;
                        value.longName = longName;
                    });
                    $scope.loading = false;
                })
                .error(function (data) {
                    $scope.loading = false;
                });

            $scope.simpan = function () {
                if ($scope.namaAlurPengadaan == '' || $scope.kondisiPengadaan == 0) {
                    RequestService.modalInformation('Nama alur pengadaan dan kondisi harus diisi!', 'danger')
                        .then(function (result) {
                            $scope.loading = false;
                        });
                } else {

                }
                var postForm = {
                    nama: $scope.namaAlurPengadaan,
                    kondisiPengadaan: $scope.kondisiPengadaan
                }
                var url = $rootScope.backendAddress+'/procurement/master/AlurPengadaanServices/create';
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
                        data: postForm
                    }).success(function (data, status, headers, config) {
                        $scope.loading = false;
                        $rootScope.alurPengadaanData = data;
                        $modalInstance.close('closed');
                    })
                    .error(function (data) {
                        $scope.loading = false;
                        console.error(data);
                    });
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalTambahAlurController.$inject = ['$scope', '$rootScope', '$modalInstance', '$filter', 'RequestService'];

        form.edit = function (data) {
            $rootScope.alurPengadaanData = data;
            $location.path('app/promise/procurement/master/alurPengadaan02/tambahtahapan');
        }

        form.del = function (id) {
            ModalService.showModalConfirmation('Apakah anda yakin akan menghapus data ini?').then(function (result) {
                ModalService.showModalInformationBlock();
                RequestService.doGET('/procurement/master/AlurPengadaanServices/delete/' + id)
                    .then(function successCallback(response) {
                        getAlurPengadaanList();
                        ModalService.closeModalInformation();
                    }, function errorCallback(response) {
                        ModalService.closeModalInformation();
                        ModalService.showModalInformation('Terjadi kesalahan pada system!');
                    });
            });
        }
        
    }

    AlurPengadaan02Controller.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal'];

})();