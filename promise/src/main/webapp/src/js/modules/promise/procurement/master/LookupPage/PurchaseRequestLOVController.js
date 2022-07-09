angular.module('naut').controller('PurchaseRequestModal', function($scope, $modalInstance, RequestService) {
	$scope.maxSize = 5;
	$scope.currentPage = 1;
	$scope.maxRecord = $scope.maxSize;
	$scope.checklist = {};
	
	$scope.getDataList = function() {        	
		var params = {
				pageNo: $scope.currentPage,
				pageSize: $scope.maxRecord
		}
		
		if ($scope.searchPR != undefined) {
			params.keywordSearch = $scope.searchPR;
		}
		
		RequestService.requestServer('/procurement/order/purchaseRequestServices/indexPageList', params)
		.then(function (data) {
			$scope.totalItems = data.jmlData;
			if (data.dataList != undefined && data.dataList.length > 0) {
				$scope.dataList = data.dataList;
				$scope.checklist.pilih = data.dataList[0];
			}
		});
	}
	
	$scope.setPage = function (pageNo) {
		$scope.currentPage = pageNo;
		$scope.getDataList();
	}
	
	$scope.pageChanged = function(maxRecord) {
      $scope.maxRecord = maxRecord;
      $scope.getDataList();
    }
	
	$scope.$watch('searchPR', function(newValue, oldValue) {
		$scope.getDataList();
	});

    $scope.pilihData = function () {
        $modalInstance.close($scope.checklist.pilih);
    }
    $scope.cancelModal = function () {
        $modalInstance.dismiss('cancel');
    };
});