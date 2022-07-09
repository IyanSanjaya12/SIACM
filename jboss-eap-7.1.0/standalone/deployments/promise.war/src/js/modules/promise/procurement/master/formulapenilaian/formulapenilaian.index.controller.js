/**=========================================================
 * Module: JabatanController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('FormulaPenilaianController', FormulaPenilaianController);

	function FormulaPenilaianController(ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state) {
		
		var vm = this; 
		vm.loading = true;
		
		$scope.getFormulaPenilaian = function () { 
			RequestService.doGET('/procurement/master/FormulaPenilaianServices/getFormulaPenilaianList')
			.then(function successCallback(data) {
				vm.formulaPenilaianList = data;
				vm.loading = false;
			}, function errorCallback(response) {				 
				RequestService.informError('Terjadi kesalahan pada system!');
				vm.loading = false;
			});			 
		}
		
		$scope.getFormulaPenilaian();
		
		$scope.edit = function () { 
			$state.go('app.promise.procurement-master-formulapenilaian-view');
		};
		
		$scope.formulaPenilaian = function(fp){
			var result = '';
			if(fp != null && fp != ''){
				result = fp.fPBatasKiri + ' ';

				if(fp.fPPersamaanKiri == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKiri == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKiri == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKiri == 'lt'){ result = result + ' <';}

				result = result + ' x ';
								
				if(fp.fPPersamaanKanan == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKanan == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKanan == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKanan == 'lt'){ result = result + ' <';}

				result = result + ' ' + fp.fPBatasKanan;
				
			}
			return result;
		}
	}

	FormulaPenilaianController.$inject = ['ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state'];

})();