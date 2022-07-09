/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('EvaluasiAdministrasiSatuanEditController', EvaluasiAdministrasiSatuanEditController);

    function EvaluasiAdministrasiSatuanEditController($http, $scope, $rootScope, $resource, $location, $modal, toaster, ngTableParams, $timeout, $q, $stateParams) {
        var form = this;
        
        form.detilPengadaan = $stateParams.dataPengadaan;

        /*Bidang Usaha Pengadaan relasi Pembukaaan Penawaran*/
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + form.detilPengadaan.id)
        .success(function (data,status, headers, config) {
        	form.subBidangUsahaByPengadaanIdList = data;
        });
		
		// Material pengadaan
        $scope.loading = true;
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + form.detilPengadaan.id, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            form.detilPengadaan.materialPengadaanList = [];
            form.detilPengadaan.jasaPengadaanList = [];
            
            if (data !== undefined && data.length > 0) {
                angular.forEach(data, function(itemPengadaan, index){
            		if (itemPengadaan.item.itemType.id === 1) {
            			form.detilPengadaan.materialPengadaanList.push(itemPengadaan);
            		} else {
            			form.detilPengadaan.jasaPengadaanList.push(itemPengadaan);
            		}
            	});
            }
            $scope.loading = false;
            
            form.itemPengadaanMaterial = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.detilPengadaan.materialPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.detilPengadaan.materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            
            form.itemPengadaanJasa = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: form.detilPengadaan.jasaPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(form.detilPengadaan.jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });
		
		form.viewModalVendorMaterial = function(dataItem) {
            var modalDataVendorMaterialInstance = $modal.open({
                templateUrl: '/modalDataVendorEachItem.html',
                controller: 'VendorEachItemModalController',
                size: 'lg',
                resolve: {
                	dataItem: function () {
                        return dataItem;
                    }
                }
            });
            modalDataVendorMaterialInstance.result.then(function (dataVendor) {
				var index = form.detilPengadaan.materialPengadaanList.indexOf(dataItem);
				form.detilPengadaan.materialPengadaanList[index].pendaftaranVendorList = dataVendor;
            });			
		}
		
		form.viewModalVendorJasa = function(dataItem) {
            var modalDataVendorJasaInstance = $modal.open({
                templateUrl: '/modalDataVendorEachItem.html',
                controller: 'VendorEachItemModalController',
                size: 'lg',
                resolve: {
                	dataItem: function () {
                        return dataItem;
                    }
                }
            });
            modalDataVendorJasaInstance.result.then(function (dataVendor) {
				var index = form.detilPengadaan.jasaPengadaanList.indexOf(dataItem);
				form.detilPengadaan.jasaPengadaanList[index].pendaftaranVendorList = dataVendor;
            });			
		}

        form.back = function () {
        	$location.path('/app/promise/procurement/evaluasiAdministrasi');
        }
        
        form.printDiv = function (divName) {
           var printContents = document.getElementById(divName).innerHTML;
           var popupWin = window.open('', '_blank', 'width=800,height=600');
           popupWin.document.open();
           popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
           popupWin.document.close();
       }

        form.btnSimpan = function () {
        	var validateData = true;
        	//validate data so all item and vendor got evaluated
        	if (form.detilPengadaan.materialPengadaanList !== undefined && form.detilPengadaan.materialPengadaanList.length > 0) {
        		angular.forEach(form.detilPengadaan.materialPengadaanList, function(material, index){
        			if (material.pendaftaranVendorList !== undefined && material.pendaftaranVendorList.length > 0) {
        			} else {
        				validateData = false;
        			}
        		});
        	}
        	if (form.detilPengadaan.jasaPengadaanList !== undefined && form.detilPengadaan.jasaPengadaanList.length > 0) {
        		angular.forEach(form.detilPengadaan.jasaPengadaanList, function(material, index){
        			if (material.pendaftaranVendorList !== undefined && material.pendaftaranVendorList.length > 0) {
        			} else {
        				validateData = false;
        			}
        		});
        	}
        	if (validateData ) {
        		// simpan evaluasi administrasi satuan
            	var targetURI = $rootScope.backendAddress + '/procurement/evaluasiAdministrasi/EvaluasiAdministrasiSatuanServices/create';
            	if (form.detilPengadaan.sistemEvaluasiPenawaran.id === 2) {
            		targetURI = $rootScope.backendAddress + '/procurement/evaluasiAdministrasi/EvaluasiAdministrasiSatuanMeritPointServices/create';
            	}

            	// material
            	if (form.detilPengadaan.materialPengadaanList !== undefined && form.detilPengadaan.materialPengadaanList.length > 0) {
            		angular.forEach(form.detilPengadaan.materialPengadaanList, function(material, index){
            			if (material.pendaftaranVendorList !== undefined && material.pendaftaranVendorList.length > 0) {
            				angular.forEach(material.pendaftaranVendorList, function(dataVendor, index){
            					if (dataVendor.labelStatus !== undefined) {
            						if (dataVendor.kriteriaAdministrasiList  !== undefined && dataVendor.kriteriaAdministrasiList.length > 0) {
                						var targetURLNilaiEvaluasiVendorSatuan = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/create';
                						var paramEvaluasiVendorSatuan = {
                								vendorId:dataVendor.vendor.id,
                								nilaiEvaluasiAdministrasi:dataVendor.kriteriaAdministrasiList[0].nilaiFinal,
                								nilaiEvaluasiTeknis:0,
                								nilaiEvaluasiHarga:0,
                								pengadaanId:form.detilPengadaan.id,
                								itemId:material.item.id
                						};
                						executePostTransaction(targetURLNilaiEvaluasiVendorSatuan, paramEvaluasiVendorSatuan);
                						
            							angular.forEach(dataVendor.kriteriaAdministrasiList, function(dataKriteria, index){
            								var paramsData = {
                    								vendorId: dataVendor.vendor.id,
                    			                	kriteriaAdministrasiId: dataKriteria.id,
                    			                	kriteriaAdministrasiMeritPointId:dataKriteria.id,
                    			                	itemId:material.item.id,
                    			                	status:(dataKriteria.status) ? 1 : 0,
                    			                	nilai:dataKriteria.nilaiInput
                    						};
            								executePostTransaction(targetURI, paramsData);
            							});
            						}        						
            					}
            				});
            			}        			
            		});
            	}

            	// jasa
            	if (form.detilPengadaan.jasaPengadaanList !== undefined && form.detilPengadaan.jasaPengadaanList.length > 0) {
            		angular.forEach(form.detilPengadaan.jasaPengadaanList, function(material, index){
            			if (material.pendaftaranVendorList !== undefined && material.pendaftaranVendorList.length > 0) {
            				angular.forEach(material.pendaftaranVendorList, function(dataVendor, index){
            					if (dataVendor.labelStatus !== undefined) {
            						if (dataVendor.kriteriaAdministrasiList  !== undefined && dataVendor.kriteriaAdministrasiList.length > 0) {
                						var targetURLNilaiEvaluasiVendorSatuan = $rootScope.backendAddress + '/procurement/NilaiEvaluasiVendorSatuanServices/create';
                						var paramEvaluasiVendorSatuan = {
                								vendorId:dataVendor.vendor.id,
                								nilaiEvaluasiAdministrasi:dataVendor.kriteriaAdministrasiList[0].nilaiFinal,
                								nilaiEvaluasiTeknis:0,
                								nilaiEvaluasiHarga:0,
                								pengadaanId:form.detilPengadaan.id,
                								itemId:material.item.id
                						};
                						executePostTransaction(targetURLNilaiEvaluasiVendorSatuan, paramEvaluasiVendorSatuan);
                						
            							angular.forEach(dataVendor.kriteriaAdministrasiList, function(dataKriteria, index){
            								var paramsData = {
                    								vendorId: dataVendor.vendor.id,
                    			                	kriteriaAdministrasiId: dataKriteria.id,
                    			                	kriteriaAdministrasiMeritPointId:dataKriteria.id,
                    			                	itemId:material.item.id,
                    			                	status:(dataKriteria.status) ? 1 : 0,
                    			                	nilai:dataKriteria.nilaiInput
                    						};
            								executePostTransaction(targetURI, paramsData);
            							});
            						}        						
            					}
            				});
            			}        			
            		});
            	}
            	
            	// update next tahapan ke pengadaan
            	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + form.detilPengadaan.id)
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
                            pengadaanId: form.detilPengadaan.id,
                            tahapanPengadaanId: form.nextTahapan
                        }
                    }).success(function (data, status, headers, config) {
                        $state.go('app.promise.procurement-panitia-evaluasiAdministrasi');
                    });
                });
        	} else {
        		$scope.messageModalError = "semua vendor untuk semua item harus di evaluasi terlebih dahulu";
        	}        	
        }
        
        var executePostTransaction = function(targetURL, dataParams) {
        	$http({
                method: 'POST',
                url: targetURL,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataParams
            });
        }
    }

    EvaluasiAdministrasiSatuanEditController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', 'ngTableParams', '$timeout', '$q', '$stateParams'];

})();

angular.module('naut')
.controller('VendorEachItemModalController', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, $modal, dataItem) {
	$scope.namaItem = dataItem.item.nama;
	
	$scope.pendaftaranVendorList = [];
	$scope.tableDataVendor = new ngTableParams({
        page: 1,
        count: 10
    }, {
        total: $scope.pendaftaranVendorList.length,
        getData: function($defer, params) {
            $defer.resolve($scope.pendaftaranVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
        }
    });
	
	if (dataItem.pendaftaranVendorList !== undefined && dataItem.pendaftaranVendorList.length > 0) {
		$scope.pendaftaranVendorList = dataItem.pendaftaranVendorList;
		$scope.tableDataVendor.reload();
	} else {
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + dataItem.pengadaan.id).
		success(function (data, status, headers, config) {
			$scope.pendaftaranVendorList = data;
			$scope.tableDataVendor.reload();
		});
	}
	
	$scope.buttonViewModalKriteria = function (dataVendor) {
		if (dataItem.pengadaan.sistemEvaluasiPenawaran.id === 1) {
			var modalKriteriaSistemGugurItemInstance = $modal.open({
                templateUrl: '/modalKriteriaSistemGugur.html',
                controller: 'ModalKriteriaSistemGugurItemController',
                size: 'lg',
                resolve: {
                	dataItem: function() {
                		return dataItem;
                	},
                	dataVendor: function() {
                		return dataVendor;
                	}
                }
            });
			modalKriteriaSistemGugurItemInstance.result.then(function (dataKriteriaList) {
				var index = $scope.pendaftaranVendorList.indexOf(dataVendor);
				if (dataKriteriaList !== undefined && dataKriteriaList.length > 0) {
					$scope.pendaftaranVendorList[index].kriteriaAdministrasiList = dataKriteriaList;
					$scope.pendaftaranVendorList[index].nilaiFinal = dataKriteriaList[0].nilaiFinal;
					$scope.pendaftaranVendorList[index].labelStatus = dataKriteriaList[0].labelStatus;
					$scope.tableDataVendor.reload();
				}
            });
		} else if (dataItem.pengadaan.sistemEvaluasiPenawaran.id === 2) {
			var modalKriteriaMeritPointInstance = $modal.open({
                templateUrl: '/modalKriteriaMeritPoint.html',
                controller: 'ModalKriteriaMeritPointController',
                size: 'lg',
                resolve: {
                	dataItem: function() {
                		return dataItem;
                	},
                	dataVendor: function() {
                		return dataVendor;
                	}
                }
            });
			modalKriteriaMeritPointInstance.result.then(function (dataKriteriaList) {
				var index = $scope.pendaftaranVendorList.indexOf(dataVendor);
				if (dataKriteriaList !== undefined && dataKriteriaList.length > 0) {
					$scope.pendaftaranVendorList[index].kriteriaAdministrasiList = dataKriteriaList;
					$scope.pendaftaranVendorList[index].nilaiFinal = dataKriteriaList[0].nilaiFinal;
					$scope.pendaftaranVendorList[index].labelStatus = dataKriteriaList[0].labelStatus;
					$scope.tableDataVendor.reload();
				}
            });			
		}
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.ok = function() {
		if ($scope.pendaftaranVendorList !== undefined && $scope.pendaftaranVendorList.length > 0) {
			var validateAllVendor = true;
			angular.forEach($scope.pendaftaranVendorList, function(daftarVendor, index){
				if (daftarVendor.nilaiFinal !== undefined) {					
				} else {
					validateAllVendor = false;					
				}
			});
			if (validateAllVendor === false) {
				$scope.messageModalError = "semua vendor harus di evaluasi terlebih dahulu";
			} else {
				$modalInstance.close($scope.pendaftaranVendorList);
			}
		}
	}
	
});

angular.module('naut')
.controller('ModalKriteriaSistemGugurItemController', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, $modal, dataItem, dataVendor) {
	$scope.detailVendor = dataVendor;
	
	$scope.kriteriaAdministrasiList=[];
	$scope.tableKriteriaSistemGugur = new ngTableParams({
        page: 1,
        count: 10
    }, {
        total: $scope.kriteriaAdministrasiList.length,
        getData: function($defer, params) {
            $defer.resolve($scope.kriteriaAdministrasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
        }
    });
	
	if (dataVendor.kriteriaAdministrasiList !== undefined && dataVendor.kriteriaAdministrasiList.length > 0) {
		$scope.kriteriaAdministrasiList = dataVendor.kriteriaAdministrasiList;
		$scope.tableKriteriaSistemGugur.reload();
		
		if (dataVendor.kriteriaAdministrasiList[0].nilaiFinal !== undefined && dataVendor.kriteriaAdministrasiList[0].nilaiFinal === 100) {
			$scope.pilihStatus = {status:100};
		} else {
			$scope.pilihStatus = {status:0};
		}
	} else {
		$scope.pilihStatus = {status:0};
		$http.get($rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/getByPengadaanIdList/' + dataItem.pengadaan.id)
		.success(function (data, status, headers, config) {
			$scope.kriteriaAdministrasiList = data;
			angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
				kriteria.status = false;
			});
			$scope.tableKriteriaSistemGugur.reload();			
		});
	}
	
	$scope.checkAll = function() {
		if ($scope.pilihStatus.status !== undefined && $scope.pilihStatus.status === '100') {
			angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
				kriteria.status = true;
			});
		} else {
			angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
				kriteria.status = false;
			});
		}
	}
	
	$scope.checkEachStatus = function() {
		var checkAllStatus = true;
		if ($scope.kriteriaAdministrasiList !== undefined && $scope.kriteriaAdministrasiList.length > 0) {
			angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
				if (kriteria.status === false) {					
					checkAllStatus = false;
				}
			});			
		}
		if (checkAllStatus === false) {
			$scope.pilihStatus = {status:0};
		} else {
			$scope.pilihStatus = {status:100};
		}
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.ok = function() {
		if ($scope.kriteriaAdministrasiList !== undefined && $scope.kriteriaAdministrasiList.length > 0) {
			$scope.kriteriaAdministrasiList[0].nilaiFinal = $scope.pilihStatus.status;
			if ($scope.pilihStatus.status == 0) {
				$scope.kriteriaAdministrasiList[0].labelStatus = 'TIDAK LULUS';
			} else {
				$scope.kriteriaAdministrasiList[0].labelStatus = 'LULUS';
			}
		}
		$modalInstance.close($scope.kriteriaAdministrasiList);
	}
	
});

angular.module('naut')
.controller('ModalKriteriaMeritPointController', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, $modal, dataItem, dataVendor) {
	$scope.detailVendor = dataVendor;
	
	$scope.kriteriaAdministrasiList=[];
	$scope.tableKriteriaMeritPoint = new ngTableParams({
        page: 1,
        count: 10
    }, {
        total: $scope.kriteriaAdministrasiList.length,
        getData: function($defer, params) {
            $defer.resolve($scope.kriteriaAdministrasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
        }
    });
	
	$scope.total = 0;
	$scope.totalInput = 0;
	if (dataVendor.kriteriaAdministrasiList !== undefined && dataVendor.kriteriaAdministrasiList.length > 0) {
		$scope.kriteriaAdministrasiList = dataVendor.kriteriaAdministrasiList;
		$scope.tableKriteriaMeritPoint.reload();
		
		angular.forEach(dataVendor.kriteriaAdministrasiList, function(nilaiKriteria, index){
			$scope.total = $scope.total + nilaiKriteria.nilai;
			$scope.totalInput = $scope.totalInput + nilaiKriteria.nilaiInput;
		});
	} else {
		$http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointServices/getByPengadaanIdList/' + dataItem.pengadaan.id)
		.success(function (data, status, headers, config) {
			$scope.kriteriaAdministrasiList = data;
			angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
				$scope.total = $scope.total + kriteria.nilai;
				kriteria.nilaiInput = 0;
			});
			$scope.tableKriteriaMeritPoint.reload();			
		});
	}
	
	$scope.accumulate = function (data) {
		$scope.totalInput = 0;
		angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
			if (kriteria.nilaiInput > 100) {
				kriteria.nilaiInput = 100;
                alert("masukkan nilai antara 0-100");
			}
			$scope.totalInput = $scope.totalInput + parseInt(kriteria.nilaiInput);
		});
    }
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.ok = function() {
		if ($scope.kriteriaAdministrasiList !== undefined && $scope.kriteriaAdministrasiList.length > 0) {
			$http.get($rootScope.backendAddress + '/procurement/kriteriaAdminMeritPointPembobotanServices/getByPengadaanIdList/' + dataItem.pengadaan.id)
			.success(function (data, status, headers, config) {
				if (data !== undefined && data.length > 0) {
					var accumulateInput = 0;
					angular.forEach($scope.kriteriaAdministrasiList, function(kriteria, index){
						accumulateInput = accumulateInput + ((kriteria.nilai * kriteria.nilaiInput) / 100);
					});
					$scope.kriteriaAdministrasiList[0].nilaiFinal = accumulateInput;
					if (accumulateInput >= data[0].nilaiMinimalKelulusanAdministrasi) {
						$scope.kriteriaAdministrasiList[0].labelStatus = 'LULUS';
					} else {
						$scope.kriteriaAdministrasiList[0].labelStatus = 'TIDAK LULUS';
					}
					$modalInstance.close($scope.kriteriaAdministrasiList);
				}			
			});			
		}
	}
	
});
