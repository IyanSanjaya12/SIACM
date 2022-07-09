/*rmt*/

(function() {
	'use strict';

	angular.module('naut').controller('SanggahanVendorSatuanDetailController', SanggahanVendorSatuanDetailController);

	function SanggahanVendorSatuanDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams,  $anchorScroll,  ngTableParams, $state) {

		$scope.pengadaanId = $stateParams.pengadaanId

		var sanggahanDetail = this;
	
		// init mode
		$scope.isEditable = $rootScope.isEditable;
		$scope.loadingSaving = false;
		$scope.loading = true;

		function loadVendorSanggahan() {

			$scope.loading = true;

			/*
			 * Ambil data vendor by userId console.log('INFO : ' +
			 * JSON.stringify($rootScope.userLogin));
			 */
			$http.get( $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
				.success(function(data, status, headers, config) {
							sanggahanDetail.vendor = data;
										
							$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByPengadaanAndVendor/'+ $scope.pengadaanId+'/'+sanggahanDetail.vendor.id)
								.success(function(data, status, headers, config) {
									
									sanggahanDetail.sanggahanItemPengadaanMaterial =[];
									sanggahanDetail.sanggahanItemPengadaanJasa =[];
									
									 angular.forEach(data,function(value,index){
										 $scope.listHarga = [];
							             if (value.itemPengadaan.item.itemType.id == 1 ) 
							             	{  
							            	  sanggahanDetail.sanggahanItemPengadaanMaterial.push(value.itemPengadaan);
							            	}
							             	else
							            	 { 
							             		sanggahanDetail.sanggahanItemPengadaanJasa.push(value.itemPengadaan);
							            	 }
							         });
									 $scope.loading = false;
								});			
				})

		}

		$scope.gotoIndex = function() {
			$location.path('/appvendor/promise/procurement/vendor/sanggahan');
		};

		$scope.viewSanggahan = function(itemPengadaan) {
		     	$state.go('appvendor.promise.procurement-vendor-sanggahan-satuan-detail-more', {itemPengadaan: itemPengadaan} );
		     }
		 
		loadVendorSanggahan();

	}

	SanggahanVendorSatuanDetailController.$inject = [ '$scope', '$http','$rootScope', '$resource', '$location', 'toaster', '$filter','$stateParams','$anchorScroll',  'ngTableParams','$state' ];

})();