'use strict';
/* Controllers */
angular.module('naut').controller('catalogAddCtrl', ['$scope', '$rootScope', 'ModalService', 'RequestService', '$modal','$state','FileUploader', '$stateParams', 'toaster', '$window', '$location','$filter',
	function ($scope, $rootScope, ModalService, RequestService, $modal, $state,  FileUploader, $stateParams, toaster, $window, $location, $filter) {
		var vm = this;
		$scope.catalog = {};
		$scope.priceRangeList = [];
		$scope.catalog.catalogFeeList=[];
		$rootScope.catalog={};
		$scope.itemOrganisasiList={};
		$scope.catalogKontrakListAll={};
		$scope.selectList = [];
		vm.isErrorMinQty=false;
		vm.isErrorMaxQty=false;
		vm.isErrorEmpty=false;
		vm.bulkToDo='';
		vm.price=[];
		vm.idKontrak=null;
		vm.stApproval='';
		vm.isRejected=false;
		vm.toDoParam = ($stateParams.toDo!= undefined)? $stateParams.toDo :{};
		vm.showBtnHargaGrosir=false;
		vm.showBtnBiayaPengiriman=false;
		
		
		if ($stateParams.dataCatalog != undefined) {
			vm.toDo = 'edit';
			if($scope.catalog.currentStock==null){
				$scope.catalog.currentStock=0;
			}
			$scope.catalog = $stateParams.dataCatalog;
		}else{
			$scope.catalog.currentStock=0;
		}
		$rootScope.catalog=$scope.catalog;
		var cekCategoryListOK = false;
		$scope.errorKodeProduk = '';
		$scope.errorKodePanitia = '';
		$scope.errorNama = '';
		$scope.errorDeskripsi = '';
		$scope.errorKodeProduk = '';
		$scope.errorAttributeGroup = '';
		$scope.errorKodePanitia = '';
		$scope.errorItem = '';
		$scope.errorMinimalQty='';
		$scope.errorMataUang = '';
		$scope.errorSatuan = '';
		$scope.errorNamaItem='';
		$scope.errorStatus = '';
		$scope.errorHarga = '';
		$scope.errorVendor = '';
		$scope.errorKatalogKontrak = '';
		$scope.errorCatalogBulkPrice = '';
		$scope.errorNoKontrak='';
		$scope.waitingApproval='';
		$scope.addOrEdit='Add New';

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
			include: 'app/views/promise/procurement/catalog/add_product/general.html',
			active: true,
			disabled:true
		}, {
			heading: 'CATEGORIES',
			include: 'app/views/promise/procurement/catalog/add_product/categories.html',
			active: false,
			disabled:true
		},{
			heading: 'IMAGES',
			include: 'app/views/promise/procurement/catalog/add_product/images.html',
			active: false,
			disabled:true
		}, {
			heading: 'SPECIFICATION',
			include: 'app/views/promise/procurement/catalog/add_product/specification.html',
			active: false,
			disabled:true
		}];
		$scope.backToGeneral = function () {
			$scope.panes[0].active = true
		}
		$scope.goToCategories = function () {
			if($scope.validasiFee($scope.catalog.catalogFeeList)==true && $scope.validateThenNext($scope.catalog)==true){
				$scope.panes[1].active = true;
			}else{
				$scope.validateThenNext($scope.catalog);
				if($scope.validasiFee($scope.catalog.catalogFeeList)==false){
					toaster.pop('error','Kesalahan','Biaya Pengiriman minimal input adalah 1 !');
				}
				$scope.panes[1].active = false;
			}
		}
		$scope.backToCategories = function () {
			$scope.panes[1].active = true
		}
		$scope.goToImages = function () {
			$scope.cekCategoryList();
			if (cekCategoryListOK == false) {
				toaster.pop('error', 'Kesalahan', 'Pilih salah satu Categories');
				$scope.panes[2].active = false;
			}else{
				$scope.panes[2].active = true;
			}
			
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
				
				//KAI 10052021
				if($scope.catalog.isApproval == 0){
					if($scope.catalog.approvalProcess.status == 1){
						document.getElementById("btnSimpan").style.display = "none";
					}
				}
			});
			if ($scope.catalog.catalogImageList.length == 0) {
				toaster.pop('error', 'Kesalahan', 'Images harus diisi');
				$scope.panes[3].active = false;
			}else{
				$scope.panes[3].active = true;
			}
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
		$scope.popUpAddStock = function () {
			$scope.modalInstance = $modal.open({
                templateUrl: '/popUpAddStock.html',
                controller: ModalCtrl,
                size: 'lg'
            });
			$scope.modalInstance.result.then(function (data) { //ok
//                console.log(data);
                $scope.catalog.quantity=data;
            }, function () {});
        };
        var ModalCtrl = function ($scope, $modalInstance) {
           $scope.catalog=$rootScope.catalog;
            $scope.simpanStok=function(){
            	RequestService.modalConfirmation('Yakin menyimpan data ?').then(function (result) {
					$scope.saveDataStock($scope.catalog.quantity);
				});
            }
            $scope.saveDataStock=function(qty){
            	$scope.catalog.currentStock=$scope.catalog.currentStock + qty;
            	$modalInstance.close(qty);
            };
            $scope.ok = function () {
            	$modalInstance.close('closed');
                
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalCtrl.$inject = ['$scope', '$modalInstance'];
        $scope.validasiFee=function(dataFee){
        	var sizeData=0;
        	if(dataFee!=undefined || dataFee!=null){
        		angular.forEach(dataFee, function(data, index){
        			/* perubahan KAI 14/12/2020 [20852]*/
        			if(data.quantityBatch==null || data.quantityBatch=="" ){
        				return false;
        			}else{
        				sizeData++;
        			}
        			if(data.slaDeliveryTime==null || data.slaDeliveryTime==""){
    					$scope.catalog.catalogFeeList[index].slaDeliveryTime = 0;
    				}
        			if(data.ongkosKirim==null || data.ongkosKirim==""){
        				$scope.catalog.catalogFeeList[index].ongkosKirim = 0;        				
        			}
        		});
        		if(dataFee.length==sizeData && dataFee.length>0){
        			return true;
            	}else{
            		return false;
            	}
        	}else{
        		return false;
        	}
        	
        }
		function resetData() {	
			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findCatalogProperties', $scope.catalog).then(function (data) {
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
					
					//get data catalog kontrak
				//	$scope.getCatalogKontrakList();
					$scope.cekApproval();
					$scope.cekDisable();
					$scope.cekDisablePrice();
					$scope.getDataLogApproval();
					
					//setPrice
					
					$scope.validMinQty=function(qtyMaxNew,diskonNew,qtyMinNew,qtyMaxOld){
	    				if(vm.bulkToDo=='add'){
	    					
	    					if(parseInt(qtyMinNew) < parseInt(qtyMaxOld) ){
	        					vm.isErrorMinQty=true;    					
	        				}else if(parseInt(qtyMinNew) > parseInt(qtyMaxNew)){
	        					vm.isErrorMinQty=true;
	        				}else{
	        					if(typeof qtyMinNew == 'null' || typeof qtyMaxNew == 'null' || typeof diskonNew == 'null'){
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
	        					if(!angular.equals(qtyMinNew, "") && qtyMaxNew != null && diskonNew !=null ){
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
	        					if(!angular.equals(qtyMinNew, "") && !angular.equals(qtyMaxNew, "") && diskonNew !=null){
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
	        					if( !angular.equals(qtyMinNew, "") && !angular.equals(qtyMaxNew, "") && diskonNew !=null){
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
				}
			})
		}
		resetData();
		if($scope.catalog.approvalProcess != null){
			if($scope.catalog.approvalProcess.status == 2){
				vm.isRejected = true;
				vm.stApproval = 'Rejected';
			}
		}
		/*perubahan KAI 27/11/2020*/
		$scope.getDataLogApproval = function(){
			if($scope.addOrEdit=="Edit"){
				if($scope.catalog != null){
					RequestService.doGET('/procurement/catalog/CatalogServices/getApprovalProcessTypeByCatalog/' + $scope.catalog.id)
					.then(function success(data) {
						vm.approvalProccessList = data;
					},function error(response) {
						RequestService.informInternalError();
					});
				}			
			}			
		}
		
		
		//cek value category list
		$scope.cekCategoryList = function () {
			angular.forEach($scope.catalog.categoryList, function (categoryList) {
				if (categoryList.value == true) {
					cekCategoryListOK = true;
				}
			});
		}
		$scope.checkAndUpdate = function (parentList, category) {
			angular.forEach(parentList, function (parent) {
				if (parent.id == category.id) {
					category.value = true;
				}
			});
		}
		$scope.buildCategory = function (category) {
			var childList = category.categoryChildList;
			angular.forEach(childList, function (child) {
				$scope.checkAndUpdate(parentList, child);
				if ((child.categoryChildList) && (child.categoryChildList.length > 0)) {
					$scope.buildCategory(child);
				}
			});
		}
		$scope.pickCategory = function (dataCategory) {
			$scope.dataCategoryTemp = dataCategory;
			$scope.arrCategoryParentListId = [];
			$scope.getCategoryParentListId($scope.dataCategoryTemp);
			$scope.setCategoryValue($scope.catalog.categoryList, $scope.arrCategoryParentListId);
		}
		$scope.setCategoryValue = function (categories, arrCategoryParentListId) {
			angular.forEach(categories, function (category) {
				if (arrCategoryParentListId) {
					if (category.categoryChildList && category.categoryChildList.length > 0) {
						if (category.id === arrCategoryParentListId[0]) {
							arrCategoryParentListId = arrCategoryParentListId.slice(1);
							category.value = true;
						} else {
							category.value = false;
						}
						$scope.setCategoryValue(category.categoryChildList, arrCategoryParentListId);
					} else {
						if (category.id === arrCategoryParentListId[0]) {
							category.value = true;
						} else {
							category.value = false;
						}
					}
				} else {
					category.value = false;
					if (category.categoryChildList && category.categoryChildList.length > 0) {
						$scope.setCategoryValue(category.categoryChildList);
					}
				}
			});
		}
		$scope.getCategoryParentListId = function (category) {
			if (category.categoryParentList && category.categoryParentList.length > 0) {
				angular.forEach(category.categoryParentList, function (cat) {
					$scope.arrCategoryParentListId.push(cat.id);
				})
			}
			$scope.arrCategoryParentListId.push(category.id);
		}

		function checkParentCategory(dataCategory) {
			//console.log(dataCategory);
		}

		function loopCategory(categoryList, keepGoing) {
			if (categoryList != undefined && categoryList.length > 0 && keepGoing) {
				angular.forEach(categoryList, function (category) {});
			}
		}

		function checkCategoryChild(dataCategory) {
			//console.log('masuk sini');
			// check below
			if (dataCategory.categoryChildList != undefined && dataCategory.categoryChildList.length > 0) {
				angular.forEach(dataCategory.categoryChildList, function (categoryChild) {
					//console.log(categoryChild);
					categoryChild.value = dataCategory.value;
					checkCategoryChild(categoryChild);
				});
			}
		}
		$scope.kembali = function () {
			$state.go("app.promise.procurement-catalog");
		}
		$scope.simpan = function () {
			document.getElementById("btnSimpan").style.display = "none";
			if($scope.catalog.isApproval != 1 || $scope.catalog.isApproval == undefined || $scope.catalog.isApproval == null){
				if ($scope.uploader.getNotUploadedItems().length > 0) {
					RequestService.modalInformation('Data Image belum di upload');
					document.getElementById("btnSimpan").style.display = "";
				} else {
					if ($scope.catalog.catalogKontrak.statusKontrak != 0 || $scope.catalog.catalogKontrak.statusCatalog == 1 ){
							$scope.validateThenSave($scope.catalog);
					} else {
						ModalService.showModalInformation('Kontrak sudah tidak aktif. Silahkan pilih nomor kontrak lain');
						document.getElementById("btnSimpan").style.display = "";
						$state.go("app.promise.procurement-catalog");
					}
				}
				
			}else{
				RequestService.modalInformation('Maaf, Tidak bisa merubah Data yang sedang menjalani tahap Approval');
				$state.go("app.promise.procurement-catalog");
				document.getElementById("btnSimpan").style.display = "";
			}
		}
		$scope.saveData = function () {
			
//			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/check-existing-catalog', $scope.catalog).then(function (data) {
//				if(data){
//					RequestService.modalInformation('Catalog already exists');
//				}else{
				RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/save', $scope.catalog).then(function (data) {
					/*perubahan KAI 01/18/2021*/
					ModalService.closeModalInformation();
					if (data.satuan == null || typeof data.satuan == 'null' || typeof data.satuan == 'undefined') {
						ModalService.showModalInformation('Data Gagal di Simpan, Cek Kembali Data Anda');
					} 
					else if (data.isApprovalTahapan == null || typeof data.isApprovalTahapan == 'null' || typeof data.isApprovalTahapan == 'undefined') {
						ModalService.showModalInformation('Data Gagal di Simpan, Setting terlebih dahulu Approval Tahapannya');
					} else {
						ModalService.showModalInformation('Data Berhasil di Simpan')
						$state.go("app.promise.procurement-catalog");
					}
					
				}, function error(response) {
					RequestService.informInternalError();
				});
				//}
			//});
		}
		$scope.validateThenNext=function(catalog){
			var valid= true;
//			if (catalog.attributeGroup == null || typeof catalog.attributeGroup == 'null') {
//				$scope.errorAttributeGroup = 'Attribut Group Harus Diisi';
//				valid= false;
//			}
			if ($scope.catalog.namaIND == null || typeof $scope.catalog.namaIND == 'null' || $scope.catalog.namaIND=="") {
				$scope.errorNama='Nama Harus Diisi';
				valid= false;
			}
			if ($scope.catalog.deskripsiIND == null || typeof $scope.catalog.deskripsiIND == 'null' || $scope.catalog.deskripsiIND == "") {
				$scope.errorDeskripsi ='Deskripsi Harus Diisi';
				valid= false;
			}
			if (catalog.catalogContractDetail == null || typeof catalog.catalogContractDetail == 'null') {
				$scope.errorNamaItem='Nama Item Harus Diisi';
				valid= false;
			}
			if ($scope.catalog.harga == null || typeof $scope.catalog.harga == 'null' || $scope.catalog.harga == "") {
				$scope.errorHarga ='Harga Harus Diisi';
				valid= false;
			}
			if ($scope.catalog.mataUang == null || typeof $scope.catalog.mataUang == 'null') {
				$scope.errorMataUang = 'Mata Uang Harus Diisi';
				valid= false;
			}
			if ($scope.catalog.catalogKontrak == null || typeof $scope.catalog.catalogKontrak == 'null') {
				$scope.errorNoKontrak = 'Nomor Kontrak Harus Diisi';
				valid= false;
			}
			if ($scope.catalog.satuan == null || typeof $scope.catalog.satuan == 'null') {
				$scope.errorSatuan = 'Satuan Harus Diisi';
				valid= false;
			}
			/*if (catalog.minimalQuantity == null || typeof catalog.minimalQuantity == 'null' || catalog.minimalQuantity == "") {
				$scope.errorMinimalQty = 'Minimal Quantity Harus Diisi';
				valid= false;
			}*/
			/*perubahan KAI 01/19/2021*/
			if (catalog.minimumQuantityOrder != null || typeof catalog.minimumQuantityOrder != 'null' || catalog.minimumQuantityOrder != "") {
				if(parseInt(catalog.minimumQuantityOrder) > parseInt(catalog.maksimumQuantityOrderDay) || parseInt(catalog.minimumQuantityOrder) > parseInt(catalog.maksimumQuantityPerOrder)){
					$scope.errorMinimalQtyOrder='Nilai Minimum Quantity Order tidak boleh Lebih dari Nilai Maksimum Quantity Order Per Hari dan Nilai Maksimum Quantity Per Order';
					valid= false;
				}else{
					$scope.errorMinimalQtyOrder='';
				}
			}
			
			if (catalog.maksimumQuantityPerOrder != null || typeof catalog.maksimumQuantityPerOrder != 'null' || catalog.maksimumQuantityPerOrder != "") {
				if(parseInt(catalog.maksimumQuantityPerOrder) > parseInt(catalog.maksimumQuantityOrderDay) ){
					$scope.errorMaksQtyOrderTransaction = 'Nilai Maksimum Quantity Per Order  tidak boleh Lebih dari Nilai Maksimum Quantity Order Per Hari';
					valid= false;					
				}else{
					$scope.errorMaksQtyOrderTransaction = '';					
				}
			}
			angular.forEach(catalog.catalogBulkPriceList, function(data,index){
				var qtyMaxOld = undefined; 
				var qtyMinNext = undefined;
				if(index > 1){
					qtyMaxOld = catalog.catalogBulkPriceList[index-1].maxQuantity;
				}
				if(catalog.catalogBulkPriceList[index+1] != undefined){
					qtyMinNext = catalog.catalogBulkPriceList[index+1].minQuantity;
				}
				if(parseInt(data.maxQuantity) < parseInt(data.minQuantity) ||
						parseInt(data.maxQuantity) > parseInt(qtyMinNext) ||
						parseInt(data.minQuantity) < parseInt(qtyMaxOld)){
					valid= false;
					$scope.errorCatalogBulkPrice ='Cek kembali data Bulk Price yang anda masukkan';
				}
			});
			/*if(vm.isErrorMaxQty==true || vm.isErrorMinQty==true || vm.isErrorEmpty==true || catalog.catalogBulkPriceList.length == 0 ){
				$scope.errorCatalogBulkPrice ='Cek kembali data Bulk Price yang anda masukkan';
				valid= false;
			}else{
				$scope.errorCatalogBulkPrice ='';
			}*/
			/* perubahan KAI 30/11/2020 */
			if (catalog.kodeProduk == null || typeof catalog.kodeProduk == 'null' || catalog.kodeProduk == "") {
				$scope.errorKodeProduct = 'Kode Produk Harus Diisi';
				valid= false;
			}
			if(valid){
				$scope.errorCatalogBulkPrice ='';
			}
			return valid;
			
			
		}
		$scope.changeBulkPrice=function(diskon,idx){
			vm.price[idx]=$scope.catalog.harga - ((diskon/100) * $scope.catalog.harga);
		}
		//buat validasi agar terisi terlebih dahulu
		$scope.validateThenSave = function (catalog) {
			var isValid = true;
			
			if ($scope.catalog.catalogLocationList == undefined) {
				$scope.catalog.catalogLocationList = [];
			}
			 else if ($scope.roleUser == false) {
				 //melepas validasi item kode/id berdasarkan task  19442
//				if (catalog.item == null || catalog.item == undefined) {
//					toaster.pop('error', 'Kesalahan', 'Item harus di isi');
//					isValid = false;
//				}
			}
			
			/* perubahan KAI 30/11/2020 */
			if ($scope.roleUser == false && isValid) {
				RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/checkKodeProduct', catalog).then(function (data) {
					if (data) {
						$scope.errorKodeProduct = "Kode Product sudah di gunakan";
						toaster.pop('error', 'Kesalahan', 'Kode Product sudah di gunakan');
						//KAI 07052021
						document.getElementById("btnSimpan").style.display = "";
						isValid = false;
					}
					if (isValid) {
						
						RequestService.modalConfirmation().then(function(result) {
							/*perubahan KAI 01/18/2021*/
							ModalService.showModalInformationBlock();
		    				$scope.saveData();
		    			},function chooseNo(){
		    				document.getElementById("btnSimpan").style.display = ""
		    			});
					}
				});
			}
//			else {
				
			//}
		}
		$scope.addPrice = function () {
			$scope.catalog.catalogPriceRangeList = [];
			$scope.catalog.catalogPriceRangeList.push({});
		}
		$scope.deletePrice = function (dataPrice) {
			var indexPrice = $scope.catalog.priceRangeList.indexOf(dataPrice);
			$scope.catalog.priceRangeList.splice(indexPrice, 1);
		}
		$scope.changeVendor = function () {
			if ($scope.catalog.catalogLocationList.length == 0) {
				$scope.catalog.catalogLocationList = [];
				$scope.catalog.catalogLocationList.push({});
			}
		}
		$scope.findProductCode = function (kodeProductVendor) {
			var tempCatalog = {
				isVendor: true,
				kodeProduk: kodeProductVendor + "%"
			};
			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findAllByProperty', tempCatalog).then(function (data) {
				$scope.catalogByProductCodeList = data;
			})
		}
//		$scope.changeItemCode = function (itemKode) {
//			if($scope.catalog.item != null){
//				RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/get-catalog-contract-detail-by-item', $scope.catalog.item).then(function (data) {
//					console.log("ccd: ", data);
//					$scope.catalog.namaIND = data.itemDesc;
//					$scope.catalog.harga = data.unitPrice;
//					$scope.catalog.mataUang = data.mataUang;
//					$scope.catalog.satuan = $scope.catalog.item.satuanId;
//				});
//			}
//				
//			RequestService.doPOSTJSON('/procurement/master/item-organisasi/get-list-item-organisasi-by-item-kode', itemKode).then(function (data) {
//				$scope.itemOrganisasiList=data;
//				if($scope.catalog.catalogFeeList !=null || $scope.catalog.catalogFeeList !=undefined){
//					console.log("item :",$scope.itemOrganisasiList);
//					if($scope.catalog.catalogFeeList.length>0){
//						angular.forEach($scope.itemOrganisasiList, function(value,index){
//							$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
//							$scope.catalog.catalogFeeList.isOpenForm=false;
//						});
//					}else{
//						angular.forEach($scope.itemOrganisasiList, function(value,index){
//							$scope.catalog.catalogFeeList.push({isOpenForm:true});
//							$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
//						});
//					}
//				}else{
//					$scope.catalog.catalogFeeList=[];
//					angular.forEach($scope.itemOrganisasiList, function(value,index){
//						$scope.catalog.catalogFeeList.push({isOpenForm:true});
//						
//					});
//				}
//			});
//		}
		
		$scope.getOrganisasiList = function () {
			RequestService.doGET('/procurement/master/organisasi/get-organisasi-level2-list').then(function (data) {
				$scope.organisasiList=data;
			});
			
		}
		
		
		
		$scope.changeItemCode = function (catalogContractDetail) {
			$scope.catalog.item = catalogContractDetail.item;
			if($scope.catalog.item != null){
				$scope.catalog.namaIND = catalogContractDetail.itemDesc;
				$scope.catalog.harga = catalogContractDetail.unitPrice;
				$scope.catalog.mataUang = catalogContractDetail.mataUang;
				$scope.catalog.satuan = catalogContractDetail.item.satuanId;
				//$scope.catalog.catalogContractDetail = catalogContractDetail;
				if($scope.catalog.catalogContractDetail != null || $scope.catalog.catalogContractDetail != undefined){
					vm.showBtnHargaGrosir = true;
					vm.showBtnBiayaPengiriman=true;
					if ( vm.toDoParam !='edit' ){
						$scope.catalog.currentStock = catalogContractDetail.quantity;
					}
				}
			}else{
				if($scope.catalog.catalogContractDetail != null || $scope.catalog.catalogContractDetail != undefined){
					vm.showBtnHargaGrosir = true;
					vm.showBtnBiayaPengiriman=true;
				}  else {
					vm.showBtnHargaGrosir = false;
					vm.showBtnBiayaPengiriman=false
				}
				
			}
			RequestService.doPOSTJSON('/procurement/master/item-organisasi/get-list-item-organisasi-by-item-kode', catalogContractDetail.item.kode).then(function (data) {
				$scope.itemOrganisasiList=data;
//				if($scope.catalog.catalogFeeList !=null || $scope.catalog.catalogFeeList !=undefined){
//					console.log("item :",$scope.itemOrganisasiList);
//					if($scope.catalog.catalogFeeList.length>0){
//						angular.forEach($scope.itemOrganisasiList, function(value,index){
//							$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
//							$scope.catalog.catalogFeeList.isOpenForm=false;
//						});
//					}else{
//						angular.forEach($scope.itemOrganisasiList, function(value,index){
//							$scope.catalog.catalogFeeList.push({isOpenForm:true});
//							$scope.catalog.catalogFeeList[index].organisasi=value.organisasi;
//						});
//					}
//				}else{
//					$scope.catalog.catalogFeeList=[];
//					angular.forEach($scope.itemOrganisasiList, function(value,index){
//						$scope.catalog.catalogFeeList.push({isOpenForm:true});
//						
//					});
//				}
			});
			
		}
		
		$scope.getCatalogKontrakList=function(){
			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/get-all-list-catalog-contract').then(function (data) {
				$scope.catalogKontrakListAll=data;
			});
		}
		
		 $scope.findCatalogKontrak = function (nama) {
	        	
	        	vm.url = nama == "" ? '/procurement/catalog/catalog-contract-service/get-all-list-catalog-contract':'/procurement/catalog/catalog-contract-service/get-by-number/'+ nama;
	        	
				RequestService.doGET(vm.url)
					.then(function success(data) {
						$scope.catalogKontrakListAll=data;

					}, function error(response) { 
						RequestService.informInternalError();
	        		
				});
			}
		
		$scope.changeNomorKontrak=function(){
			
			$scope.catalog.item = {};
			$scope.catalog.namaIND = null;
			$scope.catalog.harga = '';
			$scope.catalog.mataUang = null;
			$scope.catalog.satuan = null;
			//$scope.catalog.catalogFeeList=[];
			$scope.itemOrganisasiList=[];
//			angular.forEach($scope.itemOrganisasiList, function(value,index){
//				$scope.catalog.catalogFeeList.push({isOpenForm:true});
//				
//			});
			
			
			vm.idKontrak=$scope.catalog.catalogKontrak.id;
			//$scope.getKodeItem(vm.idKontrak);
			$scope.getCatalogContractDetail(vm.idKontrak);
			vm.showBtnHargaGrosir = false;
			vm.showBtnBiayaPengiriman=false
		}
		
		
		$scope.changeNomorKontrakAttribut=function(){

			vm.idKontrak=$scope.catalog.catalogKontrak.id;
			//$scope.getKodeItem(vm.idKontrak);
			$scope.getCatalogContractDetail(vm.idKontrak);
		}
		
		//cek item
		if($scope.catalog.item!=undefined){
			$scope.changeItemCode($scope.catalog.catalogContractDetail);
			$scope.catalog.namaIND = $scope.catalog.catalogContractDetail.itemDesc;
		}
		
		$scope.getCatalogContractDetail = function (idKontrak) {
			RequestService.doPOSTJSON('/procurement/catalog/catalog-contract-detail-service/get-list-by-catalog-contract',idKontrak).then(function (data) {
				$scope.catalogContractDetailList = data;
			})
		}
		
//		$scope.getKodeItem = function (idKontrak) {
//			RequestService.doPOSTJSON('/procurement/master/item/get-list-by-kontrak',idKontrak).then(function (data) {
//				$scope.itemByKodeItemList = data;
//				console.log("itemByKodeItemList ",$scope.itemByKodeItemList);
//			})
//		}
		if($scope.catalog.catalogKontrak!=null){
			//$scope.getCatalogContractDetail($scope.catalog.catalogKontrak.id);
		}
		$scope.tglOpen = function ($event, attributePanelGroup) {
			$event.preventDefault();
			$event.stopPropagation();
			attributePanelGroup.tglStatOpen = true;
		};
		
		$scope.addDataBulkPrice = function() {
		    if ($scope.catalog.catalogBulkPriceList == null) {
		    	$scope.catalog.catalogBulkPriceList = [];
		    }
		    vm.bulkToDo='add';
		    $scope.catalog.catalogBulkPriceList.push({isOpenForm : true});
		    
		}
		
		    
		$scope.editDataBulkPrice = function(catalogBulkPrice, idx) {
		   	catalogBulkPrice.isOpenForm = true;
		   	vm.bulkToDo='edit';
		   	$scope.changeBulkPrice(catalogBulkPrice.diskon, idx);
		}
		    
		$scope.hapusDataBulkPrice = function(catalogBulkPrice) {
			var indexBulkPrice = $scope.catalog.catalogBulkPriceList.indexOf(catalogBulkPrice);
		   	$scope.catalog.catalogBulkPriceList.splice(indexBulkPrice, 1);
		}
		
		$scope.addDataFee = function() {
		    if ($scope.catalog.catalogFeeList == null) {
		    	$scope.catalog.catalogFeeList = [];
		    }
		    vm.bulkToDo='add';
		    $scope.catalog.catalogFeeList.push({isOpenForm:true, ongkosKirim: 0});/* perubahan KAI 14/12/2020 [20852]*/
		}
		
		$scope.editDataFee = function(catalogFee) {
			if(catalogFee!=null || catalogFee!=undefined){
				catalogFee.isOpenForm = true;
			}else{
				$scope.catalog.catalogFeeList.push({isOpenForm : true});
			}
			
		}
		
		$scope.hapusDataFee = function(catalogFee) {
			var indexFee = $scope.catalog.catalogFeeList.indexOf(catalogFee);
		   	$scope.catalog.catalogFeeList.splice(indexFee, 1);
		   	$scope.organisasiList.push(catalogFee.organisasi);
		   	$scope.organisasiList = $filter('orderBy')($scope.organisasiList, 'id', false)
		}
		
		$scope.selectOrganisasi = function(organisasi,index){
			//$scope.getOrganisasiList();
			var data = organisasi.id;
			var isEquals = false;
			if ( $scope.selectList.length > 0 ){
				for (var i = 0; i < $scope.selectList.length; i++) {
		            if (i == index) {
		            	$scope.selectList[i] = organisasi.id;
		            	isEquals = true;
		            } 
		        }
				if (isEquals == false){
	            	$scope.selectList.push(data);
	            }	
			} else {
				$scope.selectList.push(data);
			}
			

			RequestService.doPOSTJSON('/procurement/master/organisasi/get-list-not-select', $scope.selectList).then(function (data) {
				$scope.organisasiList=data;
			});
			

		   	
			
//			angular.forEach($scope.selectList, function(value,index){
//				var indexOrganisasi = $scope.organisasiList.indexOf(value);
//				$scope.organisasiList.splice(indexOrganisasi, 1);
//				
//			});
			
		}
		$scope.validasiAsuransi = function(asuransi) {
			if(asuransi>100 || asuransi<0){
				toaster.pop('error', 'Kesalahan', 'Asuransi tidak boleh lebih dari 100%');
				document.getElementById("asuransi").value = 100;
			}
		}
		
		$scope.cekApproval = function() {
			if($scope.catalog.isApproval == 1 && $scope.catalog != null){
				$scope.waitingApproval='( Waiting For Approval )';
				$scope.addOrEdit='Edit';
			}else if($scope.catalog.isApproval == 0 && $scope.catalog != null){
				$scope.addOrEdit='Edit';
			}
			
		}
		
		$scope.cekDisable = function() {
			if($scope.addOrEdit =='Edit'){
				$scope.isDisable=true;
			} else {
				$scope.isDisable=false;
			}
				
			
		}
		
		$scope.cekDisablePrice = function() {
			if($scope.addOrEdit =='Edit'){
				$scope.isDisablePrice=false;
			} else {
				$scope.isDisablePrice=true;
			}
		}
		
		$scope.validasiDeskripsi = function(desc){
			if (desc == "") {
				$scope.errorDeskripsi ='Deskripsi Harus Diisi';
			}else{
				$scope.errorDeskripsi ='';
			}
		}
		
		if (vm.toDoParam == 'edit'){
			
			if ($scope.catalog != null){
				RequestService.doPOSTJSON('/procurement/master/organisasi/get-list-not-select-by-catalogId', $scope.catalog.id).then(function (data) {
					$scope.organisasiList=data.organisasiList;
					$scope.selectList=data.selectList;
				});
			}
			
		} else {
			$scope.getOrganisasiList();
		}
		    
	}
]);
