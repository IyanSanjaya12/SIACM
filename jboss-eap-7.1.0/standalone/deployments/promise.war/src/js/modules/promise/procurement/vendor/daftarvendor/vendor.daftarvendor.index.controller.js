(function () {
    'use strict';

    angular.module('naut').controller('DaftarVendorIndexController',
        DaftarVendorIndexController);

    function DaftarVendorIndexController($scope, $http, $rootScope, $resource, $location, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, $state, $translate) {
        var vm = this;

        /*$scope.getDataVendor = function () {
            dv.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorListByStatus/1')
                .success(
                    function (data, status, headers, config) {
                        dv.vendorList = data;
                        dv.loading = false;
                    })
                .error(function (data, status, headers, config) {
                    dv.loading = false;
                });
        }
        $scope.getDataVendor();*/

        $scope.edit = function (vendor) {
        	
        	RequestService.doGET('/procurement/vendor/vendorServices/getVendorById/' + vendor)
				.then(function success(data) {
		            $rootScope.detilVendor = data;
		            $rootScope.isEditable = true;
		            $state.go('app.promise.procurement-vendor-daftarvendor-detail');
				}, function error(response) {	
					RequestService.informError("Terjadi Kesalahan Pada System");
			});
        	
        };
        
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

		vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/vendor/vendorServices/getVendorListByPagination');
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
    		
    		DTColumnBuilder.newColumn('nama').withTitle('<div style="text-align:center"><span translate="promise.procurement.master.datadaftarvendor.list.nama_vendor"></span></div>').renderWith(function(data){
    			return data; 
    		}),
    		DTColumnBuilder.newColumn('nomorTelpon').withTitle('<div style="text-align:center"><span translate="promise.procurement.master.datadaftarvendor.list.nomor_telepon"></span></div>').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('email').withTitle('<div style="text-align:center">Email</div>').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.master.datadaftarvendor.list.tanggal_reg"></span></div>').renderWith(function(data){
    			return $filter('date')(data.created, 'dd-MM-yyyy', '');
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center">Status</div>').notSortable().renderWith(function(data){
    			if(data.statusBlacklist == 1){
                    return 'Persetujuan Blacklist';
            	} else if(data.statusBlacklist == 2){
            		return 'Blacklist';
            	} else if(data.statusUnblacklist == 1){
            		return 'Persetujuan Unblacklist';
            	} else if(data.statusPerpanjangan == 1){
            		return 'Persetujuan Perpanjangan Sertifikat';
            	} else if((data.statusBlacklist != 1 && data.statusBlacklist != 2 && data.statusUnblacklist != 1 && data.statusPerpanjangan != 1) && data.status == 1){
            		return 'Vendor Aktif';
            	}
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.master.datadaftarvendor.list.update_terakhir"></span></div>').renderWith(function(data){
    			if(data.updated == null) {
    				return $filter('date')(data.created, 'dd-MM-yyyy', '');
    			} else if(data.updated != null) {
    				return $filter('date')(data.updated, 'dd-MM-yyyy', '');
    			}
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(function (data, type, full, meta) {
    		
    			return ' <button class="mr btn btn-primary btn-xs" ng-click="edit('+data.id+')"><em class="fa fa-search"></em></button> ';
    		})
    	];
    			
    	DTInstances.getLast().then(function(instance) {
    		vm.dtInstance = instance;
        });

    }

    DaftarVendorIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', '$state', '$translate'
    ];

})();