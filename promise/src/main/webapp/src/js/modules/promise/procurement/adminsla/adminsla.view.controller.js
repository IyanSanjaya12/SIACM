(function() {
	'use strict';

	angular.module('naut').controller('AdminSlaViewController', AdminSlaViewController);

	function AdminSlaViewController($http, $rootScope, $resource, $location, $state, $stateParams, $scope, RequestService) {

		var vm = this;

		vm.loading = true;

		vm.pengadaan = $stateParams.pengadaan;

		RequestService.doGET('/procurement/prosespengadaan/getListByPengadaan/' + vm.pengadaan.id).then(function(data) {
			vm.ppList = data;
			vm.loading = false;
		})

		vm.back = function(pengadaan) {
			$state.go('app.promise.procurement-admin-sla');
		};

	}

	AdminSlaViewController.$inject = [ '$http', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$scope', 'RequestService' ];

})();