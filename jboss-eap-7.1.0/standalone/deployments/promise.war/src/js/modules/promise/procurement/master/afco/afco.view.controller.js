(function () {
    'use strict';

    angular.module('naut').controller('AfcoViewController', AfcoViewController);

    function AfcoViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {
    	
        var vm = this;
        //vm.organisasi = {};
        vm.afco = ($stateParams.afco != undefined) ? $stateParams.afco : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
	
		
		if (vm.toDo == "Edit") {
	        vm.organisasiNama = vm.afco.organisasi.nama;
	        vm.organisasiId = vm.afco.organisasi.id;
		}
		
        //tree
        $scope.my_tree_handler = function (branch) {
            vm.organisasiNama = branch.label;
            vm.organisasiId = branch.id;
            //form.parentId = branch.parentId;
        };

        var tree;
        // This is our API control variable
        $scope.my_tree = tree = {};
        $scope.try_async_load = function () {

            $scope.my_data = [];
            $scope.doing_async = true;

            // Request tree data via $resource
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/organisasi/get-all');

            //var

            return remoteTree.get(function (res) {

                $scope.my_data = res.data;

                $scope.doing_async = false;

                return tree.expand_all();

                // we must return a promise so the plugin 
                // can watch when it's resolved
            }).$promise;
        };
        $scope.try_async_load();
        
        $scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if($scope.validateForm()){
					$scope.savedata();
				}
			});
		}
        
        $scope.validateForm = function () {
        	vm.isValid = true;
	        var checkCompany = vm.afco.companyName;
	        var checkName = vm.afco.name;
	        vm.errorNamaPerusahaan = '';
	        vm.errorNamaPIC = '';
	        vm.errorDepartemenPengadaan = '';
	        vm.errorSingkatan = '';
	         
	        if (typeof checkCompany === 'undefined' || checkCompany == '') {
	        	vm.errorNamaPerusahaan = 'template.error.field_kosong';
	            vm.isValid = false;
	        } else {
	        	if (checkCompany.length <= 3) {
	        		vm.errorNamaPerusahaan = 'promise.procurement.master.afco.error.nama_perusahaan_pendek';
		            vm.isValid = false;
		        }
	        }
	        
	        if (typeof checkName === 'undefined' || checkName == '') {
	        	vm.errorNamaPIC = 'template.error.field_kosong';
	            vm.isValid = false;
	        } else {
            	if (checkName.length <= 3) {
	                vm.errorNamaPIC = 'promise.procurement.master.afco.error.nama_pic_pendek';
	                vm.isValid = false;
	            }
            }
	         
	        if (typeof vm.organisasiNama === 'undefined' || vm.organisasiNama == '') {
                vm.errorDepartemenPengadaan = 'template.error.field_kosong';
                vm.isValid = false;
	        }
	        
	        if (typeof vm.afco.singkatan === 'undefined' || vm.afco.singkatan == '') {
                vm.errorSingkatan = 'template.error.field_kosong';
                vm.isValid = false;
	        }
	         
	        return vm.isValid;
	    }
        
        $scope.savedata = function () {
			var url = '';
			
			var afcoDTO = {};
			afcoDTO.afco = vm.afco;
			afcoDTO.organisasiId = vm.organisasiId;
			
			if(vm.toDo == "Add"){
				url = '/procurement/master/AfcoServices/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/master/AfcoServices/update';
            }
			
			RequestService.doPOSTJSON(url, afcoDTO)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							/*if (data.errorMenuNama != undefined) {
								vm.errorMenuNama = data.errorMenuNama;
							}*/
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-afco');
						}
					}
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
				});
		}
		
		$scope.back = function () {
			$state.go('app.promise.procurement-master-afco');
        }
        
    }
    AfcoViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state','$stateParams'];

})();
