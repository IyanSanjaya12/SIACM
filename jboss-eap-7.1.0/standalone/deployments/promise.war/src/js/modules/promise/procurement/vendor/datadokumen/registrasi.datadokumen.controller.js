/**=========================================================
 * Module: RegistrasiDataDokumenController.js
 * Author: F.H.K
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataDokumenController', RegistrasiDataDokumenController);

	function RegistrasiDataDokumenController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $window, $timeout) {
		
		/* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
		var vm = this;
		vm.today = new Date();
        vm.valid=false;
        vm.id = $rootScope.userLogin.user.id;
        vm.namaPengguna = $rootScope.userLogin.user.namaPengguna;
        vm.userId = $rootScope.userLogin.user.username;
        vm.dokumenRegistrasiVendorDTO={};
        vm.VendorRegistrasiForm = {};
        vm.SalinanAktePendirian = {};
        vm.SalinanTandaDaftar = {};
        vm.SalinanSuratIjinUsaha = {};
        vm.BuktiFisikPerusahaan = {};
        vm.DokumenQuality = {};
        vm.DokumenTeknik = {};
        vm.DokumenPks = {};
        vm.DokumenSpr = {};
        vm.DokumenSpb = {};
        vm.VendorSkd = {};
        vm.SKB={};
        vm.TempSKB={};

        
        
        /* ------------------------------------------------------------------------------------------------ */
        
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
    					loginId:vm.id
    			}		
    			RequestService.doPOST(url,data)
    				.then(function success(data) {
    					RequestService.modalInformation("template.informasi.kirim_approval",'success');
    					$scope.getDokumenVendor();
    				}, function error(response) {
    					$log.info("proses gagal");
    					RequestService.modalInformation("template.informasi.gagal",'danger');
    				});

        }
        /* ================================= START Get Data Dokumen ============================================== */
        $scope.getDokumenVendor = function() {
        	vm.loading = true;
        	RequestService.doGET('/procurement/vendor/DokumenRegistrasiVendorServices/getdokumenregistrasivendorbyvendorid')
        	.then(function success(data) {
        		vm.loading = false;
        		vm.SKBHistoryList= data.SKBHistoryList;
        		vm.status=data.status;
        		vm.vendor=data.dokumenRegistrasiVendorList[0].vendor;
        		vm.dataDokumenList = data.dokumenRegistrasiVendorList;
        		vm.dataVendorSKDList = data.vendorSKDList;
        		if(vm.status==1){
					vm.valid=true;
					vm.subjudul="promise.procurement.RegistrasiVendor.DataDokumen.judul_draft";
				}else{
					vm.subjudul="promise.procurement.RegistrasiVendor.DataDokumen.judul";
				}
				
				
            	if(vm.dataDokumenList.length > 0) {
            		angular.forEach(vm.dataDokumenList,function(value,index){
            			if(value.vendor.isApproval==1){
        					vm.disable=true;
        					vm.subjudul="promise.procurement.RegistrasiVendor.DataDokumen.judul_draft_approve";
        				}
            			if(value.subject == 'Salinan Tanda Daftar Perusahaan (TDP)') {
            				$scope.ambilDataTabel(vm.SalinanTandaDaftar, value);
            				$scope.downloadFileTDP = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataTDP = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
                				if(value.tanggalBerakhir < vm.today.getTime()){
                					$scope.SalinanTandaDaftarExpired = "Expired";
     							}else{
                					$scope.SalinanTandaDaftarExpired = "";
     							}
            				}
            			}
            			if(value.subject == 'Bukti Fisik Perusahaan') {
            				$scope.ambilDataTabel(vm.BuktiFisikPerusahaan, value);
            				$scope.downloadFileBFP = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataBFP = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.BuktiFisikPerusahaanExpired = "Expired";
	 							}else{
	            					$scope.BuktiFisikPerusahaanExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Dokumen Teknik') {
            				$scope.ambilDataTabel(vm.DokumenTeknik, value);
            				$scope.downloadFileDT = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataDT = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.DokumenTeknikExpired = "Expired";
	 							}else{
	            					$scope.DokumenTeknikExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Vendor Registrasi Form') {
            				$scope.ambilDataTabel(vm.VendorRegistrasiForm, value);
            				$scope.downloadFileVRF = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataVRF = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.VendorRegistrasiFormExpired = "Expired";
	 							}else{
	            					$scope.VendorRegistrasiFormExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Surat Keterangan Bebas') {
            				$scope.ambilDataTabel(vm.SKB, value);
            				$scope.ambilDataTabel(vm.TempSKB, value);
            				$scope.downloadFileSKB = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSKB = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.SKBExpired = "Expired";
	 							}else{
	            					$scope.SKBExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya') {
            				$scope.ambilDataTabel(vm.SalinanAktePendirian, value);
            				$scope.downloadFileSAP = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSAP = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.SalinanAktePendirianExpired = "Expired";
	 							}else{
	            					$scope.SalinanAktePendirianExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Salinan Surat Ijin Usaha (SIUP SIUJK)') {
            				$scope.ambilDataTabel(vm.SalinanSuratIjinUsaha, value);
            				$scope.downloadFileSIUP = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSIUP = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.SalinanSuratIjinUsahaExpired = "Expired";
	 							}else{
	            					$scope.SalinanSuratIjinUsahaExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Dokumen Quality yang dimiliki') {
            				$scope.ambilDataTabel(vm.DokumenQuality, value);
            				$scope.downloadFileDQ = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataDQ = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.DokumenQualityExpired = "Expired";
	 							}else{
	            					$scope.DokumenQualityExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Dokumen PKS') {
            				$scope.ambilDataTabel(vm.DokumenPks, value);
            				$scope.downloadFilePKS = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				if($scope.isPKS = 1) {
            					$scope.isPks = true;
            				}
            				$scope.tampilDataPKS = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.DokumenPksExpired = "Expired";
	 							}else{
	            					$scope.DokumenPksExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Dokumen SPR') {
            				$scope.ambilDataTabel(vm.DokumenSpr, value);
            				$scope.downloadFileSPR = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSPR = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.DokumenSprExpired = "Expired";
	 							}else{
	            					$scope.DokumenSprExpired = "";
	 							}
            				}
            			}
            			if(value.subject == 'Dokumen SPB') {
            				$scope.ambilDataTabel(vm.DokumenSpb, value);
            				$scope.downloadFileSPB = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSPB = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
	            				if(value.tanggalBerakhir < vm.today.getTime()){
	            					$scope.DokumenSpbExpired = "Expired";
	 							}else{
	            					$scope.DokumenSpbExpired = "";
	 							}
            				}
            			}
            		})
            	}
            	if(vm.dataVendorSKDList.length > 0) {
            		angular.forEach(vm.dataVendorSKDList,function(value,index){
            				$scope.ambilDataSkd(vm.VendorSkd, value);
            				
            				$scope.downloadFileSKD = $rootScope.backendAddress +'/file/view/'+value.fileName;
            				$scope.tampilDataSKD = true;
            				if(value.tanggalBerakhir != null && value.tanggalBerakhir != ''){
                				if(value.tanggalBerakhir < vm.today.getTime()){
                					$scope.DokumenSkdExpired = "Expired";
     							}else{
                					$scope.DokumenSkdExpired = "";
     							}
            				}
            		})
            	}
            	
            },
            function error(response) {
            	RequestService.modalInformation('template.informasi.gagal', 'danger');
    		});
        }
        $scope.getDokumenVendor();
        
        $scope.openHistorySKB = function () {
	           $scope.loading = false;
	           $rootScope.anggaranSelected = {};
	           var modalInstance = $modal.open({
	               templateUrl: 'SKBHistoryView.html',
	               controller: ModalViewHistorySKB,
	               resolve:{
	            	   SKBHistoryList : function (){
	            	   return  vm.SKBHistoryList;
	               }},
	               size: 'lg'
	           });

	       };
	       var ModalViewHistorySKB = function ($http, $modalInstance,$scope, $rootScope, $resource, $location,SKBHistoryList) {
	    	   $scope.SKBHistoryList = SKBHistoryList;
	    	   $scope.downloadLink = $rootScope.backendAddress +'/file/view/';
	    	   $scope.closeModal = function(){
	    	       $modalInstance.close();
	    	    }
	       }
        
        $scope.checkDataDok = function(value1, value2) {
        	
        	if(value1.namaDokumen == value2.namaDokumen &&
        	   $filter('date')(value1.tanggalTerbit, "yyyy-MM-dd")==$filter('date')(value2.tanggalTerbit, "yyyy-MM-dd")&&
        	   $filter('date')(value1.tanggalBerakhir, "yyyy-MM-dd")==$filter('date')(value2.tanggalBerakhir, "yyyy-MM-dd")&&
               value1.pathFile == value2.pathFile&&
               value1.fileName == value2.fileName&&
               value1.fileSize == value2.fileSize){
        		return true;
        	}else{
        		return false;
        	}
			
		}
       
        $scope.ambilDataTabel = function(alias, value) {
        	alias.id = value.id;
			alias.namaDokumen = value.namaDokumen;
			alias.tanggalTerbit = value.tanggalTerbit;
			alias.tanggalBerakhir = value.tanggalBerakhir;
			alias.subject = value.subject;
			alias.pathFile = value.pathFile;
			alias.fileName = value.fileName;
			alias.fileSize = value.fileSize;
			
		}
        
        $scope.ambilDataSkd = function(alias, value) {
        	alias.id = value.id;
			alias.namaDokumen = value.namaDokumen;
			alias.alamat = value.alamat;
			alias.tanggalTerbit = value.tanggalTerbit;
			alias.tanggalBerakhir = value.tanggalBerakhir;
			alias.pathFile = value.pathFile;
			alias.fileName = value.fileName;
			alias.fileSize = value.fileSize;
		}
        
        $scope.isPKSChecked = function (isPKS) {
			if (isPKS) {
				$scope.isPks = true;
			} else {
				$scope.isPks = false;
			}
		}
        /* ------------------------------------------------------------------------------------------------ */
        
        /* ================================= START CONTROLLER DETAIL ====================================== */
        $scope.rekapUpload = function(dataUpload, namaProgress) {
			dataUpload.filters.push({
				name: 'customFilter',
				fn: function (item /*{File|FileLikeObject}*/ , options) {
					return this.queue.length < 10;
				}
			});

			dataUpload.onCompleteItem = function (fileItem, response, status, headers) {
				console.info('onCompleteItem', fileItem, response, status, headers);
				vm.jumlahUploaded ++;
				fileItem.realFileName = response.fileName;
				if(vm.jumlahFile == vm.jumlahUploaded){
					vm.disable = false;
					RequestService.modalInformation('template.informasi.upload_sukses', 'success');
					vm.uploadingText= "";
				}
			};
			
			dataUpload.onErrorItem = function (fileItem, response, status, headers) {
				RequestService.modalInformation('template.informasi.upload_gagal', 'danger');
			};
			
			dataUpload.onProgressItem = function (fileItem, progress) {
				$scope.progressTime = progress
	        };
		}
        
        $scope.validasiUpload = function(fileItem) {
			var fileExt = fileItem.file.name.toLowerCase().split('.').pop();
            var fileSize = fileItem.file.size / (1024 * 1024);
            var maxUpload = $rootScope.fileUploadSize / (1024 * 1024);
            
            if (fileSize > maxUpload) {
            	RequestService.modalInformation('Ukuran file tidak boleh lebih dari ' + maxUpload + ' MB!', 'danger');
            	fileItem.remove();
                return false;
            } else if ((!(fileExt == 'pdf' || fileExt == 'xls' || fileExt == 'xlsx' || fileExt == 'doc' || fileExt == 'docx' || fileExt == 'zip' || fileExt == 'jpg' || fileExt == 'jpeg'))) {
            	RequestService.modalInformation('template.informasi.upload_dok', 'danger');
            	fileItem.remove();
                return false;
            }
            return true;
		}
        
        vm.tanggalTerbitStatusVRF = false;
	    $scope.tanggalTerbitOpen1 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusVRF = true;
	    };
	    vm.tanggalBerakhirStatusVRF = false;
	    $scope.tanggalBerakhirOpen1 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusVRF = true;
	    };    		
		var uploadVendorRegistrasiForm = $scope.uploadVendorRegistrasiForm = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });	    
		$scope.rekapUpload(uploadVendorRegistrasiForm, $scope.VRFProgress);
		
		uploadVendorRegistrasiForm.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
	    vm.tanggalTerbitStatusSKB = false;
	    $scope.tanggalTerbitOpen12 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSKB = true;
	    };
	    vm.tanggalBerakhirStatusSKB = false;
	    $scope.tanggalBerakhirOpen12 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSKB = true;
	    };    		
		var uploadSKB = $scope.uploadSKB = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });	    
		$scope.rekapUpload(uploadSKB, $scope.SKBProgress);
		
		vm.tanggalTerbitStatusSAP = false;
	    $scope.tanggalTerbitOpen2 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSAP = true;
	    };
	    vm.tanggalBerakhirStatusSAP = false;
	    $scope.tanggalBerakhirOpen2 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSAP = true;
	    };   		
		var uploadSalinanAktePendirian = $scope.uploadSalinanAktePendirian = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		$scope.rekapUpload(uploadSalinanAktePendirian, $scope.SAPProgress);
		
		uploadSalinanAktePendirian.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusSTD = false;
	    $scope.tanggalTerbitOpen3 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSTD = true;
	    };
	    vm.tanggalBerakhirStatusSTD = false;
	    $scope.tanggalBerakhirOpen3 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSTD = true;
	    };		
		var uploadSalinanTandaDaftar = $scope.uploadSalinanTandaDaftar = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });		
		$scope.rekapUpload(uploadSalinanTandaDaftar, $scope.TDPProgress);
		
		uploadSalinanTandaDaftar.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusSIUP = false;
	    $scope.tanggalTerbitOpen4 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSIUP = true;
	    };
	    vm.tanggalBerakhirStatusSIUP = false;
	    $scope.tanggalBerakhirOpen4 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSIUP = true;
	    };		
		var uploadSalinanSuratIjinUsaha = $scope.uploadSalinanSuratIjinUsaha = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });	    
		$scope.rekapUpload(uploadSalinanSuratIjinUsaha, $scope.SIUPProgress);
		
		uploadSalinanSuratIjinUsaha.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusBFP = false;
	    $scope.tanggalTerbitOpen5 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusBFP = true;
	    };
	    vm.tanggalBerakhirStatusBFP = false;
	    $scope.tanggalBerakhirOpen5 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusBFP = true;
	    };		
		var uploadBuktiFisikPerusahaan = $scope.uploadBuktiFisikPerusahaan = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });	    
		$scope.rekapUpload(uploadBuktiFisikPerusahaan, $scope.BFPProgress);
		
		uploadBuktiFisikPerusahaan.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusDQ = false;
	    $scope.tanggalTerbitOpen6 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusDQ = true;
	    };
	    vm.tanggalBerakhirStatusDQ = false;
	    $scope.tanggalBerakhirOpen6 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusDQ = true;
	    };		
		var uploadDokumenQuality = $scope.uploadDokumenQuality = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });	    
		$scope.rekapUpload(uploadDokumenQuality, $scope.DQProgress);
		
		uploadDokumenQuality.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusDT = false;
	    $scope.tanggalTerbitOpen7 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusDT = true;
	    };
	    vm.tanggalBerakhirStatusDT = false;
	    $scope.tanggalBerakhirOpen7 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusDT = true;
	    };		
		var uploadDokumenTeknik = $scope.uploadDokumenTeknik = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });		
		$scope.rekapUpload(uploadDokumenTeknik, $scope.DTProgress);
		
		uploadDokumenTeknik.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusPKS = false;
	    $scope.tanggalTerbitOpen8 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusPKS = true;
	    };
	    vm.tanggalBerakhirStatusPKS = false;
	    $scope.tanggalBerakhirOpen8 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusPKS = true;
	    };
		var uploadDokumenPks = $scope.uploadDokumenPks = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		$scope.rekapUpload(uploadDokumenPks, $scope.PKSProgress);
		
		uploadDokumenPks.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusSPR = false;
	    $scope.tanggalTerbitOpen9 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSPR = true;
	    };
	    vm.tanggalBerakhirStatusSPR = false;
	    $scope.tanggalBerakhirOpen9 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSPR = true;
	    };
		var uploadDokumenSpr = $scope.uploadDokumenSpr = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		$scope.rekapUpload(uploadDokumenSpr, $scope.SPRProgress);
		
		uploadDokumenSpr.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusSPB = false;
	    $scope.tanggalTerbitOpen10 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSPB = true;
	    };
	    vm.tanggalBerakhirStatusSPB = false;
	    $scope.tanggalBerakhirOpen10 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSPB = true;
	    };
		var uploadDokumenSpb = $scope.uploadDokumenSpb = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		$scope.rekapUpload(uploadDokumenSpb, $scope.SPBProgress);
		
		uploadDokumenSpb.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		vm.tanggalTerbitStatusSKD = false;
	    $scope.tanggalTerbitOpen11 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalTerbitStatusSKD = true;
	    };
	    vm.tanggalBerakhirStatusSKD = false;
	    $scope.tanggalBerakhirOpen11 = function ($event) {
	        $event.preventDefault();
	        $event.stopPropagation();
	        vm.tanggalBerakhirStatusSKD = true;
	    };
		var uploadVendorSkd = $scope.uploadVendorSkd = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		$scope.rekapUpload(uploadVendorSkd, $scope.SKDProgress);
		
		uploadVendorSkd.onAfterAddingFile = function(fileItem) {
			$scope.validasiUpload(fileItem);
        };
		
		$scope.btnUploaded = function() {
			vm.disable = true;
			vm.jumlahFile= 0;
			vm.uploadingText= "uploading: ";
			vm.jumlahUploaded = 0;
			if (uploadSalinanTandaDaftar !== undefined && uploadSalinanTandaDaftar.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadBuktiFisikPerusahaan !== undefined && uploadBuktiFisikPerusahaan.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadDokumenTeknik !== undefined && uploadDokumenTeknik.queue.length > 0) {
				vm.jumlahFile ++;
			}
				
			if (uploadVendorRegistrasiForm !== undefined && uploadVendorRegistrasiForm.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadSKB !== undefined && uploadSKB.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadSalinanAktePendirian !== undefined && uploadSalinanAktePendirian.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadSalinanSuratIjinUsaha !== undefined && uploadSalinanSuratIjinUsaha.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadDokumenQuality !== undefined && uploadDokumenQuality.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadDokumenPks !== undefined && uploadDokumenPks.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadDokumenSpr !== undefined && uploadDokumenSpr.queue.length > 0) {
				vm.jumlahFile ++;
			}
			if (uploadDokumenSpb !== undefined && uploadDokumenSpb.queue.length > 0) {
				
				vm.jumlahFile ++;
				
			}
			if (uploadVendorSkd !== undefined && uploadVendorSkd.queue.length > 0) {
				vm.jumlahFile ++;
				
			}
			
			if (uploadSalinanTandaDaftar !== undefined && uploadSalinanTandaDaftar.queue.length > 0) {
				$scope.tampilTDP = true;
				angular.forEach(uploadSalinanTandaDaftar.queue, function(item) {
                	item.upload();
                	$scope.namaFileTDP = item.file.name;
                });
			}
			if (uploadBuktiFisikPerusahaan !== undefined && uploadBuktiFisikPerusahaan.queue.length > 0) {
				$scope.tampilBFP = true;
				angular.forEach(uploadBuktiFisikPerusahaan.queue, function(item) {
            		item.upload();                    	
            		$scope.namaFileBFP = item.file.name;
                });
			}
			if (uploadDokumenTeknik !== undefined && uploadDokumenTeknik.queue.length > 0) {
				$scope.tampilDT = true;
				angular.forEach(uploadDokumenTeknik.queue, function(item) {
            		item.upload();                   	
            		$scope.namaFileDT = item.file.name;
                });
			}
				
			if (uploadVendorRegistrasiForm !== undefined && uploadVendorRegistrasiForm.queue.length > 0) {
				$scope.tampilVRF = true;
				angular.forEach(uploadVendorRegistrasiForm.queue, function(item) {
	           		item.upload();                   	
	           		$scope.namaFileVRF = item.file.name;
	            });
			}
			if (uploadSKB !== undefined && uploadSKB.queue.length > 0) {
				$scope.tampilSKB = true;
				angular.forEach(uploadSKB.queue, function(item) {
	           		item.upload();                   	
	           		$scope.namaFileSKB = item.file.name;
	            });
			}
			if (uploadSalinanAktePendirian !== undefined && uploadSalinanAktePendirian.queue.length > 0) {
				$scope.tampilSAP = true;
				angular.forEach(uploadSalinanAktePendirian.queue, function(item) {
	           		item.upload();                   	
	           		$scope.namaFileSAP = item.file.name;
	            });
			}
			if (uploadSalinanSuratIjinUsaha !== undefined && uploadSalinanSuratIjinUsaha.queue.length > 0) {
				$scope.tampilSIUP = true;
				angular.forEach(uploadSalinanSuratIjinUsaha.queue, function(item) {
	            	item.upload();                 
	            	$scope.namaFileSIUP = item.file.name;
	            });
				}
			if (uploadDokumenQuality !== undefined && uploadDokumenQuality.queue.length > 0) {
				$scope.tampilDQ = true;
				angular.forEach(uploadDokumenQuality.queue, function(item) {
	           		item.upload();                
	           		$scope.namaFileDQ = item.file.name;
	            });
			}
			if (uploadDokumenPks !== undefined && uploadDokumenPks.queue.length > 0) {
				$scope.tampilPKS= true;
				angular.forEach(uploadDokumenPks.queue, function(item) {
	           		item.upload();                
	           		$scope.namaFilePKS = item.file.name;
	            });
			}
			if (uploadDokumenSpr !== undefined && uploadDokumenSpr.queue.length > 0) {
				$scope.tampilSPR = true;
				angular.forEach(uploadDokumenSpr.queue, function(item) {
	           		item.upload();                
	           		$scope.namaFileSPR = item.file.name;
	            });
			}
			if (uploadDokumenSpb !== undefined && uploadDokumenSpb.queue.length > 0) {
				$scope.tampilSPB = true;
				angular.forEach(uploadDokumenSpb.queue, function(item) {
	           		item.upload();                
	           		$scope.namaFileSPB = item.file.name;
	            });
			}
			if (uploadVendorSkd !== undefined && uploadVendorSkd.queue.length > 0) {
				$scope.tampilSKD = true;
				angular.forEach(uploadVendorSkd.queue, function(item) {
	           		item.upload();                
	           		$scope.namaFileSKD = item.file.name;
	            });
			}
		}
		
        $scope.saveDataDokumenVendor = function() {
        	
          	var targetURI= '/procurement/vendor/DokumenRegistrasiVendorServices/insert';
          	vm.dokumenRegistrasiVendorDTO = {
					status : vm.status,
					dokumenRegistrasiVendorDraftList:[]
					
					
				}
           	
           	if($scope.isPks == false) {
           		RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataDokumen.informasi.error_pks', 'danger');
           	} else {
           		vm.dokumenList = [];
           		
           		if (vm.VendorRegistrasiForm !== undefined && vm.VendorRegistrasiForm.namaDokumen !== undefined ) {
              		vm.VendorRegistrasiForm.vendor = vm.vendor;
                	vm.VendorRegistrasiForm.subject = 'Vendor Registrasi Form';
                   	if (uploadVendorRegistrasiForm !== undefined && uploadVendorRegistrasiForm.queue.length > 0) {
                       	//dokumen upload
                         angular.forEach(uploadVendorRegistrasiForm.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.VendorRegistrasiForm.fileName = item.realFileName;
                       			vm.VendorRegistrasiForm.pathFile = item.file.name;
                       			vm.VendorRegistrasiForm.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}       	
                         });
                   	}
                   	
                   	vm.dokumenList.push(vm.VendorRegistrasiForm);
                   	
               	}
           		if (vm.SKB !== undefined && vm.SKB.namaDokumen !== undefined ) {
           			vm.SKB.vendor = vm.vendor;
                	vm.SKB.subject = 'Surat Keterangan Bebas';                  
                    if (uploadSKB !== undefined && uploadSKB.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadSKB.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.SKB.fileName = item.realFileName;
                       			vm.SKB.pathFile = item.file.name;
                       			vm.SKB.fileSize = item.file.size;  
                       		} else {
                       			vm.statusSimpan = false;
                       		}
                            	                  	
                        });
                    }
                	vm.dokumenList.push(vm.SKB);
                }
                	
              	if (vm.SalinanAktePendirian !== undefined && vm.SalinanAktePendirian.namaDokumen !== undefined ) {
              		vm.SalinanAktePendirian.vendor = vm.vendor;
                	vm.SalinanAktePendirian.subject = 'Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya';               	
                    if (uploadSalinanAktePendirian !== undefined && uploadSalinanAktePendirian.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadSalinanAktePendirian.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.SalinanAktePendirian.fileName = item.realFileName;
                       			vm.SalinanAktePendirian.pathFile = item.file.name;
                       			vm.SalinanAktePendirian.fileSize = item.file.size;  
                       		} else {
                       			vm.statusSimpan = false;
                       		}
                            	                  	
                        });
                    }
                	vm.dokumenList.push(vm.SalinanAktePendirian);
                }
              	
              	if (vm.SalinanTandaDaftar !== undefined && vm.SalinanTandaDaftar.namaDokumen !== undefined ) {
              		vm.SalinanTandaDaftar.vendor = vm.vendor;
                	vm.SalinanTandaDaftar.subject = 'Salinan Tanda Daftar Perusahaan (TDP)';
                   	if (uploadSalinanTandaDaftar !== undefined && uploadSalinanTandaDaftar.queue.length > 0) {
                       	//Hasil upload
                        angular.forEach(uploadSalinanTandaDaftar.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.SalinanTandaDaftar.fileName = item.realFileName;
                       			vm.SalinanTandaDaftar.pathFile = item.file.name;
                       			vm.SalinanTandaDaftar.fileSize = item.file.size;  
                       		} else {
                       			vm.statusSimpan = false;
                       		}
                        });
                   	}
                	vm.dokumenList.push(vm.SalinanTandaDaftar);
                }
                	
                if (vm.SalinanSuratIjinUsaha !== undefined && vm.SalinanSuratIjinUsaha.namaDokumen !== undefined ) {
              		vm.SalinanSuratIjinUsaha.vendor = vm.vendor;
                	vm.SalinanSuratIjinUsaha.subject = 'Salinan Surat Ijin Usaha (SIUP SIUJK)';                
                   	if (uploadSalinanSuratIjinUsaha !== undefined && uploadSalinanSuratIjinUsaha.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadSalinanSuratIjinUsaha.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.SalinanSuratIjinUsaha.fileName = item.realFileName;
                       			vm.SalinanSuratIjinUsaha.pathFile = item.file.name;
                       			vm.SalinanSuratIjinUsaha.fileSize = item.file.size;  
                       		} else {
                       			vm.statusSimpan = false;
                       		}     	
                        });
                   	}
                	vm.dokumenList.push(vm.SalinanSuratIjinUsaha);
                }
                	
                if (vm.BuktiFisikPerusahaan !== undefined && vm.BuktiFisikPerusahaan.namaDokumen !== undefined ) {
              		vm.BuktiFisikPerusahaan.vendor = vm.vendor;
                	vm.BuktiFisikPerusahaan.subject = 'Bukti Fisik Perusahaan';
                   	if (uploadBuktiFisikPerusahaan !== undefined && uploadBuktiFisikPerusahaan.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadBuktiFisikPerusahaan.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.BuktiFisikPerusahaan.fileName = item.realFileName;
                       			vm.BuktiFisikPerusahaan.pathFile = item.file.name;
                       			vm.BuktiFisikPerusahaan.fileSize = item.file.size;   
                       		} else {
                       			vm.statusSimpan = false;
                       		}                 	
                        });
                   	}
                	vm.dokumenList.push(vm.BuktiFisikPerusahaan);
                }
                	
                if (vm.DokumenQuality !== undefined && vm.DokumenQuality.namaDokumen !== undefined ) {
              		vm.DokumenQuality.vendor = vm.vendor;
                	vm.DokumenQuality.subject = 'Dokumen Quality yang dimiliki';
                   	if (uploadDokumenQuality !== undefined && uploadDokumenQuality.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadDokumenQuality.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.DokumenQuality.fileName = item.realFileName;
                       			vm.DokumenQuality.pathFile = item.file.name;
                       			vm.DokumenQuality.fileSize = item.file.size;   
                       		} else {
                       			vm.statusSimpan = false;
                       		}                 	
                        });
                   	}
                	vm.dokumenList.push(vm.DokumenQuality);
                }
                	
                if (vm.DokumenTeknik !== undefined && vm.DokumenTeknik.namaDokumen !== undefined ) {
              		vm.DokumenTeknik.vendor = vm.vendor;
                	vm.DokumenTeknik.subject = 'Dokumen Teknik';	
                   	if (uploadDokumenTeknik !== undefined && uploadDokumenTeknik.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadDokumenTeknik.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.DokumenTeknik.fileName = item.realFileName;
                       			vm.DokumenTeknik.pathFile = item.file.name;
                       			vm.DokumenTeknik.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}                   	
                        });
                   	}
                	vm.dokumenList.push(vm.DokumenTeknik);
                }
                
                if (vm.DokumenPks !== undefined && vm.DokumenPks.namaDokumen !== undefined ) {
              		vm.DokumenPks.vendor = vm.vendor;
                	vm.DokumenPks.subject = 'Dokumen PKS';
                   	if (uploadDokumenPks !== undefined && uploadDokumenPks.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadDokumenPks.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.DokumenPks.fileName = item.realFileName;
                       			vm.DokumenPks.pathFile = item.file.name;
                       			vm.DokumenPks.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}                   	
                        });
                   	}
                	vm.dokumenList.push(vm.DokumenPks);
                }
                
                if (vm.DokumenSpr !== undefined && vm.DokumenSpr.namaDokumen !== undefined ) {
              		vm.DokumenSpr.vendor = vm.vendor;
                	vm.DokumenSpr.subject = 'Dokumen SPR';	
                   	if (uploadDokumenSpr !== undefined && uploadDokumenSpr.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadDokumenSpr.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.DokumenSpr.fileName = item.realFileName;
                       			vm.DokumenSpr.pathFile = item.file.name;
                       			vm.DokumenSpr.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}                   	
                        });
                   	}
                	vm.dokumenList.push(vm.DokumenSpr);
                }
                
                if (vm.DokumenSpb !== undefined && vm.DokumenSpb.namaDokumen !== undefined ) {
              		vm.DokumenSpb.vendor = vm.vendor;
                	vm.DokumenSpb.subject = 'Dokumen SPB';
                   	if (uploadDokumenSpb !== undefined && uploadDokumenSpb.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadDokumenSpb.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.DokumenSpb.fileName = item.realFileName;
                       			vm.DokumenSpb.pathFile = item.file.name;
                       			vm.DokumenSpb.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}                   	
                        });
                   	}
                	vm.dokumenList.push(vm.DokumenSpb);
                }
                
                
                if (vm.VendorSkd !== undefined && vm.VendorSkd.namaDokumen !== undefined ) {
              		vm.VendorSkd.vendor = vm.vendor;	
                   	if (uploadVendorSkd !== undefined && uploadVendorSkd.queue.length > 0) {
                       	//dokumen upload
                        angular.forEach(uploadVendorSkd.queue, function(item) {
                       		if(item.isUploaded) {
                       			vm.VendorSkd.fileName = item.realFileName;
                       			vm.VendorSkd.pathFile = item.file.name;
                       			vm.VendorSkd.fileSize = item.file.size; 
                       		} else {
                       			vm.statusSimpan = false;
                       		}                   	
                        });
                   	}	
                }
           	}
           	var data={
           			
        			alamat:vm.VendorSkd.alamat,
        			fileName:vm.VendorSkd.fileName,
        			fileSize:vm.VendorSkd.fileSize,
        			namaDokumen:vm.VendorSkd.namaDokumen,
        			pathFile:vm.VendorSkd.pathFile,
        			statusCeklist:vm.VendorSkd.statusCeklist,
        			tanggalBerakhir:vm.VendorSkd.tanggalBerakhir,
        			tanggalTerbit:vm.VendorSkd.tanggalTerbit,
        		
        	}
           
			if(vm.status == 0){
				vm.dokumenRegistrasiVendorDTO.vendorSKD = data;
			}else
			{
				data.vendorSKD=vm.VendorSkd.vendorSKD;
				vm.dokumenRegistrasiVendorDTO.vendorSKDDraft=data;
			}
			
        	vm.dokumenRegistrasiVendorDTO.vendorSKDDraft=data;
           	vm.dokumenRegistrasiVendorDTO.loginId=vm.id;
           	vm.dokumenRegistrasiVendorDTO.dokumenRegistrasiVendorDraftList=vm.dokumenList;
           	
           	RequestService.doPOSTJSON(targetURI, vm.dokumenRegistrasiVendorDTO)
           		.then(function success(data) {
           			RequestService.modalInformation('template.informasi.simpan_sukses', 'success');
           			$scope.getDokumenVendor();
	    			
           		},
    			function error(response) {
	    			RequestService.modalInformation('template.informasi.gagal', 'danger');
	         	   	
           	});       
        }
        
        $scope.checkNomorDokumen= function(){
        	vm.SKB.vendor = vm.vendor;
        	RequestService.doPOSTJSON('/procurement/vendor/DokumenRegistrasiVendorServices/checkNomorDokumen',vm.SKB)
        	.then(function success(data) {
        		if(data.valid){
        			if(data.info == "Doc Masi Berlaku"){
        				RequestService.modalConfirmation("Dokumen lama masi berlaku , ingin memperbarui data ?").then(function(result) {
    						$scope.saveDataDokumenVendor();	
    					})
        			}else{
        				RequestService.modalConfirmation().then(function(result) {
    						$scope.saveDataDokumenVendor();	
    					})
        			}
						
				}else{
					if(data.info == "Doc Exist"){
						RequestService.modalInformation('Nomor dokumen sudah pernah terpakai', 'danger');
					}
					if(data.info == "Change Doc"){
						RequestService.modalInformation('Dokumen expired, harap ganti dengan nomor yang baru', 'danger');
					}
				}
        	});
        }
          
        $scope.btnSimpan = function() {
			
				if(Boolean( vm.SalinanTandaDaftar.namaDokumen) && Boolean(vm.BuktiFisikPerusahaan.namaDokumen)&& Boolean(vm.DokumenTeknik.namaDokumen)){
					if($scope.checkDataDok(vm.SKB,vm.TempSKB)){//kalo SKB gada perubahan langsung save
						RequestService.modalConfirmation().then(function(result) {
						$scope.saveDataDokumenVendor();	
					})
					}else{
						//jika SKB ada perubahan
						$scope.checkNomorDokumen();
					}
															
				} else {
					RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataDokumen.informasi.error_data', 'danger');
				}
			
		}
        
        $scope.backToDashboard = function() {
        	$state.go('appvendor.promise.dashboard');
        }
        /* ------------------------------------------------------------------------------------------------ */
	}
	
	RegistrasiDataDokumenController.$inject = ['RequestService','$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location', '$window', '$timeout'];

})();