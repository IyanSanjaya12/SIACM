/**=========================================================
 * Module: PurchaseRequestController.js
 * Author: NK
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PurchaseRequestController', PurchaseRequestController);

    function PurchaseRequestController($scope, $http, $rootScope, $resource, $location, $state, $modal) {
        var pr = this;
        $rootScope.ListPrFromItem = {};
        $scope.searchPRNumber = '';

        //paging
        $scope.allData = 0;
        $scope.rowPerPage = 5;
        $scope.pageList = [];

        pr.go = function (path) {
            $location.path(path);
        };

        $scope.getList = function (dataPrRequest) {
            $scope.loading = true;
            $scope.pageList = [];
            var dataPR = {};
            if (typeof dataPrRequest === 'undefined') {
                dataPR = {
                    pageNumber: 1,
                    numberOfRowPerPage: $scope.rowPerPage,
                    searchingKeyWord: ''
                };
            } else {
                dataPR = dataPrRequest;
            }
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/order/purchaseRequestServices/getPurchaseRequestListByPRNumberWithPagination',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataPR
            }).success(function (data, status, headers, config) {
                $scope.purchaseRequestList = data;
                var totalPage = Math.ceil($scope.purchaseRequestList.jmlData / $scope.rowPerPage);
                for (var i = 0; i < totalPage; i++) {
                    $scope.pageList.push((i + 1));
                }
                //total
                if (dataPR.searchingKeyWord == '')
                    $scope.allData = $scope.purchaseRequestList.jmlData;
                $scope.loading = false;
            });

        };

        /* $scope.getList = function () {
             $scope.loading = true;
             $http.get($rootScope.backendAddress + '/procurement/order/purchaseRequestServices/getList')
                 .success(
                 function (data, status, headers, config) {
                     pr.orderPurchaseRequestList = data;
                     console.log('iNFO : '+JSON.stringify(data ));
                     $scope.loading = false;
                 }).error(function (data, status, headers, config) {
                 $scope.loading = false;
             });
         };
         */

        $scope.range = function (n) {
            return new Array(n);
        };
        //Sort
        $scope.sortList = [
            {
                sort: 'prNumber',
                name: 'Pr Number'
            }, {
                sort: 'department',
                name: 'Departement'
            }
        ];
        $scope.getSortPR = function () {
            $scope.getPage(1);
        };
        //Search
        $scope.getSearchPr = function () {
            if ($scope.searchPRNumber.length >= 3) {
                $scope.getPage(1);
            } else {
                if ($scope.searchPRNumber.length == 0) {
                    $scope.getList();
                }
            }
        }
        $scope.getPage = function (page) {
            var dataPR = {
                pageNumber: page,
                numberOfRowPerPage: $scope.rowPerPage,
                searchingKeyWord: '{filter:[{key:prNumber, value:"' + $scope.searchPRNumber + '"}' + (typeof $scope.status === 'undefined' ? '' : ', {key:status, value:"' + $scope.status + '"}') + ']' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
            };

            $scope.getList(dataPR);
        }

        $scope.getList();

        $scope.getListItem = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/order/purchaseRequestItemServices/getList')
                .success(
                    function (data, status, headers, config) {
                        pr.orderPurchaseRequestItemList = data;
                        $scope.loading = false;
                    }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };

        $scope.getListItem();

        $scope.showDetail = function (u) {
            if ($scope.active != u.id) {
                $scope.active = u.id;
            } else {
                $scope.active = null;
            }
        };

        pr.add = function () {
            $rootScope.materialList = [];
            $location.path('app/promise/procurement/order/purchaserequest/add');
        }


        pr.buttonEdit = function (purchaserequest) {
            $state.go('app.promise.procurement-order-purchaserequest-edit', {
                purchaserequest: purchaserequest
            });
        }


        $scope.getDetil = function (pr) {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/order/purchaseRequestItemServices/getListByPurchaseRequestId/' + pr.id)
                .success(
                    function (data, status, headers, config) {
                        pr.orderPurchaseRequestItemList = data;
                        var namavendor = "-";
                        var rowObject = document.getElementById('info_' + pr.id);
                        if ((rowObject.style.display == 'none')) {
                            var cell = document.getElementById('cell_info_' + pr.id);
                            var content = '<div class="row">';
                            content += '          <div class="col-lg-3" style="bg-color:grey-light;text-align:center"><strong>Item</strong></div>';
                            content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Vendor</strong></div>';
                            content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Qty</strong></div>';
                            content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Price</strong></div>';
                            content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Unit</strong></div>';
                            content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Total</strong></div>';
                            content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Status</strong></div>';
                            content += '     </div>';
                            angular.forEach(pr.orderPurchaseRequestItemList, function (value, key) {
                                if (typeof value.vendor === 'undefined' || value.vendor !== null) {
                                    namavendor = value.vendor.nama;

                                }
                                content += '<div class="row">';
                                content += '          <div class="col-lg-3" style="text-align:left">' + value.itemname + '</div>';
                                content += '          <div class="col-lg-2" style="text-align:left">' + namavendor + '</div>';
                                content += '          <div class="col-lg-1" style="text-align:right">' + value.quantity + '</div>';
                                content += '          <div class="col-lg-2" style="text-align:right">' + value.price.toLocaleString() + '</div>';
                                content += '          <div class="col-lg-1" style="text-align:right">' + value.unit + '</div>';
                                content += '          <div class="col-lg-2" style="text-align:right">' + (value.price * value.quantity).toLocaleString() + '</div>';
                                content += '          <div class="col-lg-1" style="text-align:center">' + value.status + '</div>';
                                content += '     </div>';
                            });
                            cell.innerHTML = content;
                            rowObject.style.display = 'table-row';
                            $scope.loading = false;
                            return true;
                        } else {
                            rowObject.style.display = 'none';
                            $scope.loading = false;
                            return false;
                        }
                    }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
        };

        pr.del = function (purchaseReq, size) {
            var modalInstance = $modal.open({
                templateUrl: '/deletePurchaseRequest.html',
                controller: ModalInstanceDeletePurchaseRequestCtrl,
                size: size,
                resolve: {
                    purchaserequest: function () {
                        return purchaseReq.id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                $http.get($rootScope.backendAddress + '/procurement/order/purchaseRequestServices/delete/' + purchaseReq.id)
                    .success(function (data, status, headers, config) {
                        $scope.getList();
                    }).error(function (data, status, headers, config) {})
            }, function () {});


        };
        var ModalInstanceDeletePurchaseRequestCtrl = function ($scope, $modalInstance, purchaserequest) {
            $scope.purchaserequest = purchaserequest;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceDeletePurchaseRequestCtrl.$inject = ['$scope', '$modalInstance', 'purchaserequest'];


    }

    PurchaseRequestController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$modal'];

})();