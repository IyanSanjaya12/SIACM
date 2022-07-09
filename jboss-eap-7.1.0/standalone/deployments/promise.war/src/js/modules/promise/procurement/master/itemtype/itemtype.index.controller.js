/**
 * ========================================================= 
 * Module:	ItemTypeController
 * =========================================================
 */

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('ItemTypeController', ItemTypeController);

	function ItemTypeController($scope, $http, $rootScope, $resource, $location, RequestService, $state) {
		
		var vm = this;

		$scope.getItemTypeList = function() {
			vm.loading = true;
			RequestService.doGET('/procurement/master/item-type/get-list')
			.then(function success(data) {
				vm.itemTypeList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
		}
		$scope.getItemTypeList();
		
		$scope.add = function() {
			$state.go('app.promise.procurement-master-itemtype-view', {
				toDo : "add"
			});

		};
		$scope.edit = function(itemtypeNew) {
			$state.go('app.promise.procurement-master-itemtype-view', {
				itemType : itemtypeNew,
				toDo : "edit"
			});
		}

		$scope.del = function(itemTypeId) {
			RequestService.deleteModalConfirmation().then(function(result) {
				vm.loading = true;
				var url = '/procurement/master/item-type/delete';
				var data = {};
				data.id = itemTypeId;
				RequestService.doPOST(url, data)
					.success(function(data) {
						RequestService.informDeleteSuccess();
						vm.loading = false;
						$scope.getItemTypeList();
					}).error(function(data, status, headers, config) {
	        			RequestService.informInternalError();
	        			vm.loading = false;
					})

			});
		}
		
	}

	ItemTypeController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state' ];

})();