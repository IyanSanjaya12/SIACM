/**=========================================================
 * Module: RekapPengadaanController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RekapPengadaanDetailController', RekapPengadaanDetailController);

	function RekapPengadaanDetailController($scope, $http, $rootScope, $resource, $location, $state, $stateParams, ngTableParams) {
		var form = this;
		
		form.divisi = $stateParams.dataDivisi;
		
		form.totalNilaiHPSPengadaan = 0;
		
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanByOrganisasiId/' + form.divisi.id)
	    .success(function (data, status, headers, config) {
	    	form.dataPengadaanList = data;
	    	
	    	if (form.dataPengadaanList !== undefined && form.dataPengadaanList.length > 0) {
	    		angular.forEach(form.dataPengadaanList, function(pengadaan, index) {
	    			// cari no SPK
	    			$http.get($rootScope.backendAddress + '/procurement/spk/SPKServices/getSPKByPengadaan/'+pengadaan.id)
	    			.success(function (data, status, headers, config) {
	    				pengadaan.nospk = data[0].nomor;
	    			});
	    			// find nilai HPS
	    			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/'+pengadaan.id)
	    			.success(function (data, status, headers, config) {
	    				pengadaan.nilaiHPS = 0;
	    				if (data !== undefined && data.length > 0) {
	    					angular.forEach(data, function(itemPengadaan, index){
	    						pengadaan.nilaiHPS = pengadaan.nilaiHPS + itemPengadaan.totalHPS;
	    						form.totalNilaiHPSPengadaan = form.totalNilaiHPSPengadaan + itemPengadaan.totalHPS;
	    					});
	    				}
	    			});
	    		});
	    	}
	    	
    		$scope.tableDataPengadaan = new ngTableParams({
	            page: 1, // show first page
	            count: 5 // count per page
	        }, {
	            total: form.dataPengadaanList.length, // length of data4
	            getData: function ($defer, params) {
	                $defer.resolve(form.dataPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	            }
	        });
	    });
		
		form.buttonView = function(dataPengadaan) {
			$state.go('app.promise.procurement-panitia-rekappengadaan-view', {dataPengadaan: dataPengadaan});
		}
	}

	RekapPengadaanDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource','$location', '$state', '$stateParams', 'ngTableParams'];

})();