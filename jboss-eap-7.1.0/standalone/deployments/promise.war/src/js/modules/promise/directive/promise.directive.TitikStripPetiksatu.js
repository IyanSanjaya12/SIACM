/**=========================================================
 * FileName		: promise.directive.TitikStripPetiksatu.js
 * Purpose		: Input text Titik Strip Petik Satu
 * Usage        : <input type="text" promise-titik-Strip-Petiksatu />
 =========================================================*/
angular.module('naut').directive('promiseTitikStripPetiksatu', ['$filter',
function ($filter) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                function fromUser(text) {
                    var transformedInput = text.replace(/[^a-zA-Z\-\.\' ]/g, '');
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