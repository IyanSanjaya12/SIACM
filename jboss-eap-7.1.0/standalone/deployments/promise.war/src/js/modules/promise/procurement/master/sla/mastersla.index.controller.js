(function() {
	'use strict';

	angular.module('naut').controller('MasterSlaController', MasterSlaController);

	function MasterSlaController(editableOptions, editableThemes, $scope, RequestService) {

		var vm = this;

		editableOptions.theme = 'bs3';
		editableThemes.bs3.inputClass = 'input-sm';
		editableThemes.bs3.buttonsClass = 'btn-sm';
		editableThemes.bs3.submitTpl = '<button type="submit" class="btn btn-success"><span class="fa fa-check" ></span></button>';
		editableThemes.bs3.cancelTpl = '<button type="button" class="btn btn-default" ng-click="$form.$cancel()">' + '<span class="fa fa-times text-muted"></span>' + '</button>';

		
		vm.saving = false;
		
		$scope.getMasterSLAList = function () {
			vm.loading = true;
    	 	RequestService.doGET('/procurement/master/SLAServices/getList').then(function success(data) {
    	 		vm.masterSLAList = data;
    	 		vm.loading=false;
    	 	}, function error(response) { 
    	 		RequestService.informError("Terjadi Kesalahan Pada System"); 
    	 		vm.loading = false;  
    	 	});
        }
		$scope.getMasterSLAList();
		
		$scope.save = function(sla){
        	vm.data = sla;
        	RequestService.modalConfirmation().then(function(result) {
        		RequestService.doPOSTJSON('/procurement/master/SLAServices/update', vm.data)
	        	.then(function success(data) {
	        		RequestService.informSaveSuccess();
	        		vm.saving = false;
	        		$scope.getMasterSLAList();
	        	}, function error(response){
	        	 	RequestService.informError("Terjadi Kesalahan Pada System");
				})
        	})
			
		}

	}

	MasterSlaController.$inject = ['editableOptions', 'editableThemes', '$scope', 'RequestService' ];

})();