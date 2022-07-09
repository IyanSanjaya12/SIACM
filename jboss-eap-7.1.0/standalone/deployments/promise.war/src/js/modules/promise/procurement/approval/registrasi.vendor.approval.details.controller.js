/**=========================================================
 * Module: vendor.approval.detail.js
 * Modul/Tahapan ID: 8
 * Author: Reinhard
 =========================================================*/
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiVendorApprovalDetailsController', RegistrasiVendorApprovalDetailsController);

	function RegistrasiVendorApprovalDetailsController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $stateParams, ModalService) {
		
		$scope.vendorId = $stateParams.vendorId;
		$scope.dataVendor = $rootScope.detilvendor;
    	
		var form = this;
		form.loading=true;
		
		form.blacklist = false;
		
		$scope.loadingSaving=false;
		
		form.penanggungJawabList = [];
		form.dataBankList = [];
	    form.dataSegmentasiList = [];
		form.beritaAcara = {};
		form.beritaAcara.vendorStatus = 1; /*default*/
		$scope.negaraList = [];
		form.showapproval = false;
		form.dokumenRegistrasiVendorList = [];
        
        form.approvalProcess = $rootScope.approvalProcess;
        form.note = '';
		$scope.downloadFile = $rootScope.viewUploadBackendAddress+'/';
		
		/*Datepicker*/
	    form.toggleMin = function () {
	    	form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        form.format = form.formats[4];

        form.tanggalBeritaAcaraOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.tanggalBeritaAcaraOpened = true;
		};

		form.viewFile = function(namaFile) {
			window.open($rootScope.viewUploadBackendAddress+"/"+namaFile, '_blank');
		}
		
		if($scope.dataVendor.statusBlacklist == 1){
			/** ini blacklist **/
			form.blacklist = true;
			form.showapproval = true;
			
			form.blacklistVendorDtl = {};
			
			RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendor/' + $scope.vendorId)
			.then(function successCallback(data) {
				form.blacklistVendorDtl = data;
			}, function errorCallback(response) {				 
				
			});			
		}else if($scope.dataVendor.statusBlacklist == 2){
			/** ini blacklist **/
			form.blacklist = true;
			form.showapproval = true;
			form.unblacklist = true;
			
			form.blacklistVendorDtl = {};
			
			RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendorMaxVendor/' + $scope.vendorId)
			.then(function successCallback(data) {
				form.blacklistVendorDtl = data;
				
				RequestService.doGET('/procurement/vendormanagement/UnblacklistVendorServices/getUnblacklistVendorMaxAndBlacklistIdDetail/' + $scope.vendorId + '/' + form.blacklistVendorDtl.blacklistVendor.blacklistVendorId)
				.then(function successCallback(data) {
					form.unblacklistVendorDtl = data;
					
					console.info(form.unblacklistVendorDtl)
				}, function errorCallback(response) {				 
					
				});			
			}, function errorCallback(response) {				 
				
			});			
		}

		$scope.saveData = function () {
			
			if (isFormValid()) {
				$scope.isEditable = false;
				updateVendorStatus();
			} else {
				toaster.pop('warning', 'Kesalahan', 'Silahkan periksa kembali inputan anda!');
			}
			return false;
		}
        		
		var isFormValid = function () {
			var isValid = true;
			if (form.beritaAcara.nomor == null || (typeof form.beritaAcara.nomor == 'undefined')) {
				isValid = false;
				document.getElementsByName('beritaacara_nomor')[0].focus();
			} else if (form.beritaAcara.tanggal == null || (typeof form.beritaAcara.tanggal == 'undefined')) {
				isValid = false;
				document.getElementsByName('beritaacara_tanggal')[0].focus();
			}
			return isValid;
		}
		
        
         // Approval
     
        $scope.getApprovalProcess = function(approvalProcessType) {
			$scope.loadingApproval = true;
			form.levelList = [];
			$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function(data) {
				form.levelList = data;
				consolelog(">> cek approval :"+JSON.stringify(form.levelList));
				$scope.loading = false;
			}).error(function(data) {
				$scope.loading = false;
			});
		};
        
        
      $scope.btnSimpan = function(statusId) {

			if (statusId == 3) { // when is approve, chek is COA available

				var isCostAvailable = true;
				angular.forEach(form.purchaseRequestItemList, function(value, index) {
					if (!value.costAvailable) {
						isCostAvailable = false;
					}
				});

				if (!isCostAvailable) {
					ModalService.showModalConfirmation('Cost Allocation tidak mencukupi, Apakah Anda Yakin ingin menyetujuinya ?').then(function (result) {
						ModalService.showModalInformationBlock(); 
						$scope.simpan(statusId);
					});
				} else {
					ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
						ModalService.showModalInformationBlock(); 
						$scope.simpan(statusId);
					});
				}
			} else {
				ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?').then(function (result) {
					ModalService.showModalInformationBlock(); 
					$scope.simpan(statusId);
				});
			}
		}
		
  
      		
		$scope.simpan = function(statusId){
           
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findPurchaseRequest/' + form.approvalProcess.approvalProcessType.valueId).success(function(data) {
			form.purchaseRequest = data;
			if (typeof form.approvalProcess.approvalLevel !== 'undefined' && form.approvalProcess.approvalLevel != null) {
				$scope.getApprovalProcess(form.approvalProcess.approvalProcessType.id);
			} else {
				$scope.isUserApproval = true;
			}

			$scope.getPRItem(form.approvalProcess.approvalProcessType.valueId);
		
            
			var post = {
				id: form.approvalProcess.approvalProcessType.valueId,
				status: statusId,
				note: form.note,
				approvalProcessId: form.approvalProcess.id
			};
						
			RequestService.doPOST('/procurement/purchaseRequestServices/updateStatusApproval', post)
			.then(function successCallback(data) { 
				$location.path('app/promise/procurement/approval');
				ModalService.closeModalInformation();
			}, function errorCallback(response) {			
				ModalService.closeModalInformation();	 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
			}); 
           
            }).error(function(data) {
			$scope.loading = false;
		});
                
		}
        
        
        $scope.getApprovalProcess = function(approvalProcessType) {
			$scope.loadingApproval = true;
			form.levelList = [];
			$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function(data) {
				form.levelList = data;
				// consolelog(">> cek approval
				// :"+JSON.stringify(form.levelList));
				$scope.loading = false;
			}).error(function(data) {
				$scope.loading = false;
			});
		};
        
        
		function updateVendorStatus() {		
			
			var data = {};
			data.vendor = {
					id: $scope.vendorId,
					status: form.beritaAcara.vendorStatus
			};
			
			data.dokumenRegistrasiVendorList = form.dokumenRegistrasiVendorList;
			
			data.vendorApproval = {
					tanggal : form.beritaAcara.tanggal,
					nomor :form.beritaAcara.nomor,
					keterangan :form.beritaAcara.keterangan,
			};
			
			// Upload-an Logo Ama Header Images
			if (uploadFileApproval !== undefined && uploadFileApproval.queue.length > 0) {
				angular.forEach(uploadFileApproval.queue, function(item) {
					if(item.isUploaded) {
						data.vendorApproval.realFileName = item.realFileName;
						data.vendorApproval.realFileSize = item.file.size;
					}
				});
			}
			
			
			RequestService.doPOSTJSON('/procurement/vendor/vendorServices/updateVendorStatusDTO', data)
			.then(function successCallback(data) {
				$scope.showSuccess();
 			}, function errorCallback(response) {				 
//				ModalService.closeModalInformation();
				ModalService.showModalInformation('Terjadi kesalahan pada system!') 
			});  
		}
		
		function saveBeritaAcara() {
			/*convert tanggal berita acara in to text formatted*/
			form.beritaAcara.tanggal = $filter('date')(form.beritaAcara.tanggal, 'dd-MM-yyyy');
			form.beritaAcara.vendor =  $scope.vendorId;
		
			$http({
				method: 'POST',
				url: $rootScope.backendAddress + '/procurement/vendor/VendorApprovalServices/insert',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				transformRequest: function (obj) {
					var str = [];
					for (var p in obj) {
						var val = obj[p];

						/*extract id value when val is object and !null*/
						if ((typeof val) == 'object') {
							if (val != null)
								val = val['id'];
						}

						str.push(encodeURIComponent(p) + "=" + encodeURIComponent(val));
					}
					return str.join("&");
				},
				data: form.beritaAcara
			}).success(function (data, status, headers, config) {
				$scope.showSuccess();
			})


		}
		


	
		$scope.showSuccess = function () {
			$scope.loadingSaving = false;
			toaster.pop('success', 'Sukses', 'Approval vendor berhasil disimpan.');
			/*$scope.gotoIndex();*/
		}
		
		
		$http.get($rootScope.backendAddress + '/procurement/master/bidang-usaha/get-list')
	        .success(function (data, status, headers, config) {
	        	$scope.bidangUsahaList = data;
	      
	        	$http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
				.success(function (data, status, headers, config) {
				$scope.mataUangList = data;
      
        
				$http.get($rootScope.backendAddress + '/procurement/master/jabatan/get-list')
				.success(function (data, status, headers, config) {
				$scope.jabatanPenanggungJawabList = data;
	      
		
				$http.get($rootScope.backendAddress + '/procurement/master/kualifikasi-vendor/get-list')
				.success(function (data, status, headers, config) {
				$scope.kualifikasiVendorList = data;
       
				// DATA WILAYAH PROPINSI
				$http.get($rootScope.backendAddress + '/procurement/master/WilayahServices/getPropinsiList')
				.success(function (data, status, headers, config) {
				$scope.provinsiPerusahaanList = data;
       
				$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendor/'+ $scope.vendorId)
				.success(function (data, status, headers, config) {
				form.vendorBean = data;
	        	
				// Get User
				$http.get($rootScope.backendAddress + '/procurement/user/get-user/'+ form.vendorBean.user)
				.success(function (data, status, headers, config) {
				form.vendorUserBean = data;
	            	
				// Get User
				$http.get($rootScope.backendAddress + '/procurement/vendor/VendorProfileServices/getVendorProfileByVendorId/'+ $scope.vendorId)
				.success(function (data, status, headers, config) {
				form.vendorProfileBean = data[0];
		            	
				$http.get($rootScope.backendAddress + '/procurement/vendor/SegmentasiVendorServices/getSegmentasiVendorByVendorId/'+ $scope.vendorId)
				.success(function (data, status, headers, config) {
				form.penanggungJawabList = data;
			             	
			            	
				$http.get($rootScope.backendAddress + '/procurement/vendor/BankVendorServices/getBankVendorByVendorId/'+ $scope.vendorId)
				.success(function (data, status, headers, config) {
				form.dataBankList = data;
				            	
				         	
				$http.get($rootScope.backendAddress + '/procurement/vendor/SegmentasiVendorServices/getSegmentasiVendorByVendorId/'+ $scope.vendorId)
				.success(function (data, status, headers, config) {
				form.dataSegmentasiList = data;
					         	
                    $http.get($rootScope.backendAddress + '/procurement/vendor/VendorPICServices/getVendorPICByVendorId/'+ $scope.vendorId)
					.success(function (data, status, headers, config) {
					form.penanggungJawabList = data;
						           
					$http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Vendor Registrasi Form')
					.success(
					 function (data, status, headers, config) {
                         var dataDokRegFormList = data;
						 if (dataDokRegFormList.length == 0) {
						 $scope.dataDokRegForm = {};
                        form.dokumenRegistrasiVendorList.push($scope.dataDokRegForm);
                        } else {
                        $scope.dataDokRegForm = dataDokRegFormList[0]; 
                        form.dokumenRegistrasiVendorList.push($scope.dataDokRegForm);
                        }


                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya')
                        .success(
                        function (data, status, headers, config) {
                        var dataDokAkteList = data;
                        if (dataDokAkteList.length == 0) {
                        $scope.dataDokAkte = {};
                        form.dokumenRegistrasiVendorList.push($scope.dataDokAkte);
                        } else {
                        $scope.dataDokAkte = dataDokAkteList[0]; 													    	    	                            
                        form.dokumenRegistrasiVendorList.push($scope.dataDokAkte);
                        }


                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Salinan Tanda Daftar Perusahaan (TDP)')
                        .success(
                        function (data, status, headers, config) {
                        var dataDokSalinanList = data;
                        if (dataDokSalinanList.length == 0) {
                        $scope.dataDokSalinan = {};
                        form.dokumenRegistrasiVendorList.push($scope.dataDokSalinan);
                        } else {
                        $scope.dataDokSalinan = dataDokSalinanList[0];
                        form.dokumenRegistrasiVendorList.push($scope.dataDokSalinan);
                        }
                        })


                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Salinan Tanda Daftar Perusahaan (TDP)')
                        .success(
                        function (data, status, headers, config) {



                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Salinan Surat Ijin Usaha (SIUP SIUJK)')
                        .success(
                        function (data, status, headers, config) {
                        var dataDokSiupList = data;
                        if (dataDokSiupList.length == 0) {
                        $scope.dataDokSiup = {};
                        form.dokumenRegistrasiVendorList.push($scope.dataDokSiup);
                        } else {
                        $scope.dataDokSiup = dataDokSiupList[0];
                        form.dokumenRegistrasiVendorList.push($scope.dataDokSiup);
                        }


                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Bukti Fisik Perusahaan')
                        .success(
                        function (data, status, headers, config) {
                        var dataDokBuktiFisikList = data;
                        if (dataDokBuktiFisikList.length == 0) {
                        $scope.dataDokBuktiFisik = {};
                        form.dokumenRegistrasiVendorList.push($scope.dataDokBuktiFisik);
                        } else {
                        $scope.dataDokBuktiFisik = dataDokBuktiFisikList[0];
                        form.dokumenRegistrasiVendorList.push($scope.dataDokBuktiFisik);
                        }


                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Dokumen Quality yang dimiliki')
                        .success(
                        function (data, status, headers, config) {
                        var dataDokQualityList = data;
                        if (dataDokQualityList.length == 0) {
                            $scope.dataDokQuality = {};
                            form.dokumenRegistrasiVendorList.push($scope.dataDokQuality);
                        } else {
                            $scope.dataDokQuality = dataDokQualityList[0];
                            form.dokumenRegistrasiVendorList.push($scope.dataDokQuality);
                        }



                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + $scope.vendorId + '/Dokumen Teknik')
                        .success(
                            function (data, status, headers, config) {
                                var dataDokTeknikList = data;
                                if (dataDokTeknikList.length == 0) {
                                    $scope.dataDokTeknik = {};
                                    form.dokumenRegistrasiVendorList.push($scope.dataDokTeknik);
                                } else {
                                    $scope.dataDokTeknik = dataDokTeknikList[0];
                                    form.dokumenRegistrasiVendorList.push($scope.dataDokTeknik);
                                }


                              $http.get($rootScope.backendAddress + '/procurement/vendor/PeralatanVendorServices/getPeralatanVendorByVendorId/' + $scope.vendorId)
                                .success(function (data, status, headers, config) {
                                    for (var i = 0; i < data.length; i++) {
                                        data[i].index = i + 1;
                                    }
                                    $scope.tablePeralatanVendor = new ngTableParams({
                                        page: 1, // show first page
                                        count: 5 // count per page
                                    }, {
                                        total: data.length, // length of data4
                                        getData: function ($defer, params) {
                                            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                                        }
                                    });


                                        $http.get($rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/getKeuanganVendorByVendorId/' + $scope.vendorId)
								        .success(function (data, status, headers, config) {
								        for (var i = 0; i < data.length; i++) {
								        data[i].index = i + 1;
								    }
				                        $scope.tableKeuanganVendor = new ngTableParams({
								        page: 1, // show first page
								        count: 5 // count per page
								        }, {
								        total: data.length, // length of data4
								        getData: function ($defer, params) {
								        $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
								    }
								    });
															    	    							                    
															    	    							                    
				  $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + $scope.vendorId + '/PELANGGAN')
                      .success(function (data, status, headers, config) {
                        for (var i = 0; i < data.length; i++) {
				        data[i].index = i + 1;
                        }
                        
                      $scope.tablePengalamanPekerjaan = new ngTableParams({
                          page: 1, // show first page
                          count: 5 // count per page
                        }, {
                          
                          total: data.length, // length of data4
                          getData: function ($defer, params) {
				          $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                          }
                      });
															    	    								                    
															    	    								                    
                      $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + $scope.vendorId + '/MITRA')
                          .success(function (data, status, headers, config) {
                          for (var i = 0; i < data.length; i++) {
                              data[i].index = i + 1;
				        }
                          $scope.tableDataMitra = new ngTableParams({
                              page: 1, // show first page
                              count: 5 // count per page
				        }, {
                            total: data.length, // length of data4
                              getData: function ($defer, params) {
                            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                        }
                          });
															    	    									                    
                            $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + $scope.vendorId + '/MITRA')
				            .success(function (data, status, headers, config) {
				            for (var i = 0; i < data.length; i++) {
                                data[i].index = i + 1;
				            }
				            $scope.tableWorkingProgress = new ngTableParams({
                                page: 1, // show first page
				                count: 5 // count per page
				            }, {
				            total: data.length, // length of data4
				            getData: function ($defer, params) {
				            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
				            }
				            });
															    	    										                    
																											    	    										                    /*start*/
				            fillForm();
				            form.loading=false;
				            /*end*/
															    	    										                    
                            })
				            })
				            })
				            })
                            })
                            })	
                            })
                            })
																				    	                    								})
																				                    									})
													    	    	                    											})
														    	                    											});
															            													});
														            													});
													            													});
												            													});
											            													});
										            													});
											        												});
									        													});	
																							});
																						 });
						        													});
					        													});
	        																 
	
	
		 
		function fillForm()
		{
			/*fill form*/
        	
        	/*TAB LOGIN*/
        	form.namaPengguna = form.vendorUserBean.namaPengguna;
        	form.userId = form.vendorUserBean.username;
        	
        	/*TAB DATA PERUSAHAAN*/
        	
            form.pilihPKP.name		    = form.vendorProfileBean.jenisPajakPerusahaan;
            form.nomorPKP      			= form.vendorProfileBean.nomorPKP;
            form.kualifikasiVendor      = form.vendorProfileBean.kualifikasiVendor;
            form.mendaftardiunit 		= form.vendorProfileBean.unitTerdaftar;
            form.NamaPerusahaan         = form.vendorProfileBean.namaPerusahaan;
            form.NamaSingkatan          = form.vendorProfileBean.namaSingkatan;
            form.alamatPerusahaan       = form.vendorProfileBean.alamat;
            form.alamatPerusahaan       = form.vendorProfileBean.alamat;
            form.jenisPerusahaan        = form.vendorProfileBean.tipePerusahaan;
            form.NPWPPerusahaan         = form.vendorProfileBean.npwpPerusahaan;
            
            form.provinsiPerusahaan     = form.vendorProfileBean.provinsi;
            form.kotaPerusahaan         = form.vendorProfileBean.kota;
            form.poboxPerusahaan		= form.vendorProfileBean.poBox;
            form.kodeposPerusahaan      = form.vendorProfileBean.kodePos;
            
            form.TeleponPerusahaan		= form.vendorProfileBean.telephone;
            form.NoFaxPerusahaan        = form.vendorProfileBean.faksimile;
            form.EmailPerusahaan        = form.vendorProfileBean.email;
            form.WebsitePerusahaan      = form.vendorProfileBean.website;
           
            form.NamaContactPerson      = form.vendorProfileBean.namaContactPerson;
            form.NoHPContactPerson      = form.vendorProfileBean.hpContactPerson;
            form.EmailContactPerson     = form.vendorProfileBean.emailContactPerson;
            form.NoKTPContactPerson      = form.vendorProfileBean.ktpContactPerson;
            
            // LIST BANK
            
            $scope.dataBankTable = new ngTableParams({
                page: 1, // show first page
                count: 5 // count per page
            }, {
                total: form.dataBankList.length, // length of data4
                getData: function ($defer, params) {
                    $defer.resolve(form.dataBankList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
            
            // SEGMENTASI
            
           
            $scope.tableSegmentasiVendor = new ngTableParams({
                page: 1, // show first page
                count: 5 // count per page
            }, {
                total: form.dataSegmentasiList.length, // length of data4
                getData: function ($defer, params) {
                    $defer.resolve(form.dataSegmentasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
		}
		
	    form.labelMandatori = '*)';
        
        form.pilihPKP = {
        		name: '1'
        };
        
        $scope.pilihPKP = function() {
        	if(form.pilihPKP.name == '2') {
        		form.disabled = true;
        		form.labelMandatori = '';
        	} else {
        		form.disabled = false;
        		form.labelMandatori = '*)';
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
			$location.path('/app/promise/procurement/vendor/approval');
		};
		
		form.ceklistCheck = function(){
			var check = true;
			form.dokumenRegistrasiVendorList.forEach(function(value, index, array) {
				if(value.fileName != null && value.fileName != ''){
	   				if(value.statusCeklist == null){
						 check = false;
					}
				}
			});
			
			form.showapproval = check;
		}
  
		var uploadFileApproval = $scope.uploadFileApproval = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		
		uploadFileApproval.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return item.type=='application/vnd.openxmlformats-officedocument.wordprocessingml.document' || item.type=='application/msword'
					|| item.type=='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.type=='application/vnd.ms-excel';
			}
		});

		uploadFileApproval.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			
			if(uploadFileApproval.queue.length > 1){
				uploadFileApproval.queue[0].remove();
			}
			
		};
		
		$scope.uploadFile = function() {
			if(uploadFileApproval.queue != undefined && uploadFileApproval.queue.length > 0) {
				angular.forEach(uploadFileApproval.queue, function(item) {
	        		item.upload();
	            });
			} else {
				toaster.pop('error', 'Kesalahan Format', 'Hanya mampu untuk Upload File Word dan Excel');
			}
		}
	  
	}
	RegistrasiVendorApprovalDetailsController.$inject = ['RequestService', '$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster','$location', '$stateParams', 'ModalService'];
	

})();