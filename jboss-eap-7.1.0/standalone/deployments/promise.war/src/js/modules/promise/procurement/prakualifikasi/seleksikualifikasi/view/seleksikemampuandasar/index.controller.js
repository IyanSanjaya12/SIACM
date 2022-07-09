/**=========================================================
 * Module: SeleksiKemampuanDasarController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiKemampuanDasarController', SeleksiKemampuanDasarController);

    function SeleksiKemampuanDasarController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	var bankCheck = [];
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
//    	$scope.detilPengadaan.jenisPengadaan.id = 2; //------> Untuk Uji Coba Pengadaan JASA
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.jenisPengadaan = $scope.detilPengadaan.jenisPengadaan.id;
        $scope.totalHPSMaterial = $rootScope.totalHPSBarang + $rootScope.totalHPSJasa;
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Vendor ============================================ */
        form.listVendorKD = $rootScope.vendorLulusListDariKriteriaDB;
        angular.forEach(form.listVendorKD,function(value,index){
        	value.vendor.statusKelulusan = "";
        	value.tableId = "";
        })
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Daftar Kriteria Teknis ================================= */
        $scope.getListDaftarKriteriaTeknis = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.daftarKriteriaTeknisList = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListDaftarKriteriaTeknis();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Seleksi Kemampuan Dasar ================================ */
        $scope.getListSeleksiKriteriaKD = function () {
        	$scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKemampuanDasarServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
            	$scope.loading = false;
                var dataVendor = form.listVendorKD;
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
                	form.listVendorKD = dataVendor;
                }
                
                $scope.seleksiKemampuanDasarList = data;
                
                if($scope.seleksiKemampuanDasarList.length == 0) {
                	angular.forEach(form.listVendorKD,function(value,index){
                    	value.vendor.statusKelulusan = "";
                    	value.tableId = "";
                    })
                }
                
                $scope.daftarVendorTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.listVendorKD.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.listVendorKD.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }
        $scope.getListSeleksiKriteriaKD();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        
        $scope.viewModal = function(index, dataVendor) {
        	var dkamodalinstance = $modal.open({
            	templateUrl: '/daftarKriteriaKualifikasiKD.html',
                controller: 'daftarKriteriaKualifikasiKD',
                size: 'lg',
                resolve: {
                	dataVendor: function () { 
                		return dataVendor; 
                	},
                	daftarKriteriaTeknisList: function() {
                		return $scope.daftarKriteriaTeknisList;
                	},
                	bidangUsahaList: function() {
                		return $rootScope.bidangUsahaList;
                	},
                	totalHPSMaterial: function() {
                		return $scope.totalHPSMaterial;
                	},
                	seleksiKemampuanDasarList: function() {
                		return $scope.seleksiKemampuanDasarList;
                	}
                }
            });
        	dkamodalinstance.result.then(function(dataVendor){
            	if (dataVendor != undefined && dataVendor !== null) {
            		angular.forEach(form.listVendorKD,function(value,index){
            			if(value.vendor.id == dataVendor.vendor.id) {
            				form.listVendorKD.splice(form.listVendorKD.indexOf(value), 1);
            				form.listVendorKD.push(dataVendor);
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
        	$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksidukunganbank");
        }
        
        $scope.lanjut = function () {
        	var kesalahan = false;
        	$rootScope.vendorLulusListDariKriteriaKD = [];
        	angular.forEach(form.listVendorKD,function(value,index){
        		if(value.vendor.statusKelulusan == "") {
        			kesalahan = true;
        		} else if(value.vendor.statusKelulusan == "Lulus") {
        			$rootScope.vendorLulusListDariKriteriaKD.push(value);
        		}
        	});
        	
        	if(kesalahan) {
        		toaster.pop('error', 'Kesalahan', 'Masih ada status kelulusan yang kosong!');
        	} else {
        		if($scope.jenisPengadaan == 1) {
        			$scope.updatePengadaan();
        			$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi");
        		} else {
        			$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikeuangan");
        		}
        	}
        }
        
        /* get Next Tahapan */
        $scope.getNextTahapan = function () {
        	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
        		ignoreLoadingBar: true
        	}).success(function (data, status, headers, config) {
                $scope.nextTahapan = data;
        	}).error(function (data, status, headers, config) {});
        }
        /* END get Next Tahapan */
        $scope.getNextTahapan();
        
      //update pengadaan
        $scope.updatePengadaan = function () {
            $scope.loading = true;
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
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        };
        /* ------------------------------------------------------------------------------------------------ */
    }
    
    SeleksiKemampuanDasarController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();


/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */
angular.module('naut')
.controller('daftarKriteriaKualifikasiKD', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, dataVendor, daftarKriteriaTeknisList, bidangUsahaList, totalHPSMaterial, seleksiKemampuanDasarList, toaster) {
	$scope.bidangUsahaList = bidangUsahaList;
	
	$scope.vendor = {
		nama: dataVendor.vendor.nama,
		kualifikasi: dataVendor.pengadaan.kualifikasiVendor.nama,
		NPP: totalHPSMaterial,
		konstanta: daftarKriteriaTeknisList[0].masterKriteriaKualifikasiTeknis.kode,
		mataUang: daftarKriteriaTeknisList[0].pengadaan.mataUang.kode
	}
	
	if(seleksiKemampuanDasarList != undefined && seleksiKemampuanDasarList.length > 0) {
		angular.forEach(seleksiKemampuanDasarList,function(value,index){
			if(value.vendor.id == dataVendor.vendor.id) {
				var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKemampuanDasarServices/getBySeleksiKualifikasiKemampuanDasar/' + value.id;
		        $http.get(uri, {
		            ignoreLoadingBar: true
		        }).success(function (data, status, headers, config) {
		            $scope.loading = false;
		            if(data != undefined) {
		            	$scope.vendor.NPT = data[0].npt;
		            	$scope.vendor.KD = data[0].nilaiKelulusan;
		            	$scope.vendor.penilaian = dataVendor.vendor.statusKelulusan;
		            	$scope.vendor.tableId = data[0].id;
		            }
		        }).error(function (data, status, headers, config) {});
			}
		})
	}
	
	$scope.setKD = function(nilaiInput) {
		$scope.vendor.KD = $scope.vendor.konstanta * nilaiInput;
	}
	
	$scope.penilaian = function() {
		if($scope.vendor.NPP > $scope.vendor.KD) {
			$scope.vendor.penilaian = "Tidak Lulus";
			$scope.statusKelulusan = 0;
		} else {
			$scope.vendor.penilaian = "Lulus";
			$scope.statusKelulusan = 1;
		}
	}
	
	$scope.btnSimpan = function() {
		if($scope.vendor.penilaian != undefined) {
			simpanDataSeleksi($scope.vendor);
	    	dataVendor.vendor.statusKelulusan = $scope.vendor.penilaian;
	    	
	    	$modalInstance.close(dataVendor);
		} else {
			alert("Data anda belum lengkap, silahkan lengkapi");
		}
    }
	
	$scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
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
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKemampuanDasarServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiKemampuanDasarServices/update';
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
				seleksiKualifikasiKemampuanDasarId: seleksiId,
				daftarKriteriaKualifikasiTeknisId: daftarKriteriaTeknisList[0].id,
				npp: dataSimpan.NPP,
				npt: dataSimpan.NPT,
				nilaiKelulusan: dataSimpan.KD
		}
		
		if (paramData.id == undefined || paramData.id == ""){
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKemampuanDasarServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiKemampuanDasarServices/update';
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