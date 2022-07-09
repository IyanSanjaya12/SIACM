(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PurchaseRequestAddItemController', PurchaseRequestAddItemController);

    function PurchaseRequestAddItemController($scope, $http, $modal, $rootScope, ngTableParams, $filter, $state, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile) {
        var form = this;
    	$scope.nameSearch = undefined;
        $scope.locationSearch = undefined;
        
        $scope.categoryList = [];
        RequestService.doPOSTJSON('/procurement/catalog/CategoryServices/findAllWithTree', {})
    	.then(function (data) {
    		$scope.categoryList = data;    		
    	});
        
        $scope.loading =  true;

        /*RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findCatalogByKontrak', {})
        .then(function (data) {
        	$scope.tableItemList = data;
            $scope.loading = false;
        });*/
        
        form.dtInstance = {};
        
        function callback(json) {
		    console.log(json);
		}
        
        form.tempObj = [];
        
        form.reloadData = function () {
			
		    var resetPaging = false;
		    
		    if(form.dtInstance != null){
		    	form.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

		form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/catalog/CatalogServices/getCatalogListByPagination');
		form.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    		$compile(nRow)($scope);
            var start = this.fnSettings()._iDisplayStart;
            //$("td:first", nRow).html(start + iDisplayIndex + 1);
        });
		form.dtColumns = [
    		DTColumnBuilder.newColumn('kodeItem').withTitle('Kode Produk').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('namaIND').withTitle('Item Name').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('deskripsiIND').withTitle('Description').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('Price').renderWith(function(data){
    			return ($filter('currency')(data.harga,'',2) + ' ' + data.mataUang.kode)
    			//return '<td align="right"> {{data.harga | number:2}} {{data.mataUang.kode}}</td>'
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('Vendor').renderWith(function(data){
    			return data.vendor.alamat + ' <br/> ' + data.vendor.nomorTelpon + ' <br/> ' + data.vendor.email;
    			
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('Action').notSortable().renderWith(function (data, type, full, meta) {
    			form.tempObj[data.id] = data;
    			return '<div class="checkbox c-checkbox c-checkbox-rounded"> <label> <input type="checkbox" ng-click="selectItem(form.tempObj['+data.id+'])"> <span class="fa fa-check"></span> </label> </div>'
    		})
    	];
    			
    	DTInstances.getLast().then(function(instance) {
    		form.dtInstance = instance;
        });
        
        $scope.getCatalogListByCategory = function () {
        	console.log(JSON.stringify(form.categorySelected));
        	$scope.loading = true;
        	var objSearch = {};
        	if(typeof form.categorySelected !== 'undefined'){
        		objSearch = {
                		categoryId: form.categorySelected.id
                }
        	}
        	
        	RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/getCatalogListByCategory', objSearch)
        	.then(function success(data){
        		$scope.tableItemList = data;  
            	$scope.loading = false;   
        	}, function error(errData){
        		$scope.tableItemList = [];
            	$scope.loading = false;
        	});
        }
        
        //$scope.dtOptions = {searching: false };

        $scope.selectedItemList = [];
        $scope.selectItem = function (item) {
        	
            var index = $scope.selectedItemList.indexOf(item);
            if (index == -1) {
                $scope.selectedItemList.push(item);
            } else {
                $scope.selectedItemList.splice(index, 1);
            }
        };

        $scope.btnImport = function () {
            /*console.log("hasil select >>> " + JSON.stringify($scope.selectedItemList));*/
            angular.forEach($scope.selectedItemList, function (item, indexItem) {
                var itemObj = {
                    material: item.item,
                    kode: item.kodeItem,
                    nama: item.namaIND,
                    deskripsi: item.deskripsiIND,
                    vendor: item.vendor.nama,
                    quantity: 0,
                    price: item.harga,
                    unit: item.satuan.nama,
                    matauangId: item.mataUang.id,
                    matauang: item.mataUang.kode,
                    vendor: item.vendor.nama,
                    vendorId: item.vendor.id,
                    specification: item.deskripsi,
                    costcenteritem: $rootScope.nomorDraft
                };
                //check item isexist
                var itemIsExist = false;
                angular.forEach($rootScope.itemPRList, function (value, index) {
                    if (typeof value.kode !== 'undefined') {
                        if (value.kode == itemObj.kode) {
                            itemIsExist = true;
                            alert('Item Pengadaan "' + value.nama + '" dengan KODE : "'+value.kode+'" sudah dipilih!');
                        }
                    }
                });
                if (!itemIsExist) {
                    $rootScope.itemPRList.push(itemObj);
                    $scope.ok();
                }
            });

        };


    }

    PurchaseRequestAddItemController.$inject = ['$scope', '$http', '$modal', '$rootScope', 'ngTableParams', '$filter', '$state', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile'];

})();