/**=========================================================
 * FileName		: promise.directive.viewDataPengadaan.js
 * Purpose		: Generate view of data pengadaan
 * Usage        : <promise-view-data-pengadaan pengadaan-id=pengadaanId></promise-view-data-pengadaan>
 * Option       : Put 'hide-Item' attribute to hide service & material items 
 *                e.g :   <promise-view-data-pengadaan pengadaan-id=pengadaanId hide-Item></promise-view-data-pengadaan>
 * Author		: Reinhard
 * Modify       :
 *                - Mamat, add total HPS
 * Since      	: Sep 18, 2015
 =========================================================*/


angular.module('naut')
    .directive('promiseViewDataPengadaan', function ($http, $rootScope, ngTableParams) {
        return {
            restrict: 'E',
            scope: {
                pengadaanId: '=pengadaanId'
            },
            templateUrl: 'app/views/promise/directive/promise.directive.viewDataPengadaan.html',
            link: function (scope, elem, attrs) {
                scope.isMaterialNotEmpty = false;
                scope.isJasaNotEmpty = false;
                scope.totalHPSBarangJasa = 0;
                scope.isVendorNotEmpty = false;

                /*is hideItem exist in attrs?*/
                scope.hideItem = 'hideItem' in attrs;

                if (typeof $rootScope.promiseDirectiveViewDataPengadaan == 'undefined' || $rootScope.promiseDirectiveViewDataPengadaan==null) {
                    $rootScope.promiseDirectiveViewDataPengadaan = {};
                } else if (($rootScope.promiseDirectiveViewDataPengadaan.id == scope.pengadaanId) && ($rootScope.promiseDirectiveViewDataPengadaan.hideItem == scope.hideItem)) {
                    console.log('ViewDataPengadaan: load from cache..');
                    scope.viewDataPengadaan = $rootScope.promiseDirectiveViewDataPengadaan.viewDataPengadaan;
                    scope.bidangUsahaList = $rootScope.promiseDirectiveViewDataPengadaan.bidangUsahaList;
                    scope.materialList = $rootScope.promiseDirectiveViewDataPengadaan.materialList;
                    angular.forEach(scope.materialList.data, function (value, index) {
                        scope.totalHPSBarangJasa = scope.totalHPSBarangJasa + value.totalHPS;
                    });
                    scope.jasaList = $rootScope.promiseDirectiveViewDataPengadaan.jasaList;
                    angular.forEach(scope.jasaList.data, function (value, index) {
                        scope.totalHPSBarangJasa = scope.totalHPSBarangJasa + value.totalHPS;
                    });
                    scope.hideItem = $rootScope.promiseDirectiveViewDataPengadaan.hideItem;
                    scope.isMaterialNotEmpty = $rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty;
                    scope.isJasaNotEmpty = $rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty;
                    return;
                }

                console.log('ViewDataPengadaan: load from services..');

                //Cache it for next use when needed
                $rootScope.promiseDirectiveViewDataPengadaan.id = scope.pengadaanId;
                $rootScope.promiseDirectiveViewDataPengadaan.hideItem = scope.hideItem;

                scope.isLoading = true;

                /*Load Data Pengadaan*/
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaan/' + scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                    	
                    scope.viewDataPengadaan = data;

                    /*Load Bidang Usaha*/
                    $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {

                        scope.bidangUsahaList = data;
                                             
                        //Cache it for next use when needed
                        $rootScope.promiseDirectiveViewDataPengadaan.bidangUsahaList = scope.bidangUsahaList;
                        $rootScope.promiseDirectiveViewDataPengadaan.viewDataPengadaan = scope.viewDataPengadaan;
                        if (!scope.hideItem) {
                        	
                        	 /*Load Data Vendor*/
                            $http.get($rootScope.backendAddress +'/procurement/jadwalPengadaanServices/getVendorByPengadaanId/' +scope.pengadaanId,{
                            	ignoreLoadingBar: true
                            }).success(function (data, status, headers, config){
                            	scope.dataVendorList = data;
                            	if(data.length > 0){
                            		scope.isVendorNotEmpty = true;
                            	}
                        	
                            /*Load Material*/
                            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + scope.pengadaanId, {
                                ignoreLoadingBar: true
                            }).success(function (data, status, headers, config) {
                                if (data.length > 0) {
                                    scope.isMaterialNotEmpty = true;
                                    $rootScope.promiseDirectiveViewDataPengadaan.isMaterialNotEmpty = scope.isMaterialNotEmpty;
                                }
                                var objList = data;
                                scope.materialList = new ngTableParams({
                                    page: 1,
                                    count: 10
                                }, {
                                    total: objList.length,
                                    getData: function ($defer, params) {
                                        $defer.resolve(objList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                    }
                                });
                                angular.forEach(objList, function (value, index) {
                                    scope.totalHPSBarangJasa = scope.totalHPSBarangJasa + value.totalHPS;
                                });
                                /*Load Jasa*/
                                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + scope.pengadaanId, {
                                    ignoreLoadingBar: true
                                }).success(function (data, status, headers, config) {
                                    if (data.length > 0) {
                                        scope.isJasaNotEmpty = true;
                                        $rootScope.promiseDirectiveViewDataPengadaan.isJasaNotEmpty = scope.isJasaNotEmpty;
                                    }
                                    var objList = data;
                                    scope.jasaList = new ngTableParams({
                                        page: 1,
                                        count: 10
                                    }, {
                                        total: objList.length,
                                        getData: function ($defer, params) {
                                            $defer.resolve(objList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                                        }
                                    });
                                    scope.isLoading = false;

                                    //Cache it for next use when needed
                                    $rootScope.promiseDirectiveViewDataPengadaan.materialList = scope.materialList;
                                    $rootScope.promiseDirectiveViewDataPengadaan.jasaList = scope.jasaList;

                                    angular.forEach(objList, function (value, index) {
                                        scope.totalHPSBarangJasa = scope.totalHPSBarangJasa + value.totalHPS;
                                    });


                                }).error(function (data, status, headers, config) {});
                            }).error(function (data, status, headers, config) {});
                          }).error(function (data, status, headers, config) {});

                        } else {
                            scope.isLoading = false;
                        } /*END IF*/

                    }).error(function (data, status, headers, config) {});
                }).error(function (data, status, headers, config) {});
              
            }
        };
    });