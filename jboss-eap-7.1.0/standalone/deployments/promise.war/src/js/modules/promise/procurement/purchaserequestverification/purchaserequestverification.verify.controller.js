(function () {
    'use strict';
    angular.module('naut').controller('PurchaseRequestVerifyController', PurchaseRequestVerifyController);

    function PurchaseRequestVerifyController($scope, $http, $rootScope, $resource, $location, toaster, ngTableParams, $modal, $filter, RequestService, FileUploader, $stateParams, $q, Route, ModalService, $timeout) {
        var form = this;
        $scope.loading = true;
        $scope.message = "";
        $scope.messageError = "";
        $scope.messageItemError = "";
        
        
        $scope.getPR = function () {
            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/getPurchaseRequestId/' + $stateParams.purchaserequest.id).success(function (data, status, headers, config) {
                $scope.dataPR = data;
                
            	if (typeof data.status != 'undefined' || typeof data.status != 'null') {
            		//console.log('masuk 1');
            		if($scope.dataPR.status==7){
            			$scope.process = {
                                availableOptions: [{
                                    id: '0',
                                    name: '---Choose Process---'
                    			}, {
                                    id: '1',
                                    name: 'Direct PO'
                    			}/*, {
                                    id: '2',
                                    name: 'Procurement Process'
                    			}*/],
                                selectedOption: {
                                    id: '1',
                                    name: 'Direct PO'
                                }
                            };
            		}
            		else {
            			$scope.process = {
                                availableOptions: [{
                                    id: '0',
                                    name: '---Choose Process---'
                    			}, {
                                    id: '1',
                                    name: 'Direct PO'
                    			}/*, {
                                    id: '2',
                                    name: 'Procurement Process'
                    			}*/],
                                selectedOption: {
                                    id: '0',
                                    name: '---Choose Process---'
                                }
                            };
            		}
                }
            	else{
            		//console.log('masuk 2');
            		$scope.process = {
                            availableOptions: [{
                                id: '0',
                                name: '---Choose Process---'
                			}, {
                                id: '1',
                                name: 'Direct PO'
                			}/*, {
                                id: '2',
                                name: 'Procurement Process'
                			}*/],
                            selectedOption: {
                                id: '0',
                                name: '---Choose Process---'
                            }
                        };
            	}
            		
            });
        };

        $scope.getPR();
        
        var prObject = {};
        if ($stateParams.purchaserequest != undefined) {
            $scope.loading = false;
            prObject = $stateParams.purchaserequest;
            //console.log(">> PR : "+JSON.stringify(prObject));
            // Tab 1
            form.prnumber = prObject.prnumber;
            form.daterequired = prObject.daterequired;
            form.costcenter = prObject.costcenter;
            form.department = prObject.department
            form.description = (((typeof prObject.description !== 'undefined') && (prObject.description !== 'undefined')) ? prObject.description : '');

            // Tab 4
            form.termandcondition = prObject.termandcondition;
        } else {
            // console.log("No data ");
            $scope.loading = false;
        }

/*        $scope.department = {};
        $scope.getDepartmentList = function () {
            $scope.departmentList = [];
            $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + $rootScope.userOrganisasi.id) // user
                // 1
                // for
                // parentid
                .then(function (response) {
                    $scope.departmentList = response.data;
                    $scope.departmentList.push($rootScope.userOrganisasi);
                    angular.forEach($scope.departmentList, function (value, index) {
                        if (value.nama == prObject.department) {
                            form.department2 = value;
                        }
                    });
                })
        }
        $scope.getDepartmentList();*/

        $scope.btnKembaliIndex = function () {
            $scope.go('/app/promise/procurement/purchaserequestverification');
        }

        // Item PR
        $rootScope.itemPRList = [];
        $scope.getItemPR = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/' + prObject.id).success(function (response) {
                if (response.length > 0) {

                    var canDirect = true;
                    angular.forEach(response, function (data, index) {

                        var objectItem = {};
                        objectItem.prItemId = data.id;

                        if (!angular.isUndefined(data.item)) {
                            objectItem.material = data.item;
                        }

                        objectItem.nama = data.itemname;

                        if (!angular.isUndefined(data.vendorname)) {
                            objectItem.vendor = data.vendorname;

                        }
                        

                        if (typeof data.item === 'undefined' || data.vendor == null ) {
                            canDirect = false;
                        } else {
                            objectItem.vendorId = data.vendor.id;
                        }

                        if (typeof data.item === 'undefined' || data.item == null ) {
                            canDirect = false;
                        }
                       
                        
                        if (typeof data.kode == null || data.kode == 'null') {
                        	objectItem.kode = '';
                        } else {
                        	objectItem.kode = data.kode;
                        }

                        objectItem.quantity = data.quantity;
                        objectItem.price = data.price;
                        objectItem.unit = data.unit;
                        objectItem.matauang = 'IDR';
                        objectItem.matauangId = 1;
                        objectItem.specification = data.specification;
                        objectItem.pathfile = data.path;
                        objectItem.costcenteritem = data.costcenter;
                        
                        
                        $rootScope.itemPRList.push(objectItem);
                        $rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (objectItem.quantity * objectItem.price);
                    });

	                    /*if (canDirect == false) {
	                        $scope.process.availableOptions.splice(1, 1);
	                    }*/
                }
                $scope.loading = false;
            }).error(function (response) {
                console.log('error');
                $scope.loading = false;
            });
        };
        $scope.getItemPR();
        

        $rootScope.totalHPSItemPR = 0;
        // Replace Item from Master
        $scope.btnReplaceItem = function (prItem, index) {

                //console.log('replace item!');

                $rootScope.prItemSelected = prItem;
                $rootScope.prItemIndexSelected = index;
                $scope.errormessageItem = '';
                var modalInstance = $modal.open({
                    templateUrl: '/add_item.html',
                    size: 'lg',
                    controller: modalInstanceReplaceItemController

                });

                //after klik ok
                modalInstance.result.then(function (data) {
                   /* $scope.rehashProsesPengadaan();*/
                });
            }
            // Replace Item from Master - Controller
        var modalInstanceReplaceItemController = function ($scope, $http, $modalInstance, $resource, $rootScope) {

            $scope.form = $rootScope.prItemSelected;
            $scope.kodeMaterial = {};
            if (typeof $scope.form.kode !== 'undefined')
                $scope.kodeMaterial.selected = $scope.form.material;

            $scope.refreshkodeMaterialList = function (kodeMaterial) {
                $scope.loading = true;
                if (kodeMaterial.length == 0)
                    kodeMaterial = 0;
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial).then(function (response) {
                    $scope.kodeMaterialList = response.data;
                    $scope.loading = false;
                });
            };
            
            
            // Satuan List
            $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data) {
                $scope.satuanList = data;
            });
            // hitung total hps
            $scope.calculateTotalHPS = function () {
                if (typeof $scope.form.quantity !== 'undefined' && typeof $scope.form.price !== 'undefined') {
                    $scope.form.total = $scope.form.quantity * $scope.form.price;
                }
            };
            $scope.calculateTotalHPS();
			
		  	$scope.saveData= function (){
            
            	var valid = true;
            	 $rootScope.prItemIndexSelected
            		 angular.forEach($rootScope.itemPRList, function (item,index) {
            			 if(item.material != null){
            				 if (item.material.id == $scope.kodeMaterial.selected.id) {
                              	if($rootScope.prItemIndexSelected!= index){
                              		valid = false;
                              		RequestService.informError("Tidak boleh memilih Item yang sama");
                              	}                      
                              }	 
            			 }
                        
                     });  
            	return valid;
            }	
			
            $scope.btnSimpanItem = function () {
            	if($scope.saveData()){
                $scope.loading = true;
                $scope.form.nama = $scope.kodeMaterial.selected.nama;
                $scope.form.material = $scope.kodeMaterial.selected;
                $scope.form.kode = $scope.kodeMaterial.selected.kode;
                $scope.form.vendor = null;
                
                //console.log($scope.form.kode);
                if (typeof $scope.form.vendorId !== 'undefined')
                    delete $scope.form.vendorId;
                
                $scope.form.isEdited = true;
                //console.log("cek : " + JSON.stringify($scope.form));
                $rootScope.itemPRList.splice($rootScope.prItemIndexSelected, 1, $scope.form);
                //console.log($rootScope.itemPRList);
                //console.log($rootScope.prItemIndexSelected);
                
                angular.forEach($rootScope.shippingList, function (shippingGroup, shippingGroupIndex) {
                    angular.forEach(shippingGroup.purchaseVerificationShippingItemDTO, function (pvShippingItem, pvShippingItemIndex) {
                        if (pvShippingItem.purchaseRequestItemId == $scope.form.prItemId) {
                            pvShippingItem.itemName = $scope.form.nama;
                            pvShippingItem.quantityPRItem = $scope.form.quantity;
                            pvShippingItem.isEdited = true;
                        }
                    });
                });

                $scope.loading = false;
                $modalInstance.close('closed');}
            	else{
            		$scope.cancel();
            	}
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            var PurchaseRequestAddItemController = function ($scope, $http, $rootScope, $resource, $location, toaster, $modalInstance, $timeout) {

                $scope.satuan = {};
                $scope.tipeItem = {};

                // tree
                $scope.my_tree_handler = function (branch) {
                    $scope.output = branch.label;
                    $scope.treeId = branch.id;

                };

                var tree;
                // This is our API control variable
                $scope.my_tree = tree = {};
                $scope.try_async_load = function () {

                    $scope.my_data = [];
                    $scope.doing_async = true;

                    // Request tree data via $resource
                    var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/item-group/get-all');

                    // var

                    return remoteTree.get(function (res) {

                        $scope.my_data = res.data;

                        $scope.doing_async = false;

                        return tree.expand_all();

                        // we must return a promise so the plugin
                        // can watch when it's resolved
                    }).$promise;
                };
                $scope.try_async_load();

                // satuan
                $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data, status, headers, config) {
                    $scope.satuanList = data;
                }).error(function (data, status, headers, config) {})

                // type item
                $http.get($rootScope.backendAddress + '/procurement/master/item-type/get-list').success(function (data, status, headers, config) {
                    $scope.tipeItemList = data;
                }).error(function (data, status, headers, config) {})

                $scope.kembaliItem = function () {
                    $modalInstance.dismiss('cancel');
                };

                $scope.simpanItem = function () {

                    if (typeof $scope.nama === 'undefined' || $scope.nama == '') {
                        alert("Nama Item belum disi.");
                    } else if (typeof $scope.kode === 'undefined' || $scope.kode == '') {
                        alert("Kode Item belum diisi.");
                    } else if (typeof $scope.satuan.selected === 'undefined') {
                        alert("Satuan belum dipilih.");
                    } else if (typeof $scope.tipeItem.selected === 'undefined') {
                        alert("Tipe Item belum dipilih.");
                    } else if (typeof $scope.output == 'undefined' || $scope.output == '') {
                        alert("Group Item belum dipilih");
                    } else {
                        // console.log(JSON.stringify(form));
                         var item = {
                            nama: $scope.nama,
                            kode: $scope.kode,
                            deskripsi: $scope.deskripsi,
                            satuanId: {id:$scope.satuan.selected.id},
                            itemType: {id:$scope.tipeItem.selected.id}
                             
                        }
                        //console.log(JSON.stringify(postForm));
                        $scope.loading = true;
                        var url = '/procurement/master/item/insert';
            				
            				
            				var itemDTO = {};
            				itemDTO.item = item;
            				itemDTO.itemGroupId = $scope.treeId;
            				RequestService.doPOSTJSON(url, itemDTO)
            						.then(function success(data) {
            									if (data != undefined) {
            										if (data.isValid != undefined) {
            											if (data.errorKode != undefined) {
            												toaster.pop('error', 'Kode sudah terdaftar');
            												$scope.loading = false;
            											}
            										} else {
            											toaster.pop('Success', 'Item', 'Simpan ' + data.nama + ', berhasil.');
            			                                $timeout(function(){
            			                                    $modalInstance.close('closed');
            			                                }, 2000);
            										}
            									}
            								},
            								function error(response) {
            									RequestService.informError("Terjadi Kesalahan Pada System");
            				});
            		};
                };

            }
            PurchaseRequestAddItemController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modalInstance', '$timeout'];

            $scope.createItem = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/views/promise/procurement/purchaserequestverification/purchaserequestverification.addItem.html',
                    controller: PurchaseRequestAddItemController,
                    size: 'lg'
                });
            };

        };
        modalInstanceReplaceItemController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        // Add Item from Katalog
        $scope.replaceItemKatalog = function (prItem, index) {

            $rootScope.prItemSelected = prItem;
            $rootScope.prItemIndexSelected = index;
            $scope.errormessageItem = '';

            //console.log(' prItemSelected = ' + JSON.stringify($rootScope.prItemSelected));

            var modalInstance = $modal.open({
                templateUrl: 'app/views/promise/procurement/purchaserequestverification/purchaserequestverification.replaceItemKatalog.html',
                controller: modalInstanceReplaceItemKatalogController,
                size: 'lg'
            });
            //after klik ok
            modalInstance.result.then(function (data) {
               /* $scope.rehashProsesPengadaan();*/
            });

        };

        //scope funciton for dynmic option proses pengadaan
        /*$scope.rehashProsesPengadaan = function () {
            var canDirect = true;
            angular.forEach($rootScope.itemPRList, function (data, index) {
                //console.log("test data : " + JSON.stringify(data));
                if (typeof data.vendor === 'undefined' || data.vendor == null) {
                    canDirect = false;
                }
                if (typeof data.material === 'undefined' || data.material == null) {
                    canDirect = false;
                }else{
                	if (typeof data.material.item === 'undefined' || data.material.item == null) {
                        canDirect = false;
                    }
                }
                	
                
                

            });
            //console.log("canDiret ? " + canDirect);
            if (!canDirect) {
                $scope.process = {
                    availableOptions: [{
                        id: '0',
                        name: '---Choose Process---'
                        }, {
                        id: '2',
                        name: 'Procurement Process'
                        }],
                    selectedOption: {
                        id: '2',
                        name: 'Procurement Process'
                    }
                };
            } else {
                $scope.process = {
                    availableOptions: [{
                        id: '0',
                        name: '---Choose Process---'
                        }, {
                        id: '1',
                        name: 'Direct PO'
                        }],
                    selectedOption: {
                        id: '1',
                        name: 'Direct PO'
                    }
                };
            }
        }*/

        // MODAL ADD CONTROLLER
        var modalInstanceReplaceItemKatalogController = function ($scope, $http, $modalInstance, $resource) {
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.ok = function () {
                $modalInstance.close('closed');
            }
        };
        modalInstanceReplaceItemKatalogController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        // validate
        $scope.validate = function () {
            var isValid = true;
            $scope.errormessageProcess = '';
            if ($scope.process.selectedOption.id <= 0) {
                isValid = false;
                $scope.errormessageProcess = 'Proses harus dipilih!';
            }

            return isValid;
        }

        // SHIPPING TO
        $rootScope.shippingList = [];
        $http.get($rootScope.backendAddress + '/procurement/PurchaseRequestVerificationServices/findByPR/' + prObject.id).success(function (data) {
            // console.log("... ok : "+JSON.stringify(data));
            $rootScope.shippingList = data;
            //console.log(data);
        });

        $scope.btnAddShipping = function () {
            if ($rootScope.itemPRList.length == 0) {
                alert("Item harus diisi terlebih dahulu!");
            } else {
                $scope.statusRemove = false;
                var shipTo = {
                    fullName: null,
                    telephone: null,
                    address: null,
                    deliveryTime: null
                };
                $scope.shippingList.push(shipTo);
            }
        }

        // getAtributteAddress
        $scope.setShippingAddress = function (index, companyAddressSelected) {
            if (companyAddressSelected.isNew != true) {
                $scope.shippingList[index].fullName = companyAddressSelected.fullName;
                $scope.shippingList[index].telephone = companyAddressSelected.telephone1;
                $scope.shippingList[index].deliveryTime = companyAddressSelected.deliveryTime;
                
                $scope.shippingList[index].address = companyAddressSelected.addressLabel + ', ' + companyAddressSelected.streetAddress + ', ' + companyAddressSelected.city + ', ' + companyAddressSelected.country;
            } else {
                $scope.shippingList[index].fullName = '';
                $scope.shippingList[index].telephone = '';
                $scope.shippingList[index].address = '';
                $scope.shippingList[index].deliveryTime = '';
                // console.log('>> new address');
            }
        };

        $scope.statusRemove = true;
        $scope.btnDelShipping = function () {
            $scope.shippingList.splice(-1);
        };

        // Approval
        form.approval = {};
        $scope.approvalList = [];
        $scope.approvalIsChange = false; // jika true, hapus data approval
        // sebelumnya, ganti pilihan baru.
        $scope.getApprovalList = function () {
            $http.get($rootScope.backendAddress + '/procurement/approval/get-list').then(function (response) {
                $scope.approvalList = response.data;
                $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByTahapanAndId/CREATE_PR/' + prObject.id).success(function (data) {
                    // jika tidak memakai approval template
                    if (typeof data.approval === 'undefined' || data.approval == null) {
                        form.approval.selected = 0;
                        // get approval process
                        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + data.id).success(function (data) {
                        	form.approval.newSelected = data;
                        	
                            // get user approval
                            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findRoleUser/' + data[0].user.id).success(function (data) {
                                //form.approval.newSelected = data[0];
                                //console.log(form.approval.newSelected);
                            });
                        }).error(function (data) {
                            console.error("Error get Process Approval Type");
                        })

                    } else {
                        form.approval.selected = data.approval.id;
                        form.approval.processTypeId = data.id;
                        $http.get($rootScope.backendAddress + '/procurement/approval/approvalLevel/' + form.approval.selected).then(function (responseApproval) {
                            form.approval.levelList = responseApproval.data;
                            $scope.loadingApproval = false;
                        });
                    }

                });
            });
        }
        $scope.getApprovalList();

        $scope.getApprovalLevel = function () {
            $scope.loadingApproval = true;
            form.approval.levelList = [];
            $http.get($rootScope.backendAddress + '/procurement/approval/approvalLevel/' + form.approval.selected).then(function (response) {
                form.approval.levelList = response.data;
                $scope.loadingApproval = false;
                $scope.approvalIsChange = true;
            })
        };

        $scope.approvalNewList = [];
        $scope.getApprovalNewList = function (name) {
            if (name.length > 0) {
                $scope.loadingApproval = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/approval/roleuserlist',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: {
                        name: name
                    }
                }).success(function (data, status, headers, config) {
                    $scope.loadingApproval = false;
                    $scope.approvalNewList = data;
                });
            }

        };

        // save Purchase Verification
        $scope.btnDisable = false;
        $scope.btnSimpanPR = function () {
        	$scope.btnDisable = true;
/*            
            if (typeof $rootScope.itemPRList.kode === 'undefined' || $rootScope.itemPRList.kode == null) {
            	$scope.messageItemError = "Item Code tidak ditemukan, Silahkan verifikasi item terlebih dahulu!";              
            }*/
        	
        	if ($scope.process.selectedOption.id > 0) {
        		
        		$scope.messageError = "";
        		
                // cek if directPO
               /* if ($scope.process.selectedOption.id == 1) { // direct PO
                    var numECatalog = 0;
                    angular.forEach($rootScope.itemPRList, function (itemPR, itemPRIndex) {
                        // cek is eCatalog
                        if ((!angular.isUndefined(itemPR.vendorId) && (itemPR.vendorId != null)) && (itemPR.material != null)) {
                            numECatalog++;
                        }

                    })

                    // if direcPO all item must be from eCatalog
                    //console.log('numECatalog                  = ' + numECatalog);
                    //console.log('$rootScope.itemPRList.length = ' + $rootScope.itemPRList.length);
                    if (numECatalog < $rootScope.itemPRList.length) {
                        ModalService.showModalInformation("Anda memilih Direct PO, item harus berasal dari eCatalog");
                        return;
                    }

                }*/
                //console.log($rootScope.itemPRList);
                var isFreeItem = false;
                var isECatalog = true;

                angular.forEach($rootScope.itemPRList, function (itemPR, itemPRIndex) {

                    // cek is free Item
                    if ((angular.isUndefined(itemPR.vendorId) || (itemPR.vendorId == null)) && (itemPR.material == null)) {
                        isFreeItem = true;
                    }

                    // cek is eCatalog
                    if ((angular.isUndefined(itemPR.vendorId) || (itemPR.vendorId == null)) || (itemPR.material == null)) {
                        isECatalog = false;
                    }

                })
                //console.log($rootScope.itemPRList);
                
        		var isItemCode = false;
        		
            	angular.forEach($rootScope.itemPRList, function (itemPR) {
            		
            		if (typeof itemPR.kode === 'undefined' || itemPR.kode == '' || itemPR.kode == null) {
            			$scope.btnDisable = false;
            			
                    	$scope.messageError = "Item Code tidak ditemukan, Silahkan verifikasi item terlebih dahulu!";
                    	isItemCode = false;
                    	
                    } else {
                    	$scope.messageError = '';
                    	isItemCode = true;
                    }
            		
            	})
                
            	//console.log(isFreeItem);
                //console.log(isItemCode);
            	//console.log(isECatalog);
            	
                if (isFreeItem) {
                    ModalService.showModalInformation("Masih ada free item, replace menggunakan master item terlebih dahulu");
                } 
                else if (isECatalog && isItemCode) {
                    ModalService.showModalConfirmation('Apakah Anda yakin akan mencari harga terbaik dari Catalog yang sudah memiliki kontrak?').then(function (result) {

                    $scope.doSimpan();

                    }, function(close) {
	                   $scope.btnDisable = false;
	                });   
                } else if (isItemCode){
                    $scope.doSimpan();
                }
                	 
            }
           
            else {
            	$scope.btnDisable = false;
                $scope.messageError = "PR Process belum dipilih!";
                return false;
            }
        };

        /* loadDokumen */
        $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + prObject.id, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.dokPrList = data;
            form.loading = false;
        }).error(function (data, status, headers, config) {
            form.loading = false;
        });

        $scope.doSimpan = function () {
        		$scope.successMessage = '';

                RequestService.modalConfirmation('Anda yakin ingin menyimpan data Purchasing Request?').then(function () {
                    $scope.loading = true;
                    // update pr item
                    angular.forEach($rootScope.itemPRList, function (itemPR, itemPRIndex) {
                    	//console.log(itemPR);
                        //console.log('itemPR = ' + JSON.stringify(itemPR));
                        //console.log('itemPR.vendorId X = ' + itemPR.vendorId);
                        if (itemPR.isEdited == true) {
                            // update pr line
                            var postObject = {
                                pritemid: itemPR.prItemId,
                                itemId: itemPR.material.id

                            };
                            //if has vendor
                            if (!angular.isUndefined(itemPR.vendorId)) {
                                postObject.vendorId = itemPR.vendorId;
                            }
                            //if direct po & katalog
                            if(typeof itemPR.isFromCatalog !== 'undefined'){
                                postObject.isFromCatalog=1;//true
                            } else{
                                postObject.isFromCatalog=0;//false
                            }

                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/PurchaseRequestVerificationServices/updatepritem',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: postObject
                            }). success(function (data, status, headers, config) {});

                        }
                    });
                    // update process
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/PurchaseRequestVerificationServices/updateprstatus',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: {
                            prId: prObject.id,
                            processId: $scope.process.selectedOption.id
                        }
                    }).success(function (data, status, headers, config) {});
                    	$scope.loading = true;
                    	$scope.successMessage = 'PR Verification Berhasil Disimpan';
                    	$scope.loading = false;
                    	$timeout( function(){
                    		$scope.btnKembaliIndex();
                        }, 2000 );
                }, function(close){
                   $scope.btnDisable = false;
                });

            }
            // test isi shipping
        $scope.testButton = function () {
            //console.log(">> select " + JSON.stringify($scope.process));
            // console.log(">>> SHIPPING :" +
            // JSON.stringify($scope.shippingList));
            // console.log(">>> PRITEM:" +
            // JSON.stringify($rootScope.itemPRList));
            // console.log(">>> PRITEM DELTED:" +
            // JSON.stringify($rootScope.itemPRListDeleted));

        };

        $scope.go = function (path) {
            $location.path(path);
        };
    }
    PurchaseRequestVerifyController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'ngTableParams', '$modal', '$filter', 'RequestService', 'FileUploader', '$stateParams', '$q', 'Route', 'ModalService', '$timeout'];

})();