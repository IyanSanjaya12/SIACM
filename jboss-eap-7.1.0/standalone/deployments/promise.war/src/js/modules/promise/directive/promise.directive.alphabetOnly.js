/**=========================================================
 * FileName		: promise.directive.alphabetOnly.js
 * Purpose		: Input text special symbols 
 * Usage        : <input type="text" promise-alphabet-only />
 =========================================================*/
angular.module('naut').directive('promiseAlphabetOnly', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z ]/g, '');
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
