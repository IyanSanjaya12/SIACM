// Please prepare your logic for this code or welcome to the dizzy world
// ----------------------------------------------------------------------
(function () {
	'use strict';

	angular
		.module('naut')
		.controller('KriteriaEvaluasiTambahSatuanMeritController', KriteriaEvaluasiTambahSatuanMeritController);
    
    function KriteriaEvaluasiTambahSatuanMeritController($scope, $http, $rootScope, $resource, $location, toaster, $modal, ngTableParams) {
        var vm = this;
        vm.adminCheck = [];
        vm.teknisCheck = [];
        vm.dataKriteria = [];
        vm.kriteriaAdmin = {};
        vm.kriteriaTeknis = {};
        $scope.valueTotal = 0;
        
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
                    vm.persenAdmin = $rootScope.kriteriaEvaluasiSatuan[i].persenAdmin;
                    vm.persenHarga = $rootScope.kriteriaEvaluasiSatuan[i].persenHarga;
                    vm.persenTeknis = $rootScope.kriteriaEvaluasiSatuan[i].persenTeknis;
                    $scope.valueMaks = $rootScope.kriteriaEvaluasiSatuan[i].valueMaks;
                }
            }
        }
        
        // for save the data from checkbox list to session
        $scope.changeAdminCheck = function(list, active) {
            if(active) {
                vm.adminCheck.push(list);
                //console.log(JSON.stringify(vm.adminCheck));
            } else {
                vm.adminCheck.splice(vm.adminCheck.indexOf(list), 1);
                //console.log(JSON.stringify(vm.adminCheck));
            }
        }
        $scope.changeTeknisCheck = function(list, active) {
            if(active) {
                vm.teknisCheck.push(list);
                //console.log(JSON.stringify(vm.teknisCheck));
            } else {
                if(list.nilai != undefined){
                    $scope.valueTotal = $scope.valueTotal - list.nilai;
                }
                vm.teknisCheck.splice(vm.teknisCheck.indexOf(list), 1);
                //console.log(JSON.stringify(vm.teknisCheck));
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
                        $rootScope.kriteriaEvaluasiSatuan[i].valueTotal = $scope.valueTotal;
                        $rootScope.kriteriaEvaluasiSatuan[i].valueMaks = $scope.valueMaks;
                        $rootScope.kriteriaEvaluasiSatuan[i].persenAdmin = vm.persenAdmin;
                        $rootScope.kriteriaEvaluasiSatuan[i].persenTeknis = vm.persenTeknis;
                        $rootScope.kriteriaEvaluasiSatuan[i].persenHarga = vm.persenHarga;
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
                valueTotal: $scope.valueTotal,
                valueMaks: $scope.valueMaks,
                batasAtas: vm.batasAtas,
                batasBawah: vm.batasBawah,
                persenAdmin: vm.persenAdmin,
                persenTeknis: vm.persenTeknis,
                persenHarga: vm.persenHarga
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
                        validationTab02 = vm.checkNilaiMaks();
                        break;
                    } else {
                        validationTab02 = vm.validationCheck(vm.teknisCheck);
                    }
                }
            } else {
                validationTab02 = vm.validationCheck(vm.teknisCheck);
                validationTab02 = vm.checkNilaiMaks();
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
        vm.checkNilaiMaks = function() {
            var valid = false;
            if($scope.valueMaks == undefined || $scope.valueMaks < 0){
                alert("Nilai Pembobotan Maksimum belum terisi");
                valid = false;
            } else {
                valid = true;
            }
            return valid;
        }
        
        //validation maksimum value from textbox Nilai Pembobotan Maksimal
        $scope.checkNilai = function(value) {
            $scope.valueMaks = value;
            if(value <= 0 || value > 100){
                alert("Nilai maksimal harus diisi lebih dari 0 dan Maksimal 100");
            } else {
                $rootScope.kriteriaEvaluasi.nilaiMaksTeknis = value;
            }
        }
        
        //set data nilai to input box binding with check box
        $scope.setNilai = function(list, nilai){
            for(var i=0; i<vm.teknisCheck.length; i++){
                if(vm.teknisCheck[i].id == list.id){
                    if(vm.teknisCheck[i].nilai != undefined){
                        $scope.valueTotal = $scope.valueTotal - vm.teknisCheck[i].nilai;
                        vm.teknisCheck[i].nilai = nilai;
                        $scope.valueTotal = $scope.valueTotal + nilai;
                    } else {
                        var coba2 = {
                            nilai: nilai
                        };
                        angular.extend(vm.teknisCheck[i], coba2); //for added property to the list
                        $scope.valueTotal = $scope.valueTotal + nilai;
                    }
                }
            }
            console.log(JSON.stringify(vm.teknisCheck));
        }
        
		//URL Link
		vm.go = function (path) {
			$location.path(path);
		}
    }
    
    KriteriaEvaluasiTambahSatuanMeritController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$modal', 'ngTableParams'];

})();