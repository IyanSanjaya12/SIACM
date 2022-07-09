(function () {
    'use strict';

    angular
        .module('naut')
        .controller('BonusIndexController', BonusIndexController);

    function BonusIndexController(RequestService, $scope, $http, $rootScope, $resource, $location, $state, ModalService) {
        var vm = this;

        $scope.getDataBonusList = function () {
            vm.loading = true;
            RequestService.doGET('/master/bonus/getBonusList')
            .then(function success(data) {
				vm.bonusList = data;
				vm.loading = false;
			}, function error(response) {
				RequestService.informInternalError();
				vm.loading = false;
			});
        }
        $scope.getDataBonusList();
        
        
        
    }

    BonusIndexController.$inject = ['RequestService', '$scope', '$http', '$rootScope', '$resource', '$location', '$state', 'ModalService'];

})();
