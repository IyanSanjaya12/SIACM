(function () {
	'use strict';

	angular.module('naut').controller('KontrakEditController', KontrakEditController);

	function KontrakEditController($scope, $http, $rootScope, $resource, $location, $stateParams, ngTableParams, $state, $filter, $modal, FileUploader, RequestService) {
		var form = this;

		form.dataKontrak = $stateParams.dataKontrak;
		//console.log("data kontra >> "+JSON.stringify(form.dataKontrak));

		//list Material
		form.getItemPengadaan = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/perolehan/PerolehanPengadaanSatuanServices/findByPengadaan/' + form.dataKontrak.pengadaan.id)
				.success(function (data, status, headers, config) {
					$scope.loading = false;
					form.dataKontrak.materialPengadaanList = [];
					form.dataKontrak.jasaPengadaanList = [];
					form.dataKontrak.totalNilaiMaterial = 0;
					form.dataKontrak.totalNilaiJasa = 0;

					if (data !== undefined && data.length > 0) {
						form.dataKontrak.valutaItemPengadaan = data[0].mataUang;
						angular.forEach(data, function (perolehan, index) {
							if (perolehan.itemPengadaan.item.itemType.id === 1) {
								form.dataKontrak.materialPengadaanList.push(perolehan);
								form.dataKontrak.totalNilaiMaterial = form.dataKontrak.totalNilaiMaterial + perolehan.nilaiTotal;
							} else {
								form.dataKontrak.jasaPengadaanList.push(perolehan);
								form.dataKontrak.totalNilaiJasa = form.dataKontrak.totalNilaiJasa + perolehan.nilaiTotal;
							}
						});
					}

					$scope.tableMaterialPengadaan = new ngTableParams({
						page: 1,
						count: 10
					}, {
						total: form.dataKontrak.materialPengadaanList.length,
						getData: function ($defer, params) {
							$defer.resolve(form.dataKontrak.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
						}
					});

					$scope.tableJasaPengadaan = new ngTableParams({
						page: 1,
						count: 10
					}, {
						total: form.dataKontrak.jasaPengadaanList.length,
						getData: function ($defer, params) {
							$defer.resolve(form.dataKontrak.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
						}
					});
				}).error(function (data, status, headers, config) {
					$scope.loading = false;
				});
		}
		form.getItemPengadaan();


		form.dataKontrak.templateKontrakList = [{
			id: "1",
			nama: "Pelelangan"
		}, {
			id: "2",
			nama: "Pengadaan"
		}];

		// File Kontrak
		var uploader = $scope.uploader = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});

		$scope.uploadFileKontrak = function (item) {
			item.upload();
			$http({
				method: 'POST',
				url: $rootScope.backendAddress + '/procurement/KontrakDokumenServices/create',
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
					kontrak: form.dataKontrak.id,
					fileName: item.file.name,
					pathFile: '/' + form.dataKontrak.nomor + '/' + item.file.name,
					fileSize: item.file.size
				}
			}).success(function (data, status, headers, config) {
				if (data !== undefined && data.id > 0) {
					item.isUploaded = true;
				}
			});
		}

		$scope.uploadAllFileKontrak = function () {
			if (uploader !== undefined && uploader.queue.length > 0) {
				angular.forEach(uploader.queue, function (item, index) {
					if (item.isUploaded === false) {
						$scope.uploadFileKontrak(item);
					}
				});
			}
		}

		// History Addendum
		form.dataKontrak.dataAddendumList = [];
		$scope.tableHistoryAddendum = new ngTableParams({
			page: 1,
			count: 10
		}, {
			total: form.dataKontrak.dataAddendumList.length,
			getData: function ($defer, params) {
				$defer.resolve(form.dataKontrak.dataAddendumList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
			}
		});

		// Syncronize PR
		$scope.messageSync = '';
		$scope.btnSyncronizePR = function () {
			RequestService.modalConfirmation('Anda yakin ingin Syncronize data Kontrak ke Data Purchasing Request?')
				.then(function () {
					$scope.loading = true;
					$http.get($rootScope.backendAddress + '/procurement/KontrakManajemenServices/getSyncronizeToPR/' + form.dataKontrak.id)
						.success(function (data) {
							$scope.loading = false;
							$scope.syncResponse = data;
						})
						.error(function (data) {
							$scope.loading = false;
							$scope.syncResponse = data;
							if(typeof data === 'undefined'){
								$scope.syncResponse = {
									msgError : 'Syncronize Error, Purchase Request Tidak ada!'
								}
							}
						});
				});

		}

		$http.get($rootScope.backendAddress + '/procurement/KontrakManajemenServices/historyaddendum/' + form.dataKontrak.pengadaan.id)
			.success(function (data, status, headers, config) {
				if (data !== undefined && data.length > 0) {
					form.dataKontrak.dataAddendumList = data;
					$scope.tableHistoryAddendum.reload();
				}
			});

		// event on click button kembali
		$scope.btnKembali = function () {
			$state.go("app.promise.procurement-manajemenkontrak-indexdatakontrak");
		}
	}

	KontrakEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$stateParams', 'ngTableParams', '$state', '$filter', '$modal', 'FileUploader', 'RequestService'];

})();