/*rmt*/

(function () {
	'use strict';

	angular.module('naut').controller('SanggahanVendorTotalDetailMoreController', SanggahanVendorTotalDetailMoreController);

	function SanggahanVendorTotalDetailMoreController($scope, $http, $rootScope, $resource, $location, toaster, $filter, $stateParams, $anchorScroll, FileUploader, ngTableParams, $state) {

		
		
		var sanggahanDetail = this;
		sanggahanDetail.itemPengadaan = $stateParams.itemPengadaan;
		
		// set mode
		$scope.isEditable = $rootScope.isEditable;
		$scope.loadingSaving = false;
		$scope.loading = true;
		
		$scope.tanggal_error_required = false;
		
		/*-------------------------------------< LOAD STATIC MODEL >-------------------------------------------------*/

		function loadVendorSanggahan() {

			$scope.loading = true;
			
			$http.get( $rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
				.success(function(data, status, headers, config) {
					
					sanggahanDetail.vendor = data;
						
					$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/' + sanggahanDetail.vendor.id + '/' + sanggahanDetail.itemPengadaan.pengadaan.id)
						.success(function (data, status, headers, config) {
							sanggahanDetail.suratPenawaran = data[0];
							
							//ambil existing data sanggahan
							$http.get($rootScope.backendAddress + '/procurement/sanggahanVendor/sanggahanVendorServices/getListByItemPengadaanAndVendor/' + sanggahanDetail.itemPengadaan.id +'/'+ sanggahanDetail.vendor.id)
								.success(function (data, status, headers, config) {
								
									sanggahanDetail.sanggahan = data[0];
								
									if (typeof sanggahanDetail.sanggahan == 'undefined') {
										sanggahanDetail.sanggahan          		= {};
										sanggahanDetail.sanggahan.pengadaan 	= sanggahanDetail.suratPenawaran.pengadaan;
										sanggahanDetail.sanggahan.vendor   		= sanggahanDetail.suratPenawaran.vendor;
										sanggahanDetail.sanggahan.keterangan	= '';
									}
									else
									{
										loadDokumen(sanggahanDetail.sanggahan.id);
										$scope.sanggahan_tanggal = sanggahanDetail.sanggahan.tanggal;
									}
									
									$scope.$watch('sanggahan_tanggal',function(){
							        	if ($scope.tanggal_error_required==true)
							        		{
							        			$scope.tanggal_error_required=false;
							        		}
							        });
								
									// Ambil informasi daftar vendor
									$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaDetailServices/getListByPengadaanAndItem/' + sanggahanDetail.itemPengadaan.pengadaan.id +'/'+ sanggahanDetail.itemPengadaan.item.id, {
					                    ignoreLoadingBar: true
										}).success(function (data, status, headers, config) {
											sanggahanDetail.penawaranPertamaDetailList = data;
					                 
						                	var ctr = 0;
						                	var evaluasiList = [];
						                	
						                	// Ambil detil evaluasi masing2 vendor
						                	angular.forEach(sanggahanDetail.penawaranPertamaDetailList, function (penawaranPertamaDetail) {
						                		$http.get($rootScope.backendAddress + '/procurement/pemasukanpenawaran/penawaranPertamaServices/getById/' + penawaranPertamaDetail.penawaranPertama.id, {
								                    ignoreLoadingBar: true
								                }).success(function (data, status, headers, config) {
								                	
								                	$http.get($rootScope.backendAddress + '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/' + data.suratPenawaran.id, {
										                    ignoreLoadingBar: true
										                }).success(function (data, status, headers, config) {
										                	evaluasiList.push(data[0]);
										                	ctr++;
										                	
										                	if (ctr == sanggahanDetail.penawaranPertamaDetailList.length) {
										                		
										                		// end load 
										                		$scope.loading = false;
										                		
										                		var objList = evaluasiList;
										                		$scope.evaluasiVendorList = new ngTableParams({
							                                        page: 1,
							                                        count: 10
							                                    }, {
							                                        total: objList.length,
							                                        getData: function ($defer, params) {
							                                            $defer.resolve(objList.slice((params.page() - 1) * params.count(), params.page() * params.count()))
							                                        }
							                                    });
										                		
										                	}
										     	
										                }).error(function (data, status, headers, config) {
															$scope.loading = false;
															console.log("***ERROR** - '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/")
										                });
								                }).error(function (data, status, headers, config) {
													$scope.loading = false;
													console.log("***ERROR** - '/procurement/evaluasiharga/EvaluasiHargaVendorServices/getBySuratPenawaran/")
								                });
					                	})
					                	
									
					                }).error(function (data, status, headers, config) {
										$scope.loading = false;
										console.log("***ERROR** - '/procurement/pemasukanpenawaran/penawaranPertamaServices/getByPengadaan/")
					                });
								  
							}).error(function (data, status, headers, config) {
									$scope.loading = false;
									console.log("***ERROR** - /procurement/sanggahanVendor/sanggahanVendorServices/getByPengadaanVendor/")
							});
						
					}).error(function (data, status, headers, config) {
						$scope.loading = false;
						console.log("***ERROR** - /procurement/pemasukanpenawaran/suratPenawaranServices/getListSuratPenawaranByVendorAndPengadaan/")
					});
			}).error(function (data, status, headers, config) {
				$scope.loading = false;
				console.log("***ERROR** - /procurement/vendor/vendorServices/getVendorByUserId/")
			});
					
							
					
				

		}

		
		function loadDokumen(sanggahanId) {
            
            $http.get($rootScope.backendAddress + '/procurement/sanggahan/dokumenSanggahanVendorServices/getListBySanggahan/' + sanggahanId, {
                ignoreLoadingBar: true
            }).success(function (data, status, headers, config) {
            	sanggahanDetail.dokumenSanggahanList = data;
                /*Pindahkan ke list uploader file quee to show*/
                for (var y = 0; y < sanggahanDetail.dokumenSanggahanList.length; y++) {
                    var file = {
                        name: sanggahanDetail.dokumenSanggahanList[y].fileName,
                        size: sanggahanDetail.dokumenSanggahanList[y].fileSize
                    };
                  
                    uploader.addToQueue(file, undefined, undefined);
                   /* console.log('INFO uploader : ' + JSON.stringify(uploader));*/
                   
                    var item = uploader.queue[uploader.queue.length - 1];
                  
                    item.isUploading = false;
                    item.isReady = false;
                    item.isSuccess = true;
                    item.isUploaded = true;
                    item.progress = 100;
                    /*added new attribute & properties to store realFileName*/
                    item.realFileName = sanggahanDetail.dokumenSanggahanList[y].realFileName;
                }
               
            }).error(function (data, status, headers, config) {
            	
            });
        }
		
		
		$scope.gotoIndex = function () {
			
			$state.go('appvendor.promise.procurement-vendor-sanggahan-satuan-detail', {pengadaanId : sanggahanDetail.itemPengadaan.pengadaan.id} );
		};
		
		
		var isFormValid = function () {
			var isValid = true;
			if ( typeof $scope.sanggahan_tanggal=='undefined' )
				{
					document.getElementsByName('sanggahan_tanggal')[0].focus();
					$scope.tanggal_error_required =true;
					isValid = false;
				}
			return isValid;
		}
		
		
		$scope.save = function () {
			 if (isFormValid()) {
	                $scope.isEditable = false;
	                saveSanggahan();
	            } else {
	                toaster.pop('warning', 'Kesalahan', 'Silahkan periksa kembali inputan anda!');
	            }
	            return false;
		};

		
		 function saveSanggahan() {
			 $scope.loadingSaving = true;

	            /*insert when object.id is undefined*/
	            if (typeof sanggahanDetail.sanggahan.id == 'undefined') {
	            	
	                $http({
	                    method: 'POST',
	                    url: $rootScope.backendAddress + '/procurement/sanggahanVendor/sanggahanVendorServices/insert',
	                    headers: {
	                        'Content-Type': 'application/x-www-form-urlencoded'
	                    },
	                    transformRequest: function (obj) {
	                        var str = [];
	                        for (var p in obj)
	                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	                        return str.join("&");
	                    },
	                    data: {
	                        pengadaan  		: sanggahanDetail.sanggahan.pengadaan.id,
	                        vendor      	: sanggahanDetail.sanggahan.vendor.id,
	                        itemPengadaan   : sanggahanDetail.itemPengadaan.id,
	                        tanggal     	: $filter('date')($scope.sanggahan_tanggal, 'dd-MM-yyyy'),
	                        keterangan  	: sanggahanDetail.sanggahan.keterangan
	                    }
	                }).success(function (data, status, headers, config) {
	                	 sanggahanDetail.sanggahan = data;
	                	 saveDokumen(sanggahanDetail.sanggahan.id);
	                })
	            } else {
	            	
	            	
	            	 
	                 
	                $http({
	                    method: 'POST',
	                    url: $rootScope.backendAddress + '/procurement/sanggahanVendor/sanggahanVendorServices/update',
	                    headers: {
	                        'Content-Type': 'application/x-www-form-urlencoded'
	                    },
	                    transformRequest: function (obj) {
	                        var str = [];
	                        for (var p in obj)
	                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	                        return str.join("&");
	                    },
	                    data: {
	                        id         		: sanggahanDetail.sanggahan.id,
	                        pengadaan   	: sanggahanDetail.sanggahan.pengadaan.id,
	                        vendor      	: sanggahanDetail.sanggahan.vendor.id,
	                        itemPengadaan   : sanggahanDetail.itemPengadaan.id,
	                        tanggal     : $filter('date')($scope.sanggahan_tanggal, 'dd-MM-yyyy'),
	                        keterangan  	: sanggahanDetail.sanggahan.keterangan
	                    }
	                }).success(function (data, status, headers, config) {
	                	saveDokumen(sanggahanDetail.sanggahan.id);
	                })
	            }
		 }
		
		 
		  function saveDokumen(sanggahanId) {

			  if (uploader.queue.length > 0 ) {
	            /*first, delete all by sanggahan*/
	            $http.get($rootScope.backendAddress + '/procurement/sanggahan/dokumenSanggahanVendorServices/deleteRowBySanggahan/' + sanggahanId, {
	                ignoreLoadingBar: false
	            }).success(function (data, status, headers, config) {
	            	
	            	var ctrSaveDok = 0;
	            	
	                /* insert new */
	                angular.forEach(uploader.queue, function (item) {
	                	
	                    var formPostPengadaan = {
	                    	sanggahanVendor: sanggahanId,
	                        fileName: item.file.name,
	                        realFileName: item.realFileName,
	                        fileSize: item.file.size
	                    }
	                    $http({
	                        method: 'POST',
	                        url: $rootScope.backendAddress + '/procurement/sanggahan/dokumenSanggahanVendorServices/insert',
	                        headers: {
	                            'Content-Type': 'application/x-www-form-urlencoded'
	                        },
	                        transformRequest: function (obj) {
	                            var str = [];
	                            for (var p in obj)
	                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	                            return str.join("&");
	                        },
	                        data: formPostPengadaan
	                    }).success(function (data, status, headers, config) {
	                    	ctrSaveDok++;
		                	
		                	if (ctrSaveDok == uploader.queue.length) {
		                		$scope.showSuccess();
		                	}
	                    	 
	                    })
	                })
	               
	            }).error(function (data, status, headers, config) {});
	            
			  }
			  else
				  {
				  	$scope.showSuccess();
				  }
	        }

		  
		$scope.showSuccess = function () {
	            $scope.loadingSaving = false;
	            toaster.pop('success', 'Sukses', 'Data sanggahan berhasil disimpan.');
	            /*$scope.gotoIndex();*/
	   }
		  
		  
		  
		// Datepicker
		sanggahanDetail.toggleMin = function () {
			sanggahanDetail.minDate = sanggahanDetail.minDate ? null : new Date();
        };
        sanggahanDetail.toggleMin();
        sanggahanDetail.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy'];
        sanggahanDetail.format = sanggahanDetail.formats[4];

        sanggahanDetail.tanggalSanggahanOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            sanggahanDetail.tanggalSanggahanOpened = true;
        };
        
       
        
        // End DatePicker
		
        
       //uploader 
        var uploader = $scope.uploader = new FileUploader({
         	url : $rootScope.uploadBackendAddress,
            method: 'POST'
        });


        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/ , options) {
                return this.queue.length < 10;
            }
        });

        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            fileItem.realFileName = response.fileName;
            
        };
       // end uploader 
        
        loadVendorSanggahan();

	}

	SanggahanVendorTotalDetailMoreController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', '$filter', '$stateParams',  '$anchorScroll','FileUploader', 'ngTableParams', '$state'];

})();