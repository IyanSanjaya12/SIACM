(function() {
	'use strict';
	angular
		.module('naut')
		.controller('ApprovalEditViewController', ApprovalEditViewController);

	function ApprovalEditViewController($state, $scope, $rootScope, $stateParams,
			RequestService, ngTableParams, $http, ModalService, toaster, $timeout, $location,$modal) {

		var vm = this;
		$scope.loading = false;
		
		// approval
		vm.approvalProcess = $rootScope.approvalProcess;
		
		vm.note = '';
		vm.vendor=vm.approvalProcess.approvalProcessType.valueId;	
		
		vm.vendorId = [];
		vm.dokumenRegistrasiVendorList = [];
		
		vm.showapproval = false;
		$scope.isUserApproval = false;
        RequestService.doGET('/procurement/approvalProcessServices/findVendor/' + vm.approvalProcess.approvalProcessType.valueId).then(function success(data) {
            if (typeof vm.approvalProcess.approvalLevel !== 'undefined' && vm.approvalProcess.approvalLevel != null) {
                $scope.getApprovalProcess(vm.approvalProcess.approvalProcessType.id);
                $scope.getApprovalProcessAndStatus(vm.approvalProcess.approvalProcessType.id);
            } else {
                $scope.isUserApproval = true;
            }
        }), function error(data) {
			$log.info("proses gagal");
			RequestService.informError("Terjadi Kesalahan Pada System"); {
            $scope.loading = false;
			}
        };

		if (typeof $rootScope.approvalProcess !== 'undefined') {
            vm.detailVendor = $rootScope.approvalProcess;
            vm.vendorId = $rootScope.approvalProcess.approvalProcessType.valueId;
           
        } else {
            
        }
		
		$scope.openHistorySKB = function () {
	           $scope.loading = false;
	           $rootScope.anggaranSelected = {};
	           var modalInstance = $modal.open({
	               templateUrl: 'SKBHistoryView.html',
	               controller: ModalViewHistorySKB,
	               resolve:{
	            	   SKBHistoryList : function (){
	            	   return  vm.SKBHistoryList;
	               }},
	               size: 'lg'
	           });

	       };
	       var ModalViewHistorySKB = function ($http, $modalInstance,$scope, $rootScope, $resource, $location,SKBHistoryList) {
	    	   $scope.SKBHistoryList = SKBHistoryList;
	    	   $scope.downloadLink = $rootScope.backendAddress +'/file/view/';
	    	   $scope.closeModal = function(){
	    	       $modalInstance.close();
	    	    }
	       }
			
		$scope.getVendorList = function () {
	            $scope.loading = true;
	            
	            vm.showSegmentasiVendorDraft=false;
	            vm.showPeralatanVendorDraft=false;
	            vm.showKeuanganVendorDraft=false;
	            vm.showPengalamanPelangganVendorDraft=false;
	            vm.showPengalamanMitraVendorDraft=false;
	            vm.showPengalamanInProgressVendorDraft=false;
	            vm.showVendorPICDraft=false;
	            vm.showVendorPIC=false;
	            
	            vm.showDataDokRegFormDraft=true;
	            vm.showDataDokAkteDraft=true;
	            vm.showDataDokSalinanDraft=true;
	            vm.showDataDokSiupDraft=true;
	            vm.showdataDokPKSDraft=true;
	            vm.showDataDokSPRDraft=true;
	            vm.showDataDokSPBDraft=true;
	            vm.showDataDokSKBDraft=true;
	            vm.showDataDokBuktiFisikDraft=true;
	            vm.showdataDokQualityDraft=true;
	            vm.showDataDokTeknikDraft=true;
	            vm.showVendorSKDDraft=true;
	            
	            RequestService.doGET('/procurement/approvalProcessServices/getVendorData/' +vm.vendor)
	        	  .then(function success(data) {
	        		  if (data != undefined) {
	        			vm.dokumenVendorEdited = !data.isDraftDuplicate;
	  					$scope.fillForm(data);	  					
	  					vm.SKBHistoryList = data.skbhistoryList;
	  					$scope.loading = false;
	  				}
	  			}, function error(data) {
	  				RequestService.informError("Terjadi Kesalahan Pada System");
	  			})
	    };
	    $scope.getVendorList();
	    

		$scope.fillForm = function(data) {
			vm.hide=false;
			// get Data Vendor
			
			/* DATA PERUSAHAAN */
			if(data.vendorProfileDraft != null){
				vm.vendorProfilDraft=data.vendorProfileDraft;
				vm.vendorProfileEdited = true;
			}else{
				  vm.vendorProfilDraft=data.vendorProfile;
			}
            
            
            vm.vendorPICDraftList=data.vendorPICDraft;
                if (vm.vendorPICDraftList.length >0){            
    				vm.showVendorPICDraft=true;
    			}
                
      
            vm.vendorProfil=data.vendorProfile;
                      
            vm.vendorPICList=data.vendorPIC;
            	if (vm.vendorPICList.length >0){
            	    vm.showVendorPIC=true;
            	}
            
            
            /* DATA DOKUMEN */
                 
            vm.dataDokRegFormDraft=data.dataDokRegFormDraft;
            vm.dataDokAkteDraft=data.dataDokAkteDraft;
            vm.dataDokSalinanDraft=data.dataDokSalinanDraft;
            vm.dataDokSiupDraft=data.dataDokSiupDraft;
            vm.dataDokPKSDraft=data.dataDokPKSDraft;
            vm.dataDokSPRDraft=data.dataDokSPRDraft;
            vm.dataDokSPBDraft=data.dataDokSPBDraft;
            vm.dataDokSKBDraft=data.dataDokSKBDraft;
            vm.dataDokBuktiFisikDraft=data.dataDokBuktiFisikDraft;
            vm.dataDokQualityDraft=data.dataDokQualityDraft;
            vm.dataDokTeknikDraft=data.dataDokTeknikDraft;
            vm.vendorSKDDraft=data.vendorSkdDraft;    
            
            
           
            vm.dataDokRegForm=data.dataDokRegForm;
        	vm.dataDokAkte=data.dataDokAkte;
        	vm.dataDokSalinan=data.dataDokSalinan;
        	vm.dataDokSiup=data.dataDokSiup;
        	vm.dataDokPKS=data.dataDokPKS;
        	vm.dataDokSPR=data.dataDokSPR;
        	vm.dataDokSPB=data.dataDokSPB;
        	vm.dataDokSKB=data.dataDokSKB;
        	vm.dataDokBuktiFisik=data.dataDokBuktiFisik;
        	vm.dataDokQuality=data.dataDokQuality;
        	vm.dataDokTeknik=data.dataDokTeknik;       	
        	vm.vendorSKD=data.vendorSkd;
        	 
	        /* validasi row dokumen, kalau gak ada data di hide*/
        	
        	if (vm.dataDokRegFormDraft != null){
				vm.dokumenRegistrasiVendorList.push(vm.dataDokRegFormDraft);
			} else{
				vm.showDataDokRegFormDraft=false;
			}
        	if (vm.dataDokSKBDraft != null){
				vm.dokumenRegistrasiVendorList.push(vm.dataDokSKBDraft);
			} else{
				vm.showDataDokSKBDraft=false;
			}
			if(vm.dataDokAkteDraft != null){
				vm.dokumenRegistrasiVendorList.push(vm.dataDokAkteDraft);
			}else
				{
				 vm.showDataDokAkteDraft=false;
				}
            if(vm.dataDokSalinanDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokSalinanDraft);
            }else{
            	vm.showDataDokSalinanDraft=false;
            }
            if(vm.dataDokSiupDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokSiupDraft);
            }else{
            	vm.showDataDokSiupDraft=false;
            }
            if(vm.dataDokPKSDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokPKSDraft);
            }else{
            	vm.showdataDokPKSDraft=false;
            }
            if(vm.dataDokSPRDraft != null){
				vm.dokumenRegistrasiVendorList.push(vm.dataDokSPRDraft);
			}else{
				 vm.showDataDokSPRDraft=false;
			}
            if(vm.dataDokSPBDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokSPBDraft);
            }else{
            	vm.showDataDokSPBDraft=false;
            }
            if(vm.dataDokBuktiFisikDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokBuktiFisikDraft);
            }else{
            	vm.showDataDokBuktiFisikDraft=false;	
            }
            if(vm.dataDokQualityDraft != null){
            	vm.dokumenRegistrasiVendorList.push(vm.dataDokQualityDraft);
            }else{
            	vm.showdataDokQualityDraft=false;
            }
            if(vm.dataDokTeknikDraft != null){
            	 vm.dokumenRegistrasiVendorList.push(vm.dataDokTeknikDraft);
            }else{
            	 vm.showDataDokTeknikDraft=false;
            }
        	
            if (vm.vendorSKDDraft==null){
            	vm.showVendorSKDDraft=false;
            }
        	
        	/* TAB LOGIN */
  			vm.namaPengguna = data.user.namaPengguna;
  			vm.userId = data.user.username;
            
  			
            // LIST BANK Draft
			vm.bankVendorDraftList = data.bankVendorDraftList;
			vm.showBankVendorDraft=false;
			if (vm.bankVendorDraftList.length >0){
				vm.showBankVendorDraft=true;
				vm.bankVendorEdited=true;
			}
			$scope.dataBankTable1 = new ngTableParams({
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.bankVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.bankVendorDraftList.slice((params.page() - 1)
							* params.count(), params.page() * params.count()));
				}
			});
			
            // LIST BANK
			vm.bankVendorList = data.bankVendorList;
			$scope.dataBankTable2 = new ngTableParams({
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.bankVendorList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.bankVendorList.slice((params.page() - 1)
							* params.count(), params.page() * params.count()));
				}
			});
			
			// SEGMENTASI
			
			vm.segmentasiVendorDraftList=data.segmentasiVendorDraft; 
			if (vm.segmentasiVendorDraftList.length >0){
				vm.showSegmentasiVendorDraft=true;
				vm.segmentasiVendorEdited=true;
			}
			
			
			$scope.tableSegmentasiVendor1 = new ngTableParams({
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.segmentasiVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.segmentasiVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			// SEGMENTASI main
			vm.segmentasiVendorList=data.segmentasiVendorList; 
			$scope.tableSegmentasiVendor2 = new ngTableParams({
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.segmentasiVendorList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.segmentasiVendorList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PERALATAN DRAFT
			vm.peralatanVendorDraftList=data.peralatanVendorDraft;
			if (vm.peralatanVendorDraftList.length >0){
				vm.peralatanVendorEdited=true;
				vm.showPeralatanVendorDraft=true;
			}
			vm.tablePeralatanVendor1 = new ngTableParams({	        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.peralatanVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.peralatanVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PERALATAN MAIN
			vm.peralatanVendorMainList=data.peralatanVendorList;
			vm.tablePeralatanVendor2 = new ngTableParams({	        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.peralatanVendorMainList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.peralatanVendorMainList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA KEUANGAN DRAFT
			vm.keuanganVendorDraftList=data.keuanganVendorDraft;
			if (vm.keuanganVendorDraftList.length >0){
				vm.showKeuanganVendorDraft=true;
				vm.keuanganVendorEdited = true;
			}
			vm.tableKeuanganVendor1 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.keuanganVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.keuanganVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA KEUANGAN MAIN
			vm.keuanganVendorMainList=data.keuanganVendorList;
			vm.tableKeuanganVendor2 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.keuanganVendorMainList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.keuanganVendorMainList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PENGALAMAN PELANGGAN MAIN
			vm.pengalamanPelangganVendorList=data.pengalamanPelangganVendorList;
			
			vm.tablePengalamanPekerjaan1 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanPelangganVendorList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanPelangganVendorList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PENGALAMAN PELANGGAN DRAFT
			vm.pengalamanPelangganVendorDraftList=data.pengalamanPelangganVendorDraftList;
			if (vm.pengalamanPelangganVendorDraftList.length >0){
				vm.showPengalamanPelangganVendorDraft=true;
				vm.pengalamanVendorEdited=true;
			}
			vm.tablePengalamanPekerjaan2 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanPelangganVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanPelangganVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA MITRA PELANGGAN DRAFT
			vm.pengalamanMitraVendorDraftList=data.pengalamanMitraVendorDraftList;
			if (vm.pengalamanMitraVendorDraftList.length >0){
				vm.showPengalamanMitraVendorDraft=true;
				vm.pengalamanVendorEdited=true;
			}
			vm.tableDataMitra1 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanMitraVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanMitraVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			vm.pengalamanMitraVendorList=data.pengalamanMitraVendorList;
			vm.tableDataMitra2 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanMitraVendorList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanMitraVendorList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PENGALAMAN IN PROGRESS DRAFT
			vm.pengalamanInProgressVendorDraftList=data.pengalamanInProgressVendorDraftList;
			if (vm.pengalamanInProgressVendorDraftList.length>0){
				vm.showPengalamanInProgressVendorDraft=true;
				vm.pengalamanVendorEdited=true;
			}
			vm.tableWorkingProgress1 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanInProgressVendorDraftList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanInProgressVendorDraftList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			// TAB DATA PENGALAMAN IN PROGRESS MAIN
			vm.pengalamanInProgressVendorList=data.pengalamanInProgressVendorList;
			vm.tableWorkingProgress2 = new ngTableParams({        		
				page : 1, // show first page
				count : 5
			// count per page
			}, {
				total : vm.pengalamanInProgressVendorList.length, // length of data4
				getData : function($defer, params) {
					$defer.resolve(vm.pengalamanInProgressVendorList.slice(
							(params.page() - 1) * params.count(), params.page()
									* params.count()));
				}
			});
			
			$scope.checkLastEditedTab();
		}

		
		$scope.getApprovalProcess = function (approvalProcessType) {
            $scope.loadingApproval = true;
            vm.levelList = [];
            RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType)
            .then (function success(data) {
                vm.levelList = data;
                $scope.loading = false;
            }), function error(data) {
                $scope.loading = false;
            };
        };
             
      //Approval log
		$scope.getApprovalProcessAndStatus = function(approvalProcessType) {
			$scope.loadingApproval = true;
			RequestService.doGET('/procurement/approvalProcessServices/findByApprovalProcessTypeAndStatus/' + approvalProcessType)
				.then(function success(data) {
				vm.statusList = data;
				$scope.loading = false;
			}), function error(data) {
				$scope.loading = false;
			};
		};
		
		vm.pilihPKP = {
			name : '1'
		};
		
		$scope.btnSimpan = function (statusId) {
        	vm.errorNoteMessage = "";
        	var checkNote = vm.note;
			var isNote = true;
			if(vm.note === null || vm.note === '') {
				isNote = false;
				vm.errorNoteMessage = "Note tidak boleh kosong";
			} else if ( checkNote.length >= 255) { 
				isNote = false;
				vm.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
			} else {
                ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?')
                    .then(function (result) {
                        ModalService.showModalInformationBlock();
                        vm.errorNoteMessage = '';
                        $scope.simpan(statusId);

                    });
                vm.errorNoteMessage = '';
            }
        }
		$scope.simpan = function (statusId) {
                 
            var formPost = {
                id: vm.approvalProcess.approvalProcessType.valueId,
                note: vm.note,
                status: statusId,
                approvalProcessId: vm.approvalProcess.id,
                dokumenRegistrasiVendorDraftList:vm.dokumenRegistrasiVendorList,
                vendorSKDDraft:vm.vendorSkdDraft
           };
                      
            
            RequestService.doPOSTJSON('/procurement/approvalProcessServices/updateEditApproval', formPost)
                .then(function successCallback(data) {
                	toaster.pop('success', 'Sukses', 'Approval vendor berhasil disimpan');
                    $timeout (function() {		
                    	$location.path('app/promise/procurement/approval');
        			}, 5000);
                    
                    
                    ModalService.closeModalInformation();
                }, function errorCallback(response) {
                    ModalService.closeModalInformation();
                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                });

        }
		$scope.checkLastEditedTab = function(){
			
			if(vm.pengalamanVendorEdited){
				vm.lastTab="pengalamanVendorEdited";
			}else if(vm.keuanganVendorEdited){
				vm.lastTab="keuanganVendorEdited";
			}else if(vm.peralatanVendorEdited){
				vm.lastTab="peralatanVendorEdited";
			}else if(vm.dokumenVendorEdited){
				vm.lastTab="dokumenVendorEdited";
			}else if(vm.segmentasiVendorEdited){
				vm.lastTab="segmentasiVendorEdited";
			}else if(vm.bankVendorEdited){
				vm.lastTab="bankVendorEdited";
			}else if(vm.vendorProfileEdited){
				vm.lastTab="vendorProfileEdited";
			}
		}
		
		$scope.isShowApproval = function(valid){
			if(vm.lastTab == valid){
				vm.showapproval = true;	
			}else{
				vm.showapproval = false;
			}
			
		}
		
		$scope.gotoIndex = function () {
            $state.go('app.promise.procurement-approval');
        }
		        
	}
	ApprovalEditViewController.$inject = [ '$state', '$scope', '$rootScope', '$stateParams', 'RequestService', 'ngTableParams', '$http', 'ModalService', 'toaster', '$timeout', '$location','$modal' ];
})();