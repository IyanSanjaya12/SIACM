/**=========================================================
 * FileName		: promise.directive.StripCommaTitik.js
 * Purpose		: Input text Strip Comma Titik
 * Usage        : <input type="text" promise-strip-comma-titik />
 =========================================================*/
angular.module('naut').directive('promiseStripCommaTitik', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\.\,\/\' ]/g, '');
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