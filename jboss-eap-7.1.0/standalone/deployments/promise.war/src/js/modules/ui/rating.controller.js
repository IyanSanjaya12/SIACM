/**=========================================================
 * Module: DemoRatingDemoController.js
 * Provides a demo for ratings UI
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('RatingDemoController', RatingDemoController);
    
    function RatingDemoController($scope) {

      $scope.rate = 7;
      $scope.max = 10;
      $scope.isReadonly = false;

      $scope.hoveringOver = function(value) {
        $scope.overStar = value;
        $scope.percent = 100 * (value / $scope.max);
      };

      $scope.ratingStates = [
        {stateOn: 'fa fa-check', stateOff: 'fa fa-check-circle'},
        {stateOn: 'fa fa-star', stateOff: 'fa fa-star-o'},
        {stateOn: 'fa fa-heart', stateOff: 'fa fa-ban'},
        {stateOn: 'fa fa-heart'},
        {stateOff: 'fa fa-power-off'}
      ];
    }
    RatingDemoController.$inject = ['$scope'];

})();
