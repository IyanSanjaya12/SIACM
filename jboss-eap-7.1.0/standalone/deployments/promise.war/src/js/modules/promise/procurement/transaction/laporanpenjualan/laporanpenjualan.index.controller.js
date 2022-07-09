(function () {
    'use strict';

    angular
        .module('naut')
        .controller('LaporanPenjualanIndexController', LaporanPenjualanIndexController);

    function LaporanPenjualanIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
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
		var newDate = new Date();
		newDate.setDate(newDate.getDate() - 30);
		vm.param = {
			startDate : new Date(newDate),
			endDate : new Date()

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
			RequestService.doPrint({reportFileName:'CetakLaporanPenjualan', startDate:vm.param.startDate, endDate:vm.param.endDate});
		}
        $scope.edit = function(laporanPenjualan){
        	$state.go('app.promise.procurement-transaction-laporanPenjualan-detail', {
				todo : 'edit',
				laporanPenjualan : laporanPenjualan
			});
        }
        
        
        $scope.getLaporanPenjualanList();
    }

    LaporanPenjualanIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
