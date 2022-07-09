/*
 *  Delevery Received Vendor Index Controller
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
        .controller('DeliveryReceivedVendorController', DeliveryReceivedVendorController);

    function DeliveryReceivedVendorController($scope, $http, $rootScope, $resource, $location, $filter, $compile, $modal, RequestService, FileUploader) {
	
	//============================================= Variable Awal ===========================================================================
    	
    	var userId = $rootScope.userLogin.user.id;
    	var namaPengguna = $rootScope.userLogin.user.namaPengguna;
    	
    	$scope.roleUser = $rootScope.userRole;
    	$scope.pageList = [];
    	$scope.maxSize = 5; //Panjang halaman yang tampil dibawah
    	$scope.sortDRList = [
    	   {
    	     name: 'PO Number',
    	     value: 'poNumber'
    	   }, {
    		 name: 'Delivery Time',
      	     value: 'purchaseOrderDate'
    	   }, {
    		 name: 'Status',
      	     value: 'status'
    	   }
    	];
    	$scope.pencarian = [{
    		cari: '',
    		urut: ''
    	}];
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
    				numberOfRowPerPage: $scope.maxRecord,
    				userId: userId
    		}
        	
        	if(pencarian.cari != undefined){
    			params.searchingKeyWord = pencarian.cari;
    		}
    			
        	if(pencarian.urut != undefined){
        		params.sortKeyWord = pencarian.urut;
        	}
        	
    		RequestService.requestServer('/procurement/purchaseorder/PurchaseOrderItemServices/findAllPOWithPOItemByVendorName',params)
    		.then(function (data, status, headers, config) {
    			if(data != undefined) {
    				$scope.poList = data;
    				$scope.totalItems = data[0].jmlData;
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
        
        
        // Detail item per table
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
//        					itemValue.quantitySend = drValue.pass;
        					itemValue.pass = drValue.pass;
        					itemValue.failed = drValue.failed;
        					itemValue.deliveryTime = drValue.dateReceived;
        					itemValue.passLog 		= drValue.passLog==undefined?0:drValue.passLog;
        					itemValue.failedLog 	= drValue.failedLog==undefined?0:drValue.failedLog;
        				}
        			} 
        		})
        	})
        	
        	if(jumlahData == jumlah) {
        		$scope.itemList = data.poitem;
    			var rowObject 	= document.getElementById('info_' + data.po.id);
    			var btnDetil 	= document.getElementById('btnDetil_' + data.po.id);
    			//console.log('data itemList ='+JSON.stringify(data.poitem));

                if ((rowObject.style.display == 'none')) {              
                    var cell = document.getElementById('cell_info_' + data.po.id);
                    var content = '<div class="table-responsive">'
                        content += '        <table class="table table-striped table-bordered table-hover">';
                        content += '            <thead>';
                        content += '                <tr>';
                        content += '                    <th scope="col"style="width:20%; text-align:left"><span>Item</span>';
                        content += '                    </th>';
                        content += '                   <th scope="col"style="width:7%; text-align:left"><span>Pass</span>';
                        content += '                   </th>';
                        content += '                    <th scope="col"style="width:7%; text-align:left"><span>Failed</span>';
                        content += '                    </th>';
                        content += '                    <th scope="col"style="width:5%; text-align:left"><span>Received</span>';
                        content += '                    </th>';
                        content += '                    <th scope="col"style="width:15%; text-align:left"><span>Date Received</span>';
                        content += '                    </th>';
                        content += '                    <th scope="col"style="width:10%; text-align:left"><span>Action</span>';
                        content += '                    </th>';
                        content += '                </tr>';
                        content += '            </thead>';
                        content += '            <tbody ng-repeat="item in itemList">';
                        content += '                <tr>';
                        content += '                    <td style="text-align:left" ng-bind="item.itemName"></td>';
                        content += '                    <td style="text-align:left">{{ item.passLog }}</td>';
                        content += '                    <td style="text-align:left">{{ item.failed == null ? "0" : item.failed }}</td>';
                        content += '                    <td>{{ item.passLog }}/{{item.quantitySend}}</td>';
                        content += '                    <td style="text-align:left" ng-bind="item.deliveryTime | date:\'dd/MMM/yyyy\'"></td>';
                        content += '                    <td style="text-align:left"><button ng-click="viewLog(item)" class="btn btn-sm btn-warning">Log</button></td>';
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
        
        
        // Fungsi2 Tambahan untuk Search dan Sort
        $scope.getSearchAndSort = function(data) {
        	$scope.getPOList($scope.currentPage, $scope.maxRecord, data);
        }
        
        
        // Fungsi2 Simpan Data
        $scope.validasiItem = function(lemparan) {
        	var itemId = 0;
        	var kode = undefined;
        	angular.forEach($scope.drList,function(value){
        		if(value.purchaseOrder.id == lemparan.purchaseOrderId) {
        			if(value.purchaseOrderItem.id == lemparan.id) {
        				itemId = value.id;
        				kode = 1;
        			}
        		} 
        	})
        	$scope.saveItem(lemparan, kode, itemId);
        };
        
        $scope.saveItem = function(lemparan, kode, itemId) {
        	
        	var dataSimpan = {
        		purchaseOrderItem: lemparan.id,
        		purchaseOrder: lemparan.purchaseOrderId,
        		pass: lemparan.quantitySend,
        		failed: lemparan.failed,
        		dateReceived: new Date(lemparan.deliveryTime),
        		comment: lemparan.comment,
        		userId: userId
        	}
        	
        	if(kode == undefined) {
        		var url = '/procurement/deliveryreceived/DeliveryReceivedServices/insert';
        	} else {
        		dataSimpan.drId = itemId;
        		var url = '/procurement/deliveryreceived/DeliveryReceivedServices/update';
        	}
        	
        	RequestService.modalConfirmation()
			.then(function (result) {
				RequestService.requestServer(url, dataSimpan)
	    		.then(function (data, status, headers, config) {
	    			if(data.id != undefined) {
	    				$scope.saveLog(data)
	    			}
	    		})
			});
        };
        
        $scope.saveLog = function(lemparan) {
        	
        	var dataSimpan = {
        			deliveryReceived: lemparan.id,
        			comment: lemparan.comment,
        			pass: lemparan.pass,
        			failed: lemparan.failed,
        			attachRealName: lemparan.attachRealName,
        			dateReceived: new Date(lemparan.dateReceived),
        			userId: userId
        	}
        	
        	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedLogServices/insert', dataSimpan)
	    		.then(function (data, status, headers, config) {})
        	}
        
        
        // Modal
        $scope.claimInvoice = function (poId) {
        	var AttachModalInstance = $modal.open({
                templateUrl: '/attachModal.html',
                controller: 'AttachModalInstanceController',
                size: 'lg',
                resolve: {
                	poId: function () { 
                		return poId; 
                	}
                }
            });
        };
        
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
        	
        	$scope.mSize = 5; //Panjang halaman yang tampil dibawah
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
        	}
        	
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
        
      //for show payment:
		$scope.showPayment = function (purchaseReq) {
            $rootScope.deletePR = purchaseReq;
            var modalInstance = $modal.open({
                templateUrl: '/showPayment.html',
                controller: ModalInstanceShowPaymenttCtrl,
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
        
        var ModalInstanceShowPaymenttCtrl = function ($scope, $modalInstance, purchaseOrder) {
        	console.debug('purchaseOrder in ModalInstanceShowPaymenttCtrl >>>>> ');
            var params = {
        			purchaseOrder: purchaseOrder,
        	}
            var prId = 0;
        	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedServices/findBy', params)
	    		.then(function (data, status, headers, config) {
	    			if(data.length > 0) {
    	    			var total = 0;
	    				angular.forEach(data,function(value){
	    					var pass 	  	= value.pass;
	    					var unitPrice 	= value.purchaseOrderItem.unitPrice;
	    					var totalItem 	= pass * unitPrice;
	    					total 		  	= total + totalItem;
    	    			});
	    				
	    				$scope.showPaymentList 			= data;
    	    			$scope.showPaymentNamaVendor 	= data[0].purchaseOrderItem.vendorName;
    	    		    $scope.showPaymentAlamatVendor 	= data[0].purchaseOrderItem.vendor.alamat;
    	    		    $scope.showPaymentTotal 		= total;
    	    		    $scope.showPaymentPoNumber 		= data[0].purchaseOrder.poNumber;
    	    		    $scope.showPaymentPoDate 		= data[0].purchaseOrder.purchaseOrderDate;
    	    		    prId 							= data[0].purchaseOrder.purchaseRequest.id;
                        $scope.mataUang = data[0].purchaseOrder.mataUang;
	    			}
	    			
	    			//purchase request pengadaan:
	    			if(prId != 0){
	    				var totalPinalty = 0;
	    				RequestService.doGET('/procurement/inisialisasi/purchaserequestpengadaan/findByPurchaseRequestIdContract/'+prId)
		            	.then(function (data, status, headers, config){
		            		angular.forEach(data,function(value){
		            			console.debug(" >>> findByPurchaseRequestIdContract");
		            			console.debug(value);
		    					var pinaltyType 	= value.kontrak.pinaltyType; //0=percen , 1=Total.
		    					var pinalty			= value.kontrak.pinalty;
		    					var nilaiKontrak 	= value.kontrak.nilai;
		    					if(pinaltyType != null){
		    						if(pinaltyType == 0){
		    							totalPinalty = (nilaiKontrak * pinalty)/100;
		    						}else{
		    							totalPinalty = pinalty;
		    						}
		    						
		    						$scope.showPaymentPinalty = totalPinalty;
		    					}
	    	    			});
		            	
		            	});
	    			}
	    		});
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceShowPaymenttCtrl.$inject = ['$scope', '$modalInstance', 'purchaserequest'];
        
    }
    DeliveryReceivedVendorController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$compile', '$modal', 'RequestService', 'FileUploader'];
})();


// Modal Controller
angular.module('naut')
.controller('AttachModalInstanceController', function (RequestService, $scope, $modalInstance, $rootScope, FileUploader, poId) {
	$scope.mSize = 5; //Panjang halaman yang tampil dibawah
	$scope.ayoSimpan = false;
	
	// Upload Images
	var uploader = $scope.uploader = new FileUploader({
        url: $rootScope.uploadBackendAddress,
        method: 'POST'
    });
	
	uploader.filters.push({
        name: 'customFilter',
        fn: function (item /*{File|FileLikeObject}*/ , options) {
        	return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
		}
    });

	uploader.onCompleteItem = function (fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        fileItem.realFileName = response.fileName;
        $scope.loading = false;
    };
    
    uploader.onProgressItem = function (fileItem, progress) {
		$scope.progressTime = progress;
		$scope.loadingUpload = true;
		$scope.loading = true;
    };
    
    uploader.onCompleteAll = function() {
    	$scope.ayoSimpan = true;
    };
    
    $scope.getAllInvoice = function(halamanke, maxRecord) {
    	$scope.maxRec = maxRecord;
    	$scope.currPage = halamanke;
    	
    	var params = {
    			purchaseOrder: poId,
    			startPage: $scope.currPage,
    			endPage: $scope.maxRec
    	}
    	
    	RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedInvoiceServices/findBy',params)
		.then(function (data, status, headers, config) {
			if(data.length > 0) {
				$scope.attachList = data;
				$scope.totItems = data[0].jmlData;
			}
		})
    }
    $scope.getAllInvoice(1, $scope.mSize)
    
    $scope.simpan = function() {
    	var hitung4 = 0;
    	var panjangData4 = uploader.queue.length;
    	
    	angular.forEach(uploader.queue, function (item) {
    		hitung4 = hitung4 + 1;
    		
    		var dataSimpan = {
    				purchaseOrder: poId,
    				attachFileName: item.file.name,
    				attachRealName: item.realFileName,
    				attachFileSize: item.file.size,
    				userId: $rootScope.userLogin.user.id
                }
    		
    		RequestService.requestServer('/procurement/deliveryreceived/DeliveryReceivedInvoiceServices/insert', dataSimpan)
    		.then(function (data, status, headers, config) {})
    	})
    	
    	if(hitung4 == panjangData4) {
    		RequestService.modalInformation('Data Berhasil di Simpan')
    		.then(function (result) {})
    	}
    }
    
    // Paging Ala E-Marketplace by Master Budi
	$scope.setPage = function (pageNo) {
	    $scope.getAllInvoice(pageNo, $scope.maxRec);
	};
	
	$scope.viewFile = function(namaFile) {
		window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
	}
	
    $scope.ok = function () {
    	$scope.simpan();
		$modalInstance.close();
    }
    $scope.batal = function () {
    	$modalInstance.dismiss('cancel');
    }
});