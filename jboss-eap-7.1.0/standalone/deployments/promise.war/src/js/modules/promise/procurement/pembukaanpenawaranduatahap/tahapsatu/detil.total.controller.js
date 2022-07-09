/**=========================================================
 * Module: MetodePengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembukaanPenawaranTotalTahapSatuController', PembukaanPenawaranTotalTahapSatuController);

    function PembukaanPenawaranTotalTahapSatuController($http, $scope, $rootScope, $resource, $location, $modal, $q, $timeout, ngTableParams, toaster, $stateParams, $filter, editableOptions, editableThemes, $state) {
        $scope.dataPengadaan = $stateParams.dataPengadaan;
        $scope.pengadaanId = $scope.dataPengadaan.id;
                
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }
        
        var suratPenawaranByPengadaanList = [];
        $scope.getSuratPenawaranPengadaan = function () {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByPengadaan/' + $scope.pengadaanId, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				$scope.loading = false;
				suratPenawaranByPengadaanList = data;
				
				$scope.daftarHadirVendorList = new ngTableParams({
		            page: 1,
		            count: 10
		        }, {
		            total: suratPenawaranByPengadaanList.length,
		            getData: function ($defer, params) {
		                $defer.resolve(suratPenawaranByPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
		            }
		        });
			}).error(function (data, status, headers, config) {
				$scope.loading = false;
			});
		}
		$scope.getSuratPenawaranPengadaan();
		
		$http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
        .success(function (data, status, headers, config) {
            $scope.jabatanList = data;
            $scope.loading = false;
        })
        .error(function (data, status, headers, config) {
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
	      }
		
		$scope.removeDaftar = function(daftar) {
			var index = $scope.kehadiranPanitiaList.indexOf(daftar);
			$scope.kehadiranPanitiaList.splice(index, 1);
		}
	      
	      $scope.backToIndex = function() {
	    	  $state.go('app.promise.procurement-panitia-pembukaanPenawaranduatahap-tahapsatu');
	      }
	      
	      $scope.simpanData = function() {
    		  $scope.checkValid = false;
	    	  if (suratPenawaranByPengadaanList.length > 0) {
	    		  angular.forEach(suratPenawaranByPengadaanList, function(dataVendor, index){
	    			  if (dataVendor.vendor.check == undefined || dataVendor.vendor.check == false) {
	    				  $scope.checkValid = true;
	    				  $scope.messageError = "Masih ada vendor yang belum melakukan konfirmasi pembukaan penawaran";
	    			  }
	    		  });
	    	  } else {
	    		  $scope.checkValid = true;
	    		  $scope.messageError = "Masih ada vendor yang belum melakukan konfirmasi pembukaan penawaran";
	    	  }
    		  if ($scope.checkValid == false) {
    			  doSimpanVendorHadir();
    			  doSimpanPanitia();
    			  goToNextTahapan();
    		  }
	      }
	      
	      function doSimpanVendorHadir() {
	    	  if (suratPenawaranByPengadaanList.length > 0) {
	    		  angular.forEach(suratPenawaranByPengadaanList, function(dataVendor, index){
	    			  $http({
                          method: 'POST',
                          url: $rootScope.backendAddress + '/procurement/pembukaanpenawaranduatahap/pembukaanPenawaranTotalTahapSatuServices/create',
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
                        	  vendor: dataVendor.vendor.id
                          }
                      });
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

    }

    PembukaanPenawaranTotalTahapSatuController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', '$q', '$timeout', 'ngTableParams', 'toaster', '$stateParams', '$filter', 'editableOptions', 'editableThemes', '$state'];

})();
