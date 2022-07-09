(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengadaanUlangController', PengadaanUlangController);

    function PengadaanUlangController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state) {
        var form = this;
        form.dtInstance = {};
        form.tempObj = [];

        form.getDataList = function () {
            form.dtOptions = RequestService
                .requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/pengadaangagal/PengadaanUlangServices/findPengadaanGagal', {});
            form.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);

            form.dtColumns = [
		      DTColumnBuilder.newColumn(null).withTitle('Nomor').notSortable(),
		      DTColumnBuilder.newColumn(null).withTitle('No. Pengadaan')
		                .renderWith(function (data, type, full, meta) {
                    return data.pengadaan.nomorNotaDinas;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Nama')
		                .renderWith(function (data, type, full, meta) {
                    return data.pengadaan.namaPengadaan;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Tahapan')
		                .renderWith(function (data, type, full, meta) {
                    return data.pengadaan.tahapanPengadaan.tahapan.nama;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('No. Berita Acara')
		                .renderWith(function (data, type, full, meta) {

                    return data.noBeritaAcara;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Tgl Berita Acara')
		                .renderWith(function (data, type, full, meta) {
                    return ($filter('date')(data.tglBeritaAcara, 'dd-MM-yyyy'));
                }),

		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                    form.tempObj[data.id] = data;
                    return '<a class="btn btn-primary btn-sm btn-addon" ng-click="btnAddPengadaanUlang(form.tempObj[' + data.id + '])"><em class="fa fa-plus"></em>Buat Pengadaan Ulang</a>';

                })
	       ];
        }
        form.getDataList();

        //addPengadaanUlang
        $scope.btnAddPengadaanUlang = function (data) {
            RequestService.modalConfirmation("Apakah Anda yakin ingin melakukan Pengadaan Ulang " + data.pengadaan.namaPengadaan + " dengan No. " + data.pengadaan.nomorNotaDinas)
                .then(function () {
                    $rootScope.pengadaanGagalSelected = data;
                    $scope.proseSimpanPengadaanUlang();
                    console.log("Add pegndaan ulang!" + JSON.stringify(data));
                });
        };

        $scope.proseSimpanPengadaanUlang = function () {
            $scope.loading = false;
            $rootScope.anggaranSelected = {};
            var modalInstance = $modal.open({
                templateUrl: 'proses_simpan.html',
                controller: ModalProseSimpanPengadaanUlangController,
                size: 'lg'
            });
        };
        var ModalProseSimpanPengadaanUlangController = function ($http, $modalInstance, $scope, $rootScope, $resource, $location, $state) {
            $scope.loadingSaving = true;
            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/pengadaangagal/PengadaanUlangServices/create',
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
                        pengadaanGagalId: $rootScope.pengadaanGagalSelected.id
                    }
                })
                .success(function (data, status, headers, config) {
                    $scope.loadingSaving = false; 
                    $rootScope.inisialisasiForm = {};
                    $rootScope.HPSForm = {};
                    $state.go('app.promise.procurement-inisialisasi-edit04', {pengadaan:data});
                    $modalInstance.close();
                })
                .error(function (data) {
                    console.log("error post data!");
                    $scope.loadingSaving = false;
                    $modalInstance.close();
                });

        }
        ModalProseSimpanPengadaanUlangController.$inject = ['$http', '$modalInstance', '$scope', '$rootScope', '$resource', '$location', '$state'];
    }

    PengadaanUlangController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state'];

})();