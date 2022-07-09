(function () {
	'use strict';

	angular.module('naut').controller('PemasukanPenawaranViewTotalController', PemasukanPenawaranViewTotalController);

	function PemasukanPenawaranViewTotalController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster) {
		var pPengadaan = this;
		$scope.isMaterialNotEmpty = false;
		$scope.isJasaNotEmpty = false;
		/* START Detail Pengadaan */
		if (typeof $rootScope.detilPengadaan !== 'undefined') {
			$scope.detilPengadaan = $rootScope.detilPengadaan;
			//console.log(JSON.stringify($scope.detilPengadaan));
		} else {
			console.log('INFO Error');
		}
		$scope.pengadaanId = $scope.detilPengadaan.id;
		/*---END Detail Pengadaan----*/
		
		/* START Rincian Kebutuhan Barang & Jasa*/
		$scope.getSuratPenawaranPengadaan = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				pPengadaan.suratPenawaranByPengadaanList = data;
                if($scope.detilPengadaan.metodePenyampaianDokumenPengadaan.id==3){
                    angular.forEach(pPengadaan.suratPenawaranByPengadaanList, function(value, index){
                        if(value.nilaiJaminan==null){
                            pPengadaan.suratPenawaranByPengadaanList[index].created = null;
                        }
                    });
                }
			}).error(function (data, status, headers, config) {
				$scope.loading = false;
			});
		}
		$scope.getSuratPenawaranPengadaan();
		/*---END Rincian Kebutuhan Jasa---*/


		/*----------------------------------------------SIMPAN TAHAPAN------------------------------------------------*/
		$scope.btnSimpan = function () {

			ModalService.showModalConfirmation().then(function (result) {
				saveToPembukaanPenawaran();
				//console.log('INFO update tahapan sukses');
				$scope.loading = true;
				$scope.statusSimpan = true;
				$scope.loading = false;
				$scope.btnSimpan = false;
				$scope.backStatus = true;
			});

		};

		function saveToPembukaanPenawaran() {
			$scope.loadingSaveToPembukaan = true;
			$http({
					method: 'POST',
					url: $rootScope.backendAddress + '/procurement/pembukaanPenawaranServices/create',
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
						pengadaan: $scope.pengadaanId
					}
				})
				.success(function (data, status, headers, config) {
					$scope.loadingSaveToPembukaan = false;
					if (typeof data !== 'undefined') {
						toaster.pop('success', 'Pemasukan Penawaran', 'Simpan ' + data.namaPengadaan + ', berhasil.');
					}
				})
				.error(function (data, status, headers, config) {
					$scope.loadingSaveToPembukaan = false;
				});
		}
		/*----------------------------------------------END SIMPAN TAHAPAN---------------------------------------------*/

		/*--------------------------------------------------------------------------------------*/
		$scope.back = function () {
			  if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/pemasukanPenawaran');
                }
            } else {
                $location.path('/app/promise/procurement/pemasukanPenawaran');
            }
            
		};

		/*--------------------------------------------------------------------------------------*/

		$scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").remove();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

	}

	PemasukanPenawaranViewTotalController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();