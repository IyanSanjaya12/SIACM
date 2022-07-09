(function () {
    'use strict';

    angular.module('naut').controller('PenawaranVendorHargaTotalController', PenawaranVendorHargaTotalController)
        .filter('propsFilter', propsFilter)
        .filter('formatMoney', formatMoney);

    function PenawaranVendorHargaTotalController(ModalService, $scope, $http, $rootScope, $resource, $location, toaster, $modal) {
        var pptc = this;
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        if (typeof $rootScope.pengadaanVendor !== 'undefined') {
            $scope.pengadaanVendor = $rootScope.pengadaanVendor;
        } else {
            console.log('INFO Error');
        }
        $scope.pengadaanId = $scope.pengadaanVendor.pengadaan.id;
        //console.log('$scope.pengadaanVendor = ' + JSON.stringify($scope.pengadaanVendor));
        $rootScope.pengadaanId = $scope.pengadaanVendor.pengadaan.id;
        if($rootScope.dataNilai != 'undefined') {
        	$rootScope.dataNilai = 1;
        }

        $scope.mataUang = 1;
        $scope.getMataUang = function () {
            $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
                .success(function (data, status, headers, config) {
                    $scope.mataUangList = data;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getMataUang();
        $scope.test = function () {
            console.log("Uang >> " + JSON.stringify($scope.mataUang));
        }
        $scope.loadSuratPenawaranHarga = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                var itemPengadaanList = data;
                if (itemPengadaanList.length == 0) {
                    pptc.itemPengadaanList = {};
                } else {
                    pptc.itemPengadaanList = itemPengadaanList[0];
                    $rootScope.itemPengadaanId = pptc.itemPengadaanList.id;
                }

                $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id).success(function (data, status, headers, config) {
                    $scope.loading = true;
                    $scope.vendor = data;
                    $rootScope.idVendor = $scope.vendor.id;
                    loadVendorConditionalPrice();
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
                        $scope.loading = false;
                        $scope.suratPenawaranId = pptc.suratPenawaran.id;
                        if (typeof $scope.suratPenawaranId === 'undefined' || $scope.suratPenawaranId == 0) {
                            $scope.konfirmasi();
                        }

                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                            if (data.length > 0)
                                $scope.isMaterialNotEmpty = true;
                            pptc.itemPengadaanMaterialByPengadaanIdList = data;
                            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (itemPengadaanMaterial) {
                                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByVendorAndItemPengadaan/' + $scope.vendor.id + '/' + itemPengadaanMaterial.id, {
                                    ignoreLoadingBar: true
                                }).success(function (data, status, headers, config) {
                                    $scope.loading = false;
                                    itemPengadaanMaterial.penawaranDetail = {};
                                    if (data != "") {
                                        itemPengadaanMaterial.penawaranDetail = data;
                                        $scope.mataUang = data.mataUang.id;
                                        $scope.hitungTotalPrice();
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.loading = false;
                                });
                            });
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            if (data.length > 0)
                                $scope.isJasaNotEmpty = true;
                            pptc.itemPengadaanJasaByPengadaanIdList = data;
                            angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (itemPengadaanJasa) {
                                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByVendorAndItemPengadaan/' + $scope.vendor.id + '/' + itemPengadaanJasa.id, {
                                    ignoreLoadingBar: true
                                }).success(function (data, status, headers, config) {
                                    $scope.loading = false;
                                    //console.log('data = '+ JSON.stringify(data));
                                    itemPengadaanJasa.penawaranDetail = {};
                                    if (data != null) {
                                        itemPengadaanJasa.penawaranDetail = data;
                                        $scope.mataUang = data.mataUang.id;
                                        $scope.hitungTotalPrice();
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.loading = false;
                                });
                            });
                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                        $scope.loading = true;
                        $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaanAndVendor/' + $scope.pengadaanId + '/' + $scope.vendor.id, {
                            ignoreLoadingBar: true
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                            pptc.penawaranPertamaList = data;

                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                        });

                    }).error(function (data, status, headers, config) {
                        $scope.loading = false;
                    });

                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                });
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        $scope.loadSuratPenawaranHarga();

        /*------------------------------------------Header Condition--------------------------------------------*/

        $scope.addHeaderCondition = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/addHeaderCondition.html',
                controller: ModalInstanceAddHeadCtrl,
                size: size
            });
        };
        var ModalInstanceAddHeadCtrl = function ($scope, $modalInstance) {
            $scope.headerCondition = {};
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getByTipe/1')
                .success(function (data, status, headers, config) {
                    $scope.headerConditionList = data;
                }).error(function (data, status, headers, config) {});

            $scope.simpanHeader = function () {
                if (typeof $scope.headerCondition.selected === 'undefined' || $scope.headerCondition.selected == '') {
                    alert("Type condition belum di pilih.");
                } else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Nilai amount belum di isi.");
                } else {
                    var formVendorConditionPrice = {
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $rootScope.itemPengadaanId,
                        conditionalPrice: $scope.headerCondition.selected.id,
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
                        loadVendorConditionalPrice();
                        $modalInstance.close('closed');
                    });
                }

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceAddHeadCtrl.$inject = ['$scope', '$modalInstance'];

        $scope.editHeaderCondition = function (mt, size) {
            $rootScope.editHeaderCondition = mt;
            var modalInstance = $modal.open({
                templateUrl: '/editHeaderCondition.html',
                controller: ModalInstanceEditHeadCtrl,
                size: size
            });
        };
        var ModalInstanceEditHeadCtrl = function ($scope, $modalInstance) {
            var headerKondisi;
            if (typeof $rootScope.editHeaderCondition !== 'undefined') {
                headerKondisi = $rootScope.editHeaderCondition;
            }

            //console.log('$scope.headerCondition =' + $scope.headerCondition)
            $scope.nilai = headerKondisi.nilai;
            $scope.headerCondition = {};
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getByTipe/1')
                .success(function (data, status, headers, config) {
                    $scope.headerConditionList = data;
                }).error(function (data, status, headers, config) {});
            $scope.headerCondition.conditionalPrice = headerKondisi.conditionalPrice;
            $scope.simpanHeader = function () {
                if (typeof $scope.headerCondition.conditionalPrice === 'undefined' || $scope.headerCondition.conditionalPrice == '') {
                    alert("Type Condition Belum Dipilih.");
                } else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Amount Belum Diisi.");
                } else {
                    var formVendorConditionPrice = {
                        id: $rootScope.editHeaderCondition.id,
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $rootScope.itemPengadaanId,
                        conditionalPrice: $scope.headerCondition.conditionalPrice.id,
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
                        loadVendorConditionalPrice();
                        $modalInstance.close('closed');
                    });
                }
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceEditHeadCtrl.$inject = ['$scope', '$modalInstance'];

        //Header Delete
        $scope.removeHeader = function (id, size) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: ModalInstanceHeadCtrl,
                size: size,
                resolve: {
                    headKode: function () {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/deleteRow/' + id).success(function (data, status, headers, config) {
                    loadVendorConditionalPrice();
                }).error(function (data, status, headers, config) {})
            }, function () {});
        };
        var ModalInstanceHeadCtrl = function ($scope, $modalInstance, headKode) {
            $scope.headKode = headKode;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceHeadCtrl.$inject = ['$scope', '$modalInstance', 'headKode'];

        function loadVendorConditionalPrice() {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $rootScope.idVendor + '/' + $scope.pengadaanId + '/' + 1, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pptc.conditionPriceList = data;
                $scope.hitungTotalPrice();
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }

        //---------------------------------------End Header Condition--------------------------------------------------

        /*------------------------------------------Item Condition--------------------------------------------*/
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

            $scope.materialLine = {};
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.itemPengadaanByPengadaanList = data;
                }).error(function (data, status, headers, config) {});

            $scope.simpanItemCondition = function () {
                if (typeof $scope.materialLine.selected === 'undefined' || $scope.materialLine.selected == '') {
                    alert("Type Condition Belum Dipilih.");
                } else if (typeof $scope.itemCondition.selected === 'undefined' || $scope.itemCondition.selected == '') {} else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Amount Belum Diisi.");
                } else {
                    var formVendorItemConditionPrice = {
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $scope.materialLine.selected.id,
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
                        data: formVendorItemConditionPrice
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
                //console.log('itemKondisi = ' + JSON.stringify(itemKondisi));
            }
            $scope.nilai = itemKondisi.nilai;
            $scope.materialLine = {};
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/' + $scope.pengadaanId)
                .success(function (data, status, headers, config) {
                    $scope.itemPengadaanByPengadaanList = data;

                }).error(function (data, status, headers, config) {});
            $scope.materialLine.itemPengadaan = itemKondisi.itemPengadaan;
            //console.log('$scope.materialLine.item = ' + $scope.materialLine.item);
            $scope.itemCondition = {};
            $http.get($rootScope.backendAddress + '/procurement/master/conditionalPriceServices/getByTipe/2')
                .success(function (data, status, headers, config) {
                    $scope.itemConditionList = data;
                }).error(function (data, status, headers, config) {});
            $scope.itemCondition.conditionalPrice = itemKondisi.conditionalPrice;

            $scope.simpanItemCondition = function () {
                if (typeof $scope.materialLine.itemPengadaan === 'undefined' || $scope.materialLine.itemPengadaan == '') {
                    alert("Type Condition Belum Dipilih.");
                } else if (typeof $scope.itemCondition.conditionalPrice === 'undefined' || $scope.itemCondition.conditionalPrice == '') {} else if (typeof $scope.nilai === 'undefined' || $scope.nilai == '') {
                    alert("Amount Belum Diisi.");
                } else {
                    var formVendorItemConditionPrice = {
                        id: $rootScope.editItemCondition.id,
                        pengadaan: $rootScope.pengadaanId,
                        vendor: $rootScope.idVendor,
                        itemPengadaan: $scope.materialLine.itemPengadaan.id,
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
                        data: formVendorItemConditionPrice
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

        //Item Delete
        $scope.removeItemCondition = function (id, size) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable3.html',
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

        function loadVendorItemConditionalPrice(tipeConditional) {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/VendorConditionalPriceServices/getListByVendorAndPengadaanAndTipe/' + $rootScope.idVendor + '/' + $scope.pengadaanId + '/' + 2, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                pptc.itemConditionPriceList = data;
                $scope.hitungTotalPrice();
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        }
        //loadVendorItemConditionalPrice();
        //---------------------------------------End Item Condition--------------------------------------------------


        $scope.hitungTotalPrice = function () {
            $scope.hargaTotalAfterCondition = 0;
            $scope.nilaiPenawaranMaterial = 0;
            $scope.nilaiPenawaranJasa = 0;
            $scope.nilaiPenawaran = 0;
            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (penawaran) {
                penawaran.penawaranDetail.hargaTotalOri = penawaran.kuantitas * penawaran.penawaranDetail.hargaSatuanOri;
                penawaran.penawaranDetail.hargaSatuanAfterCondition = penawaran.penawaranDetail.hargaTotalOri;
                penawaran.penawaranDetail.hargaTotalAfterCondition = penawaran.penawaranDetail.hargaSatuanAfterCondition;
                $scope.nilaiPenawaranMaterial += penawaran.penawaranDetail.hargaTotalOri;
                penawaran.itemCP = 0;
            })

            angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (penawaran) {
                penawaran.penawaranDetail.hargaTotalOri = penawaran.kuantitas * penawaran.penawaranDetail.hargaSatuanOri;
                penawaran.penawaranDetail.hargaSatuanAfterCondition = penawaran.penawaranDetail.hargaTotalOri;
                penawaran.penawaranDetail.hargaTotalAfterCondition = penawaran.penawaranDetail.hargaSatuanAfterCondition;
                $scope.nilaiPenawaranJasa += penawaran.penawaranDetail.hargaTotalOri;
                penawaran.itemCP = 0;
            })
            $scope.nilaiPenawaran = $scope.nilaiPenawaranJasa + $scope.nilaiPenawaranMaterial;
            //console.log('$scope.nilaiPenawaran = ' + $scope.nilaiPenawaran);

            //hitung item CP
            angular.forEach(pptc.itemConditionPriceList, function (cp) {
                angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (penawaran) {
                    if (penawaran.item.id == cp.itemPengadaan.id) {
                        var valCP = hitungCp(penawaran.penawaranDetail.hargaSatuanOri, cp);
                        penawaran.itemCP = parseFloat(penawaran.itemCP) + parseFloat(valCP);
                        penawaran.penawaranDetail.hargaSatuanAfterCondition = penawaran.kuantitas * (penawaran.penawaranDetail.hargaSatuanOri + valCP);
                        penawaran.penawaranDetail.hargaTotalAfterCondition = penawaran.penawaranDetail.hargaSatuanAfterCondition;
                    }
                })

                angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (penawaran) {
                    if (penawaran.item.id == cp.itemPengadaan.id) {
                        var valCP = hitungCp(penawaran.penawaranDetail.hargaSatuanOri, cp);
                        penawaran.itemCP = parseFloat(penawaran.itemCP) + parseFloat(valCP);
                        penawaran.penawaranDetail.hargaSatuanAfterCondition = penawaran.kuantitas * (penawaran.penawaranDetail.hargaSatuanOri + valCP);
                        penawaran.penawaranDetail.hargaTotalAfterCondition = penawaran.penawaranDetail.hargaSatuanAfterCondition;
                    }
                })
            })

            /*Hitung total hargaTotalAfterCondition*/
            $scope.hargaTotalAfterCondition = 0;

            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (penawaran) {
                $scope.hargaTotalAfterCondition += penawaran.penawaranDetail.hargaTotalAfterCondition;
                $scope.totalPenawaranDenganKonfersi = $scope.hargaTotalAfterCondition * $rootScope.dataNilai;
                
            })
            angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (penawaran) {
                $scope.hargaTotalAfterCondition += penawaran.penawaranDetail.hargaTotalAfterCondition;
                $scope.totalPenawaranDenganKonfersi = $scope.hargaTotalAfterCondition * $rootScope.dataNilai;
                
            })

            /*Hitung header condition*/
            pptc.headerCP = 0;
            angular.forEach(pptc.conditionPriceList, function (cp) {
                var valCP = hitungCp($scope.hargaTotalAfterCondition, cp);
                pptc.headerCP = parseFloat(pptc.headerCP) + parseFloat(valCP);
                $scope.hargaTotalAfterCondition += valCP;
            })
        }
        
        
        var valMataUang = false;
        console.log('validasi matauang ' +valMataUang);
        $scope.validasiMataUang = function(){
        	valMataUang = true;
			$http.get($rootScope.backendAddress + '/procurement/pembukaanPenawaran/KursPengadaanServices/getKursPengadaan02ByPengadaanAndMataUang/' + $scope.pengadaanId + '/' + $scope.mataUang)
            .success(function (data) {
            	$rootScope.dataNilai = data.nilai;
                $scope.totalPenawaranDenganKonfersi = $scope.hargaTotalAfterCondition * data.nilai;
            });
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

        var isFormValid = function () {
            var isValid = true;
            var keepGoing = true;
            var idx = 0;
            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (validasiMaterial) {
                if (keepGoing) {
                    if (validasiMaterial.penawaranDetail.hargaSatuanOri == null || (typeof validasiMaterial.penawaranDetail.hargaSatuanOri == 'undefined') || validasiMaterial.penawaranDetail.hargaSatuanOri.length <= 0) {
                        document.getElementsByName('validasiMaterial_hargaSatuanOri')[idx].focus();
                        isValid = false;
                        keepGoing = false;
                    }
                    idx++;
                }
            })

            keepGoing = true;
            idx = 0;
            if (isValid) {
                var idx = 0;
                angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (validasiJasa) {
                    if (keepGoing) {
                        if (validasiJasa.penawaranDetail.hargaSatuanOri == null || (typeof validasiJasa.penawaranDetail.hargaSatuanOri == 'undefined') || validasiJasa.penawaranDetail.hargaSatuanOri.length <= 0) {
                            document.getElementsByName('validasiJasa_hargaSatuanOri')[idx].focus();
                            isValid = false;
                            keepGoing = false;
                        }
                        idx++;
                    }
                })
            }
            return isValid;
        }

        
        $scope.btnSimpanDisable = false;
        $scope.btnSimpanPenawaran = function () {
            if (typeof $scope.suratPenawaranId === 'undefined' || $scope.suratPenawaranId == 0) {
                $scope.konfirmasi();
            } else if (isFormValid()) {
            	
            	if (valMataUang==true){
            		ModalService.showModalConfirmation('Apakah anda yakin menyimpan penawaran ini?').then(function (result) {
                        $scope.btnSimpanDisable = true;
                        $scope.loading = true;
                        simpanPenawaranPertama();
                    });
            		
            	} else{
            		 toaster.pop('warning', 'Kesalahan', 'Mata Uang belum dimasukan!');
            	}
            	
                
            } else {
                toaster.pop('warning', 'Kesalahan', 'Nilai HPS belum dimasukan!');
            }
        }

        $scope.konfirmasi = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/confirm.html',
                controller: ModalKonfirmasiCtrl,
                size: size
            });
            modalInstance.result.then(function () { //ok

            }, function () {});
        };
        var ModalKonfirmasiCtrl = function ($scope, $modalInstance) {
            $scope.ok = function () {
                $location.path('/appvendor/promise/procurement/datapenawaran/vendor');
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalKonfirmasiCtrl.$inject = ['$scope', '$modalInstance'];

        function simpanPenawaranPertama() {
            //if (typeof pptc.penawaranPerama.id == 'undefined') {
            var formPenawaranPertama = {
                id: pptc.penawaranPertamaList.id,
                suratPenawaran: $scope.suratPenawaranId,
                nilaiPenawaran: $scope.hargaTotalAfterCondition,
                nilaiPenawaranOri: $scope.nilaiPenawaran,
                mataUang: $scope.mataUang
            }
            var uri = $rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/';
            if (typeof pptc.penawaranPertamaList.id !== 'undefined' && pptc.penawaranPertamaList.id !== null) {
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
                    simpanPenawaranPeramaDetail(data.id);
                })
                .error(function (data, status, headers, config) {
                    $scope.messageModalError = "penyimpanan gagal";
                    $scope.loading = false;
                });
            //}
        }

        function simpanPenawaranPeramaDetail(penawaranPertamaId) {
            var dataPenawaranDetail = pptc.penawaranPertamaDetail;
            //angular.forEach(dataPenawaranDetail, function (penawaranDetail, index) {
            angular.forEach(pptc.itemPengadaanMaterialByPengadaanIdList, function (itemPengadaanMaterial, index) {
                var formMaterialDetail = {
                        id: itemPengadaanMaterial.penawaranDetail.id,
                        itemPengadaan: itemPengadaanMaterial.id,
                        penawaranPertama: penawaranPertamaId,
                        hargaSatuanAfterCondition: itemPengadaanMaterial.penawaranDetail.hargaSatuanAfterCondition,
                        hargaTotalOri: $scope.nilaiPenawaran,
                        hargaSatuanOri: itemPengadaanMaterial.penawaranDetail.hargaSatuanOri,
                        hargaTotalAfterCondition: $scope.hargaTotalAfterCondition,
                        jumlah: itemPengadaanMaterial.kuantitas,
                        mataUang: $scope.mataUang
                    }
                    //var dataItemMaterialId = penawaran.item.id;

                if (itemPengadaanMaterial.penawaranDetail.id != 'undefined' && itemPengadaanMaterial.penawaranDetail.id > 0) {
                    //if (penawaran.penawaranDetail.itemPengadaan.item.id == dataItemMaterialId) {
                    updateKePenawaranPertamaDetail(formMaterialDetail);
                    //}
                } else {
                    simpanKePenawaranPertamaDetail(formMaterialDetail);
                }
            })
            angular.forEach(pptc.itemPengadaanJasaByPengadaanIdList, function (itemPengadaanJasa, index) {
                    var formJasaDetail = {
                            id: itemPengadaanJasa.penawaranDetail.id,
                            itemPengadaan: itemPengadaanJasa.id,
                            penawaranPertama: penawaranPertamaId,
                            hargaSatuanAfterCondition: itemPengadaanJasa.penawaranDetail.hargaSatuanAfterCondition,
                            hargaTotalOri: $scope.nilaiPenawaran,
                            hargaSatuanOri: itemPengadaanJasa.penawaranDetail.hargaSatuanOri,
                            hargaTotalAfterCondition: $scope.hargaTotalAfterCondition,
                            jumlah: itemPengadaanJasa.kuantitas,
                            mataUang: $scope.mataUang
                        }
                        //var dataItemJasaId = itemPengadaanJasa.item.id;
                    if (itemPengadaanJasa.penawaranDetail.id != 'undefined' && itemPengadaanJasa.penawaranDetail.id > 0) {
                        //if (penawaran.penawaranDetail.itemPengadaan.item.id == dataItemJasaId) {
                        updateKePenawaranPertamaDetail(formJasaDetail);
                        //}
                    } else {
                        simpanKePenawaranPertamaDetail(formJasaDetail);
                    }
                })
                //})
        }
        var simpanKePenawaranPertamaDetail = function (dataSimpan) {
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/insert',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataSimpan
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.btnSimpanPenawaran = false;
                if (typeof data !== 'undefined') {
                    $scope.showSuccess();
                }

                /*$scope.updatePengadaan();*/
            });
        }
        var updateKePenawaranPertamaDetail = function (dataSimpan) {
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/update',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataSimpan
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.btnSimpanPenawaran = false;
                if (typeof data !== 'undefined') {
                    $scope.showSuccess();
                }

                /*$scope.updatePengadaan();*/
            });
        }

        //update pengadaan
        /*$scope.updatePengadaan = function () {
            $scope.loading = true;
            var nextTahapan = {
                    pengadaanId: $scope.pengadaanId,
                    tahapanPengadaanId: 9
                }
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
                data: nextTahapan
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                if (typeof data !== 'undefined') {
                        $scope.showSuccess();
                    }
            });
        };
        */

        $scope.showSuccess = function () {
            $scope.loading = false;
            toaster.pop('success', 'Sukses', 'Data penawaran Harga berhasil disimpan.');
        }

        /*--------------------------------------------------------------------------------------*/

        $scope.back = function () {
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor');
        };

        pptc.addHeadKondisi = function () {
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor/add/head/condition');
        };

        pptc.addItemKondisi = function () {
            $location.path('/appvendor/promise/procurement/penawaranharga/vendor/add/item/condition');
        };



    }

    PenawaranVendorHargaTotalController.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

    function formatMoney() {
        return function (number, decimals, dec_point, thousands_sep) {
            var n = !isFinite(+number) ? 0 : +number,
                prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
                sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
                dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
                toFixedFix = function (n, prec) {
                    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
                    var k = Math.pow(10, prec);
                    return Math.round(n * k) / k;
                },
                s = (prec ? toFixedFix(n, prec) : Math.round(n)).toString().split('.');
            if (s[0].length > 3) {
                s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
            }
            if ((s[1] || '').length < prec) {
                s[1] = s[1] || '';
                s[1] += new Array(prec - s[1].length + 1).join('0');
            }
            return s.join(dec);
        }
    };

    function propsFilter() {
        return function (items, props) {
            var out = [];

            if (angular.isArray(items)) {
                items.forEach(function (item) {
                    var itemMatches = false;

                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }

            return out;
        };
    }

})();