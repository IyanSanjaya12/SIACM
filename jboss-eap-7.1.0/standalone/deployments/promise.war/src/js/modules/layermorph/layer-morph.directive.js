/**=========================================================
 * Module: LayerMorphDirective.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .directive('btnLayerMorph', btnLayerMorph)
        .directive('layerMorphOverlay', layerMorphOverlay)
        .directive('layerMorphClose', layerMorphClose);
    
    function btnLayerMorph(LayerMorph) {

      return {
        restrict: 'A',
        link: function(scope, element, attrs) {
          var queryResult = document.querySelector(attrs.target);
          var target = angular.element(queryResult);
          
          if(!target.length) {
            console.log('LayerMorph: Wrong target ' + attrs.target);
            return;
          }

          element.on('click', function() {
            LayerMorph.open( element, target );
          });

        }
      };
    }
    btnLayerMorph.$inject = ['LayerMorph'];
    
    function layerMorphOverlay(LayerMorph, $document) {

      return {
        restrict: 'C',
        link: function(scope, element, attrs) {
          $document.ready(function(){
            LayerMorph.init();
          });
        }
      };
    }
    layerMorphOverlay.$inject = ['LayerMorph', '$document'];

    
    function layerMorphClose(LayerMorph) {

      return {
        restrict: 'A',
        link: function(scope, element, attrs) {
          element.on('click', function(){
            LayerMorph.close();
          });
        }
      };
    }
    layerMorphClose.$inject = ['LayerMorph'];

})();
