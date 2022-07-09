(function () {
    'use strict';

    angular.module('naut').controller('AlurPengadaanAddTahapanController', AlurPengadaanAddTahapanController);

    function AlurPengadaanAddTahapanController($scope, $http, $rootScope, $resource, $location, toaster, $state, $stateParams, RequestService, $modal) {
        var vm = this;
        vm.alurPengadaan = ($stateParams.alurPengadaan != undefined) ? $stateParams.alurPengadaan : {};


        // Single List
        RequestService.doGET('/procurement/pengadaan/tahapanPengadaanServices/getListByAlurId/' + vm.alurPengadaan.id)
            .success(function (data) {
                vm.tahapanPengadaanList = data;
                vm.loading = false;
            });

        RequestService.doGET('/procurement/master/tahapan/get-list')
            .success(function (data) {
                vm.tahapanPengadaanListAll = data;
                vm.loading = false;
            });

        $scope.add = function () {
            $scope.sortadata.push({
                id: $scope.sortadata.length + 1,
                name: 'Ronnie Nelson'
            });
        };

        $scope.sortableCallback = function (sourceModel, destModel, start, end) {
         
        };

        $scope.sortableOptions = {
            placeholder: '<div class="box-placeholder p0 m0"><div></div></div>',
            forcePlaceholderSize: true
        };

        vm.find = function () {
            if (vm.tahapanPengadaanList.length > 0) {
                angular.forEach(vm.tahapanPengadaanListAll, function (value, index) {
                    angular.forEach(vm.tahapanPengadaanList, function (value2, index2) {
                        if (value2.tahapan.id == value.id) {
                            vm.tahapanPengadaanListAll.splice(index, 1);
                        }
                    })
                })
            }

            var modalInstance = $modal.open({
                templateUrl: '/modal.html',
                controller: ModalController,
                size: 'lg',
                resolve: {
                    tahapanPengadaanList: function () {
                        return vm.tahapanPengadaanListAll;
                    }
                }
            });
            modalInstance.result.then(function (data) {
                if (data != undefined && data !== null) {
                    var found = false;
                    angular.forEach(vm.tahapanPengadaanList, function (value) {
                        if (value.tahapan.id == data.id) {
                            alert('tahapan ini sudah ada');
                            found = true;
                        } else {

                        }
                    })
                    if (!found) {
                        vm.tahapanPengadaan = {};
                        vm.tahapanPengadaan.tahapan = data;
                        vm.tahapanPengadaanList.push(vm.tahapanPengadaan);
                    }
                }
            });
        }

        var ModalController = function ($scope, $modalInstance, tahapanPengadaanList) {
            $scope.tahapanPengadaanList = tahapanPengadaanList;
            $scope.choose = function (tahapanPengadaan) {
                $modalInstance.close(tahapanPengadaan);
            }
            $scope.ok = function () {
                $modalInstance.close('closed');
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        ModalController.$inject = ['$scope', '$modalInstance', 'tahapanPengadaanList'];

        vm.save = function () {
            RequestService.modalConfirmation().then(function (result) {
                angular.forEach(vm.tahapanPengadaanList, function (value, index) {
                    value.sequence = index + 1;
                })
                var uri = $rootScope.backendAddress + '/procurement/pengadaan/tahapanPengadaanServices/saveUpdateForTahapanPengadaan';
                $http.post(uri, {
                    alurId: vm.alurPengadaan.id,
                    tahapanPengadaanList: vm.tahapanPengadaanList
                }).success(function (data) {
                    $state.go('app.promise.procurement-master-alurpengadaan-add', {
                        alurPengadaan: vm.alurPengadaan
                    });
                }).error(function (error) {
                    console.log('ERROR!')
                });

            });

        }

        vm.back = function () {
            $state.go('app.promise.procurement-master-alurpengadaan-add', {
                alurPengadaan: vm.alurPengadaan
            });
        }

        vm.copy = function () {
            $rootScope.copy = [];
            $rootScope.copy = vm.tahapanPengadaanList;
        }

        vm.paste = function () {
            if ($rootScope.copy != null || $rootScope.copy != undefined)
                vm.tahapanPengadaanList = $rootScope.copy;
        }

        vm.remove = function () {

            var modalInstanceRemove = $modal.open({
                templateUrl: '/modal.html',
                controller: ModalController,
                size: 'lg',
                resolve: {
                    tahapanPengadaanList: function () {
                        return vm.tahapanPengadaanList;
                    }
                }
            });
            modalInstanceRemove.result.then(function (data) {
                if (data != undefined && data !== null) {
                    var found = false;
                    angular.forEach(vm.tahapanPengadaanList, function (value, index) {
                        if (value.tahapan.id == data.tahapan.id) {
                            vm.tahapanPengadaanList.splice(index, 1);
                        }
                    })
                }
            });
        }

        vm.clear = function () {

            RequestService.modalConfirmation().then(function (result) {

                vm.tahapanPengadaanList = [];

            });
        }


    }
    AlurPengadaanAddTahapanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', '$stateParams', 'RequestService', '$modal'];

})();
