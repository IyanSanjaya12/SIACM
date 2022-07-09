(function () {
    'use strict';

    angular
        .module('naut')
        .controller('KriteriaEvaluasiMeritTambahController', KriteriaEvaluasiMeritTambahController);

    function KriteriaEvaluasiMeritTambahController($scope, $http, $rootScope, $resource, $location, toaster, $modal, ngTableParams) {
        var vm = this;
        vm.adminCheck = [];
        vm.teknisCheck = [];

        $scope.valueMaksTeknis = 0;
        $scope.valueMaksAdmin = 0;
        $scope.valueTotalTeknis = 0;
        $scope.valueTotalAdmin = 0;
        $scope.btnKriteriaHarga = false;
        $scope.btnTambahKriteria = true;
        $scope.validAdmin = true;
        $scope.validTeknis = true;
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

        vm.goToTabTeknisPrev = function () {
            if (vm.teknisCheck.length > 0) {
                if (confirm('Pilihan Teknis harus dikosongkan, ingin mengosongkan?')) {
                    for (var i = 0; i < vm.masterKriteriaTeknisList.length; i++) {
                        vm.masterKriteriaTeknisList[i].active = false;
                        vm.masterKriteriaTeknisList[i].nilai = "";
                    }
                    vm.teknisCheck = [];
                    $scope.valueTotalTeknis = "";
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
            if (vm.adminCheck.length > 0) {
                if ($scope.valueTotalAdmin >= $scope.valueMaksAdmin) {
                    if ($scope.valueTotalAdmin == 100) {
                        for (var i = 0; i < vm.adminCheck.length; i++) {
                            if (vm.adminCheck[i].nilai !== undefined && vm.adminCheck[i].nilai != null && vm.adminCheck[i].nilai !== "") {

                                vm.tabActiveStatus.tabAdministrasi = false;
                                vm.tabActiveStatus.tabTeknis = true;
                                vm.tabActiveStatus.tabHarga = false;
                                $scope.getTab01Validation();
                            }
                        }
                    } else {
                        toaster.pop('warning', 'Tidak Lulus', 'TOTAL NILAI PEMBOBOTAN ADMINISTRASI harus 100');
                        vm.tabActiveStatus.tabAdministrasi = true;
                        vm.tabActiveStatus.tabTeknis = false;
                        vm.tabActiveStatus.tabHarga = false;
                    }

                } else {
                    toaster.pop('warning', 'Tidak Lulus', 'Nilai Kelulusan Administrasi tidak memenuhi Nilai minimal kelulusan');
                    vm.tabActiveStatus.tabAdministrasi = true;
                    vm.tabActiveStatus.tabTeknis = false;
                    vm.tabActiveStatus.tabHarga = false;
                }
            }
        }

        vm.goToTabHargaPrev = function () {
            vm.tabActiveStatus.tabAdministrasi = false;
            vm.tabActiveStatus.tabTeknis = true;
            vm.tabActiveStatus.tabHarga = false;
        }

        vm.goToTabHarga = function () {
            if (vm.teknisCheck.length > 0) {
                if ($scope.valueTotalTeknis >= $scope.valueMaksTeknis) {
                    if ($scope.valueTotalTeknis == 100) {
                        for (var i = 0; i < vm.teknisCheck.length; i++) {
                            if (vm.teknisCheck[i].nilai !== undefined && vm.teknisCheck[i].nilai != null && vm.teknisCheck[i].nilai !== "") {

                                vm.tabActiveStatus.tabAdministrasi = false;
                                vm.tabActiveStatus.tabTeknis = false;
                                vm.tabActiveStatus.tabHarga = true;
                                $scope.getTab02Validation();
                            }
                        }
                    } else {
                        toaster.pop('warning', 'Tidak Lulus', 'TOTAL NILAI PEMBOBOTAN TEKNIS harus 100');
                        vm.tabActiveStatus.tabAdministrasi = false;
                        vm.tabActiveStatus.tabTeknis = true;
                        vm.tabActiveStatus.tabHarga = false;
                    }
                } else {
                    toaster.pop('warning', 'Tidak Lulus', 'Nilai Kelulusan Teknis tidak memenuhi Nilai minimal kelulusan');
                    vm.tabActiveStatus.tabAdministrasi = false;
                    vm.tabActiveStatus.tabTeknis = true;
                    vm.tabActiveStatus.tabHarga = false;
                }
            }
        }

        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaAdministrasiServices/getList', {
            ignoreLoadingBar: true
        }).success(
            function (data, status, headers, config) {
                vm.masterKriteriaAdministrasiList = data;
                if ($rootScope.kriteriaEvaluasi.length > 0) {
                    for (var i = 0; i < $rootScope.kriteriaEvaluasi[0].kriteriaAdmin.length; i++) {
                        for (var j = 0; j < vm.masterKriteriaAdministrasiList.length; j++) {
                            //NEW - munculkan kriteria yang sudah di add
                            if ($rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].id == vm.masterKriteriaAdministrasiList[j].id) {
                                console.log("list root : "+JSON.stringify($rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i]));
                                console.log("list master : "+JSON.stringify(vm.masterKriteriaAdministrasiList[j]));                            
                                vm.masterKriteriaAdministrasiList[j].active = true;
                                vm.masterKriteriaAdministrasiList[j].nilai =$rootScope.kriteriaEvaluasi[0].kriteriaAdmin[i].nilai;
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
                var sistemEvaluasi = $rootScope.detilPengadaan;
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

        // put data harga from $rootScope
        if ($rootScope.kriteriaEvaluasi.length > 0) {
            vm.batasAtas = $rootScope.kriteriaEvaluasi[0].batasAtas;
            vm.batasBawah = $rootScope.kriteriaEvaluasi[0].batasBawah;
            vm.persenAdmin = $rootScope.kriteriaEvaluasi[0].persenAdmin;
            vm.persenTeknis = $rootScope.kriteriaEvaluasi[0].persenTeknis;
            vm.persenHarga = $rootScope.kriteriaEvaluasi[0].persenHarga;
            $scope.valueMaksAdmin = $rootScope.kriteriaEvaluasi[0].valueMaksAdmin;
            $scope.valueTotalAdmin = $rootScope.kriteriaEvaluasi[0].valueTotalAdmin;
            $scope.valueMaksTeknis = $rootScope.kriteriaEvaluasi[0].valueMaksTeknis;
            $scope.valueTotalTeknis = $rootScope.kriteriaEvaluasi[0].valueTotalTeknis;
        }

        var refreshValidasiAdmin = function (x, totalNilai) {
            if (totalNilai > 100) {
                confirm('Nilai Total Pembobotan sudah melebihi 100.');
                if (confirm) {
                    var indeks = vm.adminCheck.indexOf(x);
                    vm.adminCheck.splice(indeks, 1);
                    var indeksTampilan = vm.masterKriteriaAdministrasiList.indexOf(x);
                    vm.masterKriteriaAdministrasiList[indeksTampilan].active = false;
                    vm.masterKriteriaAdministrasiList[indeksTampilan].nilai = "";
                    if (vm.adminCheck.length > 0) {
                        var valueTotalAdmin = 0.00;
                        for (var i = 0; i < vm.adminCheck.length; i++) {
                            valueTotalAdmin = parseFloat(valueTotalAdmin) + parseFloat(vm.adminCheck[i].nilai);
                        }
                        $scope.valueTotalAdmin = valueTotalAdmin;
                    }
                }
            }

            if (vm.adminCheck.length > 0) {
                for (var i = 0; i < vm.adminCheck.length; i++) {
                    if (vm.adminCheck[i].nilai !== undefined && vm.adminCheck[i].nilai != null && vm.adminCheck[i].nilai !== "") {
                        $scope.validAdmin = false;
                    } else {
                        $scope.validAdmin = true;
                    }
                }
            } else {
                $scope.valueTotalAdmin = 0;
            }
        }

        var refreshValidasiTeknis = function (x, totalNilai) {
            if (totalNilai > 100) {
                confirm('Nilai Total Pembobotan sudah melebihi 100.');
                if (confirm) {
                    var indeks = vm.teknisCheck.indexOf(x);
                    vm.teknisCheck.splice(indeks, 1);
                    var indeksTampilan = vm.masterKriteriaTeknisList.indexOf(x);
                    vm.masterKriteriaTeknisList[indeksTampilan].active = false;
                    vm.masterKriteriaTeknisList[indeksTampilan].nilai = "";
                    if (vm.teknisCheck.length > 0) {
                        var valueTotalTeknis = 0.00;
                        for (var i = 0; i < vm.teknisCheck.length; i++) {
                            valueTotalTeknis = parseFloat(valueTotalTeknis) + parseFloat(vm.teknisCheck[i].nilai);
                        }
                        $scope.valueTotalTeknis = valueTotalTeknis;
                    }
                }
            }
            if (vm.teknisCheck.length > 0) {
                for (var i = 0; i < vm.teknisCheck.length; i++) {
                    if (vm.teknisCheck[i].nilai !== undefined && vm.teknisCheck[i].nilai != null && vm.teknisCheck[i].nilai != "") {
                        $scope.validTeknis = false;
                    } else {
                        $scope.validTeknis = true;
                    }
                }
            } else {
                $scope.valueTotalTeknis = 0;
                $scope.validTeknis = true;
            }
        }

        // for save the data from checkbox list to sesions
        $scope.changeAdminCheck = function (listAdmin, active) {
            if (listAdmin.active) {
                vm.adminCheck.push(listAdmin);
                $scope.bukaAdmin = true;
            } else {
                vm.adminCheck.splice(vm.adminCheck.indexOf(listAdmin), 1);
                $scope.bukaAdmin = false;
                if (vm.adminCheck.length > 0) {
                    var valueTotalAdmin = 0.00;
                    for (var i = 0; i < vm.adminCheck.length; i++) {
                        valueTotalAdmin = parseFloat(valueTotalAdmin) + parseFloat(vm.adminCheck[i].nilai);
                    }
                    $scope.valueTotalAdmin = valueTotalAdmin;
                }
            }
            refreshValidasiAdmin();
        }
        $scope.changeTeknisCheck = function (listTeknis, active) {
            if (listTeknis.active) {
                vm.teknisCheck.push(listTeknis);
                $scope.bukaTeknis = true;
            } else {
                vm.teknisCheck.splice(vm.teknisCheck.indexOf(listTeknis), 1);
                $scope.bukaTeknis = false;
                if (vm.teknisCheck.length > 0) {
                    var valueTotalTeknis = 0.00;
                    for (var i = 0; i < vm.teknisCheck.length; i++) {
                        valueTotalTeknis = parseFloat(valueTotalTeknis) + parseFloat(vm.teknisCheck[i].nilai);
                    }
                    $scope.valueTotalTeknis = valueTotalTeknis;
                }
            }
            refreshValidasiTeknis();
        }

        //validation of check Kriteria
        //validation tabs
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
                if ($scope.valueMaksAdmin == undefined) {
                    toaster.pop('warning', 'Kesalahan', 'Nilai maksimal harus diisi lebih dari 0 dan Maksimal 100');
                    validationTab01 = false;
                } else {
                    validationTab01 = vm.validationCheck(vm.adminCheck);
                }
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
                if ($scope.valueMaksTeknis == undefined) {
                    toaster.pop('warning', 'Kesalahan', 'Nilai maksimal harus diisi lebih dari 0 dan Maksimal 100');
                    validationTab02 = false;
                } else {
                    validationTab02 = vm.validationCheck(vm.teknisCheck);
                }
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
            //validation maksimum value from textbox Nilai Pembobotan Maksimal
        $scope.checkNilaiTeknis = function (value) {
            if (value == undefined) {
                toaster.pop('warning', 'Kesalahan', 'Nilai maksimal harus diisi 0 sampai dengan 100');
            } else {
                $scope.valueMaksTeknis = value;
            }
        }
        $scope.checkNilaiAdmin = function (value) {
            if (value == undefined) {
                toaster.pop('warning', 'Kesalahan', 'Nilai maksimal harus diisi 0 sampai dengan 100');
            } else {
                $scope.valueMaksAdmin = value;
            }
        }

        //set data nilai to input box binding with check box
        //Kriteria Admin
        $scope.setNilaiAdmin = function (list) {
            if (vm.adminCheck.length > 0) {
                var valueTotalAdmin = 0.00;
                for (var i = 0; i < vm.adminCheck.length; i++) {
                    valueTotalAdmin = parseFloat(valueTotalAdmin) + parseFloat(vm.adminCheck[i].nilai);
                }
                $scope.valueTotalAdmin = valueTotalAdmin;
            }
            refreshValidasiAdmin(list, valueTotalAdmin);
        }

        //Kriteria Teknis
        $scope.setNilaiTeknis = function (list, nilai) {
            if (vm.teknisCheck.length > 0) {
                var valueTotalTeknis = 0.00;
                for (var i = 0; i < vm.teknisCheck.length; i++) {
                    valueTotalTeknis = parseFloat(valueTotalTeknis) + parseFloat(vm.teknisCheck[i].nilai);
                }
                $scope.valueTotalTeknis = valueTotalTeknis;
            }
            refreshValidasiTeknis(list, valueTotalTeknis);
        }

        // check data binding in session for update
        $scope.checkDataBinding = function () {
            var ok = true;
            if ($rootScope.kriteriaEvaluasi.length > 0) {
                if (vm.adminCheck.length > 0) {
                    $rootScope.kriteriaEvaluasi[0].kriteriaAdmin= [];
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
                $rootScope.kriteriaEvaluasi[0].batasAtas = vm.batasAtas;
                $rootScope.kriteriaEvaluasi[0].batasBawah = vm.batasBawah;
                $rootScope.kriteriaEvaluasi[0].valueTotalAdmin = $scope.valueTotalAdmin;
                $rootScope.kriteriaEvaluasi[0].valueMaksAdmin = $scope.valueMaksAdmin;
                $rootScope.kriteriaEvaluasi[0].valueTotalTeknis = $scope.valueTotalTeknis;
                $rootScope.kriteriaEvaluasi[0].valueMaksTeknis = $scope.valueMaksTeknis;
                ok = $scope.persenCheck();
                if (ok) {
                    //cek total persen admin,teknis,harga harus 100
                    var totalPersenEvalAdmTekHarga = (vm.persenAdmin*1) + (vm.persenTeknis*1) + (vm.persenHarga*1);
                    //console.log(">> totalPersenEvalAdmTekHarga="+totalPersenEvalAdmTekHarga);
                    if (totalPersenEvalAdmTekHarga == 100) {
                        $rootScope.kriteriaEvaluasi[0].persenAdmin = vm.persenAdmin;
                        $rootScope.kriteriaEvaluasi[0].persenTeknis = vm.persenTeknis;
                        $rootScope.kriteriaEvaluasi[0].persenHarga = vm.persenHarga;
                        vm.go('/app/promise/procurement/kriteriaEvaluasi/view');
                    } else {
                        toaster.pop('warning', 'Kesalahan', 'Jumlah Prosentase Nilai Administrasi, Teknis dan Harga harus 100!');
                        return false;
                    }

                }
            } else {
                ok = $scope.persenCheck();
                if (ok) {
                    var totalPersenEvalAdmTekHarga = (vm.persenAdmin*1) + (vm.persenTeknis*1) + (vm.persenHarga*1);
                    //console.log(">> totalPersenEvalAdmTekHarga="+totalPersenEvalAdmTekHarga);
                    if (totalPersenEvalAdmTekHarga == 100) {
                        $scope.bindingData();
                        vm.go('/app/promise/procurement/kriteriaEvaluasi/view');;
                    } else {
                        toaster.pop('warning', 'Kesalahan', 'Jumlah Prosentase Nilai Administrasi, Teknis dan Harga harus 100!');
                        return false;
                    }
                }
            }
            //console.log("isi session total : " + JSON.stringify($rootScope.kriteriaEvaluasi));
        }

        // binding data with item to session
        $scope.bindingData = function () {
            vm.dataKriteria = {
                kriteriaAdmin: vm.adminCheck,
                kriteriaTeknis: vm.teknisCheck,
                valueTotalAdmin: $scope.valueTotalAdmin,
                valueMaksAdmin: $scope.valueMaksAdmin,
                valueTotalTeknis: $scope.valueTotalTeknis,
                valueMaksTeknis: $scope.valueMaksTeknis,
                batasAtas: vm.batasAtas,
                batasBawah: vm.batasBawah,
                persenAdmin: vm.persenAdmin,
                persenTeknis: vm.persenTeknis,
                persenHarga: vm.persenHarga
            }
            $rootScope.kriteriaEvaluasi.push(vm.dataKriteria);
        }

        // to check validation of persentase administration and friends
        $scope.persenCheck = function () {
            var valid = true;
            if (vm.batasAtas == undefined) {
                vm.batasAtas = 0.00;
            }
            if (vm.batasBawah == undefined) {
                vm.batasBawah = 0.00;
            }
            if (vm.persenAdmin == undefined) {
                toaster.pop('warning', 'Kesalahan', 'Nilai Persentase Administrasi harus diisi lebih dari 0 dan Maksimal 100');
                valid = false;
            }
            if (vm.persenTeknis == undefined) {
                toaster.pop('warning', 'Kesalahan', 'Nilai Persentase Teknis harus diisi lebih dari 0 dan Maksimal 100');
                valid = false;
            }
            if (vm.persenHarga == undefined) {
                toaster.pop('warning', 'Kesalahan', 'Nilai Persentase Harga harus diisi lebih dari 0 dan Maksimal 100');
                valid = false;
            }
            return valid;
        }

        //URL Link
        vm.go = function (path) {
            $location.path(path);
        }
    }

    KriteriaEvaluasiMeritTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', 'ngTableParams'];

})();