/**
 * ========================================================= Module:
 * addressBookController.js Controller for AddressBook
 * =========================================================
 */

(function() {
	'use strict';
	angular.module('naut').controller('addressBookController',
			addressBookcontroller);
	function addressBookcontroller($scope, RequestService, $state,
			DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter) {

		var vm = this;
		vm.tempData = [];
		vm.tempBillingAddress = [];
		vm.tempShippingAddress = [];
		vm.dtInstance = {};

		vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder
				.newOptions(),
				'/procurement/master/AddressBookServices/getList');
		vm.dtOptions.withOption('fnRowCallback',
				function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
					$compile(nRow)($scope);
					$("td:eq(0)", nRow).attr("align", "center");
					$("td:eq(3)", nRow).attr("align", "center");
					$("td:eq(4)", nRow).attr("align", "center");
				}).withOption('headerCallback',
				function(thead, data, start, end, display) {
					$compile(angular.element(thead).contents())($scope);
				});

		vm.dtColumns = [
				DTColumnBuilder
						.newColumn(null)
						.withTitle(
								'<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>')
						.notSortable().renderWith(
								function(data, type, full, meta) {
									return meta.row + 1;
								}),
				DTColumnBuilder
						.newColumn('addressLabel')
						.withTitle(
								'<span translate="promise.procurement.master.addressbook.list.addresslabel"></span>')
						.renderWith(function(data) {
							return data;
						}),
				DTColumnBuilder
						.newColumn('city')
						.withTitle(
								'<div style="text-align:center"><span translate="promise.procurement.master.addressbook.list.kota"></span></div>')
						.renderWith(function(data) {
							return data;
						}),
				/*KAI - 20201217 - [21150]*/
				/*DTColumnBuilder
						.newColumn('defaultBillingAddress')
						.withTitle(
								'Default Billing Address')
						.renderWith(
								function(data, type, full, meta) {
									vm.tempBillingAddress[meta.row] = data;
									return '<div class="c-checkbox"><label>'
											+ '<input type="checkbox" ng-model="vm.tempBillingAddress['
											+ meta.row
											+ ']" class="m-b shadow-z1 input-sm" disabled />'
											+ '<span class="fa fa-check m-sm"></span></label></div>';
								}),*/
				DTColumnBuilder
						.newColumn('defaultShippingAddress')
						.withTitle('<div style="text-align:center;width: 300px"><span translate="promise.procurement.master.addressbook.list.shippingaddress"></span></div>')
						.renderWith(
								function(data, type, full, meta) {
									vm.tempShippingAddress[meta.row] = data;
									return '<div class="c-checkbox"><label>'
											+ '<input type="checkbox" ng-model="vm.tempShippingAddress['
											+ meta.row
											+ ']" class="m-b shadow-z1 input-sm" disabled />'
											+ '<span class="fa fa-check m-sm"></span></label></div>';
								}),
				DTColumnBuilder.newColumn('status').withTitle('Status')
						.renderWith(function(data) {
							return (data) ? 'Active' : 'Not Active';
						}),
				DTColumnBuilder
						.newColumn(null)
						.withTitle('<span translate="template.tabel.aksi"></span></div>')
						.notSortable()
						.renderWith(
								function(data, type, full, meta) {
									vm.tempData[data.id] = data;
									return ' <button ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit" ng-click=" edit('
											+ data.id
											+ ')"></button> '
											+ ' <button class="btn btn-danger btn-sm btn-delete" ng-click=" del(vm.tempData['
											+ data.id
											+ '])" > '
											+ ' </button> ';
								}) ];

		DTInstances.getLast().then(function(instance) {
			vm.dtInstance = instance;
		});

		$scope.add = function() {
			$state.go('app.promise.procurement-addressbook-view', {
				toDo : 'Add'
			});
		}

		$scope.edit = function(addressBookId) {
			RequestService.doGET(
					"/procurement/master/AddressBookServices/getAddressBook/"
							+ addressBookId).then(function success(data) {
				$state.go('app.promise.procurement-addressbook-view', {
					toDo : 'Edit',
					dataAddressBook : data
				});
			}, function error(response) {
				RequestService.informError('Terjadi Kesalahan Pada System');
			});
		};

		$scope.del = function(addressBookId) {
			vm.url = '/procurement/master/AddressBookServices/delete';
			RequestService
					.modalConfirmation(
							'Apakah anda yakin untuk menghapus data?')
					.then(
							function(result) {
								RequestService
										.doPOST(vm.url, addressBookId)
										.success(
												function(data) {
													RequestService
															.informDeleteSuccess();
													vm.dtInstance.reloadData();
												})
										.error(
												function(data, status, headers,
														config) {
													RequestService
															.informError("Terjadi Kesalahan Pada System");
												})
							});
		}
	}
	addressBookcontroller.$inject = [ '$scope', 'RequestService', '$state',
			'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile',
			'$filter' ];
})();