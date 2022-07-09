(function () {
    'use strict';

    angular.module('naut').controller('ItemViewController', ItemViewController);

    function ItemViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {
        var vm = this;
        vm.item = ($stateParams.item != undefined) ? $stateParams.item : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
        /*var dataIndexItem;*/
        vm.satuanId = {};
        vm.itemType = {};
        
        if (vm.toDo == "Edit") {
        	vm.itemGroupNama = vm.item.itemGroupId.nama;
        	vm.itemGroupId = vm.item.itemGroupId.id;
		}
        
        /*if (typeof $rootScope.dataIndexItem !== 'undefined') {
            dataIndexItem = $rootScope.dataIndexItem;
        }
        vm.id = dataIndexItem.id;
        vm.nama = dataIndexItem.nama;
        vm.kode = dataIndexItem.kode;
        vm.satuan.selected = dataIndexItem.satuanId;
        vm.tipeItem.selected = dataIndexItem.itemType;
        vm.deskripsi = dataIndexItem.deskripsi;
        vm.output = dataIndexItem.itemGroupId.nama;
        vm.treeId = dataIndexItem.itemGroupId.id;*/

        //tree
        $scope.my_tree_handler = function (branch) {
        	vm.itemGroupNama = branch.label;
        	vm.itemGroupId = branch.id;
            //form.parentId = branch.parentId;
        };

        var tree;
        // This is our API control variable
        $scope.my_tree = tree = {};
        $scope.try_async_load = function () {

            $scope.my_data = [];
            $scope.doing_async = true;

            // Request tree data via $resource
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/item-group/get-all');

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
        	if($scope.validateForm()){
        		RequestService.modalConfirmation().then(function(result) {
        			vm.loading = true;
    				$scope.savedata();
    			});
			}
		}
        
        $scope.validateForm = function () {
        	vm.isValid = true;
        	vm.errorNamaItem = '';
        	vm.errorKodeItem = '';
        	vm.errorSatuanIdItem = '';
        	vm.errorItemTipeItem = '';
        	vm.errorItemGroupItem = '';
        	
            if (typeof vm.item.nama === 'undefined' || vm.item.nama == '') {
                vm.errorNamaItem = 'template.error.field_kosong';
                vm.isValid = false;
            }
            if (typeof vm.item.kode === 'undefined' || vm.item.kode == '') {
                vm.errorKodeItem = 'template.error.field_kosong';
                vm.isValid = false;
            }
            if (typeof vm.item.satuanId === 'undefined'|| vm.item.satuanId == '') {
                vm.errorSatuanIdItem = 'template.error.field_kosong';
                vm.isValid = false;
            }
            if (typeof vm.item.itemType === 'undefined'|| vm.item.itemType == '') {
               vm.errorItemTipeItem = 'template.error.field_kosong';
               vm.isValid = false;
            }
            if (typeof vm.itemGroupNama == 'undefined' || vm.itemGroupNama == '') {
                vm.errorItemGroupItem = 'template.error.field_kosong';
                vm.isValid = false;
            }
            return vm.isValid;
        }
        
        $scope.getSatuanList = function () {
			RequestService.doGET('/procurement/master/satuan/get-list')
				.then(function success(data) {
					vm.satuanList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getSatuanList();
		
        //satuan
       /* $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data, status, headers, config) {
            $scope.satuanList = data;
        }).error(function (data, status, headers, config) {})*/
		
		 $scope.getTipeItemList = function () {
				RequestService.doGET('/procurement/master/item-type/get-list')
					.then(function success(data) {
						vm.tipeItemList = data;
					}, function error(response) {
						RequestService.informInternalError();
				});
			}
		 $scope.getTipeItemList();
        //type item
        /*$http.get($rootScope.backendAddress + '/procurement/master/ItemTypeServices/getItemTypeList').success(function (data, status, headers, config) {
            $scope.tipeItemList = data;
        }).error(function (data, status, headers, config) {})*/
		 
		 $scope.savedata = function () {
				var url = '';
				
				var itemDTO = {};
				itemDTO.item = vm.item;
				itemDTO.itemGroupId = vm.itemGroupId;
				
				if(vm.toDo == "Add"){
					url = '/procurement/master/item/insert';
					
	            }
				else if(vm.toDo == "Edit"){
	            	url = '/procurement/master/item/update';
	            }
				
				RequestService.doPOSTJSON(url, itemDTO)
						.then(function success(data) {
									if (data != undefined) {
										if (data.isValid != undefined) {
											if (data.errorKode != undefined) {
												 vm.errorKodeItem  = 'promise.procurement.master.item.error.kode_sama';
											}
											vm.loading = false;
										} else {
											RequestService.informSaveSuccess();
											$state.go('app.promise.procurement-master-item');
										}
									}
								},
								function error(response) {
									RequestService.informInternalError();
					            	vm.loading = false;
				});
			}
		 
        $scope.back = function () {
        	$state.go('app.promise.procurement-master-item');
        }
    }
    ItemViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state', '$stateParams'];

})();
