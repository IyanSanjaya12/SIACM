/**=========================================================
 * Module: JadwalKualifikasiController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('JadwalKualifikasiController', JadwalKualifikasiController);

	function JadwalKualifikasiController($scope, $http, $rootScope, $resource, $location) {
		$rootScope.pengadaan = {};
		//var kualifikikasipengadaan=1;
        
		$scope.getViewDetil = function (pengadaan) {
			$rootScope.pengadaan = pengadaan;
			$location.path("/app/promise/procurement/prakualifikasi/jadwalkualifikasi/detil2");
		};

		$scope.getIndexPengadaan = function () {
			$scope.loading = true;
			//console.log('info $rootScope.userLogin : '+JSON.stringify($rootScope.userLogin));
			$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanPraKualifikasi')
				.success(function (data, status, headers, config) {
					$scope.jadwalPengadaanList = data;					
					$scope.loading = false;
				})
				.error(function (data, status, headers, config) {
					console.log('Error loading jenis pengadaan');
					$scope.loading = false;
				});

		};
		$scope.getIndexPengadaan();
        
         //Hari Libur
        $scope.getHariLibur = function () {
            $scope.loadingHariLibur = true;
            $http.get($rootScope.backendAddress + '/procurement/master/hari-libur/get-list')
                .success(function (data,
                    status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        var date = new Date(data[i].tanggal);
                        date.setHours(12);
                        data[i].tanggal=date;
                    }
                    $rootScope.hariLiburList=data;
                    $scope.loadingHariLibur = false;
                    return data;
                }).error(function (data, status, headers, config) {
                    $scope.loadingHariLibur = false;
                });
        }
        $scope.getHariLibur();

	}

	JadwalKualifikasiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();