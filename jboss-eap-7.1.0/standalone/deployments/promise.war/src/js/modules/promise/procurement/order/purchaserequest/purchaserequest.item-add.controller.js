(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PurchaseRequestAddItemsController', PurchaseRequestAddItemsController);

    function PurchaseRequestAddItemsController($scope, $http, $modal, $rootScope, ngTableParams, $filter, $state) {
        $scope.nameSearch = undefined;
        $scope.locationSearch = undefined;

        $http.get($rootScope.backendAddress + '/procurement/master/CatalogServices/findall')
            .success(function (data, status, headers, config) {
                $scope.tableItemList = new ngTableParams({
                    page: 1, // show first page
                    count: 10, // count per page
                    sorting: {
                        nama: 'asc' // initial sorting
                    }
                }, {
                    total: data.length, // length of data
                    getData: function ($defer, params) {
                        // use build-in angular filter
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;

                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });

                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });

        $scope.selectedItemList = [];
        $scope.selectItem = function (item) {
            var index = $scope.selectedItemList.indexOf(item);
            //console.log("index ??? "+index);
            if (index == -1) {
                $scope.selectedItemList.push(item);
            } else {
                $scope.selectedItemList.splice(index, 1);
            }
        };

        $scope.btnImport = function () {
            console.log("hasil select >>> " + JSON.stringify($scope.selectedItemList));
            angular.forEach($scope.selectedItemList, function (item, indexItem) {
                var itemObj = {
                    material: item,
                    kode: item.kode,
                    nama: item.nama,
                    deskripsi: item.deskripsi,
                    vendor: item.vendor.nama,
                    quantity: 0,
                    price: item.harga,
                    unit: item.item.satuanId.nama,
                    matauangId: item.mataUang.id,
                    matauang: item.mataUang.kode,
                    vendor: item.vendor.nama,
                    vendorId: item.vendor.id,
                    specification: item.deskripsi
                };
                //check item isexist
                var itemIsExist = false;
                angular.forEach($rootScope.itemPRList, function (value, index) {
                    if (typeof value.kode !== 'undefined') {
                        if (value.kode == itemObj.kode) {
                            itemIsExist = true;
                            alert('Item Pengadaan "' + value.nama + '" dengan KODE : "'+value.kode+'" sudah dipilih!');
                        }
                    }
                });
                if (!itemIsExist) {
                    $rootScope.itemPRList.push(itemObj);
                    $scope.ok();
                }
            });

        };


    }

    PurchaseRequestAddItemsController.$inject = ['$scope', '$http', '$modal', '$rootScope', 'ngTableParams', '$filter', '$state'];

})();