/**=========================================================
 * Module: HistoryAuctionSatuanViewController.js
 * Author: F.H.K
 =========================================================*/

(function() {
    'use strict';

    angular.module('naut').controller('HistoryAuctionSatuanViewController', HistoryAuctionSatuanViewController);

    function HistoryAuctionSatuanViewController($scope, $http, $rootScope, $location, toaster, $resource, $filter, $modal, ngTableParams) {
        
        /* ======================= Pendeklarasian Variable Lokal/Interlokal =============================== */
        var statusAuction = '';
		var detailSesiAuction = $rootScope.sesiAuctionDetail;
		
		$rootScope.idAuction = 0;
		$scope.listHarga = [];
        $scope.penawaranAuctionTerbaikList = [];
		/* ------------------------------------------------------------------------------------------------ */
		
		
		/* =============================== Mapping Penawaran Auction from table =========================== */
		for(var i=0; i<detailSesiAuction.length; i++) {
            var noIdSesi = detailSesiAuction[i].id;
			$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByAuction/' + noIdSesi, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
				var hargaTerbaikSebelumnya = 0;
				angular.forEach(data, function(value, index){
                    $scope.listHarga.push(data[index].nilaiPenawaran);
                });
                $scope.hargaTerbaik = sortHarga($scope.listHarga);
                //console.log("Harga Terbaik No Sesi:" + noIdSesi + " = " + $scope.hargaTerbaik);
                angular.forEach(data, function(value, index){
                    if(data[index].nilaiPenawaran == $scope.hargaTerbaik) {  
						if(hargaTerbaikSebelumnya != $scope.hargaTerbaik){
							if(data[index].pesertaAuction.auction.status == 0){
								statusAuction = 'Berlangsung';
							} else {
								statusAuction = 'Sesi Ditutup';
							}
							var isiList = {
								idSesi: data[index].pesertaAuction.auction.id,
								noSesi: data[index].pesertaAuction.auction.noSesi,
								tglSesi: $filter('date')(data[index].pesertaAuction.auction.waktuAwal,'dd-MMM-yyyy'),
								idVendor: data[index].pesertaAuction.vendor.id,
								namaVendor: data[index].pesertaAuction.vendor.nama,
								hargaTerbaik: data[index].nilaiPenawaran,
								mataUang: data[index].suratPenawaran.mataUang.kode,
								status: statusAuction
							}
							hargaTerbaikSebelumnya = data[index].nilaiPenawaran;
							$scope.penawaranAuctionTerbaikList.push(isiList);
						}
                    }
                });
                $scope.loading = false;
			}).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
		}
		/* ------------------------------- END Mapping Penawaran Auction ---------------------------------- */
		
		
		/* =============================== START DETAIL AUCTION MODAL ===================================== */
		$scope.clickDetailAuction = function(idAuction) {
            $rootScope.idAuction = idAuction;
            $scope.viewDetailAuction();
        }
        
        $scope.viewDetailAuction = function (size) {
            var modalInstance = $modal.open({
                templateUrl: '/view_detail_auction.html',
                controller: ModalInstanceCtrl,
                size: size,
            });
        };
        
        var ModalInstanceCtrl = function ($http, $scope, $rootScope, $modalInstance, ngTableParams) {
            var penawaranAuctionList = [];
            
            $http.get($rootScope.backendAddress + '/procurement/evaluasiharga/PenawaranAuctionServices/getByAuction/' + $rootScope.idAuction, {
                ignoreLoadingBar: true
            }).success(function(data, status, headers, config) {
                
                angular.forEach(data,function(value,index) {
                    penawaranAuctionList.push(value);
                });
				
                //console.log("ISI penawaranAuctionList = " + JSON.stringify($scope.penawaranAuctionList));
                $scope.noSesi = penawaranAuctionList[0].pesertaAuction.auction.noSesi;
                $scope.tglAwalSesi = penawaranAuctionList[0].pesertaAuction.auction.waktuAwal;
                $scope.tglAkhirSesi = penawaranAuctionList[0].pesertaAuction.auction.waktuAkhir;
                $scope.loading = false;
				
				// paging untuk Penawaran List
				$scope.penawaranAuctionList = new ngTableParams({
					page: 1,
					count: 4
				}, {
					total: data.length,
					getData: function($defer, params) {
						$defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()))
					}
				});
			}).error(function(data, status, headers, config) {
                $scope.loading = false;
            });
            
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
        
        ModalInstanceCtrl.$inject = ['$http', '$scope', '$rootScope', '$modalInstance', 'ngTableParams'];
		/* ------------------------------- END DETAIL AUCTION MODAL --------------------------------------- */
		
		
		/* =============================== START CONTROLLER DETAIL ======================================== */
		// Buat Sorting
		var sortHarga = function(listHarga) {
            var angka = listHarga;
            for (var x = 0; x <= angka.length; x++) {
                for (var y = x+1; y<= angka.length+1; y++) {
                    if(angka[x] > angka[y]){
                        var temp = angka[x];
                        angka[x] = angka[y];
                        angka[y] = temp;
                    }
                }
            }
            return angka[0];
        }
		
        $scope.cetak = function() {
            toaster.pop('error', 'Cetak Auction', 'Maaf Belum bisa untuk mencetak Riwayat Auction');
        }
        
        $scope.back = function() {
            $scope.go('/app/promise/procurement/evaluasiharga/satuan/auction/addview');
        }
    
        $scope.go = function(path) {
            $location.path(path);
        }
        /* ------------------------------- END Rincian Controller ----------------------------------------- */
    }
    
    HistoryAuctionSatuanViewController.$inject = ['$scope', '$http', '$rootScope', '$location', 'toaster', '$resource', '$filter', '$modal', 'ngTableParams'];
    
})();