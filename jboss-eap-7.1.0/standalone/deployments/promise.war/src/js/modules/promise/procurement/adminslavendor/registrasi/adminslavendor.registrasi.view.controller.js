(function() {
	'use strict';

	angular.module('naut').controller('AdminSLARegistrasiVendorViewController', AdminSLARegistrasiVendorViewController);

	function AdminSLARegistrasiVendorViewController($location, $http, $rootScope, $resource, $state, $scope, RequestService, ModalService) {
		var vm = this;
		var id = $rootScope.slaAdminVendorId;
		
		$scope.getDetailAdminSLA = function () { 
			RequestService.doGET('/procurement/vendor/SLAAdminVendorServices/getSLAAdminVendorByIdDetail/' + id)
			.then(function successCallback(data) {
				vm.slaAdminVendor = data.slaAdminVendor;
				vm.slaAdminVendorDetailList = data.slaAdminVendorDetailList;
			}, function errorCallback(response) {				 
				RequestService.informError('Terjadi kesalahan pada system!');
				vm.loading = false;
			});
		}

		$scope.getDetailAdminSLA();
		
		$scope.back = function(){
			$state.go('app.promise.procurement-manajemenvendor-adminslavendor-registrasi');
		}
	}

	AdminSLARegistrasiVendorViewController.$inject = [ '$location', '$http', '$rootScope', '$resource', '$state', '$scope', 'RequestService', 'ModalService' ];

})();