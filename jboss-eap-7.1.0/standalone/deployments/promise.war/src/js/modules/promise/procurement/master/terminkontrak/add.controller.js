(function () {
    'use strict';

    angular.module('naut').controller('TerminKontrakTambahController', TerminKontrakTambahController);

    function TerminKontrakTambahController($scope, $http, $rootScope, $resource, $location, $filter, toaster) {
        var form = this;
        form.jenisTermin = {};
        form.mataUang = {};
        form.kontrak = {};
        form.tanggal = null;

        //datepicker
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

        form.tanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tanggalOpened = true;
        };

        //kontrak
        $http.get($rootScope.backendAddress + '/procurement/master/KontrakServices/getList').success(function (data, status, headers, config) {
            $scope.kontrakList = data;
        }).error(function (data, status, headers, config) {})

        //mata uang
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list').success(function (data, status, headers, config) {
            $scope.mataUangList = data;
        }).error(function (data, status, headers, config) {})

        //jenis termin
        $http.get($rootScope.backendAddress + '/procurement/master/jenisTerminServices/getList').success(function (data, status, headers, config) {
            $scope.jenisTerminList = data;
        }).error(function (data, status, headers, config) {})

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/terminkontrak');
        }

        form.simpan = function () {
            if (typeof form.tanggal === 'undefined' || form.tanggal == null) {
                alert("Tanggal belum disi.");
            } else if (typeof form.nilai === 'undefined' || form.nilai == '') {
                alert("Nilai belum diisi.");
            } else if (typeof form.nilaiTukar === 'undefined' || form.nilaiTukar == '') {
                alert("Nilai Tukar belum diisi.");
            } else if (typeof form.jenisTermin.selected === 'undefined') {
                alert("Jenis Termin belum dipilih.");
            } else if (typeof form.mataUang.selected === 'undefined') {
                alert("Mata Uang belum dipilih.");
            } else if (typeof form.kontrak.selected == 'undefined') {
                alert("Kontrak belum dipilih");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                    tanggal: $filter('date')(form.tanggal, 'yyyy-MM-dd'),
                    nilai: form.nilai,
                    nilaiTukar: form.nilaiTukar,
                    keterangan: form.keterangan,
                    jenisTermin: form.jenisTermin.selected.id,
                    mataUang: form.mataUang.selected.id,
                    kontrak: form.kontrak.selected.id
                }
                //console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/TerminKontrakServices/create',
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
                        toaster.pop('success', 'Termin Kontrak', 'Simpan ' + data.keterangan + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.tanggal = null;
                    form.nilai = '';
                    form.nilaiTukar = '';
                    form.keterangan = '';
                    form.jenisTermin.selected = undefined;
                    form.mataUang.selected = undefined;
                    form.kontrak.selected = undefined;
                });
            };
        };
    }
    TerminKontrakTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', '$filter', 'toaster'];

})();
