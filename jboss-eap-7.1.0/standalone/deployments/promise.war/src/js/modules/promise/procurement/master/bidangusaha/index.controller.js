/**=========================================================
 * Module: BidangUsahaController.js
 =========================================================*/

(function () {
	'use strict';

	angular
		.module('naut')
		.controller('BidangUsahaController', BidangUsahaController);

	function BidangUsahaController($http, $rootScope, $scope, $resource, $location, $timeout, toaster) {
		var form = this;

		$scope.my_tree_handler = function (branch) {
			$scope.statusEdit = false;
            //console.log("branch");
            //console.log(branch);
			$scope.output = branch.label;
			form.nama = branch.label;
			form.id = branch.id;
			form.parentId = branch.parentId;

			if (branch && branch.label) {
				$scope.output += '(' + branch.label + ')';
				return $scope.output;
			}
		};

		var tree;
		// This is our API control variable
		$scope.my_tree = tree = {};
		$scope.try_async_load = function () {

			$scope.my_data = [];
			$scope.doing_async = true;

			// Request tree data via $resource
			var remoteTree = $resource($rootScope.backendAddress + '/procurement/master/bidang-usaha/getAll');

			//var remoteTree = $resource('server/treeorganisasi.json');

			return remoteTree.get(function (res) {

				$scope.my_data = res.data;

				$scope.doing_async = false;

				return tree.expand_all();

				// we must return a promise so the plugin 
				// can watch when it's resolved
			}).$promise;
		};
		$scope.try_async_load();

		// Adds a new branch to the tree
		$scope.try_adding_a_branch = function () {
			var b;
			b = tree.get_selected_branch();
			return tree.add_branch(b, {
				label: 'New Branch',
				parentId: b.id
			});
		};

		$scope.btnBatal = function () {
			$scope.try_async_load();
			$scope.statusEdit = false;
		}
		
		$scope.batal = function () {
			$scope.try_async_load();
			$scope.statusTambah = false;
		}

		$scope.btnSimpan = function () {
			//console.log('SIMPAN OBJ : ' + JSON.stringify(form));
			$scope.loading = true;

			if (typeof form.nama === 'undefined' || form.nama == '') {
				alert("Nama Bidang Usaha tidak boleh kosong.");
			} else {
				var url = "";
				if (typeof form.id !== 'undefined') {
					url = $rootScope.backendAddress + '/procurement/master/bidang-usaha /updateBidangUsaha';
				} else {
					url = $rootScope.backendAddress + '/procurement/master/bidang-usaha/createBidangUsaha';
				}
				$http({
					method: 'POST',
					url: url,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					transformRequest: function (obj) {
						var str = [];
						for (var p in obj)
							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
						return str.join("&");
					},
					data: form
				}).success(function (data, status, headers, config) {
					toaster.pop('success', 'Bidang Usaha', 'Simpan ' + data.nama + ', berhasil.');
					$scope.try_async_load();
					$scope.loading = false;
				});
			}
		};
		
		$scope.simpan = function () {
			//console.log('SIMPAN OBJ : ' + JSON.stringify(form));
			$scope.loading = true;

			if (typeof form.namaBidangUsaha === 'undefined' || form.namaBidangUsaha == '') {
				alert("Nama Bidang Usaha tidak boleh kosong.");
			} else {
				
				$http.get($rootScope.backendAddress + '/procurement/master/bidang-usaha/get-bidang-usaha-by-nama/'+form.namaBidangUsaha, {
                    ignoreLoadingBar: true
                }).success(function (data, status, headers, config) {
                	if (data != '') {
                		alert("Nama Bidang Usaha tidak boleh sama.");
                	} else {
                		var url = $rootScope.backendAddress + '/procurement/master/bidang-usaha/createBidangUsahaBaru';
        				
        				$http({
        					method: 'POST',
        					url: url,
        					headers: {
        						'Content-Type': 'application/x-www-form-urlencoded'
        					},
        					transformRequest: function (obj) {
        						var str = [];
        						for (var p in obj)
        							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        						return str.join("&");
        					},
        					data: form
        				}).success(function (data, status, headers, config) {
        					toaster.pop('success', 'Bidang Usaha', 'Simpan ' + data.nama + ', berhasil.');
        					form.namaBidangUsaha = '';
        					$scope.statusTambah = false;
        					$scope.try_async_load();
        					$scope.loading = false;
        				});
                	}
                	$scope.loading = false;
                	
                }).error(function (data, status, headers, config) {
                    $scope.loading = false;
                })
				
			}
		};

		$scope.btnHapus = function () {
			if (typeof form.id !== 'undefined') {
				console.log('DELETE INFO  : '+$rootScope.backendAddress + '/procurement/master/bidang-usaha/delete-bidang-usaha/' + form.id);
				var konfirmasi = confirm("Apakah anda yakin ingin menghapus?");
				if (konfirmasi) {
					$scope.loading = true;
					$http.get($rootScope.backendAddress + '/procurement/master/bidang-usaha/delete-bidang-usaha/' + form.id).success(function (data, status, headers, config) {
						toaster.pop('success', 'Bidang Usaha', 'Hapus ' + data.nama + ', berhasil.');
						$scope.try_async_load();
                        $scope.output = undefined;
						$scope.loading = false;
					}).error(function (data, status, headers, config) {});
				}
			}
		}

	}

	BidangUsahaController.$inject = ['$http', '$rootScope', '$scope', '$resource', '$location', '$timeout', 'toaster'];

})();