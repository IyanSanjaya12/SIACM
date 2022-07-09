/**=========================================================
 * Module: SubBidangUsahaController
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SubBidangUsahaController', SubBidangUsahaController);

    function SubBidangUsahaController($scope, $http, $rootScope, $resource, $location,RequestService,$state) {
        var vm = this;

        $scope.getSubBidangUsahaList = function () {
        	vm.loading = true;
			RequestService.doGET('/procurement/master/sub-bidang-usaha/get-list')
				.then(function success(data) {
					vm.subBidangUsahaList = data;
					vm.loading = false;
				}, function error(response) { 
					vm.loading = false;
			});
        }
        $scope.getSubBidangUsahaList();

        $scope.add = function () {
        	$state.go('app.promise.procurement-master-subbidangusaha-view',{
				toDo:'add'
			});
        }

        $scope.edit = function (subBidangUsaha) {
        	$state.go('app.promise.procurement-master-subbidangusaha-view',{
				toDo:'edit',
				subBidangUsaha:subBidangUsaha
			});
        }

        $scope.del = function (subBidangUsaha) {
        	var url = "/procurement/master/sub-bidang-usaha/delete/";
        	
        	RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data sub bidang usaha ?").then(function (result) {
				RequestService.doPOST(url, subBidangUsaha)
					.then(function success(data) {
						RequestService.informDeleteSuccess();
						$scope.getSubBidangUsahaList();
					}, function error(response) { 
					    RequestService.informError("Terjadi Kesalahan Pada System");
				});
			});
        }
    }

    SubBidangUsahaController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location','RequestService','$state'];

})();
