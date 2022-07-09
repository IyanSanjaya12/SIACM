/**=========================================================
 * Module: MasterKriteriaTeknisController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaTeknisController', KriteriaTeknisController);

    function KriteriaTeknisController($scope, $state, RequestService, $log) {
    	
        var vm = this;
        
        $scope.getKriteriaTeknisList = function () {
        	vm.loading=true;
        	RequestService.doGET ('/procurement/master/kriteriaTeknisServices/getList')
        		.then(function success(data) {
   			 		vm.kriteriaTeknisList = data;
   			 		vm.loading=false;
        		}, function error(response) { 
   			   		vm.loading = false;
   		   	  	});
        }
        $scope.getKriteriaTeknisList();

        
        $scope.add = function () {
        	$state.go('app.promise.procurement-master-kriteriaTeknis-view',{
        		toDo : 'add'
        	});
        }
    	
    	$scope.edit = function (kriteriaTeknisBaru) {
            $state.go('app.promise.procurement-master-kriteriaTeknis-view',{
				kriteriaTeknis : kriteriaTeknisBaru,
				toDo : 'edit'
			});	
        }

        $scope.del = function (kriteriaTeknisId) {
        	vm.url = "/procurement/master/kriteriaTeknisServices/delete";
        	var data ={};
        	data.id = kriteriaTeknisId;
        	RequestService.deleteModalConfirmation().then(function(result) {
        		RequestService.doPOST(vm.url,data)
        			.success(function (data) {
        				RequestService.informDeleteSuccess();
        				$scope.getKriteriaTeknisList();
        			}).error(function (data, status, headers, config) {
                	 	RequestService.informError("Terjadi Kesalahan Pada System");
        			})
            });
	    }

    }
    
    KriteriaTeknisController.$inject = ['$scope','$state','RequestService','$log'];

})();