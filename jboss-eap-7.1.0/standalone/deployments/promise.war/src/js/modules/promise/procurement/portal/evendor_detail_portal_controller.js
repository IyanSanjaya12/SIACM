'use strict';

/* Controllers */

angular
    .module('naut')
    .controller('evendorPortalDetailCtrl', ['$scope', 'RequestService', '$state', '$stateParams', '$modal', '$log', '$q','$rootScope', function ($scope, RequestService, $state, $stateParams, $modal, $log, $q, $rootScope) {

        $scope.pagingMaxBaris = 5;
        $scope.pagingTotalBaris = 0;
        $scope.pagingHalamanKe = 1;

        $scope.defaultImgVendor = './app/img/default_vendor.png';
        $scope.defaultImgBanner = './app/img/default_banner.jpg';
        $scope.defaultImgProduct = './app/img/default_product.png';

        if ($stateParams.dataVendor != undefined) {
            var vendor = $stateParams.dataVendor;
        }

        var vendorProfileList = function (id) {
            RequestService.doGET('/procurement/vendor/VendorProfileServices/getVendorProfileByVendorId/' + id)
                .then(function (dataVP) {

                    RequestService.doGET('/procurement/master/KontrakServices/getKontrakListByVendorId/' + id)
                        .then(function (data) {

                            $scope.vendorProfile = dataVP[0];
                            $scope.vendorProfile.jumlahKontrak = 0;

                            //banner
                            if ($scope.vendorProfile.vendor.backgroundImage != null && typeof $scope.vendorProfile.vendor.backgroundImage !== 'undefined') {
                                if ($scope.validateFileImage($scope.vendorProfile.vendor.backgroundImage)) {
                                    $scope.vendorProfile.vendor.loadFile = RequestService.loadURL($scope.vendorProfile.vendor.backgroundImage);
                                } else {
                                    $scope.vendorProfile.vendor.loadFile = $scope.defaultImgBanner;
                                }
                            } else {
                                $scope.vendorProfile.vendor.loadFile = $scope.defaultImgBanner;
                            }

                            //vendor
                            if ($scope.vendorProfile.vendor.logoImage != null && typeof $scope.vendorProfile.vendor.loadFileLogo !== 'undefined') {
                                $scope.vendorProfile.vendor.loadFileLogo = RequestService.loadURL($scope.vendorProfile.vendor.logoImage);
                            } else {
                                $scope.vendorProfile.vendor.loadFileLogo = $scope.defaultImgVendor;
                            }

                            if (data.length > 0) {
                                $scope.vendorProfile.jumlahKontrak = data.length + 1;
                            }
                        });
                });
        }

        var catalogList = function (id) {
            var catalogVendor = {
                pageSize: $scope.pagingMaxBaris,
                currentPage: $scope.pagingHalamanKe,
                vendor: {
                    id: id
                }
            };

            RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findECatalogVendorById', catalogVendor)
                .then(function (data) {
                    $scope.catalogList = data.catalogList;
                    $scope.totalList = data.totalList;
                    $scope.panjangData = 4;

                    angular.forEach($scope.catalogList, function (catalog, catalogIndex) {
                    	
                        if (typeof catalog.catalogImageList === 'undefined' || catalog.catalogImageList == null) {
                            $scope.catalogList[catalogIndex].catalogImageList = [
                                {
                                    imagesRealName: '',
                                    loadFile: $scope.defaultImgProduct
                                },
                                {
                                    imagesRealName: '',
                                    loadFile: $scope.defaultImgProduct
                                }
                            ];
                        } else {
                            //untuk menampilkan katalog butuh 2 image
                            if (catalog.catalogImageList.length == 0) {
                                $scope.catalogList[catalogIndex].catalogImageList = [
                                    {
                                        imagesRealName: '',
                                        loadFile: $scope.defaultImgProduct
                                    },
                                    {
                                        imagesRealName: '',
                                        loadFile: $scope.defaultImgProduct
                                    }
                                ];
                            } else if (catalog.catalogImageList.length == 1) {
                                //jika gambar cuman 1
                                /*$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                                $scope.catalogList[catalogIndex].catalogImageList[1] = {
                                    imagesRealName: '',
                                    loadFile: $scope.defaultImgProduct
                                };
                                var temp = $scope.catalogList[catalogIndex].catalogImageList[1];
                                $scope.catalogList[catalogIndex].catalogImageList[1] = $scope.catalogList[catalogIndex].catalogImageList[0];
                                $scope.catalogList[catalogIndex].catalogImageList[0] = temp;*/
                                
                                if($scope.catalogList[catalogIndex].catalogImageList[0].imagesRealName == null) {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = catalog.catalogImageList[0].imagesFileName;
                            	} else {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                            	}
                            	
                                $scope.catalogList[catalogIndex].catalogImageList[1] = {
                                    imagesRealName: '',
                                    loadFile: $scope.gambarDefault
                                };
                                
                                var temp = $scope.catalogList[catalogIndex].catalogImageList[1];
                                $scope.catalogList[catalogIndex].catalogImageList[1] = $scope.catalogList[catalogIndex].catalogImageList[0];
                                $scope.catalogList[catalogIndex].catalogImageList[0] = temp;

                            } else {
                            	
                            	if($scope.catalogList[catalogIndex].catalogImageList[0].imagesRealName == null) {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = catalog.catalogImageList[0].imagesFileName;
                            	} else {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                            	}
                            	
                            	if($scope.catalogList[catalogIndex].catalogImageList[1].imagesRealName == null) {
                            		$scope.catalogList[catalogIndex].catalogImageList[1].loadFile = catalog.catalogImageList[0].imagesFileName;
                            	} else {
                            		$scope.catalogList[catalogIndex].catalogImageList[1].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                            	}
                            	
                                /*$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                                $scope.catalogList[catalogIndex].catalogImageList[1].loadFile = RequestService.loadURL(catalog.catalogImageList[1].imagesRealName) + '?_ts=' + new Date().getTime();*/
                            }
                        }

                    });
                    $scope.angularGridOptions = {
                        gridWidth: 500,
                        refreshOnImgLoad: true
                    }

                    if ($scope.catalogList.length > 0) {
                        if ($scope.catalogList.length < 4) {
                            $scope.panjangData = $scope.catalogList.length;
                        }
                    }

                });
        }

        var vendorBySubBidangList = function (id) {
            var params = {
                vendorId: id,
                maxBaris: 5,
                halamanKe: 1
            }
            RequestService.doPOST('/procurement/vendor/SegmentasiVendorServices/getListVendorBySegmentasi', params)
                .then(function (dataVS) {
                    var data = dataVS.data;
                    if (data != undefined) {
                        $scope.pagingTotalBaris = data.totalRow;

                        angular.forEach(data.vendorList, function (vendor) {
                            vendor.performanceAvgFloor = window.Math.floor(vendor.performanceAvg);
                            if (typeof vendor.logoImage !== 'undefined' && vendor.logoImage != null) {
                                vendor.loadLogoImage = RequestService.loadURL(vendor.logoImage);
                            } else {
                                vendor.loadLogoImage = $scope.defaultImgVendor;
                            }
                        });

                        $scope.vendorListDTO = data;

                    }
                });
        }

        var initData = function () {
        	if(vendor == undefined || vendor == null) {
        		vendorProfileList($rootScope.dataVendor.vendor.id);
                catalogList($rootScope.dataVendor.vendor.id);
                vendorBySubBidangList($rootScope.dataVendor.vendor.id);
                
        	} else {
                vendorProfileList(vendor.id);
                catalogList(vendor.id);
                vendorBySubBidangList(vendor.id);
        	}
        }
        initData();

        $scope.pagingSetMaxShow = function (n) {
            $log.info("Set maxBaris: " + n);
            $scope.pagingMaxBaris = n;
            vendorBySubBidangList(vendor.id);
        }

        $scope.pageChanged = function () {
            $log.info('Page changed to: ' + $scope.pagingHalamanKe);
            vendorBySubBidangList(vendor.id);
        };
        
        $scope.validateFileImage = function (imgFile) {
            var allowedExtension = ['jpeg', 'jpg', 'png', 'gif'];
            var fileExtension = imgFile.split('.').pop().toLowerCase();
            var isValidFile = false;
            for (var index in allowedExtension) {
                if (fileExtension === allowedExtension[index]) {
                    return true;
                    break;
                }
            }
            if (!isValidFile) {
                return false;
            }
        }
        
        $scope.mailTo = function (email) {
        	var mailmodalinstance = $modal.open({
    			templateUrl: '/mail.compose.html',
    			controller: 'mailComposeModalController',
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
.controller('mailComposeModalController', function ($rootScope, $scope, $modalInstance, email, RequestService, ModalService) {
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