/**============================================================
 * Module: TotalMeritPointController.js [penunjukan pemenang]
 * Author: G.A.I
 ==============================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenunjukanTotalMeritPointController', PenunjukanTotalMeritPointController);

	function PenunjukanTotalMeritPointController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, $filter, ngTableParams) {
        var form = this;
        
        if (typeof $rootScope.pengadaanPenunjukanPemenang !== 'undefined') {
			form.detailPengadaan = $rootScope.pengadaanPenunjukanPemenang;
		} else {
			console.log('Error');
		}
		$scope.pengadaanId = form.detailPengadaan.id;

		var checkPemenangVendor = {};

		//vendor-surat penawaran
		if (form.detailPengadaan.metodePenawaranHarga.id == 1) {
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + $scope.pengadaanId).success(function (negosiasi, status, headers, config) {
				angular.forEach(negosiasi, function (value, indeks) {
					negosiasi[indeks].pengadaanId = $scope.pengadaanId;
					//nilai Evaluasi
					$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.vendor.id).success(function (nilai, status, headers, config) {
						if (nilai.length > 0) {
							negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
						} else {
							negosiasi[indeks].harga = 0;
						}
					})
                   
					$http.get($rootScope.backendAddress + '/procurement/penunjukanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penunjukan, status, headers, config) {
                    	if (penunjukan.length > 0) {
							$scope.edit = true;
							$scope.pemenang = penunjukan[0].id;
							negosiasi[indeks].status = true;
						}
					})
					if ((indeks + 1) == negosiasi.length) {
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
						auction[indeks].harga = nilai[0].nilaiPenawaran; 
                	})
					$http.get($rootScope.backendAddress + '/procurement/penunjukanpemenangtotal/getListByVendorByPengadaan/' + value.vendor.id + '/' + $scope.pengadaanId).success(function (penunjukan, status, headers, config) {
					
						
                        if (penunjukan.length > 0) {
							$scope.edit = true;
							$scope.pemenang = penunjukan[0].id;
							auction[indeks].status = true;
						}
					})
					if ((indeks + 1) == auction.length) {
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
				$http.get($rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorTotalServices/getListByVendorByPengadaan/' + vendorId + '/' + $scope.pengadaanId, {
					ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					if (data.length > 0) {
						//nilaiEvaluasi.metodePenawaranHargaid = data[0].pengadaan.metodePenawaranHarga.id;
						nilaiEvaluasi.admin = data[0].nilaiEvaluasiAdministrasi;
						nilaiEvaluasi.teknis = data[0].nilaiEvaluasiTeknis;
						nilaiEvaluasi.harga = data[0].nilaiEvaluasiHarga;
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
			//console.log('pengadaan = ' + JSON.stringify(checkPemenangVendor.pengadaan.id));
			//console.log('vendor = ' + JSON.stringify(checkPemenangVendor.vendor.id));

			var uri = $rootScope.backendAddress + '/procurement/penunjukanpemenangtotal/';
			var postFormPemenang = {};
			if ($scope.edit) {
				uri = uri + 'update';
				postFormPemenang = {
					id: $scope.pemenang,
					vendorId: checkPemenangVendor.vendor.id,
					pengadaanId: checkPemenangVendor.pengadaanId
				};
				console.log(0);
			} else {
				uri = uri + 'create';
				postFormPemenang = {
					vendorId: checkPemenangVendor.vendor.id,
					pengadaanId: checkPemenangVendor.pengadaanId
				};
				console.log(1);
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
				data: postFormPemenang
			}).success(function (data, status, headers, config) {
				if (typeof data !== 'undefined') {
					toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
				}
			}).error(function (data, status, headers, config) {})
		}

		var getPenunjukanPemenangList = function (vendorId, pengadaanId) {
			$http.get($rootScope.backendAddress + '/procurement/penunjukanpemenangtotal/getListByVendorByPengadaan/' + vendorId + '/' + pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				var id = 0;
				if (data.length > 0) {
					id = data[0].id;
					//console.log('id = ' + JSON.stringify(id));
					simpanPemenang(id);
				} else {
					//console.log('id = ' + JSON.stringify(id));
					simpanPemenang(id);
				}
			}).error(function (data, status, headers, config) {})
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
		}
        
        $scope.printDiv = function (divName) {
            $scope.isLoading=false;
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

		form.btnSimpan = function () {
			$scope.loading = true;
			//console.log(JSON.stringify(Object.keys(checkPemenangVendor).length));
			if (Object.keys(checkPemenangVendor).length > 0) {
				//console.log('simpan');
				getPenunjukanPemenangList(checkPemenangVendor.vendor.id, checkPemenangVendor.pengadaanId);

				//tahapan pengadaan
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
					if (typeof data !== 'undefined') {
						if ($scope.isPublish == true) {
							var postFormPengumuman = {
								pengadaan: form.detailPengadaan.id,
								tglMulai: $filter('date')((new Date()), "dd-MM-yyyy"),
								tglSelesai: $filter('date')((new Date()), "dd-MM-yyyy"),
								tipe: 2,
								isiPengumuman: form.detailPengadaan.namaPengadaan
							};
							var url = $rootScope.backendAddress + "/procurement/inisialisasi/PengumumanPengadaanServices/insert";
							$http({
								method: 'POST',
								url: url,
								headers: {
									'Content-Type': 'application/x-www-form-urlencoded'
								},
								transformRequest: function (obj) {
									var str = [];
									for (var p in obj)
										str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
									return str.join("&");
								},
								data: postFormPengumuman
							}).success(function (data, status, headers, config) {
								toaster.pop('success', 'Penunjukan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
								$timeout(function () {
									$location.path('/app/promise/procurement/penunjukanpemenang');
									$scope.loading = false;
								}, 2000);
							});

						} else {
							toaster.pop('success', 'Pemenang Vendor', 'Simpan, berhasil.');
							$timeout(function () {
								$location.path('/app/promise/procurement/penunjukanpemenang');
								$scope.loading = false;
							}, 2000);
						}
					}
				});
               
			} else {
				toaster.pop('error', 'GAGAL DISIMPAN', 'Pilih salah satu pemenang Vendor pada Tabel Hasil Evaluasi Administrasi');
				$scope.loading = false;
			}
		}
        
        form.nextTahapanPengadaan = function () {
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				form.nextTahapan = data;
			}).error(function (data, status, headers, config) {})
		}
		 form.nextTahapanPengadaan();
        

	}
	PenunjukanTotalMeritPointController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', '$filter', 'ngTableParams'];
})();