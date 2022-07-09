(function() {
	'use strict';

	angular.module('naut').controller('MasterApprovalViewController',
			MasterApprovalViewController);

	function MasterApprovalViewController($scope, RequestService, $state, $stateParams,$log) {
		
		var vm = this;
		vm.submitted = false;
		vm.approvalMaster={};
		vm.userDTO=[];
		vm.approvalMaster.approval = ($stateParams.approval != undefined) ? $stateParams.approval[0] : {};
		vm.approvalMaster.approvalLevelList = ($stateParams.approval != undefined) ? $stateParams.approval[1] : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.tipe = 'promise.procurement.master.masterapproval.view.pilih_approval';
		vm.approvalTypeList = [ {
			id : 2,
			name : 'User'
		}];
		
		vm.approvalType ={id : 2,name : 'User'};
		vm.organisasi = {organisasi : null};
		
		vm.jenisList = [ {
			id : 0,
			nama : 'Serial'
		}, {
			id : 1,
			nama : 'Pararel'
		} ];
		
		
		$scope.organisasiList = function () {
		       
            RequestService.doGET('/procurement/master/organisasi/get-list-parent').then(function success(data) {
	        	vm.organisasiList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
		$scope.organisasiList();
		
		$scope.unitKerja = function(){
            RequestService.doGET('/procurement/master/organisasi/get-all-list-child-by-parent-id-and-self/' + vm.organisasi.organisasi.id).then(function success(data) {
            	vm.unitKerjaList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		
		$scope.unitKerjaBaru = function(){
			vm.approvalMaster.approval.organisasi = null;
			vm.userDTO=null;
            RequestService.doGET('/procurement/master/organisasi/get-all-list-child-by-parent-id-and-self/' + vm.organisasi.organisasi.id).then(function success(data) {
            	vm.unitKerjaList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		
		$scope.getOrganisasi = function () {
            RequestService.doGET('/procurement/master/organisasi/get-organisasi-afco-by-parentId/'+vm.approvalMaster.approval.organisasi.id).then(function success(data) {
	        	vm.organisasi.organisasi = data;
				$scope.unitKerja();
				$scope.findUser();
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
		
		
		$scope.delValidation = function($event) {
			$scope.formApproval[$event.target.name].$setValidity($event.target.name, true);
		}
		
		
		/*$scope.getApprovalTypeList = function() {

			RequestService.doGET('/procurement/approval/approvatype/all').then(
							function successCallback(data) {
								vm.approvalTypeList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		}
		$scope.getApprovalTypeList();*/
	
		
		$scope.addLevel = function() {
			if(vm.approvalMaster.approvalLevelList.length==0)
			{
				vm.approvalMaster.approvalLevelList=[];
				var approvalLevel = {
						sequence : 1,
						group : null,
						user : null,
						role : null,
						threshold : 0
					}
				vm.approvalMaster.approvalLevelList.push(approvalLevel);
			}
			else {
					var approvalLevel = {
							sequence : vm.approvalMaster.approvalLevelList[vm.approvalMaster.approvalLevelList.length-1].sequence+1,
							group : null,
							user : null,
							role : null,
							threshold : 0
						}
					vm.approvalMaster.approvalLevelList.push(approvalLevel);
			}
		}
		
		$scope.deleteLevel = function(level, userDto) {
			if(vm.approvalMaster.approvalLevelList.length==0)
			{
				$scope.setFormList();
				
			}else{
				vm.approvalMaster.approvalLevelList.splice(level, 1);
				var lastSequence = 1;
				vm.approvalMaster.approvalLevelList
						.forEach(function(approvalLevel, index, array) {
							approvalLevel.sequence = lastSequence;
							lastSequence++;
						});
				}
		}
		$scope.findUser = function() {
			vm.loading = true;

			RequestService.doGET('/procurement/user/get-list-user-by-organisasi/'+vm.approvalMaster.approval.organisasi.id)
					.then(function successCallback(data) {
								vm.userDTOList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		};
		
		
		/*$scope.findJabatan = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/user/get-list-by-organisasi/'+vm.approvalMaster.approval.organisasi.id) 
					.then(function successCallback(data) {
								vm.jabatanDTOList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		};*/
		
		$scope.findJabatanBaru = function() {
			vm.loading = true;
			vm.userDTO=null;
			RequestService.doGET('/procurement/master/jabatan/get-list-by-organisasi-and-additional/'+vm.approvalMaster.approval.organisasi.id) 
					.then(function successCallback(data) {
								vm.userDTOList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		};
		
		$scope.createApprovalLevel = function(index) {
			var user = {};
			var organisasi = {};
			/* perubahan KAI 15/12/2020 */
			user.id = vm.userDTO[index].userId;
			organisasi.id = vm.userDTO[index].organisasiId;
			vm.approvalMaster.approvalLevelList[index].user = user;
			vm.approvalMaster.approvalLevelList[index].group = organisasi;
			for(var a = 0; a < vm.approvalMaster.approvalLevelList.length; a++){
				if(a != index){
					if(angular.equals(vm.userDTO[index].userId, vm.userDTO[a].userId)){
						RequestService.informError('Approval Level tidak boleh sama');
						vm.userDTO[index] = null;
						user.id = vm.userDTO[index]=null;
						organisasi.id = vm.userDTO[index]=null;
						vm.approvalMaster.approvalLevelList[index].user = user;
						vm.approvalMaster.approvalLevelList[index].group = organisasi;
					}
				}
			}
		};
		
		$scope.setFormList = function() {
			$scope.findUser();
			vm.tipe = 'promise.procurement.master.masterapproval.view.role';
		}
		
		$scope.findOrganisasi = function() {
			vm.loading = true;
			var model = "*";

			RequestService.doGET('/procurement/master/organisasi/get-by-name/'+ model)
					.then(
							function successCallback(data) {
								vm.organisasiList = data;
								vm.loading = false;
							},
							function errorCallback(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		};

		
		
		
		if(vm.toDo=='edit'){
			$scope.getOrganisasi();
			vm.tipe = 'User';
			angular.forEach(vm.approvalMaster.approvalLevelList, function(approvalLevel){
				/* perubahan KAI 25/11/2020 */
				var userDTOS= {
					namaPengguna :approvalLevel.user.namaPengguna,
					userId :approvalLevel.user.id,
					organisasiName :approvalLevel.group.nama,
					organisasiId :approvalLevel.group.id
				};
				
				vm.userDTO.push(userDTOS);
			});
			if(vm.approvalMaster.approval.jenis==0){
				vm.jenis={
						id : 0,
						nama : 'Serial'
				}
			}
			else{
				vm.jenis={
						id : 1,
						nama : 'Pararel'
				}
			}
		}
		
		if(vm.toDo=='add'){
			vm.approvalMaster.approvalLevelList=[];
			var approvalLevel = {
					sequence : 1,
					group : null,
					user : null,
					role : null,
					threshold : 0
				}
			vm.approvalMaster.approvalLevelList.push(approvalLevel);
			vm.userDTO=null;
		}
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if (formValid && $scope.validateLevel()) {
				RequestService.modalConfirmation().then(function(result) {
					
					vm.approvalMaster.approval.jenis=vm.jenis.id;
					vm.loading = true;
					$scope.saveData();	
				});
			}
		};

		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorNamaApproval = "";
			vm.errorTypeApproval = "";
			vm.errorJenisApproval = "";
			vm.errorOrganisasiApproval = "";
			vm.errorAfcoApproval = "";
			vm.errorBusinessAreaApproval = "";

			if (typeof vm.approvalMaster.approval.name === 'undefined' || vm.approvalMaster.approval.name == '') {
				vm.errorNamaApproval = 'template.error.field_kosong';
				vm.isValid = false;
			}
			
			if (typeof vm.approvalMaster.approval.approvalType === 'undefined' || vm.approvalMaster.approval.approvalType == '') {
				vm.errorTypeApproval = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.jenis === 'undefined' || vm.jenis == '') {
				vm.errorJenisApproval = 'template.error.field_kosong';	
				vm.isValid = false;
			}
			if (typeof vm.organisasi === 'undefined' || vm.organisasi == '') {
				vm.errorOrganisasiApproval = 'template.error.field_kosong';	
				vm.isValid = false;
			}
			
			return vm.isValid;
		}
		
		$scope.validateLevel=function(){
			vm.approvalMaster.approvalLevelList.forEach(function(approvalLevel, index,
					array) {
				vm.check=true;
					if (approvalLevel.user == null || approvalLevel.user == '') {
						vm.check = false;
					}
				
				if(!vm.check){
					vm.errorLevel="promise.procurement.master.masterapproval.error.level_kosong";
				}
				
			}
			);		
			return vm.check
		}
		

		$scope.saveData = function() {
			vm.submitted = true;
			vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/approval/insert';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/approval/update';
			}
			
			RequestService.doPOSTJSON(vm.url, vm.approvalMaster)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								$scope.formApproval.namaApproval.$setValidity('namaApproval', false);
							}
							vm.loading = false;
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-approval');
						}
	
					}
				}, function error(response) {
					RequestService.informInternalError();
					vm.loading = false;
				});

		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-approval');
		};
		
		
	}
	
	MasterApprovalViewController.$inject = [ '$scope','RequestService', '$state', '$stateParams','$log' ];

})();