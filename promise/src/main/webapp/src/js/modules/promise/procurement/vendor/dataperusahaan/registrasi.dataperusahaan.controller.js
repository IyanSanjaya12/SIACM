/**=========================================================
 * Module: RegistrasiDataPerusahaanController.js
 * Author: F.H.K
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiDataPerusahaanController', RegistrasiDataPerusahaanController);

	function RegistrasiDataPerusahaanController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $timeout, $window) {
		
		var vm = this;
		var formDataVendorDraft = {};
		var formDataVendorProfileDraft = {};
        vm.penanggungJawabList = [];
                
        vm.id = $rootScope.userLogin.user.id;
        vm.namaPengguna = $rootScope.userLogin.user.namaPengguna;
        vm.userId = $rootScope.userLogin.user.username;
        vm.isChange = false;
        vm.istitle = false;
        vm.isCompany = true;
        vm.valid=false;
        vm.status = null;
        
        vm.pilihPKP = {
        		name: '1'
        };

        vm.jenisPerusahaanList = [ {
			id : 1,
			nama : "PT"
			}, {
			id : 2,
			nama : "CV"
			}, {
			id : 3,
			nama : "PO"
		} ];
        
        vm.titleList = [ { 
        	title : "Mr"
        	}, {
        	title: "Mrs"
        	}, {
        	title: "Company"
        } ];
        
        vm.checkIsChange =  function(){
        	vm.isChange = true;
        }
          
        vm.checkIsTitle =  function(){
        	vm.istitle = true;
        }
        
        vm.dateOptions = {
    			formatYear : 'yy',
    			startingDay : 1
    		};
    		
    	vm.formats = [ 'dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd' ];
    	vm.format = vm.formats[4];
        
		$scope.tanggalBerdiriOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalBerdiriStatus = true;
		}
		
		$scope.getVendorData = function(){
       	 RequestService.doGET('/procurement/vendor/vendorServices/getVendorDataPerusahaan/' + vm.id)
			.then(function success(data) {
				if(data != undefined){
					$scope.fillForm(data);
					vm.loading = false;
				}
			},
			function error(response) {
				RequestService.modalInformation('template.informasi.gagal', 'danger');
		  })
      };
      $scope.getVendorData();
      
      $scope.fillForm = function (data){
    	  vm.status=data.status;
    	  if(data.status==0){
          vm.pilihPKP.name = data.vendorProfileBean.jenisPajakPerusahaan;
          vm.nomorPKP = data.vendorProfileBean.nomorPKP;
          vm.titleVendor = data.vendorProfileBean.title;
          vm.kualifikasiVendor = data.vendorProfileBean.kualifikasiVendor;
          vm.unitTerdaftar = data.vendorProfileBean.unitTerdaftar;
          vm.businessArea = data.businessAreaOrganisasi;
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
          vm.isApproval = data.vendor.isApproval;
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
          vm.NoKKContactPerson = data.vendorProfileBean.noKK;
          
          vm.jabatanList = data.jabatanPenanggungJawabList;
          vm.kualifikasiVendorList = data.kualifikasiVendorList;
          vm.unitTerdaftarList = data.unitTerdaftarList;
          vm.provinsiNPWPList = data.provinsiPerusahaanList;
          vm.provinsiPerusahaanList = data.provinsiPerusahaanList;
          vm.wilayahList = data.wilayahList;
          
          angular.forEach(vm.unitTerdaftarList,function(value,index){
  			if(vm.unitTerdaftarList[index].nama == data.vendorProfileBean.unitTerdaftar)
  				vm.unitTerdaftar = vm.unitTerdaftarList[index];
  			});
          
          angular.forEach(vm.jenisPerusahaanList,function(value,index){
  			if(vm.jenisPerusahaanList[index].nama == data.vendorProfileBean.tipePerusahaan)
  				vm.jenisPerusahaan = vm.jenisPerusahaanList[index];
  			});
          
          angular.forEach(vm.titleList,function(value,index){
  			if(vm.titleList[index].title == data.vendorProfileBean.title)
  				vm.titleVendor = vm.titleList[index];
	  			
	  				if(data.vendorProfileBean.title == "Company"){
						vm.isCompany = true;
					}else{
						vm.isCompany = false;
					}
  			});
          
          if(vm.wilayahList == undefined) {
        	  vm.wilayahList = $rootScope.wilayahList;
          }
          
          angular.forEach(vm.provinsiNPWPList,function(value,index){
  			if(vm.provinsiPerusahaanList[index].lokasi_nama == data.vendorProfileBean.provinsiNPWP){
  				vm.provinsiNPWP = vm.provinsiNPWPList[index];
  				$scope.listWilayahKotaNPWP(vm.provinsiNPWPList[index].lokasi_propinsi);
  				angular.forEach(vm.wilayahList,function(value,index){
           			if(vm.wilayahList[index].lokasi_nama == data.vendorProfileBean.kotaNPWP) {
           				//console.log('Masuk sini');
           				vm.kotaNPWP = vm.wilayahList[index];
           			}
           				
           		});
  			}	
  		  });
          
          angular.forEach(vm.provinsiPerusahaanList,function(value,index){
  			if(vm.provinsiPerusahaanList[index].lokasi_nama == data.vendorProfileBean.provinsi){
  				vm.provinsiPerusahaan = vm.provinsiPerusahaanList[index];
  				$scope.listWilayahKota(vm.provinsiPerusahaanList[index].lokasi_propinsi);
  				angular.forEach(vm.wilayahList,function(value,index){
          			if(vm.wilayahList[index].lokasi_nama == data.vendorProfileBean.kota)
          				vm.kotaPerusahaan = vm.wilayahList[index];
          		});
  			}	
  		  });
          
          angular.forEach(data.penanggungJawabList,function(value,indexData){
  			angular.forEach(vm.jabatanList,function(value,indexPIC){
  				if(vm.jabatanList[indexPIC].id == data.penanggungJawabList[indexData].jabatan.id){
  					$scope.showJabatan(data.penanggungJawabList[indexData]);
          			vm.penanggungJawabList.push(data.penanggungJawabList[indexData]);
  				}
  			});
  		  });
          
          vm.penanggungJawabList = data.penanggungJawabList;
          vm.isPKS = data.vendor.isPKS;
          vm.vendorId = data.vendor.id;
          vm.vendorProfileId = data.vendorProfileBean.id;
          $scope.getBusinessArea(vm.unitTerdaftar.id);
    	  }else{
    		  vm.pilihPKP.name = data.vendorProfileBeanDraft.jenisPajakPerusahaan;
              vm.nomorPKP = data.vendorProfileBeanDraft.nomorPKP;
              vm.titleVendor = data.vendorProfileBeanDraft.title;
              vm.kualifikasiVendor = data.vendorProfileBeanDraft.kualifikasiVendor;
              vm.unitTerdaftar = data.vendorProfileBeanDraft.unitTerdaftar;
              vm.businessArea = data.businessAreaOrganisasi;
              vm.NamaPerusahaan = data.vendorProfileBeanDraft.namaPerusahaan;
              vm.jenisPerusahaan = data.vendorProfileBeanDraft.tipePerusahaan;
              vm.NPWPPerusahaan = data.vendorProfileBeanDraft.npwpPerusahaan;
              vm.namaNPWP = data.vendorProfileBeanDraft.namaNPWP;
              vm.alamatNPWP = data.vendorProfileBeanDraft.alamatNPWP;
              vm.provinsiNPWP = data.vendorProfileBeanDraft.provinsiNPWP;
              vm.kotaNPWP = data.vendorProfileBeanDraft.kotaNPWP;
              vm.NamaSingkatan = data.vendorProfileBeanDraft.namaSingkatan;
              vm.jumlahKaryawan = data.vendorProfileBeanDraft.jumlahKaryawan;
              vm.noAktaPendirian = data.vendorProfileBeanDraft.noAktaPendirian;
              vm.tanggalBerdiri = data.vendorProfileBeanDraft.tanggalBerdiri;
              vm.deskripsi = data.vendorDraft.deskripsi;
              vm.vendorProfileCreated=data.vendorProfileBeanDraft.created;
              vm.vendorCreated=data.vendorDraft.created;
              vm.alamatPerusahaan = data.vendorProfileBeanDraft.alamat;
              vm.provinsiPerusahaan = data.vendorProfileBeanDraft.provinsi;
              vm.kotaPerusahaan = data.vendorProfileBeanDraft.kota;
              vm.poboxPerusahaan = data.vendorProfileBeanDraft.poBox;
              vm.kodeposPerusahaan = data.vendorProfileBeanDraft.kodePos;
              vm.TeleponPerusahaan = data.vendorProfileBeanDraft.telephone;
              vm.NoFaxPerusahaan = data.vendorProfileBeanDraft.faksimile;
              vm.EmailPerusahaan = data.vendorProfileBeanDraft.email;
              vm.WebsitePerusahaan = data.vendorProfileBeanDraft.website;
              vm.isApproval= data.vendorDraft.vendor.isApproval;
              vm.NamaContactPerson = data.vendorProfileBeanDraft.namaContactPerson;
              vm.NoHPContactPerson = data.vendorProfileBeanDraft.hpContactPerson;
              vm.EmailContactPerson = data.vendorProfileBeanDraft.emailContactPerson;
              vm.NoKTPContactPerson = data.vendorProfileBeanDraft.ktpContactPerson;
              vm.NoKKContactPerson = data.vendorProfileBeanDraft.noKK;
              
              vm.jabatanList = data.jabatanPenanggungJawabList;
              vm.kualifikasiVendorList = data.kualifikasiVendorList;
              vm.unitTerdaftarList = data.unitTerdaftarList;
              vm.provinsiNPWPList = data.provinsiPerusahaanList;
              vm.provinsiPerusahaanList = data.provinsiPerusahaanList;
              vm.wilayahList = data.wilayahList;
              
              angular.forEach(vm.unitTerdaftarList,function(value,index){
      			if(vm.unitTerdaftarList[index].nama == data.vendorProfileBeanDraft.unitTerdaftar)
      				vm.unitTerdaftar = vm.unitTerdaftarList[index];
      			});
              
              angular.forEach(vm.jenisPerusahaanList,function(value,index){
      			if(vm.jenisPerusahaanList[index].nama == data.vendorProfileBeanDraft.tipePerusahaan)
      				vm.jenisPerusahaan = vm.jenisPerusahaanList[index];
      			});
              
              angular.forEach(vm.titleList,function(value,index){
      			if(vm.titleList[index].title == data.vendorProfileBeanDraft.title)
      				vm.titleVendor = vm.titleList[index];
    	  			
    	  				if(data.vendorProfileBeanDraft.title == "Company"){
    						vm.isCompany = true;
    					}else{
    						vm.isCompany = false;
    					}
      			});
              
              if(vm.wilayahList == undefined) {
            	  vm.wilayahList = $rootScope.wilayahList;
              }
              
              angular.forEach(vm.provinsiNPWPList,function(value,index){
      			if(vm.provinsiPerusahaanList[index].lokasi_nama == data.vendorProfileBeanDraft.provinsiNPWP){
      				vm.provinsiNPWP = vm.provinsiNPWPList[index];
      				$scope.listWilayahKotaNPWP(vm.provinsiNPWPList[index].lokasi_propinsi);
      				angular.forEach(vm.wilayahList,function(value,index){
               			if(vm.wilayahList[index].lokasi_nama == data.vendorProfileBeanDraft.kotaNPWP) {
               				//console.log('Masuk sini');
               				vm.kotaNPWP = vm.wilayahList[index];
               			}
               				
               		});
      			}	
      		  });
              
              angular.forEach(vm.provinsiPerusahaanList,function(value,index){
      			if(vm.provinsiPerusahaanList[index].lokasi_nama == data.vendorProfileBeanDraft.provinsi){
      				vm.provinsiPerusahaan = vm.provinsiPerusahaanList[index];
      				$scope.listWilayahKota(vm.provinsiPerusahaanList[index].lokasi_propinsi);
      				angular.forEach(vm.wilayahList,function(value,index){
              			if(vm.wilayahList[index].lokasi_nama == data.vendorProfileBeanDraft.kota)
              				vm.kotaPerusahaan = vm.wilayahList[index];
              		});
      			}	
      		  });
              
              angular.forEach(data.vendorPICDraftList,function(value,indexData){
      			angular.forEach(vm.jabatanList,function(value,indexPIC){
      				if(vm.jabatanList[indexPIC].id == data.vendorPICDraftList[indexData].jabatan.id){
      					$scope.showJabatan(data.vendorPICDraftList[indexData]);
              			vm.penanggungJawabList.push(data.vendorPICDraftList[indexData]);
      				}
      			});
      		  });
              
              vm.penanggungJawabList = data.vendorPICDraftList;
              vm.isPKS = data.vendorDraft.isPKS;
              vm.vendorId = data.vendorDraft.id;
              vm.vendorProfileId = data.vendorProfileBeanDraft.id;
              $scope.getBusinessArea(vm.unitTerdaftar.id);
    		  
    		  }
    	  if(vm.status==1){
				vm.valid=true;
				vm.subjudul="promise.procurement.RegistrasiVendor.DataPerusahaan.judul_draft";
			}else{
				vm.subjudul="promise.procurement.RegistrasiVendor.DataPerusahaan.judul";
				
			}
			if(vm.isApproval==1){
				vm.disable=true;
				vm.subjudul="promise.procurement.RegistrasiVendor.DataPerusahaan.judul_draft_approve";
			}
      }
      
      $scope.getBusinessArea = function(parentId) {
    	  RequestService.doGET('/procurement/master/organisasi/get-list-by-parent-id/' + parentId)
			.then(function success(data) {
				vm.businessAreaList = data;
			},
			function error(response) {
				RequestService.modalInformation('template.informasi.gagal', 'danger');
		  })
      };
      
      $scope.pilihPropinsi = function() {
    	  $scope.listWilayahKota(vm.provinsiPerusahaan.lokasi_propinsi);
      }
      
      $scope.pilihPropinsiNPWP = function() {
    	  $scope.listWilayahKotaNPWP(vm.provinsiNPWP.lokasi_propinsi);
      }
      
      $scope.listWilayahKotaNPWP = function(kodePropinsiNPWP) {
          RequestService.doGET('/procurement/master/WilayahServices/getKotaList/' + kodePropinsiNPWP)
			.then(function success(data) {
				vm.kotaNPWPList = data;
			},
			function error(response) {
				RequestService.modalInformation('template.informasi.gagal', 'danger');
		  })
      };
      
      $scope.listWilayahKota = function(kodePropinsi) {
          RequestService.doGET('/procurement/master/WilayahServices/getKotaList/' + kodePropinsi)
			.then(function success(data) {
				vm.kotaPerusahaanList = data;
			},
			function error(response) {
				RequestService.modalInformation('template.informasi.gagal', 'danger');
		  })
      };
      
      $scope.isCompanyCheck = function () {
			if(vm.titleVendor.title == "Company"){
				vm.isCompany = true;
			}else{
				vm.isCompany = false;
			}
		}
      
      $scope.validasiEmail = function(email){  
			var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/; 
			var lanjut = true;
			if(regex.test(email)==false){ 
				RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.email', 'danger');
				lanjut = false;
			} 
			return lanjut;
	  }
      
      $scope.checkPKP = function(noPKP) {
      	RequestService.doGET('/procurement/vendor/VendorProfileServices/getVendorProfileByNoPKP/' + noPKP)
		.then(function success(data) {
			if(data.length > 0) {
				RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.pkp_ada', 'danger');
          	} else {
          		RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.pkp_baru', 'info');
          	}
		},
		function error(response) {
			RequestService.modalInformation('template.informasi.gagal', 'danger');
		});
      }
      
      $scope.checkNPWP = function (noNPWP, flag) {
			if (vm.NPWPPerusahaan == undefined || vm.NPWPPerusahaan == "") {
				vm.NPWPPerusahaanError = true;
				document.getElementsByName("NPWPPerusahaan")[0].focus();
			} else {
				vm.NPWPPerusahaanError = false;
				RequestService.doGET('/procurement/vendor/VendorProfileServices/getVendorProfileByNoNPWP/' + noNPWP)
					.then(function success(data) {
						if (data.length > 0) {
							RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.npwp_ada', 'danger');
						} else {
							if (flag != 1) {
								RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.npwp_baru', 'info');
							}
						}
					},
					function error(response) {
						RequestService.modalInformation('template.informasi.gagal', 'danger');
					});
			}
		}
      
      //add penanggungjawab
      $scope.addPenanggungJawab = function() {
      	vm.dataInserted = {
                 
      	};
      	vm.penanggungJawabList.push(vm.dataInserted);
      };

      $scope.removePenanggungJawab = function(index, dataPIC) {
      	vm.penanggungJawabList.splice(index, 1);
      	
      };
      
      $scope.cancelPenanggungJawab = function (rowform, index) {
      	vm.penanggungJawabList.splice(index, 1);
      };
      
      $scope.showJabatan = function(pic) {
			var selected = [];
			if (pic.jabatan) {
				selected = $filter('filter')(vm.jabatanList, {
					id: pic.jabatan.id
				});
			}
			return selected.length ? selected[0].nama : 'Not set';
      };
      
      $scope.checkName = function(data, indeks) {
      	if (data !== undefined && data !== null && data.length > 0) {
      		
      	} else {
      		return 'silahkan masukkan nama penanggung jawab!';
      	}
      };
      
      $scope.checkEmail = function(data, indeks) {
      	if (data !== undefined && data !== null && data.length > 0) {
      	}
      	if (data.length >100){
      		return 'email maksimal 100 karakter';
      	}
          if (data.match(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/)) {
              
		  }else {
			 return 'format email salah';
		  }        
        };
      
      $scope.checkJabatan = function(data, indeks) {
      	if (typeof data == 'undefined') {
      		return "silahkan pilih jabatan dari penanggung jawab";
      	}
      };
      
      $scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				$scope.btnSimpan();
			});
		};
        
       $scope.btnSimpan = function() {
            
			if(vm.pilihPKP.name == '1') {
				if(vm.nomorPKP == undefined || vm.nomorPKP == "") {
	            	vm.nomorPKPError = true;
	            } else {
	            	vm.nomorPKPError = false;
	            } 

			} else {
				vm.nomorPKPError = false;
				vm.NPWPPerusahaanError = false;
			}
			
			//console.log(vm.NPWPPerusahaan);
			if(vm.NPWPPerusahaan == undefined || vm.NPWPPerusahaan == "") {
            	vm.NPWPPerusahaanError = true;
            } else {
            	vm.NPWPPerusahaanError = false;
            } 
            
			if(vm.kualifikasiVendor == undefined || vm.kualifikasiVendor == "") {
            	vm.kualifikasiVendorError = true;
            } else {
            	vm.kualifikasiVendorError = false;
            } 
            
            if (vm.titleVendor == undefined || vm.titleVendor == "") {
				vm.TitleError = true;
				
			} else {
				vm.TitleError = false;
			}
            
			if(vm.unitTerdaftar == undefined || vm.unitTerdaftar == "") {
            	vm.unitTerdaftarError = true;
            } else {
            	vm.unitTerdaftarError = false;
            } 
			
			if(vm.NamaPerusahaan == undefined || vm.NamaPerusahaan == "") {
            	vm.NamaPerusahaanError = true;
            } else {
            	vm.NamaPerusahaanError = false;
            } 
			
			if(vm.jenisPerusahaan == undefined || vm.jenisPerusahaan == "") {
            	vm.jenisPerusahaanError = true;
            } else {
            	vm.jenisPerusahaanError = false;
            } 
			
			if(vm.NamaSingkatan == undefined || vm.NamaSingkatan == "") {
            	vm.NamaSingkatanError = true;
            } else {
            	vm.NamaSingkatanError = false;
            } 
			
			if(vm.alamatPerusahaan == undefined || vm.alamatPerusahaan == "") {
            	vm.alamatPerusahaanError = true;
            } else {
            	vm.alamatPerusahaanError = false;
            } 
			
			if(vm.provinsiPerusahaan == undefined || vm.provinsiPerusahaan == "") {
            	vm.provinsiPerusahaanError = true;
            } else {
            	vm.provinsiPerusahaanError = false;
            } 
			
			if(vm.kotaPerusahaan == undefined || vm.kotaPerusahaan == "") {
            	vm.kotaPerusahaanError = true;
            } else {
            	vm.kotaPerusahaanError = false;
            } 
			
			if(vm.TeleponPerusahaan == undefined || vm.TeleponPerusahaan == "") {
            	vm.TeleponPerusahaanError = true;
            } else {
            	vm.TeleponPerusahaanError = false;
            } 
			
			if(vm.EmailPerusahaan == undefined || vm.EmailPerusahaan == "") {
            	vm.EmailPerusahaanError = true;
            } else {
            	vm.EmailPerusahaanError = false;
            } 
			
			if(vm.NamaContactPerson == undefined || vm.NamaContactPerson == "") {
            	vm.NamaContactPersonError = true;
            } else {
            	vm.NamaContactPersonError = false;
            } 
			
			if(vm.NoHPContactPerson == undefined || vm.NoHPContactPerson == "") {
            	vm.NoHPContactPersonError = true;
            } else {
            	vm.NoHPContactPersonError = false;
            } 
			
			if(vm.EmailContactPerson == undefined || vm.EmailContactPerson == "") {
            	vm.EmailContactPersonError = true;
            } else {
            	vm.EmailContactPersonError = false;
            }
			
			if(vm.NoKTPContactPerson == undefined || vm.NoKTPContactPerson == "") {
            	if (vm.isCompany){
            		vm.NoKTPContactPersonError = false;	
            	}else{
            		vm.NoKTPContactPersonError = true;
            	}
            } else {
            	vm.NoKTPContactPersonError = false;
            }
			
            if (vm.businessArea == undefined || vm.businessArea == "") {
				vm.bussinessAreaError = true;
			} else {
				vm.bussinessAreaError = false;
			}
            
            if (vm.namaNPWP == undefined || vm.namaNPWP == "") {
				vm.namaNPWPError = true;
			} else {
				vm.namaNPWPError = false;
			}
			
			if (vm.alamatNPWP == undefined || vm.alamatNPWP == "") {
				vm.alamatNPWPError = true;
			} else {
				vm.alamatNPWPError = false;
			}
			
			if (vm.kotaNPWP == undefined || vm.kotaNPWP == "") {
				vm.kotaNPWPError = true;
			} else {
				vm.kotaNPWPError = false;
			}
			
			if(vm.isCompany == true) {
				if (vm.noAktaPendirian == undefined || vm.noAktaPendirian == "") {
					vm.noAktaPendirianError = true;
				} else {
					vm.noAktaPendirianError = false;
				}
			}		
			else {
				vm.noAktaPendirianError = false;
			}
			
			if (vm.NoKKContactPerson == undefined || vm.NoKKContactPerson == "") {
				if(vm.isCompany){
					vm.NoKKContactPersonError = false;
				}else{
					vm.NoKKContactPersonError = true;
				}
			} else {
				vm.NoKKContactPersonError = false;
			}
            
			if(vm.kualifikasiVendorError == false && vm.unitTerdaftarError == false && vm.NamaPerusahaanError == false
					&& vm.jenisPerusahaanError == false && vm.NamaSingkatanError == false && vm.alamatPerusahaanError == false
					&& vm.provinsiPerusahaanError == false && vm.kotaPerusahaanError == false && vm.TeleponPerusahaanError == false
					&& vm.EmailPerusahaanError == false && vm.NamaContactPersonError == false && vm.NoHPContactPersonError == false
					&& vm.EmailContactPersonError == false && vm.NoKTPContactPersonError == false && vm.TitleError == false && vm.bussinessAreaError == false && vm.namaNPWPError == false &&
                    vm.alamatNPWPError == false && vm.kotaNPWPError == false && vm.noAktaPendirianError == false && vm.NoKKContactPersonError == false){
				if($scope.validasiEmail(vm.userId)) {
					if(vm.pilihPKP.name == '1') {
						if(vm.nomorPKPError == false && vm.NPWPPerusahaanError == false)
							saveDataVendorProfile(vm);
					} else {
						vm.nomorPKP = '';
						saveDataVendorProfile(vm);
					}
				}
			}else{
				RequestService.modalInformation('promise.procurement.RegistrasiVendor.DataPerusahaan.informasi.error_data', 'danger');
			}
        }
        
        

        var saveDataVendorProfile = function(vm) {
        	
        	var tanggalBerdiri = $filter('date')(vm.tanggalBerdiri, 'yyyy-MM-dd');
        	
        
			
        	formDataVendorDraft = {
        			id				: vm.vendorId,
        			nama 			: vm.NamaPerusahaan,
        			alamat 			: vm.alamatPerusahaan,
        			nomorTelpon 	: vm.TeleponPerusahaan,
        			email 			: vm.EmailPerusahaan,
        			npwp 			: vm.NPWPPerusahaan,
        			penanggungJawab : vm.NamaPerusahaan,
        			deskripsi 		: vm.deskripsi,
        			kota			: vm.kotaPerusahaan.lokasi_nama,
        			afco			: {id:vm.unitTerdaftar.id},
        			provinsi        : vm.provinsiPerusahaan.lokasi_nama,
        			isPKS			: vm.isPKS
        	}
        	
        	formDataVendorProfileDraft.id = vm.vendorProfileId;
        	formDataVendorProfileDraft.vendor = {id:vm.vendorId};
        	formDataVendorProfileDraft.jenisPajakPerusahaan = vm.pilihPKP.name; 
        	formDataVendorProfileDraft.nomorPKP = vm.nomorPKP;
        	formDataVendorProfileDraft.title = vm.titleVendor.title;
        	formDataVendorProfileDraft.kualifikasiVendor = {id:vm.kualifikasiVendor.id};  
        	formDataVendorProfileDraft.unitTerdaftar = vm.unitTerdaftar.nama; 
        	formDataVendorProfileDraft.bussinessArea = vm.businessArea.id;
        	formDataVendorProfileDraft.tipePerusahaan = vm.jenisPerusahaan.nama; 
        	formDataVendorProfileDraft.namaPerusahaan = vm.NamaPerusahaan; 
        	formDataVendorProfileDraft.npwpPerusahaan = vm.NPWPPerusahaan;
        	formDataVendorProfileDraft.namaNPWP = vm.namaNPWP;
        	formDataVendorProfileDraft.alamatNPWP = vm.alamatNPWP;
        	formDataVendorProfileDraft.provinsiNPWP = vm.provinsiNPWP.lokasi_nama;
        	formDataVendorProfileDraft.kotaNPWP = vm.kotaNPWP.lokasi_nama;
        	formDataVendorProfileDraft.namaSingkatan = vm.NamaSingkatan; 
        	formDataVendorProfileDraft.alamat = vm.alamatPerusahaan; 
        	formDataVendorProfileDraft.kota = vm.kotaPerusahaan.lokasi_nama; 
        	formDataVendorProfileDraft.kodePos = vm.kodeposPerusahaan;
        	formDataVendorProfileDraft.poBox = vm.poboxPerusahaan; 
        	formDataVendorProfileDraft.provinsi = vm.provinsiPerusahaan.lokasi_nama; 
        	formDataVendorProfileDraft.telephone = vm.TeleponPerusahaan; 
        	formDataVendorProfileDraft.faksimile = vm.NoFaxPerusahaan; 
        	formDataVendorProfileDraft.email = vm.EmailPerusahaan; 
        	formDataVendorProfileDraft.website = vm.WebsitePerusahaan; 
        	formDataVendorProfileDraft.namaContactPerson = vm.NamaContactPerson; 
        	formDataVendorProfileDraft.hpContactPerson = vm.NoHPContactPerson; 
        	formDataVendorProfileDraft.emailContactPerson = vm.EmailContactPerson; 
        	if (vm.NoKTPContactPerson!== undefined && vm.NoKTPContactPerson !== '' && vm.NoKTPContactPerson !== null){
            	formDataVendorProfileDraft.ktpContactPerson = vm.NoKTPContactPerson;
        	}
        	if (vm.NoKKContactPerson !== undefined && vm.NoKKContactPerson !== '' && vm.NoKKContactPerson !== null){
        		formDataVendorProfileDraft.noKK = vm.NoKKContactPerson;
            }
        	if (vm.jumlahKaryawan !== '' || vm.jumlahKaryawan !==undefined || vm.jumlahKaryawan !==""){
        		formDataVendorProfileDraft.jumlahKaryawan = vm.jumlahKaryawan;
        	}else{
        		formDataVendorProfileDraft.jumlahKaryawan = '';
        	}
        	formDataVendorProfileDraft.noAktaPendirian = vm.noAktaPendirian;
        	formDataVendorProfileDraft.tanggalBerdiri = new Date(tanggalBerdiri);
        	
        	if(vm.status==1){
        		formDataVendorProfileDraft.created=vm.vendorProfileCreated;
        		formDataVendorDraft.created= vm.vendorCreated;
        	}
        	
        	var data={
        			vendorDraft:formDataVendorDraft,
        			vendorPICDraftList:vm.penanggungJawabList,
        			vendorProfileBeanDraft:formDataVendorProfileDraft,
        			status:vm.status
        	}
        		
        	console.log(JSON.stringify(data));
        	RequestService.doPOSTJSON('/procurement/vendor/vendorServices/editDataPerusahaan', data)
			.then(function success(data) {
				RequestService.modalInformation('template.informasi.simpan_sukses', 'success');
				$scope.getVendorData();
			},
			function error(response) {
				RequestService.modalInformation('template.informasi.gagal', 'danger');
			});
        }
        
        $scope.approval = function() {
        	RequestService.modalConfirmation("Apakah Anda yakin untuk mengirimkan perubahan data?").then(function (result) {
				
				
				vm.loading = true;
				$scope.saveApproval();
				
				
				
			});    	
            }
        
        $scope.saveApproval = function() {
			var url = '/procurement/vendor/vendorServices/sendApproval';
			
			var data={
					loginId:vm.loginId
			}
			
			RequestService.doPOST(url)
				.then(function success(data) {
					RequestService.modalInformation("template.informasi.kirim_approval",'success');
					$scope.getVendorData();
					vm.loading = false;
				}, function error(response) {
					$log.info("proses gagal");
					vm.loading = false;
					RequestService.modalInformation("template.informasi.gagal",'danger');
				});

		}
      
        
        $scope.backToDashboard = function() {
        	$state.go('appvendor.promise.dashboard');
        }
	}
	
	RegistrasiDataPerusahaanController.$inject = ['RequestService','$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location', '$timeout', '$window'];

})();