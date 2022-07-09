(function () {
    'use strict';
    angular.module('naut').controller('PurchaseRequestVerificationController', PurchaseRequestVerificationController);

    function PurchaseRequestVerificationController($scope, $http, $rootScope, $resource, $location, $state, $modal, RequestService) {
        var pr = this;

        $rootScope.ListPrFromItem = {};
        $scope.searchPRNumber = '';
        $scope.status = 8;

        // paging
        $scope.allData = 0;
        $scope.rowPerPage = 5;
        $scope.currentPage = 1;

        pr.go = function (path) {
            $location.path(path);
        };

        $scope.getList = function (dataPrRequest) {
            $scope.loading = true;
            var dataPR = {};
            if (typeof dataPrRequest === 'undefined') {
                dataPR = {
                    pageNumber: 1,
                    numberOfRowPerPage: $scope.rowPerPage,
                    searchingKeyWord: ''
                };
            } else {
                dataPR = dataPrRequest;
            }

            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/purchaseRequestServices/getPurchaseRequestVerificationListByPRNumberWithPagination',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: dataPR
            }).success(function (data, status, headers, config) {
                $scope.purchaseRequestList = data;
                var i = 0;
                angular.forEach($scope.purchaseRequestList.listPurchaseRequestDTOs, function (value, key) {
                    var nextApprovalAndStatus = '';
                    var btnVerifyShow = false; //condition for show verifybutton 

                    if (value.status == 2) {
                        nextApprovalAndStatus = "<strong class='text-danger'> (Reject)</strong> " + value.nextapproval;
                        btnVerifyShow = true;
                    } else if (value.status == 3) {
                        nextApprovalAndStatus = "<strong class='text-muted'> (Approval Process)</strong> " + value.nextapproval;
                        btnVerifyShow = true;
                    } else if (value.status == 4) {
                        nextApprovalAndStatus = "<strong class='text-warning'> (Hold)</strong> " + value.nextapproval;
                        btnVerifyShow = true;
                    } else if (value.status == 5) {
                        nextApprovalAndStatus = "<strong class='text-primary'> (On Process)</strong> " + value.nextapproval;
                        btnVerifyShow = false;
                    } else if (value.status == 6) {
                        nextApprovalAndStatus = "<strong class='text-primary'> (Received)</strong> " + value.nextapproval;
                        btnVerifyShow = false;
                    } else if (value.status == 8) {
                        nextApprovalAndStatus = "<strong class='text-primary'> (Need Verification)</strong> " + value.nextapproval;
                        btnVerifyShow = true;
                    } else if (value.status == 7 && value.pengadaanId > 0) {
                        nextApprovalAndStatus = "<strong class='text-primary'> (Procurement Process)</strong> " + value.nextapproval;
                        btnVerifyShow = false;
                    } else if (value.status == 7) {
                        nextApprovalAndStatus = "<strong class='text-primary'> (Procurement Process)</strong> " + value.nextapproval;
                        btnVerifyShow = true;
                    }

                    $scope.purchaseRequestList.listPurchaseRequestDTOs[i].nextapproval = nextApprovalAndStatus;
                    $scope.purchaseRequestList.listPurchaseRequestDTOs[i].btnverifyshow = btnVerifyShow;
                    i++;
                });

                $scope.totalItems = $scope.purchaseRequestList.totalItems;
                $scope.numAllStatus = $scope.purchaseRequestList.numAllStatus;
                $scope.numNeedVerification = $scope.purchaseRequestList.numNeedVerification;
                $scope.numProcurementProcess = $scope.purchaseRequestList.numProcurementProcess;
                $scope.numOnProcess = $scope.purchaseRequestList.numOnProcess;
                $scope.numReceived = $scope.purchaseRequestList.numReceived;

                $scope.loading = false;
            });

        };
        $scope.range = function (n) {
            return new Array(n);
        };

        // Sort
        $scope.sortList = [{
                sort: 'prnumber',
                name: 'Pr Number'
		}, {
                sort: 'department',
                name: 'Departement'
		},

            {
                sort: 'postdate',
                name: 'Post Date'
		},

            {
                sort: 'daterequired',
                name: 'Required Date'
		}, {
                sort: 'status',
                name: 'Status'
		}];
        $scope.getSortPR = function () {
            $scope.getPage(1);
        };
        // Search
        $scope.getSearchPr = function () {
            if ($scope.searchPRNumber.length >= 3) {
                $scope.getPage(1);
            } else {
                if ($scope.searchPRNumber.length == 0) {
                    $scope.getPage(1);
                }
            }
        }
        $scope.getPage = function (page) {
            var dataPR = {
                pageNumber: page,
                numberOfRowPerPage: $scope.rowPerPage,
                searchingKeyWord: '{filter:[{key:prnumber, value:"' + $scope.searchPRNumber + '"}' + ']' + ((typeof $scope.sortBy !== 'undefined' ? ',sort:' + $scope.sortBy : '')) + (($scope.status != null ? ',status:' + $scope.status : '')) + '}'
            };
            $scope.getList(dataPR);
            $scope.currentPage = page;
        }
        $scope.getPage(1);

        $scope.pageChanged = function (rowPerPage) {
            $scope.rowPerPage = rowPerPage;
            $scope.getPage(1);
        };

        $scope.showDetail = function (u) {
            if ($scope.active != u.id) {
                $scope.active = u.id;
            } else {
                $scope.active = null;
            }
        };

        $scope.getDetil = function (pr) {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/' + pr.id).success(function (data, status, headers, config) {
                pr.orderPurchaseRequestItemList = data;
                var namavendor = "-";
                var btnDetil = document.getElementById('btnDetil_' + pr.id);
                var rowObject = document.getElementById('info_' + pr.id);
                if ((rowObject.style.display == 'none')) {
                    var cell = document.getElementById('cell_info_' + pr.id);
                    var content = '<div class="row">';
                    content += '          <div class="col-lg-3" style="bg-color:grey-light;text-align:center"><strong>Item</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Vendor</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Qty</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Price</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Unit</strong></div>';
                    content += '          <div class="col-lg-2" style="bg-color:grey-light;text-align:center"><strong>Total</strong></div>';
                    content += '          <div class="col-lg-1" style="bg-color:grey-light;text-align:center"><strong>Status</strong></div>';
                    content += '     </div>';
                    angular.forEach(pr.orderPurchaseRequestItemList, function (value, key) {
                        if (typeof value.vendor === 'undefined' || value.vendor !== null) {
                            namavendor = value.vendor.nama;

                        }
                        content += '<div class="row">';
                        content += '          <div class="col-lg-3" style="text-align:left">' + value.itemname + '</div>';
                        content += '          <div class="col-lg-2" style="text-align:left">' + namavendor + '</div>';
                        content += '          <div class="col-lg-1" style="text-align:right">' + value.quantity + '</div>';
                        content += '          <div class="col-lg-2" style="text-align:right">' + value.price.toLocaleString() + '</div>';
                        content += '          <div class="col-lg-1" style="text-align:right">' + value.unit + '</div>';
                        content += '          <div class="col-lg-2" style="text-align:right">' + (value.price * value.quantity).toLocaleString() + '</div>';
                        content += '          <div class="col-lg-1" style="text-align:center">' + value.status + '</div>';
                        content += '     </div>';
                    });
                    cell.innerHTML = content;
                    rowObject.style.display = 'table-row';
                    $scope.loading = false;
                    btnDetil.innerText = 'Hide Detil';
                    return true;
                } else {
                    rowObject.style.display = 'none';
                    $scope.loading = false;
                    btnDetil.innerText = 'Show Detil';
                    return false;
                }
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            });
        };

        pr.btnVerify = function (purchaserequest) {
            $state.go('app.promise.procurement-purchaserequestverification-verify', {
                purchaserequest: purchaserequest
            });
        }

        // Create Procurement
        $rootScope.inisialisasiForm = {};
        $rootScope.HPSForm = {};
        pr.btnCreateProcurement = function (pr) {
            //cek terlebih dulu user punya angota kepanitia / pelaksana pengadaan
            $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getValidationNewPengaadaan')
                .success(function (resp) {
                    if (resp) {
                        $rootScope.prSelected = pr;
                        var modalInstance = $modal.open({
                            templateUrl: '/addInisialisasi01.html',
                            controller: ModalInstanceCtrl,
                            size: 'lg'
                        });
                        var message = "INFO :<br/>";
                        modalInstance.result.then(function () {
                            // console.log('OOK input berhasil');
                        }, function () {
                            // console.log('INFO Cancel klik');
                        });
                    } else {
                        RequestService.modalInformation('Anda belum memiliki kepanitiaan!', 'danger');
                    }

                    $scope.loading = false;
                })
                .error(function (err) {
                    console.error("Error : " + err);
                    RequestService.modalInformation('Anda belum memiliki kepanitiaan!', 'danger');
                    $scope.loading = false;
                })

        }

        var ModalInstanceCtrl = function ($scope, $rootScope, $modalInstance) {
            $scope.ok = function () {
                $rootScope.inisialisasiForm.jenisPengadaan = $scope.jenisPengadaan;
                $rootScope.inisialisasiForm.metodaPengadaan = $scope.metodaPengadaan;
                $rootScope.inisialisasiForm.kualifikasiPenyediaBarangJasa = $scope.kualifikasiPenyediaBarangJasa;
                $rootScope.inisialisasiForm.jenisPenawaran = {
                    id: 1, // Total
                    nama: 'Total'
                };
                // add from verification
                $rootScope.HPSForm = {
                    pilihPerencanaan: {
                        id: 2,
                        nama: "Dengan Perencanaan"
                    }
                };

                var statusAdd = true;
                var message = "";
                if ($rootScope.inisialisasiForm.metodaPengadaan == null) {
                    message += "<strong>Metoda Pengadaan</strong> harus dipilih<br/>";
                    statusAdd = false;
                }

                if (statusAdd) {
                    $scope.loading = true;
                    $scope.detilPerencanaan = $rootScope.prSelected;
                    // console.log(">>> mamat test :
                    // "+JSON.stringify($scope.detilPerencanaan));
                    $scope.loading = true;
                    // Material
                    $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/' + $scope.detilPerencanaan.id).success(function (data, status, headers, config) {
                        $scope.showMaterial = false;
                        $scope.showJasa = false;

                        if (data.length > 0)
                            $scope.showMaterial = true;

                        $scope.itemPerencanaanList = data;
                        $scope.jumlahTotalHPSMaterial = 0;
                        // remap
                        for (var i = 0; i < $scope.itemPerencanaanList.length; i++) {
                            $scope.itemPerencanaanList[i].index = i + 1;
                            $scope.itemPerencanaanList[i].totalHPS = 0;
                            $scope.itemPerencanaanList[i].totalHPS = $scope.itemPerencanaanList[i].price * $scope.itemPerencanaanList[i].quantity;
                            $scope.jumlahTotalHPSMaterial += $scope.itemPerencanaanList[i].totalHPS;
                        }
                        // reformat for import
                        $rootScope.importPerencanaan = $scope.detilPerencanaan;
                        $rootScope.perencanaanId = $scope.detilPerencanaan.id;

                        var materialList = [];
                        angular.forEach($scope.itemPerencanaanList, function (value, key) {
                            var materialObject = {
                                material: value.item,
                                kuantitas: value.quantity,
                                hps: value.price,
                                mataUang: value.mataUang,
                                keteranga: value.specification
                            };
                            materialList.push(materialObject);
                        });
                        $rootScope.inisialisasiForm.materialList = materialList;

                        $modalInstance.close('closed');
                        $location.path('/app/promise/procurement/inisialisasi/add04');
                    }).error(function (data, status, headers, config) {
                        console.error(status);
                        $scope.loading = false;
                        $modalInstance.close('closed');
                    });

                } else {
                    $scope.message = message;
                }

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceCtrl.$inject = ['$scope', '$rootScope', '$modalInstance'];
    }

    PurchaseRequestVerificationController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state', '$modal', 'RequestService'];

})();