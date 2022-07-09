/**=========================================================
 * Module: AlokasiAnggaranViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlokasiAnggaranGabungViewController', AlokasiAnggaranGabungViewController);

    function AlokasiAnggaranGabungViewController($scope, $http, $rootScope, $resource, $location, $filter) {
        var form = this;
        
        $scope.anggaranPengadaan=$rootScope.detilAlokasiAnggaran;
        
         $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.anggaranPengadaan.pengadaan.id)
            .success(function (data, status, headers, config) {
                $scope.subBidangList = data;
            });
        
        $http.get($rootScope.backendAddress + '/procurement/pengadaan/vendorRequirementServices/getVendorRequirementListByPengadaanId/' + $scope.anggaranPengadaan.pengadaan.id)
            .success(function (data, status, headers, config) {
                $scope.vendorRequirement = data;
            });
        
        $scope.back = function () {
            $location.path("/app/promise/procurement/alokasianggaran/gabung/list");
        }
    }

    AlokasiAnggaranGabungViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();
