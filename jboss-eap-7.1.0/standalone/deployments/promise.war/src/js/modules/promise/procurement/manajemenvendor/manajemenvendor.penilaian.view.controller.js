
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenilaianVendorViewController', PenilaianVendorViewController);

	function PenilaianVendorViewController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state) {
		var vm = this;
		vm.dataVendor = $rootScope.penilaianDataVendor;
		vm.loading = true;
		
		vm.penilaianAdd = function(){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/edit');
		}
		
		vm.penilaianBack = function(){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian');
		}
		
		vm.penilaianView = function(vp){
			$rootScope.penilaianVendor = vp;
			$rootScope.penilaianDataVendor = vm.dataVendor;
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/viewdetil');
		}
		
		vm.penilaianEdit = function(vp){
			$rootScope.penilaianVendor = vp;
			$rootScope.penilaianDataVendor = vm.dataVendor;
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/viewedit');
		}
		
		vm.penilaianPrint = function(vp){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/viewdetil');
		}
		
		vm.getPenilaianRekananList = function () { 
			RequestService.doGET('/procurement/vendormanagement/PenilaianVendorServices/getPenilaianVendorListByVendor/' + vm.dataVendor.id)
			.then(function (data, status, headers, config) { 
				vm.penilaianRekananList = data;
				vm.loading = false;
			});
		}
		
		vm.penilaianDelete = function(id){
			ModalService.showModalConfirmation('Apakah anda yakin akan menghapus data ini?').then(function (result) {
				ModalService.showModalInformationBlock();
				RequestService.doGET('/procurement/vendormanagement/PenilaianVendorServices/deletePenilaianVendor/' + id)
				.then(function successCallback(response) {
					vm.getPenilaianRekananList();
					ModalService.closeModalInformation();
				}, function errorCallback(response) {	
					ModalService.closeModalInformation();			 
					ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
			});
		}
		
		vm.getPenilaianRekananList();
	}

	PenilaianVendorViewController.$inject = ['ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state'];

})();