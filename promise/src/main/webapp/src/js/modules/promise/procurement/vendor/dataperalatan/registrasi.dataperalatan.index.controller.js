/**=========================================================
 * Module: RegistrasiDataSegmentasiController.js
 * Author: F.H.K
 =========================================================*/

(function () {
	'use strict';
	angular
		.module('naut')
		.controller('RegistrasiDataPeralatanIndexController', RegistrasiDataPeralatanIndexController);

	function RegistrasiDataPeralatanIndexController($http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader,$stateParams , RequestService) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
		var vm = this;
		vm.loginId = $rootScope.userLogin.user.id;
        vm.toDo=$stateParams.toDo;
        vm.dataPeralatanList=[];
        vm.valid=false;
        
      $scope.getPeralatanVendorList = function(){
          
          vm.loading = true;
          RequestService.doGET('/procurement/vendor/PeralatanVendorServices/getperalatanvendorbyvendorid')
      	  .then(function success(data) {
      		vm.dataPeralatanList = data.peralatanVendorList;
        	vm.status=data.status;
        	
        	if(vm.status==1){
				vm.valid=true;
				vm.subjudul="promise.procurement.RegistrasiVendor.Peralatan.judul_draft";
			}else{
				vm.subjudul="promise.procurement.RegistrasiVendor.Peralatan.judul";
			}
			if(vm.dataPeralatanList[0].vendor.isApproval==1){
				vm.disable=true;
				vm.subjudul="promise.procurement.RegistrasiVendor.Peralatan.judul_draft_approve";
			}
			
        	$scope.dataperalatanTable = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.dataPeralatanList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.dataPeralatanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
        	vm.loading = false;
      	  }, function error(response) { 
      		  RequestService.modalInformation("template.informasi.gagal",'danger');
      		  vm.loading = false;
      	  });
      }
      
      $scope.getPeralatanVendorList();
      
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
					$scope.dataperalatanTable.page(2);
					$scope.getPeralatanVendorList();
					$scope.dataperalatanTable.reload();
				}, function error(response) {
					$log.info("proses gagal");
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});

		}
      
      	$scope.add = function() {
			$state.go('appvendor.promise.procurement-vendor-dataperalatan-view', {
				toDo : "Add",
				status :vm.status
			});
		}

		$scope.edit = function(dataPeralatan) {
			$state.go('appvendor.promise.procurement-vendor-dataperalatan-view', {
				dataPeralatan : dataPeralatan,
				toDo : "Edit",
				status:vm.status
			});
		}
		$scope.del = function (dataPeralatan) {
			vm.url = '/procurement/vendor/PeralatanVendorServices/delete/';
			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data Peralatan?").then(function (result) {
				vm.loading = true;
				$scope.dataperalatanTable.page(2);
				
				var data= {
						id:dataPeralatan.id,
						status:vm.status
				}
				RequestService.doPOST(vm.url,data)
				.then(function success(data) {
					RequestService.modalInformation("template.informasi.hapus_sukses",'success');
					 	$scope.getPeralatanVendorList();			
					 	$scope.dataperalatanTable.reload();
					 	vm.loading = false;
				}, function error(response) {
					RequestService.modalInformation("template.informasi.gagal",'danger');
					vm.loading = false;
				});
			});
		};
		
	}
	
	RegistrasiDataPeralatanIndexController.$inject = ['$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', '$stateParams', 'RequestService'];

})();


