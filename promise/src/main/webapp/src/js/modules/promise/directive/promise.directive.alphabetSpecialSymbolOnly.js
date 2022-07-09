/**=========================================================
 * FileName		: promise.directive.alphabetSpecialSymbolOnly.js
 * Purpose		: Input text special symbols 
 * Usage        : <input type="text" promise-special-char />
 * Author		: Hariri
 * Since      	: Nov 24, 2015
 =========================================================*/
angular.module('naut').directive('promiseSpecialChar', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z, .'-]/g, '');
                    //console.log(transformedInput);
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
