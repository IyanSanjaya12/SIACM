
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SertifikatVendorViewController', SertifikatVendorViewController);

	function SertifikatVendorViewController(RequestService, $scope, $rootScope, $stateParams, $state) {
		var vm = this;
		
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.vendor = ($stateParams.vendor != undefined) ? $stateParams.vendor : {};
		vm.vendorSertifikatList = [];
				
		vm.getVendor = function (){
			RequestService.doGET('/procurement/vendor/vendorServices/getVendor/' + vm.vendor.id)
			.then(function (data, status, headers, config) { 
				vm.vendor = data;
			});
		}
		vm.getVendor();
		
		vm.getVendorSertifikat = function (){
			RequestService.doGET('/procurement/vendor/SertifikatVendorRiwayatServices/getSertifikatVendorRiwayatByVendor/' + vm.vendor.id)
			.then(function (data, status, headers, config) { 
				vm.vendorSertifikatList = data;
			}); 
		}
		vm.getVendorSertifikat();
		
		$scope.back  = function () { 
			$state.go('app.promise.procurement-datasertifikat-datasertifikat');
		};
	}

	SertifikatVendorViewController.$inject = ['RequestService', '$scope', '$rootScope', '$stateParams','$state'];

})();