(function () {
    'use strict';

    angular.module('naut').controller('ConditionalPrizeTambahController', ConditionalPrizeTambahController);

    function ConditionalPrizeTambahController($scope, $http, $rootScope, $resource, $location, toaster) {
        var form = this;
        form.tipe = {};
        form.fungsi = {};
        form.persentasi = {};

        $scope.tipeList = [
            {id: '1', nama: 'Header / Kena price berdasarkan Total'},
            {id: '2', nama: 'Item / tiap satuan kena price'}
        ];
        
        $scope.fungsiList = [
            {id: '-1', nama: 'Mengurangi Harga'},
            {id: '1', nama: 'Menambah Harga'}
        ];
        
        $scope.persentasiList = [
            {id: '0', nama: 'Nilai'},
            {id: '1', nama: 'Nilai Persentase'}
        ];

        form.kembali = function () {
            $location.path('/app/promise/procurement/master/conditionalprice');
        }

        form.simpan = function () {
            if (typeof form.kode === 'undefined' || form.kode == '') {
                alert("Kode Conditional Price belum disi.");
            } else if (typeof form.nama === 'undefined' || form.nama == '') {
                alert("Nama Conditional Price belum disi.");
            } else if (typeof form.tipe.selected === 'undefined') {
                alert("Pilih Tipe Conditional Price.");
            } else if (typeof form.fungsi.selected === 'undefined') {
                alert("Pilih Fungsi Conditional Price.");
            } else if (typeof form.persentasi.selected === 'undefined') {
                alert("Pilih Persentasi Conditional Price.");
            } else {
                //console.log(JSON.stringify(form));
                var postForm = {
                    kode: form.kode,
                    nama: form.nama,
                    tipe: form.tipe.selected.id,
                    fungsi: form.fungsi.selected.id,
                    isPersentage: form.persentasi.selected.id
                }
                //console.log(JSON.stringify(postForm));
                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/master/conditionalPriceServices/insert',
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
                        toaster.pop('success', 'Conditional Price', 'Simpan ' + data.nama + ', berhasil.');
                    }
                    $scope.loading = false;
                    form.kode = '';
                    form.nama = '';
                    form.tipe.selected = undefined;
                    form.fungsi.selected = undefined;
                    form.persentasi.selected = undefined;
                });
            };
        };
    }
    ConditionalPrizeTambahController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
