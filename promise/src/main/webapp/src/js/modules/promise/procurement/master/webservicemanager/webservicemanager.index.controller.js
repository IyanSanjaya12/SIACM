(function () {
    'use strict';

    angular
        .module('naut')
        .controller('WebServiceManagerController', WebServiceManagerController);

    function WebServiceManagerController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state) {
        var form = this;
        form.dtInstance = {};
        form.tempObj = [];

        $scope.initLoadWSManager = function () {
            console.log('cek load!');
            form.loading = true;
            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/master/webservicesmanager/findWithPagging', {});
            form.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);

            form.dtColumns = [
		      DTColumnBuilder.newColumn(null).withTitle('Nomor').notSortable(),
		      DTColumnBuilder.newColumn(null).withTitle('Services')
		                .renderWith(function (data, type, full, meta) {
                    return data.services;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Function')
		                .renderWith(function (data, type, full, meta) {
                    return data.function;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Path')
		                .renderWith(function (data, type, full, meta) {
                    return data.path;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Public')
		                .renderWith(function (data, type, full, meta) {
                    //return data.isPublic;
                    return '<div class="form-group">' +
                        '<div class="checkbox c-checkbox c-checkbox-rounded">' +
                        '             <label>' +
                        '                 <input type="checkbox" ng-checked="' + (data.isPublic == true ? 'true' : 'else') + '" disabled=""/>' +
                        '                <span class="fa fa-check"></span>' +
                        '           </label>' +
                        '         </div>' +
                        '    </div>';
                }),

		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                    form.tempObj[data.id] = data;
                    return '<span>' +
                        '         <button type="button" ng-class="'+"'bg-'"+' + app.theme.name" class="mr btn btn-xs" ng-click="form.edit(form.tempObj[' + data.id + '])">' +
                        '             <em class="fa fa-pencil fa-lg"></em>' +
                        '        </button>' +
                        '        <button type="button" class="mr btn btn-danger btn-xs" ng-click="form.del(' + data.id + ')">' +
                        '            <em class="fa fa-trash fa-lg"></em>' +
                        '        </button>' +
                        '        <button type="button" class="mr btn btn-default btn-xs" ng-click="form.getDetil(form.tempObj[' + data.id + '])">' +
                        '            <em class="fa icon-search"></em>' +
                        '        </button>' +
                        '   </span>';
                })
	        ];
        }
        $scope.initLoadWSManager();

        //show detil
        form.getDetil = function (ws) {
            $rootScope.wsDetil = ws;
            var modalInstance = $modal.open({
                templateUrl: '/detilWebServices.html',
                controller: ModalInstanceWSDetilCtrl,
                size: 'md'
            });

        }
        var ModalInstanceWSDetilCtrl = function ($scope, $rootScope, $modalInstance) {
            $scope.ws = $rootScope.wsDetil;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceWSDetilCtrl.$inject = ['$scope', '$rootScope', '$modalInstance'];

        //add
        $scope.btnAdd = function () {
            $location.path('/app/promise/procurement/master/webservicemanager/add');
        };
        
        //edit
        form.edit = function(wsData){
            $rootScope.wsSelected = wsData;
            $location.path('/app/promise/procurement/master/webservicemanager/edit');
        }

        //delete
        form.del = function (id) {
            var modalInstance = $modal.open({
                templateUrl: '/deleteWebServices.html',
                controller: ModalInstanceDeleteAksiCtrl,
                size: 'md',
                resolve: {
                    aksi: function () {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                $http.get($rootScope.backendAddress + '/procurement/master/webservicesmanager/delete/' + id)
                    .success(function (data, status, headers, config) {
                        var resetPaging = false;
                        form.dtInstance.reloadData(resetPaging);
                        $scope.initLoadWSManager();
                    }).error(function (data, status, headers, config) {})
            }, function () {});
        };
        var ModalInstanceDeleteAksiCtrl = function ($scope, $modalInstance, aksi) {
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
    }

    WebServiceManagerController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state'];
})();