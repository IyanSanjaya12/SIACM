(function () {
    'use strict';

    angular.module('naut').controller('EvaluasiTeknisTotalSistemGugurController', EvaluasiTeknisTotalSistemGugurController);

    function EvaluasiTeknisTotalSistemGugurController($scope, $http, $rootScope, $resource, $location, toaster, $window) {
        var form = this;

        $scope.back = function () {
            $location.path('app/promise/procurement/evaluasiteknis/view/total');
        }

        /* START Vendor */
        if (typeof $rootScope.kriteriaTeknisData !== 'undefined') {
            $scope.kriteriaTeknisData = $rootScope.kriteriaTeknisData;
            console.log(JSON.stringify($scope.kriteriaTeknisData));
        } else {
            console.log('INFO Error');
        }
        form.pengadaanId = $rootScope.detilPengadaan.id;
        form.vendorId = $scope.kriteriaTeknisData.id;
        /*---END Vendor----*/

        $scope.save = function (data) {
            $location.path('app/promise/procurement/evaluasiteknis/view/total');
        }

        $scope.save = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/evaluasiteknis/EvaluasiTeknisTotalServices/create',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: form
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    toaster.pop('success', 'Item Type', 'Simpan ' + data.nama + ', berhasil.');
                }
                $scope.loading = false;
                form.nama = '';
            });
        };

    }

    EvaluasiTeknisTotalSistemGugurController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window'];

})();