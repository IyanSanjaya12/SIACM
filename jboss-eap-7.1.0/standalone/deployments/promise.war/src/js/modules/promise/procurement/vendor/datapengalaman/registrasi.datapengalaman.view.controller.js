(function() {
	'use strict';

	angular.module('naut').controller('DataPengalamanViewController',
			DataPengalamanViewController);

	function DataPengalamanViewController(FileUploader, $scope, $http,
			$rootScope, $resource, $location, toaster, RequestService, $state,
			$stateParams, $log) {
		var vm = this;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.tipe = ($stateParams.tipe != undefined) ? $stateParams.tipe : {};
		vm.status = ($stateParams.status != undefined) ? $stateParams.status : {};
		vm.id = $rootScope.userLogin.user.id;
		vm.dataPengalaman = ($stateParams.dataPengalaman != undefined) ? $stateParams.dataPengalaman: {};
		vm.list = {};
		vm.isUploading = false;
		if (vm.toDo == "edit"){
			vm.list.bidangUsaha = vm.dataPengalaman.bidangUsaha;
			vm.downloadFile1 = $rootScope.backendAddress + '/file/view/'+ vm.dataPengalaman.fileName;
		}
		if (vm.toDo == "add") {
			vm.dataPengalaman.tipePengalaman = vm.tipe;
		}

		/*
		 * ================================= START Data Bidang Usaha ==================================
		 */
		RequestService.doGET(
				'/procurement/master/BidangUsahaServices/getBidangUsahaList')
				.then(function success(data) {
					vm.bidangUsahaList = data;
				}, function error(response) {
				});
		/* ------------------------------------------------------------------------------------------------ */
		/*
		 * ================================= START Mata Uang ==============================================
		 */
		RequestService.doGET('/procurement/master/mata-uang/get-list').then(
				function success(data) 
				{
					vm.mataUangList = data;
				}, function error(response) {});
		/* ------------------------------------------------------------------------------------------------ */

		// /UPLOAD FILE
		var uploadBukti = $scope.uploadBukti = new FileUploader({
			url : $rootScope.uploadBackendAddress,
			method : 'POST'
		});

		uploadBukti.filters
				.push({
					name : 'customFilter',
					fn : function(item /* {File|FileLikeObject} */, options) {
						var valid = (RequestService.uploadFilter(this, item, true,"doc") && (this.queue.length < 10));
						if(!valid){
							$('#uploadBukti').filestyle('clear');
							uploadBukti.queue =[];
							vm.dataPengalaman.pathFile = null;
							vm.downloadFile1 = null;
						}
						
						return valid;
					}
				});
		uploadBukti.onAfterAddingFile = function(fileItem) {
            
        };
        
		uploadBukti.onCompleteItem = function(fileItem, response, status,headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			vm.isUploading = false;
			vm.dataPengalaman.fileSize = fileItem.file.size;
			vm.dataPengalaman.fileName = response.fileName;
			vm.dataPengalaman.pathFile = fileItem.file.name;
			vm.errorMessageBuktiKerjasama = '';
			vm.downloadFile1 = $rootScope.backendAddress + '/file/view/'+ response.fileName;
			RequestService.modalInformation("template.informasi.upload_sukses","success");
			
			
			
		};
		
		uploadBukti.onErrorItem = function(fileItem, response, status, headers) {
			vm.isUploading = false;
			RequestService.modalInformation("template.informasi.upload_gagal","danger");
	    };

		$scope.uploadAja = function() {
			if (uploadBukti.queue.length > 1) {
				uploadBukti.queue.splice(0, uploadBukti.queue.length - 1);
			}else if(uploadBukti.queue.length != 0){
				vm.isUploading = true;
				angular.forEach(uploadBukti.queue, function(item) {
					item.upload();
				});
			}
			
		}

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					vm.dataPengalaman.bidangUsaha = vm.list.bidangUsaha.nama;
					$scope.saveData();
				}
			});

		}
		
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageNamaPekerjaan = '';
			vm.errorMessageLokasiPekerjaan = '';
			vm.errorMessageBidangUsaha = '';
			vm.errorMessageMulaiKerjasama = '';
			vm.errorMessageNilaiKontrak = '';
			vm.errorMessageMataUang = '';
			vm.errorMessageBuktiKerjasama = '';

			if (typeof vm.dataPengalaman.namaPekerjaan == 'undefined'|| vm.dataPengalaman.namaPekerjaan == '') 
			{
				vm.errorMessageNamaPekerjaan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPengalaman.lokasiPekerjaan == 'undefined'|| vm.dataPengalaman.lokasiPekerjaan == '') {
				vm.errorMessageLokasiPekerjaan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.list.bidangUsaha == 'undefined'|| vm.list.bidangUsaha == '') 
			{
				vm.errorMessageBidangUsaha = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPengalaman.mulaiKerjasama == 'undefined'|| vm.dataPengalaman.mulaiKerjasama == '') 
			{
				vm.errorMessageMulaiKerjasama = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPengalaman.nilaiKontrak == 'undefined'|| vm.dataPengalaman.nilaiKontrak == '') 
			{
				vm.errorMessageNilaiKontrak = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.dataPengalaman.mataUang == 'undefined'|| vm.dataPengalaman.mataUang == '') 
			{
				vm.errorMessageMataUang = 'template.error.field_kosong';
				vm.isValid = false;
			}
			
			if (vm.toDo == 'add') {
				if (uploadBukti.queue.length < 1) 
				{
					vm.isValid = false;
					vm.errorMessageBuktiKerjasama = 'template.error.field_kosong';
				} 
				else if ( !Boolean(vm.dataPengalaman.fileName) || !Boolean(vm.dataPengalaman.pathFile)) 
				{	
					
					vm.errorMessageBuktiKerjasama = 'template.error.upload_belum';
					vm.isValid = false;
				}

			} else if (vm.toDo == 'edit') {
				if (!Boolean(vm.dataPengalaman.fileName)|| !Boolean(vm.dataPengalaman.pathFile)) 
				{
					vm.errorMessageBuktiKerjasama = 'template.error.upload_belum';
					vm.isValid = false;
					
				}
			}
			
			if (vm.isUploading) 
			{
				vm.errorMessageBuktiKerjasama = 'template.error.upload_belum_selesai';
				vm.isValid = false;
			}

			return vm.isValid;
		}

		$scope.mulaiKerjasamaStatus = false;
		$scope.mulaiKerjasamaOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.mulaiKerjasamaStatus = true;
		};

		$scope.saveData = function() {
			var url = ""
				var data = {
					
					status : vm.status
				}
				
				var dataPengalamanVendor = {
					id:vm.dataPengalaman.id,
					namaPekerjaan : vm.dataPengalaman.namaPekerjaan,
					lokasiPekerjaan : vm.dataPengalaman.lokasiPekerjaan,
					bidangUsaha : vm.dataPengalaman.bidangUsaha,
					mulaiKerjasama : vm.dataPengalaman.mulaiKerjasama,
					nilaiKontrak : vm.dataPengalaman.nilaiKontrak,
					fileName : vm.dataPengalaman.fileName,
					pathFile : vm.dataPengalaman.pathFile,
					fileSize : vm.dataPengalaman.fileSize,
					tipePengalaman : vm.dataPengalaman.tipePengalaman,
					mataUang: vm.dataPengalaman.mataUang,
					created : vm.dataPengalaman.created
						
					
				}
			if(vm.status== 0 && vm.toDo=="edit"){
				data.pengalamanVendor = dataPengalamanVendor;
				
			}else
			{	
				dataPengalamanVendor.pengalamanVendor=vm.dataPengalaman.pengalamanVendor;
				data.pengalamanVendorDraft = dataPengalamanVendor;
				
			}
			
			if (vm.toDo == "add") {
				url = "/procurement/vendor/PengalamanVendorServices/insert";
			} else if (vm.toDo == "edit") {
				url = "/procurement/vendor/PengalamanVendorServices/update";
			}
			RequestService.doPOSTJSON(url, data)
					.then(function success(data) {
								if (data != undefined) {
									if (data.isValid != undefined) {
										if (data.errorNama != undefined) {
											vm.errorMessageNamaPekerjaan = 'promise.procurement.RegistrasiVendor.datapengalaman.error.nama_sama';
										}
									} else {
										RequestService.modalInformation("template.informasi.simpan_sukses","success");
										$state.go('appvendor.promise.procurement-vendor-datapengalaman');
									}

								}
							},
							function error(response) {
							
								RequestService.modalInformation("template.informasi.gagal","danger");
							});
		}

		
		$scope.back = function() {
			$state.go('appvendor.promise.procurement-vendor-datapengalaman');
		}

	}
	DataPengalamanViewController.$inject = [ 'FileUploader', '$scope', '$http',
			'$rootScope', '$resource', '$location', 'toaster',
			'RequestService', '$state', '$stateParams', '$log' ];
})();