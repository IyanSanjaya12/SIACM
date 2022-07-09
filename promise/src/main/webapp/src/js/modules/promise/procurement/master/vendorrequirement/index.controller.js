(function () {
    'use strict';

    angular
        .module('naut')
        .controller('VendorRequirementController', VendorRequirementController);

    function VendorRequirementController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getVendorRequirementList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.vendorRequirementList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getVendorRequirementList();

        form.add = function () {
            $location.path('app/promise/procurement/master/vendorrequirement/tambah');
        }

        form.edit = function (data) {
            $rootScope.indexVendorRequirement = data;
            $location.path('app/promise/procurement/master/vendorrequirement/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Vendor Requirement ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/deleteVendorRequirement/' + id).success(function (data, status, headers, config) {
                    getVendorRequirementList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    VendorRequirementController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
