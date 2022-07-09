/**=========================================================
 * Module: DashboardAnggaranController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DashboardAnggaranController', DashboardAnggaranController);

    DashboardAnggaranController.$inject = ['$scope', 'colors', '$timeout', '$http', '$rootScope', 'ngTableParams', '$location', '$filter','RequestService'];

    function DashboardAnggaranController($scope, colors, $timeout, $http, $rootScope, ngTableParams, $location, $filter,RequestService) {
        
         $http.get($rootScope.backendAddress + '/procurement/DashboardAnggaranServices/getAllAnggaran')
                .success(function (data, status, headers, config) {
                    $scope.plannedBudget = data.plannedBudget;
                    $scope.availableBudget = data.availableBudget;
                    $scope.usedBudget = data.usedBudget;
                    $scope.bookedBudget = data.bookedBudget;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/countByUserGroupAndActive')
                .success(function (data, status, headers, config) {
                    $scope.countApproval = data.count;
                })
                .error(function (dataTemp, status, headers, config) {
                    console.error('Connection Failed');
                    
                });
        $scope.dataset = [];
        $scope.categories = [{
                                    "category": []}];
         $scope.attrs = {            
            "caption": "Dashboard Alokasi Anggaran",
            "subCaption": "",
            "xAxisname": "Tahun",
            "yAxisName": "Alokasi Anggaran (In IDR)",
            "numberPrefix": "Rp ",
            "plotFillAlpha": "80",
            "paletteColors": "#4caf50,#00a65a,#fe9700,#f34235",
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
            "valueFontColor": "#000",
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
            "numberScaleValue": "1000,1000,1000,1000",
            "numberScaleUnit": "R,J,M,T"
        };
         
         $scope.showGraph = function(choice){
             if (choice=='bulan'){
                         $http.get($rootScope.backendAddress + '/procurement/DashboardAnggaranServices/getAnggaranByMonth')
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
                                 dataValue.push({"value":value[0]});
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
                  $http.get($rootScope.backendAddress + '/procurement/DashboardAnggaranServices/getAnggaranByYear')
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
                 $http.get($rootScope.backendAddress + '/procurement/DashboardAnggaranServices/getAnggaranByWeek')
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
                                    dataValue.push({"value":value[0]});
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
         
         
        $scope.go = function ( path ) {
          $location.path( path );
        };
        
        $scope.showGraph('tahun');
        
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
        
    }
})();
