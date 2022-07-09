(function() {
	'use strict';
	
	angular.module('naut').controller('UserViewController', UserViewController);
	
	function UserViewController($scope, $state, $stateParams, RequestService,$log) {
		var vm = this;
		vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.user= ($stateParams.user != undefined) ? $stateParams.user : {};
		vm.userDTO = {
			user:($stateParams.user != undefined) ? $stateParams.user : {},
			roleList : [],
			roleUser:($stateParams.roleUser != undefined) ? $stateParams.roleUser : {}
		}
		vm.submitted = false;
				
		vm.status = [ {
			id : 0,
			nama : 'Aktif'
		}, {
			id : 1,
			nama : 'Terkunci'
		} ];
		
		if (vm.toDo == "edit") {
        	RequestService.doGET(
					'/procurement/master/roleUserServices/getListByUserId/'+vm.userDTO.user.id)
					.then(function success(data) {
						vm.data=data;
						vm.userDTO.roleUser=data;
						vm.organisasi = data[0].organisasi.nama;
						vm.userDTO.user = data[0].user;
						
						angular.forEach(vm.userDTO.roleUser, function(value) {
							if (value.role.appCode == 'PM') {
								vm.loginPm = true;
								vm.rolePm = value.role;
							
							}

							else if (value.role.appCode == 'CM') {
								vm.loginCm = true;
								vm.roleCm = value.role;
								
							}
							
							if ((!angular.isUndefined(value.organisasi)) && value.organisasi != null) {
								vm.userDTO.roleUser[0].organisasi = {
									id : value.organisasi.id,
									label : value.organisasi.nama,
									parentId : value.organisasi.parentId
								};
							}
							vm.userDTO.user.password ='';
							vm.rePassword='';
							$scope.loading = false;
							
							
						})
						
						vm.loading = false;
					}, function error(response) {
						vm.loading = false;
						RequestService.informInternalError();
					});
	
		}
		
		$scope.delValidation = function($event) {
			$scope.userForm[$event.target.name].$setValidity($event.target.name, true);
		}
		
		$scope.getAllList = function(){
			RequestService.doGET('/procurement/user/get-all-list')
			.then(function success(data) {
				vm.rolePmList= data[0].rolePmList;
				vm.roleCmList= data[0].roleCmList;
				vm.organisasiString= JSON.parse(data[0].organisasiString).data;
				
			
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
			
		}
		$scope.getAllList();
		
		$scope.validateEmail = function(email){
			  var validate = /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i;
			  return validate.test(email);
		}
		
		/*$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorUsername= '';
	         vm.errorNamaPengguna= '';
	         vm.errorPassword='';
	         vm.errorRepassword='';
	         vm.errorEmail='';
	         vm.errorOrganisasi='';
	         vm.errorTerkunci='';
	         vm.errorRole='';         
	         
	          Validation 
	         if (typeof vm.userDTO.user.username == 'undefined' || vm.userDTO.user.username == '') {
	                vm.errorUsername= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         if (typeof vm.userDTO.user.namaPengguna == 'undefined' || vm.userDTO.user.namaPengguna == '') {
	                vm.errorNamaPengguna= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         if (typeof vm.userDTO.user.password == 'undefined' || vm.userDTO.user.password == '') {
	                vm.errorPassword= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         if (typeof vm.userDTO.user.password && (angular.equals(vm.userDTO.user.password, vm.rePassword) == false)) {
	        	 vm.errorRepassword= 'promise.procurement.master.user.error.password_beda';
	             vm.isValid = false;
	         }
					
	         if (typeof vm.rePassword== 'undefined' || vm.rePassword == '') {
	                vm.errorRepassword= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }

	         if (typeof vm.userDTO.user.email== 'undefined' || vm.userDTO.user.email== '') {
	                vm.errorEmail= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	     	 if (vm.userDTO.roleUser[0].organisasi=='undefined' || vm.userDTO.roleUser[0].organisasi== null) {
	                vm.errorOrganisasi= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         if (vm.loginPm == undefined && vm.loginCm == undefined || vm.loginPm == false && vm.loginCm == false) {
	                vm.errorRole= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	        
	         if (vm.loginPm == undefined && vm.loginCm == false ||vm.loginPm == false && vm.loginCm == undefined) {
	                vm.errorRole= 'template.error.field_kosong';
	                vm.isValid =  false;
	            }
	         if (vm.loginPm == true) {
	        	 if(vm.rolePm =='undefined'|| vm.rolePm =='' || vm.rolePm == null){
	                vm.errorRole= 'template.error.field_kosong';
	                vm.isValid =  false;
	        	 }
	            }
	         if (vm.loginCm == true) {
	        	 if(vm.roleCm =='undefined'|| vm.roleCm =='' || vm.roleCm == null){
	                vm.errorRole= 'template.error.field_kosong';
	                vm.isValid =  false;
	        	 }
	            }
	         
	         if (typeof vm.userDTO.user.terkunci=='undefined'){
	        	 vm.errorTerkunci= 'template.error.field_kosong';
	        	 vm.isValid = false;
	         	} 
	         
	         if ($scope.validateEmail(vm.userDTO.user.email)==false){
	        	 vm.errorEmail= 'promise.procurement.master.user.error.email_salah';
	        	 vm.isValid =  false;
	         	}
	        
	         return vm.isValid;
	         
	        }
		*/
		$scope.save = function(formValid){
			vm.submitted = true;
			if(formValid){
			RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();
			});
			}
			
		};
		
		
		$scope.saveData = function(){
			if (vm.loginPm==true){
				if (vm.rolePm != undefined || vm.rolePm != null ){
					vm.userDTO.roleList.push(vm.rolePm);
				}
			} else
				if (vm.loginPm==false){
					vm.rolePm==null;
				}
			
			if (vm.loginCm==true){
			if (vm.roleCm != undefined || vm.roleCm != null){
					vm.userDTO.roleList.push(vm.roleCm);
			   }
			}else
				if (vm.loginCm==false){
					vm.roleCm==null;
				}
			
			
			vm.userDTO.roleUser[0].organisasi.nama=vm.userDTO.roleUser[0].organisasi.label;
			
			var userMasterDTO = {};
			
			userMasterDTO.roleList = vm.userDTO.roleList;
			userMasterDTO.user = vm.userDTO.user;
			userMasterDTO.organisasiId = vm.userDTO.roleUser[0].organisasi.id;
			
			var url='';
			if (vm.toDo=="add") {
				
				url = '/procurement/user/insert';
			} 
			if (vm.toDo=="edit") {
				
				url = '/procurement/user/update';
			}
			
			RequestService.doPOSTJSON(url, userMasterDTO)
		    	.then(function success(data) {
		    	
			        if(data != undefined) {
		        		if(data.isValid != undefined) {
		        			if(data.errorUsername != undefined) {
		        				vm.errorUsername = data.errorUsername;
		        			}
		        			if(data.errorEmail != undefined) {
		        				vm.errorEmail = data.errorEmail;
		        			}
		                } else {
		                	RequestService.informSaveSuccess();
				        	$state.go('app.promise.procurement-master-user');
		                }
		        	}  
		        }, 
		        function error(response) {
		        	RequestService.informInternalError();
	            	vm.loading = false;
		        });				
			
		}	
		
		$scope.generate = function(){
			RequestService.modalConfirmation("Apakah password akan di generate dan di kirim ke email ?").then(function(result) {
				
			
			var userMasterDTO = {};
			userMasterDTO.user = vm.userDTO.user;
			RequestService.doPOSTJSON('/procurement/user/generate', userMasterDTO)
	    	.then(function success(data) {	
	    		  if( data =="" || data == null) {

	    			   RequestService.informError("Error, Generate password minimal 15 menit sekali");
		        	} else {
		        		
		        		RequestService.informSaveSuccess("Success generate password");
			        	$state.go('app.promise.procurement-master-user');
		        	}  
	    	}, 
	        function error(response) {
	        	RequestService.informInternalError();
            	vm.loading = false;
	        });	
			
			});
			
		};
		
		$scope.back = function () {
        	$state.go('app.promise.procurement-master-user');
        }

	}
	UserViewController.$inject = [ '$scope', '$state', '$stateParams', 'RequestService','$log' ];

})();
