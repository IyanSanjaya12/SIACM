'use strict';

/* Controllers */

angular
    .module('naut')
    .controller('catalogCtrl', ['$scope', 'RequestService', '$state', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', 'DTRendererFactory', 'DTRendererService', '$compile', '$filter', function ($scope, RequestService, $state, DTOptionsBuilder, DTColumnBuilder, DTInstances, DTRendererFactory, DTRendererService, $compile, $filter) {

        var vm = this;
        vm.tempObj = [];
        vm.bg ="'bg-'";
        vm.dtInstance = {};
        vm.dataParam = {};
        vm.toDo='';

        if (RequestService.getRoleUser().nama == 'VENDOR'){
            vm.isVendor = true;
            $scope.loading = true;
            RequestService.doGET('/procurement/vendor/vendorServices/getVendorByUserId/' + RequestService.getUserLogin().user.id)
                .then(function (vendor, status, headers, config) {
                    $scope.loading = false;
                    $scope.checkIsActive(vendor);
                }, function(err) {
                    $scope.loading = false;
                    
                });
        } else {
            vm.isVendor = false;
        }
        
        $scope.checkIsActive = function(vendor){
        	if(vendor.status == 1 && vendor.isApproval != 1 && vendor.statusBlacklist == 0/*tidak lagi di blacklist atau request*/ ){
        		vm.isActive=true;
        	}else{
        		vm.isActive= false;
        	}
        }
        
        if (RequestService.getRoleUser().nama == 'VENDOR') {
        	vm.isExpired = false;
            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/catalog/CatalogServices/findAll', vm.dataParam);
            vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
            vm.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle('No').notSortable().renderWith(function (data, type, full, meta) {
                    return meta.row + 1;
                }),
			DTColumnBuilder.newColumn('namaIND').withTitle('Nama').renderWith(function (data) {
                    return data;
                }),
            DTColumnBuilder.newColumn('catalogContractDetail.itemPlant').withTitle('Item Plant').renderWith(function (data) {
                    return data;
                }),
			DTColumnBuilder.newColumn('attributeGroup.name').withTitle('Attribute Set').withOption('defaultContent', 'No Value').renderWith(function (data) {
                    return data;
                }),
			DTColumnBuilder.newColumn('harga').withTitle('Price').renderWith(function (data) {
                    return $filter('currency')(data, ' ', 2);
                }),
			DTColumnBuilder.newColumn('satuan.nama').withTitle('Satuan').renderWith(function (data) {
				
                    return data;
                }),
			DTColumnBuilder.newColumn('catalogKontrak.statusCatalog').withTitle('Status Item').renderWith(function (data) {
				if (data == 2){
					return 'Expired';
				}if(data == 1){
					return 'Active';					
				}if(data == 0){
					return 'Not Active';					
				}
            }),
            DTColumnBuilder.newColumn('approvalProcess.status').withTitle('Status Approval').renderWith(function (data) {
            	if(data == 0){
            		return 'Expired';
            	}if(data == 1){
            		return 'Waiting For Approval';
            	}if(data == 2){
            		return 'Rejected';
            	}if(data == 3){
            		return 'Approved';
            	}
            	return (data) ? 'Waiting For Approval' : 'Active';
            }),
			DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
                    vm.tempObj[data.id] = data;
                    return ' <button ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit" ng-click="vm.editDataCatalog(vm.tempObj[' + data.id + '])"></button> ' +
                    ' <button class="btn btn-danger btn-sm btn-delete" ng-click="vm.hapusDataCatalog(vm.tempObj[' + data.id + '])" > ' +
                    ' </button> '; 
                })
		];
        } else {
        	vm.isExpired = false;
            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/catalog/CatalogServices/findAll', vm.dataParam);
            vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
            vm.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle('No').notSortable().renderWith(function (data, type, full, meta) {
                    return meta.row + 1;
                }),
			DTColumnBuilder.newColumn('namaIND').withTitle('Nama').renderWith(function (data) {
                    return data;
                }),
            DTColumnBuilder.newColumn('catalogContractDetail.itemPlant').withTitle('Item Plant').renderWith(function (data) {
                    return data;
                }),
			DTColumnBuilder.newColumn('attributeGroup.name').withTitle('Attribute Set').withOption('defaultContent', 'No Value').renderWith(function (data) {
                    return data;
                }),
			DTColumnBuilder.newColumn('harga').withTitle('Price').renderWith(function (data) {
                    return $filter('currency')(data, ' ', 2);
                }),
			DTColumnBuilder.newColumn('satuan.nama').withTitle('Satuan').renderWith(function (data) {
				
                    return data;
                }),
			DTColumnBuilder.newColumn('catalogKontrak.statusCatalog').withTitle('Status Item').renderWith(function (data) {
				if (data == 2){
					return 'Expired';
				}if(data == 1){
					return 'Active';					
				}if(data == 0){
					return 'Not Active';					
				}
            }),
            DTColumnBuilder.newColumn('approvalProcess.status').withTitle('Status Approval').renderWith(function (data) {
            	if(data == 0){
            		return 'Expired';
            	}if(data == 1){
            		return 'Waiting For Approval';
            	}if(data == 2){
            		return 'Rejected';
            	}if(data == 3){
            		return 'Approved';
            	}
            	return (data) ? 'Waiting For Approval' : 'Active';
            }),
			DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
                    vm.tempObj[data.id] = data;
                    return ' <button ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit" ng-click="vm.editDataCatalog(vm.tempObj[' + data.id + '])"></button> ' +
                    ' <button class="btn btn-danger btn-sm btn-delete" ng-click="vm.hapusDataCatalog(vm.tempObj[' + data.id + '])" > ' +
                    ' </button> '; 
                })
		];
        }

        DTInstances.getLast().then(function (instance) {
            vm.dtInstance = instance;
        });

        vm.addData = function () {
            if (vm.isVendor) {
                if(vm.isActive){               	
                	$state.go('app.promise.procurement-catalog-add', {
                		toDo:'add'
                    });
                }else{
                    RequestService.informError("Anda tidak bisa membuat Catalog Vendor, karena status Anda tidak Aktif!");
                    return false;
                }
            } else {
            	$state.go('app.promise.procurement-catalog-add', {
            		toDo:'add'
                });
            }
        }

        vm.editDataCatalog = function (catalog) {
            $state.go('app.promise.procurement-catalog-add', {
                dataCatalog: catalog,
                toDo:'edit'
            });
        }

        vm.hapusDataCatalog = function (catalog) {
        	if(catalog.status == 1){
        		RequestService.modalInformation('Item sudah melewati tahap Approval!', 'warning');
        	}else{
        		RequestService.modalConfirmation('Apakah anda yakin untuk menghapus data?')
        		.then(function (result) {
        			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/delete', catalog)
        			.then(function (data) {
        				RequestService.modalInformation('Data Berhasil di Hapus')
        				.then(function (result) {
        					vm.dtInstance.reloadData();
        				});
        			});
        		})        		
        	}
        }

        $scope.getContract = function () {
            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/catalog/CatalogServices/findAll', vm.dataParam);
            vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);


            vm.dtColumns = [
                DTColumnBuilder.newColumn(null).withTitle('Nomor').notSortable(),
                DTColumnBuilder.newColumn('namaIND').withTitle('Nama').renderWith(function (data) {
                    return data;
                }),
                DTColumnBuilder.newColumn('attributeGroup.name').withTitle('Attribute Set').withOption('defaultContent', 'No Value').renderWith(function (data) {
                    return data;
                }),
                DTColumnBuilder.newColumn('harga').withTitle('Price').renderWith(function (data) {
                    return $filter('currency')(data, ' ', 2);
                }),
                DTColumnBuilder.newColumn('satuan.nama').withTitle('Satuan').renderWith(function (data) {
                    return data;
                }),
                DTColumnBuilder.newColumn('status').withTitle('Status Item').renderWith(function (data) {
                    return (data) ? 'Active' : 'Not Active';
                }), /*0=not process, 1 = active, 2=reject, 3=approve (last update status)*/
                DTColumnBuilder.newColumn('approvalProcess.status').withTitle('Status Approval').renderWith(function (data) {
                	if(data == 0){
                		return 'Not Process';
                	}if(data == 1){
                		return 'Waiting For Approval';
                	}if(data == 2){
                		return 'Rejected';
                	}if(data == 3){
                		return 'Approved';
                	}
                	return (data) ? 'Waiting For Approval' : 'Active';
                }),
                DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
                    vm.tempObj[data.id] = data;
                    return ' <button ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-edit" ng-click="vm.editDataCatalog(vm.tempObj[' + data.id + '])"></button> ' +
                        ' <button class="btn btn-danger btn-sm btn-delete" ng-click="vm.hapusDataCatalog(vm.tempObj[' + data.id + '])" > ' +
                        ' </button> ';
                })
            ];
            vm.dtInstance.rerender();


        }
}]);