/**=========================================================
 * Module: EvaluasiAdministrasiController.js
 * Author: H.R
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenetapanPemenangController', PenetapanPemenangController);

	function PenetapanPemenangController($http, $scope, $rootScope, $resource, $location) {
		var form = this;

		$scope.loading = true;
		var tahapan = 17; //masih hardcode penetapan, 15 negosiasi
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + tahapan)
			.success(function (data, status, headers, config) {
				form.pengadaanList = data;
				//tahapan = 15; //negosiasi
				/*$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/' + tahapan)
					.success(function (data, status, headers, config) {
						for (var i = 0; i < data.length; i++) {
							form.pengadaanList.push(data[i]);
						}
						$scope.loading = false;
					})
					.error(function (data, status, headers, config) {});*/
				$scope.loading = false;
			})
			.error(function (data, status, headers, config) {});

		form.detail = function (list) {
			$rootScope.pengadaanPenetapanPemenang = list;
			var sistemEvaluasiPenawaranId = list.sistemEvaluasiPenawaran.id;
			var jenisPenawaranId = list.jenisPenawaran.id;
			/*console.log('sistemEvaluasiPenawaranId : ' +sistemEvaluasiPenawaranId);
			console.log('jenisPenawaranId : ' +jenisPenawaranId);
			sistemEvaluasiPenawaranId = 1: sistem gugur; 2: merit point; 3: negoisasi teknis dan harga
			jenisPenawaranId = 1: total; 2: satuan*/
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 1) {
				$location.path('app/promise/procurement/penetapanpemenang/totalSistemGugur');
			};
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 2) {
				$location.path('app/promise/procurement/penetapanpemenang/totalMeritPoint');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 1) {
				$location.path('app/promise/procurement/penetapanpemenang/satuanSistemGugur');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 2) {
				$location.path('app/promise/procurement/penetapanpemenang/satuanMeritPoint');
			};
		};
	}

	PenetapanPemenangController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location'];

})();