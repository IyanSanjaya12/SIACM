/**
 * ========================================================= 
 * Module:	ItemTypeViewController
 * =========================================================
 */

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('ItemTypeViewController', ItemTypeViewController);

	function ItemTypeViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams, $log) {

		var vm = this;
		vm.toDo = ($stateParams.toDo!= undefined)? $stateParams.toDo : {};
		vm.itemType = ($stateParams.itemType != undefined) ? $stateParams.itemType : {};
		vm.submitted = false;
		
		$scope.save = function(formValid) {
			vm.submitted = true;
			if(formValid){
				RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formItemType[$event.target.name].$setValidity($event.target.name, true);
		}

		$scope.saveData = function() {

			var url = ""
			if (vm.toDo == "add") {
				url = "/procurement/master/item-type/insert";

			} else if (vm.toDo == "edit") {
				url = "/procurement/master/item-type/update";
			}
			
			RequestService.doPOSTJSON(url, vm.itemType).then(function success(data) {
				if(data != undefined) {
					if(data.isValid != undefined) {
						if(data.errorNama != undefined) {
							$scope.formItemType.namaItemType.$setValidity('namaItemType', false);
						}
						vm.loading = false;
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-itemtype');
					}
              
				}
            }, function error(response) {
            	RequestService.informInternalError();
            	vm.loading = false;
            });
			
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-itemtype');
		}

	}
	
	ItemTypeViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state', '$stateParams', '$log' ];

})();
