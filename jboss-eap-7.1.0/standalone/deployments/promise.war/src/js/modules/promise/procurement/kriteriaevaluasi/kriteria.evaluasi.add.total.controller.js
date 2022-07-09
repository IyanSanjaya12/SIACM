(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaEvaluasiTambahController', KriteriaEvaluasiTambahController);

    function KriteriaEvaluasiTambahController($scope, $http, $rootScope, $resource, $location, toaster, $modal, ngTableParams) {
        var vm = this;
        vm.adminCheck = [];
        vm.teknisCheck = [];
        $scope.btnKriteriaHarga = false;
        $scope.btnTambahKriteria = true;
        $scope.tabAdmin = true;
        $scope.tabTeknis = true;
        $scope.tabHarga = true;

        vm.tabActiveStatus = {
            tabAdministrasi: true,
            tabTeknis: false,
            tabHarga: false
        }

        if ($rootScope.flagKriteriaHarga !== undefined && $rootScope.flagKriteriaHarga == 1) {
            //console.log('flag '+$rootScope.flagKriteriaHarga);    
            vm.tabActiveStatus.tabAdministrasi = false;
            vm.tabActiveStatus.tabTeknis = false;
            vm.tabActiveStatus.tabHarga = true;
            $scope.btnKriteriaHarga = true;
            $scope.btnTambahKriteria = false;
            $rootScope.flagKriteriaHarga = undefined;
        }

        vm.goToTabAdministrasi = function () {
            vm.tabActiveStatus.tabAdministrasi = true;
            vm.tabActiveStatus.tabTeknis = false;
            vm.tabActiveStatus.tabHarga = false;
        }

        vm.goToTabTeknisPrev = function () {
            if (vm.teknisCheck.length > 0) {
                if (confirm('Pilihan Teknis harus dikosongkan, ingin mengosongkan?')) {
                    for (var i = 0; i < vm.masterKriteriaTeknisList.length; i++) {
                        vm.masterKriteriaTeknisList[i].active = false;
                    }
                    vm.teknisCheck = [];
                    vm.tabActiveStatus.tabAdministrasi = true;
                    vm.tabActiveStatus.tabTeknis = false;
                    vm.tabActiveStatus.tabHarga = false;
                } else {
                    alert('Tidak dapat kembali.');
                }
            } else {
                vm.tabActiveStatus.tabAdministrasi = true;
                vm.tabActiveStatus.tabTeknis = false;
                vm.tabActiveStatus.tabHarga = false;
            }
        }

        vm.goToTabTeknis = function () {
            $scope.getTab01Validation();
        }

        vm.goToTabHargaPrev = function () {
            vm.tabActiveStatus.tabAdministrasi = false;
            vm.tabActiveStatus.tabTeknis = true;
            vm.tabActiveStatus.tabHarga = false;
        }

        vm.goToTabHarga = function () {
            $scope.getTab02Validation();
        }

        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaAdministrasiServices/getList', {
            ignoreLoadingBar: true
        }).success(
            function (data, status, headers, config) {
                vm.masterKriteriaAdministrasiList = data;
                if ($rootScope.kriteriaEvaluasi.length > 0) {
                    for (var i = 0; i < $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length; i++) {
                        for (var j = 0; j < vm.masterKriteriaAdministrasiList.length; j++) {
                            if ($rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].id == vm.masterKriteriaAdministrasiList[j].id) {
                                vm.masterKriteriaAdministrasiList[j].active = true;
                                vm.masterKriteriaAdministrasiList[j].nilai = $rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].nilai;
                                $scope.changeAdminCheck(vm.masterKriteriaAdministrasiList[j]);
                            }
                        }
                    }
                }
                vm.tableAdminList = new ngTableParams({
                    page: 1, // show first page
                    count: 7 // count per page
                }, {
                    total: vm.masterKriteriaAdministrasiList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.masterKriteriaAdministrasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {});

        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaTeknisServices/getList', {
            ignoreLoadingBar: true
        }).success(
            function (data, status, headers, config) {
                vm.masterKriteriaTeknisList = data;
                if ($rootScope.kriteriaEvaluasi.length > 0) {
                    for (var i = 0; i < $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.length; i++) {
                        for (var j = 0; j < vm.masterKriteriaTeknisList.length; j++) {
                            if ($rootScope.kriteriaEvaluasi[0].kriteriaTeknis[i].id == vm.masterKriteriaTeknisList[j].id) {
                                vm.masterKriteriaTeknisList[j].active = true;
                                vm.masterKriteriaTeknisList[j].nilai =$rootScope.kriteriaEvaluasi[0].kriteriaTeknis[i].nilai;
                                $scope.changeTeknisCheck(vm.masterKriteriaTeknisList[j]);
                            }
                        }
                    }
                }
                vm.tableTeknisList = new ngTableParams({
                    page: 1, // show first page
                    count: 7 // count per page
                }, {
                    total: vm.masterKriteriaTeknisList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(vm.masterKriteriaTeknisList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {});

        // put data harga from session
        if ($rootScope.kriteriaEvaluasi.length > 0) {
            vm.batasAtas = $rootScope.kriteriaEvaluasi[0].batasAtas;
            vm.batasBawah = $rootScope.kriteriaEvaluasi[0].batasBawah;
        }

        // for save the data from checkbox list to sesions
        $scope.changeAdminCheck = function (list, active) {
            //console.log("check admin");
            //console.log(list);
            if (list.active) {
                vm.adminCheck.push(list);
            } else {
                vm.adminCheck.splice(vm.adminCheck.indexOf(list), 1);
            }
        }
        $scope.changeTeknisCheck = function (list, active) {
            if (list.active) {
                vm.teknisCheck.push(list);
            } else {
                vm.teknisCheck.splice(vm.teknisCheck.indexOf(list), 1);
            }
        }

        // check data binding in session for update
        $scope.checkDataBinding = function () {
            if ($rootScope.kriteriaEvaluasi.length > 0) {
                if (vm.adminCheck.length > 0) {
                    $rootScope.kriteriaEvaluasi[0].kriteriaAdmin = [];
                    for (var j = 0; j < vm.adminCheck.length; j++) {
                        $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.push(vm.adminCheck[j]);
                    }
                }
                if (vm.teknisCheck.length > 0) {
                    $rootScope.kriteriaEvaluasi[0].kriteriaTeknis = [];
                    for (var k = 0; k < vm.teknisCheck.length; k++) {
                        $rootScope.kriteriaEvaluasi[0].kriteriaTeknis.push(vm.teknisCheck[k]);
                    }
                }
                if (vm.batasAtas > 0) {
                    $rootScope.kriteriaEvaluasi[0].batasAtas = vm.batasAtas;
                } else {
                    toaster.pop('warning', 'Kesalahan', 'Nilai Ambang Batas atas belum terisi');
                }
                if (vm.batasBawah > 0) {
                    $rootScope.kriteriaEvaluasi[0].batasBawah = vm.batasBawah;
                } else {
                    toaster.pop('warning', 'Kesalahan', 'Nilai Ambang Batas Bawah belum terisi');
                }
            } else {
                $scope.bindingData();
            }
        }

        // binding data with item to session
        $scope.bindingData = function () {
            vm.dataKriteria = {
                kriteriaAdmin: vm.adminCheck,
                kriteriaTeknis: vm.teknisCheck,
                batasAtas: vm.batasAtas,
                batasBawah: vm.batasBawah
            }
            $rootScope.kriteriaEvaluasi.push(vm.dataKriteria);
        }

        //validation of check Kriteria
        $scope.getTab01Validation = function () {
            var validationTab01 = false;
            if ($rootScope.kriteriaEvaluasi.length > 0) {
                if ($rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length > 0) {
                    validationTab01 = true;
                    vm.tabActiveStatus.tabAdministrasi = false;
                    vm.tabActiveStatus.tabTeknis = true;
                    vm.tabActiveStatus.tabHarga = false;
                } else {
                    validationTab01 = vm.validationCheck(vm.adminCheck);
                }
            } else {
                validationTab01 = vm.validationCheck(vm.adminCheck);
            }
            return validationTab01;
        }
        $scope.getTab02Validation = function () {
            var validationTab02 = false;
            if ($rootScope.kriteriaEvaluasi.length > 0) {
                if ($rootScope.kriteriaEvaluasi[0].kriteriaTeknis.length > 0) {
                    validationTab02 = true;
                    vm.tabActiveStatus.tabAdministrasi = false;
                    vm.tabActiveStatus.tabTeknis = false;
                    vm.tabActiveStatus.tabHarga = true;
                } else {
                    validationTab02 = vm.validationCheck(vm.teknisCheck);
                }
            } else {
                validationTab02 = vm.validationCheck(vm.teknisCheck);
            }
            return validationTab02;
        }
        vm.validationCheck = function (object) {
            var valid = false;
            if (object.length <= 0) {
                toaster.pop('warning', 'Kesalahan', 'Minimal 1 Kriteria di pilih!');
            } else {
                valid = true;
                if (vm.adminCheck.length > 0) {
                    vm.tabActiveStatus.tabAdministrasi = false;
                    vm.tabActiveStatus.tabTeknis = true;
                    vm.tabActiveStatus.tabHarga = false;
                }
                if (typeof vm.teknisCheck !== 'undefined' && vm.teknisCheck.length > 0) {
                    vm.tabActiveStatus.tabAdministrasi = false;
                    vm.tabActiveStatus.tabTeknis = false;
                    vm.tabActiveStatus.tabHarga = true;
                }
            }
            return valid;
        }

        //URL Link
        vm.go = function (path) {
            $location.path(path);
        }
    }

    KriteriaEvaluasiTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', 'ngTableParams'];

})();