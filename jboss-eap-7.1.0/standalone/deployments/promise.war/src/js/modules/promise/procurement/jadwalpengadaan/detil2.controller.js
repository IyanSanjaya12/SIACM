/**=========================================================
 * Module: JadwalPengadaanDetilController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('JadwalPengadaanDetil2Controller', JadwalPengadaanDetil2Controller);

    function JadwalPengadaanDetil2Controller(ModalService, $scope, $http, $rootScope, $resource, $location, ngTableParams, $modal, $filter, RequestService, $state) {
        var vm = this;
    	$scope.loading = false;
        $scope.isMaterialNotEmpty = false;
        $scope.isJasaNotEmpty = false;
        $scope.pengadaan = $rootScope.pengadaan;
                      
        //Hari Libur
        $scope.getHariLibur = function () {
            $scope.loadingHariLibur = true;
            RequestService.doGET('/procurement/master/hari-libur/get-list')
                .then(function success(data,
                    status, headers, config) {
                    for (var i = 0; i < data.length; i++) {
                        var date = new Date(data[i].tanggal);
                        date.setHours(12);
                        data[i].tanggal = date;
                    }
                    $rootScope.hariLiburList = data;
                    //console.log('dataHariLibur'+JSON.stringify(data));
                    $scope.loadingHariLibur = false;
                    return data;
                }),function error(data, status, headers, config) {
                    $scope.loadingHariLibur = false;
                };
        }
        $scope.getHariLibur();
        
        //cek disable weekend
        $scope.disableWeekEndList==null;
        $scope.getDisableWeekEndData= function () {
            RequestService.doGET('/procurement/master/parameter/get-name/WEEKEND_DISABLE')
                .then(function success(data,status, headers, config) {
                    $scope.disableWeekEndList = data;
                    //console.log('dataHariLibur'+JSON.stringify(data));
                }),function error (data, status, headers, config) {
                	console.log('error getDisableWeekEndData')
                };
        }
        $scope.getDisableWeekEndData();
        
        
        //Jadwal Pengadaan
        $scope.getJadwalPengadaan = function (pengadaanObj) {
            $scope.loadingJadwalPengadaan = true;
            RequestService.doGET('/procurement/jadwalPengadaanServices/getByPengadaanId/' + pengadaanObj.id)
                .then(function success(data, status, headers, config) {
                    if (data.length == 0) {
                        //get jadwal master by alur
                        /*$http.get($rootScope.backendAddress + '/procurement/regJadwalPengadaanServices/getRegisterJadwalByAlurPengadaanIdAndKualifikasi/' + pengadaanObj.alurPengadaan.id + '/' + 0)*/
                    	
                    	//get auto jadwal jika tahapan == inisialisasi pengadaan
                    	$scope.date = new Date();
                    	$scope.dateToday =$filter('date')($scope.dateToday, 'yyyy-MM-dd');
                    	//console.log('nilai Date : '+$scope.date);
                    	RequestService.doGET('/procurement/regJadwalPengadaanServices/getJadwalDTOByAlurPengadaanIdWithPrakualifikasi/' + pengadaanObj.alurPengadaan.id + '/' + 0+'/'+$scope.dateToday)
                            .then(function success(data, status, headers, config) {
                                $scope.statusJadwal = false;
                                $scope.list = [];
                                for (var i = 0; i < data.length; i++) {
                                    $scope.list.push($scope.generateObject(data[i]));
                                    $scope.proccessData(data[i], false, data[i].isLokasi);
                                }
                                $scope.jadwalPengadaanList = data;
                                //console.log('isi jadwal pengadaan list DTO'+ JSON.stringify($scope.jadwalPengadaanList ));
                                $scope.loadingJadwalPengadaan = false;
                            })
                            , function error (data, status, headers, config) {
                                $scope.loadingJadwalPengadaan = false;
                            };
                    } else {

                        $scope.statusJadwal = true;
                        $scope.list = [];

                        $scope.jadwalPengadaanList = data;
                        //console.log('isi jadwal pengadaan list '+ JSON.stringify($scope.jadwalPengadaanList ));
                        angular.forEach(data, function (value, indeks) {
                            $scope.list.push($scope.generateObject(value));
                            RequestService.doGET('/procurement/jadwalPengadaanServices/getRegisterJadwalByTahapanPengadaanId/' + value.tahapanPengadaan.id)
                                .then(function success(datax, status, headers, config) {
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
        var toggleMax = function (tgl) {
            $scope.maxDate = new Date(tgl);
        }

        $scope.disabled = function (date, mode) {
            var today = date.getTime();
            var valid = false;
            for (var i = 0; i < $rootScope.hariLiburList.length; i++) {
                if (today == $rootScope.hariLiburList[i].tanggal.getTime()) {
                    return true;
                }

            }
            
            if($scope.disableWeekEndList.nilai=='true'){
            	 return  (mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6)); 
            }
        };
        
        
        $scope.toggleMin = function () {
            vm.minDate = vm.minDate ? null : new Date();
        };
        vm.maxDate = new Date(2020, 5, 22);
        vm.minDate = new Date(2017, 7, 20);
        $scope.toggleMin();
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        vm.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        vm.format = vm.formats[4];

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
            	
            	data.tglAwal = $filter('date')(data.tglAwal, 'yyyy-MM-dd');
                data.tglAkhir = $filter('date')(data.tglAkhir, 'yyyy-MM-dd');
                
                data['waktuJamAwal'] = 8;
                data['waktuMenitAwal'] = 0;
                data['waktuJamAkhir'] = 17;
                data['waktuMenitAkhir'] = 0;
                //data.tglAwal = undefined;
                //data.tglAkhir = undefined;
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
                tahapanId: data.tahapanPengadaan.tahapan.id,
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
                uri = $rootScope.backendAddress + '/procurement/jadwalPengadaanServices/insertJadwalPengadaan';
            } else {
                uri = $rootScope.backendAddress + '/procurement/jadwalPengadaanServices/updateJadwalPengadaan';
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
                if (index < $scope.list.length - 1) {
                    $scope.simpanJadwal(index + 1);
                } else {
                    $scope.loading = false;
                    /*$location.path("/app/promise/procurement/jadwalpengadaan");*/
                    $state.go("app.promise.procurement-jadwalpengadaan");
                }
            });
        }

        $scope.simpanJadwal = function (index) {
            $scope.proccessing(index);
            $scope.insertToJadwalPengadaan(index);
        }

        $scope.btnSimpanDisable = false;
        $scope.save = function () {
            var valid = $scope.validate();
            if (valid) {
                ModalService.showModalConfirmation().then(function (result) {
                    $scope.btnSimpanDisable = true;
                    $scope.simpanJadwal(0);
                });
            }
        }

        $scope.back = function () {
            $state.go("app.promise.procurement-jadwalpengadaan");
        }

        $scope.autoSetPengumumanOrUndangan = function (index) {
            //find tahapan penguman/undnagan
            for (var i = 0; i < $scope.jadwalPengadaanList.length; i++) {
                if ($scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 5 || $scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 10130200) {
                    //console.log("auto set copy pengambilan document")
                    $scope.jadwalPengadaanList[i].tglAwal = $scope.jadwalPengadaanList[index].tglAwal;
                    $scope.jadwalPengadaanList[i].waktuJamAwal = $scope.jadwalPengadaanList[index].waktuJamAwal;
                    $scope.jadwalPengadaanList[i].waktuMenitAwal = $scope.jadwalPengadaanList[index].waktuMenitAwal;
                    
                    $scope.jadwalPengadaanList[i].tglAkhir = $scope.jadwalPengadaanList[index].tglAkhir;
                    $scope.jadwalPengadaanList[i].waktuJamAkhir = $scope.jadwalPengadaanList[index].waktuJamAkhir;
                    $scope.jadwalPengadaanList[i].waktuMenitAkhir = $scope.jadwalPengadaanList[index].waktuMenitAkhir;
                    break;
                }
            }
        }

        $scope.validateLogicDate = function (i) {
            if (typeof $scope.jadwalPengadaanList[i].tglAwal !== 'undefined' && typeof $scope.jadwalPengadaanList[i].tglAkhir !== 'undefined') {

                if ($scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 5 || $scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 10130200 || $scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 6) {
                    if ($scope.jadwalPengadaanList[i].tahapanPengadaan.tahapan.id == 6) {
                        //console.log("is pengambilan dokumen!");
                        $scope.autoSetPengumumanOrUndangan(i);
                    }

                    return true;
                } else {
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
                        if (dateY.getTime() > dateX.getTime()) {
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

                if ($scope.validateLogicDate(i) == false) { //validasi tanggal tiap proses tidak boleh sama
                    validate = false;
                }

                
                if (i == $scope.jadwalPengadaanList.length-1) {
                    return validate;
                }
            }
        }
        
        /*$scope.btnTest01 = function(){
            $scope.validate();
            //console.log("cek data : "+JSON.stringify($scope.jadwalPengadaanList));
        }*/

    }

    JadwalPengadaanDetil2Controller.$inject = ['ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'ngTableParams', '$modal', '$filter', 'RequestService', '$state'];

})();