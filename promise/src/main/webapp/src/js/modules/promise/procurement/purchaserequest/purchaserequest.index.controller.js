(function() {
	'use strict';
	angular.module('naut').controller('PurchaseRequestController', PurchaseRequestController);

	function PurchaseRequestController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $filter, $state, $q, $modal, $compile,$stateParams) {
		var pr = this;
		pr.RequestService = RequestService;
		$rootScope.ListPrFromItem = {};
		$scope.searchPRNumber = '';
		
		$scope.downloadFile = $rootScope.viewUploadBackendAddress+'/';
		
		// paging
		$scope.allData = 0;
		$scope.rowPerPage = 5;
		$scope.currentPage = 1;

		pr.go = function(path) {
			$location.path(path);
		};

		pr.importData = function() {
			$state.go("app.promise.procurement-purchaserequest-import");
		}

		$scope.getList = function(dataPrRequest) {
			//console.log(dataPrRequest);
			$scope.loading = true;
			var dataPR = {};
			if (typeof dataPrRequest === 'undefined') {
				dataPR = {
					pageNumber : 1,
					numberOfRowPerPage : $scope.rowPerPage,
					searchingKeyWord : '{filter:[{key:prnumber, value:"' + $scope.searchPRNumber + '"}, {key:description, value:"' + $scope.searchPRNumber + '"}' + ']'
					+ ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
				};
				$scope.currentPage = 1;
			} else {
				dataPR = dataPrRequest;
			}
			RequestService.doPOST('/procurement/purchaseRequestServices/getPurchaseRequestListByPRNumberWithPagination', dataPR).then(function successCallback(reponse, index, array) {
				$scope.purchaseRequestList = reponse.data;
				var i = 0;
				angular.forEach($scope.purchaseRequestList.listPurchaseRequestDTOs, function(value, key) {
					var nextApprovalAndStatus = '';

					if (value.status == 2) {
						nextApprovalAndStatus = "<strong class='text-danger'> (Reject)</strong> " + value.nextapproval;
					} else if (value.status == 3) {
						nextApprovalAndStatus = "<strong class='text-muted'> (Approval Process)</strong> " + (value.nextapproval != undefined) ? value.nextapproval : '';
					} else if (value.status == 4) {
						nextApprovalAndStatus = "<strong class='text-warning'> (Hold)</strong> " + value.nextapproval;
					} else if (value.status == 5) {
						nextApprovalAndStatus = "<strong class='text-primary'> (On Process)</strong> " + value.nextapproval;
					} else if (value.status == 6) {
						nextApprovalAndStatus = "<strong class='text-primary'> (Received)</strong> " + value.nextapproval;
					} else if (value.status == 7) {
						nextApprovalAndStatus = "<strong class='text-primary'> (Procurement Process)</strong> " + value.nextapproval;
					} else if (value.status == 8) {
						nextApprovalAndStatus = "<strong class='text-primary'> (Need Verification)</strong> " + value.nextapproval;
					}

					$scope.purchaseRequestList.listPurchaseRequestDTOs[i].nextapproval = nextApprovalAndStatus;
					$scope.purchaseRequestList.listPurchaseRequestDTOs[i].showdetail = false;
					i++;
				});

				$scope.totalItems = $scope.purchaseRequestList.totalItems;
				// total
				
				$scope.loading = false;

			}, function errorCallback(response) {
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
				$scope.loading = false;
			});

		};
		$scope.range = function(n) {
			return new Array(n);
		};

		// Sort
		$scope.sortList = [ {
			sort : 'prnumber',
			name : 'PR Number'
		}, {
			sort : 'department',
			name : 'Departement'
		}, {
			sort : 'daterequired',
			name : 'Required Date'
		}, {
			sort : 'postdate',
			name : 'Post Date'
		}, {
			sort : 'status',
			name : 'Status'
		} ];

		$scope.getSortPR = function() {
			$scope.getPage(1);
		};
		// Search
		$scope.getSearchPr = function() {
			$scope.getPage(1);
		}
		$scope.getPage = function(page) {
			// get All
			if($stateParams.status === null || $stateParams.status === undefined){
				//console.log('state param status '+$stateParams.status );
				//console.log('masuk kondisi 1');
				var dataPR = {
				pageNumber : page,
				numberOfRowPerPage : $scope.rowPerPage,
				searchingKeyWord : '{filter:[{key:prnumber, value:"' + $scope.searchPRNumber + '"}, {key:description, value:"' + $scope.searchPRNumber + '"}' + ']'
				+ ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
				};
			}
			
			else if($stateParams.status != null){
				//console.log('state param status '+$stateParams.status );
				//console.log('masuk kondisi 2');
				var dataPR = {
				pageNumber : page,
				numberOfRowPerPage : $scope.rowPerPage,
				searchingKeyWord : '{filter:[{key:prnumber, value:"' + $scope.searchPRNumber + '"}, {key:description, value:"' + $scope.searchPRNumber + '"}' + (typeof $scope.status === 'undefined' ? '' : ', {key:status, value:"' + $scope.status + '"}') + ']'
						+ ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
				};
			}

			$scope.getList(dataPR);
			$scope.currentPage = page;
		}

		//$scope.getList();
        $scope.status = null;        
		$scope.filterOnStatus = function(value) {
			$stateParams.status=value;
			//console.log('ini nih stateparam nya '+$stateParams.status)
			if (typeof value === 'undefined' || $stateParams.status ==null) {
				$scope.getList()
				
			} else {
                $scope.status = value;
				var dataPR = {
					pageNumber : 1,
					numberOfRowPerPage : $scope.rowPerPage,
					searchingKeyWord : '{filter:[{key:status, value:"' + value + '"}] ,sort:status }'
				};

				$scope.getList(dataPR);
				$scope.currentPage = 1;
			}
		}
        
        if($stateParams.status==8){
            $scope.filterOnStatus(8);
        }
        else if($stateParams.status==5){
            $scope.filterOnStatus(5);
        }
        else if($stateParams.status==6){
            $scope.filterOnStatus(6);
        }
        else if($stateParams.status==3){
            $scope.filterOnStatus(3);
        }
        else if($stateParams.status==7){
            $scope.filterOnStatus(7);
        }
        
        else{
            $scope.getList();
        }

		$scope.pageChanged = function(rowPerPage) {
			$scope.rowPerPage = rowPerPage;
			$scope.getPage(1);
		};

		$scope.showDetail = function(u) {
			if ($scope.active != u.id) {
				$scope.active = u.id;
			} else {
				$scope.active = null;
			}
		};

		$scope.getDetil = function(pr, index) {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/' + pr.id).success(function(data, status, headers, config) {
				pr.orderPurchaseRequestItemList = data;
				var namavendor = "-";
				var rowObject = document.getElementById('info_' + pr.id);
				if ((rowObject.style.display == 'none')) {
					var cell = document.getElementById('cell_info_' + pr.id);
					var content = '<table width="100%" ><tr>';
					content += '          <th scope="col"width="40%"><strong>Item</strong></th>';
					content += '          <th scope="col"><strong>Vendor</strong></th>';
					content += '          <th scope="col"><strong>Qty</strong></th>';
					content += '          <th scope="col"><strong>Price</strong></th>';
					content += '          <th scope="col"><strong>Unit</strong></th>';
					content += '          <th scope="col"><strong>Total</strong></th>';
					content += '          <th scope="col"><strong>Status</strong></th>';
					content += '          <th scope="col"><strong>Aksi</strong></th>';
					content += '     </tr>';

					var cellAction = document.getElementById('cell_action_' + pr.id);

					angular.forEach(pr.orderPurchaseRequestItemList, function(value, key) {

						if (typeof value.vendor === 'undefined' || value.vendor === null) {
							namavendor = "-";
						}
						if (value.vendor !== null) {
							namavendor = value.vendor.nama;
						}

						content += '<tr>';
						content += '          <td height="50px">' + value.itemname + '</td>';
						content += '          <td>' + namavendor + '</td>';
						content += '          <td>' + value.quantity + '</td>';
						content += '          <td>' + value.price.toLocaleString() + '</td>';
						content += '          <td>' + value.unit + '</td>';
						content += '          <td>' + (value.price * value.quantity).toLocaleString() + '</td>';
						content += '          <td>' + value.status + '</td>';
						content += '          <td>' + '<a class="btn btn-primary btn-xs btn-search" tooltip-placement="top" tooltip="View Specs" ng-click="viewSpec(' + value.id + ')"></a>' + '</td>';
						content += '</tr>';

					});
					content += '</table>';
					cell.innerHTML = content;

					$compile(cell)($scope);

					rowObject.style.display = 'table-row';
					$scope.loading = false;
					$scope.purchaseRequestList.listPurchaseRequestDTOs[index].showdetail = true;

					return true;
				} else {
					rowObject.style.display = 'none';
					$scope.loading = false;
					$scope.purchaseRequestList.listPurchaseRequestDTOs[index].showdetail = false;
					return false;
				}
			}).error(function(data, status, headers, config) {
				$scope.loading = false;
			});
		};

		pr.add = function() {
			$rootScope.materialList = [];
			$rootScope.fromCart=false;
			$location.path('/app/promise/procurement/purchaserequest/add');
		}

		pr.buttonEdit = function(purchaserequest) {
			$state.go('app.promise.procurement-purchaserequest-edit', {
				purchaserequest : purchaserequest
			});
		}

		pr.del = function(purchaseReq, size) {
			ModalService.showModalConfirmation('Apakah Anda yakin ingin menghapus Purchase Request ' + purchaseReq.prnumber + '?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doGET('/procurement/purchaseRequestServices/delete/' + purchaseReq.id).then(function successCallback(response) {
					if(response.id != undefined) {
						$scope.getList();
					} else {
						ModalService.showModalInformation('Proses tidak dapat dilanjutkan karena PR sedang dalam JOIN dengan PR lain!');
					}
					
					ModalService.closeModalInformation();
				}, function errorCallback(response) {
					ModalService.closeModalInformation();
					ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
			});
		};

		$scope.countByStatus = function(status) {
			return $q(function(resolve, reject) {
				$http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/countByStatus/' + status).success(function(data) {
					resolve(data);
				}).error(function(data) {
					reject(0);
				});
			});

		}
		
		$scope.countAll = function() {
			return $q(function(resolve, reject) {
				$http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/countAll').success(function(data) {
					resolve(data);
				}).error(function(data) {
					reject(0);
				});
			});

		}
		
		var promiseCountAll = $scope.countAll(); // Count ALl
		promiseCountAll.then(function(resolve) {
			pr.allData  = resolve;
		}, function(reject) {
			pr.allData  = reject;
		})

		
		
		var promiseCountOnNeedVerification = $scope.countByStatus(8); // Need Verification
		promiseCountOnNeedVerification.then(function(resolve) {
			pr.needVerification = resolve;
		}, function(reject) {
			pr.needVerification = reject;
		})
		
		
		var promiseCountOnProcess = $scope.countByStatus(5); // On Process
		promiseCountOnProcess.then(function(resolve) {
			pr.prOnProgress = resolve;
		}, function(reject) {
			pr.prOnProgress = reject;
		})

		var promiseCountReceive = $scope.countByStatus(6); // Received
		promiseCountReceive.then(function(resolve) {
			pr.prReceived = resolve;
		}, function(reject) {
			pr.prReceived = reject;
		})

		var promiseCountApprovalProcess = $scope.countByStatus(3); // Approval
		// Process
		promiseCountApprovalProcess.then(function(resolve) {
			pr.prApprovalProcess = resolve;
		}, function(reject) {
			pr.prApprovalProcess = reject;
		})

		var promiseCountVerified = $scope.countByStatus(7); // Procurement Process
		promiseCountVerified.then(function(resolve) {
			pr.prProcurementProcess = resolve;
		}, function(reject) {
			pr.prProcurementProcess = reject;
		})

		$scope.viewSpec = function(purchaserequestId) {
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getByPurchaseRequestItemId/' + purchaserequestId)
			.success(function(data, status, headers, config) {
				
				$scope.dataItem = data;
				var purchaseRequestId = $scope.dataItem.purchaserequest.id;
				
				$http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + purchaseRequestId)
				.success(function(data, status, headers, config) {
					
					$scope.uploadList = data;
					
					pr.dokumenList = [];
					angular.forEach(data, function(value) {
						var file = {};
						file.fileName = value.uploadPrFileName;
						file.downloadFile = value.uploadPrRealName;
						pr.dokumenList.push(file);
					})
				})
				
				var modalInstance = $modal.open({
					templateUrl : '/viewSpec.html',
					controller : ModalViewSpecController,
					scope: $scope
				});
				var message = "INFO :<br/>";
				modalInstance.result.then(function() {

				}, function() {

				});

			})
			
		};
        
		var ModalViewSpecController = function($scope, $rootScope, $modalInstance) {
			$scope.ok = function() {
				$modalInstance.dismiss('cancel');
			};

		};
		ModalViewSpecController.$inject = [ '$scope', '$rootScope', '$modalInstance' ];

	}

	PurchaseRequestController.$inject = [ 'ModalService', 'RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$state', '$q', '$modal', '$compile','$stateParams' ];

})();