/**=========================================================
 * Module: DashboardOrderController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DashboardOrderController', DashboardOrderController);

    DashboardOrderController.$inject = ['$scope', 'colors', 'flotOptions', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter','$q','$state'];

    function DashboardOrderController($scope, colors, flotOptions, $timeout, $http, $rootScope, ngTableParams, $location, $filter,$q, $state) {

        var dash = this;
       
        $scope.dataset = [];
        $scope.categories = [{
                                    "category": []
        }];
        
        
        
        $scope.attrs = {            
            "caption": "Vendor Report",
            "subCaption": "",
            "xAxisname": "Bulan",
            "yAxisName": "Jumlah Vendor/Calon Vendor",
            "plotFillAlpha": "80",
            "paletteColors": "#8d1c9e,#6639b6",
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
                        $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countByMonth')
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
                 $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countByYear')
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
                        $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countByWeek')
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
        
        /*$scope.goTo = function(status) {
			$state.go('app.promise.procurement-vendor-approval', {
				status : status
			});
		}*/
        
        $scope.goTo = function() {
			$state.go('app.promise.procurement-approval');
		}
        
        $scope.goToAfcoApprovalList = function() {
			$state.go('app.promise.procurement-vendor-approval');
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
        
        
        
        $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countBlacklistVendor')
                .success(function (data, status, headers, config) {
                    $scope.countBlacklistVendor  = data;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
        
         /*$http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countApprovalVendor')
                .success(function (data, status, headers, config) {
                    $scope.countApprovalBlacklist  = data.blacklist;
                    $scope.countApprovalUnblacklist  = data.unblacklist;
                    $scope.countApprovalSeleksiCalonVendor  = data.calonPerAfco;
                    $scope.countApprovalPerpanjanganTDR  = data.perpanjangan;
             
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });*/
         
         $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countApprovalByUser')
         .success(function (data, status, headers, config) {
        	 $scope.countApprovalBlacklist  = data.resultBlacklistVendor;
             $scope.countApprovalUnblacklist  = data.resultUnblacklistVendor;
             $scope.countApprovalSeleksiCalonVendor  = data.resultRegVendor;
             $scope.countApprovalPerpanjanganTDR  = data.resultPerpanjanganTdrVendor;
      
         })
         .error(function (dataTemp, status, headers, config) {
             console.error('Connection Failed');
             
         });
        
        $http.get($rootScope.backendAddress + '/procurement/DashboardOrderServices/countVendor')
                .success(function (data, status, headers, config) {
                	
                    	$http.get(
                               /* $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorListByStatusApproal', {*/
                    			 $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorListByStatusApproalAndLevelOrganisasi', {
                                    ignoreLoadingBar: true
                                }).success(function(data, status, headers, config) {
                                	$scope.vendorList = data;
                            })
                	
                    $scope.countVendor  = data.vendor;
                    $scope.countCalonVendor  = data.calon;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
        
    }
})();
