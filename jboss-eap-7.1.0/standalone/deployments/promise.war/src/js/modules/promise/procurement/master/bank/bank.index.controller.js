(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BankIndexController', BankIndexController);

    function BankIndexController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $modal, $state, $stateParams, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, $translate) {
    	
        var vm = this;
        vm.bank = ($stateParams.bank != undefined) ? $stateParams.bank : {};
        $scope.dtInstance = {};
        
        function callback(json) {
    	    console.log(json);
    	}
        
        $scope.reloadData = function () {
    		
    	    var resetPaging = false;
    	    
    	    if($scope.dtInstance != null){
    	    	$scope.dtInstance.reloadData(callback, resetPaging);
    	    }
    	};  

    	$scope.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/master/bank/get-bank-by-pagination-with-token');
    	$scope.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    		$compile(nRow)($scope);
            var start = this.fnSettings()._iDisplayStart;
            var rowNum = start + iDisplayIndex + 1;
            $("td:first", nRow).html('<div class="text-center">' + rowNum + '</div>');
        })
        .withOption('headerCallback',function( thead, data, start, end, display ) {
                $compile(angular.element(thead).contents())($scope);
            })

        ;
    	$scope.dtColumns = [
    	            		DTColumnBuilder.newColumn(null).withTitle('<span class="text-center" translate="template.tabel.nomor"></span>').notSortable(),
    	            		
    	            		DTColumnBuilder.newColumn('namaBank').withTitle('<div class="text-center"> <span  translate="promise.procurement.master.bank.list.namabank"></span></div>').renderWith(function(data){
    	            			return data;
    	            		}),
    	            		DTColumnBuilder.newColumn('namaKantor').withTitle('<div class="text-center"> <span  translate="promise.procurement.master.bank.list.namakantor"></span></div>').renderWith(function(data){
    	            			return data;
    	            		}),
    	            		DTColumnBuilder.newColumn('kota').withTitle('<div class="text-center"> <span  translate="promise.procurement.master.bank.list.kota"></span></div>').renderWith(function(data){
    	            			return data;
    	            		}),
    	            		DTColumnBuilder.newColumn('telepon').withTitle('<div class="text-left"> <span  translate="promise.procurement.master.bank.list.telepon"></span></div>').renderWith(function(data){
    	            			return ('<div class="text-center">' + data + '</div>');
    	            		}),
    	            		DTColumnBuilder.newColumn(null).withTitle('<div class="text-center" ><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(function (data, type, full, meta) {
    	            		
    	            			return ' <div class="text-center"><button ng-class="'+"'bg-'"+' + app.theme.name" class="mr btn btn-icon-sm" ng-click="edit('+data.id+')"><em class="fa fa-pencil fa-lg"></em></button> '+
    	                        ' <button class="mr btn btn-danger btn-icon-sm" ng-click="del('+data.id+')"><em class="fa fa-trash fa-lg"></em></button></div>';
    	            		})
    	            	];
    			
    	DTInstances.getLast().then(function(instance) {
    		$scope.dtInstance = instance;
        });
	        
    	$scope.add = function () {
        	$state.go('app.promise.procurement-master-bank-view', {
				toDo : "add"
			});
        }

    	$scope.edit = function (bankNew) {
    		
    		$state.go('app.promise.procurement-master-bank-view', {
				bank : bankNew,
				toDo : "edit"
			});	
        }
        
    	$scope.del = function (bankId) {
        	var url = "/procurement/master/bank/delete";
			var data = {};
        	data.id = bankId;
        	RequestService.deleteModalConfirmation().then(function(result) {
        		vm.loading = true;
				RequestService.doPOST(url, data).success(function(data) {
					RequestService.informDeleteSuccess();
					vm.loading = false;
					$scope.reloadData();
				}).error(function(data, status, headers, config) {
					RequestService.informInternalError();
					vm.loading = false;
				})
			});
        };      
    }
    
   BankIndexController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', '$state','$stateParams', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', '$translate'];

})();