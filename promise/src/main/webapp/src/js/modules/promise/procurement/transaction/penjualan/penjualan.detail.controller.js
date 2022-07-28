(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenjualanDetailController', PenjualanDetailController);

    function PenjualanDetailController(RequestService, $scope, $http, $filter, $rootScope, $resource, $location, $state, $stateParams, $log, ModalService, toaster, $window) {
        var vm = this;
        vm.todo = ($stateParams.todo != undefined) ? $stateParams.todo : null;
        vm.penjualan = ($stateParams.penjualan != undefined) ? $stateParams.penjualan : null;
        vm.selectList = [];
        vm.kodeJabatanList=['KPLMK','MK','PBMK'];
        vm.penjualanDetailList = [];
        $scope.format = 'yyyy/MM/dd';
        $scope.tanggalGaransiOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalGaransitStatus = true;
		}
        vm.grandTotal = 0;
        vm.tampJasa = 0;
        
        if (vm.todo == 'edit'){
			if (vm.penjualan != null){
				RequestService.doPOSTJSON('/transaction/penjualan/getListNotSelectedByPenjualanId', vm.penjualan.id).then(function (data) {
					vm.barangList = data.barangList;
					vm.selectList = data.selectList;
					vm.penjualanDetailList = data.penjualanDetailList;
				});
				vm.tanggalView = $filter('date')(vm.penjualan.tanggal, "dd/MM/yyyy HH:mm:ss");
			}
			
		} else {
			
			RequestService.doGET('/master/barang/getBarangList').then(function success(data) {
				vm.barangList = data;
			});
			
			RequestService.doGET('/transaction/penjualan/getGenerateCode').then(function success(data) {
				vm.penjualan = {
						noFaktur:''
				};
				vm.generateCode = data.code;
				vm.penjualan.noFaktur = data.code;
				vm.penjualan.tanggal = new Date();
				vm.penjualan.hargaSetelahDiskon = 0;
				vm.tanggalView = $filter('date')(new Date(), "dd/MM/yyyy HH:mm:ss");
			});
		}
        
        $scope.getMobilList = function () {
            vm.loading = true;
            RequestService.doGET('/master/mobil/getMobilList')
            .then(function success(data) {
				vm.mobilList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        
        $scope.getPelangganList = function () {
            vm.loading = true;
            RequestService.doGET('/master/pelanggan/getPelangganList')
            .then(function success(data) {
				vm.pelangganList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        
        $scope.getKaryawanList = function () {
            vm.loading = true;
            RequestService.doGET('/master/karyawan/getPenjualanListByJabatan')
            .then(function success(data) {
				vm.karyawan = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        
        $scope.checkValidasi = function(){
        	var error = 'silahkan isi kolom ini!';
        	var valid = true;
        	vm.errorMessagePelanggan = '';
        	vm.errorMessageMekanik = '';
        	
        	if(!RequestService.validasiEmpty(vm.penjualan.pelanggan)){
        		vm.errorMessagePelanggan = error;
        		valid = false;
        	}
        	
        	if(!RequestService.validasiEmpty(vm.penjualan.karyawan)){
        		vm.errorMessageMekanik = error;
        		valid = false;
        	}
        	
        	angular.forEach(vm.penjualanDetailList, function(value){
				if(!RequestService.validasiEmpty(value.barang) || !RequestService.validasiEmpty(value.quantity)){
	        		valid = false;
	        		toaster.pop('error', 'Kesalahan', 'Mohon Melengkapi data Detail Transaksi!');
	        	}
			});
        	
        	if(!RequestService.validasiEmpty(vm.penjualan.pembayaran)){
        		vm.errorMessagePembayaran = error;
        		valid = false;
        	}else{
        		vm.errorMessagePembayaran = '';
        	}
        	
        	return valid;
        }
        
        $scope.getMobilList();
        $scope.getPelangganList();
        $scope.getKaryawanList();
        
        $scope.addDataBarang = function(){
        	if (vm.penjualanDetailList == null) {
        		vm.penjualanDetailList = [];
		    }
		    vm.bulkToDo = 'add';
		    vm.penjualanDetailList.push({isOpenForm:true, quantity: 0});
        }
        
        $scope.selectBarang = function(barang, index){
			var data = barang.id;
			var isEquals = false;
			if ( vm.selectList.length > 0 ){
				for (var i = 0; i < vm.selectList.length; i++) {
		            if (i == index) {
		            	vm.selectList[i] = barang.id;
		            	isEquals = true;
		            }
		        }
				if (isEquals == false){
	            	vm.selectList.push(data);
	            }	
			} else {
				vm.selectList.push(data);
			}
			RequestService.doPOSTJSON('/transaction/penjualan/getListNotSelectedByIdList', vm.selectList).then(function (value) {
				vm.barangList = value;
			});
		}
        
        $scope.editDataBarang = function(barang) {
			if(barang!=null || barang!=undefined){
				barang.isOpenForm = true;
			}else{
				vm.penjualanDetailList.push({isOpenForm : true});
			}
			
		}
		
		$scope.hapusDataBarang = function(penjualanDetail) {
			var index = vm.penjualanDetailList.indexOf(penjualanDetail);
			vm.penjualanDetailList.splice(index, 1);
			if(penjualanDetail.barang != undefined){
				vm.barangList.push(penjualanDetail.barang);
				vm.barangList = $filter('orderBy')(vm.barangList, 'id', false);				
			}
			$scope.getTotalPenjualan();
		}
		$scope.getTotalPenjualan = function(){
        	vm.penjualan.totalPembayaran = 0;
        	angular.forEach(vm.penjualanDetailList, function(value, index){
        		vm.penjualan.totalPembayaran += value.total;
        	});
        }
		
		$scope.changeDiscount = function(diskon){
			vm.penjualan.totalDiskon = ((diskon/100) * vm.penjualan.totalPembayaran);
			vm.penjualan.hargaSetelahDiskon = vm.penjualan.totalPembayaran - vm.penjualan.totalDiskon;
			vm.grandTotal = vm.penjualan.hargaSetelahDiskon + vm.tampJasa;
		}
		
		$scope.changeJasa = function(jasa){
			vm.grandTotal = 0;
			vm.grandTotal = vm.penjualan.hargaSetelahDiskon + parseInt(jasa);
			vm.tampJasa = parseInt(jasa);				
			
		}
		
		$scope.changePayment = function(payment){
			if(parseInt(payment) >= parseInt(vm.grandTotal)){
				vm.penjualan.kembalian = payment - vm.grandTotal;
				vm.isValid = true;
				vm.errorMessagePembayaran = "";
			}else{
				vm.isValid = false;
				vm.errorMessagePembayaran = "Jumlah Pembayaran Kurang";
			}
		}
		
		$scope.changeQty = function(penjualanDetail){
			var index = vm.penjualanDetailList.indexOf(penjualanDetail);
			vm.penjualanDetailList[index].total = (parseInt(penjualanDetail.barang.harga) * parseInt(penjualanDetail.quantity));
			$scope.getTotalPenjualan();
		}
		
		$scope.doPrint = function(penjualanId){
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=strukPenjualan&penjualanId='+penjualanId, '_blank');
		}
		
		$scope.save = function (){
			var pejualanDto = {
					penjualan: vm.penjualan,
					penjualanDetailList: vm.penjualanDetailList
			}
        	if($scope.checkValidasi()){
        		RequestService.modalConfirmation().then(function(result) {
        			pejualanDto.penjualan.hargaSetelahDiskon = vm.grandTotal;
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/transaction/penjualan/save', pejualanDto).then(function success(data) {
						ModalService.closeModalInformation();
						RequestService.modalConfirmation("Apakah ingin mencetak Struk?").then(function(result) {
							$scope.doPrint(data.penjualan.id);
							RequestService.informSaveSuccess();
						}, function error(response) {
							$log.info("proses gagal");
							RequestService.informError("Terjadi Kesalahan Pada System");
		    			});
						$state.go ('app.promise.procurement-transaction-penjualan-index');
					}, function error(response) {
						ModalService.closeModalInformation();
						$log.info("proses gagal");
						RequestService.informError("Terjadi Kesalahan Pada System");
					});
    			});
        	}
        }
        
        $scope.back = function (){
        	$state.go('app.promise.procurement-transaction-penjualan-index');
        }
    }

    PenjualanDetailController.$inject = ['RequestService', '$scope', '$http', '$filter', '$rootScope', '$resource', '$location', '$state', '$stateParams', '$log', 'ModalService', 'toaster', '$window'];

})();
