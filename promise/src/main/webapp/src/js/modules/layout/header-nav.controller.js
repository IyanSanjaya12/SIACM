/**=========================================================
 * Module: HeaderNavigationController
 * Controls the header navigation
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HeaderNavigationController', HeaderNavigationController);

    function HeaderNavigationController($scope, $rootScope, $localStorage, $state, $http, RequestService) {

        $scope.headerMenuCollapsed = true;
        
        $scope.isLanguageOpened = false;
        
        $scope.toggleHeaderMenu = function () {
            $scope.headerMenuCollapsed = !$scope.headerMenuCollapsed;
        };
        
        $scope.getDashboardPath=function() {
                $rootScope.dashboardPath="app.dashboard-default"
        }
        $scope.getDashboardPath()
        // Adjustment on route changes
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            $scope.headerMenuCollapsed = true;
        });

        $scope.btnLogout = function () {
            // set cartList to empty when user logout
            $rootScope.cartList = [];
            RequestService.doSignOut();
        }
        
        $scope.backToPortal = function () {
        	$rootScope.dashboardBreadcrumb = null;
        	$state.go('page.portal');
        }

        $scope.btnHome = function () {
           $state.go(rootScope.dashboardPath);
        }
        
        $scope.showTranslate = function() {
        	var elm = document.getElementById('showLanguage');
        	if(!$scope.isLanguageOpened) {
        		elm.classList.add('open');
        		$scope.isLanguageOpened = true;
        	} else {
        		elm.classList.remove('open');
        		$scope.isLanguageOpened = false;
        	}
        }
        $scope.edit = function (user, roleUser ) {
        	$state.go('app.promise.procurement-master-user-view', {
        		user : user,
        		roleUser:roleUser,
        		toDo : 'edit'
        	});
       }
    }
    HeaderNavigationController.$inject = ['$scope', '$rootScope', '$localStorage', '$state', '$http', 'RequestService'];

})();