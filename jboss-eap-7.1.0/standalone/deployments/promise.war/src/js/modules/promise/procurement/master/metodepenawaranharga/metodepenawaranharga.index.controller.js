/**=========================================================
 * Module: MetodePenawaranHargaController
 =========================================================*/

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('MetodePenawaranHargaController', MetodePenawaranHargaController);

	function MetodePenawaranHargaController($scope, $http, $rootScope, $resource, $location, RequestService, $state) {
		var vm = this;

		$scope.getMetodePenawaranHargaList = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/master/MetodePenawaranHargaServices/getMetodePenawaranHargaList')
			.then(function success(data) {
				vm.metodePenawaranHargaList = data;
				vm.loading = false;
			}, function error(response) {
				vm.loading = false;
			});

		}
		$scope.getMetodePenawaranHargaList();

		$scope.add = function() {
			$state.go('app.promise.procurement-master-metodepenawaranharga-view', {
				toDo : "add"
			});
		}

		$scope.edit = function(metodepenawaranhargaNew) {
			$state.go('app.promise.procurement-master-metodepenawaranharga-view', {
				metodePenawaranHarga : metodepenawaranhargaNew,
				toDo : "edit"
			});
		}

		$scope.del = function(metodePenawaranHargaId) {
			var url = '/procurement/master/MetodePenawaranHargaServices/delete/';
			var data = {};
			data.id = metodePenawaranHargaId;
			RequestService.deleteModalConfirmation().then(function(result) {
				RequestService.doPOST(url, data)
					.success(function(data) {
						RequestService.informDeleteSuccess();
						$scope.getMetodePenawaranHargaList();
					}).error(function(data, status, headers,config) {
						RequestService.informError("Terjadi Kesalahan Pada System");
					})

			});
		}
	}

	MetodePenawaranHargaController.$inject = [ '$scope', '$http', '$rootScope',
			'$resource', '$location','RequestService', '$state' ];

})();
