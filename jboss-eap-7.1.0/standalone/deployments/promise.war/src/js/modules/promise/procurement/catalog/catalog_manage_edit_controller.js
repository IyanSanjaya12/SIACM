'use strict';

/* Controllers */

angular
.module('naut')
.controller('catalogEditCtrl', ['$timeout', '$http', '$scope', 'RequestService', '$state', '$modal', 'FileUploader', '$rootScope', '$stateParams' , function($timeout, $http, $scope, RequestService, $state, $modal, FileUploader, $rootScope, $stateParams) {
	
	/** ====================================================== All Initisial Set ===================================================================== **/
	
	var dataIdLama = [];
	
	$scope.dynamic_value = [];
	$scope.inventory_value = [];
//	$scope.data = {};
	$scope.dataPriceRange = [];
	$scope.dataInventory = [];
	$scope.dataCategories = [];
	$scope.dataCatalogAttributes = [];
	$scope.rentanHarga = false;
	$scope.dataEdit = $stateParams.dataCatalog;
//	console.log("ISI DATA EDIT = "+JSON.stringify($scope.dataEdit))
	
	$scope.getAllDataFromTable = function(catalogId) {
		var params = {
				catalogId: catalogId
		}
		
		RequestService.requestServer('/Services/Process/CatalogServices/getCatalogForEdit',params)
		.then(function (data, status, headers, config) {
			if(data) {
				$scope.mappingFromStateParam(data[0]);
			}
		})
	}
	
	// Mapping Tabs
	$scope.panes = [
	    {
	    	heading: 'GENERAL',
	        include: 'app/views/tabs/edit_product/general.html'
	    },
	    {
	    	heading: 'CATEGORIES',
	    	include: 'app/views/tabs/edit_product/categories.html'
	    },
	    {
	    	heading: 'INVENTORY',
	        include: 'app/views/tabs/edit_product/inventory.html'
	    },
	    {
	    	heading: 'IMAGES',
	        include: 'app/views/tabs/edit_product/images.html'
	    },
	    {
	    	heading: 'SPECIFICATION',
	        include: 'app/views/tabs/edit_product/specification.html'
	    },
	    {
	    	heading: 'CROSS SELL',
	        include: 'app/views/tabs/edit_product/cross_sell.html'
	    },
	    {
	    	heading: 'CUSTOM OPTIONS',
	    	include: 'app/views/tabs/edit_product/custom_options.html'
	    }];
	           	
	$scope.panesKlik = function(panes) {
		if(panes == 'CUSTOM OPTIONS')
			$scope.tombolSimpan = true;
		else
			$scope.tombolSimpan = false;
	}
	           	                    
	           	                    
	$scope.clone = function(){
		var dynamic_length = $scope.dynamic_value.length;
		$scope.dynamic_value.push({
	         id: dynamic_length + 1,
	         textFrom: '',
	         textTo: '',
	         textHarga: ''
	    });
	};
	           	
	$scope.del_clone = function(val){
		$scope.dynamic_value.split[val];
	};
	           	                 
	$scope.getStatus = function() {
		$scope.statusList = [{
			id: 1,
			name: 'Active'
		}, {
			id: 2,
	        name: 'Disactive'
	    }]
	}
	           	                    
	$scope.getApproval = function() {
		$scope.approvalList = [{
			id: 1,
			name: 'Setuju'
		}, {
			id: 2,
			name: 'Tidak Setuju'
		}]
	}
	
	$scope.wholeSalerCheck = function(nilai) {
		if(nilai == true)
			$scope.rentanHarga = true;
		else
			$scope.rentanHarga = false;
	}
	
	// Get Master Attribute Set Data
	$scope.getAttributeSet = function() {
		$scope.attributeSetList = [];
		
		RequestService.requestServer('/Services/Master/AttributeSetServices/findAll')
		.then(function (data, status, headers, config) {
			if(data) {
//				console.log("ISI DATA ATTRIBUTE SET = "+JSON.stringify(data))
				angular.forEach(data,function(value){
					$scope.attributeSetList.push(value);
				})
			}
		})
	}
	
	// Get Master Produk Type
	$scope.getProdukType = function() {
		$scope.produkTypeList = [];
		
		RequestService.requestServer('/Services/Master/GeneralServices/findAllProdukType')
		.then(function (data, status, headers, config) {
			if(data) {
//				console.log("ISI DATA PRODUK TYPE = "+JSON.stringify(data))
				angular.forEach(data,function(value){
					$scope.produkTypeList.push(value);
				})
			}
		})
	}
	
	// Get Master Satuan Produk
	$scope.getSatuanProduk = function() {
		$scope.satuanProdukList = [];
		
		RequestService.requestServer('/Services/Master/GeneralServices/findAllSatuanProduk')
		.then(function (data, status, headers, config) {
			if(data) {
//				console.log("ISI DATA SATUAN PRODUK = "+JSON.stringify(data))
				angular.forEach(data,function(value){
					$scope.satuanProdukList.push(value);
				})
			}
		})
	}
	
	// Get Master Mata Uang
	$scope.getMataUang = function() {
		$scope.mataUangList = [];
		
		RequestService.requestServer('/Services/Master/GeneralServices/findAllMataUang')
		.then(function (data, status, headers, config) {
			if(data) {
//				console.log("ISI DATA MATA UANG = "+JSON.stringify(data))
				angular.forEach(data,function(value){
					$scope.mataUangList.push(value);
				})
			}
		})
	}
	
	// Get All Seller
	$scope.getAllSeller = function() {
		$scope.sellerList = [];
		
		var dataSimpan = {
				roleName: 'Seller'
		}
		RequestService.requestServer('/Services/myaccount/AccountInformationServices/findUserProfile', dataSimpan)
    	.success(function (data, status, headers, config) {
			if(data.length != undefined) {
//				console.log("ISI DATA SELLER = "+JSON.stringify(data))
				angular.forEach(data,function(value){
					$scope.sellerList.push(value);
				})
			}
		})
	}
	
	$scope.toggle = function (scope) {
        scope.toggle();
    };
    
    $scope.collapseAll = function () {
        $scope.$broadcast('angular-ui-tree:collapse-all');
    };

    $scope.expandAll = function () {
        $scope.$broadcast('angular-ui-tree:expand-all');
    };
    
    $scope.treeOptions = {
        accept: function(sourceNodeScope, destNodesScope, destIndex) {

            if (destNodesScope.$parent.$type === "uiTreeNode"){
                return true;
            }else{
                return false;
            }
            
        }
    };
    
    // Get Location Seller
    $scope.getCompanyAddress = function(seller) {
		$scope.companyAddress = [];
		
		var dataLemparan = {
				userId: seller
		}
		if(seller != undefined) {
			RequestService.requestServer('/Services/Process/CatalogServices/getCompanyAddress', dataLemparan)
	    	.success(function (data, status, headers, config) {
//	    		console.log("ALAMAT = "+JSON.stringify(data))
	    		$scope.companyAddress = data
	    	})
		}
	}
    
    // Get Attribute 
    $scope.getAttributeMapping = function(attributeSet, dataAtt) {
		$scope.groupAttributeList = [];
		$scope.dataCatalogAttributes = [];
		var dataLemparan = {
			attributeSet: attributeSet
		}
		if(attributeSet != undefined) {
			RequestService.requestServer('/Services/Process/CatalogServices/getAttributeSetForCatalog', dataLemparan)
	    	.success(function (data, status, headers, config) {
	    		
	    		angular.forEach(dataAtt,function(valueAtt){
	    			angular.forEach(data,function(hasil){
	    				var att = hasil.attribute;
	    				angular.forEach(att,function(value){
	    					
		    				if(valueAtt.attributeTypeValue != undefined && value.label == valueAtt.attributeTypeValue.codeName.codeName) {
		    					if(value.inputType == valueAtt.attributeTypeValue.codeName.inputType.name) {
		    						var isi = value.isi;
		    						angular.forEach(isi,function(isi){
		    							if(isi.value == valueAtt.nilai) {
		    								isi.nilai = isi.id;
		    								
		    								$scope.dataCatalogAttributes.push({
		    					    			mapAttId: value.id,
		    					    			attTypeId: valueAtt.attributeTypeValue.id,
		    					    			value: valueAtt.nilai
		    					    		})
		    							}
		    						})
		    					}
		    				}
	    				})
	    				
	    			})
	    		})
	    		$scope.groupAttributeList = data;
//	    		console.log("ISI DATA ATTRIBUTES = "+JSON.stringify($scope.dataCatalogAttributes))
	    	})
		}
	}
	
    $scope.clone_location = function() {
		var inventory_value = $scope.inventory_value.length + 1;
		$scope.inventory_value.push({
			id: inventory_value,
			alamat: '',
			stock: '',
			moq: '',
			supply: '',
			outStock: '',
			notifyStock: ''
	    });
//	    console.log($scope.inventory_value);
	};
	
	// Upload Images
	var uploader = $scope.uploader = new FileUploader({
        url: $rootScope.uploadFile,
        method: 'POST'
    });
	
    uploader.filters.push({
    	name: 'customFilter',
		fn: function (item /*{File|FileLikeObject}*/ , options) {
			return (RequestService.uploadFilter(this, item, true,"img") && (this.queue.length < 10));
		}
    });

    uploader.onCompleteItem = function (fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        fileItem.realFileName = response.fileName;
    };
    
    $scope.mappingFromStateParam = function(value) {
    	// Tab GENERAL
    	var catalog = value.catalog;
    	
    	if(catalog != undefined) {
    		$scope.data = {
        			attributeSet: catalog.attributeSet.attributeSetName,
        			produkType: catalog.produkType.produkTypeName,
        			produkNameEN: catalog.nama_EN,
        			produkDeskripsiEN: catalog.deskripsi_EN,
        			produkNameIND: catalog.nama_IND,
        			produkDeskripsiIND: catalog.deskripsi_IND,
        			produkCode: catalog.kode_produk,
        			harga: catalog.harga,
        			harga_eproc: catalog.harga_eproc,
        			mataUang: catalog.mata_uang.id,
        			berat: catalog.berat,
        			satuanBerat: catalog.satuanBerat,
        			satuanProduk: catalog.satuan_produk.namaSatuanProduk,
        			panjang: catalog.panjang,
        			satuanPanjang: catalog.satuanPanjang,
        			lebar: catalog.lebar,
        			satuanLebar: catalog.satuanLebar,
        			tinggi: catalog.tinggi,
        			satuanTinggi: catalog.satuanTinggi,
        			seller: catalog.seller.userId
        	}
    		
    		if(catalog.status == true)
        		$scope.data.status = 1;
        	else
        		$scope.data.status = 2;
        	
        	if(catalog.approval_status == true)
        		$scope.data.approval = 1;
        	else
        		$scope.data.approval = 2;
    	}
    	
    	var produkPrice = value.produk_price;
    	
    	if(produkPrice != undefined && produkPrice.length > 0) {
    		$scope.data.wholesaler = true;
    		$scope.rentanHarga = true;
    		angular.forEach(produkPrice,function(value,index){
    			$scope.dynamic_value.push({
    					id: index + 1,
    	    		    textFrom: value.fromPrice,
    	    		    textTo: value.toPrice,
    	    		    textHarga: value.harga
    			});
    		})
    	} else {
    		$scope.rentanHarga = false;
    		
    	}
    	
    	// Tab Inventory
    	var location = value.location;
    	$scope.getCompanyAddress($scope.data.seller);
    	if(location != undefined && location.length > 0) {
    		angular.forEach(location,function(value, index){
    			$scope.inventory_value.push({
    				id: index + 1,
    				alamat: value.lokasi,
    				stock: value.stok_produk,
    				moq: value.minimal_order,
    				supply: value.kemampuan_suplay,
    				outStock: value.stok_habis,
    				notifyStock: value.notifikasi_stok_habis
    			})
    		})
    	} else {
    		$scope.clone_location();
    	}
    	
    	// Tab Images
    	var images = value.images;
    	if(images != undefined && images.length > 0) {
    		$scope.imagesList = images;
    	}
    	
    	// Tab Spesifikasi
    	var attribute = value.attribute;
    	if(attribute != null && attribute.length > 0) {
    		var attSetId = attribute[0].mappingAttributeSet.attributeGroup.attributeSet.attributeSetName;
//    		console.log("ISI MAPPING ATT = "+JSON.stringify(attSetId));
    	} else {
    		var attSetId = $scope.dataEdit.attributeSet.attributeSetName;
    	}
    	$scope.getAttributeMapping(attSetId, attribute);
    	
    	// Tab Category
    	$scope.mappingCategories(value.category);
    }
    
    $scope.mappingCategories = function(categories) {
    	RequestService.requestServer('/Services/Master/CategoryServices/findAllWithTree')
    	.success(function (data, status, headers, config) {
    		if(categories != undefined && categories.length > 0) {
        		angular.forEach(categories,function(value,index){
        			angular.forEach(data,function(master){
        				if(value.category.id == master.id){
        					master.pilih = true;
        					$scope.dataCategories.push({
        						id: master.id,
        						text: master.text
        					})
        				} else {
        					angular.forEach(master.child,function(child1){
        	    				if(value.category.id == child1.id){
        	    					child1.pilih = true;
        	    					$scope.dataCategories.push({
        	    						id: child1.id,
        	    						text: child1.text
        	    					})
        	    				} else {
        	    					angular.forEach(child1.child,function(child2){
        	    						if(value.category.id == child2.id){
        	    							child2.pilih = true;
        	    							$scope.dataCategories.push({
        	    	    						id: child2.id,
        	    	    						text: child2.text
        	    	    					})
        	    						}
        	    					})
        	    				}
        	    			})
        				}
        			})
        		})
        	}
    		$scope.master_category = data
    	})
    }
    
    
    // Start Initialisasion Function
	$scope.initData = function() {
		$scope.getAttributeSet();
		$scope.getProdukType();
		$scope.getSatuanProduk();
		$scope.getMataUang();
		$scope.getAllSeller();
		$scope.getStatus();
		$scope.getApproval();
		$scope.getRoleUser();
		
		$timeout($scope.getAllDataFromTable($scope.dataEdit.id), 10000);
	}
	$scope.initData();
	
	/** =============================================================== END ========================================================================== **/
	
	
	/** ====================================================== All Controller Set ==================================================================== **/
	
	$scope.inputToValueList = function(id,from,to,harga) {
		var panjang = $scope.dataPriceRange.length + 1;
		var dataSimpan = {
				id: panjang,
				fromPrice: from,
				toPrice: to,
				harga: harga
		}
		$scope.dataPriceRange.push(dataSimpan);
//		console.log("ISI DATA = "+JSON.stringify($scope.dynamic_value))
	}
	
	$scope.batalToValueList = function(data) {
		$scope.dynamic_value.splice($scope.dynamic_value.indexOf(data), 1)
	}
	
	$scope.inputNode = function(pilih, id, text) {
		var dataPilih = {
				id: id,
				text: text
		}
		if(pilih == true) {
			$scope.dataCategories.push(dataPilih);
		} else {
			$scope.dataCategories.splice($scope.dataCategories.indexOf(dataPilih), 1);
		}
//		console.log("ISI NODE = "+JSON.stringify($scope.dataCategories));
	}
	
	$scope.inputToInventoryList = function(id, alamat, stock, moq, supply, outStock, notify) {
		if(stock != undefined) {
			var dataInventory = {
					id: id,
					alamat: alamat,
					stock: stock,
					moq: moq,
					supply: supply,
					outStock: outStock,
					notifyStock: notify
			}
			
			
			if(dataIdLama.indexOf(dataInventory.id) !== -1)
				$scope.dataInventory.splice($scope.dataInventory.indexOf(dataInventory), 1);
			
			$scope.dataInventory.push(dataInventory);
			dataIdLama.push(dataInventory.id);
			
		} else {
			RequestService.modalInformation('Data Stock Belum Semua Terisi')
			.then(function (result) {});
		}
//		console.log("ISI DATA = "+JSON.stringify($scope.dataInventory))
	}
	
	$scope.delImages = function(data) {
		RequestService.modalConfirmation('Yakin menghapus data Images ini ('+data.imagesFileName+') ?')
    	.then(function (result) {
    		var dataHapus = {
    				imagesId: data.id
    		}
    		RequestService.requestServer('/Services/Process/CatalogServices/deleteImages', dataHapus)
        	.success(function (data, status, headers, config) {
        		$scope.getAllDataFromTable($scope.dataEdit.id);
        	})
    	})
	}
	
	$scope.inputAttValForGeneral = function(mapAttId, attTypeId, value) {
		var dataAmbil = {
				mapAttId: mapAttId,
				attTypeId: attTypeId,
				value: value
		}
		
		if($scope.dataCatalogAttributes.length > 0) {
			angular.forEach($scope.dataCatalogAttributes,function(value){
				if(value.mapAttId == dataAmbil.mapAttId) {
					$scope.dataCatalogAttributes.splice($scope.dataCatalogAttributes.indexOf(value), 1);
				}
			})
		} 
		
		$scope.dataCatalogAttributes.push(dataAmbil);
		
//	    console.log("ISI DATA ATRRIBUTE = "+JSON.stringify($scope.dataCatalogAttributes))
	}
	
	$scope.inputAttValForCheckBox = function(mapAttId, attTypeId, checked) {
		var dataAmbil = {
				mapAttId: mapAttId,
				attTypeId: attTypeId
		}
		
		if(checked == true) {
			$scope.dataCatalogAttributes.push(dataAmbil);
		} else {
			if($scope.dataCatalogAttributes.length > 0) {
				angular.forEach($scope.dataCatalogAttributes,function(value){
					if(value.mapAttId == dataAmbil.mapAttId && value.attTypeId == dataAmbil.attTypeId) {
						$scope.dataCatalogAttributes.splice($scope.dataCatalogAttributes.indexOf(value), 1);
					}
				})
			}
		}
		
//		console.log("ISI DATA ATRRIBUTE = "+JSON.stringify($scope.dataCatalogAttributes))
	}
	
	$scope.simpan = function() {
		RequestService.modalConfirmation('Yakin menyimpan data Produk ini ?')
    	.then(function (result) {
//    		console.log("ISI PRICE RANGE = "+JSON.stringify($scope.dynamic_value))
//    		console.log("ISI CATEGORIES = "+JSON.stringify($scope.dataCategories));
//    		console.log("ISI INVENTORY = "+JSON.stringify($scope.inventory_value))
//    		console.log("ISI ATRRIBUTE = "+JSON.stringify($scope.dataCatalogAttributes))
    		$scope.updateCatalog($scope.dataEdit);
    	})
	}
	
	$scope.updateCatalog = function(value) {
		var data = $scope.data;
		
		var dataSimpan = {
			catalogId: value.id,
			nama_IND: data.produkNameIND,
			nama_EN: data.produkNameEN,
			deskripsi_IND: data.produkDeskripsiIND,
			deskripsi_EN: data.produkDeskripsiEN,
			kode_produk: data.produkCode,
			harga: data.harga,
			berat: data.berat,
			panjang: data.panjang,
			lebar: data.lebar,
			tinggi: data.tinggi,
			satuanBerat: data.satuanBerat,
			satuanPanjang: data.satuanPanjang,
			satuanLebar: data.satuanLebar,
			satuanTinggi: data.satuanTinggi,
			mata_uang: data.mataUang,
			satuan_produk: data.satuanProduk,
			seller: data.seller,
			produkType: data.produkType,
			attributeSet: data.attributeSet
		}
		
//		console.log("ISI DATA SIMPAN CATALOG = "+JSON.stringify(dataSimpan))
		RequestService.requestServer('/Services/Process/CatalogServices/update', dataSimpan)
    	.success(function (data, status, headers, config) {
    		if(data != undefined) {
    			if($scope.dataPriceRange != undefined)
    				$scope.updatePriceRange(data.id);
    		}
    	})
	}
	
	$scope.updatePriceRange = function(catalogId) {
		var dataHapus = {
				catalogId: catalogId
		}
		
		RequestService.requestServer('/Services/Process/CatalogServices/deleteProdukPriceRange', dataHapus)
    	.success(function (data, status, headers, config) {
    		var hitung = 0;
    		var panjangData = $scope.dynamic_value.length;
    		
    		angular.forEach($scope.dynamic_value,function(value){
    			var dataSimpan = {
    					catalog: catalogId,
    					fromPrice: value.textFrom,
    					toPrice: value.textTo,
    					harga: value.textHarga
    			}
    			
    			RequestService.requestServer('/Services/Process/CatalogServices/createProdukPriceRange', dataSimpan)
    	    	.success(function (data, status, headers, config) {})
    	    	
    	    	hitung = hitung + 1;
    		})
    		
    		if(hitung == panjangData) {
    			$scope.updateCatalogLocation(catalogId);
        	}
    	})
	}
	
	$scope.updateCatalogLocation = function(catalogId) {
		var dataHapus = {
				catalogId: catalogId
		}
		
		RequestService.requestServer('/Services/Process/CatalogServices/deleteCatalogLocation', dataHapus)
    	.success(function (data, status, headers, config) {
    		var hitung1 = 0;
    		var panjangData1 = $scope.inventory_value.length;
    		
    		angular.forEach($scope.inventory_value,function(value){
    			var dataSimpan = {
    					catalogId: catalogId,
    					alamat: value.alamat.officeAddress,
    					perusahaan: value.alamat.companyAddressPK.companyCode,
    					stok_produk: value.stock,
    					kemampuan_suplay: value.supply,
    					stok_habis: value.outStock,
    					notifikasi_stok_habis: value.notifyStock,
    					minimal_order: value.moq
    			}
    			
    			RequestService.requestServer('/Services/Process/CatalogServices/createCatalogLocation', dataSimpan)
    	    	.success(function (data, status, headers, config) {})
    	    	
    	    	hitung1 = hitung1 + 1;
//    			console.log("ISI DATA SIMPAN = "+JSON.stringify(dataSimpan))
    		})
    		
    		if(hitung1 == panjangData1) {
    			$scope.updateCatalogCatagories(catalogId)
        	}
    	})
	}
	
	$scope.updateCatalogCatagories = function(catalogId) {
		var dataHapus = {
				catalogId: catalogId
		}
		
		RequestService.requestServer('/Services/Process/CatalogServices/deleteCatalogCategories', dataHapus)
    	.success(function (data, status, headers, config) {
    		var hitung2 = 0;
    		var panjangData2 = $scope.dataCategories.length;
    		
    		angular.forEach($scope.dataCategories,function(value){
    			var dataSimpan = {
    					catalogId: catalogId,
    					categoryId: value.id
    			}
    			
    			RequestService.requestServer('/Services/Process/CatalogServices/createCatalogCategories', dataSimpan)
    	    	.success(function (data, status, headers, config) {})
    	    	
    	    	hitung2 = hitung2 + 1;
//    			console.log("ISI DATA SIMPAN = "+JSON.stringify(dataSimpan))
    		})
    		
    		if(hitung2 == panjangData2) {
    			$scope.updateCatalogAttribute(catalogId)
        	}
    	})
	}
	
	$scope.updateCatalogAttribute = function(catalogId) {
		var dataHapus = {
				catalogId: catalogId
		}
		
		RequestService.requestServer('/Services/Process/CatalogServices/deleteCatalogAttributes', dataHapus)
    	.success(function (data, status, headers, config) {
    		var hitung3 = 0;
    		var panjangData3 = $scope.dataCatalogAttributes.length;
    		
    		angular.forEach($scope.dataCatalogAttributes,function(value){
    			if(value.attTypeId == undefined)
    				value.attTypeId = 0;
    			
    			var dataSimpan = {
    					catalogId: catalogId,
    					mapAttId: value.mapAttId,
    					attTypeId: value.attTypeId,
    					nilai: value.value
    			}
    			
    			RequestService.requestServer('/Services/Process/CatalogServices/createCatalogAttributes', dataSimpan)
    	    	.success(function (data, status, headers, config) {})
    	    	
    	    	hitung3 = hitung3 + 1;
//    			console.log("ISI DATA SIMPAN = "+JSON.stringify(dataSimpan))
    		})
    		
    		if(hitung3 == panjangData3) {
    			$scope.simpanGambarCatalog(catalogId);
        	}
    	})
	}
	
	$scope.simpanGambarCatalog = function(catalogId) {
		var hitung4 = 0;
		var panjangData4 = uploader.queue.length;
		
		angular.forEach(uploader.queue, function (item) {
			hitung4 = hitung4 + 1;
			
			var dataSimpan = {
					catalogId: catalogId,
					imagesFileName: item.file.name,
                    imagesRealName: item.realFileName,
                    imagesFileSize: item.file.size,
                    urutanPesanan: hitung4
                }
			
			RequestService.requestServer('/Services/Process/CatalogServices/createCatalogImages', dataSimpan)
	    	.success(function (data, status, headers, config) {})
	    	
//	    	console.log("ISI DATA DOKUMEN = "+JSON.stringify(dataSimpan));
		})
		
		if(hitung4 == panjangData4) {
			RequestService.modalInformation('Data Berhasil di Simpan')
    		.then(function (result) {
    			$state.go('catalog.manage');
    		})
    	}
	}
	
	// For Role User
	$scope.getRoleUser = function() {
		RequestService.getRoleUsers(RequestService.getUserLogin().user.id)
		.then(function (data) {
			$scope.roleUser = data;
		});
	}
	
	
	/** =============================================================== END ========================================================================== **/
}]);