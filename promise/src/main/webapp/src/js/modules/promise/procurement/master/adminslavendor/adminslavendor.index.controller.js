/**=========================================================
 * Module: MasterAdminSLAVendorController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('MasterAdminSLAVendorController', MasterAdminSLAVendorController);

	function MasterAdminSLAVendorController(RequestService, $scope, $state) {
		
		var vm = this;
		
		$scope.getAdminSLAVendorList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/SLAVendorProsesServices/getSLAVendorProsesList')
			.then(function success(data) {
				vm.SLAVendorProcessList = data;
				vm.loading = false;
			}, function error(response) {				 
				RequestService.informError("Terjadi Kesalahan Pada System");
				vm.loading = false;
			});			 
		}
		
		$scope.getAdminSLAVendorList();
		
		$scope.edit = function (slaVendorProses) {
			$state.go('app.promise.procurement-master-adminslavendor-view',{
				slaVendorProses : slaVendorProses
			});	
		}
	}

	MasterAdminSLAVendorController.$inject = ['RequestService', '$scope', '$state'];

})();