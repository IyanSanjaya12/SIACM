(function () {
    'use strict';
    angular.module('naut').controller('PurchaseRequestJoinController', PurchaseRequestJoinController);

    function PurchaseRequestJoinController($scope, $http, $rootScope, $resource, DTOptionsBuilder, DTColumnBuilder, DTInstances, $modal, $filter, RequestService, $stateParams, $compile, ModalService, $state, $timeout, $q) {

        var form = this;
        var dtInstance;
        var items = '';

        // Variable Scope
        $scope.loading = false;

        form.itemList = $stateParams.itemList;
        form.pr = $stateParams.pr;
        form.purchaseRequestItemList = [];
        form.selected = {};

        for (var i = 0; i < form.itemList.length; i++) {
            if (i != (form.itemList.length - 1)) {
                items = items.concat(form.itemList[i], ', ');
            } else {
                items = items.concat(form.itemList[i]);
            }
        }

        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        form.format = form.formats[4];
        form.totalHPSItemPR = 0;

        /*		// generate PR Number
        		$scope.generateNumber = function() {
        			var date = new Date();
        			form.prnumber = "PR-Join/" + date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2) + "/AI/" + (1000 + Math.ceil(Math.random() * 9000));
        		};
        		*/
        $scope.btnDisable = false;
        // generate PR Join Number
        $scope.btnGenerateNumber = function () {

            $http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/getjoinprnumber')
                .success(function (data, status, headers, config) {
                    form.prnumber = data.prjoin;
                    $scope.btnDisable = true;
                })
        };

        $scope.testData = function () {
            console.log("Test data form.itemList : " + JSON.stringify(form.itemList));
            console.log("Test data PR : " + JSON.stringify(form.pr));
        };

        // Edit data from Index
        if (form.pr != undefined) {
            form.prnumber = form.pr.prnumber;
            form.daterequired = form.pr.daterequired;
            form.description = form.pr.description;
        }

        // date Configuration
        form.daterequiredOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.daterequiredOpened = true;
        };

        // Data Table former like PR Consolidation
        var getDataList = function () {
            form.tempObj = [];
            form.detailRows = [];
            form.params = {
                itemId: items,
                prId: null
            };
            if (form.pr != undefined) {
                form.params.prId = form.pr.id;
            }

            var obj = DTOptionsBuilder.newOptions();

            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/purchaseRequestItemServices/getPurchaseRequestItemPaggingListForPrConsolidation', form.params);

            /** untuk custom field **/
            form.dtOptions.withOption('order', [[0, 'desc']])
                .withOption('createdRow', createdRow);

            /** untuk menampilkan colom data **/
            form.dtColumns = [
            	DTColumnBuilder.newColumn('id').withTitle('No.').notSortable(),
  				DTColumnBuilder.newColumn(null).withTitle('Items').renderWith(
                    function (data, type, full, meta) {
                        return '<strong>' + data.kode + '</strong><br/>' + data.itemname + '<br/>' +
                            '<button id="hide_' + meta.row + '" ng-click="form.hideDetail(' + meta.row + ')" style="display:none;" class="btn btn-info btn-sm btn-addon"><em class="fa fa-arrow-up"></em> Hide Detail &nbsp;</button >' +
                            '<button id="show_' + meta.row + '" ng-click="form.viewDetail(' + meta.row + ',' + data.item.id + ')" class="btn btn-info btn-sm btn-addon"><em class="fa fa-arrow-down"></em> View Detail &nbsp;</button >' +
                            '<br/>';
                    }
  				),
  				DTColumnBuilder.newColumn(null).withTitle('Qty').renderWith(function (data) {
                    return $filter('currency')(data.quantity, '', 2);
                }),
  				DTColumnBuilder.newColumn('unit').withTitle('Unit'),
  				DTColumnBuilder.newColumn(null).withTitle('Price').renderWith(function (data) {
                    return $filter('currency')(data.price, '', 2);
                }),
  				DTColumnBuilder.newColumn(null).withTitle('Total').renderWith(function (data) {
                    form.totalHPSItemPR = parseFloat(data.grandTotal);
                    return $filter('currency')(data.total, '', 2);
                })
              ];
        }
        getDataList();

        DTInstances.getLast().then(function (instance) {
            dtInstance = instance;
        });

        form.hideDetail = function (id) {
            document.getElementById('hide_' + id).style.display = "none";
            document.getElementById('show_' + id).style.display = "inline";
            $("#detilview" + id).remove('');
        }

        // Detail PR
        form.viewDetail = function (id, data) {
            var content = '';
            var url = '/procurement/purchaseRequestItemServices/getPurchaseRequestItemVerifiedListByItem/' + data;
            var idx = $.inArray(id, form.detailRows);

            if (idx == 0) {
                form.hideDetail(id);
                form.detailRows.splice(idx, 1);
            } else {
                if (form.pr != undefined) {
                    url = '/procurement/purchaseRequestItemServices/getPurchaseRequestItemVerifiedListByItemAndPrJoinId/' + data + '/' + form.pr.id;
                }
                RequestService.doGET(url)
                    .then(function successCallback(datav) {
                        if (datav != null) {
                            content += '<table width="100%" datatable="ng" class="row-border hover table-striped">';
                            content += '	<tr><td><strong>No</strong></td>';
                            content += '	<td><strong>PR Number</strong></td>';
                            content += '    <td><strong>Department</strong></td>';
                            content += '    <td><strong>Qty</strong></td>';
                            content += '    <td><strong>Price</strong></td>';
                            content += '    <td><strong>Unit</strong></td>';
                            content += '    <td><strong>Total</strong></td>';
                            content += '    <td><strong>Status</strong></td></tr>';
                            content += '</td>';

                            form.purchaseRequestItemList = datav;
                            form.purchaseRequestItemList.forEach(function (value, index, array) {
                                content += '<tr>';
                                content += '    <td>' + (index + 1) + '</td>';
                                content += '    <td>' + value.purchaserequest.prnumber + '</td>';
                                content += '    <td>' + value.purchaserequest.department + '</td>';
                                content += '    <td>' + $filter('currency')(value.quantity, '', 2) + '</td>';
                                content += '    <td>' + $filter('currency')(value.price, '', 2) + '</td>';
                                content += '    <td>' + value.item.satuanId.nama + '</td>';
                                content += '    <td>' + $filter('currency')(value.total, '', 2) + '</td>';
                                content += '	<td>' + value.status + '</td>';
                                content += '</tr>';
                            });
                        }

                        document.getElementById('hide_' + id).style.display = "inline";
                        document.getElementById('show_' + id).style.display = "none";

                        if (idx === -1) {
                            form.detailRows.push(id);
                        }

                        var newRow = $('<tr id="detilview' + id + '" class="detilview"><td colspan="8">' + content + '</td></tr>');
                        newRow.insertAfter($('#data_' + id));
                    }, function errorCallback(response) {
                        ModalService.showModalInformation('Terjadi kesalahan pada system!');
                    });
            }
        }

        function createdRow(nRow, data, iDisplayIndex, iDisplayIndexFull) {
            $compile(nRow)($scope);
            var start = this.fnSettings()._iDisplayStart;
            $("td:eq(0)", nRow).html(start + iDisplayIndex + 1).attr("align", "center");

            var tr = $(nRow);
            $(tr).attr("id", "data_" + iDisplayIndex);
        }

        $scope.$on('event:dataTableLoaded', function (evt, loadedDT) {
            loadedDT.DataTable.data().each(function (data) {
                form.selected[data.item.id] = false;
            });
        });

        var _toggle = true;

        function toggleAll() {
            for (var prop in form.selected) {
                if (form.selected.hasOwnProperty(prop)) {
                    form.selected[prop] = _toggle;
                }
            }
            _toggle = !_toggle;
        }

        // Tombol Kontrol
        $scope.btnKembaliIndex = function () {
            RequestService.modalConfirmation('Anda yakin kembali ke Index PR Konsolidasi ?')
                .then(function () {
                    $state.go('app.promise.procurement-purchaserequestconsolidation');
                });
        }

        $scope.btnSimpanPR = function () {
            var params = {
                prNumber: form.prnumber,
                prDate: form.daterequired,
                prDeskripsi: form.description,
                prItemList: form.itemList,
                prGrandTotal: form.totalHPSItemPR
            }

            //console.log("test data : "+JSON.stringify(params));
            var urlSimpan = "";
            if (typeof form.pr === "undefined" || form.pr == null) {
                urlSimpan = "/procurement/purchaseRequestServices/insertPRJoin";
            } else {
                urlSimpan = "/procurement/purchaseRequestServices/updatePRJoin";
                params.prId = form.pr.id
                params.prDate = $filter('date')(form.daterequired, 'yyyy-MM-dd')
            }

            RequestService.modalConfirmation('Anda yakin SIMPAN PR Join ?')
                .then(function () {
                    RequestService.doPOST(urlSimpan, params)
                        .then(function (data) {
                            $state.go('app.promise.procurement-purchaserequestjoin-index');
                        });
                });


        }

        // validate
        $scope.tabs = [];
        $scope.validateForm = function () {
            $scope.errormessagePRNumber = '';
            $scope.errormessageDaterequired = '';
            if (typeof form.prnumber !== 'undefined' && form.prnumber !== '') {

                if (typeof form.pr === "undefined" || form.pr == null) {
                    var paramPost = {
                        prnumber: form.prnumber
                    };
                    RequestService.doPOST('/procurement/purchaseRequestServices/getprnumbervalidation', paramPost)
                        .success(function (data, status, headers, config) {
                            if (data.isnotexist == false) {
                                $scope.errormessagePRNumber = 'PR Number Sudah Ada';
                                $scope.tabs = [{
                                    active: true
                                    }, {
                                    active: false
                                    }];
                                return false;
                            } else {
                                if (typeof form.daterequired !== 'undefined') {
                                    $scope.tabs = [{
                                        active: false
                                    }, {
                                        active: true
                                    }];
                                    return true;
                                } else {
                                    $scope.errormessageDaterequired = 'Tanggal belum diisi';
                                    $scope.tabs = [{
                                        active: true
                                    }, {
                                        active: false
                                    }];
                                    return false;
                                }
                            }
                        });

                } else {
                    if (typeof form.daterequired !== 'undefined') {
                        $scope.tabs = [{
                            active: false
								}, {
                            active: true
								}];
                        return true;
                    } else {
                        $scope.errormessageDaterequired = 'Tanggal belum diisi';
                        $scope.tabs = [{
                            active: true
                                    }, {
                            active: false
                                    }];
                        return false;
                    }
                }

            } else {
                $scope.errormessagePRNumber = 'PR Number harus diisi!';
                $scope.tabs = [{
                    active: true
                                    }, {
                    active: false
                                    }];
                return false;
            }
        }
    }
    PurchaseRequestJoinController.$inject = ['$scope', '$http', '$rootScope', '$resource', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$modal', '$filter', 'RequestService', '$stateParams', '$compile', 'ModalService', '$state', '$timeout', '$q'];

})();