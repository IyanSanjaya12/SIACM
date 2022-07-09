/**=========================================================
 * Module: negosiasi.satuan.detail.penawaran.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiSatuanDetailPenawaranController', NegosiasiSatuanDetailPenawaranController);
    function NegosiasiSatuanDetailPenawaranController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams) {
    	
    	$scope.pengadaanId = $stateParams.pengadaanId;
    	$scope.itemPengadaanId = $stateParams.itemPengadaanId;
    	
    	var negosiasiDetail = this;
      
        // set mode
        $scope.loading = true;
        $scope.loadingSaving = false;

        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/
         
        function loadPenawaranList() {
        	
        	/*List penawaran by item*/
 			$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId+'/'+ $scope.itemPengadaanId, {
 				ignoreLoadingBar: true
 			}).success(function(data, status, headers, config) {
 					negosiasiDetail.vendorNegosiasiList = data;
 				 	
 					angular.forEach(negosiasiDetail.vendorNegosiasiList, function(vendorNegosiasi) {
 						
 						/*Kurs Pengadaan*/
 						$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanByPengadaanAndMataUang/' + $scope.pengadaanId+'/'+vendorNegosiasi.evaluasiHargaVendor.suratPenawaran.mataUang.id, {
	            			ignoreLoadingBar: true
	            		}).success(function(data, status, headers, config) {
	            			vendorNegosiasi.kursPengadaan = data;
	 						
	            			/*Penawaran Awal dari vendor*/
	            			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getFirstFromVendorByItemPengadaanAndVendor/' + 	$scope.itemPengadaanId+'/'+	vendorNegosiasi.evaluasiHargaVendor.vendor.id, {
		            			ignoreLoadingBar: true
		            		}).success(function(data, status, headers, config) {
		            			vendorNegosiasi.negosiasiFirstFromVendor =  data;
		             				
		             			/*Penawaran Akhir dari vendor*/
		            			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByVendorAndItemPengadaan/' +  vendorNegosiasi.evaluasiHargaVendor.vendor.id+'/'+$scope.itemPengadaanId, {
			            			ignoreLoadingBar: true
			            		}).success(function(data, status, headers, config) {
			            			vendorNegosiasi.negosiasiLastFromVendor =  data;
			            			
			            			/*Penawaran Detil Akhir dari vendor*/
			            			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/getNegosiasiDetailListByNegosiasi/' +  vendorNegosiasi.negosiasiLastFromVendor.id, {
				            			ignoreLoadingBar: true
				            		}).success(function(data, status, headers, config) {
				            			vendorNegosiasi.negosiasiLastFromVendor.negosiasiDetails =  data;
			            			
			            			/*Penawaran Akhir dari panitia*/
			            			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromPanitiaByVendorAndItemPengadaan/' +  vendorNegosiasi.evaluasiHargaVendor.vendor.id+'/'+$scope.itemPengadaanId, {
				            			ignoreLoadingBar: true
				            		}).success(function(data, status, headers, config) {
				            			
				            			/*default value*/
				                    	vendorNegosiasi.negosiasiLastFromPanitia = {
				                    			"nilaiPenawaranAfterCondition":0, 
				                    			"isDeal":0
				                    		};
				                    	
				            			/*Untuk flag apakah panitia sudah melakukan penawaran*/
				                    	negosiasiDetail.isPanitiaOffered = false;
				                    	
				                    	if (typeof data.nilaiPenawaranAfterCondition != 'undefined')
				                    		{	
				                    			negosiasiDetail.isPanitiaOffered = true;
				                    			vendorNegosiasi.negosiasiLastFromPanitia = data;
				                    		}
				                    	
				                    	$scope.updateLastOfferStatus(vendorNegosiasi );
				             			$scope.loading = false;
				            		})
			             		})
			         		})
	            		})
 					})
 		 		})
 			})
 	   }
       
         	
        $scope.updateLastOfferStatus = function (vendorNegosiasi) {
    	   vendorNegosiasi.isDeal =  (vendorNegosiasi.negosiasiLastFromPanitia.isDeal || vendorNegosiasi.negosiasiLastFromVendor.isDeal);
       }
        
        $scope.sendPriceToVendor = function()
        {
        	$scope.recordToSave = negosiasiDetail.vendorNegosiasiList.length;
        	$scope.recordSaveCtr = 0;
        	$scope.loadingSaving = true;
        	
        	angular.forEach(negosiasiDetail.vendorNegosiasiList, function(vendorNegosiasi) {
        		
        		if (vendorNegosiasi.isDeal != 1) {
        			var postData ={
	        				"suratPenawaran":vendorNegosiasi.negosiasiLastFromVendor.suratPenawaran.id,
	        				"vendor":vendorNegosiasi.negosiasiLastFromVendor.vendor.id,
	        				"pengadaan":vendorNegosiasi.negosiasiLastFromVendor.suratPenawaran.pengadaan.id,
	        				"nilaiPenawaranAfterCondition":vendorNegosiasi.negosiasiLastFromPanitia.nilaiPenawaranAfterCondition,
	        				"isVendor" 	 :0,
	        				"isPanitia"	 :1,
	        				"isDeal"	 :0,
	        				"isDelete"	 :0
	        		}
        		
        	 		$http({
	                    method: 'POST',
	                    url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/create',
	                    headers: {
	                        'Content-Type': 'application/x-www-form-urlencoded'
	                    },
	                    transformRequest: function(obj) {
	                        var str = [];
	                        for (var p in obj)
	                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	                        return str.join("&");
	                    },
	                    data: postData
	                }).success(function(data, status, headers, config) {
	                	
	                	saveDetail(data, vendorNegosiasi);
	                	
	                })
        		}
        		
        		else
    			{
    				console.log("price not send because it's already deal!")
    				$scope.recordToSave-=1;
    			}
        	})
        }
        
        
       
        var saveDetail = function(negosiasi, vendorNegosiasi)
        {
        	
        	
        	var postData ={
        			"negosiasi": negosiasi.id,
        			"itemPengadaan":vendorNegosiasi.itemPengadaan.id,
    				"deleted":0,
    				"isDelete"	 :0
    		}
    		
    		$http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                	
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postData
            }).success(function(data, status, headers, config) {
            	$scope.saveDetailSuccessHandler();
            })
        }
        
        
        $scope.saveDetailSuccessHandler = function() {
        	$scope.recordSaveCtr++;
        	if ($scope.recordSaveCtr == $scope.recordToSave )
        		{	
	        		$scope.loadingSaving = false;
	    			toaster.pop('success', 'Sukses', 'Data penawaran negosiasi berhasil dikirim.');
        		}
        }
        
        $scope.back = function()
        {	
         	 $location.path('/app/promise/procurement/negosiasi/satuan/detail/'+$scope.pengadaanId );
        }
        
        $scope.showDetail = function(negosiasi, itemPengadaanId) {
           $location.path('/app/promise/procurement/negosiasi/satuan/detail/penawaran/more/'+$scope.pengadaanId+'/'+itemPengadaanId+'/'+negosiasi.id);
        };
        
        
        $scope.dealPrice = function(vendorNegosiasi, itemPengadaanId) {
        	
        	var nilaiPenawaranTerakhirVendorDalamIDR = (vendorNegosiasi.negosiasiLastFromVendor.nilaiPenawaranAfterCondition * vendorNegosiasi.kursPengadaan.nilai);
        	var nilaiPenawaranPanitia = vendorNegosiasi.negosiasiLastFromPanitia.nilaiPenawaranAfterCondition;
        	
        	if (nilaiPenawaranTerakhirVendorDalamIDR != nilaiPenawaranPanitia)
        		{
        			alert('Nilai penawaran yang dimasukan harus sama dengan nilai penawaran terakhir vendor!');
        			return;
        		}
        	
        	if (!confirm('Apakah anda yakin ingin melakukan deal price?'))
        	{
        		return;
        	}
        	
        	
        	var postData ={
    				"itemPengadaanId": itemPengadaanId,
    				"vendorId"       : vendorNegosiasi.negosiasiLastFromVendor.vendor.id 
    		}
    		
    		$http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/negosiasi/NegosiasiPengadaanServices/panitiaDealPriceForSatuanNego',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postData
            }).success(function(data, status, headers, config) {
            	if (data)
            		{
            			/*refresh*/
            			loadPenawaranList();
            		}
            })
        };
        
         
         $scope.showHistory = function(negosiasi, itemPengadaanId) {
        	 $rootScope.myBackurl = '/app/promise/procurement/negosiasi/satuan/detail/penawaran/' + negosiasi.pengadaan.id+'/'+itemPengadaanId;
             $location.path('/app/promise/procurement/negosiasi/satuan/detail/penawaran/history/'+negosiasi.pengadaan.id+'/'+itemPengadaanId+'/'+negosiasi.vendor.id);
             
        };
         
        loadPenawaranList();
    }

    NegosiasiSatuanDetailPenawaranController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter','$stateParams'];

})();