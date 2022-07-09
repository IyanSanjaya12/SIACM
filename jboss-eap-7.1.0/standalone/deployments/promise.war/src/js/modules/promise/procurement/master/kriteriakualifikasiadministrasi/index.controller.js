/**============================================================
 * Module: MasterKriteriaKualifikasiAdministrasiController.js
 =============================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('MasterKriteriaKualifikasiAdministrasiController', MasterKriteriaKualifikasiAdministrasiController);

	function MasterKriteriaKualifikasiAdministrasiController($http, $rootScope, $resource, $location) {
		var vm = this;

		vm.go = function (path) {
			$location.path(path);
		};
		vm.getMasterKriteriaKualifikasiAdministrasi = function () {
			vm.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/getList')
				.success(
					function (data, status, headers, config) {
						vm.masterKriteriaKualifikasiAdministrasiList = data;
						vm.loading = false;
					})
				.error(function (data, status, headers, config) {
					vm.loading = false;
				});
		}
		vm.getMasterKriteriaKualifikasiAdministrasi();

		vm.add = function () {
			$location.path('/app/promise/procurement/master/kriteriaKualifikasiAdministrasi/tambah');
		};

		vm.del = function (id) {
			var konfirmasi = confirm("Apakah anda yakin ingin menghapus data Kriteria");
			if (konfirmasi) {
				vm.loading = true;
				$http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/delete/' + id).success(function (data, status, headers, config) {
					$http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/getList').success(
						function (data, status, headers, config) {
							vm.masterKriteriaAdministrasiList = data;
							vm.loading = false;
						}).error(function (data, status, headers, config) {});
				}).error(function (data, status, headers, config) {});
			}
		};
		vm.edit = function (jp) {
			$rootScope.masterKriteriaKualifikasiAdministrasiEdit = jp;
			$location.path('/app/promise/procurement/master/kriteriaKualifikasiAdministrasi/edit');
		};
	}

	MasterKriteriaKualifikasiAdministrasiController.$inject = ['$http', '$rootScope', '$resource', '$location'];

})();