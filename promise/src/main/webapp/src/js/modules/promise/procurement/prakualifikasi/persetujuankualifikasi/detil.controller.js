(function () {
    'use strict';

    angular.module('naut').controller('PersetujuanKualifikasiPengadaanDetailController',
        PersetujuanKualifikasiPengadaanDetailController);

    function PersetujuanKualifikasiPengadaanDetailController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams) {
        var persetujuanDetil = this;
        $scope.pengadaan = $rootScope.detilPengadaan;

        persetujuanDetil.persetujuan = {};
        persetujuanDetil.persetujuan.pengadaanId = $scope.pengadaan.id;

        $scope.getHasilKualifikasi = function () {
            $scope.loadingHasilKualifikasi = true;
            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/hasilkualifikasi/findByPengadaanId/' + $scope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                    $scope.hasilKualifikasiList = data;
                    $scope.loadingHasilKualifikasi = false;

                })
                .error(function (data, status, headers, config) {
                    $scope.loadingHasilKualifikasi = false;
                });
        }
        $scope.getHasilKualifikasi();

        $scope.gotoIndex = function () {
            $location.path("/app/promise/procurement/prakualifikasi/persetujuankualifikasi");
        }

        // Datepicker
        persetujuanDetil.toggleMin = function () {
            persetujuanDetil.minDate = persetujuanDetil.minDate ? null : new Date();
        };
        persetujuanDetil.toggleMin();
        persetujuanDetil.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        persetujuanDetil.format = persetujuanDetil.formats[4];

        persetujuanDetil.tanggalPersetujuanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            persetujuanDetil.tanggalPersetujuanOpened = true;
        };
        
        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaan.id)
                    .success(function (data, status, headers, config) {
                        $scope.nextTahapan = data;
                    })
                    .error(function (data, status, headers, config) {});
            }
            /* END get Next Tahapan */
        $scope.getNextTahapan();

        //update pengadaan
        $scope.updatePengadaan = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
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
                    pengadaanId: $scope.pengadaan.id,
                    tahapanPengadaanId: ($scope.nextTahapan)
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        };

        $scope.saveData = function () {

			ModalService.showModalConfirmation().then(function (result) {
	            $scope.loadingSaving = true;
	            persetujuanDetil.persetujuan.tanggalPersetujuan = $filter('date')(persetujuanDetil.persetujuan.tanggalPersetujuan, 'dd-MM-yyyy');
	            var url = $rootScope.backendAddress + '/procurement/prakualifikasi/PersetujuanKualifikasiPengadaanServices/createPersetujuanPengadaan';
	
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
	                    data: persetujuanDetil.persetujuan
	                })
	                .success(function (data, status, headers, config) {
	                    $scope.updatePengadaan();
	                    $scope.statusSimpan = true;
	                    $scope.loadingSaving = false;
	                })
	                .error(function (data, status, headers, config) {
	                    $scope.loadingSaving = false;
	                });
				});
        }
    }	

    PersetujuanKualifikasiPengadaanDetailController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams'];

})();