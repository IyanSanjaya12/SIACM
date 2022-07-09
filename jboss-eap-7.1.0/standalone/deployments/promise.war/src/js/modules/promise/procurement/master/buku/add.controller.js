(function() {
    'use strict';

    angular.module('naut').controller('BukuTambahController', BukuTambahController);

    function BukuTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
				
		$scope.simpan = function(){
			console.log(">> "+JSON.stringify(form));
			if (typeof form.namaBuku === 'undefined' || form.namaBuku == '') {
				alert("Nama Buku belum diisi.");
			} else if (typeof form.hargaBuku === 'undefined' || form.hargaBuku == '') {
				alert("Harga Buku belum diisi.");
			} else {
				$scope.loading = true;
				$http({
                        method: 'POST',
                        url: $rootScope.backendAddress + '/procurement/master/BukuServices/insertBuku',
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
							toaster.pop('success', 'Buku', 'Simpan '+data.namaBuku+', berhasil.');
						}
					$scope.loading = false;
                    form.namaBuku = '';
                    form.hargaBuku = '';
                });
			}
		};
		
		$scope.kembali = function(){
			$location.path('/app/promise/procurement/master/buku');
		};
    }

    BukuTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();


