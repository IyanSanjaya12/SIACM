/**
 * ========================================================= 
 * Module:
 * RecoverExecuteController.js 
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('naut')
        .controller('RecoverExecuteController', RecoverExecuteController);

    function RecoverExecuteController($http, $scope, $rootScope, $resource, $location, $q, LoginUserService, $localStorage, $window, RequestService, $log, $state, $stateParams, $modal) { 
        var mailHash = $stateParams.mailHash;
        
        $scope.loading = true;
        $scope.sendingMessage = false;
        $scope.sendingMessageError = false;
        
        RequestService.doPOST('/procurement/user/recover/reset', {
            hash : mailHash
        })
        .success(function(resp){
            if(resp){                
                $scope.sendingMessage  = true;
            } else {
                $scope.sendingMessageError = true;
            }
            $scope.loading = false;
        })
        .error(function(err){
            $scope.sendingMessageError = true;
            $scope.loading = false;
        });
        
        $scope.goLogin = function(){
            //$location.path('/page/promiselogin');
        	var modalInstance = $modal.open({
                templateUrl: 'loginform.html',
                controller: 'LoginController as form',
                resolve: {
                    items: function () {

                    }
                }
            });
        }
                
    }

    RecoverExecuteController.$inject = ['$http', '$scope', '$rootScope', '$resource',
			'$location', '$q', 'LoginUserService', '$localStorage', '$window',
			'RequestService', '$log', '$state', '$stateParams','$modal'];

})();