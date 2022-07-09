/**=========================================================
 * Module: MataUangController
 =========================================================*/

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('MataUangController', MataUangController);

	function MataUangController($scope, $http, $rootScope, $resource, $location, RequestService, $state) {
		var vm = this;

		$scope.getMataUangList = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/master/mata-uang/get-list')
				.then(function success(data) {
					vm.mataUangList = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
				});
		}
		$scope.getMataUangList();

		$scope.add = function() {
			$state.go('app.promise.procurement-master-matauang-view', {
				toDo : "add"
			});
		}

		$scope.edit = function(matauangNew) {
			$state.go('app.promise.procurement-master-matauang-view', {
				mataUang : matauangNew,
				toDo : "edit"
			});
		}

		$scope.del = function(mataUangId) {
			var url = '/procurement/master/mata-uang/delete/';
			var data = {};

			data.id = mataUangId;
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading = true;
				RequestService.doPOST(url, data).success(function(data) {
					RequestService.informDeleteSuccess();
					vm.loading = false;
					$scope.getMataUangList();
				}).error(function(data, status, headers, config) {
					RequestService.informInternalError();
					vm.loading = false;
				})
			});
		}
	}

	MataUangController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state' ];

})();
