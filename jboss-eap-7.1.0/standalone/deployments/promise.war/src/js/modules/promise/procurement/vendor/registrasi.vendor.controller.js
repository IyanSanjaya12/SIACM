/**=========================================================
 * Module: RegistrasiVendorController.js
 * Author: F.H.K
 =========================================================*/
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('RegistrasiVendorController', RegistrasiVendorController);

	function RegistrasiVendorController($http, $scope, $rootScope, $filter, ngTableParams, $modal, $state, FileUploader, toaster, $resource, $timeout,RequestService) {
		var form = this;
		$scope.titleSelect = [{"title":"Mr"},{"title":"Mrs"},{"title":"Company"}];
		
		var dataRegistrasi = $rootScope.formRegistrasi;

		form.duplikatEmail = [];
		form.penanggungJawabList = [];
		//form.dokumentasiSKDList = [];
		form.dataBankList = [];
		form.userOk = false;
		form.validEmail = false;
		form.isPKS = 0;
		form.isCompany = true;
		$scope.tanpaPengalaman = false
		$scope.bukanPKS = true;
		$scope.hideSaveBtn=false;
		$scope.downloadFile = $rootScope.viewUploadBackendAddress+'/';
		form.tabActiveStatus={};
		
		

		/* ---------------------------------------------------- DATA LOGIN ----------------------------------------------------- */
		$scope.checkEmailDariDB = function (email) {
			if(Boolean(email)){
			form.userOk = true;
			$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/user/get-by-username/' + email, {
				ignoreLoadingBar: true
			}).success(function (data, status, headers, config) {
				if (data.length > 0) {
					toaster.pop('error', 'Kesalahan', 'Email sudah ada yang menggunakan, silahkan ketik yang berbeda!');
					document.getElementsByName("userId")[0].focus();
					return
				} else {
					toaster.pop('sucsess', 'Perhatian', 'Email belum pernah digunakan');
				}
			});
			}
		}

		$scope.validasiEmail = function (email) {
			var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
			var lanjut = true;
			if (regex.test(email) == false) {
//				toaster.pop('error', 'Kesalahan', 'Struktur Email anda SALAH (ex.procurement@promise.co.id)');
				lanjut = false;
			}
			return lanjut;
		}
		
		$scope.validasiWeb = function (web) {
			var regex = /^([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
			var lanjutWeb = true;
			if (regex.test(web) == false) {
//				toaster.pop('error', 'Kesalahan', 'Struktur Email anda SALAH (ex.procurement@promise.co.id)');
				lanjutWeb = false;
			}
			return lanjutWeb;
		}

		$scope.validasiPassword = function (password) {
			// Peraturan password:
			// 1. Harus mengandung 1 huruf KECIL dan 1 huruf BESAR
			// 2. Harus mengandung 1 ANGKA 0-9
			// 3. Harus lebih dari 8 karakter
			if(password) {
				var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})");
				var lanjut = true;
				if (strongRegex.test(password) == false) {
					toaster.pop('error', 'Kesalahan', 'Struktur Password anda SALAH, Lihat disclaimer di bawah');
					lanjut = false;
				}

				return lanjut;
			}
			
		}
		
		var uploadVendorLogo = $scope.uploadVendorLogo = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });
		
		uploadVendorLogo.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,'img') && (this.queue.length < 10));
			}
		});

		uploadVendorLogo.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			toaster.pop('Success', 'Berhasil', 'Logo Vendor Berhasil Diupload');
		};
		
		uploadVendorLogo.onErrorItem = function(fileItem, response, status, headers) {
            toaster.pop('error', 'Kesalahan', 'Logo Vendor Gagal Di upload, Silahkan coba lagi');
        };
		
		$scope.uploadLogo = function() {
			
			if (uploadVendorLogo.queue.length > 1) {
				uploadVendorLogo.queue.splice(0, uploadVendorLogo.queue.length - 1);
			}
			
			if(uploadVendorLogo.queue != undefined && uploadVendorLogo.queue.length > 0) {
				angular.forEach(uploadVendorLogo.queue, function(item) {
	        		item.upload();
	            });
				
			} /*else {
				RequestService.showModalInformation('Silahkan upload dalam format.jpg, .png dan .gif','danger');
			}*/
		}
		
		var uploadVendorHeadImg = $scope.uploadVendorHeadImg = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            method: 'POST'
        });

		uploadVendorHeadImg.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,'img') && (this.queue.length < 10));
			}
		});
		
		uploadVendorHeadImg.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
			toaster.pop('Success', 'Berhasil', 'Header Image Berhasil Diupload');
		};
		
		uploadVendorLogo.onErrorItem = function(fileItem, response, status, headers) {
            toaster.pop('error', 'Kesalahan', 'Header Image Gagal Di upload, Silahkan coba lagi');
        };
		
		$scope.uploadHeaderImg = function() {
			if (uploadVendorHeadImg.queue.length > 1) {
				uploadVendorHeadImg.queue.splice(0, uploadVendorHeadImg.queue.length - 1);
			}
			
			if(uploadVendorHeadImg.queue != undefined && uploadVendorHeadImg.queue.length > 0) {
				angular.forEach(uploadVendorHeadImg.queue, function(item) {
	        		item.upload();
	            });
			} /*else {
				RequestService.showModalInformation('Silahkan upload dalam format .jpg dan .png','danger');
			}*/
		}

		$scope.backToLogin = function () {
			$state.go("page.portal");
		}

		$scope.nextDataPerusahaan = function () {
			var validationTab01 = false;
			//form.userIdSuccsess = false;
			
			if (form.namaPengguna == undefined || form.namaPengguna == "") {
				form.namaPenggunaError = true;
				document.getElementsByName("namaPengguna")[0].focus();
			} else {
				form.namaPenggunaError = false;
			}
			
			
			if (form.userId == undefined || form.userId == "") {
				form.userIdError = true;
				document.getElementsByName("userId")[0].focus();
			} else {
				form.userIdError = false;
			}

			if (form.password == undefined || form.password == "") {
				form.passwordError = true;
				document.getElementsByName("password")[0].focus();
			} else {
				form.passwordError = false;
			}

			if (form.retypePassword == undefined || form.retypePassword == "") {
				form.retypePasswordError = true;
				document.getElementsByName("retypePassword")[0].focus();
			} else {
				form.retypePasswordError = false;
			}

			if (form.namaPenggunaError == false && form.userIdError == false && form.passwordError == false && form.retypePasswordError == false) {
				if ($scope.validasiEmail(form.userId)) {
					if (!form.userOk) {
						toaster.pop('warning', 'Perhatian', 'Silahkan Periksa User Anda! (Check User Id)');
						document.getElementsByName("userId")[0].focus();
						return
					} else if ($scope.validasiPassword(form.password)) {
						if (form.password != form.retypePassword) {
							toaster.pop('warning', 'Kesalahan', 'Password dan Retype Password Beda!');
							document.getElementsByName("retypePassword")[0].focus();
						} else {
							validationTab01 = true;
						}
					} else {
						document.getElementsByName("password")[0].focus();
						return
					}
				} else {
					document.getElementsByName("userId")[0].focus();
					return
				}
			}

		return validationTab01; 
		//return true; //skip mandatory tab
		}

		/* ---------------------------------------------------- END DATA LOGIN ------------------------------------------------- */


		/* ---------------------------------------------------- DATA PERUSAHAAN ------------------------------------------------ */
		
		$scope.getDataList = function() {
			$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/getDataList')
				.success(function (data, status, headers, config) {
					
					$scope.kualifikasiVendorList = data.kualifikasiVendorList;
					$scope.unitTerdaftarList = data.organisasiList;
					$scope.provinsiPerusahaanList = data.wilayahList;
					$scope.jabatanPenanggungJawabList = data.jabatanList;
					$scope.mataUangList = data.mataUangList;
					$scope.bidangUsahaList = data.bidangUsahaList;
			});
		}
		$scope.getDataList();
		
		$scope.getBussinessArea = function(parentId){
			form.bussinessArea =null;
			$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/organisasi/getByParentId/'+parentId)
			.success(function (data, status, headers, config) {
				$scope.bussinessAreaList = data;
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

		$scope.jenisPerusahaanList = [
			{
				id: 1,
				nama: "PT"
            },
			{
				id: 2,
				nama: "CV"
            }
        ];
		

		// DATA WILAYAH KABUPATEN/KOTA
		var listWilayahKota = function (kodePropinsi) {
			$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/wilayah/getKotaList/' + kodePropinsi)
				.success(function (data, status, headers, config) {
					$scope.kotaPerusahaanList = data;
				});
		}

		$scope.pilihPropinsi = function () {
			form.kotaPerusahaan = null;
			listWilayahKota(form.provinsiPerusahaan.lokasi_propinsi);
		}
		
		$scope.pilihPropinsiNPWP = function () {
			form.kotaPerusahaan = null;
			listWilayahKota(form.provinsiNPWP.lokasi_propinsi);
		}

		

		// penanggung jawab in tab data perusahaan

		// add Penanggung jawab
		$scope.addPenanggungJawab = function () {
			$scope.dataInserted = {
				isNew: true
			};
			
			
			form.penanggungJawabList.push($scope.dataInserted);
		};

		$scope.showJabatan = function (pic) {
			var selected = [];
			if (pic.jabatan) {
				selected = $filter('filter')($scope.jabatanPenanggungJawabList, {
					id: pic.jabatan.id
				});
			}
			return selected.length ? selected[0].nama : 'Not set';
		};

		$scope.checkName = function (data, indeks) {
			if (data == undefined || data == null || data.length < 1) {
                return 'silahkan masukkan nama penanggung jawab!';
			}
            if (data.length > 30) {
                return 'input tidak boleh melebihi 30 karakter';
			}
            if (data.match(/^[a-zA-Z ]*$/)) {
                
			}else {
				return 'input harus berisi huruf!';
			}
		}

		$scope.checkEmail = function (data, indeks) {
			if (data == undefined || data == null || data.length < 1) {
                return 'silahkan masukkan email penanggung jawab!';
			}
            if (data.match(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/)) {
                
			}else {
				return 'format email belum valid';
			}
		}

		$scope.checkJabatan = function (data, indeks) {
			if (typeof data == 'undefined') {
				return "silahkan pilih jabatan dari penanggung jawab";
			}
		}
		
		$scope.isCompanyCheck = function () {
			
			if(form.title.title == 'Company'){
				form.isCompany = true;
				form.NoKTPContactPerson = null;
				form.NoKKContactPerson = null;
				
			}else{
				form.isCompany = false;
				form.noAktaPendirian = null;
			}
		}

		$scope.changeNonPKP = function () {
			form.nomorPKP = undefined;
		}

		$scope.checkPKP = function (noPKP, flag) {
			
			if (form.nomorPKP != undefined && form.nomorPKP != null && form.nomorPKP != '') {
				
				RequestService.doGET('/procurement/vendor/VendorProfileServices/getVendorProfileByNoPKP/' + noPKP)
				.then(function success(data) {
					if (data.length > 0) {
						toaster.pop('error', 'Kesalahan', 'Nomor PKP sudah pernah ada, Silahkan input yang Baru');
						document.getElementsByName("nomorPKP")[0].focus();
					} else {
						if (flag != 1) {
							toaster.pop('sucsess', 'Perhatian', 'Nomor PKP Baru, silahkan lanjutkan');
						}
					}
				}, function error(response) {
					
		        	RequestService.informError("Terjadi Kesalahan Pada System");
				});
				
			}
		}

		$scope.checkNPWP = function (noNPWP, flag) {
			if (form.NPWPPerusahaan == undefined || form.NPWPPerusahaan == "") {
				form.NPWPPerusahaanError = true;
				document.getElementsByName("NPWPPerusahaan")[0].focus();
				toaster.pop('error', 'Kesalahan', 'Nomor NPWP sudah pernah ada, Silahkan input yang Baru');
			} 
			
			else if (form.NPWPPerusahaan.length<15){
				form.NPWPPerusahaanLengthError = true;
				document.getElementsByName("NPWPPerusahaan")[0].focus();
			}
			
			else {
				form.NPWPPerusahaanError = false;
				$http.get($rootScope.backendAddress + '/procurement/vendor/VendorProfileServices/getVendorProfileByNoNPWP/' + noNPWP)
					.success(function (data, status, headers, config) {
					
						if (data.length > 0) {
							toaster.pop('error', 'Kesalahan', 'Nomor NPWP sudah pernah ada, Silahkan input yang Baru');
							document.getElementsByName("nomorNPWP")[0].focus();
						} else {
							if (flag != 1) {
								form.NPWPPerusahaanLengthError = false;
								toaster.pop('sucsess', 'Perhatian', 'Nomor NPWP Baru, silahkan lanjutkan');
							}if (flag == 1) {
								form.NPWPPerusahaanLengthError = false;
								
							}
						}
					});
			}
		}

		$scope.removePenanggungJawab = function (index) {
			form.penanggungJawabList.splice(index, 1);
		};

		$scope.cancelPenanggungJawab = function (rowform, index) {
			form.penanggungJawabList.splice(index, 1);
		}
		
		$scope.tanggalBerdiriOpen = {};
		  
		$scope.tanggalBerdiriStatus = function($event, elementOpened) {
			$event.preventDefault();
		    $event.stopPropagation();
	
		    $scope.tanggalBerdiriOpen[elementOpened] = !$scope.tanggalBerdiriOpen[elementOpened];
		};

		$scope.nextDataBank = function () {

			
			var validationTab02 = false;

			if (form.pilihPKP.name == '1') {
				if (form.nomorPKP == undefined || form.nomorPKP == "") {
					form.nomorPKPError = true;
					document.getElementsByName("nomorPKP")[0].focus();
				} else {
					form.nomorPKPError = false;
				}

				if (form.NPWPPerusahaan == undefined || form.NPWPPerusahaan == "") {
					form.NPWPPerusahaanError = true;
					document.getElementsByName("NPWPPerusahaan")[0].focus();
				} else {
					form.NPWPPerusahaanError = false;
				}
			} else {
				form.nomorPKPError = false;
				if (form.NPWPPerusahaan == undefined || form.NPWPPerusahaan == "") {
					form.NPWPPerusahaanError = true;
					document.getElementsByName("NPWPPerusahaan")[0].focus();
				} else {
					form.NPWPPerusahaanError = false;
				}
			}

			if (form.kualifikasiVendor == undefined || form.kualifikasiVendor == "") {
				form.kualifikasiVendorError = true;
				document.getElementsByName("kualifikasiVendor")[0].focus();
			} else {
				form.kualifikasiVendorError = false;
			}
			
			if (form.title == undefined || form.title == "") {
				form.TitleError = true;
				document.getElementsByName("title")[0].focus();
				
			} else {
				form.TitleError = false;
			}

			if (form.unitTerdaftar == undefined || form.unitTerdaftar == "") {
				form.unitTerdaftarError = true;
				document.getElementsByName("unitTerdaftar")[0].focus();
			} else {
				form.unitTerdaftarError = false;
			}
			
			if (form.bussinessArea == undefined || form.bussinessArea == "") {
				form.bussinessAreaError = true;
				document.getElementsByName("bussinessArea")[0].focus();
			} else {
				form.bussinessAreaError = false;
			}

			if (form.NamaPerusahaan == undefined || form.NamaPerusahaan == "") {
				form.NamaPerusahaanError = true;
				document.getElementsByName("NamaPerusahaan")[0].focus();
			} else {
				form.NamaPerusahaanError = false;
			}

			if (form.jenisPerusahaan == undefined || form.jenisPerusahaan == "") {
				form.jenisPerusahaanError = true;
				document.getElementsByName("jenisPerusahaan")[0].focus();
			} else {
				form.jenisPerusahaanError = false;
			}
			
			if (form.namaNPWP == undefined || form.namaNPWP == "") {
				form.namaNPWPError = true;
				document.getElementsByName("namaNPWP")[0].focus();
			} else {
				form.namaNPWPError = false;
			}
			
			if (form.alamatNPWP == undefined || form.alamatNPWP == "") {
				form.alamatNPWPError = true;
				document.getElementsByName("alamatNPWP")[0].focus();
			} else {
				form.alamatNPWPError = false;
			}
			
			if (form.provinsiNPWP == undefined || form.provinsiNPWP == "") {
				form.provinsiNPWPError = true;
				document.getElementsByName("provinsiNPWP")[0].focus();
			} else {
				form.provinsiNPWPError = false;
			}
			
			if (form.kotaNPWP == undefined || form.kotaNPWP == "") {
				form.kotaNPWPError = true;
				document.getElementsByName("alamatNPWP")[0].focus();
			} else {
				form.kotaNPWPError = false;
			}

			if (form.NamaSingkatan == undefined || form.NamaSingkatan == "") {
				form.NamaSingkatanError = true;
				document.getElementsByName("NamaSingkatan")[0].focus();
			} else {
				form.NamaSingkatanError = false;
			}

			if (form.alamatPerusahaan == undefined || form.alamatPerusahaan == "") {
				form.alamatPerusahaanError = true;
				document.getElementsByName("alamatPerusahaan")[0].focus();
			} else {
				form.alamatPerusahaanError = false;
			}

			if (form.provinsiPerusahaan == undefined || form.provinsiPerusahaan == "") {
				form.provinsiPerusahaanError = true;
				document.getElementsByName("provinsiPerusahaan")[0].focus();
			} else {
				form.provinsiPerusahaanError = false;
			}

			if (form.kotaPerusahaan == undefined || form.kotaPerusahaan == "") {
				form.kotaPerusahaanError = true;
				document.getElementsByName("kotaPerusahaan")[0].focus();
			} else {
				form.kotaPerusahaanError = false;
			}
			
			if ((form.noAktaPendirian == undefined || form.noAktaPendirian == "") && form.isCompany == true) {
				form.noAktaPendirianError = true;
				document.getElementsByName("noAktaPendirian")[0].focus();
			} else {
				form.noAktaPendirianError = false;
			}
			
			if (form.kodeposPerusahaan == undefined || form.kodeposPerusahaan == "") {
				form.kodeposPerusahaanError = true;
				document.getElementsByName("kodeposPerusahaan")[0].focus();
			} else {
				form.kodeposPerusahaanError = false;
			}

			if (form.TeleponPerusahaan == undefined || form.TeleponPerusahaan == "") {
				form.TeleponPerusahaanError = true;
				document.getElementsByName("TeleponPerusahaan")[0].focus();
			} else {
				form.TeleponPerusahaanError = false;
			}

			if (form.EmailPerusahaan == undefined || form.EmailPerusahaan == "") {
				form.EmailPerusahaanError = true;
				document.getElementsByName("EmailPerusahaan")[0].focus();
			} else {
				form.EmailPerusahaanError = false;
			}

			if (form.NamaContactPerson == undefined || form.NamaContactPerson == "") {
				form.NamaContactPersonError = true;
				document.getElementsByName("NamaContactPerson")[0].focus();
			} else {
				form.NamaContactPersonError = false;
			}

			if (form.NoHPContactPerson == undefined || form.NoHPContactPerson == "") {
				form.NoHPContactPersonError = true;
				document.getElementsByName("NoHPContactPerson")[0].focus();
			} else {
				form.NoHPContactPersonError = false;
			}

			if (form.EmailContactPerson == undefined || form.EmailContactPerson == "") {
				form.EmailContactPersonError = true;
				document.getElementsByName("EmailContactPerson")[0].focus();
			} else {
				form.EmailContactPersonError = false;
			}

			if ((form.NoKTPContactPerson == undefined || form.NoKTPContactPerson == "")&& form.isCompany == false) {
				form.NoKTPContactPersonError = true;
				document.getElementsByName("NoKTPContactPerson")[0].focus();
			} else {
				form.NoKTPContactPersonError = false;
			}
			
			if ((form.NoKKContactPerson == undefined || form.NoKKContactPerson == "") && form.isCompany == false) {
				form.NoKKContactPersonError = true;
				document.getElementsByName("NoKKContactPerson")[0].focus();
			} else {
				form.NoKKContactPersonError = false;
			}
			
			/*if (form.JumlahKaryawan == undefined || form.JumlahKaryawan == "") {
				form.JumlahKaryawanError = true;
				document.getElementsByName("JumlahKaryawan")[0].focus();
			} else {
				form.JumlahKaryawanError = false;
			}*/

			if (form.kualifikasiVendorError == false && form.unitTerdaftarError == false && form.NamaPerusahaanError == false 
					&& form.jenisPerusahaanError == false && form.NamaSingkatanError == false && form.alamatPerusahaanError == false 
					&& form.provinsiPerusahaanError == false && form.kotaPerusahaanError == false && form.TeleponPerusahaanError == false && form.kodeposPerusahaanError == false
					&& form.EmailPerusahaanError == false && form.NamaContactPersonError == false && form.NoHPContactPersonError == false 
					&& form.EmailContactPersonError == false && form.NoKTPContactPersonError == false && form.NoKKContactPersonError == false
					&& form.TitleError == false && form.bussinessAreaError == false && form.namaNPWPError == false && form.alamatNPWPError == false && form.NPWPPerusahaanLengthError == false
					&& form.kotaNPWPError == false && form.noAktaPendirianError == false) {

				if ($scope.validasiEmail(form.userId)) {
					if (form.pilihPKP.name == '1') {
						if (form.nomorPKPError == false && form.NPWPPerusahaanError == false) {
							validationTab02 = true;
						}
					} else {
						if (form.NPWPPerusahaanError == false) {
							validationTab02 = true;
						}
					}
				}
			}
		
			
			
			return validationTab02;
			//return true;//skip mandatory tab
		}

		/* ---------------------------------------------------- END DATA PERUSAHAAN -------------------------------------------- */


		/* ---------------------------------------------------- DATA BANK ------------------------------------------------------ */

		$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/negara/get-list')
			.success(function (data, status, headers, config) {
				$scope.negaraList = data;
		});

		form.dataBankList = [];
		$scope.dataBankTable = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataBankList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataBankList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		$scope.addDataBank = function () {
			var bankvendormodalinstance = $modal.open({
				templateUrl: '/bankvendor.html',
				controller: 'BankVendorModalController',
				size: 'lg',
				resolve: {
					dataBankVendor: function () {
						return {};
					},
					mataUangList: function () {
						return $scope.mataUangList;
					},
					negaraList: function () {
						return $scope.negaraList;
					}
				}
			});
			bankvendormodalinstance.result.then(function (dataBankVendor) {
				if (dataBankVendor != undefined && dataBankVendor !== null) {
					form.dataBankList.push(dataBankVendor);
					$scope.dataBankTable.reload();
				}
			});
		}

		$scope.editDataBank = function (index, dataBankVendor) {
			var bankvendormodalinstance = $modal.open({
				templateUrl: '/bankvendor.html',
				controller: 'BankVendorModalController',
				size: 'lg',
				resolve: {
					dataBankVendor: function () {
						return dataBankVendor;
					},
					mataUangList: function () {
						return $scope.mataUangList;
					},
					negaraList: function () {
						return $scope.negaraList;
					}
				}
			});
			bankvendormodalinstance.result.then(function (dataBankVendor) {
				if (dataBankVendor != undefined && dataBankVendor !== null) {
					form.dataBankList.splice(index, 1, dataBankVendor);
					$scope.dataBankTable.reload();
				}
			});
		}

		$scope.deleteDataBank = function (index, dataBankVendor) {
			var bankvendormodalinstance = $modal.open({
				templateUrl: '/alertModal.html',
				controller: 'DeleteDataModalRegistrasiController',
				size: 'sm',
				resolve: {
					nameData: function () {
						return dataBankVendor.namaBank;
					}
				}
			});
			bankvendormodalinstance.result.then(function () {
				form.dataBankList.splice(index, 1);
				$scope.dataBankTable.reload();
			});
		}

		$scope.nextDataSegmentasi = function () {
			var validationTab03 = false; 
			
			if (form.dataBankList.length > 0) {
				validationTab03 = true;
			}
				
			else {
				toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Bank');
			}
				

			return validationTab03;
			//return true;//skip mandatory tab
		}

		/* ---------------------------------------------------- DATA SEGMENTASI ------------------------------------------------ */

		form.dataSegmentasiList = [];
		$scope.tableSegmentasiVendor = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataSegmentasiList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataSegmentasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		$scope.addDataSegmentasi = function () {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/segmentasivendormodal.html',
				controller: 'SegmentasiVendorModalController',
				size: 'lg',
				resolve: {
					dataSegmentasi: function () {
						return {};
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataSegmentasi) {
				if (dataSegmentasi != undefined && dataSegmentasi !== null) {
					form.dataSegmentasiList.push(dataSegmentasi);
					$scope.tableSegmentasiVendor.reload();
				}
			});
		}

		$scope.editDataSegmentasi = function (index, dataSegmentasi) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/segmentasivendormodal.html',
				controller: 'SegmentasiVendorModalController',
				size: 'lg',
				resolve: {
					dataSegmentasi: function () {
						return dataSegmentasi;
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataSegmentasi) {
				if (dataSegmentasi != undefined && dataSegmentasi !== null) {
					form.dataSegmentasiList.splice(index, 1, dataSegmentasi);
					$scope.tableSegmentasiVendor.reload();
				}
			});
		}

		$scope.removeDataSegmentasi = function (index, dataSegmentasi) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/alertModal.html',
				controller: 'DeleteDataModalRegistrasiController',
				size: 'sm',
				resolve: {
					nameData: function () {
						return dataSegmentasi.subBidangUsaha.nama;
					}
				}
			});
			segmentasimodalinstance.result.then(function () {
				form.dataSegmentasiList.splice(index, 1);
				$scope.tableSegmentasiVendor.reload();
			});
		}

		$scope.nextDataDokumentasi = function () {
			var validationTab04 = false;
			
			if (form.dataSegmentasiList.length > 0) {
				validationTab04 = true;
			}
			else {
				toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Segmentasi');
			}
				

			
			
			return validationTab04;
			//return true;//skip mandatory tab
			
		}

		/* ---------------------------------------------------- DATA DOKUMEN --------------------------------------------------- */
		
		// add Dokuemntasi SKD
		$scope.addDokumentasiSKD = function () {
			$scope.dataDokumentasiSKDInserted = {
				isNew: true
			};
			
			
			form.dokumentasiSKDList.push($scope.dataDokumentasiSKDInserted);
		};
		
		//cek isPKS
		$scope.isPKSChecked = function (isPKS) {
			if (isPKS) {
				$scope.bukanPKS = false;
				form.isPKS = 1;
			} else {
				////console.log('masuk sini 2');
				$scope.bukanPKS = true;
				form.isPKS = 0;
				$scope.dokumenPKS.namaDokumen = null;
				$scope.dokumenPKS.tanggalBerakhir = null;
				$scope.dokumenPKS.tanggalTerbit = null;
				$scope.uploadDokumenPKS.queue = [];
			}
			////console.log("TANPA PENGALAMAN = "+$scope.tanpaPengalaman);
		}
		
		var rekapUpload = function (dataUpload) {
			dataUpload.filters.push({
				name: 'customFilter',
				fn: function (item /*{File|FileLikeObject}*/ , options) {
					return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
				}
			});

			dataUpload.onCompleteItem = function (fileItem, response, status, headers) {
				console.info('onCompleteItem', fileItem, response, status, headers);
				fileItem.realFileName = response.fileName;
			};
		}
		
		$scope.validasiUpload = function(fileItem) {
			var fileExt = fileItem.file.name.toLowerCase().split('.').pop();
            var fileSize = fileItem.file.size / (1024 * 1024);
            var maxUpload = $rootScope.fileUploadSize / (1024 * 1024);
            
            if (fileSize > maxUpload) {
            	toaster.pop('error', 'Kesalahan', 'Ukuran file tidak boleh lebih dari ' + maxUpload + ' MB!');
            	fileItem.remove();
                return false;
            } else if ((!(fileExt == 'pdf' || fileExt == 'xls' || fileExt == 'xlsx' || fileExt == 'doc' || fileExt == 'docx' || fileExt == 'zip' || fileExt == 'jpg' || fileExt == 'jpeg'))) {
            	toaster.pop('error', 'Kesalahan', 'Extensi yang diperbolehkan hanya ' + 'xls, xlsx, doc, docx, pdf, jpg, jpeg dan zip!');
            	fileItem.remove();
                return false;
            }
            
            return true;
		}
		
		/* Vendor Registrasi Form */
		
		$scope.VendorRegistrasiForm = {};
		$scope.VendorRegistrasiForm.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen1 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.VendorRegistrasiForm.tanggalTerbitStatus = true;
		};

		$scope.VendorRegistrasiForm.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen1 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.VendorRegistrasiForm.tanggalBerakhirStatus = true;
		}

		$scope.changeVendorRegistrasiForm = function () {
			$scope.VendorRegistrasiForm.tanggalBerakhir = null;
		}
		
		var uploadVendorRegistrasiForm = $scope.uploadVendorRegistrasiForm = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadVendorRegistrasiForm.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});
		
		/*uploadVendorRegistrasiForm.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

		uploadVendorRegistrasiForm.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadVendorRegistrasiForm.onErrorItem = function(fileItem, response, status, headers) {
            $scope.tampilVRF = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Vendor Registrasi Form Gagal Di upload, Silahkan coba lagi');
        };
		
		uploadVendorRegistrasiForm.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeVRF = progress;
			if($scope.progressTimeVRF == 100) {
				$scope.tampilVRF = false;
			}
        };
		
		$scope.uploadVendorRegisForm = function() {
			if(uploadVendorRegistrasiForm.queue != undefined && uploadVendorRegistrasiForm.queue.length > 0) {
				if(uploadVendorRegistrasiForm.queue.length > 1) {
					uploadVendorRegistrasiForm.queue.shift();
				}
				angular.forEach(uploadVendorRegistrasiForm.queue, function(item) {
					$scope.tampilVRF = true;
	        		item.upload();
	            });
			}
		}
		
	/* Surat Keterangan Bebas */
		
		$scope.SKB = {};
		$scope.SKB.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen11 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SKB.tanggalTerbitStatus = true;
		};

		$scope.SKB.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen11 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SKB.tanggalBerakhirStatus = true;
		}

		$scope.changeSKB = function () {
			$scope.SKB.tanggalBerakhir = null;
		}
		
		var uploadSKB = $scope.uploadSKB = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadSKB.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		uploadSKB.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadSKB.onErrorItem = function(fileItem, response, status, headers) {
            $scope.tampilSKB = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Surat Keterangan Bebas Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadSKB.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSKB = progress;
			if($scope.progressTimeSKB == 100) {
				$scope.tampilSKB = false;
			}
        };
		
		$scope.uploadSKBButton = function() {
			if(uploadSKB.queue != undefined && uploadSKB.queue.length > 0) {
				if(uploadSKB.queue.length > 1) {
					uploadSKB.queue.shift();
				}
				angular.forEach(uploadSKB.queue, function(item) {
					$scope.tampilSKB = true;
	        		item.upload();
	            });
			}
		}
		
		//rekapUpload(uploadVendorRegistrasiForm);
		
		/* Salinan Akta Pendirian */
		$scope.SalinanAktePendirian = {};
		$scope.SalinanAktePendirian.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen2 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanAktePendirian.tanggalTerbitStatus = true;
		};

		$scope.SalinanAktePendirian.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen2 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanAktePendirian.tanggalBerakhirStatus = true;
		};
		

		$scope.changeSalinanAktePendirian = function () {
			$scope.SalinanAktePendirian.tanggalBerakhir = null;
		}
		
		var uploadSalinanAktePendirian = $scope.uploadSalinanAktePendirian = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadSalinanAktePendirian.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadSalinanAktePendirian.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadSalinanAktePendirian.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadSalinanAktePendirian.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilSAP = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Salinan Akte Pendirian Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadSalinanAktePendirian.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSAP = progress;
			if($scope.progressTimeSAP == 100) {
				$scope.tampilSAP = false;
			}
        };
		
		$scope.uploadSalinanAktaPendirian = function() {
			if(uploadSalinanAktePendirian.queue != undefined && uploadSalinanAktePendirian.queue.length > 0) {
				if(uploadSalinanAktePendirian.queue.length > 1) {
					uploadSalinanAktePendirian.queue.shift();
				}
				angular.forEach(uploadSalinanAktePendirian.queue, function(item) {
					$scope.tampilSAP = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadSalinanAktePendirian);

		
		//--------------is pks-------------------//
		$scope.dokumenPKS = {};
		$scope.dokumenPKS.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpenDokumenPKS = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenPKS.tanggalTerbitStatus = true;
		};

		$scope.dokumenPKS.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpenDokumenPKS = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenPKS.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenPKS = function () {
			$scope.dokumenPKS.tanggalBerakhir = null;
		}
		
		var uploadDokumenPKS = $scope.uploadDokumenPKS = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenPKS.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});
		
		/*uploadDokumenPKS.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenPKS.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenPKS.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilPKS = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen PKS Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenPKS.onProgressItem = function (fileItem, progress) {
			$scope.progressTimePKS = progress;
			if($scope.progressTimePKS == 100) {
				$scope.tampilPKS = false;
			}
        };
		
		$scope.uploadPKSDokumen = function() {
			if(uploadDokumenPKS.queue != undefined && uploadDokumenPKS.queue.length > 0) {
				if(uploadDokumenPKS.queue.length > 1) {
					uploadDokumenPKS.queue.shift();
				}
				angular.forEach(uploadDokumenPKS.queue, function(item) {
					$scope.tampilPKS = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenPKS);
		
		//---------------------- SPR ----------------------------------
		$scope.dokumenSPR = {};
		$scope.dokumenSPR.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpenDokumenSPR = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSPR.tanggalTerbitStatus = true;
		};

		$scope.dokumenSPR.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpenDokumenSPR = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSPR.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenSPR = function () {
			$scope.dokumenSPR.tanggalBerakhir = null;
		}
		
		var uploadDokumenSPR = $scope.uploadDokumenSPR = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenSPR.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});
		
		/*uploadDokumenSPR.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenSPR.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenSPR.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilSPR = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen SPR Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenSPR.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSPR = progress;
			if($scope.progressTimeSPR == 100) {
				$scope.tampilSPR = false;
			}
        };
		
		$scope.uploadSPRDokumen = function() {
			if(uploadDokumenSPR.queue != undefined && uploadDokumenSPR.queue.length > 0) {
				if(uploadDokumenSPR.queue.length > 1) {
					uploadDokumenSPR.queue.shift();
				}
				angular.forEach(uploadDokumenSPR.queue, function(item) {
					$scope.tampilSPR = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenSPR);
		//--------------------------- SPB -------------------------------------
		$scope.dokumenSPB = {};
		$scope.dokumenSPB.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpenDokumenSPB = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSPB.tanggalTerbitStatus = true;
		};

		$scope.dokumenSPB.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpenDokumenSPB = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSPB.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenSPB = function () {
			$scope.dokumenSPB.tanggalBerakhir = null;
		}
		
		var uploadDokumenSPB = $scope.uploadDokumenSPB = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenSPB.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadDokumenSPB.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenSPB.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenSPB.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
            toaster.pop('error', 'Kesalahan', 'Dokumen SPB Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenSPB.onProgressItem = function (fileItem, progress) {
			$scope.progressTime = progress;
        };
		
		$scope.uploadSPBDokumen = function() {
			if(uploadDokumenSPB.queue != undefined && uploadDokumenSPB.queue.length > 0) {
				if(uploadDokumenSPB.queue.length > 1) {
					uploadDokumenSPB.queue.shift();
				}
				angular.forEach(uploadDokumenSPB.queue, function(item) {
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenSPB);
		
		//---------------------- SKD ----------------------------------
		$scope.dokumenSKD = {};
		$scope.dokumenSKD.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpenDokumenSKD = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSKD.tanggalTerbitStatus = true;
		};

		$scope.dokumenSKD.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpenDokumenSKD = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.dokumenSKD.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenSKD = function () {
			$scope.dokumenSKD.tanggalBerakhir = null;
		}
		
		var uploadDokumenSKD = $scope.uploadDokumenSKD = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenSKD.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadDokumenSKD.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenSKD.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenSKD.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilSKD = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen SKD Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenSKD.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSKD = progress;
			if($scope.progressTimeSKD == 100) {
				$scope.tampilSKD = false;
			}
        };
		
		$scope.uploadSKDDokumen = function() {
			if(uploadDokumenSKD.queue != undefined && uploadDokumenSKD.queue.length > 0) {
				if(uploadDokumenSKD.queue.length > 1) {
					uploadDokumenSKD.queue.shift();
				}
				angular.forEach(uploadDokumenSKD.queue, function(item) {
					$scope.tampilSKD = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenSKD);
		
		//----------------------------------------------------------------------
		
		$scope.cekFileUpload= function (){
			
		}
		
		//---------------------------------------------------------------------


		$scope.SalinanTandaDaftar = {};
		$scope.SalinanTandaDaftar.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen3 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanTandaDaftar.tanggalTerbitStatus = true;
		};

		$scope.SalinanTandaDaftar.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen3 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanTandaDaftar.tanggalBerakhirStatus = true;
		};

		$scope.changeSalinanTandaDaftar = function () {
			$scope.SalinanTandaDaftar.tanggalBerakhir = null;
		}
		
		var uploadSalinanTandaDaftar = $scope.uploadSalinanTandaDaftar = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadSalinanTandaDaftar.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadSalinanTandaDaftar.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadSalinanTandaDaftar.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadSalinanTandaDaftar.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilSTD = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Salinan Tanda Daftar Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadSalinanTandaDaftar.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSTD = progress;
			if($scope.progressTimeSTD == 100) {
				$scope.tampilSTD = false;
			}
        };
		
		$scope.uploadSTDDokumen = function() {
			if(uploadSalinanTandaDaftar.queue != undefined && uploadSalinanTandaDaftar.queue.length > 0) {
				if(uploadSalinanTandaDaftar.queue.length > 1) {
					uploadSalinanTandaDaftar.queue.shift();
				}
				angular.forEach(uploadSalinanTandaDaftar.queue, function(item) {
					$scope.tampilSTD = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadSalinanTandaDaftar);

		$scope.SalinanSuratIjinUsaha = {};
		$scope.SalinanSuratIjinUsaha.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen4 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanSuratIjinUsaha.tanggalTerbitStatus = true;
		};

		$scope.SalinanSuratIjinUsaha.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen4 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.SalinanSuratIjinUsaha.tanggalBerakhirStatus = true;
		};

		$scope.changeSalinanSuratIjinUsaha = function () {
			$scope.SalinanSuratIjinUsaha.tanggalBerakhir = null;
		}

		var uploadSalinanSuratIjinUsaha = $scope.uploadSalinanSuratIjinUsaha = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadSalinanSuratIjinUsaha.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadSalinanSuratIjinUsaha.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadSalinanSuratIjinUsaha.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadSalinanSuratIjinUsaha.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilSSIU = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Salinan Surat Ijin Usaha Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadSalinanSuratIjinUsaha.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeSSIU = progress;
			if($scope.progressTimeSSIU == 100) {
				$scope.tampilSSIU = false;
			}
        };
		
		$scope.uploadSSIUDokumen = function() {
			if(uploadSalinanSuratIjinUsaha.queue != undefined && uploadSalinanSuratIjinUsaha.queue.length > 0) {
				if(uploadSalinanSuratIjinUsaha.queue.length > 1) {
					uploadSalinanSuratIjinUsaha.queue.shift();
				}
				angular.forEach(uploadSalinanSuratIjinUsaha.queue, function(item) {
					$scope.tampilSSIU = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadSalinanSuratIjinUsaha);

		$scope.BuktiFisikPerusahaan = {};
		$scope.BuktiFisikPerusahaan.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen5 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.BuktiFisikPerusahaan.tanggalTerbitStatus = true;
		};

		$scope.BuktiFisikPerusahaan.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen5 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.BuktiFisikPerusahaan.tanggalBerakhirStatus = true;
		};

		$scope.changeBuktiFisikPerusahaan = function () {
			$scope.BuktiFisikPerusahaan.tanggalBerakhir = null;
		}

		var uploadBuktiFisikPerusahaan = $scope.uploadBuktiFisikPerusahaan = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadBuktiFisikPerusahaan.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadBuktiFisikPerusahaan.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadBuktiFisikPerusahaan.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadBuktiFisikPerusahaan.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilBFP = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Bukti Fisik Perusahaan Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadBuktiFisikPerusahaan.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeBFP = progress;
			if($scope.progressTimeBFP == 100) {
				$scope.tampilBFP = false;
			}
        };
		
		$scope.uploadBFPDokumen = function() {
			if(uploadBuktiFisikPerusahaan.queue != undefined && uploadBuktiFisikPerusahaan.queue.length > 0) {
				if(uploadBuktiFisikPerusahaan.queue.length > 1) {
					uploadBuktiFisikPerusahaan.queue.shift();
				}
				angular.forEach(uploadBuktiFisikPerusahaan.queue, function(item) {
					$scope.tampilBFP = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadBuktiFisikPerusahaan);

		$scope.DokumenQuality = {};
		$scope.DokumenQuality.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen6 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.DokumenQuality.tanggalTerbitStatus = true;
		};

		$scope.DokumenQuality.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen6 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.DokumenQuality.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenQuality = function () {
			$scope.DokumenQuality.tanggalBerakhir = null;
		}

		var uploadDokumenQuality = $scope.uploadDokumenQuality = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenQuality.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadDokumenQuality.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenQuality.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenQuality.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilDQ = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Quality Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenQuality.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeDQ = progress;
			if($scope.progressTimeDQ == 100) {
				$scope.tampilDQ = false;
			}
        };
		
		$scope.uploadQualityDokumen = function() {
			if(uploadDokumenQuality.queue != undefined && uploadDokumenQuality.queue.length > 0) {
				if(uploadDokumenQuality.queue.length > 1) {
					uploadDokumenQuality.queue.shift();
				}
				angular.forEach(uploadDokumenQuality.queue, function(item) {
					$scope.tampilDQ = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenQuality);

		$scope.DokumenTeknik = {};
		$scope.DokumenTeknik.tanggalTerbitStatus = false;
		$scope.tanggalTerbitOpen7 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.DokumenTeknik.tanggalTerbitStatus = true;
		};

		$scope.DokumenTeknik.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen7 = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.DokumenTeknik.tanggalBerakhirStatus = true;
		};

		$scope.changeDokumenTeknik = function () {
			$scope.DokumenTeknik.tanggalBerakhir = null;
		}

		var uploadDokumenTeknik = $scope.uploadDokumenTeknik = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenTeknik.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		/*uploadDokumenTeknik.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
			$scope.validasiUpload(fileItem);
        };*/

        uploadDokumenTeknik.onCompleteItem = function (fileItem, response, status, headers) {
			//console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		uploadDokumenTeknik.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
			$scope.tampilDT = false;
            toaster.pop('error', 'Kesalahan', 'Dokumen Teknik Gagal Di upload, Silahkan coba lagi');
        };
		
        uploadDokumenTeknik.onProgressItem = function (fileItem, progress) {
			$scope.progressTimeDT = progress;
			if($scope.progressTimeDT == 100) {
				$scope.tampilDT = false;
			}
        };
		
		$scope.uploadTeknikDokumen = function() {
			if(uploadDokumenTeknik.queue != undefined && uploadDokumenTeknik.queue.length > 0) {
				if(uploadDokumenTeknik.queue.length > 1) {
					uploadDokumenTeknik.queue.shift();
				}
				angular.forEach(uploadDokumenTeknik.queue, function(item) {
					$scope.tampilDT = true;
	        		item.upload();
	            });
			}
		}

		//rekapUpload(uploadDokumenTeknik);

		//validasi data dokumen
		$scope.nextDataPeralatan = function () {
			
			var validationTab05 = false;
			form.VendorRegistrasiFormError = false;
			form.VendorRegistrasiFormNotUploaded = false;
			form.SKBError = false;
			form.SKBNotUploaded = false;
			form.SalinanAktePendirianError = false;	
			form.SalinanAktePendirianNotUploaded = false;
			form.SalinanSuratIjinUsahaError = false;	
			form.SalinanSuratIjinUsahaNotUploaded = false;
			form.DokumenQualityError = false;	
			form.DokumenQualityNotUploaded = false;
			form.SalinanTandaDaftarError = false;
			form.SalinanTandaDaftarNotUploaded = false;
			form.dokumenPKSError = false;
			form.dokumenPKSNotUploaded = false;
			form.dokumenSPRError = false;
			form.dokumenSPRNotUploaded = false;
			form.dokumenSPBError = false;
			form.dokumenSPBNotUploaded = false;
			form.dokumenSKDError = false;
			form.dokumenSKDNotUploaded = false;
			form.buktiFisikPerusahaanError = false;
			form.dokumenBFPNotUploaded = false;
			form.DokumenTeknikError = false;
			form.dokumenTeknikNotUploaded = false;
			
			
			if (Boolean($scope.VendorRegistrasiForm.namaDokumen) || Boolean($scope.VendorRegistrasiForm.tanggalTerbit) ||
					Boolean($scope.VendorRegistrasiForm.tanggalBerakhir)|| uploadVendorRegistrasiForm.queue.length > 0) {
				if(!Boolean($scope.VendorRegistrasiForm.namaDokumen)|| !Boolean($scope.VendorRegistrasiForm.tanggalTerbit)||
					!Boolean($scope.VendorRegistrasiForm.tanggalBerakhir)|| uploadVendorRegistrasiForm.queue.length <= 0){
					form.VendorRegistrasiFormError = true;	
				}else if(uploadVendorRegistrasiForm.queue[0].isUploaded == false){
					form.VendorRegistrasiFormNotUploaded = true;
				}
				
			}
			
			if (Boolean($scope.SKB.namaDokumen) || Boolean($scope.SKB.tanggalTerbit) ||
					Boolean($scope.SKB.tanggalBerakhir)|| uploadSKB.queue.length > 0) {
				if(!Boolean($scope.SKB.namaDokumen)|| !Boolean($scope.SKB.tanggalTerbit)||
					!Boolean($scope.SKB.tanggalBerakhir)|| uploadSKB.queue.length <= 0){
					form.SKBError = true;	
				}else if(uploadSKB.queue[0].isUploaded == false){
					form.SKBNotUploaded = true;
				}
				
			}
			
			if (Boolean($scope.SalinanAktePendirian.namaDokumen)|| Boolean($scope.SalinanAktePendirian.tanggalTerbit)||
					Boolean($scope.SalinanAktePendirian.tanggalBerakhir)|| uploadSalinanAktePendirian.queue.length > 0) {
					if(!Boolean($scope.SalinanAktePendirian.namaDokumen) ||!Boolean($scope.SalinanAktePendirian.tanggalTerbit)||
					!Boolean($scope.SalinanAktePendirian.tanggalBerakhir)|| uploadSalinanAktePendirian.queue.length <= 0){
						form.SalinanAktePendirianError = true;	
					}else if(uploadSalinanAktePendirian.queue[0].isUploaded == false){
						form.SalinanAktePendirianNotUploaded = true;
					}
					
				}
			
			if (Boolean($scope.SalinanSuratIjinUsaha.namaDokumen)|| Boolean($scope.SalinanSuratIjinUsaha.tanggalTerbit)||
					Boolean($scope.SalinanSuratIjinUsaha.tanggalBerakhir)|| uploadSalinanSuratIjinUsaha.queue.length > 0) {
					if(!Boolean($scope.SalinanSuratIjinUsaha.namaDokumen) || !Boolean($scope.SalinanSuratIjinUsaha.tanggalTerbit)||
					!Boolean($scope.SalinanSuratIjinUsaha.tanggalBerakhir) || uploadSalinanSuratIjinUsaha.queue.length <= 0){
						form.SalinanSuratIjinUsahaError = true;	
					}else if(uploadSalinanSuratIjinUsaha.queue[0].isUploaded == false){
						form.SalinanSuratIjinUsahaNotUploaded = true;
					}
					
				}
			
			if (Boolean($scope.DokumenQuality.namaDokumen)|| Boolean($scope.DokumenQuality.tanggalTerbit)||
					Boolean($scope.DokumenQuality.tanggalBerakhir)|| uploadDokumenQuality.queue.length > 0) {
					if(!Boolean($scope.DokumenQuality.namaDokumen)|| !Boolean($scope.DokumenQuality.tanggalTerbit)||
							!Boolean($scope.DokumenQuality.tanggalBerakhir)|| uploadDokumenQuality.queue.length <= 0){
						form.DokumenQualityError = true;	
					}else if(uploadDokumenQuality.queue[0].isUploaded == false){
						form.DokumenQualityNotUploaded = true;
					}
					
				}
			
			if(form.isCompany == false){
				form.SalinanTandaDaftarError = false;
				form.SalinanTandaDaftarNotUploaded = false;
			}else{
				if($scope.SalinanTandaDaftar.namaDokumen == undefined || $scope.SalinanTandaDaftar.tanggalTerbit == undefined ||
						$scope.SalinanTandaDaftar.tanggalBerakhir == undefined || $scope.SalinanTandaDaftar.namaDokumen == null || $scope.SalinanTandaDaftar.tanggalTerbit == null ||
						$scope.SalinanTandaDaftar.tanggalBerakhir == null || $scope.SalinanTandaDaftar.namaDokumen == "" || $scope.SalinanTandaDaftar.tanggalTerbit == "" ||
						$scope.SalinanTandaDaftar.tanggalBerakhir == "" || uploadSalinanTandaDaftar.queue.length <= 0){
							form.SalinanTandaDaftarError = true;	
						}else if(uploadSalinanTandaDaftar.queue[0].isUploaded == false){
							form.SalinanTandaDaftarNotUploaded = true;
						}
			}
			
			if($scope.bukanPKS == true ){
				form.dokumenPKSError = false;
				form.dokumenPKSNotUploaded = false;
			}else{
				if($scope.dokumenPKS.namaDokumen == undefined || $scope.dokumenPKS.tanggalTerbit == undefined ||
						$scope.dokumenPKS.tanggalBerakhir == undefined || $scope.dokumenPKS.namaDokumen == null || $scope.dokumenPKS.tanggalTerbit == null ||
						$scope.dokumenPKS.tanggalBerakhir == null || $scope.dokumenPKS.namaDokumen == "" || $scope.dokumenPKS.tanggalTerbit == "" ||
						$scope.dokumenPKS.tanggalBerakhir == "" || uploadDokumenPKS.queue.length <= 0){
							form.dokumenPKSError = true;	
						}else if(uploadDokumenPKS.queue[0].isUploaded == false){
							form.dokumenPKSNotUploaded = true;
						}
			}
			
			if($scope.bukanPKS == false ){
				form.dokumenSPRError = false;
				form.dokumenSPRNotUploaded = false;
			}else{
				if($scope.dokumenSPR.namaDokumen == undefined || $scope.dokumenSPR.tanggalTerbit == undefined ||
						$scope.dokumenSPR.tanggalBerakhir == undefined || $scope.dokumenSPR.namaDokumen == null || $scope.dokumenSPR.tanggalTerbit == null ||
						$scope.dokumenSPR.tanggalBerakhir == null || $scope.dokumenSPR.namaDokumen == "" || $scope.dokumenSPR.tanggalTerbit == "" ||
						$scope.dokumenSPR.tanggalBerakhir == "" || uploadDokumenSPR.queue.length <= 0){
							form.dokumenSPRError = true;	
						}else if(uploadDokumenSPR.queue[0].isUploaded == false){
							form.dokumenSPRNotUploaded = true;
						}
			}
			
			if($scope.bukanPKS == false){
				form.dokumenSPBError = false;
				form.dokumenSPBNotUploaded = false;
			}else{
				if(form.isCompany==false){
				if($scope.dokumenSPB.namaDokumen == undefined || $scope.dokumenSPB.tanggalTerbit == undefined ||
						$scope.dokumenSPB.tanggalBerakhir == undefined || $scope.dokumenSPB.namaDokumen == null || $scope.dokumenSPB.tanggalTerbit == null ||
						$scope.dokumenSPB.tanggalBerakhir == null || $scope.dokumenSPB.namaDokumen == "" || $scope.dokumenSPB.tanggalTerbit == "" ||
						$scope.dokumenSPB.tanggalBerakhir == "" || uploadDokumenSPB.queue.length <= 0){
							form.dokumenSPBError = true;	
						}else if(uploadDokumenSPB.queue[0].isUploaded == false){
							form.dokumenSPBNotUploaded = true;
						}
				}
			}
			
			if(form.isCompany == false){
				form.dokumenSKDError = false;
				form.dokumenSKDNotUploaded = false;
			}else{
				if($scope.dokumenSKD.namaDokumen == undefined || $scope.dokumenSKD.tanggalTerbit == undefined ||
						$scope.dokumenSKD.tanggalBerakhir == undefined || $scope.dokumenSKD.namaDokumen == null || $scope.dokumenSKD.tanggalTerbit == null ||
						$scope.dokumenSKD.tanggalBerakhir == null || $scope.dokumenSKD.namaDokumen == "" || $scope.dokumenSKD.tanggalTerbit == "" ||
						$scope.dokumenSKD.tanggalBerakhir == "" || uploadDokumenSKD.queue.length <= 0){
							form.dokumenSKDError = true;	
						}else if(uploadDokumenSKD.queue[0].isUploaded == false){
							form.dokumenSKDNotUploaded = true;
						}
			}
			
			if($scope.BuktiFisikPerusahaan.namaDokumen == undefined || $scope.BuktiFisikPerusahaan.tanggalTerbit == undefined ||
					$scope.BuktiFisikPerusahaan.tanggalBerakhir == undefined || $scope.BuktiFisikPerusahaan.namaDokumen == null || $scope.BuktiFisikPerusahaan.tanggalTerbit == null ||
					$scope.BuktiFisikPerusahaan.tanggalBerakhir == null || $scope.BuktiFisikPerusahaan.namaDokumen == "" || $scope.BuktiFisikPerusahaan.tanggalTerbit == "" ||
					$scope.BuktiFisikPerusahaan.tanggalBerakhir == "" || uploadBuktiFisikPerusahaan.queue.length <= 0){
						form.buktiFisikPerusahaanError = true;
					}else if(uploadBuktiFisikPerusahaan.queue[0].isUploaded == false){
						form.dokumenBFPNotUploaded = true;
					}
			
			if($scope.DokumenTeknik.namaDokumen == undefined || $scope.DokumenTeknik.tanggalTerbit == undefined ||
					$scope.DokumenTeknik.tanggalBerakhir == undefined || $scope.DokumenTeknik.namaDokumen == null || $scope.DokumenTeknik.tanggalTerbit == null ||
					$scope.DokumenTeknik.tanggalBerakhir == null  || $scope.DokumenTeknik.namaDokumen == "" || $scope.DokumenTeknik.tanggalTerbit == "" ||
					$scope.DokumenTeknik.tanggalBerakhir == "" || uploadDokumenTeknik.queue.length <= 0){
						form.DokumenTeknikError = true;
						
					}else if(uploadDokumenTeknik.queue[0].isUploaded == false){
						form.dokumenTeknikNotUploaded = true;
					}

			
			
			
			if (form.VendorRegistrasiFormError == false &&
			form.VendorRegistrasiFormNotUploaded == false &&
			form.SalinanAktePendirianError == false &&	
			form.SalinanAktePendirianNotUploaded == false &&
			form.SalinanSuratIjinUsahaError == false &&	
			form.SalinanSuratIjinUsahaNotUploaded == false &&
			form.DokumenQualityError == false &&	
			form.DokumenQualityNotUploaded == false &&
			form.SalinanTandaDaftarError == false &&
			form.SalinanTandaDaftarNotUploaded == false &&
			form.dokumenPKSError == false &&
			form.dokumenPKSNotUploaded == false &&
			form.dokumenSPRError == false &&
			form.dokumenSPRNotUploaded == false &&
			form.dokumenSPBError == false &&
			form.dokumenSPBNotUploaded == false &&
			form.dokumenSKDError == false &&
			form.dokumenSKDNotUploaded == false &&
			form.buktiFisikPerusahaanError == false &&
			form.dokumenBFPNotUploaded == false &&
			form.DokumenTeknikError == false &&
			form.dokumenTeknikNotUploaded == false) {
				
				validationTab05 = true ;
			}
			
			return validationTab05;
			//return true; //skip mandatory tab
		}

		/* ---------------------------------------------------- DATA PERALATAN ------------------------------------------------- */

		form.dataPeralatanList = [];
		$scope.tablePeralatanVendor = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataPeralatanList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataPeralatanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		$scope.addDataPeralatan = function () {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/peralatanvendor.html',
				controller: 'PeralatanVendorModalController',
				size: 'lg',
				resolve: {
					dataPeralatan: function () {
						return {};
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataPeralatan) {
				if (dataPeralatan != undefined && dataPeralatan !== null) {
					form.dataPeralatanList.push(dataPeralatan);
					dataPeralatan.fileNameBuktiKepemilikan = fileNameUpload(dataPeralatan.uploadDokumenBuktiKepemilikan);
					$scope.tablePeralatanVendor.reload();
				}
			});
		}

		$scope.editDataPeralatan = function (index, dataPeralatan) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/peralatanvendor.html',
				controller: 'PeralatanVendorModalController',
				size: 'lg',
				resolve: {
					dataPeralatan: function () {
						return dataPeralatan;
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataPeralatan) {
				if (dataPeralatan != undefined && dataPeralatan !== null) {
					dataPeralatan.fileNameBuktiKepemilikan = fileNameUpload(dataPeralatan.uploadDokumenBuktiKepemilikan);
					form.dataPeralatanList.splice(index, 1, dataPeralatan);
					$scope.tablePeralatanVendor.reload();
				}
			});
		}

		$scope.removeDataPeralatan = function (index, dataPeralatan) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/alertModal.html',
				controller: 'DeleteDataModalRegistrasiController',
				size: 'sm',
				resolve: {
					nameData: function () {
						return dataPeralatan.jenis;
					}
				}
			});
			segmentasimodalinstance.result.then(function () {
				form.dataPeralatanList.splice(index, 1);
				$scope.tablePeralatanVendor.reload();
			});
		}

		$scope.nextDataKeuangan = function () {
			var validationTab06 = false;

			//tab peralatan boleh kosong
			//return validationTab06;
			return true; //skip mandatory tab
		}

		/* ---------------------------------------------------- DATA KEUANGAN -------------------------------------------------- */

		form.dataKeuanganList = [];
		$scope.tableKeuanganVendor = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataKeuanganList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataKeuanganList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		/* $scope.dataKeuangan.totalAktivaLancar = $scope.dataKeuangan.kas + $scope.dataKeuangan.bank + $scope.dataKeuangan.totalPiutang + $scope.dataKeuangan.totalPiutang + $scope.dataKeuangan.hutangLainnya;*/

		$scope.addDataKeuangan = function () {
			var tambah = 1;
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/keuanganvendor.html',
				controller: 'KeuanganVendorModalController',
				/*windowClass: 'app-modal-window',*/
				size : 'lg',
				resolve: {
					dataKeuangan: function () {
						return {};
					},
					namaEvent: function () {
						return 'tambah';
					}
				}
			});

			segmentasimodalinstance.result.then(function (dataKeuangan) {
				if (dataKeuangan != undefined && dataKeuangan !== null) {
					form.dataKeuanganList.push(dataKeuangan);
					$scope.tableKeuanganVendor.reload();
				}
			});
		}

		$scope.editDataKeuangan = function (index, dataKeuangan) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/keuanganvendor.html',
				controller: 'KeuanganVendorModalController',
				windowClass: 'app-modal-window',
				resolve: {
					dataKeuangan: function () {
						return dataKeuangan;
					},
					namaEvent: function () {
						return 'rubah';
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataKeuangan) {
				if (dataKeuangan != undefined && dataKeuangan !== null) {
					form.dataKeuanganList.splice(index, 1, dataKeuangan);
					$scope.tableKeuanganVendor.reload();
				}
			});
		}

		$scope.removeDataKeuangan = function (index, dataKeuangan) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/alertModal.html',
				controller: 'DeleteDataModalRegistrasiController',
				size: 'sm',
				resolve: {
					nameData: function () {
						return dataKeuangan.jenis;
					},
					namaEvent: function () {
						return 'hapus';
					}
				}
			});
			segmentasimodalinstance.result.then(function () {
				form.dataKeuanganList.splice(index, 1);
				$scope.tableKeuanganVendor.reload();
			});
		}

		$scope.nextDataPengalaman = function () {
			var validationTab07 = false;

			/*if (form.dataKeuanganList.length > 0)
				validationTab07 = true;
			else
				toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Keuangan');*/

			//tab keuangan boleh kosong
			//return validationTab07;
			return true;//skip mandatory tab
		}

		/* ---------------------------------------------------- DATA PENGALAMAN ------------------------------------------------ */


		$scope.checked = function (pengalamanStatus) {
			if (pengalamanStatus) {
				$scope.tanpaPengalaman = true;
			} else {
				$scope.tanpaPengalaman = false;
			}
		
		}


		form.dataPengalamanPekerjaanList = [];
		$scope.tablePengalamanPekerjaan = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataPengalamanPekerjaanList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataPengalamanPekerjaanList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		form.dataMitraList = [];
		$scope.tableDataMitra = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataMitraList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataMitraList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		form.dataWorkingProgressList = [];
		$scope.tableWorkingProgress = new ngTableParams({
			page: 1, // show first page
			count: 5 // count per page
		}, {
			counts: [], // hide page count
			total: form.dataWorkingProgressList.length, // length of data4
			getData: function ($defer, params) {
				$defer.resolve(form.dataWorkingProgressList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});

		var fileNameUpload = function (data) {
			var fileName = '';
			angular.forEach(data, function (item) {
				//item.upload();

				fileName = item.file.name;
			});
			return fileName;
		}

		$scope.addDataPengalaman = function (tipe) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/pengalamanpekerjaanvendor.html',
				controller: 'RegPengalamanPekerjaanModalController',
				size: 'lg',
				resolve: {
					judulModal: function () {
						if (tipe === 'PELANGGAN') {
							return "Pengalaman Pekerjaan";
						} else if (tipe === 'MITRA') {
							return "Pengalaman Mitra Kerja";
						} else {
							return "Pekerjaan Sedang Berjalan";
						}
					},
					namaModal: function () {
						if (tipe === 'MITRA') {
							return "Nama Mitra";
						} else {
							return "Nama Pekerjaan";
						}
					},
					dataPengalaman: function () {
						return {};
					},
					bidangUsahaList: function () {
						return $scope.bidangUsahaList;
					},
					mataUangList: function () {
						return $scope.mataUangList;
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataPengalaman) {
				if (dataPengalaman != undefined && dataPengalaman !== null) {
					if (tipe === 'PELANGGAN') {
						dataPengalaman.tipePengalaman = tipe;
						dataPengalaman.fileName = fileNameUpload(dataPengalaman.uploadBukti);
						form.dataPengalamanPekerjaanList.push(dataPengalaman);
						$scope.tablePengalamanPekerjaan.reload();
					} else if (tipe === 'MITRA') {
						dataPengalaman.tipePengalaman = tipe;
						dataPengalaman.fileName = fileNameUpload(dataPengalaman.uploadBukti);
						form.dataMitraList.push(dataPengalaman);
						$scope.tableDataMitra.reload();
					} else {
						dataPengalaman.tipePengalaman = tipe;
						dataPengalaman.fileName = fileNameUpload(dataPengalaman.uploadBukti);
						form.dataWorkingProgressList.push(dataPengalaman);
						$scope.tableWorkingProgress.reload();
					}
				}
			});
		}

		$scope.editDataPengalaman = function (index, dataPengalaman, tipe) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/pengalamanpekerjaanvendor.html',
				controller: 'RegPengalamanPekerjaanModalController',
				size: 'lg',
				resolve: {
					judulModal: function () {
						if (tipe === 'PELANGGAN') {
							return "Pengalaman Pekerjaan";
						} else if (tipe === 'MITRA') {
							return "Pengalaman Mitra Kerja";
						} else {
							return "Pekerjaan Sedang Berjalan";
						}
					},
					namaModal: function () {
						if (tipe === 'MITRA') {
							return "Nama Mitra";
						} else {
							return "Nama Pekerjaan";
						}
					},
					dataPengalaman: function () {
						return dataPengalaman;
					},
					bidangUsahaList: function () {
						return $scope.bidangUsahaList;
					},
					mataUangList: function () {
						return $scope.mataUangList;
					}
				}
			});
			segmentasimodalinstance.result.then(function (dataPengalaman) {
				if (dataPengalaman != undefined && dataPengalaman !== null) {
					dataPengalaman.fileName = fileNameUpload(dataPengalaman.uploadBukti);
					if (tipe === 'PELANGGAN') {
						form.dataPengalamanPekerjaanList.splice(index, 1, dataPengalaman);
						$scope.tablePengalamanPekerjaan.reload();
					} else if (tipe === 'MITRA') {
						form.dataMitraList.splice(index, 1, dataPengalaman);
						$scope.tableDataMitra.reload();
					} else {
						form.dataWorkingProgressList.splice(index, 1, dataPengalaman);
						$scope.tableWorkingProgress.reload();
					}
				}
			});
		}

		$scope.removeDataPengalaman = function (index, dataPengalaman, tipe) {
			var segmentasimodalinstance = $modal.open({
				templateUrl: '/alertModal.html',
				controller: 'DeleteDataModalRegistrasiController',
				size: 'sm',
				resolve: {
					nameData: function () {
						return dataPengalaman.jenis;
					}
				}
			});
			segmentasimodalinstance.result.then(function () {
				if (tipe === 'PELANGGAN') {
					form.dataPengalamanPekerjaanList.splice(index, 1);
					$scope.tablePengalamanPekerjaan.reload();
				} else if (tipe === 'MITRA') {
					form.dataMitraList.splice(index, 1);
					$scope.tableDataMitra.reload();
				} else {
					form.dataWorkingProgressList.splice(index, 1);
					$scope.tableWorkingProgress.reload();
				}
			});
		}
		
		$scope.validasiDataPengalaman = function () {
			if(!$scope.hideSaveBtn){
        		$scope.hideSaveBtn = true;
				var dataPekerjaan = false;
				var dataMitra = false;
				var dataWP = false;
				if ($scope.tanpaPengalaman == false) {
					if (form.dataPengalamanPekerjaanList.length > 0)
						dataPekerjaan = true;
					else{
						toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Pengalaman Pekerjaan');
						$scope.hideSaveBtn = false;
					}
					if (form.dataMitraList.length > 0)
						dataMitra = true;
					else{
						toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Mitra');
						$scope.hideSaveBtn = false;
					}
					if (form.dataWorkingProgressList.length > 0)
						dataWP = true;
					else{
						toaster.pop('error', 'Kesalahan', 'Silahkan Tambah Data Working Progress');
						$scope.hideSaveBtn = false;
					}
					if (dataPekerjaan == true && dataMitra == true && dataWP == true) {
						//var kapca = grecaptcha.getResponse();
	
						//    			    if(kapca.length == 0) {
						//    			    	toaster.pop('warning', 'Cotto-Matte', 'Kode Captcha tidak boleh ko-cong');
						//    			        return
						//    			    } else if(kapca.length != 0) {
						validasiUserUlang(form.userId);
						//    			    }
					}
				} else {
					validasiUserUlang(form.userId);
				}
			}
		}
		
		var validasiUserUlang = function (userId) {
				$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/user/get-by-username/' + userId, {
					ignoreLoadingBar: true
				}).success(function (data, status, headers, config) {
					if (data.length > 0) {
						form.tabActiveStatus.tabDataPengalaman = false;
						form.tabActiveStatus.tabDataKeuangan = false;
						form.tabActiveStatus.tabPeralatan = false;
						form.tabActiveStatus.tabDataDokumentasi = false;
						form.tabActiveStatus.tabDataSegmentasi = false;
						form.tabActiveStatus.tabDataBank = false;
						form.tabActiveStatus.tabDataPerusahaan = false;
						form.tabActiveStatus.tabDataLogin = true;
						toaster.pop('error', 'Kesalahan', 'Email sudah pernah digunakan!!');
						document.getElementsByName("userId")[0].focus();
						$scope.hideSaveBtn = false;
					} else {
						RequestService.modalConfirmation().then(function(result) {
							
								$scope.saveDataRegistrasi();
					
						}, function(close){
							$scope.hideSaveBtn = false;
						});
						
					}
				}).error(function (data, status, headers, config) {
					$scope.hideSaveBtn = false;
					toaster.pop('error', 'Kesalahan', 'Terjadi Kesalahan pada System!!');
				});
			}
			/* ---------------------------------------------------- SIMPAN DATA REGISTRASI ----------------------------------------- */

		$scope.saveDataRegistrasi = function () {
			var formDataUser = {
				roleId: 2
			};
		
				
				if (form.userId !== undefined && form.userId.length > 0) {
					
					formDataUser.username = form.userId;
					formDataUser.email = form.userId;
				}

				if (form.namaPengguna !== undefined && form.namaPengguna.length > 0) {
					formDataUser.namaPengguna = form.namaPengguna;
				}

				if (form.password !== undefined && form.password.length > 0) {
					formDataUser.password = form.password;
				}
				
				var data = {}
				
				data.user = {
					username : form.userId,
					email : form.userId,
					namaPengguna : form.namaPengguna,
					password : form.password
				}
				
				data.vendor = {
					nama 			: form.NamaPerusahaan,
					alamat 			: form.alamatPerusahaan,
					nomorTelpon 	: form.TelponPerusahaan,
					email 			: form.EmailPerusahaan,
					npwp 			: form.NPWPPerusahaan,
					penanggungJawab : form.NamaPerusahaan,
					deskripsi 		: form.deskripsi,
					kota			: form.kotaPerusahaan.lokasi_nama,
					afco			: {id:form.unitTerdaftar.id},
					provinsi        : form.provinsiPerusahaan.lokasi_nama,
					isPKS			: form.isPKS
				}
				
				if (uploadVendorLogo !== undefined && uploadVendorLogo.queue.length > 0) {
						if (uploadVendorLogo.queue[0].isUploaded) {
							data.vendor.logoImage = uploadVendorLogo.queue[0].file.name;
							data.vendor.logoImageSize = uploadVendorLogo.queue[0].file.size;
						}
					}
				
				if (uploadVendorHeadImg !== undefined && uploadVendorHeadImg.queue.length > 0) {
					
						if(uploadVendorHeadImg.queue[0].isUploaded) {
														
							data.vendor.backgroundImage = uploadVendorHeadImg.queue[0].file.name;
							data.vendor.backgroundImageSize = uploadVendorHeadImg.queue[0].file.size;
						}
				
				}
				
				
				data.vendorProfile = {
					jenisPajakPerusahaan 	: form.pilihPKP.name,
					nomorPKP				: form.nomorPKP,
					title					: form.title.title,
					kualifikasiVendor		: {id:form.kualifikasiVendor.id},
					unitTerdaftar			: form.unitTerdaftar.nama,
					bussinessArea			: form.bussinessArea.id,
					tipePerusahaan			: form.jenisPerusahaan.nama,
					namaPerusahaan			: form.NamaPerusahaan,
					npwpPerusahaan			: form.NPWPPerusahaan,
					namaNPWP				: form.namaNPWP,
					alamatNPWP				: form.alamatNPWP,
					provinsiNPWP			: form.provinsiNPWP.lokasi_nama,
					kotaNPWP				: form.kotaNPWP.lokasi_nama,
					namaSingkatan 			: form.NamaSingkatan,
					alamat					: form.alamatPerusahaan,
					kota					: form.kotaPerusahaan.lokasi_nama,
					kodePos					: form.kodeposPerusahaan,
					poBox					: form.poboxPerusahaan,
					provinsi				: form.provinsiPerusahaan.lokasi_nama,
					telephone				: form.TeleponPerusahaan,
					faksimile				: form.NoFaxPerusahaan,
					email					: form.EmailPerusahaan,
					website					: form.WebsitePerusahaan,
					namaContactPerson 		: form.NamaContactPerson,
					hpContactPerson   		: form.NoHPContactPerson,
					emailContactPerson 		: form.EmailContactPerson,
					ktpContactPerson 		: form.NoKTPContactPerson,
					noKK				 	: form.NoKKContactPerson,
					noAktaPendirian			: form.noAktaPendirian,
					jumlahKaryawan			: form.JumlahKaryawan,
					tanggalBerdiri 			: form.TanggalBerdiri
				}
					
				
				var vendorPICList = []
				if(form.penanggungJawabList !== undefined && form.penanggungJawabList.length > 0) {
					angular.forEach(form.penanggungJawabList, function (pic, index) {
						if (typeof pic.jabatan !== 'undefined') {
							var vendorPIC = {
								nama 	: pic.nama,
								email 	: pic.email,
								jabatan : pic.jabatan
							}
							
							vendorPICList.push(vendorPIC);
						}
					});
				}
				
				
				data.vendorPICList=vendorPICList;
				
				var bankVendorList = [];
				if(form.penanggungJawabList !== undefined && form.dataBankList.length > 0) {
					angular.forEach(form.dataBankList, function(dataBank, index){
	        			var bank = {
	        				alamatBank : dataBank.alamatBank,
	        				cabangBank : dataBank.cabangBank,
	        				kota : dataBank.kota,
	        				namaBank : dataBank.namaBank,
	        				namaNasabah : dataBank.namaNasabah,
	        				nomorRekening: dataBank.nomorRekening,
	        				mataUang: {id:dataBank.mataUang.id}
	        			}
	        			
	        			if(dataBank.negara != undefined){
		        			if(dataBank.negara.nama != undefined) {
		        				bank.negara = dataBank.negara.nama
		        			} else {
		        				bank.negara = dataBank.negara
		        			}
	        			}
	        			bankVendorList.push(bank);
	        		});
				}
				
				
				data.bankVendorList=bankVendorList;
				
				var segmentasiVendorList = [];
				if(form.dataSegmentasiList !== undefined && form.dataSegmentasiList.length > 0) {
					
					angular.forEach(form.dataSegmentasiList, function (dataSegmentasi, index) {
	
						var tglMulai = new Date(dataSegmentasi.tanggalMulai);
						var tglAkhir = new Date(dataSegmentasi.tanggalBerakhir);
						
						if (tglMulai == 'Invalid Date') {
							tglMulai = new Date();
						}
						if (tglAkhir == 'Invalid Date') {
							tglAkhir = new Date();
						}
	
						var segmentasiVendor = {
							asosiasi: dataSegmentasi.asosiasi.nama,
							nomor: dataSegmentasi.nomor,
							subBidangUsaha: {id:dataSegmentasi.subBidangUsaha.id},
							email: form.userId,
							tanggalMulai: tglMulai,
							tanggalBerakhir: tglAkhir
						}
						
						segmentasiVendorList.push(segmentasiVendor)
	
					});
				}
				
				
				data.segmentasiVendorList=segmentasiVendorList;
				
				var peralatanVendorList = [];
				if (form.dataPeralatanList !== undefined && form.dataPeralatanList.length > 0) {
					angular.forEach(form.dataPeralatanList, function (dataPeralatan, index) {
						
						var peralatanVendor = {
							kondisi 			: dataPeralatan.kondisi,
							buktiKepemilikan 	: dataPeralatan.buktiKepemilikan,
							jenis 				: dataPeralatan.jenis,
							kapasitas			: dataPeralatan.kapasitas,
							lokasi				: dataPeralatan.lokasi,
							merk				: dataPeralatan.merk,
							tahunPembuatan		: dataPeralatan.tahunPembuatan
						}
						

						if (dataPeralatan.uploadDokumenBuktiKepemilikan !== undefined && dataPeralatan.uploadDokumenBuktiKepemilikan.length > 0) {
							angular.forEach(dataPeralatan.uploadDokumenBuktiKepemilikan, function (item) {
								
								if (item.isUploaded) {
									peralatanVendor.fileNameBuktiKepemilikan = item.realFileName;
									peralatanVendor.pathFileBuktiKepemilikan = item.file.name;
									peralatanVendor.fileSizeBuktiKepemilikan = item.file.size;
								}
							});
						}
						if (dataPeralatan.uploadGambarPeralatanAsset !== undefined && dataPeralatan.uploadGambarPeralatanAsset.length > 0) {
							angular.forEach(dataPeralatan.uploadGambarPeralatanAsset, function (item) {
								//console.log("Gambar Peralatan Asset IS UPLOADED = " + item.isUploaded);
								if (item.isUploaded) {
									peralatanVendor.fileNameGambarPeralatan = item.realFileName
									peralatanVendor.pathFileGambarPeralatan = item.file.name;
									peralatanVendor.fileSizeGambarPeralatan = item.file.size;
								} 
							});
						}
						
						peralatanVendorList.push(peralatanVendor);
					});
				}
				
				//console.log(peralatanVendorList);
				data.peralatanVendorList=peralatanVendorList;
				
				var keuanganVendorList = [];
				if (form.dataKeuanganList !== undefined && form.dataKeuanganList.length > 0) {
					angular.forEach(form.dataKeuanganList, function (data, index) {
						
						var temp={
								tanggalAudit:new Date(data.tanggalAudit),
								kas : data.kas,
								bank: data.bank,
								totalPiutang: data.totalPiutang,
								persediaanBarang: data.persediaanBarang,
								pekerjaanDalamProses: data.pekerjaanDalamProses,
								totalAktivaLancar: data.totalAktivaLancar,
								peralatanDanMesin: data.peralatanDanMesin,
								inventaris: data.inventaris,
								gedungGedung: data.gedungGedung,
								totalAktivaTetap: data.totalAktivaTetap,
								aktivaLainnya:data.aktivaLainnya,
								totalAktiva:data.totalAktiva,
								hutangDagang:data.hutangDagang,
								hutangPajak:data.hutangPajak,
								hutangLainnya:data.hutangLainnya,
								totalHutangJangkaPendek:data.totalHutangJangkaPendek,
								hutangJangkaPanjang:data.hutangJangkaPanjang,
								kekayaanBersih:data.kekayaanBersih,
								totalPasiva:data.totalPasiva
						
						}
						keuanganVendorList.push(temp);
					});
				}
				
				//console.log(keuanganVendorList);
				data.keuanganVendorList=keuanganVendorList;
				
				var pengalamanVendorList = [];
				if (form.dataPengalamanPekerjaanList !== undefined && form.dataPengalamanPekerjaanList.length > 0) {
					angular.forEach(form.dataPengalamanPekerjaanList, function (data, index) {
						var dataPengalamanPekerjan = {
							mulaiKerjasama 	: new Date(data.mulaiKerjasama),
							bidangUsaha 	: data.bidangUsaha.nama,
							mataUang 		: {id:data.mataUang.id},
							lokasiPekerjaan : data.lokasiPekerjaan,
							namaPekerjaan 	: data.namaPekerjaan,
							nilaiKontrak	: data.nilaiKontrak,
							tipePengalaman 	: data.tipePengalaman
							
						}

						if (data.uploadBukti !== undefined && data.uploadBukti.length > 0) {
							angular.forEach(data.uploadBukti, function (item) {
								//console.log("Pengalaman Pekerjaan IS UPLOADED = " + item.isUploaded);
								if (item.isUploaded) {
									dataPengalamanPekerjan.fileName = item.realFileName;
									dataPengalamanPekerjan.pathFile = item.file.name;
									dataPengalamanPekerjan.fileSize = item.file.size;
								}
								
							});
						}
						
						pengalamanVendorList.push(dataPengalamanPekerjan);

					});
				}
	
				if (form.dataMitraList !== undefined && form.dataMitraList.length > 0) {
					angular.forEach(form.dataMitraList, function (data, index) {
						
						var dataMitra = {
							mulaiKerjasama 	: new Date(data.mulaiKerjasama),
							bidangUsaha 	: data.bidangUsaha.nama,
							mataUang 		: {id:data.mataUang.id},
							lokasiPekerjaan : data.lokasiPekerjaan,
							namaPekerjaan 	: data.namaPekerjaan,
							nilaiKontrak	: data.nilaiKontrak,
							tipePengalaman 	: data.tipePengalaman
							
						}

						if (data.uploadBukti !== undefined && data.uploadBukti.length > 0) {
							angular.forEach(data.uploadBukti, function (item) {
								//console.log("Data Mitra IS UPLOADED = " + item.isUploaded);
								if (item.isUploaded) {
									dataMitra.fileName = item.realFileName;
									dataMitra.pathFile = item.file.name;
									dataMitra.fileSize = item.file.size;
								}
							});
						}
						
						pengalamanVendorList.push(dataMitra);
					});
				}
				
			
				
				
				
				if (form.dataWorkingProgressList !== undefined && form.dataWorkingProgressList.length > 0) {
					angular.forEach(form.dataWorkingProgressList, function (data, index) {
						
						var dataWorkingProgress = {
							mulaiKerjasama 	: new Date(data.mulaiKerjasama),
							bidangUsaha 	: data.bidangUsaha.nama,
							mataUang 		: {id:data.mataUang.id},
							lokasiPekerjaan : data.lokasiPekerjaan,
							namaPekerjaan 	: data.namaPekerjaan,
							nilaiKontrak	: data.nilaiKontrak,
							tipePengalaman 	: data.tipePengalaman
						}

						if (data.uploadBukti !== undefined && data.uploadBukti.length > 0) {
							angular.forEach(data.uploadBukti, function (item) {
								//console.log("Data Working Progress IS UPLOADED = " + item.isUploaded);
								if (item.isUploaded) {
									dataWorkingProgress.fileName = item.realFileName;
									dataWorkingProgress.pathFile = item.file.name;
									dataWorkingProgress.fileSize = item.file.size;
								}
							});
						}

						pengalamanVendorList.push(dataWorkingProgress);
					});
				}
					
				data.pengalamanVendorList=pengalamanVendorList;
				
				
				var dokumenRegistrasiVendorList=[]
				if(data.vendorProfile.title=='Company'){
				  if ($scope.dokumenSKD !== undefined && $scope.dokumenSKD.tanggalTerbit !== undefined) {
				    if (uploadDokumenSKD.queue[0].isUploaded) {
				  					data.vendorSKD ={
				  						alamat					: $scope.dokumenSKD.alamat,
				  						namaDokumen				: $scope.dokumenSKD.namaDokumen,
				  						tanggalTerbit 			: new Date($scope.dokumenSKD.tanggalTerbit),
				  						tanggalBerakhir 		: new Date($scope.dokumenSKD.tanggalBerakhir),
				  						fileName				:uploadDokumenSKD.queue[0].realFileName,
				  						pathFile				:uploadDokumenSKD.queue[0].file.name,
				  						fileSize				:uploadDokumenSKD.queue[0].file.size
				  					}
				  	

				  				}
				    }

				  if ($scope.SalinanTandaDaftar !== undefined && $scope.SalinanTandaDaftar.tanggalTerbit !== undefined) {
				    if (uploadSalinanTandaDaftar.queue[0].isUploaded) {
				      var document = {
				          subject: 'Salinan Tanda Daftar Perusahaan (TDP)',
				          namaDokumen: $scope.SalinanTandaDaftar.namaDokumen,
				          tanggalTerbit : new Date($scope.SalinanTandaDaftar.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.SalinanTandaDaftar.tanggalBerakhir),
				          fileName : uploadSalinanTandaDaftar.queue[0].realFileName,
				          pathFile : uploadSalinanTandaDaftar.queue[0].file.name,
				          fileSize : uploadSalinanTandaDaftar.queue[0].file.size
				    }
				      dokumenRegistrasiVendorList.push(document);
				    }
				}
				    
				  if ($scope.SalinanSuratIjinUsaha !== undefined && $scope.SalinanSuratIjinUsaha.tanggalTerbit !== undefined) {
				    if (uploadSalinanSuratIjinUsaha.queue[0].isUploaded) {
				      var document = {
				          subject: 'Salinan Surat Ijin Usaha (SIUP SIUJK)',
				          namaDokumen: $scope.SalinanSuratIjinUsaha.namaDokumen,
				          tanggalTerbit : new Date($scope.SalinanSuratIjinUsaha.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.SalinanSuratIjinUsaha.tanggalBerakhir),
				          fileName : uploadSalinanSuratIjinUsaha.queue[0].realFileName,
				          pathFile : uploadSalinanSuratIjinUsaha.queue[0].file.name,
				          fileSize : uploadSalinanSuratIjinUsaha.queue[0].file.size
				    }
				      dokumenRegistrasiVendorList.push(document);
				    }
				  }
				}
				

				  if ($scope.VendorRegistrasiForm !== undefined && $scope.VendorRegistrasiForm.tanggalTerbit !== undefined) {
				      if (uploadVendorRegistrasiForm.queue[0].isUploaded) {
				        var document = {
				            subject: 'Vendor Registrasi Form',
				            namaDokumen: $scope.VendorRegistrasiForm.namaDokumen,
				            tanggalTerbit : new Date($scope.VendorRegistrasiForm.tanggalTerbit),
				            tanggalBerakhir : new Date($scope.VendorRegistrasiForm.tanggalBerakhir),
				            fileName : uploadVendorRegistrasiForm.queue[0].realFileName,
				            pathFile : uploadVendorRegistrasiForm.queue[0].file.name,
				            fileSize : uploadVendorRegistrasiForm.queue[0].file.size
				      }
				        dokumenRegistrasiVendorList.push(document);
				  }
				}

				  if ($scope.SKB !== undefined && $scope.SKB.tanggalTerbit !== undefined) {
				      if (uploadSKB.queue[0].isUploaded) {
				        var document = {
				            subject: 'Surat Keterangan Bebas',
				            namaDokumen: $scope.SKB.namaDokumen,
				            tanggalTerbit : new Date($scope.SKB.tanggalTerbit),
				            tanggalBerakhir : new Date($scope.SKB.tanggalBerakhir),
				            fileName : uploadSKB.queue[0].realFileName,
				            pathFile : uploadSKB.queue[0].file.name,
				            fileSize : uploadSKB.queue[0].file.size
				      }
				        dokumenRegistrasiVendorList.push(document);
				  }
				}

				  if ($scope.SalinanAktePendirian !== undefined && $scope.SalinanAktePendirian.tanggalTerbit !== undefined) {
				    if (uploadSalinanAktePendirian.queue[0].isUploaded) {
				        var document = {
				            subject: 'Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya',
				            namaDokumen: $scope.SalinanAktePendirian.namaDokumen,
				            tanggalTerbit : new Date($scope.SalinanAktePendirian.tanggalTerbit),
				            tanggalBerakhir : new Date($scope.SalinanAktePendirian.tanggalBerakhir),
				            fileName : uploadSalinanAktePendirian.queue[0].realFileName,
				            pathFile : uploadSalinanAktePendirian.queue[0].file.name,
				            fileSize : uploadSalinanAktePendirian.queue[0].file.size
				      }
				        dokumenRegistrasiVendorList.push(document);
				    }
				}


				  if ($scope.BuktiFisikPerusahaan !== undefined && $scope.BuktiFisikPerusahaan.tanggalTerbit !== undefined) {
				    if (uploadBuktiFisikPerusahaan.queue[0].isUploaded) {
				      var document = {
				          subject: 'Bukti Fisik Perusahaan',
				          namaDokumen: $scope.BuktiFisikPerusahaan.namaDokumen,
				          tanggalTerbit : new Date($scope.BuktiFisikPerusahaan.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.BuktiFisikPerusahaan.tanggalBerakhir),
				          fileName : uploadBuktiFisikPerusahaan.queue[0].realFileName,
				          pathFile : uploadBuktiFisikPerusahaan.queue[0].file.name,
				          fileSize : uploadBuktiFisikPerusahaan.queue[0].file.size
				    }
				      dokumenRegistrasiVendorList.push(document);
				  }
				}


				//gk wajib

				  if ($scope.DokumenQuality !== undefined && $scope.DokumenQuality.tanggalTerbit !== undefined) {
				    if (uploadDokumenQuality.queue[0].isUploaded) {
				      var document = {
				          subject: 'Dokumen Quality yang dimiliki',
				          namaDokumen: $scope.DokumenQuality.namaDokumen,
				          tanggalTerbit : new Date($scope.DokumenQuality.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.DokumenQuality.tanggalBerakhir),
				          fileName : uploadDokumenQuality.queue[0].realFileName,
				          pathFile : uploadDokumenQuality.queue[0].file.name,
				          fileSize : uploadDokumenQuality.queue[0].file.size
				    }
				      dokumenRegistrasiVendorList.push(document);
				  }
				}


				  if ($scope.DokumenTeknik !== undefined && $scope.DokumenTeknik.tanggalTerbit !== undefined) {
				    if (uploadDokumenTeknik.queue[0].isUploaded) {
				      var document = {
				          subject: 'Dokumen Teknik',
				          namaDokumen: $scope.DokumenTeknik.namaDokumen,
				          tanggalTerbit : new Date($scope.DokumenTeknik.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.DokumenTeknik.tanggalBerakhir),
				          fileName : uploadDokumenTeknik.queue[0].realFileName,
				          pathFile : uploadDokumenTeknik.queue[0].file.name,
				          fileSize : uploadDokumenTeknik.queue[0].file.size
				        }
				      dokumenRegistrasiVendorList.push(document);
				    }
				}

				if($scope.bukanPKS == false) {
				  if ($scope.dokumenPKS !== undefined && $scope.dokumenPKS.tanggalTerbit !== undefined) {
				    if (uploadDokumenPKS.queue[0].isUploaded) {
				      var document = {
				          subject: 'Dokumen PKS',
				          namaDokumen: $scope.dokumenPKS.namaDokumen,
				          tanggalTerbit : new Date($scope.dokumenPKS.tanggalTerbit),
				          tanggalBerakhir : new Date($scope.dokumenPKS.tanggalBerakhir),
				          fileName : uploadDokumenPKS.queue[0].realFileName,
				          pathFile : uploadDokumenPKS.queue[0].file.name,
				          fileSize : uploadDokumenPKS.queue[0].file.size
				        }
				      dokumenRegistrasiVendorList.push(document);
				    }
				  }
				}

				if($scope.bukanPKS == true) {
				  if ($scope.dokumenSPR !== undefined && $scope.dokumenSPR.tanggalTerbit !== undefined) {
				    if (uploadDokumenSPR.queue[0].isUploaded) {
				      var document = {
				        subject: 'Dokumen SPR',
				        namaDokumen: $scope.dokumenSPR.namaDokumen,
				        tanggalTerbit : new Date($scope.dokumenSPR.tanggalTerbit),
				        tanggalBerakhir : new Date($scope.dokumenSPR.tanggalBerakhir),
				        fileName : uploadDokumenSPR.queue[0].realFileName,
				        pathFile : uploadDokumenSPR.queue[0].file.name,
				        fileSize : uploadDokumenSPR.queue[0].file.size
				      }
				    dokumenRegistrasiVendorList.push(document);
				    }
				  }
				  if ($scope.dokumenSPB !== undefined && $scope.dokumenSPB.tanggalTerbit !== undefined) {
				    if (uploadDokumenSPB.queue[0].isUploaded) {
				      var document = {
				        subject: 'Dokumen SPB',
				        namaDokumen: $scope.dokumenSPB.namaDokumen,
				        tanggalTerbit : new Date($scope.dokumenSPB.tanggalTerbit),
				        tanggalBerakhir : new Date($scope.dokumenSPB.tanggalBerakhir),
				        fileName : uploadDokumenSPB.queue[0].realFileName,
				        pathFile : uploadDokumenSPB.queue[0].file.name,
				        fileSize : uploadDokumenSPB.queue[0].file.size
				      }
				      dokumenRegistrasiVendorList.push(document);
				    }
				  }
				}
				
				data.dokumenRegistrasiVendorList=dokumenRegistrasiVendorList;
				
				
				saveData(data);
		
		}
		
		var saveData = function(dataDTO){
			RequestService.doPOSTJSON('/procurement/registrasivendor/registrasiVendorServices/vendor/createall', dataDTO)
			.then(function success(data) {
				toaster.pop('Success', 'Berhasil', 'Data Registrasi berhasil disimpan, Silahkan LOG-IN dengan Email anda yang telah didaftarkan');
				RequestService.informSuccess("Data Registrasi berhasil disimpan, Silahkan LOG-IN dengan Email anda yang telah didaftarkan");
				$state.go("page.portal");
			}, function error(response) {
				$scope.hideSaveBtn = false;
	        	RequestService.informError("Terjadi Kesalahan Pada System");
			});
		}
	
	}

	RegistrasiVendorController.$inject = ['$http', '$scope', '$rootScope', '$filter', 'ngTableParams', '$modal', '$state', 'FileUploader', 'toaster', '$resource', '$timeout', 'RequestService'];
})();


/* ---------------------------------------------------- KUMPULAN MODAL REGISTRASI -------------------------------------- */

angular.module('naut')
	.controller('DeleteDataModalRegistrasiController', function ($scope, $modalInstance, nameData) {
		$scope.nameData = nameData

		$scope.ok = function () {
			$modalInstance.close('closed');
		};

		$scope.cancel = function () {
			$modalInstance.dismiss('cancel');
		};
	});

angular.module('naut')
	.controller('BankVendorModalController', function ($rootScope, $scope, $http, $modalInstance, $modal, dataBankVendor, mataUangList, negaraList, toaster) {
		$scope.dataBankVendor = dataBankVendor;
		$scope.mataUangList = mataUangList;
		$scope.negaraList = negaraList;

		$scope.cariBank = function () {
			var bankmodalinstance = $modal.open({
				templateUrl: '/bankMaster.html',
				controller: 'BankModalController',
				size: 'lg',
				resolve: {
					dataBankMaster: function () {
						return {};
					}
				}
			});
			bankmodalinstance.result.then(function (dataBankMaster) {
				if (dataBankMaster != undefined && dataBankMaster !== null) {
					dataBankVendor.namaBank = dataBankMaster.namaBank;
					dataBankVendor.cabangBank = dataBankMaster.cabangBank;
					dataBankVendor.alamatBank = dataBankMaster.alamatBank;
					dataBankVendor.kota = dataBankMaster.kota;
				}
			});
		}

		$scope.btnSimpan = function () {

			//Validasi Data Bank
			if (dataBankVendor.namaBank == undefined || dataBankVendor.namaBank == "") {
				dataBankVendor.namaBankError = true;
				document.getElementsByName("namaBank")[0].focus();
			} else {
				dataBankVendor.namaBankError = false;
			}

			if (dataBankVendor.nomorRekening == undefined || dataBankVendor.nomorRekening == "") {
				dataBankVendor.nomorRekeningError = true;
				document.getElementsByName("nomorRekening")[0].focus();
			} else {
				dataBankVendor.nomorRekeningError = false;
			}

			if (dataBankVendor.namaNasabah == undefined || dataBankVendor.namaNasabah == "") {
				dataBankVendor.namaNasabahError = true;
				document.getElementsByName("namaNasabah")[0].focus();
			} else {
				dataBankVendor.namaNasabahError = false;
			}

			if (dataBankVendor.mataUang == undefined || dataBankVendor.mataUang == "") {
				dataBankVendor.mataUangError = true;
				document.getElementsByName("mataUang")[0].focus();
			} else {
				dataBankVendor.mataUangError = false;
			}



			if (dataBankVendor.namaBankError == false && dataBankVendor.nomorRekeningError == false && dataBankVendor.namaNasabahError == false && dataBankVendor.mataUangError == false) {

				$modalInstance.close($scope.dataBankVendor);
			}
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}
	});

angular.module('naut')
	.controller('SegmentasiVendorModalController', function ($rootScope, $scope, $http, $modalInstance, $filter, ngTableParams, dataSegmentasi) {
		$scope.dataSegmentasi = dataSegmentasi;
		$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/bidangusaha/getBidangUsahaList')
			.success(function (data, status, headers, config) {
				$scope.bidangUsahaList = data;
			});

		$scope.subBidangUsahaList = [];
		
		$scope.tableSubBidangUsaha = $scope.subBidangUsahaList;
		/*$scope.tableSubBidangUsaha = new ngTableParams({
			page: 1,
			count: 5
		}, {
			counts: [], // hide page count
			total: $scope.subBidangUsahaList.length,
			getData: function ($defer, params) {
				$defer.resolve($scope.subBidangUsahaList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
			}
		});
*/
		$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/asosiasi/getList')
			.success(function (data, status, headers, config) {
				$scope.asosiasiList = data;
			});

		var loadDataSubBidang = function () {
			if ($scope.dataSegmentasi !== undefined && $scope.dataSegmentasi.bidangUsaha !== undefined) {
				$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/subbidangusaha/get-by-bidang-usaha-id/' + $scope.dataSegmentasi.bidangUsaha.id)
					.success(function (data, status, headers, config) {
						$scope.subBidangUsahaList = data;
						
												
						angular.forEach($scope.subBidangUsahaList, function (dataSubBidang, index) {
							if ($scope.dataSegmentasi !== undefined && $scope.dataSegmentasi.subBidangUsaha !== undefined && dataSubBidang.id === $scope.dataSegmentasi.subBidangUsaha.id) {
								dataSubBidang.pilihSubBidang = true;
							} else {
								dataSubBidang.pilihSubBidang = false;
							}
						});
						
						$scope.tableSubBidangUsaha = $scope.subBidangUsahaList;
						//$scope.tableSubBidangUsaha.reload();
						
						
					});
				
				
			}
			
		}
		loadDataSubBidang();


		
		$scope.changeBidangUsaha = function () {
			loadDataSubBidang();
		}

		$scope.tanggalMulaiStatus = false;
		$scope.tanggalMulaiOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalMulaiStatus = true;
		};

		$scope.tanggalBerakhirStatus = false;
		$scope.tanggalBerakhirOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalBerakhirStatus = true;
		};

		$scope.changeTglSegmentasiMulaiStatus = function () {
			$scope.dataSegmentasi.tanggalBerakhir = null;
		}

		$scope.btnSimpan = function () {
			//console.log("masuk");
			if ($scope.subBidangUsahaList !== undefined && $scope.subBidangUsahaList !== null && $scope.subBidangUsahaList.length > 0) {
				angular.forEach($scope.subBidangUsahaList, function (dataSubBidang, index) {
					if (dataSubBidang.pilihSubBidang === undefined) {
						$scope.dataSegmentasi.subBidangUsaha = dataSubBidang;
					}
				});
			}
			if ($scope.dataSegmentasi.tanggalMulai !== undefined && $scope.dataSegmentasi.tanggalMulai !== null) {
				$scope.dataSegmentasi.tanggalMulai = $filter('date')($scope.dataSegmentasi.tanggalMulai, 'dd/MM/yyyy');
			}
			if ($scope.dataSegmentasi.tanggalBerakhir !== undefined && $scope.dataSegmentasi.tanggalBerakhir !== null) {
				$scope.dataSegmentasi.tanggalBerakhir = $filter('date')($scope.dataSegmentasi.tanggalBerakhir, 'dd/MM/yyyy');
			}

			//Validasi Data Segmentasi
			if (dataSegmentasi.bidangUsaha == undefined || dataSegmentasi.bidangUsaha == "") {
				dataSegmentasi.bidangUsahaError = true;
				document.getElementsByName("bidangUsaha")[0].focus();
			} else {
				dataSegmentasi.bidangUsahaError = false;
			}
			
			if(dataSegmentasi.subBidangUsaha == null || dataSegmentasi.subBidangUsaha == undefined || dataSegmentasi.subBidangUsaha == "") {
	    		dataSegmentasi.subBidangUsahaError = true;
	            document.getElementsByName("subbidangusaha")[0].focus();
	        } else {
	        	dataSegmentasi.subBidangUsahaError = false;
	        } 
			
			if (dataSegmentasi.asosiasi == undefined || dataSegmentasi.asosiasi == "") {
				dataSegmentasi.asosiasiError = true;
				document.getElementsByName("asosiasi")[0].focus();
			} else {
				dataSegmentasi.asosiasiError = false;
			}

			if (dataSegmentasi.nomor == undefined || dataSegmentasi.nomor == "") {
				dataSegmentasi.nomorError = true;
				document.getElementsByName("nomor")[0].focus();
			} else {
				dataSegmentasi.nomorError = false;
			}

			if (dataSegmentasi.tanggalMulai == undefined || dataSegmentasi.tanggalMulai == "") {
				dataSegmentasi.tanggalMulaiError = true;
				document.getElementsByName("tanggalMulai")[0].focus();
			} else {
				dataSegmentasi.tanggalMulaiError = false;
			}

			if (dataSegmentasi.tanggalBerakhir == undefined || dataSegmentasi.tanggalBerakhir == "") {
				dataSegmentasi.tanggalBerakhirError = true;
				document.getElementsByName("tanggalBerakhir")[0].focus();
			} else {
				dataSegmentasi.tanggalBerakhirError = false;
			}

			//console.log($scope.dataSegmentasi);
			if (dataSegmentasi.bidangUsahaError == false && dataSegmentasi.subBidangUsahaError == false && dataSegmentasi.asosiasiError == false && dataSegmentasi.nomorError == false && dataSegmentasi.tanggalMulaiError == false && dataSegmentasi.tanggalBerakhirError == false) {
				$modalInstance.close($scope.dataSegmentasi); 
			}
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}
	});

angular.module('naut')
	.controller('PeralatanVendorModalController', function ($http, $rootScope, $scope, $modalInstance, ngTableParams, dataPeralatan, FileUploader,RequestService, toaster) {
		$scope.dataPeralatan = dataPeralatan;

		$http.get($rootScope.backendAddress + '/procurement/master/KondisiServices/getList')
			.success(function (data, status, headers, config) {
				$scope.kondisiList = data;
			});

		$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/buktikepemilikan/getList')
			.success(function (data, status, headers, config) {
				$scope.buktiKepemilikanList = data;
			});

		var uploadDokumenBuktiKepemilikan = $scope.uploadDokumenBuktiKepemilikan = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});
		
		uploadDokumenBuktiKepemilikan.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,'doc') && (this.queue.length < 10));
			}
		});

		uploadDokumenBuktiKepemilikan.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		$scope.uploadDoc = function() {
			if(uploadDokumenBuktiKepemilikan.queue != undefined && uploadDokumenBuktiKepemilikan.queue.length > 0) {
				angular.forEach(uploadDokumenBuktiKepemilikan.queue, function(item) {
	        		item.upload();
	            });
			} /*else {
				RequestService.showModalInformation('Silahkan upload dalam format .doc , .docx , dan .pdf','danger');
			}*/
		}

		var uploadGambarPeralatanAsset = $scope.uploadGambarPeralatanAsset = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});

		uploadGambarPeralatanAsset.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,'img') && (this.queue.length < 10));
			}
		});

		uploadGambarPeralatanAsset.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		$scope.uploadImage = function() {
			if(uploadGambarPeralatanAsset.queue != undefined && uploadGambarPeralatanAsset.queue.length > 0) {
				angular.forEach(uploadGambarPeralatanAsset.queue, function(item) {
	        		item.upload();
	            });
			} /*else {
				RequestService.showModalInformation('Silahkan upload dalam format .jpg dan .png','danger');
			}*/
		}

		$scope.validasiTahun = function (tahun) {
			var lanjut = true;
			var date = new Date();
			if (tahun < 1950 || tahun > date.getFullYear()) {
				toaster.pop('error', 'Kesalahan', 'Tahun melebihi batas (1950 s/d Tahun ini)');
				//document.getElementsByName("tahunPembuatan")[0].focus();
				lanjut = false;
			}

			return lanjut;
		}

		$scope.btnSimpan = function () {
			if (uploadDokumenBuktiKepemilikan.queue !== undefined && uploadDokumenBuktiKepemilikan.queue.length > 0) {
				$scope.dataPeralatan.uploadDokumenBuktiKepemilikan = uploadDokumenBuktiKepemilikan.queue;
			}
			if (uploadGambarPeralatanAsset.queue !== undefined && uploadGambarPeralatanAsset.queue.length > 0) {
				$scope.dataPeralatan.uploadGambarPeralatanAsset = uploadGambarPeralatanAsset.queue;
			}

			//Validasi Data Peralatan
			if (dataPeralatan.jenis == undefined || dataPeralatan.jenis == "") {
				dataPeralatan.jenisError = true;
				document.getElementsByName("jenis")[0].focus();
			} else {
				dataPeralatan.jenisError = false;
			}

			if (dataPeralatan.jumlah == undefined || dataPeralatan.jumlah == "") {
				dataPeralatan.jumlahError = true;
				document.getElementsByName("jumlah")[0].focus();
			} else {
				dataPeralatan.jumlahError = false;
			}

			if (dataPeralatan.kapasitas == undefined || dataPeralatan.kapasitas == "") {
				dataPeralatan.kapasitasError = true;
				document.getElementsByName("kapasitas")[0].focus();
			} else {
				dataPeralatan.kapasitasError = false;
			}

			if (dataPeralatan.merk == undefined || dataPeralatan.merk == "") {
				dataPeralatan.merkError = true;
				document.getElementsByName("merk")[0].focus();
			} else {
				dataPeralatan.merkError = false;
			}

			if (dataPeralatan.tahunPembuatan == undefined || dataPeralatan.tahunPembuatan == "") {
				dataPeralatan.tahunPembuatanError = true;
				document.getElementsByName("tahunPembuatan")[0].focus();
			} else {
				if ($scope.validasiTahun(dataPeralatan.tahunPembuatan)) {
					dataPeralatan.tahunPembuatanError = false;
				}
			}

			if (dataPeralatan.kondisi == undefined || dataPeralatan.kondisi == "") {
				dataPeralatan.kondisiError = true;
				document.getElementsByName("kondisi")[0].focus();
			} else {
				dataPeralatan.kondisiError = false;
			}

			if (dataPeralatan.lokasi == undefined || dataPeralatan.lokasi == "") {
				dataPeralatan.lokasiError = true;
				document.getElementsByName("lokasi")[0].focus();
			} else {
				dataPeralatan.lokasiError = false;
			}

			if (dataPeralatan.buktiKepemilikan == undefined || dataPeralatan.buktiKepemilikan == "") {
				dataPeralatan.buktiKepemilikanError = true;
				document.getElementsByName("buktiKepemilikan")[0].focus();
			} else {
				dataPeralatan.buktiKepemilikanError = false;
			}


			if (dataPeralatan.jenisError == false && dataPeralatan.jumlahError == false && dataPeralatan.kapasitasError == false && dataPeralatan.merkError == false && dataPeralatan.tahunPembuatanError == false && dataPeralatan.kondisiError == false && dataPeralatan.lokasiError == false && dataPeralatan.buktiKepemilikanError == false) {
				$modalInstance.close($scope.dataPeralatan);
			}
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}
		
		$scope.downloadFile = $rootScope.viewUploadBackendAddress+'/';

	});

angular.module('naut')
	.controller('KeuanganVendorModalController', function ($rootScope, $scope, $modalInstance, $filter, ngTableParams, dataKeuangan, namaEvent, toaster) {
		$scope.dataKeuangan = dataKeuangan;

		if (namaEvent == 'tambah') {
			$scope.dataKeuangan.kas = 0;
			$scope.dataKeuangan.bank = 0;
			$scope.dataKeuangan.totalPiutang = 0;
			$scope.dataKeuangan.persediaanBarang = 0;
			$scope.dataKeuangan.pekerjaanDalamProses = 0;
			$scope.dataKeuangan.peralatanDanMesin = 0;
			$scope.dataKeuangan.inventaris = 0;
			$scope.dataKeuangan.gedungGedung = 0;
			$scope.dataKeuangan.aktivaLainnya = 0;
			$scope.dataKeuangan.hutangDagang = 0;
			$scope.dataKeuangan.hutangPajak = 0;
			$scope.dataKeuangan.hutangLainnya = 0;
			$scope.dataKeuangan.kekayaanBersih = 0;
			$scope.dataKeuangan.hutangJangkaPanjang = 0;
			$scope.dataKeuangan.totalAktivaLancar = 0;
			$scope.dataKeuangan.totalAktivaTetap = 0;
			$scope.dataKeuangan.totalHutangJangkaPendek = 0;
			$scope.dataKeuangan.kekayaanBersih = 0;
		}

		// --------------------------------- Start Perhitungan Neraca per Lajur Buku Besar nya ----------------------------------- >

		//Aktiva
		$scope.tambahAktivaLancar = function () {
			$scope.dataKeuangan.totalAktivaLancar = parseFloat($scope.dataKeuangan.kas) + parseFloat($scope.dataKeuangan.bank) 
			+ parseFloat($scope.dataKeuangan.totalPiutang) + parseFloat($scope.dataKeuangan.persediaanBarang) + parseFloat($scope.dataKeuangan.pekerjaanDalamProses);
		}

		$scope.tambahAktivaTetap = function () {
			$scope.dataKeuangan.totalAktivaTetap = parseFloat($scope.dataKeuangan.peralatanDanMesin) + parseFloat($scope.dataKeuangan.inventaris) 
			+ parseFloat($scope.dataKeuangan.gedungGedung);
		}

		$scope.totalAktiva = function () {
			$scope.dataKeuangan.totalAktiva = parseFloat($scope.dataKeuangan.totalAktivaLancar) + parseFloat($scope.dataKeuangan.totalAktivaTetap) 
			+ parseFloat($scope.dataKeuangan.aktivaLainnya);
		}

		//Pasiva
		$scope.tambahPasiva = function () {
			$scope.dataKeuangan.totalHutangJangkaPendek = parseFloat($scope.dataKeuangan.hutangDagang) + parseFloat($scope.dataKeuangan.hutangPajak) 
			+ parseFloat($scope.dataKeuangan.hutangLainnya);
		}

		$scope.rugiLaba = function () {
			$scope.dataKeuangan.kekayaanBersih = (parseFloat($scope.dataKeuangan.kas) + parseFloat($scope.dataKeuangan.bank) + parseFloat($scope.dataKeuangan.totalPiutang) 
					+ parseFloat($scope.dataKeuangan.persediaanBarang) + parseFloat($scope.dataKeuangan.pekerjaanDalamProses) + parseFloat($scope.dataKeuangan.peralatanDanMesin) 
					+ parseFloat($scope.dataKeuangan.inventaris) + parseFloat($scope.dataKeuangan.gedungGedung) + parseFloat($scope.dataKeuangan.aktivaLainnya)) 
					- (parseFloat($scope.dataKeuangan.hutangDagang) + parseFloat($scope.dataKeuangan.hutangPajak) + parseFloat($scope.dataKeuangan.hutangLainnya) 
							+ parseFloat($scope.dataKeuangan.hutangJangkaPanjang));
		}

		$scope.totalPasiva = function () {
			$scope.dataKeuangan.totalPasiva = ((parseFloat($scope.dataKeuangan.kas) + parseFloat($scope.dataKeuangan.bank) + parseFloat($scope.dataKeuangan.totalPiutang) 
						+ parseFloat($scope.dataKeuangan.persediaanBarang) + parseFloat($scope.dataKeuangan.pekerjaanDalamProses) + parseFloat($scope.dataKeuangan.peralatanDanMesin) 
						+ parseFloat($scope.dataKeuangan.inventaris) + parseFloat($scope.dataKeuangan.gedungGedung) + parseFloat($scope.dataKeuangan.aktivaLainnya)) 
						- (parseFloat($scope.dataKeuangan.hutangDagang) + parseFloat($scope.dataKeuangan.hutangPajak) + parseFloat($scope.dataKeuangan.hutangLainnya) 
								+ parseFloat($scope.dataKeuangan.hutangJangkaPanjang))) + (parseFloat($scope.dataKeuangan.hutangDagang) + parseFloat($scope.dataKeuangan.hutangPajak) 
										+ parseFloat($scope.dataKeuangan.hutangLainnya) + parseFloat($scope.dataKeuangan.hutangJangkaPanjang));
			}
			// --------------------------------- End Perhitungan Neraca per Lajur Buku Besar nya -------------------------------------- >

		$scope.tanggalAuditStatus = false;
		$scope.tanggalAuditOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.tanggalAuditStatus = true;
		};
    
        $scope.tahunKeuanganList = [];
        var yearNow = $filter('date')(new Date(), 'yyyy');
        for(var i=yearNow; i > (yearNow-5);i-- ){
            $scope.tahunKeuanganList.push({
                id : i
            });
        }

		$scope.validasiTahun = function (tahun) {
			var lanjut = true;
			if (tahun < 1950 || tahun > 2020) {
				toaster.pop('error', 'Kesalahan', 'Tahun melebihi batas (1950 s/d 2020)');
				document.getElementsByName("tahunKeuangan")[0].focus();
				lanjut = false;
			}

			return lanjut;
		}

		$scope.btnSimpan = function () {

			$scope.dataKeuangan.totalHutang = parseFloat(dataKeuangan.hutangJangkaPanjang) + parseFloat(dataKeuangan.totalHutangJangkaPendek);
			
			if (dataKeuangan.nomorAudit == undefined || dataKeuangan.nomorAudit == "") {
				dataKeuangan.nomorAuditError = true;
				document.getElementsByName("nomorAudit")[0].focus();
			} else {
				dataKeuangan.nomorAuditError = false;
			}

			if (dataKeuangan.tanggalAudit == undefined || dataKeuangan.tanggalAudit == "") {
				dataKeuangan.tanggalAuditError = true;
				document.getElementsByName("tanggalAudit")[0].focus();
			} else {
				dataKeuangan.tanggalAuditError = false;
			}

			if (dataKeuangan.namaAudit == undefined || dataKeuangan.namaAudit == "") {
				dataKeuangan.namaAuditError = true;
				document.getElementsByName("namaAudit")[0].focus();
			} else {
				dataKeuangan.namaAuditError = false;
			}

			if (dataKeuangan.tahunKeuangan == undefined || dataKeuangan.tahunKeuangan == "") {
				dataKeuangan.tahunKeuanganError = true;
				document.getElementsByName("tahunKeuangan")[0].focus();
			} else {
				dataKeuangan.tahunKeuanganError = false;
			}

			var totalAktivaError = false;
			if (dataKeuangan.totalAktiva == undefined || dataKeuangan.totalAktiva == "") {
				totalAktivaError = true;
				toaster.pop('warning', 'Kesalahan', 'Data Neraca Harus Lengkap');
			}


			if (dataKeuangan.nomorAuditError == false && dataKeuangan.tanggalAuditError == false && dataKeuangan.namaAuditError == false && dataKeuangan.tahunKeuanganError == false && totalAktivaError == false) {
				$modalInstance.close($scope.dataKeuangan);
			}
			
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}
	});

angular.module('naut')
	.controller('RegPengalamanPekerjaanModalController', function ($rootScope, $scope, $modalInstance, $filter, FileUploader,RequestService, judulModal, namaModal, bidangUsahaList, mataUangList, dataPengalaman) {
		$scope.judulModal = judulModal;
		$scope.namaModal = namaModal;
		$scope.dataPengalaman = dataPengalaman;
		$scope.bidangUsahaList = bidangUsahaList;
		$scope.mataUangList = mataUangList;
		$scope.downloadFile = $rootScope.viewUploadBackendAddress+'/';

		var uploadBukti = $scope.uploadBukti = new FileUploader({
			url: $rootScope.uploadBackendAddress,
			method: 'POST'
		});

		uploadBukti.filters.push({
			name: 'customFilter',
			fn: function (item /*{File|FileLikeObject}*/ , options) {
				return (RequestService.uploadFilter(this, item, true,'doc') && (this.queue.length < 10));
			}
		});

		uploadBukti.onCompleteItem = function (fileItem, response, status, headers) {
			console.info('onCompleteItem', fileItem, response, status, headers);
			fileItem.realFileName = response.fileName;
		};
		
		$scope.fileLama = true;
		$scope.uploadAja = function() {
			if(uploadBukti.queue != undefined && uploadBukti.queue.length > 0) {
				angular.forEach(uploadBukti.queue, function (item) {
					item.upload();
				});
			}/* else {
				RequestService.showModalInformation('Silahkan upload dalam format .doc, .docx dan .pdf','danger');
			}*/
		}

		$scope.mulaiKerjasamaStatus = false;
		$scope.mulaiKerjasamaOpen = function ($event) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.mulaiKerjasamaStatus = true;
		};
		
		$scope.btnSimpanPengalaman = function () {
			//console.log('pengalaman');
			if (dataPengalaman.namaPekerjaan == undefined || dataPengalaman.namaPekerjaan == "") {
				dataPengalaman.namaPekerjaanError = true;
				document.getElementsByName("namaPekerjaan")[0].focus();
			} else {
				dataPengalaman.namaPekerjaanError = false;
			}

			if (dataPengalaman.lokasiPekerjaan == undefined || dataPengalaman.lokasiPekerjaan == "") {
				dataPengalaman.lokasiPekerjaanError = true;
				document.getElementsByName("lokasiPekerjaan")[0].focus();
			} else {
				dataPengalaman.lokasiPekerjaanError = false;
			}

			if (dataPengalaman.bidangUsaha == undefined || dataPengalaman.bidangUsaha == "") {
				dataPengalaman.bidangUsahaError = true;
				document.getElementsByName("bidangUsaha")[0].focus();
			} else {
				dataPengalaman.bidangUsahaError = false;
			}

			if (dataPengalaman.mulaiKerjasama == undefined || dataPengalaman.mulaiKerjasama == "") {
				dataPengalaman.mulaiKerjasamaError = true;
				document.getElementsByName("mulaiKerjasama")[0].focus();
			} else {
				dataPengalaman.mulaiKerjasamaError = false;
			}

			if (dataPengalaman.nilaiKontrak == undefined || dataPengalaman.nilaiKontrak == "") {
				dataPengalaman.nilaiKontrakError = true;
				document.getElementsByName("nilaiKontrak")[0].focus();
			} else {
				dataPengalaman.nilaiKontrakError = false;
			}

			if (dataPengalaman.mataUang == undefined || dataPengalaman.mataUang == "") {
				dataPengalaman.mataUangError = true;
				document.getElementsByName("mataUang")[0].focus();
			} else {
				dataPengalaman.mataUangError = false;
			}

			try{
				if ((uploadBukti.queue !== undefined && uploadBukti.queue.length > 0) || dataPengalaman.uploadBukti[0].realFileName != undefined) {
					if (uploadBukti.queue !== undefined && uploadBukti.queue.length > 0){
						$scope.dataPengalaman.uploadBukti = uploadBukti.queue;
					}
					dataPengalaman.buktiKerjasamaError = false;
				}
			}catch(e){
				dataPengalaman.buktiKerjasamaError = true;
				document.getElementsByName("buktiKerjasama")[0].focus();
			}
			


			if (dataPengalaman.namaPekerjaanError == false && dataPengalaman.lokasiPekerjaanError == false && dataPengalaman.bidangUsahaError == false && dataPengalaman.mulaiKerjasamaError == false && dataPengalaman.nilaiKontrakError == false && dataPengalaman.mataUangError == false && dataPengalaman.buktiKerjasamaError == false) {
				//console.log("SIMPAN");
				$modalInstance.close($scope.dataPengalaman);
			}
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}
	});

angular.module('naut')
	.controller('BankModalController', function ($rootScope, $scope, $http, $resource, $location, $state, $modalInstance, $stateParams, dataBankMaster, toaster, ngTableParams, RequestService, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter) {
		var vm = this;
		//$scope.loading = true;
		$scope.bank = ($stateParams.bank != undefined) ? $stateParams.bank : {};
		/*$http.get($rootScope.backendAddress + '/procurement/registrasivendor/registrasiVendorServices/bank/getList',{
			ignoreLoadingBar: true})
			.success(function (data, status, headers, config) {
				$scope.loading = false;
				$scope.bankList = data;
				
			}).error(function (data, status, headers, config) {
	            $scope.loading = false;
	        })  */
		
		$scope.dtInstance = {};
        
        function callback(json) {
		    //console.log(json);
		}
        
        $scope.reloadData = function () {
			
		    var resetPaging = false;
		    
		    if($scope.dtInstance != null){
		    	$scope.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

		$scope.dtOptions = RequestService.requestServerPaggingNoToken(DTOptionsBuilder.newOptions(), '/procurement/master/bank/get-bank-by-pagination');
    	$scope.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    		$compile(nRow)($scope);
    		////console.log(nRow);
    		////console.log(aData);
            var start = this.fnSettings()._iDisplayStart;
            $("td:first", nRow).html(start + iDisplayIndex + 1);
        });
    	$scope.dtColumns = [
    		DTColumnBuilder.newColumn(null).withTitle('Id').notSortable(),
    		
    		DTColumnBuilder.newColumn('namaBank').withTitle('Nama Bank').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('namaKantor').withTitle('Nama Cabang').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('alamat').withTitle('Alamat').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn('kota').withTitle('Kota').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('Aksi').notSortable().renderWith(function (data, type, full, meta) {
    		
    			return '<div class="radio c-radio"><label style="color:green">'+
    			'<input type="radio" name="dataBank" ng-model="bank.id"  ng-click="pilihBank('+data.id+')"><span class="fa fa-circle"></span>'+
    			'</label></div>';
    		})
    	];
    			
    	DTInstances.getLast().then(function(instance) {
    		$scope.dtInstance = instance;
            ////console.log("DATAAAAAAAAAAAAAAAA : "+instance);
        });
		
    	$scope.pilihBank = function (dataBank) { 	
        	RequestService.doGET('/procurement/master/bank/get-by-id/' + dataBank)
			.then(function success(data) {
				////console.log("Dataaaaaaaaaaaaaaaaaaaaaaaaaaaaa ID = "+dataBank);
				var dataBankMaster = {
					namaBank: data.namaBank,
					cabangBank: data.namaKantor,
					alamatBank: data.alamat,
					kota: data.kota
				}
				
				$scope.dataBankMaster = dataBankMaster;
			}, function error(response) {	
			});

        }

		

		$scope.btnSimpan = function () {
			$modalInstance.close($scope.dataBankMaster);
		}

		$scope.btnBatal = function () {
			$modalInstance.dismiss('cancel');
		}

	});


