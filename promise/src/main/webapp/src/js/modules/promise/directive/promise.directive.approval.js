/**=========================================================
 * FileName		: promise.directive.approval.js
 * Purpose		: menandakan data yang berbeda antara draft dan main data
 * Usage        : <input type="text" ng-to-yellow />
 =========================================================*/

angular.module('naut').directive('ngToYellow', ['$filter',
function ($filter) {
      
	  return {
		  restrict: 'AE',
	        replace: true,
	        link: function(scope, elem, attrs) {
	        	elem.css("background-color", "yellow");
	        	elem.css("font-size","12px");
	        	
          }
      };
      
}])

