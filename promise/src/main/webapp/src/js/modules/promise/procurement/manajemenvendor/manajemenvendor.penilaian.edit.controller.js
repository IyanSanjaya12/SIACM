
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('PenilaianVendorEditController', PenilaianVendorEditController);

	function PenilaianVendorEditController($modal, ModalService, RequestService, $scope, $http, $rootScope, $resource, $location, $state, $q, toaster) {
		var vm = this;
		vm.dataVendor = $rootScope.penilaianDataVendor; 
		vm.penilaianVendor = {}
		vm.penilaianVendor.vPerfNilaiAkhir = 0;
		vm.penilaianVendor.vPerfKesimpulanPenilaian = '';		
		vm.penilaianVendor.vendor = vm.dataVendor;
		vm.penilaianVendor.purchaseOrder = {}
		vm.penilaianVendor.purchaseOrder.poNumber = '';	
		
		vm.vPerfTglPenilaianOpened = false;
		vm.vPerfTglPenilaianOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.vPerfTglPenilaianOpened = true;
		};
		
		vm.vPerfTglKontrakOpened = false;
		vm.vPerfTglKontrakOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.vPerfTglKontrakOpened = true;
		};
		
		vm.vPerfKontrakTglAwalKerjaOpened = false;
		vm.vPerfKontrakTglAwalKerjaOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.vPerfKontrakTglAwalKerjaOpened = true;
		};
		
		vm.vPerfKontrakTglAkhirKerjaOpened = false;
		vm.vPerfKontrakTglAkhirKerjaOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			vm.vPerfKontrakTglAkhirKerjaOpened = true;
		};
	        
		vm.getIndikatorPenilaian = function () { 
			RequestService.doGET('/procurement/master/IndikatorPenilaianServices/getIndikatorPenilaianList')
			.then(function (data, status, headers, config) { 
				vm.indikatorPenilaianList = data;
				vm.loading = false;
			}); 
		}
		
		vm.getIndikatorPenilaian();
		
		vm.getFormulaPenilaian = function () { 
			RequestService.doGET('/procurement/master/FormulaPenilaianServices/getFormulaPenilaianList')
			.then(function (data, status, headers, config) { 
				vm.formulaPenilaianList = data;
				//console.log('isi formulaPenilaianList '+ JSON.stringify(vm.formulaPenilaianList));
				vm.loading = false;
			}); 
		}
		
		vm.getFormulaPenilaian();
		
		vm.getMataUang = function () { 
			RequestService.doGET('/procurement/master/mata-uang/get-list')
			.then(function (data, status, headers, config) { 
				vm.mataUangList = data;
				vm.loading = false;
			}); 
		}
		
		//get deskripsi penilaian
		vm.getDeskripsiPenilaian = function (score) { 
			RequestService.doGET('/procurement/master/FormulaPenilaianServices/getDeskripsiPenilaian/'+score)
			.then(function (data, status, headers, config) { 
				vm.penilaianVendor.vPerfKesimpulanPenilaian = data.deskripsi;
				vm.loading = false;
			}); 
		}
		
		vm.getMataUang();
		
		vm.formulaPenilaian = function(fp){
			//console.log('isi fp '+JSON.stringify(fp));
			var result = '';
			if(fp != null && fp != ''){
				result = fp.fPBatasKiri + ' ';

				if(fp.fPPersamaanKiri == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKiri == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKiri == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKiri == 'lt'){ result = result + ' <';}

				result = result + ' x ';
								
				if(fp.fPPersamaanKanan == 'ge'){ result = result + ' >=';}
				else if(fp.fPPersamaanKanan == 'le'){ result = result + ' <=';}
				else if(fp.fPPersamaanKanan == 'gt'){ result = result + ' >';}
				else if(fp.fPPersamaanKanan == 'lt'){ result = result + ' <';}

				result = result + ' ' + fp.fPBatasKanan;
				
			}
			return result;
		}

		vm.penilaianBack = function(){
			$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
		}
		
		//dipindah ke beckend formulaPenilaian.services
		/*vm.getFinalNote = function(finalScore){
			console.log('isi final Score '+ JSON.stringify(finalScore));
			angular.forEach(vm.formulaPenilaianList,function(formula ){
				if(formula.fPPersamaanKiri == 'ge'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 1');} 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 2');} 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 3');} 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) >= Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 4');} 
					}
				}else if(formula.fPPersamaanKiri == 'le'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 5');} 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; 
							console.log('kondisi 6');} 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription; 
							console.log('kondisi 7');} 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) <= Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 8');} 
					}
				}else if(formula.fPPersamaanKiri == 'gt'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 9');} 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 10');} 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 11');} 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) > Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 12');} 
					}
				}else if(formula.fPPersamaanKiri == 'lt'){
					if(formula.fPPersamaanKanan == 'ge'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) >= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 13');} 
					}else if(formula.fPPersamaanKanan == 'le'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) <= Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 14');} 
					}else if(formula.fPPersamaanKanan == 'gt'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) > Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 15');} 
					}else if(formula.fPPersamaanKanan == 'lt'){
						if((Number(finalScore) < Number(formula.fPBatasKiri)) && (Number(finalScore) < Number(formula.fPBatasKanan))){ 
							vm.penilaianVendor.vPerfKesimpulanPenilaian = formula.fPDescription;
							console.log('kondisi 16');} 
					}
				}
			});
		};*/
		
		vm.calculateValue = function(idIndikator, idFormula, score){
			vm.penilaianVendor.vPerfNilaiAkhir = 0

			vm.indikatorPenilaianList.forEach(function(indikator, index, array) {
				if(indikator.iPId == idIndikator){
					
					var finalScoreDetail = (Number(indikator.iPBobot)  * Number(score)) / 100;
					indikator.ipNilaiHasil = finalScoreDetail.toFixed(2);
					
					indikator.ipNilaiFormulaPk = idFormula;
				}

				vm.penilaianVendor.vPerfNilaiAkhir = (Number(vm.penilaianVendor.vPerfNilaiAkhir) + (indikator.ipNilaiHasil == null ? Number(0) : Number(indikator.ipNilaiHasil))).toFixed(2);
			});
			
			if(vm.penilaianVendor.vPerfNilaiAkhir > 0){ 
				//vm.getFinalNote(vm.penilaianVendor.vPerfNilaiAkhir);
				vm.getDeskripsiPenilaian(vm.penilaianVendor.vPerfNilaiAkhir);
			}
		}
				
		vm.penilaianSave = function(){
			vm.mdlName = 'Data Pengadaan';
			/** validasi **/
			var next = true;
			if(vm.penilaianVendor.vPerfTglPenilaian == null || vm.penilaianVendor.vPerfTglPenilaian == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal penilaian tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.purchaseOrder.poNumber == null || vm.penilaianVendor.purchaseOrder.poNumber == ''){
				toaster.pop('error', vm.mdlname, 'Nomor Purchase Order tidak boleh kosong!');
				next = false;
			}/*else if(vm.penilaianVendor.vPerfTglKontrak == null || vm.penilaianVendor.vPerfTglKontrak == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal kontrak tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfNomorPengadaan == null || vm.penilaianVendor.vPerfNomorPengadaan == ''){
				toaster.pop('error', vm.mdlname, 'Nomor pengadaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfPaketPekerjaan == null || vm.penilaianVendor.vPerfPaketPekerjaan == ''){
				toaster.pop('error', vm.mdlname, 'Paket pekerjaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfPenggunaHasilPekerjaan == null || vm.penilaianVendor.vPerfPenggunaHasilPekerjaan == ''){
				toaster.pop('error', vm.mdlname, 'Pengguna hasil pekerjaan tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.mataUang  == null || vm.penilaianVendor.mataUang == ''){
				toaster.pop('error', vm.mdlname, 'Mata uang harus dipilih!');
				next = false;
			}else if(vm.penilaianVendor.vPerfNilaiKontrak == null || vm.penilaianVendor.vPerfNilaiKontrak == ''){
				toaster.pop('error', vm.mdlname, 'Nilai kontrak tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfKontrakTglAwalKerja == null || vm.penilaianVendor.vPerfKontrakTglAwalKerja == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal awal kerja tidak boleh kosong!');
				next = false;
			}else if(vm.penilaianVendor.vPerfKontrakTglAkhirKerja == null || vm.penilaianVendor.vPerfKontrakTglAkhirKerja == ''){
				toaster.pop('error', vm.mdlname, 'Tanggal akhir kerja tidak boleh kosong!');
				next = false;
			}*/else if(vm.penilaianVendor.vPerfCatatanUser == null || vm.penilaianVendor.vPerfCatatanUser == ''){
				toaster.pop('error', vm.mdlname, 'Catatan user tidak boleh kosong!');
				next = false;
			}else{ 
				var check = true;
				vm.indikatorPenilaianList.forEach(function(indikator, index, array) {
					if(check == true){
						if(indikator.ipNilaiFormulaPk == null || indikator.ipNilaiFormulaPk == ''){
							toaster.pop('error', 'Data Penilaian', 'Penilaian aspek kerja ' + indikator.iPAspekKerja + ' harus dipilih!');
							check = false;
							next = false;
						}
					}
				})
			}
			
			if(next == true){
				var data = {}
				data.penilaianVendor = vm.penilaianVendor;
				data.penilaianVendorIndikatorList = [];
				
				var count = 0;
				vm.indikatorPenilaianList.forEach(function(indikator, index, array) {
					var penilaianVendorIndikator = {}
					penilaianVendorIndikator.vPerfAspSequence = count;
					
					var indikatorPenilaian = {}
					indikatorPenilaian.iPId = indikator.iPId; 
					
					penilaianVendorIndikator.indikatorPenilaian = indikatorPenilaian;
					
					penilaianVendorIndikator.vPerfAspSequence = count;
					penilaianVendorIndikator.vPerfAspNilaiAkhir = indikator.ipNilaiHasil;

					var formulaPenilaian = {}
					formulaPenilaian.fPId=indikator.ipNilaiFormulaPk;
					
					penilaianVendorIndikator.formulaPenilaian = formulaPenilaian;
					
					data.penilaianVendorIndikatorList.push(penilaianVendorIndikator);
					count++; 
	 			}); 

				ModalService.showModalConfirmation().then(function (result) {
					ModalService.showModalInformationBlock();
					RequestService.doPOSTJSON('/procurement/vendormanagement/PenilaianVendorServices/insertPenilaianVendorAll', data)
					.then(function successCallback(response) {
						$location.path('/app/promise/procurement/manajemenvendor/penilaian/view');
						ModalService.closeModalInformation();
					 }, function errorCallback(response) {
						ModalService.closeModalInformation();
						ModalService.showModalInformation('Terjadi kesalahan pada system!');
					});
				});
			}			
		}
	
		vm.findKontrak = function () {
			vm.approve = {};
	    	vm.todo = 'add';
	        var addModalInstance = $modal.open({
	            templateUrl: '/findPurchaseOrder.html',
	            controller: AddModalInstance,
	            size: 'lg'
	        });
	
	        addModalInstance.result.then(function () {
	        	 
	        });
		}
	
		var AddModalInstance = function (RequestService, ModalService, $scope, $modalInstance, $rootScope, toaster) {

            var form = $scope;
        	form.approvalList = [];
        	form.approval = {} 
        	form.dataVendor = vm.dataVendor; 
        	
        	form.modalCancel = function () {
                $modalInstance.dismiss('cancel');
            };
	        
            form.getPurchaseOrderList = function () { 
    			RequestService.doGET('/procurement/purchaseorder/PurchaseOrderServices/getPurchaseOrderListByVendorForPerformance/' + form.dataVendor.id)
    			.then(function (data, status, headers, config) { 
    				form.purchaseOrderList = data;
    			}); 
    		}
    		
    		form.getPurchaseOrderList();
    		
    		form.getData = function(data){ 
    			vm.penilaianVendor.purchaseOrder = {}
    			vm.penilaianVendor.purchaseOrder = data; 
    			
                $modalInstance.dismiss('cancel');    			
    		}        	 
        };
        AddModalInstance.$inject = ['RequestService', 'ModalService', '$scope', '$modalInstance', '$rootScope', 'toaster'];
	}

	PenilaianVendorEditController.$inject = ['$modal', 'ModalService', 'RequestService', '$scope','$http', '$rootScope', '$resource', '$location', '$state', '$q', 'toaster'];

})();

