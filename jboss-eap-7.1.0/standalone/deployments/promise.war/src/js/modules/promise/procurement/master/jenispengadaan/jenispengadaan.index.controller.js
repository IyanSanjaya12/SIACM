
 /* Module: JenisPengadaanController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('JenisPengadaanController', JenisPengadaanController);

	function JenisPengadaanController($scope, $state, RequestService) {
		var vm = this;
		
		$scope.getJenisPengadaanList = function () {
			vm.loading = true;
			RequestService.doGET('/procurement/master/jenisPengadaanServices/getJenisPengadaanList')
			   .then(function success(data) {
				   vm.jenisPengadaanList = data;
					vm.loading = false;
			   }, function error(response) { 
				   vm.loading = false;
			   });
		}
		$scope.getJenisPengadaanList();

		$scope.add = function () {
			$state.go('app.promise.procurement-master-jenispengadaan-view',{
				toDo : 'Add'
			});
		};
		
		$scope.edit = function (jenisPengadaanBaru) {
			$state.go('app.promise.procurement-master-jenispengadaan-view',{
				jenisPengadaan : jenisPengadaanBaru,
				toDo: 'Edit'
			});
		};
		
		$scope.del = function (jenisPengadaanId) {
        	vm.uri = "/procurement/master/jenisPengadaanServices/delete";
        	var data = {
        		id : jenisPengadaanId
        	};
     
        	RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data jenis pengadaan?").then (function (result){
        		RequestService.doPOST(vm.uri, data)
	        		.success(function (data){
	        			RequestService.informDeleteSuccess();
	        			$scope.getJenisPengadaanList();
	        		})
        	});
        }
			
	}

	JenisPengadaanController.$inject = ['$scope', '$state', 'RequestService'];

})();