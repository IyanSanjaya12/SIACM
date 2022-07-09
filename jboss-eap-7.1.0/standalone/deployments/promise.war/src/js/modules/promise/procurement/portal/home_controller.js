/**=========================================================
 * Module: PortalCarouselController
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PortalCarouselController', PortalCarouselController);

    function PortalCarouselController($scope, $location, $anchorScroll, $rootScope) {

        $scope.loading = true;

        $scope.myInterval = 5000;
        var slides = $scope.slides = [];

        $scope.addSlide = function (index) {
            $scope.loading = false;
            var newWidth = 1677 + slides.length;
            index = index || (Math.floor((Math.random() * 2)) + 1);
            slides.push({
                image: 'app/img/portal/slide/bg' + index + '.jpeg',
                text: '<h2 class="italic">CENTRA AUTO AC</h2>\n\
                        <p class="m-t"></p>\n\
                        '
            });
        };
        for (var i = 1; i <= 1; i++) {
            $scope.addSlide(i);
        }


        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $('.scroll-to').fadeIn();
            } else {
                $('.scroll-to').fadeOut();
            }
        });
        $('.scroll-to').click(function () {
            $("html, body").animate({ scrollTop: 0 }, 1000);
            return false;
        });

        var cities = [
            {
                city: 'PT Pelabuhan Indonesia II (Persero)',
                lat: -6.107744,
                long: 106.883141
            }
        ];

        var mapOptions = {
            zoom: 18,
            center: new google.maps.LatLng(-6.107744, 106.883141),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        }

        if (document.getElementById('map') != null) {
            $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);
        }

        $scope.markers = [];

        var infoWindow = new google.maps.InfoWindow();

        var createMarker = function (info) {

            var marker = new google.maps.Marker({
                map: $scope.map,
                position: new google.maps.LatLng(info.lat, info.long),
                title: info.city
            });
            //marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

            google.maps.event.addListener(marker, 'click', function () {
                infoWindow.setContent('<h2>' + marker.title + '</h2>');
                infoWindow.open($scope.map, marker);
            });

            $scope.markers.push(marker);

        }

        for (i = 0; i < cities.length; i++) {
            createMarker(cities[i]);
        }

        $scope.openInfoWindow = function (e, selectedMarker) {
            e.preventDefault();
            google.maps.event.trigger(selectedMarker, 'click');
        }

    }

    PortalCarouselController.$inject = ['$scope', '$location', '$anchorScroll', '$rootScope'];
})();
