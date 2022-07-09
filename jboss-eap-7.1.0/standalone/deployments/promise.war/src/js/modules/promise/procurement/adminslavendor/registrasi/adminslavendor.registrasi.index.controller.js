
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('AdminSLARegistrasiVendorController', AdminSLARegistrasiVendorController);

	function AdminSLARegistrasiVendorController($translate, $state,ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $ocLazyLoad) {

		/** load js sub modul **/
		/*$ocLazyLoad.load('src/js/modules/promise/procurement/adminslavendor/registrasi/adminslavendor.registrasi.view.controller.js'); */
		
		var vm = this;  
		vm.filter = {}  
		
		vm.dtInstance = {}; 
		vm.Math = window.Math;
		
		function callback(json) {
		}

		vm.reloadData = function () {
		    var resetPaging = false;
		    
		    if(vm.dtInstance != null){
			    vm.dtInstance.reloadData(callback, resetPaging);
		    }
		};  
		
        $scope.getDataList = function () {
        	vm.tempObj = [];

            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/vendor/SLAAdminVendorServices/getSLAAdminVendorPaggingList/REGISTRASI', vm.filter);
            
            /** untuk custom field **/
            vm.dtOptions.withOption('fnRowCallback', function (nRow, data, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
                var start = this.fnSettings()._iDisplayStart;
                $("td:first", nRow).html(start + iDisplayIndex + 1).attr("align","center");
                $("td", nRow).attr("align","center"); 
                
            })
            .withOption('headerCallback',function( thead, data, start, end, display ) {
                $compile(angular.element(thead).contents())($scope);
            })
            ;
            
            
            
            /** untuk menampilkan colom data **/
            vm.dtColumns = [
              	DTColumnBuilder.newColumn('id').withTitle('<span style="text-align: center;" translate="template.tabel.nomor"></span>').notSortable(), 
 				DTColumnBuilder.newColumn('vendor.nama').withTitle('<div style="text-align: center;"> <span  translate="promise.procurement.manajemenvendor.slaregistrasivendor.list.vendor"></span></div>'),   
 				DTColumnBuilder.newColumn('slaVendor.namaProses').withTitle('<div style="text-align: center;"> <span  translate="promise.procurement.manajemenvendor.slaregistrasivendor.list.proses"></span></div>'),      
 				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align: center;"> <span  translate="promise.procurement.manajemenvendor.slaregistrasivendor.list.tanggal_proses"></span></div>').renderWith(
 						function (data, type, full, meta) {
 				           	return $filter('date')(data.created, 'yyyy-MM-dd', '');
 				        }
 				),
		        DTColumnBuilder.newColumn(null).withTitle('<div style="text-align: center;"> <span  translate="template.tabel.aksi"></span></div>').notSortable().renderWith(
		        		function (data, type, full, meta) {
				           vm.tempObj[data] = data;
							return '<button class="btn btn-info btn-sm " ng-click="viewDetail(' + data.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >';
 						}
                )
            ];
        }

        $scope.getDataList();

        vm.slaVendorRegistrasi = function (instance) {
            vm.dtInstance = instance.slaVendorRegistrasi;
        }
        
        $scope.viewDetail = function(id){
        	$rootScope.slaAdminVendorId = id;
			$state.go('app.promise.procurement-manajemenvendor-adminslavendor-registrasi-view');
        }
	}

	AdminSLARegistrasiVendorController.$inject = ['$translate','$state','ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$ocLazyLoad'];

})();