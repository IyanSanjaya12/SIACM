/**=========================================================
 * Module: SeleksiTeknisController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiTeknisController', SeleksiTeknisController);

    function SeleksiTeknisController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	var bankCheck = [];
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.jenisPengadaan = $scope.detilPengadaan.jenisPengadaan.id;
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Vendor ============================================ */
        form.listVendorTeknis = $rootScope.vendorLulusListDariKriteriaKeuangan;
        angular.forEach(form.listVendorTeknis,function(value,index){
        	value.vendor.statusKelulusan = "";
        	value.tableId = "";
        });
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
        
        
        /* ================================= START Seleksi Teknis ========================================= */
        $scope.getListSeleksiTeknis = function () {
        	$scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiTeknisServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
            	$scope.loading = false;
                var dataVendor = form.listVendorTeknis;
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
                	form.listVendorTeknis = dataVendor;
                }
                
                $scope.seleksiTeknisList = data;
                
                if($scope.seleksiTeknisList.length == 0) {
                	angular.forEach(form.listVendorTeknis,function(value,index){
                    	value.vendor.statusKelulusan = "";
                    	value.tableId = "";
                    })
                }
                
                $scope.daftarVendorTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.listVendorTeknis.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.listVendorTeknis.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }
        $scope.getListSeleksiTeknis();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        $scope.viewModal = function(index, dataVendor) {
        	var dkamodalinstance = $modal.open({
            	templateUrl: '/daftarKriteriaTeknis.html',
                controller: 'daftarKriteriaTeknis',
                size: 'lg',
                resolve: {
                	dataVendor: function () { 
                		return dataVendor; 
                	},
                	bidangUsahaList: function() {
                		return $rootScope.bidangUsahaList;
                	},
                	daftarKriteriaTeknisList: function() {
                		return $scope.daftarKriteriaTeknisList;
                	},
                	seleksiTeknisList: function() {
                		return $scope.seleksiTeknisList;
                	}
                }
            });
        	dkamodalinstance.result.then(function(dataVendor){
            	if (dataVendor != undefined && dataVendor !== null) {
            		angular.forEach(form.listVendorTeknis,function(value,index){
            			if(value.vendor.id == dataVendor.vendor.id) {
            				form.listVendorTeknis.splice(form.listVendorTeknis.indexOf(value), 1);
            				form.listVendorTeknis.push(dataVendor);
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
        	$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikeuangan");
        }
        
        $scope.lanjut = function () {
        	var kesalahan = false;
        	$rootScope.vendorLulusListDariKriteriaTeknis = [];
        	angular.forEach(form.listVendorTeknis,function(value,index){
        		if(value.vendor.statusKelulusan == "") {
        			kesalahan = true;
        		} else if(value.vendor.statusKelulusan == "Lulus") {
        			$rootScope.vendorLulusListDariKriteriaTeknis.push(value);
        		}
        	});
        	
        	if(kesalahan) {
        		toaster.pop('error', 'Kesalahan', 'Masih ada status kelulusan yang kosong!');
        	} else {
        		$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksinilaikemampuanpaket");
        	}
        }
        /* ------------------------------------------------------------------------------------------------ */
    }
    
    SeleksiTeknisController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();



/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */
angular.module('naut')
.controller('daftarKriteriaTeknis', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, toaster, dataVendor, bidangUsahaList, daftarKriteriaTeknisList, seleksiTeknisList) {
	$scope.bidangUsahaList = bidangUsahaList;
	
	$scope.vendor = {
			nama: dataVendor.vendor.nama,
			kualifikasi: dataVendor.pengadaan.kualifikasiVendor.nama,
			nilaiTertinggiBidangPekerjaan: daftarKriteriaTeknisList[0].nilaiTertinggiBidangPekerjaan,
			nilaiMaxKontrak: daftarKriteriaTeknisList[0].nilaiKontrak,
			nilaiMaxStatusPenyedia: daftarKriteriaTeknisList[0].nilaiStatusPenyedia,
			nilaiMaxPersonil: daftarKriteriaTeknisList[0].nilaiPersonil,
			nilaiMaxPeralatan: daftarKriteriaTeknisList[0].nilaiPeralatan,
			nilaiMaxMutu: daftarKriteriaTeknisList[0].nilaiMutu,
			nilaiAmbang: daftarKriteriaTeknisList[0].nilaiAmbang,
			nilaiBidangPekerjaan: 0,
			nilaiKontrak: 0,
			nilaiStatusPenyedia: 0,
			nilaiPersonil: 0,
			nilaiPeralatan: 0,
			nilaiMutu: 0
		}
	
	if(seleksiTeknisList != undefined && seleksiTeknisList.length > 0) {
		angular.forEach(seleksiTeknisList,function(value,index){
			if(value.vendor.id == dataVendor.vendor.id) {
				var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiTeknisServices/getBySeleksiKualifikasiTeknis/' + value.id;
		        $http.get(uri, {
		            ignoreLoadingBar: true
		        }).success(function (data, status, headers, config) {
		            $scope.loading = false;
		            if(data != undefined) {
		            	$scope.vendor.nilaiBidangPekerjaan = data[0].nilaiBidangPekerjaan;
		            	$scope.vendor.nilaiKontrak = data[0].nilaiKontrak;
		            	$scope.vendor.nilaiStatusPenyedia = data[0].nilaiStatusPenyedia;
		            	$scope.vendor.nilaiPersonil = data[0].nilaiPersonil;
		            	$scope.vendor.nilaiPeralatan = data[0].nilaiPeralatan;
		            	$scope.vendor.nilaiMutu = data[0].nilaiMutu;
		            	$scope.vendor.tableId = data[0].id;
		            }
		        }).error(function (data, status, headers, config) {});
			}
		})
	}
	
	$scope.setNilaiBP = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiTertinggiBidangPekerjaan) {
			alert("Nilai Bidang Pekerjaan tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiBidangPekerjaan")[0].focus();
		}
	}
	
	$scope.setNilaiKontrak = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiMaxKontrak) {
			alert("Nilai Kontrak tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiKontrak")[0].focus();
		}
	}
	
	$scope.setNilaiSP = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiMaxStatusPenyedia) {
			alert("Nilai Status Penyedia tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiStatusPenyedia")[0].focus();
		}
	}
	
	$scope.setNilaiPersonil = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiMaxPersonil) {
			alert("Nilai Personil tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiPersonil")[0].focus();
		}
	}
	
	$scope.setNilaiPeralatan = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiMaxPeralatan) {
			alert("Nilai Peralatan tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiPeralatan")[0].focus();
		}
	}
	
	$scope.setNilaiMutu = function(nilai) {
		if(nilai < 0 || nilai > $scope.vendor.nilaiMaxMutu) {
			alert("Nilai Manajemen Mutu tidak boleh lebih dari Nilai Maksimum");
			document.getElementsByName("nilaiMutu")[0].focus();
		}
	}
	
	$scope.btnSimpan = function() {
		var totalNilai = $scope.vendor.nilaiBidangPekerjaan + $scope.vendor.nilaiKontrak + $scope.vendor.nilaiStatusPenyedia + $scope.vendor.nilaiPersonil 
		+ $scope.vendor.nilaiPeralatan + $scope.vendor.nilaiMutu;
		
		if(totalNilai < $scope.vendor.nilaiAmbang) {
			dataVendor.vendor.statusKelulusan = "Tidak Lulus";
			$scope.statusKelulusan = 0;
			var peringatan = confirm('Total Nilai Inputan ('+totalNilai+') KURANG dari Nilai Ambang Lulus ('+$scope.vendor.nilaiAmbang+')')
			if(peringatan){
				simpanDataSeleksi($scope.vendor);
		    	$modalInstance.close(dataVendor);
			}
		} else {
			dataVendor.vendor.statusKelulusan = "Lulus";
			$scope.statusKelulusan = 1;
			var peringatan = confirm('Total Nilai Inputan ('+totalNilai+') SAMA DENGAN/LEBIH dari Nilai Ambang Lulus ('+$scope.vendor.nilaiAmbang+')')
			if(peringatan){
				simpanDataSeleksi($scope.vendor);
		    	$modalInstance.close(dataVendor);
			}
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
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiTeknisServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiTeknisServices/update';
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
			seleksiKualifikasiTeknisId: seleksiId,
			daftarKriteriaKualifikasiTeknisId: daftarKriteriaTeknisList[0].id,
			nilaiBidangPekerjaan: dataSimpan.nilaiBidangPekerjaan,
    		nilaiKontrak: dataSimpan.nilaiKontrak,
    		nilaiStatusPenyedia: dataSimpan.nilaiStatusPenyedia,
    		nilaiPersonil: dataSimpan.nilaiPersonil,
    		nilaiPeralatan: dataSimpan.nilaiPeralatan,
    		nilaiMutu: dataSimpan.nilaiMutu
		}
		
		if (paramData.id == undefined || paramData.id == ""){
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiTeknisServices/insert';
        } else {
        	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/detailNilaiKualifikasiTeknisServices/update';
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
	
	$scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
    }
});