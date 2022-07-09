/**
 * ========================================================= Module:
 * RegistrasiDataSegmentasiController.js Author: F.H.K
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('RegistrasiDataSegmentasiController', RegistrasiDataSegmentasiController);

	function RegistrasiDataSegmentasiController($http, $scope, $rootScope, ngTableParams, $state, RequestService, $log) {

		var vm = this;

		
		vm.dataSegmentasiList = [];
		vm.valid=false;
		vm.status=null;				
		$scope.getSegmentasiVendor = function() {
			vm.dataSegmentasiList=[];
			RequestService.doGET('/procurement/vendor/SegmentasiVendorServices/getsegmentasibyvendorid/')
						.then(
							function(data, status, headers, config) {
								
								vm.dataSegmentasiList = data.segmentasiVendorList;
								vm.status=data.status;
								
								if(vm.status==1){
									vm.valid=true;
									vm.subjudul="promise.procurement.RegistrasiVendor.DataSegmentasi.subjudul_draft";
								}else{
									vm.valid=false;
									vm.subjudul="promise.procurement.RegistrasiVendor.DataSegmentasi.subjudul";
									if(vm.dataSegmentasiList.length<=1){
										vm.deletestatus= false;
									}
								}
								if(vm.dataSegmentasiList[0].vendor.isApproval==1){
									vm.disable=true;
									vm.subjudul="promise.procurement.RegistrasiVendor.DataSegmentasi.subjudul_draft_approve";
								}

								vm.tableSegmentasiVendor = new ngTableParams(
										{
											page : 1, // show first page
											count : 5
										// count per page
										},
										{
											total : vm.dataSegmentasiList.length, // length
																					// of
																					// data4
											getData : function($defer, params) {
												$defer.resolve(vm.dataSegmentasiList.slice((params.page() - 1)* params.count(),
														params.page()* params.count()));
											}
										});

							});
		}
		$scope.getSegmentasiVendor();
		
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
						
						$scope.getSegmentasiVendor();
						RequestService.modalInformation("template.informasi.kirim_approval",'success');
						 vm.tableSegmentasiVendor.page(2);
						vm.tableSegmentasiVendor.reload();
						vm.loading = false;
					}, function error(response) {
						$log.info("proses gagal");
						vm.loading = false;
						RequestService.modalInformation("template.informasi.gagal",'danger');
					});

			}
	    

		$scope.add = function(){
			 $state.go('appvendor.promise.procurement-vendor-datasegmentasi-view',{
	            	toDo : 'Add',
	            	status:vm.status
	            });
		}
		
		$scope.edit = function(dataSegmentasi){		
			$state.go('appvendor.promise.procurement-vendor-datasegmentasi-view',{
            	dataSegmentasi:dataSegmentasi,
				toDo : 'Edit',
				status:vm.status
            });
		};

		$scope.del = function (dataSegmentasi) {
        	RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data?").then(function (result) {
        		/*vm.dataSegmentasiList.splice(index, 1);*/
 
        		
				if (dataSegmentasi.id != undefined) {
					var dataSegmentasiId = dataSegmentasi.id;
					
					var data= {
    						id:dataSegmentasi.id,
    						status:vm.status
    				}

						RequestService.doPOST('/procurement/vendor/SegmentasiVendorServices/delete', data)
					.then(function success(data) {
						RequestService.informDeleteSuccess();
						$scope.getSegmentasiVendor();
						 RequestService.modalInformation("Data berhasil dihapus!",'success');
						 vm.tableSegmentasiVendor.page(2);
						vm.tableSegmentasiVendor.reload();
					}, function error(response) { 
					    RequestService.informError("Terjadi Kesalahan Pada System");
						vm.loading = false;
					});
				}
			});
        }
			
	}

	RegistrasiDataSegmentasiController.$inject = [ '$http', '$scope', '$rootScope', 'ngTableParams','$state', 'RequestService', '$log' ];
})();