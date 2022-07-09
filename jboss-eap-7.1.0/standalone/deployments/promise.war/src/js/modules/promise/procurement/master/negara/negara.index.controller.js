(function() {
	'use strict';

	angular.module('naut').controller('NegaraIndexController',
			NegaraIndexController);

	function NegaraIndexController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $state, $stateParams, $log) {

		var vm = this;
		$scope.negaraList = function() {
			vm.loading = true;

			RequestService.doGET('/procurement/master/negara/get-list')
				.then(function success(data) {
					vm.negaraList = data;
					vm.loading = false;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
					vm.loading = false;
				});
		}
		$scope.negaraList();

		$scope.add = function() {
			$state.go('app.promise.procurement-master-negara-view', {
				toDo : "Add"
			});
		}

		$scope.edit = function(negara) {
			$state.go('app.promise.procurement-master-negara-view', {
				negara : negara,
				toDo : "Edit"
			});
		}

		$scope.del = function(negaraId) {
			vm.url = '/procurement/master/negara/delete/';
			var data = {
				id : negaraId
			};
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading = true;
				RequestService.doPOST(vm.url, data)
					.success(function(data) {
						RequestService.informDeleteSuccess();
						vm.loading = false;
						$scope.negaraList();
					}).error(function (data, status, headers, config) {
						RequestService.informInternalError();
	        			vm.loading = false;
		            })
			});
		}
	}

	NegaraIndexController.$inject = [ 'RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state', '$stateParams', '$log' ];

})();