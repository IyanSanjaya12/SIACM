/**
 * ========================================================= 
 * Module: negosiasi.vendor.index.controller.js
 * Author: Reinhard
 * 
 * =========================================================
 */
(function () {
	'use strict';

	angular.module('naut').controller('NegosiasiVendorController',
		NegosiasiController);

	function NegosiasiController($scope, $http, $rootScope, $resource, $location) {

		var negosiasiIndex = this;

		$scope.getPengadaanList = function () {
			$scope.loading = true;
            var tahapan=15;//masih hardcode
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/' + $rootScope.userLogin.user.id+'/'+tahapan).success(
				function (data, status, headers, config) {
					negosiasiIndex.pengadaanList = data;
					$scope.loading = false;
				}).error(function (data, status, headers, config) {
				$scope.loading = false;
			});
		};
		$scope.getPengadaanList();


		negosiasiIndex.edit = function (pengadaan) {

			$rootScope.detilPengadaan = pengadaan;
			$rootScope.isEditable = true;

			/* Cek Jenis penawaran
        	promise_t1_jenis_penawaran 1 = Total 
        	promise_t1_jenis_penawaran 2 = Satuan*/

			if (pengadaan.jenisPenawaran.id == 1) {
				$location.path('/appvendor/promise/procurement/negosiasi/vendor/total/detail/' + pengadaan.id);
			} else {
				/*alert ( 'Pengadaan satuan vendor is not impelemented yet! ');*/
				$location.path('/appvendor/promise/procurement/negosiasi/vendor/satuan/detail/' + pengadaan.id);
			}
		};
	}

	NegosiasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'
    ];

})();