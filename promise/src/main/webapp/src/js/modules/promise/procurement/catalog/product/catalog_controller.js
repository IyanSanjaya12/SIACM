/* Controllers */

angular
    .module('naut')
    .controller('catalogPortalCtrl', ['$scope', 'RequestService', '$state', 'ROLE_USER', '$timeout','$rootScope', '$stateParams', function ($scope, RequestService, $state, ROLE_USER, $timeout, $rootScope, $stateParams ) {

        var vm = this;
        $scope.gridViewOpt = {
            gridWidth: 250,
            scrollContainer: '#prodwrap',
            refreshOnImgLoad: true
        }

        $scope.showList = true;
        $scope.gambarDefault = './app/img/no_image.png';
        
        /* untuk flag compare catalog */
        $rootScope.catalogFront = 1;


        //Get Role User
        /*RequestService.doGET('/catalog/AttributeGroupPortalServices/findAll')
        .then(function (data) {
            if (data.length > 0) {
                $scope.attGrpList = data
            }
        });*/
        /*RequestService.getRoleUsers(RequestService.getUserLogin().user.id).then(function (data) {
            $scope.roleUser = data;
        });*/

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
       /* if (thisRoleUser == ROLE_USER['SATU'] || thisRoleUser == ROLE_USER['DUA']) { // Lihat Constant.js
*/            $scope.okForECatalog = true;
       // }

        //untuk menampilkan tombol cart
        $scope.isCartOk = false;
        if (thisRoleUser == ROLE_USER['SATU'] || thisRoleUser == ROLE_USER['DUA']) {
        	$scope.isCartOk = true;
        }
        
        function attSearchInitAll(dataCategory) {
            if (dataCategory != undefined) {
                RequestService.doPOSTJSON('/catalog/AttributeGroupPortalServices/findByCategory', $scope.catalogVendor)
                    .then(function (data) {
                        if (data.length > 0) {
                            $scope.attGrpList = data
                        }
                    })
            } else {
                RequestService.doGET('/catalog/AttributeGroupPortalServices/findAll')
                    .then(function (data) {
                        if (data.length > 0) {
                            $scope.attGrpList = data
                        }
                    });
            }

        }

        RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findAllWithTree', {})
            .then(function (data) {
                $scope.categoryList = data;
                $scope.category0 = data[0];
                $scope.category1 = data[1];
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
                $('.product-layout').removeClass('product-grid');
                $('.product-layout').addClass('product-list');
                /*$scope.angularGridOptions = {
                    gridWidth: 500
                } */
                $scope.showList = true;
            } else {
                $('#grid-view').addClass('selected');
                $('#list-view').removeClass('selected');
                $('.product-layout').removeClass('product-list');
                $('.product-layout').addClass('product-grid');
                /* $scope.angularGridOptions = {
                    gridWidth: 250
                } */
                $scope.showList = false;
            }
            $scope.stateView = div;
        }

        $scope.resetData = function () {
        	
        	if($rootScope.catalogFilter != null || $rootScope.catalogFilter != undefined) {
            	$rootScope.categoryTreeActive = true;
            }
        	
            $scope.loading = true;
            $scope.attGrpList = [];
            $scope.catalogList = [];
            RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findECatalog', $scope.catalogVendor)
                .then(function (data) {
                    $scope.catalogList = data.catalogList;
                    $scope.totalList = data.totalList;
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
                    
                    if($scope.catalogVendor.currentPage == $scope.totalPage) {
                    	$scope.totalPagination = 'Showing ' + $scope.jumlahPertama + ' to ' + $scope.totalList + ' of ' + $scope.totalList + ' ' + $scope.entries;
                    } else if ($scope.totalList == 0){
                    	$scope.totalPagination = 'Showing 0 of 0 ' + $scope.entries;
                    } else {
                    	$scope.totalPagination = 'Showing ' + $scope.jumlahPertama + ' to ' + $scope.jumlahTerakhir + ' of ' + $scope.totalList + ' ' + $scope.entries;
                    }
	
                    $scope.showNoItem = false;
                    if($scope.totalList == 0) {
                    	$scope.showNoItem = true;
                    }
                    
                    //$scope.vendorLogo = RequestService.loadURL($scope.catalogList.vendor.logoImage) + '?_ts=' + new Date().getTime(); 
                    angular.forEach($scope.catalogList, function (catalog, catalogIndex) {
                    	$scope.catalogList[catalogIndex].vendor.loadFile = RequestService.loadURL(catalog.vendor.logoImage) + '?_ts=' + new Date().getTime();
                    	//console.log($scope.catalogList[catalogIndex].vendor.loadFile);
                    	
                    	
                        if (typeof catalog.catalogImageList === 'undefined' || catalog.catalogImageList == null) {
                            $scope.catalogList[catalogIndex].catalogImageList = [
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
                            	if($scope.catalogList[catalogIndex].catalogImageList[0].imagesRealName == null || $scope.catalogList[catalogIndex].catalogImageList[0].imagesRealName == '') {
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
                    console.err(err);
                });
            attSearchInitAll($scope.catalogVendor.category);
        }
        
        $timeout(function () {
        	
        	if($rootScope.subcategory != null) {
        		if (typeof $rootScope.subcategory !== 'undefined' || $rootScope.subcategory != null) {
                    vm.sbText = $rootScope.subcategory.description;
                } else {
                    vm.sbText = null;
                }
        	}
        	
        	if($rootScope.subcategory != undefined || $rootScope.subcategory != null) {
        		RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findCategoryWithTree', $rootScope.subcategory)
                .then(function (data) {
                	$rootScope.subcategory = null;
                    $scope.categoryTreeList = data;
                    $scope.lastCategory = $scope.categoryTreeList[$scope.categoryTreeList.length - 1];
                    $rootScope.categoryTreeActive = true;
                    $rootScope.categoryListTree = data;
                    
                    $scope.catalogVendor.category =  $scope.lastCategory;
                    $scope.catalogVendor.pageSize = 10;
                    $scope.catalogVendor.currentPage = 1;     
                    
                    $scope.resetData();
                    
                });
        	} else {
                $scope.resetData();
        	}
            
            
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
                    $rootScope.categoryListTree = data;
                    
                    $scope.catalogVendor.category =  $scope.lastCategory;
                    $scope.catalogVendor.pageSize = 10;
                    $scope.catalogVendor.currentPage = 1;     
                    
                    $scope.resetData();
                    
                });
            	
            } else {
          
                $scope.categoryTreeList = null;
                $rootScope.categoryListTree = null;
                
                $scope.catalogVendor.category =  category;
                $scope.catalogVendor.pageSize = 10;
                $scope.catalogVendor.currentPage = 1;
                
                $scope.resetData();
                
            }
            

        }
        
       /* $scope.changeCategoryBreadcrumb = function (category) {
            if (typeof category !== 'undefined' || category != null) {
                vm.sbText = category.description;
            } else {
                vm.sbText = null;
            }
            $scope.catalogVendor.category = category;
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            
            if (category !== undefined) {
            	RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findCategoryWithTree', category)
                .then(function (data) {
                	$rootScope.categoryListTree = data;
                	$rootScope.lastCategoryTree = $rootScope.categoryListTree[$rootScope.categoryListTree.length - 1];
                    $scope.catalogVendor.category =  $rootScope.lastCategoryTree;
                    $scope.catalogVendor.pageSize = 10;
                    $scope.catalogVendor.currentPage = 1;     
                    
                    $scope.resetData();
                    
                });
            	
            } else {
          
                $scope.categoryTreeList = null;
                
                $scope.catalogVendor.category =  category;
                $scope.catalogVendor.pageSize = 10;
                $scope.catalogVendor.currentPage = 1;
                
                $scope.resetData();
                
            }
            

        }*/

        $scope.changeAttributeGroup = function () {
            $scope.catalogVendor.attributeGroup = $scope.dataAttGrp;
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            $scope.resetData();
        }

        $scope.searchTxt = function () {
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            $scope.resetData();
        }

        $scope.checkContract = function () {
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;

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

        $scope.changeSort = function () {
            $scope.catalogVendor.pageSize = 10;
            $scope.catalogVendor.currentPage = 1;
            $scope.resetData();
        };



        $scope.findProductCode = function (productCode) {
            var vCatalog = {};
            vCatalog.kodeProduk = productCode + "%";
            RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findAllWithTree', vCatalog)
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
        	
        	$state.go('appportal.productDetail', {
        		dataCatalog : catalog,
        	});
        }




}]);