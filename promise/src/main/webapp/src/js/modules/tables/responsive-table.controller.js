/**=========================================================
 * Module: DemoResponsiveTableController.js
 * Controller for responsive tables components
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('ResponsiveTableController', ResponsiveTableController);
        
    function ResponsiveTableController($scope, colors) {

      $scope.sparkOps1 = {
        barColor: colors.byName('primary')
      };
      $scope.sparkOps2 = {
        barColor: colors.byName('info')
      };
      $scope.sparkOps3 = {
        barColor: colors.byName('amber')
      };

      $scope.sparkData1 = [1,2,3,4,5,6,7,8,9];
      $scope.sparkData2 = [1,2,3,4,5,6,7,8,9];
      $scope.sparkData3 = [1,2,3,4,5,6,7,8,9];
    }
    ResponsiveTableController.$inject = ['$scope', 'colors'];

})();
