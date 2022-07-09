
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenilaianVendorController', PenilaianVendorController);

	function PenilaianVendorController(ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $ocLazyLoad) {

		/** load js sub modul **/
		$ocLazyLoad.load('src/js/modules/promise/procurement/manajemenvendor/manajemenvendor.penilaian.view.controller.js');
		$ocLazyLoad.load('src/js/modules/promise/procurement/manajemenvendor/manajemenvendor.penilaian.edit.controller.js');
		$ocLazyLoad.load('src/js/modules/promise/procurement/manajemenvendor/manajemenvendor.penilaian.viewedit.controller.js');
		$ocLazyLoad.load('src/js/modules/promise/procurement/manajemenvendor/manajemenvendor.penilaian.viewdetil.controller.js');
		
		var form = this; 
		form.bidangUsahaList= [];
		form.subBidangUsahaList= [];
		form.loading = true;
		form.filter = {}
		
		RequestService.doGET('/procurement/master/bidang-usaha/get-list')
		.then(function (data, status, headers, config) { 
			form.bidangUsahaList.push =[];
			form.bidangUsahaList = data; 
		}); 
		
		form.edit = function(vendor){
			RequestService.doGET('/procurement/vendor/vendorServices/getVendor/' + vendor)
			.then(function (data, status, headers, config) { 
				$rootScope.penilaianDataVendor = data;
				$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
			});
		}
		
		form.getSubBidangUsaha = function(){
			var id = form.filter.bidangUsaha;
			form.subBidangUsahaList = [];
			
			if (form.filter.bidangUsaha == '' || form.filter.bidangUsaha == null) {
				var id = 0;
				form.filter.bidangUsaha = {};
				form.filter.subBidangUsaha = {};
				
			} else {
				var id = form.filter.bidangUsaha;
				RequestService.doGET('/procurement/master/sub-bidang-usaha/get-by-bidang-usaha-id/' + id)
				.then(function (data, status, headers, config) {
					form.subBidangUsahaList.push =[];
					form.subBidangUsahaList = data; 
				});
			}
			
			
		}
		
		form.dtInstance = {};
		form.purchaseRequestItemList = [];
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
		
		$scope.paramDataTable= [];
		
        form.getDataList = function () {
        	form.tempObj = [];

            form.dtOptions = RequestService.requestServerPaggingPenilaianVendor(DTOptionsBuilder.newOptions(), '/procurement/vendor/vendorServices/getVendorPerformaPaggingList', form.filter);
            
            /** untuk custom field **/
            form.dtOptions.withOption('fnRowCallback', function (nRow, data, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
                var start = this.fnSettings()._iDisplayStart;
                $("td:first", nRow).html(start + iDisplayIndex + 1);
            });

            /** untuk menampilkan colom data **/
            form.dtColumns = [
               	DTColumnBuilder.newColumn(null).withTitle('Nomor').notSortable(),
 				DTColumnBuilder.newColumn('nama').withTitle('Nama Vendor'),
				DTColumnBuilder.newColumn('email').withTitle('Email'),
				DTColumnBuilder.newColumn(null).withTitle('Tanggal Terkahir Penilaian').renderWith(
					function (data, type, full, meta) {
						if(data.performanceDate != null && data.performanceDate != ''){
				           	return $filter('date')(data.performanceDate, 'yyyy-MM-dd', '');
						}else{
				           	return '';
						}
			        }
			    ), 
				DTColumnBuilder.newColumn('performanceAvg').withTitle('Rata-rata Rating'),
		        DTColumnBuilder.newColumn(null).withTitle('Action').notSortable().renderWith(
		            function (data, type, full, meta) {
		            	$scope.paramDataTable = data.paramDataTable;
			            form.tempObj[data.id, 'desc'] = data;
						return '<button class="btn btn-info btn-sm " ng-click="form.edit(' + data.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >';
					}
                )
            ];
        }

        form.getDataList();
        
        form.manajemenpenilaianvendor = function (instance) {
            form.dtInstance = instance.manajemenpenilaianvendor;
        }
        
        $scope.exportExcel = function() {

    	   	if($rootScope.dtInstancePenilaianVendor.json.recordsTotal == 0) {
    	   		alert("Mohon maaf tidak ada data untuk di export");
    	   	} else {
    	   		if($scope.paramDataTable[0] == null) {
        		$scope.paramDataTable[0] = "\"\"";
	        	}
	        	if($scope.paramDataTable[1] == null) {
	        		$scope.paramDataTable[1] = "\"\"";
	        	}
	        	if($scope.paramDataTable[2] == null) {
	        		$scope.paramDataTable[2] = "\"\"";
	        	}
	        	if($scope.paramDataTable[3] == null) {
	        		$scope.paramDataTable[3] = "\"\"";
	        	}
	        	if($scope.paramDataTable[4] == null) {
	        		$scope.paramDataTable[4] = "\"\"";
	        	}
	        	if($scope.paramDataTable[5] == "") {
	        		$scope.paramDataTable[5] = "\"\"";
	        	}
	        	
	        	var targetUrl = $rootScope.backendAddress+'/procurement/report/vendor/printPenilaianVendor/'+ $scope.paramDataTable[0] + '/' + $scope.paramDataTable[1] + '/' + $scope.paramDataTable[2] + '/' + $scope.paramDataTable[3] 
	        		+ '/' + $scope.paramDataTable[4] + '/' + $scope.paramDataTable[5] + '/' + $scope.paramDataTable[6] + '/' + $scope.paramDataTable[7]; 
	        	
	    		$http.get(targetUrl, {
	                responseType: 'arraybuffer'
	            })
	            .success(function (data) {
	
	                var file = new Blob([data], {
	                    type: 'application/vnd.ms-excel'
	                });
	                //var fileURL = URL.createObjectURL(file);
	                //indow.open(fileURL, 'C-Sharpcorner');
	                
	                var blobURL = (window.URL || window.webkitURL).createObjectURL(file);
	                var anchor = document.createElement("a");
	                anchor.download = "Penilaian-Vendor.xls";
	                anchor.href = blobURL;
	                anchor.click();
	                
	                $scope.loading = false;
	                
	            });
    		
    	   	}
    	   	
    		return true;
    	}

	}

	PenilaianVendorController.$inject = ['ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$ocLazyLoad'];

})();