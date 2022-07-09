(function() {
	'use strict';

	angular.module('naut').controller('RegistrasiDataPeralatanViewController',
			RegistrasiDataPeralatanViewController);

	function RegistrasiDataPeralatanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log, toaster, FileUploader) {
		
		var vm = this;
		vm.id=$rootScope.userLogin.user.id;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.dataPeralatan = ($stateParams.dataPeralatan != undefined) ? $stateParams.dataPeralatan : {};
		vm.status = ($stateParams.status != undefined) ? $stateParams.status : {};
		//console.log(vm.dataPeralatan);
		vm.vendorId = $rootScope.vendorId; 
		
        $scope.kondisiList = function() {
			RequestService.doGET('/procurement/master/KondisiServices/getList')
				.then(function success(data) {
					vm.kondisiList = data;
				}, function error(response) {
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});
		}
		$scope.kondisiList();
		
		$scope.buktiKepemilikanList = function() {
				RequestService.doGET('/procurement/master/BuktiKepemilikanServices/getList')
					.then(function success(data) {
						vm.buktiKepemilikanList = data;
					}, function error(response) {
						RequestService.modalInformation("template.informasi.gagal",'danger');
					});
		}
		$scope.buktiKepemilikanList();
		
		// upload gambar peralatan asset
		var uploadGambarPeralatanAsset = $scope.uploadGambarPeralatanAsset = new FileUploader({
	        url: $rootScope.uploadBackendAddress,
	        method: 'POST'
	    });
		
		uploadGambarPeralatanAsset.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return this.queue.length < 10;
			}
		});
		
		uploadGambarPeralatanAsset.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUploadGambar(fileItem);
        };

		uploadGambarPeralatanAsset.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			
			RequestService.modalInformation('template.informasi.upload_sukses','success');
		};
		uploadGambarPeralatanAsset.onErrorItem = function(fileItem, response, status, headers) {
		      RequestService.modalInformation('template.informasi.upload_gagal','danger');
		};
		
		$scope.uploadGbrPeralatanAsset = function() {
			
			if (uploadGambarPeralatanAsset.queue.length > 1) {
				uploadGambarPeralatanAsset.queue.splice(0,uploadGambarPeralatanAsset.queue.length-1);
			}
			
			/*vm.dataPeralatan.fileNameGambarPeralatan = uploadGambarPeralatanAsset.queue[0].realFileName;
			vm.dataPeralatan.pathFileGambarPeralatan = uploadGambarPeralatanAsset.queue[0].file.name;
			vm.dataPeralatan.fileSizeGambarPeralatan = uploadGambarPeralatanAsset.queue[0].file.size;*/
			
			angular.forEach(uploadGambarPeralatanAsset.queue, function(item) {
        		item.upload();
            });
			
		}
		
		//upload dokumen bukti kepemilikan
		var uploadDokumenBuktiKepemilikan = $scope.uploadDokumenBuktiKepemilikan = new FileUploader({
	        url: $rootScope.uploadBackendAddress,
	        method: 'POST'
	    });	    
		
		uploadDokumenBuktiKepemilikan.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return this.queue.length < 10;
			}
		});
		
		uploadDokumenBuktiKepemilikan.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUploadDokumen(fileItem);
        };
        
		uploadDokumenBuktiKepemilikan.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			
			RequestService.modalInformation('template.informasi.upload_sukses','success');
		};
		uploadDokumenBuktiKepemilikan.onErrorItem = function(fileItem, response, status, headers) {
		      RequestService.modalInformation('template.informasi.upload_gagal','danger');
		};
		
		$scope.uploadDokBuktiKepemilikan = function() {
			
			if (uploadDokumenBuktiKepemilikan.queue.length > 1) {
				uploadDokumenBuktiKepemilikan.queue.splice(0,uploadDokumenBuktiKepemilikan.queue.length-1);
			}
			
			/*vm.dataPeralatan.fileNameBuktiKepemilikan = uploadDokumenBuktiKepemilikan.queue[0].realFileName;
			vm.dataPeralatan.pathFileBuktiKepemilikan = uploadDokumenBuktiKepemilikan.queue[0].file.name;
			vm.dataPeralatan.fileSizeBuktiKepemilikan = uploadDokumenBuktiKepemilikan.queue[0].file.size;*/
			
			angular.forEach(uploadDokumenBuktiKepemilikan.queue, function(item) {
        		item.upload();
            });	
		}
		
		$scope.validasiUploadDokumen = function(fileItem) {
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
		
		$scope.validasiUploadGambar = function(fileItem) {
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
		
		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					if(!$scope.checkupload()){
						RequestService.modalInformation("template.informasi.upload",'danger');
					}else{
		        		$scope.saveData();
						}
		        	}
			});
		}
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageJenisPeralatan = '';
			vm.errorMessageJumlahPeralatan = '';
			vm.errorMessageKapasitasPeralatan = '';
			vm.errorMessageMerkPeralatan = '';
			vm.errorMessageTahunPeralatan = '';
			vm.errorMessageKondisiPeralatan = '';
			vm.errorMessageLokasiPeralatan = '';
			vm.errorMessageBuktiMilikPeralatan = '';
			vm.errorMessageTahunError ='';
			
			if (typeof vm.dataPeralatan.jenis === 'undefined' || vm.dataPeralatan.jenis == '') {
				vm.errorMessageJenisPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.jumlah == 'undefined' || vm.dataPeralatan.jumlah == '') {
				vm.errorMessageJumlahPeralatan = "promise.procurement.RegistrasiVendor.Peralatan.error_peralatan";
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.kapasitas === 'undefined' || vm.dataPeralatan.kapasitas == '') {
				vm.errorMessageKapasitasPeralatan = "promise.procurement.RegistrasiVendor.Peralatan.error_peralatan";
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.merk === 'undefined' || vm.dataPeralatan.merk == '') {
				vm.errorMessageMerkPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.tahunPembuatan === 'undefined' || vm.dataPeralatan.tahunPembuatan == '') {
				vm.errorMessageTahunPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			} else {
	        	if(typeof vm.dataPeralatan.tahunPembuatan >"0" && vm.dataPeralatan.tahunPembuatan < "1950" || vm.dataPeralatan.tahunPembuatan > "2020"){
	        		//console.log("masuksini");
	        		vm.errorMessageTahunError ='promise.procurement.RegistrasiVendor.Peralatan.error_tahun';
	        		vm.isValid = false;
	        	}
			}
			if (typeof vm.dataPeralatan.kondisi === 'undefined' || vm.dataPeralatan.kondisi == '') {
				vm.errorMessageKondisiPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.lokasi === 'undefined' || vm.dataPeralatan.lokasi == '') {
				vm.errorMessageLokasiPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPeralatan.buktiKepemilikan === 'undefined' || vm.dataPeralatan.buktiKepemilikan == '') {
				vm.errorMessageBuktiMilikPeralatan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}
		
		$scope.checkupload = function(){
			vm.upload = true;
			if (vm.upload = true){
			if (uploadDokumenBuktiKepemilikan !== undefined) {
				//console.log('masuk sini');
				angular.forEach(uploadDokumenBuktiKepemilikan.queue, function(item){
					//console.log("Dokumen Bukti Kepemilikan IS UPLOADED = "+item);
	        		if(item.isUploaded) {
	        			vm.dataPeralatan.fileNameBuktiKepemilikan = item.realFileName;
						vm.dataPeralatan.pathFileBuktiKepemilikan = item.file.name;
						vm.dataPeralatan.fileSizeBuktiKepemilikan = item.file.size;					
						
	        		}
	        		else{
	        			vm.upload = false;
	        		}
				});
			}
			
			if (uploadGambarPeralatanAsset !== undefined) {
				//console.log('masuk sini 2')
				angular.forEach(uploadGambarPeralatanAsset.queue, function(item){
					//console.log("Gambar Peralatan Asset IS UPLOADED = "+item);
	        		if(item.isUploaded) {
	        			vm.dataPeralatan.fileNameGambarPeralatan = item.realFileName
						vm.dataPeralatan.pathFileGambarPeralatan = item.file.name;
						vm.dataPeralatan.fileSizeGambarPeralatan = item.file.size;
						
	        		}
	        		else{
	        			vm.upload = false;
	        		}
				});
			}
		  }
			return vm.upload;
		}
		
		$scope.saveData = function() {
			var url = '';
			vm.upload = true;
			vm.peralatanVendorDTO={};
			
			var data = {
				status : vm.status
			}
			vm.peralatanVendorDraft= {	
					id :vm.dataPeralatan.id,
					jenis 					:vm.dataPeralatan.jenis,
					buktiKepemilikan 		:vm.dataPeralatan.buktiKepemilikan,
					jumlah					:vm.dataPeralatan.jumlah, 
					kapasitas				:vm.dataPeralatan.kapasitas,
					fileNameGambarPeralatan : vm.dataPeralatan.fileNameGambarPeralatan,
					pathFileGambarPeralatan : vm.dataPeralatan.pathFileGambarPeralatan,
					fileSizeGambarPeralatan : vm.dataPeralatan.fileSizeGambarPeralatan,
					kondisi 				:vm.dataPeralatan.kondisi,
					lokasi 					: vm.dataPeralatan.lokasi,
					merk					:vm.dataPeralatan.merk,
					pathFileBuktiKepemilikan:vm.dataPeralatan.pathFileBuktiKepemilikan,
					fileNameBuktiKepemilikan:vm.dataPeralatan.fileNameBuktiKepemilikan,
					fileSizeBuktiKepemilikan:vm.dataPeralatan.fileSizeBuktiKepemilikan,
					tahunPembuatan 			:vm.dataPeralatan.tahunPembuatan,
					vendor 					:vm.dataPeralatan.vendor
					
			}
			//console.log(vm.peralatanVendorDraft);		
			if(vm.status == 0 && vm.toDo=="Edit"){
				data.peralatanVendor = vm.peralatanVendorDraft;
			}else
			{
				vm.peralatanVendorDraft.peralatanVendor=vm.dataPeralatan.peralatanVendor;
				data.peralatanVendorDraft=vm.peralatanVendorDraft;
			}
			
			if (vm.toDo == "Add") {
				vm.url = '/procurement/vendor/PeralatanVendorServices/insert';
			}

			if (vm.toDo == "Edit") {
				vm.peralatanVendorDraft.created = vm.dataPeralatan.created;
				vm.peralatanVendorDraft.isDelete = vm.dataPeralatan.isDelete;
				vm.url = '/procurement/vendor/PeralatanVendorServices/update';
			}
			//console.log(vm.toDo);
			data.loginId=vm.id;
			//console.log(data);
			
			vm.peralatanVendorDTO=data;
			//console.log(vm.peralatanVendorDTO);
			RequestService.doPOSTJSON(vm.url, vm.peralatanVendorDTO)
				.then(function success(data) {
					//console.log(data);					
						RequestService.modalInformation("template.informasi.simpan_sukses",'success');
						$state.go('appvendor.promise.procurement-vendor-dataperalatan');
				},
				function error(response) {
					$log.info("proses gagal");
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});
		}
		
		if (vm.toDo == "Edit"){
			 vm.dataPeralatan.tahunPembuatan = parseInt(vm.dataPeralatan.tahunPembuatan);
			 vm.downloadFileDok = $rootScope.backendAddress +'/file/view/'+vm.dataPeralatan.fileNameBuktiKepemilikan;
			 vm.downloadFileGbr = $rootScope.backendAddress +'/file/view/'+vm.dataPeralatan.fileNameGambarPeralatan;
			}
		
		$scope.back = function() {
			$state.go('appvendor.promise.procurement-vendor-dataperalatan');
		}
		
	}

	RegistrasiDataPeralatanViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', '$log', 'toaster', 'FileUploader' ];

})();