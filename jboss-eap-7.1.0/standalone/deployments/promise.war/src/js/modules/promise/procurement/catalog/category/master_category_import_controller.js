'use strict';

angular.module('naut').controller('CatagoryImportController', ['FileUploader', 'RequestService', '$state', '$rootScope','$scope', function(FileUploader, RequestService, $state, $rootScope,$scope) {
    
	var vm 		= this;
	vm.template = RequestService.templateXls('templateCategory');
	
	vm.uploader = new FileUploader({
        url: $rootScope.backendAddress + '/procurement/catalog/CategoryServices/readExcellCategory',
        headers: {
        	'Authorization': RequestService.getUserToken()
        }
    });
    
	/*vm.uploader.filters.push({
        name: 'customFilter',
        fn: function(item, options) {
        	return item.type=='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.type=='application/vnd.ms-excel';
        }
    });*/
	
	vm.uploader.filters.push({
		name: 'customFilter',
		fn: function (item /*{File|FileLikeObject}*/ , options) {
			return this.queue.length < 10;
		}
	});
	
	vm.uploader.onAfterAddingFile = function(fileItem) {
        //console.info('onAfterAddingFile', fileItem);
		$scope.validasiUpload(fileItem);
    };
    
    $scope.validasiUpload = function(fileItem) {
		var fileExt = fileItem.file.name.toLowerCase().split('.').pop();
        var fileSize = fileItem.file.size / (1024 * 1024);
        var maxUpload = $rootScope.fileUploadSize / (1024 * 1024);
        
        if (fileSize > maxUpload) {
        	RequestService.modalInformation('Ukuran file tidak boleh lebih dari ' + maxUpload + ' MB!','danger');
        	fileItem.remove();
            return false;
        } else if ((!(fileExt == 'xls' || fileExt == 'xlsx'))) {
        	RequestService.modalInformation('Extensi yang diperbolehkan hanya ' + 'xls dan xlsx!','danger');
        	fileItem.remove();
            return false;
        }
        
        return true;
	}
    
	vm.kembali = function() {
		$state.go("app.promise.procurement-category");
	}
	
	vm.simpan = function() {
		var gagal = false;
		RequestService.modalConfirmation('Yakin menyimpan data ?')
    	.then(function (result) {
    		angular.forEach(vm.uploader.queue, function(item){
    			if (item.isUploaded == false) {
    				gagal = true;
    			}
			});
    		
    		if(gagal) {
	    		RequestService.modalInformation('Data Gagal di Simpan !!, Karena terdapat KESALAHAN pada isi FILE')
				.then(function (result) {
					$state.go("app.promise.procurement-category");
				});
    		} else {
	    		RequestService.modalInformation('Data Berhasil di Simpan')
	    		.then(function (result) {
	    			$state.go("app.promise.procurement-category");
	    		});
    		}
    	});
	}
	
}]);