(function () {
    'use strict';

    angular.module('naut').controller('PemasukanPenawaranDetailSatuanController', PemasukanPenawaranDetailSatuanController);

    function PemasukanPenawaranDetailSatuanController($scope, $http, $rootScope, $resource, $location, toaster) {
        var pPengadaanDetail = this;

        if (typeof $rootScope.detilItemPengadaan !== 'undefined') {
            $scope.detilItemPengadaan = $rootScope.detilItemPengadaan;
            console.log(JSON.stringify($scope.detilItemPengadaan));
        } else {
            console.log('INFO Error');
        }

        $scope.getPenawaranVendor = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByPengadaanAndItem/'+$scope.detilItemPengadaan.pengadaanId+'/'+$scope.detilItemPengadaan.itemId).success(
                function (data, status, headers, config) {
                    $scope.loading = false;
                    pPengadaanDetail.detilItemPengadaan = data;
                    //console.log('iNFO : '+JSON.stringify(data));                    
                }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };
        $scope.getPenawaranVendor();


        /*- Kembali Ke View --*/
        pPengadaanDetail.backView = function () {
            $location.path('/app/promise/procurement/pemasukanPenawaran/viewSatuan');
        };

    }

    PemasukanPenawaranDetailSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();