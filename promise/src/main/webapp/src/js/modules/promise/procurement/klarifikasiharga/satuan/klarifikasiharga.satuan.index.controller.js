(function () {
    'use strict';

    angular.module('naut')
        .controller('KlarifikasiHargaSatuanController', KlarifikasiHargaSatuanController);

    function KlarifikasiHargaSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $q, $timeout, $filter, $state, ngTableParams) {
        var form = this;
        $scope.isDisabled = true;
        $scope.allStored = null;
       /* START Detail Pengadaan */
        if (typeof $rootScope.detilPengadaan !== 'undefined') {
            $scope.detilPengadaan = $rootScope.detilPengadaan;
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        //$scope.allItemPengadaan=[];
        /*---END Detail Pengadaan----*/


        /* START Bidang Usaha Pengadaan*/
        $scope.getListBidangUsaha = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                form.subBidangUsahaByPengadaanIdList = data;
            }).error(function (data, status, headers, config) {
            	console.log("** ERROR BidangUsahaPengadaanServices - getBidangUsahaPengadaanByPengadaanId ***");
            });
        }
        $scope.getListBidangUsaha();
        /*---END Bidang Usaha Pengadaan---*/

       //  START Rincian Kebutuhan Material
        $scope.getListItemPengadaan = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if(data.length>0)
    				$scope.isMaterialNotEmpty = true;

                var tableMaterialPengadaanList = data
                form.tableMaterialPengadaanList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: tableMaterialPengadaanList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(tableMaterialPengadaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
                $scope.validate(data);
            }).error(function (data, status, headers, config) {
            	console.log("** ERROR ItemPengadaanServices - getItemPengadaanMaterialByPengadaanId ***");
            });
        }
        $scope.getListItemPengadaan();
        /*---END Rincian Kebutuhan Materrial---*/

        /* START Rincian Kebutuhan Jasa*/
        $scope.getListJasaPengadaan = function (){
        	$scope.loading = true;
        	$http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
        		ignoreLoadingBar: true
        	}).success(function(data, status, headers, config) {
        		$scope.loading = false;
        		if(data.length>0)
            		$scope.isJasaNotEmpty = true;
        		
        		var tableJasaPengadaanIdList = data
                form.tableJasaPengadaanIdList = new ngTableParams({
        			page: 1,
        			count: 5
        		}, {
        			total: data.length,
        			getData: function($defer, params) {
        				$defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
        			}
        		});
        	   $scope.validate(data);
        	}).error(function(data, status, headers, config) {
        		console.log("** ERROR ItemPengadaanServices - getItemPengadaanJasaByPengadaanId ***");
        	});
        }
        /*---END Rincian Kebutuhan Jasa---*/
   
        $scope.getListJasaPengadaan()
  
        /* Validasi klarifikasi harga satuan*/
        
        $scope.validate = function (itemList) {
        	if (($scope.allStored == true || $scope.allStored == null) && itemList.length > 0 ){
        		for (var i = 0; i < itemList.length; i++) {
        			var itemId= itemList[i].item.id;
        			$http.get($rootScope.backendAddress + '/procurement/KlarifikasiHargaSatuan/klarifikasiHargaSatuanServices/getKlarifikasiHargaSatuanByPengadaanAndItem/' + $scope.pengadaanId + '/' + itemId, {
        				ignoreLoadingBar: true
                		}).success(function (data, status, headers, config) {
                			if (data.length > 0) {
                				$scope.allStored  = true;
                			} else {
                				$scope.allStored  = false;
                			}                         		   
                		}).error(function (data, status, headers, config) {
                			console.log("** ERROR klarifikasiHargaSatuanServices - getKlarifikasiHargaSatuanByPengadaanAndItem ***");
                			$scope.loading = false;
                		});
        			if ($scope.allStored == false) {
        				$scope.isDisabled = true;
        				break;
        			} else {
        				$scope.isDisabled = false;
        			}
        		}
        	}
        }
            
        /* End validasi */
        /* get Next Tahapan */
        $scope.updatePengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.nextTahapan = data;
                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: {
                            pengadaanId: $scope.pengadaanId,
                            tahapanPengadaanId: $scope.nextTahapan
                        }
                    }).success(function (data, status, headers, config) {
                        $scope.loading = false;
                        //$location.path('app/promise/procurement/klarifikasiharga');
                    });
                })
                .error(function (data, status, headers, config) {
                    console.log('get next error');
                });
        }

        $scope.showSuccess = function () {
            $scope.loadingSaving = false;
            toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
            $scope.back();
        }
        //save
        $scope.saveData = function () {
        	//$scope.updatePengadaan();
        	$scope.showSuccess();
        }

        $scope.back = function () {
        	$state.go('app.promise.procurement-panitia-klarifikasiharga');
        }
        
        $scope.edit = function(data) {
            $rootScope.detilItemPengadaan = data;
            $state.go('app.promise.procurement-panitia-klarifikasiharga-satuan-detil');
        };

        /*modal Confirmation*/
        $scope.goModalConfirmation = function (data) {
            $rootScope.saveAfterConfirmation = $scope.saveData;
            var modalInstance = $modal.open({
                templateUrl: '/ModalConfirmation.html',
                controller: ModalInstanceConfirmationCtrl,
            });
        };
        
        /*---Modal Confirmation---*/
        var ModalInstanceConfirmationCtrl = function ($scope, $http, $modalInstance, $resource, $rootScope) {

            $scope.ok = function () {
                $rootScope.saveAfterConfirmation();
                $modalInstance.dismiss('close');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceConfirmationCtrl.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        /*------*/
    }

    KlarifikasiHargaSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$q', '$timeout', '$filter', '$state', 'ngTableParams'];

})();
