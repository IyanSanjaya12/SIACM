(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengumumanEvaluasiAdminTeknisDetilController', PengumumanEvaluasiAdminTeknisDetilController);

    function PengumumanEvaluasiAdminTeknisDetilController($scope, $http, $rootScope, $resource, $location) {        
        $scope.getBidangPengadaan = function () {
            $scope.loadingSubidang = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $rootScope.pengadaan.id)
                .success(function (data,
                    status, headers, config) {
                    $scope.subBidangUsahaByPengadaanIdList = data;
                    $scope.loadingSubidang = false;
                }).error(function (data, status, headers, config) {
                    $scope.loadingSubidang = false;
                });
        }
        $scope.getBidangPengadaan();

    }

    PengumumanEvaluasiAdminTeknisDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();