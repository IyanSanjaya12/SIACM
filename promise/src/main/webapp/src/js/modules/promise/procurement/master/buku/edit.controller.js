(function() {
    'use strict';

    angular.module('naut').controller('BukuEditController', BukuEditController);

    function BukuEditController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
		form.id  = $rootScope.bukuEdit.id;
		form.namaBuku  = $rootScope.bukuEdit.namaBuku;
		form.hargaBuku
		
		$scope.save = function(){
			$scope.loading = true;
			if (typeof form.namaBuku === 'undefined' || form.namaBuku == '') {
				alert("Nama Buku belum disi.");
			} else if (typeof form.hargaBuku === 'undefined' || form.hargaBuku == '') {
				alert("Harga Buku belum disi.");
			} else {
				$http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/master/BukuServices/updateBuku',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded'},
                        transformRequest: function(obj) {
                            var str = [];
                            for (var p in obj)
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            return str.join("&");
                        },
                        data: form
                }).success(function(data, status, headers, config) {
                        if (typeof data !== 'undefined') {
							toaster.pop('success', 'Buku', 'Ubah '+data.namaBuku+', berhasil.');
						}
					$scope.loading = false;
                });
			}
		};
		$scope.back = function(){
			$location.path('/app/promise/procurement/master/buku');
		};
    }

    BukuEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();




