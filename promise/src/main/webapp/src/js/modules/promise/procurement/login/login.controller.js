/**
 * ========================================================= 
 * Module:
 * LoginController.js 
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').factory('LoginUserService', LoginUserService)
			.controller('LoginController', LoginController);

	function LoginController($http, $scope, $rootScope, $resource, $location, $q, LoginUserService, $localStorage, $window, RequestService, $log, $state, $modalInstance, $stateParams) {
		
		var formLogin = this;
		$scope.loading = false;
		$scope.messageError = false;
		
        
        RequestService.doGET('/procurement/loginservices/cleanAllCookies')
			.then(function (data, status, headers, config) { 
				$scope.refreshKaptcha();
		}); 
		
		
		$scope.refreshKaptcha = function() {
			formLogin.captcha = null;
        	$scope.image = [{
        	    src: $rootScope.backendAddress + '/kaptcha.jpg?idx=' + Math.random(),
        	}];
    	}
		
		$scope.go = function(path) {
			$location.path(path);
		};
		
		$scope.closeModal = function(){
			if ($stateParams.catalogList == undefined || $stateParams.catalogList == null){
				$modalInstance.close();
			}
			else{
				$modalInstance.close();
				$location.path('/portal');
			}
		};

		$scope.btnLogin = function() {
			
			$scope.alertMsg = null;
			$scope.loading = true;
			
			if(formLogin.rememberMe == undefined) {
				var rememberMe = false;
			} else {
				rememberMe = true;
			}
			
			var auth = RequestService.encrypt(formLogin.email,formLogin.password,formLogin.captcha,rememberMe,true);
			
		    var loginData = {
		    		auth : auth
		    }

			RequestService
					.doPOST('/procurement/loginservices/getAuthentification', loginData)
					.success(
							function(data, status, headers, config) {
								if (data.userToken.token == null) {
									$scope.refreshKaptcha();
									$scope.alertMsg = data.message;
									$scope.loading = false;
								} else {

									/* Login success! */
									//close modal login
									$scope.closeModal();
									var keepGoing = true;
									
									angular.forEach(
													data.roleUserList,
													function(roleUser, indeks) {
														if (roleUser.role.appCode == "PM" && keepGoing) {
															keepGoing = false;
															RequestService.doSignInUserPM( data, roleUser);
															
														}
													})

									angular.forEach(
													data.roleUserList,
													function(roleUser, indeks) {
														if (roleUser.role.appCode == "CM"	&& keepGoing) {
															keepGoing = false;
															RequestService.doSignInUserCM(data,roleUser.role);
															
														}
													})

									if (keepGoing) {
										alert('Error no role kode defined!');
										$state.go('page.portal');
									}

								}
							}).error(function(data, status, headers, config) {
						$scope.loading = false;
				});
		};
	}

	LoginController.$inject = [ '$http', '$scope', '$rootScope', '$resource',
			'$location', '$q', 'LoginUserService', '$localStorage', '$window',
			'RequestService', '$log', '$state', '$modalInstance', '$stateParams' ];

	/*
	 * LoginUser diganti jadi LoginUserService karena di register.controller.js
	 * juga ada :(
	 */
	function LoginUserService($q, $timeout, $http, $rootScope) {
		var getRoleAuthentification = function(userId) {
			var defered = $q.defer();
			$timeout(function() {
				$http.get(
						$rootScope.backendAddress	+ '/procurement/user/get-role-user/' + userId)
						.success(function(data, status, headers, config) {// console.log('info role get : '+data[0].role);
							defered.resolve(data);
						}).error(function(data, status, headers, config) {
							console.log('eror get http');
						});
			}, 500);
			return defered.promise;
		};

		return {
			getRoleAuthentification : getRoleAuthentification
		}
	}

})();