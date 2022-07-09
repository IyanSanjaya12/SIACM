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
        .controller('MonitoringPengadaanController', MonitoringPengadaanController);

    function MonitoringPengadaanController($scope, $http, $rootScope, $resource, $location, $filter, $compile, $modal, RequestService,$state,ModalService) {
	
	//============================================= Variable Awal ===========================================================================
    	var vm = this;
    	var userId = $rootScope.userLogin.user.id;
    	var jumlahPass = 0;
    	var inputPass = 0;
    	$scope.isSaving = false;
    	$scope.roleUser = $rootScope.userRole;
    	$scope.pageList = [];
    	$scope.maxSize = 10; //Panjang halaman yang tampil dibawah
    	$scope.maxVendor = 1;
    	$scope.maxQuotation = 1;
    	$scope.counter = Array;
    	$scope.type = {
        		name: 'Purchase Request',
        		value: 'purchaseRequest',
        	};
    	$scope.params = {};
    	$scope.startDate= null;
    	$scope.endDate = null;
    	$scope.number =null;
    	$scope.pencarian = [{
    		cari: '',
    		urut: ''
    	}];
    	$scope.typeList = [{
    		name: 'Purchase Request',
    		value: 'purchaseRequest',
    	},{
    		name: 'Purchase Order',
    		value: 'purchaseOrder',
    	}];
    	$scope.stringTd = "";
    	
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
               
    	$scope.getReportPengadaanList = function(halamanke, maxRecord, pencarian) {
    		vm.loading = true;
    		$scope.maxRecord = maxRecord;
        	$scope.currentPage = halamanke;
        	
    		var params = {
    				start: $scope.currentPage,
    				length: $scope.maxRecord,
    				search: "",
    				type: $scope.type.value
    		}
    		
    		if($scope.number != undefined){
    			params.search = $scope.number;
    		}
    		if($scope.startDate!= null){

    			params.startDate=$scope.startDate;
    		}
    		if($scope.endDate!= null){

    			params.endDate=$scope.endDate;
    		}
    			
    		RequestService.requestServer('/procurement/report/MonitoringReportService/getReportPengadaan',params)
    		.then(function (data, status, headers, config) {
    			if(data != undefined) {
    				$scope.params = Object.create(params);
    				if(data.reportPengadaanDTOList.length > 0) {
    					$scope.totalItems = data.totalData;
    					$scope.maxVendor = data.maxVendor;
    			    	$scope.maxQuotation =data.maxQuotation;
    			    	$scope.reportPengadaanList = data.reportPengadaanDTOList;
    			    	$scope.makeTd();
    				} else {
    					$scope.reportPengadaanList = [];
    					$scope.totalItems = 0;
    				}
    			}
    			vm.loading = false;
    		})
    	};
    	
    	var initData = function() {
    		$scope.getReportPengadaanList(1, $scope.maxSize, $scope.pencarian);
    	};
    	initData();
    	
    	$scope.export=function(){
    		
    		RequestService.doDownloadExcelByUrl('/procurement/report/pengadaan', $scope.params)
			
    	}
    	
    	$scope.makeTd = function (){
    		$scope.stringTd= "";
    		var cell = document.getElementById('stringTd');
    		var number = 0;
    		for(var a = 0; a < $scope.maxVendor;a++ ){
    			for(var b = 0; b < $scope.maxQuotation; b++){
    				number = b+1;
    				$scope.stringTd= $scope.stringTd +" <th scope='col'>Quotation "+ number +"</th>"
    			}
    		}
    		angular.element(cell).html($compile($scope.stringTd)($scope));
    	};
    	
    	// Paging Ala E-Marketplace by Master Budi
    	$scope.setPage = function (pageNo) {
    	    $scope.getReportPengadaanList(pageNo, $scope.maxRecord, $scope.pencarian);
    	};
    	
    	$scope.pageChanged = function(maxRecord) {
          $scope.maxRecord = maxRecord;
          $scope.getReportPengadaanList($scope.currentPage, $scope.maxRecord, $scope.pencarian);
        };
                
        // Fungsi2 Tambahan untuk Search dan Sort
        $scope.getSearchAndSort = function() {
        	$scope.getReportPengadaanList($scope.currentPage, $scope.maxRecord, null);
        }
        
        $scope.showPoItem = function (data) {
            var modalInstance = $modal.open({
                templateUrl: '/showItem.html',
                controller: ModalInstancePoItemCtrl,
                size: 'small',
                resolve: {
                    data: function () {
                        return data;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                console.log("OK Button");
            }, function () {});
        };
        
        var ModalInstancePoItemCtrl = function ($scope, $modalInstance, data) {
        	var id;
        	var url;
        	$scope.type = "";
            if(data.purchaseOrder != null){
            	id = data.purchaseOrder.id
            	url = '/procurement/purchaseorder/PurchaseOrderItemServices/getListByPoId/'
            	$scope.type = 'po';
            }else{
            	id = data.purchaseRequest.id
            	url = '/procurement/purchaseRequestItemServices/getListByPrId/'
            	$scope.type = 'pr';
            }
            
            
            RequestService.doGET(url + id)
			.then(function success(data) {
				if(data.length > 0) {
					
	    			var total = 0;
    				angular.forEach(data,function(value){
    					if($scope.type == 'po'){
    						var qty = value.quantityPurchaseRequest;
    						var unitPrice 	= value.unitPrice;
        					var totalItem 	= qty * unitPrice;
        					total 		  	= total + totalItem;	
    					}else{
    						var qty = value.quantity;
    						var unitPrice 	= value.price;
        					var totalItem 	= qty * unitPrice;
        					total 		  	= total + totalItem;	
    					}
    					
	    			});
    				$scope.showItemList 			= data;
	    		    $scope.showTotalPrice 		= total;
    			}
				$scope.loading = false;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
				$scope.loading = false;
		});
        	          
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstancePoItemCtrl.$inject = ['$scope', '$modalInstance', 'data'];
        
        
        
    }
    MonitoringPengadaanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$compile', '$modal', 'RequestService','$state','ModalService'];
})();