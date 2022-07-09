/*rmt*/

(function () {
	'use strict';
	angular.module('naut').controller('SanggahanTotalDetailController', SanggahanTotalDetailController);
	function SanggahanTotalDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams, $anchorScroll, ngTableParams, $state) {

		
		$scope.pengadaanId = $stateParams.pengadaanId
		var sanggahanDetail = this;
		
		// set mode
		$scope.isEditable = $rootScope.isEditable;
		$scope.loadingSaving = false;
		$scope.loading = true;
		$scope.tanggal_error_required = false;
		
		/*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/

		function loadSanggahan() {

			$scope.loading = true;
			
			$http.get($rootScope.backendAddress + '/procurement/sanggahan/sanggahanServices/getListByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					sanggahanDetail.sanggahan = data[0];
					
					if (typeof sanggahanDetail.sanggahan == 'undefined') {
						sanggahanDetail.sanggahan            = {};
						//sanggahanDetail.sanggahan.pengadaan  = sanggahanDetail.suratPenawaran.pengadaan;
						sanggahanDetail.sanggahan.pengadaan  = {};
						sanggahanDetail.sanggahan.keterangan = '';
					}
					else
					{
						$scope.sanggahan_tanggal = sanggahanDetail.sanggahan.tanggal;
					}
					
				// Ambil sanggahan vendor
				$http.get($rootScope.backendAddress + '/procurement/sanggahanVendor/sanggahanVendorServices/getListByPengadaan/' + $scope.pengadaanId, {
	                ignoreLoadingBar: true
					}).success(function (data, status, headers, config) {
						sanggahanDetail.sanggahanVendorList = data;
						
						angular.forEach(sanggahanDetail.sanggahanVendorList, function (sanggah) {
							$http.get($rootScope.backendAddress + '/procurement/sanggahan/dokumenSanggahanVendorServices/getListBySanggahan/' + sanggah.id, {
			                    ignoreLoadingBar: true
			                }).success(function (data, status, headers, config) {
			                	sanggah.documentList = data;
			                }).error(function (data, status, headers, config) {
								$scope.loading = false;
								console.log("***ERROR** - '/procurement/sanggahan/dokumenSanggahanVendorServices/getListBySanggahan/")
			                });
						})
						
						loadDaftarVendor();
					
					}).error(function (data, status, headers, config) {
							$scope.loading = false;
							console.log("***ERROR** - '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/")
		            });
			
			}).error(function (data, status, headers, config) {
					$scope.loading = false;
					console.log("***ERROR** - '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/")
            });
			
		}
		
		
		function loadDaftarVendor() {
			// Ambil informasi daftar vendor
			$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					sanggahanDetail.listVendor = data;
             
                	var ctr = 0;
                	var evaluasiList = [];
                	
                	// Ambil detil evaluasi masing2 vendor
                	angular.forEach(sanggahanDetail.listVendor, function (listVendor) {
		     		
	                	$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/' + listVendor.suratPenawaran.id, {
			                    ignoreLoadingBar: true
			                }).success(function (data, status, headers, config) {
			                	evaluasiList.push(data[0]);
			                	ctr++;
			                	if (ctr == sanggahanDetail.listVendor.length) {
			                		// end load 
			                		$scope.loading = false;
			                 		var objList = evaluasiList;
			                		$scope.evaluasiVendorList = new ngTableParams({
                                        page: 1,
                                        count: 10
                                    }, {
                                        total: objList.length,
                                        getData: function ($defer, params) {
                                            $defer.resolve(objList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                        }
                                    });
			                	}
			                }).error(function (data, status, headers, config) {
								$scope.loading = false;
								console.log("***ERROR** - '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/")
			                });
                	})
				
            }).error(function (data, status, headers, config) {
				$scope.loading = false;
				console.log("***ERROR** - '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/")
            });
		}
		
		
		$scope.gotoIndex = function () {
			$state.go('app.promise.procurement-panitia-sanggahan');
		};
		
		var isFormValid = function () {
			var isValid = true;
			if ( typeof $scope.sanggahan_tanggal=='undefined' )
				{
					document.getElementsByName('sanggahan_tanggal')[0].focus();
					$scope.tanggal_error_required =true;
					isValid = false;
				}
			return isValid;
		}
		
		$scope.save = function () {
			 if (isFormValid()) {
	                saveSanggahan();
	            } else {
	                toaster.pop('warning', 'Kesalahan', 'Silahkan periksa kembali inputan anda!');
	            }
	            return false;
		};

		function saveSanggahan() {
			 $scope.loadingSaving = true;
			 
	            /*insert when object.id is undefined*/
	            if (typeof sanggahanDetail.sanggahan.id == 'undefined') {
	                 $http({
	                    method: 'POST',
	                    url: $rootScope.backendAddress + '/procurement/sanggahan/sanggahanServices/insert',
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
	                        pengadaan   : $scope.pengadaanId,
	                        tanggal     : $filter('date')($scope.sanggahan_tanggal, 'dd-MM-yyyy'),
	                        keterangan  : sanggahanDetail.sanggahan.keterangan
	                    }
	                }).success(function (data, status, headers, config) {
	                	 
	                	 $scope.showSuccess();
	                })
	            } else {
	                $http({
	                    method: 'POST',
	                    url: $rootScope.backendAddress + '/procurement/sanggahan/sanggahanServices/update',
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
	                        id          : sanggahanDetail.sanggahan.id,
	                        pengadaan   : $scope.pengadaanId,
	                        tanggal     : $filter('date')($scope.sanggahan_tanggal, 'dd-MM-yyyy'),
	                        keterangan  : sanggahanDetail.sanggahan.keterangan
	                    }
	                }).success(function (data, status, headers, config) {
	                	$scope.showSuccess();
	                })
	            }
		 }
	  
		$scope.showSuccess = function () {
	            $scope.loadingSaving = false;
	            $scope.isEditable = false;
	            toaster.pop('success', 'Sukses', 'Data sanggahan berhasil disimpan.');
	            /*$scope.gotoIndex();*/
                if($scope.sanggahan_status==2){
                    $scope.updatePengadaan();
                }
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
                       
                    });
                })
                .error(function (data, status, headers, config) {
                    console.log('get next error');
                });
        }
		  
		// Datepicker
		sanggahanDetail.toggleMin = function () {
			sanggahanDetail.minDate = sanggahanDetail.minDate ? null : new Date();
        };
        sanggahanDetail.toggleMin();
        sanggahanDetail.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        sanggahanDetail.format = sanggahanDetail.formats[4];

        sanggahanDetail.tanggalSanggahanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            sanggahanDetail.tanggalSanggahanOpened = true;
        };
        // End DatePicker
      
        loadSanggahan();

	}

	SanggahanTotalDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams', '$anchorScroll', 'ngTableParams','$state'];

})();