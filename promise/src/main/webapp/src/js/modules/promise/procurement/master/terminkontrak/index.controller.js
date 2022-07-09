(function () {
    'use strict';

    angular
        .module('naut')
        .controller('TerminKontrakController', TerminKontrakController);

    function TerminKontrakController($scope, $http, $rootScope, $resource, $location) {
        var form = this;

        var getTerminKontrakList = function () {
            $scope.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/TerminKontrakServices/getList', {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
                form.terminKontrakList = data;
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.loading = false;
            })
        }
        getTerminKontrakList();

        form.add = function () {
            $location.path('app/promise/procurement/master/terminkontrak/tambah');
        }

        form.edit = function (data) {
            $rootScope.indexTerminKontrak = data;
            $location.path('app/promise/procurement/master/terminkontrak/ubah');
        }

        form.del = function (id) {
            var konfirmasi = confirm("Apakah Anda Yakin Ingin Menghapus Termin Kontrak ?");
            if (konfirmasi) {
                $http.get($rootScope.backendAddress + '/procurement/TerminKontrakServices/delete/' + id).success(function (data, status, headers, config) {
                    getTerminKontrakList();
                }).error(function (data, status, headers, config) {})
            }
        }
    }

    TerminKontrakController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location'];

})();
