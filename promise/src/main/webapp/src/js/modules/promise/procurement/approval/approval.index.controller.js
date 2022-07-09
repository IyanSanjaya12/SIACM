(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalController', ApprovalController);

    function ApprovalController($scope, $http, RequestService, $rootScope, $resource, $location, $modal, $stateParams, $state) {
        var vm = this;
		$scope.loading = true;
		vm.catalog={};
		$http.get($rootScope.backendAddress+'/procurement/approvalProcessServices/get-my-worklist')
			.success(function(data){
			vm.ListApproval = data;
			$scope.loading = false;
		})
		.error(function(data){
			$scope.loading = false;
			console.error("error get data approval");
		});
		
		$scope.selectApprovalPR = function(approvalProcess){
			$rootScope.approvalProcess = approvalProcess;
			$location.path('/app/promise/procurement/approval/view/bo')
		}
				
		$scope.selectApprovalPO = function(approvalProcess){
			$rootScope.approvalProcess = approvalProcess;
			$location.path('/app/promise/procurement/approval/view/po')
		}
		
//		vm.editDataCatalog = function (idCatalog) {
//			RequestService.doGET('/procurement/catalog/CatalogServices/get-catalog-by-id',idCatalog).then(function (data) {
//				vm.catalog = data;
//			})
//            $state.go('app.promise.procurement-approval-view-new-catalog', {
//                dataCatalog: vm.catalog
//            });
//        }
		$scope.selectApprovalNewCatalog = function(idCatalog,todo,approvalProcess){
			RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/get-catalog-by-id',idCatalog).then(function (data) {
				vm.catalog = data;
				$state.go('app.promise.procurement-approval-view-new-catalog', {
					dataCatalog			: vm.catalog,
					toDo				: todo,
					dataApprovalProcess	: approvalProcess
				});
			});
		}
        
		$scope.selectApprovalVendor = function(approvalProcess){
			$rootScope.approvalProcess = approvalProcess;
            $rootScope.isEditable = true;
            $state.go('app.promise.procurement-registrasivendor-approval-detail');
        }
        
        $scope.selectApprovalblacklist = function( approvalProcess, valueId){
			$rootScope.approvalProcess = approvalProcess;
            $rootScope.isEditable = true;
            $location.path('/app/promise/procurement/approval/view/blacklistvendor/'+valueId);
        }
        
        $scope.selectApprovalUnblacklist = function(approvalProcess, valueId){
			$rootScope.approvalProcess = approvalProcess;
            $rootScope.isEditable = true;
            $state.go('app.promise.procurement-approval-view-unblacklistvendor');

        }
       
		$scope.selectApprovalUnblacklistVendor = function(approvalProcess){
        	$rootScope.approvalProcess = approvalProcess;
            $scope.dataVendor= $rootScope.detilvendor;
            $rootScope.isEditable = true;
            window.alert("masukin alamatnya di sini");
            //$location.path('/app/promise/procurement/vendor/approval/detail/'+ valueId);
        }
		
		$scope.selectApprovalPerpanjanganTDR = function(approvalProcess){
			$rootScope.approvalProcess = approvalProcess;
            $rootScope.isEditable = true;
            $state.go('app.promise.procurement-perpanjangantdr-approval-detail');
        }
		
		//Approval Edit Vendor Button Edit
		$scope.selectApprovalEditVendor  = function(approvalProcess){
			$rootScope.approvalProcess = approvalProcess;
            $rootScope.isEditable = true;
            $state.go('app.promise.procurement-approval-view-updatedatavendor');
        }

		$scope.go = function(url){
			$location.path(url);
		}
		
		$scope.viewSpec = function(purchaserequestId) {
			$http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getByPurchaseRequestItemId/' + purchaserequestId).success(function(data, status, headers, config) {
				
				$scope.dataItem = data;
				
				var modalInstance = $modal.open({
					templateUrl : '/viewSpec.html',
					controller : ModalViewSpecController,
					scope: $scope
				});
				var message = "INFO :<br/>";
				modalInstance.result.then(function() {

				}, function() {

				});

			})

		};
    }
    ApprovalController.$inject = ['$scope', '$http','RequestService', '$rootScope', '$resource', '$location', '$modal', '$stateParams', '$state'];

})();