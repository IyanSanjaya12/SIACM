(function () {
    'use strict';

    angular.module('naut').controller('AlurPengadaanUbahController', AlurPengadaanUbahController);

    function AlurPengadaanUbahController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, toaster) {
    	var form = this;
        form.alurPengadaan = $rootScope.alurPengadaanData;
		form.models = {
			tahapanPengadaanList : [],
			tahapanList : []
		};
        
        form.getKondisiPengadaan = function () {  
        	
			RequestService.doGET('/procurement/master/kondisiPengadaanServices/getList')
			.then(function (data, status, headers, config) { 
				form.kondisiPengadaanList = data;
				form.loading = false;
			}); 
		}
        
        form.getKondisiPengadaan();
        
        form.getTahapanPengadaanByAlur = function () { 
			RequestService.doGET('/procurement/pengadaan/tahapanPengadaanServices/getListByAlurId/' + form.alurPengadaan.id)
			.then(function (data, status, headers, config) {  
				data.forEach(function(value, index, array) {
					form.models.tahapanPengadaanList.push(value.tahapan);
				});
				
				RequestService.doGET('/procurement/master/tahapan/get-list')
				.then(function (dataV, statusV, headersV, configV) { 
					form.tahapanList = dataV;  

					form.tahapanList.forEach(function(valueF, index, array) {
						var check = true;
						form.models.tahapanPengadaanList.forEach(function(value, index, array) {
							if(value.id == valueF.id){
								check = false;
							}							
						});						
						if(check == true){
							form.models.tahapanList.push(valueF);
						}
					});					
				}); 
			}); 
		}
        
        form.getTahapanPengadaanByAlur(); 
        
        form.kembali = function () {
            $location.path('/app/promise/procurement/master/alurPengadaan');
        }

        form.simpan = function () {
            if (typeof form.alurPengadaan.nama === 'undefined' || form.alurPengadaan.nama == '') {
				toaster.pop('error', 'Alur Pengadaan', 'Nama alur pengadaan tidak boleh kosong!');
            } else if (typeof form.alurPengadaan.kondisiPengadaan === 'undefined') {
				toaster.pop('error', 'Alur Pengadaan', 'Kondisi pengadaan harus dipilih!');
            } else {
            	if(form.models.tahapanPengadaanList.length > 0){

                    ModalService.showModal().then(function (result) {
    					var data = {};
    					data.alurPengadaan = form.alurPengadaan;
    					data.tahapanList = form.models.tahapanPengadaanList;
    					
    					ModalService.showModalInformationBlock(); 
    					
    					RequestService.doPOSTJSON('/procurement/master/AlurPengadaanServices/updateAll', data)
    					.then(function successCallback(data) {
    			            $location.path('app/promise/procurement/master/alurPengadaan');
    						ModalService.closeModalInformation();
    					}, function errorCallback(response) {				 
    						ModalService.closeModalInformation();
    						ModalService.showModalInformation('Terjadi kesalahan pada system!');
    					});				
    				});
            	}else{
    				toaster.pop('error', 'Alur Pengadaan', 'Silahkan pilih tahapan pengadaan!');
            	}
            };
        }; 
        
        form.currentDropElement = null;

        form.remove = function(l, o) {
			var index = l.indexOf(o);
			if (index > -1) {
				l.splice(index, 1);
			}
		};

		form.onDragStart = function() {

		};

		form.onDragLeave = function(data) {

		};

		form.onDragOver = function(data, dragElement, dropElement) {
			form.currentDropElement = dropElement;
		};

		form.onDragLeave = function() {
			form.currentDropElement = null;
		};

		form.onDrop = function(data, dragElement, dropElement, event, tipe) {
			var indexFinal = 0;
			console.info(tipe)
			if(tipe == 1){
				
				var checkVal = true;
				form.models.tahapanPengadaanList.forEach(function(valueF, index, array) {
					 
						if(valueF.id == data.id){
							checkVal = false;
						} 
				});
				console.info(checkVal)
				
				if(checkVal == true){
					
					form.models.tahapanPengadaanList.push(data);
	 				
					var check = true;
					form.models.tahapanList.forEach(function(valueF, index, array) {
						if(check == true){
							if(valueF.id == data.id){
								indexFinal = index;
								check = false;
							}
						}
					});

					form.models.tahapanList.splice(indexFinal, 1);
				}
			}else{
				form.currentDropElement = null;
				var checkVal = true;
				form.models.tahapanList.forEach(function(valueF, index, array) {
					 
						if(valueF.id == data.id){
							checkVal = false;
						} 
				});

				console.info(checkVal)
				if(checkVal == true){

					
					form.models.tahapanList.push(data);

					var check = true;
					form.models.tahapanPengadaanList.forEach(function(valueF, index, array) {
						if(check == true){
							if(valueF.id == data.id){
								indexFinal = index;
								check = false;
							}
						}
					});
					form.models.tahapanPengadaanList.splice(indexFinal, 1);
				}
			}
        };

		form.onDropRem = function(data) {
			form.models.tahapanList.push(data);
        };
    }
    AlurPengadaanUbahController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', 'toaster'];

})();
