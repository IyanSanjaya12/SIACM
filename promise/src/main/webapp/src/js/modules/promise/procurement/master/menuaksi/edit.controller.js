/**=========================================================
 * Module: EditMenuAksiController.js
 * Controller for Menu Aksi
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('EditMenuAksiController', EditMenuAksiController);
    function EditMenuAksiController($scope, $http, $rootScope, $resource, $location, toaster, $modal) {

        var ma = this;

        if (typeof $rootScope.menuAksiEdit !== 'undefined') {
            $scope.menuAksi = $rootScope.menuAksiEdit;
            //console.log('Data  =' +JSON.stringify($scope.menuAksi));
        } else {
            console.log('INFO Error');
        }

        $scope.menu = {};
        $scope.getMenu = function () {
            $http.get($rootScope.backendAddress + '/procurement/menu/menu/get-list')
                .success(function (data, status, headers, config) {
                    $scope.menuList = data;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getMenu();
        
        $scope.aksi = {};
        $scope.getAksi = function () {
            $http.get($rootScope.backendAddress + '/procurement/menu/aksi/get-list')
                .success(function (data, status, headers, config) {
                    $scope.aksiList = data;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getAksi();
        
        ma.save = function () {
            if (typeof $scope.menuAksi.menu === 'undefined' || $scope.menuAksi.menu == '') {
                toaster.pop('warning', 'Kesalahan', 'Menu belum disi...');
            } else if (typeof $scope.menuAksi.aksi === 'undefined' || $scope.menuAksi.aksi == '') {
                toaster.pop('warning', 'Kesalahan', 'Aksi belum disi...');
            } else {
                $scope.loading = true;
                var dataMenuAksi = {
                    id: $scope.menuAksi.id,
                    menu: $scope.menuAksi.menu.id,
                    aksi: $scope.menuAksi.aksi.id
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/menu/menu-aksi/update',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataMenuAksi
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'Menu Aksi', 'Update ' + data.nama + ', berhasil.');
                    }
                    ma.save = false;
                });
            }
        };

        ma.back = function () {
            $location.path('/app/promise/procurement/master/menuaksi');
        };

    }
    EditMenuAksiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

})();