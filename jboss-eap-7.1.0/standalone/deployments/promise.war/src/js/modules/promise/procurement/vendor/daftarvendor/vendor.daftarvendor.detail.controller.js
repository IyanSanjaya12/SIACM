/**=========================================================
 * Module: vendor.approval.detail.js
 * Modul/Tahapan ID: 8
 * Author: Reinhard
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('DaftarVendorDetailController', DaftarVendorDetailController);

    function DaftarVendorDetailController($http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, toaster, $location, $stateParams, RequestService) {
    	
    	 var vm = this;
    	 
    	 vm.formats = [ 'dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd' ];
     	 vm.format = vm.formats[4];
     	 vm.vendorId =[]; 	
    	 
        if (typeof $rootScope.detilVendor !== 'undefined') {
            vm.detailVendor = $rootScope.detilVendor;
            vm.vendorId = $scope.detilVendor.id;
        } else {
            console.log('INFO Error');
        }

       
        vm.loading = true;

        $scope.getVendorData = function(){
        	 RequestService.doGET('/procurement/vendor/vendorServices/getVendorData/' + vm.vendorId)
 			.then(function success(data) {
 				if(data != undefined){
 					$scope.fillForm(data);
 					vm.SKBHistoryList = data.dataHistorySKBList;
 					vm.loading = false;
 				}
 			},
 			function error(response) {
 				RequestService.informError("Terjadi Kesalahan Pada System");
 		  })
       };
       $scope.getVendorData();
       
       //addPengadaanGagal
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
        
        vm.pilihPKP = {
                name: '1'
            };
        
        /*$scope.pilihPKP = function () {
            if (vm.pilihPKP.name == '2') {
                vm.disabled = true;
                vm.labelMandatori = '';
            } else {
                vm.disabled = false;
                vm.labelMandatori = '*';
            }
        }*/

        /*vm.jenisPerusahaanList = [{
                id: 1,
                nama: "PT"
            },
            {
                id: 2,
                nama: "CV"
            },
            {
                id: 3,
                nama: "PO"
            }
                                     ];*/

        /*$scope.showJabatan = function (pic) {
            var selected = [];
            if (pic.jabatan) {
                selected = $filter('filter')(scope.jabatanPenanggungJawabList, {
                    id: pic.jabatan.id
                });
            }
            return selected.length ? selected[0].nama : 'Not set';
        };*/
        
    }
    DaftarVendorDetailController.$inject = ['$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'toaster', '$location', '$stateParams', 'RequestService'];


})();