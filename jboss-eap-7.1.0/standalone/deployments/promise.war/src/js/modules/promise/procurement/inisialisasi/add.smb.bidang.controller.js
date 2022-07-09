(function() {
    'use strict';

    angular.module('naut')
		.controller('BidangPengadaanSMBController', BidangPengadaanSMBController)
        .filter('propsFilter', propsFilter);
	function BidangPengadaanSMBController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams) {
		$scope.bidangUsaha = {};
		$scope.getBidangUsaha = function() {
			$scope.loading = true;
			$http.get($rootScope.backendAddress + '/procurement/master/bidang-usaha/get-bidang-usaha-by-parent-id/0').success(
            function(data, status, headers, config) {
                $scope.bidangUsahaList = data;
            }).error(function(data, status, headers, config) {});
			$scope.loading = false;
		};
		$scope.getBidangUsaha();
		$scope.getSubBidangUsaha = function(){
			if(typeof $scope.bidangUsaha.selected !=='undefined'){	
				$scope.loading = true;
				$scope.subBidangUsahaList = [];
				$http.get($rootScope.backendAddress + '/procurement/master/sub-bidang-usaha/get-by-bidang-usaha-id/'+$scope.bidangUsaha.selected.id)
					.success(function(data, status, headers, config) {
						$scope.subBidangUsahaList = data;
						$scope.checkedSubBidangUsaha = [];
					
						if($rootScope.inisialisasiForm.subBidangUsahaList.length > 0){
							//console.log('INFO $rootScope.inisialisasiForm.subBidangUsahaList.length ' + $rootScope.inisialisasiForm.subBidangUsahaList.length);
							//console.log('INFO $rootScope.inisialisasiForm.subBidangUsahaList: ' + JSON.stringify( $rootScope.inisialisasiForm.subBidangUsahaList));

							for(var i=0; i < $rootScope.inisialisasiForm.subBidangUsahaList.length; i++){
								for(var j=0; j < $scope.subBidangUsahaList.length; j++){
									//console.log('INFO Loop j = '+j);
									//console.log('INFO Checked: $rootScope.inisialisasiForm.subBidangUsahaList[i].id = '+$rootScope.inisialisasiForm.subBidangUsahaList[i].id+', $scope.subBidangUsahaList[j].id= '+$scope.subBidangUsahaList[j].id);
									if($rootScope.inisialisasiForm.subBidangUsahaList[i].id == $scope.subBidangUsahaList[j].id){
										//$scope.togleCheckSubBidangUsaha( $scope.subBidangUsahaList[j] );	
										$scope.subBidangUsahaList.splice( $scope.subBidangUsahaList.indexOf($scope.subBidangUsahaList[j]), 1 );
										//console.log('INFO Remove: ' + JSON.stringify( $scope.subBidangUsahaList[j] ));
									}
								}
							}
						}
				
					
					}).error(function(data, status, headers, config) {});
				
					$scope.tableSubBidangUsahaList = new ngTableParams({
					  page: 1,            // show first page
					  count: 10           // count per page
				  	}, {
					  total: $scope.subBidangUsahaList.length, // length of data4
					  getData: function($defer, params) {
						  $defer.resolve($scope.subBidangUsahaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
					  }
				  });
				
				$scope.loading = false;	
			}
		};
		
		$scope.checkedSubBidangUsaha = [];
		$scope.togleCheckSubBidangUsaha = function(subBidangUsaha){
			if($scope.checkedSubBidangUsaha.indexOf(subBidangUsaha) === -1){
				$scope.checkedSubBidangUsaha.push(subBidangUsaha);
			} else {
				$scope.checkedSubBidangUsaha.splice($scope.checkedSubBidangUsaha.indexOf(subBidangUsaha), 1);
			}
		};
		
		$scope.btnBatal = function(){
			$location.path('/app/promise/procurement/inisialisasi/add/smb');	
		}
		$scope.btnSimpanBidang = function() {
			//console.log('INFO Bidang : '+JSON.stringify($scope.checkedSubBidangUsaha));
			if($scope.checkedSubBidangUsaha.length == 0){
				$scope.bidangUsahaError = true;	
			} else {
				$scope.bidangUsahaError = false;	
				if($rootScope.inisialisasiForm.subBidangUsahaList.length == 0 ){
					$scope.loading = true;
					$rootScope.inisialisasiForm.subBidangUsahaList = $scope.checkedSubBidangUsaha;
					$location.path('/app/promise/procurement/inisialisasi/add/smb');											
					$scope.loading = false;
				} else {
					$scope.loading = true;
					$rootScope.inisialisasiForm.subBidangUsahaList = $rootScope.inisialisasiForm.subBidangUsahaList.concat($scope.checkedSubBidangUsaha);							
					$scope.loading = false;
					$location.path('/app/promise/procurement/inisialisasi/add/smb');													
					$scope.loading = false;
				}
			
				//console.log('SAVE $rootScope.inisialisasiForm.subBidangUsahaList: ' + JSON.stringify( $rootScope.inisialisasiForm.subBidangUsahaList));
			}
			//$location.path('/app/promise/procurement/inisialisasi/add/bumn');	
		}
	}
	
	BidangPengadaanSMBController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams'];
	
	function propsFilter() {
      return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
          items.forEach(function(item) {
            var itemMatches = false;

            var keys = Object.keys(props);
            for (var i = 0; i < keys.length; i++) {
              var prop = keys[i];
              var text = props[prop].toLowerCase();
              if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                itemMatches = true;
                break;
              }
            }

            if (itemMatches) {
              out.push(item);
            }
          });
        } else {
          // Let the output be the input untouched
          out = items;
        }

        return out;
      };
    }
})();