'use strict';

/* Controllers */

angular
    .module('naut')
    .controller('evendorcatalogCtrl', ['$scope', 'RequestService', 'ModalService', '$state', '$filter', '$log', '$modal', '$stateParams', '$http', function ($scope, RequestService, ModalService, $state, $filter, $log, $modal,$stateParams, $http) {
    	
    	$scope.gridViewOpt = {
                gridWidth: 250,
                scrollContainer: '#prodwrap',
                refreshOnImgLoad: true
        }

        $scope.showList = true;

        $scope.gambarDefault = './app/img/default_vendor.png';
    	
        var vm = this;
        vm.ratings = null; // 0 sampai 5
        vm.values = [{
            'key': 1,
            'value': 'Name'
        }, {
            'key': 2,
            'value': 'Rating'
        }, {
            'key': 3,
            'value': 'Transaksi'
        }];
        vm.selected;
        vm.bidangUsahaId = 0;
        vm.subBidangUsahaId = 0;
        vm.sbText = null;

        vm.pagingMaxBaris = 10;
        vm.pagingTotalBaris = 0;
        vm.pagingHalamanKe = 1;
        //console.log('data vm =' + JSON.stringify(vm));

        $scope.dtOptions = {
            searching: false,
            order: []
        };
        
        RequestService.doGET('/procurement/master/bidang-usaha//find-all-with-tree-for-list-vendor')
            .then(function (data) {
                $scope.bidangUsahaList = data;
            });

        $scope.search = function () {
            $scope.loading = true;
            vm.searchData = {
                bidangUsahaId: vm.bidangUsahaId,
                subBidangUsahaId: vm.subBidangUsahaId
            };

            if ((typeof vm.selected != 'undefined') && (vm.selected != null)) {
                vm.searchData.sortByColumn = vm.selected.key;
            }

            if ((typeof vm.vendorAndProductCategoryName != 'undefined') && (vm.vendorAndProductCategoryName != null)) {
                vm.searchData.vendorAndProductCategoryName = vm.vendorAndProductCategoryName;
            }

            if ((typeof vm.location != 'undefined') && (vm.location != null)) {
                vm.searchData.location = vm.location;
            }

            if ((typeof vm.ratings != 'undefined') && (vm.ratings != null)) {
                vm.searchData.ratings = vm.ratings;
            }

            vm.searchData.maxBaris = vm.pagingMaxBaris;
            vm.searchData.halamanKe = vm.pagingHalamanKe;
            //console.log('search data =' + JSON.stringify(vm.searchData));
            RequestService.doPOST('/procurement/vendor/vendorServices/findAllVendorCatalogListForSearch', vm.searchData)
                .success(function (data) {
                    vm.pagingTotalBaris = data.totalRow;
                        
                    angular.forEach(data.vendorList, function (vendor) {
                        vendor.performanceAvgFloor = window.Math.floor(vendor.performanceAvg);
                        if (typeof vendor.logoImage !== 'undefined' && vendor.logoImage != null) {
                            vendor.loadLogoImage = RequestService.loadURL(vendor.logoImage);
                        } else {
                            vendor.loadLogoImage = $scope.gambarDefault;
                        }                                                
                    });
                    
                    //$scope.checkSegmentasi(data);
                    
                    $scope.vendorListDTO = data;

                    $scope.angularGridOptions = {
                        gridWidth: 500,
                        refreshOnImgLoad: true
                    }
                    
                    $scope.swap($scope.stateView);
                    $scope.loading = false;
                })
                .error(function (err) {
                    $scope.loading = false;
                    console.error(err);
                });
        }

        $scope.search();
        
        /*$scope.checkSegmentasi = function(data) {
        	//console.log(data.vendorList);
        	
        	angular.forEach(data.vendorList, function(vendor) {
        		$http.get($rootScope.backendAddress + '/procurement/vendor/SegmentasiVendorServices/getSegmentasiVendorByVendorId/' + vendor.id)
                .success(function (data, status, headers, config) {
                    console.log(data);
                })
        	})
        	
        }*/
        
        $scope.angularGridOptions = {
            gridWidth: 500,
            scrollContainer: '#prodwrap',
            refreshOnImgLoad: true
        }
        
        $scope.stateView = 'list';
        $scope.swap = function (div) {
            if (div == 'list') {
                $('#grid-view').removeClass('selected');
                $('#list-view').addClass('selected');
                /*
                $('.product-layout').removeClass('product-grid');
                $('.product-layout').addClass('product-list');*/
                /*$scope.angularGridOptions = {
                    gridWidth: 500
                } */
                $scope.showList = true;
            } else if (div == 'grid'){
                $('#grid-view').addClass('selected');
                $('#list-view').removeClass('selected');
                /*
                $('.product-layout').removeClass('product-list');
                $('.product-layout').addClass('product-grid');*/
                /* $scope.angularGridOptions = {
                    gridWidth: 250
                } */
                $scope.showList = false;
            }
            $scope.stateView = div;
        }

        $scope.pageChanged = function () {
            $log.info('Page changed to: ' + vm.pagingHalamanKe);
            $scope.search();
        };

        $scope.searchByBidangUsaha = function (bidangUsaha) {
            vm.sbText = bidangUsaha.nama;
            vm.bidangUsahaId = bidangUsaha.id;
            vm.subBidangUsahaId = 0;
            $scope.search();
        }

        $scope.searchBySubBidangUsaha = function (bidangUsaha) {
            vm.sbText = bidangUsaha.nama;
            vm.bidangUsahaId = 0;
            vm.subBidangUsahaId = bidangUsaha.id;
            $scope.search();
        }

        $scope.searchTxtVendorAndProductCategoryName = function () {
            $scope.search();
        }

        $scope.searchTxtLocation = function () {
            $scope.search();
        }

        $scope.searchShortBy = function () {
            $scope.search();
        }


        $scope.clearSbText = function () {
            vm.sbText = null;
            vm.bidangUsahaId = 0;
            vm.subBidangUsahaId = 0;
            $scope.search();
        }

        $scope.clearRatings = function () {
            vm.ratings = null;
            $scope.search();
        }

        $scope.pagingSetMaxShow = function (n) {
            $log.info("Set maxBaris: " + n);
            vm.pagingMaxBaris = n;
            $scope.search();
        }
        
         $scope.mailTo = function (email) {
        	var mailmodalinstance = $modal.open({
    			templateUrl: '/mail.compose.list.html',
    			controller: 'mailComposeListModalController',
    			resolve: {
    				email: function () {
    					return email;
    				}
    			}
    		});
//        	$state.go('app.promise.mailbox', {email: email});
        }
         

}]);


  //Tambah kirim email ke vendor
angular.module('naut')
.controller('mailComposeListModalController', function ($rootScope, $scope, $modalInstance, email, RequestService, ModalService) {
	$scope.emailTujuan = email;
	$scope.emailIsiError = false;
	$scope.mailCC = false;
    $scope.emailIsi = "<br/><p></p><p></p><br/><p>Regards,</p>"+
        "<p><br/>"+ RequestService.getUserLogin().user.namaPengguna +" <br/>"+
        "<em><u>"+ RequestService.getUserLogin().user.email +"</u></em></p>";
	
	$scope.btnKirim = function () {
		if ($scope.emailIsi == undefined || $scope.emailIsi == "") {
			$scope.emailIsiError = true;
			document.getElementsByName("emailIsi")[0].focus();
		}
		
		if (!$scope.emailIsiError) {
			var params = {
					emailTujuan: $scope.emailTujuan,
					emailSubject: $scope.emailSubject,
					emailIsi: $scope.emailIsi,
					emailFrom: RequestService.getUserLogin().user.email
			}
			
			if($scope.emailCC) {
				params.emailCC = $scope.emailCC;
			}
			
			RequestService.doPOST('/procurement/catalog/CatalogServices/sendingEmail', params)
			.then(function (data) {
				console.info(data);
				if(data.statusText == "OK") {
					ModalService.showModalInformation('Email dalam pengiriman');
	 				$scope.btnBatal();
				} else {
					ModalService.showModalInformation('Gagal mengirim Email, alamat Tujuan SALAH!');
				}
			});
		}
	}
	
	$scope.btnBatal = function () {
		$modalInstance.dismiss('cancel');
	}
});