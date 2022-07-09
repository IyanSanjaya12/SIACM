(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalViewPOController', ApprovalViewPOController);

    function ApprovalViewPOController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, ModalService, toaster) {
        var form = this;
        form.approvalProcess = $rootScope.approvalProcess;

        /*		if (typeof form.approvalProcess.keterangan !== 'undefined'){
        			form.note = form.approvalProcess.keterangan;
        		}*/

        console.log("cek approvalProcess : " + JSON.stringify(form.approvalProcess));

        form.loading = true;
        form.isUserApproval = false;
        form.levelList = [];
        form.purchaseOrderItemList = [];

        RequestService.doGET('/procurement/approvalProcessServices/findPurchaseOrder/' + form.approvalProcess.approvalProcessType.valueId)
            .then(function successCallback(data) {
                form.purchaseOrder = data;
                if (typeof form.approvalProcess.approvalLevel !== 'undefined' && form.approvalProcess.approvalLevel != null) {
                    RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessType/' + form.approvalProcess.approvalProcessType.id)
                        .then(function successCallback(data) {
                            form.levelList = data;

                            RequestService.doGET('/procurement/approvalProcessServices/findPurchaseOrderItemByPO/' + form.approvalProcess.approvalProcessType.valueId)
                                .then(function successCallback(dataItem) {
                                    form.purchaseOrderItemList = dataItem;
                                    form.loading = false;
                                }, function errorCallback(response) {
                                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                                    form.loading = false;
                                });

                        }, function errorCallback(response) {
                            ModalService.showModalInformation('Terjadi kesalahan pada system!');
                            form.loading = false;
                        });

                    RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + form.approvalProcess.approvalProcessType.id)
                        .then(function successCallback(data) {
                            $scope.statusList = data;
                        }, function errorCallback(response) {
                            ModalService.showModalInformation('Terjadi kesalahan pada system!');
                            form.loading = false;
                        });

                } else {
                    RequestService.doGET('/procurement/approvalProcessServices/findPurchaseOrderItemByPO/' + form.approvalProcess.approvalProcessType.valueId)
                        .then(function successCallback(dataItem) {
                            form.purchaseOrderItemList = dataItem;

                            RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessType/' + form.approvalProcess.approvalProcessType.id)
                                .then(function successCallback(data) {
                                    form.levelList = data;
                                });

                            RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + form.approvalProcess.approvalProcessType.id)
                                .then(function successCallback(data) {
                                    $scope.statusList = data;
                                }, function errorCallback(response) {
                                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                                    form.loading = false;
                                });

                        }, function errorCallback(response) {
                            ModalService.showModalInformation('Terjadi kesalahan pada system!');
                            form.loading = false;
                        });


                    form.isUserApproval = true;
                    form.loading = false;
                }
            }, function errorCallback(response) {
                ModalService.showModalInformation('Terjadi kesalahan pada system!');
                form.loading = false;
            });

        form.btnSimpan = function (statusId) {
            $scope.errorNoteMessage = "";
            var isNote = true;
            if (form.note == undefined || form.note == '' ) {
                isNote = false;
                $scope.errorNoteMessage = "Note tidak boleh kosong";
            } else {
                ModalService.showModalConfirmation().then(function (result) {
                    ModalService.showModalInformationBlock();

                    var post = {
                        id: form.approvalProcess.approvalProcessType.valueId,
                        status: statusId,
                        note: form.note,
                        approvalProcessId: form.approvalProcess.id
                    };

                    RequestService.doPOST('/procurement/purchaseorder/PurchaseOrderServices/updateStatusApproval', post)
                        .then(function successCallback(data) {
                            $location.path('app/promise/procurement/approval');
                            ModalService.closeModalInformation();
                        }, function errorCallback(response) {
                            ModalService.closeModalInformation();
                            ModalService.showModalInformation('Terjadi kesalahan pada system!')
                        });
                });

            }
        }


    }
    ApprovalViewPOController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'ModalService', 'toaster'];

})();