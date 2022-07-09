
(function () {
	'use strict';

	angular.module('naut').controller('DataSertifikatController', DataSertifikatController);

	function DataSertifikatController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $window) {
		var vm = this;
		
		vm.viewFormSertifikat = false;
		vm.vendor = {};
		 
		vm.mdlname = 'Data Sertifikat';
		
		vm.getDataSertifikat = function () { 
			RequestService.doGET('/procurement/vendor/SertifikatVendorServices/getSertifikatVendorUserId/' + $rootScope.userLogin.user.id)
			.then(function successCallback(data) {
				vm.dataSertifikat = data;
			}, function errorCallback(response) {				 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
			});
		}
		
		vm.getDataSertifikat();
		
		vm.printSertifikat = function(){
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=PrintTDR&vPk=' + $rootScope.userLogin.user.id, '_blank');
//			RequestService.doPrint({reportFileName:'PrintTDR', vPk:$rootScope.userLogin.user.id});			
 		} 
	    
		vm.refreshKaptcha = function() {
        	vm.image = [{
        	    src: $rootScope.backendAddress + '/kaptcha.jpg?idx=' + Math.random(),
        	}];
    	}
		
		vm.refreshKaptcha();
		
		vm.perpanjangSertifikat = function(){
			vm.viewFormSertifikat = true;
		}
		
		vm.batalPerpanjangSertifikat = function(){
			vm.viewFormSertifikat = false;
		}
		 
		vm.prosesPerpanjangSertifikat = function(){
			if(vm.dataSertifikat.captcha == null || vm.dataSertifikat.captcha == ''){
				toaster.pop('error', vm.mdlname, 'Captcha harus diisi terlebih dahulu!');
			}else{
				ModalService.showModalConfirmation('Apakah Anda yakin akan melakukan permintaan perpanjangan sertifikat?').then(function (result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/procurement/vendor/SertifikatVendorServices/requestPerpanjanganSertifikatVendor', vm.dataSertifikat)
					.then(function successCallback(data) {
						vm.batalPerpanjangSertifikat();
						vm.getDataSertifikat();
						ModalService.closeModalInformation();
						if(data.captcha == null || data.captcha == ''){
							ModalService.showModalInformation('Kaptcha tidak sesuai!');	
						}else{
							ModalService.showModalInformation('Terima kasih, proses perpanjangan sertifikat menunggu persetujuan admin!');	
						}
						
						vm.refreshKaptcha();
					}, function errorCallback(response) {				 
						ModalService.closeModalInformation();
						ModalService.showModalInformation('Terjadi kesalahan pada system!');						
						vm.refreshKaptcha();
					});
		        });
			}
		}
	}

	DataSertifikatController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window'];

})();