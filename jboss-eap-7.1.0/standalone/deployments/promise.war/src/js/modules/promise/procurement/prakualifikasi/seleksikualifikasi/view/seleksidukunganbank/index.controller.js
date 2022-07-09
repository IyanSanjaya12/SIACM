/**=========================================================
 * Module: SeleksiDukunganBankController.js
 * Author: F.H.K
 =========================================================*/



(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SeleksiDukunganBankController', SeleksiDukunganBankController);

    function SeleksiDukunganBankController($scope, $http, $rootScope, $resource, $location, $filter, ngTableParams, $modal, toaster) {
    	
    	/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
    	var form = this;
    	var bankCheck = [];
    	
    	$scope.detilPengadaan = $rootScope.pengadaan;
        $scope.pengadaanId = $scope.detilPengadaan.id;
        form.listVendorDataBank = $rootScope.vendorLulusListDariKriteriaAdmin;
    	/* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Seleksi Dukungan Bank ================================== */
        $scope.getListSeleksiDukunganBank = function () {
            $scope.loading = true;
            var uri = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiDukunganBankServices/getByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                var dataVendor = form.listVendorDataBank;
                if(data.length > 0) {
                	angular.forEach(data,function(value,index){
                		angular.forEach(dataVendor,function(value,indexVendor){
                			if(dataVendor[indexVendor].vendor.id == data[index].vendor.id){
                				if(data[index].statusKelulusan == 1) {
                					dataVendor[indexVendor].vendor.statusKelulusan = "Lulus";
                					dataVendor[indexVendor].active = true;
                				} else {
                					dataVendor[indexVendor].vendor.statusKelulusan = "Tidak Lulus";
                					dataVendor[indexVendor].active = false;
                				}
                				dataVendor[indexVendor].tableId = data[index].id;
                			} 
                		})
                	})
                	form.listVendorDataBank = dataVendor;
                }
                
                $scope.seleksiDukunganBankList = data;
                
                if($scope.seleksiDukunganBankList.length == 0) {
                	angular.forEach(form.listVendorDataBank,function(value,index){
                    	value.vendor.statusKelulusan = "";
                    })
                }
                
                $scope.daftarVendorTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.listVendorDataBank.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.listVendorDataBank.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListSeleksiDukunganBank();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        $scope.changeBankCheck = function(list, active) {
        	if(list.active) {
        		list.vendor.statusKelulusan = "Lulus";
    			bankCheck.push(list);
            } else {
            	list.vendor.statusKelulusan = "Tidak Lulus";
            	bankCheck.splice(bankCheck.indexOf(list), 1);
            }
        	//console.log("ISI VENDOR SIMPAN = "+JSON.stringify(bankCheck)+" -> Status Kelulusan = "+$scope.statusKelulusan);
        	$scope.daftarVendorTable.reload();
        }
        
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        
        $scope.back = function () {
        	$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiadministrasi");
        }
        
        $scope.lanjut = function () {
        	var kesalahan = false;
        	$rootScope.vendorLulusListDariKriteriaDB = [];
        	angular.forEach(form.listVendorDataBank,function(value,index){
        		if(value.vendor.statusKelulusan == "") {
        			kesalahan = true;
        		} else {
        			if(value.vendor.statusKelulusan == 'Lulus') {
            			$scope.statusKelulusan = 1;
            			$rootScope.vendorLulusListDariKriteriaDB.push(value);
            		} else {
            			$scope.statusKelulusan = 0;
            		}
        			
        			var paramData = {
        					id: value.tableId,
                			pengadaanId: $scope.pengadaanId,
                			vendorId: value.vendor.id,
                			statusKelulusan: $scope.statusKelulusan
                	}
                	
        			if (paramData.id == undefined){
                    	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiDukunganBankServices/insert';
                    } else {
                    	var targetURI = $rootScope.backendAddress + '/procurement/prakualifikasi/seleksiKualifikasiDukunganBankServices/update';
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
        		
        	})
        	
        	if(kesalahan)
        		toaster.pop('error', 'Kesalahan', 'Masih ada status kelulusan yang kosong!');
    		else
    			$location.path("/app/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikemampuandasar");
        }
        /* ------------------------------------------------------------------------------------------------ */
        
        }
    
    SeleksiDukunganBankController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'ngTableParams', '$modal', 'toaster'];

})();