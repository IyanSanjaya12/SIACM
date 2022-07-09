/**=========================================================
 * Module: SPKController.js
 * Author: musisician
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SPKController', SPKController);

	function SPKController($scope, $http, $rootScope, $resource, $location) {
		var form = this;
		$scope.loading = true;

		$scope.getList = function () {
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNonJadwal/20', {
					ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					if (data.length > 0) {
						form.pengadaanList = data;
						if (form.pengadaanList !== undefined && form.pengadaanList !== null && form.pengadaanList.length > 0) {
							angular.forEach(form.pengadaanList, function (pengadaanValue, pengadaanKey) {
								if (pengadaanValue.jenisPenawaran.id === 1) {
									$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getPenetapanPemenangTotalByPengadaan/' + pengadaanValue.id)
										.success(function (data, status, headers, config) {
											if (data !== undefined && data !== null && data.length > 0) {
												form.pengadaanList[pengadaanKey].vendor = data[0].vendor;
											}
										});
								}
							});
						}
						$scope.loading = false;
					} else {
						$scope.loading = false;
					}
				})
				.error(function (data, status, headers, config) {
					$scope.loading = false;
				});
		}
		$scope.getList();

		form.view = function (data) {
			$rootScope.statusSPK = false;
			$rootScope.penetapanPemenang = data;

			$location.path('/app/promise/procurement/spk/view');
		};

	}

	SPKController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();