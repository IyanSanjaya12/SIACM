// Please prepare your logic for this code or welcome to the dizzy world
// ----------------------------------------------------------------------

(function() {
    'use strict';

    angular.module('naut').controller('KriteriaEvaluasiViewSatuanController', KriteriaEvaluasiViewSatuanController);

    function KriteriaEvaluasiViewSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $window) {
        var vm = this;
        
        vm.adaListAdmin = true;
        vm.adaListTeknis = true;
        vm.adaListAmbang = true;
        
        var sistemEvaluasi = $rootScope.detilPengadaan;
        
        $scope.kriteriaAminId = 0;
        $scope.kriteriaTeknisId = 0;
        $scope.kriteriaAmbangId = 0;
            
        /* START Pengadaan Detail */
        if(typeof $rootScope.detilPengadaan !== 'undefined'){
            $scope.detilPengadaan = $rootScope.detilPengadaan;
            console.log(JSON.stringify($scope.detilPengadaan));
        } else {
            console.log('INFO Error');   
        }
        $scope.pengadaanId = $scope.detilPengadaan.id;
        $scope.sistemEvaluasiPenawaranId = $scope.detilPengadaan.sistemEvaluasiPenawaran.id;
		/*---END Detail Pengadaan----*/
        
        /* START Bidang Usaha Pengadaan Detail*/
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/BidangUsahaPengadaanServices/getBidangUsahaPengadaanByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            vm.subBidangUsahaByPengadaanIdList = data;
        }).error(function(data, status, headers, config) {

        });
        /*---END Bidang Usaha Pengadaan---*/
        
        /* START Kebutuhan Material Detail*/
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanMaterialByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            vm.itemPengadaanMaterialByPengadaanIdList = data;
        }).error(function(data, status, headers, config) {

        });
        /*---END Rincian Kebutuhan Materrial---*/
        
        /* START Kebutuhan Jasa Detail*/
        $http.get($rootScope.backendAddress + '/procurement/inisialisasi/ItemPengadaanServices/getItemPengadaanJasaByPengadaanId/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            vm.itemPengadaanJasaByPengadaanIdList = data;
        }).error(function(data, status, headers, config) {

        });
        /*---END Rincian Kebutuhan Jasa---*/
        
        /* START get from table Kriteria Evaluasi */
        //Administrasi
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            if(data != 'undefined'){
                vm.adaListAdmin = false;
                angular.forEach(data,function(value,index){
                    $rootScope.isiListAdminFromTable.push(value);
                });
            }
            console.log('INFO listAdmin : ' + JSON.stringify($scope.listAdminFromTable));
        }).error(function(data, status, headers, config) {});
        //Teknis
        $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            if(data != 'undefined'){
                vm.adaListTeknis = false;
                angular.forEach(data,function(value,index){
                    $rootScope.isiListTeknisFromTable.push(value);
                });
            }
        }).error(function(data, status, headers, config) {});
        //Teknis Merit Point
        $http.get($rootScope.backendAddress + '/procurement/kriteriaTeknisMeritPointServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            if(data != 'undefined'){
                vm.adaListTeknis = false;
                angular.forEach(data,function(value,index){
                    $rootScope.isiListTeknisFromTable.push(value);
                });
            }
        }).error(function(data, status, headers, config) {});
        //Ambang Batas
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/getByPengadaanIdList/' + $scope.pengadaanId, {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            if(data != 'undefined'){
                angular.forEach(data,function(value,index){
                    $rootScope.isiListAmbangFromTable.push(value);
                });
                vm.adaListAmbang = false;
            }
        }).error(function(data, status, headers, config) {});
        /*---END get from kriteria Evaluasi---*/
        
        // back to index.html
		vm.back = function(){
			$location.path('/app/promise/procurement/kriteriaEvaluasi');
		};
        
        // check the sistemEvaluasiPengadaan from detailPengadaan for change view Detail Kriteria
        vm.addKriteria = function(kodeItem){
            $rootScope.item = kodeItem.id;
            console.log("$rootScope.kriteriaEvaluasiSatuan : " + JSON.stringify($rootScope.kriteriaEvaluasiSatuan));
            if (sistemEvaluasi.sistemEvaluasiPenawaran.id == 1) { // 1 = sistem gugur, 2 = merit point
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addViewSatuan');
            } else if (sistemEvaluasi.sistemEvaluasiPenawaran.id == 2) {
                //alert('belum tersedia');
                $location.path('/app/promise/procurement/kriteriaEvaluasi/addMeritViewSatuan');
            } else {
                alert('tidak tersedia');
            }
        };
        
        // click the button view from detail form
        vm.viewKriteria = function(kodeItem){
            $rootScope.item = kodeItem.id;
            if(vm.adaListAdmin){
                if($rootScope.kriteriaEvaluasiSatuan.length > 0) {
                    for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++) {
                        vm.hasilList = $rootScope.kriteriaEvaluasiSatuan[i];
                        if($rootScope.item == vm.hasilList.itemId){
                            vm.hasilKriteria = true;
                            break;
                        }
                    }
                    if(vm.hasilKriteria)
                        $location.path('/app/promise/procurement/kriteriaEvaluasi/satuan');
                    else
                        alert("Tidak ada kriteria dengan no ITEM(" + kodeItem.item.kode + ") ini, Silahkan lakukan penambahan");
                } else {
                    alert("Tidak ada kriteria dengan no ITEM(" + kodeItem.item.kode + ") ini, Silahkan lakukan penambahan");
                }
            } else {
                if($rootScope.isiListAdminFromTable.length > 0){
                    for(var i=0; i<$rootScope.isiListAdminFromTable.length; i++){
                        if($rootScope.item == $rootScope.isiListAdminFromTable[i].itemPengadaan.id){
                            $location.path('/app/promise/procurement/kriteriaEvaluasi/satuan');
                        } else {
                            alert("Maaf data tidak ada yang sama dengan Item pengadaan id:("+kodeItem.item.kode+"), kemungkinan data masih DUMMY");
                            break;
                        }
                    }
                } else {
                    alert("Maaf data tidak ada yang sama dengan Item pengadaan id:("+kodeItem.item.kode+"), kemungkinan data masih DUMMY");
                }
            }
        }
        
        //save data Kriteria Administrasi, Teknis and Harga
        $scope.saveData = function() {
            //check data
            var jumlahItem = vm.itemPengadaanJasaByPengadaanIdList.length + vm.itemPengadaanMaterialByPengadaanIdList.length;
            if($rootScope.kriteriaEvaluasiSatuan.length < jumlahItem) {
                window.alert("Kriteria Evaluasi belum terisi semua coba ingat-ingat mana yang belum diisi.....hayooo");
            } else {
                alert("Maaf belum bisa ya Gan, sambil nambahin ItemPengadaanId di BACK_END......hehehe");
                /*for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                    console.log("item id : "+$rootScope.kriteriaEvaluasiSatuan[i].itemId+" : ");
                    //save data k.Administrasi
                    for(var j=0; j<$rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin.length; j++){
                        var dataKriteriaAdministrasi = {
                            id: $scope.kriteriaAminId,
                            nama: $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin[j].nama,
                            pengadaan: $scope.pengadaanId,
                            masterKriteriaAdministrasi: $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin[j].id,
                            itemPengadaan: $rootScope.kriteriaEvaluasiSatuan[i].itemId,
                            created: new Date()
                        };
                        console.log((j+1)+" : "+JSON.stringify($rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin[j])+" , ");
                        
                        if ($scope.kriteriaAminId > 0) {
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/update',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function(obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: dataKriteriaAdministrasi
                            }).success(function(data, status, headers, config) {
                                console.log("UPDATE KRITERIA ADMIN OK : " + JSON.stringify(data));
                            });
                        } else {
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/create',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function(obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: dataKriteriaAdministrasi
                            }).success(function(data, status, headers, config) {
                                if (typeof data !== 'undefined')
                                    $scope.kriteriaAminId = data.id;
                                console.log("CREATE KRITERIA ADMIN OK : " + JSON.stringify(data));
                            });
                        }
                    }
                    
                    //save data k.Teknis
                    for(var k=0; k<$rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis.length; k++){
                        var dataKriteriaTeknis = {
                            id: $scope.kriteriaTeknisId,
                            nama: $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis[k].nama,
                            pengadaan: $scope.pengadaanId,
                            masterKriteriaAdministrasi: $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis[k].id,
                            itemPengadaan: $rootScope.kriteriaEvaluasiSatuan[i].itemId,
                            created: new Date()
                        };
                        console.log((k+1)+" : "+JSON.stringify($rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis[k])+" , ");
                        
                        if ($scope.kriteriaTeknisId > 0) {
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/kriteriaTeknisServices/update',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function(obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: dataKriteriaTeknis
                            }).success(function(data, status, headers, config) {
                                console.log("UPDATE KRITERIA TEKNIS OK : " + JSON.stringify(data));
                            });
                        } else {
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/kriteriaTeknisServices/create',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function(obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: dataKriteriaTeknis
                            }).success(function(data, status, headers, config) {
                                if (typeof data !== 'undefined')
                                    $scope.kriteriaTeknisId = data.id;
                                console.log("CREATE KRITERIA TEKNIS OK : " + JSON.stringify(data));
                            });
                        }
                    }
                    
                    //save data k.AmbangBatas sistem gugur
                    var dataKriteriaAmbang = {
                        id: $scope.kriteriaAmbangId,
                        atas: $rootScope.kriteriaEvaluasiSatuan[i].batasAtas,
                        bawah: $rootScope.kriteriaEvaluasiSatuan[i].batasBawah,
                        pengadaan: $scope.pengadaanId,
                        itemPengadaan: $rootScope.kriteriaEvaluasiSatuan[i].itemId,
                        created: new Date()
                    };
                    
                    if ($scope.kriteriaAmbangId > 0) {
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/update',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function(obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: dataKriteriaAmbang
                        }).success(function(data, status, headers, config) {
                            console.log("UPDATE KRITERIA AMBANG OK : " + JSON.stringify(data));
                        });
                    } else {
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/kriteriaAmbangBatasServices/create',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function(obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: dataKriteriaAmbang
                        }).success(function(data, status, headers, config) {
                            if (typeof data !== 'undefined')
                                $scope.kriteriaTeknisId = data.id;
                            console.log("CREATE KRITERIA AMBANG OK : " + JSON.stringify(data));
                        });
                    }
                }*/
                                
                //change tahapan
                /*var noTahapanSelanjutnya = {
                    pengadaanId: $scope.pengadaanId,
                    tahapanPengadaanId: 8
                }
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/inisialisasi/updateTahapanPengadaan',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function(obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: noTahapanSelanjutnya
                }).success(function(data, status, headers, config) {
                    console.log("KE TAHAPAN SELANJUTNYA : " + JSON.stringify(data));
                });*/
                
                vm.back();
            }
        }
        
        //URL Link
		vm.go = function (path) {
			$location.path(path);
		};
    }

    KriteriaEvaluasiViewSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window'];

})();