(function() {
	'use strict';

	angular.module('naut').controller('AdminSlaController', AdminSlaController);

	function AdminSlaController($http, $rootScope, $resource, $state, $scope, RequestService) {

		var vm = this;

		vm.loading = true;

		RequestService.doGET('/procurement/inisialisasi/getPengadaanWithSlaList').then(function(data) {
			vm.pengadaanList = data;
			vm.loading = false;
		})

		vm.view = function(pengadaan) {
			$state.go('app.promise.procurement-admin-sla-view', {
				pengadaan : pengadaan
			});
		};

	}

	AdminSlaController.$inject = [ '$http', '$rootScope', '$resource', '$state', '$scope', 'RequestService' ];

})();