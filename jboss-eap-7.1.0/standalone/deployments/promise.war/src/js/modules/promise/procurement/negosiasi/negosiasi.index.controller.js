/**
 * ========================================================= 
 * Module: negosiasi.index.controller.js
 * Author: Reinhard
 * 
 * =========================================================
 */
(function () {
	'use strict';

	angular.module('naut').controller('NegosiasiController',
		NegosiasiController);

	function NegosiasiController($scope, $http, $rootScope, $resource, $location) {
		var negosiasiIndex = this;
		$scope.loading = true;
        var tahapan=15//masih hard code
		$http.get(
				$rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan)
			.success(function (data, status, headers, config) {
				negosiasiIndex.pengadaanList = data;
				$scope.loading = false;
			})
			.error(function (data, status, headers, config) {
				$scope.loading = false;
			});
		
		negosiasiIndex.edit = function (pengadaan) {
			$rootScope.detilPengadaan = pengadaan;
			$rootScope.isEditable = true;

			/* Cek Jenis penawaran
        	promise_t1_jenis_penawaran 1 = Total 
        	promise_t1_jenis_penawaran 2 = Satuan*/
			if (pengadaan.jenisPenawaran.id == 1) {
				$location.path('/app/promise/procurement/negosiasi/total/detail/' + pengadaan.id);
			} else {
				$location.path('/app/promise/procurement/negosiasi/satuan/detail/' + pengadaan.id);
			}
		};
	}

	NegosiasiController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location'
    ];

})();