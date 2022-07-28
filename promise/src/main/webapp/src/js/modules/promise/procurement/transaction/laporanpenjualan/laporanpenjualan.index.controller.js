(function () {
    'use strict';

    angular
        .module('naut')
        .controller('LaporanPenjualanIndexController', LaporanPenjualanIndexController);

    function LaporanPenjualanIndexController($window, RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService, $filter) {
        var vm = this;
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[0];
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
			startDate : $filter('date')(startDate, "dd/MM/yyyy"),
			endDate : $filter('date')(endDate, "dd/MM/yyyy")

		};

        $scope.getLaporanPenjualanList = function () {
            vm.loading = true;
            RequestService.doPOST('/transaction/penjualan/getLaporanPenjualanList', vm.param).then(
			function success(data) {
				vm.laporanPenjualanList = data.data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.doFilter = function () {
            $scope.getLaporanPenjualanList();
        }
        $scope.doPrint = function(){
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=laporanPenjualan&startDate=' 
				+$filter('date')(vm.param.startDate, "yyyy-MM-dd")+'&endDate='+$filter('date')(vm.param.endDate, "yyyy-MM-dd"), '_blank');
			//RequestService.doPrint({reportFileName:'laporanPenjualan', startDate:vm.param.startDate, endDate:vm.param.endDate});
		}
        $scope.edit = function(laporanPenjualan){
        	$state.go('app.promise.procurement-transaction-laporanPenjualan-detail', {
				todo : 'edit',
				laporanPenjualan : laporanPenjualan
			});
        }
        
        
        $scope.getLaporanPenjualanList();
    }

    LaporanPenjualanIndexController.$inject = ['$window','RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService', '$filter'];

})();
