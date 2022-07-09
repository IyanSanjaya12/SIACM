(function () {
	
	'use strict';

    angular
        .module('naut')
        .controller('AttributeController', AttributeController);

    function AttributeController($scope, RequestService, $state, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, $rootScope) {
    	var vm = this;
    	vm.tampObj = [];
    	//Isi tabel Attribute
    	$scope.initData = function() {
    		RequestService.doGET('/procurement/catalog/AttributeServices/findAll')
    		.then(function (data) {
    			$scope.attributeList = data;
    		});
    	}
    	$scope.initData();
    	
    	$scope.deleteData = function(id) {
    		
    		RequestService.modalConfirmation('Yakin Ingin Menghapus Attribute '+vm.tampObj[id].name+' ?')
    		.then(function (result) {
    			RequestService.doPOSTJSON('/procurement/catalog/AttributeServices/delete', vm.tampObj[id])
    			.then(function (data) {
					RequestService.modalInformation('Data Berhasil di Hapus')
					.then(function (result) {
						var postData = {
			    				refresh: ''+ data.id +''
						};
						$scope.initTable(postData);
					});
    			},function error(response) {
    				RequestService.informError("Terjadi Kesalahan Pada System");
    			})
    			
    		});
    	}
    	
    	$scope.editData = function(id) {
    		$state.go('app.promise.procurement-master-attribute-add', { dataAttribute: vm.tampObj[id] });
    	}
    	
    	$scope.tambahData = function() {
    		$state.go('app.promise.procurement-master-attribute-add');
    	}
    	
    	$scope.importData = function() {
        	$state.go("app.promise.procurement-master-attribute-import");
        }
    	
    	$scope.initTable = function(postData) {
    		vm.dtOptions = DTOptionsBuilder.newOptions()
	    	.withOption('ajax', {
	    		url: $rootScope.backendAddress + '/procurement/catalog/AttributeServices/getListPaging',
	    		type: 'POST',
	    		data: postData,
	    		 /*PROMISE_SECURITY*/
				"beforeSend": function(xhr) {
			     	xhr.setRequestHeader("Authorization", $rootScope.userToken);
			     },
			     error: function (xhr, error, thrown) {
			 	 	var errorCode = xhr.responseText.substr(xhr.responseText.indexOf("PRMS-ERR"), 12);
	             	$location.path('/page/errorPage/' + errorCode);
	       	     }
			     /*END PROMISE_SECURITY*/
	        })
	        .withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	        	$compile(nRow)($scope);
	        	var start = this.fnSettings()._iDisplayStart;
	        	$("td:first", nRow).html(start + iDisplayIndex + 1);
	        })
	        .withOption('headerCallback', function(thead, data, start, end, display) {
                $compile(angular.element(thead).contents())($scope);
            }) 
	        .withDataProp('data')
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('scrollX', '100%')
            .withPaginationType('full_numbers')
            .withDisplayLength(10)
			
			vm.dtColumns = [
				DTColumnBuilder.newColumn(null).withTitle('<span >No</span>').withOption('width', '50px'),
	            DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.attribute.list.Label"></span>')
	            .renderWith(function(data, type, full, meta) {
	            	return data.name==null?'-':data.name;
	            }),
	            DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.attribute.list.InputType"></span>')
	            .renderWith(function(data, type, full, meta) {
	            	return data.inputType==null?'-':data.inputType.name;
	            }),
                DTColumnBuilder.newColumn(null).withTitle('<span translate="promise.procurement.master.attribute.list.Aksi"></span>').notSortable().withOption('width', '100px')
                .renderWith(function(data, type, full, meta) {
                	vm.tampObj[data.id] = data; 
                	return 	'<a class="btn btn-primary btn-sm btn-edit" ng-click="editData('+ data.id +')" tooltip-placement="top" tooltip="Edit"></a>&nbsp;&nbsp;'+
							'<a class="btn btn-danger btn-sm btn-delete" ng-click="deleteData('+ data.id +')" tooltip-placement="top" tooltip="Delete"></a>';
                	
                })
            ];
		}
    	
    	$scope.getListPaging = function() {
			var postData = {
    				refresh: ''
			};
			$scope.initTable(postData);
		}
    	
    	$scope.getListPaging();
    }
    
    AttributeController.$inject = ['$scope', 'RequestService', '$state', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', '$rootScope'];
	
})();