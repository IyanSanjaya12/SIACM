/**=========================================================
 * Module: ItemGroupController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ItemGroupController', ItemGroupController);

    function ItemGroupController($http, $rootScope, $scope, $resource, $location, $timeout, $modal, toaster,RequestService) {
        var vm = this;
        vm.itemGroup={};
  
        $scope.my_tree_handler = function (branch) {
            vm.statusEdit = false;
            vm.errorNamaItemGroup = "";
			vm.errorDescItemGroup = "";
            $scope.output = branch.label;
            vm.itemGroup.nama = branch.label;
            vm.itemGroup.id = branch.id;
            vm.itemGroup.deskripsi = branch.deskripsi;
            vm.itemGroup.parentId = branch.parentId;
            vm.itemGroup.created=  new Date(branch.created);

            if (branch && branch.label) {
                $scope.output += ' (' + branch.deskripsi + ')';
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
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/item-group/get-all');
            
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
        
        $scope.save = function() {
			RequestService.modalConfirmation(
					"Apakah anda yakin ingin menyimpan Item Group ?").then(
					function(result) {
						if ($scope.validateForm()) {
							$scope.saveData();
						}
					});
		};
        
        $scope.validateForm = function() {
			vm.isValid = true;
			vm.errorNamaItemGroup = "";
			vm.errorDescItemGroup = "";
			

			if (typeof vm.itemGroup.nama === 'undefined' || vm.itemGroup.nama == '') {
				vm.errorNamaItemGroup = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.itemGroup.deskripsi === 'undefined' || vm.itemGroup.deskripsi == '') {
				vm.errorDescItemGroup = 'template.error.field_kosong';
				vm.isValid = false;
			}
			return vm.isValid;
		}
        
		$scope.saveData = function() {
			vm.url = '';
			if (typeof vm.itemGroup.id !== 'undefined') {
				vm.url = '/procurement/master/item-group/update';
			}else{
				vm.url = '/procurement/master/item-group/insert';
			}

			RequestService
					.doPOSTJSON(vm.url, vm.itemGroup)
					.then(
							function success(data) {
								if (data != undefined) {
									if (data.isValid != undefined) {
										if (data.errorNama != undefined) {
											vm.errorNamaItemGroup = 'promise.procurement.master.itemgroup.error.nama_sama';
										}
										vm.loading = false;
									} else {
										RequestService.informSaveSuccess();
										vm.statusEdit = null;
										$scope.try_async_load();
										vm.loading = false;
									}

								}
							}, function error(response) {
								RequestService.informInternalError();
				            	vm.loading = false;
							});

		}

        $scope.cancel = function () {
            $scope.try_async_load();
            $scope.output = undefined;
            vm.statusEdit = null;
            vm.errorNamaItemGroup = "";
			vm.errorDescItemGroup = "";
        }
        

        $scope.del = function () {
            if (typeof vm.itemGroup.id !== 'undefined') {
            	
            	RequestService.deleteModalConfirmation().then(function (result) {
    				vm.loading = true;
    				
    				var post={
    						id:vm.itemGroup.id
    				};
    				
    				RequestService.doPOST('/procurement/master/item-group/delete/',post)
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

        var modalInstanceController = function ($scope, $http, $modalInstance, $resource) {
        	
        	$scope.save = function() {
    			RequestService.modalConfirmation(
    					"Apakah anda yakin ingin menyimpan Item Group ?").then(
    					function(result) {
    						if ($scope.validateForm()) {
    							$scope.saveData();
    						}
    					});
    		};
            
            $scope.validateForm = function() {
    			$scope.isValid = true;
    			$scope.errorNamaItemGroup = "";
    			$scope.errorDescItemGroup = "";
    			

    			if (typeof $scope.nama === 'undefined' || $scope.nama == '') {
    				$scope.errorNamaItemGroup = 'template.error.field_kosong';
    				$scope.isValid = false;
    			}
    			if (typeof $scope.deskripsi === 'undefined' || $scope.deskripsi == '') {
    				$scope.errorDescItemGroup = 'template.error.field_kosong';
    				$scope.isValid = false;
    			}
    			return $scope.isValid;
    		}
        	
            $scope.saveData = function () {
            			
            	
                    	var modalForm = {
                                parentId: 0,
                                nama: $scope.nama,
                                deskripsi: $scope.deskripsi
                            }
                            $scope.loading = true;
                    	RequestService
    					.doPOSTJSON('/procurement/master/item-group/insert',modalForm)
    					.then(
    							function success(data) {
    								if (data != undefined) {
    									if (data.isValid != undefined) {
    										if (data.errorNama != undefined) {
    											$scope.errorNamaItemGroup = "promise.procurement.master.itemgroup.error.nama_sama";
    											$scope.loading = false;
    										}
    									} else {
    										RequestService.informSaveSuccess();
    										$modalInstance.close();
    									}

    								}
    							}, function error(response) {
    								$scope.loading = false;
    								RequestService.informInternalError();
    							});
                    	
            
                    }
                	
               

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }
        }

        modalInstanceController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];


        $scope.modalsTambah = function () {
            var modalInstance = $modal.open({
                templateUrl: '/tambah.html',
                controller: modalInstanceController
            });
            modalInstance.result.then(function () {
                $scope.try_async_load();
            });
        }
    }

    ItemGroupController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$timeout', '$modal', 'toaster','RequestService'];

})();
