/**=========================================================
 * Module: vendor.approval.detail.js
 * Modul/Tahapan ID: 8
 * Author: Reinhard
 =========================================================*/
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiVendorApprovalDetailController', RegistrasiVendorApprovalDetailController);

	function RegistrasiVendorApprovalDetailController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $stateParams, ModalService, $timeout) {
		var vm = this;
		$scope.dataVendor = $rootScope.detilvendor;
    	$scope.titleSelect = [{"title":"Mr"},{"title":"Mrs"},{"title":"Company"}];
		
		vm.loading=true;
		
		$scope.loadingSaving=false;
		
		vm.penanggungJawabList = [];
		vm.dataBankList = [];
	    vm.dataSegmentasiList = [];
		vm.beritaAcara = {};
		vm.beritaAcara.vendorStatus = 1; /*default*/
		$scope.negaraList = [];
		vm.showapproval = false;
		vm.dokumenRegistrasiVendorList = [];
		vm.skdVendor = [];
		
		vm.viewFile = function(namaFile) {
			window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
		}
		
		vm.approvalProcess = $rootScope.approvalProcess;
		vm.vendorId =vm.approvalProcess.approvalProcessType.valueId
        vm.note = '';
		//$scope.isPKS = false;
/*		if (typeof vm.approvalProcess.keterangan !== 'undefined'){
			vm.note = vm.approvalProcess.keterangan;
		}*/

		$scope.isUserApproval = false;
		
		$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findVendor/' + vm.approvalProcess.approvalProcessType.valueId).success(function(data) {
			vm.vendorApproval = data;
			if (typeof vm.approvalProcess.approvalLevel !== 'undefined' && vm.approvalProcess.approvalLevel != null) {
				$scope.getApprovalProcess(vm.approvalProcess.approvalProcessType.id);
				$scope.getApprovalProcessAndStatus(vm.approvalProcess.approvalProcessType.id);
			} else {
				$scope.isUserApproval = true;
			}
		}).error(function(data) {
			$scope.loading = false;
		});
		
		
		$scope.getApprovalProcess = function(approvalProcessType) {
			$scope.loadingApproval = true;
			vm.levelList = [];
			$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function(data) {
				vm.levelList = data;
				$scope.loading = false;
			}).error(function(data) {
				$scope.loading = false;
			});
		};
		
		//Approval log
		$scope.getApprovalProcessAndStatus = function(approvalProcessType) {
			$scope.loadingApproval = true;
			$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + approvalProcessType)
				.success(function(data) {
				$scope.statusList = data;
				$scope.loading = false;
			}).error(function(data) {
				$scope.loading = false;
			});
		};
		
		$scope.btnSimpan = function(statusId) {
			$scope.errorNoteMessage = "";
			
			var checkNote = vm.note;
			var isNote = true;
			if(vm.note === null || vm.note === '') {
				isNote = false;
				$scope.errorNoteMessage = "Note tidak boleh kosong";
			} else if ( checkNote.length >= 255) { 
				isNote = false;
				$scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
			} else {
				ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
				$scope.simpan(statusId);
				});
			}				
		}		
		
		
		$scope.simpan = function(statusId){
			
			var data = {};
			
			data.vendor = {
				id: vm.approvalProcess.approvalProcessType.valueId
			};
			
			data.approvalProcess = {
				id: vm.approvalProcess.id,
				status: statusId
			}
			
			data.dokumenRegistrasiVendorList = vm.dokumenRegistrasiVendorList;
			
			data.vendorSkd = [];
			
			if(vm.skdVendor.length != 0) {
				data.vendorSkd = vm.skdVendor;
				data.dokumenRegistrasiVendorList.splice(-1,1);
			}
			
			data.vendorApproval = {
				keterangan : vm.note
			};
			
			if (uploadFileRegister !== undefined && uploadFileRegister.queue.length > 0) {
				angular.forEach(uploadFileRegister.queue, function(item) {
					if(item.isUploaded) {
						data.vendorApproval.realFileName = item.realFileName;
						data.vendorApproval.realFileSize = item.file.size;
					}
				});
			}
			
			//console.log(JSON.stringify(data));
						
			RequestService.doPOSTJSON('/procurement/vendor/vendorServices/approvalCalonVendorDTO', data)
			.then(function successCallback(data) { 
				$scope.showSuccess();
			}, function errorCallback(response) {			
				//ModalService.closeModalInformation();	 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
			});
			
		}

		$scope.showSuccess = function () {
			$scope.loadingSaving = false;
			toaster.pop('success', 'Sukses', 'Approval Vendor berhasil disimpan');
			$timeout (function() {		
				$state.go('app.promise.procurement-approval');
			}, 5000);
			/*$scope.gotoIndex();*/
		}
 
		$scope.getVendorData = function(){
       	 RequestService.doGET('/procurement/vendor/vendorServices/getVendorData/' + vm.vendorId)
			.then(function success(data) {
				if(data != undefined){
					$scope.fillForm(data);
					vm.loading = false;
				}
			},
			function error(response) {
				RequestService.informError("Terjadi Kesalahan Pada System");
		  })
      };
      $scope.getVendorData();
		
      $scope.fillForm = function (data){

          //TAB LOGIN
          vm.namaPengguna = data.vendorUserBean.namaPengguna;
          vm.userId = data.vendorUserBean.username;

          //TAB DATA PERUSAHAAN
          vm.pilihPKP.name = data.vendorProfileBean.jenisPajakPerusahaan;
          vm.nomorPKP = data.vendorProfileBean.nomorPKP;
          vm.titleVendor = data.vendorProfileBean.title;
          vm.kualifikasiVendor = data.vendorProfileBean.kualifikasiVendor;
          vm.mendaftardiunit = data.vendorProfileBean.unitTerdaftar;
          vm.businessArea = data.businessArea;
          vm.NamaPerusahaan = data.vendorProfileBean.namaPerusahaan;
          vm.jenisPerusahaan = data.vendorProfileBean.tipePerusahaan;
          vm.NPWPPerusahaan = data.vendorProfileBean.npwpPerusahaan;
          vm.namaNPWP = data.vendorProfileBean.namaNPWP;
          vm.alamatNPWP = data.vendorProfileBean.alamatNPWP;
          vm.provinsiNPWP = data.vendorProfileBean.provinsiNPWP;
          vm.kotaNPWP = data.vendorProfileBean.kotaNPWP;
          vm.NamaSingkatan = data.vendorProfileBean.namaSingkatan;
          vm.jumlahKaryawan = data.vendorProfileBean.jumlahKaryawan;
          vm.noAktaPendirian = data.vendorProfileBean.noAktaPendirian;
          vm.tanggalBerdiri = data.vendorProfileBean.tanggalBerdiri;
          vm.deskripsi = data.vendor.deskripsi;
          
          vm.alamatPerusahaan = data.vendorProfileBean.alamat;
          vm.provinsiPerusahaan = data.vendorProfileBean.provinsi;
          vm.kotaPerusahaan = data.vendorProfileBean.kota;
          vm.poboxPerusahaan = data.vendorProfileBean.poBox;
          vm.kodeposPerusahaan = data.vendorProfileBean.kodePos;
          vm.TeleponPerusahaan = data.vendorProfileBean.telephone;
          vm.NoFaxPerusahaan = data.vendorProfileBean.faksimile;
          vm.EmailPerusahaan = data.vendorProfileBean.email;
          vm.WebsitePerusahaan = data.vendorProfileBean.website;
          
          vm.NamaContactPerson = data.vendorProfileBean.namaContactPerson;
          vm.NoHPContactPerson = data.vendorProfileBean.hpContactPerson;
          vm.EmailContactPerson = data.vendorProfileBean.emailContactPerson;
          vm.NoKTPContactPerson = data.vendorProfileBean.ktpContactPerson;
          vm.noKK = data.vendorProfileBean.noKK;
          
          vm.penanggungJawabList = data.penanggungJawabList;
          
          vm.isPKS = data.vendor.isPKS;
          
          //TAB DATA BANK
          vm.dataBankTable = new ngTableParams({
              page: 1, // show first page
              count: 5 // count per page
          }, {
              total: data.dataBankList.length, // length of data4
              getData: function ($defer, params) {
                  $defer.resolve(data.dataBankList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
              }
          });
          
          //TAB DATA SEGMENTASI
          vm.tableSegmentasiVendor = new ngTableParams({
              page: 1, // show first page
              count: 5 // count per page
          }, {
              total: data.dataSegmentasiList.length, // length of data4
              getData: function ($defer, params) {
                  $defer.resolve(data.dataSegmentasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
              }
          });
          
          //TAB DATA DOKUMEN
          var dataDokRegFormList = data.dataDokRegFormList;
          if (dataDokRegFormList.length == 0) {
              vm.dataDokRegForm = {};
          } else {
              vm.dataDokRegForm = dataDokRegFormList[0];
              vm.downloadFile1 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokRegForm.fileName;
              /*console.log('$scope.downloadFile = ' + JSON.stringify($scope.dataDokRegForm));*/
          };
          
          var dataDokSKBList = data.dataDokSKBList;
          if (dataDokSKBList.length == 0) {
              vm.dataDokSKBList = {};
          } else {
              vm.dataDokSKB = dataDokSKBList[0];
              vm.downloadFile11 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSKB.fileName;
              /*console.log('$scope.downloadFile = ' + JSON.stringify($scope.dataDokRegForm));*/
          };
          
          var dataDokAkteList = data.dataDokAkteList;
          if (dataDokAkteList.length == 0) {
              vm.dataDokAkte = {};
          } else {
              vm.dataDokAkte = dataDokAkteList[0];
              vm.downloadFile2 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokAkte.fileName;
          };
          	
          var dataDokSalinanList = data.dataDokSalinanList;
          if (dataDokSalinanList.length == 0) {
              vm.dataDokSalinan = {};
          } else {
              vm.dataDokSalinan = dataDokSalinanList[0];
              vm.downloadFile3 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSalinan.fileName;
          };
          //console.log(dataDokSalinanList);
          var dataDokSiupList = data.dataDokSiupList;
          if (dataDokSiupList.length == 0) {
              vm.dataDokSiup = {};
          } else {
              vm.dataDokSiup = dataDokSiupList[0];
              vm.downloadFile4 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSiup.fileName;
          };
          
          var dataDokPKSList = data.dataDokPKSList;
          if (dataDokPKSList.length == 0) {
              vm.dataDokPKS = {};
          } else {
              vm.dataDokPKS = dataDokPKSList[0];
              vm.downloadFile5 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokPKS.fileName;
          };
          
          var dataDokSPRList = data.dataDokSPRList;
          if (dataDokSPRList.length == 0) {
              vm.dataDokSPR = {};
          } else {
              vm.dataDokSPR = dataDokSPRList[0];
              vm.downloadFile6 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSPR.fileName;
          };
          
          var dataDokSPBList = data.dataDokSPBList;
          if (dataDokSPBList.length == 0) {
              vm.dataDokSPB = {};
          } else {
              vm.dataDokSPB = dataDokSPBList[0];
              vm.downloadFile7 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSPB.fileName;
          };
          
          var dataDokBuktiFisikList = data.dataDokBuktiFisikList;
          if (dataDokBuktiFisikList.length == 0) {
             vm.dataDokBuktiFisik = {};
          } else {
             vm.dataDokBuktiFisik = dataDokBuktiFisikList[0];
             vm.downloadFile8 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokBuktiFisik.fileName;
          };
          
          var dataDokQualityList = data.dataDokQualityList;
          if (dataDokQualityList.length == 0) {
             vm.dataDokQuality = {};
          } else {
             vm.dataDokQuality = dataDokQualityList[0];
             vm.downloadFile9 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokQuality.fileName;
          };
          
          var dataDokTeknikList = data.dataDokTeknikList;
          if (dataDokTeknikList.length == 0) {
              vm.dataDokTeknik = {};
          } else {
              vm.dataDokTeknik = dataDokTeknikList[0];
              vm.downloadFile10 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokTeknik.fileName;
          };
          
          var dataDokSKDList = data.dataDokSKDList;
          if (dataDokSKDList.length == 0) {
              vm.dataDokSKD = {};
          } else {
              vm.dataDokSKD = dataDokSKDList[0];
              vm.downloadFile11 = $rootScope.backendAddress +'/file/view/'+ vm.dataDokSKD.fileName;
          };
          
          //TAB DATA PERALATAN
          for (var i = 0; i < data.peralatanListVendor.length; i++) {
              data.peralatanListVendor[i].index = i + 1;
           }
             vm.tablePeralatanVendor = new ngTableParams({
                   page: 1, // show first page
                   count: 5 // count per page
               }, {
                   total: data.peralatanListVendor.length, // length of data4
                   getData: function ($defer, params) {
                  	 $defer.resolve(data.peralatanListVendor.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                   }
               })
             
             //TAB DATA KEUANGAN
             for (var i = 0; i < data.keuanganListVendor.length; i++) {
                 data.keuanganListVendor[i].index = i + 1;
             }
             vm.tableKeuanganVendor = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
             }, {
                    total: data.keuanganListVendor.length, // length of data4
                    getData: function ($defer, params) {
                    	$defer.resolve(data.keuanganListVendor.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
             });
             
             //TAB DATA PENGALAMAN
             for (var i = 0; i < data.pengalamanPelangganVendorList.length; i++) {
                 data.pengalamanPelangganVendorList[i].index = i + 1;
             }
    			vm.tablePengalamanPekerjaan = new ngTableParams({
                 page: 1, // show first page
                 count: 5 // count per page
             }, {
                 total: data.pengalamanPelangganVendorList.length, // length of data4
                 getData: function ($defer, params) {
                     $defer.resolve(data.pengalamanPelangganVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                 }
             });           
    			
    			for (var i = 0; i < data.pengalamanMitraVendorList.length; i++) {
                  data.pengalamanMitraVendorList[i].index = i + 1;
              }
              vm.tableDataMitra = new ngTableParams({
                  page: 1, // show first page
                  count: 5 // count per page
              }, {
                  total: data.pengalamanMitraVendorList.length, // length of data4
                  getData: function ($defer, params) {
                      $defer.resolve(data.pengalamanMitraVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                  }
              });     
              
              for (var i = 0; i < data.pengalamanInProgressVendorList.length; i++) {
                  data.pengalamanInProgressVendorList[i].index = i + 1;
              }
              vm.tableWorkingProgress = new ngTableParams({
                  page: 1, // show first page
                  count: 5 // count per page
              }, {
                  total: data.pengalamanInProgressVendorList.length, // length of data4
                  getData: function ($defer, params) {
                      $defer.resolve(data.pengalamanInProgressVendorList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                  }
              });       
      }
		
	    vm.labelMandatori = '*)';
        
        vm.pilihPKP = {
        		name: '1'
        };
        
        $scope.pilihPKP = function() {
        	if(vm.pilihPKP.name == '2') {
        		vm.disabled = true;
        		vm.labelMandatori = '';
        	} else {
        		vm.disabled = false;
        		vm.labelMandatori = '*)';
        	}
        }
        
        $scope.jenisPerusahaanList = [   { id: 1, nama: "PT"  },
                                         { id: 2, nama: "CV"  },
                                         { id: 3, nama: "PO"  }
                                     ];
		
      
        $scope.showJabatan = function(pic) {
			var selected = [];
			if (pic.jabatan) {
				selected = $filter('filter')($scope.jabatanPenanggungJawabList, {
					id: pic.jabatan.id
				});
			}
			return selected.length ? selected[0].nama : 'Not set';
        };
    	
	     
        /*tombol aksi back to index*/
		$scope.gotoIndex = function () {
			$location.path('/app/promise/procurement/approval');
		};
		
		vm.ceklistCheck = function(){
			var check = true;// sbnernya loopingnya gak ada gunanya
			vm.dokumenRegistrasiVendorList.forEach(function(value, index, array) {
				if(value.fileName != null && value.fileName != '' ){
	   				if(value.statusCeklist == null){
						 check = false;
					}
				}
			});
			vm.showapproval = check;
		}
  
		var uploadFileRegister = $scope.uploadFileRegister = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		
		uploadFileRegister.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		uploadFileRegister.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			
			if(uploadFileRegister.queue.length > 1){
				uploadFileRegister.queue[0].remove();
			}
			
		};
		
		$scope.uploadFile = function() {
			if(uploadFileRegister.queue != undefined && uploadFileRegister.queue.length > 0) {
				angular.forEach(uploadFileRegister.queue, function(item) {
	        		item.upload();
	            });
			}
		}
	  
	}
	
	RegistrasiVendorApprovalDetailController.$inject = ['RequestService', '$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster','$location', '$stateParams', 'ModalService','$timeout'];

})();