/**=========================================================
 * FileName		: promise.directive.viewDataPengadaan.js
 * Purpose		: Format text into currenncy on blur
 * Usage        : <input type="text" promise-float-blur />
 * Author		: Reinhard
 * Since      	: Sep 18, 2015
 =========================================================*/

	angular.module('naut').directive('promiseFloatBlur', [ '$filter',

	function ($filter) {
		return {
			require : '^ngModel',
			scope : true,
			link : function(scope, el, attrs, ngModelCtrl) {

				function formatter(value) {
					
					if (isNaN(value))
					{
					  value = 0;
					}
					
					value = parseFloat(value.toString().replace(/[^0-9._-]/g, ''));
					/*val, sign, number of fraction*/
					
					if (isNaN(value))
					{
					  value = 0;
					}
					
					var formattedValue = $filter('number')(value, 2); 
					el.val(formattedValue);
					return formattedValue;
				}

				ngModelCtrl.$formatters.push(formatter);
		
				el.bind('focus', function() {
					value = parseFloat(el.val().replace(/[^0-9._-]/g, ''));
					el.val(value);
				});
				
	            el.bind('keydown', function (event) {
	            	/*console.log(event);*/
	            	var validCondition = 
	            		(event.keyCode == 46) ||  /*DEL*/
	            		(event.keyCode == 37) || 
	            		(event.keyCode == 39) ||  /*Left & Right Arrow*/
	            		(event.keyCode == 8)  ||  /*BackSpace*/
	            		(event.keyCode == 9)  ||  /*Tab*/
	            		(event.keyCode == 48) || (event.keyCode == 96) || /*0*/
	            		(event.keyCode == 49) || (event.keyCode == 97) || /*1*/
	            		(event.keyCode == 50) || (event.keyCode == 98) || /*2*/
	            		(event.keyCode == 51) || (event.keyCode == 99) || /*3*/
	            		(event.keyCode == 52) || (event.keyCode == 100) || /*4*/
	            		(event.keyCode == 53) || (event.keyCode == 101) || /*5*/
	            		(event.keyCode == 54) || (event.keyCode == 102) || /*6*/
	            		(event.keyCode == 55) || (event.keyCode == 103) || /*7*/
	            		(event.keyCode == 56) || (event.keyCode == 104) || /*8*/
	            		(event.keyCode == 57) || (event.keyCode == 105) || /*9*/
	            	    (event.keyCode == 190) ;  /*.*/
	            		
	            	if (validCondition) {
	                    return true;
	                } else {
	                    event.preventDefault();
	                    return false;
	                }
	            });
				
			
				el.bind('blur', function() {
					formatter(el.val());
				});
			}
		};
	}])



