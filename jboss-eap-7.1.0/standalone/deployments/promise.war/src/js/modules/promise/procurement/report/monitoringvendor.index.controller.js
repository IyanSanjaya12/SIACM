/*
 *  Delevery Received Index Controller
 *
 *  Copyright (c) 2016 F.H.K
 *  http://forum.mitramandiri.co.id:8088
 *
 *  Licensed under GOD's give
 *
 */

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MonitoringVendorController', MonitoringVendorController);

    function MonitoringVendorController($scope, $http, $rootScope, $resource, $location, $filter, $compile, $modal, RequestService,$state,ModalService) {
	
	//============================================= Variable Awal ===========================================================================
    	var vm = this;
    	$scope.pageList = [];
    	$scope.maxSize = 10; //Panjang halaman yang tampil dibawah
    	vm.departement = null;
    	$scope.startDate= null;
    	$scope.endDate = null;
    	$scope.nama = null;
    	$scope.params= null;
    	vm.status = null;
    	$scope.statusList = [{
    		name: 'Aktif',
    		value: '1',
    	},{
    		name: 'Non Aktif',
    		value: '0',
    	}];
    	
    	// Datepicker
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
	//=======================================================================================================================================
               
    	$scope.getReportVendorList = function(halamanke, maxRecord, pencarian) {
    		vm.loading = true;
    		$scope.maxRecord = maxRecord;
        	$scope.currentPage = halamanke;
        	
    		var params = {
    				start: $scope.currentPage,
    				length: $scope.maxRecord,
    				search: "",
    				status:3 //sementara biar tidak selalu 0 di back end
    		}
    		
    		if(vm.departement != null){
    			params.departementId = vm.departement.id;
    		}
    		
    		if(vm.status != null){
    			params.status = vm.status.value;
    		}
    		
    		if($scope.nama != undefined){
    			params.search = $scope.nama;
    		}
    		if($scope.startDate!= null){

    			params.startDate=$scope.startDate;
    		}
    		if($scope.endDate!= null){

    			params.endDate=$scope.endDate;
    		}
    			
    		RequestService.requestServer('/procurement/report/MonitoringReportService/getReportVendor',params)
    		.then(function (data, status, headers, config) {
    			if(data != undefined) {

    				$scope.params = Object.create(params);
    				if(data.reportVendorDTOList.length > 0) {
    					$scope.totalItems = data.totalData;
    					$scope.vendorProfileList = data.reportVendorDTOList;
    				} else {
    					$scope.vendorProfileList = [];
    					$scope.totalItems = 0;
    				}
    				vm.departementList = data.organisasiList;
    			}
    			vm.loading = false;
    		})
    	};
    	
    	var initData = function() {
    		$scope.getReportVendorList(1, $scope.maxSize, $scope.pencarian);
    	};
    	initData();
    	
    	$scope.export=function(){
    		
    		RequestService.doDownloadExcelByUrl('/procurement/report/listVendor', $scope.params)
			
    	}
    	
    	// Paging Ala E-Marketplace by Master Budi
    	$scope.setPage = function (pageNo) {
    	    $scope.getReportVendorList(pageNo, $scope.maxRecord, $scope.pencarian);
    	};
    	
    	$scope.pageChanged = function(maxRecord) {
          $scope.maxRecord = maxRecord;
          $scope.getReportVendorList($scope.currentPage, $scope.maxRecord, $scope.pencarian);
        };
                
        // Fungsi2 Tambahan untuk Search dan Sort
        $scope.getSearchAndSort = function() {
        	$scope.getReportVendorList($scope.currentPage, $scope.maxRecord, null);
        }      
    }
    MonitoringVendorController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$compile', '$modal', 'RequestService','$state','ModalService'];
})();