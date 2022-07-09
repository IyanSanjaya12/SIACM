(function() {
	'use strict';

	angular
		.module('naut')
		.controller('ConditionalPriceViewController', ConditionalPriceViewController);

	function ConditionalPriceViewController($scope, $http, $rootScope, $resource, $location, toaster, $stateParams, RequestService, $log, $state) {
		
		var vm = this;
		var dataConditionalPrice;
		
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.conditionalPrice = ($stateParams.conditionalPrice != undefined) ? $stateParams.conditionalPrice : {};
		vm.list = {};

		if (vm.toDo == "Edit") {
			if (vm.conditionalPrice.tipe.id == 1) {
				vm.list.tipe = {
					id : 1,
					nama : 'Header / Kena price berdasarkan Total'
				}
			} else {
				vm.list.tipe = {
					id : 2,
					nama : 'Item / tiap satuan kena price'
				}
			}
			if (vm.conditionalPrice.fungsi.id == -1) {
				vm.list.fungsi = {
					id : -1,
					nama : 'Mengurangi Harga'
				}
			} else {
				vm.list.fungsi = {
					id : 1,
					nama : 'Menambah Harga'
				}
			}
			if (vm.conditionalPrice.isPersentage.id == 0) {
				vm.list.persentasi = {
					id : 0,
					nama : 'Nilai'
				}
			} else {
				vm.list.persentasi = {
					id : 1,
					nama : 'Nilai Persentase'

				}
			}
		}

		$scope.tipeList = [ {
			id : 1,
			nama : 'Header / Kena price berdasarkan Total'
		}, {
			id : 2,
			nama : 'Item / tiap satuan kena price'
		} ];

		$scope.fungsiList = [ {
			id : -1,
			nama : 'Mengurangi Harga'
		}, {
			id : 1,
			nama : 'Menambah Harga'
		} ];

		$scope.persentasiList = [ {
			id : 0,
			nama : 'Nilai'
		}, {
			id : 1,
			nama : 'Nilai Persentase'
		} ];

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					vm.conditionalPrice.tipe = vm.list.tipe.id;
					vm.conditionalPrice.fungsi = vm.list.fungsi.id;
					vm.conditionalPrice.isPersentage = vm.list.persentasi.id;
					$scope.saveData();
				}
			});
		}

		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageKode = '';
			vm.errorMessageNama = '';
			vm.errorMessageTipe = '';
			vm.errorMessageFungsi = '';
			vm.errorMessagePersentasi = '';

			if (typeof vm.conditionalPrice.kode === 'undefined' || vm.conditionalPrice.kode == '') {
				vm.errorMessageKode = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.conditionalPrice.nama == 'undefined' || vm.conditionalPrice.nama == '') {
				vm.errorMessageNama = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.list.tipe == 'undefined' || vm.list.tipe == '') {
				vm.errorMessageTipe = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.list.fungsi == 'undefined' || vm.list.fungsi == '') {
				vm.errorMessageFungsi = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.list.persentasi == 'undefined' || vm.list.persentasi == '') {
				vm.errorMessagePersentasi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}

		$scope.saveData = function() {
			var url = '';
			if (vm.toDo == "Add") {
				url = '/procurement/master/conditionalPriceServices/insert'
			} else if (vm.toDo == "Edit") {
				url = '/procurement/master/conditionalPriceServices/update'
			}
			RequestService.doPOSTJSON(url, vm.conditionalPrice)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorMessageKode != undefined) {
								vm.errorMessageKode = "promise.procurement.master.conditionalprize.error.kode_sama";
							}
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-conditionalprize');
						}

					}
				}, function error(response) {
					$log.info("proses gagal");
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-conditionalprize');
		}
	}
	
	ConditionalPriceViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$stateParams', 'RequestService', '$log', '$state' ];

})();
