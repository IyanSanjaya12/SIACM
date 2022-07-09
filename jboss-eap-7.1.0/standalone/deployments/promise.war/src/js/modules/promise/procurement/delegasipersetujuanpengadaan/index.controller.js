(function () {
    'use strict';

    angular.module('naut').controller('DelegasiPersetujuanPengadaanDetailController',
        DelegasiPersetujuanPengadaanDetailController);

    function DelegasiPersetujuanPengadaanDetailController($scope, $http, $rootScope, $resource, $location, toaster, $filter, ngTableParams, $modal, $q, $timeout) {

        var delegasiPersetujuan = this;
        delegasiPersetujuan.post = {};
        $rootScope.roleUser = {};
        $scope.sekarang = new Date();

        // Datepicker
        delegasiPersetujuan.toggleMin = function () {
            delegasiPersetujuan.minDate = delegasiPersetujuan.minDate ? null : new Date();
        };
        delegasiPersetujuan.toggleMin();
        delegasiPersetujuan.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        delegasiPersetujuan.format = delegasiPersetujuan.formats[4];

        delegasiPersetujuan.tglAwalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            delegasiPersetujuan.tglAwalOpened = true;
        };
        delegasiPersetujuan.tglBerakhirOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            delegasiPersetujuan.tglBerakhirOpened = true;
        };


        $scope.jenisPersetujuanPengadaan = {};
        $http.get($rootScope.backendAddress + '/procurement/master/JenisPersetujuanPengadaanServices/getAll')
            .success(function (data, status, headers, config) {
                $scope.jenisPersetujuanPengadaanList = data;
            })
            .error(function (data, status, headers, config) {});

        $scope.saveDelegasiPersetujuan = function () {
            delegasiPersetujuan.loading = true;
            delegasiPersetujuan.post.jenisPersetujuanId = $scope.jenisPersetujuanPengadaan.selected.id;
            delegasiPersetujuan.post.tanggalAwal = $filter('date')(delegasiPersetujuan.post.tanggalAwal, 'dd-MM-yyyy');
            delegasiPersetujuan.post.tanggalAkhir = $filter('date')(delegasiPersetujuan.post.tanggalAkhir, 'dd-MM-yyyy');

            console.log("POST : " + JSON.stringify(delegasiPersetujuan.post));
            var url = $rootScope.backendAddress + "/procurement/delegasipersetujuan/DelegasiPersetujuanServices/createDelegasiPersetujuan";
            if (typeof delegasiPersetujuan.post.delegasiPersetujuanId !== 'undefined') {
                url = $rootScope.backendAddress + "/procurement/delegasipersetujuan/DelegasiPersetujuanServices/editDelegasiPersetujuan";
            }
            $http({
                    method: 'POST',
                    url: url,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: delegasiPersetujuan.post
                })
                .success(function (data, status, headers, config) {
                    delegasiPersetujuan.loading = false;
                    getDelegasiPersetujuanList();
                })
                .error(function (data, status, headers, config) {
                    delegasiPersetujuan.loading = false;
                });
            return false;
        };

        function getDelegasiPersetujuanList() {
            delegasiPersetujuan.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/delegasipersetujuan/DelegasiPersetujuanServices/getListDelegasiPersetujuan')
                .success(function (data, status, headers, config) {
                    delegasiPersetujuan.jenisPersetujuanPengadaanList = data;
                    for (var i = 0; i < data.length; i++) {
                        //console.log("User list : " + delegasiPersetujuan.jenisPersetujuanPengadaanList[i].user);
                        $scope.callDeferGetUser(i, delegasiPersetujuan.jenisPersetujuanPengadaanList[i].user);
                    }
                    delegasiPersetujuan.loading = false;
                })
                .error(function (data, status, headers, config) {
                    delegasiPersetujuan.loading = false;
                });
        }
        getDelegasiPersetujuanList();

        $scope.getUser = function (userid) {
            var deferred = $q.defer();

            $timeout(function () {
                $http.get($rootScope.backendAddress + '/procurement/user/get-user/' + userid)
                    .success(function (data, status, headers, config) {
                        deferred.resolve(data);

                    });
            }, 1000);
            return deferred.promise;
        }

        $scope.callDeferGetUser = function (i, userid) {
            var myPromise = $scope.getUser(userid);
            myPromise.then(function (resolve) {
                //console.log('Resolve : ' + JSON.stringify(resolve));
                if (typeof delegasiPersetujuan.jenisPersetujuanPengadaanList[i].userObj !== 'undefined') {
                    delegasiPersetujuan.jenisPersetujuanPengadaanList[i]['userObj'] = resolve;
                } else {
                    if (resolve == null) {
                        delegasiPersetujuan.jenisPersetujuanPengadaanList[i].userObj = {};
                    } else {
                        delegasiPersetujuan.jenisPersetujuanPengadaanList[i].userObj = resolve;
                    }
                }
            }, function (reject) {});

        }

        $scope.editDelegasiPersetujuan = function (dppId) {
            delegasiPersetujuan.loading = true;
            $http.get($rootScope.backendAddress + '/procurement/delegasipersetujuan/DelegasiPersetujuanServices/getDelegasiPersetujuan/' + dppId)
                .success(function (data, status, headers, config) {
                    delegasiPersetujuan.loading = false;
                    delegasiPersetujuan.post.delegasiPersetujuanId = data.id;
                    delegasiPersetujuan.post.tanggalAwal = data.tanggalAwal;
                    delegasiPersetujuan.post.tanggalAkhir = data.tanggalAkhir;
                    delegasiPersetujuan.post.user = data.user;
                    delegasiPersetujuan.post.jenisPersetujuanId = data.jenisPersetujuanPengadaan.id;
                    $scope.jenisPersetujuanPengadaan.selected = {
                        id: data.jenisPersetujuanPengadaan.id,
                        nama: data.jenisPersetujuanPengadaan.nama,
                        status: data.jenisPersetujuanPengadaan.status
                    };
                    var myPromise = $scope.getUser(data.user);
                    myPromise.then(function (resolve) {
                        delegasiPersetujuan.post.userName = resolve.namaPengguna;
                    });

                })
                .error(function (data, status, headers, config) {
                    delegasiPersetujuan.loading = false;
                });
        }

        $scope.removeDelegasiPersetujuan = function (dppId) {
            var konfirmasi = confirm("Anda yakin ingin menghapus Delegasi Persetujuan Pengadaan dengan ID : " + dppId);
            if (konfirmasi) {
                delegasiPersetujuan.loading = true;
                $http.get($rootScope.backendAddress + '/procurement/delegasipersetujuan/DelegasiPersetujuanServices/delete/' + dppId)
                    .success(function (data, status, headers, config) {
                        delegasiPersetujuan.loading = false;
                        getDelegasiPersetujuanList();
                    })
                    .error(function (data, status, headers, config) {
                        delegasiPersetujuan.loading = false;
                    });
            }
        }
        
        $scope.newDelegasiPersetujuan = function(){
            delegasiPersetujuan.post = {};
            $scope.jenisPersetujuanPengadaan.selected = {};
        }

        $scope.searchUserForm = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/userdelegasipersetujuan01.html',
                controller: UserSearchModalInstanceCtrl,
                size: 'lg'
            });
            var message = "INFO :<br/>";
            modalInstance.result.then(function () {
                //console.log('OOK input berhasil : '+$rootScope.roleUser.id);
                if (typeof $rootScope.roleUser.id !== 'undefined') {
                    delegasiPersetujuan.post.userName = $rootScope.roleUser.nama;
                    delegasiPersetujuan.post.user = $rootScope.roleUser.id;
                }
            }, function () {
                //console.log('INFO Cancel klik');
            });
        };
        var UserSearchModalInstanceCtrl = function ($scope, $rootScope, $http, $modalInstance, ngTableParams) {
            var vm = this;
            $scope.roleUser = {};
            $http.get($rootScope.backendAddress + '/procurement/user/get-role')
                .success(function (data, status, headers, config) {
                    $scope.roleUserList = data;
                })
                .error(function (data, status, headers, config) {});

            $scope.searchUserForm = function () {
                vm.loading = true;
                if (typeof $scope.roleUser.selected !== 'undefined' && $scope.userNameSearch != '') {
                    var url = $rootScope.backendAddress + "/procurement/user/get-role-user-by-role-id-and-name/" + $scope.roleUser.selected.id + "/" + $scope.userNameSearch;
                    $http.get(url)
                        .success(function (data, status, headers, config) {
                            $scope.roleUserSearchList = data;
                            vm.loading = false;
                        })
                        .error(function (data, status, headers, config) {
                            vm.loading = false;
                        });
                } else {
                    alert("Role User belum dipilih!");
                }
            }

            $scope.pilihUser = function (id, name) {
                //console.log('data : '+id+", nama : "+name);
                $rootScope.roleUser.id = id;
                $rootScope.roleUser.nama = name;
            }

            $scope.ok = function () {
                $modalInstance.close('closed');
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }
    }

    DelegasiPersetujuanPengadaanDetailController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', 'ngTableParams', '$modal', '$q', '$timeout'];

})();