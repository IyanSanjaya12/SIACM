/**=========================================================
 * Module: negosiasi.satuan.detail.controller.js
 * Modul/Tahapan ID: 15
 * Author: Reinhard
 =========================================================*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('NegosiasiSatuanDetailController', NegosiasiSatuanDetailController);
    function NegosiasiSatuanDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams) {
    	
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
 				negosiasiDetail.negosiasiDetailMaterialList = data;
 				/*Jasa*/
 	 			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId , {
 	 				ignoreLoadingBar: true
 	 			}).success(function(data, status, headers, config) {
 	 				negosiasiDetail.negosiasiDetailJasaList = data;
 	 				 $scope.loading = false;
 	 			})
 			})
        }
        
        $scope.viewPenawaran = function(vendorId, itemPengadanId) {
        	$location.path('/app/promise/procurement/negosiasi/satuan/detail/penawaran/'+vendorId+'/'+itemPengadanId);
        }
        
        $scope.back = function()
        {	
        	if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/negosiasi');
                }
            } else {
                $location.path('/app/promise/procurement/negosiasi');
            }
        }
        
        $scope.printDiv = function (divName) {
            $(".loading").remove();
            $(".ng-table-pager").remove();
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty) {
            	$(".barang-panel").remove();
            }
            if (!$rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty) {
            	$(".jasa-panel").remove();
            }
            
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        
        $scope.next = function()
        {	
       	 	$location.path('/app/promise/procurement/negosiasi');
        }
       
        loadPengadaanItemList();
    }

    NegosiasiSatuanDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter','$stateParams'];

})();