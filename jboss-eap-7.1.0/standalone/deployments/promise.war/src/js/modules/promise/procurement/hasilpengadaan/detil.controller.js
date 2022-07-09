/**=========================================================
 * Module: UndanganPengadaanDetilController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('HasilPengadaanDetilController', HasilPengadaanDetilController);

	function HasilPengadaanDetilController($scope, $http, $rootScope, $resource, $location, $stateParams, ngTableParams, $state) {
		var form = this;

		form.dataPengadaan = $stateParams.dataPengadaan.pengadaan;
		form.metodePengadaanId=form.dataPengadaan.metodePengadaan.id;
		form.dataPengadaan.penawaranHargaList = [];

		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.dataPengadaan.id)
			.success(function (data, status, headers, config) {
				form.dataPengadaan.bidang = data;
			});

		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + form.dataPengadaan.id)
			.success(function (data, status, headers, config) {
				form.dataPengadaan.vendorList = data;

				// Pengambilan Dokumen
				$http.get($rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/getByPengadaan/' + form.dataPengadaan.id)
					.success(function (data, status, headers, config) {
						form.dataPengadaan.dataPengambilanDokumenList = data;

						// loop vendor
						if (form.dataPengadaan.vendorList !== undefined && form.dataPengadaan.vendorList.length > 0) {
							angular.forEach(form.dataPengadaan.vendorList, function (vendor, index) {
								// Pengambilan Dokumen
								vendor.statusPengambilanDokumen = 'No';
								if (form.dataPengadaan.dataPengambilanDokumenList !== undefined && form.dataPengadaan.dataPengambilanDokumenList.length > 0) {
									angular.forEach(form.dataPengadaan.dataPengambilanDokumenList, function (pengambilanDokumen, index) {
										if (vendor.vendor.id === pengambilanDokumen.vendor.id) {
											vendor.statusPengambilanDokumen = 'Ya';
										}
									});
								}
							});
						}
					});

				// Penjelasan
				$http.get($rootScope.backendAddress + '/procurement/penjelasan/vendorPenjelasanServices/getListByPengadaan/' + form.dataPengadaan.id)
					.success(function (data, status, headers, config) {
						form.dataPengadaan.dataPenjelasanList = data;

						// loop vendor
						if (form.dataPengadaan.vendorList !== undefined && form.dataPengadaan.vendorList.length > 0) {
							angular.forEach(form.dataPengadaan.vendorList, function (vendor, index) {
								// Penjelasan
								vendor.statusPenjelasan = 'No';
								if (form.dataPengadaan.dataPenjelasanList !== undefined && form.dataPengadaan.dataPenjelasanList.length > 0) {
									angular.forEach(form.dataPengadaan.dataPenjelasanList, function (penjelasan, index) {
										if (vendor.vendor.id === penjelasan.vendor.id) {
											vendor.statusPenjelasan = 'Ya';
										}
									});
								}
							});
						}
					});

				// pemasukan
				$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + form.dataPengadaan.id)
					.success(function (data, status, headers, config) {
						form.dataPengadaan.suratPenawaranList = data;

						// loop vendor
						if (form.dataPengadaan.vendorList !== undefined && form.dataPengadaan.vendorList.length > 0) {
							angular.forEach(form.dataPengadaan.vendorList, function (vendor, index) {
								// Pemasukan 
								vendor.statusPemasukan = 'No';
								if (form.dataPengadaan.suratPenawaranList !== undefined && form.dataPengadaan.suratPenawaranList.length > 0) {
									angular.forEach(form.dataPengadaan.suratPenawaranList, function (penawaran, index) {
										if (vendor.vendor.id === penawaran.vendor.id) {
											vendor.statusPemasukan = 'Ya';
										}
									});
								}
							});
						}
					});

				// pembukaan
				$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/daftarHadirVendorServices/getDaftarHadirVendorListByPengadaan/' + form.dataPengadaan.id)
					.success(function (data, status, headers, config) {
						form.dataPengadaan.daftarHadirVendorList = data;

						// loop vendor
						if (form.dataPengadaan.vendorList !== undefined && form.dataPengadaan.vendorList.length > 0) {
							angular.forEach(form.dataPengadaan.vendorList, function (vendor, index) {
								// Pembukaan
								vendor.statusPembukaan = 'No';
								if (form.dataPengadaan.daftarHadirVendorList !== undefined && form.dataPengadaan.daftarHadirVendorList.length > 0) {
									angular.forEach(form.dataPengadaan.daftarHadirVendorList, function (daftarHadirVendor, index) {
										if (vendor.vendor.id === daftarHadirVendor.vendor.id) {
											vendor.statusPembukaan = 'Ya';
										}
									});
								}
							});
						}
					});

				// Evaluasi Administrasi
				if (form.dataPengadaan.jenisPenawaran.id === 1) {
					$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getListByPengadaan/' + form.dataPengadaan.id)
						.success(function (data, status, headers, config) {
							form.dataPengadaan.evaluasiAdministrasiList = data;
							
							// loop vendor
							if (form.dataPengadaan.vendorList !== undefined && form.dataPengadaan.vendorList.length > 0) {
								angular.forEach(form.dataPengadaan.vendorList, function (vendor, index) {

									// Evaluasi Admin
									vendor.evaluasiAdmin = 0;
									vendor.evaluasiTeknis = 0;
									vendor.evaluasiHarga = 0;

									// Pembuktian Kualifikasi
									vendor.buktiKualifikasi = 'No';

									if (form.dataPengadaan.evaluasiAdministrasiList !== undefined && form.dataPengadaan.evaluasiAdministrasiList.length > 0) {
										angular.forEach(form.dataPengadaan.evaluasiAdministrasiList, function (nilaiVendor, index) {
											if (vendor.vendor.id === nilaiVendor.vendor.id) {
												vendor.evaluasiAdmin = nilaiVendor.nilaiAdmin;
												vendor.evaluasiTeknis = nilaiVendor.nilaiTeknis;
												/*if (form.dataPengadaan.sistemEvaluasiPenawaran.id == 1) {
													vendor.evaluasiHarga = 'LULUS';
												} else {*/
													vendor.evaluasiHarga = nilaiVendor.nilaiHarga;
												/*}*/
											}
										});
									}
								});

								// Pemenang
								$http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getPenetapanPemenangTotalByPengadaan/' + form.dataPengadaan.id)
									.success(function (data, status, headers, config) {
										form.dataPengadaan.dataPemenang = data;
										if (data !== undefined && data.length > 0) {
											if (form.dataPengadaan.evaluasiAdministrasiList !== undefined && form.dataPengadaan.evaluasiAdministrasiList.length > 0) {
												angular.forEach(form.dataPengadaan.evaluasiAdministrasiList, function (nilaiVendor, index) {
													if (form.dataPengadaan.dataPemenang[0].vendor.id === nilaiVendor.vendor.id) {
														form.dataPengadaan.dataPemenang[0].evaluasiAdmin = nilaiVendor.nilaiAdmin;
														form.dataPengadaan.dataPemenang[0].evaluasiTeknis = nilaiVendor.nilaiTeknis;
														form.dataPengadaan.dataPemenang[0].evaluasiHarga = nilaiVendor.nilaiHarga;
													}
												});
											}
										}
										$scope.tablePemenang = new ngTableParams({
											page: 1, // show first page
											count: 5 // count per page
										}, {
											total: form.dataPengadaan.dataPemenang.length,
											getData: function ($defer, params) {
												$defer.resolve(form.dataPengadaan.dataPemenang.slice((params.page() - 1) * params.count(), params.page() * params.count()));
											}
										});
									});
							}
						});
				}

				$scope.tableDetailPelelangan = new ngTableParams({
					page: 1, // show first page
					count: 5 // count per page
				}, {
					total: form.dataPengadaan.vendorList.length, // length of data4
					getData: function ($defer, params) {
						$defer.resolve(form.dataPengadaan.vendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
					}
				});

				// Usulan calon pemenang
				$scope.tableUsulanPemenang = new ngTableParams({
					page: 1, // show first page
					count: 5 // count per page
				}, {
					total: form.dataPengadaan.vendorList.length, // length of data4
					getData: function ($defer, params) {
						$defer.resolve(form.dataPengadaan.vendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
					}
				});
				
			});

		$http.get($rootScope.backendAddress +  '/procurement/negosiasi/NegosiasiServices/getPenawaranList/' + form.dataPengadaan.id)
			.success(function (data, status, headers, config) {
				if (data.negosiasiList  != 0 && data.negosiasiList.length>0 ){
					angular.forEach( data.negosiasiList,function(value,index){
						form.dataPengadaan.penawaranHargaList.push(data.negosiasiList[index]);
		      		})
				}
				else if(data.penawaranAuctionList  != 0 && data.penawaranAuctionList.length>0 ){
					angular.forEach( data.penawaranAuctionList,function(value,index){
						form.dataPengadaan.penawaranHargaList.push(data.penawaranAuctionList[index]);
		      		})
				}
				else if(data.penawaranPertamaList  != 0 && data.penawaranPertamaList.length>0 ){
					angular.forEach( data.penawaranPertamaList,function(value,index){
						form.dataPengadaan.penawaranHargaList.push(data.penawaranPertamaList[index]);
		      		})
				}
				$scope.tableLogPenawaran = new ngTableParams({
		              page: 1, // show first page
		              count: 5 // count per page
		          }, {
		              total: form.dataPengadaan.penawaranHargaList.length, // length of data4
		              getData: function ($defer, params) {
		                  $defer.resolve(form.dataPengadaan.penawaranHargaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
		              }
		          });
				
			});
			
		
		$scope.btnKembali = function () {
			$state.go('appvendor.promise.procurement-hasilpengadaan');
		}
	}

	HasilPengadaanDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$stateParams', 'ngTableParams', '$state'];

})();