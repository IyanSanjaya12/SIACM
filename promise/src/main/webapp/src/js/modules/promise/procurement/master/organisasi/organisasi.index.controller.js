/**=========================================================
 * Module: OrganisasiController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('OrganisasiController', OrganisasiController);

	function OrganisasiController($rootScope, $scope, $resource,RequestService) {
		var vm = this;
		vm.organisasi = {};
		$scope.my_tree_handler = function (branch) {
			vm.statusEdit = false;
			vm.errorNamaOrganisasi = "";
			$scope.output = branch.label;
			vm.organisasi.nama = branch.label;
			vm.organisasi.id = branch.id;
			vm.organisasi.parentId = branch.parentId;
			vm.organisasi.created = new Date(branch.created);

			if (branch && branch.label) {
				$scope.output += '(' + branch.label + ')';
				return $scope.output;
			}
		};

		var tree;
		// This is our API control variable
		$scope.my_tree = tree = {};
		$scope.try_async_load = function () {

			$scope.my_data = [];
			$scope.doing_async = true;

			// Request tree data via $resource
			var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/organisasi/get-all');

			//var remoteTree = $resource('server/treeorganisasi.json');

			return remoteTree.get(function (res) {

				$scope.my_data = res.data;

				$scope.doing_async = false;

				return tree.expand_all();

				// we must return a promise so the plugin 
				// can watch when it's resolved
			}).$promise;
		};
		$scope.try_async_load();

		// Adds a new branch to the tree
		$scope.try_adding_a_branch = function () {
			var b;
			b = tree.get_selected_branch();
			return tree.add_branch(b, {
				label: 'New Branch',
				parentId: b.id
			});
		};

		$scope.cancel = function () {
			$scope.try_async_load();
			vm.statusEdit = null;
			vm.errorNamaOrganisasi = "";
		}
		
		 $scope.save = function() {
				RequestService.modalConfirmation(
						"Apakah anda yakin ingin menyimpan organisasi ?").then(
						function(result) {
							if ($scope.validateForm()) {
								$scope.saveData();
							}
						});
			};
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorNamaOrganisasi = "";
			
			if (typeof vm.organisasi.nama === 'undefined' || vm.organisasi.nama == '') {
				vm.errorNamaOrganisasi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			
			return vm.isValid;
		}
		
		$scope.saveData = function() {
			vm.url = '';
			if (typeof vm.organisasi.id !== 'undefined') {
				vm.url = '/procurement/master/organisasi/edit';
			}else{
				vm.url = '/procurement/master/organisasi/insert';
			}

			RequestService
					.doPOSTJSON(vm.url, vm.organisasi)
					.then(
							function success(data) {
								
										RequestService.informSaveSuccess();
										vm.statusEdit = null;
										$scope.try_async_load();
										vm.loading = false;
									
							}, function error(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});

		}

		
		$scope.del = function () {
            if (typeof vm.organisasi.id !== 'undefined') {
            	
            	RequestService.deleteModalConfirmation().then(function (result) {
    				vm.loading = true;
    				
    				var post={
    						id:vm.organisasi.id
    				};
    				
    				RequestService.doPOST('/procurement/master/organisasi/delete/',post)
    				.then(function success(data) {
    					RequestService.informDeleteSuccess();
    					$scope.try_async_load();
                        $scope.output = undefined;
                        vm.loading = false;
    				}, function error(response) { 
    					RequestService.informInternalError();
    					vm.loading = false;
    			});
    			});
            }
        }

	}

	OrganisasiController.$inject = ['$rootScope', '$scope', '$resource','RequestService'];

})();
