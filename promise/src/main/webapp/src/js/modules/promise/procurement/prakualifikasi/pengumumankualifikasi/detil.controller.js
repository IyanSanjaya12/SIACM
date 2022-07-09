(function () {
	'use strict';

	angular.module('naut')
		.controller('PengumumanKualifikasiViewController', PengumumanKualifikasiViewController);

	function PengumumanKualifikasiViewController($scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $q, $timeout, $filter, ngTableParams) {
		var form = this;
		$scope.statusForm = false;
		$scope.isJasaNotEmpty = false;
		$scope.isMaterialNotEmpty = false;
		$scope.back = function () {
			$location.path('appvendor/promise/procurement/prakualifikasi/pengumumankualifikasi');
		}

		/* START Detail Pengadaan */
		if (typeof $rootScope.detilPengadaan !== 'undefined') {
			$scope.detilPengadaan = $rootScope.detilPengadaan;
		}
		$scope.pengadaanId = $scope.detilPengadaan.id;
		$scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
		/*---END Detail Pengadaan----*/
		
		$scope.getVendorByUser = function () {
			$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				if (typeof data !== 'undefined') {
					$scope.vendor = data.id;
					$scope.vendorData = data;
					$rootScope.dataVendor = data;
				} else {}
				$scope.loading = false;
			}).error(function (data, status, headers, config) {

			});
		}
		$scope.getVendorByUser();
        
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
		$scope.getJadwalPengadaan($scope.pengadaanId);


		/* check list pendaftaran vendor*/
		$scope.getListPendaftaranVendor = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pendaftaranVendorPraKualifikasiServices/getPendaftaranVendorPraKualifikasiByVendorUserIdAndPengadaanId/' + $rootScope.userLogin.user.id + '/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				if (typeof data.id !== 'undefined') {
					$scope.statusForm = true;
				} else {
					$scope.statusForm = false;
				}
			}).error(function (data, status, headers, config) {

			});
		}
		$scope.getListPendaftaranVendor();

		//save ke pendaftaran vendor
		$scope.pendaftaranVendor = function () {
			$scope.loading = true;
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
					pengadaanId: $scope.pengadaanId,
					vendorId: $scope.vendor,
					tanggalDaftar: $filter('date')(new Date(), "yyyy-MM-dd"),
					nomorPendaftaran: $scope.detilPengadaan.nomorNotaDinas
				}
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				$scope.goModalPengumuman();
				$scope.getListPendaftaranVendor();
			});
		};

		//validasi
		$scope.validate = function () {
			var valid = true;
			//logic of validation
			return valid;
		}

		//save
		$scope.saveData = function () {
			if ($scope.validate()) {
				$scope.pendaftaranVendor();
			} else {
				alert('something wrong');
			}

		}

		/*modal Pengumuman Pengadaan*/
		$scope.goModalPengumuman = function () {
			var modalInstance = $modal.open({
				templateUrl: '/ModalPengumumanKualifikasi.html',
				controller: ModalInstancePengumumanPengadaanCtrl,
			});
		};
	}

	var ModalInstancePengumumanPengadaanCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope, toaster, $filter) {
		$scope.dataVendor = $rootScope.dataVendor;
		$scope.detilPengadaan = $rootScope.detilPengadaan;
		$scope.token = $rootScope.userLogin.token;

		$scope.dokumenPengadaan = function () {
			$http.get($rootScope.backendAddress + '/procurement/pengadaan/dokumenPengadaanServices/getDokumenPengadaanByPengadaanIdList/' + $scope.detilPengadaan.id)
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
						pengadaan: $scope.detilPengadaan.id,
						vendor: $scope.dataVendor.id
					}
				})
				.success(function (data, status, headers, config) {
					//window.location.assign($rootScope.backendAddressIP+'/promise/files/'+file.nama);
					window.open($rootScope.viewUploadBackendAddress+"/"+file.path, '_blank');
					//console.log('Download File : ' + file.nama+', Path : '+file.path);
				});
		};

		$scope.cancel = function () {
			$modalInstance.dismiss('cancel');
		};
		$scope.printDiv = function (divName) {
			var printContents = document.getElementById(divName).innerHTML;
			var popupWin = window.open('', '_blank', 'width=300,height=300');
			popupWin.document.open()
			popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</html>');
			popupWin.document.close();
		}
	};
	ModalInstancePengumumanPengadaanCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'toaster', '$filter'];
	/*------*/

	PengumumanKualifikasiViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$q', '$timeout', '$filter', 'ngTableParams'];

})();