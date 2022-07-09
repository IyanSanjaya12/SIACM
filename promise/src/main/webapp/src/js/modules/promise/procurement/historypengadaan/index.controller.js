/**=========================================================
 * Module: HistoryPengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HistoryPengadaanController', HistoryPengadaanController);

    function HistoryPengadaanController($scope, $http, $rootScope, $resource, $location, $filter) {
        var form=this;
        form.data={};
        $rootScope.pengadaan = {};

        $scope.getViewDetil = function (pengadaan) {
            $rootScope.pengadaanView = pengadaan;
            $location.path("/app/promise/procurement/historypengadaan/view");
        };

        $scope.getIndexPengadaan = function () {
            $scope.loading = true;
            //console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList')
                .success(function (data, status, headers, config) {
                    $scope.jadwalPengadaanList = data;
                    $scope.loading = false;
                })
                .error(function (data, status, headers, config) {
                    console.log('Error loading jenis pengadaan');
                    $scope.loading = false;
                });

        };
        //$scope.getIndexPengadaan();

        $http.get($rootScope.backendAddress + '/procurement/master/jenisPengadaanServices/getJenisPengadaanList')
            .success(function (data, status, headers, config) {
                $scope.jenisList = [];
                var obj = {
                    id: 0,
                    nama: 'semua'
                }
                $scope.jenisList.push(obj);
                for (var i = 0; i < data.length; i++) {
                    $scope.jenisList.push(data[i]);
                }
            })

        $scope.getList = function () {
            var jenis = null;
            var nomor = null;
            var startDate = $filter('date')($scope.tglAwal, 'yyyy-MM-dd');
            var endDate = $filter('date')($scope.tglAkhir, 'yyyy-MM-dd');
            if (typeof form.data.jenisPengadaan !== 'undefined') {
                if (form.data.jenisPengadaan.id == 0) {
                    jenis = null;
                } else {
                    jenis = form.data.jenisPengadaan.id;
                }
            }
            if (typeof $scope.nomor !== 'undefined') {
                if ($scope.nomor.length == 0) {
                    nomor = null;
                } else {
                    nomor = $scope.nomor.replace('/', '%252F');
                }
            }
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanByDateJenisNomor/' + startDate + '/' + endDate + '/' + jenis + '/' + nomor, {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    $scope.list = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }

        $scope.search = function () {
            if (typeof $scope.tglAwal === 'undefined' || typeof $scope.tglAkhir === 'undefined') {
                alert('isi tanggal terlebih dahulu');
            } else {
                $scope.getList();
            }
        }


        $scope.disabled = function (date, mode) {
            return false;
        };
        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.maxDate = new Date(2020, 5, 22);
        $scope.toggleMin();
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[4];

        $scope.tglAwalOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglAwalOpened = true;
        };
        $scope.tglAkhirOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglAkhirOpened = true;
        };

    }

    HistoryPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();
