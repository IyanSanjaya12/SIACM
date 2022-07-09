/**=========================================================
 * Module: BukuController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BukuController', BukuController);

    function BukuController($scope,$http, $rootScope, $resource, $location) {
        var form = this;

        $scope.getBukuList = function(){
        	$scope.loading=true;
        	$http.get($rootScope.backendAddress + '/procurement/master/BukuServices/getBukuList', {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    form.bukuList = data;
                    $scope.loading=false;
                }).error(function (data, status, headers, config) {});
	
        }
        $scope.getBukuList();
        
        $scope.add = function(){
			$location.path('/app/promise/procurement/master/buku/tambah');
		};
        $scope.edit = function (data) {
            $rootScope.bukuEdit = data;
            $location.path('/app/promise/procurement/master/buku/edit');
        };
        
        $scope.del = function (id) {
            var konfirmasi = confirm("Apakah anda yakin ingin menghapus Buku ?");
            if (konfirmasi) {
                $scope.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/master/BukuServices/deleteBuku/' + id).success(function (data, status, headers, config) {
         $scope.getBukuList();
                }).error(function (data, status, headers, config) {});
            }
        };


    }

    BukuController.$inject = ['$scope','$http', '$rootScope', '$resource', '$location'];

})();