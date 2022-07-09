/**=========================================================
 * FileName		: promise.directive.TitikGarisMiringStripGarisBawahAngkaHuruf.js
 * Purpose		: Input text Strip Garis Miring Garis Bawah Angka Huruf
 * Usage        : <input type="text" promise-titik-Strip-Comma-Garismiring-Angka-Huruf />
 =========================================================*/
angular.module('naut').directive('promiseTitikGarisMiringStripGarisBawahAngkaHuruf', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z0-9\-\.\/\_ ]/g, '');
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