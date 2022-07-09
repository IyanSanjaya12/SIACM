(function () {
    'use strict';

    angular
        .module('naut')
        .controller('UsulanCalonPemenangSatuanMeritPointController', UsulanCalonPemenangSatuanMeritPointController);

    function UsulanCalonPemenangSatuanMeritPointController($http, $scope, $rootScope, $resource, $location, $modal, toaster, $q, $timeout, ngTableParams) {
        var form = this;

        if (typeof $rootScope.pengadaanUsulanCalonPemenang !== 'undefined') {
            form.detailPengadaan = $rootScope.pengadaanUsulanCalonPemenang;
        } else {
            toaster.pop('error', 'Data Pengadaan Tidak Ada', '');
        }
        form.isDisabled = false;
        $scope.pengadaanId = form.detailPengadaan.id;

        //bidang-sub bidang
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.subBidangUsahaByPengadaanIdList = data;
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        })

        //material
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.itemPengadaanMaterial = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            });
            $scope.loading = false;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        })

        //jasa
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.itemPengadaanJasa = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            })
        }).error(function (data, status, headers, config) {})
        
        // cek, sudah pernah di insert apa belum
        $http.get($rootScope.backendAddress + '/procurement/usulancalonpemenangsatuan/getByPengadaan/' + $scope.pengadaanId, {
        	ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
        	if (data.length > 0) {
        		form.isDisabled = false;
        	} else {
        		form.isDisabled = true;
        	}
        }).error(function (data, status, headers, config) {})

        form.goDetail = function (tipeTabel, list, large) {
            // vendor list
        	if (form.detailPengadaan.metodePenawaranHarga.id == 1) {
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + list.id).success(function (negosiasi, status, headers, config) {
                    angular.forEach(negosiasi, function (value, indeks) {
                        negosiasi[indeks].pengadaanId = $scope.pengadaanId;
                        //nilai Evaluasi
                        $http.get($rootScope.backendAddress + '/procurement/negosiasi/NegosiasiServices/getLastFromVendorByPengadaanAndVendor/' + $scope.pengadaanId + '/' + value.evaluasiHargaVendor.vendor.id).success(function (nilai, status, headers, config) {
                            if (nilai.length > 0) {
                                negosiasi[indeks].harga = nilai[0].nilaiPenawaranAfterCondition;
                            } else {
                                negosiasi[indeks].harga = 0;
                            }
                        })
                        $http.get($rootScope.backendAddress + '/procurement/usulancalonpemenangsatuan/getByItemByVendorByPengadaan/' + list.id + '/' + value.evaluasiHargaVendor.vendor.id + '/' + $scope.pengadaanId).success(function (usulan, status, headers, config) {
                            if (usulan.length > 0) {
                            	negosiasi[indeks].idUsulanCalonPemenang = usulan[0].id;
                                negosiasi[indeks].finalCheck = usulan[0].finalCheck;
                            } else {
                            	negosiasi[indeks].finalCheck = false;
                            }
                        })
                    })
                    var modalInstance = $modal.open({
                        templateUrl: '/detail.html',
                        controller: modalInstanceController,
                        size: large,
                        resolve: {
                            data1: function () {
                                return list;
                            },
                            data2: function () {
                                return negosiasi;
                            }
                        }
                    });
                })
            } else {
                $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorDetailServices/getByPengadaanItem/' + $scope.pengadaanId + '/' + list.id).success(function (auction, status, headers, config) {
                    angular.forEach(auction, function (value, indeks) {
                        auction[indeks].pengadaanId = $scope.pengadaanId;
                        $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionDetailServices/getByVendorAndItemPengadaan/' + value.evaluasiHargaVendor.vendor.id + '/' + list.id).success(function (nilai, status, headers, config) {
                            auction[indeks].harga = nilai[0].hargaTotalAfterCondition;
                        })
                        $http.get($rootScope.backendAddress + '/procurement/usulancalonpemenangsatuan/getByItemByVendorByPengadaan/' + list.id + '/' + value.evaluasiHargaVendor.vendor.id + '/' + $scope.pengadaanId).success(function (usulan, status, headers, config) {
                            if (usulan.length > 0) {
                            	auction[indeks].idUsulanCalonPemenang = usulan[0].id;
                                auction[indeks].finalCheck = usulan[0].finalCheck;
                            } else {
                            	auction[indeks].finalCheck = false;
                            }
                        })
                    })
                    var modalInstance = $modal.open({
                        templateUrl: '/detail.html',
                        controller: modalInstanceController,
                        size: large,
                        resolve: {
                            data1: function () {
                                return list;
                            },
                            data2: function () {
                                return auction;
                            }
                        }
                    });
                })
            };
        }

        var modalInstanceController = function ($scope, $http, $modalInstance, $resource, $rootScope, data1, data2) {
            $scope.data = data1;
            $scope.data2 = data2;
             
            var proses = function (indeks) {
            	$scope.loading = true;
            	var postFormUsulanCalonPemenang= {};
            	postFormUsulanCalonPemenang = {
                        id: $scope.data2[indeks].idUsulanCalonPemenang,
                        vendorId: $scope.data2[indeks].evaluasiHargaVendor.vendor.id,
                        itemId: $scope.data2[indeks].itemPengadaan.id,
                        pengadaanId: form.detailPengadaan.id,
                        finalCheck: $scope.data2[indeks].finalCheck
                    };
                var uri = '';
                if (typeof $scope.data2[indeks].idUsulanCalonPemenang !== 'undefined') {
                    uri = $rootScope.backendAddress + '/procurement/usulancalonpemenangsatuan/update';
                } else {
                    uri = $rootScope.backendAddress + '/procurement/usulancalonpemenangsatuan/create';
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
                    data: postFormUsulanCalonPemenang
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                    if (($scope.data2.length - 1) != indeks) {
                        proses(indeks + 1);
                    } else {
                    	$scope.showSuccess();
                    }
                }).error(function (data, status, headers, config) {
                    alert('error');
                }); 	
            }
            $scope.showSuccess = function () {
                $scope.loadingSaving = false;
                toaster.pop('success', 'Sukses', 'Data berhasil disimpan.');
                $scope.cancel();
            }
            
            $scope.ok = function () {
            	proses(0);
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }
        }

        modalInstanceController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'data1', 'data2'];

        form.nextTahapanPengadaan = function () {
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.nextTahapan = data;
            }).error(function (data, status, headers, config) {})
        }
        form.nextTahapanPengadaan();

        form.back = function () {
             if (typeof $rootScope.view !== 'undefined') {
                if ($rootScope.view) {
                    $rootScope.backToView();
                } else {
                    $location.path('/app/promise/procurement/usulancalonpemenang');
                }
            } else {
                $location.path('/app/promise/procurement/usulancalonpemenang');
            }
        }
        
        $scope.printDiv = function (divName) {
            $scope.isLoading=false;
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=800,height=600');
            popupWin.document.open()
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /><link rel="stylesheet" href="app/css/bootstrap.css"></head><body onload="window.print()">' + printContents + '</html>');
            popupWin.document.close();
        }

        form.btnSimpan = function () {
            $scope.loading = true;

            //tahapan pengadaan
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
                    tahapanPengadaanId: form.nextTahapan
                }
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    toaster.pop('success', 'Penetapan Pemenang', 'Simpan ' + data.namaPengadaan + ', berhasil.');
                }
                $scope.loading = false;
            });
        }

    }
    UsulanCalonPemenangSatuanMeritPointController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$modal', 'toaster', '$q', '$timeout', 'ngTableParams'];
})();
