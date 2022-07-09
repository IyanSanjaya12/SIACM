/**=========================================================
 * FileName		: promise.directive.onErrorSrc.js
 * Purpose		: Image not found
 * Usage        : <on-error-src />
 =========================================================*/
angular.module('naut').directive('onErrorSrc', ['$rootScope', 
function ($rootScope) {
	return {
	    link: function(scope, element, attrs) {
	      element.bind('error', function() {
	        if (attrs.src != attrs.onErrorSrc) {
	          attrs.$set('src', attrs.onErrorSrc);
	        }
	      });
	    }
	}
}])