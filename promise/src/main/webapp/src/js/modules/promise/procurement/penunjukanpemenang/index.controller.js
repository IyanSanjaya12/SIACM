/**=========================================================
 * Module: PenunjukanPemenangController.js
 * Author: G.A.I
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenunjukanPemenangController', PenunjukanPemenangController);

	function PenunjukanPemenangController($http, $scope, $rootScope, $resource, $location) {
		var form = this;
        form.loading = true;
        var tahapan=19;
        $http.get(
            $rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanList/'+tahapan, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
            form.pengadaanList = data;
            form.loading = false;
        }).error(function(data, status, headers, config) {
            form.loading = false;
        });

		form.detail = function (list) {
			$rootScope.pengadaanPenunjukanPemenang = list;
			var sistemEvaluasiPenawaranId = list.sistemEvaluasiPenawaran.id;
			var jenisPenawaranId = list.jenisPenawaran.id;
			/*console.log('sistemEvaluasiPenawaranId : ' +sistemEvaluasiPenawaranId);
			console.log('jenisPenawaranId : ' +jenisPenawaranId);
			sistemEvaluasiPenawaranId = 1: sistem gugur; 2: merit point; 3: negoisasi teknis dan harga
			jenisPenawaranId = 1: total; 2: satuan*/
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 1) {
				$location.path('app/promise/procurement/penunjukanpemenang/totalSistemGugur');
			};
			if (jenisPenawaranId == 1 && sistemEvaluasiPenawaranId == 2) {
				$location.path('app/promise/procurement/penunjukanpemenang/totalMeritPoint');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 1) {
				$location.path('app/promise/procurement/penunjukanpemenang/satuanSistemGugur');
			};
			if (jenisPenawaranId == 2 && sistemEvaluasiPenawaranId == 2) {
				$location.path('app/promise/procurement/penunjukanpemenang/satuanMeritPoint');
			};
		};
	}

	PenunjukanPemenangController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location'];

})();