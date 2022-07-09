
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('DaftarPengadaanKontrakController', DaftarPengadaanKontrakController);

	function DaftarPengadaanKontrakController($scope, $http, $rootScope, $resource, $location, $state,RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService) {
		var form = this;
        form.dtInstance = {};
        form.tempObj = [];
        
        form.getPengadaanList = function () {
            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/KontrakManajemenServices/getNewKontrakListWithNewPagination', {});
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
		      DTColumnBuilder.newColumn(null).withTitle('Vendor')
		                .renderWith(function (data, type, full, meta) {
                    return data.vendor ? data.vendor.nama:'';
                }),
		     

		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                            data.pengadaan.vendor = data.vendor
                            form.tempObj[data.pengadaan.id] = data.pengadaan;
                    return '<a class="view-order badge bg-primary" ng-click="form.edit(form.tempObj['+data.pengadaan.id+'])" tooltip-placement="top" tooltip="View">' +
                        '<em class="fa fa-search"></em></a>';

                })
	            ];
        }
		form.getPengadaanList();
		
		form.edit = function (jp) {
			$state.go('app.promise.procurement-manajemenkontrak-createdatakontrak', {dataPengadaan: jp});
		};
	}

	DaftarPengadaanKontrakController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService'];

})(); 