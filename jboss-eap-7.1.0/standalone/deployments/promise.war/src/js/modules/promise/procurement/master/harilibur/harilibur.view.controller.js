(function () {
    'use strict';
    
    angular
    	.module('naut')
    	.controller('HariLiburViewController', HariLiburViewController);

    function HariLiburViewController($scope, $http, $rootScope, $resource, $location, toaster, $state, $stateParams, RequestService, $filter,$log) {
        
    	var vm = this;
        
        vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
        vm.hariLibur = ($stateParams.hariLibur != undefined) ? $stateParams.hariLibur : {};
        vm.submitted = false;
        
        
        
		$scope.delValidation = function($event) {
			$scope.formHariLibur[$event.target.name].$setValidity($event.target.name, true);
		}
		
        
        $scope.save = function(valid) {
			vm.submitted = true;
        	if (valid){
        		RequestService.modalConfirmation()
				.then(function(result) {
				vm.loading = true;
				$scope.saveData();		
				});																			
			}			
		}
        
        $scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageTanggal = '';
			vm.errorMessageNama = '';
			vm.errorMessageDeskripsi = '';

			if (typeof vm.hariLibur.tanggal == 'undefined' || vm.hariLibur.tanggal == '') {
				vm.errorMessageTanggal = 'template.error.field_kosong';
				vm.isValid = false;
			}

			if (typeof vm.hariLibur.nama == 'undefined' || vm.hariLibur.nama == '') {
				vm.errorMessageNama = 'template.error.field_kosong';
				vm.isValid = false;

			}

			if (typeof vm.hariLibur.deskripsi == 'undefined' || vm.hariLibur.deskripsi == '') {
				vm.errorMessageDeskripsi= 'template.error.field_kosong';
				vm.isValid = false;
			}

			return vm.isValid;
		}

        
        vm.clear = function() {
			vm.hariLibur.tanggal = null;
		};
		vm.disabled = function(date, mode) {
			return false;
		};
		
		vm.dateOptions = {
			formatYear : 'yy',
			startingDay : 1
		};
		
		vm.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd' ];
		vm.format = vm.formats[4];

		vm.daterequiredOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.daterequiredOpened = true;
		};
		
		
		/*$scope.getHariLiburObj = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/master/hari-libur/getEditHariLiburById/'+vm.hariLibur.id)
			.then(function success(data) {
				vm.hariLibur = data[0];
				vm.loading = false;
			}, function error(response) {
				vm.loading = false;
			});
		}
		if (vm.toDo == "edit") {
			$scope.getHariLiburObj();
		}*/
		
		$scope.saveData = function(){
			
			var url = ""
			
			if (vm.toDo == "add") {
				url = "/procurement/master/hari-libur/insert";	
			} else if (vm.toDo == "edit") {				
				url = "/procurement/master/hari-libur/update";
			}
			
			RequestService.doPOSTJSON(url, vm.hariLibur)
				.then(function success(data) {
            
					if(data != undefined) {
						if(data.isValid != undefined) {
							if(data.errorNama != undefined) {
								$scope.formHariLibur.namaHariLibur.$setValidity('namaHariLibur', false);
							}
							vm.loading = false;
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-harilibur');
						}
              
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
			
		
		}	
		
		$scope.back = function () {
        	 $state.go('app.promise.procurement-master-harilibur');
        };
        
    }
    
           
    HariLiburViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', '$stateParams', 'RequestService','$filter','$log'];

})();
