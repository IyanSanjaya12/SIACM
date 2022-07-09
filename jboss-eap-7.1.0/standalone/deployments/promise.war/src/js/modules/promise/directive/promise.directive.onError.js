/**=========================================================
 * FileName		: promise.directive.onError.js
 * Purpose		: Image not found
 * Usage        : <on-error- />
 =========================================================*/
angular.module('naut').directive('onError', ['$rootScope', 
function ($rootScope) {
	return {
	    restrict:'A',
	    link: function(scope, element, attr) {
	      element.on('error', function() {
	        element.attr('src', attr.onError);
	      })
	    }
	}
}])