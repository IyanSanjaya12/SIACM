/**=========================================================
 * Module: KualifikasiVendorController
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KualifikasiVendorController', KualifikasiVendorController);

    function KualifikasiVendorController($scope, $http, $rootScope, $state, RequestService) {
        var vm = this;

        $scope.getKualifikasiVendorList = function () {
            vm.loading = true;
            RequestService.doGET('/procurement/master/kualifikasi-vendor/get-list')
            .then(function success(data) {
   			 	vm.kualifikasiVendorList = data;
                vm.loading=false;
   		   	}, function error(response) { 
   		   		vm.loading = false;
   		   	});	
        }
        $scope.getKualifikasiVendorList();
        
        $scope.add = function () {
        	$state.go('app.promise.procurement-master-kualifikasivendor-view',{
        		toDo : 'add'
        	});
        }
    	
    	$scope.edit = function (kualifikasiVendorBaru) {
            $state.go('app.promise.procurement-master-kualifikasivendor-view',{
				kualifikasiVendor: kualifikasiVendorBaru,
				toDo : 'edit'
			});	
        }

        $scope.del = function (kualifikasiVendorId) {
        	var url = "/procurement/master/kualifikasi-vendor/delete";
        	var data ={};
        	data.id = kualifikasiVendorId;
        	RequestService.deleteModalConfirmation().then(function(result) {
        		RequestService.doPOST(url,data)
        			.success(function (data) {
        				RequestService.informDeleteSuccess();
        				$scope.getKualifikasiVendorList();
        				vm.loading = false;
	                 }).error(function (data, status, headers, config) {
	                	 RequestService.informInternalError();
	                	 vm.loading = false;
	                 })
            });
	    }
    }

    KualifikasiVendorController.$inject = ['$scope', '$http', '$rootScope','$state','RequestService'];

})();
