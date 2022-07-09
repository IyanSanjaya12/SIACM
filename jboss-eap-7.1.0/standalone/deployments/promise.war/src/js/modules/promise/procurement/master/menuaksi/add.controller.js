/**=========================================================
 * Module: RoleMenuController.js
 * Controller for Role Menu
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AddMenuAksiController', AddMenuAksiController);
    function AddMenuAksiController($scope, $http, $rootScope, $resource, $location, toaster, $modal) {

        var ma = this;
        
        $scope.menu = {};
        $scope.getMenu = function () {
            $http.get($rootScope.backendAddress + '/procurement/menu/menu/get-list')
                .success(function (data, status, headers, config) {
                    $scope.menuList = data;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getMenu();
        
        $scope.aksi = {};
        $scope.getAksi = function (search) {
            if(search.length==0)
                search="a";
            
            $http.get($rootScope.backendAddress + '/procurement/menu/aksi/search-path/50/'+search)
                .success(function (data, status, headers, config) {
                    $scope.aksiList = data;
                }).error(function (data, status, headers, config) {});
        }

        ma.save = function () {
            if (typeof $scope.menu.selected === 'undefined' || $scope.menu.selected == '') {
                toaster.pop('warning', 'Kesalahan', 'Menu belum disi...');
            } else if (typeof $scope.aksi.selected === 'undefined' || $scope.aksi.selected == '') {
                toaster.pop('warning', 'Kesalahan', 'Aksi belum disi...');
            } else {
                $scope.loading = true;
                var dataMenuAksi = {
                    menu: $scope.menu.selected.id,
                    aksi: $scope.aksi.selected.id
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/menu/menu-aksi/insert',
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
                        toaster.pop('success', 'Menu Aksi', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    ma.save = false;
                });
            }
        };

        ma.back = function () {
            $location.path('/app/promise/procurement/master/menuaksi');
        };

    }
    AddMenuAksiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

})();