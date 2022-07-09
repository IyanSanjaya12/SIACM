(function() {
	'use strict';

	angular.module('naut').controller('BookingOrderIndexController',
			SalesOrderIndexController);

	function SalesOrderIndexController($scope, $http, $rootScope, $resource,
			$location, RequestService, $state, $stateParams, $filter, $modal,
			ModalService, $window, $log, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile) {
		var vm = this;
		vm.tampObj=[];
		
		 $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
	        $scope.format = $scope.formats[0];
	        $scope.tanggalStartOpen = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.tanggalStartStatus = true;
			}
	        $scope.tanggalEndOpen = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.tanggalEndStatus = true;
			}

		vm.param = {
			search :'',
			pageNo : 1,
			pageSize : 5,
			jmlData : {},
			dataList : {},
			startDate : '',
			endDate : ''

		};
		
		vm.status = [ {
			name : 'New',
			value : '1'
		},{
			name : 'Reject PR',
			value : '2'
		},{
			name : 'Processing PR',
			value : '3'
		},{
			name : 'Reject PR',
			value : '4'
		},{
			name : 'Ready to create PO',
			value : '5'
		},{
			name : 'Processing PO',
			value : '6'
		},{
			name : 'Reject PO',
			value : '7'
		},{
			name : 'PO Completed',
			value : '8'
		},{
			name : 'Failed to Send PR',
			value : '9'
		},{
			name : 'PO Received',
			value : '10'
		},{
			name : 'Awaiting Vendor Approval',
			value : '11'
		},{
			name : 'Awaiting User Approval',
			value : '12'
		},{
			name : 'Vendor Rejected',
			value : '13'
		}];
		
		$scope.bookingOrderList = function() {
			var data = vm.param;
			
			if(!vm.param.startDate ){
				delete data['startDate'];
			}
			
			if(!vm.param.endDate){
				delete data['endDate'];
			}		
			vm.loading = true;
			RequestService.doPOST(
					'/procurement/purchaseRequestServices/get-list', vm.param)
					.then(
							function success(data) {
								if (data.data.dataList != undefined
									&& data.data.dataList.length > 0) {
									vm.dataList = data.data.dataList;
									vm.totalItems = data.data.jmlData;
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

		//$scope.bookingOrderList();
		
		$scope.detail =  function(data){
			$state.go('app.promise.procurement-booking-order-detail', {
				bookingOrder : data
			});
			
		}
		
		$scope.sync = function(data) {
			ModalService.showModalConfirmation('Apakah anda yakin ingin resend BO ini?')
			.then(function(result) {
				ModalService.showModalInformationBlock();
				$scope.sync2(data);
			});
		}
		
		$scope.sync2 = function(purchaseRequest) {
			var button = document.getElementById("Btn");
			button.disabled = true;
			var targetURL = '/catalog/interfacing/bookingOrderInterfacingService/send-data-bo/'+purchaseRequest.id;
			RequestService.doGET(targetURL).then(
					function success(data){
						if(angular.equals(data.status,"ERROR")){
							ModalService.closeModalInformation();
							ModalService.showModalInformation("Response Error from SAP : "+data.statusText+". Please click Resend button");
						}else{
							ModalService.closeModalInformation();
							$scope.bookingOrderList();
							$state.go('app.promise.procurement-booking-order-index');
							$state.reload();
						}
						button.disabled = false;
					},
					function error(response) {
			        	RequestService.informInternalError();
					});
		}

		$scope.add = function (data) {
			
			$state.go('app.promise.procurement-purchaseorder-add', {
				bookingOrder : data,
				toDo : "add"
			});
        }
		
		$scope.setPage = function() {
			$scope.bookingOrderList();
		}

		$scope.pageChanged = function(maxRecord) {
			vm.param.pageSize = maxRecord;
			$scope.bookingOrderList();
		}
		
		$scope.refresh = function() {
			vm.param.statusBO = null;
			vm.param.startDate = null;
			vm.param.endDate = null;
			$scope.getListPaging();
			/*vm.loading = true;
			var button = document.getElementById("refresh");
			button.disabled = true;
			var targetURL = '/procurement/purchaseRequestServices/getPurchaseRequestInterfacingService';
			RequestService.doGET(targetURL).then(
					function success(data){
						button.disabled = false;
						$scope.bookingOrderList();
					},
					function error(response) {
			        	RequestService.informInternalError();
					});*/
		}
		$scope.initTable = function(postData) {
			vm.dtOptions = DTOptionsBuilder.newOptions()
	    	.withOption('ajax', {
	    		url: $rootScope.backendAddress + '/procurement/purchaseRequestServices/getListPaging',
	    		type: 'POST',
	    		data: postData,
	    		 /*PROMISE_SECURITY*/
				"beforeSend": function(xhr) {
			     	xhr.setRequestHeader("Authorization", $rootScope.userToken);
			     },
			     error: function (xhr, error, thrown) {
			 	 	var errorCode = xhr.responseText.substr(xhr.responseText.indexOf("PRMS-ERR"), 12);
	             	$location.path('/page/errorPage/' + errorCode);
	       	     }
			     /*END PROMISE_SECURITY*/
	        })
	        .withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	        	$compile(nRow)($scope);
	        	var start = this.fnSettings()._iDisplayStart;
	        	$("td:first", nRow).html(start + iDisplayIndex + 1);
	        })
	        .withOption('headerCallback', function(thead, data, start, end, display) {
                $compile(angular.element(thead).contents())($scope);
            }) 
	        .withDataProp('data')
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('scrollX', '100%')
            .withPaginationType('full_numbers')
            .withDisplayLength(10)

			vm.dtColumns = [
					DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable()
					.renderWith(function(data, type, full, meta) {
						return meta.row + 1;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.noBo"></span>')
					.renderWith(function(data) {
						return data.boNumber;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.noPR"></span>')
					.renderWith(function(data) {
						return data.prnumber;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.date"></span>')
					.renderWith(function(data, type, full, meta) {
						return $filter('date')(data.created, 'dd/MM/yyyy');
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.buyer"></span>')
					.renderWith(function(data, type, full, meta) {
						return data.organisasi.nama;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.status"></span>')
					.renderWith(function(data) {
						var status = "-";
						if(data.status == 1){
							status ="New";
						}else if(data.status == 2){
							status ="Reject BO";
						}else if(data.status == 3){
							status ="Processing PR";
						}else if(data.status == 4){
							status ="Reject PR";
						}else if(data.status == 4){
							status ="Reject PR";
						}else if(data.status == 5){
							status ="Ready to create PO";
						}else if(data.status ==6){
							status ="Processing PO";
						}else if(data.status == 7){
							status ="Reject PO";
						}else if(data.status == 8){
							status ="PO Completed";
						}else if(data.status == 9){
							status ="Failed to Send BO";
						}else if(data.status == 10){
							status =">PO Received";
						}else if(data.status == 11 || data.status == 12){
							status ="Awaiting For Approval";
						}else if(data.status == 13){
							status ="Vendor Rejected";
						}else if(data.status == 14){
							status ="Publishing Invoice";
						}else if(data.status == 15){
							status ="Payment Completed";
						}else if(data.status == 16){
							status ="Failed Sending DR";
						}
						
						return status;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.bookingOrder.list.interfacing_notes"></span>')
					.renderWith(function(data) {
						return data.interfacingNotes;
					}),
					DTColumnBuilder.newColumn(null).withTitle('<span translate="template.tabel.aksi"></span>')
					.renderWith(function(data) {
						vm.tampObj[data.id] = data;
						var strAksi = '<center><button type="button" class="btn btn-primary" ng-click="detail(vm.tampObj['+data.id+'])" title="Detail Purchase Request">'+
											'<em class="fa fa-search"></em>'+
										'</button>&nbsp;';
						if((data.status == 3 || data.status == 9) && data.prnumber == null && data.isAvailable == true ){
							strAksi +='<button type="button" id="Btn" class="btn btn-warning" title="Re-Send BO" ng-click="sync(vm.tampObj['+data.id+'])">'+
										'<em class="fa fa-send"></em>'+
									'</button>&nbsp;';
						}if(angular.equals($rootScope.userRole.nama, 'Logistik') && data.status == 5 && data.isAvailable){
							strAksi += '<button type="button" class="btn btn-success" ng-click="add(vm.tampObj['+data.id+'])" title="Create Purchase Order">'+
											'<em class="fa fa-plus"></em>'+
										'</button>&nbsp;';
						}
						strAksi+='</center>';
						return strAksi;
					}),
				];

			/*DTInstances.getLast().then(function(instance) {
				vm.dtInstance = instance;
			});*/
		}
		$scope.getListPaging = function() {
			vm.loading = true;
			var postData = {
    				refresh: '',
    				startDate: vm.param.startDate==undefined?"":$filter('date')(vm.param.startDate, "yyyy-MM-dd"),
    				endDate: vm.param.endDate==undefined?"":$filter('date')(vm.param.endDate, "yyyy-MM-dd"),
    				statusBO: vm.param.statusBO==undefined?"":vm.param.statusBO,
    				statusPR: vm.param.statusPR==undefined?"":vm.param.statusPR
			};
			$scope.initTable(postData);
			vm.loading = false;
		}
		$scope.getListPaging();
	}

	SalesOrderIndexController.$inject = [ '$scope', '$http', '$rootScope',
			'$resource', '$location', 'RequestService', '$state',
			'$stateParams', '$filter', '$modal', 'ModalService', '$window',
			'$log', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile' ];

})();