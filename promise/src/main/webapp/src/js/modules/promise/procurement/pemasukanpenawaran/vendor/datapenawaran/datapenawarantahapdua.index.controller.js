/**=========================================================
 * Module: PemasukanPenawaranController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PemasukanPenawaranTahapDuaIndexController', PemasukanPenawaranTahapDuaIndexController);

    function PemasukanPenawaranTahapDuaIndexController($scope, $http, $rootScope, $resource, $location, $state) {
        var form = this;
        
        var tahapan=10//masih hard code
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/pendaftaranVendorServices/getPendaftaranVendorByVendorDateTahapan/'+ $rootScope.userLogin.user.id+'/'+tahapan)
		.success(function (data, status, headers, config) {
			form.pemasukanPenawaranList = data;
		});
        
        form.btnDetil = function (pengadaan) {
        	$state.go('appvendor.promise.procurement-vendor-pemasukanpenawaranvendortahapdua', {dataPengadaan: pengadaan});            
        }
    }

    PemasukanPenawaranTahapDuaIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$state'];

})();