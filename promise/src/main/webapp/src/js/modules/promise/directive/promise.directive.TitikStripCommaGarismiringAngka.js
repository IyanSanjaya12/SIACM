/**=========================================================
 * FileName		: promise.directive.TitikStripCommaGarismiringAngka.js
 * Purpose		: Input text Titik Strip Comma Garis Miring Angka
 * Usage        : <input type="text" promise-titik-Strip-Comma-Garismiring-Angka />
 =========================================================*/
angular.module('naut').directive('promiseTitikStripCommaGarismiringAngka', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\.\,\/ ]/g, '');
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