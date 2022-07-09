
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('AdminSLAUnblacklistVendorController', AdminSLAUnblacklistVendorController);

	function AdminSLAUnblacklistVendorController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $ocLazyLoad) {

		/** load js sub modul **/
		$ocLazyLoad.load('src/js/modules/promise/procurement/adminslavendor/unblacklist/adminslavendor.unblacklist.view.controller.js'); 
		
		var form = this;  
		form.filter = {}  
		
		form.dtInstance = {}; 
		form.Math = window.Math;
		
		function callback(json) {
		}

		form.reloadData = function () {
		    var resetPaging = false;
		    
		    if(form.dtInstance != null){
			    form.dtInstance.reloadData(callback, resetPaging);
		    }
		};  
		
        form.getDataList = function () {
        	form.tempObj = [];

            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/vendor/SLAAdminVendorServices/getSLAAdminVendorPaggingList/UNBLACKLIST', form.filter);
            
            /** untuk custom field **/
            form.dtOptions.withOption('fnRowCallback', function (nRow, data, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
                var start = this.fnSettings()._iDisplayStart;
                $("td:first", nRow).html(start + iDisplayIndex + 1).attr("align","center");
                $("td", nRow).attr("align","center"); 
            });
            
            /** untuk menampilkan colom data **/
            form.dtColumns = [
              	DTColumnBuilder.newColumn('id').withTitle('No.').notSortable(), 
 				DTColumnBuilder.newColumn('vendor.nama').withTitle('Nama Vendor'),   
 				DTColumnBuilder.newColumn('slaVendor.namaProses').withTitle('Nama Proses'),      
 				DTColumnBuilder.newColumn(null).withTitle('Tanggal Proses').renderWith(
 						function (data, type, full, meta) {
 				           	return $filter('date')(data.created, 'yyyy-MM-dd', '');
 				        }
 				),
		        DTColumnBuilder.newColumn(null).withTitle('Action').notSortable().renderWith(
		        		function (data, type, full, meta) {
				           form.tempObj[data] = data;
							return '<button class="btn btn-info btn-sm " ng-click="form.viewDetail(' + data.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >';
 						}
                )
            ];
        }

        form.getDataList();

        form.slaVendorUnblacklist = function (instance) {
            form.dtInstance = instance.slaVendorUnblacklist;
        }
        
        form.viewDetail = function(id){
        	$rootScope.slaAdminVendorId = id;
			$location.path('/app/promise/procurement/adminslavendor/blacklist/view');
        }
	}

	AdminSLAUnblacklistVendorController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$ocLazyLoad'];

})();