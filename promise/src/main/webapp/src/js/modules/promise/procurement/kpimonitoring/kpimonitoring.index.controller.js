(function () {
    'use strict';
    angular.module('naut').controller('KPIMonitoringController', KPIMonitoringController);

    function KPIMonitoringController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter, $state, RequestService) {

        $scope.pencarian = {};
        $scope.pencarian.departementId=0;
        $scope.jmlPengadaan = 0;
        /** Pie Chart **/
        $scope.piePercent1 = 0;
        $scope.piePercent2 = 0;
        $scope.pieOptions1 = {
            animate: {
                duration: 700,
                enabled: true
            },
            barColor: colors.byName('success'),
            //trackColor: colors.byName('inverse'),
            scaleColor: false,
            lineWidth: 10,
            lineCap: 'circle'
        };
        $scope.pieOptions2 = {
            animate: {
                duration: 700,
                enabled: true
            },
            barColor: colors.byName('primary'),
            //trackColor: colors.byName('inverse'),
            scaleColor: false,
            lineWidth: 10,
            lineCap: 'circle'
        };

        $scope.chartBarStackedFlotChart = flotOptions['bar-stacked'];
        /** pie chart end **/

        /** datepicker **/
        $scope.openDateOne = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.openedDateOne = true;
        };

        $scope.openDateTwo = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.openedDateTwo = true;
        };

        //set format datepicker:  
        $scope.formats = ['yyyy', 'MMMM', 'dd/MM/yyyy', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[2];
        $scope.today = new Date();
        /** datepicker end. **/

        /** load afco/departement **/
        RequestService.doGET("/procurement/master/organisasi/get-list-by-parent-id/1")
            .then(function (data) {
                if (data.length > 0) {
                    $scope.departementList = data;
                }
            });

        /** load procurement report **/
        $scope.loadAll = function (pencarian) {

            var post = {};
            if (pencarian.departementId != undefined) {
                post.departementId = pencarian.departementId;
            }
            if (pencarian.dateOne != undefined) {
                post.dateOne = pencarian.dateOne;
            }
            if (pencarian.dateTwo != undefined) {
                post.dateTwo = pencarian.dateTwo;
            }

            /** load kpi procurement report **/
            $scope.procurementDS = {
                data: []
            };
            
            RequestService.requestServer("/procurement/master/kpiMonitorServices/getKpiProcurementReport", post)
                .then(function (data) {
                	 $scope.jmlPengadaan = 0;
                    if (data.length > 0) {
                        $scope.procurementreport = data;
                        angular.forEach(data, function (value, index) {
                        	$scope.jmlPengadaan=$scope.jmlPengadaan+value.total;
                            $scope.procurementDS.data.push({
                                "label": "" + value.departementName,
                                "value": "" + value.total
                            });
                        })

                    }

                    /** load anggaran KPI **/
                    RequestService.requestServer("/procurement/master/kpiMonitorServices/getKPIMonitoringAnggaran01", post)
                        .then(function (data) {
                            if (data.length > 0) {
                                $scope.plannedBudget = data[0].totalAnggaran;
                                $scope.availableBudget = data[0].availableAnggaran;
                                $scope.usedBudget = data[0].usedAnggaran;
                                $scope.bookedBudget = data[0].bookedAnggaran;
                                if($scope.plannedBudget==null){
                                	$scope.plannedBudget = 0;
                                }
                                if($scope.availableBudget==null){
                                	$scope.availableBudget = 0;
                                }
                                if($scope.usedBudget==null){
                                	$scope.usedBudget = 0;
                                }
                                if($scope.bookedBudget==null){
                                	$scope.bookedBudget = 0;
                                }
                            } else {
                                $scope.plannedBudget = 0;
                                $scope.availableBudget = 0;
                                $scope.usedBudget = 0;
                                $scope.bookedBudget = 0;
                                console.log($scope.plannedBudget);
                            }
                        });

                    RequestService.requestServer("/procurement/master/kpiMonitorServices/getKpiProcurementSaving", post)
                        .then(function (data) {
                        	
                        	$scope.piePercent1 = 0;
                            if (data.length > 0) {
                            	
                            	if(data[0].jmlHPS==null){
                            		$scope.jmlHPS=0;
                            
                            	}else{
                            		$scope.jmlHPS = data[0].jmlHPS;
                            		
                            	}
                            	
                            	if(data[0].jmlPerolehan==null){
                            		$scope.jmlPerolehan=0;	
                            	}else{
                            		$scope.jmlPerolehan = data[0].jmlPerolehan;
                            	}
         
                                if($scope.jmlHPS==0) {
                                	$scope.piePercent1 = 0;
                                } else {
                                    $scope.piePercent1 = Math.ceil((($scope.jmlHPS - $scope.jmlPerolehan) / $scope.jmlHPS) * 100);
                                }
                                console.log($scope.piePercent1);
                                
                            } else {
                                $scope.jmlPengadaan = 0;
                                $scope.jmlHPS = 0;
                                $scope.jmlPerolehan = 0;
                            }
                        });
                    
                    
                    RequestService.doGET('/procurement/master/SLAServices/getSlaJumlahHari').then(function(data) {
                    	$scope.slaJumlahHari = data;
                        
                    	RequestService.requestServer("/procurement/master/kpiMonitorServices/getKPIMonitoringLeadTime", post)
                        .then(function (data) {
                            if (data.length > 0) {
                                $scope.jumlahHari = data[0].jumlahHari;
                                
                                if ($scope.jumlahHari == 0) {
                                	$scope.slaJumlahHari = 0;
                                	$scope.piePercent2 = 0;
                                } else {
                                   // $scope.piePercent2 = Math.ceil(( $scope.jumlahHari / $scope.slaJumlahHari) * 100);
                                	$scope.piePercent2 = Math.ceil(0);
                                }

                            } else {
                                $scope.jumlahHari = 0;
                            }
                        });
                    	
            		})

  
                    
                });
        }

        /** load all data **/
        $scope.initData = function () {
            $scope.loadAll(0);
        }

        $scope.initData();

        /** function for search **/
        $scope.getSearchAndSort = function (pencarian) {
            $scope.loadAll(pencarian);
        }

    }
    KPIMonitoringController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter', '$state', 'RequestService'];
})();