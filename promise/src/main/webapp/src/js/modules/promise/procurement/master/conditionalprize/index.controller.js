(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ConditionalPrizeController', ConditionalPrizeController);

    function ConditionalPrizeController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getConditionalPrizeList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.conditionalPrizeList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getConditionalPrizeList();

        form.add = function () {
            $location.path('app/promise/procurement/master/conditionalprice/tambah');
        }

        form.edit = function (data) {
            $rootScope.dataIndexConditionalPrize = data;
            $location.path('app/promise/procurement/master/conditionalprice/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Conditional Prize ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/delete/' + id).success(function (data, status, headers, config) {
                    getConditionalPrizeList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    ConditionalPrizeController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
