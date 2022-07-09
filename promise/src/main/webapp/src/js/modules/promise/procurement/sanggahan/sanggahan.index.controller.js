/*rmt*/
(function() {
    'use strict';

    angular.module('naut').controller('SanggahanController',
        SanggahanController);

    function SanggahanController($scope, $http, $rootScope, $resource, $location, $state) {
        var sanggahanIndex = this;
        sanggahanIndex.loading = true;
        var tahapan=18;// SANGGAHAN
        $http.get(
            $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            sanggahanIndex.pengadaanList = data;
            sanggahanIndex.loading = false;
        }).error(function(data, status, headers, config) {
            sanggahanIndex.loading = false;
        });

      

        sanggahanIndex.edit = function (pengadaan) {
			$rootScope.detilPengadaan = pengadaan;
			$rootScope.isEditable = true;

			/* Cek Jenis penawaran
        	promise_t1_jenis_penawaran 1 = Total 
        	promise_t1_jenis_penawaran 2 = Satuan*/
			
			if (pengadaan.jenisPenawaran.id == 1) {
				$state.go('app.promise.procurement-sanggahan-total-detail', {pengadaanId : pengadaan.id} );
			} else {
				$state.go('app.promise.procurement-sanggahan-satuan-listitem', {pengadaanId : pengadaan.id} );
			}
		};

    }

    SanggahanController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location', '$state'
    ];

})();