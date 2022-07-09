/**=========================================================
 * Module: RegistrasiDataKeuanganController.js
 * Author: F.H.K
 =========================================================*/


(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataKeuanganController', RegistrasiDataKeuanganController);

	function RegistrasiDataKeuanganController( $scope, $rootScope, ngTableParams, $state,RequestService,$stateParams) {
		
		var vm = this;
        vm.loginId = $rootScope.userLogin.user.id;
        vm.dataKeuanganList = [];
        vm.toDo=$stateParams.toDo;
        vm.valid=false;
        
        $scope.getKeuanganVendorList = function() {
        	vm.loading = true;
			RequestService.doGET('/procurement/vendor/KeuanganVendorServices/getkeuanganvendorbyvendorid')
				.then(function success(data) {
					vm.dataKeuanganList = data.keuanganVendorList;
		        	vm.status=data.status;
		        	if(vm.status==1){
						vm.valid=true;
						vm.subjudul="promise.procurement.RegistrasiVendor.DataKeuangan.judul_draft";
					}else{
						vm.subjudul="promise.procurement.RegistrasiVendor.DataKeuangan.judul";
					}
					if(vm.dataKeuanganList[0].vendor.isApproval==1){
						vm.disable=true;
						vm.subjudul="promise.procurement.RegistrasiVendor.DataKeuangan.judul_draft_approve";
					}
					
					$scope.tableKeuanganVendor = new ngTableParams({
	                    page: 1, // show first page
	                    count: 5 // count per page
	                }, {
	                    total: vm.dataKeuanganList.length, // length of data4
	                    getData: function ($defer, params) {
	                        $defer.resolve(vm.dataKeuanganList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	                    }
	                });
					vm.loading = false;
				}, function error(response) { 
					RequestService.modalInformation("template.informasi.gagal",'danger');
					vm.loading = false;
			});
        	
        }
        
        $scope.getKeuanganVendorList();
        
      
        $scope.approval = function() {
          	RequestService.modalConfirmation("Apakah Anda yakin untuk mengirimkan perubahan data?").then(function (result) {
    				vm.toDo='done';
    				$scope.saveApproval();	
    				vm.loading = false;
    			});    	
        }
        $scope.saveApproval = function() {
			var url = '/procurement/vendor/vendorServices/sendApproval/';
			
			var data={
					loginId:vm.loginId
			}
			RequestService.doPOST(url,data)
				.then(function success(data) {
					RequestService.modalInformation("template.informasi.kirim_approval",'success');
					$scope.tableKeuanganVendor.page(2);
					$scope.getKeuanganVendorList();
					$scope.tableKeuanganVendor.reload();
				}, function error(response) {
					$log.info("proses gagal");
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});

        }
        
        $scope.add = function() {
        $state.go('appvendor.promise.procurement-vendor-datakeuangan-view',{
			toDo:'add',
			status: vm.status
		});	      	
        }
        
        $scope.edit = function(dataKeuangan) {
        $state.go('appvendor.promise.procurement-vendor-datakeuangan-view',{
			toDo:'edit',
			dataKeuangan:dataKeuangan,
			status: vm.status
		});	
        }
        
        $scope.del = function (dataKeuangan) {
        	vm.url = '/procurement/vendor/KeuanganVendorServices/delete'; 		
			
			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data Keuangan?").then(function (result) {
				vm.loading = true;
				$scope.tableKeuanganVendor.page(2);
				
				var data= {
						id:dataKeuangan.id,
						status:vm.status
				}
				RequestService.doPOST(vm.url,data)
				.then(function success(data) {
					RequestService.modalInformation("template.informasi.hapus_sukses",'success');
						$scope.getKeuanganVendorList();
						$scope.tableKeuanganVendor.reload();
					 	vm.loading = false;
				}, function error(response) {
					RequestService.modalInformation("template.informasi.gagal",'danger');
					vm.loading = false;
				});
			});
		};
      
		
        
       
        
         
        /* ------------------------------------------------------------------------------------------------ */
		
	}
	
	RegistrasiDataKeuanganController.$inject = ['$scope', '$rootScope', 'ngTableParams', '$state' ,'RequestService','$stateParams'];

})();