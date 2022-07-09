(function() {
	'use strict';

	angular
		.module('naut')
		.controller('ConditionalPrizeController', ConditionalPrizeController);

	function ConditionalPrizeController($scope, $http, $rootScope, $resource, $location, RequestService, $state) {
		
		var vm = this;

		$scope.getConditionalPriceList = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/master/conditionalPriceServices/getList')
				.then(function success(data) {
					vm.conditionalPriceList = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
				})
		}
		$scope.getConditionalPriceList();

		$scope.add = function() {
			$state.go('app.promise.procurement-master-conditionalprize-view', {
				toDo : 'Add'
			});
		}

		$scope.edit = function(conditionalPrice) {
			$state.go('app.promise.procurement-master-conditionalprize-view', {
				conditionalPrice : conditionalPrice,
				toDo : 'Edit'
			});
		};

		$scope.del = function (conditionalPriceId) {
        	RequestService.modalConfirmation("Apakah anda yakin ingin menghapus conditional price ?").then(function (result) {
				vm.loading = true;
				var data = {
					id : conditionalPriceId
				};
				RequestService.doPOST('/procurement/master/conditionalPriceServices/delete/', data)
					.then(function success(data) {
						RequestService.informDeleteSuccess();
						$scope.getConditionalPriceList();
					}, function error(response) { 
					    RequestService.informError("Terjadi Kesalahan Pada System");
						vm.loading = false;
				});
			});
        }
	}

	ConditionalPrizeController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state' ];

})();
