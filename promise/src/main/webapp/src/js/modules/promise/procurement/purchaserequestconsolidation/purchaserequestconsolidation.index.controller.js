(function () {
    'use strict';
    angular
        .module('naut')
        .controller('PurchaseRequestConsolidationController', PurchaseRequestConsolidationController);

    function PurchaseRequestConsolidationController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $state) {
        var form = this;

        var dtInstance;
        form.purchaseRequestItemList = [];
        form.selected = {};

        var getDataList = function () {
            form.tempObj = [];
            form.detailRows = [];

            var obj = DTOptionsBuilder.newOptions();

            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/purchaseRequestItemServices/getPurchaseRequestItemPaggingListForPrConsolidation');

            /** untuk custom field **/
            form.dtOptions.withOption('order', [[0, 'desc']])
                .withOption('createdRow', createdRow);

            /** untuk menampilkan colom data **/
            form.dtColumns = [
              	DTColumnBuilder.newColumn(null).withTitle('Cek').renderWith(
                    function (data, type, full, meta) {
                        return '<input type="checkbox" ng-model="showCase.selected[' + data.item.id + ']"/>';
                    }
              	),
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
                    return $filter('currency')(data.total, '', 2);
                }),
		        DTColumnBuilder.newColumn(null).withTitle('Action').notSortable().renderWith(
                    function (data, type, full, meta) {
                        form.tempObj[data] = data;
                        return '<button id="join_' + meta.row + '"style="display:none;" class="btn btn-info btn-sm btn-addon" ng-click="form.joinPR(' + data.item.id + ')"><em class="fa fa-pencil fa-lg"></em> Join PR &nbsp;</button >';
                    }
                )
            ];
        }

        getDataList();

        DTInstances.getLast().then(function (instance) {
            dtInstance = instance;
        });

        form.hideDetail = function (id) {
            document.getElementById('hide_' + id).style.display = "none";
            document.getElementById('show_' + id).style.display = "inline";
            document.getElementById('join_' + id).style.display = "none";
            $("#detilview" + id).remove('');
        }

        form.joinPR = function (id) {
            var itemList = [];
            itemList.push(id);
            $state.go("app.promise.procurement-purchaserequestjoin", {
                itemList: itemList
            });
        }

        form.joinPRAfterChecked = function () {
            if ($scope.showCase != undefined) {
                var itemList = [];
                angular.forEach($scope.showCase.selected, function (value, key) {
                    if (value) {
                        itemList.push(key);
                    }
                });
                
                $state.go("app.promise.procurement-purchaserequestjoin", {
                    itemList: itemList
                });

            } else {
                ModalService.showModalInformation('Silahkan pilih item untuk Join!!');
            }
        }

        form.viewDetail = function (id, data) {
            var content = '';
            var idx = $.inArray(id, form.detailRows);

            if (idx == 0) {
                form.hideDetail(id);
                form.detailRows.splice(idx, 1);
            } else {
                RequestService.doGET('/procurement/purchaseRequestItemServices/getPurchaseRequestItemVerifiedListByItem/' + data)
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
                        document.getElementById('join_' + id).style.display = "inline";

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
            $("td:eq(0)", nRow).attr("align", "center");
            $("td:eq(1)", nRow).html(start + iDisplayIndex + 1).attr("align", "center");

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
    }

    PurchaseRequestConsolidationController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$state'];

})();