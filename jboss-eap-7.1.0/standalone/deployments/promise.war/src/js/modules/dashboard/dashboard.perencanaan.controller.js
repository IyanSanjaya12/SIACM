/**=========================================================
 * Module: DashboardPerencanaanController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DashboardPerencanaanController', DashboardPerencanaanController);

    DashboardPerencanaanController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter','$q','$state'];

    function DashboardPerencanaanController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter,$q, $state) {
        var dash = this;
       
        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/countByUserGroupAndActive')
                .success(function (data, status, headers, config) {
                    $scope.countApproval = data.count;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
        
        $scope.dataset = [];
        $scope.categories = [{
                                    "category": []
        }];
        $scope.attrs = {            
            "caption": "Dashboard Perencanaan",
            "subCaption": "",
            "xAxisname": "Bulan",
            "yAxisName": "Jumlah PR/PO",
            "plotFillAlpha": "80",
            "paletteColors": "#23b7e5,#fe9700,#a0aab2,#ffc107,#3f51b5",
            "baseFontColor": "#333333",
            "baseFont": "Helvetica Neue,Arial",
            "captionFontSize": "14",
            "subcaptionFontSize": "14",
            "subcaptionFontBold": "0",
            "showBorder": "0",
            "bgColor": "#ffffff",
            "showShadow": "0",
            "canvasBgColor": "#ffffff",
            "canvasBorderAlpha": "0",
            "divlineAlpha": "100",
            "divlineColor": "#999999",
            "divlineThickness": "1",
            "divLineIsDashed": "1",
            "divLineDashLen": "1",
            "divLineGapLen": "1",
            "usePlotGradientColor": "0",
            "showplotborder": "0",
            "valueFontColor": "#ffffff",
            "placeValuesInside": "1",
            "showHoverEffect": "1",
            "rotateValues": "1",
            "showXAxisLine": "1",
            "xAxisLineThickness": "1",
            "xAxisLineColor": "#999999",
            "showAlternateHGridColor": "0",
            "legendBgAlpha": "0",
            "legendBorderAlpha": "0",
            "legendShadow": "0",
            "legendItemFontSize": "10",
            "legendItemFontColor": "#666666",
            "labelDisplay":"rotate",
            slantLabels:1
        };

        $scope.showGraph = function(choice){
                    if (choice=='bulan'){
                        $http.get($rootScope.backendAddress + '/procurement/DashboardPerencanaanServices/countByMonth')
                .success(function (data, status, headers, config) {
                    
                    $scope.attrs.xAxisname =  "Bulan";
                    $scope.categories[0].category = [];
                    for (var tahun = data.minyear; tahun<=data.maxyear;tahun++){
                        for(var month =1;month<=12;month++ ){
                            if ((tahun==data.minyear && month<data.minmonth) || (tahun==data.maxyear && month>data.maxmonth)){continue;}
                                
                            var monthNames= ["-","Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"];
                            $scope.categories[0].category.push({"label":monthNames[month]+"-"+tahun});
                        }
                    }
                             
            
                    $scope.dataset = [];
                    angular.forEach(data.data, function(value, key) {
                        
                        var dataValue = [];
                        angular.forEach(value.data, function(value, key) {
                                dataValue.push({"value":value[1]});
                        });
                        var datasetValue = {
                                "seriesname": value.label,
                                "data": dataValue
                        };
                        $scope.dataset.push(datasetValue);
                    });
            
                
        
                   
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
                    }
            
            else if(choice=='tahun'){
                 $http.get($rootScope.backendAddress + '/procurement/DashboardPerencanaanServices/countByYear')
                .success(function (data, status, headers, config) {
                   
                    $scope.attrs.xAxisname =  "Tahun";
                    $scope.categories[0].category = [];
                    for (var tahun = data.minyear; tahun<=data.maxyear;tahun++){
                        $scope.categories[0].category.push({"label":''+tahun});
                    }
                             
            
                    $scope.dataset = [];
                    angular.forEach(data.data, function(value, key) {
                        
                        var dataValue = [];
                        angular.forEach(value.data, function(value, key) {
                                dataValue.push({"value":value[1]});
                        });
                        var datasetValue = {
                                "seriesname": value.label,
                                "data": dataValue
                        };
                        $scope.dataset.push(datasetValue);
                    });       
            
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
            }
            else if (choice=='minggu'){
                        $http.get($rootScope.backendAddress + '/procurement/DashboardPerencanaanServices/countByWeek')
                .success(function (data, status, headers, config) {
                   
                    $scope.attrs.xAxisname =  "Minggu";
                    $scope.categories[0].category = [];
                    var times = data.times;
                    angular.forEach(times, function(time, key) {
                        $scope.categories[0].category.push({"label":new Date(time).toDateString()});
                    });
                             
            
                    $scope.dataset = [];
                    angular.forEach(data.data, function(value, key) {
                        
                        var dataValue = [];
                        angular.forEach(value.data, function(value, key) {
                                dataValue.push({"value":value[1]});
                        });
                        var datasetValue = {
                                "seriesname": value.label,
                                "data": dataValue
                        };
                        $scope.dataset.push(datasetValue);
                    });
            
                
        
                   
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
                    }
        };
        
        
        $scope.showGraph('bulan');
        
        $scope.go = function ( path ) {
          $location.path( path );
        };
        
        $scope.goTo = function(status) {
			$state.go('app.promise.procurement-purchaserequest', {
				status : status
			});
		}
        
        $scope.changeGraph = function (rangetime){
            if(rangetime=='tahun'){
               $scope.showGraph('tahun');
            }
            else if(rangetime=='bulan'){
                $scope.showGraph('bulan');
            }
            else if(rangetime=='minggu'){
                $scope.showGraph('minggu');
            }
        }
        
        $scope.countByStatus = function(status) {
			return $q(function(resolve, reject) {
				$http.get($rootScope.backendAddress + '/procurement/purchaseRequestServices/countByStatus/' + status).success(function(data) {
					resolve(data);
				}).error(function(data) {
					reject(0);
				});
			});

		}
		
		
		var promiseCountOnNeedVerification = $scope.countByStatus(8); // Need Verification
		promiseCountOnNeedVerification.then(function(resolve) {
			dash.needVerification = resolve;
		}, function(reject) {
			dash.needVerification = reject;
		})
		
		
		var promiseCountOnProcess = $scope.countByStatus(5); // On Process
		promiseCountOnProcess.then(function(resolve) {
			dash.prOnProgress = resolve;
		}, function(reject) {
			dash.prOnProgress = reject;
		})

		var promiseCountReceive = $scope.countByStatus(6); // Received
		promiseCountReceive.then(function(resolve) {
			dash.prReceived = resolve;
		}, function(reject) {
			dash.prReceived = reject;
		})

		var promiseCountApprovalProcess = $scope.countByStatus(3); // Approval
		// Process
		promiseCountApprovalProcess.then(function(resolve) {
			dash.prApprovalProcess = resolve;
		}, function(reject) {
			dash.prApprovalProcess = reject;
		})

		var promiseCountVerified = $scope.countByStatus(7); // Procurement Process
		promiseCountVerified.then(function(resolve) {
			dash.prProcurementProcess = resolve;
		}, function(reject) {
			dash.prProcurementProcess = reject;
		})

    }
})();
