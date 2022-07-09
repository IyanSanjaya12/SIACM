/**=========================================================
 * Module: negosiasi.total.detail.more.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/



(function() {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiTotalDetailMoreController', NegosiasiTotalDetailMoreController);

    function NegosiasiTotalDetailMoreController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams) {
  
    	var negosiasiDetail = this;
    	
    	$scope.pengadaanId = $stateParams.pengadaanId;
    	$scope.vendorId    = $stateParams.vendorId;
    	$scope.negosiasiId = $stateParams.negosiasiId;
 
        // set mode
        $scope.loading = true;
        $scope.loadingSaving = false;
        
        
        
        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/
        
        // Load history harga by vendor
       
        function loadDetilPenawaran() { 
        	$scope.loading = true;
           
        	/*Ambil informasi negosiasi, untuk mengambil surat penawaran dan mata uang vendor*/
        	$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getId/' + $scope.negosiasiId , {
                  ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            	
            	negosiasiDetail.negosiasi = data;
            	negosiasiDetail.suratPenawaran = data.suratPenawaran;
            	
            	/*Ambil informasi kurs mata uang vendor*/
            	$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaanByPengadaanAndMataUang/' + $scope.pengadaanId+'/'+negosiasiDetail.suratPenawaran.mataUang.id, {
         			ignoreLoadingBar: true
         		}).success(function(data, status, headers, config) {
         			negosiasiDetail.kursPengadaan = data;
         			
         			/*Ambil detil penawaran material dari negoasiasi_detil*/
         			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/getNegosiasiDetailListByNegosiasiAndItemType/' + $scope.negosiasiId+'/' + 1 , {
         				ignoreLoadingBar: true
         			}).success(function(data, status, headers, config) {
         				negosiasiDetail.negosiasiDetailMaterialList = data;
         				
         				/*Ambil detil penawaran jasa dari negoasiasi_detil*/
             			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetailServices/getNegosiasiDetailListByNegosiasiAndItemType/' + $scope.negosiasiId+'/'+ 2 , {
             				ignoreLoadingBar: true
             			}).success(function(data, status, headers, config) {
             				negosiasiDetail.negosiasiDetailJasaList = data;
             				
             				/*Ambil nego header condition*/
                 			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiConditionalPriceServices/getListByNegosiasiAndTipe/' + $scope.negosiasiId+'/'+ 1 , {
                 				ignoreLoadingBar: true
                 			}).success(function(data, status, headers, config) {
                 				negosiasiDetail.headerConditionList = data;
                 				
                 				/*Ambil nego item condition*/
                     			$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiConditionalPriceServices/getListByNegosiasiAndTipe/' + $scope.negosiasiId+'/'+ 2 , {
                     				ignoreLoadingBar: true
                     			}).success(function(data, status, headers, config) {
                     				/*------------- main code ------------------*/
                     				
                     				negosiasiDetail.itemConditionList = data;
                     				
                     				$scope.loading = false;
                     				
                     				/*------------- end main code ------------------*/
                     				
                     			}).error(function(data, status, headers, config) {
                     				$scope.loading = false;
                     			});
                     			
                 				
                 				
                 			}).error(function(data, status, headers, config) {
                 				$scope.loading = false;
                 			});
             				
             				
             			}).error(function(data, status, headers, config) {
             				$scope.loading = false;
             			});
         				
         				
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
        
       loadDetilPenawaran();
        
        
        /*tombol aksi back to index*/
        $scope.gotoIndex = function() {
            $location.path('/app/promise/procurement/negosiasi/total/detail/' + $stateParams.pengadaanId);
        };
        
        
        
    }

    NegosiasiTotalDetailMoreController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams'];

})();