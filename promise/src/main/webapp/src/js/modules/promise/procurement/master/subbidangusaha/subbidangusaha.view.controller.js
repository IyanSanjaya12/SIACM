/**
 * ========================================================= Module:
 * SubBidangUsahaViewController
 * =========================================================
 */

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('SubBidangUsahaViewController', SubBidangUsahaViewController);

	function SubBidangUsahaViewController($scope, $http, $rootScope, $resource, $location, toaster, RequestService, $state, $stateParams) {
		var vm = this;
		
		vm.subBidangUsaha = ($stateParams.subBidangUsaha != undefined) ? $stateParams.subBidangUsaha : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.bidangUsaha = {};
		vm.submitted = false;
		
		console.log(vm.subBidangUsaha);
		
		if (vm.toDo == "edit") {
			vm.bidangUsaha.selected = vm.subBidangUsaha.bidangUsaha;
		}

		$scope.getBidangUsahaList = function() {
			RequestService.doGET('/procurement/master/bidang-usaha/get-list')
				.then(function success(data) {
					vm.bidangUsahaList = data;
				}, function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
			});
		}

		$scope.getBidangUsahaList();

		$scope.save = function(formValid) {
			vm.submitted = true;
			if(formValid){
			RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.saveData();
			});
			}
		};

		$scope.saveData = function() {
			vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/master/sub-bidang-usaha/insert';
			}
			if (vm.toDo == "edit") {
				vm.url = '/procurement/master/sub-bidang-usaha/update';

			}
			
			console.log(vm.subBidangUsaha);
			
			RequestService.doPOSTJSON(vm.url, vm.subBidangUsaha).then(function success(data) {
				if (data != undefined) {
					if (data.isValid != undefined) {
						if (data.errorNama != undefined) {
							vm.errorNamaSubBidangUsaha = 'promise.procurement.master.subbidangusaha.error.nama_sama';
						}
						vm.loading = false;
					} else {
						RequestService.informSaveSuccess();
						$state.go('app.promise.procurement-master-subbidangusaha');
					}

				}
			}, function error(response) {
				RequestService.informInternalError();
            	vm.loading = false;
			});
		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-subbidangusaha');
		}
	}
	
	SubBidangUsahaViewController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'RequestService', '$state', '$stateParams' ];

})();
