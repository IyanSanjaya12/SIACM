/**=========================================================
 * FileName		: promise.directive.alphaNumericLimitedSpecialSymbolOnly.js
 * Purpose		: Input text Alpha Numeric Special Limited Symbol
 * Usage        : <input type="text" promise-alphanumericlimitedspecialsymbol-only />
 =========================================================*/
angular.module('naut').directive('promiseAlphanumericlimitedspecialsymbolOnly', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-_\s\\\/ ]/g, '');
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