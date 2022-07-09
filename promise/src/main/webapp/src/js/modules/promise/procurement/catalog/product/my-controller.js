'use strict';

/* Controllers */

angular.module('naut').controller('MyCarousel', ['$scope', function($scope) {
    $scope.myInterval = 5000;
    var slides = $scope.slides = [];
    var slideLength = (slides.length)+1;
    $scope.addSlide = function() {
        slides.push({
            image: 'app/img/banner/bg-banner-' + slideLength + '.jpg',
            text: ['Carousel text #0','Carousel text #1','Carousel text #2','Carousel text #3'][slides.length % 4]
        });
    };
    for (var i=1; i<3; i++) {
        $scope.addSlide();
    }
}]);

angular.module('naut').controller('HeaderPageController', ['$scope', '$translate', '$state', function($scope, $translate, $state) {
    $scope.openMenu = function(div) {
        var countShow = $('.pop-up-item[style="display: block;"]').length;
        $('.pop-up-item').not('[id="'+div+'"]').removeAttr('style');
        $('#'+div).toggle(500);
    };
    
    $scope.country = {};
      $scope.countries = [ // Taken from https://gist.github.com/unceus/6501985
        {name: 'Afghanistan', code: 'AF'},
        {name: 'Åland Islands', code: 'AX'},
        {name: 'Albania', code: 'AL'},
        {name: 'Algeria', code: 'DZ'},
        {name: 'American Samoa', code: 'AS'},
        {name: 'Andorra', code: 'AD'},
        {name: 'Angola', code: 'AO'},
        {name: 'Anguilla', code: 'AI'},
        {name: 'Antarctica', code: 'AQ'},
        {name: 'Antigua and Barbuda', code: 'AG'},
        {name: 'Argentina', code: 'AR'},
        {name: 'Armenia', code: 'AM'},
        {name: 'Aruba', code: 'AW'},
        {name: 'Australia', code: 'AU'},
        {name: 'Austria', code: 'AT'},
        {name: 'Azerbaijan', code: 'AZ'},
        {name: 'Bahamas', code: 'BS'},
        {name: 'Bahrain', code: 'BH'},
        {name: 'Bangladesh', code: 'BD'},
        {name: 'Barbados', code: 'BB'},
        {name: 'Belarus', code: 'BY'},
        {name: 'Belgium', code: 'BE'},
        {name: 'Belize', code: 'BZ'},
        {name: 'Benin', code: 'BJ'},
        {name: 'Bermuda', code: 'BM'},
        {name: 'Bhutan', code: 'BT'},
        {name: 'Bolivia', code: 'BO'},
        {name: 'Bosnia and Herzegovina', code: 'BA'},
        {name: 'Botswana', code: 'BW'},
        {name: 'Bouvet Island', code: 'BV'},
        {name: 'Brazil', code: 'BR'},
        {name: 'British Indian Ocean Territory', code: 'IO'},
        {name: 'Brunei Darussalam', code: 'BN'},
        {name: 'Bulgaria', code: 'BG'},
        {name: 'Burkina Faso', code: 'BF'},
        {name: 'Burundi', code: 'BI'},
        {name: 'Cambodia', code: 'KH'},
        {name: 'Cameroon', code: 'CM'},
        {name: 'Canada', code: 'CA'},
        {name: 'Cape Verde', code: 'CV'},
        {name: 'Cayman Islands', code: 'KY'},
        {name: 'Central African Republic', code: 'CF'},
        {name: 'Chad', code: 'TD'},
        {name: 'Chile', code: 'CL'},
        {name: 'China', code: 'CN'},
        {name: 'Christmas Island', code: 'CX'},
        {name: 'Cocos (Keeling) Islands', code: 'CC'},
        {name: 'Colombia', code: 'CO'},
        {name: 'Comoros', code: 'KM'},
        {name: 'Congo', code: 'CG'},
        {name: 'Congo, The Democratic Republic of the', code: 'CD'},
        {name: 'Cook Islands', code: 'CK'},
        {name: 'Costa Rica', code: 'CR'},
        {name: 'Cote D\'Ivoire', code: 'CI'},
        {name: 'Croatia', code: 'HR'},
        {name: 'Cuba', code: 'CU'},
        {name: 'Cyprus', code: 'CY'},
        {name: 'Czech Republic', code: 'CZ'},
        {name: 'Denmark', code: 'DK'},
        {name: 'Djibouti', code: 'DJ'},
        {name: 'Dominica', code: 'DM'},
        {name: 'Dominican Republic', code: 'DO'},
        {name: 'Ecuador', code: 'EC'},
        {name: 'Egypt', code: 'EG'},
        {name: 'El Salvador', code: 'SV'},
        {name: 'Equatorial Guinea', code: 'GQ'},
        {name: 'Eritrea', code: 'ER'},
        {name: 'Estonia', code: 'EE'},
        {name: 'Ethiopia', code: 'ET'},
        {name: 'Falkland Islands (Malvinas)', code: 'FK'},
        {name: 'Faroe Islands', code: 'FO'},
        {name: 'Fiji', code: 'FJ'},
        {name: 'Finland', code: 'FI'},
        {name: 'France', code: 'FR'},
        {name: 'French Guiana', code: 'GF'},
        {name: 'French Polynesia', code: 'PF'},
        {name: 'French Southern Territories', code: 'TF'},
        {name: 'Gabon', code: 'GA'},
        {name: 'Gambia', code: 'GM'},
        {name: 'Georgia', code: 'GE'},
        {name: 'Germany', code: 'DE'},
        {name: 'Ghana', code: 'GH'},
        {name: 'Gibraltar', code: 'GI'},
        {name: 'Greece', code: 'GR'},
        {name: 'Greenland', code: 'GL'},
        {name: 'Grenada', code: 'GD'},
        {name: 'Guadeloupe', code: 'GP'},
        {name: 'Guam', code: 'GU'},
        {name: 'Guatemala', code: 'GT'},
        {name: 'Guernsey', code: 'GG'},
        {name: 'Guinea', code: 'GN'},
        {name: 'Guinea-Bissau', code: 'GW'},
        {name: 'Guyana', code: 'GY'},
        {name: 'Haiti', code: 'HT'},
        {name: 'Heard Island and Mcdonald Islands', code: 'HM'},
        {name: 'Holy See (Vatican City State)', code: 'VA'},
        {name: 'Honduras', code: 'HN'},
        {name: 'Hong Kong', code: 'HK'},
        {name: 'Hungary', code: 'HU'},
        {name: 'Iceland', code: 'IS'},
        {name: 'India', code: 'IN'},
        {name: 'Indonesia', code: 'ID'},
        {name: 'Iran, Islamic Republic Of', code: 'IR'},
        {name: 'Iraq', code: 'IQ'},
        {name: 'Ireland', code: 'IE'},
        {name: 'Isle of Man', code: 'IM'},
        {name: 'Israel', code: 'IL'},
        {name: 'Italy', code: 'IT'},
        {name: 'Jamaica', code: 'JM'},
        {name: 'Japan', code: 'JP'},
        {name: 'Jersey', code: 'JE'},
        {name: 'Jordan', code: 'JO'},
        {name: 'Kazakhstan', code: 'KZ'},
        {name: 'Kenya', code: 'KE'},
        {name: 'Kiribati', code: 'KI'},
        {name: 'Korea, Democratic People\'s Republic of', code: 'KP'},
        {name: 'Korea, Republic of', code: 'KR'},
        {name: 'Kuwait', code: 'KW'},
        {name: 'Kyrgyzstan', code: 'KG'},
        {name: 'Lao People\'s Democratic Republic', code: 'LA'},
        {name: 'Latvia', code: 'LV'},
        {name: 'Lebanon', code: 'LB'},
        {name: 'Lesotho', code: 'LS'},
        {name: 'Liberia', code: 'LR'},
        {name: 'Libyan Arab Jamahiriya', code: 'LY'},
        {name: 'Liechtenstein', code: 'LI'},
        {name: 'Lithuania', code: 'LT'},
        {name: 'Luxembourg', code: 'LU'},
        {name: 'Macao', code: 'MO'},
        {name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK'},
        {name: 'Madagascar', code: 'MG'},
        {name: 'Malawi', code: 'MW'},
        {name: 'Malaysia', code: 'MY'},
        {name: 'Maldives', code: 'MV'},
        {name: 'Mali', code: 'ML'},
        {name: 'Malta', code: 'MT'},
        {name: 'Marshall Islands', code: 'MH'},
        {name: 'Martinique', code: 'MQ'},
        {name: 'Mauritania', code: 'MR'},
        {name: 'Mauritius', code: 'MU'},
        {name: 'Mayotte', code: 'YT'},
        {name: 'Mexico', code: 'MX'},
        {name: 'Micronesia, Federated States of', code: 'FM'},
        {name: 'Moldova, Republic of', code: 'MD'},
        {name: 'Monaco', code: 'MC'},
        {name: 'Mongolia', code: 'MN'},
        {name: 'Montserrat', code: 'MS'},
        {name: 'Morocco', code: 'MA'},
        {name: 'Mozambique', code: 'MZ'},
        {name: 'Myanmar', code: 'MM'},
        {name: 'Namibia', code: 'NA'},
        {name: 'Nauru', code: 'NR'},
        {name: 'Nepal', code: 'NP'},
        {name: 'Netherlands', code: 'NL'},
        {name: 'Netherlands Antilles', code: 'AN'},
        {name: 'New Caledonia', code: 'NC'},
        {name: 'New Zealand', code: 'NZ'},
        {name: 'Nicaragua', code: 'NI'},
        {name: 'Niger', code: 'NE'},
        {name: 'Nigeria', code: 'NG'},
        {name: 'Niue', code: 'NU'},
        {name: 'Norfolk Island', code: 'NF'},
        {name: 'Northern Mariana Islands', code: 'MP'},
        {name: 'Norway', code: 'NO'},
        {name: 'Oman', code: 'OM'},
        {name: 'Pakistan', code: 'PK'},
        {name: 'Palau', code: 'PW'},
        {name: 'Palestinian Territory, Occupied', code: 'PS'},
        {name: 'Panama', code: 'PA'},
        {name: 'Papua New Guinea', code: 'PG'},
        {name: 'Paraguay', code: 'PY'},
        {name: 'Peru', code: 'PE'},
        {name: 'Philippines', code: 'PH'},
        {name: 'Pitcairn', code: 'PN'},
        {name: 'Poland', code: 'PL'},
        {name: 'Portugal', code: 'PT'},
        {name: 'Puerto Rico', code: 'PR'},
        {name: 'Qatar', code: 'QA'},
        {name: 'Reunion', code: 'RE'},
        {name: 'Romania', code: 'RO'},
        {name: 'Russian Federation', code: 'RU'},
        {name: 'Rwanda', code: 'RW'},
        {name: 'Saint Helena', code: 'SH'},
        {name: 'Saint Kitts and Nevis', code: 'KN'},
        {name: 'Saint Lucia', code: 'LC'},
        {name: 'Saint Pierre and Miquelon', code: 'PM'},
        {name: 'Saint Vincent and the Grenadines', code: 'VC'},
        {name: 'Samoa', code: 'WS'},
        {name: 'San Marino', code: 'SM'},
        {name: 'Sao Tome and Principe', code: 'ST'},
        {name: 'Saudi Arabia', code: 'SA'},
        {name: 'Senegal', code: 'SN'},
        {name: 'Serbia and Montenegro', code: 'CS'},
        {name: 'Seychelles', code: 'SC'},
        {name: 'Sierra Leone', code: 'SL'},
        {name: 'Singapore', code: 'SG'},
        {name: 'Slovakia', code: 'SK'},
        {name: 'Slovenia', code: 'SI'},
        {name: 'Solomon Islands', code: 'SB'},
        {name: 'Somalia', code: 'SO'},
        {name: 'South Africa', code: 'ZA'},
        {name: 'South Georgia and the South Sandwich Islands', code: 'GS'},
        {name: 'Spain', code: 'ES'},
        {name: 'Sri Lanka', code: 'LK'},
        {name: 'Sudan', code: 'SD'},
        {name: 'Suriname', code: 'SR'},
        {name: 'Svalbard and Jan Mayen', code: 'SJ'},
        {name: 'Swaziland', code: 'SZ'},
        {name: 'Sweden', code: 'SE'},
        {name: 'Switzerland', code: 'CH'},
        {name: 'Syrian Arab Republic', code: 'SY'},
        {name: 'Taiwan, Province of China', code: 'TW'},
        {name: 'Tajikistan', code: 'TJ'},
        {name: 'Tanzania, United Republic of', code: 'TZ'},
        {name: 'Thailand', code: 'TH'},
        {name: 'Timor-Leste', code: 'TL'},
        {name: 'Togo', code: 'TG'},
        {name: 'Tokelau', code: 'TK'},
        {name: 'Tonga', code: 'TO'},
        {name: 'Trinidad and Tobago', code: 'TT'},
        {name: 'Tunisia', code: 'TN'},
        {name: 'Turkey', code: 'TR'},
        {name: 'Turkmenistan', code: 'TM'},
        {name: 'Turks and Caicos Islands', code: 'TC'},
        {name: 'Tuvalu', code: 'TV'},
        {name: 'Uganda', code: 'UG'},
        {name: 'Ukraine', code: 'UA'},
        {name: 'United Arab Emirates', code: 'AE'},
        {name: 'United Kingdom', code: 'GB'},
        {name: 'United States', code: 'US'},
        {name: 'United States Minor Outlying Islands', code: 'UM'},
        {name: 'Uruguay', code: 'UY'},
        {name: 'Uzbekistan', code: 'UZ'},
        {name: 'Vanuatu', code: 'VU'},
        {name: 'Venezuela', code: 'VE'},
        {name: 'Vietnam', code: 'VN'},
        {name: 'Virgin Islands, British', code: 'VG'},
        {name: 'Virgin Islands, U.S.', code: 'VI'},
        {name: 'Wallis and Futuna', code: 'WF'},
        {name: 'Western Sahara', code: 'EH'},
        {name: 'Yemen', code: 'YE'},
        {name: 'Zambia', code: 'ZM'},
        {name: 'Zimbabwe', code: 'ZW'}
      ];

    // angular translate
    $scope.lang = { isopen: false };
    $scope.langs = {en:'English', id:'Bahasa Indonesia'};
    $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
    $scope.setLang = function(langKey, $event) {
            // set the current lang
            $scope.selectLang = $scope.langs[langKey];
            // You can change the language during runtime
            $translate.use(langKey);
            $scope.lang.isopen = !$scope.lang.isopen;
    };
    
    
    $scope.companyType = {};
    $scope.companyTypeList = [
        { id: 1, text: "PT" },
        { id: 2, text: "CV" },
        { id: 3, text: "UD" }
    ];
    
}]);


//    var swapViews = angular.module('grid-list', ['angularGrid']);
angular.module('naut').controller('swapView', ['$scope', '$http', function($scope, $http) {
    $scope.angularGridOptions = {
        gridWidth : 500,
        refreshOnImgLoad : false
    }
    $scope.searchTxt = function(){
    }

    $http({
        method:'GET',
        url:'server/company.json'
    }).success(function(data){
        $scope.pics = data;
    });
    
    $scope.swap = function(div) {
        if(div == 'list'){
            $('#grid-view').removeClass('selected');
            $('#list-view').addClass('selected');
            $('.product-layout').removeClass('product-grid');
            $('.product-layout').addClass('product-list');
            $scope.angularGridOptions = {
                gridWidth : 500
            }
        }
        else{
            $('#grid-view').addClass('selected');
            $('#list-view').removeClass('selected');
            $('.product-layout').removeClass('product-list');
            $('.product-layout').addClass('product-grid');
            $scope.angularGridOptions = {
                gridWidth : 250
            }
        }

    }


    $scope.totalItems = 64;
    $scope.currentPage = 4;

    $scope.setPage = function (pageNo) {
      $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function() {
      $log.info('Page changed to: ' + $scope.currentPage);
    };

    $scope.maxSize = 5;
    $scope.bigTotalItems = 175;
    $scope.bigCurrentPage = 1;
}]);

angular.module('naut').controller('HeaderNavController', ['$scope', '$modal', 'RequestService', '$location','$rootScope', function($scope, $modal, RequestService, $location, $rootScope){
	
    $scope.login = function () {
        var modalInstance = $modal.open({
            templateUrl: 'loginform.html',
            controller: 'LoginController as form',
            resolve: {
                items: function () {

                }
            }
        });

        modalInstance.result.then(function () {
//            $scope.panelDashboard[isShow].isShow = true;
        });

    };
    
    $scope.logout = function () {
        // set cartList to empty when user logout
        $rootScope.cartList = [];
    	RequestService.doSignOut();
//    	$location.path('/portal');
    	/*perubahan KAI 24/11/2020*/
    	RequestService.doGET('/procurement/master/parameter/get-name/PM_URL')
        .success(function (data, status) {
        	window.location.href = data.nilai + '/logout.promise';
        })
    }
    
    $scope.goToDashboard = function () {
    	$rootScope.cartList = [];
    	RequestService.goToDashboard();
    }
       
    $scope.headerMenuCollapsed = true;

    $scope.toggleHeaderMenu = function () {
            $scope.headerMenuCollapsed = !$scope.headerMenuCollapsed;
    };
    

}]);

angular.module('naut').controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'items', function($scope, $modalInstance, items) {

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);


angular.module('naut').controller('MyRating', ['$scope', function($scope) {
    $scope.rate = 1;
    $scope.max = 5;
    $scope.isReadonly = false;

    $scope.hoveringOver = function(value) {
      $scope.overStar = value;
      $scope.percent = 100 * (value / $scope.max);
    };
}]);


angular.module('naut').controller('DatepickerDemoCtrl', ['$scope', function($scope) {
    $scope.today = function() {
      $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function () {
      $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
      return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
      $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1,
      class: 'datepicker'
    };

    $scope.initDate = new Date('2016-15-20');
    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
}]);

angular.module('naut').controller('TimepickerDemoCtrl', ['$scope', function($scope) {
    $scope.mytime = new Date();

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
      hstep: [1, 2, 3],
      mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = true;
    $scope.toggleMode = function() {
      $scope.ismeridian = ! $scope.ismeridian;
    };

    $scope.update = function() {
      var d = new Date();
      d.setHours( 14 );
      d.setMinutes( 0 );
      $scope.mytime = d;
    };

    $scope.changed = function () {
      //console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function() {
      $scope.mytime = null;
    };
}]);
angular.module('naut').controller('delCtrl', ['$scope', '$modal', function($scope, $modal) {
    $scope.ConfirmAlert = function () {
        var modalInstance = $modal.open({
            templateUrl: 'delConfirm.html',
            controller: 'ModalInstanceCtrl',
            resolve: {
                items: function () {

                }
            }
        });

        modalInstance.result.then(function () {
//            $scope.panelDashboard[isShow].isShow = true;
        });

    };
}]);

angular.module('naut').controller('ConfirmationDialogController', ['$scope', '$modalInstance', function($scope, $modalInstance) {
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);

angular.module('naut').controller('TabPengumumanController', ['$scope', 'RequestService', function($scope, RequestService) {
      var targetURL = "/Services/Process/PengumumanPengadaanServices/getPengumumanPengadaanList";
      RequestService.requestServerNew(targetURL, {})
      .then(function(data){
          $scope.pengumumanList = data.data;
      });
}]);

angular.module('naut').controller('chartCtrl', ['$scope', function($scope) {
    $scope.items = [
        { label: "Jasa", data: 45 },
        { label: "Peralatan & Suku Cadang", data: 25 },
        { label: "Bahan Baku", data: 10 },
        { label: "Hasil Pengelolaan", data: 20 }
    ];
}]);

angular.module('naut').controller('ImgZoomCtrl', function ($scope, $location) {
    $scope.imagesForGallery = [];
    $scope.setApproot = function(appRoot) {
        //only change when needed.
        if ($scope.approot && appRoot === $scope.approot) {
            return;
        }
        $scope.approot = appRoot;
        $scope.imagesForGallery = [
            {
                thumb: 'app/img/product/garing-garuda.jpg',
                small: 'app/img/product/garing-garuda.jpg',
                large: 'app/img/product/garing-garuda.jpg'
            },
            {
                thumb: 'app/img/product/garuda-roasted-flavored.png',
                small: 'app/img/product/garuda-roasted-flavored.png',
                large: 'app/img/product/garuda-roasted-flavored.png'
            },
            {
                thumb: 'app/img/product/kacang-kulit.jpg',
                small: 'app/img/product/kacang-kulit.jpg',
                large: 'app/img/product/kacang-kulit.jpg'
            }
        ];


        $scope.zoomModelGallery01 = $scope.imagesForGallery[0];
    };

    //default
    $scope.setApproot('');

    $scope.zoomOptions = {
        lensShape: 'round',

        cursor: 'crosshair',
        galleryActiveClass: "active",
        loadingIcon: true
    };

    $scope.setActiveImageInGallery = function (prop, img) {
        $scope[prop] = img;
    };
});

angular.module('naut').controller('RangeSliderCtrl', ['$scope', function($scope) {
   //START RANGE SLIDER CTRL//
        $scope.scopes = [];
        $scope.value = 0;
        $scope.values = {
            low: 0,
            high: 999999
        };

        $scope.scale = function (value) {
            return Math.pow(value, 3);
        };
        $scope.inverseScale = function (value) {
            var sign = value == 0 ? 1 : (value / Math.abs(value));
            return sign * Math.pow(Math.abs(value), 1 / 3);
        };


        $scope.translate = function (value) {
            return value;
        };

        $scope.translateCombined = function (low, high) {
            return $scope.translate(low.toFixed($scope.precision)) + " *** " + $scope.translate(high.toFixed($scope.precision));
        };

        $scope.translateRange = function (low, high) {
            return $scope.translate((high - low).toFixed($scope.precision));
        };

        $scope.$broadcast('refreshSlider');
        //END RANGE SLIDER CTRL//
}]);


angular.module('naut').controller('sidebarselectcategoriesCtrl', ['$scope', '$state', function($scope, $state) {
    $scope.categories = 0;
    $scope.changeCategories = function(selected){
        if (selected == 2) {
            $state.go('app.categoryMain');
        };
    };
}]);

angular.module('naut').controller('footerCtrl', ['$scope', '$state', function($scope, $state){
    $scope.compareWin = false;
//    $scope.openCompare = function(){
//        $scope.compareWin = true;
//    }
}]);