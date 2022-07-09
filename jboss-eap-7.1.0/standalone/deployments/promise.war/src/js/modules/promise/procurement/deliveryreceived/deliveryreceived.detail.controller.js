(function() {
	'use strict';

	angular.module('naut').controller('DetailDeliveryReceivedController',
			DetailDeliveryReceivedController);

	function DetailDeliveryReceivedController(RequestService, ModalService, $scope, FileUploader, toaster,
			$http, $rootScope, $resource, $location, $modal, $state, $filter,
			$stateParams, $log, $window) {
		var vm = this;
		vm.toDo = $state.params.toDo;
		vm.deliveryReceived = ($state.params.deliveryReceived != undefined) ? $state.params.deliveryReceived : {};
		vm.submitted = false;
		
		// Tab Image
		$scope.uploader = new FileUploader({
			url: RequestService.uploadURL()
		});
		$scope.uploader.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

	  
		$scope.incrementItem = function(index) {
			vm.detailList[index].dikirim += 1;
		}

		$scope.decrementItem = function(index) {
			vm.detailList[index].dikirim -= 1;
		}
		
		$scope.supportedFile = "Supported file:" + $rootScope.fileTypeImg.toLowerCase().replace(/\s+/g, '') + ". Max Size:" + $rootScope.fileUploadSize / (1024 * 1024) + " MB";
		
		 $scope.formats = ['dd-MMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
	        $scope.format = $scope.formats[4];
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
	        vm.minDate = vm.minDate ? null : new Date();
	        vm.dateOptions = {
	    			formatYear : 'yy',
	    			startingDay : 1
	    		};
	        vm.poDateOpen = function($event) {
				$event.preventDefault();
				$event.stopPropagation();
				vm.poDateStartStatus = true;
			}
			vm.poDateEnd = function($event){
				$event.preventDefault();
				$event.stopPropagation();
				vm.poDateEndStatus = true;
			}
		vm.param = {
			dikirim : 0,
			dataList : {},
			poId : vm.deliveryReceived.poId,
			drId : vm.deliveryReceived.drId,
			catatan : '',

		};
			
		if(vm.toDo == "received"){
			$scope.deliveryReceivedDetail = function() {
				var data = vm.param;
				vm.loading = true;
				RequestService.doPOST('/procurement/deliveryreceived/DeliveryReceivedServices/get-detail-received', vm.param)
						.then(
								function success(data) {
									if (data.data.detailList != undefined
											&& data.data.detailList.length > 0) {
										vm.detailData = data.data.detailData;
										vm.detailList = data.data.detailList;
										
										angular.forEach(vm.detailData.deliveryReceivedFiles, function (image, indexImage) {
											var item = new FileUploader.FileItem($scope.uploader, {
												size: 0,
												name: image.receiptFileName
											});
											item.isUploading = false;
											item.isReady = false;
											item.progress = 100;
											item.isUploaded = true;
											item.isSuccess = true;
											item.imagesRealName = '';
//											item.loadFile = RequestService.loadURL(catalogImage.imagesRealName);
											$scope.uploader.queue.push(item);
										});

									} else {
										vm.detailList = [];
										vm.detailData = null;
									}
									vm.loading = false;
								}, function error(response) {
									RequestService.informInternalError();
									vm.loading = false;
								});
			}

			$scope.deliveryReceivedDetail();
		}
		
		if(vm.toDo == "process"){
			$scope.deliveryReceivedDetail = function() {
				var data = vm.param;
				vm.loading = true;
				RequestService.doPOST('/procurement/deliveryreceived/DeliveryReceivedServices/get-detail-process', vm.param)
						.then(
								function success(data) {
									if (data.data.detailList != undefined
											&& data.data.detailList.length > 0) {
										vm.detailData = data.data.detailData;
										vm.detailList = data.data.detailList;
										
									} else {
										vm.detailList = [];
										vm.detailData = null;
									}
									vm.loading = false;
								}, function error(response) {
									RequestService.informInternalError();
									vm.loading = false;
								});
			}

			$scope.deliveryReceivedDetail();
		}
				
		$scope.back = function (){
			$state.go('app.promise.procurement-deliveryreceived', {});
		}
		
		 $scope.printDr = function(){
			 	RequestService.doPrint({reportFileName:'PrintDeliveryReceipt', drId:vm.detailData.drId});
	 		}
		
			$scope.btnSimpan = function() {
//				formValid
				vm.submitted = true;
				var sekarang = new Date;
				if (vm.detailData.dateReceived == null) {
					vm.submitted = false;
					$scope.errormessagePoDate = 'Field tidak boleh kosong!';
				}
				if (vm.detailData.deliveryOrderDate == null) {
					vm.submitted = false;
					$scope.errormessagePoDateEnd = 'Field tidak boleh kosong!';
				}
				else if (vm.detailData.dateReceived != null) {
					if (vm.detailData.dateReceived > sekarang){
						vm.submitted = false;
						alert('Tanggal Penerimaan tidak boleh lebih dari current date!');
					}
				}
				vm.imageList = [];
				angular.forEach($scope.uploader.queue, function (item, indexItem) {
					var image = {
						receiptFileName: item.file.name
					};
					vm.imageList.push(image);
				});
				if (vm.imageList.length == 0) {
					$scope.errormessageImage = 'Image Harus Diisi!';
					vm.submitted = false;
				}
				if (vm.submitted) {
					RequestService.modalConfirmation().then(function(result) {
						$scope.saveData();
						ModalService.showModalInformationBlock();
					});
				}
			}
			$scope.saveData = function() {
				vm.loading = true;
				vm.detail = [];
				vm.total = [];
				angular.forEach(vm.detailList, function (list, indexItem) {
					var detail = {
							purchaseOrderItem: list.purchaseOrderItem,
							dikirim: list.dikirim
					};
					var total = list.dikirim;
					vm.detail.push(detail);
					vm.total.push(total);
				});
				var deliveryReceivedListPagination = {
					poId : vm.deliveryReceived.poId,
					dateReceived : vm.detailData.dateReceived,
					estimasi : vm.detailData.estimasi,
					description : vm.detailData.description,
					deliveryOrderNum : vm.detailData.deliveryOrderNum,
					dikirim : vm.total,
					deliveryReceivedFiles : vm.imageList,
					deliveryReceivedDetailDTO : vm.detail,
					deliveryOrderDate : vm.detailData.deliveryOrderDate
				};

				RequestService.doPOSTJSON(
						'/procurement/deliveryreceived/DeliveryReceivedServices/insert',
						deliveryReceivedListPagination).then(function success(data) {
					if(!data.response.includes("SUCCESS")){
						ModalService.closeModalInformation();
						ModalService.showModalInformation("Response Error from SAP: "+data.response +" Please click Resend button");
						vm.loading = false;
						$state.go('app.promise.procurement-deliveryreceived');
					}else{
						ModalService.closeModalInformation();
						vm.loading = false;
						$state.go('app.promise.procurement-deliveryreceived');
						RequestService.informSaveSuccess();
						
					}

				}, function error(response) {
					RequestService.informInternalError();
					ModalService.closeModalInformation();
					vm.loading = false;
				});
			}

	}

	DetailDeliveryReceivedController.$inject = [ 'RequestService', 'ModalService', 
			'$scope', 'FileUploader', 'toaster', '$http', '$rootScope', '$resource', '$location',
			'$modal', '$state', '$filter', '$stateParams', '$log', '$window'];

})();