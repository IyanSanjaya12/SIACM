(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AdditionalUserViewController', AdditionalUserViewController);
    function AdditionalUserViewController($scope, $http, $rootScope, $modal, $resource, $location, toaster, RequestService, $state, $stateParams, $filter, FileUploader, ModalService) {

        var vm = this;
        vm.additionalUser = ($stateParams.additionalUser != undefined) ? $stateParams.additionalUser : {};
        vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};     
        vm.post = {};
        $rootScope.user = {};
        vm.submitted = false;
        vm.valid = false;
		vm.minDateAwal = new Date();
		vm.dokumen='';
		
        $scope.startDateOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.startDateStatus = true;
		}
		$scope.endDateOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.endDateStatus = true;
		}
		
		if(vm.additionalUser.isActive == undefined || vm.additionalUser.isActive == null || vm.additionalUser.isActive == 1){
			vm.checkedIsActive = false;
			vm.isActive = false;
		}
		if(vm.additionalUser.isActive == 0){
			vm.checkedIsActive = true;
			vm.isActive = true;
		}
		
		$scope.validasiKosong = function(){
			vm.valid = true;
			if (vm.additionalUser.organisasi == null || typeof vm.additionalUser.organisasi == 'null') {
				vm.errorOrganisasi='Harus Diisi';
				vm.valid= false;
			}if (vm.additionalUser.jabatan == null || typeof vm.additionalUser.jabatan == 'null') {
				vm.errorJabatan='Harus Diisi';
				vm.valid= false;
			}if (vm.post.namaPengguna == null || typeof vm.post.namaPengguna == 'null') {
				vm.errorNamaPengguna='Harus Diisi';
				vm.valid= false;
			}if (vm.post.email == null || typeof vm.post.email == 'null') {
				vm.errorEmail='Harus Diisi';
				vm.valid= false;
			}if (vm.additionalUser.startDate == null || typeof vm.additionalUser.startDate == 'null') {
				vm.errorStartDate='Harus Diisi';
				vm.valid= false;
			}if (vm.additionalUser.endDate == null || typeof vm.additionalUser.endDate == 'null') {
				vm.errorEndDate='Harus Diisi';
				vm.valid= false;
			}if(vm.additionalUser.startDate != undefined && vm.additionalUser.endDate != undefined && vm.isActive != true){
				vm.startDateTamp = $filter('date')(vm.additionalUser.startDate, "dd/MM/yyyy");
				vm.endDateTamp = $filter('date')(vm.additionalUser.endDate, "dd/MM/yyyy");
				if(vm.endDateTamp == vm.startDateTamp){
					RequestService.informError('Tanggal Akhir tidak boleh sama dengan Tanggal Awal');
					vm.valid= false;
				}
			}
			
			return vm.valid;
		}
		
		$scope.changeOrganisasiPlh=function(){
			vm.errorOrganisasi="";
			$rootScope.organisasiId = vm.additionalUser.organisasi.id;
			RequestService.doGET('/procurement/master/jabatan/getJabatanListByOrganisasi/'+vm.additionalUser.organisasi.id).then(function success(data) {
				var checkData = [];
				vm.jabatanListTamp = data;
				checkData = vm.jabatanListTamp;
	        	if(checkData.length > 0){
	        		vm.jabatanList = data;
	        	}else{
	        		vm.additionalUser.jabatan = undefined;
	        		vm.jabatanList = undefined;
	        	}
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		$scope.changeJabatan=function(){
			vm.errorJabatan="";
		}
		$scope.changeEmail=function(email){
			vm.errorEmail="";
			vm.post.email = email;
			$rootScope.user.email = email;
		}
		$scope.changeTglAwal=function(){
			vm.errorStartDate="";
		}
		$scope.changeTglAkhir=function(){
			vm.errorEndDate="";
		}
		$scope.nonActiveIsChecked = function(){
			if(vm.checkedIsActive == true || vm.isActive == true){
				vm.checkedIsActive = true;
				vm.additionalUser.isActive = 0; //non-aktif
				vm.additionalUser.endDate = new Date();
				if(vm.additionalUser.startDate > vm.additionalUser.endDate){
					RequestService.informError('Tanggal Awal tidak boleh lebih dari Tanggal Akhir');
					vm.valid = false;
				}
			}if(vm.checkedIsActive == false || vm.isActive == false){
				vm.checkedIsActive = false;
				vm.additionalUser.isActive = 1; //aktif
			}
		}
		$scope.nonActiveIsChecked();
		
		$scope.uploader = new FileUploader({
			url: RequestService.uploadURL(),
			 queueLimit: 1
		});
		
		$scope.uploader.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});
		$scope.uploader.onCompleteItem = function (fileItem, response, status, headers) {
			fileItem.imagesRealName = response.fileName;
		};
		
		if (vm.toDo == "Edit") {
			vm.checkedIsActive = false;
			var item = new FileUploader.FileItem($scope.uploader, {
				size: 0,
				name: vm.additionalUser.notaDinas
			});
			item.isUploading = false;
			item.isReady = false;
			item.progress = 100;
			item.isUploaded = true;
			item.isSuccess = true;
			item.imagesRealName = '';
			$scope.uploader.queue.push(item);
			
			if(vm.additionalUser.isActive == 0){
				vm.checkedIsActive = true;
				vm.isActive = true;
			}
			
			$scope.getUserByUsername = function () {
				RequestService.doGET('/procurement/user/getUserByUsername/'+ vm.additionalUser.nippPlh)
					.then(function success(data) {
						vm.userEmailTamp = data.email;
						vm.post.email	 = vm.userEmailTamp;
					}, function error(response) {
						RequestService.informInternalError();
				});
			}
			$scope.getUserByUsername();
			
			$scope.resend = function () {
				ModalService
				.showModalConfirmation(
						'Apakah anda yakin ingin resend username & password ?')
				.then(function(result) {
					ModalService.showModalInformationBlock();
					RequestService.doGET('/procurement/master/userAdditional/resendMail/'+ vm.additionalUser.nippPlh+'/'+vm.post.email)
					.then(function success(data) {
						if(angular.equals(data.responseStr,"SUCCESS")){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Berhasil Kirim ulang username & password ke email");
						}else{
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Gagal Kirim ulang username & password ke email");
						}
					}, function error(response) {
						RequestService.informInternalError();
					});
				});
			}
			
			vm.post.namaPengguna = vm.additionalUser.user.namaPengguna;
			vm.post.username	 = vm.additionalUser.user.username;
			vm.post.parentUserId = vm.additionalUser.user.parentUserId;
		}
		
		if (vm.toDo == "Add") {
			$scope.getUserAdditionalIdMax = function () {
				RequestService.doGET('/procurement/master/userAdditional/getUserAdditionalMax')
					.then(function success(data) {
						vm.kodePlh = data.kodePlh;
						vm.tahun = data.tahun;
						vm.additionalUser.nippPlh = "ADD."+vm.tahun+"."+ vm.kodePlh;
					}, function error(response) {
						RequestService.informInternalError();
				});
			}
			$scope.getUserAdditionalIdMax();
		}
		
		$scope.back = function () {
			$state.go('app.promise.procurement-master-user-additional');
		}
		
        $scope.getOrganisasiListParentIsNull = function () {
			RequestService.doGET('/procurement/master/organisasi/getOrganisasiListParentIdIsNull')
				.then(function success(data) {
					vm.organisasiListParentIsNull = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getOrganisasiListParentIsNull();
		
		$scope.save = function() {
    		if(vm.post.namaPengguna != null){
    			vm.errorNamaPengguna="";
    		}if(vm.post.email != null){
    			vm.errorEmail="";
    		}
    		
    		angular.forEach($scope.uploader.queue, function (item, indexItem) {
				 vm.dokumen = item.file.name
			});
    		
    		if(vm.dokumen == null || vm.dokumen == ""){
    			$scope.errormessageImage = 'Harus Diisi!';
				vm.valid = false;
    		}
    		if ($scope.uploader.getNotUploadedItems().length > 0) {
				RequestService.modalInformation('Data belum di upload');
				vm.valid = false;
			} 
    		if($scope.validasiKosong()){
    			RequestService.modalConfirmation().then(function(result) {
    				vm.loading = true;
    				$scope.saveData();
    			});
        	}
        }
        
		$scope.saveData = function () {
			var url = '';
			
			vm.additionalUser.notaDinas = vm.dokumen;
			if(vm.toDo == "Add"){
				url = '/procurement/master/userAdditional/insert';
				vm.additionalUser.user = $rootScope.user;
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/userAdditional/update';
            	vm.additionalUser.user.email = $rootScope.user.email;
            }
			RequestService.doPOSTJSON(url, vm.additionalUser)
			.then(function success(data) {
				if( data =="" || data == null) {
	    			RequestService.informError("Error, Generate password minimal 15 menit sekali");
	        	}else {
	        		$state.go('app.promise.procurement-master-user-additional');
	        	} 
			}, function error(response) {
				RequestService.informInternalError();
            	vm.loading = false;
			});
		}
		
		 $scope.searchUserForm = function (size) {
	            var modalInstance = $modal.open({
	                templateUrl: '/user.html',
	                controller: UserSearchModalInstanceCtrl,
	                size: 'lg'
	            });
	            modalInstance.result.then(function () {
	                if (typeof $rootScope.user.id !== 'undefined') {
	                    vm.post.namaPengguna = $rootScope.user.namaPengguna;
	                    vm.post.username = $rootScope.user.username;
	                    vm.post.email = $rootScope.user.email;
	                    vm.post.parentUserId = $rootScope.user.parentUserId;
	                }
	            }, function () {
	            });
	        };
	        
	        var UserSearchModalInstanceCtrl = function ($scope, $rootScope, $http, $modalInstance, ngTableParams, RequestService) {
	            var vm2 = this;
	            $scope.param = {
        			nama : '',
        			pageNo : 1,
        			pageSize : 5,
        			jmlData : {},
        			dataUserList : {},
        			organisasiId : $rootScope.organisasiId
	        	};
	            
	            $scope.UserList = function () {
	            	vm2.loading = true;

	    			RequestService
	    					.doPOST(
	    							'/procurement/master/roleUserServices/getUserListByOrganisasi', $scope.param).then(
	    							function success(data) {
	    								if (data.data.dataUserList != undefined
	    										&& data.data.dataUserList.length > 0) {
	    									$scope.dataUserList = data.data.dataUserList;
	    									$scope.totalItems = data.data.jmlData;
	    								} else {
	    									$scope.dataUserList = [];
	    									$scope.totalItems = 0;
	    								}
	    								vm2.loading = false;
	    							}, function error(response) {
	    								RequestService.informInternalError();
	    								vm2.loading = false;
	    							});
	    		}
	    		$scope.UserList();
	    		
	    		$scope.pilihUser = function (user) {
	                $rootScope.user = user;
	                
	            }
	    		
	            $scope.ok = function () {
	                $modalInstance.close('closed');
	            }

	            $scope.cancel = function () {
	                $modalInstance.dismiss('cancel');
	            }
	            
	            $scope.setPage = function() {
	    			$scope.UserList();
	    		}

	    		$scope.pageChanged = function(maxRecord) {
	    			$scope.param.pageSize = maxRecord;
	    			$scope.UserList();
	    		}
	        }

    }
    AdditionalUserViewController.$inject = ['$scope', '$http', '$rootScope', '$modal', '$resource', '$location', 'toaster', 'RequestService', '$state','$stateParams','$filter','FileUploader','ModalService'];

})();