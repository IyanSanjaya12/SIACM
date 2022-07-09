/**=========================================================
 * Module: JenisPenawaranController
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('JenisPenawaranController', JenisPenawaranController);

    function JenisPenawaranController($scope, $resource, $location, RequestService, $state, $log) {
        var vm = this;

        $scope.getJenisPenawaranList = function () {
            $scope.loading = true;
            RequestService.doGET('/procurement/master/JenisPenawaranServices/getJenisPenawaranList')
            .then(function success(data) {
                vm.jenisPenawaranList = data;
                vm.loading = false;
            }, function error(response) {
                vm.loading = false;
            })
        }
        $scope.getJenisPenawaranList();

        $scope.add = function () {
            $state.go('app.promise.procurement-master-jenispenawaran-view',{
            	toDo : 'Add'
            });
        }

        $scope.edit = function (jenisPenawaran) {
        	$log.info(jenisPenawaran)
        	$state.go('app.promise.procurement-master-jenispenawaran-view',{
				jenisPenawaran : jenisPenawaran,
				toDo: 'Edit'
			});
		};

        $scope.del = function (jenisPenawaranId) {
        	vm.url = "/procurement/master/JenisPenawaranServices/delete";
            RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data jenis penawaran?").then (function (result){
        		RequestService.doPOST(vm.url,jenisPenawaranId)
        		.success(function (data){
        			RequestService.informDeleteSuccess();
        			$scope.getJenisPenawaranList();
        		})
        	});
            
        }
    }

    JenisPenawaranController.$inject = ['$scope','$resource', '$location','RequestService', '$state', '$log'];

})();
