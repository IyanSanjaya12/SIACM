/**
 * ========================================================= 
 * Module:
 * RecoverController.js 
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('naut')
        .controller('RecoverController', RecoverController);

    function RecoverController($http, $scope, $rootScope, $resource, $location, $q, LoginUserService, $localStorage, $window, RequestService, $log, $state) {
        $scope.emailUser = '';
        $scope.sendingMessage = false;
        $scope.sendingMessageError = false;
        $scope.loading = false;

        $scope.resetPassword = function () {

            $scope.sendingMessage = false;
            $scope.sendingMessageError = false;

            $scope.loading = true;
            var param = {
                email: $scope.emailUser
            }

            RequestService.doPOST('/procurement/user/recover', param)
                .success(
                    function (data) {
                        if (data) {
                            $scope.sendingMessage = true;
                        } else {
                            $scope.sendingMessageError = true;
                        }
                        $scope.loading = false;

                    })
                .error(function (err) {
                    $scope.loading = false;
                });

        }
        
        $scope.back=function(){
        	$location.path('/app/promise/app/portal');
        }
        
        
    }

    RecoverController.$inject = ['$http', '$scope', '$rootScope', '$resource',
			'$location', '$q', 'LoginUserService', '$localStorage', '$window',
			'RequestService', '$log', '$state'];

})();