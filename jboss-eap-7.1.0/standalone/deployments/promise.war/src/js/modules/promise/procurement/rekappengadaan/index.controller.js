/**=========================================================
 * Module: RekapPengadaanController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RekapPengadaanIndexController', RekapPengadaanIndexController);

	function RekapPengadaanIndexController($scope, $http, $rootScope, $resource, $location, $state, $stateParams, ngTableParams, $modal, $filter) {
		var form = this;
		
		$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list')
	    .success(function (data, status, headers, config) {
	    	form.OrganisasiList = data;
	    	form.dataOrganisasi = data[0];
	    });
		
		form.tglAwalOpened = false;
		form.tglAwalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAwalOpened = true;
        };
        
        form.tglAkhirOpened = false;
		form.tglAkhirOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tglAkhirOpened = true;
        };
        
        form.downloadToExcell = function() {
        	
        }
        
        form.showFilterAdvanced = function() {
            var advancedSearchModal = $modal.open({
                templateUrl: '/advancedSearchModal.html',
                controller: 'advancedSearchModalController',
                size: 'lg'
            });
            advancedSearchModal.result.then(function (filterPengadaan) {
                if (filterPengadaan != undefined && filterPengadaan !== null) {
                    form.jenisPengadaan = filterPengadaan.jenisPengadaan;
                    form.metodePengadaan = filterPengadaan.metodePengadaan;
                    form.mataUang = filterPengadaan.mataUang;
                }
            });
        }
        
        form.indexRekapPengadaanList = [];
        
        $scope.tableSummaryPengadaan = new ngTableParams({
            page: 1, // show first page
            count: 5 // count per page
        }, {
            total: form.indexRekapPengadaanList.length, // length of data4
            getData: function ($defer, params) {
                $defer.resolve(form.indexRekapPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    	
    	if ($stateParams !== undefined && $stateParams.dataCabang !== null && $stateParams.dataCabang.id > 0) {
			$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + $stateParams.dataCabang.id)
    	    .success(function (data, status, headers, config) {
    	    	form.indexRekapPengadaanList = data;
            	form.totalNilaiHPSPengadaan = 0; 
    	    	if (form.indexRekapPengadaanList !== undefined && form.indexRekapPengadaanList.length > 0) {
    	    		angular.forEach(form.indexRekapPengadaanList, function(organisasi, index){
    	    	        var dataParameter = {};
    	    			if (form.tglAwal !== undefined) {
    	    				dataParameter.periodeAwal = $filter('date')(form.tglAwal, 'dd-MM-yyyy');
    	    			}
    	    			if (form.tglAkhir !== undefined) {
    	    				dataParameter.periodeAkhir = $filter('date')(form.tglAkhir, 'dd-MM-yyyy');
    	    			}
    	    			if (form.jenisPengadaan !== undefined) {
    	    				dataParameter.jenisPengadaanId = form.jenisPengadaan.id;
    	    			}
    	    			if (form.metodePengadaan !== undefined) {
    	    				dataParameter.metodePengadaanId = form.metodePengadaan.id;
    	    			}
    	    			if (form.mataUang !== undefined) {
    	    				dataParameter.mataUangId = form.mataUang.id;
    	    			}
    	    			dataParameter.organisasiId = organisasi.id;
    	    			// hitung jumlah pengadaan
    	    			$http({
    	    			    method: 'POST',
    	    			    url: $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanByParentOrganisasi',
    	    			    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    	    			    transformRequest: function(obj) {
    	    			        var str = [];
    	    			        for(var p in obj)
    	    			        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    	    			        return str.join("&");
    	    			    },
    	    			    data: dataParameter
    	    			}).success(function (data, status, headers, config) {
    	    				form.indexRekapPengadaanList[index].jumlahPengadaan = data[0];         	    				

    	        			// hitung jumlah nlai HPS
    	        			$http({
    	        			    method: 'POST',
    	        			    url: $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getTotalNilaiHPSPengadanByOrganisasiId',
    	        			    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    	        			    transformRequest: function(obj) {
    	        			        var str = [];
    	        			        for(var p in obj)
    	        			        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    	        			        return str.join("&");
    	        			    },
    	        			    data: dataParameter
    	        			}).success(function (data, status, headers, config) {
    	        				form.indexRekapPengadaanList[index].nilaiHPSPengadaan = data[0];
    	        				
    	        				form.totalNilaiHPSPengadaan = form.totalNilaiHPSPengadaan + data[0];     	    				
    	        			});
    	    			});
    	    		});
    	    	}
    	    	$scope.tableSummaryPengadaan.reload();
    	    });
		}
        
        form.searchRekapPengadaan = function() {
        	form.totalNilaiHPSPengadaan = 0; 
        	
        	var dataParameter = {};
    		if (form.tglAwal !== undefined) {
    			dataParameter.periodeAwal = $filter('date')(form.tglAwal, 'dd-MM-yyyy');
    		}
    		if (form.tglAkhir !== undefined) {
    			dataParameter.periodeAkhir = $filter('date')(form.tglAkhir, 'dd-MM-yyyy');
    		}
    		if (form.jenisPengadaan !== undefined) {
    			dataParameter.jenisPengadaanId = form.jenisPengadaan.id;
    		}
    		if (form.metodePengadaan !== undefined) {
    			dataParameter.metodePengadaanId = form.metodePengadaan.id;
    		}
    		if (form.mataUang !== undefined) {
    			dataParameter.mataUangId = form.mataUang.id;
    		}
        	
        	if (form.dataOrganisasi !== undefined) {
    			$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-by-id/'+form.dataOrganisasi.id)
        	    .success(function (data, status, headers, config) {
        	    	form.indexRekapPengadaanList = [];
        	    	form.indexRekapPengadaanList.push(data);
        	    	dataParameter.organisasiId = data.id;
        	    	// hitung jumlah pengadaan
        			$http({
        			    method: 'POST',
        			    url: $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanByParentOrganisasi',
        			    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        			    transformRequest: function(obj) {
        			        var str = [];
        			        for(var p in obj)
        			        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        			        return str.join("&");
        			    },
        			    data: dataParameter
        			}).success(function (data, status, headers, config) {
        				form.indexRekapPengadaanList[0].jumlahPengadaan = data[0];
        				
            			// hitung jumlah nlai HPS
            			$http({
            			    method: 'POST',
            			    url: $rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getTotalNilaiHPSPengadanByOrganisasiId',
            			    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            			    transformRequest: function(obj) {
            			        var str = [];
            			        for(var p in obj)
            			        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            			        return str.join("&");
            			    },
            			    data: dataParameter
            			}).success(function (data, status, headers, config) {
            				form.indexRekapPengadaanList[0].nilaiHPSPengadaan = data[0];
            				
            				form.totalNilaiHPSPengadaan = form.totalNilaiHPSPengadaan + data[0];     	 
            				
            			});
        			});

        	    	$scope.tableSummaryPengadaan.reload();
        	    });
    		}    		
        }
		
		$scope.buttonViewIndex = function(indexRekapPengadaan) {
			$http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/'+indexRekapPengadaan.id)
		    .success(function (data, status, headers, config) {
		    	if (data !== undefined && data[0] !== undefined && data[0].id > 1) {
		    		$state.go('app.promise.procurement-panitia-rekappengadaan-index', {dataCabang: indexRekapPengadaan});
		    	} else {
		    		$state.go('app.promise.procurement-panitia-rekappengadaan-detail', {dataDivisi: indexRekapPengadaan});
		    	}
		    });
		}
	}

	RekapPengadaanIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource','$location', '$state', '$stateParams', 'ngTableParams', '$modal', '$filter'];

})();

angular.module('naut')
.controller('advancedSearchModalController', function ($scope, $modalInstance, $http, $rootScope) {
	
    // find jenis pengadaan
    $http.get($rootScope.backendAddress + '/procurement/master/jenisPengadaanServices/getJenisPengadaanList')
    .success(function (data, status, headers, config) {
    	$scope.jenisPengadaanList = data;
    	if (data !== undefined && data.length > 0) {
    		$scope.selectedJenisPengadaan = data[0];
    	}
    });
    
    // find metode pengadaan
    $http.get($rootScope.backendAddress + '/procurement/master/metodePengadaanServices/getMetodePengadaanList')
    .success(function (data, status, headers, config) {
    	$scope.metodePengadaanList = data;
    	if (data !== undefined && data.length > 0) {
    		$scope.selectedMetodePengadaan = data[0];
    	}
    });
    
    // find mata Uang
    $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
    .success(function (data, status, headers, config) {
    	$scope.mataUangList = data;
    	if (data !== undefined && data.length > 0) {
    		$scope.selectedMataUang = data[0];
    	}
    });
    
    $scope.addFilterSearch = function () {
        var filterPengadaan = {};
        filterPengadaan.jenisPengadaan = $scope.selectedJenisPengadaan;
        filterPengadaan.metodePengadaan = $scope.selectedMetodePengadaan;
        filterPengadaan.mataUang = $scope.selectedMataUang;
        
        $modalInstance.close(filterPengadaan);
    }
    $scope.backToSearchForm = function () {
        $modalInstance.dismiss('cancel');
    }
});
