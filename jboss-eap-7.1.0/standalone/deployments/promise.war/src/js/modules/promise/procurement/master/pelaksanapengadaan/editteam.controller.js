/**=========================================================
 * Module: PelaksanaPengadaanUbahTimController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelaksanaPengadaanUbahTimController', PelaksanaPengadaanUbahTimController);

    function PelaksanaPengadaanUbahTimController($http, $rootScope, $scope, $resource, $location, $modal, $filter, toaster, $timeout, RequestService, $state) {
        var form = this;
        var validate = false;
        var dataPanitiaEdit;
        form.divisi = {};
        form.jabatan = {};
        form.organisasi = {};
        form.ketua = {};
        form.sekretaris = {};
        form.anggota = [];
        $scope.parentId;
        $scope.notParent = true;

        $scope.testBtn = function () {
            form.multipleDemo.colors.push(form.wedusList[0]);
        }

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

        //tim panitia
        $http.get($rootScope.backendAddress + '/procurement/master/timPanitiaServices/getId/' + dataPanitiaEdit.teamId).success(function (data, status, headers, config) {
            form.nama = data.nama;
            form.namaKeputusan = data.namaKeputusan;
            form.penanggungJawab = data.penanggungJawab;
            form.nomorSk = data.nomorSk;
            form.tanggalSk = data.tanggalSk;
            form.jabatan.selected = data.pembuatKeputusan;
        }).error(function (data, status, headers, config) {})


        //role user
        $http.get($rootScope.backendAddress + '/procurement/master/panitiaServices/getListRoleUser').success(function (data, status, headers, config) {
            $scope.userRoleList = data;
            //console.log(JSON.stringify($scope.userRoleList));
            //data anggota
            $http.get($rootScope.backendAddress + '/procurement/master/anggotaPanitiaServices/getByTimPanitiaList/' + dataPanitiaEdit.teamId).success(function (data, status, headers, config) {
                    var anggotaId = [];

                    angular.forEach($scope.userRoleList, function (roleValue, roleIndex) {
                        angular.forEach(data, function (value, indeks) {
                            if (value.kdPosisi == 1) {
                                $rootScope.ketuaPelaksanaPengadaan = value.pic;
                                $rootScope.ketuaPelaksanaPengadaanId = value.id;
                            } else if (value.kdPosisi == 2) {
                                $rootScope.sekretarisPelaksanaPengadaan = value.pic;
                                $rootScope.sekretarisPelaksanaPengadaanId = value.id;
                            } else {
                                anggotaId.push(value.id);

                                if (roleValue.user.id == value.pic.user.id) {
                                    form.anggota.push($scope.userRoleList[roleIndex]);
                                }
                            }
                        });
                    })
                    form.ketua.selected = $rootScope.ketuaPelaksanaPengadaan;
                    form.sekretaris.selected = $rootScope.sekretarisPelaksanaPengadaan;
                    $rootScope.anggotaPelaksanaPengadaanId = anggotaId;

                }).error(function (data, status, headers, config) {})
                /*---------------------------*/


        }).error(function (data, status, headers, config) {})

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

        //pembuat keputusan
        $http.get($rootScope.backendAddress + '/procurement/master/pembuatKeputusanServices/getList').success(function (data, status, headers, config) {
            $scope.jabatanList = data;
        }).error(function (data, status, headers, config) {})

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

        //tanggal SK
        form.tanggalOpenSk = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.tanggalOpenedSk = true;
        };

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

        //validasi anggota
        $scope.changeAnggota = function () {
            if (form.ketua.selected !== undefined && form.sekretaris.selected !== undefined) {
                if (form.sekretaris.selected.user.id == form.ketua.selected.user.id) {
                    alert('Pilih Nama Anggota tidak boleh sama dengan sekretaris');
                    form.ketua.selected = $rootScope.ketuaPelaksanaPengadaan;
                    validate = false;
                }
                for (var i = 0; i < form.anggota.length; i++) {
                    if (form.ketua.selected.user.id == form.anggota[i].user.id && form.ketua.selected !== undefined) {
                        alert('Nama ini sudah dipilih menjadi Anggota Panitia');
                        form.ketua.selected = $rootScope.ketuaPelaksanaPengadaan;
                        validate = false;
                    }
                }
            }
        }

        $scope.changeAnggota1 = function () {
            if (form.ketua.selected !== undefined && form.sekretaris.selected !== undefined) {
                if (form.sekretaris.selected.user.id == form.ketua.selected.user.id) {
                    alert('Pilih Nama Anggota tidak boleh sama dengan ketua');
                    form.sekretaris.selected = $rootScope.sekretarisPelaksanaPengadaan;
                    validate = false;
                }
                for (var i = 0; i < form.anggota.length; i++) {
                    if (form.sekretaris.selected.user.id == form.anggota[i].user.id) {
                        alert('Nama ini sudah dipilih menjadi Anggota Panitia');
                        form.sekretaris.selected = $rootScope.sekretarisPelaksanaPengadaan;
                        validate = false;
                    }
                }
            }
        }

        $scope.changeAnggota2 = function () {
            if (form.anggota !== undefined && form.anggota.length > 0) {
                if (form.ketua.selected === undefined || form.sekretaris.selected === undefined) {
                    alert('Pilih Ketua dan Sekretaris terlebih dahulu');
                    //form.anggota = [];
                };
                var selected = [];
                angular.forEach(form.anggota, function (dataUser, index) {
                    var check = false;
                    if (form.ketua.selected !== undefined && dataUser.user !== undefined && form.ketua.selected.user.id === dataUser.user.id) {
                        alert('Pilih Nama Anggota tidak boleh sama dengan ketua');
                        form.anggota.splice(index, 1);
                        check = true;
                    }
                    if (form.sekretaris.selected !== undefined && dataUser.user !== undefined && form.sekretaris.selected.user.id === dataUser.user.id) {
                        alert('Pilih Nama Anggota tidak boleh sama dengan sekretaris');
                        form.anggota.splice(index, 1);
                        check = true;
                    }
                    if (check === false) {
                        selected.push(dataUser);
                    }
                });
                form.anggota = selected;
            };
        }

        var simpanAnggotaPanitia = function (id) {
            //ketua
            //console.log('ketua ' + form.ketua.selected.user.id);
            var postKetuaForm = {
                id: $rootScope.ketuaPelaksanaPengadaanId,
                kdPosisi: '1',
                pic: form.ketua.selected.user.id,
                timPanitia: id
            }
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/anggotaPanitiaServices/update',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postKetuaForm
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    console.log('ketua Panitia sukses');
                }
                $scope.loading = false;
            });
            //sekretaris
            //console.log('sekretaris ' + form.sekretaris.selected.user.id);
            var postSekretarisForm = {
                id: $rootScope.sekretarisPelaksanaPengadaanId,
                kdPosisi: '2',
                pic: form.sekretaris.selected.user.id,
                timPanitia: id
            }
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/anggotaPanitiaServices/update',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: postSekretarisForm
            }).success(function (data, status, headers, config) {
                if (typeof data !== 'undefined') {
                    console.log('sekretaris Panitia sukses');
                }
                $scope.loading = false;
            });

            //anggota
            //clear data anggota Panitia
            $http.get($rootScope.backendAddress + '/procurement/master/anggotaPanitiaServices/deleteByTeamId/' + id)
                .success(function (responseData) {
                    $timeout(function () {
                        //insert anggota
                        angular.forEach(form.anggota, function (value, indeks) {
                            var postAnggotaForm = {
                                kdPosisi: '3',
                                pic: value.user.id,
                                timPanitia: id
                            }
                            $scope.loading = true;
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/master/anggotaPanitiaServices/create',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: postAnggotaForm
                            }).success(function (data, status, headers, config) {
                            	RequestService.informSaveSuccess();
                        		$state.go('app.promise.procurement-master-pelaksanapengadaan');
                            });
                        })
                    }, 2000);
                })
                .error(function (err) {
                    console.error(err);
                });
        }

        var simpanTimPanitia = function (id) {
            var postForm = {
                    id: dataPanitiaEdit.teamId,
                    nama: form.nama,
                    nomorSk: form.nomorSk,
                    namaKeputusan: form.namaKeputusan,
                    penanggungJawab: form.penanggungJawab,
                    tanggalSk: $filter('date')(form.tanggalSk, 'yyyy-MM-dd'),
                    panitia: id,
                    pembuatKeputusan: form.jabatan.selected.id
                }
                //console.log(JSON.stringify(postForm));
            $scope.loading = true;
            $http({
                method: 'POST',
                url: $rootScope.backendAddress + '/procurement/master/timPanitiaServices/update',
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
                    console.log('timPanitia ' + JSON.stringify(data.id));
                    simpanAnggotaPanitia(data.id);
                }
                $scope.loading = false;
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
                    simpanTimPanitia(data.id);
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
                alert('Nama Kepanitiaan masih kosong');
                validate = false;
            } else if (form.jabatan.selected == undefined) {
                alert('Pembuat Keputusan masih kosong');
                validate = false;
            } else if (typeof form.namaKeputusan === 'undefined' || form.namaKeputusan == null || form.namaKeputusan == '') {
                alert('Nama Keputusan masih kosong');
                validate = false;
            } else if (typeof form.penanggungJawab === 'undefined' || form.penanggungJawab == null || form.penanggungJawab == '') {
                alert('Penanggung Jawab masih kosong');
                validate = false;
            } else if (typeof form.nomorSk === 'undefined' || form.nomorSk == null || form.nomorSk == '') {
                alert('Nomor SK masih kosong');
                validate = false;
            } else if (typeof form.tanggalSk === 'undefined' || form.tanggalSk == null) {
                alert('Tanggal SK Direksi masih kosong');
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
            } else if (form.ketua.selected == undefined) {
                alert('Ketua Panitia masih kosong');
                validate = false;
            } else if (form.sekretaris.selected == undefined) {
                alert('Sekretaris Panitia masih kosong');
                validate = false;
            } else if (form.anggota.length > 0) {
                validate = true;
            } else {
                alert('Anggota Panitia masih kosong');
                validate = false;
            }
        }

        $scope.simpan = function () {
            //console.log(JSON.stringify(form));
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

    PelaksanaPengadaanUbahTimController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$modal', '$filter', 'toaster', '$timeout', 'RequestService', '$state'];

})();