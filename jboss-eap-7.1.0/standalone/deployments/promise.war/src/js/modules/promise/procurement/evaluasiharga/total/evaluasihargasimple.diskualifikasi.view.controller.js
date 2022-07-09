(function() {
    'use strict';

    angular.module('naut').controller('SimpleDiskualifikasiViewController', SimpleDiskualifikasiViewController);

    function SimpleDiskualifikasiViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal ============================== */
        $rootScope.dataPenawaranAuction = [];
        var nilaiCheck = [];
        /* ----------------------------------------------------------------------------------------------- */
        
        
        /* =============================== Mapping Sesi Auction from table =============================== */
        var detailSesiAuction = $rootScope.sesiAuctionDetail;
        
        // untuk isi dari Header-nya
        $scope.auctionList = detailSesiAuction;
        
        // fungsi untuk mengambil data dari dropdown ketika berubah
        $scope.pilih = function() {
            mappingPenawaranAuction($scope.noSesi);
            angular.forEach(detailSesiAuction, function(value,index){
                if($scope.noSesi == detailSesiAuction[index].id)
                    $scope.statusAuction = detailSesiAuction[index].status;
            });
        }
        /* ------------------------------- END Mapping Sesi Auction -------------------------------------- */
        
        
        /* =============================== Mapping Penawaran Auction from table ========================== */
        var mappingPenawaranAuction = function(noSesi) {
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getWithKursByAuction/' + noSesi, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
				
                $rootScope.dataPenawaranAuction = data;
				var nilaiList = [];
				
                angular.forEach(data,function(value,index){
                    nilaiList.push(data[index].totalPenawaranKonfersi);
                });
				
                $scope.nilaiMax = sortNilai(nilaiList, 'dsc');
                $scope.nilaiMin = sortNilai(nilaiList, 'asc');
                $scope.mataUang = 'IDR';
                $scope.loading = false;
                
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
        }
        /* ------------------------------- END Mapping Penawaran Auction --------------------------------- */
        
        
        /* =============================== START CONTROLLER DETAIL ======================================= */
        // untuk peserta Auction dari vendor yang akan ikut
        $scope.disHarga = function(idPenawaran, harga, dis) {
            var isiDataDis = {
                idPenawaran: idPenawaran,
                diskualifikasiHarga: dis,
                nilaiPenawaran: harga
            }
            if(dis) {
                nilaiCheck.push(isiDataDis);
            } else {
                nilaiCheck.splice(nilaiCheck.indexOf(isiDataDis), 1);
            }
            //console.log("vendorCheck = " + JSON.stringify(nilaiCheck));
        }
        
        // fungsi untuk pengurutan nilai
        var sortNilai = function(nilai, type) {
            var angka = nilai;
            for (var x = 0; x <= angka.length; x++) {
                for (var y = x+1; y<= angka.length+1; y++) {
                    if(type == 'dsc'){
                        if(angka[x] < angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }
                    } else {
                        if(angka[x] > angka[y]){
                            var temp = angka[x];
                            angka[x] = angka[y];
                            angka[y] = temp;
                        }    
                    }
                }
            }
            return angka[0];
        }
        
        // update ke tabel Penawaran Auction
        $scope.simpan = function() { // NOTE: apakah nilai yang terdiskualifikasi hilang di semua sesi auction ato hanya di Menu DIS
            if(nilaiCheck.length > 0){
                for(var i=0; i<nilaiCheck.length; i++) {
                    angular.forEach($rootScope.dataPenawaranAuction,function(value,index) {
                        if(nilaiCheck[i].idPenawaran == $rootScope.dataPenawaranAuction[index].id) {
                            var dataUpdatePenawaranAuction = {
                                id: nilaiCheck[i].idPenawaran,
                                diskualifikasiHarga: 1,
                                nilaiPenawaran: $rootScope.dataPenawaranAuction[index].nilaiPenawaran,
                                nilaiPenawaranOri: $rootScope.dataPenawaranAuction[index].nilaiPenawaranOri,
                                pesertaAuction: $rootScope.dataPenawaranAuction[index].pesertaAuction.id,
                                suratPenawaran: $rootScope.dataPenawaranAuction[index].suratPenawaran.id,
                                mataUang:$rootScope.dataPenawaranAuction[index].mataUang.id
                            }
                            
                            $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/update',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                transformRequest: function(obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: dataUpdatePenawaranAuction
                            }).success(function(data, status, headers, config) {
                                if (typeof data !== 'undefined'){
                                    var dataUpdatePenawaranAuction = data.id;
                                }
                                //console.log("CREATE PENAWARAN AUCTION OK : " + JSON.stringify(data));
                            });
                        }
                    });
                }
                $scope.back();
            } else {
                toaster.pop('warning', 'Check-Box Kosong', 'Tidak ada Nilai yang didiskualifikasi');
            }
        }
        
        $scope.back = function() {
            $scope.go('/app/promise/procurement/evaluasihargasimple/total/auction/view');
        }
    
        $scope.go = function(path) {
            $location.path(path);
        }
        /* ------------------------------- END Rincian Controller ---------------------------------------- */
    }
    
    SimpleDiskualifikasiViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();