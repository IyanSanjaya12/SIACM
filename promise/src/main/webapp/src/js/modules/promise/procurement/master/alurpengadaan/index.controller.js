(function () {
    'use strict';

    angular
        .module('naut')
        .controller('AlurPengadaanController', AlurPengadaanController);

    function AlurPengadaanController(RequestService, ModalService, $scope, $http, $rootScope, $resource, $location, $ocLazyLoad) { 
		$ocLazyLoad.load('vendor/angular-sanitize/angular-sanitize.js');
		$ocLazyLoad.load('src/js/adapt-strap.js');
		$ocLazyLoad.load('src/js/adapt-strap.tpl.js');

        var form = this;

        var getAlurPengadaanList = function () {
            $scope.loading = true;
            RequestService.doGET('/procurement/master/AlurPengadaanServices/getList/')
			.then(function successCallback(data) {
				form.alurPengadaanList = data;
				form.loading = false;
			}, function errorCallback(response) {	
                $scope.loading = false;		 
				ModalService.showModalInformation('Terjadi kesalahan pada system!');
			});
        }
        
        getAlurPengadaanList();

        form.add = function () {
            $location.path('app/promise/procurement/master/alurPengadaan/tambah');
        }

        form.edit = function (data) {
            $rootScope.alurPengadaanData = data;
            $location.path('app/promise/procurement/master/alurPengadaan/ubah');
        }

        form.del = function (id) {
        	ModalService.showModalConfirmation('Apakah anda yakin akan menghapus data ini?').then(function (result) {
				ModalService.showModalInformationBlock();
				RequestService.doGET('/procurement/master/AlurPengadaanServices/delete/' + id)
				.then(function successCallback(response) {
					getAlurPengadaanList();					
			        ModalService.closeModalInformation();
				}, function errorCallback(response) {	
					ModalService.closeModalInformation();			 
					ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
			});
        }
        
        form.getDetil = function(dataInput){
        	RequestService.doGET('/procurement/pengadaan/tahapanPengadaanServices/getListByAlurId/' + dataInput.id)
			.then(function (data, status, headers, config) {  
				
				var rowObject = document.getElementById('info_' + dataInput.id);
                var btnDetil = document.getElementById('btnDetil_' + dataInput.id);

                if ((rowObject.style.display == 'none')) {
                    var cell = document.getElementById('cell_info_' + dataInput.id);
                    var content = '';

                    content = content + '<br/><div class="col-md-12"><ul id="car-list" class="list-group">'+
				        '<li class="list-group-item active">List Tahapan Pengadaan</li>';
                    
                    data.forEach(function(valueF, index, array) {

                    	content = content + 
                    		'<li class="list-group-item" >'+
    				          '<span>'+
    				            '<span class="glyphicon glyphicon-thumbs-up"></span>'+
    				            '<span>&nbsp;' + (index + 1) + '. '+ valueF.tahapan.nama + '</span> '+
    				          '</span>'+
    				        '</li>';
                    });

                    content = content + '<div></ul>';
				      
                    cell.innerHTML = content;
                    rowObject.style.display = 'table-row';
                    btnDetil.innerText = 'Hide Detil';
                    $scope.loading = false;
                    return true;
                } else {
                    rowObject.style.display = 'none';
                    btnDetil.innerText = 'Show Detil';
                    $scope.loading = false;
                    return false;
                }
				
			})
        }
    }

    AlurPengadaanController.$inject = ['RequestService', 'ModalService', '$scope', '$http', '$rootScope', '$resource', '$location', '$ocLazyLoad'];

})();
