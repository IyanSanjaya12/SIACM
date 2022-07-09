/**=========================================================
 * Module: RegistrasiDataBankController.js
 * Author: F.H.K
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataBankController', RegistrasiDataBankController);

	function RegistrasiDataBankController($http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $window ,$timeout, RequestService, ModalService, $stateParams, $log) {

		
		var vm = this;
        vm.toDo=$stateParams.toDo;
        vm.loginId = $rootScope.userLogin.user.id;
        
        vm.dataBankList = [];
        vm.valid=false;
     
        $scope.getBankVendor = function(){
        	
        	vm.dataBankList = [];
        	vm.loading = true;
        	RequestService.doGET('/procurement/vendor/BankVendorServices/getbankvendorbyvendorid')
			.then(function success(data) {
				vm.dataBankList = data.bankVendorList;
				vm.status=data.status;
				if(vm.status==1){
					vm.valid=true;
					vm.subjudul="promise.procurement.RegistrasiVendor.DataBank.judul_draft";
				}else{
					vm.subjudul="promise.procurement.RegistrasiVendor.DataBank.judul";
					if(vm.dataBankList.length<=1){
						vm.deletestatus= false;
					}
				}
				if(vm.dataBankList[0].vendor.isApproval==1){
					vm.disable=true;
					vm.subjudul="promise.procurement.RegistrasiVendor.DataBank.judul_draft_approve";
				}
				
				$scope.dataBankTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.dataBankList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.dataBankList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
				vm.loading = false;
			}, function error(response) { 
				RequestService.modalInformation("template.informasi.gagal",'danger');
				vm.loading = false;
		});
        }
        
        $scope.getBankVendor();
        
        
        $scope.approval = function() {
        	RequestService.modalConfirmation("Apakah Anda yakin untuk mengirimkan perubahan data?").then(function (result) {
				
				
				vm.loading = true;
				$scope.saveApproval();
				
				
				
			});    	
            }
        
        $scope.saveApproval = function() {
			var url = '/procurement/vendor/vendorServices/sendApproval';
			
			var data={
					loginId:vm.loginId
			}
			
			RequestService.doPOST(url)
				.then(function success(data) {
					RequestService.modalInformation("template.informasi.kirim_approval",'success');
					$scope.dataBankTable.page(2);

					$scope.getBankVendor();
					
					$scope.dataBankTable.reload();
					vm.loading = false;
				}, function error(response) {
					$log.info("proses gagal");
					vm.loading = false;
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});

		}
            
        
         
        $scope.add = function() {
            $state.go('appvendor.promise.procurement-vendor-databank-view',{
    			toDo:'Add',
    			status:vm.status
    				
    		});	      	
            }
            
            $scope.edit = function(dataBank) {
            $state.go('appvendor.promise.procurement-vendor-databank-view',{
    			toDo:'Edit',
    			dataBank:dataBank,
    			status:vm.status
    		});	
            }
         
        $scope.del = function (dataBank) {
    			var url = '/procurement/vendor/BankVendorServices/delete';
    			
    			
    			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data keuangan ?").then(function (result) {
    				vm.loading = true;
    				$scope.dataBankTable.page(2);
    				
    				var data= {
    						id:dataBank.id,
    						status:vm.status
    				}
    			
    				
    				RequestService.doPOST(url,data)
					.then(function success(data) {
						
						RequestService.modalInformation("template.informasi.hapus_sukses",'success');
						
						
						$scope.getBankVendor();
						
						
						$scope.dataBankTable.reload();
						vm.loading = false;
					}, function error(response) {
						RequestService.modalInformation("template.informasi.gagal",'danger');
						
						vm.loading = false;
				});
    					
    				
    				
    			});
    		};
    		
        /* -------------------------------- END Rincian Controller --------------------------------------- */
	}
	
	RegistrasiDataBankController.$inject = ['$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location', '$window' ,'$timeout', 'RequestService', 'ModalService','$stateParams', '$log'];

})();
