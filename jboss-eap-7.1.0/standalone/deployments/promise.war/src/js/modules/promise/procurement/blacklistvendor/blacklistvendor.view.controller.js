(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BlacklistViewController', BlacklistViewController);

    function BlacklistViewController($modal, ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state, $q, toaster, FileUploader, $log) {
    	
        var vm = this;
        
        vm.data = $rootScope.blacklistVendor;
        vm.blacklistVendorDtl = {};  
        vm.unblacklist = false;
        vm.unblacklistVendorDtl = {};
        vm.unblacklistVendorDtl.unblacklistVendor = {};
        vm.unblacklistVendorDtl.unblacklistVendor.vendor = {};
        vm.unblacklistVendorDtl.unblacklistVendorFileList = [];
		vm.canUnblacklist = true;
        
        $scope.formUnblacklist = function(){
        	if(vm.unblacklist == true){
        		vm.unblacklist = false;
        	}else{
        		vm.unblacklist = true;
        	}
        }
        
        $scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		}
                
        $scope.getDetail = function(id){
        	vm.blacklistVendorDtl = {};
			
			RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendorDetail/' + id)
			.then(function successCallback(data) {
				vm.blacklistVendorDtl = data;				
 
		        vm.unblacklistVendorDtl.blacklistVendor = vm.blacklistVendorDtl.blacklistVendor;
		        vm.unblacklistVendorDtl.unblacklistVendor.vendor = vm.blacklistVendorDtl.blacklistVendor.vendor;

				RequestService.doGET('/procurement/vendormanagement/UnblacklistVendorServices/getUnblacklistVendorMaxAndBlacklistId/' + vm.blacklistVendorDtl.blacklistVendor.vendor.id + '/' + id)
				.then(function successCallback(data2) {

					if(data2 != null && data2 != undefined && data2 != ''){ 
						$log.info(data2)
						vm.canUnblacklist = false;
						vm.unblacklistVendorDtl = data2; 
					}

					$log.info(vm.canUnblacklist)
					$log.info(vm.unblacklist)
					 
				}, function errorCallback(response) {				 
					
				});	
				
			}, function errorCallback(response) {				 
				
			});
        }
        $scope.getDetail(vm.data.blacklistVendorId);
        
        $scope.validateForm = function (){
        	vm.isValid = true;
        	vm.errorMessageCatatan = '';
        	vm.errorMessageCatatanVendor = '';
        	vm.errorMessageFileBlacklist = '';
			vm.errorMessageFile='';
			
			if(vm.unblacklistVendorDtl.unblacklistVendor.unblacklistVendorKeterangan == null || vm.unblacklistVendorDtl.unblacklistVendor.unblacklistVendorKeterangan == ''){
				vm.isValid = false;
				vm.errorMessageCatatan = 'promise.procurement.manajemenvendor.blacklistvendor.error.catatan';
			}if(vm.unblacklistVendorDtl.unblacklistVendor.unblacklistVendorKeteranganVnd == null || vm.unblacklistVendorDtl.unblacklistVendor.unblacklistVendorKeteranganVnd == ''){
				vm.isValid = false;
				vm.errorMessageCatatanVendor = 'promise.procurement.manajemenvendor.blacklistvendor.error.catatan_vendor';
			}if(uploader.queue.length == 0){
					vm.isValid = false;
					vm.errorMessageFileBlacklist = 'promise.procurement.manajemenvendor.blacklistvendor.error.file_blacklist';
				}else{
					var check = true;
					uploader.queue.forEach(function(file, index, array) {
						if(file.realFileName == null || file.realFileName == ''){
							check = false;
						} 
					});
					if(check == false){
						vm.isValid = false;
						vm.errorMessageFile = 'promise.procurement.manajemenvendor.blacklistvendor.error.file';
					}else{
						
					} 
				}
			
			return vm.isValid;
        }
        
        $scope.saveData= function(){
				vm.unblacklistVendorDtl.unblacklistVendorFileList = [];
	        		uploader.queue.forEach(function(file, index, array) {
	        			var unblacklistVendorFile = {};
	        				unblacklistVendorFile.unblacklistFile = file.realFileName;
	        					vm.unblacklistVendorDtl.unblacklistVendorFileList.push(unblacklistVendorFile);
				});
				
				RequestService.doPOSTJSON('/procurement/vendormanagement/UnblacklistVendorServices/insertUnblacklistVendor', vm.unblacklistVendorDtl)
				.then(function successCallback(data) {
					RequestService.informSaveSuccess();
					$state.go('app.promise.procurement-manajemenvendor-blacklistvendor');
				},
				function error(response) {
					$log.info("proses gagal");
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
        }
        		
		/** upload **/
		$scope.mSize = 5; //Panjang halaman yang tampil dibawah
		$scope.ayoSimpan = false;
		
		// Upload Images
		var uploader = $scope.uploader = new FileUploader({
	        url: $rootScope.uploadBackendAddress,
	        method: 'POST'
	    });
		
		uploader.filters.push({
	        name: 'customFilter',
	        fn: function (item /*{File|FileLikeObject}*/ , options) {
				return this.queue.length < 10;
			}
	    });
		
		uploader.onAfterAddingFile = function (fileItem) {

			if(uploader.queue.length>1) {
				angular.forEach(uploader.queue, function(item) {
						 
				var i=0;
					angular.forEach(uploader.queue, function(itm) {
						if(item.file.name == itm.file.name){
							i++;
						}
					});
						 
					//console.log("jumlah duplicate " + i );
					if(i == 2 && item.file.name == fileItem.file.name){
						toaster.pop('error', fileItem.file.name, 'File tidak boleh sama!');
						fileItem.remove();
					}
						 
				});
					 
			}
			 
	    };
	    
	    uploader.onBeforeUploadItem = function(item) {
            $log.info('onBeforeUploadItem', item);
            $log.info(uploader.queue);
            
        };

		uploader.onCompleteItem = function (fileItem, response, status, headers) {
	        fileItem.realFileName = response.fileName;
	        $scope.loading = false;
	    };
	    
	    uploader.onProgressItem = function (fileItem, progress) {
			$scope.progressTime = progress;
			$scope.loadingUpload = true;
			$scope.loading = true;
	    };
	    
	    uploader.onCompleteAll = function() {
	    	$scope.ayoSimpan = true;
	    };
	    
		$scope.viewFile = function(namaFile) {
			window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
		}
		/** upload **/
		
		$scope.back  = function () {
        	$state.go('app.promise.procurement-manajemenvendor-blacklistvendor');
        }
    }
    
    BlacklistViewController.$inject = ['$modal', 'ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state', '$q', 'toaster', 'FileUploader', '$log'];

})();