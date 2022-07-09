/**
 * ========================================================= Module:
 * MasterKriteriaAdministrasiController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('MasterKriteriaAdministrasiController',
			MasterKriteriaAdministrasiController);

	function MasterKriteriaAdministrasiController($state, RequestService,
			$scope) {
		var vm = this;

		$scope.getMasterKriteriaAdministrasiList = function() {
			vm.loading = true;
			RequestService.doGET(
					'/procurement/master/kriteriaAdministrasiServices/getList')
					.then(function success(data) {
						vm.masterKriteriaAdministrasiList = data;
						vm.loading = false;
					}, function error(response) {
						vm.loading = false;
					});
		}
		$scope.getMasterKriteriaAdministrasiList();

		vm.add = function() {
			$state.go(
					'app.promise.procurement-master-kriteriaAdministrasi-view',
					{
						toDo : 'Add'
					});

		};

		vm.edit = function(kriteriaAdministrasiBaru) {
			$state.go(
					'app.promise.procurement-master-kriteriaAdministrasi-view',
					{
						kriteriaAdministrasi : kriteriaAdministrasiBaru,
						toDo : 'Edit'
					});
		};

		vm.del = function(kriteriaAdministrasiId) {
			vm.url = "/procurement/master/kriteriaAdministrasiServices/delete";
			var data = {
				id : kriteriaAdministrasiId
			};
			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data kriteria administrasi?")
					.then(function(result) {
								RequestService.doPOST(vm.url, data).success(
									function(data) {RequestService.informDeleteSuccess();
										$scope.getMasterKriteriaAdministrasiList();
										}).error(function(data, status, headers,config) {
											RequestService.informError("Terjadi Kesalahan Pada System");
											});
							});
		}
	}

	MasterKriteriaAdministrasiController.$inject = [ '$state',
			'RequestService', '$scope' ];

})();