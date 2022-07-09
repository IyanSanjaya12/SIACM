(function() {
	'use strict';
	angular.module('naut').controller('PRJoinIndexController', PRJoinIndexController);

	function PRJoinIndexController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $filter, $state, $q, $modal, $compile) {
		var pr = this;
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
			$scope.loading = true;
			var dataPR = {};
			if (typeof dataPrRequest === 'undefined') {
				dataPR = {
					pageNumber : 1,
					numberOfRowPerPage : $scope.rowPerPage,
					searchingKeyWord : '',
					isJoin : 2
				};
			} else {
				dataPR = dataPrRequest;
			}
			RequestService.doPOST('/procurement/purchaseRequestServices/getPurchaseRequestListByPRNumberWithPagination', dataPR)
			.then(function successCallback(reponse, index, array) {
				$scope.purchaseRequestList = reponse.data;
				var i = 0;
				angular.forEach($scope.purchaseRequestList.listPurchaseRequestDTOs, function(value, key) {
					var nextApprovalAndStatus = '';
					if(value.nextapproval == undefined) {
						value.nextapproval = '';
					}
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
					} else if (value.status == 9) {
						nextApprovalAndStatus = "<strong class='text-primary'> (Join PR)</strong> " + value.nextapproval;
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
			var dataPR = {
				pageNumber : page,
				numberOfRowPerPage : $scope.rowPerPage,
				isJoin : 2,
				searchingKeyWord : '{filter:[{key:prnumber, value:"' + $scope.searchPRNumber + '"}, {key:description, value:"' + $scope.searchPRNumber + '"}' + (typeof $scope.status === 'undefined' ? '' : ', {key:status, value:"' + $scope.status + '"}') + ']'
						+ ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
			};

			$scope.getList(dataPR);
			$scope.currentPage = page;
		}

		$scope.getList();

		$scope.filterOnStatus = function(value) {
			if (value == undefined) {
				$scope.getList()
			} else {
				var dataPR = {
					pageNumber : 1,
					numberOfRowPerPage : $scope.rowPerPage,
					isJoin : 2,
					searchingKeyWord : '{filter:[{key:status, value:"' + value + '"}] ,sort:status }'
				};

				$scope.getList(dataPR);
				$scope.currentPage = 1;
			}
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
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestJoinId/' + pr.id)
			.success(function(data, status, headers, config) {
				pr.orderPurchaseRequestItemList = data;
				var namavendor = "-";
				var rowObject = document.getElementById('info_' + pr.id);
				if ((rowObject.style.display == 'none')) {
					var cell = document.getElementById('cell_info_' + pr.id);
					var content = '<table width="100%" ><tr>';
					content += '          <th scope="col"><strong>Item</strong></th>';
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

						if (typeof value.vendor === 'undefined' || value.vendor !== null) {
							namavendor = value.vendor.nama;
						}

						content += '<tr>';
						content += '          <td height="35px">' + $filter('limitTo')(value.itemname, 35, 0) + '</td>';
						content += '          <td>' + namavendor + '</td>';
						content += '          <td>' + value.qtyafterjoin + '</td>';
						content += '          <td>' + value.priceafterjoin.toLocaleString() + '</td>';
						content += '          <td>' + value.unit + '</td>';
						content += '          <td>' + (value.priceafterjoin * value.qtyafterjoin).toLocaleString() + '</td>';
						content += '          <td>' + value.status + '</td>';
						content += '          <td>' + '<a class="view-order badge bg-primary" tooltip-placement="top" tooltip="View Specs" ng-click="viewSpec(' + value.id + ')"> <em class="glyphicon glyphicon-search"></em></a>' + '</td>';
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

		pr.addJoinPR = function() {
			$state.go('app.promise.procurement-purchaserequestconsolidation');
		}
        
        $rootScope.prJoinList = [];
		pr.buttonEdit = function(purchaserequest) {
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestJoinId/' + purchaserequest.id)
			.success(function(data) {
				var itemList = [];
				angular.forEach(data,function(value){
					itemList.push(value.item.id);
				})
	        	$rootScope.prJoinList = data;
	        	$state.go("app.promise.procurement-purchaserequestjoin", {itemList: itemList, pr: purchaserequest});
			});
		}

		pr.del = function(purchaseReq, size) {
			ModalService.showModalConfirmation('Apakah Anda yakin ingin menghapus Purchase Request ' + purchaseReq.prnumber + '?').then(function(result) {
				ModalService.showModalInformationBlock();
				RequestService.doGET('/procurement/purchaseRequestServices/join/delete/' + purchaseReq.id).then(function successCallback(response) {
					$scope.getList();
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
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getByPurchaseRequestItemId/' + purchaserequestId).success(function(data, status, headers, config) {
				
				$scope.dataItem = data;
				
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

	PRJoinIndexController.$inject = [ 'ModalService', 'RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$filter', '$state', '$q', '$modal', '$compile' ];

})();