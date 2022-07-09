/**=========================================================
 * Module: KriteriaKualifikasiViewController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaKualifikasiViewController', KriteriaKualifikasiViewController);

    function KriteriaKualifikasiViewController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        form.dataKriteriaKualifikasiAdminList = [];
        
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Master ============================================ */
        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaKualifikasiTeknisServices/getList')
        .success(function (data, status, headers, config) {
        	if(data != undefined) {
                console.log(">>cek data : "+JSON.stringify(data));
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
            console.log("Cek data existing : "+JSON.stringify(data));
        	if(data.length > 0) {
        		angular.forEach($scope.kriteriaKualifikasiTeknisList,function(value,index){
        			if(value.id == data[0].masterKriteriaKualifikasiTeknis.id) {
        				value.pilih = value;
        				var tambahanData = {
            				daftarId: data[0].id,
            				pengadaanId: data[0].pengadaan.id
            			}
        				angular.extend(value, tambahanData);
        				form.kriteriaKualifikasiTeknis = value;
        				$scope.deleteKriteriaKualifikasiTeknis = value;
        				console.info(form.kriteriaKualifikasiTeknis)
        			}
        		});
        	}
        });
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        
        $scope.checkSummary = function(value) {
        	if(value != undefined)
        		form.kriteriaKualifikasiTeknis = value;
        }
        
        $scope.addDataKriteria = function() {
        	var mkkamodalinstance = $modal.open({
            	templateUrl: '/masterKriteriaKualifikasiAdmin.html',
                controller: 'masterKriteriaKualifikasiAdmin',
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
                controller: 'DeleteDataModalController',
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
        	
        	if(form.dataKriteriaKualifikasiAdminList.length > 0) {
        		if(form.kriteriaKualifikasiTeknis != undefined)
        			teruskan = true;
        	}
    			
    		if(teruskan){
    			var konfirmasi = confirm("Yakin untuk menyimpan data dari Form ini?");
            	if(konfirmasi) {
            		saveData(form.dataKriteriaKualifikasiAdminList, form.kriteriaKualifikasiTeknis);
            	}
    		} else {
    			toaster.pop('error', 'Kesalahan', 'Data belum terisi semua!');
    		}
        }
        
        var saveData = function(dataAdmin, dataTeknis) {
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
                		$scope.simpanDataTeknis(dataTeknis);
                	}
                });
    		});
        }
        
        $scope.simpanDataTeknis = function(dataTeknis) {
        	console.info(dataTeknis);      	
        	var dataSimpanKriteriaTeknis = {
        		id: dataTeknis.daftarId,
            	pengadaanId: $scope.pengadaanId,
            	masterKriteriaKualifikasiTeknisId: dataTeknis.id
            }
        	
            if (dataSimpanKriteriaTeknis.id == undefined){
            	if($scope.deleteKriteriaKualifikasiTeknis != undefined) {
            		var id = $scope.deleteKriteriaKualifikasiTeknis.daftarId;
            		deleteKriteriaTeknis(id);
            	} 
            	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/daftarKriteriaKualifikasiTeknisServices/insert';
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

    KriteriaKualifikasiViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();

/* ---------------------------------------------------- KUMPULAN MODAL KUALIFIKASI -------------------------------------- */

angular.module('naut')
.controller('DeleteDataModalController', function ($scope, $modalInstance, dataKriteria) {
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
.controller('masterKriteriaKualifikasiAdmin', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, dataKriteriaKualifikasi, toaster) {
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