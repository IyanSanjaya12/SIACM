/**=========================================================
 * Module: nestable.js
 * Nestable controller
 =========================================================*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('NestableController', NestableController);
    
    function NestableController($scope) {

      $scope.myNestable = {};
      $scope.myNestable2 = {};

      $scope.myNestable.onchange = function() {
        console.log('Nestable changed..');
      };


      $scope.myNestable2.onchange = function() {
        $scope.serialized = $scope.myNestable2.serialize();
      };

    }
    NestableController.$inject = ['$scope'];

})();
