/**=========================================================
 * Module: PengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengadaanController', PengadaanController);

    function PengadaanController($scope, $http, $resource, $location, $modal, $log, $rootScope, $state) {
        var vm = this;

        vm.go = function (path) {
            $location.path(path);
        };

        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList')
            .success(
                function (data, status, headers, config) {
                    vm.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                //console.log('INFO get jenis Pengadaan error');
                $scope.loading = false;
            });

        vm.buttonEdit = function(pengadaan) {
        	$state.go('app.promise.procurement-inisialisasi-edit-bumn', {pengadaan:pengadaan});
        }

        /*
	  	$resource('server/pengadaan/pengadaan.json').query().$promise.then(function(pengadaanList) {
          vm.pengadaanList = pengadaanList;
      	}); */


        $rootScope.inisialisasiForm = {};
        $rootScope.HPSForm = {};

        vm.addInisialisasi = function (size) {
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
        };
        var ModalInstanceCtrl = function ($scope, $rootScope, $modalInstance) {
            $scope.ok = function () {
                $rootScope.inisialisasiForm.jenisPengadaan = $scope.jenisPengadaan;
                $rootScope.inisialisasiForm.metodaPengadaan = $scope.metodaPengadaan;
                $rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa = $scope.kualifikasiPenyediaBarangJasa;
                var statusAdd = true;
                var message = "";
                if ($rootScope.inisialisasiForm.jenisPengadaan == null) {
                    message = "<strong>Jenis Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }
                if ($rootScope.inisialisasiForm.metodaPengadaan == null) {
                    message += "<strong>Metoda Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }
                if ($rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa == null) {
                    message += "<strong>Kualifikasi Penyedia Barang dan Jasa</strong> harus dipilih<br/>";
                    statusAdd = false;
                }
                
                //console.log("kualifikasiPenyediaBarangJasa");
                //console.log($scope.kualifikasiPenyediaBarangJasa);
                //console.log($rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa);
                
                if (statusAdd) {
                    $scope.loading = true;
                    $modalInstance.close('closed');
                    $location.path('/app/promise/procurement/inisialisasi/add/bumn');
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

    PengadaanController.$inject = ['$scope', '$http', '$resource', '$location', '$modal', '$log', '$rootScope', '$state'];

})();