// Please prepare your logic for this code or welcome to the dizzy world
// ----------------------------------------------------------------------

(function() {
    'use strict';

    angular.module('naut').controller('KriteriaEvaluasiSatuanController', KriteriaEvaluasiSatuanController);
    
    function KriteriaEvaluasiSatuanController($scope, $http, $rootScope, $resource, $location, toaster, $window, ngTableParams) {
        var vm = this;
        
        var sistemEvaluasi = $rootScope.detilPengadaan;
        
        // put the session data to the table
        if($rootScope.item != 'undefined') {
            if($rootScope.isiListAdminFromTable.length > 0){
                for(var i=0; i<$rootScope.isiListAdminFromTable.length; i++){
                    if($rootScope.item == $rootScope.isiListAdminFromTable[i].itemPengadaan.id){
                        vm.listAdmin = $rootScope.isiListAdminFromTable;
                    } 
                }
                for(var k=0; k<$rootScope.isiListTeknisFromTable.length; k++){
                    if($rootScope.item == $rootScope.isiListTeknisFromTable[k].itemPengadaan.id){
                        vm.listTeknis = $rootScope.isiListTeknisFromTable;
                    }
                }
                for(var j=0; j<$rootScope.isiListAmbangFromTable.length; j++){
                    if($rootScope.item == $rootScope.isiListAmbangFromTable[j].itemPengadaan.id){
                        vm.batasAtas = $rootScope.isiListAmbangFromTable[j].atas;
                        vm.batasBawah = $rootScope.isiListAmbangFromTable[j].bawah;
                        vm.persenAdmin = $rootScope.isiListAmbangFromTable[j].presentasiNilaiAdmin;
                        vm.persenTeknis = $rootScope.isiListAmbangFromTable[j].presentasiNilaiTeknis;
                        vm.persenHarga = $rootScope.isiListAmbangFromTable[j].presentasiNilaiHarga;
                    }
                }
            } else {
                for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++) {
                    var hasilList = $rootScope.kriteriaEvaluasiSatuan[i];
                    if($rootScope.item == hasilList.itemId){
                        vm.listAdmin = hasilList.kriteriaAdmin;
                        vm.listTeknis = hasilList.kriteriaTeknis;
                        vm.batasAtas = hasilList.batasAtas;
                        vm.batasBawah = hasilList.batasBawah;
                        vm.persenAdmin = hasilList.persenAdmin;
                        vm.persenTeknis = hasilList.persenTeknis;
                        vm.persenHarga = hasilList.persenHarga;
                    }
                }
            }
        }
        
        // Get Administration Criteria List for View Only
        $http.get($rootScope.backendAddress + '/procurement/kriteriaAdministrasiServices/getList', {
            ignoreLoadingBar: true
        }).success(function(data, status, headers, config) {
            vm.kriteriaAdministrasiList = data;
        }).error(function(data, status, headers, config) {
            console.log('Error on calling getList');
        });
        
        // back to view detail pengadaan
        vm.backView = function(){
			$location.path('/app/promise/procurement/kriteriaEvaluasi/viewSatuan');
            console.log('$rootScope.kriteriaEvaluasiSatuan : ' + JSON.stringify($rootScope.kriteriaEvaluasiSatuan));
		};
        
        // remove kriteria from table
        // 1. remove kriteria Administrasi
        $scope.removeKriteriaAdmin = function (kriteriaAdmin) {
            for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId){
                    console.log('INFO obj : ' + JSON.stringify(kriteriaAdmin));
                    var index = $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin.indexOf(kriteriaAdmin);
                    $rootScope.kriteriaEvaluasiSatuan[i].kriteriaAdmin.splice(index, 1);
                }
            }
			pPengadaan.backView();
		}
        // 2. remove kriteria Teknis
        $scope.removeKriteriaTeknis = function (kriteriaTeknis) {
            for(var i=0; i<$rootScope.kriteriaEvaluasiSatuan.length; i++){
                if($rootScope.item == $rootScope.kriteriaEvaluasiSatuan[i].itemId){
                    console.log('INFO obj : ' + JSON.stringify(kriteriaTeknis));
                    var index = $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis.indexOf(kriteriaTeknis);
                    $rootScope.kriteriaEvaluasiSatuan[i].kriteriaTeknis.splice(index, 1);
                }
            }
			pPengadaan.backView();
		}
        
        //URL Link
		vm.go = function (path) {
			$location.path(path);
		}; 
    }

    KriteriaEvaluasiSatuanController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$window', 'ngTableParams'];

})();