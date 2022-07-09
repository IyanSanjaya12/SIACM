(function () {
	'use strict';

	angular.module('naut').controller('IndikatorPenilaianViewController', IndikatorPenilaianViewController);

	function IndikatorPenilaianViewController(RequestService, ModalService, $scope, $log, $state,$stateParams) {
		
		var vm = this;
		$scope.loading = false;
		vm.toDo =($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.indikatorPenilaian= ($stateParams.indikatorPenilaian != undefined) ? $stateParams.indikatorPenilaian : {};
		vm.maxLimit = 100;
		vm.limit = 0;
		vm.sisa = 100;
				
		vm.getIndikatorPenilaian = function () { 
			RequestService.doGET('/procurement/master/IndikatorPenilaianServices/getIndikatorPenilaianList')
			.then(function successCallback(data) {
				vm.indikatorPenilaianList = data;
				vm.indikatorPenilaianList.forEach(function(value, index, array) {
					vm.limit = vm.limit + value.iPBobot;
				});				
				if(vm.toDo=="edit"){
					vm.limit = vm.limit - vm.indikatorPenilaian.iPBobot;
				}
 				vm.sisa =  (vm.maxLimit - vm.limit);

			}, function errorCallback(response) {				 
				RequestService.informError('Terjadi kesalahan pada system!');
			});
		}
		vm.getIndikatorPenilaian();
		
		$scope.save = function(){
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
			
		};
		
		$scope.validateForm = function () {
	         vm.isValid = true;
	         vm.errorKode= '';
	         vm.errorAspek='';
	         vm.errorIndikator='';
	         vm.errorBobot='';
	         if (typeof vm.indikatorPenilaian.iPCode == 'undefined' || vm.indikatorPenilaian.iPCode == '') {
	             vm.errorKode= 'template.error.field_kosong';
	             vm.isValid =  false;
	         }
	         if (typeof vm.indikatorPenilaian.iPAspekKerja == 'undefined' || vm.indikatorPenilaian.iPAspekKerja == '') {
	             vm.errorAspek= 'template.error.field_kosong';
	             vm.isValid =  false;
	         }
	         if (typeof vm.indikatorPenilaian.iPIndikator == 'undefined' || vm.indikatorPenilaian.iPIndikator == '') {
	        	 vm.errorIndikator= 'template.error.field_kosong';
	             vm.isValid =  false;
	         }
	         if (typeof vm.indikatorPenilaian.iPBobot == 'undefined' || vm.indikatorPenilaian.iPBobot == '0'|| vm.indikatorPenilaian.iPBobot == 0) {
	        	 vm.errorBobot= 'template.error.field_kosong';
	        	 vm.isValid =  false;
	         }
	         return vm.isValid;
	    }
		
		$scope.saveData = function(){
			var url='';
			if (vm.indikatorPenilaian.iPBobot > vm.sisa) {
				ModalService.showModalInformation('Bobot tidak boleh lebih dari sisa bobot, sisa bobot ' + vm.sisa + ' %!');
			} else {
				
				if (vm.toDo=="add") {
					url = '/procurement/master/IndikatorPenilaianServices/insert';
				} 
				if (vm.toDo=="edit") {
					url = '/procurement/master/IndikatorPenilaianServices/update';
				}
				
				RequestService.doPOSTJSON(url, vm.indikatorPenilaian)
		        	.then(function success(data) {
		        		if(data != undefined) {
		        			if(data.isValid != undefined) {
		        				if(data.errorKode!= undefined) {
		        					vm.errorKode = data.errorKode;
		        				}
		        			} else {
		        				RequestService.informSaveSuccess();
		        				$state.go('app.promise.procurement-master-indikatorpenilaian');
		        			}
		        		}  
		        	}, function error(response) {
		        		$log.info("proses gagal");
		        		RequestService.informError("Terjadi Kesalahan Pada System");
		        	});		
			}
			
		}
	
		$scope.back = function(){
			$state.go('app.promise.procurement-master-indikatorpenilaian');
		};
	}

	IndikatorPenilaianViewController.$inject = ['RequestService', 'ModalService', '$scope', '$log', '$state', '$stateParams'];

})();