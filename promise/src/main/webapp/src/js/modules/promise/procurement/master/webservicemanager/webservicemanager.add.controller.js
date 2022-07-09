(function () {
    'use strict';

    angular
        .module('naut')
        .controller('WebServiceManagerAddController', WebServiceManagerAddController);

    function WebServiceManagerAddController($scope, $http, $rootScope, $resource, $location, toaster, $modal) {

        var form = this;
        $scope.isSaved = false;
        
        $scope.save = function () {
            if (typeof form.path === 'undefined' || form.path == '') {
                toaster.pop('warning', 'Kesalahan', 'Path belum disi...');
            } else {
                $scope.loading = true;
                var publik;
                if (form.isPublic == true) {
                    publik = 1;
                } else {
                    publik = 0;
                }
                var dataPost = {
                    isPublic: publik,
                    path: form.path,                    
                    services : form.services,
                    function: form.function
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/webservicesmanager/insert',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: dataPost
                }).success(function (data, status, headers, config) {
                    if (typeof data !== 'undefined') {
                        toaster.pop('success', 'WebService Manager', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.isSaved = true;
                    $scope.loading = false;
                });
            }
        };

        $scope.back = function () {
            $location.path('/app/promise/procurement/master/webservicemanager');
        };

    }
    WebServiceManagerAddController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

})();