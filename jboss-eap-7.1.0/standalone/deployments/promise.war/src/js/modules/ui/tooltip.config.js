/**=========================================================
 * Module: TooltipConfig.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .config(tooltipConfig);
    
    function tooltipConfig($tooltipProvider) {

      $tooltipProvider.options({
        appendToBody: true
      });

    }
    tooltipConfig.$inject = ['$tooltipProvider'];

})();
