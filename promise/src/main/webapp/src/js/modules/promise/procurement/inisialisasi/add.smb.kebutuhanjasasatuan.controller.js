(function () {
    'use strict';

    angular.module('naut')
        .controller('KebutuhanJasaTambahSMBSatuanController', KebutuhanJasaTambahSatuanController)
        .filter('propsFilter', propsFilter)
        .filter('formatMoney', formatMoney);

    function KebutuhanJasaTambahSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $filter) {

        var KebutuhanJasaTambah = this;
        var vm = this;
        $scope.checked = false;

        vm.go = function (path) {
            $location.path(path);
        };

        KebutuhanJasaTambah.back = function () {
            $location.path('/app/promise/procurement/inisialisasi/add/smb');
        };


        $scope.checkTax = function () {
            if ($scope.checked == false) {
                $scope.checked = true;
                $scope.disabled = true;
            } else {
                $scope.checked = false;
                $scope.disabled = false;
            }
        }

        $scope.pajak = {};
        $http.get($rootScope.backendAddress + '/procurement/master/JenisPajakServices/getJenisPajakList')
            .success(function (data, status, headers, config) {
                $scope.pajakList = data;
                $scope.pajak.selected = {
                    id: data[0].id,
                    nama: data[0].nama,
                    prosentase: data[0].prosentase
                };
            }).error(function (data, status, headers, config) {});

        $scope.checkPajak = function () {
            $scope.calculateTotalHPS();
        }

        //Currency
        $scope.mataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                //console.log(' INFO ' + JSON.stringify(data));
                $scope.mataUang.selected = {
                    id: data[0].id,
                    nama: data[0].nama,
                    kode: data[0].kode
                };
            }).error(function (data, status, headers, config) {});

        //HPS
        $scope.presentase = 0;
        $scope.calculateTotalHPS = function () {
                if (typeof vm.kuantitas !== 'undefined' && typeof vm.hps !== 'undefined') {
                    var total = 0;
                    if ($scope.checked) {
                        total = (vm.hps * vm.kuantitas) + ((vm.hps * vm.kuantitas) * $scope.presentase / 100);
                    } else {
                        if (typeof $scope.pajak.selected !== 'undefined') {
                            total = (vm.hps * vm.kuantitas) + ((vm.hps * vm.kuantitas) * $scope.pajak.selected.prosentase / 100);
                        } else {
                            total = vm.hps * vm.kuantitas;
                        }
                    }
                    vm.totalHps = total;
                    if ($scope.mataUang.selected.id == 1) { //Rupiah Default
                        vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, ',', '.');
                    } else {
                        vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, '.', ',');
                    }
                }
            }
            //Edit
        $scope.setEditJasa = function () {
            if (typeof $rootScope.jasaEditId !== 'undefined') {
                $scope.jasaObj = $rootScope.inisialisasiForm.jasaList[$rootScope.jasaEditId];
                vm.kuantitas = $scope.jasaObj.kuantitas;
                vm.hps = $scope.jasaObj.hps;
                $scope.mataUang.selected = $scope.jasaObj.mataUang;
                $scope.pajak.selected = $scope.jasaObj.jenisPajak;
                vm.keterangan = $scope.jasaObj.keterangan;
                vm.namaJasa = $scope.jasaObj.nama;
                $scope.presentase = $scope.jasaObj.prosentasePajak;
                if ($scope.presentase > 0) {
                    $scope.checked = true;
                    $scope.disabled = true;
                }
                $scope.calculateTotalHPS();
            }
        };
        $scope.setEditJasa();

        //Simpan Jasa
        $scope.btnSimpanJasa = function () {
            $scope.namaError = false;
            $scope.kuantitasError = false;
            $scope.kuantitasError01 = false;
            $scope.hpsError = false;
            $scope.hpsError01 = false;
            if (typeof vm.namaJasa === 'undefined' || vm.namaJasa.length == 0) {
                $scope.namaError = true;
            } else {
                if (typeof vm.kuantitas === 'undefined') {
                    $scope.kuantitasError = true;
                } else {
                    if (vm.kuantitas <= 0) {
                        $scope.kuantitasError01 = true;
                    } else {
                        if (typeof vm.hps === 'undefined') {
                            $scope.hpsError = true;
                        } else {
                            if (vm.hps <= 0) {
                                $scope.hpsError01 = true;
                            } else {
                                $scope.defineObject();
                                $scope.fillToList();
                                $location.path('/app/promise/procurement/inisialisasi/add/smb');
                            }
                        }
                    }
                }
            }
        };

        $scope.defineObject = function () {
            var total = 0;
            if ($scope.checked) {
                total = (vm.hps * vm.kuantitas) + ((vm.hps * vm.kuantitas) * $scope.presentase / 100);
            } else {
                total = (vm.hps * vm.kuantitas) + ((vm.hps * vm.kuantitas) * $scope.pajak.selected.prosentase / 100);
            }
            $scope.jasaObj = {
                nama: vm.namaJasa,
                kuantitas: vm.kuantitas,
                hps: vm.hps,
                mataUang: $scope.mataUang.selected,
                keterangan: vm.keterangan,
                jenisPajak: $scope.pajak.selected,
                totalHps: total,
                prosentasePajak: $scope.presentase
            };
        }

        $scope.fillToList = function () {
            if (typeof $rootScope.jasaEditId !== 'undefined') {
                $rootScope.inisialisasiForm.jasaList[$rootScope.jasaEditId] = $scope.jasaObj;
            } else {
                $rootScope.inisialisasiForm.jasaList = $rootScope.inisialisasiForm.jasaList.concat($scope.jasaObj);
            }
        }
    }

    KebutuhanJasaTambahSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter'];

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
