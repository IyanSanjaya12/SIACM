(function() {
    'use strict';

    angular
        .module('naut')
        .filter('shortnumber', shortnumber);

    function shortnumber() {
        return filterFilter;

        ////////////////
        function filterFilter(number) {
          if ( number ) {
            var abs = Math.abs( number );
            if ( abs >= Math.pow( 10, 12 ) ) {
            // trillion
            number = ( number / Math.pow( 10, 12 ) ).toFixed( 1 ) + " T";
            } else if ( abs < Math.pow( 10, 12 ) && abs >= Math.pow( 10, 9 ) ) {
            // billion
            number = ( number / Math.pow( 10, 9 ) ).toFixed( 1 ) + " M";
            } else if ( abs < Math.pow( 10, 9 ) && abs >= Math.pow( 10, 6 ) ) {
            // million
            number = ( number / Math.pow( 10, 6 ) ).toFixed( 1 ) + " J";
            } else if ( abs < Math.pow( 10, 6 ) && abs >= Math.pow( 10, 3 ) ) {
            // thousand
            number = ( number / Math.pow( 10, 3 ) ).toFixed( 1 ) + " R";
            }
            return number;
            }
        }
    }

})();