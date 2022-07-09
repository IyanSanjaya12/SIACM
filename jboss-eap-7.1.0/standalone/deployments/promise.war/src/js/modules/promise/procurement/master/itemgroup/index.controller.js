/**=========================================================
 * Module: ItemGroupController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ItemGroupController', ItemGroupController);

    function ItemGroupController($http, $rootScope, $scope, $resource, $location, $timeout, $modal, toaster) {
        var form = this;

        $scope.my_tree_handler = function (branch) {
            $scope.statusEdit = false;
            //console.log("branch");
            //console.log(branch);
            $scope.output = branch.label;
            form.nama = branch.label;
            form.id = branch.id;
            form.deskripsi = branch.deskripsi;
            form.parentId = branch.parentId;

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

        $scope.btnBatal = function () {
            $scope.try_async_load();
            $scope.statusEdit = false;
        }
        
        /*$scope.checkItemGroup = function (nama) {
        	
        	$http.get($rootScope.backendAddress + '/procurement/master/item-group/getItemGroupByNama/'+ nama , {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
            	
            	if (data.nama == undefined) {
            		var namaItemGroup = data.nama;
            	} else {
            		namaItemGroup =  data.nama.toUpperCase();
            	}
            	
            	if (nama.toUpperCase() == namaItemGroup && form.id == undefined) {
            		alert("Nama item group tidak boleh sama.");
                } else {
            		var url = "";
                    if (typeof form.id !== 'undefined') {
                        url = $rootScope.backendAddress + '/procurement/master/item-group/updateItemGroup';
                    } else {
                        url = $rootScope.backendAddress + '/procurement/master/item-group/insertItemGroup';
                    }
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: form
                    }).success(function (data, status, headers, config) {
                        toaster.pop('success', 'Group Item', 'Simpan ' + data.nama + ', berhasil.');
                        $scope.try_async_load();
                        $scope.loading = false;
                    });
            	}
            	
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        	
        }*/

        $scope.btnSimpan = function () {
            console.log('SIMPAN OBJ : ' + JSON.stringify(form));

            if (typeof form.nama === 'undefined' && form.nama == '' && form.nama == null) {
                alert("Nama Group Item tidak boleh kosong.");
            } else if (typeof form.deskripsi === 'undefined' && form.deskripsi == '' && form.deskripsi == null) {
                alert("Deskripsi Group Item tidak boleh kosong.");
            } else {
            	
            	$http.get($rootScope.backendAddress + '/procurement/master/item-group/get-item-group-by-nama/'+ form.nama , {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                	
                	if (data.nama == undefined) {
                		var namaItemGroup = data.nama;
                	} else {
                		namaItemGroup =  data.nama.toUpperCase();
                	}
                	
           
                		var url = "";
                        if (typeof form.id !== 'undefined') {
                        	if (form.nama.toUpperCase() == namaItemGroup ) {
                        		
                        		if (form.id == data.id) {
                        			url = $rootScope.backendAddress + '/procurement/master/item-group/updateItemGroup';
                            		
                        		} else {
                        			alert("Nama item group tidak boleh sama.");
                        		}
                        		

                        	} else {
                        		url = $rootScope.backendAddress + '/procurement/master/item-group/updateItemGroup';
                        	}
                        	
                            
                        } else {
                        	if (form.nama.toUpperCase() == namaItemGroup) {
                        		alert("Nama item group tidak boleh sama.");
                            } else {
                            	url = $rootScope.backendAddress + '/procurement/master/item-group/insertItemGroup';
                            }
                            
                        }
                        
                        $http({
                            method: 'POST',
                            url: url,
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: form
                        }).success(function (data, status, headers, config) {
                            toaster.pop('success', 'Group Item', 'Simpan ' + data.nama + ', berhasil.');
                            $scope.try_async_load();
                            $scope.loading = false;
                        });
                	
                	
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                })
            }
        };

        $scope.btnHapus = function () {
            if (typeof form.id !== 'undefined') {
                console.log('DELETE INFO  : ' + $rootScope.backendAddress + '/procurement/master/item-group/delete/' + form.id);
                var konfirmasi = confirm("Apakah anda yakin ingin menghapus?");
                if (konfirmasi) {
                    $scope.loading = true;
                    $http.get($rootScope.backendAddress + '/procurement/master/item-group/delete/' + form.id).success(function (data, status, headers, config) {
                        toaster.pop('success', 'Group Item', 'Hapus ' + data.nama + ', berhasil.');
                        $scope.try_async_load();
                        $scope.output = undefined;
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {});
                }
            }
        }

        var modalInstanceController = function ($scope, $http, $modalInstance, $resource) {
        	
            $scope.ok = function () {
            	
            	$http.get($rootScope.backendAddress + '/procurement/master/item-group/get-item-group-by-nama/'+ $scope.nama , {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                	
                	if (data.nama == undefined) {
                		var namaItemGroup = data.nama;
                	} else {
                		namaItemGroup =  data.nama.toUpperCase();
                	}
                	
                	if ($scope.nama.toUpperCase() == namaItemGroup && $scope.id == undefined) {
                		alert("Nama item group tidak boleh sama.");
                    } else {
                    	var modalForm = {
                                parentId: 0,
                                nama: $scope.nama,
                                deskripsi: $scope.deskripsi
                            }
                            $scope.loading = true;
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/master/item-group/insertItemGroup',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: modalForm
                            }).success(function (data, status, headers, config) {
                                $modalInstance.close();
                            }).error(function (data, status, headers, config) {
                                $scope.messageError = "penyimpanan gagal";
                                $scope.loading = false;
                            });
                    }
                	
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                })
 
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

    ItemGroupController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$timeout', '$modal', 'toaster'];

})();
