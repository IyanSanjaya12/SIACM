(function () {
    'use strict';

    angular.module('naut').controller('ApprovalViewBOController', ApprovalViewBOController);

    function ApprovalViewBOController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, ModalService, toaster) {
        var form = this;
        form.approvalProcess = $rootScope.approvalProcess;
        form.note = '';
        $scope.downloadFile = $rootScope.viewUploadBackendAddress + '/';

        /*		if (typeof form.approvalProcess.keterangan !== 'undefined'){
        			form.note = form.approvalProcess.keterangan;
        		}*/

        form.totalPRItem = 0;
        $scope.loading = true;
        $scope.isUserApproval = false;
        form.prItemList = [{grandTotal:0}];
        form.asuransi=[];
        form.slaDeliveryTime = null;
        
        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findPurchaseRequest/' + form.approvalProcess.approvalProcessType.valueId).success(function (data) {
            $scope.bookingOrder = data;
            
            RequestService.doPOSTJSON('/procurement/purchaseRequestServices/slaDeliveryTimeListByPurchaseRequest', data.id).then(function successCallback(data) {
				if(data.length > 0){
					form.slaDeliveryTimeList=data;							
				}
			})
            
            if(data.slaDeliveryTime != null || data.slaDeliveryTime != undefined){
            	form.slaDeliveryTime = data.slaDeliveryTime;
            }else{
            	form.slaDeliveryTime = 0;
            }
            if (typeof form.approvalProcess.approvalLevel !== 'undefined' && form.approvalProcess.approvalLevel != null) {
                $scope.isUserApproval = false;
            } else {
                $scope.isUserApproval = true;
            }
            
            $scope.getApprovalProcess(form.approvalProcess.approvalProcessType.id);
            $scope.getApprovalProcessAndStatus(form.approvalProcess.approvalProcessType.id);
            $scope.getUploadPurchaseRequest(form.approvalProcess.approvalProcessType.valueId);

            $scope.getPRItem(form.approvalProcess.approvalProcessType.valueId);
        }).error(function (data) {
            $scope.loading = false;
        });

        $scope.getUploadPurchaseRequest = function (purchaseRequest) {
            $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + purchaseRequest).success(function (data) {
                form.uploadPurchaseRequest = data;

                form.dokumenPr = [];
                angular.forEach(data, function (value) {
                    var file = {};
                    file.fileName = value.uploadPrFileName;
                    file.downloadFile = $rootScope.backendAddress + '/file/view/' + value.uploadPrRealName;
                    form.dokumenPr.push(file);
                })


            }).error(function (data) {
                $scope.loading = false;
            });

        }

        $scope.getApprovalConfirmationVendorList = function() {
        	$scope.loading = false;
			RequestService
					.doPOSTJSON('/procurement/master/approvalProcessVendor/getApprovalConfirmationVendorList', form.approvalProcess.approvalProcessType.valueId)
					.then(function success(data) {
						form.approvalConfirmationVendorList = data;
						$scope.loading = false;
					}, function error(response) {
						RequestService.informInternalError();
						$scope.loading = false;
					});
		}
		$scope.getApprovalConfirmationVendorList();
		
		$scope.getApprovalInternalLogList = function() {
			$scope.loading = false;
			RequestService
					.doPOSTJSON('/procurement/approvalProcessServices/getApprovalInternalLogList', form.approvalProcess.approvalProcessType.id)
					.then(function success(data) {
						form.approvalInternalLogList = data;
						$scope.loading = false;
					}, function error(response) {
						RequestService.informInternalError();
						$scope.loading = false;
					});
		}
		$scope.getApprovalInternalLogList();

        
        $scope.btnSimpan = function (statusId) {
            $scope.errorNoteMessage = "";

            var checkNote = form.note;
            var isNote = true;
            if (form.note === null || form.note === '') {
                isNote = false;
                $scope.errorNoteMessage = "Note tidak boleh kosong";
            } else if (checkNote.length >= 255) {
                isNote = false;
                $scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
            } else {

//                if (statusId == 1) { // when is approve, chek is COA available
//
//                    var isCostAvailable = true;
//                    angular.forEach(form.purchaseRequestItemList, function (value, index) {
//                        if (!value.costAvailable) {
//                            isCostAvailable = false;
//                        }
//                    });
//
//                    if (!isCostAvailable) {
//                        ModalService.showModalConfirmation('Cost Allocation tidak mencukupi, Apakah Anda Yakin ingin menyetujuinya ?').then(function (result) {
//                            ModalService.showModalInformationBlock();
//                            $scope.simpan(statusId);
//                        });
//                    } else {
//                        ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
//                            ModalService.showModalInformationBlock();
//                            $scope.simpan(statusId);
//                        });
//                    }
//
//                } else {

                    ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
                        ModalService.showModalInformationBlock();
                        $scope.simpan(statusId);
                    });
//                }

            }
        }

        $scope.simpan = function (statusId) {
           var ApprovalDoProcessDTO = {
                id: form.approvalProcess.approvalProcessType.valueId,
                status: statusId,
                note: form.note,
                approvalProcessId: form.approvalProcess.id
            };

            RequestService.doPOSTJSON('/procurement/approvalProcessServices/doProcess', ApprovalDoProcessDTO)
                .then(function successCallback(data) {
                	if(angular.equals(data.status, "ERROR")){
                		ModalService.closeModalInformation();
                		ModalService.showModalInformation("Response Error from SAP : "+data.statusText +". Please click Resend button");
                		$location.path('app/promise/procurement/approval');
                	}else{
                		ModalService.closeModalInformation();
                		ModalService.showModalInformation("Success send data BO");
                		$location.path('app/promise/procurement/approval');
                	}
                }, function errorCallback(response) {
                    ModalService.closeModalInformation();
                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                });
        }

        $scope.getApprovalProcess = function (approvalProcessType) {
            $scope.loadingApproval = true;
            form.levelList = [];
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function (data) {
                form.levelList = data;
                $scope.loading = false;
            }).error(function (data) {
                $scope.loading = false;
            });
        };

        //Approval log
        $scope.getApprovalProcessAndStatus = function (approvalProcessType) {
            $scope.loadingApproval = true;
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + approvalProcessType)
                .success(function (data) {
                    $scope.statusList = data;
                    $scope.loading = false;
                }).error(function (data) {
                    $scope.loading = false;
                });
        };

        form.purchaseRequestItemList = [];
        form.tempBulkPriceDiskon=[];
        
        form.shipping = [];
        $scope.getPRItem = function (prId) {
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findPurchaseRequestItemByPR/' + prId).success(function (data) {
                $scope.prItemList = data;
                form.prItemList = data;
                //console.log(">> cek requestitemlist:"+JSON.stringify(data));
                form.prItemList.grandTotal=0;
				
				angular.forEach(data, function(value1,index1){
					RequestService.doPOSTJSON('/procurment/catalog/catalog-bulk-price/get-list-by-catalog-id', value1.purchaseRequestItem.catalog.id)
					.then(function successCallback(data2) {
						angular.forEach(data2, function(value2,index2){
							if(value2!=null){
								if((value1.purchaseRequestItem.quantity >= value2.minQuantity && value1.purchaseRequestItem.quantity <= value2.maxQuantity) || value1.purchaseRequestItem.quantity > value2.maxQuantity){
									form.tempBulkPriceDiskon[index1]=value2.diskon;
								}
							}
						});
					});
					if(form.tempBulkPriceDiskon[index1]==null){
						form.tempBulkPriceDiskon[index1]=0;
					}
//					form.asuransi[index1]=false;
//					if(form.purchaseRequestItemList[index1].asuransi>0){
//						form.asuransi[index1]=true;
//					}
					form.prItemList.grandTotal+=value1.purchaseRequestItem.quantity * value1.purchaseRequestItem.price-((form.tempBulkPriceDiskon[index1]/100) * (value1.purchaseRequestItem.price * value1.purchaseRequestItem.quantity));
				});
				
				
                angular.forEach(data, function (value, index) {
                	// KAI - 20210204 - #20867
                	if (form.shipping.length == 0){
                		form.shipping.push(value.purchaseRequestItem.addressBook);
                	} else {
                		var isSame = false;
                		angular.forEach(form.shipping, function (shipping, indexShipping) {
                			if (shipping.id == value.purchaseRequestItem.addressBook.id ){
                				isSame = true;
                			}
                		})
                		
                		if(!isSame){
                			form.shipping.push(value.purchaseRequestItem.addressBook);
                		}	
                	}
                	
                	
                	value.purchaseRequestItem.grandTotal=0;
                    var obj = value.purchaseRequestItem;
                    obj.costAvailable = value.costAvailable;
                    form.purchaseRequestItemList.push(obj);
                });
                
                // KAI - 20210204 - #20867
                form.prItemListNew = [];
                form.totalHargaShipTo = [];
                form.totalOngkirShipTo = [];
                form.subTotalShipTo = [];
                angular.forEach(form.shipping, function (shipping, index) {
                	var prItemTemp = [];
                	var totalHargaShipTo = 0;
                	var totalOngkirShipTo = 0;
                	var subTotalShipTo = 0;
                	 angular.forEach(data, function (value, index2) {
                		 if (shipping.id == value.purchaseRequestItem.addressBook.id ){
                			 prItemTemp.push(value.purchaseRequestItem);
                			 totalHargaShipTo += value.purchaseRequestItem.total;
                			 totalOngkirShipTo += value.purchaseRequestItem.ongkosKirim;
                		 } 
                	 })
                	 form.prItemListNew.push(prItemTemp);
                	 form.totalHargaShipTo.push(totalHargaShipTo);
                	 form.totalOngkirShipTo.push(totalOngkirShipTo);
                	 form.subTotalShipTo.push(totalHargaShipTo+totalOngkirShipTo);
                });
                form.priceIncludePPN = [];
        		angular.forEach(form.prItemListNew, function(prItem,index){
        			var tamPriceIncludePPN =[];
        			angular.forEach(prItem, function(data, index2){
        				tamPriceIncludePPN.push(data.catalog.harga);
        				if(data.catalog.catalogKontrak.isPpn == 1){
        					tamPriceIncludePPN[index2] = data.catalog.harga * (10/11);
        				}        				
        			});
        			form.priceIncludePPN.push(tamPriceIncludePPN);
        		});
                
               
				
                $scope.loading = false;
            }).error(function (data) {
                $scope.loading = false;
            });

            $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + prId).success(function (data) {
                $scope.dataUploadPRList = data;
                //				console.log(">> id:"+JSON.stringify(data));

            });
        }

        $scope.btnInfoCoa = function (data) {
            $rootScope.dataItem = data;
            var modalInstance = $modal.open({
                templateUrl: '/info_coa.html',
                size: 'md',
                controller: modalInstanceInfoCoaController

            });
        }
        var modalInstanceInfoCoaController = function ($scope, $http, $modalInstance, $resource, $rootScope) {
            // console.log(">>> "+JSON.stringify($rootScope.dataItem));
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findCostAllocation/' + $rootScope.dataItem.costcenter).success(function (data) {
                if (data.length > 0) {
                    $scope.coa = data[0];
                    console.log(">>> " + JSON.stringify($scope.coa));
                }
                $scope.loading = false;
            });

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        modalInstanceInfoCoaController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        //dari mas mamat untuk menampilkan spek
        $scope.viewSpek = function (prItemId) {
            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getByPurchaseRequestItemId/' + prItemId)
                .success(function (data, status, headers, config) {

                    $scope.dataitem = data;
                    var modalInstance = $modal.open({
                        templateUrl: '/viewSpec.html',
                        controller: ModalViewSpecController,
                        scope: $scope
                    });
                    var message = "INFO :<br/>";
                    modalInstance.result.then(function () {

                    }, function () {

                    });

                })
        }

        var ModalViewSpecController = function ($scope, $rootScope, $modalInstance) {
            $scope.ok = function () {
                $modalInstance.dismiss('cancel');
            };

        };
        ModalViewSpecController.$inject = ['$scope', '$rootScope', '$modalInstance'];

        // console.info("USER :"+JSON.stringify($rootScope.userLogin));
        // console.info("ROLE : "+JSON.stringify($rootScope.userRole ));
        // console.info($rootScope.userOrganisasi);
    }
    ApprovalViewBOController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'ModalService', 'toaster'];

})();