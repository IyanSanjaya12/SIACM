/**
 * ========================================================= Module:
 * PurchaseOrderAddController.js Author: Mamat, <n>
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('PurchaseOrderEditController',
			PurchaseOrderEditController);

	function PurchaseOrderEditController(RequestService, $scope, $http,
			$rootScope, $resource, $location, $filter, toaster, ModalService) {
		var form = this;
		form.po = {};
		form.purchaseOrderForm = {};
		form.purchaseOrderDTO = {};
		form.purchaseOrder = $rootScope.purchaseOrder;
		form.vendorList = [];
		form.billingAddressList = [];
		form.shippingToList = [];
		form.purchaseOrder.vendor = {};
		form.purchaseRequestItemList = [];
		form.vendor = {};
		form.approvalLevelList = [];
		form.vendor = {};
		form.purchaseOrderTerm = {};
		form.purchaseRequestList = [];
		form.isProcessing = false;
		form.btnKembaliIndex = true;
		form.statusSimpan = false;
		form.purchaseOrderTerm.poTermType = 2;
		form.doGenerateNumber = false;
		$scope.shipToDisable = true;
		if (typeof $rootScope.radioShipToThisAddress != 'undefined'
				|| $rootScope.radioShipToThisAddress != undefined) {
			form.shipToThisAddress = $rootScope.radioShipToThisAddress;
		}

		if ($rootScope.purchaseOrder.poNumber == ''
				|| typeof $rootScope.purchaseOrder.poNumber == '') {
			// form.shipToThisAddress = true;
			$scope.shipToDisable = false;
		} else {
			$scope.shipToDisable = true;
		}

		// console.log('isi rootscope PO =' +
		// JSON.stringify($rootScope.purchaseOrder));
		// form.shipToThisAddress = false;

		$scope.btnDisable = false;

		
		 form.getAddEdit = function (){
	        	RequestService.doPOSTJSON('/procurement/purchaseorder/PurchaseOrderServices/edit', form.purchaseOrder.id)
					.then(function success(data) {
						
						
						form.purchaseOrderAddEditDTO = data;
						$scope.billingAddressList =  form.purchaseOrderAddEditDTO.addressBookDTOList;
						$scope.termList = form.purchaseOrderAddEditDTO.termAndConditionList;
						form.approvalList = form.purchaseOrderAddEditDTO.approvalList;
						
						form.purchaseOrder = form.purchaseOrderAddEditDTO.purchaseOrder;
						
						form.purchaseOrder.approvalId = form.purchaseOrderAddEditDTO.approvalId;
						form.roleUserNewSelected = form.purchaseOrderAddEditDTO.roleUserNewSelected;
						form.approvalLevelList = form.purchaseOrderAddEditDTO.approvalLevelList;
						

						$scope.inputDisable = true;
						if (form.purchaseOrder.poNumber == "") {
							$scope.inputDisable = false;
						} else {
							$scope.btnDisable = true;
						}

						if (form.purchaseOrder.addressBook == null) {
							form.purchaseOrder.addressBook = {};
							form.purchaseOrder.addressBook.organisasi = form.purchaseOrder.purchaseRequest.organisasi;
						}

						form.purchaseRequestList
								.push(form.purchaseOrder.purchaseRequest);
						form.purchaseRequestItemList = form.purchaseOrderAddEditDTO.purchaseRequestItemList;

						if (form.purchaseOrder.mataUang != undefined
								|| form.purchaseOrder.mataUang != null) {
							form.termValue = form.purchaseOrder.subTotal
									* form.purchaseOrder.mataUang.kurs;
						}

						form.purchaseOrderTerm = form.purchaseOrderAddEditDTO.purchaseOrderTerm;

						form.vendor = form.purchaseOrderAddEditDTO.vendor;

						form.vendorList.push(form.vendor);

						form.purchaseOrderAddEditDTO.shippingToDtlList
								.forEach(function(shippingToDtl,
										indexShipping, array) {
									var shippingTo = shippingToDtl.shippingTo;
									shippingTo.purchaseOrderItemList = [];

									var purchaseOrderItemListNew = [];
									var purchaseOrderItemList = shippingToDtl.purchaseOrderItemList;

									var purchaseRequestItemList = shippingToDtl.purchaseRequestItemList;

									purchaseRequestItemList
											.forEach(function(
													purchaseRequestItem,
													indexPOI, array) {

												var purchaseOrderItem = {};
												var check = false;

												purchaseOrderItemList
														.forEach(function(
																purchaseOrderItemParam,
																index,
																array) {
															if (purchaseOrderItemParam.purchaseRequestItem.id == purchaseRequestItem.id) {
																purchaseOrderItemParam.selected = '1';
																purchaseOrderItem.shipquantityReadOnly = false;
																purchaseOrderItem = purchaseOrderItemParam;
																check = true
															}
														});

												if (check == false) {
													purchaseOrderItem.item = {};
													purchaseOrderItem.item = purchaseRequestItem.item;

													purchaseOrderItem.unitPrice = purchaseRequestItem.price;

													purchaseOrderItem.selected = '';
													purchaseOrderItem.shipquantityReadOnly = true;

													purchaseOrderItem.purchaseRequestItem = {};
													purchaseOrderItem.purchaseRequestItem = purchaseRequestItem;
													purchaseOrderItem.totalUnitPrices = 0;
													purchaseOrderItem.quantitySend = 0;
												}
												purchaseOrderItemListNew
														.push(purchaseOrderItem);
											});

									shippingTo.purchaseOrderItemList = purchaseOrderItemListNew;

									form.shippingToList.push(shippingTo);
								});
						
				}, function error(response) {
					$scope.loading = false;
		        	RequestService.informError("Terjadi Kesalahan Pada System");
				});
	        }
		 form.getAddEdit();

		// get company Address
		/* $scope.getCompanyAddressList = function () {
			 form.billingAddressList = [];
	        	RequestService.doGET('/procurement/master/AddressBookServices/getAddressBookListByToken')
				.then(function success(data) {
					form.billingAddressList =  data;			
				}, function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
			
			}); 
	        }
	        $scope.getCompanyAddressList();*/

		/*RequestService
				.doGET(
						'/procurement/master/TermAndConditionServices/getTermAndConditionList/')
				.then(function(responseTermin) {
					form.termList = responseTermin;
				});*/

		/*RequestService
				.doGET(
						'/procurement/purchaseorder/PurchaseOrderServices/getPurchaseOrderDetailById/'
								+ form.purchaseOrder.id)
				.then(
						function successCallback(data) {
							form.purchaseOrderForm = data;
							form.purchaseOrder = form.purchaseOrderForm.purchaseOrder;

							$scope.inputDisable = true;
							if (form.purchaseOrder.poNumber == "") {
								$scope.inputDisable = false;
							} else {
								$scope.btnDisable = true;
							}

							if (form.purchaseOrder.addressBook == null) {
								form.purchaseOrder.addressBook = {};
								form.purchaseOrder.addressBook.organisasi = form.purchaseOrder.purchaseRequest.organisasi;
							}

							form.purchaseRequestList
									.push(form.purchaseOrder.purchaseRequest);
							form.purchaseRequestItemList = form.purchaseOrderForm.purchaseRequestItemList;

							if (form.purchaseOrder.mataUang != undefined
									|| form.purchaseOrder.mataUang != null) {
								form.termValue = form.purchaseOrder.subTotal
										* form.purchaseOrder.mataUang.kurs;
							}

							form.purchaseOrderTerm = form.purchaseOrderForm.purchaseOrderTerm;

							form.vendor = form.purchaseOrderForm.vendor;

							form.vendorList.push(form.vendor);

							form.purchaseOrderForm.shippingToDtlList
									.forEach(function(shippingToDtl,
											indexShipping, array) {
										var shippingTo = shippingToDtl.shippingTo;
										shippingTo.purchaseOrderItemList = [];

										var purchaseOrderItemListNew = [];
										var purchaseOrderItemList = shippingToDtl.purchaseOrderItemList;

										var purchaseRequestItemList = shippingToDtl.purchaseRequestItemList;

										purchaseRequestItemList
												.forEach(function(
														purchaseRequestItem,
														indexPOI, array) {

													var purchaseOrderItem = {};
													var check = false;

													purchaseOrderItemList
															.forEach(function(
																	purchaseOrderItemParam,
																	index,
																	array) {
																if (purchaseOrderItemParam.purchaseRequestItem.id == purchaseRequestItem.id) {
																	purchaseOrderItemParam.selected = '1';
																	purchaseOrderItem.shipquantityReadOnly = false;
																	purchaseOrderItem = purchaseOrderItemParam;
																	check = true
																}
															});

													if (check == false) {
														purchaseOrderItem.item = {};
														purchaseOrderItem.item = purchaseRequestItem.item;

														purchaseOrderItem.unitPrice = purchaseRequestItem.price;

														purchaseOrderItem.selected = '';
														purchaseOrderItem.shipquantityReadOnly = true;

														purchaseOrderItem.purchaseRequestItem = {};
														purchaseOrderItem.purchaseRequestItem = purchaseRequestItem;
														purchaseOrderItem.totalUnitPrices = 0;
														purchaseOrderItem.quantitySend = 0;
													}
													purchaseOrderItemListNew
															.push(purchaseOrderItem);
												});

										shippingTo.purchaseOrderItemList = purchaseOrderItemListNew;

										form.shippingToList.push(shippingTo);
									});


						},
						function errorCallback(response) {
							ModalService
									.showModalInformation('Terjadi kesalahan pada system!');
						});*/

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

		form.poDateOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.poDateOpened = true;
		};

		form.poDateOpenDynamic = function(indexInput, $event) {
			$event.preventDefault();
			$event.stopPropagation();

			form.shippingToList.forEach(function(shipTo, index, array) {
				if (index == indexInput) {
					shipTo.poDateOpened = true;
				} else {
					shipTo.poDateOpened = false;
				}
			});
		}

		form.btnKembaliIndex = function() {
			$location.path('/app/promise/procurement/purchaseorder');
		}

		form.generateNumber = function() {
			var date = new Date();
			form.purchaseOrder.poNumber = "PO/" + date.getFullYear() + '-'
					+ ('0' + (date.getMonth() + 1)).slice(-2) + '-'
					+ ('0' + date.getDate()).slice(-2) + "/AI/"
					+ (1000 + Math.ceil(Math.random() * 9000));
		};

		// generate PR Number
		$scope.btnGenerateNumber = function() {
			$http
					.get(
							$rootScope.backendAddress
									+ '/procurement/purchaseorder/PurchaseOrderServices/getponumber')
					.success(function(data, status, headers, config) {
						form.purchaseOrder.poNumber = data.po;
						$scope.btnDisable = true;
					})
		};

		form.findPRNumber = function(prNumber) {
			if (typeof prNumber !== 'undefined') {
				if (prNumber.length > 0) {
					RequestService
							.doPOST(
									'/procurement/purchaseRequestServices/getVerifiedPurchaseRequestByNumberAndToken',
									{
										keyword : prNumber
									}).then(function successCallback(response) {
								form.prNumberList = response.data;
							});
				} else {
					form.prNumberList = [];
				}
			}
		}

		// lookup Approval List
		$scope.approvalNewList = [];
		$scope.getApprovalNewList = function(name) {
			if (name.length > 0) {
				$scope.loadingApproval = true;
				$http(
						{
							method : 'POST',
							url : $rootScope.backendAddress
									+ '/procurement/approval/roleuserlist',
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

		/*$scope.getApprovalList = function() {
			form.approvalList = [];
			RequestService
					.doGET(
							'/procurement/approval/approvalServices/getApprovalByTahapanAndToken/CREATE_PO')
					.then(
							function success(data) {
								form.approvalList = data;
								$http
										.get(
												$rootScope.backendAddress
														+ '/procurement/approvalProcessServices/findByTahapanAndId/CREATE_PO/'
														+ form.purchaseOrder.id)
										.success(
												function(data) {
													if (data != undefined && data.id != undefined) {
														// jika tidak memakai
														// approval template
														if (typeof data.approval === 'undefined' || data.approval == null) {
															form.purchaseOrder.approvalId = 0;
															// get approval process
															$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/'+ data.id)
																	.success(function(data) {
																				// get user approval
																				$http.get($rootScope.backendAddress+ '/procurement/approvalProcessServices/findRoleUser/'+ data[0].user.id)
																						.success(function(data) {
																							form.po.approval.newSelected = data[0];
																								});
																			})
																	.error(
																			function(
																					data) {
																				// console.error("Error
																				// get
																				// Process
																				// Approval
																				// Type");
																			})

														} else {
															form.purchaseOrder.approvalId = data.approval.id;
															$http
																	.get(
																			$rootScope.backendAddress
																					+ '/procurement/approval/approvalServices/approvalLevel/'
																					+ form.purchaseOrder.approvalId)
																	.then(
																			function(
																					responseApproval) {
																				form.approvalLevelList = responseApproval.data;
																				$scope.loadingApproval = false;
																			});
														}
													}
												});

							},
							function error(response) {
								RequestService
										.informError("Terjadi Kesalahan Pada System");

							});
		}

		$scope.getApprovalList(); */

		form.getApprovalLevel = function() {
			if (form.purchaseOrder.approvalId > 0) {
				form.approvalLevelList = [];
				RequestService.doGET(
						'/procurement/approval/approvalLevel/'
								+ form.purchaseOrder.approvalId).then(
						function(response) {
							form.approvalLevelList = response;
						});
			}
		};

	

		form.selectBillingAdress = function() {
			if (form.purchaseOrder.addressBook.id == 0) {
				form.purchaseOrder.fullName = '';
				form.purchaseOrder.streetAddress = '';
				form.purchaseOrder.telephone1 = '';
			} else {
				form.billingAddressList
						.forEach(function(billingAddress, index, array) {
							if (billingAddress.id == form.purchaseOrder.addressBook.id) {
								form.purchaseOrder.fullName = billingAddress.fullName;
								form.purchaseOrder.streetAddress = billingAddress.streetAddress;
								form.purchaseOrder.telephone1 = billingAddress.telephone;
							}
						});
			}
		}

		form.selectBillingAdressShipping = function(indexForm) {
			form.shippingToList
					.forEach(function(shippingTo, index, array) {
						if (indexForm == index) {
							if (shippingTo.addressBook.id == 0) {
								shippingTo.fullName = '';
								shippingTo.streetAddress = '';
								shippingTo.telephone1 = '';
							} else {
								form.billingAddressList
										.forEach(function(billingAddress,
												index, array) {
											if (billingAddress.id == shippingTo.addressBook.id) {
												shippingTo.fullName = billingAddress.fullName;
												shippingTo.streetAddress = billingAddress.streetAddress;
												shippingTo.telephone1 = billingAddress.telephone;
											}
										});
							}
						}
					});
		}

		form.btnDelShipping = function(id) {
			if (id == null) {
				if (form.shippingToList.length > 0) {
					form.shippingToList.splice(form.shippingToList.length - 1,
							1);
				}
			} else {
				form.shippingToList.splice(id, 1);
			}
		}

		form.btnAddShipping = function() {
			var shippingTo = {};
			shippingTo.purchaseOrderItemList = [];

			if (form.shipToThisAddress == true) {
				shippingTo.afco = form.purchaseOrder.addressBook.afco;
				shippingTo.addressBook = {};
				shippingTo.addressBook.id = form.purchaseOrder.addressBook.id;
				shippingTo.fullName = form.purchaseOrder.fullName;
				shippingTo.streetAddress = form.purchaseOrder.telephone1;
				shippingTo.telephone1 = form.purchaseOrder.streetAddress
			}

			form.purchaseRequestItemList.forEach(function(purchaseRequestItem,
					indexPOI, array) {
				var purchaseOrderItem = {};
				purchaseOrderItem.item = {};
				purchaseOrderItem.item = purchaseRequestItem.item;
				purchaseOrderItem.unitPrice = purchaseRequestItem.price;
				purchaseOrderItem.selected = '';
				purchaseOrderItem.shipquantityReadOnly = true;
				purchaseOrderItem.purchaseRequestItem = {};
				purchaseOrderItem.purchaseRequestItem = purchaseRequestItem;
				purchaseOrderItem.totalUnitPrices = 0;
				purchaseOrderItem.quantitySend = 0;
				shippingTo.purchaseOrderItemList.push(purchaseOrderItem);
			});

			form.shippingToList.push(shippingTo);
		}

		// cek radio button ship to this address
		form.getShippingDefalut = function() {

			// var shippingTo.purchaseOrderItemList = [];
			if (form.shipToThisAddress == true) {
				form.shippingToList[0].poNumber = form.purchaseOrder.poNumber;
				form.shippingToList[0].fullName = form.purchaseOrder.fullName;
				form.shippingToList[0].telephone1 = form.purchaseOrder.telephone1;
				form.shippingToList[0].addressBook = {};
				form.shippingToList[0].addressBook.id = form.purchaseOrder.addressBook.id
				form.shippingToList[0].streetAddress = form.purchaseOrder.streetAddress;

			}

			// console.log('isiShipToList
			// ='+JSON.stringify(form.shippingToList));
		}

		form.doInput = function(indexShipping, indexPOI) {

			form.shippingToList
					.forEach(function(shipTo, index, array) {
						if (index == indexShipping) {
							shipTo.purchaseOrderItemList
									.forEach(function(purchaseOrderItem,
											indexDua, array) {
										if (indexDua == indexPOI) {
											if (purchaseOrderItem.shipquantityReadOnly == null) {
												purchaseOrderItem.shipquantityReadOnly = true;
												purchaseOrderItem.selected = "";
												purchaseOrderItem.quantitySend = 0;
											} else {
												if (purchaseOrderItem.shipquantityReadOnly == true) {
													purchaseOrderItem.shipquantityReadOnly = false;
													purchaseOrderItem.selected = "1";
													purchaseOrderItem.quantitySend = 0;
												} else {
													purchaseOrderItem.shipquantityReadOnly = true;
													purchaseOrderItem.selected = "";
													purchaseOrderItem.quantitySend = 0;
												}
											}
											purchaseOrderItem.totalUnitPrices = 0;
										}
									});
						}
					});
		};

		form.getTotalUnitPrice = function(indexShipping, indexPOI) {

			/** validasi jumlah item * */
			var maxQty = 0;
			var sisaQty = 0;
			var actualQty = 0;
			var prId = 0;
			var countQty = 0;
			var itemName = '';

			form.shippingToList
					.forEach(function(shipTo, index, array) {
						if (index == indexShipping) {
							shipTo.purchaseOrderItemList
									.forEach(function(purchaseOrderItem,
											indexDua, array) {
										if (indexDua == indexPOI) {
											maxQty = purchaseOrderItem.purchaseRequestItem.quantity;
											prId = purchaseOrderItem.purchaseRequestItem.id;
											itemName = purchaseOrderItem.purchaseRequestItem.item.nama;
											actualQty += Number(purchaseOrderItem.quantitySend);
										}
									});
						}
					});

			form.shippingToList.forEach(function(shipTo, index, array) {
				shipTo.purchaseOrderItemList.forEach(function(
						purchaseOrderItem, indexDua, array) {
					if (prId == purchaseOrderItem.purchaseRequestItem.id) {
						countQty += Number(purchaseOrderItem.quantitySend);
					}
				});
			});

			if (Number(countQty) > Number(maxQty)) {

				var check = true;
				while (check) {

					actualQty = actualQty.toString();
					actualQty = actualQty.substring(0, actualQty.length - 1);

					form.shippingToList
							.forEach(function(shipTo, index, array) {
								if (index == indexShipping) {
									shipTo.purchaseOrderItemList
											.forEach(function(
													purchaseOrderItem,
													indexDua, array) {
												if (indexDua == indexPOI) {
													purchaseOrderItem.quantitySend = Number(actualQty);
													purchaseOrderItem.totalUnitPrices = actualQty
															* purchaseOrderItem.unitPrice;
												}
											});
								}
							});

					var countCheck = 0;
					form.shippingToList
							.forEach(function(shipTo, index, array) {
								shipTo.purchaseOrderItemList
										.forEach(function(purchaseOrderItem,
												indexDua, array) {
											if (prId == purchaseOrderItem.purchaseRequestItem.id) {
												countCheck += Number(purchaseOrderItem.quantitySend);
											}
										});
							});

					if (Number(countCheck) <= Number(maxQty)) {
						check = false;
						break;
					}
				}

				toaster.pop('error', 'Purchase Order', 'Maksimal Quantity '
						+ itemName + ' ' + maxQty + ' unit!');
			}

			var subTotal = 0;
			form.shippingToList.forEach(function(shipTo, index, array) {
				if (index == indexShipping) {
					shipTo.purchaseOrderItemList.forEach(function(
							purchaseOrderItem, indexDua, array) {
						if (indexDua == indexPOI) {
							purchaseOrderItem.quantitySend = Number(actualQty);
							purchaseOrderItem.totalUnitPrices = actualQty
									* purchaseOrderItem.unitPrice;
						}
					});
				}
			});

			/** jumlah harga * */
			form.shippingToList.forEach(function(shipTo, index, array) {
				shipTo.purchaseOrderItemList.forEach(function(
						purchaseOrderItem, indexDua, array) {
					subTotal += purchaseOrderItem.totalUnitPrices;
				});
			});

			form.purchaseOrder.subTotal = subTotal;
		}
		
		form.simpan= function(){
			if(form.validateForm()){
				form.saveData();
			}
		}

		form.saveData = function() {
			form.purchaseOrderDtl = {};
			form.purchaseOrderDtl.purchaseOrder = form.purchaseOrder;
			form.purchaseOrderDtl.shippingToDtlList = [];

			form.shippingToList.forEach(function(shippingTo, index, array) {
				var shippingToDtl = {};
				shippingToDtl.shippingTo = shippingTo;
				shippingToDtl.purchaseOrderItemList = [];

				shippingTo.purchaseOrderItemList.forEach(function(
						purchaseOrderItem, index, array) {
					var purchaseOrderItemParam = purchaseOrderItem;
					delete purchaseOrderItemParam["selected"];
					delete purchaseOrderItemParam["shipquantityReadOnly"];

					shippingToDtl.purchaseOrderItemList
							.push(purchaseOrderItemParam);
				});

				delete shippingToDtl.shippingTo["purchaseOrderItemList"];

				form.purchaseOrderDtl.shippingToDtlList.push(shippingToDtl);
			});

			form.purchaseOrderDtl.purchaseOrderTerm = form.purchaseOrderTerm;

			// Aproval
			if (form.purchaseOrder.approvalId > 0) {
				form.purchaseOrderDtl.approvalId = form.purchaseOrder.approvalId;
			}

			if (form.purchaseOrder.approvalId == 0) {
				form.purchaseOrderDtl.newApproval = {
					id : form.po.approval.newSelected.id,
					user : form.po.approval.newSelected.user,
					organisasi : form.po.approval.newSelected.organisasi,
					role : form.po.approval.newSelected.role
				};
			}

			ModalService
					.showModal()
					.then(
							function(result) {
								ModalService.showModalInformationBlock();
								// console.log("post data :
								// "+JSON.stringify(form.purchaseOrderDtl));

								RequestService
										.doPOSTJSON(
												'/procurement/purchaseorder/PurchaseOrderServices/updatePurchaseOrderDtl',
												form.purchaseOrderDtl)
										.then(
												function successCallback(
														response) {
													ModalService
															.closeModalInformation();
													ModalService
															.showModalInformation('Purchase Order Berhasil Disimpan!');
													form.btnKembaliIndex();
												},
												function errorCallback(response) {
													ModalService
															.closeModalInformation();
													ModalService
															.showModalInformation('Terjadi kesalahan pada system!');
													form.btnKembaliIndex();
												});

							});

		}

		form.validateForm = function() {
			/** validasi * */
			var valid = true;
			if (form.purchaseOrder.poNumber == null ||form.purchaseOrder.poNumber == "") {
				valid = false;
				toaster
						.pop(
								'error',
								'Silahkan Isi PO Number!');
			}
			
			if (form.purchaseOrder.approvalId == null) {
				valid = false;
				toaster
						.pop(
								'error',
								'Silahkan pilih approval tipe! Edit PO akan merubah Approval yang sedang berjalan.');
			}
			
			if (form.shippingToList == 0) {
				valid = false;
				toaster.pop('error',
						'Setiap Purhcase Order wajib memiliki 1 shipping!');
			}

			form.purchaseRequestItemList
					.forEach(function(purchaseRequestItem, index, array) {
						var maxQty = 0;
						var actualQty = 0;
						form.shippingToList
								.forEach(function(shipTo, index, array) {
									shipTo.purchaseOrderItemList
											.forEach(function(
													purchaseOrderItem,
													indexDua, array) {
												if (purchaseRequestItem.id == purchaseOrderItem.purchaseRequestItem.id) {
													maxQty = Number(purchaseOrderItem.purchaseRequestItem.quantity);
													actualQty = Number(actualQty)
															+ Number(purchaseOrderItem.quantitySend);
												}
											});
								});

						if (Number(maxQty) > Number(actualQty)) {
							var sisaQuantity = Number(maxQty)
									- Number(actualQty);
							toaster.pop('error', 'Jumlah item '
									+ purchaseRequestItem.item.nama
									+ ' masih tersisa ' + sisaQuantity
									+ ' unit!');
							valid = false;
						}
					});

			return valid;
		}

	}
	PurchaseOrderEditController.$inject = [ 'RequestService', '$scope',
			'$http', '$rootScope', '$resource', '$location', '$filter',
			'toaster', 'ModalService' ];

})();