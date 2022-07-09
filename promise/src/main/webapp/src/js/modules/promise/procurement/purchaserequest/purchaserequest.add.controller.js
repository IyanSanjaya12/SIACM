(function() {
	'use strict';
	angular.module('naut').controller('PurchaseRequestAddController',
			PurchaseRequestAddController);

	function PurchaseRequestAddController($scope, $http, $rootScope, $resource,
			$location, toaster, ngTableParams, $modal, $filter, RequestService, $timeout,
			FileUploader) {
		var form = this;
		$scope.loading = false;
		$scope.message = "";
		$scope.isEditable = true;
		$scope.hasUploaded = false;
		$scope.errormessagePRNumber = '';
		$scope.isTotalValid = true;

		//validasi jumlah nomor telepon
		$scope.count=function(shipTo){
	        	if(shipTo.telephone==undefined || typeof shipTo.telephone== 'undefined'||typeof shipTo.telephone=='null'|| shipTo.telephone==null){
	        		shipTo.telephone=" ";
	        	}
	        	//console.log('value '+ vm.po.billTo.telephone);
	        	$scope.telNumLength=(shipTo.telephone).toString().length;
	        	//console.log('length '+(vm.po.billTo.telephone).toString().length);
	        	if($scope.telNumLength>15){
	        		alert("Telephone Number must be less than 15 numbers");
	        	}
	    }
		 
		form.isJoin = 0;

		$rootScope.nomorDraft = '';
		$rootScope.jenisAnggaranId = 0;

		// Lemparan dari Cart List
		if ($rootScope.fromCart) {
			$scope.itemPRList = $rootScope.itemPRList;
			
		} else {
			$rootScope.itemPRList = [];
		}
		
		form.clear = function() {
			form.daterequired = null;
		};
		
		form.disabled = function(date, mode) {
			return false;
		};
		form.toggleMin = function() {
			form.minDate = form.minDate ? null : new Date();
		};
		
		form.toggleMin();
		form.dateOptions = {
			formatYear : 'yy',
			startingDay : 1
		};
		
		form.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy',
				'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd' ];
		form.format = form.formats[4];

		form.daterequiredOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.daterequiredOpened = true;
		};

		$http
				.get(
						$rootScope.backendAddress
								+ '/procurement/master/organisasi/get-list-by-parent-id/'
								+ $rootScope.userOrganisasi.id) // user
				.then(function(response) {
					$scope.departmentList = response.data;
				})

		/* Uploader */
		form.uploader = new FileUploader({
			url : $rootScope.uploadBackendAddress,
			headers : {
				'Authorization' : RequestService.getUserToken()
			}
		});

		form.uploader.filters.push({
			name : 'customFilter',
			fn : function(item, options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		form.uploader.onCompleteItem = function(fileItem, response, status,
				headers) {
			//console.log('realFileName=' + response.fileName);
			//console.log(response);

			fileItem.realFileName = response.fileName;
			fileItem.statusFile = true;
			$scope.hasUploaded = true;

		};
	
		/* FindCostCenterList */
		$scope.findCostcenterList = function(value) {

			// console.log(value.length == 0);
			if (value.length != 0) {
				var data = {search:value}
				RequestService.doPOST("/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraffAndOrganisasi",data)
				.then(function success(response) {
					if (data != undefined) {
						$scope.costCenterList = response.data;
						$scope.loading = false;
					}
				}, function error(response) {
					$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
				});

			} else {
				$http
						.get(
								$rootScope.backendAddress
										+ "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi")
						.then(function(response) {
							$scope.costCenterList = response.data;
							$scope.loading = false;
						});
				}
		}

		$scope.findCostcenterListItem = function(value) {
			if (value != undefined) {
				if (value.length != 0) {
					if ($rootScope.jenisAnggaranId != 0) {
						$http
								.get(
										$rootScope.backendAddress
												+ "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraffAndjenisAnggaran/"
												+ value + "/"
												+ $rootScope.jenisAnggaranId)
								.then(function(response) {
									$scope.costCenterListItem = response.data;
									$scope.loading = false;
								});

					} else {
						$http
								.get(
										$rootScope.backendAddress
												+ "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraff/"
												+ value)
								.then(function(response) {
									$scope.costCenterListItem = response.data;
									$scope.loading = false;
								});
					}
				} else if (value.length == 0 && $rootScope.jenisAnggaranId != 0) {
					$http
							.get(
									$rootScope.backendAddress
											+ "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi/"
											+ $rootScope.jenisAnggaranId)
							.then(function(response) {
								$scope.costCenterListItem = response.data;
								$scope.loading = false;
							});
				} else {
					$http
							.get(
									$rootScope.backendAddress
											+ "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi")
							.then(function(response) {
								$scope.costCenterListItem = response.data;
								$scope.loading = false;
							});
				}
			} else {
				$http
						.get(
								$rootScope.backendAddress
										+ "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi/"
										+ $rootScope.jenisAnggaranId).then(
								function(response) {
									$scope.costCenterListItem = response.data;
									$scope.loading = false;
								});
			}

		}

		$scope.prSelected = function(value) {
			$rootScope.nomorDraft = value;
			$rootScope.jenisAnggaranId = value.jenisAnggaran.id;

			angular.forEach($rootScope.itemPRList, function(val, index) {
				val.costcenteritem = value;
			});

			$scope.findCostcenterListItem(undefined);
		}

		$scope.prSelectedItem = function(value, index) {
			$rootScope.itemPRList[index].costcenteritem = value;
		}

		$scope.btnKembaliIndex = function() {
			$scope.go('/app/promise/procurement/purchaserequest');
		}

		$scope.btnDisable = false;
		// generate PR Number
		$scope.btnGenerateNumber = function() {
			
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/getprnumber')
			.success(function(data, status, headers, config) {
				form.prnumber = data.pr;
				$scope.btnDisable = true;
			})
		};

		// Item PR
		$rootScope.totalHPSItemPR = 0;
		$scope.btnTambahItemFree = function() {
			$scope.errormessageItem = '';
			var modalInstance = $modal.open({
				templateUrl : '/add_item.html',
				controller : modalInstanceAddItemController

			});
		}
		var modalInstanceAddItemController = function($scope, $http, $modalInstance, $resource, $rootScope, FileUploader) {
			$scope.form = {};
			$scope.kodeMaterial = {};
			$scope.form.itemname = '';
			$scope.form.quantity = 0;
			$scope.form.price = 0;
			$scope.form.unit = "";
			$scope.form.disabled =false;

			$scope.refreshkodeMaterialList = function(kodeMaterial) {
				$scope.loading = true;
				if (kodeMaterial.length == 0)
					kodeMaterial = 0;
				return $http
						.get(
								$rootScope.backendAddress
										+ '/procurement/master/item/get-list-by-kode/'
										+ kodeMaterial).then(
								function(response) {
									$scope.kodeMaterialList = response.data;
									$scope.loading = false;
								});
			};

			$scope.setKodeMaterialSelected = function() {
				if (typeof $scope.kodeMaterial.selected !== 'undefined') {
					$scope.form.itemname = $scope.kodeMaterial.selected.nama;
					$scope.form.unit = $scope.kodeMaterial.selected.satuanId.nama;
					$scope.form.disabled =true;
				}
			};
			// Satuan List
			$http
				.get(
						$rootScope.backendAddress
								+ '/procurement/master/satuan/get-list')
				.success(function(data) {
					$scope.satuanList = data;
			});
			
			// hitung total hps
			$scope.calculateTotalHPS = function() {
				if (typeof $scope.form.quantity !== 'undefined'
						&& typeof $scope.form.price !== 'undefined') {
					$scope.form.total = $scope.form.quantity
							* $scope.form.price;
				}
			};

			// fileupload
			var uploader = $scope.uploader = new FileUploader({
				url : $rootScope.uploadBackendAddress,
				method : 'POST'
			});
			
			// filter
			uploader.filters.push({
				name : 'customFilter',
				fn : function(item /* {File|FileLikeObject} */, options) {
					return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
				}
			});

			// simpan
			$scope.btnSimpanItem = function() {
				$scope.loading = true;
				var bolehSimpan = true;
				$scope.errormessageItemname = '';
				$scope.errormessageQuantity = '';
				$scope.errormessagePrice = '';
				$scope.errormessageUnit = '';
				$scope.message = '';
				// cek nama
				if (typeof $scope.kodeMaterial.selected == 'undefined') {
					$scope.kodeMaterial.selected = {
						nama : $scope.form.itemname
					}
				}
				if ($scope.form.itemname == '') {
					$scope.errormessageItemname = "Nama Item harus diisi!";
					bolehSimpan = false;
					return false;
				}

				// cek item exist
				angular.forEach($rootScope.itemPRList,function(value, index) {
					if (value.nama == $scope.form.itemname) {
						$scope.errormessageItemname = "Nama Item Sudah Dipilih!";
						bolehSimpan = false;
						return false;
					}
				})

				// quantity
				if ($scope.form.quantity <= 0) {
					$scope.errormessageQuantity = "Jumlah(Quantity) Item tidak boleh nol!";
					bolehSimpan = false;
					return false;
				}
				// satuan
				if ($scope.form.unit == "") {
					$scope.errormessageUnit = "Satuan Harus dipilih!";
					bolehSimpan = false;
					return false;
				}
				// price
				if ($scope.form.price <= 0) {
					$scope.errormessagePrice = "Harga Item tidak boleh nol!";
					bolehSimpan = false;
					return false;
				}

				/*
				 * console.log('$scope.kodeMaterial.selected = ' +
				 * $scope.kodeMaterial.selected);
				 * console.log('$scope.kodeMaterial.selected.kode = ' +
				 * $scope.kodeMaterial.selected.kode);
				 */

				if (bolehSimpan) {
					if (uploader.queue.length == 0) {
						var tot = 0;
						var objectItem = {
							material : $scope.kodeMaterial.selected,
							kode : $scope.kodeMaterial.selected.kode,
							nama : $scope.form.itemname,
							vendor : $scope.kodeMaterial.selected.vendor,
							quantity : $scope.form.quantity,
							price : $scope.form.price,
							unit : $scope.form.unit,
							matauang : 'IDR',
							matauangId : 1,
							vendorId : '0',
							specification : $scope.form.specification,
							pathfile : $scope.pathfile,
							costcenteritem : $rootScope.nomorDraft
						}
						$rootScope.itemPRList.push(objectItem);
						$rootScope.totalHPSItemPR = 0;
						angular.forEach($rootScope.itemPRList, function (value, index) {
							$rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
						});
						$scope.loading = false;
						$modalInstance.close('closed');
					} else {
						$scope.message = 'Upload File Item';
						uploader.uploadAll();
						uploader.onCompleteItem = function(fileItem, response,status, headers) {
							$scope.pathfile = response.fileName;
							var tot = 0;
							var objectItem = {
								material : $scope.kodeMaterial.selected,
								kode : $scope.kodeMaterial.selected.kode,
								nama : $scope.form.itemname,
								vendor : $scope.kodeMaterial.selected.vendor,
								quantity : $scope.form.quantity,
								price : $scope.form.price,
								unit : $scope.form.unit,
								matauang : 'IDR',
								matauangId : 1,
								vendorId : '0',
								specification : $scope.form.specification,
								pathfile : $scope.pathfile,
								costcenteritem : $rootScope.nomorDraft
							}
							$rootScope.itemPRList.push(objectItem);
							$rootScope.totalHPSItemPR = 0;
							angular.forEach($rootScope.itemPRList, function (value, index) {
								$rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
							});
							$scope.message = 'Upload File Item Berhasil';
							$scope.loading = false;
							$timeout (function() {		
								$modalInstance.close('closed');
							}, 3000);							
						};
					}

				} else {
					$scope.loading = false;
				}
			}

			$scope.ok = function() {
				$modalInstance.close('closed');
			};

			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			};
			
		}
		modalInstanceAddItemController.$inject = [ '$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'FileUploader' ];
		
		$rootScope.totalHPSItemPR = 0;
		angular.forEach($scope.itemPRList, function (value, index) {
			$rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
		});
		
		// on change quantity
		$scope.onChangeQuantity = function () {
			$rootScope.totalHPSItemPR = 0;
			$scope.isTotalValid = true;
			angular.forEach($rootScope.itemPRList, function(value, index) {
				$rootScope.totalHPSItemPR += (value.quantity * value.price);
				if(isNaN($rootScope.totalHPSItemPR)){
					$scope.isTotalValid = false;
				}
			});
		};

		// on onChangeCostCenter
		$scope.onChangeCostCenter = function (indeks, nomorDraft) {
			$rootScope.itemPRList[indeks].costcenteritem = nomorDraft

		};

		// Add Item from Katalog
		$scope.addItemKatalog = function () {
			$scope.errormessageItem = '';
			var modalInstance = $modal.open({
				templateUrl : 'app/views/promise/procurement/purchaserequest/purchaserequest.itemadd.html',
				controller : modalInstanceAddItemKatalogController,
				size : 'lg'
			});
		};

		// MODAL ADD CONTROLLER
		var modalInstanceAddItemKatalogController = function ($scope, $http, $modalInstance, $resource) {
			$scope.cancel = function () {
				$modalInstance.dismiss('cancel');
			}

			$scope.ok = function () {
				$modalInstance.close('closed');
			}

		}
		modalInstanceAddItemKatalogController.$inject = [ '$scope', '$http', '$modalInstance', '$resource', '$rootScope','FileUploader' ];

		// remove Item
		// Material Delete
		$scope.removeItem = function(obj) {
			$rootScope.removeItemSelected = obj;
			var modalInstance = $modal.open({
				templateUrl : '/remove_item.html',
				controller : modalInstanceRemoveController,
				size : 'md'
			});
			modalInstance.result.then(function() { // ok
				var index = $rootScope.itemPRList.indexOf(obj);
				$rootScope.itemPRList.splice(index, 1);
				$scope.onChangeQuantity();
			}, function () {});
		};
		var modalInstanceRemoveController = function($scope, $http,	$modalInstance, $resource) {
			$scope.materialName = $rootScope.removeItemSelected.nama;
			$scope.cancel = function() {
				$modalInstance.dismiss('cancel');
			}

			$scope.ok = function() {
				$modalInstance.close('closed');
			}
		}
		modalInstanceRemoveController.$inject = [ '$scope', '$http', '$modalInstance', '$resource', '$rootScope' ];

		// SHIPPING TO
		$scope.shippingList = [];
		$scope.btnAddShipping = function() {
			if ($rootScope.itemPRList.length == 0) {
				alert("Item harus diisi terlebih dahulu!");
			} else {
				$scope.statusRemove = false;
				var shipTo = {
					fullName : null,
					telephone : null,
					address : null
				};
				var ctr = $scope.shippingList.push(shipTo);

				// Function for delivery time date picker
				$scope.shippingList[ctr - 1].clear = function() {
					$scope.shippingList[ctr - 1].deliveryTime = null;
				};
				$scope.shippingList[ctr - 1].disabled = function(date, mode) {
					return false;
				};
				$scope.shippingList[ctr - 1].toggleMin = function() {
					$scope.shippingList[ctr - 1].minDate = $scope.shippingList[ctr - 1].minDate ? null
							: new Date();
				};
				$scope.shippingList[ctr - 1].toggleMin();
				$scope.shippingList[ctr - 1].dateOptions = {
					formatYear : 'yy',
					startingDay : 1
				};
				$scope.shippingList[ctr - 1].formats = [ 'dd-MMMM-yyyy',
						'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy',
						'yyyy-MM-dd' ];
				$scope.shippingList[ctr - 1].format = $scope.shippingList[ctr - 1].formats[4];

				$scope.shippingList[ctr - 1].deliveryTimeOpen = function($event) {
					$event.preventDefault();
					$event.stopPropagation();
					$scope.shippingList[ctr - 1].deliveryTimeOpened = true;
				};
				// end function

			}
		}

		// companySelected
		
        $scope.findCompany = function () {
        	$scope.companyList = [];
            $scope.loading = true;
            $http.get($rootScope.backendAddress+'/procurement/master/organisasi/get-organisasi-afco-by-token')
                .success(function(data){
                $scope.loading = false;
                $scope.companyList.push(data);
                
            })
            .error(function(err){
                $scope.loading = false;
                console.error("err : "+err);
            });
        };
        $scope.findCompany();

		// get company Address
		
		$scope.getAddressBookList = function () {
				$rootScope.companyAddresslist = [];			
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

		// getAtributteAddress
		$scope.setShippingAddress = function(index, companyAddressSelected) {
			if (companyAddressSelected.isNew != true) {
				$scope.shippingList[index].fullName = companyAddressSelected.fullName;
				$scope.shippingList[index].telephone = companyAddressSelected.telephone1;
				$scope.shippingList[index].address = companyAddressSelected.addressLabel
						+ ', '
						+ companyAddressSelected.streetAddress
						+ ', '
						+ companyAddressSelected.city
						+ ', '
						+ (companyAddressSelected.country != null ? companyAddressSelected.country : '')
			} else {
				$scope.shippingList[index].fullName = '';
				$scope.shippingList[index].telephone = '';
				$scope.shippingList[index].address = '';
				// console.log('>> new address');
			}
		};

		$scope.statusRemove = true;
		$scope.btnDelShipping = function() {
			$scope.shippingList.splice(-1);
		};

		// Approval
		form.approval = {};
		$scope.getApprovalList = function () {
			$scope.approvalList = [];				
			RequestService.doGET('/procurement/approval/get-approval-by-tahapan-and-token/CREATE_PR')
			.then(function success(data) {
				$scope.approvalList = data;
				
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
			});       
		}
		$scope.getApprovalList();

		$scope.getApprovalLevel = function() {
			$scope.loadingApproval = true;
			form.approval.levelList = [];
			$http.get($rootScope.backendAddress	+ '/procurement/approval/approvalLevel/' + form.approval.selected).then(function(response) {
				form.approval.levelList = response.data;
				$scope.loadingApproval = false;
			})
		};

		$scope.approvalNewList = [];
		$scope.getApprovalNewList = function(name) {
			if (name.length > 0) {
				$scope.loadingApproval = true;
				$http(
						{
							method : 'POST',
							url : $rootScope.backendAddress
									+ '/procurement/approval/role-user-list-organisasi',
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							},
							transformRequest : function(obj) {
								var str = [];
								for ( var p in obj)
									str.push(encodeURIComponent(p) + "="
											+ encodeURIComponent(obj[p]));
								return str.join("&");
							},
							data : {
								name : name
							}
						}).success(function(data, status, headers, config) {
					$scope.loadingApproval = false;
					$scope.approvalNewList = data;
				});
			}

		};
		
/*		$scope.approvalNewList = [];
		$scope.getApprovalNewList = function() {
			$scope.loadingApproval = true;
			$http.get($rootScope.backendAddress	+ '/procurement/approval/approvalServices/roleuserlistorganisasi').then(function(response) {
				$scope.loadingApproval = false;
				$scope.approvalNewList = response.data;
			})

		};*/
		
		$scope.validateForm = function() {

			var isValid = true;
			$scope.errormessagePRNumber = '';
        
	        if (typeof form.prnumber === 'undefined' || form.prnumber == '') {
	        	isValid = false;
    			$scope.errormessagePRNumber = 'PR Number harus diisi!';
    		}
			
			$scope.errormessageDaterequired = '';
			if (typeof form.daterequired === 'undefined') {
				isValid = false;
				$scope.errormessageDaterequired = 'Required Date harus diisi!';
			}
			$scope.errormessageDepartment = '';
			if (typeof form.department === 'undefined') {
				isValid = false;
				$scope.errormessageDepartment = 'Department harus dipilih!';
			}
			$scope.errormessageCost = '';
			if (typeof form.costcenter === 'undefined') {
				isValid = false;
				$scope.errormessageCost = 'Cost Center harus dipilih!';
			}
	        
			return isValid;
			
		}
		
		/*$scope.tabs = [];
		$scope.validateForm = function() {
			
			console.log("test : "+JSON.stringify($scope.tabs));
			$scope.errormessagePRNumber = '';
			$scope.errormessageDaterequired = '';
			$scope.errormessageDepartment = '';
			$scope.errormessageCost = '';
					
			if(typeof form.prnumber !== 'undefined' && form.prnumber !== '') {				
				var paramPost = {
					prnumber : form.prnumber
				};
				
				RequestService.doPOST('/procurement/purchaseRequestServices/getprnumbervalidation', paramPost)
			    .success(function (data, status, headers, config) {
					if (data.isnotexist == false) {
						$scope.errormessagePRNumber = 'PR Number Sudah Ada';
						return false;
					} else {
						if(typeof form.daterequired !== 'undefined'){
			
							if(typeof form.department !== 'undefined'){
								
								if(typeof form.costcenter !== 'undefined'){
									console.log("masuk sini");
											$scope.tabs = [{
												active: false
											}, {
												active: true
											}];
										return true												
								} else {
									$scope.errormessageCost = 'Cost Center harus dipilih!';
									return false;
								}

								return true												
							} else {
								$scope.errormessageDepartment = 'Department harus dipilih!';
								return false;
							}	
							
							return true;
								
						} else {
							$scope.errormessageDaterequired = 'Tanggal belum diisi';
							return false;								
						}
						
					}

				});
					
			} else {
				$scope.errormessagePRNumber = 'PR Number harus diisi!';
				return false;
			}
			
		}*/

		$scope.validateForm2 = function() {
			var isValid = true;
			$scope.errormessageItem = '';
			//console.log($rootScope.itemPRList);
			if ($rootScope.itemPRList.length == 0) {
				isValid = false;
				$scope.errormessageItem = 'Item pengadaan harus dipilih!';
			} else {
				for (var i = 0; i < $rootScope.itemPRList.length; i++) {
					if ($rootScope.itemPRList[i].quantity == 0) {
						isValid = false;
						$scope.errormessageItem = 'Item Qty tidak boleh 0!';
						break;
					}
				}
			}
			return isValid;
		}

		// Shipping Validation
		$scope.validateShipping = function(itemPR) {

			$scope.errorShipping = null;
			$scope.errormessageDateDeliveryTimerequired = null;
			$scope.errormessagePerusahaan = null;
			var isValid = true;
			/*
			 * $scope.errorAddress = ''; if (typeof form.prnumber ===
			 * 'undefined') { isValid = false; $scope.errormessagePRNumber = 'PR
			 * Number harus diisi!'; }
			 */

			for (var i = 0; i < $scope.shippingList.length; i++) {
				var a = $scope.shippingList[i];
				
				 if(angular.isUndefined(a.companySelected)){
					 isValid = false;
					$scope.errormessagePerusahaan = "Silahkan isi Perusahaan";
					/*return false;*/
					
				}else if (angular.isUndefined(a.deliveryTime)) {
					isValid = false
					$scope.errormessageDateDeliveryTimerequired = "Silahkan isi Delivery Time";
					/*return false;*/
				}
				 
				 angular.forEach(
							$rootScope.itemPRList,
							function(itemPR, indexItemPR) {
								var totalShippingItem = 0;
								angular.forEach($scope.shippingList,function(shipping) {
									angular.forEach(shipping.itemPR,function(shippingItem) {
										if (indexItemPR == shippingItem.index && shippingItem.shipSelected == true) {
											totalShippingItem += shippingItem.shipQuantity;
										}
									});
								});
								if (itemPR.quantity != totalShippingItem) {
									isValid = false
									$scope.errorShipping = "Jumlah Shipping tidak sama dengan Jumlah Quota Item PR!";
									/*return false;*/
								}
							});

				for (var x = 0; x < $scope.shippingList.length; x++) {
					if (i != x) {

						var b = $scope.shippingList[x];

						if ((angular.equals(a.companySelected,
								b.companySelected))
								&& (angular.equals(a.companyAddressSelected,
										b.companyAddressSelected))
								&& (angular.equals(a.deliveryTime,
										b.deliveryTime))) {
							isValid = false;
							$scope.errorShipping = "Shipping Address & Delivery Time tidak boleh ada yang sama untuk setiap shipping";
							/*return false;*/
						}
					}
				}
			}

			return isValid;
		}

		// count total shipping Item
		$scope.countTotalShipingByItem = function(itemIndex) {
			var totalShippingItem = 0;
			angular.forEach($scope.shippingList, function(shipping,
					shippingIndex) {
				angular.forEach(shipping.itemPR, function(shippingItem,
						shippingItemIndex) {
					if (shippingItem.index == itemIndex
							&& shippingItem.shipSelected == true) {
						totalShippingItem += shippingItem.shipQuantity;
					}
				});
			});
			return totalShippingItem;
		}

		$scope.validateFormApproval = function() {
			var isValid = true;
			$scope.errormessageApproval = '';
			if (typeof form.approval.selected == 'undefined') {
				isValid = false;
				$scope.errormessageApproval = 'Approval belum disi!';
			} else if (form.approval.selected == 0) {
				if (typeof form.approval.newSelected === 'undefined') {
					isValid = false;
					$scope.errormessageApproval = 'Approval belum disi!';
				}
			}
			return isValid;
		}

		$scope.btnSimpanPRStatus = true;
		$scope.btnSimpanPR = function() {
			$scope.btnSimpanPRStatus = false;
			if ($scope.validateForm() && $scope.validateForm2()	&& $scope.validateFormApproval() && $scope.validateShipping()) {
				
					if (form.prnumber) {
						
						$http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/getList', {
				            ignoreLoadingBar: true
				        }).success(function (data, status, headers, config) {
				        	$scope.purchaseRequestList = data;
				            $scope.loading = false;
				            
				            var isValid = true
							
				            angular.forEach($scope.purchaseRequestList, function(prList) {
			    				if (form.prnumber == prList.prnumber) {
			    					$scope.errormessagePRNumber = 'PR Number Sudah Ada';
			    					$scope.errorBeforeSave = 'Terjadi Kesalahan! - PR Number Sudah Ada';
			    					isValid = false;
			    				}
			                })

				            if (isValid == true) {
					            $scope.errormessagePRNumber = '';
		    					$scope.errorBeforeSave = '';
								
								RequestService.modalConfirmation('Anda yakin ingin menyimpan data Purchase Request?').then(function(result) {
									
									$scope.loading = true;
									$scope.message = "";
									form.totalcost = 0;
									for (var i = 0; i < $rootScope.itemPRList.length; i++) {
										form.totalcost += $rootScope.itemPRList[i].quantity * $rootScope.itemPRList[i].price;
									}
									
									$scope.fileuploadList = [];
									angular.forEach(form.uploader.queue,function(item) {
										var fileUpload = {
											fileRealName : item.realFileName,
											fileName : item.file.name,
											fileSize : item.file.size
										}
									$scope.fileuploadList.push(fileUpload);
									})

									var formHeaderPR = {
										prnumber : form.prnumber,
										department : form.department.nama,
										departmentId : form.department.id,
										totalcost : form.totalcost,
										daterequired : $filter('date')(form.daterequired,'yyyy-MM-dd'),
										fileuploadList : JSON.stringify($scope.fileuploadList),
										isJoin : form.isJoin
									};

									if (!angular.isUndefined(form.termandcondition)) {
										formHeaderPR.termandcondition = form.termandcondition;
									}

									if (!angular.isUndefined(form.description)) {
										formHeaderPR.description = form.description;
									}

									if (!angular.isUndefined(form.procurementprocess)) {
										formHeaderPR.procurementprocess = form.procurementprocess;
									}

									if (form.costcenter != null) {
										formHeaderPR.costcenter = form.costcenter.nomorDraft;
									}

									$scope.appId = '';

									if (form.approval.selected > 0) {
										formHeaderPR.approvalId = form.approval.selected;
										formHeaderPR.approvalIsNew = false;
										var flow = form.approval.levelList[0];

										if (flow.group != null) {
											formHeaderPR.nextapproval = flow.group.nama;
										} else if (flow.user != null) {
											formHeaderPR.nextapproval = flow.user.namaPengguna;
										} else if (flow.role != null) {
											formHeaderPR.nextapproval = flow.role.nama;
										}

									} else {
										formHeaderPR.approvalUser = form.approval.newSelected.user.id;
										formHeaderPR.apparovalOrganisasi = form.approval.newSelected.organisasi.id;
										formHeaderPR.nextapproval = form.approval.newSelected.organisasi.nama + ' - ' + form.approval.newSelected.user.namaPengguna;
										formHeaderPR.approvalIsNew = true;
									}

									var prosesNow = 0;

									$http({
										method : 'POST',
										url : $rootScope.backendAddress	+ '/procurement/purchaseRequestServices/insert',
										headers : {
											'Content-Type' : 'application/x-www-form-urlencoded'
										},
										transformRequest : function(obj) {
											var str = [];
											for ( var p in obj)
												str.push(encodeURIComponent(p) + "="+ encodeURIComponent(obj[p]));
												return str.join("&");
											},
											data : formHeaderPR
										})
										.success(function(data, status,	headers, config) {
											prosesNow++;
											$scope.message = ('Proses Penyimpanan Purchase Request');
											if (typeof data !== 'undefined') {
												var purchaserequestId = data.id;
												
												// Simpan PR ITem
												var indexItemPRSave = 0;
												
												angular.forEach($rootScope.itemPRList,function(valueItemPR,indexItemPr) {
													//console.log("value itemPR nih: "+JSON.stringify(valueItemPR));
													
													var ttl = valueItemPR.quantity* valueItemPR.price;
													
													var formItemPR = {
														purchaserequestId : purchaserequestId,
														itemname : valueItemPR.material.nama,
														quantity : valueItemPR.quantity,
														unit : valueItemPR.unit,
														price : valueItemPR.price,
														specification : angular.isUndefined(valueItemPR.specification) ? "": valueItemPR.specification,
														mataUangId : '1', // temporary HC
														costcenter : valueItemPR.costcenteritem.nomorDraft,
														vendorId : valueItemPR.vendorId,
														kode : angular.isUndefined(valueItemPR.kode) ? valueItemPR.material.kodeProduk : valueItemPR.kode,
														status : 'Pending',
														total : ttl
													};

													if (typeof valueItemPR.pathfile !== 'undefined') {
														formItemPR.pathfile = valueItemPR.pathfile;
													}

													formItemPR.itemId = valueItemPR.material.id;
													if (valueItemPR.material.item != undefined) {
														formItemPR.itemId = valueItemPR.material.item.id;
													}

													var targetURI = '';
													if (typeof valueItemPR.material.kode !== 'undefined'|| typeof valueItemPR.material.kodeProduk !== 'undefined') {
														targetURI = $rootScope.backendAddress+ '/procurement/purchaseRequestItemServices/insert';
														if (typeof valueItemPR.material.kodeItem !== 'undefined') {
															formItemPR.kode = valueItemPR.material.kodeItem;
														}
														if (typeof formItemPR.itemname === 'undefined') {
															formItemPR.itemname = valueItemPR.material.namaIND;
														}

														if (!angular.isUndefined(valueItemPR.vendor)) {
															formItemPR.vendorName = valueItemPR.vendor;
														} else if (typeof valueItemPR.material.vendor !== 'undefined') {
															if (typeof valueItemPR.material.vendor.nama !== 'undefined') {
																formItemPR.vendorName = valueItemPR.material.vendor.nama;
															}
														}
													} else {
														targetURI = $rootScope.backendAddress+ '/procurement/purchaseRequestItemServices/insertPurchaseItemFreeText';
													}

													$http({
														method : 'POST',
														url : targetURI,
														headers : {
															'Content-Type' : 'application/x-www-form-urlencoded'
														},
														transformRequest : function(obj) {
															var str = [];
															for ( var p in obj)
															str.push(encodeURIComponent(p)+ "="+ encodeURIComponent(obj[p]));
															return str.join("&");
														},
														data : formItemPR
													})
													
													.success(function(dataPrItem,status,headers,config) {
														$scope.message = ('Proses Penyimpanan Item Purchase Request');
														valueItemPR.prItemId = dataPrItem.id;
														indexItemPRSave++;
																																														
														if ($rootScope.itemPRList.length == indexItemPRSave) {

															if ($scope.shippingList.length == 0) {
																$scope.message = ('Simpan Purchase Request Berhasil');
																$scope.loading = false;
																$scope.btnSimpanPRStatus = false;
															} else {
																var indexShippingSave = 0;
																angular.forEach($scope.shippingList,function(valShipping,indexShipping) {
																	var addressBookId = 0;
				
																	if (typeof valShipping.companyAddressSelected !== 'undefined') {
				
																		$scope.message = ('Proses penyimpanan Shipping..');

																		if (valShipping.companyAddressSelected.isNew == true) {

																			var newAddressData = {
																				organisasi : valShipping.companySelected,
																				addressLabel : valShipping.companySelected.companyName,
																				fullName : valShipping.fullName,
																				streetAddress : valShipping.address,
																				telephone : valShipping.telephone,
																				defaultBillingAddress : 1,
																				defaultShippingAddress : 1,
																				city : '',
																				country : '',
																				status : 1
																			}

																			RequestService.doPOSTJSON('/procurement/master/AddressBookServices/insert/',newAddressData)
																			.then(function(data) {
																				addressBookId = data.id;
																				indexShippingSave++;
																				$scope
																				.saveShipping(
																				valShipping,
																				indexShippingSave,
																				addressBookId,
																				indexShipping);
																			})

																							
																		} else {
																			addressBookId = valShipping.companyAddressSelected.id;
																			indexShippingSave++;
																			$scope
																			.saveShipping(
																			valShipping,
																			indexShippingSave,
																			addressBookId,
																			indexShipping);
																		}
																	}
				
																});
															}
				
														}
													})
													.error(function(data,status,headers,config) {
														console
														.log("Error ");
														$scope.loading = false;
													});
												});

											}
										}).error(
										function(data, status,
												headers, config) {
											$scope.loading = true;
										});
								}, function(close){
                                    $scope.btnSimpanPRStatus = true;
                                });
                                
								
							}
				            		            
			                return isValid;
				            
				        })			
					
					}
		
			} 
			
			else {
				RequestService.modalInformation(
						'Isian form belum lengkap silahkan periksa kembali!',
						'danger').then(function(data) {
				});
                $scope.btnSimpanPRStatus = true;
			}
		};

		$scope.saveShipping = function(valShipping, indexShippingSave,
				addressBookId, indexShipping) {
			var indexItemShippingSave = 0;
			angular
					.forEach(
							valShipping.itemPR,
							function(valItemShipping, indexItemShipping) {
								if (valItemShipping.shipSelected) { // check
									// item
									// is
									// selected
									var prItemId = 0;
									if (typeof $rootScope.itemPRList[valItemShipping.index].prItemId !== 'undefined') {
										prItemId = $rootScope.itemPRList[valItemShipping.index].prItemId;
									}

									// console.log (valShipping.deliveryTime );

									// console.log
									// ($filter('date')(valShipping.deliveryTime,
									// 'yyyy-MM-dd') );

									var shippingObj = {
										shippingGroup : (indexShipping + 1),
										addressBookId : addressBookId,
										prItemId : prItemId,
										quantity : valItemShipping.shipQuantity,
										prNumber : form.prnumber,
										fullName : valShipping.fullName,
										addressLabel : valShipping.companySelected.companyName,
										streetAddress : valShipping.address,
										telephone1 : valShipping.telephone,
										deliveryTime : $filter('date')(
												valShipping.deliveryTime,
												'yyyy-MM-dd')
									};
									// //console.log(">>>
									// shippingObj : " +
									// JSON.stringify(shippingObj));
									$http(
											{
												method : 'POST',
												url : $rootScope.backendAddress
														+ '/procurement/ShippingToPRServices/insert',
												headers : {
													'Content-Type' : 'application/x-www-form-urlencoded'
												},
												transformRequest : function(obj) {
													var str = [];
													for ( var p in obj)
														str
																.push(encodeURIComponent(p)
																		+ "="
																		+ encodeURIComponent(obj[p]));
													return str.join("&");
												},
												data : shippingObj
											})
											.success(
													function(data, status,
															headers, config) {
														$scope.message = ('Proses Penyimpanan Shipping Purchase Request ');
														indexItemShippingSave++;
														if (indexShippingSave == $scope.shippingList.length) {
															$scope.message = ('Simpan Purchase Request Berhasil');
															$scope.loading = false;
															$scope.btnSimpanPRStatus = false;
														}
													});

								}
							});

		}

		$scope.btnEdit = function(prId) {
			$rootScope.selectedPRId = prId;
			$scope.go('/app/promise/procurement/purchaserequest/edit');
		}

		$scope.go = function(path) {
			$location.path(path);
		};
	}
	PurchaseRequestAddController.$inject = [ '$scope', '$http', '$rootScope',
			'$resource', '$location', 'toaster', 'ngTableParams', '$modal',
			'$filter', 'RequestService', '$timeout', 'FileUploader' ];

})();