/**
 * ========================================================= Module:
 * PelaksanaPengadaanTambahTimController.js
 * =========================================================
 */

(function() {
	'use strict';

	angular.module('naut').controller('PelaksanaPengadaanTambahTimController',
			PelaksanaPengadaanTambahTimController);

	function PelaksanaPengadaanTambahTimController( $rootScope, $scope,
			$resource, $filter, RequestService,
			$state, $stateParams) {
		var vm = this;
		var validate = false;
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.panitiaDTO={};
		if(vm.toDo=="edit"){
			vm.panitiaDTO.panitia=$stateParams.panitiaDetail.panitia;
			vm.panitiaDTO.timPanitia=$stateParams.panitiaDetail.timPanitia;	
		}else{
			vm.panitiaDTO.panitia={};
			vm.panitiaDTO.timPanitia={};
			vm.panitiaDTO.panitia.cabang={};
			vm.panitiaDTO.panitia.divisi={};
			}
		
		vm.panitiaDTO.anggotaPanitiaList = [];
		vm.anggota = {};
		vm.anggota.anggotaList = [];
		$scope.parentId;
		$scope.notParent = true;

		// divisi
		$scope.getOrganisasi = function(parentId, id) {
			RequestService.doGET(
					'/procurement/master/organisasi/get-all-by-id/'
							+ parentId + '/' + id).then(function success(data) {
				$scope.divisiList = data[0].children;
				vm.loading = false;
			}, function error(response) {
				RequestService.informError("Terjadi Kesalahan Pada System");
				vm.loading = false;
			});

		}

		$scope.getPembuatKebutusanList = function() {
			RequestService
					.doGET(
							'/procurement/master/pembuatKeputusanServices/getList')
					.then(
							function success(data) {
								$scope.pembuatKebutusanList = data;
								vm.loading = false;
							},
							function error(response) {
								RequestService
										.informError("Terjadi Kesalahan Pada System");
								vm.loading = false;
							});
		}
		$scope.getRoleList = function() {
			RequestService
					.doGET(
							'/procurement/master/panitiaServices/getListRoleUser')
					.then(
							function success(data) {
								$scope.userRoleList = data;
								vm.loading = false;
								if(vm.toDo=='edit'){
									angular.forEach($scope.userRoleList, function (roleValue, roleIndex) {
						                angular.forEach($stateParams.panitiaDetail.anggotaPanitiaList, function (value, indeks) {
						                    if (value.kdPosisi == 1) {
						                    	vm.anggota.ketua = value.pic;
						                    } else if (value.kdPosisi == 2) {
						                    	vm.anggota.sekretaris = value.pic;
						                    } else {
						                        
						                        if (roleValue.user.id == value.pic.user.id) {
						                        	vm.anggota.anggotaList.push($scope.userRoleList[roleIndex]);
						                        }
						                    }
						                });
						            })	
								}
							},
							function error(response) {
								RequestService
										.informError("Terjadi Kesalahan Pada System");
								vm.loading = false;
							});
		}
		$scope.getRoleList();
		$scope.getPembuatKebutusanList();

		// tree
		$scope.my_tree_handler = function(branch) {
			vm.output = branch.label;
			$scope.treeId = branch.id;
			vm.panitiaDTO.panitia.cabang.id = branch.id;

			if (typeof $scope.output !== 'undefined' || $scope.output != ''
					|| $scope.output != null) {
				$scope.hasParent = true;
				$scope.notParent = false;
			}
			vm.panitiaDTO.panitia.divisi = {};
			vm.divisi = null;
			$scope.getOrganisasi(branch.parentId, branch.id);
		};

		var tree;
		$scope.my_tree = tree = {};
		$scope.try_async_load = function(parentId, id) {
			$scope.my_data = [];
			$scope.doing_async = true;
			var remoteTree = $resource($rootScope.backendAddress
					+ '/procurement/master/organisasi/get-all');
			return remoteTree.get(function(res) {
				$scope.my_data = res.data;
				$scope.doing_async = false;
				return tree.expand_all();
			}).$promise;
		};
		$scope.try_async_load();

		// datepicker
		$scope.chngTglMulai = function() {
			vm.berlakuSelesai = null;
		}

		vm.disabled = function(date, mode) {
			return false;
			// return ( mode === 'day' && ( date.getDay() === 0 ||date.getDay()
			// === 6 ) );
		};
		vm.toggleMin = function() {
			vm.minDate = vm.minDate ? null : new Date();
		};
		vm.toggleMin();
		vm.dateOptions = {
			formatYear : 'yy',
			startingDay : 1
		};
		vm.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate',
				'dd-MM-yyyy' ];
		vm.format = vm.formats[4];

		// tanggal SK
		vm.tanggalOpenSk = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.tanggalOpenedSk = true;
		};

		// tanggal Awal
		vm.tanggalOpenAwal = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.tanggalOpenedAwal = true;
		};

		// tanggal Akhir
		vm.tanggalOpenAkhir = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.tanggalOpenedAkhir = true;
		};

		// validasi anggota

		$scope.changeKetua = function() {
			var valid = true;

			if (vm.anggota.ketua !== undefined
					&& vm.anggota.sekretaris !== undefined) {

				if (vm.anggota.sekretaris.user.id == vm.anggota.ketua.user.id) {
					vm.errorAnggotaKetua = 'promise.procurement.master.pelaksanapengadaan.error.ketua_sama';

					valid = false;
				}
				if (vm.anggota.anggotaList !== undefined) {
					for (var i = 0; i < vm.anggota.anggotaList.length; i++) {
						if (vm.anggota.ketua.user.id == vm.anggota.anggotaList[i].user.id) {
							valid = false;
							vm.errorAnggotaKetua = 'promise.procurement.master.pelaksanapengadaan.error.ketua_sama';

						}
					}
				}
			}
			if (valid) {

				vm.errorAnggotaKetua = '';
			}
			return valid;
		}

		$scope.changeSekretaris = function() {

			var valid = true;
			if (vm.anggota.ketua !== undefined
					&& vm.anggota.sekretaris !== undefined) {
				if (vm.anggota.sekretaris.user.id == vm.anggota.ketua.user.id) {
					vm.errorAnggotaSekretaris = 'promise.procurement.master.pelaksanapengadaan.error.sekertaris_sama';

					valid = false;
				}
				if (vm.anggota.anggotaList !== undefined) {
					for (var i = 0; i < vm.anggota.anggotaList.length; i++) {
						if (vm.anggota.sekretaris.user.id == vm.anggota.anggotaList[i].user.id) {
							vm.errorAnggotaSekretaris = 'promise.procurement.master.pelaksanapengadaan.error.sekertaris_sama';

							valid = false;
						}
					}
				}
			}
			if (valid) {

				vm.errorAnggotaSekretaris = ''
			}
			return valid;
		}

		$scope.changeAnggota = function() {

			var valid = false;
			if (vm.anggota.anggotaList !== undefined
					&& vm.anggota.anggotaList.length > 0) {
				if (vm.anggota.ketua === undefined
						|| vm.anggota.sekretaris === undefined) {
					vm.errorAnggotaList = 'promise.procurement.master.pelaksanapengadaan.error.ketua_sekertaris_kosong';
					vm.anggota.anggotaList = [];

				} else {

					valid = true;
					var selected = [];
					angular
							.forEach(
									vm.anggota.anggotaList,
									function(dataUser, index) {

										if (vm.anggota.ketua !== undefined
												&& vm.anggota.ketua.user.id === dataUser.user.id) {
											vm.errorAnggotaList = 'promise.procurement.master.pelaksanapengadaan.error.anggota_sama';

											valid = false;
										} else if (vm.anggota.sekretaris !== undefined
												&& vm.anggota.sekretaris.user.id === dataUser.user.id) {
											vm.errorAnggotaList = 'promise.procurement.master.pelaksanapengadaan.error.anggota_sama';

											valid = false;
										}

									});

					if (valid == true) {
						vm.errorAnggotaList = '';
					}

				}

			}
			;
			return valid;

		}

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
			if ($scope.validateForm()) {
				$scope.getData();
				$scope.saveData();

			}
			});
		}
		$scope.validateForm = function() {
			 
			vm.isValid = true;
			vm.tglValid=true;
			vm.errorCabang = "";
			vm.errorDivisi = "";
			vm.errorNamaPanitia = "";
			vm.errorPembuatKeputusan="";
			vm.errorNamaKeputusan = "";
			vm.errorPenanggungJawab = "";
			vm.errorSKDireksi = "";
			vm.errorTanggalSKDireksi="";
			vm.errorTanggalMulai="";
			vm.errorTanggalAkhir="";

			if (typeof vm.output === 'undefined' || vm.output == '') {
				vm.errorCabang = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.divisi === 'undefined' || vm.divisi == null || vm.divisi == '') {
				vm.errorDivisi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.nama === 'undefined' || vm.panitiaDTO.timPanitia.nama == '') {
				vm.errorNamaPanitia = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.pembuatKeputusan === 'undefined' || vm.panitiaDTO.timPanitia.pembuatKeputusan == '') {
				vm.errorPembuatKeputusan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.namaKeputusan === 'undefined' || vm.panitiaDTO.timPanitia.namaKeputusan == '') {
				vm.errorNamaKeputusan = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.penanggungJawab === 'undefined' || vm.panitiaDTO.timPanitia.penanggungJawab == '') {
				vm.errorPenanggungJawab = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.nomorSk === 'undefined' || vm.panitiaDTO.timPanitia.nomorSk == '') {
				vm.errorSKDireksi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.timPanitia.tanggalSk === 'undefined' || vm.panitiaDTO.timPanitia.tanggalSk == '') {
				vm.errorTanggalSKDireksi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.panitia.berlakuSelesai === 'undefined' || vm.panitiaDTO.panitia.berlakuSelesai == ''){
				vm.errorTanggalAkhir = 'template.error.field_kosong';
				vm.isValid = false;
				vm.tglValid=false;
			}			
			if(typeof vm.panitiaDTO.panitia.berlakuMulai === 'undefined' || vm.panitiaDTO.panitia.berlakuMulai == ''){
				vm.errorTanggalMulai = 'template.error.field_kosong';
				vm.isValid = false;
				vm.tglValid=false;
				
			}
			
			if(vm.tglValid==true){
				var tglMulai = new Date($filter('date')(vm.panitiaDTO.panitia.berlakuMulai, 'yyyy-MM-dd'));
		         var tglAkhir = new Date($filter('date')(vm.panitiaDTO.panitia.berlakuSelesai, 'yyyy-MM-dd'));
				if(tglAkhir<tglMulai){
					vm.isValid = false;
					vm.errorTanggalMulai='promise.procurement.master.pelaksanapengadaan.error.tgl_berlaku_lebih_kecil';
				}
			}
			
			if(typeof vm.anggota.anggotaList === 'undefined' || vm.anggota.anggotaList == ''){
				vm.errorAnggotaList='template.error.field_kosong';
				vm.isValid=false;
			}
			else if(!$scope.changeAnggota()){
				vm.isValid=false;		
			}
			
			if(typeof vm.anggota.sekretaris === 'undefined' || vm.anggota.sekretaris == ''){
				vm.errorAnggotaSekretaris='template.error.field_kosong';
				vm.isValid=false;
			} 
			else if(!$scope.changeSekretaris()){
				vm.isValid=false;		
			}
			if(typeof vm.anggota.ketua === 'undefined' || vm.anggota.ketua == ''){
				vm.errorAnggotaKetua='template.error.field_kosong';
				vm.isValid=false;
			} else if(!$scope.changeKetua()){
				vm.isValid=false;		
			}
			
			
			return vm.isValid;
		}

		$scope.getData = function() {

			vm.panitiaDTO.anggotaPanitiaList = [];
			vm.ketua = {
				kdPosisi : 1,
				pic : vm.anggota.ketua
			}
			vm.wakil = {
				kdPosisi : 2,
				pic : vm.anggota.sekretaris
			}
			angular.forEach(vm.anggota.anggotaList, function(dataUser, index) {
				var post = {
					kdPosisi : 3,
					pic : dataUser
				};
				vm.panitiaDTO.anggotaPanitiaList.push(post);

			});
			vm.panitiaDTO.anggotaPanitiaList.push(vm.ketua);
			vm.panitiaDTO.anggotaPanitiaList.push(vm.wakil);
			vm.panitiaDTO.panitia.divisi.id = vm.divisi.id;
		}

		$scope.saveData = function() {
			vm.url = '';
			if (vm.toDo == "add") {
				vm.url = '/procurement/master/panitiaServices/insert/';
			}

			if (vm.toDo == "edit") {
				vm.url = '/procurement/master/panitiaServices/update/';
			}

			RequestService
					.doPOSTJSON(vm.url, vm.panitiaDTO)
					.then(
							function success(data) {
								if (data != undefined) {
									if (data.isValid != undefined) {
										if (data.errorNama != undefined) {
											vm.errorNamaPanitia = 'promise.procurement.master.pelaksanapengadaan.error.nama_panitia_sama';
										}
									} else {
										RequestService.informSaveSuccess();
										$state.go('app.promise.procurement-master-pelaksanapengadaan');
									}

								}
							},
							function error(response) {
								RequestService
										.informError("Terjadi Kesalahan Pada System");
							});

		}

		$scope.back = function() {
			$state.go('app.promise.procurement-master-pelaksanapengadaan');
		}

		if (vm.toDo == "edit") {
			$scope.hasParent = true;
			$scope.notParent = false;
			vm.output = $stateParams.panitiaDetail.panitia.cabang.nama;
			$scope.getOrganisasi(
					$stateParams.panitiaDetail.panitia.cabang.parentId,
					$stateParams.panitiaDetail.panitia.cabang.id);
			vm.divisi = $stateParams.panitiaDetail.panitia.divisi;
			vm.divisi.label = vm.divisi.nama;
			vm.panitiaDTO.panitia.cabang= { id:$stateParams.panitiaDetail.panitia.cabang.id};
			vm.panitiaDTO.panitia.divisi= {id :$stateParams.panitiaDetail.panitia.divisi.id};

		}

	}

	PelaksanaPengadaanTambahTimController.$inject = ['$rootScope',
			'$scope', '$resource', '$filter',
			'RequestService', '$state', '$stateParams' ];

})();
