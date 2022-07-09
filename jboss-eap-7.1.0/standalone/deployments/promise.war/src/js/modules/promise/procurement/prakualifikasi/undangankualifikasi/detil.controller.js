/**=========================================================
 * Module: UndanganKualifikasiDetilController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('UndanganKualifikasiDetilController', UndanganKualifikasiDetilController);

	function UndanganKualifikasiDetilController($scope, $http, $rootScope, $resource, $location, ngTableParams, $modal) {
		$scope.pengadaan = $rootScope.pengadaan;
        $scope.pengadaanId=$scope.pengadaan.id;
		//console.log('info pengadaan '+JSON.stringify($scope.pengadaan));

		$scope.getDaftarPengadaanStatus = function (pengadaanId, userId) {
			$scope.loadingValidasiPendaftaran = true;
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pendaftaranVendorPraKualifikasiServices/getPendaftaranVendorPraKualifikasiByVendorUserIdAndPengadaanId/' + userId + '/' + pengadaanId)
				.success(function (data,
					status, headers, config) {
					if (typeof data.vendor !== 'undefined') {
						//console.log('INFO data : ' + JSON.stringify(data));
						if (data.vendor.user == userId) {
							$scope.loadingValidasiPendaftaran = false;
							$scope.btnDaftarPengadaanStatus = false;
						} else {
							$scope.loadingValidasiPendaftaran = false;
							$scope.btnDaftarPengadaanStatus = true;
						}
					} else {
						$scope.loadingValidasiPendaftaran = false;
						$scope.btnDaftarPengadaanStatus = true;
					}
				}).error(function (data, status, headers, config) {
					$scope.loadingValidasiPendaftaran = false;
				});
		};
		$scope.getDaftarPengadaanStatus($scope.pengadaan.id, $rootScope.userLogin.user.id);

		//Vendor Requirement
		$scope.getVendorRequirement = function (pengadaanId) {
			$scope.loadingVendorRequirement = true;
			$http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementListByPengadaanId/' + pengadaanId)
				.success(function (data,
					status, headers, config) {
					$scope.vendorRequirement = data;
					$scope.loadingVendorRequirement = false;
				}).error(function (data, status, headers, config) {
					$scope.loadingVendorRequirement = false;
				});
		};
		$scope.getVendorRequirement($scope.pengadaan.id);

		//Jadwal Pengadaan	
		$scope.getJadwalPengadaan = function (pengadaanId) {
			$scope.loadingJadwalPengadaan = true;
			$http.get($rootScope.backendAddress + '/procurement/prakualifikasi/jadwalKualifikasiServices/getByPengadaanId/' + pengadaanId)
				.success(function (data, status, headers, config) {
					$scope.jadwalPengadaanList = data;
					//console.log('info pengadaan : ' + JSON.stringify(data));
					$scope.loadingJadwalPengadaan = false;
				})
				.error(function (data, status, headers, config) {
					//console.log('Error loading jenis pengadaan');
					$scope.loadingJadwalPengadaan = false;
				});
		};
		$scope.getJadwalPengadaan($scope.pengadaan.id);

		//hasilpendaftaran
		$scope.btnDaftarPengadaan = function (size) {
			$scope.loadingDaftarPengadaan = true;
			$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
				.success(function (data, status, headers, config) {
					if (typeof data.id !== 'undefined') {
						var vendor = data;
						$http({
								method: 'POST',
								url: $rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pendaftaranVendorPraKualifikasiServices/insertPendaftaranVendorPraKualifikasi',
								headers: {
									'Content-Type': 'application/x-www-form-urlencoded'
								},
								transformRequest: function (obj) {
									var str = [];
									for (var p in obj)
										str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
									return str.join("&");
								},
								data: {
									tanggalDaftar: "",
									nomorPendaftaran: "",
									pengadaanId: $scope.pengadaan.id,
									vendorId: vendor.id
								}
							}).success(function (data, status, headers, config) {
								$rootScope.vendor = vendor;
								var modalInstance = $modal.open({
									templateUrl: '/hasilpendaftaran.html',
									controller: ModalInstanceUndanganVendorCtrl,
									size: size
								});
								$scope.loadingDaftarPengadaan = false;
							})
							.error(function (data, status, headers, config) {
								$scope.messageError = "Daftar Pengadaan Gagal";
								$scope.loadingDaftarPengadaan = false;
							});
					} else {

						$scope.loadingDaftarPengadaan = false;
					}
				})
				.error(function (data, status, headers, config) {
					//console.log('Error loading loadingDaftarPengadaan');
					$scope.loadingDaftarPengadaan = false;
				});
		};

		var ModalInstanceUndanganVendorCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, $location, $filter) {
			$scope.pengadaan = $rootScope.pengadaan;
			$scope.vendor = $rootScope.vendor;
			$scope.token = $rootScope.userLogin.token;

			$scope.dokumenPengadaan = function () {
				$http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + $rootScope.pengadaan.id)
					.success(function (data, status, headers, config) {
						$scope.download = data;
					})
					.error(function (data, status, headers, config) {});
			};
			$scope.dokumenPengadaan();

			$scope.downloadFile = function (file) {
				$http({
					method: 'POST',
					url: $rootScope.backendAddress + '/procurement/prakualifikasi/pengambilanDokumenKualifikasiServices/insert',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					transformRequest: function (obj) {
						var str = [];
						for (var p in obj)
							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data: {
						tglAmbil: $filter('date')(new Date(), "dd-MM-yyyy"),
						pengadaan: $scope.pengadaan.id,
						vendor: $scope.vendor.id
					}
				})
				.success(function (data, status, headers, config) {
					//window.location.assign($rootScope.backendAddressIP+'/promise/files/'+file.nama);
					window.open($rootScope.viewUploadBackendAddress+"/"+file.path, '_blank');
					//console.log('Download File : ' + file.nama+', Path : '+file.path);
				});
			};

			$scope.printDiv = function (divName) {
				var printContents = document.getElementById(divName).innerHTML;
				var popupWin = window.open('', '_blank', 'width=300,height=300');
				popupWin.document.open()
				popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</html>');
				popupWin.document.close();
			}

			$scope.cancel = function () {
				$location.path("/appvendor/promise/procurement/prakualifikasi/undanganvendorkualifikasi");
				$modalInstance.dismiss('cancel');
			};
		};		
		ModalInstanceUndanganVendorCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', '$location', '$filter'];

		$scope.btnKembali = function () {
			$location.path('/appvendor/promise/procurement/prakualifikasi/undanganvendorkualifikasi');
		};
	}

	UndanganKualifikasiDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'ngTableParams', '$modal'];

})();