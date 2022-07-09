/**
 * ========================================================= Module:
 * AksiController.js Controller for Aksi
 * =========================================================
 */

(function() {
	'use strict';

	angular
		.module('naut')
		.controller('AksiController', AksiController);

	function AksiController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory,
			DTRendererService, $state, $log, $translate) {

		var vm = this;

		vm.dtInstance = {};
		vm.tempObj = [];
		vm.nomor='template.tabel.nomor';

		function callback(json) {
			$log.info(json);
		}

		vm.reloadData = function() {
			var resetPaging = false;
			if (vm.dtInstance != null) {
				vm.dtInstance.reloadData(callback, resetPaging);
			}
		};

			vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/menu/aksi/find',{});
			vm.dtOptions.withOption('fnRowCallback', function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
				$compile(nRow)($scope);
				var start = this.fnSettings()._iDisplayStart;
				$('td:first', nRow).html(start + iDisplayIndex + 1).attr("align","center");
			})
			.withOption('headerCallback',function( thead, data, start, end, display ) {
                  $compile(angular.element(thead).contents())($scope);
              });
			
			vm.dtColumns = [
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable(),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center">Path</div>').renderWith(function(data, type, full, meta) {
					return data.path;
				}),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center">Public</div>').renderWith(function(data, type, full, meta) {
					// return data.isPublic;
					$log.info(data.isPublic);
					return '<div class="form-group" style="text-align:center;">'+
                    '<div class="checkbox c-checkbox c-checkbox-rounded">'+
                    '             <label>'+
                    '                 <input type="checkbox" ng-checked="'+(data.isPublic == true ? 'true':'false')+'" disabled=""/>'+
                    '                <span class="fa fa-check"></span>'+
                    '           </label>'+
                    '         </div>'+
                    '    </div>';
				}),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(function(data, type, full, meta) {
					vm.tempObj[data.id] = data;
					return '<div style="text-align:center;">'+
                    '         <button type="button" ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit"  ng-click=" edit('+data.id+')">'+
                    '        </button>'+
                    '        <button type="button" class="btn btn-danger btn-sm btn-delete" ng-click=" del('+data.id+')">'+
                    '        </button>'+
                    '   </div>';
				}) 
			];

		$scope.add = function() {
			$state.go('app.promise.procurement-master-aksi-view', {
				toDo : 'Add'
			});

		};
		
		$scope.edit = function(aksiId) {
			RequestService.doGET('/procurement/menu/aksi/getAksi/' + aksiId)
				.then(function success(data) {
					$state.go('app.promise.procurement-master-aksi-view', {
						toDo : 'Edit',
						aksi : data
					});
				},
				function error(response) {
					RequestService.informInternalError();
        			vm.loading = false;
				});
		};
		
		$scope.del = function (aksiId) {
			RequestService.deleteModalConfirmation().then(function (result) {
				vm.loading = true;
				var data = {
					id : aksiId
				};
				RequestService.doPOST('/procurement/menu/aksi/delete/', data)
					.then(function success(data) {
						RequestService.informDeleteSuccess();
						vm.loading = false;
						vm.reloadData();
					}, function error(response) { 
						RequestService.informInternalError();
	        			vm.loading = false;
				});
			});
        }
	}

	AksiController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter',
			'DTRendererFactory', 'DTRendererService', '$state', '$log', '$translate'];

})();