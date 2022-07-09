'use strict';

/* Controllers */

angular
.module('naut')
.controller('evendorCompareCtrl', ['$scope', 'RequestService', '$stateParams', function($scope, RequestService, $stateParams) {
    
	if ($stateParams.vendorList != undefined) {
		$scope.vendorList = $stateParams.vendorList;
	}
    
}]);