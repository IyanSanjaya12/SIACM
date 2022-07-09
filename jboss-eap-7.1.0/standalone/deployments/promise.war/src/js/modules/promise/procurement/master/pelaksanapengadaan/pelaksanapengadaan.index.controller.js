/**=========================================================
 * Module: PelaksanaPengadaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelaksanaPengadaanController', PelaksanaPengadaanController);

    function PelaksanaPengadaanController($scope,RequestService,$state) {
        var vm = this;
       
        
        $scope.getPanitiaList=function() {
        	vm.loading = true;
			
			RequestService.doGET('/procurement/master/panitiaServices/getPanitiaList')
				.then(function success(data) {
					vm.detailPanitiaList=data;
					vm.loading = false;
				}, function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
					vm.loading = false;
			});
        }
        $scope.getPanitiaList();
         

        $scope.addTeam= function(){
        	$state.go('app.promise.procurement-master-pelaksanapengadaan-viewteam',{
				toDo:'add'
			});	
        }
        
        $scope.addPejabat= function(){
        	$state.go('app.promise.procurement-master-pelaksanapengadaan-viewpejabat',{
				toDo:'add'
			});
        }
        
        $scope.del = function (panitia) {
			
			var url = '/procurement/master/panitiaServices/delete';
			
			RequestService.modalConfirmation("Apakah anda yakin ingin menghapus data satuan ?").then(function (result) {
				
				RequestService.doPOST(url, panitia)
					.success(function (data) {
	            		RequestService.informDeleteSuccess();
	            		$scope.getPanitiaList();
	               }).error(function (data, status, headers, config) {
	            	   	RequestService.informError("Terjadi Kesalahan Pada System");
	               });
				
			});
		};
        $scope.edit = function (detailPanitia) {
            if (detailPanitia.type == 'Tim') {
            	$state.go('app.promise.procurement-master-pelaksanapengadaan-viewteam',{
    				toDo:'edit',
    				panitiaDetail:detailPanitia
    			});
            } else {
            	$state.go('app.promise.procurement-master-pelaksanapengadaan-viewpejabat',{
    				toDo:'edit',
    				panitiaDetail:detailPanitia
    			});
            }
        };
    }

    PelaksanaPengadaanController.$inject = ['$scope','RequestService','$state'];

})();
