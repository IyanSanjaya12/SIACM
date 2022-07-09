/**
 * ========================================================= 
 * Module: tablelog.index.controller.js
 * =========================================================
 */
(function () {
	'use strict';
			
	angular
		.module('naut')
		.controller('TableLogIndexController', TableLogIndexController);

    function TableLogIndexController($rootScope, $scope, $stateParams, $filter, $compile, $state, $location, RequestService) {
        var vm = this;

        vm.userSystem = {
        		id: 0,
        		namaPengguna: "SYSTEM"
        };
        
        vm.getListTableLog = function () {
            vm.loading = true;
            RequestService.doGET('/procurement/modules/tablelog/tableLogModulesServices/getDataIndexTableLogByTableUser')
                .then(function success(result) {
                	if(result.length > 0) {
                		vm.listTableLog = result;
                        vm.listTableLog.push(vm.userSystem);
                		vm.loading = false;
                	}
                })
        }
        vm.getListTableLog();

        vm.viewTableLog = function(paramUserId) {
            $state.go('app.promise.procurement-tablelog-view', {
                userId: paramUserId
            })
        }
    }

    TableLogIndexController.$inject = ['$rootScope', '$scope', '$stateParams', '$filter', '$compile', '$state', '$location', 'RequestService']

})();