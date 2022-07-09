// Please prepare your logic for this code or welcome to the dizzy world
// ----------------------------------------------------------------------
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('KriteriaEvaluasiTambahSatuanController', KriteriaEvaluasiTambahSatuanController);
    
    function KriteriaEvaluasiTambahSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $modal, ngTableParams) {
        var vm = this;
        vm.adminCheck = [];
        vm.teknisCheck = [];
        vm.dataKriteria = [];
        vm.kriteriaAdmin = {};
        vm.kriteriaTeknis = {};
        
        // regular Expression for number's input
        $scope.numberPattern = /^[0-9]*$/;
        
        // put Master Kriteria Administrasi to check point table
        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaAdministrasiServices/getList', {
            ignoreLoadingBar: true
        }).success(
            function (data, status, headers, config) {
                vm.masterKriteriaAdministrasiList = data;
                if($rootScope.kriteriaEvaluasiSatuan.length > 0){
                    for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                        if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId) {
                            vm.kriteriaAdmin = $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin;
                            //console.log("isi session kriteria Admin : " + JSON.stringify(vm.kriteriaAdmin));
                        }
                    }
                    for(var j=0; j<vm.kriteriaAdmin.length; j++){
                        for(var k=0; k<vm.masterKriteriaAdministrasiList.length; k++){
					       if(vm.kriteriaAdmin[j].id == vm.masterKriteriaAdministrasiList[k].id){	
						      vm.masterKriteriaAdministrasiList.splice(     vm.masterKriteriaAdministrasiList.indexOf(vm.masterKriteriaAdministrasiList[k]), 1 );
					       }
				        }
			         }
		          }
                vm.tableAdminList = new ngTableParams({
                    page: 1,            // show first page
                    count: 7           // count per page
                }, {
                    total: vm.masterKriteriaAdministrasiList.length, // length of data4
                    getData: function($defer, params) {
                        $defer.resolve(vm.masterKriteriaAdministrasiList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {});
        
        // put Master Kriteria Teknis to check point table
        $http.get($rootScope.backendAddress + '/procurement/master/kriteriaTeknisServices/getList', {
            ignoreLoadingBar: true
        }).success(
            function (data, status, headers, config) {
                vm.masterKriteriaTeknisList = data;
                if($rootScope.kriteriaEvaluasiSatuan.length > 0){
                    for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                        if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId) {
                            vm.kriteriaTeknis = $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis;
                        }
                    }
                    for(var j=0; j<vm.kriteriaTeknis.length; j++){
                        for(var k=0; k<vm.masterKriteriaTeknisList.length; k++){
					       if(vm.kriteriaTeknis[j].id == vm.masterKriteriaTeknisList[k].id){	
						      vm.masterKriteriaTeknisList.splice(     vm.masterKriteriaTeknisList.indexOf(vm.masterKriteriaTeknisList[k]), 1 );
					       }
				        }
			         }
		          }
                vm.tableTeknisList = new ngTableParams({
                    page: 1,            // show first page
                    count: 7           // count per page
                }, {
                    total: vm.masterKriteriaTeknisList.length, // length of data4
                    getData: function($defer, params) {
                        $defer.resolve(vm.masterKriteriaTeknisList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }).error(function (data, status, headers, config) {});
        
        // put data harga from $rootScope to textbox
        if($rootScope.kriteriaEvaluasiSatuan.length > 0){
            for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId) {
                    vm.batasAtas = $rootScope.kriteriaEvaluasiSatuan[i].batasAtas;
                    vm.batasBawah = $rootScope.kriteriaEvaluasiSatuan[i].batasBawah;
                }
            }
        }
        
        // for save the data from checkbox list to session
        $scope.changeAdminCheck = function(list, active) {
            if(active) {
                vm.adminCheck.push(list);
            } else {
                vm.adminCheck.splice(vm.adminCheck.indexOf(list), 1);
            }
        }
        $scope.changeTeknisCheck = function(list, active) {
            if(active) {
                vm.teknisCheck.push(list);
            } else {
                vm.teknisCheck.splice(vm.teknisCheck.indexOf(list), 1);
            }
        }
        
        // check data binding in session for update
        $scope.checkDataBinding = function() {
            if($rootScope.kriteriaEvaluasiSatuan.length > 0) {
                for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                    if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId) {
                        var flagTambah = true;
                        if(vm.adminCheck.length > 0) {
                            for(var j=0; j<vm.adminCheck.length; j++){
                                $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin.push(vm.adminCheck[j]);
                            }
                        }
                        if(vm.teknisCheck.length > 0) {
                            for(var k=0; k<vm.teknisCheck.length; k++){
                                $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis.push(vm.teknisCheck[k]);
                            }
                        }
                        $rootScope.kriteriaEvaluasiSatuan[i].batasAtas = vm.batasAtas;
                        $rootScope.kriteriaEvaluasiSatuan[i].batasBawah = vm.batasBawah;
                    }
                }
                if(!flagTambah) {
                    $scope.bindingData();
                }
            } else {
                $scope.bindingData();
            }
            console.log("isi session satuan : " + JSON.stringify($rootScope.kriteriaEvaluasiSatuan));
        }
        
        // binding data with item to session
        $scope.bindingData = function() {
            vm.dataKriteria = {
                itemId: $rootScope.item,
                kriteriaAdmin: vm.adminCheck,
                kriteriaTeknis: vm.teknisCheck,
                batasAtas: vm.batasAtas,
                batasBawah: vm.batasBawah
            }
            $rootScope.kriteriaEvaluasiSatuan.push(vm.dataKriteria);
        }
        
        //validation of check Kriteria
        $scope.getTab01Validation = function () {
            var validationTab01 = false;
            if($rootScope.kriteriaEvaluasiSatuan.length > 0){
                for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                    if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId){
                        validationTab01 = true;
                        break;
                    } else {
                        validationTab01 = vm.validationCheck(vm.adminCheck);
                    }
                }
            } else {
                validationTab01 = vm.validationCheck(vm.adminCheck);
            }
            return validationTab01;
        }
        $scope.getTab02Validation = function () {
            var validationTab02 = false;
			if($rootScope.kriteriaEvaluasiSatuan.length > 0){
                for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                    if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId){
                        validationTab02 = true;
                        break;
                    } else {
                        validationTab02 = vm.validationCheck(vm.teknisCheck);
                    }
                }
            } else {
                validationTab02 = vm.validationCheck(vm.teknisCheck);
            }
            return validationTab02;
        }
        vm.validationCheck = function(object) {
            var valid = false;
            if(object.length <= 0){
                alert("Minimal 1 Kriteria di pilih!");
            } else {
                valid = true;
            }
            return valid;
        }
        
		//URL Link
		vm.go = function (path) {
			$location.path(path);
		}
    }
    
    KriteriaEvaluasiTambahSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', 'ngTableParams'];

})();