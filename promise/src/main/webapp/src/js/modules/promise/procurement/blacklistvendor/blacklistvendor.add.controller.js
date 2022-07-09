(function() {
	'use strict';

	angular.module('naut').controller('BlacklistAddController',
			BlacklistAddController);

	function BlacklistAddController($stateParams, $modal, ModalService,
			RequestService, $scope, $http, $rootScope, $resource, $location,
			$state, $q, toaster, FileUploader) {

		var vm = this;

		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.blacklistVendor = ($stateParams.blacklistVendor != undefined) ? $stateParams.blacklistVendor
				: {};

		vm.alasanBlacklistList = [];
		vm.blacklistVendorDtl = {};
		vm.blacklistVendorDtl.blacklistVendor = {};
		vm.blacklistVendorDtl.blacklistVendor.vendor = {};
		vm.blacklistVendorDtl.blacklistVendorDetailList = [];
		vm.blacklistVendorDtl.blacklistVendorFileList = [];

		$scope.save = function() {
			RequestService.modalConfirmation().then(function(result) {
				if ($scope.validateForm()) {
					$scope.saveData();
				}
			});
		}

		$scope.validateForm = function() {
			vm.isValid = true;
			vm.errorMessageDataVendor = '';
			vm.errorMessageAlasanBlacklist = '';
			vm.errorMessageWaktu = '';
			vm.errorMessageJangkaWaktu = '';
			vm.errorMessageCatatan = '';
			vm.errorMessageCatatanVendor = '';
			vm.errorMessageAlasanBlacklist = '';
			vm.errorMessageFileBlacklist = '';
			vm.errorMessageFile = '';

			if (vm.blacklistVendorDtl.blacklistVendor.vendor.id == null
					|| vm.blacklistVendorDtl.blacklistVendor.vendor.id == '') {
				vm.isValid = false;
				vm.errorMessageDataVendor = 'promise.procurement.manajemenvendor.blacklistvendor.error.data_vendor';
			}
			if (vm.blacklistVendorDtl.blacklistVendor.blacklistVendorJmlWaktu == null
					|| vm.blacklistVendorDtl.blacklistVendor.blacklistVendorJmlWaktu == '') {
				vm.isValid = false;
				vm.errorMessageWaktu = 'promise.procurement.manajemenvendor.blacklistvendor.error.durasi_kosong';
			}else{
				if (vm.blacklistVendorDtl.blacklistVendor.blacklistVendorJmlWaktu <= 0) {
				vm.isValid = false;
				vm.errorMessageWaktu = 'promise.procurement.manajemenvendor.blacklistvendor.error.durasi_nol';
				}
			}
			if (vm.blacklistVendorDtl.blacklistVendor.blacklistVendorJkWaktu == null
					|| vm.blacklistVendorDtl.blacklistVendor.blacklistVendorJkWaktu == '') {
				vm.isValid = false;
				vm.errorMessageJangkaWaktu = 'promise.procurement.manajemenvendor.blacklistvendor.error.jangka_waktu';
			}
			if (vm.blacklistVendorDtl.blacklistVendor.blacklistVendorKeterangan == null
					|| vm.blacklistVendorDtl.blacklistVendor.blacklistVendorKeterangan == '') {
				vm.isValid = false;
				vm.errorMessageCatatan = 'promise.procurement.manajemenvendor.blacklistvendor.error.catatan';
			}
			if (vm.blacklistVendorDtl.blacklistVendor.blacklistVendorKeteranganVnd == null
					|| vm.blacklistVendorDtl.blacklistVendor.blacklistVendorKeteranganVnd == '') {
				vm.isValid = false;
				vm.errorMessageCatatanVendor = 'promise.procurement.manajemenvendor.blacklistvendor.error.catatan_vendor';
			}
			if (vm.alasanBlacklistList.length == 0) {
				vm.isValid = false;
				vm.errorMessageAlasanBlacklist = 'promise.procurement.manajemenvendor.blacklistvendor.error.alasan_blacklist';
			}
			if (uploader.queue.length == 0) {
				vm.isValid = false;
				vm.errorMessageFileBlacklist = 'promise.procurement.manajemenvendor.blacklistvendor.error.file_blacklist';
			} else {
				var check = true;
				uploader.queue.forEach(function(file, index, array) {
					if (file.realFileName == null || file.realFileName == '') {
						check = false;
					} else {
						check = true;
					}
				});
				if (check == false) {
					vm.isValid = false;
					vm.errorMessageFile = 'promise.procurement.manajemenvendor.blacklistvendor.error.file';
				}
			}
			return vm.isValid;
		}

		$scope.saveData = function() {
			vm.blacklistVendorDtl.blacklistVendorDetailList = [];
			vm.alasanBlacklistList.forEach(function(alasan, index, array) {
				var blacklistVendorDetail = {};
				blacklistVendorDetail.alasanBlacklist = {};
				blacklistVendorDetail.alasanBlacklist = alasan;
				vm.blacklistVendorDtl.blacklistVendorDetailList
						.push(blacklistVendorDetail);
			});

			vm.blacklistVendorDtl.blacklistVendorFileList = [];
			uploader.queue.forEach(function(file, index, array) {
				var blacklistVendorFile = {};
				blacklistVendorFile.blacklistFile = file.realFileName;
				vm.blacklistVendorDtl.blacklistVendorFileList
						.push(blacklistVendorFile);
			});
			RequestService
					.doPOSTJSON(
							'/procurement/vendormanagement/BlacklistVendorServices/insertBlacklistVendor',
							vm.blacklistVendorDtl)
					.then(
							function successCallback(data) {
								RequestService.informSaveSuccess();
								$state
										.go('app.promise.procurement-manajemenvendor-blacklistvendor');
							},
							function error(response) {
								$log.info("proses gagal");
								RequestService
										.informError("Terjadi Kesalahan Pada System");
							});
		}

		/** upload * */
		$scope.mSize = 5; // Panjang halaman yang tampil dibawah
		$scope.ayoSimpan = false;

		// Upload Images
		var uploader = $scope.uploader = new FileUploader({
			url : $rootScope.uploadBackendAddress,
			method : 'POST'
		});

		uploader.onAfterAddingFile = function(fileItem) {

			if (uploader.queue.length > 1) {
				angular.forEach(uploader.queue, function(item) {

					var i = 0;
					angular.forEach(uploader.queue, function(itm) {
						if (item.file.name == itm.file.name) {
							i++;
						}
					});

					// console.log("jumlah duplicate " + i );
					if (i == 2 && item.file.name == fileItem.file.name) {
						// alert("file tidak boleh sama");
						toaster.pop('error', fileItem.file.name,
								'File tidak boleh sama!');
						fileItem.remove();
					}

				});

			}

		};

		uploader.onBeforeUploadItem = function(item) {
			console.info('onBeforeUploadItem', item);

		};
		uploader.filters.push({
			name : 'customFilter',
			fn : function(item /* {File|FileLikeObject} */, options) {
				return (RequestService.uploadFilter(this, item, true) && (this.queue.length < 10));
			}
		});

		uploader.onCompleteItem = function(fileItem, response, status, headers) {
			fileItem.realFileName = response.fileName;
			$scope.loading = false;
		};

		uploader.onProgressItem = function(fileItem, progress) {
			$scope.progressTime = progress;
			$scope.loadingUpload = true;
			$scope.loading = true;
		};

		uploader.onCompleteAll = function() {
			$scope.ayoSimpan = true;
		};

		$scope.viewFile = function(namaFile) {
			window.open($rootScope.viewUploadBackendAddress + "/" + namaFile,
					'_blank');
		}
		/** upload * */

		$scope.findVendor = function() {
			var addModalInstanceVendor = $modal.open({
				templateUrl : '/findVendor.html',
				controller : AddModalInstance,
				size : 'lg'
			});
		}

		var AddModalInstance = function(RequestService, ModalService, $scope,
				$modalInstance, $rootScope, toaster) {

			var form = $scope;

			form.modalCancel = function() {
				$modalInstance.dismiss('cancel');
			};

			form.getVendorList = function() {
				RequestService
						.doGET(
								'/procurement/vendor/vendorServices/getVendorListByStatusForBlacklist/1')
						.then(function(data, status, headers, config) {
							form.vendorList = data;
						});
			}

			form.getVendorList();

			form.getData = function(vendor) {
				vm.blacklistVendorDtl.blacklistVendor.vendor = vendor;
				$modalInstance.dismiss('cancel');
			}
		};
		AddModalInstance.$inject = [ 'RequestService', 'ModalService',
				'$scope', '$modalInstance', '$rootScope', 'toaster' ];

		$scope.addAlasan = function() {
			var addModalInstanceAlasan = $modal.open({
				templateUrl : '/findAlasanBlacklist.html',
				controller : AddModalInstanceAlasan,
				size : 'lg'
			});
		}

		var AddModalInstanceAlasan = function(RequestService, ModalService,
				$scope, $modalInstance, $rootScope, toaster) {

			var form = $scope;

			form.modalCancel = function() {
				$modalInstance.dismiss('cancel');
			};

			form.getAlasanBlacklistList = function() {

				RequestService
						.doGET(
								'/procurement/master/AlasanBlacklistServices/getList')
						.then(
								function successCallback(data) {
									form.alasanBlacklistList = data;
									form.loading = false;

									if (vm.alasanBlacklistList.length > 0) {
										form.alasanBlacklistList
												.forEach(function(
														alasanBlacklist, index,
														array) {
													vm.alasanBlacklistList
															.forEach(function(
																	alasan,
																	index,
																	array) {
																if (alasan.alasanBlacklistId == alasanBlacklist.alasanBlacklistId) {
																	alasanBlacklist.selected = '1';
																	console
																			.info('daed')
																}
															})
												});
									}
								},
								function errorCallback(response) {
									ModalService
											.showModalInformation('Terjadi kesalahan pada system!');
									form.loading = false;
								});
			}

			form.getAlasanBlacklistList();

			form.getData = function() {
				vm.alasanBlacklistList = [];
				form.alasanBlacklistList.forEach(function(alasanBlacklist,
						index, array) {
					if (alasanBlacklist.selected == 1) {
						vm.alasanBlacklistList.push(alasanBlacklist);
					}
				});
			}
		};
		AddModalInstanceAlasan.$inject = [ 'RequestService', 'ModalService',
				'$scope', '$modalInstance', '$rootScope', 'toaster' ];

		$scope.del = function(id) {
			var check = false;
			var index = 0;
			vm.alasanBlacklistList.forEach(function(alasanBlacklist, index,
					array) {
				if (alasanBlacklist.alasanBlacklistId == id) {
					check = true;
				}
				index++;
			});

			if (check == true) {
				vm.alasanBlacklistList.splice(index, 1);
			}
		}

		$scope.back = function() {
			$state
					.go('app.promise.procurement-manajemenvendor-blacklistvendor');
		}
	}

	BlacklistAddController.$inject = [ '$stateParams', '$modal',
			'ModalService', 'RequestService', '$scope', '$http', '$rootScope',
			'$resource', '$location', '$state', '$q', 'toaster', 'FileUploader' ];

})();