/**=========================================================
 * Module: SPKController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('SPKIndexController', SPKIndexController);

    function SPKIndexController($scope, $http, $rootScope, $resource, $location,$filter) {
        var form = this;
        $scope.loading = true;

        $scope.getList = function () {
            $http.get($rootScope.backendAddress + '/procurement/spk/SPKServices/getList', {
                ignoreLoadingBar: true
            }).success(
                function (data, status, headers, config) {
                    for(var i=0;i<data.length;i++){
                        data[i].tanggal = $filter('date')(data[i].tanggal, 'dd MMMM yyyy');
                    }
                    $scope.spkList = data;
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {});
        }
        $scope.getList();

        $scope.view = function (data) {
            $rootScope.statusSPK=true;
            $rootScope.spk = data;
            $location.path('/app/promise/procurement/spk/view');
        };

    }

    SPKIndexController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location','$filter'];

})();