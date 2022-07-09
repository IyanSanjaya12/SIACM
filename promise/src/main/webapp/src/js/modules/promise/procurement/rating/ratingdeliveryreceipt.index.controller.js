/**
 * ========================================================= Module:
 * LaporanCatalogController
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('RatingDeleveryReceiptController',
			RatingDeleveryReceiptController);

	function RatingDeleveryReceiptController(RequestService, ModalService, $scope,
			$http, $resource, $location, $modal, $state, $filter,
			$stateParams, $log) {
		var vm = this;
		
		$scope.startDateOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.startDateStatus = true;
		}
		$scope.endDateOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.endDateStatus = true;
		}
		
		$scope.showRating = true;
		
		vm.param = {
			search : '',
			pageNo : 1,
			pageSize : 5,
			dataList : {},
			jmlData : {},
			status : '',
			startDate : '',
			endDate : '',
			sort : ''

		};

		vm.sortDRList = [ {
			name : 'Already Rated',
			value : '1'
		}, {
			name : 'Ready to Rate',
			value : '2'
		}
		];
		
		vm.sortAscDesc = [ {
			name : 'ASC',
			value : 'ASC'
		}, {
			name : 'DESC',
			value : 'DESC'
		} ];

		$scope.vendorList = function () {
		       
            RequestService.doGET('/procurement/laporan/get-vendor-list').then(function success(data) {
	        	vm.vendorList = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
       }
	  $scope.vendorList();
	  
		
		$scope.DRList = function() {
			vm.loading = true;
			var data = vm.param;
			if(!vm.param.startDate){
				delete data['startDate'];
			}
			if(!vm.param.endDate){
				delete data['endDate'];
			}
			
			RequestService.doPOST('/procurement/rating/delivery-receipt/get-list', vm.param)
					.then(
							function success(data) {
								if (data.data.dataList != undefined
										&& data.data.dataList.length > 0) {
									vm.dataList = data.data.dataList;
									vm.totalItems = data.data.jmlData;
									vm.role =  data.data.role;
									if(vm.role == "PENYEDIA"){
										$scope.showRating = false;
									}
								} else {
									vm.dataList = [];
									vm.totalItems = 0;
								}
								vm.loading = false;
							}, function error(response) {
								RequestService.informInternalError();
								vm.loading = false;
							});
		}

		$scope.DRList();


		$scope.popUpRating = function (data,vendor, action, role) {
			$scope.modalInstance = $modal.open({
                templateUrl: '/popUpRating.html',
                controller: ModalCtrl,
                size: 'md',
                resolve: {
                	deliveryReceipt: function () {
                      return data;
                    }, 
                    toDo: function () {
                        return action;
                      }, 
	                vendor: function () {
	                      return vendor;
	                    },
	                role: function () {
		                      return role;
		                    }
                  }
            });
			$scope.modalInstance.result.then(function () { //ok
				$scope.DRList();
            }, function () {});
        };
        var ModalCtrl = function ($scope, $modalInstance, deliveryReceipt, toDo, vendor, role) {
        	$scope.submitted = false;
        	$scope.deliveryReceipt = {};
        	$scope.deliveryReceipt= deliveryReceipt;
        	$scope.vendor = null;
        	$scope.vendor = vendor;
        	$scope.toDo = null;
        	$scope.toDo = toDo;
        	$scope.selStars = 0;
        	$scope.isDisable = false;
        	
        	$scope.role = role;
        	

        	if (  $scope.toDo == "detail" ) {
        		$scope.selStars = $scope.deliveryReceipt.purchaseOrder.rating;
        		$scope.isDisable = true;
        	}
    	   
    	    $scope.maxStars = 6;

    	    $scope.getStarArray = function() {
    	      var result = [];
    	      for (var i = 1; i <= $scope.maxStars; i++)
    	        result.push(i);
    	      return result;
    	    };

    	    $scope.getClass = function(value) {
    	      return 'glyphicon glyphicon-star' + ($scope.selStars >= value ? '' : '-empty');
    	    };

    	    $scope.setClass = function(sender, value) {
    	    	$scope.selStars = value;
    	      sender.currentTarget.setAttribute('class', $scope.getClass(value));

    	    };
    	    
    	    $scope.save = function (starValue) {
    	    	$scope.submitted = true;			
    			if(starValue != 0){
    				RequestService.modalConfirmation().then(function (result){
    					$scope.deliveryReceipt.purchaseOrder.rating = starValue;
    					$scope.saveData();   
    					vm.loading = true;
    				});
    			}
        	}
        	    
    	    $scope.saveData = function(){
    	    	var url = '/procurement/purchaseorder/PurchaseOrderServices/updateRatingPO'
    	        RequestService.doPOSTJSON(url, $scope.deliveryReceipt.purchaseOrder).then(function success(data) {
    	        	if(data != undefined) {
    	                	RequestService.informSaveSuccess();
    	                	$modalInstance.close();
    	                	$state.go('app.promise.procurement-rating-delivery-receipt');
    	        	}
    	        }, function error(response) {
    	        	RequestService.informInternalError();
    	        	$scope.loading = false;
    	        });
            }    
        	
            //$scope.simpanStok=function(){
            	//RequestService.modalConfirmation('Yakin menyimpan data ?').then(function (result) {
					//$scope.saveDataStock($scope.deliveryReceipt.purchaseOrder);
				//});
            //}
            //$scope.saveDataStock=function(po){
            	//$modalInstance.close(po);
            //};
            $scope.ok = function () {
            	$modalInstance.close('closed');
                
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalCtrl.$inject = ['$scope', '$modalInstance', 'deliveryReceipt', 'toDo', 'vendor','role'];
        
        
		
		
		
		
		
		
		

		$scope.setPage = function() {
			$scope.DRList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.DRList();
		}

	}

	RatingDeleveryReceiptController.$inject = [ 'RequestService', 'ModalService',
			'$scope', '$http', '$resource', '$location',
			'$modal', '$state', '$stateParams', '$log', '$filter' ];

})();
