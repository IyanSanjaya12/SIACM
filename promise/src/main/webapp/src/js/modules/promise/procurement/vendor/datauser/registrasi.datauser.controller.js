/**=========================================================
 * Module: RegistrasiDataUserController.js
 * Author: F.H.K
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataUserController', RegistrasiDataUserController);

	function RegistrasiDataUserController($scope,$rootScope,$timeout, $state, FileUploader, RequestService,$window,$log) {
		var vm = this;
        vm.id = $rootScope.userLogin.user.id;
        vm.isVendor = false;
        vm.loading = true;
        
        $scope.getUserAndVendor= function(){
        	RequestService.doGET('/procurement/user/get-user-and-vendor/'+ vm.id ).then(function success(data) {
        		//memastikan siapa yang ingin mengubah data vendor atau yang lain
        		vm.user=data.user;        		
        		if(data.vendor != null){
        			vm.vendor=data.vendor;
        			vm.isVendor= true;
        			vm.loading = false;
        		}
    	 		vm.loading=false;
    	 	}, function error(response) { 
    	 		RequestService.informError("Terjadi Kesalahan Pada System"); 
    	 		vm.loading = false;  
    	 	});
        }
       	$scope.getUserAndVendor();
       	
        
       	$scope.validatePassword = function(password){
			// Peraturan password:
			// 1. Harus mengandung 1 huruf KECIL dan 1 huruf BESAR
			// 2. Harus mengandung 1 ANGKA 0-9
			// 3. Harus lebih dari 8 karakter
			var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})")
			return strongRegex.test(password);
		}
       	
       	var uploadVendorLogo = $scope.uploadVendorLogo = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
       	

       	
		uploadVendorLogo.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,"img") && (this.queue.length < 10));
			}
		});
		
		uploadVendorLogo.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			RequestService.modalInformation('template.informasi.upload_sukses','success');
			
		};
		uploadVendorLogo.onErrorItem = function(fileItem, response, status, headers) {
			RequestService.modalInformation('template.informasi.upload_gagal','danger');
		};       
		uploadVendorLogo.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };
		
		$scope.uploadLogo = function() {
			angular.forEach(uploadVendorLogo.queue, function(item) {
        		item.upload();
            });
		}
		
		var uploadVendorHeadImg = $scope.uploadVendorHeadImg = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		
		uploadVendorHeadImg.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,"img") && (this.queue.length < 10));
			}
		});

		uploadVendorHeadImg.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			RequestService.modalInformation('template.informasi.upload_sukses','success');
		};
		
		uploadVendorHeadImg.onErrorItem = function(fileItem, response, status, headers) {
			RequestService.modalInformation('template.informasi.upload_gagal','danger');
		}; 
		
		uploadVendorHeadImg.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };
        
		$scope.uploadHeaderImg = function() {
			angular.forEach(uploadVendorHeadImg.queue, function(item) {
        		item.upload();
            });
		}
		
		
		$scope.save = function(){
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});	
		};
		
       	$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorNamaPengguna= '';
	         vm.errorPassword='';
	         vm.errorRetypePassword='';
	         vm.errorEmail='';
	         
	         /* Validation */
	         if (typeof vm.user.namaPengguna == 'undefined' || vm.user.namaPengguna == '') {
	                vm.errorNamaPengguna= 'template.error.field_kosong';
	                vm.isValid =  false;
	         }
	         if (typeof vm.newPassword && (angular.equals(vm.newPassword, vm.retypePassword) == false)) {
	        	 vm.errorRetypePassword= 'promise.procurement.master.user.error.password_beda';
	             vm.isValid = false;
	         }
	         if (typeof vm.newPassword === 'undefined' || vm.newPassword == '') {  
	         }
	         else{
	        	 if (typeof vm.retypePassword == undefined || vm.retypePassword == '') {
	        		 vm.errorRetypePassword= 'template.error.field_kosong';
		             vm.isValid =  false;
		         }
	        	 if ($scope.validatePassword(vm.newPassword)==false){
		        	 vm.errorPassword= 'promise.procurement.RegistrasiVendor.data_login.password_salah';
		        	 vm.isValid =  false;
		         }
	         }
	         
	         return vm.isValid;
	    }
       	
       	$scope.validasiUpload = function(fileItem) {
			var fileExt = fileItem.file.name.toLowerCase().split('.').pop();
            var fileSize = fileItem.file.size / (1024 * 1024);
            var maxUpload = $rootScope.fileUploadSize / (1024 * 1024);
            
            if (fileSize > maxUpload) {
            	RequestService.modalInformation('Ukuran file tidak boleh lebih dari '+maxUpload +'MB!','danger')
            	fileItem.remove();
                return false;
            } else if ((!(fileExt == 'jpg' || fileExt == 'jpeg'||fileExt == 'png'))) {
            	RequestService.modalInformation('promise.procurement.RegistrasiVendor.data_login.upload_ext','danger')
            	fileItem.remove();
                return false;
            }
            
            return true;
		}
		
        $scope.saveData = function(){
        	vm.upload=true;
        	vm.userMasterDTO={};
        	vm.userMasterDTO.user=vm.user;
        	vm.userMasterDTO.vendor=vm.vendor;
        	//console.log(vm.userMasterDTO)
        	
        	if (vm.newPassword == undefined || vm.newPassword == ''|| vm.newPassword == null){
        		vm.userMasterDTO.user.password = vm.user.password;
        	}
        	else {
				vm.userMasterDTO.user.password = vm.newPassword;
		    }
       		if (uploadVendorLogo !== undefined && uploadVendorLogo.queue.length > 0) {
				angular.forEach(uploadVendorLogo.queue, function(item) {
					if(item.isUploaded) {
						vm.vendor.logoImage = item.realFileName;
						vm.vendor.logoImageSize = item.file.size;
					}
					else{
						vm.upload=false;
					}
				});
			}
        	
        	if (uploadVendorHeadImg !== undefined && uploadVendorHeadImg.queue.length > 0) {
				angular.forEach(uploadVendorHeadImg.queue, function(item) {
					if(item.isUploaded) {
						vm.vendor.backgroundImage = item.realFileName;
						vm.vendor.backgroundImageSize = item.file.size;
					}
					else{
						vm.upload=false;
					}
				});
			}
        	RequestService.doPOSTJSON('/procurement/user/update-user-data', vm.userMasterDTO)
		    	.then(function success(data) {
		    		if(vm.upload==true){
		    			RequestService.informSaveSuccess();	    		
		    			$state.go($rootScope.dashboardPath);
		    		}
					else 
		    		{
		    			RequestService.modalInformation("template.informasi.upload",'danger');
		    		}
		        }, 
		        function error(response) {
		        	$log.info("proses gagal");
		        		RequestService.modalInformation("template.informasi.gagal",'danger');
		        });				
		}
     
        $scope.back= function() {
        	$state.go('appvendor.promise.dashboard');
        }
	}
	
	RegistrasiDataUserController.$inject = ['$scope','$rootScope','$timeout', '$state', 'FileUploader', 'RequestService', '$window','$log'];

})();