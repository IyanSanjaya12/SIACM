/**=========================================================
 * Module: JadwalPengadaanController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('JadwalPengadaanController', JadwalPengadaanController);

	function JadwalPengadaanController($scope, $http, $rootScope, $resource, $location, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state) {
        var form = this;
        form.dtInstance = {};
        form.tempObj = [];
		$rootScope.pengadaan = {};
		
		$scope.getViewDetil = function (purchaseRequestPengadaan) {
			$rootScope.pengadaan = purchaseRequestPengadaan.pengadaan;
			$location.path("/app/promise/procurement/jadwalpengadaan/detil2");
		};

        form.getDataList = function () {
            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/inisialisasi/getPengadaanWithPurchaseRequestListByUserWithNewPagination', {});
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
		      DTColumnBuilder.newColumn(null).withTitle('Nama Pengadaan')
		                .renderWith(function (data, type, full, meta) {
                    return data.pengadaan.namaPengadaan;
                }),
              
              DTColumnBuilder.newColumn(null).withTitle('No.Purchase Request')
		                .renderWith(function (data, type, full, meta) {
		            return data.purchaseRequest.prnumber;
		        }),
              DTColumnBuilder.newColumn(null).withTitle('Tanggal')
		                .renderWith(function (data, type, full, meta) {
                    return ($filter('date')(data.pengadaan.tanggalNotaDinas, 'dd-MM-yyyy'));
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Tahapan')
		                .renderWith(function (data, type, full, meta) {
                    return data.pengadaan.tahapanPengadaan.tahapan.nama;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                            form.tempObj[data.id] = data;
                    return (typeof data.pengadaan.tahapanPengadaan.id !== 'undefined' && data.pengadaan.tahapanPengadaan.id !=1) ? '<a  class="view-order badge bg-primary" ng-click="getViewDetil(form.tempObj['+data.id+'])"><em class="fa fa-search"></em></a> ' : '<a  class="view-order badge bg-success" ng-click="getViewDetil(form.tempObj['+data.id+'])"><em class="fa fa-pencil fa-lg"></em></a>';
                })
	            ];
        }
        form.getDataList();

        
         //Hari Libur
        $scope.getHariLibur = function () {
            $scope.loadingHariLibur = true;
            $http.get($rootScope.backendAddress + '/procurement/master/hari-libur/get-list')
                .success(function (data,
                    status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        var date = new Date(data[i].tanggal);
                        date.setHours(12);
                        data[i].tanggal=date;
                    }
                    $rootScope.hariLiburList=data;
                    $scope.loadingHariLibur = false;
                    return data;
                }).error(function (data, status, headers, config) {
                    $scope.loadingHariLibur = false;
                });
        }
        $scope.getHariLibur();

	}

	JadwalPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state'];

})();