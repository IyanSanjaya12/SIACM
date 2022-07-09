/**=========================================================
 * Module: SeleksiPendaftaranIndexController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SeleksiPendaftaranIndexController', SeleksiPendaftaranIndexController);

	function SeleksiPendaftaranIndexController($scope, $http, $rootScope, $resource, $location, $state) {
		var form = this;
		
		$http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListByTahapanId/23')
	    .success(function (data, status, headers, config) {
	    	form.pengadaanList = data;
	    });
		
		form.buttonViewDetail = function(dataPengadaan) {
			$state.go('app.promise.procurement-panitia-seleksipendaftaran-view', {dataPengadaan: dataPengadaan});
		}
	}

	SeleksiPendaftaranIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource','$location', '$state'];

})();