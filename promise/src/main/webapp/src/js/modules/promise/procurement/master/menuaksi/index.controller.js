/**=========================================================
 * Module: MenuAksiController.js
 * Controller for Master Menu Aksi
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MenuAksiController', MenuAksiController);

    function MenuAksiController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state) {
        var menuAksi = this;
        var form = this;
        form.dtInstance = {};
        form.tempObj = [];

        menuAksi.go = function (path) {
            $location.path(path);
        };
        $scope.getMenuAksi = function () {
            menuAksi.loading = true;
            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/menu/menu-aksi/find', {});
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
		      DTColumnBuilder.newColumn(null).withTitle('Menu')
		                .renderWith(function (data, type, full, meta) {
                    return data.menu.nama;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Path')
		                .renderWith(function (data, type, full, meta) {
                    return data.aksi.path;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                            form.tempObj[data.id] = data;                    
                    return '<span>'+
                           '         <button type="button" ng-class="'+"'bg-'"+' + app.theme.name" class="mr btn btn-xs" ng-click="form.edit(form.tempObj['+data.id+'])">'+
                           '             <em class="fa fa-pencil fa-lg"></em>'+
                           '        </button>'+
                           '        <button type="button" class="mr btn btn-danger btn-xs" ng-click="form.del('+data.id+')">'+
                           '            <em class="fa fa-trash fa-lg"></em>'+
                            '        </button>'+
                             '   </span>';
                })
	        ];
        }
        $scope.getMenuAksi();
        
        menuAksi.add = function () {
            $location.path('/app/promise/procurement/master/menuaksi/add');
        };

        menuAksi.del = function (id, size) {
            var modalInstance = $modal.open({
                templateUrl: '/deleteMenuAksi.html',
                controller: ModalInstanceDeleteMenuAksiCtrl,
                size: size,
                resolve: {
                    menuAks: function () {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                $http.get($rootScope.backendAddress + '/procurement/menu/menu-aksi/delete/' + id)
                    .success(function (data, status, headers, config) {
                        $scope.getMenuAksi();
                    }).error(function (data, status, headers, config) {})
            }, function () {});
        };
        var ModalInstanceDeleteMenuAksiCtrl = function ($scope, $modalInstance, menuAks) {
            $scope.menuAksi = menuAks;
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalInstanceDeleteMenuAksiCtrl.$inject = ['$scope', '$modalInstance', 'menuAks'];

        menuAksi.edit = function (ma) {
            $rootScope.menuAksiEdit = ma;
            $location.path('/app/promise/procurement/master/menuaksi/edit');
        };
    }

    MenuAksiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state'];

})();