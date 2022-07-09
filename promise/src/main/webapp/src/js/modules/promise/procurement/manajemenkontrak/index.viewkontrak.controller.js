
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('DaftarKontrakController', DaftarKontrakController);

	function DaftarKontrakController($scope, $http, $rootScope, $resource, $location, $state) {
		var vm = this;
		
		vm.getKontrakList = function() {
			vm.loading = true;
			
			$http.get($rootScope.backendAddress + '/procurement/KontrakManajemenServices/getLastKontrakListUser')
			.success(function (data, status, headers, config) {
				vm.kontrakList = data;
				vm.loading = false;
			})
			.error(function (data, status, headers, config) {
				vm.loading = false;
			});
		}
		
		vm.getKontrakList();
		
		vm.edit = function (jp) {
			$state.go('app.promise.procurement-manajemenkontrak-editdatakontrak', {dataKontrak: jp});
		};
	}

	DaftarKontrakController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state'];

})();