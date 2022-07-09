/**=========================================================
 * Module: RegistrasiDataPengalamanController.js
 * Author: YYI_WYDYN
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataPengalamanController', RegistrasiDataPengalamanController);

	function RegistrasiDataPengalamanController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
		var vm = this;
        
        vm.id = $rootScope.userLogin.user.id;
        vm.namaPengguna = $rootScope.userLogin.user.namaPengguna;
        vm.userId = $rootScope.userLogin.user.username;
        vm.dataPengalamanPekerjaanList = [];
        vm.dataMitraList = [];
        vm.dataWorkingProgressList = [];
        vm.pengalamanVendor={};
        vm.status=null;
        vm.valid=false;
        $scope.tanpaPengalaman = false;
        
        
        
        /* ================================= START Data Bidang Usaha ====================================== */
        RequestService.doGET('/procurement/master/BidangUsahaServices/getBidangUsahaList')
		.then(function success(data) {
			vm.bidangUsahaList = data;
		}, function error(response) {	
		});
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START Data Pengalaman ======================================== */
        $scope.getPengalamanVendor = function() {
        	vm.dataMitraList=[];
			vm.dataPengalamanPekerjaanList=[];
			vm.dataWorkingProgressList=[];
        	RequestService.doGET('/procurement/vendor/PengalamanVendorServices/getpengalamanbyvendorid')
    		.then(function success(data){

            	if(data.pengalamanVendorList.length > 0) {
            		angular.forEach(data.pengalamanVendorList,function(value,index) {
            			angular.forEach(vm.bidangUsahaList,function(BUList,index) {
                			if(BUList.nama == value.bidangUsaha)
                				value.bidangUsaha = BUList;
                		})
                		
            			if(value.tipePengalaman == 'PELANGGAN') {
            				vm.dataPengalamanPekerjaanList.push(value);
            			} else if(value.tipePengalaman == 'MITRA') {
            				vm.dataMitraList.push(value);
            			} else if(value.tipePengalaman == 'INPROGRESS') {
            				vm.dataWorkingProgressList.push(value);
            				
            			}
            		})
            	} else {
            		$scope.tanpaPengalaman = true;
            	}
            	vm.status=data.status;
            	
            	if(vm.status==1){
					vm.valid=true;
					vm.subjudul="promise.procurement.RegistrasiVendor.datapengalaman.judul_draft";
				}else{
					vm.subjudul="promise.procurement.RegistrasiVendor.datapengalaman.judul";
					
				}
				if(data.pengalamanVendorList[0].vendor.isApproval==1){
					vm.disable=true;
					vm.subjudul="promise.procurement.RegistrasiVendor.datapengalaman.judul_draft_approve";
				}
            	
            	$scope.tablePengalamanPekerjaan = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.dataPengalamanPekerjaanList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.dataPengalamanPekerjaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
                
                
                $scope.tableDataMitra = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.dataMitraList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.dataMitraList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
                
                
                $scope.tableWorkingProgress = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: vm.dataWorkingProgressList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.dataWorkingProgressList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
                
            }, function error(response) {	
    		});
        }
        $scope.getPengalamanVendor();
        /* ------------------------------------------------------------------------------------------------ */
        
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
 
        $scope.checked = function(pengalamanStatus) {
			if(pengalamanStatus) {
				$scope.tanpaPengalaman = true;
			} else {
				$scope.tanpaPengalaman = false;
			}
		}

        $scope.add = function(tipeNew) {
			$state.go('appvendor.promise.procurement-vendor-datapengalaman-view', {
				toDo : "add",
				status:vm.status,
				tipe :tipeNew
			});
		}
        
        $scope.edit = function(datapengalamanNew) {
			$state.go('appvendor.promise.procurement-vendor-datapengalaman-view', {
				toDo : "edit",
				status:vm.status,
				dataPengalaman :datapengalamanNew
			});
		
		}
        
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
					$scope.getPengalamanVendor();
					$scope.tablePengalamanPekerjaan.page(2);
					$scope.tablePengalamanPekerjaan.reload();
					$scope.tableDataMitra.page(2);
					$scope.tableDataMitra.reload();
					$scope.tableWorkingProgress.page(2);
					$scope.tableWorkingProgress.reload();
					vm.loading = false;
				}, function error(response) {
					$log.info("proses gagal");
					vm.loading = false;
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});

		}

        $scope.del = function (dataPengalaman) {
        	var url = '/procurement/vendor/PengalamanVendorServices/delete/';
			
        	var data={
        		id:dataPengalaman.id,
        		status:vm.status
        	};
        	
        	RequestService.modalConfirmation('Apakah anda yakin ingin menghapus data Pengalaman?').then(function(result) {
				RequestService.doPOST(url,data).success(function(data) {
					RequestService.modalInformation("template.informasi.hapus_sukses","success");
					$scope.getPengalamanVendor();
					$scope.tablePengalamanPekerjaan.page(2);
					$scope.tablePengalamanPekerjaan.reload();
					$scope.tableDataMitra.page(2);
					$scope.tableDataMitra.reload();
					$scope.tableWorkingProgress.page(2);
					$scope.tableWorkingProgress.reload();
				}).error(function(data, status, headers, config) {
					RequestService.modalInformation("template.informasi.gagal","danger");
				})
			});
        };      
        
        
	}
	
	RegistrasiDataPengalamanController.$inject = ['RequestService', '$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location'];

})();

