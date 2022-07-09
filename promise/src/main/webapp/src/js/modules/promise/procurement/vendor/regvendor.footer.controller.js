/**=========================================================
 * Module: RegVendorFooterController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('RegVendorFooterController', RegVendorFooterController);

    function RegVendorFooterController($http, $timeout, $window, $scope) {
        
        $scope.tampilkanTanggal = function() {
            var tanggallengkap = new String(); 
            var namahari = ("Minggu Senin Selasa Rabu Kamis Jumat Sabtu"); 
            var namabulan = ("Januari Februari Maret April Mei Juni Juli Agustus September Oktober Nopember Desember"); 
            var tgl = new Date(); 
            var hari = tgl.getDay(); 
            var tanggal = tgl.getDate(); 
            var bulan = tgl.getMonth(); 
            var tahun = tgl.getFullYear(); 
            
            namabulan = namabulan.split(" "); 
            namahari = namahari.split(" "); 
            
            $scope.tanggallengkap = namahari[hari] + ", " +tanggal + " " + namabulan[bulan] + " " + tahun;     
        }
        
        $scope.tampilkanjam = function() {
            var waktu = new Date();
            var jam = waktu.getHours();
            var menit = waktu.getMinutes();
            var detik = waktu.getSeconds();
            var teksjam = new String();
            if ( menit <= 9 )
                menit = "0" + menit;
            if ( detik <= 9 )
                detik = "0" + detik;
            teksjam = jam + ":" + menit + ":" + detik;
            $scope.tempatjam = teksjam;
            
            $timeout($scope.tampilkanjam, 1000);
        }
        
        $scope.tampilkanTanggal();
        $scope.tampilkanjam();
    }

    RegVendorFooterController.$inject = ['$http', '$timeout', '$window', '$scope'];

})();