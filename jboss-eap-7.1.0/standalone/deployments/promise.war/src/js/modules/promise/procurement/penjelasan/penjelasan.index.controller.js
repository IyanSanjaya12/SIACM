/**
 * ========================================================= Module:
 * PenjelasanController.js Author: Reinhard
 * =========================================================
 */
(function() {
    'use strict';

    angular.module('naut').controller('PenjelasanController',
        PenjelasanController);

    function PenjelasanController($scope, $http, $rootScope, $resource, $location) {
        var penjelasanIndex = this;
        penjelasanIndex.loading = true;
        var tahapan = 8; // masih hard code
        $http.get(
            $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            penjelasanIndex.pengadaanList = data;
            penjelasanIndex.loading = false;
        }).error(function(data, status, headers, config) {
            penjelasanIndex.loading = false;
        });

        penjelasanIndex.view = function(pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = false;
            $location.path('/app/promise/procurement/penjelasan/detail');
        };

        penjelasanIndex.edit = function(pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = true;
            $location.path('/app/promise/procurement/penjelasan/detail');
        };

    }

    PenjelasanController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location'
    ];

})();