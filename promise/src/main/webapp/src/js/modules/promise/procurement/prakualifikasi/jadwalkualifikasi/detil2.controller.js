/**=========================================================
 * Module: JadwalKualifikasiDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('JadwalKualifikasiDetil2Controller', JadwalKualifikasiDetil2Controller);

    function JadwalKualifikasiDetil2Controller(ModalService, $scope, $http, $rootScope, $resource, $location, ngTableParams, $modal, $filter) {
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        $scope.pengadaan = $rootScope.pengadaan;

        //Jadwal Pengadaan
        $scope.getJadwalPengadaan = function (pengadaanObj) {
            $scope.loadingJadwalPengadaan = true;
            $http.get($rootScope.backendAddress + '/procurement/prakualifikasi/jadwalKualifikasiServices/getByPengadaanId/' + pengadaanObj.id)
                .success(function (data, status, headers, config) {
                    if (data.length == 0) {
                        //get jadwal master by alur and status flag isPrakualifikasi
                        var isPrakualifikasi = 0;
                        if(pengadaanObj.kualifikasiPengadaan.id==1)
                            isPrakualifikasi = 1;
                        $http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/getRegisterJadwalByAlurPengadaanIdAndKualifikasi/' + pengadaanObj.alurPengadaan.id+'/'+isPrakualifikasi)
                            .success(function (data, status, headers, config) {
                                $scope.statusJadwal = false;
                                $scope.list = [];
                                for (var i = 0; i < data.length; i++) {
                                    $scope.list.push($scope.generateObject(data[i]));
                                    $scope.proccessData(data[i], false, data[i].isLokasi);
                                }
                                $scope.jadwalPengadaanList = data;
                                $scope.loadingJadwalPengadaan = false;
                            })
                            .error(function (data, status, headers, config) {
                                $scope.loadingJadwalPengadaan = false;
                            });
                    } else {

                        $scope.statusJadwal = true;
                        $scope.list = [];

                        $scope.jadwalPengadaanList = data;
                        angular.forEach(data, function (value, indeks) {
                            $scope.list.push($scope.generateObject(value));
                            $http.get($rootScope.backendAddress + '/procurement/jadwalPengadaanServices/getRegisterJadwalByTahapanPengadaanId/' + value.tahapanPengadaan.id)
                                .success(function (datax, status, headers, config) {
                                    $scope.proccessData(value, true, datax[0].isLokasi);
                                    $scope.loadingJadwalPengadaan = false;
                                })
                        })
                    }
                })
        }
        $scope.getJadwalPengadaan($scope.pengadaan);

        // Datepicker
        var form = this;

        form.disabled = function (date, mode) {
            var today = date.getTime();
            var valid = false;
            for (var i = 0; i < $rootScope.hariLiburList.length; i++) {
                if (today == $rootScope.hariLiburList[i].tanggal.getTime()) {
                    return true;
                }

            }
        };
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.maxDate = new Date(2020, 5, 22);
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        $scope.tglAwalOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.jadwalPengadaanList[index].tglAwalOpened = true;
        };
        $scope.tglAkhirOpen = function ($event, index) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.jadwalPengadaanList[index].tglAkhirOpened = true;
        };

        /*control the array*/
        $scope.proccessData = function (data, status, isLokasi) {
            if (status) {
                data.tglAwal = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                data.tglAkhir = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                data.waktuJamAwal = parseInt($filter('date')(data.waktuAwal, 'HH'));
                data.waktuMenitAwal = parseInt($filter('date')(data.waktuAwal, 'mm'));
                data.waktuJamAkhir = parseInt($filter('date')(data.waktuAkhir, 'HH'));
                data.waktuMenitAkhir = parseInt($filter('date')(data.waktuAkhir, 'mm'));
                if (typeof data.lokasiStatus !== 'undefined') {
                    data.lokasiStatus = isLokasi;
                } else {
                    data['lokasiStatus'] = isLokasi;
                }
            } else {
                data['waktuJamAwal'] = 8;
                data['waktuMenitAwal'] = 0;
                data['waktuJamAkhir'] = 17;
                data['waktuMenitAkhir'] = 0;
                data.tglAwal = undefined;
                data.tglAkhir = undefined;
                data['lokasiStatus'] = data.isLokasi;
                if (data.isLokasi) {
                    data.lokasi = '';
                }
            }
            data['tglAwalOpened'] = false;
            data['tglAkhirOpened'] = false;
            data['TglAwalMsg'] = '';
            data['errorTglAwal'] = false;
            data['errorWaktuJamAwal'] = false;
            data['errorWaktuMenitAwal'] = false;
            data['errorTglAkhir'] = false;
            data['TglAkhirMsg'] = '';
            data['errorWaktuJamAkhir'] = false;
            data['errorWaktuMenitAkhir'] = false;
        }

        /*formatting the date*/
        $scope.formatDate = function (tgl, jam, menit) {
            var date = null;
            if (typeof tgl === 'string') {
                date = new Date(tgl);
            } else {
                date = tgl;
            }
            date.setHours(jam);
            date.setMinutes(menit);
            return date;
        }

        /*define the object to post*/
        $scope.generateObject = function (data) {
            var object = {
                id: data.id,
                pengadaanId: $scope.pengadaan.id,
                tahapanPengadaanId: data.tahapanPengadaan.id,
                tanggalAwal: data.tglAwal,
                waktuAwal: data.waktuAwal,
                tanggalAkhir: data.tglAkhir,
                waktuAkhir: data.waktuAkhir,
                lokasi: data.lokasi
            };
            return object;
        }

        /*control the array*/
        $scope.proccessing = function (index) {
            var object = $scope.list[index];
            var dateAwal = $scope.formatDate($scope.jadwalPengadaanList[index].tglAwal, $scope.jadwalPengadaanList[index].waktuJamAwal, $scope.jadwalPengadaanList[index].waktuMenitAwal);
            var dateAkhir = $scope.formatDate($scope.jadwalPengadaanList[index].tglAkhir, $scope.jadwalPengadaanList[index].waktuJamAkhir, $scope.jadwalPengadaanList[index].waktuMenitAkhir);
            object.tanggalAwal = $filter('date')(dateAwal, 'yyyy-MM-dd');
            object.waktuAwal = $filter('date')(dateAwal, 'HH:mm:ss');
            object.tanggalAkhir = $filter('date')(dateAkhir, 'yyyy-MM-dd');
            object.waktuAkhir = $filter('date')(dateAkhir, 'HH:mm:ss');
            object.lokasi = $scope.jadwalPengadaanList[index].lokasi;
        }

        $scope.insertToJadwalPengadaan = function (index) {
            $scope.loading = true;
            var uri = "";
            if (!$scope.statusJadwal) {
                uri = $rootScope.backendAddress + '/procurement/prakualifikasi/jadwalKualifikasiServices/insertJadwalKualifikasi';
            } else {
                uri = $rootScope.backendAddress + '/procurement/prakualifikasi/jadwalKualifikasiServices/updateJadwalKualifikasi';
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
                data: $scope.list[index]
            }).success(function (data, status, headers, config) {
                $scope.loading = false;

            });
        }

        $scope.btnTest = function () {
            //var valid = $scope.validate(); //disable jadwal apapaun dibuka
            var valid = true;
            if (valid) {

				ModalService.showModalConfirmation().then(function (result) {
	                for (var i = 0; i < $scope.list.length; i++) {
	                    $scope.proccessing(i);
	                    $scope.insertToJadwalPengadaan(i);
	                }
	                $location.path("/app/promise/procurement/prakualifikasi/jadwalkualifikasi");
           		});
            }
        }

        $scope.btnKembali = function () {
            $location.path("/app/promise/procurement/prakualifikasi/jadwalkualifikasi");
        }

        $scope.validateLogicDate = function (i) {
            if (typeof $scope.jadwalPengadaanList[i].tglAwal !== 'undefined' && typeof $scope.jadwalPengadaanList[i].tglAkhir !== 'undefined') {
                var tglAwal = $filter('date')($scope.jadwalPengadaanList[i].tglAwal, 'MM-dd-yyyy');
                var tglAkhir = $filter('date')($scope.jadwalPengadaanList[i].tglAkhir, 'MM-dd-yyyy');
                var dateTime1 = tglAwal + ' ' + $scope.jadwalPengadaanList[i].waktuJamAwal + ':' + $scope.jadwalPengadaanList[i].waktuMenitAwal + ':00';
                var dateTime2 = tglAkhir + ' ' + $scope.jadwalPengadaanList[i].waktuJamAkhir + ':' + $scope.jadwalPengadaanList[i].waktuMenitAkhir + ':00';
                var date1 = new Date(dateTime1);
                var date2 = new Date(dateTime2);
                if (date2.getTime() < date1.getTime()) {
                    $scope.jadwalPengadaanList[i].errorTglAkhir = true;
                    $scope.jadwalPengadaanList[i].tglAkhirMsg = 'Tanggal Akhir Harus Lebih Besar';
                    return false;
                } else {
                    $scope.jadwalPengadaanList[i].errorTglAkhir = false;
                }

                if (i > 0) {
                    var tglSebelumnya = $filter('date')($scope.jadwalPengadaanList[i - 1].tglAkhir, 'MM-dd-yyyy');
                    var dateTime3 = tglSebelumnya + ' ' + $scope.jadwalPengadaanList[i - 1].waktuJamAkhir + ':' + $scope.jadwalPengadaanList[i - 1].waktuMenitAkhir + ':00';
                    var dateX = new Date(dateTime1);
                    var dateY = new Date(dateTime3);
                    if (dateY.getTime() < dateX.getTime()) {
                        $scope.jadwalPengadaanList[i].errorTglAkhir = true;
                        $scope.jadwalPengadaanList[i].tglAkhirMsg = 'Tanggal Awal Tahapan ini harus Lebih Besar';
                        return false;
                    } else {
                        $scope.jadwalPengadaanList[i].errorTglAkhir = false;
                        return true;
                    }
                }
            }
        }
        $scope.validate = function () {
            var validate = true;
            for (var i = 0; i < $scope.jadwalPengadaanList.length; i++) {
                /*validate date*/
                if (typeof $scope.jadwalPengadaanList[i].tglAwal === 'undefined') {
                    $scope.jadwalPengadaanList[i].errorTglAwal = true;
                    $scope.jadwalPengadaanList[i].tglAwalMsg = 'Tanggal Awal Harus Diisi';
                    validate = false;
                } else {
                    $scope.jadwalPengadaanList[i].errorTglAwal = false;
                }
                if (typeof $scope.jadwalPengadaanList[i].tglAkhir === 'undefined') {
                    $scope.jadwalPengadaanList[i].errorTglAkhir = true;
                    $scope.jadwalPengadaanList[i].tglAkhirMsg = 'Tanggal Akhir Harus Diisi';
                    validate = false;
                } else {
                    $scope.jadwalPengadaanList[i].errorTglAkhir = false;
                }
                if ($scope.validateLogicDate(i) == false) {
                    validate = false;
                }
            }
            return validate;
        }

    }

    JadwalKualifikasiDetil2Controller.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'ngTableParams', '$modal', '$filter'];

})();
