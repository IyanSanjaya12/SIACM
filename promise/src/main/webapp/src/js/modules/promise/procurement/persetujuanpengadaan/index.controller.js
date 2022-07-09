(function() {
    'use strict';

    angular.module('naut').controller('PersetujuanPengadaanController',
        PersetujuanPengadaanController);

    function PersetujuanPengadaanController($scope, $http, $rootScope, $resource, $location) {
        var persetujuanIndex = this;
        persetujuanIndex.loading = true;
        var tahapan = 10040000; // masih hard code
      
        
        $scope.reload = function () {
		        $http.get( $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
		        		ignoreLoadingBar: true
		            }).success(function(data, status, headers, config) {
		            persetujuanIndex.pengadaanList = data;
		            persetujuanIndex.loading = false;
		        }).error(function(data, status, headers, config) {
		            //persetujuanIndex.loading = false;
		        });
		        
    	  };
    	  $scope.reload();
    	 // $interval($scope.reload, 1000);
        
        
        persetujuanIndex.view = function(pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = false;
            $location.path('/app/promise/procurement/persetujuanpengadaan/detil');
        };

        persetujuanIndex.edit = function(pengadaan) {
            $rootScope.detilPengadaan = pengadaan;
            $rootScope.isEditable = true;
            $location.path('/app/promise/procurement/persetujuanpengadaan/detil');
        };

    }

    PersetujuanPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location'
    ];

})();