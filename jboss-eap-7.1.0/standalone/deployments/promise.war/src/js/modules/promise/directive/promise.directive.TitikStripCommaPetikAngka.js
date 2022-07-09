/**=========================================================
 * FileName		: promise.directive.TitikStripCommaPetikAngka.js
 * Purpose		: Input text Titik Strip Koma Petik Angka
 * Usage        : <input type="text" promise-titik-Strip-Comma-Petik-Angka />
 =========================================================*/
angular.module('naut').directive('promiseTitikStripCommaPetikAngka', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\.\,\"\' ]/g, '');
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