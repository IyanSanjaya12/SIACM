(function () {
    'use strict';

    angular.module('naut').controller('PenawaranVendorIndependenceHargaSatuanMaterialController', PenawaranVendorIndependenceHargaSatuanMaterialController);

    function PenawaranVendorIndependenceHargaSatuanMaterialController($scope, $http, $rootScope, $resource, $location, toaster, $modal, $state, $filter) {
        var pptc = this;
        
        if (typeof $rootScope.pengadaanVendor !== 'undefined') {
            $scope.pengadaanVendor = $rootScope.pengadaanVendor;
        }

        $scope.pengadaanId = $scope.pengadaanVendor.pengadaan.id;

        $scope.loadDataVendor = function () {
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                .success(function (data, status, headers, config) {
                    $scope.vendor = data;
                    $rootScope.idVendor = $scope.vendor.id;
                    loadVendorItemConditionalPrice();
                    $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + $scope.vendor.id + '/' + $scope.pengadaanId, {
                        ignoreLoadingBar: true
                    }).success(function (data, status, headers, config) {
                        var suratPenawaranList = data;
                        if (suratPenawaranList.length == 0) {
                            pptc.suratPenawaran = {};
                        } else {
                            pptc.suratPenawaran = suratPenawaranList[0];
                        }
                        pptc.loading = false;
						
						$http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
						.success(function (data, status, headers, config) {
								$scope.mataUangList = data;
								if (pptc.suratPenawaran != undefined && pptc.suratPenawaran.mataUang != undefined) {
									pptc.mataUang = pptc.suratPenawaran.mataUang;
								}
						})
						.error(function (data, status, headers, config) {});

                        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + $scope.vendor.id, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                            pptc.penawaranPertamaList = data;

                        }).error(function (data, status, headers, config) {});
                    }).error(function (data, status, headers, config) {});
                }).error(function (data, status, headers, config) {});
        }
        $scope.loadDataVendor();

        /* Detail Item Pengadaan */
        if (typeof $rootScope.itemPengadaanDetail !== 'undefined') {
            $scope.itemPengadaanDetail = $rootScope.itemPengadaanDetail;
        }
        $rootScope.pengadaanId = $scope.itemPengadaanDetail.pengadaan.id;
        $rootScope.itemPengadaanId = $scope.itemPengadaanDetail.id;
        $scope.itemId = $scope.itemPengadaanDetail.item.id;


        //-----------------------------------------------------------------------------------------------------------

        $scope.addItemCondition = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/addItemCondition.html',
                controller: ModalInstanceAddItemCtrl,
                size: size
            });
        };
        var ModalInstanceAddItemCtrl = function ($scope, $modalInstance) {
            $scope.itemCondition = {};
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getByTipe/2')
                .success(function (data, status, headers, config) {
                    $scope.itemConditionList = data;
                }).error(function (data, status, headers, config) {});

            $scope.simpanItem = function () {
                if (typeof $scope.itemCondition.selected === 'undefined' || $scope.itemCondition.selected == '') {
                    alert("Type condition belum di pilih.");
                } else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Nilai amount belum di isi.");
                } else {
                    var formVendorConditionPrice = {
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $rootScope.itemPengadaanId,
                        conditionalPrice: $scope.itemCondition.selected.id,
                        nilai: $scope.nilai
                    }

                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/insert',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formVendorConditionPrice
                    }).success(function (data, status, headers, config) {
                        loadVendorItemConditionalPrice();
                        $modalInstance.close('closed');
                    });
                }

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel'); 
            };
        };
        ModalInstanceAddItemCtrl.$inject = ['$scope', '$modalInstance'];

        $scope.editItemCondition = function (mt, size) {
            $rootScope.editItemCondition = mt;
            var modalInstance = $modal.open({
                templateUrl: '/editItemCondition.html',
                controller: ModalInstanceEditItemCtrl,
                size: size
            });
        };
        var ModalInstanceEditItemCtrl = function ($scope, $modalInstance) {
            var itemKondisi;
            if (typeof $rootScope.editItemCondition !== 'undefined') {
                itemKondisi = $rootScope.editItemCondition;
            }
            
            //console.log('itemKondisi =' + JSON.stringify(itemKondisi))
            $scope.nilai = itemKondisi.nilai;
            $scope.itemCondition = {};
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getByTipe/2')
                .success(function (data, status, headers, config) {
                    $scope.itemConditionList = data;
                }).error(function (data, status, headers, config) {});
            $scope.itemCondition.conditionalPrice = itemKondisi.conditionalPrice;
            $scope.simpanItem = function () {
                if (typeof $scope.itemCondition.conditionalPrice === 'undefined' || $scope.itemCondition.conditionalPrice == '') {
                    alert("Type Condition Belum Dipilih.");
                } else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Amount Belum Diisi.");
                } else {
                    var formVendorConditionPrice = {
                        id: $rootScope.editItemCondition.id,
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $rootScope.itemPengadaanId,
                        conditionalPrice: $scope.itemCondition.conditionalPrice.id,
                        nilai: $scope.nilai
                    }

                    $http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/update',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        transformRequest: function (obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: formVendorConditionPrice
                    }).success(function (data, status, headers, config) {
                        loadVendorItemConditionalPrice();
                        $modalInstance.close('closed');
                    });
                }
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceEditItemCtrl.$inject = ['$scope', '$modalInstance'];

        //Item Condition Delete
        $scope.removeItem = function (id, size) {
            var modalInstance = $modal.open({
                templateUrl: '/removeItem.html',
                controller: ModalInstanceItemCtrl,
                size: size,
                resolve: {
                    itemKode: function () {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/deleteRow/' + id).success(function (data, status, headers, config) {
                    loadVendorItemConditionalPrice();
                }).error(function (data, status, headers, config) {})
            }, function () {});
        };
        var ModalInstanceItemCtrl = function ($scope, $modalInstance, itemKode) {
            $scope.itemKode = itemKode;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceItemCtrl.$inject = ['$scope', '$modalInstance', 'itemKode'];

        function loadVendorItemConditionalPrice() {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAnditemPengadaanAndTipe/' + $rootScope.idVendor + '/' + $scope.pengadaanId + '/'+ $rootScope.itemPengadaanId + '/'+ 2, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pptc.itemConditionPriceList = data;
                //console.log('itemConditionPriceList = ' + JSON.stringify(pptc.itemConditionPriceList));
                $scope.hitungTotalPrice();                
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }

        //---------------------------------------End Item Condition--------------------------------------------------

        $scope.mataUang = {};
        $scope.getMataUang = function () {
            
        }
        $scope.getMataUang();

        /*--------------------------------------------------------------------------------------*/
        var itemPengadaan = $scope.itemPengadaanDetail;
        $scope.hitungTotalPrice = function () {

            itemPengadaan.penawaranDetail.hargaTotalAfterCondition = 0;

            itemPengadaan.penawaranDetail.hargaTotalOri = itemPengadaan.kuantitas * itemPengadaan.penawaranDetail.hargaSatuanOri;
            itemPengadaan.penawaranDetail.hargaSatuanAfterCondition = itemPengadaan.penawaranDetail.hargaTotalOri;
            itemPengadaan.itemCP = 0;

            //hitung item CP
            angular.forEach(pptc.itemConditionPriceList, function (cp) {
                var valCP = hitungCp(itemPengadaan.penawaranDetail.hargaSatuanOri, cp);
                itemPengadaan.itemCP = parseFloat(itemPengadaan.itemCP) + parseFloat(valCP);
                itemPengadaan.penawaranDetail.hargaTotalOri = parseFloat(itemPengadaan.penawaranDetail.hargaSatuanOri) + parseFloat(valCP);
                itemPengadaan.penawaranDetail.hargaSatuanAfterCondition = itemPengadaan.penawaranDetail.hargaTotalOri * itemPengadaan.kuantitas;
                itemPengadaan.penawaranDetail.hargaTotalAfterCondition = itemPengadaan.penawaranDetail.hargaSatuanAfterCondition;
            })
        }
        var hitungCp = function (val, cp) {
                var retVal = 0;
                if (cp.conditionalPrice.isPersentage == 1) {
                    retVal = (val * (cp.nilai / 100)) * cp.conditionalPrice.fungsi;
                } else {
                    retVal = (cp.nilai * cp.conditionalPrice.fungsi);
                }
                return retVal;
            }
            //----------------------------------------------------------------------------------------

        $scope.btnSimpanPenawaran = function () {
            if (typeof itemPengadaan.penawaranDetail.hargaSatuanOri === 'undefined' || itemPengadaan.penawaranDetail.hargaSatuanOri == '') {
                toaster.pop('warning', 'Kesalahan', 'Nilai HPS belum disi...');
            } else {
            	simpanSuratPenawaran();
            }
        }
		
		function simpanSuratPenawaran() {
            var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/';
            if (typeof pptc.suratPenawaran.id !== 'undefined') {
                uri = uri + 'updateSuratPenawaran';
            } else {
                uri = uri + 'insertSuratPenawaran';
                pptc.suratPenawaran = {
	                pengadaan: $scope.pengadaanId,
	                vendor: $scope.vendor.id,
	                tanggalAwal: $filter('date')(new Date(), 'yyyy-MM-dd'),
	                mataUang: pptc.mataUang.id
                };
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
                    data: pptc.suratPenawaran
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    simpanPenawaranPertama(data.id);
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
        }

        function simpanPenawaranPertama(suratPenawaranId) {
            var formPenawaranPertama = {
                id: pptc.penawaranPertamaList.id,
                suratPenawaran: suratPenawaranId,
                nilaiPenawaran: itemPengadaan.penawaranDetail.hargaTotalAfterCondition,
                nilaiPenawaranOri: itemPengadaan.penawaranDetail.hargaTotalOri
            }
            var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/';
            if (typeof pptc.penawaranPertamaList.id !== 'undefined') {
                uri = uri + 'update';
                formPenawaranPertama;
            } else {
                uri = uri + 'insert';
                formPenawaranPertama;
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
                    data: formPenawaranPertama
                })
                .success(function (data, status, headers, config) {
                    $scope.loading = false;
                    simpanPenawaranPertamaDetail(data.id);
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
        }

        function simpanPenawaranPertamaDetail(penawaranPertamaId) {
            var formPenawaranPertamaDetail = {
                id: itemPengadaan.penawaranDetail.id,
                itemPengadaan: itemPengadaan.id,
                penawaranPertama: penawaranPertamaId,
                hargaSatuanAfterCondition: itemPengadaan.penawaranDetail.hargaSatuanAfterCondition,
                hargaTotalOri: itemPengadaan.penawaranDetail.hargaTotalOri,
                hargaSatuanOri: itemPengadaan.penawaranDetail.hargaSatuanOri,
                hargaTotalAfterCondition: itemPengadaan.penawaranDetail.hargaTotalAfterCondition,
                jumlah: itemPengadaan.kuantitas
            }
            var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/';
            if (typeof itemPengadaan.penawaranDetail.id !== 'undefined') {
                uri = uri + 'update';
                formPenawaranPertamaDetail;
            } else {
                uri = uri + 'insert';
                formPenawaranPertamaDetail;
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
                    data: formPenawaranPertamaDetail
                })
                .success(function (data, status, headers, config) {
                    $scope.btnSimpanPenawaran = false;
                    $scope.loading = false;
                    if (typeof data !== 'undefined') {
                        $scope.showSuccess();
                    }
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
        }


        $scope.showSuccess = function () {
            $scope.loading = false;
            toaster.pop('success', 'Sukses', 'Data penawaran harga berhasil disimpan..');
        }

        $scope.back = function () {
        	$state.go('appvendor.promise.procurement-vendor-penawaranvendorindependencehargasatuan');
        };


    }

    PenawaranVendorIndependenceHargaSatuanMaterialController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', '$state', '$filter'];

})();