/**=========================================================
 * Module: EditRoleJabatanController.js
 * Controller for Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('RoleJabatanViewController', RoleJabatanViewController);
    function RoleJabatanViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {

        var vm = this;
        vm.roleJabatan = ($stateParams.roleJabatan != undefined) ? $stateParams.roleJabatan : {};
        vm.roleJabatanCm = ($stateParams.roleJabatan != undefined) ? $stateParams.roleJabatan : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};        
        vm.submitted = false;
        $scope.showRoleCatalog = false;
        $scope.showRoleCm = false;  
		vm.valid = false;
		vm.errorRoleCm="";
		vm.errorRolePm="";
		vm.errorjabatan="";
		
		if (vm.toDo == "Edit") {
			if(vm.roleJabatan.role.appCode == 'PM'){
				$scope.showRoleCatalog = true;
				vm.role = vm.roleJabatan.role;
				
			}
			else if (vm.roleJabatan.role.appCode == 'CM') {
				$scope.showRoleCm = true;
				vm.role = vm.roleJabatan.role;
					
			}
		}
		
		$scope.getRoleListPm = function () {
			RequestService.doGET('/procurement/master/role/getRoleListPm')
				.then(function success(data) {
					vm.roleListPm = data;
					$scope.checkBoxValidCatalog();
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getRoleListPm();
		
		$scope.getRoleListCm = function () {
			RequestService.doGET('/procurement/master/role/getRoleListCm')
				.then(function success(data) {
					vm.roleListCm = data;
					$scope.checkBoxValidCm();
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getRoleListCm();
		
		$scope.getOrganisasiListParentIsNull = function () {
			RequestService.doGET('/procurement/master/organisasi/getOrganisasiListParentIdIsNull')
				.then(function success(data) {
					vm.organisasiListParentIsNull = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getOrganisasiListParentIsNull();
		
		$scope.changeOrganisasi=function(){
			vm.errorOrganisasi="";
			RequestService.doGET('/procurement/master/jabatan/get-not-registered-list/'+vm.roleJabatan.jabatan.organisasi.id).then(function success(data) {
				var checkData = [];
				vm.jabatanListTamp = data;
				checkData = vm.jabatanListTamp;
	        	if(checkData.length > 0){
	        		vm.jabatanList = data;
	        	}else{
	        		vm.roleJabatan.jabatan = undefined;
	        		vm.jabatanList = undefined;
	        	}
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
		}
		 
		$scope.validasiKosong=function(role,roleCm){
			var valid= true;
			if ((roleCm == null || typeof roleCm == 'null') && $scope.showRoleCm == true) {
				vm.errorRoleCm= 'Role CM Harus Diisi';
				valid= false;
			}
			if (role.jabatan == null || typeof role.jabatan == 'null') {
				vm.errorJabatan='Jabatan Harus Diisi';
				valid= false;
			}
			if ((role.role == null || typeof role.role == 'null') && $scope.showRoleCatalog == true) {
				vm.errorRolePm='Role Catalog Harus Diisi';
				valid= false;
			}
			
			return valid;
		}
		
        $scope.save = function(role,roleCm) {
        	if($scope.showRoleCatalog == false && $scope.showRoleCm == false){
        		RequestService.informError('Mohon Pilih salahsatu Role !');
        	}else{
        		if($scope.validasiKosong(role,roleCm)){
        			RequestService.modalConfirmation().then(function(result) {
        				vm.loading = true;
        				$scope.saveData();
        			});
        		}
        	}
        }
        
        $scope.delValidation = function($event) {
        	$scope.formRoleJabatan[$event.target.name].$setValidity($event.target.name, true);        		
		}
      
		$scope.saveData = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/master/role-jabatan/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/role-jabatan/update';
            }
			if($scope.showRoleCatalog == true){
				RequestService.doPOSTJSON(url, vm.roleJabatan)
				.then(function success(data) {
					$state.go('app.promise.procurement-master-rolejabatan');
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
			}
			if($scope.showRoleCm == true){
				vm.roleJabatanCm.jabatan=vm.roleJabatan.jabatan;
				RequestService.doPOSTJSON(url,  vm.roleJabatanCm)
				.then(function success(data) {
					$state.go('app.promise.procurement-master-rolejabatan');
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
			}
			
		}
		
		$scope.checkBoxValidCatalog = function () {
			if($scope.showRoleCatalog != true){
				vm.errorRolePm="";
				if(vm.toDo != "Edit"){
					vm.roleJabatan.role = undefined;
				}
			}else{
				$scope.showRoleCatalog = true;
			}			
		}
		
		$scope.checkBoxValidCm = function () {
			if($scope.showRoleCm != true){
				vm.errorRoleCm="";
				if(vm.toDo != "Edit"){
					vm.roleJabatanCm.role = undefined;
				}
			}else{
				$scope.showRoleCm = true;
			}			
		}
		
		$scope.back = function () {
			$state.go('app.promise.procurement-master-rolejabatan');
        }
		
		$scope.changeMandatoryJab=function(){
			vm.errorJabatan="";
		}
		
		$scope.changeMandatoryPm=function(){
			vm.errorRolePm="";
		}
		
		$scope.changeMandatoryCm=function(){
			vm.errorRoleCm="";
		}
		

    }
    RoleJabatanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state','$stateParams'];

})();