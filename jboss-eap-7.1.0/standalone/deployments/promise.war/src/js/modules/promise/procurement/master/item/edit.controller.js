(function () {
    'use strict';

    angular.module('naut').controller('ItemUbahController', ItemUbahController);

    function ItemUbahController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state) {
        var form = this;
        var dataIndexItem;
        form.satuan = {};
        form.tipeItem = {};

        if (typeof $rootScope.dataIndexItem !== 'undefined') {
            dataIndexItem = $rootScope.dataIndexItem;
        }
        form.id = dataIndexItem.id;
        form.nama = dataIndexItem.nama;
        form.kode = dataIndexItem.kode;
        form.satuan.selected = dataIndexItem.satuanId;
        form.tipeItem.selected = dataIndexItem.itemType;
        form.deskripsi = dataIndexItem.deskripsi;
        form.output = dataIndexItem.itemGroupId.nama;
        form.treeId = dataIndexItem.itemGroupId.id;

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
        $http.get($rootScope.backendAddress + '/procurement/master/satuan/getSatuanList').success(function (data, status, headers, config) {
            $scope.satuanList = data;
        }).error(function (data, status, headers, config) {})

        //type item
        $http.get($rootScope.backendAddress + '/procurement/master/item-type/get-list').success(function (data, status, headers, config) {
            $scope.tipeItemList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/item');
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
                var postForm = {
                        id: form.id,
                        nama: form.nama,
                        kode: form.kode,
                        deskripsi: form.deskripsi,
                        satuanId: form.satuan.selected.id,
                        itemTypeId: form.tipeItem.selected.id,
                        itemGroupId: form.treeId
                    }
                    //console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/item/update-item',
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
            };
        };
    }
    ItemUbahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state'];

})();
