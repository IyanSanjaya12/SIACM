/**=========================================================
 * Module: PelaksanaPengadaanUbahPejabatController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelaksanaPengadaanUbahPejabatController', PelaksanaPengadaanUbahPejabatController);

    function PelaksanaPengadaanUbahPejabatController($http, $rootScope, $scope, $resource, $location, $filter, toaster, RequestService, $state) {
        var form = this;
        var dataPanitiaEdit;
        var validate = false;
        form.divisi = {};
        form.organisasi = {};
        form.pic = {};
        $scope.parentId;
        $scope.notParent = true;

        //divisi
        var getOrganisasi = function (parentId, id) {
            $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-all-by-id/' + parentId + '/' + id).success(function (data, status, headers, config) {
                //console.log(JSON.stringify(data));
                angular.forEach(data, function (value, indeks) {
                    //console.log(JSON.stringify(value.children));
                    $scope.divisiList = value.children;
                })
            }).error(function (data, status, headers, config) {})
        }

        //event btn edit
        if (typeof $rootScope.indeksPanitiaEdit !== 'undefined') {
            dataPanitiaEdit = $rootScope.indeksPanitiaEdit;
            if (typeof $scope.output !== 'undefined' || $scope.output != '' || $scope.output != null) {
                $scope.hasParent = true;
                $scope.notParent = false;
            }
            getOrganisasi(dataPanitiaEdit.cabang.parentId, dataPanitiaEdit.cabang.id);

        }
        //console.log(JSON.stringify(dataPanitiaEdit));

        form.id = dataPanitiaEdit.id;
        $scope.output = dataPanitiaEdit.cabang.nama;
        form.berlakuMulai = dataPanitiaEdit.berlakuMulai;
        form.berlakuSelesai = dataPanitiaEdit.berlakuSelesai;
        form.organisasi = {
            label: $scope.output,
            labelId: dataPanitiaEdit.cabang.id
        }

        //divisi
        $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-all-by-id/' + dataPanitiaEdit.divisi.parentId + '/' + dataPanitiaEdit.divisi.id).success(function (data, status, headers, config) {
            angular.forEach(data, function (value, indeks) {
                //console.log(JSON.stringify(value));
                form.divisi.selected = value;
            })
        }).error(function (data, status, headers, config) {})

        //pejabat pelaksana pengadaan
        $http.get($rootScope.backendAddress + '/procurement/master/pejabatPelaksanaPengadaanServices/getId/' + dataPanitiaEdit.pejabatId).success(function (data, status, headers, config) {
                form.nama = data.nama;
                form.pic.selected = data.pic;
            }).error(function (data, status, headers, config) {})
            /*---------------------------*/

        //tree
        $scope.my_tree_handler = function (branch) {
            $scope.output = branch.label;
            $scope.treeId = branch.id;
            form.organisasi = {
                label: $scope.output,
                labelId: $scope.treeId
            }
            if (typeof $scope.output !== 'undefined' || $scope.output != '' || $scope.output != null) {
                $scope.hasParent = true;
                $scope.notParent = false;
            }
            form.divisi.selected = undefined;
            getOrganisasi(branch.parentId, branch.id);
        };

        var tree;
        $scope.my_tree = tree = {};
        $scope.try_async_load = function (parentId, id) {
            $scope.my_data = [];
            $scope.doing_async = true;
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/organisasi/get-all');
            return remoteTree.get(function (res) {
                $scope.my_data = res.data;
                $scope.doing_async = false;
                return tree.expand_all();
            }).$promise;
        };
        $scope.try_async_load();

        //datepicker
        $scope.chngTglMulai = function () {
            form.berlakuSelesai = null;
        }
        
        form.disabled = function (date, mode) {
            return false;
            //return ( mode === 'day' && ( date.getDay() === 0 ||date.getDay() === 6 ) );
        };
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        //tanggal Awal
        form.tanggalOpenAwal = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tanggalOpenedAwal = true;
        };

        //tanggal Akhir
        form.tanggalOpenAkhir = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tanggalOpenedAkhir = true;
        };

        //role user
        $http.get($rootScope.backendAddress + '/procurement/master/panitiaServices/getListRoleUser').success(function (data, status, headers, config) {
            $scope.picList = data;
            //console.log(JSON.stringify($scope.userRoleList));
        }).error(function (data, status, headers, config) {})

        var simpanPejabat = function (id) {
            var postForm = {
                    id: dataPanitiaEdit.pejabatId,
                    nama: form.nama,
                    panitia: id,
                    pic: form.pic.selected.id
                }
                //console.log(JSON.stringify(postForm));
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/pejabatPelaksanaPengadaanServices/update',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postForm
            }).success(function (data, status, headers, config) {
            	RequestService.informSaveSuccess();
        		$state.go('app.promise.procurement-master-pelaksanapengadaan');
            });
        }

        var simpanPanitia = function () {
            var postForm = {
                    id: form.id,
                    berlakuMulai: $filter('date')(form.berlakuMulai, 'yyyy-MM-dd'),
                    berlakuSelesai: $filter('date')(form.berlakuSelesai, 'yyyy-MM-dd'),
                    cabang: form.organisasi.labelId,
                    divisi: form.divisi.selected.id
                }
                //console.log(JSON.stringify(postForm));
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/panitiaServices/update',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postForm
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    console.log('panitia ID ' + JSON.stringify(data.id));
                    simpanPejabat(data.id);
                }
                $scope.loading = false;
            });
        }

        var validasi = function () {
            var tglAwal = new Date($filter('date')(form.berlakuMulai, 'yyyy-MM-dd'));
            var tglAkhir = new Date($filter('date')(form.berlakuSelesai, 'yyyy-MM-dd'));
            if (typeof $scope.output === 'undefined' || $scope.output == null || $scope.output == '') {
                alert('Kantor / Cabang masih kosong');
                validate = false;
            } else if (form.divisi.selected == undefined) {
                alert('Divisi masih kosong');
                validate = false;
            } else if (typeof form.nama === 'undefined' || form.nama == null || form.nama == '') {
                alert('Nama Pejabat Pelaksana masih kosong');
                validate = false;
            } else if (typeof form.berlakuMulai === 'undefined' || form.berlakuMulai == null) {
                alert('Tanggal Mulai masih kosong');
                validate = false;
            } else if (typeof form.berlakuSelesai === 'undefined' || form.berlakuSelesai == null) {
                alert('Tanggal Selesai masih kosong')
                validate = false;
            } else if (tglAkhir < tglAwal) {
                alert('Tanggal Berlaku Selesai harus lebih besar dari Tanggal Mulai');
                validate = false;
            } else if (form.pic.selected == undefined) {
                alert('nama PIC masih kosong');
                validate = false;
            } else {
                validate = true;
            }
        }

        $scope.simpan = function () {
            validasi();
            if (validate == true) {
                simpanPanitia();
                //console.log(JSON.stringify(form));
            }
        }

        $scope.back = function () {
            $location.path('/app/promise/procurement/master/pelaksanapengadaan');
        }

    }

    PelaksanaPengadaanUbahPejabatController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$filter', 'toaster', 'RequestService', '$state'];

})();
