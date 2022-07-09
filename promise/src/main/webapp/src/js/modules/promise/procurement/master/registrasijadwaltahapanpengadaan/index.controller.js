(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiJadwalTahapanPengadaanController', RegistrasiJadwalTahapanPengadaanController);

	function RegistrasiJadwalTahapanPengadaanController($http, $scope, $rootScope, $resource, $location) {
		var form = this;

		form.go = function (path) {
			$location.path(path);
		};
		var getRegistrasiJadwalTahapanPengadaan = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/getList')
				.success(
					function (data, status, headers, config) {
						form.registrasiJadwalTahapanPengadaanList = data;
						$scope.loading = false;
					})
				.error(function (data, status, headers, config) {
					$scope.loading = false;
				});
		}
		getRegistrasiJadwalTahapanPengadaan();
		
		form.add = function () {
			$location.path('/app/promise/procurement/master/registrasijadwaltahapanpengadaan/tambah');
		};

		form.del = function (id) {
			var konfirmasi = confirm("Apakah anda yakin ingin menghapus data Registrasi Jadwal Tahapan Pengadaan");
			if (konfirmasi) {
				$scope.loading = true;
				$http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/delete/' + id).success(function (data, status, headers, config) {
					getRegistrasiJadwalTahapanPengadaan();
				}).error(function (data, status, headers, config) {});
			}
		};
		form.edit = function (jp) {
			$rootScope.indexRegistrasiJadwalTahapanPengadaan = jp;
			$location.path('/app/promise/procurement/master/registrasijadwaltahapanpengadaan/ubah');
		};
	}

	RegistrasiJadwalTahapanPengadaanController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location'];

})();