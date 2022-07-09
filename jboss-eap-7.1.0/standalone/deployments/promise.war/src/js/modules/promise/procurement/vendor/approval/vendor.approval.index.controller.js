/**
 * =========================================================
 * Module:
 * VendorApprovalController Author: Reinhard
 * =========================================================
 */

(function() {
    'use strict';

    angular.module('naut').controller('VendorApprovalController',
    		VendorApprovalController);

    function VendorApprovalController($scope, $http, $rootScope, $resource, $location,$filter,$stateParams) {
        var form = this;
        form.loading = true;
       
        

        form.edit = function(vendor) {
            $rootScope.detilvendor = vendor;
            $rootScope.isEditable = true;
            $location.path('/app/promise/procurement/vendor/approval/detail/'+vendor.id);
        };
        
        form.getData = function(){
        	$http.get(
                   /* $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorListByStatusApproal', {*/
        			 $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorListByStatusApproalAndLevelOrganisasi', {
                        ignoreLoadingBar: true
                    }).success(function(data, status, headers, config) {
                    	
                        if($stateParams.status=='blacklist'){
                            form.vendorList = $filter('filter')(data, {statusBlacklist:1}, true);
                        }
                        else if($stateParams.status=='unblacklist'){
                            form.vendorList = $filter('filter')(data, {statusUnblacklist:1}, true);
                        }
                        else if($stateParams.status=='sertifikat'){
                            form.vendorList = $filter('filter')(data, {statusPerpanjangan:1}, true);
                        }
                        else if($stateParams.status=='calon'){
                            form.vendorList = $filter('filter')(data, {status:0}, true);
                        }
                        else{
                            form.vendorList = data;
                        }
                    	form.loading = false;
                }).error(function(data, status, headers, config) {
                	form.loading = false;
                });
        }
        
        form.getData();
        
    }

    VendorApprovalController.$inject = ['$scope', '$http', '$rootScope', '$resource',
        '$location','$filter','$stateParams'
    ];

})();