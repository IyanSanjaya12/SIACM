(function() {
    'use strict';

    angular.module('naut')
		.controller('KebutuhanMaterialTambahSMBController', KebutuhanMaterialTambahSMBController)
        .filter('propsFilter', propsFilter)
		.filter('formatMoney', formatMoney);

    function KebutuhanMaterialTambahSMBController($scope, $http, $rootScope, $resource, $location, toaster, $filter) {

        var kebutuhanMaterialTambah = this;
        var vm = this;

        vm.go = function(path) {
            $location.path(path);
        };

        kebutuhanMaterialTambah.back = function() {
            $location.path('/app/promise/procurement/inisialisasi/add/smb');
        };
		
		$scope.kodeMaterial = {};
		$scope.refreshkodeMaterialList = function(kodeMaterial) {
			$scope.loading = true;
			if(kodeMaterial.length == 0)
				kodeMaterial = 0;
			
			return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/'+kodeMaterial)
				.then(function(response) {
					$scope.kodeMaterialList = response.data;
					$scope.loading = false;
				});
		};
		$scope.setKodeMaterialSelected = function(){
			if(typeof $scope.kodeMaterial.selected !== 'undefined'){
				vm.namaMaterial = $scope.kodeMaterial.selected.nama;
				//console.log('INFO : '+JSON.stringify($scope.kodeMaterial.selected));
				vm.jenisMaterial = $scope.kodeMaterial.selected.itemGroupId.nama;
				vm.satuan = $scope.kodeMaterial.selected.satuanId.nama;
			}
		};
		
		//Currency
		$scope.mataUang = {};
        $http.get($rootScope.backendAddress + '/procurement/master/mata-uang/get-list')
            .success(function(data, status, headers, config) {
                $scope.mataUangList = data;	
				//console.log(' INFO ' + JSON.stringify(data));
				$scope.mataUang.selected = {id : data[0].id, nama:data[0].nama, kode:data[0].kode};
            }).error(function(data, status, headers, config) {});
		//HPS
		$scope.calculateTotalHPS = function() {
			if(typeof vm.kuantitas !== 'undefined' && typeof vm.hps !== 'undefined'){
				vm.totalHps = vm.kuantitas * vm.hps;
				if($scope.mataUang.selected.id == 1) { //Rupiah Default
					vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, ',', '.');
				} else {
					vm.totalHpsTxt = $filter('formatMoney')(vm.totalHps, 2, '.', ',');
				}
			}
		}
		//Edit
		$scope.setEditMaterial = function(){
			if(typeof $rootScope.materialEditId !== 'undefined'){
				$scope.materialObj = $rootScope.inisialisasiForm.materialList[$rootScope.materialEditId];
				$scope.kodeMaterial.selected = $scope.materialObj.material;
				vm.kuantitas = $scope.materialObj.kuantitas;
				vm.hps = $scope.materialObj.hps;
				$scope.mataUang.selected = $scope.materialObj.mataUang;
				vm.keterangan = $scope.materialObj.keterangan;
				$scope.calculateTotalHPS();
				vm.namaMaterial = $scope.kodeMaterial.selected.nama;
				vm.jenisMaterial = $scope.kodeMaterial.selected.itemGroupId.nama;
				vm.satuan = $scope.kodeMaterial.selected.satuanId.nama;
			}
		};
		$scope.setEditMaterial();
		
		//Simpan Material
		$scope.btnSimpanMaterial = function(){
			$scope.kodeMaterialError = false;
			$scope.kuantitasError = false;
			$scope.kuantitasError01 = false;
			$scope.hpsError = false;
			$scope.hpsError01 = false;
			if(typeof $scope.kodeMaterial.selected === 'undefined'){				
				$scope.kodeMaterialError = true;
			} else {
				if(typeof vm.kuantitas === 'undefined'){
					$scope.kuantitasError = true;
				} else {
					if(vm.kuantitas <= 0){
						$scope.kuantitasError01 = true;
					} else {
						if(typeof vm.hps === 'undefined'){
							$scope.hpsError = true;
						} else {
							if(vm.hps <= 0){								
								$scope.hpsError01 = true;
							} else {
								$scope.materialObj = {
									material : $scope.kodeMaterial.selected,
									kuantitas : vm.kuantitas,
									hps : vm.hps,
									mataUang : $scope.mataUang.selected,
									keterangan : vm.keterangan,
                                    totalHps:vm.totalHps
								};
								//console.log('INFO length materialObj : '+JSON.stringify($scope.materialObj));
								if(typeof $rootScope.materialEditId !== 'undefined'){
									$rootScope.inisialisasiForm.materialList[$rootScope.materialEditId] = $scope.materialObj;
								} else {
									$rootScope.inisialisasiForm.materialList = $rootScope.inisialisasiForm.materialList.concat($scope.materialObj);									}
								$location.path('/app/promise/procurement/inisialisasi/add/smb');
							}
						}
					}
				}
			}
		};		
    }

    KebutuhanMaterialTambahSMBController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter'];
	
	function formatMoney(){
		return function(number, decimals, dec_point, thousands_sep) {
			 var n = !isFinite(+number) ? 0 : +number, 
				prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
				sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
				dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
				toFixedFix = function (n, prec) {
					// Fix for IE parseFloat(0.55).toFixed(0) = 0;
					var k = Math.pow(10, prec);
					return Math.round(n * k) / k;
				},
				s = (prec ? toFixedFix(n, prec) : Math.round(n)).toString().split('.');
			if (s[0].length > 3) {
				s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
			}
			if ((s[1] || '').length < prec) {
				s[1] = s[1] || '';
				s[1] += new Array(prec - s[1].length + 1).join('0');
			}
			return s.join(dec);
		}
	};
	
	function propsFilter() {
      return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
          items.forEach(function(item) {
            var itemMatches = false;

            var keys = Object.keys(props);
            for (var i = 0; i < keys.length; i++) {
              var prop = keys[i];
              var text = props[prop].toLowerCase();
              if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                itemMatches = true;
                break;
              }
            }

            if (itemMatches) {
              out.push(item);
            }
          });
        } else {
          // Let the output be the input untouched
          out = items;
        }

        return out;
      };
    }
})();