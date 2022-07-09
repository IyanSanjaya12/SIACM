(function () {
    'use strict';

    angular.module('naut').controller('approvalViewCatalogController', approvalViewCatalogController);

    function approvalViewCatalogController($scope, $rootScope, ModalService, RequestService, $modal, $state,  FileUploader, $stateParams, toaster, $window, $location) {
    	var vm = this;
		$scope.catalog = {};
		$scope.catalogContract = {};
		$scope.priceRangeList = [];
		$rootScope.catalog={};
		$scope.itemOrganisasiList={};
		$scope.catalogHistory={};
		vm.isErrorMinQty=false;
		vm.isErrorMaxQty=false;
		vm.isErrorEmpty=false;
		vm.bulkToDo='';
		vm.price=[];
		vm.todo='';
		vm.status="New";
		vm.harga=false;
    	vm.namaIND =false;
    	vm.deskripsiIND=false;
    	vm.kodeProd=false;
    	vm.beart=false;
    	vm.minQty=false;
    	vm.stok=false;
    	vm.panjang=false;
    	vm.lebar=false;
    	vm.tinggi=false;
		vm.note='';
		vm.idKontrak=null;
		$scope.errorNoteMessage='';
		if ($stateParams.dataCatalog != undefined) {
			if($scope.catalog.currentStock==null){
				$scope.catalog.currentStock=0;
			}
			$scope.catalog = $stateParams.dataCatalog;
		}else{
			$scope.catalog.currentStock=0;
		}
		vm.todo=$stateParams.toDo;
		vm.approvalProcess = $stateParams.dataApprovalProcess;
		
		$rootScope.catalog=$scope.catalog;
		var cekCategoryListOK = false;
		$scope.supportedFile = "Supported file:" + $rootScope.fileTypeImg.toLowerCase().replace(/\s+/g, '') + ". Max Size:" + $rootScope.fileUploadSize / (1024 * 1024) + " MB";
		// category
		$scope.dataCategoryTemp;
		$scope.arrCategoryParentListId;
		//Get Role User
		RequestService.getRoleUsers(RequestService.getUserLogin().user.id).then(function (data) {
			$scope.roleUser = data;
		});
		$scope.panes = [{
			heading: 'GENERAL',
			include: 'app/views/promise/procurement/approval/approval.catalog.general.html',
			active: true,
			disabled:true
		}, {
			heading: 'CATEGORIES',
			include: 'app/views/promise/procurement/approval/approval.catalog.categories.html',
			active: false,
			disabled:true
		},{
			heading: 'IMAGES',
			include: 'app/views/promise/procurement/approval/approval.catalog.images.html',
			active: false,
			disabled:true
		}, {
			heading: 'SPECIFICATION',
			include: 'app/views/promise/procurement/approval/approval.catalog.specification.html',
			active: false,
			disabled:true
		}];
		$scope.backToGeneral = function () {
			$scope.panes[0].active = true
		}
		$scope.goToCategories = function () {
			$scope.panes[1].active = true;
		}
		$scope.backToCategories = function () {
			$scope.panes[1].active = true
		}
		$scope.goToImages = function () {
			$scope.panes[2].active = true;
		}
		$scope.backToImages = function () {
			$scope.panes[2].active = true;
		}
		$scope.goToSpecification = function () {
			$scope.catalog.catalogImageList = [];
			angular.forEach($scope.uploader.queue, function (item, indexItem) {
				var catalogImage = {
					imagesFileName: item.file.name,
					imagesRealName: item.imagesRealName,
					imagesFileSize: item.file.size,
					status: true,
					urutanPesanan: indexItem + 1
				};
				$scope.catalog.catalogImageList.push(catalogImage);
			});
			/*if ($scope.catalog.catalogImageList.length == 0) {
				toaster.pop('error', 'Kesalahan', 'Images harus diisi');
				$scope.panes[3].active = false;
			}else{*/
			$scope.panes[3].active = true;
//			}
		}
		// Tab Image
		$scope.uploader = new FileUploader({
			url: RequestService.uploadURL()
		});
		
		$scope.uploader.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true, "img") && (this.queue.length < 10));
			}
		});
		$scope.uploader.onCompleteItem = function (fileItem, response, status, headers) {
			fileItem.imagesRealName = response.fileName;
		};
		
		$scope.uploader2 = new FileUploader({
			url: RequestService.uploadURL()
		});
		$scope.uploader2.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true, "img") && (this.queue.length < 10));
			}
		});
		$scope.uploader2.onCompleteItem = function (fileItem, response, status, headers) {
			fileItem.imagesRealName = response.fileName;
		};
		
		$scope.resetData=function() {
			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findCatalogPropertiesApproval', $scope.catalog).then(function (data) {
				vm.isEditable = data.isEditable;
				$scope.tempCatalog = $scope.catalog.kodeProduk;
				if ($scope.tempCatalog) {
					$scope.tempCatalog = $scope.catalog;
				}
//				$scope.catalog.status = ($scope.catalog.status != undefined && $scope.catalog.status) ? "true" : "false";
				$scope.catalog.approvalStatus = $scope.catalog.approvalStatus != undefined ? "0" : "1";
				$scope.attributeGroupList = data.attributeGroupList;
				$scope.itemTypeList = data.itemTypeList;
				$scope.productTypeList = data.productTypeList;
				$scope.mataUangList = data.mataUangList;
				$scope.vendorList = data.vendorList;
				$scope.satuanList = data.satuanList;
				$scope.catalog.categoryList = data.categoryList;
				$scope.catalog.catalogLocationList = data.catalogLocationList;
				$scope.catalog.catalogImageList = data.catalogImageList;
				$scope.catalog.catalogPriceRangeList = data.catalogPriceRangeList;
				$scope.catalog.catalogBulkPriceList 	= data.catalogBulkPriceList;
				$scope.catalog.catalogStockList 	= data.catalogStockList;
				$scope.catalog.catalogFeeList 	= data.catalogFeeList;
				if ($scope.catalog != undefined) {
					if ($scope.catalog.attributeGroup != undefined) {
						angular.forEach($scope.attributeGroupList, function (attributeGroup) {
							if (attributeGroup.id == $scope.catalog.attributeGroup.id) {
								$scope.catalog.attributeGroup = attributeGroup;
							}
						});
					}
					if ($scope.catalog.catalogImageList != undefined) {
						angular.forEach($scope.catalog.catalogImageList, function (catalogImage, indexImage) {
							var item = new FileUploader.FileItem($scope.uploader, {
								size: catalogImage.imagesFileSize,
								name: catalogImage.imagesFileName
							});
							item.isUploading = false;
							item.isReady = false;
							item.progress = 100;
							item.isUploaded = true;
							item.isSuccess = true;
							item.imagesRealName = catalogImage.imagesRealName;
							item.loadFile = RequestService.loadURL(catalogImage.imagesRealName);
							$scope.uploader.queue.push(item);
						});
					}
					
					//setPrice
					
					$scope.validMinQty=function(qtyMaxNew,diskonNew,qtyMinNew,qtyMaxOld){
	    				if(vm.bulkToDo=='add'){
	    					
	    					if(parseInt(qtyMinNew) < parseInt(qtyMaxOld) ){
	        					vm.isErrorMinQty=true;    					
	        				}else if(parseInt(qtyMinNew) > parseInt(qtyMaxNew)){
	        					vm.isErrorMinQty=true;
	        				}else{
	        					if(qtyMinNew=='' || qtyMaxNew=='' || diskonNew==''){
	        						vm.isErrorEmpty=true;
	        					}else{
	        						if(parseInt(qtyMinNew) < parseInt(qtyMaxNew) && parseInt(qtyMinNew) > parseInt(qtyMaxOld)){
	        							vm.isErrorMaxQty=false;
	        						}
	        						vm.isErrorMinQty=false;
	        						vm.isErrorEmpty=false;
	        					}
	    					}
	    				}if(vm.bulkToDo=='edit'){
	    					if(parseInt(qtyMinNew) < parseInt(qtyMaxOld) || parseInt(qtyMinNew) > parseInt(qtyMaxNew)){
	        					vm.isErrorMinQty=true;    					
	        				}else if(parseInt(qtyMinNew) > parseInt(qtyMaxNew)){
	        					vm.isErrorMinQty=true;
	        				}else{
	        					if(qtyMinNew!='' && qtyMaxNew!='' && diskonNew==''){
	        						vm.isErrorMinQty=false;
	        						vm.isErrorEmpty=false;
	        					}else{
	        						vm.isErrorEmpty=true;
	        					}
	    					}
	    				}
	    				
	    			}
	    			
	    			$scope.validMaxQty=function(qtyMaxNew,diskonNew,qtyMinNew,qtyMaxOld,qtyMinNext){
//	    				console.log(qtyMaxNew,diskonNew,qtyMinNew,qtyMaxOld,qtyMinNext);
	    				if(vm.bulkToDo=='add'){
	    					
	    					if(parseInt(qtyMaxNew) < parseInt(qtyMinNew) ){
	        					vm.isErrorMaxQty=true;    					
	        				}else if(parseInt(qtyMaxNew) > parseInt(qtyMinNext)){
	        					vm.isErrorMinQty=true;
	        				}else{
	        					if(qtyMinNew=='' || qtyMaxNew=='' || diskonNew==''){
	        						vm.emptyBulk=true;
	        					}else{
	        						if(parseInt(qtyMinNew) > parseInt(qtyMaxOld) && parseInt(qtyMinNew) < parseInt(qtyMaxNew) ){
	        							vm.isErrorMinQty=false;
	        						}
	        						vm.emptyBulk=false;
	        						vm.isErrorMaxQty=false;
	        					}	
	    					}
	    				}if(vm.bulkToDo=='edit'){
	    					if(parseInt(qtyMaxNew) > parseInt(qtyMinNew)){
								vm.isErrorMinQty=false;
							}
	    					if(parseInt(qtyMaxNew) < parseInt(qtyMinNew) || parseInt(qtyMaxNew) > parseInt(qtyMinNext)){
	    						vm.isErrorMaxQty=true;    					
	        				}else{
	        					if(qtyMinNew!='' && qtyMaxNew!='' && diskonNew!=''){
	        						if(parseInt(qtyMaxNew) > parseInt(qtyMinNew)){
	        							vm.isErrorMaxQty=false;
	        							vm.emptyBulk=false;
	        						}
									
	        					}else{
	        						vm.emptyBulk=true;
	        					}	
	    					}
	    				}
	    				
	    			}
					if ($scope.roleUser) {
						RequestService.doGET('/procurement/vendor/vendorServices/getVendorByUserId/' + RequestService.getUserLogin().user.id).then(function (data) {
							$scope.catalog.vendor = data;
						})
					}
					$scope.getCatalogHistory();
					
				}
			})
		}
		$scope.resetData();
		$scope.getCatalogHistory=function(){
		    RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/get-catalog-history-by-catalog', $scope.catalog).then(function (data) {
				$scope.catalogHistory = data;
				$scope.checkTodo();
				/* perubahan KAI 14/12/2020 [20852]*/
				if ($scope.catalogHistory.catalogImageHistoryList != undefined) {
					angular.forEach($scope.catalogHistory.catalogImageHistoryList, function (catalogImageHistory, indexImage) {
						var item = new FileUploader.FileItem($scope.uploader2, {
							size: catalogImageHistory.imagesFileSize,
							name: catalogImageHistory.imagesFileName
						});
						item.isUploading = false;
						item.isReady = false;
						item.progress = 100;
						item.isUploaded = true;
						item.isSuccess = true;
						item.imagesRealName = catalogImageHistory.imagesRealName;
						item.loadFile = RequestService.loadURL(catalogImageHistory.imagesRealName);
						$scope.uploader2.queue.push(item);
					});
				}
				vm.attributeShow = false;
				vm.imageShow = false;
				vm.categoryShow = false;
				if($scope.catalogHistory.attributeGroup.attributePanelGroupList.length != $scope.catalog.attributeGroup.attributePanelGroupList.length){
					vm.attributeShow = true;
				}else{
					for(var idx = 0;idx < $scope.catalog.attributeGroup.attributePanelGroupList.length;idx++){
						if(!angular.equals($scope.catalog.attributeGroup.attributePanelGroupList[idx].value, 
								$scope.catalogHistory.attributeGroup.attributePanelGroupList[idx].value)){
							vm.attributeShow = true;
						}
					}
				}
				if($scope.catalogHistory.catalogImageHistoryList.length != $scope.catalog.catalogImageList.length){
					vm.imageShow = true;
				}else{
					angular.forEach($scope.catalogHistory.catalogImageHistoryList, function (catalogImageHistory, idx) {
						if(catalogImageHistory.imagesFileName != $scope.catalog.catalogImageList[idx].imagesFileName){
							vm.imageShow = true;
						}
					});
				}
				if($scope.catalogHistory.isChange == true){
					vm.categoryShow = true;
				}
			});
		    
		    
		}
		    
    $scope.changeItemCode = function (itemKode) {
		RequestService.doPOSTJSON('/procurement/master/item-organisasi/get-list-item-organisasi-by-item-kode', itemKode).then(function (data) {
			$scope.itemOrganisasiList=data;
			if($scope.catalog.catalogFeeList !=null || $scope.catalog.catalogFeeList !=undefined){
				if($scope.catalog.catalogFeeList.length>0){
					angular.forEach($scope.itemOrganisasiList, function(value,index){
						$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
						$scope.catalog.catalogFeeList.isOpenForm=false;
					});
				}else{
					angular.forEach($scope.itemOrganisasiList, function(value,index){
						$scope.catalog.catalogFeeList.push({isOpenForm:true});
						$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
					});
				}
			}else{
				$scope.catalog.catalogFeeList=[];
				angular.forEach($scope.itemOrganisasiList, function(value,index){
					$scope.catalog.catalogFeeList.push({isOpenForm:true});
					
				});
			}
			
							
		});
    }
    if($scope.catalog.item!=undefined){
		$scope.changeItemCode($scope.catalog.item.kode);
	}
    
    $scope.kembali = function () {
		$state.go("app.promise.procurement-approval");
	}
  
	
    $scope.checkTodo=function(){
    	if(vm.todo!='add' && ($scope.catalogHistory != null && $scope.catalogHistory != undefined)){
    		vm.status="Edit";
    		vm.harga=true;
        	vm.namaIND =true;
        	vm.deskripsiIND=true;
        	vm.kodeProd=true;
        	vm.beart=true;
        	vm.minQty=true;
        	vm.stok=true;
        	vm.panjang=true;
        	vm.lebar=true;
        	vm.tinggi=true;
    	}
    }
    
    $scope.btnSimpan = function (statusId, catalogContract) {
    	$scope.catalogContract = catalogContract;
        var checkNote = vm.note;
        var isNote = true;
        if (vm.note === null || vm.note === '') {
            isNote = false;
            $scope.errorNoteMessage = "Note tidak boleh kosong";
        } else if (checkNote.length >= 255) {
            isNote = false;
            $scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
        } else {
        	if ($scope.catalogContract != null){
        		if ($scope.catalogContract.statusKontrak != 0){
    	        	ModalService.showModalConfirmation('Apakah Anda yakin data Approval sudah benar?').then(function (result) {
    	               			ModalService.showModalInformationBlock();
    	               			$scope.simpan(statusId);
    	        	});
            	} else  {
        			ModalService.showModalInformation('Kontrak '+ $scope.catalogContract.nomorKontrak +' sudah tidak aktif, silahkan hubungi Penyedia untuk dilakukan rilis ulang item catalog');
           		}
        	}
        	else{
        		ModalService.showModalConfirmation('Apakah Anda yakin mereject data ini?').then(function (result) {
           			ModalService.showModalInformationBlock();
           			$scope.simpan(statusId);
    	});
        	}
       }
    }
    $scope.simpan = function (statusId) {
    	if($scope.catalogHistory ==null || angular.equals($scope.catalogHistory,"") || $scope.catalogHistory == undefined){
    		$scope.catalogHistory=null;
    	}
        var approvalDoProcessDTO = {
        	 todo: vm.todo,
             id: $scope.catalog.id,
             status: statusId,
             note: vm.note,
             approvalProcessId: vm.approvalProcess.id,
             catalog: $scope.catalog,
             catalogHistory: $scope.catalogHistory
         };

         RequestService.doPOSTJSON('/procurement/approvalProcessServices/doProcess', approvalDoProcessDTO)
             .then(function successCallback(data) {
            	 
            		 $location.path('app/promise/procurement/approval');
        			 ModalService.closeModalInformation();

//            	if($scope.catalogHistory ==null || angular.equals($scope.catalogHistory,"") || $scope.catalogHistory == undefined){
//            		RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/approve-new-catalog', $scope.catalog)
//            		.then(function successCall(data) {
//            			$location.path('app/promise/procurement/approval');
//            			ModalService.closeModalInformation();
//            			
//            		}, function errorCall(response) {
//            			ModalService.closeModalInformation();
//            			ModalService.showModalInformation('Terjadi kesalahan pada system!');
//            		});
//            	}else{
//            		$scope.catalogHistory.todo = vm.todo;
//            		RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/update-catalog-after-approve', $scope.catalogHistory)
//            		.then(function successCall(data) {
//            			$location.path('app/promise/procurement/approval');
//            			ModalService.closeModalInformation();
//            			
//            		}, function errorCall(response) {
//            			ModalService.closeModalInformation();
//            			ModalService.showModalInformation('Terjadi kesalahan pada system!');
//            		});
//            	}
             }, function errorCallback(response) {
                 ModalService.closeModalInformation();
                 ModalService.showModalInformation('Terjadi kesalahan pada system!');
             });
     }
    
    approvalViewCatalogController.$inject = ['$scope', '$rootScope', 'ModalService', 'RequestService', '$modal', '$state',  'FileUploader', '$stateParams', 'toaster', '$window', '$location'];
    }
})();
