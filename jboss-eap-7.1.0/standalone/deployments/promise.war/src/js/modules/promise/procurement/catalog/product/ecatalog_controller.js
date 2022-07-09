'use strict';

/* Controllers */

angular
    .module('naut')
    .controller('ecatalogCtrl', ['$scope', 'RequestService', '$state', 'angularGridInstance', 'ROLE_USER', '$timeout', '$rootScope', function ($scope, RequestService, $state, angularGridInstance, ROLE_USER, $timeout, $rootScope) {

        var vm = this;
        $scope.showCartButton = true;
        
        $scope.gridViewOpt = {
            gridWidth: 250,
            scrollContainer: '#prodwrap',
            refreshOnImgLoad: true
        }

        // check if chartList is undefined
        // then set is an empty array
        if(typeof $rootScope.cartList ==='undefined') {
            $rootScope.cartList = [];
        }

        $scope.showList = true;
        
        /* untuk flag compare catalog */
        $rootScope.catalogFront = 0;

    	$scope.gambarDefault = './app/img/no_image.png';
        
        //Get Role User
        RequestService.getRoleUsers(RequestService.getUserLogin().user.id).then(function (data) {
            $scope.roleUser = data;
        });
        
        if ($rootScope.cartList.length > 0) {
        	$rootScope.cartShow = true;
        }

        $scope.lihat = false;
        $scope.okForECatalog = false;
        
        if($rootScope.catalogFilter != null || $rootScope.catalogFilter != undefined) {
        	$scope.catalogVendor = $rootScope.catalogFilter;
        } else {
        	$scope.catalogVendor = {
                pageSize: 10,
                currentPage: 1
            };
        }
        
        var thisRoleUser = RequestService.getRoleUser().nama;
        if (thisRoleUser == ROLE_USER['SATU'] || thisRoleUser == ROLE_USER['DUA']) { // Lihat Constant.js
            $scope.okForECatalog = true;
        }
        
        RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/getRole')
        .then(function (data) {
            $scope.role = data.role;
            if($scope.role != "PRC"){
            	$scope.showCartButton = false;
            }
 
        });
       
        //untuk menampilkan tombol cart
       /* $scope.isCartOk = false;
        if (thisRoleUser == ROLE_USER['SATU'] || thisRoleUser == ROLE_USER['DUA']) {
        	$scope.isCartOk = true;
        }*///sepertinya tidak di pake
        $scope.isCartOk = true;    

        /*function attSearchInitAll(dataCategory) {
       
            if (dataCategory != undefined) {
                RequestService.doPOSTJSON('/procurement/catalog/AttributeGroupServices/findByCategory', $scope.catalogVendor)
                    .then(function (data) {
                        if (data.length > 0) {
                            $scope.attGrpList = data
                        }
                    })
            } else {
                RequestService.doGET('/procurement/catalog/AttributeGroupServices/findAll')
                    .then(function (data) {
                        if (data.length > 0) {
                            $scope.attGrpList = data
                        }
                    });
            }

        }*/

        RequestService.doPOSTJSON('/procurement/catalog/CategoryServices/findAllWithTree', {})
            .then(function (data) {
                $scope.categoryList = data;
            });

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
            } else {
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

        $scope.resetData = function (isSearch) {
            $scope.loading = true;
            $scope.attGrpList = [];
            $scope.catalogList = [];
            //console.info($scope.catalogVendor);
            RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findECatalog', $scope.catalogVendor)
                .then(function (data) {
                    $scope.catalogList = data.catalogList;
                    $scope.totalList = data.totalList;
                    $scope.showNoItem = false;
                    if($scope.totalList == 0) {
                    	$scope.showNoItem = true;
                    }
                    if($scope.totalList == 1 || $scope.totalList == 0) {
                    	$scope.entries = 'entry';
                    } else {
                    	$scope.entries = 'entries';
                    }
                    
                    if($scope.catalogVendor.pageSize == 10) {
                    	$scope.jumlahPertama = ($scope.catalogVendor.pageSize * $scope.catalogVendor.currentPage) - 9;
                    }
                    else if($scope.catalogVendor.pageSize == 20) {
                    	$scope.jumlahPertama = ($scope.catalogVendor.pageSize * $scope.catalogVendor.currentPage) - 19;
                    }
                    else if($scope.catalogVendor.pageSize == 30) {
                    	$scope.jumlahPertama = ($scope.catalogVendor.pageSize * $scope.catalogVendor.currentPage) - 29;
                    }
                    else if($scope.catalogVendor.pageSize == 50) {
                    	$scope.jumlahPertama = ($scope.catalogVendor.pageSize * $scope.catalogVendor.currentPage) - 49;
                    }
  
                    $scope.totalPage = Math.ceil($scope.totalList / $scope.catalogVendor.pageSize);
                    
                    $scope.jumlahTerakhir = $scope.catalogVendor.pageSize * $scope.catalogVendor.currentPage;;
                    var lengthIndex = 0;
                    if($scope.catalogVendor.currentPage == $scope.totalPage) {
                    	$scope.totalPagination = 'Showing ' + $scope.jumlahPertama + ' to ' + $scope.totalList + ' of ' + $scope.totalList + ' ' + $scope.entries;
                    	lengthIndex = $scope.totalList;
                    } else {
                    	$scope.totalPagination = 'Showing ' + $scope.jumlahPertama + ' to ' + $scope.jumlahTerakhir + ' of ' + $scope.totalList + ' ' + $scope.entries;
                    	lengthIndex = $scope.jumlahTerakhir;
                    }
                    var tampCtalogList =[];
                    if($scope.catalogList.length > 0){
                    	for(var index =($scope.jumlahPertama-1); index < lengthIndex; index++){
                    		tampCtalogList.push($scope.catalogList[index]);
                    	}                    	
                    }
                    $scope.catalogList = tampCtalogList;
                    angular.forEach($scope.catalogList, function (catalog, catalogIndex) {
                        if (typeof catalog.catalogImageList === 'undefined' || catalog.catalogImageList == null) {
                            $scope.catalogList[catalogIndex].catalogImageList = [
                                {
                                    imagesRealName: '',
                                    loadFile: $scope.gambarDefault
                                },
                                {
                                    imagesRealName: '',
                                    loadFile: $scope.gambarDefault
                                }
                            ];
                        } else {
                            //untuk menampilkan katalog butuh 2 image
                            if (catalog.catalogImageList.length == 0) {
                                $scope.catalogList[catalogIndex].catalogImageList = [
                                    {
                                        imagesRealName: '',
                                        loadFile: $scope.gambarDefault
                                    },
                                    {
                                        imagesRealName: '',
                                        loadFile: $scope.gambarDefault
                                    }
                                ];
                            } else if (catalog.catalogImageList.length == 1) {
                                
                            	if($scope.catalogList[catalogIndex].catalogImageList[0].imagesRealName == null) {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = catalog.catalogImageList[0].imagesFileName;
                            	} else {
                            		$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                            	}
                            	
                                /*$scope.catalogList[catalogIndex].catalogImageList[1] = {
                                    imagesRealName: '',
                                    loadFile: $scope.gambarDefault
                                };
                                
                                var temp = $scope.catalogList[catalogIndex].catalogImageList[1];
                                $scope.catalogList[catalogIndex].catalogImageList[1] = $scope.catalogList[catalogIndex].catalogImageList[0];
                                $scope.catalogList[catalogIndex].catalogImageList[0] = temp;*/

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
                            	
                                //$scope.catalogList[catalogIndex].catalogImageList[0].loadFile = RequestService.loadURL(catalog.catalogImageList[0].imagesRealName) + '?_ts=' + new Date().getTime();
                                //$scope.catalogList[catalogIndex].catalogImageList[1].loadFile = RequestService.loadURL(catalog.catalogImageList[1].imagesRealName) + '?_ts=' + new Date().getTime();
                            }
                        }
                        //console.log("cek image : " + JSON.stringify(catalog.catalogImageList));
                    });

                    $scope.angularGridOptions = {
                        gridWidth: 500,
                        refreshOnImgLoad: true
                    }
                    $scope.swap($scope.stateView);
                    
                    $rootScope.catalogFilter = null;

                    $scope.loading = false;

                }, function (err) {
                	$scope.loading = false;
                    console.log(err);
                });

            //attSearchInitAll($scope.catalogVendor.category);
        }
        $timeout(function () {
            $scope.resetData();
        }, 500);

        $scope.changeCategory = function (category) {
             
            if (typeof category !== 'undefined' || category != null) {
                vm.sbText = category.description;
            } else {
                vm.sbText = null;
            }
            /*$scope.catalogVendor.category = category;
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;*/
            
            if (category !== undefined) {
            	RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findCategoryWithTree', category)
                .then(function (data) {
                    $scope.categoryTreeList = data;
                    $scope.lastCategory = $scope.categoryTreeList[$scope.categoryTreeList.length - 1];
                    
                    $rootScope.categoryTreeActive = true;
                    $rootScope.categoryTreeList = data;

                    $scope.catalogVendor.category =  $scope.lastCategory;
                    $scope.catalogVendor.pageSize = 10;
                    $scope.catalogVendor.currentPage = 1;
                    
                    $scope.resetData();
                    
                });
            	
            } else {
            	$scope.categoryTreeList = null;
            	
            	$scope.resetData();
            }
            

        }
        
        $scope.changeAttributeGroup = function () {
            $scope.catalogVendor.attributeGroup = $scope.dataAttGrp;
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            $scope.resetData();
        }

        $scope.search = function () {
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            $scope.resetData();
        }
        
        $scope.resetFilter = function () {
            $scope.catalogVendor = {
            		currentPage: 1,
            		pageSize: 10
            }
            vm.sbText = null;
            $scope.resetData();
        }


        $scope.checkECatalog = function () {
            if ($scope.catalogVendor.eCatalog == true) {
                $scope.catalogVendor.userId = RequestService.getUserLogin().user.id;
                $('#prodwrap').addClass("auto-h");
            } else {
                $scope.catalogVendor.userId = undefined;
                $('#prodwrap').removeClass("auto-h");
            }
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;

            $scope.resetData();
        }

        $scope.clickCurrentPage = function (page, pageSize) {
            $scope.catalogVendor.pageSize = pageSize;
            $scope.catalogVendor.currentPage = page;
            $scope.resetData();
        };

        $scope.pageSizeChanged = function (jmlData) {
            $scope.catalogVendor.currentPage = 1;
            $scope.catalogVendor.pageSize = jmlData;
            $scope.resetData();
        };
        
        $scope.findVendor = function (nama) {
        	
        	vm.url = nama == "" ? '/procurement/vendor/vendorServices/getVendorListCatalogByNama/null':'/procurement/vendor/vendorServices/getVendorListCatalogByNama/'+nama;
        	
			RequestService.doGET(vm.url)
				.then(function success(data) {
					vm.vendorList = data;
					vm.loading = false;
				}, function error(response) { 
					RequestService.informInternalError();
        			vm.loading = false;
			});
		}
        
        $scope.findProductCode = function (productCode) {
            var vCatalog = {};
            vCatalog.kodeProduk = productCode + "%";
            RequestService.doPOSTJSON('/procurement/catalog/CategoryServices/findAllWithTree', vCatalog)
                .then(function (data) {
                    $scope.catalogByProductCodeList = data;
                });
        }

        $scope.cariAttGrp = function (data) {
            $scope.dataAttGrp = [];
            $scope.lihat = true;

            angular.forEach($scope.attGrpList, function (attGrp) {
                if (attGrp.id == data) {
                    $scope.dataAttGrp = attGrp;
                }
            });
        }
        
        $scope.viewProductDetail = function (catalog) {
        	$rootScope.catalogFilter = $scope.catalogVendor
        	
        	$state.go('app.promise.procurement-ecatalog-Detail', {
        		dataCatalog : catalog,
        	});
        }
        

}]);