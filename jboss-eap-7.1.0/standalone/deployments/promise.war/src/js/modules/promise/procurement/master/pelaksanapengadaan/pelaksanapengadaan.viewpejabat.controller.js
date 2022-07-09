/**=========================================================
 * Module: PelaksanaPengadaanTambahPejabatController.js
 =========================================================*/

(function () {
    'use strict';

    angular
        .module('naut')
        .controller('PelaksanaPengadaanTambahPejabatController', PelaksanaPengadaanTambahPejabatController);

    function PelaksanaPengadaanTambahPejabatController($rootScope,$scope, $resource, $filter, RequestService, $state,$stateParams) {
        var vm = this;
        vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
        vm.panitiaDTO={};
		if(vm.toDo=="edit"){
			vm.panitiaDTO.panitia=$stateParams.panitiaDetail.panitia;
			vm.panitiaDTO.pejabatPelaksanaPengadaan=$stateParams.panitiaDetail.pejabatPelaksanaPengadaan;	
		}else{
			vm.panitiaDTO.panitia={};
			vm.panitiaDTO.pejabatPelaksanaPengadaan={};
			vm.panitiaDTO.panitia.cabang={};
			vm.panitiaDTO.panitia.divisi={};
			}
        $scope.parentId;
        $scope.notParent = true;

        //divisi
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
        $scope.getRoleList = function() {
			RequestService
					.doGET(
							'/procurement/master/panitiaServices/getListRoleUser')
					.then(
							function success(data) {
								$scope.picList = data;
								vm.loading = false;
							},
							function error(response) {
								RequestService
										.informError("Terjadi Kesalahan Pada System");
								vm.loading = false;
							});
		}
		$scope.getRoleList();

        //tree
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
        $scope.try_async_load = function (parentId, id) {
            $scope.my_data = [];
            $scope.doing_async = true;
            var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/organisasi/getAll');
            return remoteTree.get(function (res) {
                $scope.my_data = res.data;
                $scope.doing_async = false;
                return tree.expand_all();
            }).$promise;
        };
        $scope.try_async_load();

        //datepicker
        $scope.chngTglMulai = function () {
            vm.berlakuSelesai = null;
        }
        
        vm.disabled = function (date, mode) {
            return false;
            //return ( mode === 'day' && ( date.getDay() === 0 ||date.getDay() === 6 ) );
        };
        vm.toggleMin = function () {
            vm.minDate = vm.minDate ? null : new Date();
        };
        vm.toggleMin();
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        vm.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        vm.format = vm.formats[4];

        //tanggal Awal
        vm.tanggalOpenAwal = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.tanggalOpenedAwal = true;
        };

        //tanggal Akhir
        vm.tanggalOpenAkhir = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            vm.tanggalOpenedAkhir = true;
        };

       
        $scope.save = function() {
        	RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					vm.panitiaDTO.panitia.divisi.id = vm.divisi.id;
					$scope.saveData();
				}
			});

		}
        
        $scope.validateForm=function(){
        	vm.isValid = true;
        	vm.tglValid=true;
        	vm.errorCabang = '';
        	vm.errorDivisi = '';
        	vm.errorNamaPejabat = '';
        	vm.errorTanggalAkhir = '';
        	vm.errorTanggalMulai = '';
        	vm.errorPIC='';
        	
        	if (typeof vm.output === 'undefined' || vm.output == '') {
				vm.errorCabang = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.divisi === 'undefined' || vm.divisi == null || vm.divisi == '') {
				vm.errorDivisi = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.pejabatPelaksanaPengadaan.nama === 'undefined' || vm.panitiaDTO.pejabatPelaksanaPengadaan.nama == '') {
				vm.errorNamaPejabat = 'template.error.field_kosong';
				vm.isValid = false;
			}
			if (typeof vm.panitiaDTO.pejabatPelaksanaPengadaan.pic === 'undefined' || vm.panitiaDTO.pejabatPelaksanaPengadaan.pic == '') {
				vm.errorPIC = 'template.error.field_kosong';
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
			
			return vm.isValid;
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
											vm.errorNamaPejabat = 'promise.procurement.master.pelaksanapengadaan.error.nama_pejabat_sama';
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

    PelaksanaPengadaanTambahPejabatController.$inject = ['$rootScope','$scope', '$resource', '$filter', 'RequestService', '$state','$stateParams'];

})();
