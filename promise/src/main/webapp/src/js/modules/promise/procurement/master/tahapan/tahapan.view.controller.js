/**=========================================================
 * Module: TahapanViewController
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('TahapanViewController', TahapanViewController);

	function TahapanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, $log) {
		
		var vm = this;
		vm.toDo= ($stateParams.toDo != undefined)? $stateParams.toDo : {};
		vm.tahapan = ($stateParams.tahapan != undefined) ? $stateParams.tahapan : {};
		vm.submitted = false;
		
		$scope.save = function (formValid) {
			vm.submitted = true;
			if (formValid){
			RequestService.modalConfirmation().then(function (result){	
					vm.loading = true;
					$scope.saveData();                     
				});
			}
		}
		
		$scope.delValidation = function($event) {
			$scope.formTahapan[$event.target.name].$setValidity($event.target.name, true);
		}

       $scope.saveData = function(){
    	   var url='';
    	   if (vm.toDo == "Add" ){
    		   url = '/procurement/master/tahapan/insert'			
    	   }
    	   else if(vm.toDo=="Edit"){ 
    		   url = '/procurement/master/tahapan/update'
    	   }	
	       
    	   RequestService.doPOSTJSON(url, vm.tahapan)
    	   	   .then(function success(data) {
	    		   if(data != undefined) {
	    			   if(data.isValid != undefined) {
	    				   if(data.errorNama != undefined) {
	    					   $scope.formTahapan.namaTahapan.$setValidity('namaTahapan', false);
	    				   }
	    				   vm.loading = false;
	    			   } else {
	    				   RequestService.informSaveSuccess();
	    				   $state.go('app.promise.procurement-master-tahapan');
		               }
	    		   }
    	   	   },
    	   	   function error(response) {
    	   		   RequestService.informInternalError();
    	   		   vm.loading = false;
    	   	   });
        }
 
       	$scope.back = function () {
       		$state.go('app.promise.procurement-master-tahapan');
       	}
	}

	TahapanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter','$log'];

})();