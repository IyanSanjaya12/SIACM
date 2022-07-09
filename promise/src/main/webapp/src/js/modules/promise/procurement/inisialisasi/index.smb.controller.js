/**=========================================================
 * Module: PengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengadaanSMBController', PengadaanSMBController);

    function PengadaanSMBController($scope, $http, $resource, $location, $modal, $log, $rootScope, $state) {
        var vm = this;

        vm.go = function (path) {
            $location.path(path);
        };

        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/listPengadaanSMB')
            .success(
                function (data, status, headers, config) {
                    vm.pengadaanList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                console.log('INFO get jenis Pengadaan error');
                $scope.loading = false;
            });


        /*
	  	$resource('server/pengadaan/pengadaan.json').query().$promise.then(function(pengadaanList) {
          vm.pengadaanList = pengadaanList;
      	}); */


        $rootScope.inisialisasiForm = {};
        $rootScope.HPSForm = {};

        vm.addInisialisasi = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/addInisialisasi01.html',
                controller: InitModalInstanceCtrl,
                size: size
            });
            var message = "INFO :<br/>";
            modalInstance.result.then(function () {
                //console.log('OOK input berhasil');
            }, function () {
                //console.log('INFO Cancel klik');
            });
        };

        $scope.view = function (pengadaan) {
            $rootScope.pengadaan = pengadaan;
            $location.path('/app/promise/procurement/inisialisasi/edit/smb');
        }


        $scope.updateKategoriPengadaan = function () {
            if (typeof $scope.kategoriPengadaan.selected !== 'undefined') {
                $rootScope.inisialisasiForm.kategoriPengadaan = $scope.kategoriPengadaan.selected;
            }
        };
        var InitModalInstanceCtrl = function ($scope, $rootScope, $modalInstance) {
            $scope.kategoriPengadaan = {};
            $http.get($rootScope.backendAddress + '/procurement/master/kategoriPengadaanServices/getList')
                .success(function (data, status, headers, config) {    
                $scope.kategoriPengadaanList = data;
                }).error(function (data, status, headers, config) {});
            if (typeof $rootScope.inisialisasiForm.kategoriPengadaan !== 'undefined') {
                $scope.kategoriPengadaan.selected = {
                    id: $rootScope.inisialisasiForm.kategoriPengadaan.id,
                    nama: $rootScope.inisialisasiForm.kategoriPengadaan.nama
                };
            }
            
            $scope.metodePengadaan = {};
            $http.get($rootScope.backendAddress + '/procurement/master/metodePengadaanServices/getMetodePengadaanList')
                .success(function (data, status, headers, config) {    
                $scope.metodePengadaanList = data;
                }).error(function (data, status, headers, config) {});
            if (typeof $rootScope.inisialisasiForm.metodePengadaan !== 'undefined') {
                $scope.metodePengadaan.selected = {
                    id: $rootScope.inisialisasiForm.metodePengadaan.id,
                    nama: $rootScope.inisialisasiForm.metodePengadaan.nama
                };
            }
            $scope.ok = function () {
                $rootScope.inisialisasiForm.kategoriPengadaan = $scope.kategoriPengadaan.selected;
                $rootScope.inisialisasiForm.metodePengadaan = $scope.metodePengadaan.selected;
                var statusAdd = true;
                var message = "";
                if ($rootScope.inisialisasiForm.kategoriPengadaan == null) {
                    message = "<strong>Jenis Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }
                if ($rootScope.inisialisasiForm.metodePengadaan == null) {
                    message += "<strong>Metoda Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }

                if (statusAdd) {
                    $scope.loading = true;
                    $modalInstance.close('closed');
                    $location.path('/app/promise/procurement/inisialisasi/add/smb');
                } else {
                    $scope.message = message;
                }

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        InitModalInstanceCtrl.$inject = ['$scope', '$rootScope', '$modalInstance'];
    }

    PengadaanSMBController.$inject = ['$scope', '$http', '$resource', '$location', '$modal', '$log', '$rootScope', '$state'];

})();
