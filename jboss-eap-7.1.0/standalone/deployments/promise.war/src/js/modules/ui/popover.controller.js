/**=========================================================
 * Module: DemoPopoverController.js
 * Provides a simple demo for popovers
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('PopoverController', PopoverController);
    
    function PopoverController($scope) {

      $scope.dynamicPopover = 'Hello, World!';
      $scope.dynamicPopoverTitle = 'Title';

    }
    PopoverController.$inject = ['$scope'];
})();
