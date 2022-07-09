/**=========================================================
 * FileName		: promise.directive.DanKurungTitikGarismiringCommaStripTitikduaAngkaHuruf.js
 * Purpose		: Input text Dan Kurung Titik Garis miring Comma Strip Titik dua Angka Huruf
 * Usage        : <input type="text" promise-organisasi />
 =========================================================*/
angular.module('naut').directive('promiseOrganisasi', ['$filter',
function ($filter) {
        
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\(\) ]/g, '');
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