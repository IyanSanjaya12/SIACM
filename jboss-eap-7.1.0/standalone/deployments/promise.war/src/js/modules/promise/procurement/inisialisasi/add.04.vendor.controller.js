(function () {
	'use strict';

	angular.module('naut')
		.controller('UndangVendorPengadaan04Controller', UndangVendorPengadaan04Controller);

	function UndangVendorPengadaan04Controller($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, RequestService) {
		var vm = this;
		$scope.paramsUndangVendor = $rootScope.dataUndangVendor;
		$scope.jenisPengadaan = $rootScope.dataUndangVendor.jenisPengadaan;
			
		
		$scope.paramsUndangVendor.maxBaris = 0;
		RequestService.doPOST('/procurement/vendor/vendorServices/findAllVendorListForInitial', $scope.paramsUndangVendor)
	    .success(function (data, status, headers, config) {
	    	$scope.dataVendorList = data.vendorList;
	    	angular.forEach($scope.dataVendorList, function(dataVendor, indexVendor){
	    		angular.forEach($rootScope.inisialisasiForm.vendorList, function(dataUndanganVendor, indexUndanganVendor){	
	    			if (dataVendor.id === dataUndanganVendor.id) {
	    				dataVendor.checkUndanganVendor = true;	    				
	    			}
	    		});
	    	});
	    	
	    	$scope.tableVendorList = new ngTableParams({
	    		page: 1,            // show first page
	    		count: 10           // count per page
	    	}, {
	    		total: $scope.dataVendorList.length, // length of data4
	    		getData: function($defer, params) {
	    			$defer.resolve($scope.dataVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	    		}
	    	});
	    });
				
//		$scope.searchVendor = function () {
//			$scope.loading = true;
//			$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByName/' + $scope.namaVendor)
//				.success(function (data, status, headers, config) {
//					$scope.vendorList = data;
//					$scope.loading = false;
//				}).error(function (data, status, headers, config) {
//					$scope.loading = false;
//				});
//
//		};
//		$scope.checkedVendor = [];
//		$scope.togleCheckVendor = function (vendor) {
//			if ($scope.checkedVendor.indexOf(vendor) === -1) {
//				$scope.checkedVendor.push(vendor);
//			} else {
//				$scope.checkedVendor.splice($scope.checkedVendor.indexOf(vendor), 1);
//			}
//		};
//		
//		$scope.btnSimpanVendor = function () {
//			if ($scope.checkedVendor.length == 0) {
//				$scope.vendorError = true;
//			} else {
//				$scope.vendorError = false;
//				$scope.vendorDuplicateError = false;
//				if ($rootScope.inisialisasiForm.vendorList.length > 0) {
//					for (var i = 0; i < $rootScope.inisialisasiForm.vendorList.length; i++) {
//						for (var j = 0; j < $scope.checkedVendor.length; j++) {
//							if ($rootScope.inisialisasiForm.vendorList[i].id == $scope.checkedVendor[j].id) {
//								$scope.vendorDuplicateError = true;
//								break;
//							}
//						}
//					}
//				}
//				if ($scope.vendorDuplicateError == false) {
//					if ($rootScope.inisialisasiForm.vendorList.length == 0) {
//						$scope.loading = true;
//						$rootScope.inisialisasiForm.vendorList = $scope.checkedVendor;
//						$location.path('/app/promise/procurement/inisialisasi/add/bumn');
//						$scope.loading = false;
//					} else {
//						$scope.loading = true;
//						$rootScope.inisialisasiForm.vendorList = $rootScope.inisialisasiForm.vendorList.concat($scope.checkedVendor);
//						$location.path('/app/promise/procurement/inisialisasi/add/bumn');
//						$scope.loading = false;
//					}
//				}
//			}
//		}
		
		$scope.vendorCheck = function (vendor){
			$scope.vendorChecked = vendor;
		}
		
		$scope.btnSimpanVendor = function () {
			$rootScope.inisialisasiForm.vendorList = [];
			
			if($scope.jenisPengadaan == 5){
				$rootScope.inisialisasiForm.vendorList.push($scope.vendorChecked);
			}else{
		    	angular.forEach($scope.dataVendorList, function(value, index) {
		    		if (value.checkUndanganVendor === true) {
		    			$rootScope.inisialisasiForm.vendorList.push(value);
		    		}
		    	})
			}
			
			/*;*/
	        $location.path('/app/promise/procurement/inisialisasi/add04');
	       
	    }
		
		$scope.btnBatal = function () {
			$location.path('/app/promise/procurement/inisialisasi/add04');
		}
	}
	

	UndangVendorPengadaan04Controller.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', 'RequestService'];

})();