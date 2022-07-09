(function () {
    'use strict';

    angular.module('naut')
        .controller('UndangVendorPengadaanSMBController', UndangVendorPengadaanSMBController);

    function UndangVendorPengadaanSMBController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams) {
        $scope.searchVendor = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByName/' + $scope.namaVendor)
                .success(function (data, status, headers, config) {
                    //console.log('INFO data : '+JSON.stringify(data));
                    $scope.vendorList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });

        };
        $scope.checkedVendor = [];
        $scope.togleCheckVendor = function (vendor) {
            if ($scope.checkedVendor.indexOf(vendor) === -1) {
                $scope.checkedVendor.push(vendor);
            } else {
                $scope.checkedVendor.splice($scope.checkedVendor.indexOf(vendor), 1);
            }
        };
        $scope.btnSimpanVendor = function () {
            //console.log('INFO Bidang : ' + JSON.stringify($scope.checkedVendor));
            if ($scope.checkedVendor.length == 0) {
                $scope.vendorError = true;
            } else {
                $scope.vendorError = false;
                $scope.vendorDuplicateError = false;
                if ($rootScope.inisialisasiForm.vendorList.length > 0) {
                    for (var i = 0; i < $rootScope.inisialisasiForm.vendorList.length; i++) {
                        for (var j = 0; j < $scope.checkedVendor.length; j++) {
                            if ($rootScope.inisialisasiForm.vendorList[i].id == $scope.checkedVendor[j].id) {
                                $scope.vendorDuplicateError = true;
                                break;
                            }
                        }
                    }
                }
                if ($scope.vendorDuplicateError == false) {
                    if ($rootScope.inisialisasiForm.vendorList.length == 0) {
                        $scope.loading = true;
                        $rootScope.inisialisasiForm.vendorList = $scope.checkedVendor;
                        $location.path('/app/promise/procurement/inisialisasi/add/smb');
                        $scope.loading = false;
                    } else {
                        $scope.loading = true;
                        $rootScope.inisialisasiForm.vendorList = $rootScope.inisialisasiForm.vendorList.concat($scope.checkedVendor);
                        $location.path('/app/promise/procurement/inisialisasi/add/smb');
                        $scope.loading = false;
                    }
                }
            }
        }
        $scope.btnBatal = function () {
            $location.path('/app/promise/procurement/inisialisasi/add/smb');
        }
    }

    UndangVendorPengadaanSMBController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams'];

})();
