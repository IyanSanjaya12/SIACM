(function () {
    'use strict';

    angular.module('naut')
        .controller('AddItemFromMasterController', AddItemFromMasterController)
        .filter('propsFilter', propsFilter)
        .filter('formatMoney', formatMoney);

    function AddItemFromMasterController($scope, $http, $rootScope, $resource, $location, toaster, $filter) {

        var kebutuhanMaterialTambah = this;
        var form = this;
        $scope.checkedPajak = false;
        $scope.jenisPajak = {};

       form.go = function (path) {
            $location.path(path);
        };

        $scope.changePajak = function (x) {
            if (x == true) {
                $scope.jenisPajak.selected = undefined;
                $scope.checkedPajak = true;
            }
        }

        kebutuhanMaterialTambah.back = function () {
            $location.path('/app/promise/procurement/order/purchaserequest/add');
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
               form.itemname = $scope.kodeMaterial.selected.nama;
               form.unit = $scope.kodeMaterial.selected.satuanId.nama;
            }
        };
        
       
        

        //Currency
        $scope.mataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
                if (typeof $rootScope.materialEditId !== 'undefined') {
                    $scope.mataUang.selected = $rootScope.materialList[$rootScope.materialEditId].mataUang;
                }
            }).error(function (data, status, headers, config) {});

      //HPS
        $scope.calculateTotalHPS = function () {
                if (typeof form.quantity !== 'undefined' && typeof form.price !== 'undefined') {
                    form.total = form.quantity * form.price;
                    if ($scope.mataUang.selected !== undefined && $scope.mataUang.selected !== null && $scope.mataUang.selected.id > 1) { //Rupiah Default
                        form.totalHpsTxt = $filter('formatMoney')(form.totalHps, 2, '.', ',');
                    } else {
                        form.totalHpsTxt = $filter('formatMoney')(form.totalHps, 2, ',', '.');
                    }
                }
            }
        
        
            //Edit
        $scope.setEditMaterial = function () {
            if (typeof $rootScope.materialEditId !== 'undefined') {
                $scope.materialObj = $rootScope.materialList[$rootScope.materialEditId];
                $scope.kodeMaterial.selected = $scope.materialObj.material;
               form.quantity = $scope.materialObj.quantity;
               form.price = $scope.materialObj.price;
               form.specification = $scope.materialObj.specification;
                $scope.calculateTotalHPS();
               form.itemname = $scope.kodeMaterial.selected.nama;
               form.unit = $scope.kodeMaterial.selected.satuanId.nama;

                
            } else {
                $scope.materialObj = {};
                
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
            //if (typeof $scope.kodeMaterial.selected === 'undefined') {
              //  $scope.kodeMaterialError = true;
                //bolehSimpan = false;
           // }

            if (typeof form.quantity === 'undefined') {
                $scope.kuantitasError = true;
                bolehSimpan = false;
            }

            if (form.quantity <= 0) {
                $scope.kuantitasError01 = true;
                bolehSimpan = false;
            }

            if (typeof form.price === 'undefined') {
                $scope.hpsError = true;
                bolehSimpan = false;
            }

            if (form.price <= 0) {
                $scope.hpsError01 = true;
                bolehSimpan = false;
            }

            if ($scope.mataUang.selected === undefined) {
                $scope.mataUangError = true;
                bolehSimpan = false;
            }

            
            if (bolehSimpan) {
            	
            	
            	var materialObj2 = {
                    material: form.itemname,//$scope.kodeMaterial.selected,
                	//material: mtr,
                    quantity: form.quantity,
                    price: form.price,
                    unit: form.unit,
                    mataUang: $scope.mataUang.selected,
                    specification: form.specification
                }
                

                if (typeof $rootScope.materialEditId !== 'undefined') {
                    $rootScope.materialList[$rootScope.materialEditId] = materialObj2;
                } else {
                    $rootScope.materialList.push(materialObj2);
                }
                $location.path('/app/promise/procurement/order/purchaserequest/add');
            }
        }
    }

    AddItemFromMasterController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter'];

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
