(function () {
    'use strict';

    angular.module('naut').controller('ApprovalViewPRController', ApprovalViewPRController);

    function ApprovalViewPRController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, ModalService, toaster) {
        var form = this;
        form.approvalProcess = $rootScope.approvalProcess;
        form.note = '';
        $scope.downloadFile = $rootScope.viewUploadBackendAddress + '/';

        /*		if (typeof form.approvalProcess.keterangan !== 'undefined'){
        			form.note = form.approvalProcess.keterangan;
        		}*/

        form.totalPRItem = 0;
        $scope.loading = true;
        $scope.isUserApproval = false;


        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findPurchaseRequest/' + form.approvalProcess.approvalProcessType.valueId).success(function (data) {
            form.purchaseRequest = data;
            if (typeof form.approvalProcess.approvalLevel !== 'undefined' && form.approvalProcess.approvalLevel != null) {
                $scope.isUserApproval = false;
            } else {
                $scope.isUserApproval = true;
            }
            
            $scope.getApprovalProcess(form.approvalProcess.approvalProcessType.id);
            $scope.getApprovalProcessAndStatus(form.approvalProcess.approvalProcessType.id);
            $scope.getUploadPurchaseRequest(form.approvalProcess.approvalProcessType.valueId);

            $scope.getPRItem(form.approvalProcess.approvalProcessType.valueId);
        }).error(function (data) {
            $scope.loading = false;
        });

        $scope.getUploadPurchaseRequest = function (purchaseRequest) {
            $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + purchaseRequest).success(function (data) {
                form.uploadPurchaseRequest = data;

                form.dokumenPr = [];
                angular.forEach(data, function (value) {
                    var file = {};
                    file.fileName = value.uploadPrFileName;
                    file.downloadFile = $rootScope.backendAddress + '/file/view/' + value.uploadPrRealName;
                    form.dokumenPr.push(file);
                })


            }).error(function (data) {
                $scope.loading = false;
            });

        }

        $scope.btnSimpan = function (statusId) {
            $scope.errorNoteMessage = "";

            var checkNote = form.note;
            var isNote = true;
            if (form.note === null || form.note === '') {
                isNote = false;
                $scope.errorNoteMessage = "Note tidak boleh kosong";
            } else if (checkNote.length >= 255) {
                isNote = false;
                $scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
            } else {

                if (statusId == 3) { // when is approve, chek is COA available

                    var isCostAvailable = true;
                    angular.forEach(form.purchaseRequestItemList, function (value, index) {
                        if (!value.costAvailable) {
                            isCostAvailable = false;
                        }
                    });

                    if (!isCostAvailable) {
                        ModalService.showModalConfirmation('Cost Allocation tidak mencukupi, Apakah Anda Yakin ingin menyetujuinya ?').then(function (result) {
                            ModalService.showModalInformationBlock();
                            $scope.simpan(statusId);
                        });
                    } else {
                        ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
                            ModalService.showModalInformationBlock();
                            $scope.simpan(statusId);
                        });
                    }

                } else {

                    ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
                        ModalService.showModalInformationBlock();
                        $scope.simpan(statusId);
                    });
                }

            }
        }

        $scope.simpan = function (statusId) {
            var post = {
                id: form.approvalProcess.approvalProcessType.valueId,
                status: statusId,
                note: form.note,
                approvalProcessId: form.approvalProcess.id
            };

            RequestService.doPOST('/procurement/purchaseRequestServices/updateStatusApproval', post)
                .then(function successCallback(data) {
                    $location.path('app/promise/procurement/approval');
                    ModalService.closeModalInformation();
                }, function errorCallback(response) {
                    ModalService.closeModalInformation();
                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                });
        }

        $scope.getApprovalProcess = function (approvalProcessType) {
            $scope.loadingApproval = true;
            form.levelList = [];
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function (data) {
                form.levelList = data;
                $scope.loading = false;
            }).error(function (data) {
                $scope.loading = false;
            });
        };

        //Approval log
        $scope.getApprovalProcessAndStatus = function (approvalProcessType) {
            $scope.loadingApproval = true;
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + approvalProcessType)
                .success(function (data) {
                    $scope.statusList = data;
                    $scope.loading = false;
                }).error(function (data) {
                    $scope.loading = false;
                });
        };

        form.purchaseRequestItemList = [];
        $scope.getPRItem = function (prId) {
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findPurchaseRequestItemByPR/' + prId).success(function (data) {
                $scope.prItemList = data;
                var prItemList = data;
                //console.log(">> cek requestitemlist:"+JSON.stringify(data));

                angular.forEach(data, function (value, index) {
                    var obj = value.purchaseRequestItem;
                    obj.costAvailable = value.costAvailable;
                    form.purchaseRequestItemList.push(obj);
                });
                $scope.loading = false;
            }).error(function (data) {
                $scope.loading = false;
            });

            $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + prId).success(function (data) {
                $scope.dataUploadPRList = data;
                //				console.log(">> id:"+JSON.stringify(data));

            });
        }

        $scope.btnInfoCoa = function (data) {
            $rootScope.dataItem = data;
            var modalInstance = $modal.open({
                templateUrl: '/info_coa.html',
                size: 'md',
                controller: modalInstanceInfoCoaController

            });
        }
        var modalInstanceInfoCoaController = function ($scope, $http, $modalInstance, $resource, $rootScope) {
            // console.log(">>> "+JSON.stringify($rootScope.dataItem));
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findCostAllocation/' + $rootScope.dataItem.costcenter).success(function (data) {
                if (data.length > 0) {
                    $scope.coa = data[0];
                    console.log(">>> " + JSON.stringify($scope.coa));
                }
                $scope.loading = false;
            });

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        modalInstanceInfoCoaController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        //dari mas mamat untuk menampilkan spek
        $scope.viewSpek = function (prItemId) {
            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getByPurchaseRequestItemId/' + prItemId)
                .success(function (data, status, headers, config) {

                    $scope.dataitem = data;
                    var modalInstance = $modal.open({
                        templateUrl: '/viewSpec.html',
                        controller: ModalViewSpecController,
                        scope: $scope
                    });
                    var message = "INFO :<br/>";
                    modalInstance.result.then(function () {

                    }, function () {

                    });

                })
        }

        var ModalViewSpecController = function ($scope, $rootScope, $modalInstance) {
            $scope.ok = function () {
                $modalInstance.dismiss('cancel');
            };

        };
        ModalViewSpecController.$inject = ['$scope', '$rootScope', '$modalInstance'];

        // console.info("USER :"+JSON.stringify($rootScope.userLogin));
        // console.info("ROLE : "+JSON.stringify($rootScope.userRole ));
        // console.info($rootScope.userOrganisasi);
    }
    ApprovalViewPRController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'ModalService', 'toaster'];

})();