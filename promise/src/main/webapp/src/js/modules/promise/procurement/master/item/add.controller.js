(function () {
    'use strict';

    angular.module('naut').controller('ItemTambahController', ItemTambahController);

    function ItemTambahController($scope, $http, $rootScope, $resource, $location, toaster, $state, RequestService) {
        var form = this;
        form.satuan = {};
        form.tipeItem = {};

        //tree
        $scope.my_tree_handler = function (branch) {
            form.output = branch.label;
            form.treeId = branch.id;
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

        //satuan
        $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data, status, headers, config) {
            $scope.satuanList = data;
        }).error(function (data, status, headers, config) {})

        //type item
        $http.get($rootScope.backendAddress + '/procurement/master/item-type/get-list').success(function (data, status, headers, config) {
            $scope.tipeItemList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/item');
        }
        
        form.post=function(){
        	var postForm = {
                    nama: form.nama,
                    kode: form.kode,
                    deskripsi: form.deskripsi,
                    satuanId: form.satuan.selected.id,
                    itemTypeId: form.tipeItem.selected.id,
                    itemGroupId: form.treeId
                }
                console.log(JSON.stringify(postForm));
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/item/insert-item',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postForm
            }).success(function (data, status, headers, config) {
            	RequestService.informSaveSuccess();
        		$state.go('app.promise.procurement-master-item');
            });
        }

        form.simpan = function () {
            if (typeof form.nama === 'undefined' || form.nama == '') {
                alert("Nama Item belum disi.");
            } else if (typeof form.kode === 'undefined' || form.kode == '') {
                alert("Kode Item belum diisi.");
            } else if (typeof form.satuan.selected === 'undefined') {
                alert("Satuan belum dipilih.");
            } else if (typeof form.tipeItem.selected === 'undefined') {
                alert("Tipe Item belum dipilih.");
            } else if (typeof form.output == 'undefined' || form.output == '') {
                alert("Group Item belum dipilih");
            } else {
                //console.log(JSON.stringify(form));
            	$http.get($rootScope.backendAddress + '/procurement/master/item/get-by-kode/' + form.kode, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                    if (data.kode == form.kode){
                    	 alert("Kode tidak boleh sama.");
                    }
                    else{
                    	form.post();
                    }
                	
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                })
            };
        };
    }
    ItemTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', 'RequestService'];

})();
