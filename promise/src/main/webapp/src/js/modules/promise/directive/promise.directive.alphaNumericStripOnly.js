/**=========================================================
 * FileName		: promise.directive.alphaNumericStripOnly.js
 * Purpose		: Input text Alpha Numeric Strip
 * Usage        : <input type="text" promise-alphanumeric-strip-only />
 * Author		: F.H.K
 * Since      	: Apr 21, 2017
 =========================================================*/
angular.module('naut').directive('promiseAlphanumericStripOnly', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\- ]/g, '');
                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                ngModelCtrl.$parsers.push(fromUser);
            }
        };
}])