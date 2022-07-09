(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RoleViewController', RoleViewController);

	function RoleViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		var vm = this;
		vm.toDo= ($stateParams.toDo != undefined)? $stateParams.toDo : {};
		vm.role = ($stateParams.role != undefined) ? $stateParams.role : {};		
		vm.submitted = false;
		
		$scope.getDashboardList = function(){
			RequestService.doGET('/procurement/master/role/get-dashboard-list')
			.then(function success(data){
				vm.dashboardList = data;
			});
		}
		$scope.getDashboardList();
		
		$scope.save = function (formValid) {
			vm.submitted = true;			
			if(formValid){
				RequestService.modalConfirmation().then(function (result){
					$scope.saveData();   
					vm.loading = true;
				});
			}
    	}
		
		$scope.delValidation = function($event) {
			$scope.formRole[$event.target.name].$setValidity($event.target.name, true);
		}
	        	
        
       	$scope.saveData = function(){
	        var url='';
	        if (vm.toDo == "Add" ){
	        	url = '/procurement/master/role/insert'		
	        }
	        else if(vm.toDo=="Edit"){
	        	url = '/procurement/master/role/update'
	        }	
	        RequestService.doPOSTJSON(url, vm.role).then(function success(data) {
	        	if(data != undefined) {
	        		if(data.isValid != undefined) {
	        			if(data.errorKode != undefined) {	        				
	        				$scope.formRole.code.$setValidity('code', false);
	                  	}
	        			vm.loading = false;
	                }
	                else {
	                	RequestService.informSaveSuccess();
	                	$state.go('app.promise.procurement-master-role');
	                }
	        	}
	        }, function error(response) {
	        	RequestService.informInternalError();
	            vm.loading = false;
	        });
        }
 
        $scope.back = function () {
        	$state.go('app.promise.procurement-master-role');
        }
	}

	RoleViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter','$log'];

})();