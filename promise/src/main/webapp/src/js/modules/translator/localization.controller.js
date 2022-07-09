/**=========================================================
 * Module: LocalizationController
 * Demo for locale settings
 =========================================================*/
(function() {
    'use strict';

    angular
        .module('naut')
        .controller('LocalizationController', LocalizationController);
    
    function LocalizationController($rootScope, tmhDynamicLocale, $locale) {
      
      $rootScope.availableLocales = {
        'en': 'English',
        'id': 'Indonesia'};
      
      $rootScope.model = {selectedLocale: 'id'};
      
      $rootScope.$locale = $locale;
      
      $rootScope.changeLocale = tmhDynamicLocale.set;

    }
    LocalizationController.$inject = ['$rootScope', 'tmhDynamicLocale', '$locale'];

})();
