/**=========================================================
    this page only can be accessed when login data of user is null
    Reinhard
 =========================================================*/

//(function () {
	'use strict';

	angular
		.module('naut')
		.controller('ReloginController', ['$scope', '$rootScope', '$timeout', '$state','RequestService','$cookies','$log','$modal',//ReloginController);
	
	function ($scope, $rootScope, $timeout, $state, RequestService, $cookies, $log, $modal) {
		
		//Check cookies			
		$rootScope.hasCM =false;
		$rootScope.hasPM =false;
		var token = $cookies['JPROMISESESSIONID'];
		var code  = $cookies['JPROMISEAPPLIST'];
			
			
		$log.info('RELOGIN!! >> Token from cookies = ' +  token);
		
		var loginData = {
				strToken : token,
				roleCode :code
		    }
		
		RequestService.doPOST('/procurement/loginservices/reAuthentificationByToken/', loginData)
		.success(function (data, status, headers, config) {

			if (data.userToken.token != null)
				{
					var keepGoing = true;
					
					// check if user has PM Role
					angular.forEach(data.roleUserList, function (roleUser, indeks) {
						if (roleUser.role.appCode =="PM" && keepGoing)
							{
								keepGoing = false;
								RequestService.doSignInUserPMDashboard(data, roleUser);
							}
					})
					
					// check if user has CM Role
					angular.forEach(data.roleUserList, function (roleUser, indeks) {
						
						if (roleUser.role.appCode =="CM" && keepGoing)
							{
								keepGoing = false;
								RequestService.doSignInUserCMDashboard(data, roleUser.role);
							}
					})
					
					angular.forEach(data.appCodeList, function (appCode) {
						
						if (appCode.replace(" ", "").toUpperCase() =="CM")
							{
								$rootScope.hasCM =true;
							}
						if (appCode.replace(" ", "").toUpperCase() =="PM")
							{
								$rootScope.hasPM =true;
							}
					})
					
					
					
					if (keepGoing)
						{	
						alert('Error no role kode defined!');
//						  window.location.href = "https://eprocdev.kimiafarma.co.id/ProMISE-KimiaFarma/dashboard.promise";
						  window.location.href = "http://10.10.10.101/kai/dashboard.promise";  
						}
				}
			else
				{
					if($rootScope.cartList.length > 0) {
						$scope.onTimeout = function(){
							//$state.go('page.login');
							var modalInstance = $modal.open({
					            templateUrl: 'loginform.html',
					            controller: 'LoginController as form',
					            resolve: {
					                items: function () {

					                }
					            }
					        });
					    }
						var mytimeout = $timeout($scope.onTimeout, 1000);
					}
					
				}
		})
	//}
	
	//ReloginController.$inject = ['$scope', '$rootScope', '$timeout', '$state','RequestService','$cookies','$log','$modal'];

//})();
	}]);