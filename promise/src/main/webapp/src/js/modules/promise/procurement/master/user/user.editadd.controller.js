(function() {
	'use strict';

	angular.module('naut').controller('UserEditAddController', UserEditAddController);

	function UserEditAddController($scope, $http, $rootScope, $resource, $location, toaster, $state, $stateParams, RequestService) {

		$scope.loading = true;
		$scope.saving = false;
		

		var form = this;

		form.status = [ {
			id : 0,
			nama : 'Aktif'
		}, {
			id : 1,
			nama : 'Terkunci'
		} ];

		var isAddNewUser = $stateParams.user == null;
		$scope.title = isAddNewUser? "Add":"Edit";
		
		// get Role List PM
		RequestService.doGET('/procurement/master/role/get-role-by-application/' + "PM").then(function(data) {
			form.roleListPM = data;
			// get Role List CM
			RequestService.doGET('/procurement/master/role/get-role-by-application/' + "CM").then(function(data) {
				form.roleListCM = data;
				// get departement
				RequestService.doGET('/procurement/master/organisasi/get-all').then(function(data) {
					form.organisasiTree = data.data;
					$scope.init();
				});

			});
		});

		$scope.init = function() {

			if (isAddNewUser) {
				form.user = {};
				form.selectedOrganisasi = {};
				$scope.loading = false;
			} else {
				form.user = $stateParams.user;

				RequestService.doGET('/procurement/master/roleUserServices/getListByUserId/' + form.user.id).then(function(data) {
					if (data.length > 0) {

						angular.forEach(data, function(value) {
							if (value.role.appCode == 'PM') {
								form.user.loginPm = true;
								form.user.rolePm = value.role;
								console.log("ROLE PM");
							}

							else if (value.role.appCode == 'CM') {
								form.user.loginCm = true;
								form.user.roleCm = value.role;
								console.log("ROLE CM");
							}

							if ((!angular.isUndefined(value.organisasi)) && value.organisasi != null) {
								form.user.organisasi = {
									id : value.organisasi.id,
									label : value.organisasi.nama,
									parentId : value.organisasi.parentId
								};
							}
						})

						form.user.terkunci = form.status[form.user.terkunci];
						form.user.password = null;
						form.user.rePassword = null;
						$scope.loading = false;
					}
				});
			}
		}

		var validate = function() {

			/* Trimming */
			if (form.user.username) {
				form.user.username = form.user.username.trim()
			}
			;
			if (form.user.namaPengguna) {
				form.user.namaPengguna = form.user.namaPengguna.trim()
			}
			;
			if (form.user.email) {
				form.user.email = form.user.email.trim()
			}
			;

			/* Validation */
			if (!form.user.username) {
				alert('Silahkan masukkan username!');
				document.getElementsByName("form.user.username")[0].focus();
				return false;
			} else if (!form.user.namaPengguna) {
				alert('Silahkan masukkan nama pengguna!');
				document.getElementsByName("form.user.namaPengguna")[0].focus();
				return false;
			} else if (isAddNewUser && (!form.user.password)) {
				alert('Silahkan masukan Password');
				document.getElementsByName("form.user.password")[0].focus();
				return false;

			} else if ((form.user.password) && (angular.equals(form.user.password, form.user.rePassword) == false)) {
				alert('Konfirmasi Password tidak cocok');
				document.getElementsByName("form.user.rePassword")[0].focus();
				return false;
			} else if (!form.user.email) {
				alert('Silahkan isi alamat email!');
				document.getElementsByName("form.user.email")[0].focus();
				return false;
			} else if (!form.user.organisasi) {
				alert('Silahkan pilih organisasi!');
				return false;
			} else if ((!form.user.loginPm) && (!form.user.loginCm)) {
				alert('Pilih role user!');
				return false;
			} else if (form.user.loginPm && !form.user.rolePm) {
				document.getElementsByName("form.user.loginPM")[0].focus();
				form.user.rolePMStatus = true;
				return false;
			} else if (form.user.loginCm && !form.user.roleCm) {
				document.getElementsByName("form.user.loginCM")[0].focus();
				form.user.roleCMStatus = true;
				return false;
			} else if (!form.user.terkunci) {
				alert('Pilih status!');
				return false;
			}

			return true;
		}

		$scope.simpan = function() {
			if (!validate()) {
				return;
			}

			$scope.saving = true;
			RequestService.doGET('/procurement/user/getUserByUserName/' + form.user.email).then(function(data) {
				if (data.length == 0 || ((data.length > 0) && (!isAddNewUser) && (data[0].id == form.user.id))) {
					RequestService.doGET('/procurement/user/get-by-real-username/' + form.user.username).then(function(data) {
						if (data.length == 0 || ((data.length > 0) && (!isAddNewUser) && (data[0].id == form.user.id))) {
							$scope.doSimpan();
						} else {
							$scope.saving = false;
							alert('Username sudah ada!');
							document.getElementsByName("form.user.username")[0].focus();
							return false;
						}
					});

				} else {
					$scope.saving = false;
					alert('Email sudah ada!');
					document.getElementsByName("form.user.email")[0].focus();
					return false;
				}
			});

		}

		$scope.doSimpan = function() {

			var postForm = {
				username : form.user.username,
				organisasiId : form.user.organisasi.id,
				namaPengguna : form.user.namaPengguna,
				email : form.user.email,
				terkunci : form.user.terkunci.id
			}

			if (!isAddNewUser) {
				postForm.id = form.user.id;
			}

			if ((form.user.password) && (form.user.password.length > 0)) {
				postForm.password = form.user.password;
			}

			if (form.user.loginPm) {
				if ((!angular.isUndefined(form.user.rolePm)) && form.user.rolePm != null) {
					postForm.roleIdPM = form.user.rolePm.id;
				}
			}

			if (form.user.loginCm) {
				if ((!angular.isUndefined(form.user.roleCm)) && form.user.roleCm != null) {
					postForm.roleIdCM = form.user.roleCm.id;
				}
			}

			console.log(JSON.stringify(postForm));

			RequestService.doPOST(isAddNewUser ? '/procurement/user/create' : '/procurement/user/update', postForm).success(function(data, status, headers, config) {
				$scope.saving = false;
				RequestService.informSaveSuccess();
				$state.go('app.promise.procurement-master-user');
			});

		}

		$scope.kembali = function() {
			$location.path('/app/promise/procurement/master/user');
		}

	}
	UserEditAddController.$inject = [ '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$state', '$stateParams', 'RequestService' ];

})();
