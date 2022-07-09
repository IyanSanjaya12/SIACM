/**=========================================================
 * Module: AlokasiAnggaranViewController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlokasiAnggaranViewController', AlokasiAnggaranViewController);

    function AlokasiAnggaranViewController($scope, $http, $rootScope, $resource, $location, $filter) {
        var form = this;
        if (typeof $rootScope.detilAlokasiAnggaran !== 'undefined') {
            form.data = $rootScope.detilAlokasiAnggaran;
        }

        $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getJenisList')
            .success(function (data, status, headers, config) {
                $scope.jenisList = data;
            });

        $http.get($rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/getPeriodeList')
            .success(function (data, status, headers, config) {
                $scope.periodeList = data;
            });

        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function (data, status, headers, config) {
                $scope.mataUangList = data;
            }).error(function (data, status, headers, config) {});

        $http.get($rootScope.backendAddress + '/procurement/master/panitiaServices/getPanitiaByUserId/' + $rootScope.userLogin.user.id)
            .success(function (data, status, headers, config) {
                $scope.panitia = data;
                if (data.length == 0) {
                    $http.get($rootScope.backendAddress + '/procurement/user/get-role-user/' + $rootScope.userLogin.user.id)
                        .success(function (dataRoleUser, status, headers, config) {
                            $scope.panitia = {};
                            $scope.panitia.cabang = dataRoleUser[0].organisasi;
                            $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + dataRoleUser[0].organisasi.id)
                                .success(function (data, status, headers, config) {
                                    $scope.biroPengadaanList = data;
                                });
                        });
                } else {
                    $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + data.cabang.id)
                        .success(function (data, status, headers, config) {
                            $scope.biroPengadaanList = data;
                        });
                }

            });
        // Datepicker
        $scope.disabled = function (date, mode) {
            //            var today = date.getTime();
            //            var valid = false;
            //            for (var i = 0; i < $rootScope.hariLiburList.length; i++) {
            //                if (today == $rootScope.hariLiburList[i].tanggal.getTime()) {
            //                    return true;
            //                }
            //
            //            }
            return false;
        };
        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.maxDate = new Date(2020, 5, 22);
        $scope.toggleMin();
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[4];

        $scope.tglOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglOpened = true;
        };

        $scope.tglMulaiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglMulaiOpened = true;
        };

        $scope.tglSelesaiOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.tglSelesaiOpened = true;
        };



        /* get Next Tahapan */
        //        $scope.getNextTahapan = function () {
        //                $http.get($rootScope.backendAddress + '/procurement/inisialisasi/getNextTahapanPengadaan/' + $scope.detilPengadaan.id, {
        //                    ignoreLoadingBar: true
        //                }).success(function (data, status, headers, config) {
        //                    $scope.nextTahapan = data;
        //                }).error(function (data, status, headers, config) {});
        //            }
        //            /* END get Next Tahapan */
        //        $scope.getNextTahapan();
        //
        //        //update pengadaan
        //        $scope.updatePengadaan = function () {
        //            $scope.loading = true;
        //            $http({
        //                method: 'POST',
        //                url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
        //                headers: {
        //                    'Content-Type': 'application/x-www-form-urlencoded'
        //                },
        //                transformRequest: function (obj) {
        //                    var str = [];
        //                    for (var p in obj)
        //                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        //                    return str.join("&");
        //                },
        //                data: {
        //                    pengadaanId: $scope.detilPengadaan.id,
        //                    tahapanPengadaanId: $scope.nextTahapan
        //                }
        //            }).success(function (data, status, headers, config) {
        //                $scope.loading = false;
        //                $location.path("/app/promise/procurement/spk");
        //            });
        //        };

        $scope.save = function () {
            var date = $filter('date')(form.data.tanggalDraft, 'dd-MM-yyyy');
            var post = {
                id: form.data.id,
                nomorDraft: form.data.nomorDraft,
                tanggalDraft: date,
                tahunAnggaran: form.data.tahunAnggaran,
                jumlah: form.data.jumlah,
                sisaAnggaran: form.data.jumlah,
                isUsed: 0
            }
            if (typeof form.data.biroPengadaan !== 'undefined') {
                post.biroPengadaan = form.data.biroPengadaan.id;
            }
            if (typeof form.data.periodeAnggaran !== 'undefined') {
                post.periodeAnggaran = form.data.periodeAnggaran.id;
            }
            if (typeof form.data.jenisAnggaran !== 'undefined') {
                post.jenisAnggaran = form.data.jenisAnggaran.id;
            }
            if (typeof form.data.mataUang !== 'undefined') {
                post.mataUang = form.data.mataUang.id;
            }

            var uri = '';
            if (typeof post.id !== 'undefined') {
                uri = $rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/update';
            } else {
                uri = $rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/create';
                post.status = 0;
            }
            $http({
                method: 'POST',
                url: uri,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: post
            }).success(function (data, status, headers, config) {
                if ($rootScope.gabungAnggaran == true) {
                    angular.forEach($rootScope.tableGabungAnggaran, function (value, indeks) {
                        var anggaran = {};
                        anggaran.id = value.id;
                        anggaran.parent = data.id;
                        anggaran.isUsed = 1;
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/alokasianggaran/AlokasiAnggaranServices/updateParentAndIsUsed',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: anggaran
                        }).success(function (data, status, headers, config) {
                            $location.path("/app/promise/procurement/alokasianggaran");
                        });
                    })

                    $rootScope.gabungAnggaran = undefined;
                } else {
                    $location.path("/app/promise/procurement/alokasianggaran");
                    $rootScope.gabungAnggaran = undefined;
                }
            });
        }

        $scope.back = function () {
            $location.path("/app/promise/procurement/alokasianggaran");
        }


    }

    AlokasiAnggaranViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();