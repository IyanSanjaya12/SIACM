/**=========================================================
 * Module: negosiasi.satuan.detail.penawaran.history.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiTotalDetailHistoryController', NegosiasiTotalDetailController);

    function NegosiasiTotalDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams) {
  
    	var negosiasiDetail = this;
    	
    	$scope.pengadaanId     = $stateParams.pengadaanId;
    	$scope.itemPengadaanId = $stateParams.itemPengadaanId;
    	$scope.vendorId        = $stateParams.vendorId;

        // set mode
        $scope.loading = true;
        $scope.loadingSaving = false;
        
        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/
        
        // Load history harga by vendor
       
        function loadHistoryPriceByVendor() { 
        	$scope.loading = true;
            
        	/*Ambil informasi surat Penawaran, untuk mengambil mata uang vendor*/
        	$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendorId+'/'+$scope.pengadaanId , {
                  ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	negosiasiDetail.suratPenawaran = data[0];
            	
            	/*Ambil informasi kurs mata uang vendor*/
            	$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanByPengadaanAndMataUang/' + $scope.pengadaanId+'/'+negosiasiDetail.suratPenawaran.mataUang.id, {
         			ignoreLoadingBar: true
         		}).success(function(data, status, headers, config) {
         			negosiasiDetail.kursPengadaan = data;
         			
         			/*Ambil history nego*/
         			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getNegosiasiListByPengadaanAndVendor/' + $scope.pengadaanId+'/'+$scope.vendorId, {
         				ignoreLoadingBar: true
         			}).success(function(data, status, headers, config) {
         				negosiasiDetail.vendorPriceHistoryList = data;
         				$scope.loading = false;
         			}).error(function(data, status, headers, config) {
         				$scope.loading = false;
         			});
         			
         			
         		}).error(function(data, status, headers, config) {
	                    $scope.loading = false;
	            });
              	
            }).error(function(data, status, headers, config) {
            	$scope.loading = false;
            });
            
        }
        
        loadHistoryPriceByVendor();
        
        
        /*tombol aksi back to index*/
        $scope.back = function() {
        	$location.path($rootScope.myBackurl);
        };
        
        
        
    }

    NegosiasiTotalDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams'];

})();