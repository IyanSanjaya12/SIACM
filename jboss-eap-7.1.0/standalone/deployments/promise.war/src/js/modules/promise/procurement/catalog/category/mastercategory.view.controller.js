'use strict';

/* Controllers */

angular
.module('naut')
.controller('MasterCategoryViewController', ['$log','RequestService', '$state', '$stateParams', '$http', '$rootScope', '$scope', function($log,RequestService, $state, $stateParams, $http, $rootScope, $scope) {
	var vm = this;
	vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
	
	vm.category={};
	if (vm.toDo == "edit") {
	vm.category = ($stateParams.category != undefined) ? $stateParams.category : {};
	}
	if (vm.toDo == "addSub") {
		vm.category.parentCategory=($stateParams.category != undefined) ? $stateParams.category : {};
	}

	
			$scope.save = function() {
			
					RequestService.modalConfirmation()
					.then(function(result) {
						if ($scope.validateForm()){
							$scope.saveData();																				
						}
			});
		
		 $scope.validateForm = function() {
				vm.isValid = true;
				vm.errorMessageDeskripsi = '';
				vm.errorMessageAdmin = '';
				vm.errorMessageTranslateId = '';
				vm.errorMessageTranslateEn = '';
				
				if (typeof vm.category.description == 'undefined' || vm.category.description == '') {
					vm.errorMessageDeskripsi= 'template.error.field_kosong';
					vm.isValid = false;
				}
				if (typeof vm.category.adminLabel == 'undefined' || vm.category.adminLabel == '') {
					vm.errorMessageAdmin = 'template.error.field_kosong';
					vm.isValid = false;
				}

				if (typeof vm.category.translateInd == 'undefined' || vm.category.translateInd == '') {
					vm.errorMessageTranslateId = 'template.error.field_kosong';
					vm.isValid = false;

				}
				if (typeof vm.category.translateEng == 'undefined' || vm.category.translateEng == '') {
					vm.errorMessageTranslateIn = 'template.error.field_kosong';
					vm.isValid = false;

				}
				return vm.isValid;
			}	

	}
			$scope.saveData = function(){
				
				var url = ""
				
				if (vm.toDo == "add") {
					url = "/procurement/catalog/CategoryServices/insert";
				} else if (vm.toDo == "edit") {
					url = "/procurement/catalog/CategoryServices/update";
				}
				
				else if (vm.toDo == "addSub") {
					vm.loading = true;
					RequestService.doGET('/procurement/catalog/CategoryServices/getCategoryByNama/'+ vm.category.translateInd)
						.then(function success(data) {
							vm.loading = false;
						}, function error(response) {
							vm.loading = false;
						});
					url = "/procurement/catalog/CategoryServices/insert";
				}
					
				RequestService.doPOSTJSON(url, vm.category).then(function success(data) {
	            	if(data != undefined) {
	            		if(data.isValid != undefined) {
		            		if(data.errorTranslateId != undefined) {
		            			vm.errorMessageTranslateId = 'promise.procurement.master.mastercategory.error.nama_sama';
		            		}
	            		} else {
	            			RequestService.informSaveSuccess();
	    	            	$state.go('app.promise.procurement-category');
	            		}	
	            	}
	            }, 
	            function error(response) {
	            	$log.info("proses gagal");
	            	RequestService.informError("Terjadi Kesalahan Pada System");
	            });
			}
	
	$scope.back = function() {
		$state.go('app.promise.procurement-category');
	}
	
}]);