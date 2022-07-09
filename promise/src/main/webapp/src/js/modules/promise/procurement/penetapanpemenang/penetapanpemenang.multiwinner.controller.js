/**=========================================================
 * Module: PenetapanMultiwinnerController.js
 * Author: MMI
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PenetapanMultiwinnerController', PenetapanMultiwinnerController);

    function PenetapanMultiwinnerController($http, $scope, $rootScope, $resource, $location, $compile) {
        var form = this;
        if (typeof $rootScope.pengadaanPenetapanPemenang !== 'undefined') {
            $scope.pengadaanId = $rootScope.pengadaanPenetapanPemenang.id;
        } else {
            console.log('Error');
        }
        $scope.loading = true;
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.pengadaanId).success(function (data, status, headers, config) {
            form.nextTahapan = data;
        }).error(function (data, status, headers, config) {
            $scope.loading = false;
        });

        $http.get($rootScope.backendAddress + "/procurement/penetapanpemenangperitem/getPenawaranVendorPerItem/" + $scope.pengadaanId)
            .success(function (data) {
                $scope.loading = false;
                //console.log(">> Data "+JSON.stringify(data));
                $scope.dataPenawaran = data;
                //distinct vendor list for combobox
                var uniqueNamesVendor = [];
                $scope.uniqueObjVendor = [];
                for (var i = 0; i < data.length; i++) {
                    if (uniqueNamesVendor.indexOf(data[i].vendor.id) === -1) {
                        $scope.uniqueObjVendor.push(data[i].vendor)
                        uniqueNamesVendor.push(data[i].vendor.id);
                    }
                }

            });

        $http.get($rootScope.backendAddress + "/procurement/inisialisasi/ItemPengadaanServices/getItemPengadanByPengadaanId/" + $scope.pengadaanId)
            .success(function (data) {
                $scope.loading = false;
                $scope.dataItemPengadaan = data;
            })
            .error(function (err) {
                $scope.loading = false;
            });

        //add vendor & kuantitas
        $scope.vendorKuantitas = [];
        $scope.addVendor = function (itemPengadaan) {
            if ($scope.vendorKuantitas.length == 0) {
                var object = {
                    itemPengadaanId: itemPengadaan.id,
                    itemId: itemPengadaan.item.id,
                    vendorKuantitas: [{
                        vendorId: 0,
                        kuantitas: 0,
                        index: 0
                    }]
                }
                $scope.vendorKuantitas.push(object);
                //view
                var rawObject = document.getElementById('vendorList' + itemPengadaan.id);
                var options = '<option value="0" selected>+++ Pilih Vendor ++++</option>';
                for (var i = 0; i < $scope.uniqueObjVendor.length; i++) {
                    options += '<option value="' + $scope.uniqueObjVendor[i].id + '">' + $scope.uniqueObjVendor[i].nama + '</option>';
                }
                var contentHTML = rawObject.innerHTML + '<div class="row" id="rowVendorSelected' + itemPengadaan.id + '_' + 0 + '"><div class="col-md-8"><select ng-model="rowVendorSelected' + itemPengadaan.id + '_' + 0 + '" id="vendorSelected' + itemPengadaan.id + '_' + 0 + '" class="form-control input-sm" ng-change="selectVendor(' + itemPengadaan.id + ',' + 0 + ')">' + options + '</select></div>' +
                    '<div class="col-md-4"><input class="form-control input-sm" type="number" min="1" id="kuantitas' + itemPengadaan.id + '_' + 0 + '" ng-model="kuantitas' + itemPengadaan.id + '_' + 0 + '" ng-change="changeKuantitas(' + itemPengadaan.id + ', ' + 0 + ')"/></div></div>';
                angular.element(rawObject).html($compile(contentHTML)($scope));
            } else {
                var indexFound = -1;
                for (var i = 0; i < $scope.vendorKuantitas.length; i++) {
                    if (itemPengadaan.id == $scope.vendorKuantitas[i].itemPengadaanId) {
                        indexFound = i;
                        break;
                    }
                }

                if (indexFound != -1) {
                    var jumlahVendor = $scope.vendorKuantitas[indexFound].vendorKuantitas.length;
                    var objectVendor = {
                        vendorId: 0,
                        kuantitas: 0,
                        index: jumlahVendor
                    }
                    $scope.vendorKuantitas[indexFound].vendorKuantitas.push(objectVendor);
                    //view
                    var rawObject = document.getElementById('vendorList' + itemPengadaan.id);
                    var options = '<option value="0" selected>+++ Pilih Vendor ++++</option>';
                    for (var i = 0; i < $scope.uniqueObjVendor.length; i++) {
                        options += '<option value="' + $scope.uniqueObjVendor[i].id + '">' + $scope.uniqueObjVendor[i].nama + '</option>';
                    }
                    var contentHTML = rawObject.innerHTML + '<div class="row" id="rowVendorSelected' + itemPengadaan.id + '_' + jumlahVendor + '"><div class="col-md-8"><select ng-model="rowVendorSelected' + itemPengadaan.id + '_' + jumlahVendor + '" id="vendorSelected' + itemPengadaan.id + '_' + jumlahVendor + '" class="form-control input-sm" ng-change="selectVendor(' + itemPengadaan.id + ', ' + jumlahVendor + ')">' + options + '</select></div>' +
                        '<div class="col-md-4"><input class="form-control input-sm" type="number" min="1" ng-model="kuantitas' + itemPengadaan.id + '_' + jumlahVendor + '" id="kuantitas' + itemPengadaan.id + '_' + jumlahVendor + '" ng-change="changeKuantitas(' + itemPengadaan.id + ', ' + jumlahVendor + ')"/></div></div>';
                    angular.element(rawObject).html($compile(contentHTML)($scope));
                } else {
                    var object = {
                        itemPengadaanId: itemPengadaan.id,
                        itemId: itemPengadaan.item.id,
                        vendorKuantitas: [{
                            vendorId: 0,
                            kuantitas: 0,
                            index: 0
                            }]
                    }
                    $scope.vendorKuantitas.push(object);
                    //view
                    var rawObject = document.getElementById('vendorList' + itemPengadaan.id);
                    var options = '<option value="0" selected>+++ Pilih Vendor ++++</option>';
                    for (var i = 0; i < $scope.uniqueObjVendor.length; i++) {
                        options += '<option value="' + $scope.uniqueObjVendor[i].id + '">' + $scope.uniqueObjVendor[i].nama + '</option>';
                    }
                    var contentHTML = rawObject.innerHTML + '<div class="row" id="rowVendorSelected' + itemPengadaan.id + '_' + 0 + '"><div class="col-md-8"><select ng-model="rowVendorSelected' + itemPengadaan.id + '_' + 0 + '" id="vendorSelected' + itemPengadaan.id + '_' + 0 + '" class="form-control input-sm" ng-change="selectVendor(' + itemPengadaan.id + ', ' + 0 + ')">' + options + '</select></div>' +
                        '<div class="col-md-4"><input class="form-control input-sm" type="number" min="1" ng-model="kuantitas' + itemPengadaan.id + '_' + 0 + '" id="kuantitas' + itemPengadaan.id + '_' + 0 + '" ng-change="changeKuantitas(' + itemPengadaan.id + ', ' + 0 + ')"/></div></div>';
                    angular.element(rawObject).html($compile(contentHTML)($scope));
                }
            }
            //cek data
            console.log("Vendor Add : " + JSON.stringify($scope.vendorKuantitas));
        }

        $scope.delVendor = function (itemPengadaan) {
            var indexFound = -1;
            for (var i = 0; i < $scope.vendorKuantitas.length; i++) {
                if (itemPengadaan.id == $scope.vendorKuantitas[i].itemPengadaanId) {
                    indexFound = i;
                    break;
                }
            }
            var jumlahVendor = $scope.vendorKuantitas[indexFound].vendorKuantitas.length;
            $scope.vendorKuantitas[indexFound].vendorKuantitas.pop();
            var element = document.getElementById("rowVendorSelected" + itemPengadaan.id + "_" + (jumlahVendor - 1));
            element.parentElement.removeChild(element);
        }

        $scope.selectVendor = function (itemPengadaanId, index) {
            var vendorSelected = document.getElementById('vendorSelected' + itemPengadaanId + '_' + index);
            var vendorId = vendorSelected.options[vendorSelected.selectedIndex].value;
            console.log('hello : itempengadaanId =>  ' + itemPengadaanId + ', index => ' + index + ', vendor => ' + vendorId);
            for (var i = 0; i < $scope.vendorKuantitas.length; i++) {
                if ($scope.vendorKuantitas[i].itemPengadaanId == itemPengadaanId) {
                    for (var j = 0; j < $scope.vendorKuantitas[i].vendorKuantitas.length; j++) {
                        if ($scope.vendorKuantitas[i].vendorKuantitas[j].index == index) {
                            $scope.vendorKuantitas[i].vendorKuantitas[j].vendorId = vendorId;
                            break;
                        }
                    }
                }
            }
        }

        $scope.changeKuantitas = function (itemPengadaanId, index) {
            var kuantitas = document.getElementById('kuantitas' + itemPengadaanId + '_' + index).value;
            console.log("Hello kuantitas : " + itemPengadaanId + ', index:' + index + ', kuantitas:' + kuantitas);
            for (var i = 0; i < $scope.vendorKuantitas.length; i++) {
                if ($scope.vendorKuantitas[i].itemPengadaanId == itemPengadaanId) {
                    for (var j = 0; j < $scope.vendorKuantitas[i].vendorKuantitas.length; j++) {
                        if ($scope.vendorKuantitas[i].vendorKuantitas[j].index == index) {
                            $scope.vendorKuantitas[i].vendorKuantitas[j].kuantitas = kuantitas;
                            break;
                        }
                    }
                }
            }
            $scope.validationKuantitas();
        }
        $scope.isValid = true;
        $scope.validationKuantitas = function () {
            $scope.isValid = true;
            for (var j = 0; j < $scope.vendorKuantitas.length; j++) {
                //jumlah kuantitas yang sudah diinput
                var totalKuantitas = 0;
                for (var k = 0; k < $scope.vendorKuantitas[j].vendorKuantitas.length; k++) {
                    totalKuantitas = totalKuantitas + ($scope.vendorKuantitas[j].vendorKuantitas[k].kuantitas * 1);
                }
                //find hps item pengadaan
                for (var i = 0; i < $scope.dataItemPengadaan.length; i++) {
                    if ($scope.dataItemPengadaan[i].id == $scope.vendorKuantitas[j].itemPengadaanId) {
                        if (totalKuantitas > ($scope.dataItemPengadaan[i].kuantitas * 1)) {
                            console.log("Jumlah Total Kuantitas : " + totalKuantitas + " | Kuantitas Kebutuhan : " + $scope.dataItemPengadaan[i].kuantitas)
                            alert('Jumlah Item (Kuantitas) melebihi jumlah kebutuhan Item Pengadaan! Jumlah Kebutuhan ' + $scope.dataItemPengadaan[i].item.nama + ' adalah ' + $scope.dataItemPengadaan[i].kuantitas);
                            $scope.isValid = false;
                        }
                        break;
                    }
                }
            }

        }

        //jeniskontrak
        $scope.jenisKontrak = 1; //Normal default
        $http.get($rootScope.backendAddress + '/procurement/master/JenisKontrakServices/findAll')
            .success(function (data) {
                $scope.jenisKontrakList = data;
            })
            .error(function (data) {
                console.error("No data");
            });

        //btnSimpan
        $scope.btnSimpan = function () {
            var isValidForm = true;
            for (var j = 0; j < $scope.vendorKuantitas.length; j++) {
                //jumlah kuantitas yang sudah diinput
                var totalKuantitas = 0;
                for (var k = 0; k < $scope.vendorKuantitas[j].vendorKuantitas.length; k++) {
                    totalKuantitas = totalKuantitas + ($scope.vendorKuantitas[j].vendorKuantitas[k].kuantitas * 1);
                }
                //find hps item pengadaan
                for (var i = 0; i < $scope.dataItemPengadaan.length; i++) {
                    if ($scope.dataItemPengadaan[i].id == $scope.vendorKuantitas[j].itemPengadaanId) {
                        if (totalKuantitas != ($scope.dataItemPengadaan[i].kuantitas * 1)) {
                            isValidForm = false;
                        }
                        break;
                    }
                }
                if (!isValidForm)
                    break;
            }
            if (!isValidForm || $scope.vendorKuantitas.length == 0) {
                $scope.isValid = false;
            } else {
                console.log(">> Final for Save : " + JSON.stringify($scope.vendorKuantitas));
                for (var i = 0; i < $scope.vendorKuantitas.length; i++) {
                    for (var j = 0; j < $scope.vendorKuantitas[i].vendorKuantitas.length; j++) {
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/penetapanpemenangperitem/createPenetapanPemenangPerItemAndPerolehan',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: {
                                pengadaanId: $scope.pengadaanId,
                                itemId: $scope.vendorKuantitas[i].itemId,
                                vendorId: $scope.vendorKuantitas[i].vendorKuantitas[j].vendorId,
                                nilaiKuantitas: $scope.vendorKuantitas[i].vendorKuantitas[j].kuantitas
                            }
                        }).success(function (data, status, headers, config) {
                            $scope.loading = false;
                        });
                    }
                }

                //jenis kontrak
                var postFormPemenang = {
                    pengadaanId: $scope.pengadaanId,
                    jenisKontrakId: $scope.jenisKontrak
                };
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/penetapanpemenangtotal/createjeniskontrak',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: postFormPemenang
                }).success(function (data, status, headers, config) {
                    //console.log("save jenis kontrak");
                });

                //update tahapan
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: {
                        pengadaanId: $scope.pengadaanId,
                        tahapanPengadaanId: form.nextTahapan
                    }
                }).success(function (data, status, headers, config) {
                    $scope.loading = false;
                });

                $location.path('/app/promise/procurement/penetapanpemenang');
            }


        }

        $scope.btnBack = function () {
            $location.path($rootScope.switchBack);
        }

    }
    PenetapanMultiwinnerController.$inject = ['$http', '$scope', '$rootScope', '$resource', '$location', '$compile'];

})();