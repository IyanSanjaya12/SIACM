
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('DokumenExpiredVendorController', DokumenExpiredVendorController);

	function DokumenExpiredVendorController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $ocLazyLoad) {

		
		var form = this;  
		form.filter = {}  
		
		var today = new Date();
		
	 	var akan_tenggang = new Date();
	 	akan_tenggang.setDate(akan_tenggang.getDate() + 15);
	 	
	 	var akan_expired = new Date();
	 	akan_expired.setDate(akan_expired.getDate() + 15);
	  	
		
		form.dtInstance = {}; 
		form.Math = window.Math;
		
		function callback(json) {
		    console.log(json);
		}

		form.reloadData = function () {
		    var resetPaging = false;
		    
		    if(form.dtInstance != null){
			    form.dtInstance.reloadData(callback, resetPaging);
		    }
		};  
		
        form.getDataList = function () {
        	form.tempObj = [];

            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/vendor/DokumenRegistrasiVendorServices/getRekananDokumenExpiredPaggingList', form.filter);
            
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
 				DTColumnBuilder.newColumn('subject').withTitle('Nama Dokumen'),        
 				DTColumnBuilder.newColumn(null).withTitle('Tanggal Berakhir').renderWith(
 						function (data, type, full, meta) {
 							if(data.tanggalBerakhir < today.getTime()){
 	 				          	return $filter('date')(data.tanggalBerakhir, 'yyyy-MM-dd', '') + " <font color='red'> Expired </font>";
 							}else if(data.tanggalBerakhir > today.getTime() && data.tanggalBerakhir < akan_tenggang.getTime()){
 	 				           	return $filter('date')(data.tanggalBerakhir, 'yyyy-MM-dd', '') + " <font color='orange'> Masa Tenggang </font>";
 							}else if(data.tanggalBerakhir > today.getTime() && data.tanggalBerakhir < akan_expired.getTime()){
 	 				           	return $filter('date')(data.tanggalBerakhir, 'yyyy-MM-dd', '');
 							} 
 							else {
 	 				           	return $filter('date')(data.tanggalBerakhir, 'yyyy-MM-dd', '');
 							}
 							
 				        }
 				)
		       /* DTColumnBuilder.newColumn(null).withTitle('Action').notSortable().renderWith(
		        		function (data, type, full, meta) {
				           form.tempObj[data] = data;
							return '<button class="btn btn-info btn-sm " ng-click="form.viewDetail(' + data.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >';
 						}
                )*/
            ];
        }

        form.getDataList();

        form.dokumenExpiredVendor = function (instance) {
            form.dtInstance = instance.dokumenExpiredVendor;
        }
	}

	DokumenExpiredVendorController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$ocLazyLoad'];

})();