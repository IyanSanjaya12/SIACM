/*rmt*/

(function () {
	'use strict';

	angular.module('naut').controller('SanggahanVendorController', NegosiasiController);

	function NegosiasiController($scope, $http, $rootScope, $resource, $location, $state) {

		var sanggahanIndex = this;

		$scope.getPengadaanList = function () {
			$scope.loading = true;
            var tahapan=18;// SANGGAHAN
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id+'/'+tahapan).success(
				function (data, status, headers, config) {
					sanggahanIndex.pengadaanList = data;
					$scope.loading = false;
				}).error(function (data, status, headers, config) {
				$scope.loading = false;
			});
		};

		sanggahanIndex.edit = function (pengadaan) {

			$rootScope.detilPengadaan = pengadaan;
			$rootScope.isEditable = true;

			/* Cek Jenis penawaran
        	promise_t1_jenis_penawaran 1 = Total, 2 = Satuan*/

			if (pengadaan.jenisPenawaran.id == 1) {
				$state.go('appvendor.promise.procurement-vendor-sanggahan-total-detail', {pengadaanId : pengadaan.id} );
			} else {
				$state.go('appvendor.promise.procurement-vendor-sanggahan-satuan-detail', {pengadaanId : pengadaan.id} );
			}
		};
		
		$scope.getPengadaanList();
	}

	NegosiasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location','$state'];

})();