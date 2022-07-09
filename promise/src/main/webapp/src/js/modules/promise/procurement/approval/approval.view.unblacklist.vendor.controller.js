/**=========================================================
 * Module: vendor.approval.detail.js
 * Modul/Tahapan ID: 8
 * Author: Reinhard
 * unblacklistvendors.approval.details.controller
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('naut')
        .controller('ApprovalViewUnblacklistVendorController', ApprovalViewUnblacklistVendorController);

    function ApprovalViewUnblacklistVendorController(RequestService, $http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $location, $stateParams, ModalService, $timeout) {

        $scope.vendorId = $stateParams.vendorId;
        var form = this;
        form.loading = true;

        form.blacklist = false;
        form.unblacklist = false;

        $scope.loadingSaving = false;

        form.penanggungJawabList = [];
        form.dataBankList = [];
        form.dataSegmentasiList = [];
        form.beritaAcara = {};
        form.beritaAcara.vendorStatus = 1; /*default*/
        $scope.negaraList = [];
        form.showapproval = false;
        form.dokumenRegistrasiVendorList = [];


        // approval
        form.approvalProcess = $rootScope.approvalProcess;
        form.note = '';

        /*if (typeof form.approvalProcess.keterangan !== 'undefined') {
            form.note = form.approvalProcess.keterangan;
        }*/
        $scope.isUserApproval = false;


        $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findBlacklistVendor/' + form.approvalProcess.approvalProcessType.valueId).success(function (data) {
            form.blacklistVendor = data;
            if (typeof form.approvalProcess.approvalLevel !== 'undefined' && form.approvalProcess.approvalLevel != null) {
                $scope.getApprovalProcess(form.approvalProcess.approvalProcessType.id);
                $scope.getApprovalProcessAndStatus(form.approvalProcess.approvalProcessType.id);
            } else {
                $scope.isUserApproval = true;
            }

        }).error(function (data) {
            $scope.loading = false;
        });


        $scope.getApprovalProcess = function (approvalProcessType) {
            $scope.loadingApproval = true;
            form.levelList = [];
            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + approvalProcessType).success(function (data) {
                form.levelList = data;
                // consolelog(">> cek approval
                // :"+JSON.stringify(form.levelList));
                $scope.loading = false;
            }).error(function (data) {
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


        $scope.btnSimpan = function (statusId) {
        	$scope.errorNoteMessage = "";
			
			var checkNote = form.note;
			var isNote = true;
			if(form.note === null || form.note === '') {
				isNote = false;
				$scope.errorNoteMessage = "Note tidak boleh kosong";
			} else if ( checkNote.length >= 255) { 
				isNote = false;
				$scope.errorNoteMessage = "Note tidak boleh lebih dari 255 karakter";
			} else {
                ModalService.showModalConfirmation('Anda yakin data Approval sudah benar?')
                    .then(function (result) {
                        ModalService.showModalInformationBlock();
                        $scope.errorNoteMessage = '';
                        $scope.simpan(statusId);

                    });
                $scope.errorNoteMessage = '';
            }
        }


        $scope.simpan = function (statusId) {

            var formPost = {
                id: form.approvalProcess.approvalProcessType.valueId,
                note: form.note,
                status: statusId,
                approvalProcessId: form.approvalProcess.id
            };

            RequestService.doPOST('/procurement/vendormanagement/UnblacklistVendorServices/updateUnblacklistApproval', formPost)
                .then(function successCallback(data) { 
                    toaster.pop('success', 'Sukses', 'Approval Unblacklist vendor berhasil disimpan');
                    $timeout (function() {		
                    	$location.path('app/promise/procurement/approval');
        			}, 5000);
                    
                    ModalService.closeModalInformation();
                }, function errorCallback(response) {
                    ModalService.closeModalInformation();
                    ModalService.showModalInformation('Terjadi kesalahan pada system!');
                });

        }

        form.blacklist = true;
        form.showapproval = true;
        form.unblacklist = true;
        form.blacklistVendorDtl = {};
        
        RequestService.doGET('/procurement/vendormanagement/BlacklistVendorServices/getBlacklistVendorMaxVendor/' + form.approvalProcess.approvalProcessType.valueId)
        .then(function successCallback(data) {
            form.blacklistVendorDtl = data;
            RequestService.doGET('/procurement/vendormanagement/UnblacklistVendorServices/getUnblacklistVendorMaxAndBlacklistIdDetail/' + form.approvalProcess.approvalProcessType.valueId + '/' + form.blacklistVendorDtl.blacklistVendor.blacklistVendorId)
                .then(function successCallback(data) {
                    form.unblacklistVendorDtl = data;
                }, function errorCallback(response) {

                });

        	}, function errorCallback(response) {
        });


        $scope.showSuccess = function () {
            $scope.loadingSaving = false;
            toaster.pop('success', 'Sukses', 'Approval Blacklist vendor berhasil disimpan.');
            /*$scope.gotoIndex();*/
        }

        /*tombol aksi back to index approval vendor*/
        $scope.gotoIndex = function () {
            $location.path('/app/promise/procurement/approval');
        }



        //---------------------------------------------------------------------------------------------------------  
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

                                                $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendor/' + form.approvalProcess.approvalProcessType.valueId)
                                                    .success(function (data, status, headers, config) {
                                                        form.vendorBean = data;

                                                        // Get User
                                                        $http.get($rootScope.backendAddress + '/procurement/user/get-user/' + form.vendorBean.user)
                                                            .success(function (data, status, headers, config) {
                                                                form.vendorUserBean = data;

                                                                // Get User
                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/VendorProfileServices/getVendorProfileByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
                                                                    .success(function (data, status, headers, config) {
                                                                        form.vendorProfileBean = data[0];

                                                                        $http.get($rootScope.backendAddress + '/procurement/vendor/SegmentasiVendorServices/getSegmentasiVendorByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
                                                                            .success(function (data, status, headers, config) {
                                                                                form.penanggungJawabList = data;


                                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/BankVendorServices/getBankVendorByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
                                                                                    .success(function (data, status, headers, config) {
                                                                                        form.dataBankList = data;


                                                                                        $http.get($rootScope.backendAddress + '/procurement/vendor/SegmentasiVendorServices/getSegmentasiVendorByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
                                                                                            .success(function (data, status, headers, config) {
                                                                                                form.dataSegmentasiList = data;

                                                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/VendorPICServices/getVendorPICByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
                                                                                                    .success(function (data, status, headers, config) {
                                                                                                        form.penanggungJawabList = data;

                                                                                                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Vendor Registrasi Form')
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


                                                                                                                    $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya')
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


                                                                                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Salinan Tanda Daftar Perusahaan (TDP)')
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


                                                                                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Salinan Tanda Daftar Perusahaan (TDP)')
                                                                                                                                    .success(
                                                                                                                                        function (data, status, headers, config) {



                                                                                                                                            $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Salinan Surat Ijin Usaha (SIUP SIUJK)')
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


                                                                                                                                                        $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Bukti Fisik Perusahaan')
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


                                                                                                                                                                    $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Dokumen Quality yang dimiliki')
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



                                                                                                                                                                                $http.get($rootScope.backendAddress + '/procurement/vendor/DokumenRegistrasiVendorServices/getDokumenRegistrasiVendorByVendorIdAndSubject/' + form.approvalProcess.approvalProcessType.valueId + '/Dokumen Teknik')
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


                                                                                                                                                                                            $http.get($rootScope.backendAddress + '/procurement/vendor/PeralatanVendorServices/getPeralatanVendorByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
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


                                                                                                                                                                                                    $http.get($rootScope.backendAddress + '/procurement/vendor/KeuanganVendorServices/getKeuanganVendorByVendorId/' + form.approvalProcess.approvalProcessType.valueId)
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


                                                                                                                                                                                                            $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + form.approvalProcess.approvalProcessType.valueId + '/PELANGGAN')
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


                                                                                                                                                                                                                    $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + form.approvalProcess.approvalProcessType.valueId + '/MITRA')
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

                                                                                                                                                                                                                            $http.get($rootScope.backendAddress + '/procurement/vendor/PengalamanVendorServices/getPengalamanVendorByVendorIdAndTipePengalaman/' + form.approvalProcess.approvalProcessType.valueId + '/MITRA')
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
                                                                                                                                                                                                                                    form.loading = false;
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


        function fillForm() {
            /*fill form*/

            /*TAB LOGIN*/
            form.namaPengguna = form.vendorUserBean.namaPengguna;
            form.userId = form.vendorUserBean.username;

            /*TAB DATA PERUSAHAAN*/

            form.pilihPKP.name = form.vendorProfileBean.jenisPajakPerusahaan;
            form.nomorPKP = form.vendorProfileBean.nomorPKP;
            form.kualifikasiVendor = form.vendorProfileBean.kualifikasiVendor;
            form.mendaftardiunit = form.vendorProfileBean.unitTerdaftar;
            form.NamaPerusahaan = form.vendorProfileBean.namaPerusahaan;
            form.NamaSingkatan = form.vendorProfileBean.namaSingkatan;
            form.alamatPerusahaan = form.vendorProfileBean.alamat;
            form.alamatPerusahaan = form.vendorProfileBean.alamat;
            form.jenisPerusahaan = form.vendorProfileBean.tipePerusahaan;
            form.NPWPPerusahaan = form.vendorProfileBean.npwpPerusahaan;

            form.provinsiPerusahaan = form.vendorProfileBean.provinsi;
            form.kotaPerusahaan = form.vendorProfileBean.kota;
            form.poboxPerusahaan = form.vendorProfileBean.poBox;
            form.kodeposPerusahaan = form.vendorProfileBean.kodePos;

            form.TeleponPerusahaan = form.vendorProfileBean.telephone;
            form.NoFaxPerusahaan = form.vendorProfileBean.faksimile;
            form.EmailPerusahaan = form.vendorProfileBean.email;
            form.WebsitePerusahaan = form.vendorProfileBean.website;

            form.NamaContactPerson = form.vendorProfileBean.namaContactPerson;
            form.NoHPContactPerson = form.vendorProfileBean.hpContactPerson;
            form.EmailContactPerson = form.vendorProfileBean.emailContactPerson;
            form.NoKTPContactPerson = form.vendorProfileBean.ktpContactPerson;

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

        $scope.pilihPKP = function () {
            if (form.pilihPKP.name == '2') {
                form.disabled = true;
                form.labelMandatori = '';
            } else {
                form.disabled = false;
                form.labelMandatori = '*)';
            }
        }

        $scope.jenisPerusahaanList = [{
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
                                     ];


        $scope.showJabatan = function (pic) {
            var selected = [];
            if (pic.jabatan) {
                selected = $filter('filter')($scope.jabatanPenanggungJawabList, {
                    id: pic.jabatan.id
                });
            }
            return selected.length ? selected[0].nama : 'Not set';
        };

    }
    ApprovalViewUnblacklistVendorController.$inject = ['RequestService', '$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$location', '$stateParams', 'ModalService', '$timeout'];


})();