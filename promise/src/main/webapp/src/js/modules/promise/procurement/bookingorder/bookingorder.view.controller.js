(function () {
    'use strict';
    
    angular.module('naut').controller('bookingOrderCtrl',  bookingOrderCtrl);

    function bookingOrderCtrl($rootScope, $scope, RequestService, $state, $stateParams, $location, $modal, ModalService, $http, $anchorScroll, ROLE_USER, $resource, toaster) {
    	if($scope.userRole.nama == 'VENDOR') {
    		$location.path('/appvendor/promise/dashboard');
    	}
    	if ($rootScope.cartList.length > 0) {
        	$rootScope.cartShow = false;
        	$rootScope.cartList.length = [];
        }
    	var vm = this;
    	var isCatalogFee = true;
    	$scope.salesOrder = ($stateParams.salesOrder != undefined) ? $stateParams.salesOrder : {};
    	$rootScope.cartShow = false;
    	$scope.grandTotal = 0;
    	$scope.grandTotalCurrency = 'IDR';
		// $scope.useHargaEproc = false;
		vm.isGenerated = false;
		vm.tempBulkPriceDiskon=[];
		vm.tempAsuransiList=[];
		vm.tempAsuransiList2=[];
		vm.tempOngkirList=[];
		$scope.salesOrder.grandTotalWithQty=0;
		vm.qtyList=[];
		vm.hargaNormalList=[];
		vm.totalOngkir=0;
		vm.totalAsuransi=0;
		vm.total=0;
		vm.slaDeliveryTime = 0;
		vm.slaDeliveryTimeList = [0];
		vm.quantity=0;
		vm.totalBarang=0;
		vm.catalogData= {
				catalogFee : {},
				id : 0
		};
		vm.tempNomorKontrakList=[];
		vm.nomorKontrakList='';
		vm.nomorKontrakList2 = '';
		vm.list = null;
		vm.isSame = false;
		
		vm.gvDoctype ='';
		vm.gvHeadnote ='';
		vm.gvIntermsoft ='';
		vm.gvAttachment ='';
		vm.glAccount ='';
		
		vm.costCenter ='';
		vm.busArea ='';
		vm.storeLoc ='';
		$scope.errormessageGLAccount = [[]];
		vm.catalogDtoList=[];
		// KAI - 20210203 - #20867
		vm.gLAccount = [[]];
		vm.qty = [[0]];
		vm.totalHargaShipTo = [];
		vm.totalOngkirShipTo = [];
		vm.subTotalShipTo = [];
		vm.disableButtonAdd = true;
		vm.totalPerItem = [[]];
		vm.totalOngkirItem = [[]];
		vm.qtyCartItem= [[]];
		$scope.errormessageQty=[[]];
		vm.isDisableList = [];
		vm.buttonIsDisable = false;
		vm.shippingList = [];
		vm.checkBoxList = [];
		$scope.checkedItem = [];
		vm.totalAll = 0;
		vm.totalOngkirAll = 0;
		vm.grandTotal = 0;
		$scope.errormessageFieldNmShip=[];
		$scope.errormessageFieldALShip=[];
		$scope.errormessageFieldtlpShip=[];
		vm.qtyAllUsed = false;
		
		
		
		vm.shippingToList =[{
				sequence : 1,
				index : 0,
				id:null,
				namaShipTo : '',
				tlpShipTo : '',
				alamatShipTo : '',
				listCatalog:$scope.salesOrder.listCatalog
		}];
		vm.priceIncludePPN = [];
		angular.forEach($scope.salesOrder.listCatalog, function(catalog,index){
			vm.priceIncludePPN[index] = catalog.harga;
			if(catalog.catalogKontrak.isPpn == 1){
				vm.priceIncludePPN[index] = catalog.harga * (10/11);
			}
		});
		
		vm.addressBookIdList=[{id:null}];
		
		$scope.panes = [{
			heading: 'PR',
			include: 'app/views/promise/procurement/bookingorder/purchaserequest/purchaserequest.view.html',
			active: true,
			disabled:true
		}, {
			heading: 'Shipping To ',
			include: 'app/views/promise/procurement/bookingorder/shipping/shipping.view.html',
			active: false,
			disabled:true
		}];
		
		
		
		
		// KAI - 20201207
		$scope.getList = function () {
		var ongkirItem =[];
		var totalItem =[];
		var qtyTemp = [];
		var errormessageQty = [];
		var isDisable = [];
		angular.forEach($scope.salesOrder.listCatalog, function(data,index){
			
			var obj = {};
			obj.catalogDto= {
					id : data.id,
					qtyCartItem: data.qtyCartItem,
					harga_eproc: data.harga_eproc,
					harga: data.harga
					
			};
			vm.catalogDtoList.push(obj.catalogDto);
			vm.qtyList[index]=data.qtyCartItem;
			vm.hargaNormalList[index]=data.harga;
			ongkirItem.push(0);
			totalItem.push(0);
			qtyTemp.push(data.qtyCartItem);
			errormessageQty.push('');
			isDisable.push(false);
		});
		
		vm.totalOngkirItem[0]=ongkirItem;
		vm.totalPerItem[0] = totalItem;
		vm.totalHargaShipTo[0] = 0;
		vm.totalOngkirShipTo[0] = 0;
		vm.subTotalShipTo[0] = 0;
		vm.qtyCartItem[0] = qtyTemp;
		$scope.errormessageQty[0] = errormessageQty;
//		vm.isDisable[0]=isDisable;
		$scope.errormessageFieldNmShip[0]='';
		$scope.errormessageFieldALShip[0]='';
		$scope.errormessageFieldtlpShip[0]='';
		
		
		RequestService.doPOSTJSON('/procurement/purchaseRequestServices/get-booking-order-view-catalog', vm.catalogDtoList)
		.then(function success(dataCat) {
			vm.tempBulkPriceDiskon = dataCat.tempBulkPriceDiskon;
			vm.tempOngkirList = dataCat.tempOngkirList; 
			vm.tempAsuransiList2 = dataCat.tempAsuransiList2
			vm.tempNomorKontrakList = dataCat.tempNomorKontrakList;
			vm.totalOngkir = dataCat.totalOngkir;
			$scope.salesOrder.grandTotalWithQty = dataCat.grandTotalWithQty;
			vm.total = dataCat.total;
			isCatalogFee = dataCat.isCatalogFee;
			vm.slaDeliveryTime = dataCat.slaDeliveryTime;
			vm.isSame = dataCat.isSame;
			vm.nomorKontrakList = dataCat.tempNomorKontrakList.join(",\n");
			
		}, function error(response) {				 
			RequestService.informInternalError();
		});
		
		}
		
		$scope.getList();
		// KAI - 20210204 - #20867
		$scope.backToPr = function () {
			$scope.panes[0].active = true
		}
		$scope.goToShipping = function () {
			
			var isvalid = $scope.validateFormPR();
			if (isvalid){
				if(vm.qty[0][0] == 0 && vm.qty.length == 1 && vm.qty[0].length == 1){
					vm.shippingToList.forEach((data,indexShipTo)=> {
						data.listCatalog.forEach((data2,index)=> {
								vm.qty[indexShipTo][index] = 0;					
						});
					});
				}											
				$scope.panes[1].active = true;
			}
			
			
			/*if($scope.validasiFee($scope.catalog.catalogFeeList)==true && $scope.validateThenNext($scope.catalog)==true){
				$scope.panes[1].active = true;
			}else{
				$scope.validateThenNext($scope.catalog);
				if($scope.validasiFee($scope.catalog.catalogFeeList)==false){
					toaster.pop('error','Kesalahan','Biaya Pengiriman minimal input adalah 1 !');
				}
				$scope.panes[1].active = false;
			}*/
			
			
		}
		
		$scope.addShippingTo = function() {
			vm.isDisableList.push(true);
			if(vm.shippingToList.length==0){
				vm.shippingToList=[];
				var shippingTo = {
						sequence : 1,
						index : 0,
						id:null,
						namaShipTo:'',
						tlpShipTo:'',
						alamatShipTo : '',
						listCatalog:$scope.salesOrder.listCatalog
				}
				vm.shippingToList.push(shippingTo);
			}
			else {
				var indexShipping = vm.shippingToList.length-1;
				var dataNol = 0;
				angular.forEach(vm.shippingToList[indexShipping].listCatalog, function(data, index){
					if(vm.qty[indexShipping][index] == 0){
						dataNol +=1;
					}
				});
				if(vm.qty[indexShipping].length > dataNol){
				
					var shippingTo = {
							sequence : vm.shippingToList[vm.shippingToList.length-1].sequence+1,
							index : vm.shippingToList[vm.shippingToList.length-1].index+1,
							id:null,
							namaShipTo:'',
							tlpShipTo:'',
							alamatShipTo : '',
							listCatalog:$scope.salesOrder.listCatalog
						}
					vm.shippingToList.push(shippingTo);
					
					var ongkirItem = [];
					var totalItem = [];
					var qtyCartItem = [];
					var qty = [];
					var errorQty = [];
					var isDisableIndexLast= [];
					var isDisableIndexCurrent = [];
					var isEmpty =  false;
					var gLAccountTemp = [];
					var errormessageGLAccountTemp = []
					angular.forEach(shippingTo.listCatalog, function(data,index){
						ongkirItem.push(0);
						totalItem.push(0);
						errorQty.push('');
						qty.push(0);
						gLAccountTemp.push(null);
						errormessageGLAccountTemp.push('');
					
					});
					//pengurangan jumlah qty
					angular.forEach(vm.qtyCartItem[vm.qtyCartItem.length-1], function(data,index){
						var sisa = vm.qtyCartItem[vm.qtyCartItem.length-1][index]-vm.qty[vm.qtyCartItem.length-1][index];
						qtyCartItem.push(sisa);
						isDisableIndexLast.push(true);
						
						if (sisa <= 0){
							isDisableIndexCurrent.push(true);	
							isEmpty = true;
						} else {
							isDisableIndexCurrent.push(false);
						}
					
					});
					
					//pemberian nilai pertama
					vm.qtyCartItem[vm.qtyCartItem.length] = qtyCartItem;
					vm.qty[vm.qty.length] = qty;
					vm.gLAccount[vm.gLAccount.length] = gLAccountTemp;
					$scope.errormessageGLAccount[$scope.errormessageGLAccount.length] = errormessageGLAccountTemp;
					vm.totalOngkirItem[vm.totalOngkirItem.length]=ongkirItem;
					vm.totalPerItem[vm.totalPerItem.length]=totalItem;
					$scope.errormessageQty[$scope.errormessageQty.length]=errorQty; // belum ok
//					vm.isDisable[vm.isDisable.length-1] = isDisableIndexLast;
//					if(isEmpty){
//						vm.isDisable[vm.isDisable.length] = isDisableIndexCurrent;
//					} 
					
					 vm.totalHargaShipTo.push(0);
				     vm.totalOngkirShipTo.push(0);
				     vm.subTotalShipTo.push(0);
				     vm.slaDeliveryTimeList.push(0);
				     $scope.errormessageFieldNmShip.push('');
				     $scope.errormessageFieldALShip.push('');
				     $scope.errormessageFieldtlpShip.push('');
				}else{
					ModalService.showModalInformation('Mohon untuk mengisi minimal salahsatu Quantity Item dari masing-masing shipping!');
				}
			}
			
		}
		
		$scope.checkBox = function(index) {
			if (vm.shippingList[index]){
				vm.checkBoxList.push(index);
			} else {
				vm.checkBoxList.splice(index,1);
			}
		}
		
		$scope.onCheckOneItem = function(index) {
			$scope.checkedItem[index] = $scope.checkedItem[index] ? true : false;
			//var isFalseExists = $scope.checkedItem.indexOf(false);
			//$scope.isCheckedAllItem = (isFalseExists === -1) ? true : false;
		}
		
		$scope.removeSelectedItem = function() {
			var arrayIsChecked = [];
			$scope.checkedItem.forEach((isChecked, index) => {
				// get isChecked array index
				if(isChecked) arrayIsChecked.push(index);
			});
			// check if not is checked item, than return
			if(arrayIsChecked.length === 0) return;

			// KAI - 20210204 - #20867
			var message = '';
			if(arrayIsChecked.length > 1) {
				var itemLength = arrayIsChecked.length;
				message = `Anda yakin ingin menghapus (${itemLength}) Item?`;
			} else {
				var itemChecked = vm.shippingToList[arrayIsChecked[0]]; 
				message = `Anda yakin ingin menghapus ?`;
			}

			RequestService.modalConfirmation(message)
				.then(function(){
					angular.forEach(arrayIsChecked, function (value){
						vm.isDisableList.splice(value, 1);						
					});
					var catalogListTemp = vm.shippingToList.filter((catalog, index) => {
						// get unchecked item
						if(!arrayIsChecked.includes(index)) return catalog;
					});
					
					var qtyCartItemTemp = [];
					var totalPerItemTemp = [];
					var totalOngkirItem = [];
					var errormessageQtyTemp = [];
					var isDisableTemp = [];
					var isDisableIndexLast= [];
					var gLAccountTemp = [];
					var errormessageGLAccountTemp = [];
					vm.qtyCartItem.filter((qtyCartItem, index) => {
						// get unchecked item
						if(!arrayIsChecked.includes(index)){
							var qtyCartItemList = [];
							var totalPerItemList = [];
							var totalOngkirItemList = [];
							var errormessageQtyList = [];
							var isDisableList = [];
							var gLAccountList = [];
							var errormessageGLAccountList = []
							
							qtyCartItem.filter((qtyCartItem2, index2) => {
								qtyCartItemList.push(qtyCartItem2);
								totalPerItemList.push(vm.totalPerItem[index][index2]);
								totalOngkirItemList.push(vm.totalOngkirItem[index][index2]);
								gLAccountList.push(vm.gLAccount[index][index2]);
								errormessageGLAccountList.push($scope.errormessageGLAccount[index][index2]);
								errormessageQtyList.push('');
								isDisableList.push(true);
								isDisableIndexLast.push(false)
							});
							
							qtyCartItemTemp.push(qtyCartItemList);
							totalPerItemTemp.push(totalPerItemList);
							totalOngkirItem.push(totalOngkirItemList);
							errormessageQtyTemp.push(errormessageQtyList);
							isDisableTemp.push(isDisableList);
							gLAccountTemp.push(gLAccountList);
							errormessageGLAccountTemp.push(errormessageGLAccountList);
						}
						
					});
					
					
					
					/* var qtyItemTemp = [];
					vm.qty.filter((qtyItem, index) => {
						// get unchecked item
						if(arrayIsChecked.includes(index)){
							var qtyItemList = 0;
							qtyItem.filter((qtyItem2, index2) => {
								if(qtyItemTemp[index2] == undefined){
									qtyItemTemp[index2] = 0;
								}
								var valueNumber = parseInt(qtyItem2);
								qtyItemTemp[index2] += valueNumber;
							});
							
						}
						
					}); */
					
					var qtyTemp = [];
					vm.qty.filter((qty, index) => {
						// get unchecked item
						if(!arrayIsChecked.includes(index)){
							var qtyList = [];
							qty.filter((qty2, index2) => {
								qtyList.push(qty2);
							});
							qtyTemp.push(qtyList);
						}
						
					});
					
					var totalHargaShipToTemp = [], totalOngkirShipToTemp = [], subTotalShipToTemp = [];
				    var errormessageFieldNmShipTemp = [] , errormessageFieldALShipTemp = [], errormessageFieldtlpShipTemp = [];
					
					
					vm.totalHargaShipTo.filter((totalHargaShipTo, index) => {
						// get unchecked item
						if(!arrayIsChecked.includes(index)){
							totalHargaShipToTemp.push(totalHargaShipTo);
							totalOngkirShipToTemp.push(vm.totalOngkirShipTo[index]);
							subTotalShipToTemp.push(vm.subTotalShipTo[index]);
							errormessageFieldNmShipTemp.push($scope.errormessageFieldNmShip[index]);
							errormessageFieldALShipTemp.push($scope.errormessageFieldALShip[index]);
							errormessageFieldtlpShipTemp.push($scope.errormessageFieldtlpShip[index]);
						
						}
						
					});
					
					
					//mencari Qty tersedia di array Pertama
					var qtyFirst = []
					angular.forEach(vm.shippingToList[0].listCatalog, function(catalog,index){
						qtyFirst.push(catalog.qtyCartItem);
					})
					
					//proses pengurangan item tersedia dengan item yang di inputkan
					vm.qtyCartItem = [];
					angular.forEach(qtyCartItemTemp, function(data,index){
						if(index == 0){
							vm.qtyCartItem[index] = qtyFirst;
						} else {
							var sisaTemp = []
							angular.forEach(data, function(data2,index2){					 
								var sisa = vm.qtyCartItem[index-1][index2]-qtyTemp[index-1][index2];
								sisaTemp.push(sisa);
							})
							
							vm.qtyCartItem.push(sisaTemp);
						}
								
					});
					

					
					// set checked item to an emtpy array
					$scope.checkedItem = [];
					$scope.qtyCartItem = [];
					catalogListTemp.forEach(item => {
						// set checked item to fals from new catalog list
						$scope.checkedItem.push(false);
					});
					qtyCartItemTemp.forEach(item => {
						
						item.forEach(item2 => {
							// set checked item to fals from new catalog list
							$scope.qtyCartItem.push(false);
						});
						
						
					});
					
					// set Per Item from unchecked item

					vm.shippingToList = [];
					vm.qty = [];
					vm.totalPerItem = [];
					vm.totalOngkirItem = [];
					$scope.errormessageQty = [];
//					vm.isDisable = []
					vm.gLAccount = []
					vm.gLAccount = gLAccountTemp ;
//					vm.isDisable = isDisableTemp;
//					vm.isDisable[vm.isDisable.length-1] = isDisableIndexLast;
					$scope.errormessageQty = errormessageQtyTemp;
					vm.totalOngkirItem = totalOngkirItem;
					vm.totalPerItem = totalPerItemTemp;
					vm.qty = qtyTemp;
					vm.shippingToList = catalogListTemp;
					
					// set Per Shipping from unchecked item
					vm.totalHargaShipTo = totalHargaShipToTemp;
					vm.totalOngkirShipTo = totalOngkirShipToTemp; 
					vm.subTotalShipTo = subTotalShipToTemp;
					
				     $scope.errormessageFieldNmShip = errormessageFieldNmShipTemp;
				     $scope.errormessageFieldALShip = errormessageFieldALShipTemp;
				     $scope.errormessageFieldtlpShip = errormessageFieldtlpShipTemp;
				     vm.disableButtonAdd = false;
					$scope.onChangeGrandTotal();

			})
		}
		
		$scope.onChangeGrandTotal = function () {
			vm.qtyAllUsed = false;
			vm.totalAll = 0;
			vm.totalOngkirAll = 0;
			angular.forEach(vm.totalHargaShipTo, function (value, index) {
    			vm.totalAll += value;	
			})
			
			angular.forEach(vm.totalOngkirShipTo, function (value, index) {
			        vm.totalOngkirAll += value;	
			})
			
			vm.grandTotal = vm.totalAll + vm.totalOngkirAll;
			
			//jika total ALL item sama dengan total item dari DB
			if (vm.totalAll == vm.total){
	        	vm.disableButtonAdd = true;
	        	vm.qtyAllUsed = true;
	        }
			
			
			
		}
		
		$scope.removeShippingTo = function() {
			/*var temp = [];
					angular.forEach(vm.checkBoxList, function (value, index) {
						
						angular.forEach(vm.qty[value], function (value2, index2) {
							if(temp[index2] == undefined){
								temp[index2] = 0;
							}
							var valueNumber = parseInt(value2);
							
							temp[index2] += valueNumber;
							
						});
							
						vm.shippingToList.splice(value,1);  
						vm.qtyCartItem.splice(value,1);
				});  
					
					angular.forEach(temp, function (value, index) {
						vm.qtyCartItem[vm.qtyCartItem.length-1][index] += value;
					}); */
			
			vm.shippingToList = vm.shippingToList.filter(
				    function(item) {
				      return !item.selected
				    }
				  );

			}
		
		$scope.qtyFunction = function (quantity,indexShipTo, indexItem) {
				
		      //  console.log($event.target.value)
				var qty = parseInt(quantity);					
		        vm.totalHargaShipTo[indexShipTo] = 0;
		        vm.totalOngkirShipTo[indexShipTo] = 0;
		        vm.subTotalShipTo[indexShipTo] = 0;
		        vm.disableButtonAdd = false;
		        $scope.errormessageQty[indexShipTo][indexItem] = '';
		        
		        var obj = {};
		        
		        if (qty > vm.qtyCartItem[indexShipTo][indexItem]){
		        	vm.disableButtonAdd = true;
	    			$scope.errormessageQty[indexShipTo][indexItem] = 'Angka yang di masukan tidak sesuai';
		        } else if (isNaN(qty)){//perubahan 22/2/2021[20867]
		        	vm.disableButtonAdd = true;
	    			$scope.errormessageQty[indexShipTo][indexItem] = 'Data harus di isi';
	    			qty = 0;
		        }
				        
			        obj.request= {
			        		catalogId : $scope.salesOrder.listCatalog[indexItem].id,
			        		qty: qty,
			        		harga: vm.shippingToList[indexShipTo].listCatalog[indexItem].harga,
			        		diskon: vm.tempBulkPriceDiskon[indexItem]/100,
			        		organisasiId: vm.addressSelected.organisasi.id
			        };
			        
			        RequestService.doPOSTJSON('/procurement/purchaseRequestServices/get-data-from-qty', obj.request)
					.then(function success(data) {
						vm.totalPerItem[indexShipTo][indexItem] = data.totalHargaItem;
						vm.totalOngkirItem[indexShipTo][indexItem] =data.totalOngkirItem;
						
						
						if(obj.request.qty == 0 ){
							vm.totalPerItem[indexShipTo][indexItem] = 0;
							vm.totalOngkirItem[indexShipTo][indexItem] = 0;
							
						}
						
						angular.forEach(vm.totalPerItem[indexShipTo], function (value, index) {
			        			vm.totalHargaShipTo[indexShipTo] += value;	
						})
						angular.forEach(vm.totalOngkirItem[indexShipTo], function (value, index) {
						        vm.totalOngkirShipTo[indexShipTo] += value;	
						})
						
						 vm.subTotalShipTo[indexShipTo] = vm.totalHargaShipTo[indexShipTo]+vm.totalOngkirShipTo[indexShipTo];
						
						//GRAND TOTAL
						$scope.onChangeGrandTotal();
						
						/* vm.totalAll = 0;
						vm.totalOngkirAll = 0;
						angular.forEach(vm.totalHargaShipTo, function (value, index) {
		        			vm.totalAll += value;	
						})
						
						angular.forEach(vm.totalOngkirShipTo, function (value, index) {
						        vm.totalOngkirAll += value;	
						})
						
						vm.grandTotal = vm.totalAll + vm.totalOngkirAll;
						
						//jika total ALL item sama dengan total item dari DB
						if (vm.totalAll == vm.total){
				        	vm.disableButtonAdd = true;
				        } */
								
						
					}, function error(response) {				 
						RequestService.informInternalError();
					});

		             
		};
		
		
		

		if ($stateParams.catalogList != undefined) {
			
			$scope.catalogList = $stateParams.catalogList;
			
			angular.forEach($scope.catalogList, function (value, index) {
						value.qtyCartItem = 1;
						value.isQtyCart0 = false;
						if(value.harga_eproc == null || value.harga_eproc == 0){
							$scope.grandTotal = $scope.grandTotal + (value.qtyCartItem * value.harga);
							
						
						}
						else{
							$scope.grandTotal = $scope.grandTotal + (value.qtyCartItem * value.harga_eproc);
							// $scope.useHargaEproc = true;
						}
						$scope.grandTotalCurrency = value.mataUang.kode;

			});
		}
		
		// Get Role User
        RequestService.getRoleUsers(RequestService.getUserLogin().user.id).then(function (data) {
            $scope.roleUser = data;
        });
		
        
        var thisRoleUser = RequestService.getRoleUser().nama;
		$scope.createPr = false;
		$scope.createBookingOrder = false;
        if (thisRoleUser == ROLE_USER['SATU']) {
        	$scope.createBookingOrder = true;
        } else if (thisRoleUser == ROLE_USER['DUA']) {
        	$scope.createPr = true;
        }
            

		$scope.onChangeItemQty = function () {
			if ($scope.catalogList.length > 0) {
				$scope.grandTotal = 0;
				angular.forEach($scope.catalogList, function (value, index) {
// if(value.qtyCartItem <= value.catalogLocationList[0].supplyAbility) {
					
					if(value.qtyCartItem == 0 || value.qtyCartItem == undefined || value.qtyCartItem == null){
						value.isQtyCart0 = true;
					}else{
						value.isQtyCart0 = false;
					}
					
					
					if(value.harga_eproc == null || value.harga_eproc == 0){
						$scope.grandTotal = $scope.grandTotal + (value.qtyCartItem * value.harga);
						$scope.useHargaEproc = false;
					}
					else{
						$scope.grandTotal = $scope.grandTotal + (value.qtyCartItem * value.harga_eproc);
						$scope.useHargaEproc = true;
					}
						$scope.grandTotalCurrency = value.mataUang.kode;
// } else {
// ModalService.showModalInformation('Item - '+value.kodeProduk+' melebihi
// Supply Ability');
// value.qtyCartItem = value.catalogLocationList[0].supplyAbility;
// }
				});
			} else {
				$scope.grandTotal = 0;
			}
		}
		
		$scope.loading = false;
	    $scope.getAddressBookList = function () {
	    	$scope.companyAddresslist =[];
	    	var catalogIdList = [];
	    	angular.forEach(vm.shippingToList[0].listCatalog, function(data){
				catalogIdList.push(data.id);
			})
	    	var object = {
					catalogIdList: catalogIdList
			}
			RequestService.doPOSTJSON('/procurement/master/AddressBookServices/getAddressBookOrganisasiList', object)
			.then(function success(data) {
				$scope.companyAddresslist = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
			});       
	    }
	    $scope.getAddressBookList();
	    
	    $scope.checkAddress = function (catalog, shippingIndex, indexCatalog) {
	    	
	    	var object = {
	    			catalogId: catalog.id,
	    			addresBookId: vm.shippingToList[shippingIndex].id
			}
			RequestService.doPOSTJSON('/procurement/master/AddressBookServices/checkAddressBookByCatalog', object)
			.then(function success(data) {
				if(!data){
					ModalService.showModalInformation('Item '+catalog.namaIND+' belum memiliki ongkos kirim untuk '+vm.shippingToList[shippingIndex].namaShipTo);
					vm.qty[shippingIndex][indexCatalog] = 0;
					vm.disableButtonAdd = false;
				}
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
			});       
	    }
	    
	    // KAI - 20201202 - [20595]
	    $scope.getCostCenterList = function () {
	    	$scope.costCenterList =[];
			RequestService.doGET('/procurement/master/cost-center/get-list')
			.then(function success(data) {
				$scope.costCenterList = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.getCostCenterList();
	    
	    $scope.getPurGroupList = function () {
	    	$scope.purGroupList =[];
			RequestService.doGET('/procurement/master/pur-group/get-list')
			.then(function success(data) {
				$scope.purGroupList = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.getPurGroupList();
	    
	    $scope.getStoreLocList = function () {
	    	$scope.StoreLocList =[];
			RequestService.doGET('/procurement/master/store-loc/get-list')
			.then(function success(data) {
				$scope.storeLocList = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.getStoreLocList();
	  /*  $scope.gLAccountList = function () {
	    	$scope.gLAccountList =[];
			RequestService.doGET('/procurement/master/gl-account/get-list')
			.then(function success(data) {
				$scope.gLAccountList = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.gLAccountList(); */
	    
	    $scope.getPurchOrgList = function () {
	    	$scope.purchOrgList =[];
			RequestService.doGET('/procurement/master/purch-org/getAllCode')
			.then(function success(data) {
				$scope.purchOrgList = data;
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
	    $scope.getPurchOrgList();
	    
	    $scope.acctasscatList = [{id:1, code:'K'}];
	  // $scope.gLAccountList = [{id:1, code:'K'}];
	    

		// ///get bill addres
        $scope.billToAddress = {};
        $scope.disabledbillto = true;
		$scope.setBillToAddress = function(index, companyAddressSelected) {
		$scope.billToAddress = companyAddressSelected;
		$scope.objAddressLabel = companyAddressSelected;
		if (companyAddressSelected.isNew != true) {
			$scope.disabledbillto = true;
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
			$scope.disabledbillto = false;
			$scope.namaBillTo = '';
			$scope.telpBillTo = '';
			$scope.alamatBillTo = '';
		}
	};
		
		// get ship address
	$scope.shipToAddress = {};
	$scope.disabledshipto = true;
	$scope.setShipToAddress = function(index, companyAddressSelected) {
		vm.addressBookId = companyAddressSelected.id;
		vm.shippingToList[index].id = companyAddressSelected.id;
		vm.isDisableList[index] = false;
		$scope.shipToAddress[index] = companyAddressSelected;
		vm.addressSelected = companyAddressSelected;
		var catalogIdList = [];
		angular.forEach(vm.shippingToList[index].listCatalog, function(data){
			catalogIdList.push(data.id);
		})
		var object = {
				organisasiId: companyAddressSelected.organisasi.id,
				catalogIdList: catalogIdList
		}
		RequestService.doPOSTJSON('/procurement/purchaseRequestServices/getCatalogFeeByCatalogAndOrgId', object)
		.then(function success(data) {
			vm.slaDeliveryTimeList[index] = data.slaDeliveryTime;		
		});
		if (companyAddressSelected.isNew != true) {
			$scope.disabledshipto = true;
			vm.shippingToList[index].namaShipTo= companyAddressSelected.fullName;
			vm.shippingToList[index].tlpShipTo = companyAddressSelected.telephone1;
			vm.shippingToList[index].alamatShipTo  = companyAddressSelected.addressLabel
					+ ', '
					+ companyAddressSelected.streetAddress
					+ ', '
					+ companyAddressSelected.city
					+ ', '
					+ (companyAddressSelected.country != null ? companyAddressSelected.country : '')
			
		} else {
			$scope.disabledshipto = false;
			// KAI - 20210203 - #20867
			vm.shippingToList[index].namaShipTo = '';
			vm.shippingToList[index].tlpShipTo = '';
			vm.shippingToList[index].alamatShipTo = '';
		}
		
		for (var i = 0; i < vm.shippingToList[index].listCatalog.length; i++){
			$scope.qtyFunction(vm.qty[index][i],index, i);
		}
	};
	
	 
	 $scope.setShipTo = function (isSetShipTo){
		 if(isSetShipTo){
			 	$scope.disabledshipto = true;
			 	$scope.addressLabelShipTo = $scope.objAddressLabel;
				$scope.namaShipTo = $scope.namaBillTo;
				$scope.tlpShipTo = $scope.telpBillTo;
				$scope.alamatShipTo  = $scope.alamatBillTo;
							
			} else {
				$scope.disabledshipto = false;
				$scope.addressLabelShipTo = '';
				$scope.namaShipTo = '';
				$scope.tlpShipTo = '';
				$scope.alamatShipTo = '';
			}
	 }
	 
        $scope.btnGenerateNumber = function() {
        	vm.isGenerated = true;
        	$http.get($scope.backendAddress + '/procurement/purchaseRequestServices/getBONumberByOrganisasiId')
			.success(function(data, status, headers, config) {
				vm.boNumber = data.co;
			}).error(function(data, status, headers, config) {
				vm.isGenerated = false;
			})
			
		};
		$scope.btnGenerateNumber();
		
		 $scope.validateFormPR = function () {
			 var isValid = true;
	        	
	        	if (typeof vm.nomorKontrakList == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageLinkLampiranKontrak = 'Lampiran Kontrak harus diisi!';
	    		}else{
	    			$scope.errormessageLinkLampiranKontrak = '';
	    		}

	        	if (typeof vm.gvHeadnote == 'undefined' || vm.gvHeadnote == '') {
	    			isValid = false;
	    			$scope.errormessageheadnote = 'Judul Pembelian harus diisi!';
	    		}else{
	    			$scope.errormessageheadnote = '';
	    		}
	        	if (typeof vm.costCenter == 'undefined' || vm.costCenter == '') {
	    			isValid = false;
	    			$scope.errormessageCostCenter = 'Cost Center harus diisi!';
	    		}else{
	    			$scope.errormessageCostCenter = '';
	    		}
	        	if (typeof vm.purGroup == 'undefined' || vm.purGroup == '') {
	    			isValid = false;
	    			$scope.errormessagePurGroup = 'Purchasing Group harus diisi!';
	    		}else{
	    			$scope.errormessagePurGroup = '';
	    		}
	        	if (typeof vm.storeLoc == 'undefined' || vm.storeLoc == '') {
	    			isValid = false;
	    			$scope.errormessageStoreLoc = 'Store Loc harus diisi!';
	    		}else{
	    			$scope.errormessageStoreLoc = '';
	    		}
	        	if (typeof vm.purchOrg == 'undefined' || vm.purchOrg == '') {
	    			isValid = false;
	    			$scope.errormessagePurchOrg = 'Purch Org harus diisi!';
	    		}else{
	    			$scope.errormessagePurchOrg = '';
	    		}
	        	if (typeof vm.gvDoctype == 'undefined' || vm.gvDoctype == '') {
	    			isValid = false;
	    			$scope.errormessageDoctype = 'Doc Type harus diisi!';
	    		}else{
	    			$scope.errormessageDoctype = '';
	    		}
	        	
	        	

	        	
	        	return isValid;
	        	
	        }
		
		 $scope.validateForm = function () {
			 var isValid = true;
	        	
	        	
	        	if (typeof vm.nomorKontrakList == 'undefined') {
	    			isValid = false;
	    			$scope.errormessageLinkLampiranKontrak = 'Lampiran Kontrak harus diisi!';
	    		}else{
	    			$scope.errormessageLinkLampiranKontrak = '';
	    		}
	        	
	        	vm.shippingToList.forEach((data,indexShipTo)=> {

	        		if (typeof data.namaShipTo == 'undefined' || data.namaShipTo == '') {
		    			isValid = false;
		    			$scope.errormessageFieldNmShip[indexShipTo] = 'Nama Ship To harus diisi!';
		    		}else{
		    			$scope.errormessageFieldNmShip[indexShipTo] = '';
		    		}
	        		
	        		/*if (typeof data.alamatShipTo == 'undefined' || data.alamatShipTo == '' ) {
		    			isValid = false;
		    			$scope.errormessageFieldALShip[indexShipTo] = 'Alamat Ship To harus diisi!';
		    		}else{
		    			$scope.errormessageFieldALShip[indexShipTo] = '';
		    		}
	        		
	        		if (typeof data.tlpShipTo == 'undefined'|| data.tlpShipTo == '') {
		    			isValid = false;
		    			$scope.errormessageFieldtlpShip[indexShipTo] = 'Tlp Ship To harus diisi!';
		    		}else{
		    			$scope.errormessageFieldtlpShip[indexShipTo] = '';
		    		}*/
	        		
	        		angular.forEach($scope.salesOrder.listCatalog, function(value, index) {
	        			
	        			if (typeof vm.gLAccount[indexShipTo][index] == 'undefined' || vm.gLAccount[indexShipTo][index] == '' || vm.gLAccount[indexShipTo][index] == null) {
	    	    			isValid = false;
	    	    			$scope.errormessageGLAccount[indexShipTo][index] = 'GLAccount harus diisi!';
	        			} else {
	        				$scope.errormessageGLAccount[indexShipTo][index] = '';
	        			}
	        			
	        			if (vm.qty[indexShipTo][index] == undefined || vm.qty[indexShipTo][index] == null ){
	        				isValid = false;
	        				$scope.errormessageQty[indexShipTo][index] = 'Data harus di isi';
	        			} else {
	        				$scope.errormessageQty[indexShipTo][index] = '';
	        			}
	        			
	        		});
	        	});
	        	var isSameAddress = false;
	        	for(var x = 0; x < vm.shippingToList.length; x++){
	        		for(var y = x + 1; y < vm.shippingToList.length; y++){
		        		if(vm.shippingToList[x].id == vm.shippingToList[y].id){
		        			isSameAddress = true;
		        		}
		        			
		        	}
	        	}
	        	if(isSameAddress){
	        		isValid = false;
	        		ModalService.showModalInformation('Mohon untuk memilih Alamat yang berbeda!');
	        	}
	        	
	        	return isValid;
	        	
	        }
		 
		 /*
			 * $scope.orgApprovalPath = function () {
			 * RequestService.doGET('/procurement/purchaseRequestServices/get-list-by-organisasi-id').then(function
			 * success(data) { vm.orgApprovalPathList = data; }, function
			 * error(response) { RequestService.informInternalError();
			 * vm.loading = false; }); } $scope.orgApprovalPath();
			 */
		 
		 $scope.save = function () {
	    		console.log(vm.shippingToList);
	    		var isvalid = $scope.validateForm();
	    		if(isCatalogFee){
		    		if(isvalid){
		    			//perubahan 22/02/2020 [20867]
		    			var allQtyTamp = 0;
		    			var allQtyReal = 0;
		    			vm.shippingToList.forEach((data,indexShipTo)=> {
							data.listCatalog.forEach((data2,index)=> {
								allQtyTamp += vm.qty[indexShipTo][index];
								allQtyReal += vm.qtyCartItem[indexShipTo][index];
							});
						});
		    			if(allQtyTamp < allQtyReal){
		    				vm.qtyAllUsed = false;
		    			}
		    			if(vm.qtyAllUsed){
			    			$scope.saveData();
		    			} else {
//		    				vm.disableButtonAdd = true;
		    				ModalService.showModalInformation('Quantity item yang tersedia belum di gunakan semua, silahkan sesuaikan kembali inputan Quantity nya.');
		    			}

		    		}
	    		}

	        	
	        };
	        
		 
		 $scope.saveData = function () {
		    	
		    	RequestService.modalConfirmation().then(function (result) {
		    		$scope.loading = true;
		    		ModalService.showModalInformationBlock();
		    		var data = {};
		    		// KAI - 2021/01/08 - [21483]
		    		delete vm.storeLoc['$$hashKey'];
		    		delete vm.purGroup['$$hashKey'];
		    		delete vm.purchOrg['$$hashKey'];
		    		data.bookingOrderDTO = {
		        		
    	        		boNumber : vm.boNumber,
		        		  // 
		        		grandTotalWithQty:vm.grandTotal, //
		        		qtyList:vm.qtyList, //
		        		hargaNormalList:vm.hargaNormalList,
		        		totalOngkir:vm.totalOngkirAll,
		        		discountList:vm.tempBulkPriceDiskon,
		        		// asuransiList:vm.tempAsuransiList,
		        		ongkirList:vm.tempOngkirList,
		        		totalHarga:vm.totalAll,
		        		linkLampiranKontrak :vm.nomorKontrakList,
		        		vendor : $scope.salesOrder.vendor,
		        		slaDeliveryTime : vm.slaDeliveryTime,
		        		gvDoctype : vm.gvDoctype,
			    		gvHeadnote : vm.gvHeadnote,
			    		gvIntermsoft : vm.gvIntermsoft,
			    		gvAttachment : vm.gvAttachment,
			    		gvRequisitioner : vm.gvRequisitioner,
			    		gvTestRun : vm.gvTestRun,
			    		// KAI - 2021/01/08 - [21483]
			    		costCenter : {code : vm.costCenter},
			    		purGroup : vm.purGroup,
			    		storeLoc : vm.storeLoc,
			    		purchOrg : vm.purchOrg,
			    		acctasscat: vm.acctasscat.code 		
		        	}
		    		var bookingOrderItemDTOList = [];
		    		
		    			// KAI - 20201202 - [20595]
		    		
		    		angular.forEach(vm.shippingToList, function(shipping, indexShipping) {
		    			angular.forEach($scope.salesOrder.listCatalog, function(value, index) {
		        			var bookingOrderItemDTO = {
		        					catalogId : value.id,
		        	        		qty : vm.qty[indexShipping][index],
		        	        		gLAccount : vm.gLAccount[indexShipping][index],
		        	        		addressBookId: vm.shippingToList[indexShipping].id,
		        	        		ongkosKirim: vm.totalOngkirItem[indexShipping][index],
		        	        		hargaWithDiscount: value.harga-((vm.tempBulkPriceDiskon[index]/100)*value.harga),
		        	        		discount : vm.tempBulkPriceDiskon[index],
		        	        		slaDeliveryTime: vm.slaDeliveryTimeList[indexShipping]
		        	        		
		        	        	}
		        			
		        			bookingOrderItemDTOList.push(bookingOrderItemDTO);
		    			});
		    		});
		    			
		    		data.bookingOrderDTO.bookingOrderItemDTOList = bookingOrderItemDTOList;
		    		RequestService.doPOSTJSON('/procurement/purchaseRequestServices/create', data.bookingOrderDTO)
					.then(function success(data) {
						if(data.response.includes("NOT_FOUND")){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Tahapan Persetujuan belum dibuat, Hubungin Admin E-Purchasing!");
							$scope.loading = false;
						}else{
							ModalService.closeModalInformation();
							$scope.loading = false;
							$state.go('app.promise.procurement-booking-order-index');
							RequestService.informSaveSuccess();
						}
					}, function error(response) {				 
							ModalService.closeModalInformation();
							ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
		    	});
		    };
		    vm.asuransi=[];
		    vm.tambah=[];
		    
		    $scope.tambahAsuransi = function (asuransi,idx){
				vm.tambah[idx]=0;
				vm.tamp=0;
				if(vm.asuransi[idx]==true){
// console.log("HH: ",$scope.salesOrder.listCatalog[idx]);
					angular.forEach($scope.salesOrder.listCatalog,function(data,index){
						if($scope.salesOrder.listCatalog[idx].id==data.id){
							if(data.catalogFeeList!=null){
								vm.tamp=data.catalogFeeList[0].asuransi;
							}							
						}
					});
					$scope.salesOrder.grandTotalWithQty+=asuransi;
					vm.tambah[idx]=asuransi;
					vm.tempAsuransiList[idx]=vm.tempAsuransiList2[idx];
					vm.totalAsuransi+=(vm.tempAsuransiList[idx]/100)*(($scope.salesOrder.listCatalog[idx].qtyCartItem * $scope.salesOrder.listCatalog[idx].harga)-((vm.tempBulkPriceDiskon[idx]/100)*($scope.salesOrder.listCatalog[idx].qtyCartItem * $scope.salesOrder.listCatalog[idx].harga)));
				}else{
					$scope.salesOrder.grandTotalWithQty-=asuransi;
					vm.totalAsuransi-=asuransi;
					vm.tempAsuransiList[idx]=0;
				}
			}
		    
		    //// POP UP GL ACCOUNT /////		    
		    $scope.popUpGLAccount = function (index, indexShipTo) {
				$scope.modalInstance = $modal.open({
	                templateUrl: '/popUpGLAccount.html',
	                controller: ModalCtrl,
	                size: 'md',
	                resolve: {
	                	
	                    index: function () {
	                        return index;
	                      },
	                      indexShipTo: function () {
		                        return indexShipTo;
		                      }
	                  }
	            });
				$scope.modalInstance.result.then(function (code) { //ok
					vm.gLAccount[indexShipTo][index] = code;
	            }, function () {});
	        };
	        
	        var ModalCtrl = function ($scope, $modalInstance, index,indexShipTo, ngTableParams, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile) {
	        	$scope.submitted = false;
	        	
	        	$scope.index = null;
	        	$scope.index = index;
	        	
	        	$scope.indexShipTo = null;
	        	$scope.indexShipTo = indexShipTo;
	        	$scope.isDisable = false;
	        	$scope.dtInstance = {};
	            
	            function callback(json) {
	    		}
	            
	            $scope.reloadData = function () {
	    			
	    		    var resetPaging = false;
	    		    
	    		    if($scope.dtInstance != null){
	    		    	$scope.dtInstance.reloadData(callback, resetPaging);
	    		    }
	    		};  

	    		$scope.dtOptions = RequestService.requestServerPaggingNoToken(DTOptionsBuilder.newOptions(), '/procurement/master/gl-account/get-list-by-pagination');
	        	$scope.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	        		$compile(nRow)($scope);
	                var start = this.fnSettings()._iDisplayStart;
	                $("td:first", nRow).html(start + iDisplayIndex + 1);
	            });
	        	$scope.dtColumns = [
	        		DTColumnBuilder.newColumn(null).withTitle('No.').notSortable(),
	        		
	        		DTColumnBuilder.newColumn('code').withTitle('Code').renderWith(function(data){
	        			return data;
	        		}),
	        		DTColumnBuilder.newColumn('description').withTitle('Description').renderWith(function(data){
	        			return data;
	        		}),
	        		
	        		DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
	        		
	        			return '<div class="radio c-radio"><label style="color:green">'+
	        			'<input type="radio" name="gLAccount" ng-click="select('+data.code+')"><span class="fa fa-circle"></span>'+
	        			'</label></div>';
	        		})
	        	];
	        			
	        	DTInstances.getLast().then(function(instance) {
	        		$scope.dtInstance = instance;
	            });
	        	
	      	    
	      	    $scope.select = function (code) {
	      	    	$modalInstance.close(code);	
	      	    }
	      	        
	            $scope.ok = function () {
	            	$modalInstance.close('closed');
	            };
	            $scope.cancel = function () {
	                $modalInstance.dismiss('cancel');
	            };
	        };
	        ModalCtrl.$inject = ['$scope', '$modalInstance', 'index', 'indexShipTo', 'ngTableParams', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances','$compile'];


	   //// POP UP COST CENTER/////		    
		    $scope.popUpCostCenter = function () {
				$scope.modalInstance = $modal.open({
	                templateUrl: '/popUpCostCenter.html',
	                controller: ModalCostCenterCtrl,
	                size: 'md'
	            });
				$scope.modalInstance.result.then(function (code) { //ok
					vm.costCenter= code;
	            }, function () {});
	        };
	        
	        var ModalCostCenterCtrl = function ($scope, $modalInstance, ngTableParams, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile) {
	        	$scope.submitted = false;
	        	$scope.isDisable = false;
	        	$scope.dtInstance = {};
	            
	            function callback(json) {
	    		}
	            
	            $scope.reloadData = function () {
	    			
	    		    var resetPaging = false;
	    		    
	    		    if($scope.dtInstance != null){
	    		    	$scope.dtInstance.reloadData(callback, resetPaging);
	    		    }
	    		};  

	    		$scope.dtOptions = RequestService.requestServerPaggingNoToken(DTOptionsBuilder.newOptions(), '/procurement/master/cost-center/get-list-by-pagination');
	        	$scope.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	        		$compile(nRow)($scope);
	                var start = this.fnSettings()._iDisplayStart;
	                $("td:first", nRow).html(start + iDisplayIndex + 1);
	            });
	        	$scope.dtColumns = [
	        		DTColumnBuilder.newColumn(null).withTitle('No.').notSortable(),
	        		
	        		DTColumnBuilder.newColumn('code').withTitle('Code').renderWith(function(data){
	        			return data;
	        		}),
	        		DTColumnBuilder.newColumn('description').withTitle('Description').renderWith(function(data){
	        			return data;
	        		}),
	        		
	        		DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
	        		
	        			return '<div class="radio c-radio"><label style="color:green">'+
	        			'<input type="radio" name="costCenter" ng-click="selectCostCenter('+data.id+')"><span class="fa fa-circle"></span>'+
	        			'</label></div>';
	        		})
	        	];
	        			
	        	DTInstances.getLast().then(function(instance) {
	        		$scope.dtInstance = instance;
	            });
	        	
	      	    
	      	    $scope.selectCostCenter = function (id) {
	      	    	RequestService.doGET('/procurement/master/cost-center/get-by-id/' + id)
	    			.then(function success(data) {
	    				$modalInstance.close(data.code);	
	    			}, function error(response) {	
	    			});
	      	    	
	      	    }
   
	            $scope.ok = function () {
	            	$modalInstance.close('closed');
	                
	            };
	            $scope.cancel = function () {
	                $modalInstance.dismiss('cancel');
	            };
	        };
	        ModalCostCenterCtrl.$inject = ['$scope', '$modalInstance', 'ngTableParams', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances','$compile'];
       
	        
}

bookingOrderCtrl.$inject = ['$rootScope', '$scope', 'RequestService', '$state', '$stateParams', '$location', '$modal', 'ModalService', '$http', '$anchorScroll', 'ROLE_USER', '$resource', 'toaster'];

})();