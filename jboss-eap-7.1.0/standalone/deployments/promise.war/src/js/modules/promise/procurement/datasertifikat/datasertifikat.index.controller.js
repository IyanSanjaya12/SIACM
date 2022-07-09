
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('SertifikatVendorController', SertifikatVendorController);

	function SertifikatVendorController($window, ModalService, RequestService, $filter, $scope, $http, $rootScope, $resource, $location, $modal, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $ocLazyLoad, $log, $state) {
	
		var vm = this;
		vm.filter = {}
		
		vm.dtInstance = {};
		vm.purchaseRequestItemList = [];
		vm.Math = window.Math;
		
		function callback(json) {
		    $log.info(json);
		}

		$scope.reloadData = function () {
		    var resetPaging = false;
		    
		    if(vm.dtInstance != null){
			    vm.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

        	vm.tempObj = [];
            vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/vendor/SertifikatVendorServices/getSertifikatVendorPaggingList', vm.filter);
            
            /** untuk custom field **/
            vm.dtOptions.withOption('fnRowCallback', function (nRow, data, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
                var start = this.fnSettings()._iDisplayStart;
                $("td:first", nRow).html(start + iDisplayIndex + 1).attr("align","center");
                $("td", nRow).attr("align","center"); 
            })
            .withOption('headerCallback',function( thead, data, start, end, display ) {
                  $compile(angular.element(thead).contents())($scope);
              });
            
            
            /** untuk menampilkan colom data **/
            vm.dtColumns = [
              	DTColumnBuilder.newColumn('id').withTitle('<div style="text-align:center"><span translate="template.tabel.nomor"></span></div>').notSortable(), 
 				DTColumnBuilder.newColumn('vendor.nama').withTitle('<div style="text-align:center"><span translate="promise.procurement.RegistrasiVendor.sertifikatvendor.list.nama_vendor"></div>'),
				DTColumnBuilder.newColumn('vendor.email').withTitle('<div style="text-align:center"><span translate="promise.procurement.RegistrasiVendor.sertifikatvendor.list.email"></div>'),
				DTColumnBuilder.newColumn('nomor').withTitle('<div style="text-align:center"><span translate="promise.procurement.RegistrasiVendor.sertifikatvendor.list.nomor_sertifikat"></div>'),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.RegistrasiVendor.sertifikatvendor.list.tanggal_terbit"></div>').renderWith(
					function (data, type, full, meta) {
			           	return $filter('date')(data.tanggalMulai, 'yyyy-MM-dd', '');
			        }
			    ),
				DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="promise.procurement.RegistrasiVendor.sertifikatvendor.list.tanggal_berakhir"></div>').renderWith(
				        function (data, type, full, meta) {
				           	return $filter('date')(data.tanggalBerakhir, 'yyyy-MM-dd', '');
				        }
				),
				DTColumnBuilder.newColumn(null).withTitle('Status').renderWith(
				        function (data, type, full, meta) {
				           	if(data.status == 0){
				           		return 'Persetujuan Penerbitan Sertifikat';
				           	}else if(data.status == 1){
				        		return 'Aktif';
				           	}else if(data.status == 2){
				           		return 'Request Perpanjangan Sertifikat';
				           	}else if(data.status == 3){
				           		return 'Ditolak';
				           	}else if(data.status == 4){
				           		return 'Expired';
				           	}
				        }
				),
		        DTColumnBuilder.newColumn(null).withTitle('<div style="text-align:center"><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(
		            function (data, type, full, meta) {
			            vm.tempObj[data] = data;
						return '<button class="btn btn-info btn-sm " ng-click=" viewDetail(' + data.vendor.id + ')"><em class="glyphicon glyphicon-zoom-in"></em></button >' + 
						'&nbsp;<button class="btn btn-success btn-sm " ng-click=" printSertifikat(' + data.vendor.user + ')"><em class="glyphicon glyphicon-print"></em></button >';
					}
                )
            ];
              

        $scope.sertifikat = function (instance) {
            vm.dtInstance = instance.sertifikat;
        }
		
		$scope.printSertifikat = function(id){
//			RequestService.doPrint({reportFileName:'PrintTDR', vPk:id});	
			$window.open($rootScope.backendAddress + '/procurement/report/printReportGet?reportFileName=PrintTDR&vPk=' + id, '_blank');
 		} 
        
         
        $scope.viewDetail = function(vendorId){
			RequestService.doGET('/procurement/vendor/vendorServices/getVendor/' + vendorId)
				.then(function success(data){
						$state.go('app.promise.procurement-datasertifikat-datasertifikat-view', {
							toDo: 'detail',
							vendor:data	
						});
				},
				function error(response) {
					RequestService.informError('Terjadi Kesalahan Pada System');
			});
        };
	}

	SertifikatVendorController.$inject = ['$window', 'ModalService', 'RequestService', '$filter', '$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$ocLazyLoad', '$log', '$state'];

})();