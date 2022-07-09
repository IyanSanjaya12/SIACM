/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PembukaanPenawaranSatuanTahapSatuController', PembukaanPenawaranSatuanTahapSatuController);

    function PembukaanPenawaranSatuanTahapSatuController($http, $scope, $rootScope, $stateParams, $modal, ngTableParams, $filter, editableOptions, editableThemes, $state) {
    	$scope.dataPengadaan = $stateParams.dataPengadaan;
        $scope.pengadaanId = $scope.dataPengadaan.id;
        
     // Material pengadaan
        var dataItemPengadaan = [];
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId)
        .success(function(data, status, headers, config) {
        	dataItemPengadaan = data;
            var materialPengadaanList = [];
            var jasaPengadaanList = [];
            
            if (dataItemPengadaan !== undefined && dataItemPengadaan.length > 0) {
                angular.forEach(dataItemPengadaan, function(itemPengadaan, index){
            		if (itemPengadaan.item.itemType.id === 1) {
            			materialPengadaanList.push(itemPengadaan);
            		} else {
            			jasaPengadaanList.push(itemPengadaan);
            		}
            	});
            }
            
            $scope.tableMaterialPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: materialPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(materialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            
            $scope.tableJasaPengadaan = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: jasaPengadaanList.length,
                getData: function($defer, params) {
                    $defer.resolve(jasaPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
        });

		
		
		$http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
        .success(function (data, status, headers, config) {
            $scope.jabatanList = data;
            $scope.loading = false;
        });
		
		$scope.kehadiranPanitiaList = [];
		$scope.buttonTambahPanitia = function() {
	        $scope.inserted = {
	                id: $scope.kehadiranPanitiaList.length+1,
	                nama: '',
	                jabatan: null,
	                isNew: true
	        };
	        $scope.kehadiranPanitiaList.push($scope.inserted);			
		}
		
		$scope.showJabatan = function(daftarPanitia) {
	        var selected = [];
	        if(daftarPanitia.jabatan) {
	          selected = $filter('filter')($scope.jabatanList, {id: daftarPanitia.jabatan});
	        }
	        return selected.length ? selected[0].nama : '';
	      };
	      
	      $scope.removeDaftar = function(daftar) {
				var index = $scope.kehadiranPanitiaList.indexOf(daftar);
				$scope.kehadiranPanitiaList.splice(index, 1);
			}
	      
	      $scope.backToIndex = function() {
	    	  $state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-tahapsatu');
	      }
	      
	      $scope.goPembukaanPenawaranDetail = function(item) {
          		var index = dataItemPengadaan.indexOf(item);
          		var dataItemTosend = dataItemPengadaan[index]; 
				var viewModalDataVendor = $modal.open({
	                templateUrl: '/pembukaanPenawaranDetail.html',
	                controller: 'ModalDaftarHadirVendorTahapSatuController',
	                size: 'lg',
	                resolve: {
	                	dataItem: function () {
	                        return dataItemTosend;
	                    }
	                }
	            });
				viewModalDataVendor.result.then(function (daftarHadirVendorList) {
					dataItemPengadaan[index].daftarHadirVendorList = daftarHadirVendorList;
	            });	    	  
	      }
        
         $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
         
         $scope.simpanData = function() {
        	 $scope.checkValid = false;
	    	 if (dataItemPengadaan.length > 0) {
	    		 angular.forEach(dataItemPengadaan, function(dataItem, index){
	    			 if (dataItem.daftarHadirVendorList != undefined && dataItem.daftarHadirVendorList.length > 0) {
	    				 angular.forEach(dataItem.daftarHadirVendorList, function(dataVendor, indexVendor){
	    					 if (dataItem.vendor.check == undefined || dataItem.vendor.check == false) {
	    						 $scope.checkValid = true;
	    						 $scope.messageError = "Masih ada vendor yang belum melakukan konfirmasi pembukaan penawaran";
	    					 }
	    				  });
	    			  } else {
	    	    		  $scope.checkValid = true;
	    	    		  $scope.messageError = "Masih ada vendor yang belum melakukan konfirmasi pembukaan penawaran";
	    			  }
	    		 });
	    	 }
	    	 if ($scope.checkValid == false) {
	    		 	doSimpanVendorHadir();
	    		 	doSimpanPanitia();
   			  		doSimpanKurs();
   			  		goToNextTahapan();
   		  		}
         }
	      
         function doSimpanVendorHadir() {
        	 if (dataItemPengadaan.length > 0) {
	    		 angular.forEach(dataItemPengadaan, function(dataItem, index){
	    			 if (dataItem.daftarHadirVendorList != undefined && dataItem.daftarHadirVendorList.length > 0) {
	    				 angular.forEach(dataItem.daftarHadirVendorList, function(dataVendor, indexVendor){
	    					 $http({
	                             method: 'POST',
	                             url: $rootScope.backendAddress + '/procurement/pembukaanpenawaranduatahap/pembukaanPenawaranSatuanTahapSatuServices/create',
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
	                           	  pengadaan: $scope.pengadaanId,
	                           	  item:dataItem.item.id,
	                           	  vendor: dataVendor.vendor.id
	                             }
	                         });
	    				 });
	    			 }
	    		 });
        	 }
        	 if (suratPenawaranByPengadaanList.length > 0) {
        		 angular.forEach(suratPenawaranByPengadaanList, function(dataVendor, index){
	    			  
	    		  });
	    	  }
	      }
	      
	      function doSimpanPanitia() {
	    	  if ($scope.kehadiranPanitiaList.length > 0) {
	    		  angular.forEach($scope.kehadiranPanitiaList, function(dataPanitia, index){
	    			  $http({
                         method: 'POST',
                         url: $rootScope.backendAddress + '/procurement/pembukaanpenawaranduatahap/daftarHadirPanitiaTahapSatuServices/create',
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
                       	  nama:dataPanitia.nama,
                       	  pengadaan: $scope.pengadaanId,
                       	  jabatan: dataPanitia.jabatan
                         }
                     });
	    		  });
	    	  }
	      }
	      
	      function goToNextTahapan() {
	    	  $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
             .success(function (data, status, headers, config) {
             	var nextTahapan = data;
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
                         tahapanPengadaanId: nextTahapan
                     }
                 })
                 .success(function (data, status, headers, config) {
    			  		$state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-tahapsatu');
		        });
             });
	      }

	      function doSimpanKurs() {
	    	  
	      }
        
    }

    PembukaanPenawaranSatuanTahapSatuController.$inject = ['$http', '$scope', '$rootScope', '$stateParams', '$modal', 'ngTableParams', '$filter', 'editableOptions', 'editableThemes', '$state'];

})();

angular.module('naut')
.controller('ModalDaftarHadirVendorTahapSatuController', function ($rootScope, $scope, $http, $modalInstance, ngTableParams, dataItem) {
	$scope.dataItem = dataItem;
	
	$scope.suratPenawaranByPengadaanList = [];	
	$scope.daftarHadirVendorList = new ngTableParams({
        page: 1,
        count: 10
    }, {
        total: $scope.suratPenawaranByPengadaanList.length,
        getData: function ($defer, params) {
            $defer.resolve($scope.suratPenawaranByPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
        }
    });
	
	if (dataItem.daftarHadirVendorList !== undefined && dataItem.daftarHadirVendorList.length > 0) {
		$scope.suratPenawaranByPengadaanList = dataItem.daftarHadirVendorList;
		$scope.daftarHadirVendorList.reload();
	} else {
		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + dataItem.pengadaan.id, {
			ignoreLoadingBar: true
		}).success(function (data, status, headers, config) {
			$scope.suratPenawaranByPengadaanList = data;
			$scope.daftarHadirVendorList.reload();
		});
	}

    $scope.ok = function () {
    	$modalInstance.close($scope.suratPenawaranByPengadaanList);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});