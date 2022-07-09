/**=========================================================
 * Module: GeneratePrNumber.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('GeneratePrNumberController', GeneratePrNumberController);

	function GeneratePrNumberController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter) {
		var vm = this;
		
		var prObject = {};
		
		vm.autoNumber = ($stateParams.autoNumber != undefined) ? $stateParams.autoNumber : {};
		
		vm.simpanIndex = [];
		
		$scope.getAutoNumberByPr = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'PR', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexPr = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByPr();
		
		$scope.getAutoNumberByPo = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'PO', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexPo = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByPo();
		
		$scope.getAutoNumberByCoa = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'COA', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexCoa = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByCoa();
		
		$scope.getAutoNumberByPrJoin = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'PRJOIN', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexPrJoin = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByPrJoin();
		
		$scope.getAutoNumberByCO = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'CO', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexCO = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByCO();
		
		$scope.getAutoNumberByPengadaan = function() {
			$http.get($rootScope.backendAddress + '/procurement/master/autonumberservices/getautonumberbytype/' + 'PA', {
				ignoreLoadingBar : true
			})
			.success(function (data, status, headers, config) {
				vm.simpanIndexPengadaan = data[0];
			})	
			$scope.loading = false;
		}
		$scope.getAutoNumberByPengadaan();
		
		
		$scope.isDisabled = false;

		vm.save = function () {
			$scope.messagePrError = '';
			$scope.messagePoError = '';
			$scope.messageCoaError = '';
			$scope.successMessage = '';
			$scope.messagePrJoinError = '';
			$scope.messagePengadaanError= '';
					
			if (typeof vm.simpanIndexPr.format === 'undefined' || vm.simpanIndexPr.format == "") {
                $scope.messagePrError = 'Format PR Belum Dimasukkan';
        	} else if (typeof vm.simpanIndexPo.format === 'undefined' || vm.simpanIndexPo.format == "") { 
        		$scope.messagePoError = 'Format PO Belum Dimasukkan';
        	} else if (typeof vm.simpanIndexCoa.format === 'undefined' || vm.simpanIndexCoa.format == "") { 
        		$scope.messageCoaError = 'Format COA Belum Dimasukkan';
        	} else if (typeof vm.simpanIndexPrJoin.format === 'undefined' || vm.simpanIndexPrJoin.format == "") { 
        		$scope.messagePrJoinError = 'Format PR Join Belum Dimasukkan';
        	} else if (typeof vm.simpanIndexCO.format === 'undefined' || vm.simpanIndexCO.format == "") { 
        		$scope.messageCOError = 'Format CO Belum Dimasukkan';
        	}else if (typeof vm.simpanIndexPengadaan.format === 'undefined' || vm.simpanIndexPengadaan.format == "") { 
        		$scope.messagePengadaanError = 'Format Pengadaan Belum Dimasukkan';
        	} else {		        	
        		RequestService.modalConfirmation().then(function (result) {
        			
		        	var postPr = {
		        		format : vm.simpanIndexPr.format,
		        		id : vm.simpanIndexPr.id	
		        	}
		        	
		        	var postPo = {
			        	format : vm.simpanIndexPo.format,
			        	id : vm.simpanIndexPo.id	
			        }
		        	
		        	var postCoa = {
				        	format : vm.simpanIndexCoa.format,
				        	id : vm.simpanIndexCoa.id	
				    }
		        	
		        	var postPrJoin = {
				        	format : vm.simpanIndexPrJoin.format,
				        	id : vm.simpanIndexPrJoin.id	
				    }
		        	
		        	var postCO = {
				        	format : vm.simpanIndexCO.format,
				        	id : vm.simpanIndexCO.id	
				    }
		        	
		        	var postPengadaan = {
				        	format : vm.simpanIndexPengadaan.format,
				        	id : vm.simpanIndexPengadaan.id	
				    }
		        	 
		            var uri = "/procurement/master/autonumberservices/update";		        	           
		        	RequestService.doPOST(uri, postPr)
		        		.success(function (autonumber) {
		        		$scope.successMessage = "Format Berhasil Disimpan";
		        		$scope.messagePrError = "";
                    })
                    
                    RequestService.doPOST(uri, postPo) 
	        			.success(function (autonumber) {
	        			$scope.successMessage = "Format Berhasil Disimpan";
	        			$scope.messagePoError = "";
	        		
	        		})
	        		
	        		RequestService.doPOST(uri, postCoa) 
	        			.success(function (autonumber) {
	        			$scope.successMessage = "Format Berhasil Disimpan";
	        			$scope.messageCoaError = "";
	        		
	        		})
	        		
	        		RequestService.doPOST(uri, postPrJoin) 
	        			.success(function (autonumber) {
	        			$scope.successMessage = "Format Berhasil Disimpan";
	        			$scope.messagePrJoinError = "";
	        		
	        		})
	        		
	        		RequestService.doPOST(uri, postCO) 
	        			.success(function (autonumber) {
	        			$scope.successMessage = "Format Berhasil Disimpan";
	        			$scope.messageCOError = "";
	        		
	        		})
	        		
	        		RequestService.doPOST(uri, postPengadaan) 
	        			.success(function (autonumber) {
	        			$scope.successMessage = "Format Berhasil Disimpan";
	        			$scope.messagePengadaanError = "";
	        		
	        		})
	        		
	        		//$scope.isDisabled = true;
                          	        
        		});
        		$scope.loading = false;
        	}
                	
	    }
	}	

	GeneratePrNumberController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter'];

})();