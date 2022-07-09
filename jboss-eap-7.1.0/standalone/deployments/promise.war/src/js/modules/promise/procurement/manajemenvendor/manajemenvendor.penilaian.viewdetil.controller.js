
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenilaianVendorViewDetilController', PenilaianVendorViewDetilController);

	function PenilaianVendorViewDetilController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, $q) {
		var vm = this;
		vm.dataVendor = $rootScope.penilaianDataVendor; 
		vm.penilaianVendor = $rootScope.penilaianVendor; 
		
		vm.getMataUang = function () { 
			RequestService.doGET('/procurement/master/mata-uang/get-list')
			.then(function (data, status, headers, config) { 
				vm.mataUangList = data; 
			}); 
		}
		
		vm.getMataUang();
		
		vm.formulaPenilaianList = [];
		
		vm.getPenilaianVendorIndikator = function () { 
			RequestService.doGET('/procurement/vendormanagement/PenilaianVendorIndikatorServices/getPenilaianVendorIndikatorGetByPenilaian/' + vm.penilaianVendor.vPerfId)
			.then(function (data, status, headers, config) { 
				vm.penilaianVendorIndikatorList = data; 
				
				vm.penilaianVendorIndikatorList.forEach(function(penilaianVendorIndikator, index, array) {
					RequestService.doGET('/procurement/vendormanagement/PenilaianVendorIndikatorDetailServices/getFormulaPenilaianByPenilaianIndikator/' + penilaianVendorIndikator.vPerfAspId)
					.then(function (data, status, headers, config) {
						vm.formulaPenilaianList = [];
						vm.formulaPenilaianList = data;
						vm.loading = false;
					});
				});
			}); 
		}
		
		vm.getPenilaianVendorIndikator();
		
		
		vm.penilaianBack = function(){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
		}
		
		vm.formulaPenilaian = function(fp){
			var result = '';
			if(fp != null && fp != ''){
				result = fp.fPBatasKiri + ' ';

				if(fp.fPPersamaanKiri == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKiri == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKiri == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKiri == 'lt'){ result = result + ' <';}

				result = result + ' x ';
								
				if(fp.fPPersamaanKanan == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKanan == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKanan == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKanan == 'lt'){ result = result + ' <';}

				result = result + ' ' + fp.fPBatasKanan;
				
			}
			return result;
		}
	}

	PenilaianVendorViewDetilController.$inject = ['RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state', '$q'];

})();

