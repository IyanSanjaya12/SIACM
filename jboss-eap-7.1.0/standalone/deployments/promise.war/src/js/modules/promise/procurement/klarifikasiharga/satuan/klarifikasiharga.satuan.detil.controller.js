(function () {
    'use strict';

    angular.module('naut')
        .controller('KlarifikasiHargaSatuanDetilController', KlarifikasiHargaSatuanDetilController);

    function KlarifikasiHargaSatuanDetilController($scope, $http, $rootScope, $resource, $location, toaster, $window, $modal, $timeout, $state, ngTableParams) {
        var form = this;
        $scope.isDisabled = false;
       /* START Detail Item Pengadaan */
        if (typeof $rootScope.detilItemPengadaan !== 'undefined') {
            $scope.detilItemPengadaan = $rootScope.detilItemPengadaan;
        }
        $scope.itemPengadaanId = $scope.detilItemPengadaan.id;
        $scope.pengadaanId = $scope.detilItemPengadaan.pengadaan.id;
        $scope.itemId = $scope.detilItemPengadaan.item.id;
        $scope.detilPengadaan = $scope.detilItemPengadaan.pengadaan;
        /*---END Detail Item Pengadaan----*/

        /* START List Vendor*/
        $scope.getListVendor = function () {
            $scope.loading = true;
            $scope.list = [];
            var uri = $rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByPengadaan/' + $scope.pengadaanId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                angular.forEach(data, function (value, indeks) {
                    //console.log(value);
                    data[indeks]['finalCheck'] = false;
                    data[indeks]['satuanHarga'] = 0;
                    $scope.getKlarifikasiHarga(data[indeks], data[indeks].vendor.id);
                    $scope.getSatuanHarga(data[indeks], data[indeks].vendor.id, $scope.detilPengadaan.id);
                    $scope.list.push(form);
                })
                $scope.listVendors = data;
            }).error(function (data, status, headers, config) {

            });
        }
        $scope.getListVendor();

       $scope.getKlarifikasiHarga = function (itemList, vendor) {
            $scope.loading = true;
            $scope.list = [];
            var uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaSatuan/klarifikasiHargaSatuanServices/getKlarifikasiHargaSatuanByVendorAndPengadaanAndItem/' + vendor + '/' + $scope.pengadaanId + '/' + $scope.itemId;
            $http.get(uri, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                console.log('setelah sukses : ' + vendor + '/' + $scope.pengadaanId + '/' + $scope.itemId+ ' : '+data.length);
                if (data.length > 0) {
                    itemList['finalCheck'] = data[0].finalCheck;
                    itemList['idKlarifikasiHargaSatuan']=data[0].id;
                } else {
                    form.finalCheck = false;
                }
            }).error(function (data, status, headers, config) {
            	console.log('***ERROR klarifikasiHargaSatuanServices - getKlarifikasiHargaSatuanByVendorAndPengadaanAndItem');
            });
        }

       $scope.getSatuanHarga = function (itemList, vendor) {
           $scope.loading = true;
           $scope.list = [];
           /* Get negosiasi Id first*/
           $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByVendorAndItemPengadaan/'+ vendor + '/' + $scope.itemPengadaanId, {
               ignoreLoadingBar: true
           }).success(function (data, status, headers, config) {
               $scope.loading = false;
               if (data.length > 0) {
            	   /*Cek harga total dari negosiasi*/
            	   /*$http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiDetilServices' + pengadaan + '/' + vendor, {
                       ignoreLoadingBar: true
                   }).success(function (data, status, headers, config) {
                	   if (data.length > 0) {
                           itemList['satuanHarga'] = data[0].nilaiPenawaran;
                	   } else {
                		   itemList['satuanHarga'] = 100;
                	   }
                		   
                   }).error(function (data, status, headers, config) {
                       console.log("** ERROR penawaranPertamaServices - getByPengadaanAndVendor ***");
                       $scope.loading = false;
                   });*/
                   itemList['satuanHarga'] = data[0].negosiasiDetil.hargaTotalAfterCondition;
               } else {
                   console.log("Data negosiasi tidak ditemukan");
                   /*Cek harga total dari auction*/
                   $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByVendorAndItemPengadaan/' + vendor + '/' + $scope.itemPengadaanId, {
                       ignoreLoadingBar: true
                   }).success(function (data, status, headers, config) {
                	   if (data.length > 0) {
                           itemList['satuanHarga'] = data[0].hargaTotalAfterCondition;
                       } else {
                    	   console.log("Data auction tidak ditemukan");
                    	   /*Cek harga total dari penawaran pertama*/
                    	   $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByVendorAndItemPengadaan/' + vendor + '/' + $scope.itemPengadaanId, {
                               ignoreLoadingBar: true
                           }).success(function (data, status, headers, config) {
                        	   if (data.length > 0) {
                                   itemList['satuanHarga'] = data[0].hargaTotalAfterCondition;
                        	   }                        		   
                           }).error(function (data, status, headers, config) {
                               console.log("** ERROR penawaranPertamaServices - getByPengadaanAndVendor ***");
                               $scope.loading = false;
                           });
                       }
                   }).error(function (data, status, headers, config) {
                       console.log("** ERROR PenawaranAuctionServices - getByVendorPengadaan ***");
                       $scope.loading = false;
                   });
               }
           }).error(function (data, status, headers, config) {
        	   console.log("** ERROR NegosiasiServices - getLastFromVendorByPengadaanAndVendor ***");
               $scope.loading = false;
           });
       }
   
        	$scope.checked = function (data) {
            var obj = $scope.listVendors.indexOf(data);
            if ($scope.listVendors[obj].finalCheck == true) {
                $scope.list[obj].finalCheck = true;
            } else {
                $scope.list[obj].finalCheck = false;
            }
            $scope.isDisabled = false;
        }

        $scope.proses = function (indeks) {
        	console.log(JSON.stringify($scope.listVendors));
            $scope.loading = true;
            form = new Object();
            form.finalCheck = $scope.listVendors[indeks].finalCheck;
            form.id = $scope.listVendors[indeks].idKlarifikasiHargaSatuan;
            form.pengadaan = $scope.detilPengadaan.id;
            form.vendor = $scope.listVendors[indeks].vendor.id;
            form.item = $scope.itemId;
            console.log(JSON.stringify(form)+'=>'+$scope.listVendors[indeks].idKlarifikasiHargaSatuan);
            var uri = '';
            if (typeof $scope.listVendors[indeks].idKlarifikasiHargaSatuan !== 'undefined') {
            	console.log(typeof $scope.listVendors[indeks].idKlarifikasiHargaSatuan+" : UPDATE");
                uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaSatuan/klarifikasiHargaSatuanServices/update';
            } else {
            	console.log(typeof $scope.listVendors[indeks].idKlarifikasiHargaSatuan+" : CREATE");
                uri = $rootScope.backendAddress + '/procurement/KlarifikasiHargaSatuan/klarifikasiHargaSatuanServices/create';
            }
            $http({
                method: 'POST',
                url: uri,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: form
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (($scope.list.length - 1) != indeks) {
                    $scope.proses(indeks + 1);
                }
                $scope.showSuccess();
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        }
   
        $scope.showSuccess = function () {
            $scope.loadingSaving = false;
            toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
            //$scope.back();
        }
        
        //save
        $scope.saveData = function () {
                $scope.proses(0);
                $scope.isDisabled = true;
        }

        $scope.back = function () {
        	$rootScope.detilPengadaan = $scope.detilPengadaan;
        	$state.go('app.promise.procurement-panitia-klarifikasiharga-satuan');
        }

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

    KlarifikasiHargaSatuanDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', '$modal', '$timeout', '$state', 'ngTableParams'];

})();
