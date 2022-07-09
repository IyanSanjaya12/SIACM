(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PengadaanGagalController', PengadaanGagalController);

    function PengadaanGagalController($scope, $http, $rootScope, $resource, $location, $modal, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter, DTRendererFactory, DTRendererService, $state) {
        var form = this;
        form.dtInstance = {};
        form.tempObj = [];
        
        form.getDataList = function () {
            form.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/pengadaangagal/PengadaanGagalServices/find', {});
            form.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $compile(nRow)($scope);
                    var start = this.fnSettings()._iDisplayStart;
                    $("td:first", nRow).html(start + iDisplayIndex + 1);
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);

            form.dtColumns = [
		      DTColumnBuilder.newColumn(null).withTitle('Nomor').notSortable(),
		      DTColumnBuilder.newColumn(null).withTitle('No. Pengadaan')
		                .renderWith(function (data, type, full, meta) {
                    return data[1];
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Nama')
		                .renderWith(function (data, type, full, meta) {
                    return data[2];
                }),
              DTColumnBuilder.newColumn(null).withTitle('No. Purchase Request')
		                .renderWith(function (data, type, full, meta) {
		            return data[3];
		        }),
		      DTColumnBuilder.newColumn(null).withTitle('Tahapan')
		                .renderWith(function (data, type, full, meta) {
                    return data[4];
                }),
		      DTColumnBuilder.newColumn(null).withTitle('No. Berita Acara')
		                .renderWith(function (data, type, full, meta) {

                    return data[5];
                }),
		      DTColumnBuilder.newColumn(null).withTitle('Tgl Berita Acara')
		                .renderWith(function (data, type, full, meta) {
                    return ($filter('date')(data[6], 'dd-MM-yyyy'));
                }),

		      DTColumnBuilder.newColumn(null).withTitle('AKSI').notSortable()
		                .renderWith(function (data, type, full, meta) {
                            form.tempObj[data[0]] = data[0];
                    return '<a class="view-order badge bg-success" tooltip-placement="top" tooltip="Edit" ng-click="editPengadaanGagal(form.tempObj[' + data[0] + '])">' +
                        '<em class="fa fa-pencil fa-lg"></em></a> ' +
                        '<a class="view-order badge bg-primary" ng-click="viewPengadaanGagal(form.tempObj['+data[0]+'])" tooltip-placement="top" tooltip="View">' +
                        '<em class="fa fa-search"></em></a>';

                })
	            ];
        }
        form.getDataList();
        
        $scope.getPengadaanGagal = function (pengadaanGagalId,url) {	
			RequestService.doGET('/procurement/pengadaangagal/PengadaanGagalServices/find/'+pengadaanGagalId)
				.then(function success(data) {
					$rootScope.pengadaanGagalSelected = data;
					$location.path(url);
				}, function error(response) { 
					RequestService.informError("Terjadi Kesalahan Pada System");
			});
		}

        //addPengadaanGagal
        $scope.btnAddPengadaanGagal = function () {
            $scope.loading = false;
            $rootScope.anggaranSelected = {};
            var modalInstance = $modal.open({
                templateUrl: 'pengdaan_search.html',
                controller: ModalAddPengadaanGagalController,
                size: 'lg'
            });
            modalInstance.result.then(function (data) {
                $rootScope.pengdaanSelected = data;
                $location.path('/app/promise/procurement/pengadaangagal/add');
            });

        };
        var ModalAddPengadaanGagalController = function ($http, $modalInstance,$scope, $rootScope, $resource, $location) {
            $scope.getIndexPengadaan = function () {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getPengadaanListNotContract')
                    .success(function (data, status, headers, config) {
                        $scope.jadwalPengadaanList = data;
                        //console.log(data);
                        $scope.loading = false;
                    })
                    .error(function (data, status, headers, config) {
                        console.log('Error loading jenis pengadaan');
                        $scope.loading = false;
                    });

            };
            $scope.getIndexPengadaan();        
            
			$scope.cancel = function () {
				$modalInstance.dismiss('cancel');
			};
            
            $scope.choice = function(pengadaan){                
                $modalInstance.close(pengadaan);
            }
        }
        ModalAddPengadaanGagalController.$inject = ['$http', '$modalInstance', '$scope', '$rootScope', '$resource', '$location'];
        
        $scope.viewPengadaanGagal = function(pengadaanGagal){
            $scope.getPengadaanGagal(pengadaanGagal,'/app/promise/procurement/pengadaangagal/view');    
        }
        $scope.editPengadaanGagal = function(pengadaanGagal){
        	 $scope.getPengadaanGagal(pengadaanGagal,'/app/promise/procurement/pengadaangagal/edit');
        }
    }

    PengadaanGagalController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$modal', 'RequestService', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter', 'DTRendererFactory', 'DTRendererService', '$state'];

})();