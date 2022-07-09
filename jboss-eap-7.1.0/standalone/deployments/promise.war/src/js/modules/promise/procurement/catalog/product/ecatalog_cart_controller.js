(function () {
    'use strict';
    
    angular.module('naut').controller('ecatalogCartCtrl', ecatalogCartCtrl);

    function ecatalogCartCtrl($rootScope, $scope, RequestService, $state, $stateParams, $location, $modal, ModalService, $http, $anchorScroll, ROLE_USER) {
    	if($rootScope.userRole.nama == 'VENDOR') {
    		$location.path('/appvendor/promise/dashboard');
    	}
    	var vm = this;	
    	$rootScope.cartShow = false;
		$rootScope.grandTotal = 0;
		$rootScope.grandTotalCurrency = 'IDR';
		//$scope.useHargaEproc = false;
		
		$scope.checkedItem = [];
		$scope.isCheckedAllItem = true;
		
		if ($stateParams.catalogList != undefined) {
			$scope.catalogList = $stateParams.catalogList;
			angular.forEach($scope.catalogList, function (value, index) {
//				if(value.catalogLocationList != undefined) {
//					if(value.catalogLocationList[0].supplyAbility != undefined) {
						$scope.checkedItem.push(true);
						value.qtyCartItem = 1;
						value.isQtyCart0 = false;
						if(value.harga_eproc == null || value.harga_eproc == 0){
							$rootScope.grandTotal = $rootScope.grandTotal + (value.qtyCartItem * value.harga);
							//$scope.useHargaEproc = false;
						
						}
						else{
							$rootScope.grandTotal = $rootScope.grandTotal + (value.qtyCartItem * value.harga_eproc);
							//$scope.useHargaEproc = true;
						}
						$rootScope.grandTotalCurrency = value.mataUang.kode;
//					} else {
//						ModalService.showModalInformation('Item - '+value.kodeProduk+' melebihi Supply Ability');
//						value.qtyCartItem = 1;
//					}
//				} else {
//					ModalService.showModalInformation('Item - '+value.kodeProduk+' Tidak memiliki Stock');
//					$state.go('app.promise.procurement-ecatalog2');
//				}
			});
		}
		
		//Get Role User
        RequestService.getRoleUsers(RequestService.getUserLogin().user.id).then(function (data) {
            $scope.roleUser = data;
        });
		
        
        var thisRoleUser = RequestService.getRoleUser().nama;
		$scope.createPr = false;
		$scope.createBookingOrder = false;
        $scope.createBookingOrder = true;
       
            

		$scope.onChangeItemQty = function () {
			if ($scope.catalogList.length > 0) {
				$rootScope.grandTotal = 0;
				angular.forEach($scope.catalogList, function (value, index) {
//					if(value.qtyCartItem <= value.catalogLocationList[0].supplyAbility) {
					
					if(value.qtyCartItem == 0 || value.qtyCartItem == undefined || value.qtyCartItem == null){
						value.isQtyCart0 = true;
					}else{
						value.isQtyCart0 = false;
					}
					
					
					if(value.harga_eproc == null || value.harga_eproc == 0){
						$rootScope.grandTotal = $rootScope.grandTotal + (value.qtyCartItem * value.harga);
						$scope.useHargaEproc = false;
					}
					else{
						$rootScope.grandTotal = $rootScope.grandTotal + (value.qtyCartItem * value.harga_eproc);
						$scope.useHargaEproc = true;
					}
						$rootScope.grandTotalCurrency = value.mataUang.kode;
//					} else {
//						ModalService.showModalInformation('Item - '+value.kodeProduk+' melebihi Supply Ability');
//						value.qtyCartItem = value.catalogLocationList[0].supplyAbility;
//					}
				});
			} else {
				$rootScope.grandTotal = 0;
			}
		}

		$scope.incrementItem = function(index) {
			$scope.catalogList[index].qtyCartItem += 1;
			$scope.onChangeItemQty();
		}

		$scope.decrementItem = function(index) {
			$scope.catalogList[index].qtyCartItem -= 1;
			$scope.onChangeItemQty();
		}

		$scope.onCheckOneItem = function(index) {
			$scope.checkedItem[index] = $scope.checkedItem[index] ? true : false;
			var isFalseExists = $scope.checkedItem.indexOf(false);
			$scope.isCheckedAllItem = (isFalseExists === -1) ? true : false;
		}

		$scope.onCheckAllItem = function() {
			$scope.isCheckedAllItem = $scope.isCheckedAllItem ? true : false;
			for(let i = 0; i < $scope.checkedItem.length; i++) {
				$scope.checkedItem[i] = $scope.isCheckedAllItem ? true : false;
			};
		}
		
		$scope.removeSelectedItem = function() {
			var arrayIsChecked = [];
			$scope.checkedItem.forEach((isChecked, index) => {
				// get isChecked array index
				if(isChecked) arrayIsChecked.push(index);
			});
			// check if not is checked item, than return
			if(arrayIsChecked.length === 0) return;

			var message = '';
			if(arrayIsChecked.length > 1) {
				var itemLength = arrayIsChecked.length;
				message = `Anda yakin ingin menghapus (${itemLength}) Item?`;
			} else {
				var itemChecked = $scope.catalogList[arrayIsChecked[0]]; 
				message = `Anda yakin ingin menghapus Item ${itemChecked.namaIND}?`;
			}

			RequestService.modalConfirmation(message)
				.then(function(){
					var catalogListTemp = $scope.catalogList.filter((catalog, index) => {
						// get unchecked item
						if(!arrayIsChecked.includes(index)) return catalog;
						// removed checked item from rootScope cart
						else $rootScope.removeCart($scope.catalogList[index]);
					});
					// set checked item to an emtpy array
					$scope.checkedItem = [];
					catalogListTemp.forEach(item => {
						// set checked item to fals from new catalog list
						$scope.checkedItem.push(false);
					});
					// set catalog list from unchecked item
					$scope.catalogList = catalogListTemp;
					$scope.onChangeItemQty();
			})
		}

		$scope.removeItem = function(index){
			RequestService.modalConfirmation('Anda yakin ingin menghapus Item "'+$scope.catalogList[index].namaIND+'"?')
				.then(function(){
				$scope.catalogList.splice(index, 1);
				$scope.onChangeItemQty();
				$rootScope.removeCart($scope.catalogList[index]);
			})
		}

		$scope.btnCatalogList = function () {
			$rootScope.cartShow = true;
			$state.go('app.promise.procurement-ecatalog2');
		}
		
		$scope.btnAddPR = function () {
			
			$rootScope.cartList = [];
			$rootScope.materialList = [];
			$rootScope.itemPRList = [];
			
			// Tambah Item ke dalam List
			angular.forEach($scope.catalogList, function (item, indexItem) {
				var itemObj = {
	                    material: item,
	                    kode: item.kodeProduk,
	                    nama: item.namaIND,
	                    deskripsi: item.deskripsiIND,
	                    vendor: item.vendor.nama,
	                    quantity: item.qtyCartItem,
	                    price: item.harga,
	                    unit: item.satuan.nama,
	                    matauangId: item.mataUang.id,
	                    matauang: item.mataUang.kode,
	                    vendor: item.vendor.nama,
	                    vendorId: item.vendor.id,
	                    specification: item.deskripsi,
	                    costcenteritem: $rootScope.nomorDraft
	                };
				
				$rootScope.itemPRList.push(itemObj);
			});
			$rootScope.fromCart = true;
			$location.path('/app/promise/procurement/purchaserequest/add');
		}
		
		
		//========================generate BO=======================//
		$scope.showFormCreateSo = false;
		vm.catalogData= {
				catalogFee : {},
				id : 0
		};
		RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/getOrganisasiLogin')
	        .then(function (data) {
	            $scope.organisasi = data.organisasi;
	    });
		$scope.loading = false;
		$scope.btnCreateSO = function (){
			var validCatalogFee = true;
			var validQty = true;
			
			if($scope.catalogList.length == 0) {
				RequestService.informError("Masukan minimal 1 item pada cart");
				$scope.showFormCreateSo = false;
			} else {
				$scope.loading = false;
				var isValidForm = true;
				angular.forEach($scope.catalogList, function (value, index) {
//							if(value.qtyCartItem <= value.catalogLocationList[0].supplyAbility) {
					
					if(value.qtyCartItem == 0 || value.qtyCartItem == undefined || value.qtyCartItem == null){
						isValidForm = false;
					}
					
				})
				
				if(isValidForm){
					$scope.showFormCreateSo = true;
			    	 var salesorder ={};
			    	 var data = {};
			    	 var listcat = [];
			    	 angular.forEach($scope.catalogList , function(val, index) {

			    			 data.vendor = val.vendor;
			    			 data.soNumber = "";
			    			 data.errorSoNumber = "";
			    			 listcat.push(val);	 	 		    			 
			    	 })
			    	 data.listCatalog = listcat;
			    	 salesorder=data;
			    	 
			    	 //grand total yang sudah dikali qty
			    	
			    		 var totalWithQty = 0;
			    		 angular.forEach(salesorder.listCatalog, function(valListCat, index) {
			    			 if(valListCat.harga_eproc == null || typeof valListCat.harga_eproc == 'null' || valListCat.harga_eproc == 0){
			    				 totalWithQty = totalWithQty + ((valListCat.harga)*(valListCat.qtyCartItem));
			    			 }else{
			    				 totalWithQty = totalWithQty + ((valListCat.harga_eproc)*(valListCat.qtyCartItem));
			    			 }
			    		 })
			    		 salesorder.grandTotalWithQty = totalWithQty;
			    	
			    	 
			    	 $scope.salesOrder = salesorder;
			    	 
			    	 //$scope.gotoAnchor(1);
			    	 var validJumlah =true;
			    	 angular.forEach($scope.catalogList,function (value, data){
			    		 if(validJumlah){
			    			 /*if(value.qtyCartItem > value.currentStock){
			    				 validJumlah = false
			    				 RequestService.modalInformation('Jumlah item '+value.item.nama+' Melebihi Stock yang tersedia!');
			    			 }else*/ 
			    			 if(value.maksimumQuantityOrderDay != null && value.qtyCartItem > value.maksimumQuantityOrderDay){
			    				 validJumlah = false
			    				 RequestService.modalInformation('Jumlah item '+value.item.nama+' telah melewati batas Maksimum Kuantitas Pemesanan Total Per Hari. Silahkan cek lagi jumlah yang anda Masukkan!');
			    			 }else if(value.maksimumQuantityPerOrder != null && value.qtyCartItem > value.maksimumQuantityPerOrder){
			    				 validJumlah = false
			    				 RequestService.modalInformation('Jumlah item '+value.item.nama+' telah melewati batas Maksimum Kuantitas Pemesanan per Order. Silahkan cek lagi jumlah yang anda Masukkan!');
			    			 }else if(value.minimumQuantityOrder != null && value.qtyCartItem < value.minimumQuantityOrder){
			    				 validJumlah = false
			    				 RequestService.modalInformation('Jumlah Minimum item '+value.item.nama+' adalah '+value.minimumQuantityOrder+'. Silahkan cek lagi jumlah yang anda Masukkan!');
			    			 }	 
			    		 }
			    	 });
			    	 var tampIndex = 0;
			    	 angular.forEach($scope.catalogList, function (data, index) {
			    		if(validQty){/* perubahan KAI 24/02/2021 [22288]*/
			    			if(data.catalogContractDetail.qtyMaxOrder != null && data.qtyCartItem > data.catalogContractDetail.qtyMaxOrder){
			    				ModalService.showModalInformation('Item '+data.namaIND+' Tidak Mencukupi Kuantitinya Untuk Booking Order ini, Kuantiti yang tersedia Sebesar '+data.catalogContractDetail.qtyMaxOrder+'. Mohon Untuk Merevisi Kuantiti Atau Menghapus Item Tersebut!');
			    				validQty = false;
			    			}else{
			    				RequestService.doGET('/procurement/purchaseRequestServices/get-catalog-fee/'+ data.id).then(function success(dataFee) {
									if(validCatalogFee){
										if (dataFee.catalogFee != null || dataFee.catalogFee != undefined) {
											if(dataFee.catalogFee.ongkosKirim == null || dataFee.catalogFee.ongkosKirim == undefined){
												ModalService.showModalInformation('Item '+data.namaIND+' belum diatur ongkos kirimnya untuk lokasi '+$scope.organisasi+' . Silahkan hubungi admin untuk ditindaklanjuti!');
												$scope.loading = false;
												validCatalogFee = false;
											}else{
												if(validJumlah && tampIndex == $scope.catalogList.length-1 && validCatalogFee){
										    		 $state.go('app.promise.procurement-booking-order-view', {
										    			 salesOrder: salesorder
										    		 });			    		 
										    	 }
											}
										}else {
											ModalService.showModalInformation('Item '+data.namaIND+' belum diatur ongkos kirimnya untuk lokasi '+$scope.organisasi+' . Silahkan hubungi admin untuk ditindaklanjuti!');
											$scope.loading = false;
											validCatalogFee = false;
										}
									}
									tampIndex +=1;
								
								}, function error(response) {				 
									RequestService.informInternalError();
								});
			    			}
			    		}
						
			    	 });
				}else{
					RequestService.modalInformation(
							'Jumlah item belum sesuai silahkan periksa kembali!',
							'danger').then(function(data) {
					});
				}
					
			}
        };
        
       $scope.gotoAnchor = function(x) {
	      var newHash = 'anchor' + x;
	      if ($location.hash() !== newHash) {
	        // set the $location.hash to `newHash` and
	        // $anchorScroll will automatically scroll to it
	        $location.hash('anchor' + x);
	      } else {
	        // call $anchorScroll() explicitly,
	        // since $location.hash hasn't changed
	        $anchorScroll();
	      }
	    };
	    
	    $scope.getAddressBookList = function () {
	    	$rootScope.companyAddresslist =[];
			RequestService.doGET('/procurement/master/AddressBookServices/getAddressBookListByToken')
			.then(function success(data) {
				$rootScope.companyAddresslist = data;
				var companyAddress = {
						addressLabel : 'Alamat Baru',
						isNew : true
					};
					$rootScope.companyAddresslist
							.push(companyAddress);
				
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.getAddressBookList();

		/////get bill addres
        $rootScope.billToAddress = {};
        $rootScope.disabledbillto = true;
		$scope.setBillToAddress = function(index, companyAddressSelected) {
		$rootScope.billToAddress = companyAddressSelected;
		$scope.objAddressLabel = companyAddressSelected;
		if (companyAddressSelected.isNew != true) {
			$rootScope.disabledbillto = true;
			$scope.addressLabelBillTo = companyAddressSelected.addressLabel;
			$scope.namaBillTo = companyAddressSelected.fullName;
			$scope.telpBillTo = companyAddressSelected.telephone1;
			$scope.alamatBillTo = companyAddressSelected.addressLabel
					+ ', '
					+ companyAddressSelected.streetAddress
					+ ', '
					+ companyAddressSelected.city
					+ ', '
					+ (companyAddressSelected.country != null ? companyAddressSelected.country : '')
			
		} else {
			$rootScope.disabledbillto = false;
			$scope.namaBillTo = '';
			$scope.telpBillTo = '';
			$scope.alamatBillTo = '';
		}
	};
		
		// get ship address
	$rootScope.shipToAddress = {};
	$rootScope.disabledshipto = true;
	$scope.setShipToAddress = function(index, companyAddressSelected) {
		$rootScope.shipToAddress = companyAddressSelected;	
		if (companyAddressSelected.isNew != true) {
			$rootScope.disabledshipto = true;
			$scope.namaShipTo = companyAddressSelected.fullName;
			$scope.tlpShipTo = companyAddressSelected.telephone1;
			$scope.alamatShipTo  = companyAddressSelected.addressLabel
					+ ', '
					+ companyAddressSelected.streetAddress
					+ ', '
					+ companyAddressSelected.city
					+ ', '
					+ (companyAddressSelected.country != null ? companyAddressSelected.country : '')
			
		} else {
			$rootScope.disabledshipto = false;
			$scope.namaShipTo = '';
			$scope.tlpShipTo = '';
			$scope.alamatShipTo = '';
		}
		
	};
	
	 
	 $scope.setShipTo = function (isSetShipTo){
		 if(isSetShipTo){
			 	$rootScope.disabledshipto = true;
			 	$scope.addressLabelShipTo = $scope.objAddressLabel;
				$scope.namaShipTo = $scope.namaBillTo;
				$scope.tlpShipTo = $scope.telpBillTo;
				$scope.alamatShipTo  = $scope.alamatBillTo;
							
			} else {
				$rootScope.disabledshipto = false;
				$scope.addressLabelShipTo = '';
				$scope.namaShipTo = '';
				$scope.tlpShipTo = '';
				$scope.alamatShipTo = '';
			}
	 }
	 
        $scope.btnGenerateNumber = function(valIndex) {
        	$http.get($rootScope.backendAddress + '/procurement/salesorder/SalesOrderServices/getCoNumber')
			.success(function(data, status, headers, config) {
	        	 angular.forEach($scope.salesOrderList , function(val, index) {
	        		 if(index==valIndex){
	        			 val.soNumber = data.co;
	        		 }
	        	 });
			})
			
		};
		
		 $scope.validateForm = function () {
	        	var isValid = true;
	        	if (typeof $scope.soNumber == 'undefined' || $scope.soNumber.length == 0 || $scope.soNumber == "" ) {
	    			isValid = false;
	    			$scope.errormessageFieldSO = 'CO Number harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldSO = '';
	    		}
	        	
	        	if (typeof $scope.namaBillTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldNmBill = 'Nama Bill To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldNmBill = '';
	    		}
	        	
	        	if (typeof $scope.alamatBillTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldALBill = 'Alamat Bill To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldALBill = '';
	    		}
	        	
	        	if (typeof $scope.telpBillTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldTlpBill = 'Telpon Bill To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldTlpBill = '';
	    		}
	        	
	        	if (typeof $scope.namaShipTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldNmShip = 'Nama Ship To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldNmShip = '';
	    		}
	        	
	        	if (typeof $scope.alamatShipTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldALShip = 'Alamat Ship To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldALShip = '';
	    		}
	        	
	        	if (typeof $scope.tlpShipTo == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageFieldtlpShip = 'Tlp Ship To harus diisi!';
	    		}else{
	    			isValid = true;
	    			$scope.errormessageFieldtlpShip = '';
	    		}
	        	
	        	// cek BO NUMBER
	        	 angular.forEach($scope.salesOrderList , function(val, index) {
	        		 if(val.soNumber == "" || val.soNumber.length == 0){
	        			 isValid = false;
	        			 val.errorSoNumber ="Bo Number harus Diisi";
	        		 }else{
	        			 isValid = true;
	        			 val.errorSoNumber ="";
	        		 }
	        	 });
	       
	        	return isValid;
	        	
	        }
		 
		 
		 $scope.btnGenerateSO = function () {
			 	$rootScope.cartList = [];
	        	$rootScope.soNumber = $scope.soNumber;
	        	var temp = {};
	        	
	        	$rootScope.billToAddress.billToName = $scope.namaBillTo;
	    		$rootScope.billToAddress.billToAddress = $scope.alamatBillTo;
	    		$rootScope.billToAddress.billToTelp =$scope.telpBillTo;
	    		$rootScope.shipToAddress.shipToName = $scope.namaShipTo;
	    		$rootScope.shipToAddress.shipToAddress =$scope.alamatShipTo;
	    		$rootScope.shipToAddress.shipToTelp =$scope.tlpShipTo;    		
	    		
	    		var isvalid = $scope.validateForm();
	    		if(isvalid){
	    			$scope.save();
	    		}
	        	
	        };
	        
		 
		 $scope.save = function () {
		    	
		    	RequestService.modalConfirmation().then(function (result) {
		    		$scope.loading = true;
		    		var data = {};
		    		
		    		data.salesOrder = {
		        		billToName :$rootScope.billToAddress.billToName,
		        		billToAddress : $rootScope.billToAddress.billToAddress,
		        		billToTelp : $rootScope.billToAddress.billToTelp,
		        		shipToName : $rootScope.shipToAddress.shipToName,
		        		shipToAddress : $rootScope.shipToAddress.shipToAddress,
		        		shipToTelp : $rootScope.shipToAddress.shipToTelp,
		        		status : 0
		        		
		        	}
		    		var salesorderList = [];
		    		
		    		    		
		    		angular.forEach($scope.salesOrderList, function(val, index) {
		    			
		    			angular.forEach(val.listCatalog, function(value, index) {
		        			var salesOrderItem = {
		        					catId : value.id,
		        	        		qty : value.qtyCartItem,
		        	        		soNumber : val.soNumber
		        	        	}
		        			
		        			salesorderList.push(salesOrderItem);
		    			})
	        			
		    		})
		    			
		    		data.salesOrderItemList = salesorderList;
		    		
		    		RequestService.doPOSTJSON('/procurement/salesorder/SalesOrderServices/create', data)
					.then(function successCallback(data) {
						
							$scope.sendEmail();
						 //ModalService.closeModalInformation();
					}, function errorCallback(response) {				 
							ModalService.closeModalInformation();
							ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
		    	});
		    };
		    
		    
		    $scope.sendEmail = function (){
		    	
		    	 var data = {};
		    	 
		    	 data.salesOrder = {};
		    	 
		    	 var salesorderList =[];
		    	 angular.forEach($scope.salesOrderList , function(val, index) {
		    		var isAdd = true;
		    		 
		    		 angular.forEach(salesorderList , function(valId, index) {		    		
		    			 
		    			 if(valId.venId == val.vendor.id){
		    				 isAdd = false;
		    			 }
		    		 })
		    		 
		    		 if(isAdd){
		    			 var salesOrderItem = {
		        					venId : val.vendor.id,
		        					soNumber :val.soNumber
		        	        	}
		        			
		    			 salesorderList.push(salesOrderItem);
		    		 }
		    		 
		    	 })
		    	 
		    		    	 
		    	 data.salesOrderItemList = salesorderList;
		    	 
		    	RequestService.doPOSTJSON('/procurement/salesorder/SalesOrderServices/insertNotificationCo', data)
					.then(function successCallback(data) {
						 $state.go('app.promise.procurement-sales-order');
						 RequestService.informSaveSuccess();
						 //ModalService.closeModalInformation();
						 $scope.loading = false;
				}, function errorCallback(response) {				 
						ModalService.closeModalInformation();
						ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
	           
		    }
}

ecatalogCartCtrl.$inject = ['$rootScope', '$scope', 'RequestService', '$state', '$stateParams', '$location', '$modal', 'ModalService', '$http', '$anchorScroll', 'ROLE_USER'];

})();