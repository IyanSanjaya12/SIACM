/**=========================================================
 * Module: MenuAksiController.js
 * Controller for Master Menu Aksi
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('MenuAksiController', MenuAksiController);

    function MenuAksiController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state,$translate) {
        var menuAksi = this;
        var vm = this;
        vm.dtInstance = {};
        vm.tempObj = [];
        
        function callback(json){
		    console.log(json);
		}
        
        $scope.reloadData = function () {
			
		    var resetPaging = false;
		    
		    if(vm.dtInstance != null){
		    	vm.dtInstance.reloadData(callback, resetPaging);
		    }
		};

        $scope.getMenuAksi = function () {
            vm.loading = true;
            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/menu/menu-aksi/find', {});
            vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1).attr("align","center");
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withOption('headerCallback',function( thead, data, start, end, display ) {
                	$compile(angular.element(thead).contents())($scope);
            	})
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
            
            vm.dtColumns = [
		      DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable(),
		      DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center;"><span translate="promise.procurement.master.menuaksi.judul"></span></div>')
		                .renderWith(function (data, type, full, meta) {
                    return data.menu.nama;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center;">Path</div>')
		                .renderWith(function (data, type, full, meta) {
                    return data.aksi.path;
                }),
		      DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center;"><span translate="template.tabel.aksi"></span></div>').notSortable()
		                .renderWith(function (data, type, full, meta) {
                            vm.tempObj[data.id] = data;                    
                    return '<div style="text-align:center;">'+
                           '         <button type="button" ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit" ng-click="edit(vm.tempObj['+data.id+'])">'+
                           '        </button>'+
                           '        <button type="button" class="btn btn-danger btn-sm btn-delete" ng-click="del('+data.id+')">'+
                           '        </button>'+
                           '  </div>';
                })
	        ];
            vm.loading = false;
        }
        $scope.getMenuAksi();
        
        $scope.add = function () {
        	
        	vm.dtInstance.rerender();
			$state.go('app.promise.procurement-master-menuaksi-view',{
				toDo:'add'
			});
        	
		};
		
		$scope.edit = function (menuAksi) {
			$state.go('app.promise.procurement-master-menuaksi-view',{
				menuAksi : menuAksi,
				toDo:'edit'
			});
		};

        $scope.del = function (menuAksiId) {
        	var url = '/procurement/menu/menu-aksi/delete/';
        	
        	var post={
				id : menuAksiId
			};
        	RequestService.deleteModalConfirmation().then(function (result) {
				RequestService.doPOST(url, post)
				.then(function success(data) {
					RequestService.informDeleteSuccess();
					$scope.reloadData();
				}, function error(response) { 
					vm.loading = false;
					RequestService.informInternalError();
				});
			});
        };
        
        
       
		
		
    
    }

    MenuAksiController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state','$translate'];

})();