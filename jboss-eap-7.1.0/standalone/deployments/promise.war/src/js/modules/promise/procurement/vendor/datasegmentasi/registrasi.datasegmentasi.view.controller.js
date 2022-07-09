/**
 * ========================================================= Module:
 * RegistrasiDataSegmentasiViewController.js Author: F.H.K
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('naut').controller('RegistrasiDataSegmentasiViewController', RegistrasiDataSegmentasiViewController);

	function RegistrasiDataSegmentasiViewController($http, $scope, $rootScope, $filter, ngTableParams, $state, RequestService, $stateParams, $log) {

		var vm = this;
		var isChange = false;
	
		vm.dataSegmentasiList = [];
		vm.dataSegmentasi = {}; 

		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.status = ($stateParams.status != undefined) ? $stateParams.status : {};
		vm.dataSegmentasi = ($stateParams.dataSegmentasi != undefined) ? $stateParams.dataSegmentasi : {};
		
		$scope.bidangUsahaList = function() {
			RequestService.doGET('/procurement/master/BidangUsahaServices/getBidangUsahaList')
					.then(function(data, status, headers, config) { 
						vm.bidangUsahaList = data;
					});
		}
		$scope.bidangUsahaList();

		$scope.asosiasiList = function() {
			RequestService.doGET('/procurement/master/AsosiasiServices/getList')
					.then(function(data, status, headers, config) {
						vm.asosiasiList = data;
						angular.forEach(vm.asosiasiList,function(value,index){
							
	            			if(vm.asosiasiList[index].nama == vm.dataSegmentasi.asosiasi){
	            				vm.dataSegmentasi.asosiasi={};
	            				vm.dataSegmentasi.asosiasi = vm.asosiasiList[index];
	            			}
	            		})
					});
		}
		$scope.asosiasiList();

		vm.subBidangUsahaList = [];
		vm.tableSubBidangUsaha = new ngTableParams({
			page : 1,
			count : 5
		}, {
			total : vm.subBidangUsahaList.length,
			getData : function($defer, params) {
				$defer.resolve(vm.subBidangUsahaList.slice(
						(params.page() - 1) * params.count(), params.page()
								* params.count()));
			}
		});

	$scope.loadDataSubBidang = function(reload) {
			if (vm.dataSegmentasi !== undefined && vm.dataSegmentasi.bidangUsaha !== undefined) {
				$log.info("MASUK VALIDASI 1")
				RequestService.doGET('/procurement/master/sub-bidang-usaha/get-by-bidang-usaha-id/' + vm.dataSegmentasi.bidangUsaha.id)
						.then(
								function(data, status, headers, config) {
									vm.subBidangUsahaList = data;
									angular.forEach(vm.subBidangUsahaList, function(dataSubBidang, index) {
									$log.info(dataSubBidang);
									$log.info(vm.dataSegmentasi.subBidangUsaha);
										if (reload) {
											if (vm.dataSegmentasi !== undefined && vm.dataSegmentasi.subBidangUsaha !== undefined
													&& dataSubBidang.id === vm.dataSegmentasi.subBidangUsaha.id) {
												dataSubBidang.pilihSubBidang = dataSubBidang;
											} else {
												dataSubBidang.pilihSubBidang = null;
											}
										} else {
											if (dataSubBidang.pilihSubBidang != null) {
												dataSubBidang.pilihSubBidang = null;
											}
										}
									});
				vm.tableSubBidangUsaha.reload();
				});
			}
		}
		$scope.loadDataSubBidang(true);

		$scope.removelistSubbidang = function(data) {
			angular.forEach(vm.subBidangUsahaList, function(dataSubBidang,
					index) {
				if (dataSubBidang.pilihSubBidang == data) {

					dataSubBidang.pilihSubBidang = data;
				} else {
					dataSubBidang.pilihSubBidang = null;
				}
			});
		}

		$scope.changeBidangUsaha = function() {
			isChange = true;
			$scope.loadDataSubBidang(false);
		}

		vm.tanggalMulaiStatus = false;
		$scope.tanggalMulaiOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.tanggalMulaiStatus = true;
		};

		vm.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.tanggalBerakhirStatus = true;
		};

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		}

		$scope.saveData = function() {			
			
			var data = {
								
								status : vm.status
						}
			
    		vm.segmentasiVendorDraft={
					id:vm.dataSegmentasi.id,
					asosiasi : vm.dataSegmentasi.asosiasi.nama,
					nomor : vm.dataSegmentasi.nomor,
					tanggalBerakhir : vm.dataSegmentasi.tanggalBerakhir,
					tanggalMulai : vm.dataSegmentasi.tanggalMulai,
					created:vm.dataSegmentasi.created,
					subBidangUsaha:{
						id : vm.dataSegmentasi.subBidangUsaha.id
					}	
    				
			}
			if(vm.status== 0 && vm.toDo=="Edit"){
				data.segmentasiVendor = vm.segmentasiVendorDraft;
				
			}else
			{	
				vm.segmentasiVendorDraft.segmentasiVendor=vm.dataSegmentasi.segmentasiVendor;
				data.segmentasiVendorDraft = vm.segmentasiVendorDraft;
				
			}
			
			
			if (vm.toDo == "Add") {
				vm.url = "/procurement/vendor/SegmentasiVendorServices/insert"
			} else if (vm.toDo == "Edit") {
				vm.url = "/procurement/vendor/SegmentasiVendorServices/update"
			}
			RequestService
					.doPOSTJSON(vm.url, data)
					.then(function success(data) {
						
							RequestService.modalInformation("Data berhasil disimpan!",'success');
								$state.go('appvendor.promise.procurement-vendor-datasegmentasi');
							},
							function error(response) {
						          RequestService.modalInformation("Terjadi Kesalahan Pada System",'danger');
							});
		}
		
		
		
		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageBidangUsaha = "";
			vm.errorMessageSubBidangUsaha = "";
			vm.errorMessageAssosiasi = "";
			vm.errorMessageNomor = "";
			vm.errorMessageTanggalMulai = "";
			vm.errorMessageTanggalAkhir = "";

			if (vm.subBidangUsahaList !== undefined
					&& vm.subBidangUsahaList !== null
					&& vm.subBidangUsahaList.length > 0) {

				angular.forEach(vm.subBidangUsahaList, function(
						dataSubBidang, index) {
					if (dataSubBidang.pilihSubBidang != undefined) {
						vm.dataSegmentasi.subBidangUsaha = dataSubBidang;
						isChange = false
					}
				});
			}

			if (vm.dataSegmentasi.tanggalMulai !== undefined
					&& vm.dataSegmentasi.tanggalMulai !== null) {
				vm.dataSegmentasi.tanggalMulai = $filter('date')(
						vm.dataSegmentasi.tanggalMulai, 'yyyy-MM-dd');
			}
			if (vm.dataSegmentasi.tanggalBerakhir !== undefined
					&& vm.dataSegmentasi.tanggalBerakhir !== null) {
				vm.dataSegmentasi.tanggalBerakhir = $filter('date')(
						vm.dataSegmentasi.tanggalBerakhir, 'yyyy-MM-dd');
			}
			$log.info(isChange);
			$log.info(vm.dataSegmentasi);
			$log.info(vm.isValid);
		
			// Validasi Data Segmentasi
			if (vm.dataSegmentasi.bidangUsaha == undefined
					|| vm.dataSegmentasi.bidangUsaha == "") {
				vm.dataSegmentasi.bidangUsahaError = true;
				vm.isValid = false;
				vm.errorMessageBidangUsaha = "template.error.field_kosong";
			} else {
				if ((vm.dataSegmentasi.subBidangUsaha == null
						|| vm.dataSegmentasi.subBidangUsaha == undefined || vm.dataSegmentasi.subBidangUsaha == "")) {
					vm.isValid = false;
					vm.errorMessageSubBidangUsaha = "promise.procurement.RegistrasiVendor.DataSegmentasi.error.error_subbidang";
				}
			}
			if (vm.dataSegmentasi.asosiasi == undefined
					|| vm.dataSegmentasi.asosiasi == "") {
				vm.isValid = false;
				vm.errorMessageAssosiasi = "template.error.field_kosong";
			}

			if (vm.dataSegmentasi.nomor == undefined
					|| vm.dataSegmentasi.nomor == "") {
				vm.isValid = false;
				vm.errorMessageNomor = "template.error.field_kosong";
			}

			if (vm.dataSegmentasi.tanggalMulai == undefined
					|| vm.dataSegmentasi.tanggalMulai == "") {
				vm.isValid = false;
				vm.errorMessageTanggalMulai = "template.error.field_kosong";
			}

			if (vm.dataSegmentasi.tanggalBerakhir == undefined
					|| vm.dataSegmentasi.tanggalBerakhir == "") {
				vm.isValid = false;
				vm.errorMessageTanggalAkhir = "template.error.field_kosong";
			}
			return vm.isValid;
		}
		
		if(vm.toDo=="Edit"){
			RequestService.doGET('/procurement/master/BidangUsahaServices/getBidangUsahaSegmentasiById/'+vm.dataSegmentasi.subBidangUsaha.bidangUsaha.id)
			.then(function success(data) {
				vm.dataSegmentasi.bidangUsaha = data;
				$scope.loadDataSubBidang(true);
				$scope.removelistSubbidang();
          vm.loading = false;
        }, function error(response) {
          RequestService.modalInformation("Terjadi Kesalahan Pada System",'danger');
          vm.loading = false;
      });
		}
	$scope.back = function() {
			$state.go('appvendor.promise.procurement-vendor-datasegmentasi');
		}
	}
	RegistrasiDataSegmentasiViewController.$inject = [ '$http', '$scope','$rootScope', '$filter', 'ngTableParams', '$state', 'RequestService', '$stateParams', '$log'];

})();