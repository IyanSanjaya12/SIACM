(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlasanBlacklistIndexController', AlasanBlacklistIndexController);

    function AlasanBlacklistIndexController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $ocLazyLoad, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, $state, $translate) {
    	
    	/** load js sub modul **/
		/*$ocLazyLoad.load('src/js/modules/promise/procurement/master/alasanblacklist/alasanblacklist.add.controller.js');
		$ocLazyLoad.load('src/js/modules/promise/procurement/master/alasanblacklist/alasanblacklist.edit.controller.js');*/
		
        var vm = this;
        vm.tempData = [];
        
        vm.dtInstance = {};
        
        function callback(json) {
		    console.log(json);
		}
        
        vm.reloadData = function () {
		    var resetPaging = false;
		    
		    if(vm.dtInstance != null){
			    vm.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

    	vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/master/AlasanBlacklistServices/getAlasanBlacklistByPagination');
    	vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    		$compile(nRow)($scope);
            var start = this.fnSettings()._iDisplayStart;
            $("td:first", nRow).html(start + iDisplayIndex + 1);
        })
        .withOption('headerCallback',function( thead, data, start, end, display ) {
                $compile(angular.element(thead).contents())($scope);
        })
    	
    	vm.dtColumns = [
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable(),
    		
    		DTColumnBuilder.newColumn('alasanBlacklistKeterangan').withTitle('<div style="text-align:center"><span translate="promise.procurement.master.alasanblacklist.list.keterangan"></span></div>')
    		/*.withTitle( $translate('promise.procurement.master.alasanblacklist.list.KETERANGAN'))*/
    		/*'<div style="text-align:center">Keterangan</div>'*/
    		.renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.master.alasanblacklist.list.jangka_waktu"></span></div>')
    		.renderWith(function(data){
    			if(data.alasanBlacklistJkWaktu == 'Y'){
                    return data.alasanBlacklistJmlWaktu + ' Tahun';
            	} else if(data.alasanBlacklistJkWaktu == 'M'){
            		return data.alasanBlacklistJmlWaktu + ' Bulan';
            	} else if(data.alasanBlacklistJkWaktu == 'D') {
            		return data.alasanBlacklistJmlWaktu + ' Hari';
            	}
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(function (data, type, full, meta) {
    		
    			return ' <button ng-class="'+"'bg-'"+' + app.theme.name" class="mr btn btn-xs" ng-click="edit('+data.alasanBlacklistId+')"><em class="fa fa-pencil fa-lg"></em></button> '+
                	   ' <button class="mr btn btn-danger btn-xs" ng-click="del('+data.alasanBlacklistId+')" > '+
                       ' <em class="fa fa-trash fa-lg"></em> '+
                       ' </button> ';
    		})	
    	];
    	
    	DTInstances.getLast().then(function(instance) {
            vm.dtInstance = instance;
        });
        
		$scope.add = function () {
			$state.go('app.promise.procurement-master-alasanblacklist-view',{
				toDo: "Add"
        	})
		};
		
		$scope.edit = function(alasanBlacklist) {
			
			RequestService.doGET('/procurement/master/AlasanBlacklistServices/getById/' + alasanBlacklist)
			.then(function success(data) {
				$state.go('app.promise.procurement-master-alasanblacklist-view', {
					alasanBlacklist : data,
					toDo:"Edit"
				});
			},
			function error(response) {
					RequestService.informError("Terjadi Kesalahan Pada System");
			});
			
		}
        
		$scope.del = function(id){
        	ModalService.showModalConfirmation('Apakah anda yakin ingin menghapus data alasan blacklist?').then(function (result) {
				ModalService.showModalInformationBlock();
				RequestService.doGET('/procurement/master/AlasanBlacklistServices/delete/' + id)
				.then(function successCallback(response) {
					vm.reloadData();
					ModalService.closeModalInformation();
				}, function errorCallback(response) {	
					ModalService.closeModalInformation();			 
					ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
			});
        } 
    }
    
    AlasanBlacklistIndexController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$ocLazyLoad', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter','$state','$translate'];

})();