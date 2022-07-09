/**=========================================================
 * FileName		: promise.directive.login.js
 * Purpose		: Input text huruf, angka, @, ., -
 * Usage        : <input type="text" promise-peralatan />
 =========================================================*/
angular.module('naut').directive('promisePeralatan', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\(\)\.]/g, '');
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