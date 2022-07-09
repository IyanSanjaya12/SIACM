/**=========================================================
 * Module: SeleksiAdministrasiController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiAdministrasiController', SeleksiAdministrasiController);

    function SeleksiAdministrasiController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $rootScope.vendorLulusListDariKriteriaAdmin = [];
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Vendor ============================================ */
        $scope.getListVendor = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/inisialisasi/prakualifikasi/pendaftaranVendorPraKualifikasiServices/getPendaftaranVendorPraKualifikasiByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                form.listVendors = data;
                
                $scope.daftarVendorTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.listVendors.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.listVendors.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Daftar Kriteria Administrasi =========================== */
        $scope.getListDaftarKriteriaAdmin = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiAdministrasiServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.daftarKriteriaAdminList = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListDaftarKriteriaAdmin();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Bidang Usaha Pengadaan ================================= */
        $scope.getListBidangUsaha = function () {
        	$scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	$rootScope.bidangUsahaList = data;
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
            	$scope.loading = false;
            });
        }
        $scope.getListBidangUsaha();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ======================== START Rincian Kebutuhan Material ====================================== */
        var jumlahBarang = 0;
        var jumlahJasa = 0;
        $scope.getListBarang = function () {
        	$scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	if(data.length > 0) {
            		angular.forEach(data,function(value,index){
            			jumlahBarang = jumlahBarang + value.totalHPS;
            		})
            	}
            	$rootScope.totalHPSBarang = jumlahBarang;
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
        }
        
        $scope.getListJasa = function () {
        	$scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	if(data.length > 0){
            		angular.forEach(data,function(value,index){
            			jumlahJasa = jumlahJasa + value.totalHPS;
            		})
            	}
            	$rootScope.totalHPSJasa = jumlahJasa;
                $scope.loading = false;
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
        }
        
        $scope.getListBarang();
        $scope.getListJasa();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Seleksi Kualifikasi Administrasi ======================= */
        $scope.getListSeleksiKriteriaAdmin = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiAdministrasiServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                var dataVendor = form.listVendors;
                if(data.length > 0) {
                	angular.forEach(data,function(value,index){
                		angular.forEach(dataVendor,function(value,indexVendor){
                			if(dataVendor[indexVendor].vendor.id == data[index].vendor.id){
                				if(data[index].statusKelulusan == 1)
                					dataVendor[indexVendor].vendor.statusKelulusan = "Lulus";
                				else
                					dataVendor[indexVendor].vendor.statusKelulusan = "Tidak Lulus";
                			} 
                		})
                	})
                }
                form.listVendors = dataVendor;
                $scope.seleksiAdministrasiList = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListSeleksiKriteriaAdmin();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        
        $scope.viewModal = function(index, dataVendor) {
        	var dkamodalinstance = $modal.open({
            	templateUrl: '/daftarKriteriaKualifikasiAdmin.html',
                controller: 'daftarKriteriaKualifikasiAdmin',
                size: 'lg',
                resolve: {
                	dataVendor: function () { 
                		return dataVendor; 
                	},
                	daftarKriteriaAdminList: function() {
                		return $scope.daftarKriteriaAdminList;
                	},
                	bidangUsahaList: function() {
                		return $scope.bidangUsahaList;
                	},
                	seleksiAdministrasiList: function() {
                		return $scope.seleksiAdministrasiList;
                	}
                }
            });
        	dkamodalinstance.result.then(function(dataVendor){
            	if (dataVendor != undefined && dataVendor !== null) {
            		form.listVendors = dataVendor;
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
            if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi");
                }
            } else {
                $location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi");
            }
        }
        
        $scope.lanjut = function () {
        	var kesalahan = false;
        	angular.forEach(form.listVendors,function(value,index){
        		if(value.vendor.statusKelulusan == undefined) {
        			kesalahan = true;
        		} else {
        			if(value.vendor.statusKelulusan == "Lulus")
        				$rootScope.vendorLulusListDariKriteriaAdmin.push(value);
        		}
        	})
        	
        	if(kesalahan)
        		toaster.pop('error', 'Kesalahan', 'Masih ada status kelulusan yang kosong!');
    		else
    			$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksidukunganbank");
    	
        }
        /* ------------------------------------------------------------------------------------------------ */
    }
    
    SeleksiAdministrasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();


/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */
angular.module('naut')
.controller('daftarKriteriaKualifikasiAdmin', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, seleksiAdministrasiList, dataVendor, daftarKriteriaAdminList, bidangUsahaList, toaster) {
	var adminCheck = [];
	$scope.bidangUsahaList = bidangUsahaList;
	
	$scope.vendor = {
		nama: dataVendor.vendor.nama,
		kualifikasi: dataVendor.pengadaan.kualifikasiVendor.nama
	}
	
	if(seleksiAdministrasiList.length > 0){
		angular.forEach(daftarKriteriaAdminList,function(value,index){
			value.active = false;
		})
		angular.forEach(seleksiAdministrasiList,function(value,index){
			if(value.vendor.id == dataVendor.vendor.id) {
				$scope.view = true;
				if(value.statusKelulusan == 1)
					$scope.lulus = true;
				else
					$scope.lulus = false;
				
				$scope.loading = true;
	            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiAdministrasiServices/getBySeleksiKualifikasiAdministrasi/' + value.id;
	            $http.get(uri, {
	                ignoreLoadingBar: true
	            }).success(function (data, status, headers, config) {
	                $scope.loading = false;
	                if(data.length > 0) {
//	                	console.log("DATA NILAI = "+JSON.stringify(data));
//	                	console.log("DAFTAR KRITERIA = "+JSON.stringify(daftarKriteriaAdminList));
	                	angular.forEach(data,function(value,index){
	                		angular.forEach(daftarKriteriaAdminList,function(value,indexDka){
	                			if(data[index].daftarKriteriaKualifikasiAdministrasi.id == daftarKriteriaAdminList[indexDka].id)
	                				daftarKriteriaAdminList[indexDka].active = true;
	                		})
	                	})
	                }
	            }).error(function (data, status, headers, config) {});
			}
		})
	} else {
		$scope.view = false;
		angular.forEach(daftarKriteriaAdminList,function(value,index){
			value.active = false;
		})
	}
	
	$scope.daftarKriteriaAdminTable = new ngTableParams({
        page: 1, // show first page
        count: 5 // count per page
    }, {
        total: daftarKriteriaAdminList.length, // length of data4
        getData: function ($defer, params) {
            $defer.resolve(daftarKriteriaAdminList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });
    
    $scope.changeAdminCheck = function(list, active) {
		if(list.active) {
			list.nilai = '100';
			adminCheck.push(list);
        } else {
        	adminCheck.splice(adminCheck.indexOf(list), 1);
        }
		
		if(adminCheck.length > 0) {
			$scope.pilihSemua(true);
			$scope.lulus = true;
		} else {
			$scope.pilihSemua(false);
			$scope.lulus = false;
		}
    }
	
	$scope.pilihSemua = function(active) {
		if(active) {
			$scope.statusKelulusan = 1;
		} else {
			$scope.statusKelulusan = 0;
		}

	}
	
    $scope.btnSimpan = function() {
    	simpanDataSeleksi(adminCheck);
    	$modalInstance.close($scope.dataVendor);
    }
    
    var simpanDataSeleksi = function(dataSimpan) {
    	//Simpan Seleksi Kualifikasi Administrasi
    	if($scope.statusKelulusan == undefined)
    		$scope.statusKelulusan = 0;
    	
    	var paramData = {
    			pengadaanId: dataVendor.pengadaan.id,
    			vendorId: dataVendor.vendor.id,
    			statusKelulusan: $scope.statusKelulusan
    	}
    	
    	$http({
            method: 'POST',
            url: $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiAdministrasiServices/insert',
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
        	if (typeof data !== 'undefined') {
            	var seleksiKualifikasiAdministrasiId = data.id;
            	if(data.statusKelulusan == 1)
            		dataVendor.vendor.statusKelulusan = "Lulus";
            	else
            		dataVendor.vendor.statusKelulusan = "Tidak Lulus";
            	
            	$scope.dataVendor = dataVendor;
            	if(dataSimpan.length > 0)
            		simpanDataNilaiKualifikasi(dataSimpan, seleksiKualifikasiAdministrasiId);
        	}
        });
    }
    
    var simpanDataNilaiKualifikasi = function(dataSeleksi, seleksiId) {
    	angular.forEach(dataSeleksi,function(value,index){
    		var paramData = {
    				seleksiKualifikasiAdministrasiId: seleksiId,
    				daftarKriteriaKualifikasiAdministrasiId: value.id
    		}
    		if(value.nilai == '100')
    			paramData.nilaiKelulusan = 100;
    		else
    			paramData.nilaiKelulusan = 0;
    		
    		$http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiAdministrasiServices/insert',
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
    	})
    }
    
	$scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
    }
});