(function () {
    'use strict';
    angular
        .module('naut')
        .controller('RekapHistoryVendorViewControllerController', RekapHistoryVendorViewControllerController);

    function RekapHistoryVendorViewControllerController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, $state) {
    	var vm = this;
    	
    	RequestService.doGET('/procurement/vendor/vendorServices/getVendor/' + $rootScope.rekapDataRekanan)
    			.then(function success(data) {
	    			vm.vendor = data;
	    			$scope.userId= data.user;
    	
    	//get pernah mengikuti
    	RequestService.doGET('/procurement/inisialisasi/pendaftaranVendorServices/getPernahMengikutiPengadaanByVendorUserId/' + $scope.userId)
    			.then(function success(data) {
    				vm.dataPernahMengikutiList = data;
    			}, function error(response) {
    				RequestService.informError("Terjadi Kesalahan Pada System");
			});
    	
    	//sedang mengikuti
    	RequestService.doGET('/procurement/inisialisasi/pendaftaranVendorServices/getSedangMengikutiPengadaanByVendorUserId/' + $scope.userId)
    			.then(function success(data) {
    				vm.dataSedangMengikutiList = data;
    			}, function error(response) {
    				RequestService.informError("Terjadi Kesalahan Pada System");
			});
    		vm.vendor.star = $scope.getStars(data.performanceAvg);
    	}, function error(response) {
				RequestService.informError("Terjadi Kesalahan Pada System");
		});
    	/*$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendor/' + $rootScope.rekapDataRekanan)
        .success(function (data, status, headers, config) {
            vm.vendor = data;
            //console.log('data vendor :'+JSON.stringify(data));
            $scope.userId= data.user;
            //console.log('data vendor user id :'+JSON.stringify($scope.userId ));
            
          //get pernah mengikuti
        	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPernahMengikutiPengadaanByVendorUserId/' + $scope.userId)
            .success(function (data, status, headers, config) {
                vm.dataPernahMengikutiList = data;
                //console.log('data PernahMengikutiList :'+JSON.stringify($scope.dataPernahMengikutiList));
            })
            .error(function (dataTemp, status, headers, config) {
                console.error('Connection Failed');
                
            });
        	
        	//sedang mengikuti
        	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getSedangMengikutiPengadaanByVendorUserId/' + $scope.userId)
            .success(function (data, status, headers, config) {
                vm.dataSedangMengikutiList = data;
            })
            .error(function (dataTemp, status, headers, config) {
                console.error('Connection Failed');
                
            });
        	
            vm.vendor.star = vm.getStars(data.performanceAvg);
        })
        .error(function (dataTemp, status, headers, config) {
            console.error('Connection Failed');
            
        });*/
    	
    	//tombol back
    	$scope.back = function(){
    		$state.go('app.promise.procurement-manajemenvendor-rekaphistoryvendor');
    	}
    	
    	//get rating
    	$scope.getStars = function(rating) {
            var val = parseFloat(rating);
            var size = val/5*100;
            return size + '%';
        } 
    	
    	//list pemenang total 
    	/*$scope.dataPemenangListByVendor=null;
    	 $http.get($rootScope.backendAddress + '/procurement/penetapanpemenangtotal/getListByVendor/' + $rootScope.rekapDataRekanan)
         .success(function (data, status, headers, config) {
             $scope.dataPemenangListByVendor = data;
         })
         .error(function (dataTemp, status, headers, config) {
             console.error('Connection Failed');
             
         });*/
    	 
    	 // list pemenang total dialihkan ke db promise_t6_kontrak
    	 vm.dataPemenangListByVendor=null;
    	 RequestService.doGET('/procurement/penetapanpemenangtotal/getListByVendor/' + $rootScope.rekapDataRekanan)
			.then(function success(data) {
				vm.dataPemenangListByVendor = data;
			}, function error(response) {
				RequestService.informError("Terjadi Kesalahan Pada System");
		 });
    	 
    	 /*$http.get($rootScope.backendAddress + '/procurement/master/KontrakServices/getKontrakListByVendorId/' + $rootScope.rekapDataRekanan)
         .success(function (data, status, headers, config) {
             vm.dataPemenangListByVendor = data;
         })
         .error(function (dataTemp, status, headers, config) {
             console.error('Connection Failed');
             
         });*/
    	 
    }

    RekapHistoryVendorViewControllerController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state'];

})();