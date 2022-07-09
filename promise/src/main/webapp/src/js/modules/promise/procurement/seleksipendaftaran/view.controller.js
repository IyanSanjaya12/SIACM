/**=========================================================
 * Module: SeleksiPendaftaranViewController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SeleksiPendaftaranViewController', SeleksiPendaftaranViewController);

	function SeleksiPendaftaranViewController($scope, $http, $rootScope, $resource, $location, $state, ngTableParams, $stateParams, $modal, toaster) {
		var form = this;
		
		form.dataPengadaan = $stateParams.dataPengadaan;
				
		// find Bidang Usaha Pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.subBidangUsahaList = data;
	    });
		
		// Material pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + form.dataPengadaan.id)
        .success(function(data, status, headers, config) {
            form.dataPengadaan.materialPengadaanList = [];
            form.dataPengadaan.jasaPengadaanList = [];
            
            if (data !== undefined && data.length > 0) {
                angular.forEach(data, function(itemPengadaan, index){
            		if (itemPengadaan.item.itemType.id === 1) {
            			form.dataPengadaan.materialPengadaanList.push(itemPengadaan);
            			form.dataPengadaan.totalNilaiMaterial = form.dataPengadaan.totalNilaiMaterial + itemPengadaan.totalHPS;
            		} else {
            			form.dataPengadaan.jasaPengadaanList.push(itemPengadaan);
            			form.dataPengadaan.totalNilaiJasa = form.dataPengadaan.totalNilaiJasa + itemPengadaan.totalHPS;
            		}
            	});
            }
            
            form.tableMaterialPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.dataPengadaan.materialPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.dataPengadaan.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            
            form.tableJasaPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.dataPengadaan.jasaPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.dataPengadaan.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
        });
		
		// vendor yang terdaftar di pengadaan
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + form.dataPengadaan.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaan.vendorPengadaanList = data;
	    	
	    	// vendor yang terdaftar di seleksi pendaftaran
	    	$http.get($rootScope.backendAddress + '/procurement/seleksipendaftaran/seleksipendaftaranservices/getSeleksiPendaftaranListByPengadaanId/' + form.dataPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
		    		angular.forEach(form.dataPengadaan.vendorPengadaanList, function(vendorPengadaan, index){
		    			vendorPengadaan.status = 'TIDAK LULUS';
		    			if (data !== undefined && data.length > 0) {
		    				angular.forEach(data, function(seleksiPendaftaran, index) {
		    					if (seleksiPendaftaran.vendor.id === vendorPengadaan.vendor.id && seleksiPendaftaran.status == 1) {
		    						vendorPengadaan.status = 'LULUS';
		    					}
		    				});
		    			}
		    		});
		    	}
		    	
		    	form.tableVendor = new ngTableParams({
	                page: 1,
	                count: 10
	            }, {
	                total: form.dataPengadaan.vendorPengadaanList.length,
	                getData: function($defer, params) {
	                    $defer.resolve(form.dataPengadaan.vendorPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
	                }
	            });
		    });
	    });
		
		form.buttonViewVendor = function(dataVendor) {
			var viewModalDataVendor = $modal.open({
                templateUrl: '/modalDataVendor.html',
                controller: 'ModalViewDataVendorController',
                size: 'lg',
                resolve: {
                	dataVendor: function () {
                        return dataVendor;
                    }
                }
            });
			viewModalDataVendor.result.then(function (statusLulus) {
                if (statusLulus != undefined && statusLulus !== null) {
                	var index = form.dataPengadaan.vendorPengadaanList.indexOf(dataVendor);
                	form.dataPengadaan.vendorPengadaanList[index].status = statusLulus;
                    form.tableVendor.reload();
                }
            });
		}
		
		form.btnKembali = function() {
			$state.go('app.promise.procurement-panitia-seleksipendaftaran-index');
		}
		
		form.btnSimpan = function() {
			if (form.dataPengadaan.vendorPengadaanList !== undefined && form.dataPengadaan.vendorPengadaanList.length > 0) {
				angular.forEach(form.dataPengadaan.vendorPengadaanList, function(dataVendor, index){
					$http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/seleksipendaftaran/seleksipendaftaranservices/create',
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
                        	vendorId: dataVendor.vendor.id,
                        	pengadaanId: dataVendor.pengadaan.id,
                        	status: (dataVendor.status ==='LULUS') ? '1' : '0'
                        }
                    })
                    .success(function (data, status, headers, config) {
                    	if (index == form.dataPengadaan.vendorPengadaanList.length - 1) {
                    		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + dataVendor.pengadaan.id)
                            .success(function (data, status, headers, config) {
                            	form.nextTahapan = data;
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
                                        pengadaanId: dataVendor.pengadaan.id,
                                        tahapanPengadaanId: form.nextTahapan
                                    }
                                }).success(function (data, status, headers, config) {
                                	//toaster.pop('success', 'Sukses', 'Data Seleksi Pendaftaran berhasil disimpan.');
                                    $state.go('app.promise.procurement-panitia-seleksipendaftaran-index');
                                });
                            });
                    	}
                    });
				});
			}
		}
	}

	SeleksiPendaftaranViewController.$inject = ['$scope', '$http', '$rootScope', '$resource','$location', '$state', 'ngTableParams', '$stateParams', '$modal', 'toaster'];

})();

angular.module('naut')
.controller('ModalViewDataVendorController', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, dataVendor) {

	$http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorId/' + dataVendor.vendor.id)
    .success(function (data, status, headers, config) {
    	$scope.tableDokumenVendor = new ngTableParams({
            page: 1,
            count: 10
        }, {
            total: data.length,
            getData: function($defer, params) {
                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
            }
        });
    });

	$http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorId/' + dataVendor.vendor.id)
    .success(function (data, status, headers, config) {
    	$scope.tablePengalamanVendor = new ngTableParams({
            page: 1,
            count: 10
        }, {
            total: data.length,
            getData: function($defer, params) {
                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
            }
        });
    });
	
	$scope.pilihStatus = {status: dataVendor.status};

    $scope.simpanData = function () {
    	$modalInstance.close($scope.pilihStatus.status);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});