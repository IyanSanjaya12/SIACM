(function () {
	'use strict';

	angular.module('naut')
		.controller('PengumumanPengadaanViewController', PengumumanPengadaanViewController);

	function PengumumanPengadaanViewController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $q, $timeout, $filter, ngTableParams, RequestService) {
		var form = this;
		$scope.statusForm = false;
		$scope.isJasa = false;
		$scope.isMaterial = false;
		$scope.hideDaftar = true;
		$scope.loadingUserValidate = true;
		$scope.rightAccess = false;
		$scope.vendorData = {
			status : 1
		};
		$scope.back = function () {
			$location.path('appvendor/promise/procurement/pengumumanpengadaan');
		}

		
		
		/* START Detail Pengadaan */
		if (typeof $rootScope.detilPengadaan !== 'undefined') {
			$scope.detilPengadaan = $rootScope.detilPengadaan;
			
			// Filter Vendor with Condition For Pengumuman Pengadaan
			var params = {
					pengadaanId: $scope.detilPengadaan.id,
					userId: RequestService.getUserLogin().user.id
			}
			RequestService.doPOST('/procurement/inisialisasi/PengumumanPengadaanServices/pengumumanPengadaanWithConditionForVendor', params)
        	.success(function (data, status, headers, config) {
        		if((data.id != undefined) || ($scope.detilPengadaan.kualifikasiVendor.id == 3) ) {
        			$scope.rightAccess = true;
        		} else {
                    $scope.noRightAccessMessage = true;
                }
          		
                $scope.getListPendaftaranVendor();
        	})
            .error(function(data){
                if( $scope.detilPengadaan.kualifikasiVendor.id == 3) { //tidak dikualifikasikan
        			$scope.rightAccess = true;
        		} else {
                    $scope.noRightAccessMessage = true;
                }
                $scope.getListPendaftaranVendor();
                 
            });
		}
		$scope.pengadaanId = $scope.detilPengadaan.id;
		$scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
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

			});
		}
        $scope.getListBidangUsaha();
		
		/*---END Bidang Usaha Pengadaan---*/

		/* START Rincian Kebutuhan Material*/
		$scope.getListItemPengadaan = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				if (data.length > 0)
					$scope.isMaterial = true;
				for (var i = 0; i < data.length; i++) {
					data[i].index = (i + 1);
				}
				$scope.loading = false;
				$scope.tableMaterialPengadaanList = new ngTableParams({
					page: 1, // show first page
					count: 5 // count per page
				}, {
					total: data.length, // length of data4
					getData: function ($defer, params) {
						$defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
					}
				});
			}).error(function (data, status, headers, config) {});
		}
		$scope.getListItemPengadaan();
		/*---END Rincian Kebutuhan Materrial---*/

		/* START Rincian Kebutuhan Jasa*/
		$scope.getListRincianKebutuhanJasa = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				//form.itemPengadaanJasaByPengadaanIdList = data;
				if (data.length > 0)
					$scope.isJasa = true;

				$scope.loading = false;
				$scope.tableJasaPengadaanList = new ngTableParams({
					page: 1, // show first page
					count: 5 // count per page
				}, {
					total: data.length, // length of data4
					getData: function ($defer, params) {
						$defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
					}
				});
			}).error(function (data, status, headers, config) {

			});
		}
		$scope.getListRincianKebutuhanJasa();
		/*---END Rincian Kebutuhan Jasa---*/

		$scope.getVendorByUser = function () {
			$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				if (typeof data !== 'undefined') {
					$scope.vendor = data.id;
					$scope.vendorData = data;
					$rootScope.dataVendor = data;
				}
				$scope.validateBtnDaftar();
				$scope.loading = false;
			}).error(function (data, status, headers, config) {

			});
		}
        
        //Jadwal Pengadaan	
		$scope.getJadwalPengadaan = function (pengadaanId) {
			$scope.loadingJadwalPengadaan = true;
			$http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getByPengadaanId/' + pengadaanId)
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
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorUserIdAndPengadaanId/' + $rootScope.userLogin.user.id + '/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				if (typeof data.id !== 'undefined') {
					$scope.statusForm = true;
				} else {
					$scope.statusForm = false;
				}

				$scope.getVendorByUser();
			}).error(function (data, status, headers, config) {

			});
		}
		
		//validate tombol daftar
		$scope.validateBtnDaftar = function () {
            console.log("$scope.statusForm="+$scope.statusForm+", $scope.vendorData.status="+$scope.vendorData.status+",$scope.rightAccess="+$scope.rightAccess+", $scope.vendorData.statusBlacklist="+$scope.vendorData.statusBlacklist);
            
			if ($scope.statusForm == true || $scope.vendorData.status != 1 || $scope.rightAccess == false || $scope.vendorData.statusBlacklist != 0 ) {
				$scope.hideDaftar = true;
			} else {
				$scope.hideDaftar = false;
			}
			$scope.loadingUserValidate = false;
		}

		//save ke pendaftaran vendor
		$scope.pendaftaranVendor = function () {
			$scope.loading = true;
			$http({
				method: 'POST',
				url: $rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/insertPendaftaranVendor',
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
        $scope.btnSimpanDisable=false;
		$scope.saveData = function () {
			if ($scope.validate()) {
				ModalService.showModalConfirmation('Apakah anda yakin akan mengikuti pengadaan ini?')
                    .then(function (result) {
                        $scope.btnSimpanDisable=true;
                        $scope.pendaftaranVendor(); 
				});
			} else {
				alert('something wrong');
			}

		}

		/*modal Pengumuman Pengadaan*/
		$scope.goModalPengumuman = function () {
			var modalInstance = $modal.open({
				templateUrl: '/ModalPengumumanPengadaan.html',
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
					url: $rootScope.backendAddress + '/procurement/pengambilandokumen/PengambilanDokumenServices/insert',
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
					window.open($rootScope.viewUploadBackendAddress + "/" + file.path, '_blank');
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

	PengumumanPengadaanViewController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$q', '$timeout', '$filter', 'ngTableParams', 'RequestService'];

})();