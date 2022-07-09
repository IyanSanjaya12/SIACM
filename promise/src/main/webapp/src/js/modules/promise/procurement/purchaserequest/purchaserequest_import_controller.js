'use strict';

/* Controllers */

angular.module('naut').controller('PurchaseRequestImportCtrl', [ 'FileUploader', 'RequestService', '$state','$scope','$rootScope', function(FileUploader, RequestService, $state, $scope, $rootScope) {

	var vm = this;
	vm.RequestService = RequestService;
	vm.uploader = new FileUploader({
		url : RequestService.readPurchaseRequestImport(),
		headers : {
			'Authorization' : RequestService.getUserToken()
		}
	});

	/*vm.uploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			return item.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' && this.queue.length < 1;
			;
		}
	});*/
	
	vm.uploader.filters.push({
		name: 'customFilter',
		fn: function (item /*{File|FileLikeObject}*/ , options) {
			return this.queue.length < 10;
		}
	});
	
	
	
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
	
	vm.uploader.onAfterAddingFile = function(fileItem) {
        //console.info('onAfterAddingFile', fileItem);
		$scope.validasiUpload(fileItem);
    };
    
	vm.uploader.onCompleteItem = function(fileItem, response, status, headers) {

		if (angular.isUndefined(response.msg)) {
			RequestService.informError("Unknown Error!");
			this.clearQueue();
			 vm.clear();
		} else if (response.msg.includes("ERROR")) {
			RequestService.informError(response.msg);
			this.clearQueue();
			 vm.clear();
		} else {
			RequestService.informSaveSuccess(response.msg);
			$state.go("app.promise.procurement-purchaserequest");
		}

	};
	
	
	//edit yuyu
	 vm.clear = function () {
		    angular.element("input[type='file']").val(null);
		};

	/*
	 * vm.uploader.onCompleteAll = function () {
	 * RequestService.modalInformation('Data Berhasil di Simpan') .then(function
	 * (result) { $state.go("app.promise.procurement-purchaserequest"); }); }
	 */

	vm.kembali = function() {
		$state.go("app.promise.procurement-purchaserequest");
	}

	vm.simpan = function() {
		angular.forEach(vm.uploader.queue, function(item) {
			if (item.isUploaded == false) {
				item.upload();
			}
		});
	}

} ]);