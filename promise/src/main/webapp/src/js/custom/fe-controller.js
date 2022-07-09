(function() {
    
    'use strict';

    /* vendor compare controller */
    angular.module('naut').controller('rateCtrl', ['$scope','$modal','$location','$stateParams','ModalService', function($scope, $modal,$location,$stateParams,ModalService) {
       
        $scope.rate = function () {
            var modalInstance = $modal.open({
                templateUrl: 'rate.html',
                controller: 'ModalController'
            });
            modalInstance.result.then(function () {

            });
        };
        if ($stateParams.compareVendorList != undefined) {
    		$scope.vendorList = $stateParams.compareVendorList;
    		
    		$scope.removeCompareVendorInView = function(compareVendor) {
    	      	  var indexCompareVendor = $scope.vendorList.indexOf(compareVendor);
    	      	  
    	      	  if($scope.vendorList.length == 1){
    	      		  ModalService.showModalInformation('info : Anda tidak bisa menghapus vendor');
    	      		  return false;
    	      	  }else{
    	      		  $scope.vendorList.splice(indexCompareVendor, 1);
    	      	  }
    	        }
        };
        
        $scope.a = function(){
            $location.path("/app/promise/procurement/vendor");
        };
        
        
         $scope.mailTo = function (email) {
        	var mailmodalinstance = $modal.open({
    			templateUrl: '/mail.compose.compare.html',
    			controller: 'mailComposeCompareModalController',
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
.controller('mailComposeCompareModalController', function ($rootScope, $scope, $modalInstance, email, RequestService, ModalService) {
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
    
    
    angular
        .module('naut')
        .controller('ImgZoomCtrl', function ($scope, $location) {
            
        $scope.imagesForGallery = [];
        $scope.setApproot = function(appRoot) {
            //only change when needed.
            if ($scope.approot && appRoot === $scope.approot) {
                return;
            }
            $scope.approot = appRoot;
            $scope.imagesForGallery = [
                {
                    thumb: 'app/img/product/garing-garuda.jpg',
                    small: 'app/img/product/garing-garuda.jpg',
                    large: 'app/img/product/garing-garuda.jpg'
                },
                {
                    thumb: 'app/img/product/garuda-roasted-flavored.png',
                    small: 'app/img/product/garuda-roasted-flavored.png',
                    large: 'app/img/product/garuda-roasted-flavored.png'
                },
                {
                    thumb: 'app/img/product/kacang-kulit.jpg',
                    small: 'app/img/product/kacang-kulit.jpg',
                    large: 'app/img/product/kacang-kulit.jpg'
                }
            ];


            $scope.zoomModelGallery01 = $scope.imagesForGallery[0];
        };

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
    });


    angular
        .module('naut')
        .controller('ModalController', ['$scope', '$modalInstance', function($scope, $modalInstance) {

        $scope.ok = function () {
            $modalInstance.close();
    //        $modalInstance.close($scope.groupField.groupName);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

    angular
        .module('naut')
        .controller('MyRating', ['$scope', function($scope) {
            $scope.rate = 1;
            $scope.max = 5;
            $scope.isReadonly = false;

            $scope.hoveringOver = function(value) {
              $scope.overStar = value;
              $scope.percent = 100 * (value / $scope.max);
            };
    }]);


    angular
        .module('naut')
        .controller('swapViews', ['$scope', '$http', function($scope, $http) {
                
            $http({
                method:'GET',
                url:'server/company.json'
            }).success(function(data){
                $scope.pics = data;
            });
            
            $http({
                method:'GET',
                url:'server/product.json'
            }).success(function(data){
                $scope.productList = data;
            });
            
            $scope.angularGridOptions = {
                gridWidth : 500,
                scrollContainer: '#prodwrap'
            }
            $scope.searchTxt = function(){
            }


            $scope.swap = function(div) {
                if(div == 'list'){
                    $('#grid-view').removeClass('selected');
                    $('#list-view').addClass('selected');
                    $('.product-layout').removeClass('product-grid');
                    $('.product-layout').addClass('product-list');
                    $scope.angularGridOptions = {
                        gridWidth : 500
                    }
                }
                else{
                    $('#grid-view').addClass('selected');
                    $('#list-view').removeClass('selected');
                    $('.product-layout').removeClass('product-list');
                    $('.product-layout').addClass('product-grid');
                    $scope.angularGridOptions = {
                        gridWidth : 250
                    }
                }

            }


            $scope.totalItems = 64;
            $scope.currentPage = 4;

            $scope.setPage = function (pageNo) {
              $scope.currentPage = pageNo;
            };

            $scope.pageChanged = function() {
              $log.info('Page changed to: ' + $scope.currentPage);
            };

            $scope.maxSize = 5;
            $scope.bigTotalItems = 175;
            $scope.bigCurrentPage = 1;
    }]);
        
        
    angular
        .module('naut')
        .controller('GeneralCtrl', ['$scope', '$http', '$modal', function($scope, $http, $modal) {
                
            $http({
                method:'GET',
                url:'server/company.json'
            }).success(function(data){
                $scope.pics = data;
            });
            
            
            $http({
                method:'GET',
                url:'server/product.json'
            }).success(function(data){
                $scope.productList = data;
            });
            
            
            //Remote items
            
            $http({
                method: 'get',
                url:'server/country.json'
            }).success(function(posts) {
                $scope.country = posts;
            });            
            
            
            $scope.notBlackListed = function(value) {
                var blacklist = ['bad@domain.com','verybad@domain.com'];
                return blacklist.indexOf(value) === -1;
            };

            $scope.words = function(value) {
                return value && value.split(' ').length;
            };
            
            
            $scope.totalItems = 64;
            $scope.currentPage = 4;

            $scope.setPage = function (pageNo) {
              $scope.currentPage = pageNo;
            };

            $scope.pageChanged = function() {
              $log.info('Page changed to: ' + $scope.currentPage);
            };

            $scope.maxSize = 5;
            $scope.bigTotalItems = 175;
            $scope.bigCurrentPage = 1;
            
            
            $scope.openModal = function (id,size) {
                var modalInstance = $modal.open({
                    templateUrl: id,
                    controller: 'ModalController',
                    size: size
                });


                modalInstance.result.then(function () {

                });

            };
            
           
    }]);


})();