
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('SanggahanSatuanListitemController', SanggahanSatuanListitemController);
    function SanggahanSatuanListitemController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams, $state) {
    	
    	$scope.pengadaanId = $stateParams.pengadaanId
    	
    	var negosiasiDetail = this;
        negosiasiDetail.negosiasiTahapId = 15;
        negosiasiDetail.nextTahapId = 16;

        // set mode
        $scope.isEditable = $rootScope.isEditable;
        $scope.loading = true;
        $scope.loadingSaving = false;

        /*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/
         
        function loadPengadaanItemList() {
        	
        	/*Material*/
 			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId , {
 				ignoreLoadingBar: true
 			}).success(function(data, status, headers, config) {
 				negosiasiDetail.sanggahanDetailMaterialList = data;
 				/*Jasa*/
 	 			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId , {
 	 				ignoreLoadingBar: true
 	 			}).success(function(data, status, headers, config) {
 	 				negosiasiDetail.sanggahanDetailJasaList = data;
 	 				 $scope.loading = false;
 	 			})
 			})
        }
        
        $scope.viewSanggahan = function(itemPengadaan) {
        
        	$state.go('app.promise.procurement-sanggahan-satuan-detail', {itemPengadaan : itemPengadaan} );
        }
        
        $scope.back = function()
        {	
        	$state.go('app.promise.procurement-panitia-sanggahan' );
         }
        
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        
       
       
        loadPengadaanItemList();
    }

    SanggahanSatuanListitemController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter','$stateParams','$state'];

})();