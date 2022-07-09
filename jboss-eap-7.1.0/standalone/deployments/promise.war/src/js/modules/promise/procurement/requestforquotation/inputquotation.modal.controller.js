angular
.module('naut')
.controller('InputQuotationModalInstance', function ($scope, $modalInstance, FileUploader, RequestService, requestQuotation, vendorRFQ) {
	$scope.vendorRFQ = vendorRFQ;
	$scope.requestQuotation = requestQuotation;
	
	$scope.attachUploader = new FileUploader({
		url: RequestService.uploadURL(),
        method: 'POST'
	});
	
	$scope.attachUploader.filters.push({
	    name: 'imageFilter',
	    fn: function(item /*{File|FileLikeObject}*/, options) {
	        var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
	        return '|doc|pdf|xls|'.indexOf(type) !== -1;
	    }
	});
	
	$scope.attachUploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
		item.cancel();
	}
	
    $scope.attachUploader.onAfterAddingFile = function(item) {
        if ($scope.attachUploader.queue.length > 1) {
        	$scope.attachUploader.removeFromQueue(0);
        }
    }
	
	$scope.sendQuoteDateStatus = false;
	$scope.sendQuoteDateEvent = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.sendQuoteDateStatus = true;
    }
	
	RequestService.doGET("/procurement/master/satuan/get-list")
	.then(function(data){
		$scope.satuanList = data;
	})

    $scope.okModal = function () {
		RequestService.modalConfirmation()
		.then(function(){
			RequestService.doPOSTJSON("/procurement/RequestQuotationServices/updateVendor", $scope.vendorRFQ)
			.then(function(data){
				$modalInstance.close($scope.vendorRFQ);
			})
		});
    }
    $scope.cancelModal = function () {
        $modalInstance.dismiss('cancel');
    };
});