/**=========================================================
 * Module: JenisPengadaanController.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('MetodePenyampaianDokumenPengadaanController', MetodePenyampaianDokumenPengadaanController);

    function MetodePenyampaianDokumenPengadaanController($scope, $http, $rootScope, $resource, $location, $state, RequestService) {
        var vm = this;
        
        $scope.getMetodePenyampaianDokumenPengadaanList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/metodePenyampaianDokumenServices/listMetodePenyampaianDokumenPengadaan')
				.then(function success(data) {
					vm.metodePenyampaianDokumenPengadaan = data;
					vm.loading = false;
				}, function error(response) {
					vm.loading = false;
			});
		}
		$scope.getMetodePenyampaianDokumenPengadaanList();
		
		$scope.add = function () {
			$state.go('app.promise.procurement-master-metodePenyampaianDokumenPengadaan-view', {
				toDo: 'Add'
        	});
		};
		
		$scope.edit = function (metodePenyampaianDokumenPengadaan) {
			$state.go('app.promise.procurement-master-metodePenyampaianDokumenPengadaan-view',{
				metodePenyampaianDokumenPengadaan : metodePenyampaianDokumenPengadaan,
				toDo : 'Edit'
			});
		};
		
		$scope.del = function (metodePenyampaianDokumenPengadaanId) {
			var url = '/procurement/master/metodePenyampaianDokumenServices/delete';
			var data = {};
			
			data.id = metodePenyampaianDokumenPengadaanId;
			RequestService.deleteModalConfirmation().then(function(result) {
	            RequestService.doPOST(url,data)
		        	.success(function (data) {
	            		RequestService.informDeleteSuccess();
	               		$scope.getMetodePenyampaianDokumenPengadaanList();
		        	}).error(function (data, status, headers, config) {
	            	   	RequestService.informError("Terjadi Kesalahan Pada System");
		        	});
	        });
		}
		
    }

    MetodePenyampaianDokumenPengadaanController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location', '$state', 'RequestService'];

})();