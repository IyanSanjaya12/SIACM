/**=========================================================
 * Module: PortalCarouselController
 =========================================================*/
//(function() {
'use strict';

angular
    .module('naut')
    .controller('PortalCarouselController', ['$scope', '$location', '$anchorScroll',//PortalCarouselController);

        function ($scope, $location, $anchorScroll) {

            $scope.loading = true;

            $scope.myInterval = 5000;
            var slides = $scope.slides = [];

            $scope.addSlide = function (index) {
                $scope.loading = false;
                var newWidth = 1677 + slides.length;
                index = index || (Math.floor((Math.random() * 2)) + 1);
                slides.push({
                    image: 'app/img/portal/slide/bg' + index + '.jpg',
                    text: '<h2 class="italic">IPC E-Katalog</h2>\n\
                        <p class="m-t">IPC E-Katalog adalah sistem aplikasi berbasis web yang menyediakan berbagai kemudahan serta informasi \n\
                        dalam kaitannya dengan Proses Pengadaan Barang/Jasa di lingkungan PT Pelabuhan Indonesia II (Persero).\n\
                        Sistem aplikasi ini dibangun guna meningkatkan efisiensi dan efektivitas pengadaan.</p>\n\
                        <a href="" class="btn btn-rounded btn-white m-t">Hubungi Kami</a>'
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



            //}
            //PortalCarouselController.$inject = ['$scope', '$location', '$anchorScroll'];
            //})();
        }]);