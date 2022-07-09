/**=========================================================
 * Module: SeleksiKeuanganController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiKeuanganController', SeleksiKeuanganController);

    function SeleksiKeuanganController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	var bankCheck = [];
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.jenisPengadaan = $scope.detilPengadaan.jenisPengadaan.id;
        $scope.totalHPSMaterial = $rootScope.totalHPSBarang + $rootScope.totalHPSJasa;
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Vendor ============================================ */
        form.listVendorKeuangan = $rootScope.vendorLulusListDariKriteriaKD;
        angular.forEach(form.listVendorKeuangan,function(value,index){
        	value.vendor.statusKelulusan = "";
        	value.tableId = "";
        });
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Daftar Kriteria Keuangan =============================== */
        $scope.getListDaftarKriteriaKeuangan = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiKeuanganServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.daftarKriteriaKeuanganList = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListDaftarKriteriaKeuangan();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Seleksi Keuangan ======================================= */
        $scope.getListSeleksiKeuangan = function () {
        	$scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKeuanganServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
            	$scope.loading = false;
                var dataVendor = form.listVendorKeuangan;
                if(data.length > 0) {
                	angular.forEach(data,function(value,index){
                		angular.forEach(dataVendor,function(value,indexVendor){
                			if(dataVendor[indexVendor].vendor.id == data[index].vendor.id){
                				if(data[index].statusKelulusan == 1) {
                					dataVendor[indexVendor].vendor.statusKelulusan = "Lulus";
                				} else {
                					dataVendor[indexVendor].vendor.statusKelulusan = "Tidak Lulus";
                				}
                				dataVendor[indexVendor].tableId = data[index].id;
                			} 
                		})
                	})
                	form.listVendorKeuangan = dataVendor;
                }
                
                $scope.seleksiKeuanganList = data;
                
                if($scope.seleksiKeuanganList.length == 0) {
                	angular.forEach(form.listVendorKeuangan,function(value,index){
                    	value.vendor.statusKelulusan = "";
                    	value.tableId = "";
                    })
                }
                
                $scope.daftarVendorTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.listVendorKeuangan.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.listVendorKeuangan.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }
        $scope.getListSeleksiKeuangan();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        $scope.viewModal = function(index, dataVendor) {
        	var dkamodalinstance = $modal.open({
            	templateUrl: '/daftarKriteriaKeuangan.html',
                controller: 'daftarKriteriaKeuangan',
                size: 'lg',
                resolve: {
                	dataVendor: function () { 
                		return dataVendor; 
                	},
                	bidangUsahaList: function() {
                		return $rootScope.bidangUsahaList;
                	},
                	daftarKriteriaKeuanganList: function() {
                		return $scope.daftarKriteriaKeuanganList;
                	},
                	seleksiKeuanganList: function() {
                		return $scope.seleksiKeuanganList;
                	},
                	totalHPSMaterial: function() {
                		return $scope.totalHPSMaterial;
                	}
                }
            });
        	dkamodalinstance.result.then(function(dataVendor){
            	if (dataVendor != undefined && dataVendor !== null) {
            		angular.forEach(form.listVendorKeuangan,function(value,index){
            			if(value.vendor.id == dataVendor.vendor.id) {
            				form.listVendorKeuangan.splice(form.listVendorKeuangan.indexOf(value), 1);
            				form.listVendorKeuangan.push(dataVendor);
            			}
            		});
            		$scope.daftarVendorTable.reload();
            	}
            });        	
        }
        
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        
        $scope.back = function () {
        	$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikemampuandasar");
        }
        
        $scope.lanjut = function () {
        	var kesalahan = false;
        	$rootScope.vendorLulusListDariKriteriaKeuangan = [];
        	angular.forEach(form.listVendorKeuangan,function(value,index){
        		if(value.vendor.statusKelulusan == "") {
        			kesalahan = true;
        		} else if(value.vendor.statusKelulusan == "Lulus") {
        			$rootScope.vendorLulusListDariKriteriaKeuangan.push(value);
        		}
        	});
        	
        	if(kesalahan) {
        		toaster.pop('error', 'Kesalahan', 'Masih ada status kelulusan yang kosong!');
        	} else {
        		$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiteknis");
        	}
        }
        /* ------------------------------------------------------------------------------------------------ */
    }
    
    SeleksiKeuanganController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();



/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */
angular.module('naut')
.controller('daftarKriteriaKeuangan', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, toaster, dataVendor, bidangUsahaList, daftarKriteriaKeuanganList, seleksiKeuanganList, totalHPSMaterial) {
	$scope.bidangUsahaList = bidangUsahaList;
	
	$scope.vendor = {
			nama: dataVendor.vendor.nama,
			kualifikasi: dataVendor.pengadaan.kualifikasiVendor.nama,
			FP: daftarKriteriaKeuanganList[0].faktorPerputaranModal,
			FL: daftarKriteriaKeuanganList[0].faktorLikuiditas,
			besarAmbang: daftarKriteriaKeuanganList[0].besarAmbang,
			NK: 0,
			prestasi: 0,
			nilaiSKK: 0
		}
	
	$scope.getDataRegistrasiKeuangan = function () {
    	$scope.loading = true;
        var uri = $rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/getKeuanganVendorByVendorId/' + dataVendor.vendor.id;
        $http.get(uri, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            $scope.loading = false;
            if(data != undefined && data.length > 0) {
            	$scope.vendor.KB = data[0].kekayaanBersih;
            } else {
            	$scope.vendor.KB = 0;
            }
            $scope.dataKeuangan = data[0];
            $scope.dataKeuangan.tahunKeuangan = parseInt($scope.dataKeuangan.tahunKeuangan, 10); 
            
        }).error(function (data, status, headers, config) {

        });
    }
    $scope.getDataRegistrasiKeuangan();
	
	$scope.nilaiBatasAmbang = totalHPSMaterial * $scope.vendor.besarAmbang;
	
	if(seleksiKeuanganList != undefined && seleksiKeuanganList.length > 0) {
		angular.forEach(seleksiKeuanganList,function(value,index){
			if(value.vendor.id == dataVendor.vendor.id) {
				var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKeuanganServices/getBySeleksiKualifikasiKeuangan/' + value.id;
		        $http.get(uri, {
		            ignoreLoadingBar: true
		        }).success(function (data, status, headers, config) {
		            $scope.loading = false;
		            if(data != undefined) {
		            	$scope.vendor.KB = data[0].kekayaanBersih;
		            	$scope.vendor.NK = data[0].nilaiKontrak;
		            	$scope.vendor.prestasi = data[0].prestasi;
		            	$scope.vendor.nilaiSKK = data[0].nilaiSkk;
		            	$scope.vendor.tableId = data[0].id;
		            }
		        }).error(function (data, status, headers, config) {});
			}
		})
	}
	
	$scope.setNilaiSKK = function() {
		$scope.vendor.nilaiSKK = parseFloat($scope.vendor.FP) * parseFloat($scope.vendor.FL) * parseFloat($scope.vendor.KB) - (parseFloat($scope.vendor.NK) - parseFloat($scope.vendor.prestasi));
	}
	
	$scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
    }
	
	$scope.btnSimpan = function() {
		if($scope.vendor.KB > 0) {
			if($scope.vendor.NK != undefined && $scope.vendor.prestasi != undefined) {
				if($scope.vendor.NK >= 0 && $scope.vendor.prestasi >= 0) {
					if($scope.vendor.nilaiSKK >= $scope.nilaiBatasAmbang) {
						dataVendor.vendor.statusKelulusan = "Lulus";
						$scope.statusKelulusan = 1;
					} else {
						dataVendor.vendor.statusKelulusan = "Tidak Lulus";
						$scope.statusKelulusan = 0;
					}
					simpanDataSeleksi($scope.vendor);
			    	$modalInstance.close(dataVendor);
				} else {
					alert("Nilai NK dan Prestasi tidak boleh lebih kecil dari 0!");
				}
			} else {
				alert("Data anda belum lengkap, silahkan lengkapi");
			}
		} else {
			alert("Kekayaan Bersih belum Terisi, silahkan di isi terlebih dahulu pada Menu Registrasi Keuangan Vendor");
		}
    }
	
	var simpanDataSeleksi = function(dataSimpan) {
		//console.log("ISI DATA VENDOR = "+JSON.stringify(dataVendor));
		var paramData = {
			id: dataVendor.tableId,
    		pengadaanId: dataVendor.pengadaan.id,
    		vendorId: dataVendor.vendor.id,
    		statusKelulusan: $scope.statusKelulusan
    	}
		
		if (paramData.id == undefined || paramData.id == ""){
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKeuanganServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKeuanganServices/update';
        }
		
		$http({
            method: 'POST',
            url: targetURI,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: paramData
        }).success(function (data, status, headers, config) {
        	simpanDataNilaiKualifikasi(dataSimpan, data.id);
        });
	}
	
	var simpanDataNilaiKualifikasi = function(dataSimpan, seleksiId) {
		var paramData = {
				id: dataSimpan.tableId,
				seleksiKualifikasiKeuanganId: seleksiId,
				daftarKriteriaKualifikasiKeuanganId: daftarKriteriaKeuanganList[0].id,
				kekayaanBersih: dataSimpan.KB,
				nilaiKontrak: dataSimpan.NK,
				prestasi: dataSimpan.prestasi,
				nilaiSkk: dataSimpan.nilaiSKK
		}
		
		if (paramData.id == undefined || paramData.id == ""){
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKeuanganServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKeuanganServices/update';
        }
		
		$http({
            method: 'POST',
            url: targetURI,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: paramData
        }).success(function (data, status, headers, config) {
        	
        });
	}
});