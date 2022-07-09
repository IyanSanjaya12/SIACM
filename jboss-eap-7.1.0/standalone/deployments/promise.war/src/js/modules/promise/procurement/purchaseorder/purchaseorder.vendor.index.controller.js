/**=========================================================
 * Module: PurchaseOrderController.js
 * Author: Lia, Mamat
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PurchaseOrderVendorController', PurchaseOrderVendorController);

    function PurchaseOrderVendorController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $filter, $ocLazyLoad, $window) {

		$ocLazyLoad.load('src/js/modules/promise/procurement/purchaseorder/purchaseorder.edit.controller.js');
		
        var po = this;
        po.poOnProgress = 0;
        po.poReceived = 0;
        po.poRequsetFromContract = 0;
        po.poApprovalProcess = 0;
        //paging
        $scope.allData = 0;
        $scope.rowPerPage = 10;
        $scope.currentPage = 1;

        $scope.getList = function (dataPORequest) {
        	//console.log(dataPORequest.pageNumber);
            $scope.loading = true;
            $scope.pageList = [];
            var dataPO = {};
            if (typeof dataPORequest === 'undefined') {
                dataPO = {
                    pageNumber: 1,
                    numberOfRowPerPage: $scope.rowPerPage,
                    searchingKeyWord: ''
                };
            } else {
                dataPO = dataPORequest;
            }
            RequestService.doPOST('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseOrderListWithPaginationVendor', dataPO)
			.then(function successCallback(reponse, index, array) {
				$scope.purchaseOrderList = reponse.data;
				$scope.totalItems = $scope.purchaseOrderList.jmlData;

				if (dataPO.searchingKeyWord == '{filter:[{key:poNumber, value:"' + $scope.searchPONumber + '"}]' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}' ) $scope.allData = $scope.purchaseOrderList.jmlData;
				$scope.loading = false;
				
			}, function errorCallback(response) {				 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
				$scope.loading = false;
			});
        };
        
		$scope.pageChanged = function(rowPerPage) {
          $scope.rowPerPage = rowPerPage;
          $scope.getPage(1);
          //console.log('masuk sini');
        };

        $scope.range = function (n) {
            return new Array(n);
        };
        //Sort
        $scope.sortList = [
            {
            	sort: 'purchaseOrderDate',
                name: 'Post Date'
            },
            {
                sort: 'poNumber',
                name: 'PO Number'
            }, {
                sort: 'purchaseRequest.prnumber',
                name: 'PR Number'
            }, {
                sort: 'department',
                name: 'Departement'
            },
        ];
        
        $scope.getSortPO = function () {
            $scope.getPage(1);
        };
        //Search
        $scope.getSearchPo = function () {
            if ($scope.searchPONumber.length >= 3) {
                $scope.getPage(1);
            } else {
                if ($scope.searchPONumber.length == 0) {
                    $scope.getList();
                }
            }
        }
        $scope.getPage = function (page) {
        	//console.log(page);

            var dataPO = {
                pageNumber: page,
                numberOfRowPerPage: $scope.rowPerPage,
                searchingKeyWord: '{filter:[{key:poNumber, value:"' + $scope.searchPONumber + '"}' + (typeof $scope.status === 'undefined' ? '' : ', {key:status, value:"' + $scope.status + '"}') + ']' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}'
            };

            $scope.getList(dataPO);
        }

        $scope.getDetil = function (po) {
            $scope.loading = true;
            var formParam = {
                poId: po.id
            };
            
            RequestService.doPOST('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseOrderListByPoId', formParam)
			.then(function successCallback(reponse, index, array) {
				$scope.getPODetil = reponse.data;
                var rowObject = document.getElementById('info_' + po.id);
                var btnDetil = document.getElementById('btnDetil_' + po.id);

                if ((rowObject.style.display == 'none')) {
                    //rowObject.innerHTML = '<td colspan="7">Dus asdfasdf adfasdf adfadfadf</td>';                
                    var cell = document.getElementById('cell_info_' + po.id);
                    var content = '<div class="row">';
                    content += '          <div class="col-lg-3" style="bg-color:grey-light"><strong>Item</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light"><strong>Vendor</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light"><strong>Qty</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light"  align="right"><strong>Price</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light"><strong>Unit</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light"  align="right"><strong>Total</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light"><strong>Status</strong></div>';
                    content += '     </div>';
                    $scope.getPODetil.forEach(function (value, key) {
                        content += '<div class="row">';
                        content += '          <div class="col-lg-3">' + value.itemName + '</div>';
                        content += '          <div class="col-lg-2">' + value.vendorName + '</div>';
                        content += '          <div class="col-lg-1">' + value.quantitySend + '</div>';
                        content += '          <div class="col-lg-2" align="right">' + $filter('number')(value.unitPrice, 2) + '</div>';
                        content += '          <div class="col-lg-1">' + value.unit + '</div>';
                        content += '          <div class="col-lg-2" align="right">' + $filter('number')((value.unitPrice * value.quantitySend), 2) + '</div>';
                        content += '          <div class="col-lg-1">' + value.status + '</div>';
                        content += '     </div><hr/>';
                    });
                    cell.innerHTML = content;
                    rowObject.style.display = 'table-row';
                    btnDetil.innerText = 'Hide Detil';
                    $scope.loading = false;
                    return true;
                } else {
                    rowObject.style.display = 'none';
                    btnDetil.innerText = 'Show Detil';
                    $scope.loading = false;
                    return false;
                }
			}, function errorCallback(response) {				 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
				$scope.loading = false;
			});

        };
        //Count Satus
        $scope.getCountStatus = function (status) {
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/purchaseorder/PurchaseOrderServices/countByStatusAndOrgVendor',
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
                    status: status
                }
            }).success(function (data, status, headers, config) {
                return data;
                $scope.loading = false;
            });
        };

        $scope.getOnStatus = function (status) {
            $scope.status = status;
            var dataPO = {
                pageNumber: 1,
                numberOfRowPerPage: $scope.rowPerPage
            };
            if (status == 'All') {
            	dataPO.searchingKeyWord = '{filter:[{key:poNumber, value:"' + $scope.searchPONumber + '"}]' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}';
                //dataPO.searchingKeyWord = '';
                $scope.status = undefined;
            } else {
                dataPO.searchingKeyWord = '{filter:[{key:poNumber, value:"' + $scope.searchPONumber + '"}, {key:status, value:"' + status + '"}]' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + '}';
            }
            $scope.getList(dataPO);
        };

        //On Process
        $scope.getCountOnProgress = function () {
            $scope.getCountStatus('On Process').then(function (result) {
                po.poOnProgress = result.data;
            });
        }
        $scope.getCountOnProgress();
         
        $scope.getCountRequsetFromContract = function () {
            $scope.getCountStatus('Request From Contract').then(function (result) {
                po.poRequsetFromContract = result.data;
            });
        }
        $scope.getCountRequsetFromContract();
        
       $scope.getCountApprovalProcess = function () {
           $scope.getCountStatus('Approval Process').then(function (result) {
               po.poApprovalProcess = result.data;
           });
       }
       $scope.getCountApprovalProcess();

        //Received
        $scope.getCountReceived = function () {
            $scope.getCountStatus('DONE').then(function (result) {
                po.poReceived = result.data;
            });
        }
        $scope.getCountReceived();

        
        //$scope.sortBy = 'id';
        $scope.getOnStatus('All');

        $scope.editPO = function (po) {
        	$rootScope.purchaseOrder = po;
            $location.path('/app/promise/procurement/purchaseorder/vendor/detail');
        }
		
        $scope.printSertifikat = function(po){
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=PrintPurchasOrder&poId=' + po.id, '_blank');
//			RequestService.doPrint({reportFileName:'PrintPurchasOrder', poId:po.id});			
 		}
    }
    
    PurchaseOrderVendorController.$inject = ['ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$filter', '$ocLazyLoad', '$window'];

})();