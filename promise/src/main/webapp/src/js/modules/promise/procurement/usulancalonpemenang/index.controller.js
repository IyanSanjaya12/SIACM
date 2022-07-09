/**=========================================================
 * Module: EvaluasiAdministrasiController.js
 * Author: H.R
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('UsulanCalonPemenangController', UsulanCalonPemenangController);

	function UsulanCalonPemenangController($http, $scope, $rootScope, $resource, $location) {
		var form = this;
		$scope.getList = function () {
            var tahapan = 16;
            form.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan)
				.success(function (data, status, headers, config) {
					form.pengadaanList = data;
            		form.loading = false;
				})
				.error(function (data, status, headers, config) {
					console.log('Error loading jenis pengadaan');
					form.loading = false;
				});
        }
        $scope.getList();
        
        form.detail = function (list) {
			$rootScope.pengadaanUsulanCalonPemenang = list;
			var sistemEvaluasiPenawaranId = list.sistemEvaluasiPenawaran.id;
			var jenisPenawaranId = list.jenisPenawaran.id;
			/*console.log('sistemEvaluasiPenawaranId : ' +sistemEvaluasiPenawaranId);
			console.log('jenisPenawaranId : ' +jenisPenawaranId);
			sistemEvaluasiPenawaranId = 1: sistem gugur; 2: merit point; 3: negoisasi teknis dan harga
			jenisPenawaranId = 1: total; 2: satuan*/
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 1) {
				$location.path('/app/promise/procurement/usulancalonpemenang/totalsistemgugur');
			};
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 2) {
				$location.path('/app/promise/procurement/usulancalonpemenang/totalmeritpoint');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 1) {
				$location.path('/app/promise/procurement/usulancalonpemenang/satuansistemgugur');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 2) {
				$location.path('/app/promise/procurement/usulancalonpemenang/satuanmeritpoint');
			};
		};
	}

	UsulanCalonPemenangController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location'];

})();