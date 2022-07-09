(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengadaanIndex04Controller', PengadaanIndex04Controller);

    function PengadaanIndex04Controller($scope, $http, $resource, $location, $modal, $log, $rootScope, $state, RequestService) {
        var vm = this;

        vm.go = function (path) {
            $location.path(path);
        };

        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getInisiasliasiPengadaanListByUser')
            .success(
                function (data, status, headers, config) {
                    vm.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                //console.log('INFO get jenis Pengadaan error');
                $scope.loading = false;
            });

        vm.buttonEdit = function (pengadaan) {
            $state.go('app.promise.procurement-inisialisasi-edit04', {
                pengadaan: pengadaan
            });
        }

        /*
	  	$resource('server/pengadaan/pengadaan.json').query().$promise.then(function(pengadaanList) {
          vm.pengadaanList = pengadaanList;
      	}); */


        $rootScope.inisialisasiForm = {};
        $rootScope.HPSForm = {};

        vm.addInisialisasi = function (size) {
            $scope.loading = true;
            //cek terlebih dulu user punya angota kepanitia / pelaksana pengadaan
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getValidationNewPengaadaan')
                .success(function (resp) {
                    if (resp) {
                        var modalInstance = $modal.open({
                            templateUrl: '/addInisialisasi01.html',
                            controller: ModalInstanceCtrl,
                            size: size
                        });
                        var message = "INFO :<br/>";
                        modalInstance.result.then(function () {
                            //console.log('OOK input berhasil');
                        }, function () {
                            //console.log('INFO Cancel klik');
                        });
                    } else {
                        RequestService.modalInformation('Anda belum memiliki kepanitiaan!', 'danger');
                    }

                    $scope.loading = false;
                })
                .error(function (err) {
                    console.error("Error : " + err);
                    RequestService.modalInformation('Anda belum memiliki kepanitiaan!', 'danger');
                    $scope.loading = false;
                })

        };
        var ModalInstanceCtrl = function ($scope, $rootScope, $modalInstance) {
            $scope.ok = function () {
                $rootScope.inisialisasiForm.jenisPengadaan = $scope.jenisPengadaan;
                $rootScope.inisialisasiForm.metodaPengadaan = $scope.metodaPengadaan;
                $rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa = $scope.kualifikasiPenyediaBarangJasa;
                var statusAdd = true;
                var message = "";
                if ($rootScope.inisialisasiForm.metodaPengadaan == null) {
                    message += "<strong>Metoda Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }

                //console.log("kualifikasiPenyediaBarangJasa");
                //console.log($scope.kualifikasiPenyediaBarangJasa);
                //console.log($rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa);

                if (statusAdd) {
                    $scope.loading = true;
                    $modalInstance.close('closed');
                    $location.path('/app/promise/procurement/inisialisasi/add04');
                } else {
                    $scope.message = message;
                }

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceCtrl.$inject = ['$scope', '$rootScope', '$modalInstance'];
    }

    PengadaanIndex04Controller.$inject = ['$scope', '$http', '$resource', '$location', '$modal', '$log', '$rootScope', '$state', 'RequestService'];

})();