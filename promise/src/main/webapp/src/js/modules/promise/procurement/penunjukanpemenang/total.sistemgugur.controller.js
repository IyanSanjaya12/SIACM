(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenunjukanTotalSistemGugurController', PenunjukanTotalSistemGugurController);

	function PenunjukanTotalSistemGugurController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams, $filter) {

		var form = this;

		if (typeof $rootScope.pengadaanPenunjukanPemenang !== 'undefined') {
			form.detailPengadaan = $rootScope.pengadaanPenunjukanPemenang;
		} else {
			console.log('Error');
		}
		$scope.pengadaanId = form.detailPengadaan.id;
		form.nextTahapanPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.nextTahapan = data;
            }).error(function (data, status, headers, config) {})
        }
        form.nextTahapanPengadaan();
        $scope.getUpdateTahapan = function () {
            $scope.loading = true;
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
                    tahapanPengadaanId: form.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.saveBtnStatus = true;
            });
        };

		var checkPemenangVendor = {};
		$scope.loading = true;
		if (form.detailPengadaan.metodePenawaranHarga.id == 1) {
			$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (negosiasi, status, headers, config) {
				angular.forEach(negosiasi, function (value, indeks) {
					negosiasi[indeks].pengadaanId = $scope.pengadaanId;
					//nilai Evaluasi
					$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id).success(function (nilai, status, headers, config) {
						if (nilai.length > 0) {
							negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
						} else {
							negosiasi[indeks].harga = negosiasi[indeks].nilaiTotal;
						}
					});
					$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {

						if (penetapan.length > 0) {
							$scope.edit = true;
							$scope.pemenang = penetapan[0].id;
							negosiasi[indeks].status = true;
						}
					});
					if ((indeks + 1) == negosiasi.length) {
						$scope.loading = false;
						form.evaluasiHargaVendor = new ngTableParams({
							page: 1,
							count: 10
						}, {
							total: negosiasi.length,
							getData: function ($defer, params) {
								$defer.resolve(negosiasi.slice((params.page() - 1) * params.count(), params.page() * params.count()));
							}
						});
					}
				})
			})
		} else {
			$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (auction, status, headers, config) {
				angular.forEach(auction, function (value, indeks) {
					auction[indeks].pengadaanId = $scope.pengadaanId;
					$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByVendorPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (nilai, status, headers, config) {
						if (nilai.length > 0) {
							auction[indeks].harga = nilai[0].nilaiPenawaran;
						} else {
							auction[indeks].harga = null;
						}
					})
					$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penetapan, status, headers, config) {
						if (penetapan.length > 0) {
							$scope.edit = true;
							$scope.pemenang = penetapan[0].id;
							auction[indeks].status = true;
						}
					})
					if ((indeks + 1) == auction.length) {
						$scope.loading = false;
						form.evaluasiHargaVendor = new ngTableParams({
							page: 1,
							count: 10
						}, {
							total: auction.length,
							getData: function ($defer, params) {
								$defer.resolve(auction.slice((params.page() - 1) * params.count(), params.page() * params.count()));
							}
						});
					}
				})
			})
		}


		//vendor-surat penawaran
		$scope.loading = true;
		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getSuratPenawaranByPengadaanIdNilaiJaminan/' + $scope.pengadaanId, {
			ignoreLoadingBar: true
		}).success(function (data, status, headers, config) {
			form.vendorList = data;
			for (var i = 0; i < data.length; i++) {
				$scope.asynchronousNilai(i, data[i].vendor.id);
				//console.log(i);
			}
			$scope.loading = false;
		}).error(function (data, status, headers, config) {
			$scope.loading = false;
		})

		$scope.asynchronousNilai = function (x, id) {
			var nilai = $scope.getEvaluasiHargaVendor(id);
			nilai.then(function (nilaiEvaluasi) {
				//console.log('resolve ' + nilaiEvaluasi);
				if (typeof form.vendorList[x].nilaiAdmin !== 'undefined' && typeof form.vendorList[x].nilaiTeknis !== 'undefined') {
					form.vendorList[x]['admin'] = nilaiEvaluasi.admin;
					form.vendorList[x]['teknis'] = nilaiEvaluasi.teknis;
					form.vendorList[x]['status'] = false;
				} else {
					form.vendorList[x].admin = nilaiEvaluasi.admin;
					form.vendorList[x].teknis = nilaiEvaluasi.teknis;
					form.vendorList[x]['status'] = false;
				}
				if (x == 0) {
					//console.log(x);
					form.vendorList[x].status = true;
					checkPemenangVendor = form.vendorList[x];
					//console.log('check 1 = '+JSON.stringify($scope.checkPemenangVendor));
				}
				//console.log('index ' + x + '' + JSON.stringify(form.vendorList));
				if ((x + 1) == form.vendorList.length) {
					form.evaluasiHargaVendor = new ngTableParams({
						page: 1,
						count: 10
					}, {
						total: form.vendorList.length,
						getData: function ($defer, params) {
							$defer.resolve(form.vendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
						}
					});
				}
			}, function (reject) {});
		}

		form.pemenangCheck = function (list) {
			checkPemenangVendor = {};
			checkPemenangVendor = list;
		}

		//evaluasi harga vendor
		var nilaiEvaluasi = {
			admin: 0,
			teknis: 0,
			harga: 0
		};
		$scope.getEvaluasiHargaVendor = function (vendorId) {
			$scope.loading = true;
			var defered = $q.defer();
			$timeout(function () {
				$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getByPengadaanVendor/' + $scope.pengadaanId + '/' + vendorId, {
					ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					if (data.length > 0) {
						nilaiEvaluasi.admin = data[0].nilaiAdmin;
						nilaiEvaluasi.teknis = data[0].nilaiTeknis;
						//console.log('nilai admin ' + nilaiEvaluasi.admin);
						//console.log('nilai teknis ' + nilaiEvaluasi.teknis);
						defered.resolve(nilaiEvaluasi);
					} else {
						defered.resolve(0);
					}
					$scope.loading = false;
				}).error(function (data, status, headers, config) {
					$scope.loading = false;
				});
			}, 1000);
			return defered.promise;
		};

		var simpanPemenang = function (indeks) {
			var uri = $rootScope.backendAddress + '/procurement/penunjukanpemenangtotal/';
			var postFormPemenang = {};
			uri = uri + 'create';
			postFormPemenang = {
				vendorId: checkPemenangVendor.vendor.id,
				pengadaanId: $scope.pengadaanId
			};

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
				data: postFormPemenang
			}).success(function (data, status, headers, config) {
				if (typeof data !== 'undefined') {
					toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
					$scope.getUpdateTahapan();
				}
			}).error(function (data, status, headers, config) {})
		}

		var getPenetapanPemenangList = function (vendorId, pengadaanId) {
			$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendorByPengadaan/' + vendorId + '/' + pengadaanId)
				.success(function (data, status, headers, config) {
					var id = 0;
					if (data.length > 0) {
						id = data[0].id;
						simpanPemenang(id);
					} else {
						simpanPemenang(id);
					}
					if (typeof data !== 'undefined') {
						toaster.pop('success', 'Penetapan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
					}
				})
				.error(function (data, status, headers, config) {})
		}

		form.back = function () {
			if (typeof $rootScope.view !== 'undefined') {
				if ($rootScope.view) {
					$rootScope.backToView();
				} else {
					$location.path('/app/promise/procurement/penunjukanpemenang');
				}
			} else {
				$location.path('/app/promise/procurement/penunjukanpemenang');
			}
		};

		$scope.printDiv = function (divName) {
			$scope.isLoading = false;
			var printContents = document.getElementById(divName).innerHTML;
			var popupWin = window.open('', '_blank', 'width=800,height=600');
			popupWin.document.open()
			popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
			popupWin.document.close();
		}
		form.btnSimpan = function () {
			$scope.loading = true;
			getPenetapanPemenangList(checkPemenangVendor.vendor.id, $scope.pengadaanId);

			$location.path('/app/promise/procurement/penunjukanpemenang');
			$scope.loading = false;
		};

	}
	PenunjukanTotalSistemGugurController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams', '$filter'];
})();