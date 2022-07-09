/**=========================================================
 * Module: BidangUsahaController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BidangUsahaController', BidangUsahaController);

    function BidangUsahaController($http, $rootScope, $scope, $resource, $location, $timeout, $modal, toaster, RequestService) {
        var vm = this;
        vm.bidangUsaha = {};
        
        $scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if($scope.validateForm()){
					$scope.savedata();
				}
			});
		}
		
		$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorNamaBidangUsaha = '';
	         
	         if (typeof vm.bidangUsaha.nama === 'undefined' || vm.bidangUsaha.nama == '') {
                vm.errorNamaBidangUsaha = 'template.error.field_kosong';
                vm.isValid = false;
	         }
	         
	         return vm.isValid;
	    }
		
		$scope.savedata = function () {
			$scope.loading = true;
			var url = '';
			
			if(typeof vm.bidangUsaha.id !== 'undefined') {
				url = '/procurement/master/bidang-usaha/update';
            } else {
            	url = '/procurement/master/bidang-usaha/insert';
            }
			
			RequestService.doPOSTJSON(url, vm.bidangUsaha)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorNama != undefined) {
								vm.errorNamaBidangUsaha = 'promise.procurement.master.bidang_usaha.error.nama_sama';
							}
						} else {
							RequestService.informSaveSuccess();
							$scope.try_async_load();
							$scope.statusEdit = null;
							$scope.loading = false;
						}
					}
				}, function error(response) {
					RequestService.informInternalError();
					$scope.loading = false;
				});
		}
		
		$scope.del = function (bidangUsahaId) {
			var url = '/procurement/master/bidang-usaha/delete';
			var data = {};
			
			data.id = bidangUsahaId;
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading = true;
	            RequestService.doPOST(url,data).success(function (data) {
	            	RequestService.informDeleteSuccess();
			       	$scope.try_async_load();
			       	$scope.output = undefined;
			       	$scope.statusEdit = null;
			       	$scope.loading = false;
			    }).error(function(data, status, headers, config) {
        			RequestService.informInternalError();
        			vm.loading = false;
				})
	        });
		}

        $scope.my_tree_handler = function (branch) {
			$scope.statusEdit = false;
            /*console.log("branch");
            console.log(branch);*/
			$scope.output = branch.label;
			vm.bidangUsaha.nama = branch.label;
			vm.bidangUsaha.id = branch.id;
			vm.bidangUsaha.parentId = branch.parentId;
			vm.bidangUsaha.created = new Date(branch.Created);

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
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/bidang-usaha/get-all');

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

        $scope.btnBatal = function () {
            $scope.try_async_load();
            $scope.statusEdit = null;
            vm.errorNamaBidangUsaha = '';
        }

        var modalInstanceController = function ($scope, $http, $modalInstance, $resource) {
        	
        	$scope.save = function() {
    			if($scope.validateForm()){
    				$scope.savedata();
    			}
    		}
        	
        	$scope.validateForm = function () {
	   	        vm.isValid = true;
	   	        vm.errorNamaBidangUsaha = '';
	   	         
	   	        if (typeof $scope.nama === 'undefined' || $scope.nama == '') {
	   	        	$scope.errorNamaBidangUsaha = 'template.error.field_kosong';
	   	            vm.isValid = false;
	   	        }
	   	        
	   	        return vm.isValid;
        	}
        	
            $scope.savedata = function () {
	            $scope.bidangUsaha = {};
	            $scope.bidangUsaha.parentId = 0;
	            $scope.bidangUsaha.nama = $scope.nama;
	            var url = '/procurement/master/bidang-usaha/insert';
				RequestService.doPOSTJSON(url, $scope.bidangUsaha)
					.then(function success(data) {
						if (data != undefined) {
							if (data.isValid != undefined) {
								if (data.errorNama != undefined) {
									$scope.errorNamaBidangUsaha = 'promise.procurement.master.bidang_usaha.error.nama_sama';
									vm.loading = false;
								}
							} else {
								RequestService.informSaveSuccess();
								$modalInstance.close();
							}
						}
					},
					function error(response) {
						RequestService.informInternalError();
						$scope.loading = false;
					});
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
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

    BidangUsahaController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$timeout', '$modal', 'toaster', 'RequestService'];

})();
