(function () {
    'use strict';

    angular
        .module('naut')
        .controller('Test02Controller', Test02Controller);

    function Test02Controller($scope,$rootScope, DTOptionsBuilder, DTColumnDefBuilder, $location, $timeout) {
        
        //pancingan biar datatables tidak error.
        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withDisplayLength(2);

        $scope.dtColumnDefs = [
            DTColumnDefBuilder.newColumnDef(0),
            DTColumnDefBuilder.newColumnDef(1)
        ];

        $scope.cacheList = [
            {
                nama: 'cacheList1'
            },
            {
                nama: 'cacheList2'
            },
            {
                nama: 'cacheList3'
            },
            {
                nama: 'cacheList4'
            },
            {
                nama: 'cacheList5'
            },
            {
                nama: 'cacheList6'
            }
        ];

        $timeout(function(){
        	if($rootScope.interfacing.replace(" ", "").toUpperCase()=="FALSE"){
        		$location.path('/page/portal');
        	}
        	else{
        		window.location = $rootScope.pmAppLink;
        	}
        }, 2000);
    }
    Test02Controller.$inject = ['$scope','$rootScope', 'DTOptionsBuilder', 'DTColumnDefBuilder','$location', '$timeout'];
})();