(function () {
    'use strict';
    angular
        .module('naut')
        .controller('PengadaanGagalViewController', PengadaanGagalViewController);

    function PengadaanGagalViewController($http, $scope, $rootScope, $resource, $location, $filter, $sce) {
        var form = this;
        form.pengadaanGagal = $rootScope.pengadaanGagalSelected;
        $scope.pengadaanId = form.pengadaanGagal.pengadaan.id;
        
        form.pengadaanGagal.keteranganWithSce = $sce.trustAsHtml(form.pengadaanGagal.keterangan);
        
        $scope.btnBack = function(){
            $location.path('/app/promise/procurement/pengadaangagal');
        }
        
    }

    PengadaanGagalViewController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$filter', '$sce'];
})();