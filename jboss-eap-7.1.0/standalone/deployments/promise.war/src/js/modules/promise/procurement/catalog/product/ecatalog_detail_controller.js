'use strict';

/* Controllers */

angular
    .module('naut')
    .controller('ecatalogDetailCtrl', ['$scope', 'RequestService', '$state', '$stateParams', 'ModalService', 'ROLE_USER', '$modal', '$rootScope', function ($scope, RequestService, $state, $stateParams, ModalService, ROLE_USER, $modal, $rootScope) {

    	var vm = this;
        if ($stateParams.dataCatalog != undefined) {
            $scope.catalog = $stateParams.dataCatalog;
            $rootScope.dataVendor = $stateParams.dataCatalog;
            var thisRoleUser = RequestService.getRoleUser().nama;
            
            $scope.isVendor =  false;
            if(thisRoleUser == 'VENDOR') {
            	$scope.isVendor = true;
            }
            
            $scope.commentSize = 0;
            $scope.jumlahTerjual = 0;
            $scope.okForComment = false;
            if (thisRoleUser == ROLE_USER['SATU'] || thisRoleUser == ROLE_USER['DUA']) { // Lihat Constant.js
                $scope.okForComment = true;
            }
        }
        
        $scope.backToCatalogList = function () {
    		$state.go('app.promise.procurement-ecatalog2');
    	}
        
        vm.showNoData = true;
        
        $scope.maxSla = 0;
        $scope.minSla = 0;
        $scope.cariMaxMinSla=function(){
          if($scope.catalog.catalogFeeList!=undefined){
            $scope.maxSla = $scope.catalog.catalogFeeList[0].slaDeliveryTime;
            $scope.minSla = $scope.catalog.catalogFeeList[0].slaDeliveryTime;
            angular.forEach($scope.catalog.catalogFeeList,function(catalog){
              if($scope.maxSla < catalog.slaDeliveryTime){
                $scope.maxSla = catalog.slaDeliveryTime;
              }
              if($scope.minSla > catalog.slaDeliveryTime){
                $scope.minSla = catalog.slaDeliveryTime;
              }
            });
            
          }
        }
        $scope.cariMaxMinSla();
        
        $scope.hitungTransaction=function(){
        	 var params = {
                     catalogId: $scope.catalog.id,
                     itemId: $scope.catalog.item.id
                 }
        	 RequestService.doPOST('/procurement/purchaseRequestServices/getTotalTransaction', params)
             .then(function (data) {
                 var transaksi = data.data;
                 if (transaksi != undefined) {
                     $scope.prCount = transaksi;
                 }
             });
        }
        $scope.hitungTransaction();
        
        $scope.hitungProductSold=function(){
       	 var params = {
                    catalogId: $scope.catalog.id,
                    itemId: $scope.catalog.item.id
                }
       	 RequestService.doPOST('/procurement/purchaseRequestItemServices/getTotalProductSold', params)
            .then(function (data) {
                var productSold = data.data;
                if (productSold != undefined) {
                    $scope.prItemCount = productSold;
                }
            });
       }
        $scope.hitungProductSold();
        
        $scope.dataDiskon=function(){
          	 var params = {
                       catalogId: $scope.catalog.id,
                   }
          	 RequestService.doPOST('/procurment/catalog/catalog-bulk-price/getDataDiskon', params)
               .then(function (data) {
                   var dataDiskon = data.data;
                   if (dataDiskon != undefined) {
                       $scope.dataDiskonList = dataDiskon;
                       var harga = $scope.catalog.harga;
                   }
               });
          }
           $scope.dataDiskon();
        
        var emptyComment = function () {
            $scope.dataRating = {
                rating: 5,
                ratingComment: '',
                catalogId: $scope.catalog.id,
                userId: RequestService.getUserLogin().user.id
            }
        }

        $scope.hitungLamaBerdiri = function (tgl) {
            var date = new Date(tgl);
            var ageDifMs = Date.now() - date.getTime();
            var ageDate = new Date(ageDifMs);
            return Math.abs(ageDate.getUTCFullYear() - 1970);
        }

        $scope.catalogCommentList = [];
        var catalogCommentList = function () {
            var params = {
                catalogId: $scope.catalog.id
            }
            RequestService.doPOST('/procurement/catalog/CatalogServices/findCatalogCommentByCatalog', params)
                .then(function (data) {
                    var dataCC = data.data;
                    if (dataCC != undefined) {
                        $scope.catalogCommentList = dataCC;
                        $scope.commentSize = dataCC.length;
                    }
                });
        }

        $scope.imagesForGallery = [];
        $scope.setApproot = function (appRoot) {
            //only change when needed.
            if ($scope.approot && appRoot === $scope.approot) {
                return;
            }
            $scope.approot = appRoot;
            if ($scope.catalog.catalogImageList != undefined && $scope.catalog.catalogImageList[0] != undefined) {
            	var imagesFile;
            	if($scope.catalog.catalogImageList[0].imagesRealName == null) {
            		imagesFile = $scope.catalog.catalogImageList[0].imagesFileName;
            	} else {
            		imagesFile = RequestService.loadURL($scope.catalog.catalogImageList[0].imagesRealName);
            	}

                var dataImage = {
                    thumb: imagesFile,
                    small: imagesFile,
                    large: imagesFile
                };
                $scope.imagesForGallery.push(dataImage);
            }
            if ($scope.catalog.catalogImageList != undefined && $scope.catalog.catalogImageList[1] != undefined) {
	
            	var imagesFile1;
            	if($scope.catalog.catalogImageList[1].imagesRealName == null) {
            		imagesFile1 = $scope.catalog.catalogImageList[1].imagesFileName;
            	} else {
            		imagesFile1 = RequestService.loadURL($scope.catalog.catalogImageList[1].imagesRealName);
            	}
            	
                var dataImage = {
                    thumb: imagesFile1,
                    small: imagesFile1,
                    large: imagesFile1
                };
                $scope.imagesForGallery.push(dataImage);
            }
            if ($scope.catalog.catalogImageList != undefined && $scope.catalog.catalogImageList[2] != undefined) {
            	
            	var imagesFile2;
            	if($scope.catalog.catalogImageList[2].imagesRealName == null) {
            		imagesFile2 = $scope.catalog.catalogImageList[2].imagesFileName;
            	} else {
            		imagesFile2 = RequestService.loadURL($scope.catalog.catalogImageList[2].imagesRealName);
            	}
            	
                var dataImage = {
                    thumb: imagesFile2,
                    small: imagesFile2,
                    large: imagesFile2
                };
                $scope.imagesForGallery.push(dataImage);
            }
            //console.log($scope.imagesForGallery);

            angular.forEach($scope.catalog.catalogKontrakList, function (ck) {
                $scope.jumlahTerjual = +ck.stock;
            })

            $scope.zoomModelGallery01 = $scope.imagesForGallery[0];
            catalogList($scope.catalog.vendor.id);
            catalogCommentList();
            emptyComment();
        };


        $scope.defaultImgProduct = './app/img/default_product.png';
        var catalogList = function (id) {
            var catalogVendor = {
                pageSize: 10,
                currentPage: 1,
                attributeGroup: $scope.catalog.attributeGroup
            };

            RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findECatalog', catalogVendor)
                .then(function (data) {
                    $scope.catalogList = [];
                    $scope.panjangData = 5;
              
                    angular.forEach(data.catalogList, function (hasil) {
                        if (hasil.id != $scope.catalog.id) {
                            $scope.catalogList.push(hasil);
                        }
                    })
                    $scope.totalList = data.totalList;
                    
					if($scope.catalogList.length > 0){
						vm.showNoData = false;
					 }
                    
                    angular.forEach($scope.catalogList, function (catalog) {
                        if (typeof catalog.catalogImageList !== 'undefined' && catalog.catalogImageList != null) {
                            if (typeof catalog.catalogImageList[0] !== 'undefined' && catalog.catalogImageList[0] != null) {
                            	if(catalog.catalogImageList[0].imagesRealName == null || catalog.catalogImageList[0].imagesRealName == 'undefined') {
                            		 catalog.catalogImageList[0].loadFile = catalog.catalogImageList[0].imagesFileName;
                            	} else {
                            		 catalog.catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                            	}

                            } else {
                                catalog.catalogImageList[0].loadFile = $scope.defaultImgProduct;
                            }
                        } else {
                            catalog.catalogImageList = [
                                {
                                    loadFile : $scope.defaultImgProduct
                                }
                            ];
                        }
                    });
                    $scope.angularGridOptions = {
                        gridWidth: 500,
                        refreshOnImgLoad: true
                    }

                    if ($scope.catalogList.length > 0) {
                        if ($scope.catalogList.length < 5) {
                            $scope.panjangData = $scope.catalogList.length;
                        }
                    }
                });
        }

        //default
        $scope.setApproot('');

        $scope.zoomOptions = {
            lensShape: 'round',
            cursor: 'crosshair',
            galleryActiveClass: "active",
            loadingIcon: true
        };

        $scope.setActiveImageInGallery = function (prop, img) {
            $scope[prop] = img;
        };

        $scope.saveRating = function (dataRating) {
            RequestService.doPOST('/procurement/catalog/CatalogServices/simpanCatalogComment', dataRating)
                .then(function (dataCC) {
                    if (dataCC != undefined) {
                        RequestService.doPOST('/procurement/catalog/CatalogServices/simpanRating', dataRating)
                            .then(function (data) {
                                if (data.data != undefined) {
                                    var catalog = data.data;
                                    ModalService.showModalInformation('Rating telah tersimpan dengan Baik');
                                    setelahSimpan(catalog);
                                }
                            });
                    }
                });
        }

        $scope.tulisComment = function () {
            $scope.writeComment = true;
        }

        var setelahSimpan = function (catalog) {
            if (catalog.catalogImageList != undefined && catalog.catalogImageList[0] != undefined) {
                catalog.catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
            }
            if (catalog.catalogImageList != undefined && catalog.catalogImageList[1] != undefined) {
                catalog.catalogImageList[1].loadFile = RequestService.loadURL(catalog.catalogImageList[1].imagesRealName) + '?_ts=' + new Date().getTime();
            }

            $state.go('app.promise.procurement-ecatalog-Detail', {
                dataCatalog: catalog
            });
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
            //    	$state.go('app.promise.mailbox', {email: email});
        }
}]);

angular.module('naut')
    .controller('mailComposeModalController', function ($rootScope, $scope, $modalInstance, email, RequestService, ModalService) {
        $scope.emailTujuan = email;
        $scope.emailIsiError = false;
        $scope.mailCC = false;
        $scope.emailIsi = "<p></p><p></p><br/><p>Regards,</p>" +
            "<p><br/>" + RequestService.getUserLogin().user.namaPengguna + " <br/>" +
            "<em><u>" + RequestService.getUserLogin().user.email + "</u></em></p>";

        $scope.btnKirim = function () {
            if ($scope.emailIsi == undefined || $scope.emailIsi == "") {
                $scope.emailIsiError = true;
                document.getElementsByName("emailIsi")[0].focus();
            }if($scope.emailSubject == undefined || $scope.emailSubject == ""){
            	$scope.emailSubjectError = true;
            }

            if (!$scope.emailIsiError && !$scope.emailSubjectError) {
                var params = {
                    emailTujuan: $scope.emailTujuan,
                    emailSubject: $scope.emailSubject,
                    emailIsi: $scope.emailIsi,
                    emailFrom: RequestService.getUserLogin().user.email
                }

                if ($scope.emailCC) {
                    params.emailCC = $scope.emailCC;
                }

                RequestService.doPOST('/procurement/catalog/CatalogServices/sendingEmail', params)
                    .then(function (data) {
                        console.info(data);
                        if (data.statusText == "OK") {
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