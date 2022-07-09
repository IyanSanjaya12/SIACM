(function () {
	'use strict';
	angular
		.module('naut')
		.controller('InvoiceMatchingIndexController', InvoiceMatchingIndexController);

	function InvoiceMatchingIndexController($scope, $http, $rootScope, $resource, $location, $state, $modal, RequestService,$compile) {
		
	//============================================= Variable Awal ===========================================================================
    	
    	var userId 		= $rootScope.userLogin.user.id;
    	$scope.roleUser = $rootScope.userRole;
    	$scope.pageList = [];
    	$scope.maxSize 	= 5; //Panjang halaman yang tampil dibawah
    	$scope.sortDRList = [
    	   {
    	     name: 'PO Number',
    	     value: 'poNumber'
    	   }, {
    	     name: 'Purchased by',
    	     value: 'department'
    	   }, {
    	     name: 'Status',
    	     value: 'status'
    	   }, {
    		 name: 'PO Date',
      	     value: 'purchaseOrderDate'
    	   }
    	];
    	$scope.pencarian = [{
    		cari: '',
    		urut: ''
    	}];
    	
    	// Datepicker
        $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        $scope.format = $scope.formats[0];
        $scope.deliveryTimeSesiOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.itemList[index].deliveryTimeSesiOpened = true;
        };
	//=======================================================================================================================================
       
     // Fungsi2 Initial Data
        $scope.getAllDR = function() {
        	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedServices/findAll')
    		.then(function (data, status, headers, config) {
    			if(data != null) {
    				$scope.drList = data;
    			}
    		})
        }
        
        
        $scope.getPOList = function(halamanke, maxRecord, pencarian) {
    		$scope.maxRecord = maxRecord;
        	$scope.currentPage = halamanke;
        	
    		var params = {
    				pageNumber: $scope.currentPage,
    				numberOfRowPerPage: $scope.maxRecord
    		}
    		
    		if(pencarian.cari != undefined){
    			params.searchingKeyWord = pencarian.cari;
    		}
    		if(pencarian.vendor != undefined){
    			params.searchingVendor = pencarian.vendor;
    		}
    		if(pencarian.item != undefined){
    			params.searchingItem = pencarian.item;
    		}
    			
        	if(pencarian.urut != undefined){
        		params.sortKeyWord = pencarian.urut;
        	}
    			
        	
    		RequestService.requestServer('/procurement/purchaseorder/PurchaseOrderItemServices/findAllPOWithPOItem',params)
    		.then(function (data, status, headers, config) {
    			if(data != undefined) {
    				if(data.length > 0) {
    					$scope.poList = data;
    					$scope.totalItems = data[0].jmlData;
    				} else {
    					$scope.poList = [];
    					$scope.totalItems = 0;
    				}
    			}
    		})
    	};
    	
    	var initData = function() {
    		$scope.getAllDR();
    		$scope.getPOList(1, $scope.maxSize, $scope.pencarian);
    	};
    	initData();
    	
    	// Paging Ala E-Marketplace by Master Budi
    	$scope.setPage = function (pageNo) {
    	    $scope.getPOList(pageNo, $scope.maxRecord, $scope.pencarian);
    	};
    	
    	$scope.pageChanged = function(maxRecord) {
          $scope.maxRecord = maxRecord;
          $scope.getPOList($scope.currentPage, $scope.maxRecord, $scope.pencarian);
        };
     
        // for show detail:
        $scope.getDetil = function (data) {
        	var jumlahData = $scope.poList.length;
        	var jumlah = 0;
        	
        	angular.forEach($scope.poList,function(value){
        		if(value.po.id !== data.po.id) {
        			document.getElementById('info_' + value.po.id).style.display = 'none';
        		}
        		jumlah = jumlah + 1;
        	})
        	
        	angular.forEach($scope.drList,function(drValue){
        		angular.forEach(data.poitem,function(itemValue){
        			if(drValue.purchaseOrder.id == itemValue.purchaseOrderId) {
        				if(drValue.purchaseOrderItem.id == itemValue.id) {
        					itemValue.pass = drValue.pass;
        					itemValue.failed = drValue.failed;
        					itemValue.deliveryTime = drValue.dateReceived;
        					itemValue.isFinish = drValue.isFinish;
        					itemValue.attachFileName = drValue.attachFileName;
        					itemValue.attachRealName = drValue.attachRealName;
        					itemValue.dateReceived = drValue.dateReceived;
        				}
        			}
        		});
        	});
        	
        	if(jumlahData == jumlah) {
        		angular.forEach(data.poitem,function(value){
        			if(value.pass == null){
        				value.pass = 0;
        			}if(value.failed == null){
        				value.failed = 0;
        			}if(value.uploader == null){
        				value.uploader = "#";
        			}
        		})
        		$scope.itemList = data.poitem;
    			var rowObject = document.getElementById('info_' + data.po.id);
    			var btnDetil = document.getElementById('btnDetil_' + data.po.id);

                if ((rowObject.style.display == 'none')) {
                	var cell = document.getElementById('cell_info_' + data.po.id);
                    var content = '<div class="table-responsive">'
                        content += '        <table class="table table-striped table-bordered table-hover">';
                        content += '            <thead>';
                        content += '                <tr>';
                        content += '                    <th scope="col"style="width:20%; text-align:left"><span>Item</span> </th>';
                        content += '                   <th scope="col"style="width:7%; text-align:left"><span>Pass</span> </th>';
                        content += '                    <th scope="col"style="width:7%; text-align:left"><span>Failed</span> </th>';
                        content += '                    <th scope="col"style="width:5%; text-align:left"><span>Received</span> </th>';
                        content += '                    <th scope="col"style="width:15%; text-align:left"><span>Date Received</span> </th>';
                        content += '                    <th scope="col"style="width:20%; text-align:left"><span>File Attachment</span> </th>';
                        content += '                    <th scope="col"style="width:10%; text-align:left"><span>Status</span> </th>';
                        content += '                    <th scope="col"style="width:10%; text-align:left"><span>Log</span> </th>';
                        content += '                </tr>';
                        content += '            </thead>';
                        content += '            <tbody ng-repeat="item in itemList">';
                        content += '                <tr>';
                        content += '                    <td style="text-align:left" ng-bind="item.itemName"></td>';
                        content += '                    <td style="text-align:center"> <font color="GREEN"> <strong> {{item.pass}} </strong></font> </td>';
                        content += '                    <td style="text-align:center"> <font color="RED"> <strong> {{item.failed}} </strong></font> </td>';
                        content += '                    <td style="text-align:center"> {{item.pass}}/{{item.quantitySend}} </td>';
                        content += '                    <td style="text-align:center"> {{item.dateReceived | date:"dd/MM/yyyy"}} </td>';
                        content += '                    <td style="text-align:left">';
                        content += '          				<div class="col-lg-3" style="text-align:left"> <a href="#" ng-click="viewFile(item.attachRealName)">{{item.attachFileName}}</a></div>';
                        content += '					</td>';
                        content += '                    <td style="text-align:center"><label ng-bind="item.status"  class="label label-success text-bold" /> </td>';
                        content += '                    <td style="text-align:left"><button ng-click="viewLog(item)" class="btn btn-sm btn-warning" >Log</button></td>';
                        content += '                </tr>';
                        content += '            </tbody>';
                        content += '        </table>';
                        content += '    </div>';
                    angular.element(cell).html($compile(content)($scope));
                    rowObject.style.display = 'table-row';
                    btnDetil.innerText = 'Hide Detil';
                    $scope.loading = false;
                    return true;
                	
                } else {
                    rowObject.style.display = 'none';
                    btnDetil.innerText = 'Show Detil';
                    $scope.loading = false;
                    return false;
                }
        	}
        };
        
        // for view log delivery received :
        $scope.viewLog = function (data) {
        	$rootScope.poitemList = data;
            var komentarModalInstance = $modal.open({
                templateUrl: '/viewlog.html',
                controller: LogModalInstance,
                size: 'lg'
            });
        };
        var LogModalInstance = function ($scope, $modalInstance, $rootScope) {
        	var lemparan = $rootScope.poitemList;
        	var poItemId = lemparan.id;
        	var poId = lemparan.purchaseOrderId;
        	
        	$scope.mSize = 3; //Panjang halaman yang tampil dibawah
        	
        	$scope.itemName = lemparan.itemName;
        	$scope.totalD = lemparan.quantityPurchaseRequest;
        	
        	$scope.getData = function(halamanke, maxRecord) {
        		$scope.maxRec = maxRecord;
            	$scope.currPage = halamanke;
        		
        		var params = {
            			purchaseOrder: poId,
            			purchaseOrderItem: poItemId,
            			startPage: $scope.currPage,
            			endPage: $scope.maxRec
            	}
            	
            	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedLogServices/finfDeliveryReceivedLogBy', params)
    	    		.then(function (data, status, headers, config) {
    	    			if(data.length > 0) {
    	    				angular.forEach(data,function(value){
        	    				if(value.drLog.attachRealName != undefined) {
        	    					var str = value.drLog.attachRealName;
            	    				var n = str.lastIndexOf('_');
            	    				value.drLog.attachFileName = str.substring(n + 1);
        	    				}
        	    			})
        	    			$scope.listItem = data;
        	    			$scope.totItems = data[0].panjangData;
    	    			}
    	    		})
        	}
        	$scope.getData(1, $scope.mSize);
	    		
        	// Paging Ala E-Marketplace by Master Budi
        	$scope.setPage = function (pageNo) {
        	    $scope.getData(pageNo, $scope.maxRec);
        	};
        	
	    	$scope.viewFile = function(namaFile) {
        		window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
        	}
        	
        	$scope.ok = function () {
                $modalInstance.close();
            }
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }
        };
        LogModalInstance.$inject = ['$scope', '$modalInstance', '$rootScope'];
        
        //for match invoice:
		$scope.viewMatchInvoice = function (purchaseReq) {
            $rootScope.deletePR = purchaseReq;
            var modalInstance = $modal.open({
                templateUrl: '/matchview.html',
                controller: ModalInstanceDeletePurchaseRequestCtrl,
                size: 'lg',
                resolve: {
                    purchaserequest: function () {
                        return purchaseReq.id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
            }, function () {});


        };
        var ModalInstanceDeletePurchaseRequestCtrl = function ($scope, $modalInstance, purchaserequest) {
        	console.debug('purchaserequest in ModalInstanceDeletePurchaseRequestCtrl >>>>> ');
            var params = {
        			purchaseOrder: purchaserequest,
        	}
        	
        	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedServices/findBy', params)
	    		.then(function (data, status, headers, config) {
	    			console.debug(data);
	    			if(data.length > 0) {
    	    			var total = 0;
	    				angular.forEach(data,function(value){
	    					var pass 	  = value.pass;
	    					var unitPrice = value.purchaseOrderItem.unitPrice;
	    					var totalItem = pass * unitPrice;
	    					total 		  = total + totalItem;
    	    			})
	    				
	    				
	    				$scope.matchInvoiceList = data;
    	    			$scope.matchInvoiceNamaVendor = data[0].purchaseOrderItem.vendorName;
    	    		    $scope.matchInvoiceAlamatVendor = data[0].purchaseOrderItem.vendor.alamat;
    	    		    $scope.matchInvoiceTotal = total;
    	    		    
	    			}
	    		});
            
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceDeletePurchaseRequestCtrl.$inject = ['$scope', '$modalInstance', 'purchaserequest'];
        
        //for download file attachment :
        $scope.viewFile = function(namaFile) {
    		window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
    	}
        
        //for searching :
        $scope.getSearchAndSort = function(data) {
        	$scope.getPOList($scope.currentPage, $scope.maxRecord, data);
        }
	}

	InvoiceMatchingIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$modal', 'RequestService','$compile'];

})();