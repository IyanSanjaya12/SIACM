(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BonusIndexController', BonusIndexController);

    function BonusIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService, $filter, $window) {
        var vm = this;
        
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[5];
        $scope.tanggalStartOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalStartStatus = true;
		}
        $scope.tanggalEndOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalEndStatus = true;
		}
		var startDate = new Date();
		startDate.setDate(startDate.getMonth() - 1);
		var endDate = new Date();
		vm.param = {
			startDate : $filter('date')(startDate, "yyyy-MM-dd"),
			endDate : $filter('date')(endDate, "yyyy-MM-dd")
		};
		
		$scope.doFilter = function () {
            $scope.getDataBonusList();
        }
		
        $scope.doPrint = function(){
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=laporanBonusPenjualan&startDate=' 
				+$filter('date')(vm.param.startDate, "yyyy-MM-dd")+'&endDate='+$filter('date')(vm.param.endDate, "yyyy-MM-dd"), '_blank');
			//RequestService.doPrint({reportFileName:'laporanPenjualan', startDate:vm.param.startDate, endDate:vm.param.endDate});
		}

        $scope.getDataBonusList = function () {
            vm.loading = true;
            RequestService.doPOST('/master/bonus/getBonusList', vm.param)
            .then(function success(data) {
				vm.bonusList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.getDataBonusList();
        
        
        
    }

    BonusIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService', '$filter', '$window'];

})();
