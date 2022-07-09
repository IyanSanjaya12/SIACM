(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengumumanEvaluasiAdminTeknisController', PengumumanEvaluasiAdminTeknisController);

    function PengumumanEvaluasiAdminTeknisController($scope, $http, $rootScope, $resource, $location) {
        $scope.getIndexPengadaan = function () {
			$scope.loading = true;
			//console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList')
				.success(function (data, status, headers, config) {
					$scope.pengadaanList = data;					
					$scope.loading = false;
				})
				.error(function (data, status, headers, config) {
					console.log('Error loading jenis pengadaan');
					$scope.loading = false;
				});
		};
		$scope.getIndexPengadaan();
        
        $rootScope.pengadaan = {};
		
		$scope.getViewDetil = function (pengadaan) {
			$rootScope.pengadaan = pengadaan;
			$location.path("/app/promise/procurement/pengumumanevaluasiadminteknis/detil");
		};
    }

    PengumumanEvaluasiAdminTeknisController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();