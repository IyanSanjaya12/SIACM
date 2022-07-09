/**=========================================================
 * Module: PortalCarouselController
 =========================================================*/
(function () {
  'use strict';

  angular
    .module('naut')
    .controller('PortalCarouselController', PortalCarouselController);

  function PortalCarouselController($scope) {

    $scope.myInterval = 5000;
    var slides = $scope.slides = [];

    $scope.addSlide = function (index) {
      var newWidth = 1677 + slides.length;
      index = index || (Math.floor((Math.random() * 2)) + 1);
      slides.push({
        image: 'app/img/portal/slide/bg' + index + '.jpg',
        text: '<h2 class="italic">e-Catalogue & <br> Contract Management System</h2>\n\
                    <p class="m-t"><strong>Katalog Elektronik </strong>(e-Catalogue) adalah sistem informasi elektronik yang memuat informasi berupa daftar, jenis, spesifikasi teknis, merk, harga, Penyedia, dan informasi lainnya terkait barang dan jasa. \n\
                    </p>\n\
        	<p class="m-t"><strong>Contract Management System </strong>(CMS) adalah pengelolaan Kontrak secara elektronik dari mulai tahapan proses pembuatan kontrak sampai dengan pendokumentasian Kontrak serta monitoring seluruh tahapan tersebut secara sistem.\n\
            </p>'
        	
      });
    };
    for (var i = 1; i <= 1; i++) {
      $scope.addSlide(i);
    }
  }
  PortalCarouselController.$inject = ['$scope'];
})();
