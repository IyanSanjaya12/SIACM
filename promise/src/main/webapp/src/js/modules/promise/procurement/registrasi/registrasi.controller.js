/**=========================================================
 * Module: RegistrasiController.js
 =========================================================*/
(function () {
	'use strict';

	angular
		.module('naut')
		.factory('LoginUser', LoginUser)
		.controller('RegistrasiController', RegistrasiController);

	function RegistrasiController($http, $scope, $rootScope, $resource, $location, $q, LoginUser, $state) {
		var formRegistrasi = this;
		
		$scope.btnRegistrasi = function () {
			$scope.loading = true;
			var isEmailValid = LoginUser.checkEmailIsValid(formRegistrasi.email);
			if (formRegistrasi.passwd1 != formRegistrasi.passwd2) {
				$scope.errorPassword = true;
				$scope.loading = false;
			} else if (isEmailValid==true) {
				$scope.errorEmail = true;
				$scope.loading = false;
			} else {
                $rootScope.formRegistrasi = formRegistrasi;
                $state.go("page.promiseregister"); //Terjadi Perubahan alur pada bisnis proses (BSA)
				/*$http({
					method: 'POST',
					url: $rootScope.backendAddress + '/procurement/vendor/RegistrasiServices/register',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					transformRequest: function (obj) {
						var str = [];
						for (var p in obj)
							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data: { email : formRegistrasi.email, password : formRegistrasi.passwd1 }
				}).success(function (data, status, headers, config) {
					if(typeof data.id !== 'undefined'){
						console.log('user : '+JSON.stringify(data));
						$scope.btnLogin(formRegistrasi.email, formRegistrasi.passwd1);
					}
				}).error(function (data, status, headers, config) {
					$scope.loading = false;
				});*/
			}
		};
		
		$scope.btnLogin = function (email, password) {
			$scope.loading = true;
			$http({
				method: 'POST',
				url: $rootScope.backendAddress + '/procurement/user/getAuthentification',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				transformRequest: function (obj) {
					var str = [];
					for (var p in obj)
						str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					return str.join("&");
				},
				data: { email : email, password : password}
			}).success(function (data, status, headers, config) {
				if (data.length == 0) {
					$scope.messageError = true;
					$scope.loading = false;
				} else {
					$rootScope.userLogin = data;
					//var roleUser = LoginUser.getRoleAuthentification(data.user.id);
					LoginUser.getRoleAuthentification(data.user.id).then(function (roleUser) {
						if (roleUser == 'PANITIA') {
							$rootScope.userRoleName = 'PANITIA';
							$location.path('/app/dashboard');
							$scope.loading = false;
						} else if (roleUser == 'VENDOR') {
							$rootScope.userRoleName = 'VENDOR';
							$location.path('/appvendor/promise/dashboard');
							$scope.loading = false;
						} else {
							$scope.loading = false;
						}
					});

				}
			}).error(function (data, status, headers, config) {
				$scope.loading = false;
			});

		};
	}

	RegistrasiController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$q' ,  'LoginUser', '$state'];
	function LoginUser($q, $timeout, $http, $rootScope) {
		var getRoleAuthentification = function (userId) {
			var defered = $q.defer();
			$timeout(function () {
				$http.get($rootScope.backendAddress + '/procurement/user/get-role-user/' + userId)
					.success(function (data, status, headers, config) {
						defered.resolve(data[0].role.nama);
					})
					.error(function (data, status, headers, config) {
						console.log('eror get http');
					});
			}, 500);
			return defered.promise;
		};
		
		var checkEmailIsValid = function(email){
			var defered = $q.defer();
			$timeout(function() {
			$http.get($rootScope.backendAddress + '/procurement/vendor/RegistrasiServices/getEmailIsValid/' +email)
				.success(function (data, status, headers, config) {
					return data;
				})
				.error(function (data, status, headers, config) {
					console.log('Error get Email');
					return false;
				});	
			}, 500);
			return defered.promise;
		};

		return {
			getRoleAuthentification: getRoleAuthentification,
			checkEmailIsValid : checkEmailIsValid
		}
	}
})();