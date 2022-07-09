/**=========================================================
 * Module: PieChartController.js
 =========================================================*/
/*jshint -W069*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PieChartController', PieChartController);
    
    PieChartController.$inject = ['$scope', 'colors'];
    function PieChartController($scope, colors) {

      // Easy Pie Charts
      // ----------------------------------- 


      $scope.piePercent1 = 75;
      $scope.piePercent2 = 50;
      $scope.piePercent3 = 10;
      $scope.piePercent4 = 95;

      $scope.pieOptions = {
          animate:{
              duration: 700,
              enabled: true
          },
          barColor: colors.byName('info'),
          // trackColor: colors.byName('inverse'),
          scaleColor: false,
          lineWidth: 10,
          lineCap: 'circle'
      };

      $scope.pieOptions1 = {
          animate:{
              duration: 700,
              enabled: true
          },
          barColor: colors.byName('info'),
          // trackColor: colors.byName('inverse'),
          scaleColor: false,
          lineWidth: 4,
          lineCap: 'circle'
      };

      $scope.pieOptions2 = {
          animate:{
              duration: 700,
              enabled: true
          },
          barColor: colors.byName('purple'),
          trackColor: false,
          scaleColor: colors.byName('gray'),
          lineWidth: 15,
          lineCap: 'circle'
      };

      $scope.randomize = function(type) {
        if ( type === 'easy') {
          $scope.piePercent1 = random();
          $scope.piePercent2 = random();
          $scope.piePercent3 = random();
          $scope.piePercent4 = random();
        }
        if ( type === 'knob') {
          $scope.knobLoaderData1 = random();
          $scope.knobLoaderData2 = random();
          $scope.knobLoaderData3 = random();
          $scope.knobLoaderData4 = random();
        }
      }

      function random() { return Math.floor((Math.random() * 100) + 1); }

      // KNOB Charts
      // ----------------------------------- 

      $scope.knobLoaderData1 = 100;
      $scope.knobLoaderOptions1 = {
          width: '50%', // responsive
          displayInput: true,
          fgColor: colors.byName('primary')
        };

      $scope.knobLoaderData2 = 50;
      $scope.knobLoaderOptions2 = {
          width: '50%', // responsive
          displayInput: true,
          fgColor: colors.byName('success'),
          readOnly : true,
          lineCap : 'round'
        };

      $scope.knobLoaderData3 = 37;
      $scope.knobLoaderOptions3 = {
          width: '50%', // responsive
          displayInput: true,
          fgColor: colors.byName('purple'),
          displayPrevious : true,
          thickness : 0.1
        };

      $scope.knobLoaderData4 = 60;
      $scope.knobLoaderOptions4 = {
          width: '50%', // responsive
          displayInput: true,
          fgColor: colors.byName('danger'),
          bgColor: colors.byName('warning'),
          angleOffset: -125,
          angleArc: 250
        };

    }

})();
