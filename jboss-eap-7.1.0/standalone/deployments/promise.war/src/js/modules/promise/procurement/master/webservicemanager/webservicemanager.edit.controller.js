
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('WebServiceManagerEditController', WebServiceManagerEditController);

    function WebServiceManagerEditController($scope, $http, $rootScope, $resource, $location, toaster, $modal) {

        var form = this;
        $scope.isSaved = false;
         if (typeof $rootScope.wsSelected !== 'undefined') {
            console.log('Data  =' +JSON.stringify($rootScope.wsSelected));
             form.id = $rootScope.wsSelected.id;
             form.services = $rootScope.wsSelected.services;
             form.isPublic = $rootScope.wsSelected.isPublic;
             form.path = $rootScope.wsSelected.path;
             form.function = $rootScope.wsSelected.function;
        } else {
            console.error('No Webservices data selected');
        }
        
        $scope.save = function () {
            if (typeof form.path === 'undefined' || form.path == '') {
                toaster.pop('warning', 'Kesalahan', 'Path belum disi...');
            } else {
                $scope.loading = true;
                var dataPost = {
                    id:form.id,
                    isPublic: form.isPublic,
                    path: form.path,                    
                    services : form.services,
                    function: form.function
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/webservicesmanager/edit',
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
                        toaster.pop('success', 'WebServices Manager ', 'Edit ' + data.nama + ', berhasil.');
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
    WebServiceManagerEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal'];

})();