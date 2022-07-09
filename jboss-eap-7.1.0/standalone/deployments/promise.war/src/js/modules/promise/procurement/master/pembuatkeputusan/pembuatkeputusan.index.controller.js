/**=========================================================
 * Module: PembuatKeputusanControllerIndex
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PembuatKeputusanController', PembuatKeputusanController);

    function PembuatKeputusanController($scope, $state,RequestService,$log) {
    	var vm = this;
        
    	$scope.getPembuatKeputusanList = function () {
    		vm.loading=true;
    		
    		RequestService.doGET('/procurement/master/pembuatKeputusanServices/getList')
    			.then(function success(data) {
    				vm.pembuatKeputusanList = data;
    				vm.loading=false;
				}, function error(response) { 
					vm.loading = false;
			});
	    		
        }
    	$scope.getPembuatKeputusanList();
        
    	$scope.add = function () {
        	$state.go('app.promise.procurement-master-pembuatkeputusan-view',{
        		toDo : 'add'
        	});
        }
    	
    	$scope.edit = function (pembuatKeputusanBaru) {
            $state.go('app.promise.procurement-master-pembuatkeputusan-view',{
				pembuatKeputusan : pembuatKeputusanBaru,
				toDo : 'edit'
			});	
        }

        $scope.del = function (pembuatKeputusanId) {
        	var url = "/procurement/master/pembuatKeputusanServices/delete";
        	var data = {};
        	data.id = pembuatKeputusanId;
        	RequestService.deleteModalConfirmation().then(function(result) {
        		RequestService.doPOST(url,data)
        			.success(function (data) {
        				RequestService.informDeleteSuccess();
        				$scope.getPembuatKeputusanList();
        			}).error(function (data, status, headers, config) {
        				RequestService.informError("Terjadi Kesalahan Pada System");
		            })
            });
	    }
	       

        
    }

    PembuatKeputusanController.$inject = ['$scope' ,'$state','RequestService','$log'];

})();
