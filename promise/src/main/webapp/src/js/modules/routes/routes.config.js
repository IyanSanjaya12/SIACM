/**
 * ========================================================= Module:
 * RoutesConfig.js =========================================================
 */

(function () {
    'use strict';

    angular
        .module('naut')
        .factory('RequestInterceptor', ['$localStorage', '$rootScope', '$injector',
			function ($localStorage, $rootScope, $injector) {
                var interceptor;
                interceptor = {
                    request: function (config) {


                        if ($rootScope.userToken) {
                            config.headers['Authorization'] = $rootScope.userToken;
                        }
                        var token = $rootScope.userToken;

                        /* insertSecurityPath */

                        
						  //aktifkan hanya ketika mau menginsert aksi service path
						 
                        if( $rootScope.isInsertSecurityServices == "TRUE" ){
                        	var $http = $injector.get('$http'); if
  						  ((config.url.indexOf('insertSecurity') == -1)) {
  						  insertSecurityPath(token, $rootScope.activeMenuId,
  						 config.url, $rootScope, $http); }
                        }
                        
                        if (config.url.indexOf('views') !== -1) {
                        	config.url = config.url + '?v=' + $rootScope.aplicationVersion;
                        }
						 
                        //end insertSecurityPath

                        return config;
                    }
                };
                return interceptor;
			}
		])

    .factory('Response401Interceptor', function ($location, $q) {
        return {
            responseError: function (response) {
                if (response.status === 401) {

                    // check error response msg

                    var data = response.data

                    console.log('response data = ' + data);

                    var errorCode = data.substr(data.indexOf("PRMS-ERR"), 12);

                    $location.path('/page/errorPage/' + errorCode);

                    return $q.reject(response);
                } else {
                    return $q.reject(response);
                }
            }
        };
    })

    .config(['$httpProvider',
	    function ($httpProvider) {
            return $httpProvider.interceptors.push('RequestInterceptor', 'Response401Interceptor');
		}
	])

    .config(routesConfig);

    routesConfig.$inject = ['$locationProvider', '$stateProvider', '$urlRouterProvider', 'RouteProvider'];

    function routesConfig($locationProvider, $stateProvider, $urlRouterProvider, Route) {

        // use the HTML5 History API
        $locationProvider.html5Mode(false);

        // Default route
        // $urlRouterProvider.otherwise('/portal');
        $urlRouterProvider.otherwise('/init');
        // $urlRouterProvider.otherwise('/front/portal');

        // Application Routes States
        $stateProvider
            .state('page.errorPage', {
            url: '/errorPage/:errorCode',
            templateUrl: Route.base('promise/errorPage/errorPage.html'),
            resolve: {
                assets: Route.require('ui.select')
            }
        })
        
        // state test
        .state('test', {
            url: '/test_mamatfusion01',
            templateUrl: Route.base('test_mamatfusion01.html'),
            resolve: {
                assets: Route.require('ng-fusioncharts')
            }
        })
        .state('test2', {
            url: '/init',
            templateUrl: Route.base('test_tables.html'),
            resolve: {
                assets: Route.require('datatables')
            }
        })

        .state('page.portal', {
            url: '/portal',
            templateUrl: Route.base('/promise/procurement/portal/portal.html'),
            resolve: {
                // assets: Route.require('datatables', 'ui.select',
				// 'slimscroll')
                assets: Route.require('datatables')
            }
        })
        
        .state('page.catalog', {
            url: '/e-cagtalog',
            templateUrl: Route.base('/app/promise/procurement/e-catalog'),
            resolve: {
                // assets: Route.require('datatables', 'ui.select',
				// 'slimscroll')
                assets: Route.require('datatables')
            }
        })
        
        // Baru 2018
//        .state('front.login', {
//            url: '/login',
//            templateUrl: Route.base('login/login.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['icons', 'slimscroll'])
//                    .then(function () {
//                        return $ocLazyLoad.load(['src/js/modules/promise/procurement/portal/home_controller.js']);
//                    });
//            }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('front.login', {
            url: '/login',
            templateUrl: Route.base('login/login.html'),
            resolve: {
                assets: Route.require('icons', 'slimscroll')
            }
        })
        
//        .state('front.portal', {
//            url: '/portal',
//            templateUrl: Route.base('home/home.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['icons', 'slimscroll'])
//                    .then(function () {
//                        return $ocLazyLoad.load(['src/js/modules/promise/procurement/portal/home_controller.js']);
//                    });
//            }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('front.portal', {
            url: '/portal',
            templateUrl: Route.base('home/home.html'),
            resolve: {
                assets: Route.require('icons', 'slimscroll')
            }
        })
        
        .state('appportal', {
            url: '/appportal',
            abstract: true,
            templateUrl: Route.base('/promise/procurement/portal/app.html'),
            resolve: {
                _assets: Route.require('TouchSpin', 'icons', 'screenfull', 'toaster', 'animate')
            }
        })
        
//        .state('appportal.catalog', {
//            url: '/catalog',
//            params:{
//            	subcategory:null,
//            	catalogFilter: null
//            },
//            templateUrl: Route.base('/promise/procurement/portal/catalog.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['icons', 'slimscroll','angulargrid'])
//                    .then(function () {
//                        return $ocLazyLoad.load(['src/js/modules/promise/procurement/catalog/product/catalog_controller.js']);
//                    });
//            }],
//            data: {
//                displayName: 'Katalog'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.catalog', {
            url: '/catalog',
            templateUrl: Route.base('/promise/procurement/portal/catalog.html'),
            params:{
            	subcategory:null,
            	catalogFilter: null
            },
            resolve: {
                assets: Route.require('icons', 'slimscroll','angulargrid')
            },
            data: {
                displayName: 'Katalog'
            }
        })
        
//        .state('appportal.productDetail', {
//            url: '/product/:productName',
//            params: {
//                dataCatalog: null,
//                catalogFilter: null
//            },
//            templateUrl: Route.base('/promise/procurement/portal/product_detail.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['ez-plus', 'TouchSpin', 'owl-carousel'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/product/product_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'View Catalog'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.productDetail', {
            url: '/product/:productName',
            templateUrl: Route.base('/promise/procurement/portal/product_detail.html'),
            params: {
                dataCatalog: null,
                catalogFilter: null
            },
            resolve: {
                assets: Route.require('ez-plus', 'TouchSpin', 'owl-carousel')
            },
            data: {
                displayName: 'View Catalog'
            }
        })
        
//        .state('appportal.kebijakan', {
//            url: '/kebijakan',
//            templateUrl: Route.base('/promise/procurement/portal/policy.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['ez-plus'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/portal/home_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Kebijakan'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.kebijakan', {
            url: '/kebijakan',
            templateUrl: Route.base('/promise/procurement/portal/policy.html'),
            resolve: {
                assets: Route.require('ez-plus')
            },
            data: {
                displayName: 'Kebijakan'
            }
        })
        
//        .state('appportal.berita', {
//            url: '/berita',
//            templateUrl: Route.base('/promise/procurement/portal/news.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['ez-plus'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/portal/home_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Berita'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.berita', {
            url: '/berita',
            templateUrl: Route.base('/promise/procurement/portal/news.html'),
            resolve: {
                assets: Route.require('ez-plus')
            },
            data: {
                displayName: 'Berita'
            }
        })
        
//        .state('appportal.hubungiKami', {
//            url: '/hubungiKami',
//            templateUrl: Route.base('/promise/procurement/portal/hubungi_kami.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['ez-plus'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/portal/home_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Hubungi Kami'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.hubungiKami', {
            url: '/hubungiKami',
            templateUrl: Route.base('/promise/procurement/portal/hubungi_kami.html'),
            resolve: {
                assets: Route.require('ez-plus')
            },
            data: {
                displayName: 'Hubungi Kami'
            }
        })
        
//        .state('appportal.procurement-ecatalog-compare', {
//            url: '/compare_product',
//            templateUrl: Route.base('/promise/procurement/portal/compare_product.html'),
//            params: {
//                catalogList: null
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/portal/ecatalog_portal_compare_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Compare Catalog'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.procurement-ecatalog-compare', {
            url: '/compare_product',
            templateUrl: Route.base('/promise/procurement/portal/compare_product.html'),
            resolve: {
                assets: Route.require('angulargrid')
            },
            data: {
                displayName: 'Compare Catalog'
            }
        })
        
//        .state('appportal.procurement-catalog-vendor-detail', {
//            url: '/catalog/vendor/detail',
//            templateUrl: Route.base('promise/procurement/portal/vendor_detail.html'),
//            params: {
//                dataVendor: null
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ez-plus', 'TouchSpin', 'textAngular'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/vendor/evendor_detail_controller.js');
//                    });
//            }],
//            	
//            data: {
//                displayName: 'View Vendor'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appportal.procurement-catalog-vendor-detail', {
            url: '/catalog/vendor/detail',
            templateUrl: Route.base('promise/procurement/portal/vendor_detail.html'),
            params: {
                dataVendor: null
            },
            resolve: {
                assets: Route.require('angulargrid', 'ez-plus', 'TouchSpin', 'textAngular')
            },
            data: {
                displayName: 'View Vendor'
            }
        })
        
        // End Baru 2018

        // vendor
        .state('appvendor', {
                url: '/appvendor',
                abstract: true,
                templateUrl: Route.base('appvendor.html'),
                resolve: {
                    _assets: Route.require('icons', 'screenfull', 'sparklines', 'slimscroll', 'toaster', 'animate')
                }
            })
            .state('appvendor.promise', {
                url: '/promise',
                template: '<div ui-view ng-class="app.views.animation"></div>'

            })
            .state('appvendor.promise.dashboard', {
                url: '/dashboard',
                templateUrl: Route.base('/promise/procurement/dashboard/vendor/dashboard.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable')
                },
                data: {
                    displayName: 'Dashboard'
                }
            })
            // undanganvendor
            .state('appvendor.promise.procurement-undanganvendor', {
                url: '/procurement/undanganvendor',
                templateUrl: Route.base('/promise/procurement/undanganpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Undangan Vendor'
                }
            })
            .state('appvendor.promise.procurement-undanganvendor-detil', {
                url: '/procurement/undanganvendor/detil',
                templateUrl: Route.base('/promise/procurement/undanganpengadaan/detil.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Undangan Vendor'
                }
            })
            // hasil pengadaan
            .state('appvendor.promise.procurement-hasilpengadaan', {
                url: '/procurement/vendor/hasilpengadaan',
                templateUrl: Route.base('/promise/procurement/hasilpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Hasil Pengadaan'
                }
            })
            .state('appvendor.promise.procurement-hasilpengadaan-detail', {
                url: '/procurement/vendor/hasilpengadaan/detail',
                templateUrl: Route.base('/promise/procurement/hasilpengadaan/detail.html'),
                params: {
                    dataPengadaan: null
                },
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Hasil Pengadaan'
                }
            })
            // Route of Pengumuman Pengadaan (Index)
            .state('appvendor.promise.procurement-vendor-pengumumanpengadaan', {
                url: '/procurement/pengumumanpengadaan',
                templateUrl: Route.base('/promise/procurement/pengumumanpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pengumuman Pengadaan'
                }
            })
            .state('appvendor.promise.procurement-vendor-pengumumanpengadaan-view', {
                url: '/procurement/pengumumanpengadaan/view',
                templateUrl: Route.base('/promise/procurement/pengumumanpengadaan/view/index.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Pengumuman Pengadaan'
                }
            })

        // index Penawaran harga independence
        .state('appvendor.promise.procurement-vendor-penawaranhargaindependence', {
                url: '/procurement/penawaranhargaindependence/vendor',
                templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Penawaran Harga'
                }
            })
            .state('appvendor.promise.procurement-vendor-penawaranvendorindependencehargatotal', {
                url: '/procurement/penawaranhargaindependence/vendor/harga/total',
                templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/HargaTotal.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                },
                data: {
                    displayName: 'Penawaran Harga'
                }
            })

        .state('appvendor.promise.procurement-vendor-penawaranvendorindependencehargasatuan', {
            url: '/procurement/penawaranhargaindependence/vendor/harga/satuan',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/HargaSatuan.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-penawaranvendorindependencehargasatuanjasa', {
            url: '/procurement/penawaranharga/vendor/satuan/detail/jasa',
            templateUrl: Route.base('/promise/procurement/penawaranhargaindependence/vendor/penawaranhargaindependence/HargaSatuanJasa.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-penawaranvendorindependencehargasatuanmaterial', {
                url: '/procurement/penawaranhargaindependence/vendor/satuan/detail/material',
                templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/HargaSatuanMaterial.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                },
                data: {
                    displayName: 'Penawaran Harga'
                }
            })
            // Vendor --> index Pemasukan Penawaran
            .state('appvendor.promise.procurement-vendor-datapenawaran', {
                url: '/procurement/datapenawaran/vendor',
                templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaran.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Penawaran Harga'
                }
            })

        // Vendor -- Pemasukan Penawaran Dua Tahap - data penawaran tahap dua -
		// index
        .state('appvendor.promise.procurement-vendor-datapenawarantahapdua-index', {
            url: '/procurement/datapenawaran/vendor/tahapdua/index',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaranTahapDuaIndex.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // index Data Penawaran
        .state('appvendor.promise.procurement-vendor-penawaranharga', {
            url: '/procurement/penawaranharga/vendor',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaranHarga.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // pemasukan penawaran vendor satu sampul
        .state('appvendor.promise.procurement-vendor-datapenawaranvendorsatusampul', {
            url: '/procurement/datapenawaran/vendor/satu/sampul',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaranVendorSatuSampul.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // pemasukan penawaran vendor dua sampul
        .state('appvendor.promise.procurement-vendor-datapenawaranvendorduasampul', {
            url: '/procurement/datapenawaran/vendor/dua/sampul',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaranVendorDuaSampul.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // detil pemasukan penawaran vendor Tahap Satu
        .state('appvendor.promise.procurement-vendor-datapenawaranvendortahapsatu', {
            url: '/procurement/datapenawaran/vendor/tahap/satu',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaranVendorTahapSatu.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // detil pemasukan penawaran vendor Tahap Dua
        .state('appvendor.promise.procurement-vendor-pemasukanpenawaranvendortahapdua', {
            url: '/procurement/datapenawaran/vendor/tahap/dua',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/dataPenawaranVendorTahapDua.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // View penawaran Harga vendor Total
        .state('appvendor.promise.procurement-vendor-penawaranvendorhargatotal', {
            url: '/procurement/penawaranharga/vendor/harga/total',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaranVendorHargaTotal.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // detil penawaran Harga vendor Satuan
        .state('appvendor.promise.procurement-vendor-penawaranhargavendorsatuan', {
            url: '/procurement/penawaranharga/vendor/harga/satuan',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaranVendorHargaSatuan.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // detail satuan Material
        .state('appvendor.promise.procurement-vendor-penawaranhargavendorsatuandetailjasa', {
            url: '/procurement/penawaranharga/vendor/satuan/detail/jasa',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaranHargaDetailJasa.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // detail satuan Material
        .state('appvendor.promise.procurement-vendor-penawaranhargavendorsatuandetailmaterial', {
            url: '/procurement/penawaranharga/vendor/satuan/detail/material',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaranHargaDetailMaterial.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // Add Item Condition Total
        .state('appvendor.promise.procurement-vendor-additemcondition', {
            url: '/procurement/penawaranharga/vendor/add/item/condition',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/addItemCondition.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // Add Item Condition Satuan
        .state('appvendor.promise.procurement-vendor-additemconditionsatuan', {
            url: '/procurement/penawaranharga/vendor/add/item/condition/satuan',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/addItemConditionSatuan.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })


        // Route of Evaluasi Harga Vendor (index)
        .state('appvendor.promise.procurement-vendor-auctionvendortotal', {
            url: '/procurement/evaluasiharga/vendor/auction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/penawaranharga.total.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // Total
        .state('appvendor.promise.procurement-vendor-pemasukanhargatotalterbuka', {
            url: '/procurement/evaluasiharga/vendor/total/auction/terbuka',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.auction.terbuka.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanpenawarantotalterbuka', {
            url: '/procurement/evaluasiharga/vendor/total/pemasukanpenawaran/terbuka',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.pemasukanpenawaran.terbuka.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanhargatotaltertutup', {
            url: '/procurement/evaluasiharga/vendor/total/auction/tertutup',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.auction.tertutup.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanpenawarantotaltertutup', {
            url: '/procurement/evaluasiharga/vendor/total/pemasukanpenawaran/tertutup',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.pemasukanpenawaran.tertutup.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        // Satuan
        .state('appvendor.promise.procurement-vendor-pemasukanhargasatuanterbuka', {
            url: '/procurement/evaluasiharga/vendor/satuan/auction/terbuka',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.terbuka.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanhargasatuandetailterbuka', {
            url: '/procurement/evaluasiharga/vendor/satuan/auction/detail/terbuka',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.detail.terbuka.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanpenawaransatuanterbuka', {
            url: '/procurement/evaluasiharga/vendor/satuan/pemasukanpenawaran/terbuka',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.pemasukanpenawaran.terbuka.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanhargasatuantertutup', {
            url: '/procurement/evaluasiharga/vendor/satuan/auction/tertutup',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.tertutup.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanhargasatuandetailtertutup', {
            url: '/procurement/evaluasiharga/vendor/satuan/auction/detail/tertutup',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.detail.tertutup.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })

        .state('appvendor.promise.procurement-vendor-pemasukanpenawaransatuantertutup', {
            url: '/procurement/evaluasiharga/vendor/satuan/pemasukanpenawaran/tertutup',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.pemasukanpenawaran.tertutup.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Penawaran Harga'
            }
        })
        
        // Ubah Data User
        .state('app.promise.procurement-ubah-datauser', {// menggunakan file
															// yang sama dengan
															// registrasi data
															// user
            url: '/procurement/ubah/datauser',
            templateUrl: Route.base('/promise/procurement/vendor/datauser/registrasi.datauser.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Ubah Password'
            }
        })

        // Registrasi Data User
        .state('appvendor.promise.procurement-vendor-datauser', {
            url: '/procurement/vendor/datauser',
            templateUrl: Route.base('/promise/procurement/vendor/datauser/registrasi.datauser.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

        // Registrasi Data Perusahaan
        .state('appvendor.promise.procurement-vendor-dataperusahaan', {
            url: '/procurement/vendor/dataperusahaan',
            templateUrl: Route.base('/promise/procurement/vendor/dataperusahaan/registrasi.dataperusahaan.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables', 'textAngular')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

       // Registrasi Data Bank
        .state('appvendor.promise.procurement-vendor-databank', {
            url: '/procurement/vendor/databank',
            templateUrl: Route.base('/promise/procurement/vendor/databank/registrasi.databank.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            params: {
              toDo : null
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })
        
        .state('appvendor.promise.procurement-vendor-databank-view', {
            url: '/procurement/vendor/databank/view',
            templateUrl: Route.base('/promise/procurement/vendor/databank/registrasi.databank.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            },
            params: {
              dataBank: null,         
              toDo : null,
              status:null
            }
        })
        
      // Registrasi Data Segmentasi
        .state('appvendor.promise.procurement-vendor-datasegmentasi', {
            url: '/procurement/vendor/datasegmentasi/index',
            templateUrl: Route.base('/promise/procurement/vendor/datasegmentasi/registrasi.datasegmentasi.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })
        
        .state('appvendor.promise.procurement-vendor-datasegmentasi-view', {
            url: '/procurement/vendor/datasegmentasi/view',
            templateUrl: Route.base('/promise/procurement/vendor/datasegmentasi/registrasi.datasegmentasi.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            },
            params: {
            	dataSegmentasi: null,
            	toDo : null,
            	status: null
            }
        })

        // Registrasi Data Keuangan
        .state('appvendor.promise.procurement-vendor-datakeuangan', {
            url: '/procurement/vendor/datakeuangan',
            templateUrl: Route.base('/promise/procurement/vendor/datakeuangan/registrasi.datakeuangan.index.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })
        
        .state('appvendor.promise.procurement-vendor-datakeuangan-view', {
            url: '/procurement/vendor/datakeuangan/view',
            templateUrl: Route.base('/promise/procurement/vendor/datakeuangan/registrasi.datakeuangan.view.html'),
            params:{
                dataKeuangan: null,
                toDo: null,
                status:null
            },
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

        // Registrasi Data Dokumen
        .state('appvendor.promise.procurement-vendor-datadokumen', {
            url: '/procurement/vendor/datadokumen',
            templateUrl: Route.base('/promise/procurement/vendor/datadokumen/registrasi.datadokumen.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

        // Registrasi Data Peralatan
        .state('appvendor.promise.procurement-vendor-dataperalatan', {
            url: '/procurement/vendor/dataperalatan',
            templateUrl: Route.base('/promise/procurement/vendor/dataperalatan/registrasi.dataperalatan.index.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })
        .state('appvendor.promise.procurement-vendor-dataperalatan-view', {
            url: '/procurement/vendor/dataperalatan/view',
            templateUrl: Route.base('/promise/procurement/vendor/dataperalatan/registrasi.dataperalatan.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            params : {
				dataPeralatan : null,
				toDo: null,
				status:null
			},
            data: {
                displayName: 'Vendor Setting'
            }
        })

       // Registrasi Data Pengalaman
        .state('appvendor.promise.procurement-vendor-datapengalaman', {
            url: '/procurement/vendor/datapengalaman',
            templateUrl: Route.base('/promise/procurement/vendor/datapengalaman/registrasi.datapengalaman.index.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            params : {
          	  dataPengalaman : null,
          	  status : null,
          	  toDo : null,
          	  tipe : null
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })
        .state('appvendor.promise.procurement-vendor-datapengalaman-view', {
            url: '/procurement/vendor/datapengalaman/view',
            templateUrl: Route.base('/promise/procurement/vendor/datapengalaman/registrasi.datapengalaman.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            params : {
          	  dataPengalaman : null,
          	  status :null,
          	  toDo : null,
          	  tipe : null
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

        // Registrasi Data Peralatan
        .state('appvendor.promise.procurement-vendor-datasertifikat', {
            url: '/procurement/vendor/datasertifikat',
            templateUrl: Route.base('/promise/procurement/vendor/datasertifikat/datasertifikat.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Vendor Setting'
            }
        })

        // undanganvendorkualifikasi
        .state('appvendor.promise.procurement-prakualifikasi-undanganvendorkualifikasi', {
            url: '/procurement/prakualifikasi/undanganvendorkualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/undangankualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Kualifikasi Vendor'
            }
        })

        .state('appvendor.promise.procurement-prakualifikasi-undanganvendorkualifikasi-detil', {
            url: '/procurement/prakualifikasi/undanganvendorkualifikasi/detil',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/undangankualifikasi/detil.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Kualifikasi Vendor'
            }
        })

        // pengumuman Kualifikasi (Index)
        .state('appvendor.promise.procurement-prakualifikasi-pengumumankualifikasi', {
                url: '/procurement/prakualifikasi/pengumumankualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/pengumumankualifikasi/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })
            .state('appvendor.promise.procurement-prakualifikasi-pengumumankualifikasi-detil', {
                url: '/procurement/prakualifikasi/pengumumankualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/pengumumankualifikasi/detil.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })

        // Data Kualifikasi (Index)
        .state('appvendor.promise.procurement-prakualifikasi-datakualifikasi', {
                url: '/procurement/prakualifikasi/datakualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/datakualifikasi/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })
            .state('appvendor.promise.procurement-prakualifikasi-datakualifikasi-detil', {
                url: '/procurement/prakualifikasi/datakualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/datakualifikasi/detil.html'),
                resolve: {
                    assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })
            // Vendor - Hasil Kualifikasi
            .state('appvendor.promise.procurement-panitia-prakualifikasi-hasilkualifikasi', {
                url: '/procurement/prakualifikasi/hasilkualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })
            .state('appvendor.promise.procurement-panitia-prakualifikasi-hasilkualifikasi-detil', {
                url: '/procurement/prakualifikasi/hasilkualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.detil.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'filestyle')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
            })

        // Delivery Recieved Vendor
//        .state('appvendor.promise.procurement-deliveryreceived', {
//            url: '/procurement/vendor/deliveryreceived',
//            templateUrl: Route.base('promise/procurement/deliveryreceived/deliveryreceived.vendor.index.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.vendor.index.controller.js');
//                    });
//      	}],
//            data: {
//                displayName: 'Delivery Receipt'
//            }
//        })
        
        // Delivery Recieved Vendor
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appvendor.promise.procurement-deliveryreceived', {
            url: '/procurement/vendor/deliveryreceived',
            templateUrl: Route.base('promise/procurement/deliveryreceived/deliveryreceived.vendor.index.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Delivery Receipt'
            }
        })
            
            // confirmation order
        .state('appvendor.promise.procurement-vendor-sales-order', {
	            url: '/procurement/vendor/confirmation_order',
	            templateUrl: Route.base('promise/procurement/vendor/sales_order/sales.order.vendor.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Confirmation Order'
	            }
	        })
            /*--------------------------------------------------END VENDOR---------------------------------------------*/


        // panitia
        .state('app', {
                url: '/app',
                abstract: true,
                templateUrl: Route.base('app.html'),
                resolve: {
                    _assets: Route.require('icons', 'screenfull', 'sparklines', 'slimscroll', 'toaster', 'animate')
                },
                data: {
                    displayName: 'Home'
                }
            })
            .state('app.promise', {
                url: '/promise',
                template: '<div ui-view ng-class="app.views.animation"></div>',
                data: {
                    displayName: 'Home'
                }
            })

        // Mailbox
        .state('app.promise.mailbox', {
                url: '/mailbox',
                params: {
                    email: null
                },
                templateUrl: Route.base('mailbox.html'),
                resolve: {
                    assets: Route.require('moment')
                },
                data: {
                    displayName: 'Mail Box'
                }
            })
            .state('app.promise.mailbox.folder', {
                url: '/folder'
            })
            .state('app.promise.mailbox.folder.list', {
                url: '/:folder',
                views: {
                    'container@app.promise.mailbox': {
                        templateUrl: Route.base('mailbox.folder.html')
                    }
                }
            })
            .state('app.promise.mailbox.folder.list.view', {
                url: '/:id',
                views: {
                    'mails@app.promise.mailbox.folder.list': {
                        templateUrl: Route.base('mailbox.view-mail.html')
                    }
                },
                resolve: {
                    assets: Route.require('textAngular', 'textAngularSetup')
                },
                data: {
                    displayName: 'Mail Box List'
                }
            })
            .state('app.promise.mailbox.compose', {
                url: '/compose',
                params: {
                    email: null
                },
                views: {
                    'container@app.promise.mailbox': {
                        templateUrl: Route.base('mailbox.compose.html')
                    }
                },
                resolve: {
                    assets: Route.require('textAngular', 'textAngularSetup')
                },
                data: {
                    displayName: 'Mail Box Compose'
                }
            })

        // inisialisasi
        .state('app.promise.procurement-inisialisasi', {
                url: '/procurement/inisialisasi',
                templateUrl: Route.base('/promise/procurement/inisialisasi/index.bumn.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Inisialisasi'
                }
            })
            .state('app.promise.procurement-inisialisasi-smb', {
                url: '/procurement/inisialisasi/smb',
                templateUrl: Route.base('/promise/procurement/inisialisasi/index.smb.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                },
                data: {
                    displayName: 'Inisialisasi'
                }
            })
            .state('app.promise.procurement-inisialisasi-tambah', {
                url: '/procurement/inisialisasi/add',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.html'),
                resolve: {
                    assets: Route.require('ui.select', 'xeditable', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi'
                }
            })
            .state('app.promise.procurement-inisialisasi-upload', {
                url: '/procurement/inisialisasi/upload',
                templateUrl: Route.base('promise.pengadaan.uploadv2.html'),
                resolve: {
                    assets: Route.require('angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi'
                }
            })

        // inisialisasi BUMN
        .state('app.promise.procurement-inisialisasi-add-bumn', {
                url: '/procurement/inisialisasi/add/bumn',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi BUMN'
                }
            })
            .state('app.promise.procurement-inisialisasi-edit-bumn', {
                url: '/procurement/inisialisasi/edit/bumn',
                templateUrl: Route.base('/promise/procurement/inisialisasi/edit.bumn.html'),
                params: {
                    pengadaan: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi BUMN'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-kebutuhanmaterialsatuan', {
                url: '/procurement/inisialisasi/add/kebutuhanmaterialsatuan',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.kebutuhanmaterialsatuan.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kebutuhan Material Satuan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-kebutuhanmaterialtotal', {
                url: '/procurement/inisialisasi/add/kebutuhanmaterialtotal',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.kebutuhanmaterialtotal.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kebutuhan Material Total'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-kebutuhanjasatotal', {
                url: '/procurement/inisialisasi/add/kebutuhanjasatotal',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.kebutuhanjasatotal.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kebutuhan Jasa Total'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-kebutuhanjasasatuan', {
                url: '/procurement/inisialisasi/add/kebutuhanjasasatuan',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.kebutuhanjasasatuan.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kebutuhan Jasa Satuan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-bidang', {
                url: '/procurement/inisialisasi/add/bidang',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.bidang.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi Bidang'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-vendor', {
                url: '/procurement/inisialisasi/add/vendor',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.bumn.vendor.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi Vendor'
                }
            })

        // inisialisasi SMB
        .state('app.promise.procurement-inisialisasi-add-smb', {
                url: '/procurement/inisialisasi/add/smb',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-kebutuhanmaterialsatuan', {
                url: '/procurement/inisialisasi/add/smb/kebutuhanmaterialsatuan',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.kebutuhanmaterialsatuan.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-kebutuhanmaterialtotal', {
                url: '/procurement/inisialisasi/add/smb/kebutuhanmaterialtotal',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.kebutuhanmaterialtotal.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-kebutuhanjasatotal', {
                url: '/procurement/inisialisasi/add/smb/kebutuhanjasatotal',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.kebutuhanjasatotal.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-kebutuhanjasasatuan', {
                url: '/procurement/inisialisasi/add/smb/kebutuhanjasasatuan',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.kebutuhanjasasatuan.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-bidang', {
                url: '/procurement/inisialisasi/add/smb/bidang',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.bidang.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-add-smb-vendor', {
                url: '/procurement/inisialisasi/add/smb/vendor',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.smb.vendor.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })
            .state('app.promise.procurement-inisialisasi-edit-smb', {
                url: '/procurement/inisialisasi/edit/smb',
                templateUrl: Route.base('/promise/procurement/inisialisasi/edit.smb.html'),
                params: {
                    pengadaan: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi SMB'
                }
            })

        // Inisiasliasi - Astra
        .state('app.promise.procurement-inisialisasi-index04', {
                url: '/procurement/inisialisasi/index04',
                templateUrl: Route.base('/promise/procurement/inisialisasi/index.04.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add04', {
                url: '/procurement/inisialisasi/add04',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.04.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add04-kebutuhanmaterialtotal', {
                url: '/procurement/inisialisasi/add04/kebutuhanmaterialtotal',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.04.kebutuhanmaterialtotal.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add04-bidang', {
                url: '/procurement/inisialisasi/add04/bidang',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.04.bidang.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            .state('app.promise.procurement-inisialisasi-add04-vendor', {
                url: '/procurement/inisialisasi/add04/vendor',
                templateUrl: Route.base('/promise/procurement/inisialisasi/add.04.vendor.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            .state('app.promise.procurement-inisialisasi-edit04', {
                url: '/procurement/inisialisasi/edit04',
                templateUrl: Route.base('/promise/procurement/inisialisasi/edit.04.html'),
                params: {
                    pengadaan: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Inisialisasi Pengadaan'
                }
            })
            
            /*
			 * //Role Master : .state('app.promise.procurement-master-role', {
			 * url: '/procurement/master/role', templateUrl:
			 * Route.base('/promise/procurement/master/role/role.index.html'),
			 * resolve: ['$ocLazyLoad', function ($ocLazyLoad) { return
			 * $ocLazyLoad.load(['angulargrid', 'ui.select',
			 * 'angularFileUpload', 'filestyle', 'datatables']) .then(function () {
			 * return
			 * $ocLazyLoad.load('src/js/modules/promise/procurement/master/role/role.index.controller.js');
			 * }); }], data: { displayName: 'Role' } })
			 * .state('app.promise.procurement-master-role-tambah', { url:
			 * '/procurement/master/role', templateUrl:
			 * Route.base('/promise/procurement/master/role/role.add.html'),
			 * resolve: ['$ocLazyLoad', function ($ocLazyLoad) { return
			 * $ocLazyLoad.load(['angulargrid', 'ui.select',
			 * 'angularFileUpload', 'filestyle', 'datatables']) .then(function () {
			 * return
			 * $ocLazyLoad.load('src/js/modules/promise/procurement/master/role/role.add.controller.js');
			 * }); }], data: { displayName: 'Role' } })
			 * .state('app.promise.procurement-master-role-ubah', { url:
			 * '/procurement/master/role', templateUrl:
			 * Route.base('/promise/procurement/master/role/role.edit.html'),
			 * params: { role: null }, resolve: ['$ocLazyLoad', function
			 * ($ocLazyLoad) { return $ocLazyLoad.load(['angulargrid',
			 * 'ui.select', 'angularFileUpload', 'filestyle', 'datatables'])
			 * .then(function () { return
			 * $ocLazyLoad.load('src/js/modules/promise/procurement/master/role/role.edit.controller.js');
			 * }); }], data: { displayName: 'Role' } })
			 */            
            
            // Master Role
            .state('app.promise.procurement-master-role', {
                url: '/procurement/master/role',
                templateUrl: Route.base('/promise/procurement/master/role/role.index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Role'
                }
            })
            .state('app.promise.procurement-master-role-view', {
                url: '/procurement/master/role/view',
                templateUrl: Route.base('/promise/procurement/master/role/role.view.html'),
                params: {
                    role: null,
                    toDo: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Role'
                }
            })
            
            // Role Menu
	          .state('app.promise.procurement-master-rolemenu', {
	              url: '/procurement/master/rolemenu',
	              templateUrl: Route.base('/promise/procurement/master/rolemenu/rolemenu.index.html'),
	              resolve: {
	                  assets: Route.require('datatables')
	              },
	              data: {
	                  displayName: 'Role Menu'
	              }
	          })
	          .state('app.promise.procurement-master-rolemenu-detail', {
	              url: '/procurement/master/rolemenu/detail',
	              templateUrl: Route.base('/promise/procurement/master/rolemenu/rolemenu.detail.html'),
	              resolve: {
	                  assets: Route.require('datatables')
	              },
	              data: {
	                  displayName: 'Role Menu'
	              }
	          })
	          .state('app.promise.procurement-master-rolemenu-view', {
	              url: '/procurement/master/rolemenu/view',
	              templateUrl: Route.base('/promise/procurement/master/rolemenu/rolemenu.view.html'),
	              params: {
	                  roleMenu: null,
	                  toDo: null
	              },
	              resolve: {
	                  assets: Route.require('ui.select')
	              },
	              data: {
	                  displayName: 'Role Menu'
	              }
	          })

        .state('app.promise.procurement-master-rolemenu-edit', {
            url: '/procurement/master/rolemenu/edit',
            templateUrl: Route.base('/promise/procurement/master/rolemenu/edit.html'),
            resolve: {
                assets: Route.require('ui.select')
            },
            data: {
                displayName: 'Role Menu'
            }
        })

        // Master Menu Aksi
    	.state('app.promise.procurement-master-menuaksi',{
            	url: '/procurement/master/menuaksi',
            	templateUrl: Route.base('/promise/procurement/master/menuaksi/menuaksi.index.html'),
            	resolve: {
                	assets: Route.require('datatables')
            	},
            	data: {
                	displayName: 'Menu Aksi'
            	}
        	})
        	
    	.state('app.promise.procurement-master-menuaksi-view', {
        	url: '/procurement/master/menuaksi/view',
        	templateUrl: Route.base('/promise/procurement/master/menuaksi/menuaksi.view.html'),
        	params:{
        		toDo:null,
        		menuAksi:null
        	},
        	resolve: {
            	assets: Route.require( 'ui.select')
        	},
        	data: {
            	displayName: 'Menu Aksi'
        	}
    	})

        .state('app.promise.procurement-application-generateprnumber', {
            url: '/procurement/application/generateprnumber',
            templateUrl: Route.base('/promise/procurement/application/generateprnumber/generateprnumber.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            params: {
                autoNumber: null
            },
            data: {
                displayName: 'Generate Number'
            }
        })

        // master Aksi
        .state('app.promise.procurement-master-aksi', {
            url: '/procurement/master/aksi',
            templateUrl: Route.base('/promise/procurement/master/aksi/aksi.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Aksi'
            }
        })
            
            
         .state('app.promise.procurement-master-aksi-view', {
            url: '/procurement/master/aksi',
            templateUrl: Route.base('/promise/procurement/master/aksi/aksi.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Aksi'
            },
            params: {
            	aksi: null,
            	toDo : null
            }
         })

        .state('app.promise.procurement-master-aksi-edit', {
            url: '/procurement/master/aksi/edit',
            templateUrl: Route.base('/promise/procurement/master/aksi/edit.html'),
            resolve: {
                assets: Route.require('ui.select')
            },
            data: {
                displayName: 'Aksi'
            }
        })

         // master Menu
        .state('app.promise.procurement-master-menu', {
                url: '/procurement/master/menu',
                templateUrl: Route.base('/promise/procurement/master/menu/menu.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Menu'
                }
            })
        .state('app.promise.procurement-master-menu-view', {
            url: '/procurement/master/menu/view',
            templateUrl: Route.base('/promise/procurement/master/menu/menu.view.html'),
            params: {
                menu: null,
                toDo: null
            },
            data: {
                displayName: 'menu'
            }
        })

        // master email notifikasi
        .state('app.promise.procurement-master-emailnotification', {
        	url : '/procurement/master/emailnotification',
        	templateUrl : Route.base('/promise/procurement/master/emailnotification/emailnotification.index.html'),
        	resolve : {
        		assets : Route.require('datatables')
        	},
        	data : {
        		displayName : 'Email Notification'
        	}
        })
        .state('app.promise.procurement-master-emailnotification-view', {
              url : '/procurement/master/emailnotification/view',
              templateUrl : Route.base('/promise/procurement/master/emailnotification/emailnotification.view.html'),
              resolve : {
            	  assets : Route.require('moment', 'textAngular', 'textAngularSetup', 'ui.select')
              },
              params : {
            	  toDo : null,
            	  emailNotification : null
              },
              data : {
            	  displayName : 'Email Notification'
              }
        })

        // master email smtp
        .state('app.promise.procurement-master-emailsmtp', {
            url: '/procurement/master/emailsmtp',
            templateUrl: Route.base('/promise/procurement/master/emailsmtp/emailsmtp.index.html'),
            resolve: {
                assets: Route.require('moment')
            },
            data: {
                displayName: 'Email SMTP Server'
            }
        })

        // master jabatan
        .state('app.promise.procurement-master-jabatan', {
                url: '/procurement/master/jabatan',
                templateUrl: Route.base('/promise/procurement/master/jabatan/jabatan.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Position'
                }
            })
        .state('app.promise.procurement-master-jabatan-view', {
                url: '/procurement/master/jabatan/view',
                templateUrl: Route.base('/promise/procurement/master/jabatan/jabatan.view.html'),
                params: {
                    jabatan: null,
                    toDo: null
                },
                data: {
                    displayName: 'Jabatan'
                }
            })
            
        // master Item Type
        .state('app.promise.procurement-master-itemtype', {
              url : '/procurement/master/itemtype',
              templateUrl : Route.base('/promise/procurement/master/itemtype/itemtype.index.html'),
              resolve : {
            	  assets : Route.require('datatables')
              },
              params : {
            	  itemType : null,
            	  toDo : null
              },
              data : {
            	  displayName : 'Item Type'
              }
            })
            
        .state('app.promise.procurement-master-itemtype-view', {
              url : '/procurement/master/itemtype/view',
              templateUrl : Route.base('/promise/procurement/master/itemtype/itemtype.view.html'),
              params : {
            	  itemType : null,
            	  toDo : null
              },
              data : {
                displayName : 'Item Type'
              }
            })

        // master afco
        .state('app.promise.procurement-master-afco', {
                url: '/procurement/master/afco',
                templateUrl: Route.base('/promise/procurement/master/afco/afco.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Afco'
                }
            })
        .state('app.promise.procurement-master-afco-view', {
                url: '/procurement/master/afco/view',
                templateUrl: Route.base('/promise/procurement/master/afco/afco.view.html'),
                params: {
                    afco: null,
                    toDo: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'angularBootstrapNavTree')
                },
                data: {
                    displayName: 'Afco'
                }
            })



        // master alur pengadaan
        .state('app.promise.procurement-master-alurpengadaan', {
                url: '/procurement/master/alurPengadaan',
                templateUrl: Route.base('/promise/procurement/master/alurpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Alur Pengadaan'
                }
            })
            .state('app.promise.procurement-master-alurpengadaan-tambah', {
                url: '/procurement/master/alurPengadaan/tambah',
                templateUrl: Route.base('/promise/procurement/master/alurpengadaan/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Alur Pengadaan'
                }
            })
            .state('app.promise.procurement-master-alurpengadaan-ubah', {
                url: '/procurement/master/alurPengadaan/ubah',
                templateUrl: Route.base('/promise/procurement/master/alurpengadaan/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Alur Pengadaan'
                }
            })
            .state('app.promise.procurement-master-alurpengadaan-02', {
                url: '/procurement/master/alurPengadaan02',
                templateUrl: Route.base('/promise/procurement/master/alurpengadaan/alurpengadaan.02.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Alur Pengadaan'
                }
            })
            .state('app.promise.procurement-master-alurpengadaan-02-tambahtahapan', {
                url: '/procurement/master/alurPengadaan02/tambahtahapan',
                templateUrl: Route.base('/promise/procurement/master/alurpengadaan/alurpengadaan.02.tambahtahapan.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                },
                data: {
                    displayName: 'Alur Pengadaan'
                }
            })
          


        .state('app.promise.procurement-master-user', {
                url: '/procurement/master/user',
                templateUrl: Route.base('/promise/procurement/master/user/user.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'User'
                }
            })
            .state('app.promise.procurement-master-user-view', {
                url: '/procurement/master/user/view',
                templateUrl: Route.base('/promise/procurement/master/user/user.view.html'),
                params: {
                    user: null,
                    roleUser:null,
                    toDo:null
                    
                },
                resolve: {
                    assets: Route.require('tree.dropdown', 'ui.select')
                },
                data: {
                    displayName: 'User'
                }
            })        
        // master negara
        .state('app.promise.procurement-master-negara', {
                url: '/procurement/master/negara',
                templateUrl: Route.base('/promise/procurement/master/negara/negara.index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Nation'
                }
            })
            .state('app.promise.procurement-master-negara-view', {
                url: '/procurement/master/negara/view',
                templateUrl: Route.base('/promise/procurement/master/negara/negara.view.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                params : {
					negara : null,
					toDo: null
				},
                data: {
                    displayName: 'Nation'
                }
            })
            
        // master bank
        .state('app.promise.procurement-master-bank', {
                url: '/procurement/master/bank',
                templateUrl: Route.base('/promise/procurement/master/bank/bank.index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                params : {
                	bank : null,
                	toDo : null,
                },
                data: {
                    displayName: 'Bank'
                }
            })
            .state('app.promise.procurement-master-bank-view', {
                url: '/procurement/master/bank/view',
                templateUrl: Route.base('/promise/procurement/master/bank/bank.view.html'),
                resolve: {
                    assets: Route.require( 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                params : {
                	bank : null,
                	toDo : null,
                },
                data: {
                    displayName: 'Bank'
                }
            })   
            
        // master conditional price
        .state('app.promise.procurement-master-conditionalprize', {
                url: '/procurement/master/conditionalprice',
                templateUrl: Route.base('/promise/procurement/master/conditionalprize/conditionalprice.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Conditional Prize'
                }
            })
            
        .state('app.promise.procurement-master-conditionalprize-view', {
              url: '/procurement/master/conditionalprice',
              templateUrl: Route.base('/promise/procurement/master/conditionalprize/conditionalprice.view.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Conditional Prize'  
              },
              params : {
                  conditionalPrice: null,
                  toDo : null
                    },
                resolve: {
                    assets: Route.require('ui.select')
                 }
          })

        // master jenis pajak
        .state('app.promise.procurement-master-jenispajak', {
                url: '/procurement/master/jenispajak',
                templateUrl: Route.base('/promise/procurement/master/jenispajak/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Jenis Pajak'
                }
            })
            .state('app.promise.procurement-master-jenispajak-tambah', {
                url: '/procurement/master/jenispajak/tambah',
                templateUrl: Route.base('/promise/procurement/master/jenispajak/add.html'),
                data: {
                    displayName: 'Jenis Pajak'
                }
            })
            .state('app.promise.procurement-master-jenispajak-ubah', {
                url: '/procurement/master/jenispajak/ubah',
                templateUrl: Route.base('/promise/procurement/master/jenispajak/edit.html'),
                data: {
                    displayName: 'Jenis Pajak'
                }
            })

         // master jenis penawaran
            .state('app.promise.procurement-master-jenispenawaran', {
                url: '/procurement/master/jenispenawaran',
                templateUrl: Route.base('/promise/procurement/master/jenispenawaran/jenispenawaran.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Jenis Penawaran'
                }
            })
            
            .state('app.promise.procurement-master-jenispenawaran-view', {
                url: '/procurement/master/jenispenawaran',
                templateUrl: Route.base('/promise/procurement/master/jenispenawaran/jenispenawaran.view.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Jenis Penawaran'
                },
                params: {
                	jenisPenawaran: null,
                	toDo : null
                }
            })

        // master kualifikasi pengadaan
        .state('app.promise.procurement-master-kualifikasipengadaan', {
                url: '/procurement/master/kualifikasipengadaan',
                templateUrl: Route.base('/promise/procurement/master/kualifikasipengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kualifikasi Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kualifikasipengadaan-tambah', {
                url: '/procurement/master/kualifikasipengadaan/tambah',
                templateUrl: Route.base('/promise/procurement/master/kualifikasipengadaan/add.html'),
                data: {
                    displayName: 'Kualifikasi Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kualifikasipengadaan-ubah', {
                url: '/procurement/master/kualifikasipengadaan/ubah',
                templateUrl: Route.base('/promise/procurement/master/kualifikasipengadaan/edit.html'),
                data: {
                    displayName: 'Kualifikasi Pengadaan'
                }
            })

       // master kualifikasi vendor
        .state('app.promise.procurement-master-kualifikasivendor', {
                url: '/procurement/master/kualifikasivendor',
                templateUrl: Route.base('/promise/procurement/master/kualifikasivendor/kualifikasivendor.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
          })
         .state('app.promise.procurement-master-kualifikasivendor-view', {
                url: '/procurement/master/kualifikasivendor/view',
                templateUrl: Route.base('/promise/procurement/master/kualifikasivendor/kualifikasivendor.view.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                params: {
                    kualifikasiVendor: null,
                    toDo: null
                },
                data: {
                    displayName: 'Kualifikasi Vendor'
                }
          })

         // master mata uang
        .state('app.promise.procurement-master-matauang', {
              url : '/procurement/master/matauang',
              templateUrl : Route.base('/promise/procurement/master/matauang/matauang.index.html'),
              params : {
            	  mataUang : null,
            	  toDo : null
              },
              resolve : {
            	  assets : Route.require('datatables')
              },
              data : {
            	  displayName : 'Currency'
              }
         })

        .state('app.promise.procurement-master-matauang-view',{
              url : '/procurement/master/matauang/view',
              templateUrl : Route.base('/promise/procurement/master/matauang/matauang.view.html'),
              params : {
            	  mataUang : null,
            	  toDo : null
              },
              data : {
            	  displayName : 'Currency'
              }
         })
            
        // master metode penawaran harga
        .state('app.promise.procurement-master-metodepenawaranharga', {
              url : '/procurement/master/metodepenawaranharga',
              templateUrl : Route.base('/promise/procurement/master/metodepenawaranharga/metodepenawaranharga.index.html'),
              resolve : {
            	  assets : Route.require('datatables')
              },
              params : {
            	  metodePenawaranHarga : null,
            	  toDo : null
              },
              data : {
            	  displayName : 'Metode Penawaran Harga'
              }
            })    
        
        .state('app.promise.procurement-master-metodepenawaranharga-view', {
              url : '/procurement/master/metodepenawaranharga/view',
              templateUrl : Route.base('/promise/procurement/master/metodepenawaranharga/metodepenawaranharga.view.html'),
              resolve : {
            	  assets : Route.require('datatables')
              },
              params : {
            	  metodePenawaranHarga : null,
            	  toDo : null
              },
              data : {
            	  displayName : 'Metode Penawaran Harga'
              }
            })
        

        // master pembuat keputusan
        .state('app.promise.procurement-master-pembuatkeputusan', {
                url: '/procurement/master/pembuatkeputusan',
                templateUrl: Route.base('/promise/procurement/master/pembuatkeputusan/pembuatkeputusan.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pembuat Keputusan'
                }
            })
            
         .state('app.promise.procurement-master-pembuatkeputusan-view', {
                url: '/procurement/master/pembuatkeputusan/view',
                templateUrl: Route.base('/promise/procurement/master/pembuatkeputusan/pembuatkeputusan.view.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                params: {
                    pembuatKeputusan: null,
                    toDo: null
                },
                data: {
                    displayName: 'Pembuat Keputusan'
                }
            })

        // master sistem evaluasi penawaran
        .state('app.promise.procurement-master-sistemevaluasipenawaran', {
                url: '/procurement/master/sistemevaluasipenawaran',
                templateUrl: Route.base('/promise/procurement/master/sistemevaluasipenawaran/sistemevaluasipenawaran.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Sistem Evaluasi Penawaran'
                }
            })
        .state('app.promise.procurement-master-sistemevaluasipenawaran-view', {
            url: '/procurement/master/sistemevaluasipenawaran/view',
            params:{
            	toDo:null,
            	sistemEvaluasiPenawaran:null	
            },
            templateUrl: Route.base('/promise/procurement/master/sistemevaluasipenawaran/sistemevaluasipenawaran.view.html'),
            data: {
                displayName: 'Sistem Evaluasi Penawaran'
            }
        })

        // master tahapan
        .state('app.promise.procurement-master-tahapan', {
            url: '/procurement/master/tahapan',
            templateUrl: Route.base('/promise/procurement/master/tahapan/tahapan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Tahapan'
            }
        })
        
         // master orgapprovalpath
        .state('app.promise.procurement-master-approval-path', {
                url: '/procurement/master/approvalpath',
                templateUrl: Route.base('/promise/procurement/master/approvalpath/approvalpath.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Approval Path'
                }
            })
        .state('app.promise.procurement-master-approvalpath-view', {
            url: '/procurement/master/approvalpath/view',
            templateUrl: Route.base('/promise/procurement/master/approvalpath/approvalpath.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
            },
            params: {
            	approvalPath: null,
                toDo: null
            },
            data: {
                displayName: 'Approval Path'
            }
        })
        
	          // laporan catalog
	        .state('app.promise.procurement-laporan-catalog-index', {
	            url: '/procurement/catalog',
	            templateUrl: Route.base('promise/procurement/laporan/laporancatalog.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report Catalog'
	            }
	        })
	        
	         // laporan catalog
	        .state('app.promise.procurement-rating-delivery-receipt', {
	            url: '/procurement/ratingdeliveryreceipt',
	            templateUrl: Route.base('promise/procurement/rating/ratingdeliveryreceipt.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Rating Delivery Receipt'
	            }
	        })
	        
	        // laporan vendor
	        .state('app.promise.procurement-laporan-vendor-index', {
	            url: '/procurement/vendor',
	            templateUrl: Route.base('promise/procurement/laporan/laporanvendor.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report Vendor'
	            }
	        })
	        
	          // laporan Purchase Order
	        .state('app.promise.procurement-laporan-purchase-order-index', {
	            url: '/procurement/catalog',
	            templateUrl: Route.base('promise/procurement/laporan/laporanpurchaseorder.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report PurchaseOrder'
	            }
	        })
	        
	        // laporan catalog contrack
	        .state('app.promise.procurement-laporan-catalog-contrack-index', {
	            url: '/procurement/catalog',
	            templateUrl: Route.base('promise/procurement/laporan/laporancatalogcontrack.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Catalog Contract Report'
	            }
	        })
	        
	        // laporan catalog pembelian
	        .state('app.promise.procurement-laporan-pembelian', {
	            url: '/procurement/laporanpembelian',
	            templateUrl: Route.base('promise/procurement/laporan/laporanpembelian.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Purchase Order Processing Report'
	            }
	        })
        
	        // laporan budget dan realisasi
	        .state('app.promise.procurement-laporan-budget-dan-realisasi-index', {
	            url: '/procurement/budgetdanrealisasi',
	            templateUrl: Route.base('promise/procurement/laporan/laporanbudgetdanrealisasi.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report Budget Dan Realisasi'
	            }
	        })
	     	         
	        // laporan evaluasi kinerja vendor
	        .state('app.promise.procurement-laporan-evaluasi-kinerja-vendor-index', {
	            url: '/procurement/evaluasikinerjavendor',
	            templateUrl: Route.base('promise/procurement/laporan/laporanevaluasikinerjavendor.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report Evaluasi Kinerja Vendor'
	            }
	        })
	        .state('app.promise.procurement-laporan-evaluasi-kinerja-vendor-detail', {
	            url: '/procurement/evaluasikinerjavendor',
	            templateUrl: Route.base('promise/procurement/laporan/laporanevaluasikinerjavendor.detail.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            params: {
	            	evaluasiKinerjaVendor: null
                },
	            data: {
	                displayName: 'Report Detail Evaluasi Kinerja Vendor '
	            }
	        })
	        
	        // laporan item katalog po
	        .state('app.promise.procurement-laporan-item-katalog-po-index', {
	            url: '/procurement/itemkatalogpo',
	            templateUrl: Route.base('promise/procurement/laporan/laporanitemkatalogpo.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Report Item Catalog PO'
	            }
	        })
	        
        .state('app.promise.procurement-master-tahapan-view', {
            url: '/procurement/master/tahapan/view',
            templateUrl: Route.base('/promise/procurement/master/tahapan/tahapan.view.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
            },
            params : {
				tahapan : null,
				toDo: null
			},
            data: {
                displayName: 'Tahapan'
            }
        })

        // master registrasi jadwal tahapan pengadaan
        .state('app.promise.procurement-master-registrasijadwaltahapanpengadaan', {
                url: '/procurement/master/registrasijadwaltahapanpengadaan',
                templateUrl: Route.base('/promise/procurement/master/registrasijadwaltahapanpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Registrasi Jadwal Tahapan Pengadaan'
                }
            })
            .state('app.promise.procurement-master-registrasijadwaltahapanpengadaan-tambah', {
                url: '/procurement/master/registrasijadwaltahapanpengadaan/tambah',
                templateUrl: Route.base('/promise/procurement/master/registrasijadwaltahapanpengadaan/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Registrasi Jadwal Tahapan Pengadaan'
                }
            })
            .state('app.promise.procurement-master-registrasijadwaltahapanpengadaan-ubah', {
                url: '/procurement/master/registrasijadwaltahapanpengadaan/ubah',
                templateUrl: Route.base('/promise/procurement/master/registrasijadwaltahapanpengadaan/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Registrasi Jadwal Tahapan Pengadaan'
                }
            })

        // master kategori pengadaan
        .state('app.promise.procurement-master-kategoripengadaan', {
                url: '/procurement/master/kategoripengadaan',
                templateUrl: Route.base('/promise/procurement/master/kategoripengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kategori Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kategoripengadaan-tambah', {
                url: '/procurement/master/kategoripengadaan/tambah',
                templateUrl: Route.base('/promise/procurement/master/kategoripengadaan/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kategori Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kategoripengadaan-ubah', {
                url: '/procurement/master/kategoripengadaan/ubah',
                templateUrl: Route.base('/promise/procurement/master/kategoripengadaan/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kategori Pengadaan'
                }
            })

        // master kondisi pengadaan
        .state('app.promise.procurement-master-kondisipengadaan', {
                url: '/procurement/master/kondisipengadaan',
                templateUrl: Route.base('/promise/procurement/master/kondisipengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kondisi Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kondisipengadaan-tambah', {
                url: '/procurement/master/kondisipengadaan/tambah',
                templateUrl: Route.base('/promise/procurement/master/kondisipengadaan/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kondisi Pengadaan'
                }
            })
            .state('app.promise.procurement-master-kondisipengadaan-ubah', {
                url: '/procurement/master/kondisipengadaan/ubah',
                templateUrl: Route.base('/promise/procurement/master/kondisipengadaan/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Kondisi Pengadaan'
                }
            })

        // master termin kontrak
        .state('app.promise.procurement-master-terminkontrak', {
                url: '/procurement/master/terminkontrak',
                templateUrl: Route.base('/promise/procurement/master/terminkontrak/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Termin Kontrak'
                }
            })
            .state('app.promise.procurement-master-terminkontrak-tambah', {
                url: '/procurement/master/terminkontrak/tambah',
                templateUrl: Route.base('/promise/procurement/master/terminkontrak/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Termin Kontrak'
                }
            })
            .state('app.promise.procurement-master-terminkontrak-ubah', {
                url: '/procurement/master/terminkontrak/ubah',
                templateUrl: Route.base('/promise/procurement/master/terminkontrak/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Termin Kontrak'
                }
            })

        // master vendor requirement
        .state('app.promise.procurement-master-vendorrequirement', {
                url: '/procurement/master/vendorrequirement',
                templateUrl: Route.base('/promise/procurement/master/vendorrequirement/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Vendor Requirement'
                }
            })
            .state('app.promise.procurement-master-vendorrequirement-tambah', {
                url: '/procurement/master/vendorrequirement/tambah',
                templateUrl: Route.base('/promise/procurement/master/vendorrequirement/add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Vendor Requirement'
                }
            })
            .state('app.promise.procurement-master-vendorrequirement-ubah', {
                url: '/procurement/master/vendorrequirement/ubah',
                templateUrl: Route.base('/promise/procurement/master/vendorrequirement/edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Vendor Requirement'
                }
            })

        // master sub bidang usaha
        .state('app.promise.procurement-master-subbidangusaha', {
            url: '/procurement/master/subbidangusaha',
            templateUrl: Route.base('/promise/procurement/master/subbidangusaha/subbidangusaha.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Sub Business Field'
            }
        })
            
        .state('app.promise.procurement-master-subbidangusaha-view', {
            url: '/procurement/master/subbidangusaha/view',
            templateUrl: Route.base('/promise/procurement/master/subbidangusaha/subbidangusaha.view.html'),
            params:{
                subBidangUsaha: null,
                toDo: null
            },
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'Sub Business Field'
            }
        })

        // master item
        .state('app.promise.procurement-master-item', {
              url: '/procurement/master/item',
              templateUrl: Route.base('/promise/procurement/master/item/item.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Item'
              }
          })
          .state('app.promise.procurement-master-item-view', {
              url: '/procurement/master/item/view',
              templateUrl: Route.base('/promise/procurement/master/item/item.view.html'),
              resolve: {
                  assets: Route.require('ui.select', 'angularBootstrapNavTree')
              },
              params : {
               item : null,
               toDo: null
            },
              data: {
                  displayName: 'Item'
              }
          })

        // master organisasi
        .state('app.promise.procurement-master-organisasi', {
            url: '/procurement/master/organisasi',
            templateUrl: Route.base('/promise/procurement/master/organisasi/organisasi.index.html'),
            resolve: {
                assets: Route.require('angularBootstrapNavTree')
            },
            data: {
                displayName: 'Organisasi'
            }
        })

        // master parameter
        .state('app.promise.procurement-master-parameter', {
            url: '/procurement/master/parameter',
            templateUrl: Route.base('/promise/procurement/master/parameter/parameter.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Parameter'
            }
        })

        .state('app.promise.procurement-master-parameter-view', {
            url: '/procurement/master/parameter/view',
            templateUrl: Route.base('/promise/procurement/master/parameter/parameter.view.html'),
            params:{
              parameter: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Parameter'
            }
        })

        // master formula penilaian
    	.state('app.promise.procurement-master-formulapenilaian', {
        	url: '/procurement/master/formulapenilaian',
        	templateUrl: Route.base('/promise/procurement/master/formulapenilaian/formulapenilaian.index.html'),
        	resolve: {
            	assets: Route.require('datatables')
        	},
        	data: {
            	displayName: 'Formula Penilaian'
        	}
    	})

    	.state('app.promise.procurement-master-formulapenilaian-view', {
        	url: '/procurement/master/formulapenilaian/view',
        	templateUrl: Route.base('/promise/procurement/master/formulapenilaian/formulapenilaian.view.html'),
        	resolve: {
            	assets: Route.require('datatables')
        	},
        	data: {
            	displayName: 'Formula Penilaian'
        	}
    	})

        // master indikator penilaian
       .state('app.promise.procurement-master-indikatorpenilaian', {
            url: '/procurement/master/indikatorpenilaian',
            templateUrl: Route.base('/promise/procurement/master/indikatorpenilaian/indikatorpenilaian.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Indikator Penilaian'
            }
        })

       .state('app.promise.procurement-master-indikatorpenilaian-view', {
            url: '/procurement/master/indikatorpenilaian/view',
            templateUrl: Route.base('/promise/procurement/master/indikatorpenilaian/indikatorpenilaian.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            params: {
                indikatorPenilaian: null,
                toDo: null
            },
            data: {
                displayName: 'Indikator Penilaian'
            }
        })



        // master admin sla
        .state('app.promise.procurement-master-adminslavendor', {
            url: '/procurement/master/adminslavendor',
            templateUrl: Route.base('/promise/procurement/master/adminslavendor/adminslavendor.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Admin Vendor Sla'
            }
        })

        // master admin sla view
        .state('app.promise.procurement-master-adminslavendor-view', {
            url: '/procurement/master/adminslavendor/view',
            templateUrl: Route.base('/promise/procurement/master/adminslavendor/adminslavendor.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            params: {
              slaVendorProses: null,
              toDo: null
          },
            data: {
                displayName: 'Admin Vendor Sla'
            }
        })

        // master bidang usaha
        .state('app.promise.procurement-master-bidangusaha', {
            url: '/procurement/master/bidangusaha',
            templateUrl: Route.base('/promise/procurement/master/bidangusaha/bidangusaha.index.html'),
            resolve: {
                assets: Route.require('angularBootstrapNavTree')
            },
            data: {
                displayName: 'Bidang Usaha'
            }
        
        })

        // blacklist vendor
        .state('app.promise.procurement-manajemenvendor-blacklistvendor', {
            url: '/procurement/manajemenvendor/blacklistvendor',
            templateUrl: Route.base('/promise/procurement/blacklistvendor/blacklistvendor.index.html'),
            resolve: {
                assets: Route.require('datatables', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Blacklist Vendor'
            }
        })

        .state('app.promise.procurement-manajemenvendor-blacklistvendor-view', {
            url: '/procurement/manajemenvendor/blacklistvendor/view',
            templateUrl: Route.base('/promise/procurement/blacklistvendor/blacklistvendor.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Blacklist Vendor'
            },
            params: {
            	blacklistVendor: null,
            	toDo : null
            }
        })
                
        .state('app.promise.procurement-manajemenvendor-blacklistvendor-add', {
            url: '/procurement/manajemenvendor/blacklistvendor/add',
            templateUrl: Route.base('/promise/procurement/blacklistvendor/blacklistvendor.add.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Blacklist Vendor'
            }
        })

        // master alasan blacklist
        .state('app.promise.procurement-master-alasanblacklist', {
            url: '/procurement/master/alasanblacklist',
            templateUrl: Route.base('/promise/procurement/master/alasanblacklist/alasanblacklist.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Alasan Blacklist'
            }
        })
        
        .state('app.promise.procurement-master-alasanblacklist-view', {
            url: '/procurement/master/alasanblacklist/view',
            templateUrl: Route.base('/promise/procurement/master/alasanblacklist/alasanblacklist.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            params : {
		        alasanBlacklist : null,
		        toDo: null
		    },
            data: {
                displayName: 'Alasan Blacklist'
            }
        })

        // master Item Group
        .state('app.promise.procurement-master-itemgroup', {
            url: '/procurement/master/itemgroup',
            templateUrl: Route.base('/promise/procurement/master/itemgroup/itemgroup.index.html'),
            resolve: {
                assets: Route.require('angularBootstrapNavTree')
            },
            data: {
                displayName: 'Item Group'
            }
        })

        // master jenis pengadaan
        .state('app.promise.procurement-master-jenisPengadaan', {
            url: '/procurement/master/jenisPengadaan',
            templateUrl: Route.base('/promise/procurement/master/jenispengadaan/jenispengadaan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Jenis Pengadaan'
            }
        })
            
        .state('app.promise.procurement-master-jenispengadaan-view', {
	        url: '/procurement/master/jenisPengadaan/view',
	        templateUrl: Route.base('/promise/procurement/master/jenispengadaan/jenispengadaan.view.html'),
	        data: {
	            displayName: 'Jenis Pengadaan'
	        },
	        params : {
	        	jenisPengadaan: null,
	        	toDo : null
	        }
        })
            
         // master hari libur
        .state('app.promise.procurement-master-harilibur', {
                url: '/procurement/master/harilibur',
                templateUrl: Route.base('/promise/procurement/master/harilibur/harilibur.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Holiday'
                }
            })
         .state('app.promise.procurement-master-harilibur-view', {
                url: '/procurement/master/harilibur/view',
                templateUrl: Route.base('/promise/procurement/master/harilibur/harilibur.view.html'),
                params: {
                    hariLibur: null,
                    toDo: null
                },
                data: {
                    displayName: 'Holiday'
                }
            })


         // master metode penyampaian dokumen pengadaan
         .state('app.promise.procurement-master-metodePenyampaianDokumenPengadaan', {
            url: '/procurement/master/metodepenyampaiandokumenpengadaan',
            templateUrl: Route.base('/promise/procurement/master/metodepenyampaiandokumen/metodepenyampaiandokumenpengadaan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Metode Penyampaian Dokumen'
            }
        })
        .state('app.promise.procurement-master-metodePenyampaianDokumenPengadaan-view', {
            url: '/procurement/master/metodepenyampaiandokumenpengadaan/view',
            templateUrl: Route.base('/promise/procurement/master/metodepenyampaiandokumen/metodepenyampaiandokumenpengadaan.view.html'),
            params: {
              metodePenyampaianDokumenPengadaan: null,
                toDo: null
            },
            data: {
                displayName: 'Metode Penyampaian Dokumen View'
            }
        })

        // master metodepengadaan
        .state('app.promise.procurement-master-metodePengadaan', {
            url: '/procurement/master/metodepengadaan',
            templateUrl: Route.base('/promise/procurement/master/metodepengadaan/metodepengadaan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Metode Pengadaan'
            }
        })

        .state('app.promise.procurement-master-metodePengadaan-view', {
            url: '/procurement/master/metodepengadaan/view',
            templateUrl: Route.base('/promise/procurement/master/metodepengadaan/metodepengadaan.view.html'),
            data: {
                displayName: 'Metode Pengadaan'
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
            },
            params : {
		        metodePengadaan : null,
		        toDo: null
		    }
        })

        // master satuan
        .state('app.promise.procurement-master-satuan', {
                url: '/procurement/master/satuan',
                templateUrl: Route.base('/promise/procurement/master/satuan/satuan.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Unit'
                }
            })

        .state('app.promise.procurement-master-satuan-view', {
                url: '/procurement/master/satuan/view',
                templateUrl: Route.base('/promise/procurement/master/satuan/satuan.view.html'),
                params: {
                    satuan: null,
                    toDo:null
                },
                data: {
                    displayName: 'Unit'
                }
            })

        // master kriteria administrasi
        .state('app.promise.procurement-master-kriteriaAdministrasi', {
                url: '/procurement/master/kriteriaAdministrasi',
                templateUrl: Route.base('/promise/procurement/master/kriteriaadministrasi/kriteriaadministrasi.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Kriteria Administrasi'
                }
            })
            .state('app.promise.procurement-master-kriteriaAdministrasi-view',{
              url:'/procurement/master/kriteriaAdministrasi/view',
              templateUrl: Route.base('/promise/procurement/master/kriteriaadministrasi/kriteriaadministrasi.view.html'),
              data: {
                displayName: 'Kriteria Administrasi'
              },
              params : {
                kriteriaAdministrasi: null,
                toDo : null
              }
              
            })


        // master kriteria kualifikasi administrasi
        .state('app.promise.procurement-master-kriteriaKualifikasiAdministrasi', {
                url: '/procurement/master/kriteriaKualifikasiAdministrasi',
                templateUrl: Route.base('/promise/procurement/master/kriteriakualifikasiadministrasi/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                }, data: {
                    displayName: 'Kriteria Kualifikasi Administrasi'
                }
            })
            .state('app.promise.procurement-master-kriteriaKualifikasiAdministrasi-tambah', {
                url: '/procurement/master/kriteriaKualifikasiAdministrasi/tambah',
                templateUrl: Route.base('/promise/procurement/master/kriteriakualifikasiadministrasi/add.html'), 
                data: {
                    displayName: 'Kriteria Kualifikasi Administrasi'
                }
            })

        .state('app.promise.procurement-master-kriteriaKualifikasiAdministrasi-edit', {
            url: '/procurement/master/kriteriaKualifikasiAdministrasi/edit',
            templateUrl: Route.base('/promise/procurement/master/kriteriakualifikasiadministrasi/edit.html'), 
            data: {
                displayName: 'Kriteria Kualifikasi Administrasi'
            }
        })

        // master kriteria teknis
        .state('app.promise.procurement-master-kriteriaTeknis', {
                url: '/procurement/master/kriteriateknis',
                templateUrl: Route.base('/promise/procurement/master/kriteriateknis/kriteriateknis.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                }, 
                data: {
                    displayName: 'Kriteria Teknis'
                }
        })
                               
        .state('app.promise.procurement-master-kriteriaTeknis-view', {
                url: '/procurement/master/kriteriateknis/view',
                templateUrl: Route.base('/promise/procurement/master/kriteriateknis/kriteriateknis.view.html'), 
                params: {
                    kriteriaTeknis: null,
                    toDo: null
                },
                data: {
                    displayName: 'Kriteria Teknis'
                }
        })

         // master pelaksana pengadaan
        .state('app.promise.procurement-master-pelaksanapengadaan', {
                url: '/procurement/master/pelaksanapengadaan',
                templateUrl: Route.base('/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                }, 
                data: {
                    displayName: 'Pelaksana Pengadaan'
                }
            })
            .state('app.promise.procurement-master-pelaksanapengadaan-viewteam', {
                url: '/procurement/master/pelaksanapengadaan/viewteam',
                templateUrl: Route.base('/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.viewteam.html'),
                params:{
                	toDo:null,
                	panitiaDetail:null
                },
                resolve: {
                    assets: Route.require('ui.select', 'angularBootstrapNavTree')
                }, 
                data: {
                    displayName: 'Pelaksana Pengadaan Team'
                }
         
            })
            .state('app.promise.procurement-master-pelaksanapengadaan-viewpejabat', {
                url: '/procurement/master/pelaksanapengadaan/viewpejabat',
                templateUrl: Route.base('/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.viewpejabat.html'),
                params:{
                	toDo:null,
                	panitiaDetail:null
                },
                resolve: {
                    assets: Route.require('ui.select', 'angularBootstrapNavTree')
                }, 
                data: {
                    displayName: 'Pelaksana Pengadaan Pejabat'
                }
            })

        // Master Address Book for Panitia only
        .state('app.promise.procurement-addressbook-index', {
                url: '/addressbook/index',
                templateUrl: Route.base('/promise/procurement/master/addressbook/addressbook.index.html'),
                resolve: {
                  assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Master Alamat'
                }
            })
        .state('app.promise.procurement-addressbook-view', {
                url: '/addressbook/view',
                templateUrl: Route.base('/promise/procurement/master/addressbook/addressbook.view.html'),
                resolve: {
                  assets: Route.require('datatables')
              },
                data: {
                    displayName: 'Master Alamat'
                },
                params: {
                  addressBook: null,
                  dataAddressBook:null,
                  toDo : null
                }
            })

        // Master WS Manager
        .state('app.promise.procurement-webservicemanager-index', {
            url: '/procurement/master/webservicemanager',
            templateUrl: Route.base('/promise/procurement/master/webservicemanager/webservicemanager.index.html'),
            resolve: {
                assets: Route.require('datatables')
            }, 
            data: {
                displayName: 'Web Services Manager'
            }
        })
        .state('app.promise.procurement-webservicemanager-add', {
            url: '/procurement/master/webservicemanager/add',
            templateUrl: Route.base('/promise/procurement/master/webservicemanager/webservicemanager.add.html'), 
            data: {
                displayName: 'Web Services Manager'
            }
        })
        .state('app.promise.procurement-webservicemanager-edit', {
            url: '/procurement/master/webservicemanager/edit',
            templateUrl: Route.base('/promise/procurement/master/webservicemanager/webservicemanager.edit.html'), 
            data: {
                displayName: 'Web Services Manager'
            }
        })
        

        // panitia --> Jadwal Pengadaan
        .state('app.promise.procurement-jadwalpengadaan', {
                url: '/procurement/jadwalpengadaan',
                templateUrl: Route.base('/promise/procurement/jadwalpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Jadwal Pengadaan'
                }
            })
            .state('app.promise.procurement-jadwalpengadaan-detil', {
                url: '/procurement/jadwalpengadaan/detil',
                templateUrl: Route.base('promise/procurement/jadwalpengadaan/detil.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Jadwal Pengadaan'
                }
            })
            .state('app.promise.procurement-jadwalpengadaan-detil2', {
                url: '/procurement/jadwalpengadaan/detil2',
                templateUrl: Route.base('promise/procurement/jadwalpengadaan/detil2.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Jadwal Pengadaan '
                }
            })

        // panitia --> index Pemasukan Penawaran
        .state('app.promise.procurement-panitia-pemasukanPenawaran', {
            url: '/procurement/pemasukanPenawaran',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/pemasukanPenawaran.html'),
            params: {
                tahapanId: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pemasukan Penawaran'
            }
        })

        // Pemasukan Penawaran Detail Satuan
        .state('app.promise.procurement-panitia-pemasukanPenawaranDetailSatuan', {
            url: '/procurement/pemasukanPenawaran/detailSatuan',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/pemasukanPenawaranDetailSatuan.html'),
            data: {
                displayName: 'Pemasukan Penawaran'
            }
        })

        // View Pemasukan Penawaran
        .state('app.promise.procurement-panitia-pemasukanPenawaranViewSatuan', {
            url: '/procurement/pemasukanPenawaran/viewSatuan',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/pemasukanPenawaranViewSatuan.html'),
            params: {
                tahapanId: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pemasukan Penawaran'
            }
        })

        .state('app.promise.procurement-panitia-pemasukanPenawaranViewTotal', {
            url: '/procurement/pemasukanPenawaran/viewTotal',
            templateUrl: Route.base('/promise/procurement/pemasukanpenawaran/pemasukanPenawaranViewTotal.html'),
            params: {
                tahapanId: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pemasukan Penawaran'
            }
        })

        // Pembukaan Penawaran
        .state('app.promise.procurement-panitia-pembukaanPenawaran', {
            url: '/procurement/pembukaaanPenawaran',
            templateUrl: Route.base('/promise/procurement/pembukaanpenawaran/index.html'),
            params: {
                tahapanId: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        .state('app.promise.procurement-panitia-pembukaanPenawaran-total', {
            url: '/procurement/pembukaaanPenawaranTotal',
            templateUrl: Route.base('promise/procurement/pembukaanpenawaran/detil.total.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })
        
        .state('app.promise.procurement-panitia-pembukaanPenawaran-total-cetak', {
            url: '/procurement/viewPembukaanPenawaranTotal',
            templateUrl: Route.base('promise/procurement/pembukaanpenawaran/detil.total.cetak.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        .state('app.promise.procurement-panitia-pembukaanPenawaran-satuan', {
            url: '/procurement/pembukaanPenawaranSatuan',
            templateUrl: Route.base('promise/procurement/pembukaanpenawaran/detil.satuan.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        // pembukaan penawaran dua tahap
        .state('app.promise.procurement-panitia-pembukaanPenawaranduatahap-tahapsatu', {
            url: '/procurement/pembukaaanPenawaranDuaTahap/TahapSatu',
            templateUrl: Route.base('/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/index.html'),
            params: {
                tahapanId: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        .state('app.promise.procurement-panitia-pembukaanPenawaranduatahap-total-tahapsatu', {
            url: '/procurement/pembukaaanPenawaranDuaTahap/TahapSatuTotal',
            templateUrl: Route.base('/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/detil.total.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('ngTable', 'xeditable')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        .state('app.promise.procurement-panitia-pembukaanPenawaranduatahap-satuan-tahapsatu', {
            url: '/procurement/pembukaanPenawaranDuaTahap/TahapSatuSatuan',
            templateUrl: Route.base('/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/detil.satuan.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('ngTable', 'xeditable')
            },
            data: {
                displayName: 'Pembukaan Penawaran'
            }
        })

        // panitia
        // Route of Kriteria Evaluasi (Index)
        .state('app.promise.procurement-panitia-kriteriaEvaluasi', {
            url: '/procurement/kriteriaEvaluasi',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.index.html'),
            resolve: {
                assets: Route.require('datatables', 'toaster')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-view', {
            url: '/procurement/kriteriaEvaluasi/view',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.total.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-view-satuan', {
            url: '/procurement/kriteriaEvaluasi/viewSatuan',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.satuan.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-addView', {
            url: '/procurement/kriteriaEvaluasi/addView',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.total.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-addViewSatuan', {
            url: '/procurement/kriteriaEvaluasi/addViewSatuan',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.satuan.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-addMeritView', {
            url: '/procurement/kriteriaEvaluasi/addMeritView',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.merit.total.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-addMeritViewSatuan', {
            url: '/procurement/kriteriaEvaluasi/addMeritViewSatuan',
            templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.merit.satuan.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Evaluasi'
            }
        })

        .state('app.promise.procurement-panitia-kriteriaEvaluasi-satuan', {
                url: '/procurement/kriteriaEvaluasi/satuan',
                templateUrl: Route.base('/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.satuandetail.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Kriteria Evaluasi'
                }
            })
            // ============================================================================================================

        // Evaluasi Administrasi
        .state('app.promise.procurement-panitia-evaluasiAdministrasi', {
            url: '/procurement/evaluasiAdministrasi',
            templateUrl: Route.base('/promise/procurement/evaluasiadministrasi/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Evaluasi Administrasi'
            }
        
        })

        .state('app.promise.procurement-panitia-evaluasiAdministrasi-edit', {
            url: '/procurement/evaluasiAdministrasi/edit',
            templateUrl: Route.base('promise/procurement/evaluasiadministrasi/edit.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Evaluasi Administrasi'
            }
        })
        
        .state('app.promise.procurement-panitia-evaluasiAdministrasi-cetak', {
            url: '/procurement/evaluasiAdministrasi/viewEvaluasiAdministrasi',
            templateUrl: Route.base('promise/procurement/evaluasiadministrasi/cetak.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Evaluasi Administrasi'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiAdministrasi-edit-satuan', {
            url: '/procurement/evaluasiAdministrasi/edit.satuan',
            templateUrl: Route.base('promise/procurement/evaluasiadministrasi/edit.satuan.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Evaluasi Administrasi'
            }
        })

        // penetapan pemenang
        .state('app.promise.procurement-panitia-penetapanpemenang', {
                url: '/procurement/penetapanpemenang',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penetapanpemenang-totalsistemgugur', {
                url: '/procurement/penetapanpemenang/totalSistemGugur',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/total.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            
            .state('app.promise.procurement-panitia-penetapanpemenang-viewtotalsistemgugur', {
                url: '/procurement/penetapanpemenang/viewTotalSistemGugur',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/total.sistemgugur.cetak.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            
            
            .state('app.promise.procurement-panitia-penetapanpemenang-totalmeritpoint', {
                url: '/procurement/penetapanpemenang/totalMeritPoint',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/total.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            
            .state('app.promise.procurement-panitia-penetapanpemenang-totalmeritpoint-cetak', {
                url: '/procurement/penetapanpemenang/viewTotalMeritPoint',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/total.meritpoint.cetak.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            
            .state('app.promise.procurement-panitia-penetapanpemenang-satuansistemgugur', {
                url: '/procurement/penetapanpemenang/satuanSistemGugur',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/satuan.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penetapanpemenang-satuanmeritpoint', {
                url: '/procurement/penetapanpemenang/satuanMeritPoint',
                templateUrl: Route.base('/promise/procurement/penetapanpemenang/satuan.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penetapan Pemenang'
                }
            })

        // penetapan pemenang multiwinner
        .state('app.promise.procurement-panitia-penetapanpemenang-multiwinner', {
            url: '/procurement/penetapanpemenang/multiwinner',
            templateUrl: Route.base('/promise/procurement/penetapanpemenang/penetapanpemenang.multiwinner.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Penetapan Pemenang Multiwinner'
            }
        })

        // penunjukan pemenang
        .state('app.promise.procurement-panitia-penunjukanpemenang', {
                url: '/procurement/penunjukanpemenang',
                templateUrl: Route.base('/promise/procurement/penunjukanpemenang/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Penunjukan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penunjukanpemenang-totalsistemgugur', {
                url: '/procurement/penunjukanpemenang/totalSistemGugur',
                templateUrl: Route.base('/promise/procurement/penunjukanpemenang/total.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penunjukan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penunjukanpemenang-totalmeritpoint', {
                url: '/procurement/penunjukanpemenang/totalMeritPoint',
                templateUrl: Route.base('/promise/procurement/penunjukanpemenang/total.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penunjukan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penunjukanpemenang-satuansistemgugur', {
                url: '/procurement/penunjukanpemenang/satuanSistemGugur',
                templateUrl: Route.base('/promise/procurement/penunjukanpemenang/satuan.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penunjukan Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-penunjukanpemenang-satuanmeritpoint', {
                url: '/procurement/penunjukanpemenang/satuanMeritPoint',
                templateUrl: Route.base('/promise/procurement/penunjukanpemenang/satuan.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Penunjukan Pemenang'
                }
            })

        // usulan calon pemenang
        .state('app.promise.procurement-panitia-usulancalonpemenang', {
                url: '/procurement/usulancalonpemenang',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-usulancalonpemenang-totalsistemgugur', {
                url: '/procurement/usulancalonpemenang/totalsistemgugur',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/total.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-usulancalonpemenang-totalmeritpoint', {
                url: '/procurement/usulancalonpemenang/totalmeritpoint',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/total.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-usulancalonpemenang-totalmeritpoint-cetak', {
                url: '/procurement/usulancalonpemenang/viewtotalmeritpoint',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/total.meritpoint.cetak.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })
            
            
            .state('app.promise.procurement-panitia-usulancalonpemenang-satuansistemgugur', {
                url: '/procurement/usulancalonpemenang/satuansistemgugur',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/satuan.sistemgugur.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })
            .state('app.promise.procurement-panitia-usulancalonpemenang-satuanmeritpoint', {
                url: '/procurement/usulancalonpemenang/satuanmeritpoint',
                templateUrl: Route.base('/promise/procurement/usulancalonpemenang/satuan.meritpoint.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Usulan Calon Pemenang'
                }
            })


        // Route of Evaluasi Teknis (Index)
        .state('app.promise.procurement-panitia-evaluasiteknis', {
            url: '/procurement/evaluasiteknis',
            templateUrl: Route.base('/promise/procurement/evaluasiteknis/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Evaluasi Teknis'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiteknis-view-total', {
            url: '/procurement/evaluasiteknis/view/total',
            templateUrl: Route.base('/promise/procurement/evaluasiteknis/total/index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Teknis'
            }
        })
        
        .state('app.promise.procurement-panitia-evaluasiteknis-view-totalcetak', {
            url: '/procurement/evaluasiteknis/view/viewTotal',
            templateUrl: Route.base('/promise/procurement/evaluasiteknis/total/cetak.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Teknis'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiteknis-view-satuan', {
            url: '/procurement/evaluasiteknis/view/satuan',
            templateUrl: Route.base('/promise/procurement/evaluasiteknis/satuan/index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Teknis'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiteknis-view-total-sistemgugur', {
            url: '/procurement/evaluasiteknis/view/total/sistemgugur',
            templateUrl: Route.base('/promise/procurement/evaluasiteknis/total/sistemgugur/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Evaluasi Teknis'
            }
        })

        // panitia
        // Route of Klarifikasi Harga (Index)
        .state('app.promise.procurement-panitia-klarifikasiharga', {
            url: '/procurement/klarifikasiharga',
            templateUrl: Route.base('/promise/procurement/klarifikasiharga/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Klarifikasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-klarifikasiharga-total', {
            url: '/procurement/klarifikasiharga/total',
            templateUrl: Route.base('/promise/procurement/klarifikasiharga/total/klarifikasiharga.total.index.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Klarifikasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-klarifikasiharga-satuan', {
            url: '/procurement/klarifikasiharga/satuan',
            templateUrl: Route.base('/promise/procurement/klarifikasiharga/satuan/klarifikasiharga.satuan.index.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Klarifikasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-klarifikasiharga-satuan-detil', {
            url: '/procurement/klarifikasiharga/satuan/detil',
            templateUrl: Route.base('promise/procurement/klarifikasiharga/satuan/klarifikasiharga.satuan.detil.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Klarifikasi Harga'
            }
        })

        // panitia
        // Route of Penjelasan (Index)
        .state('app.promise.procurement-panitia-penjelasan', {
            url: '/procurement/penjelasan',
            templateUrl: Route.base('/promise/procurement/penjelasan/penjelasan.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Panitia Penjelasan'
            }
        })

        .state('app.promise.procurement-panitia-penjelasan-detail', {
            url: '/procurement/penjelasan/detail',
            templateUrl: Route.base('/promise/procurement/penjelasan/penjelasan.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Panitia Penjelasan'
            }
        })


        // Route of Negosiasi
        .state('app.promise.procurement-panitia-negosiasi', {
            url: '/procurement/negosiasi',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        // Route of Negosiasi Total

        // Route of Negosiasi Total - Panitia

        .state('app.promise.procurement-panitia-negosiasi-total-detail', {
            url: '/procurement/negosiasi/total/detail/:pengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.total.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('app.promise.procurement-panitia-negosiasi-total-detail-more', {
            url: '/procurement/negosiasi/total/detail/more/:pengadaanId/:vendorId/:negosiasiId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.total.detail.more.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('app.promise.procurement-panitia-negosiasi-total-detail-history', {
            url: '/procurement/negosiasi/total/detail/history/:pengadaanId/:vendorId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.total.detail.history.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })
        
        // Cetak negosiasi total
        .state('app.promise.procurement-panitia-negosiasi-total-detail-cetak', {
            url: '/procurement/negosiasi/total/detail/cetak/:pengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.total.detail.cetak.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        // Route of Negosiasi Total - Vendor

        .state('appvendor.promise.procurement-vendor-negosiasi', {
            url: '/procurement/vendor/negosiasi',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.vendor.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('appvendor.promise.procurement-vendor-negosiasi-total-detail', {
            url: '/procurement/negosiasi/vendor/total/detail/:pengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.vendor.total.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle', 'xeditable')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('appvendor.promise.procurement-vendor-negosiasi-satuan-detail', {
            url: '/procurement/negosiasi/vendor/satuan/detail/:pengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.vendor.satuan.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle', 'xeditable')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })


        // Route of Negosiasi Satuan
        // Route of Negosiasi Satuan - Panitia

        .state('app.promise.procurement-panitia-negosiasi-satuan-detail', {
            url: '/procurement/negosiasi/satuan/detail/:pengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.satuan.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('app.promise.procurement-panitia-negosiasi-satuan-detail-penawaran', {
            url: '/procurement/negosiasi/satuan/detail/penawaran/:pengadaanId/:itemPengadaanId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.satuan.detail.penawaran.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('app.promise.procurement-panitia-negosiasi-satuan-detail-penawaran-more', {
            url: '/procurement/negosiasi/satuan/detail/penawaran/more/:pengadaanId/:itemPengadaanId/:negosiasiId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.satuan.detail.penawaran.more.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        .state('app.promise.procurement-panitia-negosiasi-satuan-detail-penawaran-history', {
            url: '/procurement/negosiasi/satuan/detail/penawaran/history/:pengadaanId/:itemPengadaanId/:vendorId',
            templateUrl: Route.base('/promise/procurement/negosiasi/negosiasi.satuan.detail.penawaran.history.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Negosiasi'
            }
        })

        /* Sanggahan */

        /* Vendor - Index */
        .state('appvendor.promise.procurement-vendor-sanggahan', {
            url: '/procurement/vendor/sanggahan',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.vendor.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        /* Vendor - Total - Detail */
        .state('appvendor.promise.procurement-vendor-sanggahan-total-detail', {
            url: '/procurement/vendor/sanggahan/total/detail',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.vendor.total.detail.html'),
            params: {
                pengadaanId: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        /* Vendor - Satuan - Detail */
        .state('appvendor.promise.procurement-vendor-sanggahan-satuan-detail', {
            url: '/procurement/vendor/sanggahan/satuan/detail',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.vendor.satuan.detail.html'),
            params: {
                pengadaanId: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        /* Vendor - Satuan - Detail - More */
        .state('appvendor.promise.procurement-vendor-sanggahan-satuan-detail-more', {
            url: '/procurement/vendor/sanggahan/satuan/detail/more',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.vendor.satuan.detail.more.html'),
            params: {
                itemPengadaan: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })



        // Panitia
        .state('app.promise.procurement-panitia-sanggahan', {
            url: '/procurement/sanggahan',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.index.html'),
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        // Panitia - total - detail
        .state('app.promise.procurement-sanggahan-total-detail', {
            url: '/procurement/sanggahan/total/detail',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.total.detail.html'),
            params: {
                pengadaanId: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })


        // Panitia - satuan - material list
        .state('app.promise.procurement-sanggahan-satuan-listitem', {
            url: '/procurement/sanggahan/satuan/listitem/',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.satuan.listitem.html'),
            params: {
                pengadaanId: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        // Panitia - satuan - detail
        .state('app.promise.procurement-sanggahan-satuan-detail', {
            url: '/procurement/sanggahan/satuan/detil',
            templateUrl: Route.base('/promise/procurement/sanggahan/sanggahan.satuan.detail.html'),
            params: {
                itemPengadaan: null
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Sanggahan'
            }
        })

        // Route of Evaluasi Harga Simple (Index)
        // Total
        // Auction
        .state('app.promise.procurement-panitia-evaluasihargasimple-auction', {
                url: '/procurement/evaluasihargasimple/auction',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/evaluasihargasimple.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })
            .state('app.promise.procurement-panitia-evaluasihargasimple-total-view', {
                url: '/procurement/evaluasihargasimple/total/auction/view',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasihargasimple.total.view.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })
            .state('app.promise.procurement-panitia-evaluasihargasimple-total-sesiauction', {
                url: '/procurement/evaluasihargasimple/sesiauction',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasihargasimple.sesiauction.add.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })
            .state('app.promise.procurement-panitia-evaluasihargasimple-total-historyauction', {
                url: '/procurement/evaluasihargasimplesimple/historyauction',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasihargasimple.historyauction.view.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })
            .state('app.promise.procurement-panitia-evaluasihargasimple-total-diskualifikasi', {
                url: '/procurement/evaluasihargasimple/diskualifikasi',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasihargasimple.diskualifikasi.view.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })

        // Bidding
        .state('app.promise.procurement-panitia-evaluasihargasimple-bidding', {
                url: '/procurement/evaluasihargasimple/bidding',
                templateUrl: Route
                    .base('/promise/procurement/evaluasiharga/evaluasihargasimple.bidding.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })
            .state('app.promise.procurement-panitia-evaluasihargasimple-bidding-total-view', {
                url: '/procurement/evaluasihargasimple/total/bidding/view',
                templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasihargasimple.total.bidding.view.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Evaluasi Harga Simple'
                }
            })

        // Route of Evaluasi Harga (Index)
        // Total
        .state('app.promise.procurement-panitia-evaluasiharga-auction', {
            url: '/procurement/evaluasiharga/auction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/evaluasiharga.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
	        data: {
	            displayName: 'Evaluasi Harga Auction'
	        }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-bidding', {
            url: '/procurement/evaluasiharga/bidding',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/evaluasiharga.bidding.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga Bidding'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-total-view', {
            url: '/procurement/evaluasiharga/total/auction/view',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.total.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })
        
        .state('app.promise.procurement-panitia-evaluasiharga-total-cetak', {
            url: '/procurement/evaluasiharga/total/auction/viewEvaluasiHarga',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.total.cetak.html'),
            resolve: {
            	assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-total-sesiauction', {
            url: '/procurement/evaluasiharga/sesiauction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.sesiauction.add.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-total-historyauction', {
            url: '/procurement/evaluasiharga/historyauction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.historyauction.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-total-diskualifikasi', {
            url: '/procurement/evaluasiharga/diskualifikasi',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.diskualifikasi.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-bidding-total-view', {
            url: '/procurement/evaluasiharga/total/bidding/view',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/total/evaluasiharga.total.bidding.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga Bidding'
            }
        })

        // Satuan
        .state('app.promise.procurement-panitia-evaluasiharga-bidding-satuan-view', {
            url: '/procurement/evaluasiharga/satuan/bidding/view',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.bidding.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga Bidding'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-satuan-view', {
            url: '/procurement/evaluasiharga/satuan/auction/view',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-satuan-add-view', {
            url: '/procurement/evaluasiharga/satuan/auction/addview',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.add.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-satuan-sesiauction', {
            url: '/procurement/evaluasiharga/satuan/sesiauction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.sesiauction.add.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-satuan-historyauction', {
            url: '/procurement/evaluasiharga/satuan/historyauction',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.historyauction.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })

        .state('app.promise.procurement-panitia-evaluasiharga-satuan-diskualifikasi', {
            url: '/procurement/evaluasiharga/satuan/diskualifikasi',
            templateUrl: Route.base('/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.diskualifikasi.view.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Evaluasi Harga'
            }
        })


        // Route of SPK (Index)
        .state('app.promise.procurement-panitia-spk', {
            url: '/procurement/spk',
            templateUrl: Route.base('/promise/procurement/spk/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Panitia SPK'
            }
        })

        .state('app.promise.procurement-panitia-spk-view', {
            url: '/procurement/spk/view',
            templateUrl: Route.base('/promise/procurement/spk/view/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Panitia SPK'
            }
        })

        .state('app.promise.procurement-panitia-spk-index', {
            url: '/procurement/spk-index',
            templateUrl: Route.base('/promise/procurement/spk/index-spk.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Panitia SPK'
            }
        })

        // MANAJEMEN KONTRAK
        .state('app.promise.procurement-manajemenkontrak-viewdatapengadaan', {
            url: '/procurement/kontrak/indexdatapengadaan',
            templateUrl: Route.base('/promise/procurement/manajemenkontrak/indexpengadaan.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Manajemen Kontrak'
            }
        })

        .state('app.promise.procurement-manajemenkontrak-createdatakontrak', {
            url: '/procurement/kontrak/createkontrak',
            templateUrl: Route.base('/promise/procurement/manajemenkontrak/createkontrak.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'ui.select')
            },
            data: {
                displayName: 'Manajemen Kontrak'
            }
        })

        .state('app.promise.procurement-manajemenkontrak-indexdatakontrak', {
            url: '/procurement/kontrak/indexdatakontrak',
            templateUrl: Route.base('/promise/procurement/manajemenkontrak/indexkontrak.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Manajemen Kontrak'
            }
        })

        .state('app.promise.procurement-manajemenkontrak-editdatakontrak', {
                url: '/procurement/kontrak/editdatakontrak',
                templateUrl: Route.base('/promise/procurement/manajemenkontrak/editkontrak.html'),
                params: {
                    dataKontrak: null
                },
                resolve: {
                    assets: Route.require('datatables', 'ngTable', 'ui.select', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Manajemen Kontrak'
                }
            })
            // Route of Pengambilan Dokumen (Index)
            .state('app.promise.procurement-panitia-pengambilandokumen', {
                url: '/procurement/pengambilandokumen',
                templateUrl: Route.base('/promise/procurement/pengambilandokumen/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pengambilan Dokumen'
                }
            })
            .state('app.promise.procurement-panitia-pengambilandokumen-view', {
                url: '/procurement/pengambilandokumen/view',
                templateUrl: Route.base('/promise/procurement/pengambilandokumen/view/index.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Pengambilan Dokumen'
                }
            })
            // Pengambilan Dokumen Cetak
            .state('app.promise.procurement-panitia-pengambilandokumen-view-cetak', {
                url: '/procurement/pengambilandokumen/viewPengambilan',
                templateUrl: Route.base('promise/procurement/pengambilandokumen/view/pengambilandokumencetak.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable', 'ui.select', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Pengambilan Dokumen'
                }
            })

        // Route of Alokasi Anggaran (Index)
        .state('app.promise.procurement-panitia-alokasianggaran', {
            url: '/procurement/alokasianggaran',
            templateUrl: Route.base('/promise/procurement/alokasianggaran/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Alokasi Anggaran'
            }
        })

        .state('app.promise.procurement-panitia-alokasianggaran-view', {
            url: '/procurement/alokasianggaran/view',
            templateUrl: Route.base('/promise/procurement/alokasianggaran/alokasianggaran.view.html'),
            resolve: {
                assets: Route.require('ui.select')
            },
            data: {
                displayName: 'Alokasi Anggaran'
            }
        })

        .state('app.promise.procurement-panitia-alokasianggaran-gabung', {
            url: '/procurement/alokasianggaran/gabung',
            templateUrl: Route.base('/promise/procurement/alokasianggaran/alokasianggaran.gabung.html'),
            resolve: {
                assets: Route.require('ui.select', 'datatables')
            },
            data: {
                displayName: 'Alokasi Anggaran Gabung'
            }
        })

        .state('app.promise.procurement-panitia-alokasianggaran-gabung-list', {
            url: '/procurement/alokasianggaran/gabung/list',
            templateUrl: Route.base('/promise/procurement/alokasianggaran/alokasianggaran.gabung.list.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable')
            },
            data: {
                displayName: 'Alokasi Anggaran Gabung'
            }
        })

        .state('app.promise.procurement-panitia-alokasianggaran-gabung-view', {
            url: '/procurement/alokasianggaran/gabung/view',
            templateUrl: Route.base('/promise/procurement/alokasianggaran/alokasianggaran.gabung.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Alokasi Anggaran Gabung'
            }
        })

        // Alokasi Anggaran - Versi 02
        .state('app.promise.procurement-alokasianggaran-index02', {
            url: '/procurement/alokasianggaran/index02',
            templateUrl: Route.base('promise/procurement/alokasianggaran/alokasianggaran.index.02.html'),
            resolve: {
                assets: Route.require('ez-plus', 'TouchSpin', 'textAngular', 'datatables')
            },
            data: {
                displayName: 'Alokasi Anggaran'
            }
        })
        
        // Import Anggaran:
//        .state('app.promise.procurement-alokasianggaran-index02-import', {
//            url: '/procurement/alokasianggaran/index02/import',
//            templateUrl: Route.base('promise/procurement/alokasianggaran/alokasianggaran.index.02.import.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.index.02.import.controller.js');
//                    });
//            }]
//        })
        
        // Import Anggaran:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-alokasianggaran-index02-import', {
            url: '/procurement/alokasianggaran/index02/import',
            templateUrl: Route.base('promise/procurement/alokasianggaran/alokasianggaran.index.02.import.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
            }
        })

        // Perencanaan
        .state('app.promise.procurement-panitia-perencanaan', {
            url: '/procurement/perencanaan',
            templateUrl: Route.base('/promise/procurement/perencanaan/perencanaan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Perencanaan'
            }
        })

        .state('app.promise.procurement-panitia-perencanaan-detail', {
            url: '/procurement/perencanaan/detail',
            templateUrl: Route.base('/promise/procurement/perencanaan/perencanaan.detail.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Perencanaan'
            }
        })

        .state('app.promise.procurement-panitia-perencanaan-detail-add', {
            url: '/procurement/perencanaan/detail/add',
            templateUrl: Route.base('/promise/procurement/perencanaan/perencanaan.detail.add.html'),
            resolve: {
                assets: Route.require('datatables', 'ngTable', 'angularFileUpload', 'filestyle', 'ui.select')
            },
            data: {
                displayName: 'Perencanaan'
            }
        })

        // Route of Laporan Pengadaan (Index)
        .state('app.promise.procurement-panitia-historypengadaan', {
            url: '/procurement/historypengadaan',
            templateUrl: Route.base('/promise/procurement/historypengadaan/index.html'),
            resolve: {
                assets: Route.require('datatables', 'ui.select')
            },
            data: {
                displayName: 'History Pengadaan'
            }
        })

        .state('app.promise.procurement-panitia-historypengadaan-view', {
            url: '/procurement/historypengadaan/view',
            templateUrl: Route.base('/promise/procurement/historypengadaan/historypengadaan.view.html'),
            data: {
                displayName: 'History Pengadaan'
            },
            resolve: {}
        })

        // Manajemen Vendor
        .state('app.promise.procurement-manajemenvendor-penilaian', {
                url: '/procurement/manajemenvendor/penilaian',
                templateUrl: Route.base('/promise/procurement/manajemenvendor/manajemenvendor.penilaian.index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Penilaian Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-penilaian-view', {
                url: '/procurement/manajemenvendor/penilaian/view',
                templateUrl: Route.base('/promise/procurement/manajemenvendor/manajemenvendor.penilaian.view.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Penilaian Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-penilaian-edit', {
                url: '/procurement/manajemenvendor/penilaian/edit',
                templateUrl: Route.base('/promise/procurement/manajemenvendor/manajemenvendor.penilaian.edit.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Penilaian Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-penilaian-viewdetil', {
                url: '/procurement/manajemenvendor/penilaian/viewdetil',
                templateUrl: Route.base('/promise/procurement/manajemenvendor/manajemenvendor.penilaian.viewdetil.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Penilaian Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-penilaian-viewedit', {
                url: '/procurement/manajemenvendor/penilaian/viewedit',
                templateUrl: Route.base('/promise/procurement/manajemenvendor/manajemenvendor.penilaian.viewedit.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                },
                data: {
                    displayName: 'Penilaian Vendor'
                }
            })

        // Manajemen Vendor
        .state('app.promise.procurement-manajemenvendor-rekaphistoryvendor', {
                url: '/procurement/rekaphistoryvendor/rekaphistoryvendor',
                templateUrl: Route.base('/promise/procurement/rekaphistoryvendor/rekaphistoryvendor.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Rekap History Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-rekaphistoryvendor-view', {
                url: '/procurement/rekaphistoryvendor/rekaphistoryvendor/view',
                templateUrl: Route.base('/promise/procurement/rekaphistoryvendor/rekaphistoryvendor.view.html'),
                data: {
                    displayName: 'Rekap History Vendor'
                }
            })

        // expired dokumen vendor
        .state('app.promise.procurement-manajemenvendor-dokumen-expired', {
            url: '/procurement/vendordokumenexpired/dokumenexpired',
            templateUrl: Route.base('/promise/procurement/vendordokumenexpired/vendordokumenexpired.index.html'),
            resolve: {
                assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
            },
            data: {
                displayName: 'Vendor Dokumen Expired'
            }
        })

        // SLA Admin Vendor Registrasi
        .state('app.promise.procurement-manajemenvendor-adminslavendor-registrasi', {
                url: '/procurement/adminslavendor/registrasi',
                templateUrl: Route.base('/promise/procurement/adminslavendor/registrasi/adminslavendor.registrasi.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Admin SLA Registrasi Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-registrasi-view', {
                url: '/procurement/adminslavendor/registrasi/view',
                templateUrl: Route.base('/promise/procurement/adminslavendor/registrasi/adminslavendor.registrasi.view.html'),
                data: {
                    displayName: 'Admin SLA Registrasi Vendor'
                }
            })

        .state('app.promise.procurement-manajemenvendor-adminslavendor-perpanjangan', {
                url: '/procurement/adminslavendor/perpanjangan',
                templateUrl: Route.base('/promise/procurement/adminslavendor/perpanjangan/adminslavendor.perpanjangan.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Admin SLA Perpanjangan Sertifikat Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-perpanjangan-view', {
                url: '/procurement/adminslavendor/perpanjangan/view',
                templateUrl: Route.base('/promise/procurement/adminslavendor/perpanjangan/adminslavendor.perpanjangan.view.html'),
                data: {
                    displayName: 'Admin SLA Perpanjangan Sertifikat Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-blacklist', {
                url: '/procurement/adminslavendor/blacklist',
                templateUrl: Route.base('/promise/procurement/adminslavendor/blacklist/adminslavendor.blacklist.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Admin SLA Blacklist Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-blacklist-view', {
                url: '/procurement/adminslavendor/blacklist/view',
                templateUrl: Route.base('/promise/procurement/adminslavendor/blacklist/adminslavendor.blacklist.view.html'),
                data: {
                    displayName: 'Admin SLA Blacklist Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-unblacklist', {
                url: '/procurement/adminslavendor/unblacklist',
                templateUrl: Route.base('/promise/procurement/adminslavendor/unblacklist/adminslavendor.unblacklist.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Admin SLA Unblacklist Vendor'
                }
            })
            .state('app.promise.procurement-manajemenvendor-adminslavendor-unblacklist-view', {
                url: '/procurement/adminslavendor/unblacklist/view',
                templateUrl: Route.base('/promise/procurement/adminslavendor/unblacklist/adminslavendor.unblacklist.view.html'),
                data: {
                    displayName: 'Admin SLA Unblacklist Vendor'
                }
            })
            // Manajemen Vendor
            .state('app.promise.procurement-datasertifikat-datasertifikat', {
                url: '/procurement/datasertifikat/datasertifikat',
                templateUrl: Route.base('/promise/procurement/datasertifikat/datasertifikat.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Data Sertifikat Vendor'
                }
            })

            .state('app.promise.procurement-datasertifikat-datasertifikat-view', {
            	url: '/procurement/datasertifikat/datasertifikat/view',
            	templateUrl: Route.base('/promise/procurement/datasertifikat/datasertifikat.view.html'),
            	resolve: {
            		assets: Route.require('datatables', 'ez-plus', 'TouchSpin', 'textAngular')
            	},
            	data: {
            		displayName: 'Data Sertifikat Vendor'
            	},
            	 params: {
                 	vendor: null,
                 	toDo : null
                 }
            })


        .state('app.promise.procurement-vendor-approval', {
            url: '/procurement/vendor/approval',
            templateUrl: Route.base('/promise/procurement/vendor/approval/vendor.approval.index.html'),
            params: {
                status: null
            },
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Approval Vendor'
            }
        })

        .state('app.promise.procurement-vendor-approval-detail', {
            url: '/procurement/vendor/approval/detail/:vendorId',
            templateUrl: Route.base('promise/procurement/vendor/approval/vendor.approval.detail.html'),
            resolve: {
                assets: Route.require('ui.select',  'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Approval Vendor'
            }
        })
        
        .state('app.promise.procurement-registrasivendor-approval-detail', {
            url: '/procurement/registrasivendor/approval/detail',
            templateUrl: Route.base('/promise/procurement/vendor/approval/registrasivendor.approval.detail.html'),
            resolve: {
                assets: Route.require('datatables', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Approval Calon Vendor'
            }
        })
        
         .state('app.promise.procurement-perpanjangantdr-approval-detail', {
            url: '/procurement/perpanjangantdr/approval/detail',
            templateUrl: Route.base('/promise/procurement/vendor/approval/perpanjangantdr.approval.detail.html'),
            resolve: {
                assets: Route.require('datatables', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Approval Perpanjangan TDR Vendor'
            }
        })
        // Daftar Vendor
        .state('app.promise.procurement-vendor-daftarvendor', {
            url: '/procurement/vendor/daftarvendor',
            templateUrl: Route.base('/promise/procurement/vendor/daftarvendor/vendor.daftarvendor.index.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Daftar Vendor'
            }
        })

        .state('app.promise.procurement-vendor-daftarvendor-detail', {
            url: '/procurement/vendor/daftarvendor/detail',
            templateUrl: Route.base('/promise/procurement/vendor/daftarvendor/vendor.daftarvendor.detail.html'),
            resolve: {
                assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Daftar Vendor'
            }
        })

        // panitia --> Jadwal Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-jadwalkualifikasi', {
            url: '/procurement/prakualifikasi/jadwalkualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/jadwalkualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Jadwal Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-jadwalkualifikasi-detil2', {
            url: '/procurement/prakualifikasi/jadwalkualifikasi/detil2',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/jadwalkualifikasi/detil2.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Jadwal Kualifikasi'
            }
        })

        // panitia --> Pengambilan Dokumen Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-pengambilandokumenkualifikasi', {
            url: '/procurement/prakualifikasi/pengambilandokumenkualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pengambilan Dokumen Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-pengambilandokumenkualifikasi-view', {
            url: '/procurement/prakualifikasi/pengambilandokumenkualifikasi/view',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi/view/index.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Pengambilan Dokumen Kualifikasi'
            }
        })

        // panitia --> Pemasukan Dokumen Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-pemasukandokumenkualifikasi', {
            url: '/procurement/prakualifikasi/pemasukandokumenkualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pemasukan Dokumen Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-pemasukandokumenkualifikasi-view', {
            url: '/procurement/prakualifikasi/pemasukandokumenkualifikasi/view',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi/view/index.html'),
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Pemasukan Dokumen Kualifikasi'
            }
        })

        // panitia --> Kriteria Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-kriteriakualifikasi', {
            url: '/procurement/prakualifikasi/kriteriakualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/kriteriakualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables', 'toaster', 'ngTable')
            },
            data: {
                displayName: 'Kriteria Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-kriteriakualifikasi-view', {
            url: '/procurement/prakualifikasi/kriteriakualifikasi/view',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/kriteriakualifikasi/view/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Kriteria Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-kriteriakualifikasi-viewjasa', {
            url: '/procurement/prakualifikasi/kriteriakualifikasi/viewjasa',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/kriteriakualifikasi/viewjasa/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Kriteria Kualifikasi View Jasa'
            }
        })

        // panitia --> Seleksi Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi', {
            url: '/procurement/prakualifikasi/seleksikualifikasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/index.html'),
            resolve: {
                assets: Route.require('datatables', 'toaster', 'ngTable')
            },
            data: {
                displayName: 'Seleksi Kualifikasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksiadministrasi', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksiadministrasi',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiadministrasi/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Seleksi Administrasi'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksidukunganbank', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksidukunganbank',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksidukunganbank/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Seleksi Dukungan Bank'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksikemampuandasar', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksikemampuandasar',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikemampuandasar/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Seleksi Kemampuan Dasar'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksikeuangan', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksikeuangan',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikeuangan/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Seleksi Keuangan'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksiteknis', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksiteknis',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiteknis/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Selekasi Teknis'
            }
        })

        .state('app.promise.procurement-panitia-prakualifikasi-seleksikualifikasi-view-seleksinilaikemampuanpaket', {
            url: '/procurement/prakualifikasi/seleksikualifikasi/view/seleksinilaikemampuanpaket',
            templateUrl: Route.base('/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksinilaikemampuanpaket/index.html'),
            resolve: {
                assets: Route.require('ngTable', 'toaster', 'datatables')
            },
            data: {
                displayName: 'Seleksi Nilai Kemampuan Paket'
            }
        })

        // panitia --> Sanggahan Prakualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-sanggahanprakualifikasi', {
                url: '/procurement/prakualifikasi/sanggahanprakualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/sanggahanprakualifikasi/index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables')
                },
                data: {
                    displayName: 'Sanggahan Prakualifikasi'
                }
            })
            .state('app.promise.procurement-panitia-prakualifikasi-sanggahanprakualifikasi-detail', {
                url: '/procurement/prakualifikasi/sanggahanprakualifikasi-detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/sanggahanprakualifikasi/detil.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'filestyle')
                },
                data: {
                    displayName: 'Sanggahan Prakualifikasi'
                }
            })

        // Panitia - Hasil Kualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-hasilkualifikasi', {
                url: '/procurement/prakualifikasi/hasilkualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Hasil Kualifikasi'
                }
            })
            .state('app.promise.procurement-panitia-prakualifikasi-hasilkualifikasi-detil', {
                url: '/procurement/prakualifikasi/hasilkualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.detil.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'filestyle')
                },
                data: {
                    displayName: 'Hasil Kualifikasi'
                }
            })

        // panitia --> Persetujuan Prakualifikasi
        .state('app.promise.procurement-panitia-prakualifikasi-persetujuankualifikasi', {
                url: '/procurement/prakualifikasi/persetujuankualifikasi',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/persetujuankualifikasi/index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables')
                },
                data: {
                    displayName: 'Persetujuan Kualifikasi'
                }
            })
            .state('app.promise.procurement-panitia-prakualifikasi-persetujuankualifikasi-detail', {
                url: '/procurement/prakualifikasi/persetujuankualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/prakualifikasi/persetujuankualifikasi/detil.html'),
                resolve: {
                    assets: Route.require('datatables', 'ngTable')
                },
                data: {
                    displayName: 'Persetujuan Kualifikasi'
                }
            })


        // panitia -->rekap pengadaan index
        .state('app.promise.procurement-panitia-rekappengadaan-index', {
            url: '/procurement/rekappengadaan/index',
            templateUrl: Route.base('/promise/procurement/rekappengadaan/index.html'),
            params: {
                dataCabang: null
            },
            resolve: {
                assets: Route.require('ngTable', 'ngTableExport', 'ui.select')
            },
            data: {
                displayName: 'Rekap Pengadaan'
            }
        })

        // panitia - Rekap pengadaan detail pengadaan
        .state('app.promise.procurement-panitia-rekappengadaan-detail', {
            url: '/procurement/rekappengadaan/detail',
            templateUrl: Route.base('/promise/procurement/rekappengadaan/detail.html'),
            params: {
                dataDivisi: null
            },
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Rekap Pengadaan'
            }
        })

        // panitia - Rekap pengadaan view pengadaan
        .state('app.promise.procurement-panitia-rekappengadaan-view', {
            url: '/procurement/rekappengadaan/view',
            templateUrl: Route.base('/promise/procurement/rekappengadaan/view.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Rekap Pengadaan'
            }
        })

        // Seleksi Pendaftaran Index
        .state('app.promise.procurement-panitia-seleksipendaftaran-index', {
            url: '/procurement/seleksipendaftaran/index',
            templateUrl: Route.base('/promise/procurement/seleksipendaftaran/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Seleksi Pendaftaran'
            }
        })

        // Seleksi Pendaftaran View
        .state('app.promise.procurement-panitia-seleksipendaftaran-view', {
            url: '/procurement/seleksipendaftaran/view',
            templateUrl: Route.base('/promise/procurement/seleksipendaftaran/view.html'),
            params: {
                dataPengadaan: null
            },
            resolve: {
                assets: Route.require('ngTable')
            },
            data: {
                displayName: 'Seleksi Pendaftaran'
            }
        })

        // Delegasi Pengadaan
        .state('app.promise.procurement-panitia-delegasipengadaan', {
            url: '/procurement/delegasipengadaan',
            templateUrl: Route.base('/promise/procurement/delegasipengadaan/index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Delegasi Pengadaan'
            }
        })

        .state('app.promise.procurement-panitia-delegasipengadaan-detil', {
            url: '/procurement/delegasipengadaan/detil',
            templateUrl: Route.base('/promise/procurement/delegasipengadaan/detil.html'),
            resolve: {
                assets: Route.require('ui.select')
            },
            data: {
                displayName: 'Delegasi Pengadaan'
            }
        })

        // Pembuktian Kualifikasi
        .state('app.promise.procurement-panitia-pembuktiankualifikasi', {
                url: '/procurement/pembuktiankualifikasi',
                templateUrl: Route.base('/promise/procurement/pembuktiankualifikasi/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pembuktian Kualifikasi'
                }
            })
            .state('app.promise.procurement-panitia-pembuktiankualifikasi-detil', {
                url: '/procurement/pembuktiankualifikasi/detil',
                templateUrl: Route.base('/promise/procurement/pembuktiankualifikasi/detil.html'),
                resolve: {
                    assets: Route.require('ngTable')
                },
                data: {
                    displayName: 'Pembuktian Kualifikasi'
                }
            })
            // Persetujuan Pengadaan
            .state('app.promise.procurement-panitia-persetujuanpengadaan', {
                url: '/procurement/persetujuanpengadaan',
                templateUrl: Route.base('/promise/procurement/persetujuanpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Persetujuan Pengadaan'
                }
            })
            .state('app.promise.procurement-panitia-persetujuanpengadaan-detil', {
                url: '/procurement/persetujuanpengadaan/detil',
                templateUrl: Route.base('/promise/procurement/persetujuanpengadaan/detil.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Persetujuan Pengadaan'
                }
            })
            // Delegasi Persetujuan Pengadaan
            .state('app.promise.procurement-panitia-delegasipersetujuanpengadaan', {
                url: '/procurement/delegasipersetujuanpengadaan',
                templateUrl: Route.base('/promise/procurement/delegasipersetujuanpengadaan/index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'filestyle')
                },
                data: {
                    displayName: 'Delegasi Persetujuan Pengadaan'
                }
            })
            // Panitia - Pengumuman Evaluasi Admin Teknis
            .state('app.promise.procurement-panitia-pengumumanevaluasiadminteknis', {
                url: '/procurement/pengumumanevaluasiadminteknis',
                templateUrl: Route.base('/promise/procurement/pengumumanevaluasiadminteknis/pengumumanevaluasiadminteknis.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pengumuman Evaluasi Admin Teknis'
                }
            })
            .state('app.promise.procurement-panitia-pengumumanevaluasiadminteknis-detil', {
                url: '/procurement/pengumumanevaluasiadminteknis/detil',
                templateUrl: Route.base('/promise/procurement/pengumumanevaluasiadminteknis/pengumumanevaluasiadminteknis.detil.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'filestyle')
                },
                data: {
                    displayName: 'Pengumuman Evaluasi Admin Teknis'
                }
            })

        // Aproval - Settings
        .state('app.promise.procurement-master-approval', {
                url: '/procurement/master/approval',
                templateUrl: Route.base('/promise/procurement/master/approval/approval.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                }, data: {
                    displayName: 'Master Approval'
                }
            })
            
            .state('app.promise.procurement-master-approval-view', {
                url: '/procurement/master/approval/view',
                templateUrl: Route.base('/promise/procurement/master/approval/approval.view.html'),
                params:{
                	toDo:null,
                	approval:null
                },
                resolve: {
                    assets: Route.require('datatables', 'ui.select')
                }, data: {
                    displayName: 'Master Approval'
                }
            })    
        
            // Aproval - Proses
            .state('app.promise.procurement-approval', {
                url: '/procurement/approval',
                templateUrl: Route.base('/promise/procurement/approval/approval.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Daftar Approval'
                }
            })
             // Monitoring - Worklist
            .state('app.promise.procurement-monitoring-worklist', {
                url: '/procurement/approval',
                templateUrl: Route.base('/promise/procurement/monitoringworklist/monitoringworklist.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Monitoring Worklist'
                }
            })
            .state('app.promise.procurement-approval-view-pr', {
                url: '/procurement/approval/view/pr',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.pr.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'textAngularSetup', 'textAngular')
                },
                data: {
                    displayName: 'Approval Purchase Request'
                }
            })
            .state('app.promise.procurement-approval-view-bo', {
                url: '/procurement/approval/view/bo',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.bo.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'textAngularSetup', 'textAngular')
                },
                data: {
                    displayName: 'Approval Purchase Request'
                }
            })
            .state('app.promise.procurement-approval-view-po', {
                url: '/procurement/approval/view/po',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.po.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'textAngularSetup', 'textAngular')
                },
                data: {
                    displayName: 'Approval Purchase Order'
                }
            })
            
            .state('app.promise.procurement-approval-view-new-catalog', {
                url: '/procurement/approval/view/new/catalog',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.new.catalog.html'),
                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                return $ocLazyLoad.load(['ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle'])
                    .then(function () {
                        return $ocLazyLoad.load('src/js/modules/promise/procurement/approval/approval.view.catalog.controller.js');
                    });
                }],
                params: {
                    dataCatalog			: null,
                    toDo				: null,
                    dataApprovalProcess	: null
                },
                data: {
                    displayName: 'Approval New Catalog'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//	        .state('app.promise.procurement-approval-view-new-catalog', {
//	            url: '/procurement/approval/view/new/catalog',
//	            templateUrl: Route.base('/promise/procurement/approval/approval.view.new.catalog.html'),
//	            params: {
//                    dataCatalog			: null,
//                    toDo				: null,
//                    dataApprovalProcess	: null
//                },
//	            resolve: {
//	                assets: Route.require('ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle')
//	            },
//	            data: {
//	                displayName: 'Approval New Catalog'
//	            }
//	        })
            
            // blacklist approval
            .state('app.promise.procurement-approval-view-blacklistvendor', {
                url: '/procurement/approval/view/blacklistvendor/:vendorId',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.blacklistvendor.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Approval Blacklist Vendor'
                }
            })
            
             // unblacklist approval
            .state('app.promise.procurement-approval-view-unblacklistvendor', {
                url: '/procurement/approval/view/unblacklistvendor/:vendorId',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.unblacklistvendor.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Approval Unblacklist Vendor'
                }
            })
            
            // Approval untuk Edit Vendor
            .state('app.promise.procurement-approval-view-updatedatavendor', {
                url: '/procurement/approval/view/updatevendor/:vendorId',
                templateUrl: Route.base('/promise/procurement/approval/approval.view.updatevendor.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Approval Update Vendor '
                }
            })
            
            // kroscek
            .state('app.promise.procurement-master-approvalsetvendor', {
                url: '/procurement/master/approvalsetvendor',
                templateUrl: Route.base('/promise/procurement/master/approval/approvalsetvendor.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Approval Set Vendor'
                }
            })   
        
        
        // ApprovalTahapan
        .state('app.promise.procurement-master-approvaltahapan', {
                url: '/procurement/master/approvaltahapan',
                templateUrl: Route.base('promise/procurement/master/approvaltahapan/approvaltahapan.index.html'),
                resolve: {
                    assets: Route.require('datatables', 'ui.select', 'ngTable')
                },
                data: {
                    displayName: 'Approval Stage'
                }
            })
        .state('app.promise.procurement-master-approvaltahapan-view', {
                url: '/procurement/master/approvaltahapan/view',
                templateUrl: Route.base('promise/procurement/master/approvaltahapan/approvaltahapan.view.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                params : {
                	toDo:null,
					approvalTahapan : null
				},
                data: {
                    displayName: 'Approval Stage'
                }
        
            })
        // Purchase Request
        .state('app.promise.procurement-purchaserequest', {
                url: '/procurement/purchaserequest',
                templateUrl: Route.base('/promise/procurement/purchaserequest/purchaserequest.index.html'),
                params: {
                    status: null
                },
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Purchase Request'
                }
            })
            
//            .state('app.promise.procurement-purchaserequest-import', {
//                url: '/procurement/purchaserequest/import',
//                templateUrl: Route.base('/promise/procurement/purchaserequest/purchaserequest_import.html'),
//                data: {
//                    displayName: 'Purchase Request Import'
//                },
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angularFileUpload'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/purchaserequest/purchaserequest_import_controller.js');
//                        });
//                	}]
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
	        .state('app.promise.procurement-purchaserequest-import', {
	            url: '/procurement/purchaserequest/import',
	            templateUrl: Route.base('/promise/procurement/purchaserequest/purchaserequest_import.html'),
	            resolve: {
	                assets: Route.require('angularFileUpload')
	            },
	            data: {
	                displayName: 'Purchase Request Import'
	            }
	        })
            
            .state('app.promise.procurement-purchaserequest-add', {
                url: '/procurement/purchaserequest/add',
                templateUrl: Route.base('/promise/procurement/purchaserequest/purchaserequest.add.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Purchase Request'
                }
            })
            .state('app.promise.procurement-purchaserequest-edit', {
                url: '/procurement/purchaserequest/edit',
                templateUrl: Route.base('/promise/procurement/purchaserequest/purchaserequest.edit.html'),
                params: {
                    purchaserequest: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                data: {
                    displayName: 'Purchase Request'
                }
            })
            .state('app.promise.procurement-purchaserequest-itemadd', {
                url: '/procurement/purchaserequest/itemadd',
                templateUrl: Route.base('/promise/procurement/order/purchaserequest/purchaserequest.item-add.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable')
                }
            })

        // --end purchase request

        // Purchase Request Verification
        .state('app.promise.procurement-purchaserequestverification', {
                url: '/procurement/purchaserequestverification',
                templateUrl: Route.base('/promise/procurement/purchaserequestverification/purchaserequestverification.index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables')
                },
                data: {
                    displayName: 'Purchase Request Verification'
                }
            })
            .state('app.promise.procurement-purchaserequestverification-verify', {
                url: '/procurement/purchaserequestverification/verify',
                templateUrl: Route.base('/promise/procurement/purchaserequestverification/purchaserequestverification.verify.html'),
                params: {
                    purchaserequest: null
                },
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle', 'angularBootstrapNavTree')
                },
                data: {
                    displayName: 'Purchase Request Verification'
                }
            })

        // Purchase Order
        .state('app.promise.procurement-purchaseorder', {
                url: '/procurement/purchaseorder',
                templateUrl: Route.base('/promise/procurement/purchaseorder/purchaseorder.index.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Purchase Order'
                }
            })
        
         // PO khusus buat vendor
            .state('app.promise.procurement-purchaseorder-vendor-detail', {
                url: '/procurement/purchaseorder/vendor/detail',
                templateUrl: Route.base('/promise/procurement/purchaseorder/purchaseorder.vendor.detail.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Purchase Order Vendor'
                }
            })
			
			

          .state('app.promise.procurement-purchaseorder-add', {
                url: '/procurement/purchaseorder/add',
                templateUrl: Route.base('/promise/procurement/purchaseorder/purchaseorder.add.html'),
                resolve: {
                    assets: Route.require('ui.select')
                }, params: {
                	bookingOrder: null,
                	toDo:null
                },
                data: {
                    displayName: 'Purchase Order'
                }
            })
        
        
        
        // Purchase Order vendor
        .state('app.promise.procurement-purchaseorder-vendor', {
                url: '/procurement/purchaseorder/vendor',
                templateUrl: Route.base('/promise/procurement/purchaseorder/purchaseorder.vendor.index.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Purchase Order'
                }
            })
        
		
            .state('app.promise.procurement-purchaseorder-edit', {
                url: '/procurement/purchaseorder/edit',
                templateUrl: Route.base('/promise/procurement/purchaseorder/purchaseorder.edit.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Purchase Order'
                }
            })

        // KPI MOnitoring
        .state('app.promise.procurement-kpimonitoring', {
            url: '/procurement/kpimonitoring',
            templateUrl: Route.base('/promise/procurement/kpimonitoring/kpimonitoring.index.html'),
            resolve: {
                assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable', 'datatables', 'ng-fusioncharts')
            },
            data: {
                displayName: 'KPI Monitoring'
            }
        })

        // Request For Quotation
        .state('app.promise.procurement-requestforquotation', {
            url: '/procurement/requestforquotation',
            templateUrl: Route.base('/promise/procurement/requestforquotation/requestforquotation.index.html'),
            data: {
                displayName: 'Request for Quotation'
            }
        })

        .state('app.promise.procurement-requestforquotation-add', {
            url: '/procurement/requestforquotation/add',
            templateUrl: Route.base('/promise/procurement/requestforquotation/requestforquotation.add.html'),
            params: {
                dataRequest: null
            },
            data: {
                displayName: 'Request for Quotation'
            },
            resolve: {
                assets: Route.require('ui.select', 'ngTable')
            }
        })

        // vendor
//        .state('appvendor.promise.procurement-requestforquotation', {
//            url: '/procurement/requestforquotation',
//            templateUrl: Route.base('/promise/procurement/requestforquotation/requestforquotation.vendor.index.html'),
//            data: {
//                displayName: 'Request for Quotation'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/requestforquotation/requestforquotation.vendor.index.controller.js');
//                    });
//                }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('appvendor.promise.procurement-requestforquotation', {
            url: '/procurement/requestforquotation',
            templateUrl: Route.base('/promise/procurement/requestforquotation/requestforquotation.vendor.index.html'),
            resolve: {
                assets: Route.require('angulargrid')
            },
            data: {
                displayName: 'Request for Quotation'
            }
        })
        
        // Delivery Recieved Panitia:
        .state('app.promise.procurement-deliveryreceived', {
                url: '/procurement/deliveryreceived',
                templateUrl: Route.base('/promise/procurement/deliveryreceived/deliveryreceived.index.html'),
                resolve: {
                    assets: Route.require('ui.select')
                },
                data: {
                    displayName: 'Delivery Received'
                }
            })
            
            .state('app.promise.procurement-deliveryReceived-detail', {
                url: '/procurement/deliveryreceived/detail',
                templateUrl: Route.base('/promise/procurement/deliveryreceived/deliveryreceived.detail.html'),
//                resolve: {
//                    assets: Route.require('ui.select')
//                }, 
                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load(['ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle'])
                        .then(function () {
                            return $ocLazyLoad.load('src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.detail.controller.js');
                    });
                }],
                params: {
                	deliveryReceived: null,
                	toDo: null
                },
                data: {
                    displayName: 'Purchase Order'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//	        .state('app.promise.procurement-deliveryReceived-detail', {
//	            url: '/procurement/deliveryreceived/detail',
//	            templateUrl: Route.base('/promise/procurement/deliveryreceived/deliveryreceived.detail.html'),
//	            params: {
//                	deliveryReceived: null,
//                	toDo: null
//                },
//	            resolve: {
//	                assets: Route.require('ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle')
//	            },
//	            data: {
//	                displayName: 'Purchase Order'
//	            }
//	        })
            
            // Delivery Recieved Import Panitia:
//            .state('app.promise.procurement-deliveryreceived-import', {
//                url: '/procurement/deliveryreceived/import',
//                templateUrl: Route.base('promise/procurement/deliveryreceived/deliveryreceived.import.html'),
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.import.controller.js');
//                        });
//            }],
//                data: {
//                    displayName: 'Delivery Received'
//                }
//            })
            
            // Delivery Recieved Import Panitia:
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
	        .state('app.promise.procurement-deliveryreceived-import', {
	            url: '/procurement/deliveryreceived/import',
	            templateUrl: Route.base('promise/procurement/deliveryreceived/deliveryreceived.import.html'),
	            resolve: {
	                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
	            },
	            data: {
	                displayName: 'Delivery Received'
	            }
	        })

        // sample
        // dashboard Panitia
        .state('app.dashboard', {
                url: '/dashboard',
                templateUrl: Route.base('dashboard.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable')
                },
                data: {
                    displayName: 'Dashboard'
                }
            })
            // dashboard Anggaran
            .state('app.dashboard-anggaran', {
                url: '/dashboard-anggaran',
                templateUrl: Route.base('dashboard-anggaran.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable', 'ng-fusioncharts')
                },
                data: {
                    displayName: 'Dashboard'
                }
            })
            // dashboard Anggaran
            .state('app.dashboard-perencanaan', {
                url: '/dashboard-perencanaan',
                templateUrl: Route.base('dashboard-perencanaan.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable', 'ng-fusioncharts')
                },
                data: {
                    displayName: 'Dashboard'
                }
            })
            // dashboard PurchaseOrder
            .state('app.dashboard-order', {
                url: '/dashboard-order',
                templateUrl: Route.base('dashboard-order.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable', 'ng-fusioncharts')
                },
                data: {
                    displayName: 'Dashboard'
                }
            })

        // dashboard Kosong
        .state('app.dashboard-default', {
            url: '/dashboard-default',
            templateUrl: Route.base('dashboard-default.html'),
            resolve: {
                assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart', 'ngTable', 'ng-fusioncharts')
            },
            data: {
                displayName: 'Dashboard'
            }
        })

        // Single Page Routes
        .state('page', {
                url: '/page',
                templateUrl: Route.base('page.html'),
                resolve: {
                    assets: Route.require('icons', 'animate')
                }
            })
            // login Promise
            .state('page.promiselogin', {
                url: '/promiselogin',
                templateUrl: Route.base('/promise/procurement/login/login.html')
            })
            .state('page.promiseregister', {
                url: '/promiseregister',
                // templateUrl:
				// Route.base('/promise/procurement/registrasi/registrasi.html'),
                templateUrl: Route.base('/promise/procurement/vendor/index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables', 'textAngular')
                }
            })
            .state('page.promiseregister.bankvendor', {
                url: '/promiseregister/bankvendor',
                // templateUrl:
				// Route.base('/promise/procurement/registrasi/registrasi.html')
                templateUrl: Route.base('/promise/procurement/vendor/bank.vendor.modal.html'),
                resolve: {
                    assets: Route.require('ui.select', 'xeditable', 'ngTable')
                }
            })
            .state('page.promiseregistervendor', {
                url: '/promiseregistervendor',
                templateUrl: Route.base('/promise/procurement/vendor/index.html'),
                resolve: {
                    assets: Route.require('ui.select', 'xeditable', 'ngTable', 'angularFileUpload', 'filestyle', 'toaster', 'datatables')
                }
            })

        .state('page.login', {
                url: '/login',
                templateUrl: Route.base('page.login.html')
            })
            .state('page.register', {
                url: '/register',
                templateUrl: Route.base('page.register.html')
            })
            .state('page.recover', {
                url: '/recover',
                templateUrl: Route.base('page.recover.html')
            })
            .state('page.recover-execute', {
                url: '/recover-execute/:mailHash',
                templateUrl: Route.base('page.recover.execute.html')
            })
            .state('page.lock', {
                url: '/lock',
                templateUrl: Route.base('page.lock.html')
            })
            // Layout dock
            .state('app-dock', {
                url: '/dock',
                abstract: true,
                templateUrl: Route.base('app-dock.html'),
                controller: ['$rootScope', '$scope',
                function ($rootScope, $scope) {
                        $rootScope.app.layout.isDocked = true;
                        $scope.$on('$destroy', function () {
                            $rootScope.app.layout.isDocked = false;
                        });
                        // we can't use dropdown when material and docked
                        // main content overlaps dropdowns (forced for demo)
                        $rootScope.app.layout.isMaterial = false;
                }
            ],
                resolve: {
                    assets: Route.require('icons', 'screenfull', 'sparklines', 'slimscroll', 'toaster', 'animate')
                }
            })
            .state('app-dock.dashboard', {
                url: '/dashboard',
                templateUrl: Route.base('dashboard.html'),
                resolve: {
                    assets: Route.require('flot-chart', 'flot-chart-plugins', 'easypiechart')
                }
            })
            // Layout full height
            .state('app-fh', {
                url: '/fh',
                abstract: true,
                templateUrl: Route.base('app-fh.html'),
                resolve: {
                    assets: Route.require('icons', 'screenfull', 'sparklines', 'slimscroll', 'toaster', 'animate')
                }

            })
            .state('app-fh.columns', {
                url: '/columns',
                templateUrl: Route.base('layout.columns.html')
            })
            .state('app-fh.chat', {
                url: '/chat',
                templateUrl: Route.base('extras.chat.html')
            })


        // CATALOG
        // master Atrribute
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-master-attribute', {
//            url: '/procurement/master/atrribute',
//            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.index.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'filestyle', 'datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attribute/attribute.index.controller.js');
//                    });
//            		}],
//            data: {
//                displayName: 'Attribute'
//            }
//        })
        
        // master Attribute
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-master-attribute', {
            url: '/procurement/master/atrribute',
            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.index.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'filestyle', 'datatables')
            },
            data: {
                displayName: 'Attribute'
            }
        })
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-master-attribute-add', {
//            url: '/procurement/master/atrribute/add',
//            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.add.html'),
//            params: {
//                dataAttribute: null
//            },
//            data: {
//                displayName: 'Attribute'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'filestyle', 'datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attribute/attribute.add.controller.js');
//                    });
//            		}]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-master-attribute-add', {
            url: '/procurement/master/atrribute/add',
            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.add.html'),
            params: {
                dataAttribute: null
            },
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'filestyle', 'datatables')
            },
            data: {
                displayName: 'Attribute'
            }
        })

        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-master-attribute-edit', {
//            url: '/procurement/master/atrribute/edit',
//            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.edit.html'),
//            params: {
//                dataAttribute: null
//            },
//            data: {
//                displayName: 'Attribute'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'filestyle', 'datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attribute/attribute.edit.controller.js');
//                    });
//            		}]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-master-attribute-edit', {
            url: '/procurement/master/atrribute/edit',
            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.edit.html'),
            params: {
                dataAttribute: null
            },
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'filestyle', 'datatables')
            },
            data: {
                displayName: 'Attribute'
            }
        })

        // Import Panitia:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-master-attribute-import', {
//            url: '/procurement/master/atrribute/import',
//            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.import.html'),
//            data: {
//                displayName: 'Attribute Import'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attribute/attribute.import.controller.js');
//                    });
//            }]
//        })
        
        // Import Panitia:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-master-attribute-import', {
            url: '/procurement/master/atrribute/import',
            templateUrl: Route.base('promise/procurement/catalog/attribute/attribute.import.html'),
            params: {
                dataAttribute: null
            },
            resolve: {
                assets: Route.require('datatables', 'angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Attribute Import'
            }
        })

        // categori product
        
         .state('app.promise.procurement-category', {
                url: '/procurement/catalog/category',
                templateUrl: Route.base('promise/procurement/catalog/category/mastercategory.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Category'
                }
            })
         .state('app.promise.procurement-category-view', {
                url: '/procurement/catalog/category/view',
                templateUrl: Route.base('promise/procurement/catalog/category/mastercategory.view.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                params : {
                  category : null,
                  toDo : null
                },
                data: {
                    displayName: 'Category'
                }
            })

        // Import Category:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-category-import', {
//            url: '/procurement/master/category/import',
//            templateUrl: Route.base('promise/procurement/catalog/category/master_category_import.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/category/master_category_import_controller.js');
//                    });
//            }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-category-import', {
            url: '/procurement/master/category/import',
            templateUrl: Route.base('promise/procurement/catalog/category/master_category_import.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
            }
        })

        // attribute group
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-attribute-group', {
//            url: '/procurement/master/category-group',
//            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_controller.js');
//                    });
//                }],
//                data: {
//                    displayName: 'Attribute Group'
//                }
//        })
        
        // attribute group
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-attribute-group', {
            url: '/procurement/master/category-group',
            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Attribute Group'
            }
        })

        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-attribute-group-add', {
//            url: '/procurement/master/category-group/add',
//            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_add.html'),
//            params: {
//                dataAttributeGroup: null
//            },
//            data: {
//                displayName: 'Attribute Group'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['nestable'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_add_controller.js');
//                    });
//                }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-attribute-group-add', {
            url: '/procurement/master/category-group/add',
            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_add.html'),
            params: {
                dataAttributeGroup: null
            },
            resolve: {
                assets: Route.require('datatables', 'nestable')
            },
            data: {
                displayName: 'Attribute Group'
            }
        })

        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-attribute-group-edit', {
//            url: '/procurement/master/category-group/edit',
//            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_edit.html'),
//            params: {
//                dataAttributeGroup: null
//            },
//            data: {
//                displayName: 'Attribute Group'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['nestable'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_edit_controller.js');
//                    });
//                }]
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-attribute-group-edit', {
            url: '/procurement/master/category-group/edit',
            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_edit.html'),
            params: {
                dataAttributeGroup: null
            },
            resolve: {
                assets: Route.require('datatables', 'nestable')
            },
            data: {
                displayName: 'Attribute Group'
            }
        })

        // Import Panitia:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-attribute-group-import', {
//            url: '/procurement/master/category-group/import',
//            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_import.html'),
//            data: {
//                displayName: 'Attribute Group Import'
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_import_controller.js');
//                    });
//            }]
//        })
        
        // Import Panitia:
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-attribute-group-import', {
            url: '/procurement/master/category-group/import',
            templateUrl: Route.base('promise/procurement/catalog/attributegroup/master_attribute_set_import.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Attribute Group Import'
            }
        })

        // catalog
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-catalog', {
//            url: '/procurement/catalog',
//            templateUrl: Route.base('promise/procurement/catalog/catalog_manage.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/catalog_manage_controller.js');
//                    });
//                }],
//            data: {
//                displayName: 'Manage Catalog'
//            }
//        })
        
        // manage catalog
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-catalog', {
            url: '/procurement/catalog',
            templateUrl: Route.base('promise/procurement/catalog/catalog_manage.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Manage Catalog'
            }
        })

        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-catalog-add', {
            url: '/procurement/catalog/add',
            templateUrl: Route.base('promise/procurement/catalog/catalog_manage_add.html'),
            params: {
                dataCatalog: null,
                toDo: null
            },
            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                return $ocLazyLoad.load(['ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle'])
                    .then(function () {
                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/catalog_manage_add_controller.js');
                    });
                }],
            data: {
                displayName: 'Manage Catalog'
            }
        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-catalog-add', {
//            url: '/procurement/catalog/add',
//            templateUrl: Route.base('promise/procurement/catalog/catalog_manage_add.html'),
//            params: {
//                dataCatalog: null,
//                toDo: null
//            },
//            resolve: {
//                assets: Route.require('ui.select', 'ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'angularFileUpload', 'filestyle')
//            },
//            data: {
//                displayName: 'Manage Catalog'
//            }
//        })

        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-catalog-edit', {
            url: '/procurement/catalog/edit',
            templateUrl: Route.base('promise/procurement/catalog/catalog_manage_edit.html'),
            params: {
                dataAttributeGroup: null
            },
            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
                return $ocLazyLoad.load(['ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'filestyle', 'angularFileUpload'])
                    .then(function () {
                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/catalog_manage_edit_controller.js');
                    });
            }],
            data: {
                displayName: 'Manage Catalog'
            }
        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-catalog-edit', {
//            url: '/procurement/catalog/edit',
//            templateUrl: Route.base('promise/procurement/catalog/catalog_manage_edit.html'),
//            params: {
//                dataAttributeGroup: null
//            },
//            resolve: {
//                assets: Route.require('ui.tree', 'datatables', 'chosen', 'slimscroll', 'textAngular', 'filestyle', 'angularFileUpload')
//            },
//            data: {
//                displayName: 'Manage Catalog'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-catalog-import', {
//            url: '/procurement/catalog/edit',
//            templateUrl: Route.base('promise/procurement/catalog/catalog_import.html'),
//            params: {
//                dataAttributeGroup: null
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angularFileUpload', 'filestyle'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/catalog_import_controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Manage Catalog'
//            }
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-catalog-import', {
            url: '/procurement/catalog/edit',
            templateUrl: Route.base('promise/procurement/catalog/catalog_import.html'),
            params: {
                dataAttributeGroup: null
            },
            resolve: {
                assets: Route.require('angularFileUpload', 'filestyle')
            },
            data: {
                displayName: 'Manage Catalog'
            }
        })

        // Invoice Matching
//        .state('app.promise.procurement-invoicematching', {
//            url: '/procurement/invoicematching',
//            templateUrl: Route.base('promise/procurement/invoicematching/invoicematch.index.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select', 'angularFileUpload', 'filestyle', 'datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/invoicematching/invoicematch.index.controller.js');
//                    });
//            }]
//        })
        
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-invoicematching', {
            url: '/procurement/invoicematching',
            templateUrl: Route.base('promise/procurement/invoicematching/invoicematch.index.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select', 'angularFileUpload', 'filestyle', 'datatables')
            }
        })
        
        // compare product :
        .state('app.promise.procurement-compare-product', {
            url: '/procurement/compare_product',
            templateUrl: Route.base('promise/procurement/product/compare_product.html'),
            data: {
                displayName: 'Compare Catalog'
            }
        })
        
        // untuk compare vendor :
//        .state('app.promise.procurement-catalog-vendor-compare', {
//            url: '/procurement/compare_vendor',
//            templateUrl: Route.base('promise/procurement/vendor/compare_vendor.html'),
//            params: {
//                compareVendorList: null
//            },
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/custom/fe-controller.js');
//                    });
//		}],
//            data: {
//                displayName: 'Compare Vendor'
//            }
//        })
        
        // untuk compare vendor :
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-catalog-vendor-compare', {
            url: '/procurement/compare_vendor',
            templateUrl: Route.base('promise/procurement/vendor/compare_vendor.html'),
            params: {
            	compareVendorList: null
            },
            resolve: {
                assets: Route.require('angulargrid')
            },
            data: {
                displayName: 'Compare Vendor'
            }
        })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.katalog', {
//                url: '/e-catalog-portal',
//                templateUrl: Route.base('promise/procurement/portal/catalog.html'),
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/product/catalog_controller.js');
//                        });
//                }],
//                data: {
//                    displayName: 'View Catalog'
//                }
//            })
            
            /*View Catalog - Start*/
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.katalog', {
                url: '/e-catalog-portal',
                templateUrl: Route.base('promise/procurement/portal/catalog.html'),
                resolve: {
                    assets: Route.require('datatables', 'angulargrid')
                },
                data: {
                    displayName: 'View Catalog'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.procurement-ecatalog2', {
//                url: '/procurement/e-catalog',
//                templateUrl: Route.base('promise/procurement/catalog/product/product_list.html'),
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid','ui.select'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/product/ecatalog_controller.js');
//                        });
//                }],
//                data: {
//                    displayName: 'View Catalog'
//                }
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.procurement-ecatalog2', {
                url: '/procurement/e-catalog',
                templateUrl: Route.base('promise/procurement/catalog/product/product_list.html'),
                resolve: {
                    assets: Route.require('datatables','angulargrid','ui.select')
                },
                data: {
                    displayName: 'View Catalog'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.procurement-ecatalog-Detail', {
//                url: '/procurement/e-catalog',
//                params: {
//                    dataCatalog: null
//                },
//                templateUrl: Route.base('promise/procurement/catalog/product/product_detail.html'),
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid', 'ez-plus', 'TouchSpin', 'textAngular'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/product/ecatalog_detail_controller.js');
//                        });
//                }],
//                data: {
//                    displayName: 'View Catalog'
//                }
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.procurement-ecatalog-Detail', {
                url: '/procurement/e-catalog',
                templateUrl: Route.base('promise/procurement/catalog/product/product_detail.html'),
                params: {
                	dataCatalog: null
                },
                resolve: {
                    assets: Route.require('datatables', 'angulargrid', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'View Catalog'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.procurement-ecatalog-compare', {
//                url: '/procurement/compare_product',
//                templateUrl: Route.base('promise/procurement/catalog/product/compare_product.html'),
//                params: {
//                    catalogList: null
//                },
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/product/ecatalog_compare_controller.js');
//                        });
//                }],
//                data: {
//                    displayName: 'Compare Catalog'
//                }
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.procurement-ecatalog-compare', {
                url: '/procurement/compare_product',
                templateUrl: Route.base('promise/procurement/catalog/product/compare_product.html'),
                params: {
                    catalogList: null
                },
                resolve: {
                    assets: Route.require('datatables', 'angulargrid')
                },
                data: {
                    displayName: 'Compare Catalog'
                }
            })
            
            .state('app.promise.procurement-ecatalog-cart', {
                url: '/procurement/catalog/cart',
                templateUrl: Route.base('promise/procurement/catalog/product/cart.html'),
                params: {
                    catalogList: null
                },
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular', 'ui.select')
                },
                data: {
                    displayName: 'Cart'
                }
            })
            /*View Catalog - End*/
            
            .state('app.promise.procurement-booking-order-view', {
                url: '/procurement/booking_order',
                templateUrl: Route.base('promise/procurement/bookingorder/bookingorder.view.html'),
                params: {
                    catalogList: null,
                    salesOrder: null,
                },
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular', 'ui.select')
                },
                data: {
                    displayName: 'Purchase Request'
                }
            })
            
              .state('app.promise.procurement-booking-order-index', {
	            url: '/procurement/booking_order',
	            templateUrl: Route.base('promise/procurement/bookingorder/bookingorder.index.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            data: {
	                displayName: 'Purchase Request'
	            }
	        })
	        
	        .state('app.promise.procurement-booking-order-detail', {
	            url: '/procurement/booking_order',
	            templateUrl: Route.base('promise/procurement/bookingorder/bookingorder.detail.html'),
	            resolve: {
	                assets: Route.require('datatables')
	            },
	            params: {
	            	bookingOrder: null
                },
	            data: {
	                displayName: 'Purchase Request Detail'
	            }
	        })

	        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.procurement-catalog-vendor', {
//                url: '/procurement/vendor',
//                templateUrl: Route.base('promise/procurement/catalog/vendor/vendor_list.html'),
//                resolve: {
//                    lazyload: ['$ocLazyLoad', function ($ocLazyLoad) {
//                        return $ocLazyLoad.load(['angulargrid'])
//                            .then(function () {
//                                return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/vendor/evendor_controller.js');
//                            });
//                }],
//                    assets: Route.require('ui.select', 'datatables', 'ngTable')
//                },
//                data: {
//                    displayName: 'View Vendor'
//                }
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.procurement-catalog-vendor', {
                url: '/procurement/vendor',
                templateUrl: Route.base('promise/procurement/catalog/vendor/vendor_list.html'),
                resolve: {
                    assets: Route.require('ui.select', 'datatables', 'ngTable', 'angulargrid')
                },
                data: {
                    displayName: 'View Vendor'
                }
            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//            .state('app.promise.procurement-catalog-vendor-detail', {
//                url: '/procurement/catalog/vendor/detail',
//                templateUrl: Route.base('promise/procurement/catalog/vendor/vendor_detail.html'),
//                params: {
//                    dataVendor: null
//                },
//                resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                    return $ocLazyLoad.load(['angulargrid', 'ez-plus', 'TouchSpin', 'textAngular'])
//                        .then(function () {
//                            return $ocLazyLoad.load('src/js/modules/promise/procurement/catalog/vendor/evendor_detail_controller.js');
//                        });
//                }],
//                	
//                data: {
//                    displayName: 'View Vendor'
//                }
//            })
            
            // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
            .state('app.promise.procurement-catalog-vendor-detail', {
                url: '/procurement/catalog/vendor/detail',
                templateUrl: Route.base('promise/procurement/catalog/vendor/vendor_detail.html'),
                params: {
                  dataVendor: null
                },
                resolve: {
                    assets: Route.require('angulargrid', 'ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'View Vendor'
                }
            })
            
            .state('app.promise.procurement-ecatalog', {
                url: '/procurement/ecatalog',
                templateUrl: Route.base('promise/procurement/product/product_list.html'),
                resolve: {
                    assets: Route.require('angulargrid')
                },
                data: {
                    displayName: 'View Catalog'
                }
            })
            .state('app.promise.procurement-ecatalogDetail', {
                url: '/procurement/ecatalog/:productName',
                templateUrl: Route.base('promise/procurement/product/product_detail.html'),
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'View Catalog'
                }
            })

        .state('app.promise.procurement-vendor', {
                url: '/procurement/vendor/list',
                templateUrl: Route.base('promise/procurement/vendor/vendor_list.html'),
                resolve: {
                    assets: Route.require('angulargrid')
                },
                data: {
                    displayName: 'View Vendor'
                }
            })
            .state('app.promise.procurement-vendorDetail', {
                url: '/procurement/vendor/:companyName',
                templateUrl: Route.base('promise/procurement/vendor/vendor_detail.html'),
                data: {
                    displayName: 'Vendor Detail'
                }
            })
            .state('app.promise.procurement-listPayment', {
                url: '/procurement/fe/list_payment',
                templateUrl: Route.base('promise/procurement/fe/list_payment.html'),
                data: {
                    displayName: 'List Payment'
                }
            })
            .state('app.promise.procurement-deliveryReceived', {
                url: '/procurement/fe/delivery_received',
                templateUrl: Route.base('promise/procurement/fe/delivery_received.html'),
                data: {
                    displayName: 'Delivery Received'
                }
            })
            .state('app.promise.procurement-purchaseOrder', {
                url: '/procurement/fe/purchase_order',
                templateUrl: Route.base('promise/procurement/fe/purchase_order.html')
                ,
                data: {
                    displayName: 'Purchase Order'
                }
            })
            .state('app.promise.procurement-costAllocation', {
                url: '/procurement/fe/cost_allocation',
                templateUrl: Route.base('promise/procurement/fe/cost_allocation.html'),
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Cost Allocation'
                }
            })
            .state('app.promise.procurement-cart', {
                url: '/procurement/fe/cart',
                templateUrl: Route.base('promise/procurement/fe/cart.html'),
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular')
                },
                data: {
                    displayName: 'Cart'
                }
            })
            .state('app.promise.procurement-sampleForm', {
                url: '/procurement/fe/sample_form',
                templateUrl: Route.base('promise/procurement/fe/elements.html'),
                resolve: {
                    assets: Route.require('ez-plus', 'TouchSpin', 'textAngular', 'textAngularSetup', 'filestyle', 'angularFileUpload')
                }
            })

        /** purchase request konsolidasi * */
        .state('app.promise.procurement-purchaserequestconsolidation', {
            url: '/procurement/purchaserequestconsolidation',
            templateUrl: Route.base('/promise/procurement/purchaserequestconsolidation/purchaserequestconsolidation.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Purchase Request Consolidation'
            }
        })

        /** purchase request join * */
//        .state('app.promise.procurement-purchaserequestjoin', {
//            url: '/procurement/purchaserequestjoin',
//            templateUrl: Route.base('/promise/procurement/purchaserequestjoin/purchaserequestjoin.add.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/purchaserequestjoin/purchaserequestjoin.add.controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Join Purchase Request'
//            },
//            params: {
//                itemList: null,
//                pr: null
//            },
//        })
        
        /** purchase request join * */
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-purchaserequestjoin', {
            url: '/procurement/purchaserequestjoin',
            templateUrl: Route.base('/promise/procurement/purchaserequestjoin/purchaserequestjoin.add.html'),
            params: {
                itemList: null,
                pr: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Join Purchase Request'
            }
        })
        
//        .state('app.promise.procurement-purchaserequestjoin-index', {
//            url: '/procurement/purchaserequestjoin/index',
//            templateUrl: Route.base('/promise/procurement/purchaserequestjoin/purchaserequestjoin.index.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['datatables'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/purchaserequestjoin/purchaserequestjoin.index.controller.js');
//                    });
//            }],
//            data: {
//                displayName: 'Join Purchase Request'
//            },
//            params: {
//                itemList: null
//            },
//        })
        
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-purchaserequestjoin-index', {
            url: '/procurement/purchaserequestjoin/index',
            templateUrl: Route.base('/promise/procurement/purchaserequestjoin/purchaserequestjoin.index.html'),
            params: {
                itemList: null
            },
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Join Purchase Request'
            }
        })

        .state('app.promise.procurement-master-sla', {
            url: '/procurement/master/sla',
            templateUrl: Route.base('/promise/procurement/master/sla/mastersla.index.html'),
            resolve: {
                assets: Route.require('datatables', 'xeditable')
            }, data: {
                displayName: 'SLA'
            }
        })

        .state('app.promise.procurement-admin-sla', {
            url: '/procurement/adminsla',
            templateUrl: Route.base('/promise/procurement/sla/adminsla.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Admin SLA'
            }
        })

        .state('app.promise.procurement-admin-sla-view', {
            url: '/procurement/adminsla/view',
            templateUrl: Route.base('/promise/procurement/sla/adminsla.view.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            params: {
                pengadaan: null
            },
            data: {
                displayName: 'Admin SLA'
            }
        })

        // Route of Pengadaan Gagal (Index)
        .state('app.promise.procurement-panitia-pengadaangagal', {
                url: '/procurement/pengadaangagal',
                templateUrl: Route.base('/promise/procurement/pengadaangagal/pengadaangagal.index.html'),
                resolve: {
                    assets: Route.require('datatables')
                },
                data: {
                    displayName: 'Pengadaan Gagal'
                }
            })
            .state('app.promise.procurement-panitia-pengadaangagal-add', {
                url: '/procurement/pengadaangagal/add',
                templateUrl: Route.base('/promise/procurement/pengadaangagal/pengadaangagal.add.html'),
                resolve: {
                    assets: Route.require('datatables', 'moment', 'textAngular', 'textAngularSetup')
                },
                data: {
                    displayName: 'Pengadaan Gagal'
                }
            })
            .state('app.promise.procurement-panitia-pengadaangagal-edit', {
                url: '/procurement/pengadaangagal/edit',
                templateUrl: Route.base('/promise/procurement/pengadaangagal/pengadaangagal.edit.html'),
                resolve: {
                    assets: Route.require('datatables', 'moment', 'textAngular', 'textAngularSetup')
                },
                data: {
                    displayName: 'Pengadaan Gagal'
                }
            })
            .state('app.promise.procurement-panitia-pengadaangagal-view', {
                url: '/procurement/pengadaangagal/view',
                templateUrl: Route.base('/promise/procurement/pengadaangagal/pengadaangagal.view.html'),
                resolve: {
                    assets: Route.require('datatables', 'moment', 'textAngular', 'textAngularSetup')
                },
                data: {
                    displayName: 'Pengadaan Gagal'
                }
            })

        // Pengadaan Ulang
        .state('app.promise.procurement-panitia-pengadaanulang', {
            url: '/procurement/pengadaanulang',
            templateUrl: Route.base('/promise/procurement/pengadaanulang/pengadaanulang.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Pengadaan Ulang'
            }
        })
        
        // Report Monitoring Pengadaan
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//        .state('app.promise.procurement-monitoringpengadaan', {
//            url: '/procurement/monitoringpengadaan',
//            templateUrl: Route.base('promise/procurement/report/monitoringpengadaan.index.html'),
//            resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//                return $ocLazyLoad.load(['angulargrid', 'ui.select'])
//                    .then(function () {
//                        return $ocLazyLoad.load('src/js/modules/promise/procurement/report/monitoringpengadaan.index.controller.js');
//                    });
//        }],
//            data: {
//                displayName: 'Monitoring Pengadaan'
//            }
//        })
        
        // Report Monitoring Pengadaan
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-monitoringpengadaan', {
            url: '/procurement/monitoringpengadaan',
            templateUrl: Route.base('promise/procurement/report/monitoringpengadaan.index.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select')
            },
            data: {
                displayName: 'Monitoring Pengadaan'
            }
        })
        
        // Report Monitoring Vendor
        // Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
//		.state('app.promise.procurement-monitoringvendor', {
//		    url: '/procurement/monitoringvendor',
//		    templateUrl: Route.base('promise/procurement/report/monitoringvendor.index.html'),
//		    resolve: ['$ocLazyLoad', function ($ocLazyLoad) {
//		        return $ocLazyLoad.load(['angulargrid', 'ui.select'])
//		            .then(function () {
//		                return $ocLazyLoad.load('src/js/modules/promise/procurement/report/monitoringvendor.index.controller.js');
//		            });
//		}],
//		    data: {
//		        displayName: 'Monitoring Vendor'
//		    }
//		})
		
		// Report Monitoring Vendor
		// Konsep lazy load diganti, karena kalau pakai lazy load ketika di concat dan uglify menu tidak bisa dibuka
        .state('app.promise.procurement-monitoringvendor', {
            url: '/procurement/monitoringvendor',
            templateUrl: Route.base('promise/procurement/report/monitoringvendor.index.html'),
            resolve: {
                assets: Route.require('angulargrid', 'ui.select')
            },
            data: {
                displayName: 'Monitoring Vendor'
            }
        })
		
         // Role Jabatan
	     .state('app.promise.procurement-master-rolejabatan', {
	              url: '/procurement/master/rolejabatan',
	              templateUrl: Route.base('/promise/procurement/master/rolejabatan/rolejabatan.index.html'),
	              resolve: {
	                  assets: Route.require('datatables')
	              },
	              data: {
	                  displayName: 'Role Menu'
	              }
	          })
	      .state('app.promise.procurement-master-rolejabatan-view', {
	              url: '/procurement/master/rolejabatan/view',
	              templateUrl: Route.base('/promise/procurement/master/rolejabatan/rolejabatan.view.html'),
	              params: {
	                  roleJabatan: null,
	                  toDo: null
	              },
	              resolve: {
	                  assets: Route.require('ui.select')
	              },
	              data: {
	                  displayName: 'Role Jabatan'
	              }
	          })
	        .state('app.promise.procurement-master-user-additional', {
              url: '/procurement/master/useradditional',
              templateUrl: Route.base('/promise/procurement/master/useradditional/useradditional.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Role PLH'
              }
          })
          .state('app.promise.procurement-master-user-additional-view', {
                url: '/procurement/master/useradditional/view',
                templateUrl: Route.base('promise/procurement/master/useradditional/useradditional.view.html'),
                resolve: {
                    assets: Route.require('ui.select', 'ngTable', 'datatables', 'angularFileUpload', 'filestyle')
                },
                params : {
                	toDo:null,
                	additionalUser : null
				},
                data: {
                    displayName: 'Role PLH'
                }
        
            })
            /*penambahan modul KAI 22/12/2020 [21152]*/
            .state('app.promise.procurement-master-log-email-notification', {
              url: '/procurement/master/emailnotificationlog',
              templateUrl: Route.base('/promise/procurement/master/emailnotificationlog/emailnotificatonlog.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Log Email Notification'
              }
          })
          
          //KAI 22042021 [23186]
          .state('app.promise.procurement-tablelog-index', {
              url: '/procurement/modules/tablelog',
              templateUrl: Route.base('promise/procurement/modules/tablelog/tablelog.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Table Log' 
              }
          })
          
          .state('app.promise.procurement-tablelog-view', {
              url: '/procurement/modules/tablelog/view',
              templateUrl: Route.base('promise/procurement/modules/tablelog/tablelog.view.html'),
              params: {
          		userId: null
          	},
              resolve: {
                  assets: Route.require('datatables', 'ui.select')
              },
              data: {
                  displayName: 'Table Log'
              }
          })
          
          /*========================================================== CENTRA AUTO AC =================================================================================*/
		
		.state('app.promise.procurement-master-position-index', {
            url: '/master/position',
            templateUrl: Route.base('promise/procurement/master/position/position.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Master Jabatan' 
            }
        })
        
    	.state('app.promise.procurement-master-position-add-edit', {
            url: '/master/position/view',
            templateUrl: Route.base('promise/procurement/master/position/position.add.edit.html'),
            params: {
                todo: null,
                position:null
            },
            resolve: {
                assets: Route.require('datatables','ui.select')
            },
            data: {
                displayName: 'Master Jabatan' 
            }
        })
        
        .state('app.promise.procurement-master-karyawan-index', {
            url: '/master/karyawan',
            templateUrl: Route.base('promise/procurement/master/karyawan/karyawan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Master Karyawan' 
            }
        })
        
        .state('app.promise.procurement-master-karyawan-add-edit', {
            url: '/master/karyawan/view',
            templateUrl: Route.base('promise/procurement/master/karyawan/karyawan.add.edit.html'),
            params: {
                todo: null,
                karyawan:null
            },
            resolve: {
                assets: Route.require('datatables','ui.select')
            },
            data: {
                displayName: 'Master Karyawan' 
            }
        })
        
        .state('app.promise.procurement-master-mobil-index', {
            url: '/master/mobil',
            templateUrl: Route.base('promise/procurement/master/mobil/mobil.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Master Mobil' 
            }
        })
        
        .state('app.promise.procurement-master-mobil-add-edit', {
            url: '/master/mobil/view',
            templateUrl: Route.base('promise/procurement/master/mobil/mobil.add.edit.html'),
            params: {
                todo: null,
                mobil:null
            },
            resolve: {
                assets: Route.require('datatables','ui.select')
            },
            data: {
                displayName: 'Master Mobil' 
            }
        })

		.state('app.promise.procurement-master-barang-index', {
        	url: '/master/barang',
          	templateUrl: Route.base('promise/procurement/master/barang/barang.index.html'),
          	resolve: {
	              assets: Route.require('datatables')
	          },
	          data: {
	              displayName: 'Master Barang' 
	          }
	     })
      
      	.state('app.promise.procurement-master-barang-add-edit', {
	          url: '/master/barang/view',
	          templateUrl: Route.base('promise/procurement/master/barang/barang.add.edit.html'),
	          params: {
	              todo: null,
	              barang:null
	          },
	          resolve: {
	              assets: Route.require('datatables','ui.select')
	          },
	          data: {
	              displayName: 'Master Barang' 
	          }
      	})
      	
      	.state('app.promise.procurement-master-pelanggan-index', {
            url: '/master/pelanggan',
            templateUrl: Route.base('promise/procurement/master/pelanggan/pelanggan.index.html'),
            resolve: {
                assets: Route.require('datatables')
            },
            data: {
                displayName: 'Master Pelanggan' 
            }
        })
        
        .state('app.promise.procurement-master-pelanggan-add-edit', {
            url: '/master/pelanggan/view',
            templateUrl: Route.base('promise/procurement/master/pelanggan/pelanggan.add.edit.html'),
            params: {
                todo: null,
                pelanggan:null
            },
            resolve: {
                assets: Route.require('datatables','ui.select')
            },
            data: {
                displayName: 'Master Pelanggan' 
            }
        })
          
        .state('app.promise.procurement-transaction-penjualan-index', {
              url: '/transaksi/penjualan',
              templateUrl: Route.base('promise/procurement/transaction/penjualan/penjualan.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Penjualan' 
              }
          })
          
          .state('app.promise.procurement-transaction-penjualan-detail', {
              url: '/transaksi/penjualan/detail',
              templateUrl: Route.base('promise/procurement/transaction/penjualan/penjualan.detail.html'),
              params: {
                  todo: null,
                  penjualan:null
              },
              resolve: {
                  assets: Route.require('datatables','ui.select')
              },
              data: {
                  displayName: 'Penjualan / Detail' 
              }
          })
          
          .state('app.promise.procurement-transaction-laporanpenjualan-index', {
              url: '/transaksi/laporanpenjualan',
              templateUrl: Route.base('promise/procurement/transaction/laporanpenjualan/laporanpenjualan.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Laporan Penjualan' 
              }
          })
        
        .state('app.promise.procurement-bonus-index', {
              url: '/bonus',
              templateUrl: Route.base('promise/procurement/bonus/bonus.index.html'),
              resolve: {
                  assets: Route.require('datatables')
              },
              data: {
                  displayName: 'Data Bonus' 
              }
          })
          
          .state('app.promise.procurement-approval-barang-index', {
        	url: '/Approval/Barang',
          	templateUrl: Route.base('promise/procurement/approval/approval.data.barang.index.html'),
          	resolve: {
	              assets: Route.require('datatables')
	          },
	          data: {
	              displayName: 'Approval Master Barang' 
	          }
	     })
      
      	.state('app.promise.procurement-approval-barang-detail', {
	          url: '/Approval/Barang/Detail',
	          templateUrl: Route.base('promise/procurement/approval/approval.data.barang.detail.html'),
	          params: {
	              todo: null,
	              barangHistory:null
	          },
	          resolve: {
	              assets: Route.require('datatables','ui.select')
	          },
	          data: {
	              displayName: 'Approval Master Barang' 
	          }
      	})
          ;
          
           /*========================================================== END CENTRA AUTO AC =================================================================================*/
		
    }


    /* Insert security path tools */
    function insertSecurityPath(token, activeMenuId, url, $rootScope, $http) {

      //  var validUrlContain = $rootScope.backendAddress;
       // var requestPath = validUrlContain+url;
    	
    	  var validUrlContain = $rootScope.backendAddress ;
          var requestPath = url;

        

        // sample result = /procurement/inisialisasi/getPengadaanList
        if (requestPath.indexOf(validUrlContain) > -1) {
            requestPath = requestPath.replace(validUrlContain, "");
            // clean requestpath from parameter
            requestPath = cleanRequestPathFromParameter(requestPath);
            // console.log('token = ' + token + ' active menu id = ' +
			// activeMenuId + ', requestpath ' + requestPath);
            var myForm = [];
            myForm.requestPath = requestPath;
            myForm.token = "a";
            myForm.activeMenuId = 0;
            	if (typeof token != 'undefined' ){
            		 myForm.token = token;
            	}
            	
            	if (typeof activeMenuId != 'undefined'){
            		 myForm.activeMenuId = activeMenuId;
            	}
                
            	//if ((typeof activeMenuId != 'undefined') && (activeMenuId != null) &&  ((typeof token != 'undefined'))) { 
		            $http({
		                method: 'POST',
		                url: $rootScope.backendAddress + '/procurement/menu/securityToolServices/insertSecurity',
		                headers: {
		                    'Content-Type': 'application/x-www-form-urlencoded'
		                },
		                transformRequest: function (obj) {
		                    var str = [];
		                    for (var p in obj) {
		                        var val = obj[p];
		                        if ((typeof val) == 'object') {
		                            if (val != null)
		                                val = val['id'];
		                        }
		                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(val));
		                    }
		                    return str.join("&");
		                },
		                data: myForm
		            }).success(function (data, status, headers, config) {
		            	// console.log('>> insert security sukses');
		            })
            	//}
        }
    }

    function cleanRequestPathFromParameter(path) {
        var str = "";

        path = path.replace('/promisews', '');

        var strs = path.split("/");
        var ctr = 0;
        for (var i = 0; i < strs.length; i++) {
            var s = strs[i];
            if (isNumeric(s)) {
                break;
            } else {
                if (ctr != 0) {
                    str += ("/" + s);
                };
                ctr++;
            }
        }
        return str;
    }

    function isNumeric(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }


})();