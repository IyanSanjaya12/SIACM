(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataBankViewController', RegistrasiDataBankViewController);

	function RegistrasiDataBankViewController($http, $scope,$stateParams, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $window ,$timeout,RequestService, ModalService) {
		
		var vm= this;
		
		vm.dataBankVendor = ($stateParams.dataBank != undefined) ? $stateParams.dataBank : {};
		vm.status = ($stateParams.status != undefined) ? $stateParams.status : {};
	    vm.toDo = $stateParams.toDo;


        	RequestService.doGET('/procurement/master/negara/get-list')
        	.then(function success(data) {
					vm.negaraList = data;
					angular.forEach(vm.negaraList,function(value,index){
            			if(vm.negaraList[index].nama == vm.dataBankVendor.negara){
            				vm.dataBankVendor.negara={};
            				vm.dataBankVendor.negara = vm.negaraList[index];
            			}
            		})
				}, function error(response) {
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});
		        
		        RequestService.doGET('/procurement/master/mata-uang/get-list')
				.then(function success(data) {
					vm.mataUangList = data;
				}, function error(response) {
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});
        
		       
		        
		
            		
            		
        
	    $scope.cariBank = function() {
	    	var bankmodalinstance = $modal.open({
	        	templateUrl: '/bankMaster.html',
	            controller: 'BankModalController2',
	            size: 'lg',
	            resolve: {
	            	dataBankMaster: function () { 
	            		return {}; 
	            	}
	            }
	        });
	    	bankmodalinstance.result.then(function(dataBankMaster){
	        	if (dataBankMaster != undefined && dataBankMaster !== null) {
	        		vm.dataBankVendor.namaBank = dataBankMaster.namaBank;
	        		vm.dataBankVendor.cabangBank = dataBankMaster.cabangBank;
	        		vm.dataBankVendor.alamatBank = dataBankMaster.alamatBank;
	        		vm.dataBankVendor.kota = dataBankMaster.kota;
	        		
	        	}
	        });        	
	    }
	    
	    $scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		};
	    
	    $scope.validateForm = function () {
	    	
	    	//Validasi Data Bank
	    	if(vm.dataBankVendor.namaBank == undefined || vm.dataBankVendor.namaBank == "") {
	    		vm.dataBankVendor.namaBankError = true;
	            document.getElementsByName("namaBank")[0].focus();
	        } else {
	        	vm.dataBankVendor.namaBankError = false;
	        } 
	    	
	    	if(vm.dataBankVendor.nomorRekening == undefined || vm.dataBankVendor.nomorRekening == "") {
	    		vm.dataBankVendor.nomorRekeningError = true;
	            document.getElementsByName("nomorRekening")[0].focus();
	        } else {
	        	vm.dataBankVendor.nomorRekeningError = false;
	        }
	    	
	    	if(vm.dataBankVendor.namaNasabah == undefined || vm.dataBankVendor.namaNasabah == "") {
	    		vm.dataBankVendor.namaNasabahError = true;
	            document.getElementsByName("namaNasabah")[0].focus();
	        } else {
	        	vm.dataBankVendor.namaNasabahError = false;
	        }
	    	
	    	if(vm.dataBankVendor.mataUang == undefined || vm.dataBankVendor.mataUang == "") {
	    		vm.dataBankVendor.mataUangError = true;
	            document.getElementsByName("mataUang")[0].focus();
	        } else {
	        	vm.dataBankVendor.mataUangError = false;
	        }

	    	if(vm.dataBankVendor.namaBankError == false && vm.dataBankVendor.nomorRekeningError == false && vm.dataBankVendor.namaNasabahError == false && vm.dataBankVendor.mataUangError == false) {
	    		return true;
	    	}
	    }

		$scope.saveData = function() {
			
			var data = {
					
					status : vm.status
			}
			
    		vm.bankVendorDraft={
    				id :vm.dataBankVendor.id,
    				alamatBank : vm.dataBankVendor.alamatBank,
    				cabangBank : vm.dataBankVendor.cabangBank,
    				kota : vm.dataBankVendor.kota,
    				mataUang : vm.dataBankVendor.mataUang,
    				namaBank : vm.dataBankVendor.namaBank,
    				namaNasabah : vm.dataBankVendor.namaNasabah,
    				created:vm.dataBankVendor.created,
    				updated:vm.dataBankVendor.updated,
    				nomorRekening : vm.dataBankVendor.nomorRekening	
    				
			}
			if(vm.status== 0 && vm.toDo=="Edit"){
				data.bankVendor = vm.bankVendorDraft;
				if(vm.dataBankVendor.negara != undefined){
	    			data.bankVendor.negara = vm.dataBankVendor.negara.nama;
	    		}
			}else
			{	
				vm.bankVendorDraft.bankVendor=vm.dataBankVendor.bankVendor;
				data.bankVendorDraft = vm.bankVendorDraft;
				if(vm.dataBankVendor.negara != undefined){
	    			data.bankVendorDraft.negara = vm.dataBankVendor.negara.nama;
	    		}
			}
    		
    		
			if (vm.toDo == "Add") {
				vm.url = '/procurement/vendor/BankVendorServices/insert';
			}

			if (vm.toDo == "Edit") {
				vm.url = '/procurement/vendor/BankVendorServices/update';
			}

			RequestService.doPOSTJSON(vm.url,data)
				.then(function success(data) {
							 $state.go('appvendor.promise.procurement-vendor-databank',{
				        			toDo:'Edited',			
				        		});	 
						
				}, function error(response) {
					$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
				});

		}

	    $scope.back = function () {
	    	$state.go('appvendor.promise.procurement-vendor-databank',{
    			toDo:'Edited'
    		});	      	
	    }    	
		
      
	}
	
	RegistrasiDataBankViewController.$inject = ['$http', '$scope','$stateParams', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location', '$window' ,'$timeout','RequestService', 'ModalService'];

})();

angular.module('naut')
.controller('BankModalController2', function ($rootScope, $scope, $http, $modalInstance, dataBankMaster, toaster, ngTableParams, RequestService, $stateParams, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter) {

	/*$http.get($rootScope.backendAddress + '/procurement/master/bank/getList')
    .success(function (data, status, headers, config) {
    	$scope.bankList = data;
    });*/
	
	$scope.bank = ($stateParams.bank != undefined) ? $stateParams.bank : {};
	
	$scope.dtInstance = {};
    
    function callback(json) {
	    console.log(json);
	}
    
    $scope.reloadData = function () {
		
	    var resetPaging = false;
	    
	    if($scope.dtInstance != null){
	    	$scope.dtInstance.reloadData(callback, resetPaging);
	    }
	};  

	$scope.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/master/bank/get-bank-by-pagination-with-token');
	$scope.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
		$compile(nRow)($scope);
		//console.log(nRow);
		//console.log(aData);
        var start = this.fnSettings()._iDisplayStart;
        $("td:first", nRow).html(start + iDisplayIndex + 1);
    });
	$scope.dtColumns = [
		DTColumnBuilder.newColumn(null).withTitle('Id').notSortable(),
		
		DTColumnBuilder.newColumn('namaBank').withTitle('Nama Bank').renderWith(function(data){
			return data;
		}),
		DTColumnBuilder.newColumn('namaKantor').withTitle('Nama Cabang').renderWith(function(data){
			return data;
		}),
		DTColumnBuilder.newColumn('alamat').withTitle('Alamat').renderWith(function(data){
			return data;
		}),
		DTColumnBuilder.newColumn('kota').withTitle('Kota').renderWith(function(data){
			return data;
		}),
		DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
		
			return '<div class="radio c-radio"><label style="color:green">'+
			'<input type="radio" name="dataBank" ng-model="bank.id"  ng-click="pilihBank('+data.id+')"><span class="fa fa-circle"></span>'+
			'</label></div>';
		})
	];
			
	DTInstances.getLast().then(function(instance) {
		$scope.dtInstance = instance;
        //console.log("DATAAAAAAAAAAAAAAAA : "+instance);
    });
	
	$scope.pilihBank = function (dataBank) { 	
    	RequestService.doGET('/procurement/master/bank/get-by-id/' + dataBank)
		.then(function success(data) {
			//console.log("Dataaaaaaaaaaaaaaaaaaaaaaaaaaaaa ID = "+dataBank);
			var dataBankMaster = {
				namaBank: data.namaBank,
				cabangBank: data.namaKantor,
				alamatBank: data.alamat,
				kota: data.kota
			}
			
			$scope.dataBankMaster = dataBankMaster;
			//console.log("Bataaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+dataBankMaster);
		}, function error(response) {	
		});

    }

	/*$scope.pilihBank = function(dataBank) {
		dataBankMaster = {
				namaBank: dataBank.namaBank,
				cabangBank: dataBank.namaKantor,
				alamatBank: dataBank.alamat,
				kota: dataBank.kota
		}
		
		$scope.dataBankMaster = dataBankMaster;
	}*/
	
	$scope.btnSimpan = function() {
		$modalInstance.close($scope.dataBankMaster);
	}
	
	$scope.btnBatal = function () {
        $modalInstance.dismiss('cancel');
    }  
	
});
