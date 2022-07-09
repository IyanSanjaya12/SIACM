(function () {
    'use strict';

    angular.module('naut')
        .controller('KebutuhanMaterialTambah04Controller', KebutuhanMaterialTambah04Controller)
        .filter('propsFilter', propsFilter)
        .filter('formatMoney', formatMoney);

    function KebutuhanMaterialTambah04Controller($scope, $http, $rootScope, $resource, $location, toaster, $filter) {

        var kebutuhanMaterialTambah = this;
        var vm = this;
        $scope.checkedPajak = false;
        $scope.jenisPajak = {};

        vm.go = function (path) {
            $location.path(path);
        };
        
        // Jika Join PR
        if($rootScope.inisialisasiForm.materialList[$rootScope.materialEditId] != undefined) {
        	vm.join = $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId].join;
        }
        
        $scope.changePajak = function (x) {
            if (x == true) {
                $scope.jenisPajak.selected = undefined;
                $scope.checkedPajak = true;
            }
        }

        kebutuhanMaterialTambah.back = function () {
            $location.path('/app/promise/procurement/inisialisasi/add04');
        };

        $scope.kodeMaterial = {};
        $scope.refreshkodeMaterialList = function (kodeMaterial) {
            $scope.loading = true;
            if (kodeMaterial.length == 0)
                kodeMaterial = 0;

            return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial)
                .then(function (response) {
                    $scope.kodeMaterialList = response.data;
                    $scope.loading = false;
                });
        };
        $scope.setKodeMaterialSelected = function () {
            if (typeof $scope.kodeMaterial.selected !== 'undefined') {
                vm.namaMaterial = $scope.kodeMaterial.selected.nama;
                vm.jenisMaterial = $scope.kodeMaterial.selected.itemGroupId.nama;
                vm.satuan = $scope.kodeMaterial.selected.satuanId.nama;
            }
        };

        //Currency
        $scope.mataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                if (typeof $rootScope.materialEditId !== 'undefined') {
                    $scope.mataUang.selected = $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId].mataUang;
                }
            }).error(function (data, status, headers, config) {});

        if ($rootScope.inisialisasiForm.jenisPenawaran.id === 2) {
            $scope.jenisPajak = {};
            $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
                .success(function (data, status, headers, config) {
                    $scope.pajakList = data;
                    if (typeof $rootScope.materialEditId !== 'undefined') {
                        $scope.jenisPajak.selected = $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId].jenisPajak;
                    }
                });
        }

        //HPS
        $scope.calculateTotalHPS = function () {
                if (typeof vm.kuantitas !== 'undefined' && typeof vm.hps !== 'undefined') {
                    vm.totalHps = vm.kuantitas * vm.hps;
                    if ($scope.mataUang.selected !== undefined && $scope.mataUang.selected !== null && $scope.mataUang.selected.id > 1) { //Rupiah Default
                        vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, '.', ',');
                    } else {
                        vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, ',', '.');
                    }
                }
            }
            //Edit
        $scope.setEditMaterial = function () {
            $scope.tipeJenisPenawaran = $rootScope.inisialisasiForm.jenisPenawaran;
            if (typeof $rootScope.materialEditId !== 'undefined') {
                $scope.materialObj = $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId];
                $scope.kodeMaterial.selected = $scope.materialObj.material;
                vm.kuantitas = $scope.materialObj.kuantitas;
                vm.hps = $scope.materialObj.hps;
                vm.keterangan = $scope.materialObj.keterangan;
                $scope.calculateTotalHPS();
                vm.namaMaterial = $scope.kodeMaterial.selected.nama;
                vm.jenisMaterial = $scope.kodeMaterial.selected.itemGroupId.nama;
                vm.satuan = $scope.kodeMaterial.selected.satuanId.nama;

                if ($scope.tipeJenisPenawaran.id === 2) {
                    if ($scope.materialObj.prosentasePajak !== undefined && $scope.materialObj.prosentasePajak !== null && $scope.materialObj.prosentasePajak > 0) {
                        $scope.materialObj.checkStatusPajak = true;
                    }
                }
            } else {
                $scope.materialObj = {};
                $scope.materialObj.checkStatusPajak = false;
            }
        };
        $scope.setEditMaterial();

        //Simpan Material
        $scope.btnSimpanMaterial = function () {
            $scope.kodeMaterialError = false;
            $scope.kuantitasError = false;
            $scope.kuantitasError01 = false;
            $scope.hpsError = false;
            $scope.hpsError01 = false;
            $scope.mataUangError = false;
            $scope.jenisPajakError = false;
            $scope.prosentasePajakError = false;

            var bolehSimpan = true;
            if (typeof $scope.kodeMaterial.selected === 'undefined') {
                $scope.kodeMaterialError = true;
                bolehSimpan = false;
            }

            if (typeof vm.kuantitas === 'undefined') {
                $scope.kuantitasError = true;
                bolehSimpan = false;
            }

            if (vm.kuantitas <= 0) {
                $scope.kuantitasError01 = true;
                bolehSimpan = false;
            }

            if (typeof vm.hps === 'undefined') {
                $scope.hpsError = true;
                bolehSimpan = false;
            }

            if (vm.hps <= 0) {
                $scope.hpsError01 = true;
                bolehSimpan = false;
            }

            if ($scope.mataUang.selected === undefined) {
                $scope.mataUangError = true;
                bolehSimpan = false;
            }

            if ($scope.tipeJenisPenawaran.id === 2) {
                if ($scope.jenisPajak.selected === undefined) {
                    $scope.jenisPajakError = true;
                    bolehSimpan = false;
                }
                if ($scope.materialObj.checkStatusPajak === true) {
                    if ($scope.materialObj.prosentasePajak !== undefined && $scope.materialObj.prosentasePajak !== null && $scope.materialObj.prosentasePajak > 0) {

                    } else {
                        $scope.prosentasePajakError = true;
                        bolehSimpan = false;
                    }
                }
            }

            if (bolehSimpan) {
                var materialObj2 = {
                    material: $scope.kodeMaterial.selected,
                    kuantitas: vm.kuantitas,
                    hps: vm.hps,
                    mataUang: $scope.mataUang.selected,
                    keterangan: vm.keterangan
                }
                if ($scope.tipeJenisPenawaran.id === 2) {
                    materialObj2.jenisPajak = $scope.jenisPajak.selected;
                    if ($scope.materialObj.checkStatusPajak === true) {
                        materialObj2.prosentasePajak = $scope.materialObj.prosentasePajak;
                        materialObj2.nilaiPajak = $scope.materialObj.prosentasePajak * vm.kuantitas * vm.hps / 100;
                    } else {
                        materialObj2.nilaiPajak = $scope.jenisPajak.selected.prosentase * vm.kuantitas * vm.hps / 100;
                    }
                }

                if (typeof $rootScope.materialEditId !== 'undefined') {
                    $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId] = materialObj2;
                } else {
                    $rootScope.inisialisasiForm.materialList.push(materialObj2);
                }
                $location.path('/app/promise/procurement/inisialisasi/add04');
            }
        }
    }

    KebutuhanMaterialTambah04Controller.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter'];

    function formatMoney() {
        return function (number, decimals, dec_point, thousands_sep) {
            var n = !isFinite(+number) ? 0 : +number,
                prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
                sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
                dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
                toFixedFix = function (n, prec) {
                    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
                    var k = Math.pow(10, prec);
                    return Math.round(n * k) / k;
                },
                s = (prec ? toFixedFix(n, prec) : Math.round(n)).toString().split('.');
            if (s[0].length > 3) {
                s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
            }
            if ((s[1] || '').length < prec) {
                s[1] = s[1] || '';
                s[1] += new Array(prec - s[1].length + 1).join('0');
            }
            return s.join(dec);
        }
    };

    function propsFilter() {
        return function (items, props) {
            var out = [];

            if (angular.isArray(items)) {
                items.forEach(function (item) {
                    var itemMatches = false;

                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }

            return out;
        };
    }
})();
