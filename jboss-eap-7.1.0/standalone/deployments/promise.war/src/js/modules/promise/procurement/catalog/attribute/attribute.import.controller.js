'use strict';

angular.module('naut').controller('AttributeImportController', ['FileUploader', 'RequestService', '$state', function(FileUploader, RequestService, $state) {
    
	var vm 		= this;
	vm.template = RequestService.templateXls('templateAtt');
	
	vm.uploader = new FileUploader({
        url: RequestService.readAttImport()
    });
	
	vm.uploader.filters.push({
        name: 'customFilter',
        fn: function(item, options) {
        	return item.type=='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.type=='application/vnd.ms-excel';
        }
    });
	
	vm.kembali = function() {
		$state.go("app.promise.procurement-master-attribute");
	}
	
	vm.simpan = function() {
		RequestService.modalConfirmation('Yakin menyimpan data ?')
    	.then(function (result) {
    		angular.forEach(vm.uploader.queue, function(item){
    			if (item.isUploaded == false) {
    				item.upload();
    			}
			});
    		RequestService.modalInformation('Data Berhasil di Simpan')
    		.then(function (result) {
    			$state.go("app.promise.procurement-master-attribute");
    		});
    	});
	}
	
}]);