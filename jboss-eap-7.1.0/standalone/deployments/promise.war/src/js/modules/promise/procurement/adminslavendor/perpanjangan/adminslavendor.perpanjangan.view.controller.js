(function() {
	'use strict';

	angular.module('naut').controller('AdminSLAPerpanjanganVendorViewController', AdminSLAPerpanjanganVendorViewController);

	function AdminSLAPerpanjanganVendorViewController($location, $http, $rootScope, $resource, $state, $scope, RequestService, ModalService) {
		var vm = this;
		var id = $rootScope.slaAdminVendorId;
		
		vm.getDetailAdminSLA = function () { 
			RequestService.doGET('/procurement/vendor/SLAAdminVendorServices/getSLAAdminVendorByIdDetail/' + id)
			.then(function successCallback(data) {
				vm.slaAdminVendor = data.slaAdminVendor;
				vm.slaAdminVendorDetailList = data.slaAdminVendorDetailList;
			}, function errorCallback(response) {				 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
				vm.loading = false;
			});
		}

		vm.getDetailAdminSLA();
		
		vm.back = function(){
			$location.path('/app/promise/procurement/adminslavendor/perpanjangan');
		}
	}

	AdminSLAPerpanjanganVendorViewController.$inject = [ '$location', '$http', '$rootScope', '$resource', '$state', '$scope', 'RequestService', 'ModalService' ];

})();