(function () {
    'use strict';
    angular
        .module('naut')
        .controller('PengadaanGagalEditController', PengadaanGagalEditController);

    function PengadaanGagalEditController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams, $filter, RequestService) {
        var form = this;
        $scope.pengadaanId = $rootScope.pengadaanGagalSelected.pengadaan.id;
        console.log("test : "+JSON.stringify(form));
        form.noBA = $rootScope.pengadaanGagalSelected.noBeritaAcara;
        form.tglBA = $rootScope.pengadaanGagalSelected.tglBeritaAcara;
        form.keterangan = $rootScope.pengadaanGagalSelected.keterangan;
        form.pengadaanGagalId = $rootScope.pengadaanGagalSelected.id;

        // Datepicker
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        form.tglBAOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglBAOpened = true;
        };

        $scope.btnBack = function () {
            $location.path('/app/promise/procurement/pengadaangagal');
        }

        $scope.saveData = function () {
            
            RequestService.modalConfirmation('Anda yakin ingin mengubah Data Pengadaan Gagal ini ?')
                .then(function () {
                $scope.loadingSaving = true;
                    $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/pengadaangagal/PengadaanGagalServices/update',
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
                                pengadaanId: $scope.pengadaanId,
                                noBA: form.noBA,
                                tglBA: $filter('date')(form.tglBA,'dd-MM-yyyy'),
                                keterangan: form.keterangan,
                                pengadaanGagalId : form.pengadaanGagalId
                            }
                        })
                        .success(function (data, status, headers, config) {
                            $scope.loadingSaving = false;
                            $scope.btnBack();
                        })
                        .error(function (data) {
                            console.log("error post data!");
                            $scope.loadingSaving = false;
                        });
                });
        }

    }

    PengadaanGagalEditController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams', '$filter', 'RequestService'];
})();