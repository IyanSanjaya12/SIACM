/**=========================================================
 * Module: DashboardController.js
 =========================================================*/

(function () {
  'use strict';

  angular.module('naut').controller('DashboardDefaultController', DashboardDefaultController);

  DashboardDefaultController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter', 'RequestService', '$state'];

  function DashboardDefaultController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter, RequestService, $state) {
	  var vm = this;
	  $scope.currentDate = new Date();
	  $scope.isReady = false;
	    $scope.chartData = {};
	    /*perubahan KAI 25 Januari 2021 [21377]*/
	    $scope.isVendor = false;
	    $scope.dataopsi = '';
	    $scope.categorySelected = [];
	    $scope.monitoringVendorSelected = [];
	    $scope.organizationFilterObject = { data: null, viewField: 'nama' };
	    //$scope.yearArray = Array(5).fill('').map((emptyValue, index) => $scope.currentDate.getFullYear() - 2 + index);
	    $scope.yearArray5last = Array(5)
	      .fill('')
	      .map((emptyValue, index) => $scope.currentDate.getFullYear() - 4 + index);
	    $scope.yearDataFilter = { data: null };
	    $scope.dateFilter = {
	      startDate: $scope.currentDate,
	      endDate: $scope.currentDate,
	      maxDate: $scope.currentDate,
	      monitorKinerjaPenyedia: {
	        startDate: $scope.currentDate,
	        endDate: $scope.currentDate,
	      },
	      monitoringProsesPembelian: {
	        startDate: $scope.currentDate,
	        endDate: $scope.currentDate,
	      },
	    };
	    $scope.dashboardDto = {
	    		year:null,
	    		organizationId:null
	    };
	    
	      $http
	      .post($scope.backendAddress + '/procurement/dashboard/cekDataByYear', $scope.yearArray5last) .success((response) => {
	        	  $scope.yearDataFilter.data = response.yearList;
	        	  $scope.yearArray = $scope.yearDataFilter.data;
	        	  console.log($scope.yearDataFilter);
	        	  console.log($scope.yearArray);
	          });
	      
	
	    
	
	    // first load section
	    $http
	      .get($scope.backendAddress + '/procurement/dashboard/firstLoad')
	      .success((response) => {
	        $scope.chartData = response;
	        // add filter
	        $scope.chartData.monitoringPembelian.filterItem = $scope.yearDataFilter;
	        const monitoringProsesPembelianDataLabelsArray = response.monitoringProsesPembelian.dataLabels[0].split('-');
	        $scope.dateFilter.startDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[0]);
	        $scope.dateFilter.endDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[1]);
	        $scope.dateFilter.tglMulai = $scope.dateFilter.startDate;
	        $scope.dateFilter.tglAkhir = $scope.dateFilter.endDate;
	        $scope.dateFilter.monitoringProsesPembelian.startDate = $scope.dateFilter.startDate;
	        $scope.dateFilter.monitoringProsesPembelian.endDate = $scope.dateFilter.endDate;
	        $scope.isReady = true;
	        /*perubahan KAI 25 Januari 2021 [21377]*/
	        $scope.isVendor = response.isVendor;
	        $scope.isAdmin = true;
	        
	      })
	      .error((error) => {
	        console.log(error);
	      });
	
	    // dashboard header filter section
	
	    $scope.organisasiList = function () {
		       
	        RequestService.doGET('/procurement/master/organisasi/get-list-parent').then(function success(data) {
	        	$scope.organizationFilterObject.data = data;
	        }, function error(response) {
	        	RequestService.informInternalError();
	        	vm.loading = false;
	        });
	   }
		$scope.organisasiList();
	    
	    
	    $scope.monthDataFilter = ['Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni', 'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'];
	
	
	    // dashboard header
	    $scope.handleFilterOrganization = (organization) => {
	    	$scope.dashboardDto.organizationId = organization.id;
	    	console.log(organization);
	    	console.log($scope.dashboardDto);
	    	$scope.getDataFromBackend('getFilter', $scope.dashboardDto).then((response) => {
	            $scope.$apply(() => { 
	          $scope.chartData = response;
	            // add filter
	            $scope.chartData.monitoringPembelian.filterItem = $scope.yearDataFilter;
	            const monitoringProsesPembelianDataLabelsArray = response.monitoringProsesPembelian.dataLabels[0].split('-');
	            $scope.dateFilter.startDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[0]);
	            $scope.dateFilter.endDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[1]);
	            $scope.dateFilter.tglMulai = $scope.dateFilter.startDate;
	            $scope.dateFilter.tglAkhir = $scope.dateFilter.endDate;
	            $scope.dateFilter.monitoringProsesPembelian.startDate = $scope.dateFilter.startDate;
	            $scope.dateFilter.monitoringProsesPembelian.endDate = $scope.dateFilter.endDate;
	            $scope.isReady = true;
	            });
	          });
	    };
	
	    $scope.handleFilterYear = (year) => {
	    	$scope.dashboardDto.year = year;
	    	console.log(year);
	    	console.log($scope.dashboardDto);
	      
	      $scope.getDataFromBackend('getFilter', $scope.dashboardDto).then((response) => {
	          $scope.$apply(() => { 
	        $scope.chartData = response;
	          // add filter
	          $scope.chartData.monitoringPembelian.filterItem = $scope.yearDataFilter;
	          const monitoringProsesPembelianDataLabelsArray = response.monitoringProsesPembelian.dataLabels[0].split('-');
	          $scope.dateFilter.startDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[0]);
	          $scope.dateFilter.endDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[1]);
	          $scope.dateFilter.tglMulai = $scope.dateFilter.startDate;
	          $scope.dateFilter.tglAkhir = $scope.dateFilter.endDate;
	          $scope.dateFilter.monitoringProsesPembelian.startDate = $scope.dateFilter.startDate;
	          $scope.dateFilter.monitoringProsesPembelian.endDate = $scope.dateFilter.endDate;
	          $scope.isReady = true;
	          });
	        });
	    };
	
	    $scope.handleFilterMonth = (monthName) => {
	      console.log('Info: Comming from handleFilterMonth');
	      console.log(monthName);
	    };
	
	    // dashboard charts section
	    // Bar chart (1)
	    $scope.handleMonitoringPembelianSelect = (year) => {
	      $scope.getDataFromBackend('getPembelianPerPeriodeReport', { year }).then((response) => {
	        $scope.$apply(() => {
	          $scope.chartData.monitoringPembelian = response.monitoringPembelian;
	          $scope.chartData.monitoringPembelian.filterItem = $scope.yearDataFilter;
	        });
	      });
	    };
	
	    $scope.handleFilterMonitoringKinerjaPenyedia = (date, target) => {
	      $scope.chartData.monitoringKinerjaPenyedia = null;
	      $scope.dateFilter[target] = new Date(date);
	      const startDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.startDate);
	      const endDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.endDate);
	      $scope.getDataFromBackend('getKinerjaPenyediaReport', { startDate, endDate }).then((response) => {
	        $scope.$apply(() => {
	          $scope.chartData.monitoringKinerjaPenyedia = response.monitoringKinerjaPenyedia;
	        });
	      });
	    };
	
	    // Bar chart (3)
	    $scope.handleMonitoringCatalogSelect = (eventName, categoryIdList) => {
	      if (eventName === 'click') {
	        $scope.chartData.monitoringCatalog = null;
	        $scope.categorySelected = categoryIdList;
	        const startDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.tglMulai);
	        const endDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.tglAkhir);
	        $scope.getDataFromBackend('getItemCatalogReport', { dataIdList: categoryIdList, startDate, endDate }).then((response) => {
	          $scope.$apply(() => {
	            $scope.chartData.monitoringCatalog = response.monitoringCatalog;
	          });
	        });
	      }
	    };
	
	    // Doughnut chart (4)
	    $scope.handleFilterMonitoringProsesPembelian = (date, target) => {
	      $scope.dateFilter.monitoringProsesPembelian[target] = new Date(date);
	      const startDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.monitoringProsesPembelian.startDate);
	      const endDate = $scope.convertDateToBackendDateFormat($scope.dateFilter.monitoringProsesPembelian.endDate);
	      $scope.chartData.monitoringProsesPembelian = null;
	      $scope.getDataFromBackend('getProsesPembelianReport', { startDate, endDate }).then((response) => {
	        $scope.$apply(() => {
	          $scope.chartData.monitoringProsesPembelian = response.monitoringProsesPembelian;
	          // const monitoringProsesPembelianDataLabelsArray = response.monitoringCatalog.dataLabels[0].split('-');
	          // $scope.dateFilter.monitoringProsesPembelian.startDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[0]);
	          // $scope.dateFilter.monitoringProsesPembelian.endDate = $scope.convertToJavascriptDate(monitoringProsesPembelianDataLabelsArray[1]);
	        });
	      });
	    };
	
	    // bar chart (5)
	    // Grafik Bidang Usaha Penyedia
	    $scope.handleMonitoringVendorSelect = (eventName, subBidangUsahaListId) => {
	      if (eventName === 'click') {
	        $scope.chartData.monitoringVendor = null;
	        const dataIdList = subBidangUsahaListId;
	        $scope.monitoringVendorSelected = subBidangUsahaListId;
	        $scope.getDataFromBackend('getVendorReport', { dataIdList }).then((response) => {
	          $scope.$apply(() => {
	            $scope.chartData.monitoringVendor = response.monitoringVendor;
	          });
	        });
	      }
	    };
	
	    // doughnut chart (6)
	    // Grafik Masa Kontrak Catalog
	    $scope.handleFilterMonitoringMasaKontrakCatalog = (date) => {
	      $scope.chartData.monitoringMasaKontrakCatalog = null;
	      const endDate = $scope.convertDateToBackendDateFormat(new Date(date));
	      $scope.getDataFromBackend('getMasaKontrakCatalogReport', { endDate }).then((response) => {
	        $scope.$apply(() => {
	          $scope.chartData.monitoringMasaKontrakCatalog = response.monitoringMasaKontrakCatalog;
	        });
	      });
	    };
	
	    // others section
	    $scope.getDataFromBackend = (endpoint, param) => {
	      return new Promise((resolve, reject) => {
	        $http
	          .post($scope.backendAddress + '/procurement/dashboard/' + endpoint, param)
	          .success((response) => {
	            resolve(response);
	          })
	          .error((error, status) => {
	            console.log('Info: Comming fromd dashboard filter error');
	            console.log(error);
	            console.log(status);
	          });
	      });
	    };
	
	    $scope.convertDateToBackendDateFormat = (date) => {
	      if (!(date instanceof Date)) return;
	      date = new Date(date);
	      return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
	    };
	
	    $scope.convertToJavascriptDate = (str) => {
	      const arrStr = str.split('/');
	      const newFormat = arrStr[1] + '/' + arrStr[0] + '/' + arrStr[2];
	      return new Date(newFormat);
	    };
	    
	    $scope.getBarangList = function () {
	        vm.loading = true;
	        RequestService.doGET('/master/barang/getBarangListMinimum')
	        .then(function success(data) {
				vm.barangList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
	    }
	
	    $scope.getBarangList();
    
  }
})();
