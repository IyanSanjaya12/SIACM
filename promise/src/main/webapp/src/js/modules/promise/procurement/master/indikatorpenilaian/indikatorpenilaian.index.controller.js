/**=========================================================
 * Module: IndikatorPenilaianController.js
 =========================================================*/
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('IndikatorPenilaianController', IndikatorPenilaianController);

	function IndikatorPenilaianController($scope, $http, $rootScope, $state,RequestService,$log) {
		var vm = this; 	
		
		$scope.getIndikatorPenilaianList = function () { 
			vm.loading = true;
			RequestService.doGET('/procurement/master/IndikatorPenilaianServices/getIndikatorPenilaianList')
				.then(function success(data) {
	   			 	vm.indikatorPenilaianList = data;
	                vm.loading=false;
	   		   	}, function error(response) { 
	   		   		RequestService.informError("Terjadi Kesalahan Pada System"); 
	   		   		vm.loading = false;
	   		   	});
		}
		$scope.getIndikatorPenilaianList();
		
		$scope.add = function () {
        	$state.go('app.promise.procurement-master-indikatorpenilaian-view',{
        		toDo : 'add'
        	});
        }
    	
    	$scope.edit = function (indikatorPenilaianBaru) {
            $state.go('app.promise.procurement-master-indikatorpenilaian-view',{
				indikatorPenilaian : indikatorPenilaianBaru,
				toDo : 'edit'
			});	
        }
	
    	$scope.del = function (indikatorPenilaianId) {
        	var url = "/procurement/master/IndikatorPenilaianServices/delete";
        	var data ={};
        	data.iPId = indikatorPenilaianId;
        	RequestService.deleteModalConfirmation().then(function(result) {
        		RequestService.doPOST(url,data)
        			.success(function (data) {
        				RequestService.informDeleteSuccess();
        				$scope.getIndikatorPenilaianList();
        			}).error(function (data, status, headers, config) {
        				RequestService.informError("Terjadi Kesalahan Pada System");
        			})
           });
    	}
	}

	IndikatorPenilaianController.$inject = ['$scope','$http', '$rootScope','$state','RequestService','$log'];

})();