/**=========================================================
 * Module: KriteriaKualifikasiViewJasaController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaKualifikasiViewJasaController', KriteriaKualifikasiViewJasaController);

    function KriteriaKualifikasiViewJasaController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	
    	$scope.jumlahNilai = 0;
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.jenisPengadaan = $scope.detilPengadaan.jenisPengadaan.id;
        form.dataKriteriaKualifikasiAdminList = [];
        $scope.tambahanDataKriteriaTeknis = {
        		nilaiTertinggiBidangPekerjaan: 0,
        		nilaiKontrak: 0,
        		nilaiStatusPenyedia: 0,
        		nilaiPersonil: 0,
        		nilaiPeralatan: 0,
        		nilaiMutu: 0,
        		nilaiAmbang: 0
        }
        
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Master ============================================ */
        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiTeknisServices/getList')
        .success(function (data, status, headers, config) {
        	if(data != undefined) {
        		$scope.kriteriaKualifikasiTeknisList = data;
        	} 
        });
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Kriteria Kualifikasi ============================== */
        //Daftar Kriteria Kualifikasi Administrasi
        $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiAdministrasiServices/getByPengadaan/' + $scope.pengadaanId)
        .success(function (data, status, headers, config) {
        	var dataDariDB = [];
        	if(data.length > 0) {
        		angular.forEach(data,function(value,index){
        			var tambahanData = {
        					daftarId: value.id,
        					pengadaanId: value.pengadaan.id
        			}
        			angular.extend(value.masterKriteriaKualifikasiAdministrasi, tambahanData);
        			dataDariDB.push(value.masterKriteriaKualifikasiAdministrasi);
        		});
        		form.dataKriteriaKualifikasiAdminList = dataDariDB;
        	}
        	
        	$scope.kriteriaKualifikasiTable = new ngTableParams({
                page: 1, // show first page
                count: 5 // count per page
            }, {
                total: form.dataKriteriaKualifikasiAdminList.length, // length of data4
                getData: function ($defer, params) {
                    $defer.resolve(form.dataKriteriaKualifikasiAdminList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
        });
        
        //Daftar Kriteria Kualifikasi Teknis
        $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/getByPengadaan/' + $scope.pengadaanId)
        .success(function (data, status, headers, config) {
        	if(data.length > 0) {
        		angular.forEach($scope.kriteriaKualifikasiTeknisList,function(value,index){
        			if(value.id == data[0].masterKriteriaKualifikasiTeknis.id) {
        				value.pilih = value;
        				var tambahanData = {
            				daftarId: data[0].id,
            				pengadaanId: data[0].pengadaan.id,
            				nilaiTertinggiBidangPekerjaan: data[0].nilaiTertinggiBidangPekerjaan,
                    		nilaiKontrak: data[0].nilaiKontrak,
                    		nilaiStatusPenyedia: data[0].nilaiStatusPenyedia,
                    		nilaiPersonil: data[0].nilaiPersonil,
                    		nilaiPeralatan: data[0].nilaiPeralatan,
                    		nilaiMutu: data[0].nilaiMutu,
                    		nilaiAmbang: data[0].nilaiAmbang
            			}
        				angular.extend(value, tambahanData);
        				form.kriteriaKualifikasiTeknis = value;
        				$scope.deleteKriteriaKualifikasiTeknis = value;
        				$scope.tambahanDataKriteriaTeknis = tambahanData;
//        				console.info(form.kriteriaKualifikasiTeknis)
        			}
        		});
        	}
        });
        
        //Daftar Kriteria Kualifikasi Keuangan
        $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiKeuanganServices/getByPengadaan/' + $scope.pengadaanId)
        .success(function (data, status, headers, config) {
        	if(data.length > 0) {
        		form.dataKriteriaKeuangan = {
        				daftarId: data[0].id,
        				faktorLikuiditas: data[0].faktorLikuiditas,
        				faktorModal: data[0].faktorPerputaranModal,
        				besarAmbang: data[0].besarAmbang
        		}
        	}
        });
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        
        $scope.checkSummary = function(value) {
        	if(value != undefined)
        		form.kriteriaKualifikasiTeknis = value;
        }
       
        $scope.cekNilaiAmbang = function(nilai) {
        	if(nilai < 0 || nilai == undefined) {
        		toaster.pop('error', 'Kesalahan', 'Jumlah Nilai Ambang Tidak Boleh Kosong Atau Minus');
        		document.getElementsByName("nilaiAmbang")[0].focus();
        	}
        }
        
        $scope.cekNilai = function(nilai) {
        	if(nilai == null || nilai < 0) {
        		toaster.pop('error', 'Kesalahan', 'Jumlah Nilai Tidak Boleh Kosong Atau Minus');
        		document.getElementsByName("item1")[0].focus();
        		$scope.jumlahNilai = null;
        	}
        }
        
        $scope.totalNilai = function() {
        	$scope.jumlahNilai = parseInt($scope.tambahanDataKriteriaTeknis.nilaiTertinggiBidangPekerjaan) + parseInt($scope.tambahanDataKriteriaTeknis.nilaiKontrak) 
        	+ parseInt($scope.tambahanDataKriteriaTeknis.nilaiStatusPenyedia) + parseInt($scope.tambahanDataKriteriaTeknis.nilaiPersonil) 
        	+ parseInt($scope.tambahanDataKriteriaTeknis.nilaiPeralatan) + parseInt($scope.tambahanDataKriteriaTeknis.nilaiMutu);
        	if($scope.jumlahNilai > 100) {
        		toaster.pop('error', 'Kesalahan', 'Jumlah Nilai Kriteria Tidak Boleh Lebih Dari 100!');
        		document.getElementsByName("item1")[0].focus();
        	}
        }
        
        $scope.cekNilaiFL = function(nilai) {
        	nilai = parseFloat(nilai);
        	if(nilai < 0.6 || nilai > 0.8) {
        		toaster.pop('error', 'Kesalahan', 'Nilai SALAH!');
        		document.getElementsByName("FL")[0].focus();
        	}
        }
        
        $scope.cekNilaiFM = function(nilai) {
        	nilai = parseInt(nilai);
        	if(nilai < 6 || nilai > 8) {
        		toaster.pop('error', 'Kesalahan', 'Nilai SALAH!');
        		document.getElementsByName("FM")[0].focus();
        	}
        }
        
        $scope.cekNilaiBA = function(nilai) {
        	nilai = parseFloat(nilai);
        	if(nilai < 0.2 || nilai > 0.8) {
        		toaster.pop('error', 'Kesalahan', 'Nilai SALAH!');
        		document.getElementsByName("BA")[0].focus();
        	}
        }
        
        $scope.addDataKriteria = function() {
        	var mkkamodalinstance = $modal.open({
            	templateUrl: '/masterKriteriaKualifikasiAdmin.html',
                controller: 'masterKriteriaKualifikasiAdminJasa',
                size: 'lg',
                resolve: {
                	dataKriteriaKualifikasi: function () { 
                		if(form.dataKriteriaKualifikasiAdminList.length > 0){
                			return form.dataKriteriaKualifikasiAdminList;
                		} else {
                			return {};
                		}
                	}
                }
            });
        	mkkamodalinstance.result.then(function(dataKriteriaKualifikasi){
            	if (dataKriteriaKualifikasi != undefined && dataKriteriaKualifikasi !== null) {
            		form.dataKriteriaKualifikasiAdminList = dataKriteriaKualifikasi;
                	$scope.kriteriaKualifikasiTable.reload();
            	}
            });        	
        }
        
        $scope.deleteDataKriteria = function(index, dataKriteria) {
        	var mkkamodalinstance = $modal.open({
        		templateUrl: '/alertModal.html',
                controller: 'DeleteDataModalJasaController',
                size: 'sm',
                resolve: {
                	dataKriteria: function () { 
                		return dataKriteria; 
                	}
                }
            });
        	mkkamodalinstance.result.then(function(dataKriteria){
        		form.dataKriteriaKualifikasiAdminList.splice(index, 1);

            	if(dataKriteria.daftarId != undefined) {
            		var dataKriteriaId = dataKriteria.daftarId;
            		
                	$http.get($rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiAdministrasiServices/delete/' + dataKriteriaId)
                	.success(function (data, status, headers, config) {});
            	}
        		
            	$scope.kriteriaKualifikasiTable.reload();
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
                    $location.path("/app/promise/procurement/prakualifikasi/kriteriakualifikasi");
                }
            } else {
                $location.path("/app/promise/procurement/prakualifikasi/kriteriakualifikasi");
            }
        }
        
        $scope.simpan = function () {
        	var teruskan = false;
        	angular.extend(form.kriteriaKualifikasiTeknis, $scope.tambahanDataKriteriaTeknis);
        	console.info(form.kriteriaKualifikasiTeknis);
        	if(form.dataKriteriaKualifikasiAdminList.length > 0) {
        		if(parseFloat(form.dataKriteriaKeuangan.faktorLikuiditas) >= 0.6 && parseFloat(form.dataKriteriaKeuangan.faktorLikuiditas) <= 0.8) {
        			if(parseInt(form.dataKriteriaKeuangan.faktorModal) >= 6 && parseInt(form.dataKriteriaKeuangan.faktorModal) <= 8) {
        				if(parseFloat(form.dataKriteriaKeuangan.besarAmbang) >= 0.2 && parseFloat(form.dataKriteriaKeuangan.besarAmbang) <= 0.8) {
        					if(form.kriteriaKualifikasiTeknis != undefined) {
        						if($scope.jenisPengadaan != 1 && $scope.jumlahNilai != null) {
        							if($scope.tambahanDataKriteriaTeknis.nilaiAmbang != undefined)
        								teruskan = true;
        						} else if($scope.jenisPengadaan == 1) {
        							teruskan = true;
        						}
        					}
        				}
        			}
        		}
        	}
    			
    		if(teruskan){
    			var konfirmasi = confirm("Yakin untuk menyimpan data dari Form ini?");
            	if(konfirmasi) {
            		saveData(form.dataKriteriaKualifikasiAdminList, form.kriteriaKualifikasiTeknis, form.dataKriteriaKeuangan);
            	}
    		} else {
    			toaster.pop('error', 'Kesalahan', 'Data belum terisi semua dengan benar!');
    		}
        }
        
        var saveData = function(dataAdmin, dataTeknis, dataKeuangan) {
        	form.panjangDataAdmin = dataAdmin.length;
        	angular.forEach(dataAdmin, function(dataKriteriaAdmin, index){
        		var dataSimpanKriteriaAdmin = {
        			id: dataKriteriaAdmin.daftarId,
        			pengadaanId: $scope.pengadaanId,
        			masterKriteriaKualifikasiAdministrasiId: dataKriteriaAdmin.id
        		}
                
                if (dataSimpanKriteriaAdmin.id == undefined){
                	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiAdministrasiServices/insert';
                } else {
                	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiAdministrasiServices/update';
                }
        		$scope.loading = true;
        		
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
                    data: dataSimpanKriteriaAdmin
                }).success(function (data, status, headers, config) {
                	$scope.loading = false;
                	if((form.panjangDataAdmin - 1) == index) {
                		$scope.simpanDataTeknis(dataTeknis, dataKeuangan);
                	}
                });
    		});
        }
        
        $scope.simpanDataTeknis = function(dataTeknis, dataKeuangan) {
        	console.info(dataTeknis);      	
        	var dataSimpanKriteriaTeknis = {
        		id: dataTeknis.daftarId,
            	pengadaanId: $scope.pengadaanId,
            	masterKriteriaKualifikasiTeknisId: dataTeknis.id,
            	nilaiTertinggiBidangPekerjaan: dataTeknis.nilaiTertinggiBidangPekerjaan,
            	nilaiKontrak: dataTeknis.nilaiKontrak,
            	nilaiStatusPenyedia: dataTeknis.nilaiStatusPenyedia,
            	nilaiPersonil: dataTeknis.nilaiPersonil,
            	nilaiPeralatan: dataTeknis.nilaiPeralatan,
            	nilaiMutu: dataTeknis.nilaiMutu,
            	nilaiAmbang: dataTeknis.nilaiAmbang
            }
        	
            if (dataSimpanKriteriaTeknis.id == undefined){
            	if($scope.deleteKriteriaKualifikasiTeknis != undefined) {
            		var id = $scope.deleteKriteriaKualifikasiTeknis.daftarId;
            		deleteKriteriaTeknis(id);
            	} 
            	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/insert';
            } else {
            	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/update';
            }
        	$scope.loading = true;
        	
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
                data: dataSimpanKriteriaTeknis
            }).success(function (data, status, headers, config) {
            	$scope.loading = false;
            	if($scope.jenisPengadaan == 1)
            		$scope.updatePengadaan();
            	else
            		$scope.simpanDataKeuangan(dataKeuangan);
             	$scope.back();
            });
        }
        
        $scope.simpanDataKeuangan = function(dataKeuangan) {
        	console.info(dataKeuangan);      	
        	var dataSimpanKriteriaKeuangan = {
        		id: dataKeuangan.daftarId,
            	pengadaanId: $scope.pengadaanId,
            	faktorLikuiditas: dataKeuangan.faktorLikuiditas,
            	faktorPerputaranModal: dataKeuangan.faktorModal,
            	besarAmbang: dataKeuangan.besarAmbang
            }
        	
        	if (dataSimpanKriteriaKeuangan.id == undefined){
            	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiKeuanganServices/insert';
            } else {
            	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiKeuanganServices/update';
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
                data: dataSimpanKriteriaKeuangan
            }).success(function (data, status, headers, config) {
            	$scope.loading = false;
                $scope.updatePengadaan();
             	$scope.back();
            });
        }
        
        var deleteKriteriaTeknis = function(id) {
        	$http.get($rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/delete/' + id)
        	.success(function (data, status, headers, config) {});
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

    KriteriaKualifikasiViewJasaController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();



/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */

angular.module('naut')
.controller('DeleteDataModalJasaController', function ($scope, $modalInstance, dataKriteria) {
    $scope.nameData = dataKriteria.nama;
    $scope.dataKriteria = dataKriteria;
    
    $scope.ok = function () {
    	$modalInstance.close($scope.dataKriteria);
    };
    
    $scope.cancel = function () {
    	$modalInstance.dismiss('cancel');
    };
});

angular.module('naut')
.controller('masterKriteriaKualifikasiAdminJasa', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, dataKriteriaKualifikasi, toaster) {
	$scope.dataKriteriaKualifikasi = [];
    //$scope.loading == true;
    
    $http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiAdministrasiServices/getList')
    .success(function (data, status, headers, config) {
    	$scope.masterKriteriaKualifikasiAdminList = data;
    	if(dataKriteriaKualifikasi.length > 0){
	         for(var i=0; i < dataKriteriaKualifikasi.length; i++){
		        for(var j=0; j < $scope.masterKriteriaKualifikasiAdminList.length; j++){
                   if(dataKriteriaKualifikasi[i].id == $scope.masterKriteriaKualifikasiAdminList[j].id){
                	   $scope.masterKriteriaKualifikasiAdminList.splice($scope.masterKriteriaKualifikasiAdminList.indexOf($scope.masterKriteriaKualifikasiAdminList[j]), 1 );
			       }
		        }
	         }
         }
    	//$scope.loading == false;
    });
    
    var adminCheck = [];
    
    // for save the data from checkbox list to sesions
    $scope.changeAdminCheck = function(list, active) {
        if(list.active) {
        	adminCheck.push(list);
        } else {
        	adminCheck.splice(adminCheck.indexOf(list), 1);
        }
    }
    
    $scope.btnSimpan = function() {
    	if(dataKriteriaKualifikasi.length > 0) {
        	$scope.dataKriteriaKualifikasi = dataKriteriaKualifikasi;

        	if(adminCheck.length > 0) {
        		angular.forEach(adminCheck,function(value,index){
            		$scope.dataKriteriaKualifikasi.push(value);
            	})
        	}
        } else {
        	$scope.dataKriteriaKualifikasi = adminCheck;
        }
    	
		$modalInstance.close($scope.dataKriteriaKualifikasi);
	}
    
    $scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
    }
});