/**=========================================================
 * Module: DelegasiPengadaanDetilController.js
 * Author: musisician
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DelegasiPengadaanDetilController', DelegasiPengadaanDetilController);

    function DelegasiPengadaanDetilController($scope, $http, $rootScope, $resource, $location, $filter) {

        var form = this;
        $scope.perencanaan = $rootScope.perencanaan;
        form.delegasi = {};
        form.post = {};

        //panitiaList
        $http.get($rootScope.backendAddress + '/procurement/master/panitiaServices/getPanitiaByUserId/' + $rootScope.userLogin.user.id)
            .success(function (data, status, headers, config) {
                $scope.panitia = data;
                $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + data.cabang.id)
                    .success(function (data, status, headers, config) {
                        $scope.panitiaList = data;
                    });
            });

        //get data existing
        //panitiaList
        $http.get($rootScope.backendAddress + '/procurement/delegasipengadaan/delegasiPengadaanServices/getListByPerencanaan/' + $scope.perencanaan.id)
            .success(function (data, status, headers, config) {
                if (data.length > 0) {
                    form.delegasi = data[0];
                    if (form.delegasi.statusPersetujuan == true) {
                        form.delegasi.statusPersetujuan = {};
                        form.delegasi.statusPersetujuan.id = 1;
                        form.delegasi.statusPersetujuan.nama = "Ya";
                        form.delegasi.statusPersetujuan.nilai = true;
                    } else {
                        form.delegasi.statusPersetujuan = {};
                        form.delegasi.statusPersetujuan.id = 2;
                        form.delegasi.statusPersetujuan.nama = "Tidak";
                        form.delegasi.statusPersetujuan.nilai = false;
                    }
                    //list persetujuan
                    $scope.statusList = [];
                    for (var i = 0; i < 2; i++) {
                        $scope.status = {};
                        $scope.status.id = i + 1;
                        if (i == 0) {
                            $scope.status.nama = "Ya"
                            $scope.status.nilai = true;
                        } else {
                            $scope.status.nama = "Tidak"
                            $scope.status.nilai = false;
                        }
                        $scope.statusList.push($scope.status);
                    }
                } else {
                    //list persetujuan
                    $scope.statusList = [];
                    for (var i = 0; i < 2; i++) {
                        $scope.status = {};
                        $scope.status.id = i + 1;
                        if (i == 0) {
                            $scope.status.nama = "Ya"
                            $scope.status.nilai = true;
                        } else {
                            $scope.status.nama = "Tidak"
                            $scope.status.nilai = false;
                        }
                        $scope.statusList.push($scope.status);
                    }
                }
            });

        //fungsi controller
        $scope.back = function () {
            $location.path("/app/promise/procurement/delegasipengadaan");
        }

        $scope.save = function () {
            if (validate()) {
                saveProccess();
            }
        }

        var validate = function () {
            if (form.delegasi.nomorPendelegasian == undefined || form.delegasi.nomorPendelegasian.length == 0) {
                $scope.nomorError = true;
            } else {
                $scope.nomorError = false;
            }
            if (form.delegasi.tanggalPendelegasian == undefined) {
                $scope.tanggalError = true;
            } else {
                $scope.tanggalError = false;
            }
            if (form.delegasi.panitia == undefined) {
                $scope.panitiaError = true;
            } else {
                $scope.panitiaError = false;
            }
            if ($scope.nomorError == false && $scope.tanggalError == false && $scope.panitiaError == false) {
                return true;
            } else {
                return false;
            }
        }

        var saveProccess = function () {
            $scope.loading = true;
            form.post.tanggalPendelegasian = $filter('date')(form.delegasi.tanggalPendelegasian, 'dd-MM-yyyy');
            form.post.pelaksanaPengadaan = $scope.panitia.id;
            form.post.nomorPendelegasian = form.delegasi.nomorPendelegasian;
            form.post.statusPersetujuan = form.delegasi.statusPersetujuan.nilai;
            form.post.perencanaan = $scope.perencanaan.id;
            var uri = "";
            if (form.delegasi.id == undefined) {
                uri = $rootScope.backendAddress + '/procurement/delegasipengadaan/delegasiPengadaanServices/create';
            } else {
                form.post.id = form.delegasi.id;
                uri = $rootScope.backendAddress + '/procurement/delegasipengadaan/delegasiPengadaanServices/update';
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
                data: form.post
            }).success(function (data, status, headers, config) {
                $scope.loading = false;
                $scope.back();
            }).error(function (data, status, headers, config) {
                alert('error');
            });
        }



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

    }

    DelegasiPengadaanDetilController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter'];

})();
