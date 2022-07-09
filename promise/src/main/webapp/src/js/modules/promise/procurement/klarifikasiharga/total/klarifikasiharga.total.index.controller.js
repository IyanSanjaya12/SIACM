(function () {
	'use strict';

	angular.module('naut')
		.controller('KlarifikasiHargaTotalController', KlarifikasiHargaTotalController);

	function KlarifikasiHargaTotalController($scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $q, $timeout, $filter, $state, ngTableParams) {
		var form = this;
		//console.log("Cek > mamat ");
		$scope.isDisabled = false;
		/* START Detail Pengadaan */
		if (typeof $rootScope.detilPengadaan !== 'undefined') {
			$scope.detilPengadaan = $rootScope.detilPengadaan;
		}
		$scope.pengadaanId = $scope.detilPengadaan.id;
		/*---END Detail Pengadaan----*/


		/* START Bidang Usaha Pengadaan*/
		$scope.getListBidangUsaha = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				form.subBidangUsahaByPengadaanIdList = data;
			}).error(function (data, status, headers, config) {
				console.log("** ERROR BidangUsahaPengadaanServices - getBidangUsahaPengadaanByPengadaanId ***");
			});
		}
		$scope.getListBidangUsaha();
		/*---END Bidang Usaha Pengadaan---*/

		$scope.getKlarifikasiHarga = function (itemList, vendor, pengadaan) {
			$scope.loading = true;
			var uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaTotal/klarifikasiHargaTotalServices/getKlarifikasiHargaTotalByPengadaanAndVendor/' + pengadaan + '/' + vendor;
			$http.get(uri, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				if (data.length > 0) {
					itemList['finalCheck'] = data[0].finalCheck;
					itemList['idKlarifikasiHargaTotal'] = data[0].id;
				} else {
					form.finalCheck = false;
				}
			}).error(function (data, status, headers, config) {

			});
		};

		$scope.getTotalHargaIfAuction = function (itemList, vendor, pengadaan) {
			//cek negosiasi - meski auction,  
			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + pengadaan + '/' + vendor, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				if (data.length > 0) {
					itemList['totalHarga'] = data[0].nilaiPenawaranAfterCondition;
					$scope.loading = false;
				} else {
					$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getDistinctPenawaranAuctionAndLastAuctionByPengadaanVendor/' + pengadaan + '/' + vendor)
						.success(function (data, status, headers, config) {
							if (typeof data.nilaiPenawaran !== 'undefined') {
								itemList['totalHarga'] = data.nilaiPenawaran;
							}
							$scope.loading = false;
						});
				}
			});

		}

		$scope.getTotalHarga = function (itemList, vendor, pengadaan) {
			$scope.loading = true;
			//console.log("> get last total harga");
			/* Cek harga total dari negosiasi*/
			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + pengadaan + '/' + vendor, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				//console.info(data);
				if (data.length > 0) {
					itemList['totalHarga'] = data[0].nilaiPenawaranAfterCondition;
					$scope.loading = false;
				} else {
					//console.log("Data negosiasi tidak ditemukan");
					/*Cek harga total dari auction*/
					$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByVendorPengadaan/' + pengadaan + '/' + vendor, {
						ignoreLoadingBar: true
					}).success(function (data, status, headers, config) {
						if (data.length > 0) {
							itemList['totalHarga'] = data[0].nilaiPenawaran;
						} else {
							console.log("Data auction tidak ditemukan");
							/*Cek harga total dari penawaran pertama*/
							$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + pengadaan + '/' + vendor, {
								ignoreLoadingBar: true
							}).success(function (data, status, headers, config) {
								if (data.length > 0) {
									itemList['totalHarga'] = data[0].nilaiPenawaran;
								}
							}).error(function (data, status, headers, config) {
								console.log("** ERROR penawaranPertamaServices - getByPengadaanAndVendor ***");
								$scope.loading = false;
							});
						}
					}).error(function (data, status, headers, config) {
						console.log("** ERROR PenawaranAuctionServices - getByVendorPengadaan ***");
						$scope.loading = false;
					});
				}
			}).error(function (data, status, headers, config) {
				console.log("** ERROR NegosiasiServices - getLastFromVendorByPengadaanAndVendor ***");
				$scope.loading = false;
			});
		}

		/* START List Vendor*/
		$scope.getListVendor = function () {
			$scope.loading = true;
			$scope.list = [];
			var uri = $rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $scope.pengadaanId;
			$http.get(uri, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				angular.forEach(data, function (value, indeks) {
					data[indeks]['finalCheck'] = false;
					data[indeks]['totalHarga'] = 0;
					$scope.getKlarifikasiHarga(data[indeks], data[indeks].vendor.id, $scope.pengadaanId);
					if (value.pengadaan.metodePenawaranHarga.id == 2 || value.pengadaan.metodePenawaranHarga.id == 3) {
						// console.log("> cek metode " + value.pengadaan.metodePenawaranHarga.id);
						$scope.getTotalHargaIfAuction(data[indeks], data[indeks].vendor.id, $scope.pengadaanId);
					} else if (value.pengadaan.metodePenawaranHarga.id == 1) {
						$scope.getTotalHarga(data[indeks], data[indeks].vendor.id, $scope.pengadaanId);
					} else {
						console.error("Tidak ada metode penawaran Harga");
					}
					$scope.list.push(form);
				})
				$scope.listVendors = data;
			}).error(function (data, status, headers, config) {

			});
		}
		$scope.getListVendor();


		$scope.checked = function (data) {
			var obj = $scope.listVendors.indexOf(data);
			if ($scope.listVendors[obj].finalCheck == true) {
				$scope.list[obj].finalCheck = true;
			} else {
				$scope.list[obj].finalCheck = false;
			}
			$scope.isDisabled = false;
		}

		/* get Next Tahapan */
		$scope.updatePengadaan = function () {
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
				.success(function (data, status, headers, config) {
					$scope.nextTahapan = data;
					$http({
						method: 'POST',
						url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
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
							tahapanPengadaanId: $scope.nextTahapan
						}
					}).success(function (data, status, headers, config) {
						$scope.loading = false;
						//$location.path('app/promise/procurement/klarifikasiharga');
					});
				})
				.error(function (data, status, headers, config) {
					console.log('get next error');
				});
		}

		$scope.proses = function (indeks) {
			$scope.loading = true;
			form = new Object();
			form.finalCheck = $scope.listVendors[indeks].finalCheck;
			form.id = $scope.listVendors[indeks].idKlarifikasiHargaTotal;
			form.pengadaan = $scope.detilPengadaan.id;
			form.vendor = $scope.listVendors[indeks].vendor.id;
			var uri = '';
			if (typeof $scope.listVendors[indeks].idKlarifikasiHargaTotal !== 'undefined') {
				console.log(typeof $scope.listVendors[indeks].idKlarifikasiHargaTotal + " : UPDATE");
				uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaTotal/klarifikasiHargaTotalServices/update';
			} else {
				console.log(typeof $scope.listVendors[indeks].idKlarifikasiHargaTotal + " : CREATE");
				uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaTotal/klarifikasiHargaTotalServices/create';
			}
			$http({
				method: 'POST',
				url: uri,
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				transformRequest: function (obj) {
					var str = [];
					for (var p in obj)
						str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					return str.join("&");
				},
				data: form
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				if (($scope.list.length - 1) != indeks) {
					$scope.proses(indeks + 1);
				}
				$scope.showSuccess();
			}).error(function (data, status, headers, config) {
				alert('error');
			});
		}

		$scope.showSuccess = function () {
			$scope.loadingSaving = false;
			toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
			//$scope.back();
		}

		//save
		$scope.saveData = function () {
			$scope.proses(0);
			$scope.isDisabled = true;
		}

		$scope.back = function () {
			$state.go('app.promise.procurement-panitia-klarifikasiharga');
		}

		/*modal Confirmation*/
		$scope.goModalConfirmation = function (data) {
			$rootScope.saveAfterConfirmation = $scope.saveData;
			$rootScope.editTahapanPengadaan = $scope.updatePengadaan;
			var modalInstance = $modal.open({
				templateUrl: '/ModalConfirmation.html',
				controller: ModalInstanceConfirmationCtrl,
			});


		};

		/*---Modal Confirmation---*/
		var ModalInstanceConfirmationCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {

			$scope.ok = function () {
				$rootScope.saveAfterConfirmation();
				$rootScope.editTahapanPengadaan();
				$modalInstance.dismiss('close');
			};

			$scope.cancel = function () {
				$modalInstance.dismiss('cancel');
			};
		};
		ModalInstanceConfirmationCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
		/*------*/
	}

	KlarifikasiHargaTotalController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$q', '$timeout', '$filter', '$state', 'ngTableParams'];

})();