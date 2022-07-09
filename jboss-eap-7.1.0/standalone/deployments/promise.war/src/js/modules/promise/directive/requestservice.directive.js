/*

 *  Controller for Request HTTP
 *
 *  Copyright (c) 2016 F.H.K
 *  http://forum.mitramandiri.co.id:8088
 *
 *  Licensed under GOD's give
 *
 */

angular.module('naut')
    .service('RequestService', ['$rootScope', '$http', '$modal', '$log', '$localStorage', '$location', 'inform', '$state', function ($rootScope, $http, $modal, $log, $localStorage, $location, inform, $state) {
    	
        $rootScope.requestService = this;
        
        this.validasiEmpty = function (data){
        	if(data != undefined && data != null && !angular.equals(data,'')){
        		return true;
        	}else{
        		return false;
        	}
        }
        
        this.doSignInUserPM = function (data, roleUser) {
            $rootScope.userLogin = data.userToken;
            $rootScope.userToken = data.userToken.token;
            $rootScope.userRole = roleUser.role;
            $rootScope.userRoleName = $rootScope.userRole.nama;
            $rootScope.userRoleCode = $rootScope.userRole.code
            $rootScope.userRoleId = $rootScope.userRole.id;
            $rootScope.userRoleList = data.roleUserList;
            $rootScope.userRoleString = this.getUserRoleStringByRoleList($rootScope.userRoleList);


            //Init or clear data on signin, add here!

            //Clear cache 
            $rootScope.promiseDirectiveViewDataPengadaan = null;


            $log.info('Login Success!');
//            $log.info('# User ID        : ' + $rootScope.userLogin.user.id);
//            $log.info('# User name      : ' + $rootScope.userLogin.user.username);
//            $log.info('# User Role      : ' + $rootScope.userRoleName);
//            $log.info('# User Role Code : ' + $rootScope.userRoleCode);
//            $log.info('# User Role Id   : ' + $rootScope.userRoleId);
//            $log.info('# User Role App  : ' + $rootScope.userRole.appCode);
//            $log.info('# User token     : ' + $rootScope.userToken);
//            $log.info('# User roleList  : ' + this.getUserRoleStringByRoleList($rootScope.userRoleList));


            /*Fungsi Existing tidak dihilangkan*/
            
            //jika ada barang di cart maka mengarah ke cart ecatalog
            $rootScope.userOrganisasi = roleUser.organisasi;
            if ($rootScope.cartList.length > 0){
            	$state.go('app.promise.procurement-ecatalog-cart')
			  }
            else if (typeof $rootScope.userOrganisasi == 'undefined' || $rootScope.userOrganisasi == null) {
                //this is vendor
                $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                    .success(function (data, status, headers, config) {
                        //console.log(">>> " + JSON.stringify($rootScope.userLogin));
                        $rootScope.userOrganisasi = {
                            nama: data.nama
                        };
                        $rootScope.userOrganisasiParent = {
                            nama: ''
                        };
                    });

            } else if ($rootScope.userOrganisasi != null && $rootScope.userRoleName == 'VENDOR'){
            	//this is vendor with organisasi
            	$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                	.success(function (data, status, headers, config) {
                		//console.log(">>> " + JSON.stringify($rootScope.userLogin));
                		$rootScope.userOrganisasi = {
                				nama: data.nama
                		};
                		$rootScope.userOrganisasiParent = {
                				nama: ''
                		};
                });
            } else {
                $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-by-id/' + $rootScope.userOrganisasi.parentId)
                    .success(function (data, status, headers, config) {
                        $rootScope.userOrganisasiParent = data;
                    });
            }

            $localStorage.dataTemp = {
                userLogin: $rootScope.userLogin,
                userRole: $rootScope.userRole,
                userOrganisasi: $rootScope.userOrganisasi,
                userRoleName: $rootScope.userRoleName
            };

            /*end old function*/

            /* this.doGET('/procurement/sistemtimeservices/getCurrentTime')
                   .success(function (data) {
                       $rootScope.serverClock = serverTimeService.init(data);

                   });*/

            this.doGET('/procurement/master/roleUserServices/getListByToken')
                .then(function (data) {
                    $rootScope.listRoleUser = data;
                    /*parent.informClearAll();
                    $state.go($rootScope.userRole.dashboard.state);*/
                    $location.path('/portal');
                    
                })
        };
        
        this.doSignInUserPMDashboard = function (data, roleUser) {
            $rootScope.userLogin = data.userToken;
            $rootScope.userToken = data.userToken.token;
            $rootScope.userRole = roleUser.role;
            $rootScope.userRoleName = $rootScope.userRole.nama;
            $rootScope.userRoleCode = $rootScope.userRole.code
            $rootScope.userRoleId = $rootScope.userRole.id;
            $rootScope.userRoleList = data.roleUserList;
            $rootScope.userRoleString = this.getUserRoleStringByRoleList($rootScope.userRoleList);

            //Init or clear data on signin, add here!
            //Clear cache 
            $rootScope.promiseDirectiveViewDataPengadaan = null;

            $log.info('Login Success!');
//            $log.info('# User ID        : ' + $rootScope.userLogin.user.id);
//            $log.info('# User name      : ' + $rootScope.userLogin.user.username);
//            $log.info('# User Role      : ' + $rootScope.userRoleName);
//            $log.info('# User Role Code : ' + $rootScope.userRoleCode);
//            $log.info('# User Role Id   : ' + $rootScope.userRoleId);
//            $log.info('# User Role App  : ' + $rootScope.userRole.appCode);
//            $log.info('# User token     : ' + $rootScope.userToken);
//            $log.info('# User roleList  : ' + this.getUserRoleStringByRoleList($rootScope.userRoleList));

            
            //jika ada barang di cart maka mengarah ke cart ecatalog
            $rootScope.userOrganisasi = roleUser.organisasi;
            if ($rootScope.cartList.length > 0){
            	$state.go('app.promise.procurement-ecatalog-cart')
			  }
            else if (typeof $rootScope.userOrganisasi == 'undefined' || $rootScope.userOrganisasi == null) {
                //this is vendor
                $http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                    .success(function (data, status, headers, config) {
                        //console.log(">>> " + JSON.stringify($rootScope.userLogin));
                        $rootScope.userOrganisasi = {
                            nama: data.nama
                        };
                        $rootScope.userOrganisasiParent = {
                            nama: ''
                        };
                    });

            } else if ($rootScope.userOrganisasi != null && $rootScope.userRoleName == 'VENDOR'){
            	//this is vendor with organisasi
            	$http.get($rootScope.backendAddress + '/procurement/vendor/vendorServices/getVendorByUserId/' + $rootScope.userLogin.user.id)
                	.success(function (data, status, headers, config) {
                		//console.log(">>> " + JSON.stringify($rootScope.userLogin));
                		$rootScope.userOrganisasi = {
                				nama: data.nama
                		};
                		$rootScope.userOrganisasiParent = {
                				nama: ''
                		};
                });
            } else {
                $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-by-id/' + $rootScope.userOrganisasi.parentId)
                    .success(function (data, status, headers, config) {
                        $rootScope.userOrganisasiParent = data;
                    });
            }

            $localStorage.dataTemp = {
                userLogin: $rootScope.userLogin,
                userRole: $rootScope.userRole,
                userOrganisasi: $rootScope.userOrganisasi,
                userRoleName: $rootScope.userRoleName
            };

            this.doGET('/procurement/master/roleUserServices/getListByToken')
                .then(function (data) {
                    $rootScope.listRoleUser = data;
                    $rootScope.dashboardBreadcrumb = true;
                    /*parent.informClearAll();
                    $state.go($rootScope.userRole.dashboard.state);*/
                    if ($rootScope.userRoleName == 'PANITIA') {
                    	if($rootScope.compareList.length==0){
                        $location.path('/app/dashboard');
                    	}
                    } else if ($rootScope.userRoleName == 'ANGGARAN') {
                    	if($rootScope.compareList.length==0){
                        $location.path('/app/dashboard-anggaran');
                        delete $rootScope.cartList
                    	}
                    } else if ($rootScope.userRoleName == 'PERENCANA') {
                    	if($rootScope.cartList.length==0){
                    		if($rootScope.compareList.length==0){
                    		$location.path('/app/dashboard-perencanaan');
                    		}
                        } 
                    	if($rootScope.compareList.length==0){
                    		if($rootScope.cartList.length==0){
                    		$location.path('/app/dashboard-perencanaan');
                    		}
                        }
                    } else if ($rootScope.userRoleName == 'VENDOR') {
                    	if($rootScope.compareList.length==0){
                    		
                    		//Ini hanya untuk setting vendor agak redirect kehalaman itu sendiri
                    		if($state.current.data.displayName == 'Vendor Setting') {
                    			$state.go($state.current.name);
                    		} else {
                    			$location.path('/appvendor/promise/dashboard');
                    		}

	                        delete $rootScope.cartList
                    	}
                    } 
                    else if($rootScope.userRoleName == 'Pengelola Kontrak' || $rootScope.userRoleName == 'Pengelola Pembayaran'){
                    	if($rootScope.compareList.length==0){
                    	$location.path('/app/dashboard-kosong')
                    	delete $rootScope.cartList
                    	}
                    }else {
                    	if($rootScope.compareList.length==0){
                        $location.path('/app/dashboard-default');
                        delete $rootScope.cartList
                    	}
                    }
                    
                })
        };

        this.goToDashboard = function () {
        	$rootScope.dashboardBreadcrumb = true;
        	$location.path('/app/dashboard-default')
        	delete $rootScope.cartList
        }

        this.doSignInUserCM = function (data, roleUser) {
            $log.info('SIGNING CM');
            location.href = $rootScope.cmAppLink;
        };
        
        this.doSignInUserCMDashboard = function (data, roleUser) {
            $log.info('SIGNING CM');
            location.href = $rootScope.cmAppLink;
        };

        this.resetUserVar = function () {
            $rootScope.userLogin = null;
            $rootScope.userToken = null;
            $rootScope.userRole = null;
            $rootScope.userRoleName = null;
            $rootScope.listRoleUser = null;
            $rootScope.activeMenuId = null;
            $rootScope.userRoleList = null;
            $rootScope.userRoleString = null;
            $rootScope.dashboardBreadcrumb = null;

            // Other Data to clear
            $rootScope.compareList = [];
            $rootScope.compareVendorList = [];
            $rootScope.cartList = [];
            $rootScope.cartShow = false;
            $localStorage.token = "";
            $localStorage.dataTemp = "";

        };

        this.doSignOut = function () {

            $http.get($rootScope.backendAddress + '/procurement/loginservices/dologout/' + $rootScope.userToken)
                .success(function (data, status, headers, config) {
                    $rootScope.requestService.resetUserVar();
                    //$location.path('/#');
//                    $location.path('/page/portal');
//                    $location.path('http://10.10.10.101/kai/portal.promise');
//                    window.location.href = "http://10.10.10.101/kai/portal.promise";
                    $http.get($rootScope.backendAddress + '/procurement/master/parameter/get-name/PM_URL')
                    .success(function (data, status) {
                    	window.location.href = data.nilai;
                    })
                    
                })
                .error(function (data, status, headers, config) {
                    $rootScope.requestService.resetUserVar();
                    //$location.path('/#');
//                    $location.path('/page/portal');
//                    window.location.href = "http://10.10.10.101/kai/portal.promise";
                    /*perubahan KAI 24/11/2020*/
                    $http.get($rootScope.backendAddress + '/procurement/master/parameter/get-name/PM_URL')
                    .success(function (data, status) {
                    	window.location.href = data.nilai;
                    })
                });
        };


        this.getUserRoleStringByRoleList = function (userRoleList) {
            var roleString = "";
            angular.forEach(userRoleList, function (userRole) {
                roleString += userRole.role.nama + " (" + userRole.role.appCode + "), ";
            });
            return roleString;
        };

        this.requestServerPagging = function (Builder, targetURL, paramData) {
            return Builder.withOption('ajax', {
                    url: $rootScope.backendAddress + targetURL,
                    type: 'POST',
                    //				data:paramData,
                    data: function (data, dtInstance) {
                        data.param = paramData;
                        return data;
                    },
                    "beforeSend": function (xhr) {
                        xhr.setRequestHeader("Authorization", $rootScope.userLogin.token);
                    },
                    error: function (xhr, error, thrown) {
                        console.info('Terjadi kesalahan pada system!');
                        this.informError('Terjadi kesalahan pada system');
                        
                    }
                    
                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
        }
        
        this.requestServerPaggingNoToken = function (Builder, targetURL, paramData) {
            return Builder.withOption('ajax', {
                    url: $rootScope.backendAddress + targetURL,
                    type: 'POST',
                    //				data:paramData,
                    data: function (data, dtInstance) {
                        data.param = paramData;
                        return data;
                    },
                    error: function (xhr, error, thrown) {
                        console.info('Terjadi kesalahan pada system!');
                    }                    
                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
        }
        
        
        /* request server khusus rekap history */
        this.requestServerPaggingRekapHistory = function (Builder, targetURL, paramData) {
            return Builder.withOption('ajax', {
                    url: $rootScope.backendAddress + targetURL,
                    type: 'POST',
                    //				data:paramData,
                    data: function (data, dtInstance) {
                    	$rootScope.dtInstanceRekapHistory = dtInstance;
                        data.param = paramData;
                        return data;
                    },
                    "beforeSend": function (xhr) {
                        xhr.setRequestHeader("Authorization", $rootScope.userLogin.token);
                    },
                    error: function (xhr, error, thrown) {
                        console.info('Terjadi kesalahan pada system!');
                    }
                    
                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
        }
        
        /* request server khusus rekap history */
        this.requestServerPaggingPenilaianVendor = function (Builder, targetURL, paramData) {
            return Builder.withOption('ajax', {
                    url: $rootScope.backendAddress + targetURL,
                    type: 'POST',
                    //				data:paramData,
                    data: function (data, dtInstance) {
                    	$rootScope.dtInstancePenilaianVendor = dtInstance;
                        data.param = paramData;
                        return data;
                    },
                    "beforeSend": function (xhr) {
                        xhr.setRequestHeader("Authorization", $rootScope.userLogin.token);
                    },
                    error: function (xhr, error, thrown) {
                        console.info('Terjadi kesalahan pada system!');
                    }
                    
                    
                })
                .withDataProp('data')
                .withOption('processing', true)
                .withOption('serverSide', true)
                .withPaginationType('full_numbers')
                .withDisplayLength(10);
        }

        
        this.requestServer = function (targetURL, paramData) {
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + targetURL,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: paramData
            }).then(function (response) {
                return response.data;
            });
        }

        this.doPOST = function (targetURL, postData) {
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + targetURL,
                data: postData,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }
            }).success(function (data, status, headers, config) {
                return data;
            }).error(function (data, status, headers, config) {
                $log.error('** [ ERROR ON POST SERVICE ] ' + $rootScope.backendAddress + targetURL);
            })
        }

        this.doGET = function (targetURL) {
            return $http({
                method: 'GET',
                url: $rootScope.backendAddress + targetURL
            }).then(function (response) {
                return response.data;
            });
        }

        this.doPOSTJSON = function (targetURL, paramData) {
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + targetURL,
                headers: {
                    'Content-Type': 'application/json'
                },
                data: paramData
            }).then(function (response) {
                return response.data;
            });
        }

        this.doPOSTText = function (targetURL, paramData) {
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + targetURL,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: paramData
            }).then(function (response) {
                return response.data;
            });
        }

        this.informSuccess = function (msg) {
            inform.add(msg, {
                type: 'success'
            });
            return true;
        }

        this.informSaveSuccess = function (msg) {
            var strMsg = typeof msg == 'undefined' ? 'Data berhasil disimpan!' : msg;
            this.informSuccess(strMsg);
            return true;
        }
        
        this.informDeleteSuccess = function (msg) {
            var strMsg = typeof msg == 'undefined' ? 'Data berhasil dihapus!' : msg;
            this.informSuccess(strMsg);
            return true;
        } 
        

        this.informError = function (msg) {
            inform.add(msg, {
                type: 'danger'
            });
            return true;
        }
        
        this.informInternalError = function () {
        	var msg = "Internal Server Error!"
            inform.add(msg, {
                type: 'danger'
            });
            return true;
        }

        this.informClearAll = function () {
            inform.clear();
            return true;
        }


        this.modalConfirmation = function (msg) {
            return $modal.open({
                templateUrl: 'app/views/promise/directive/modalconfirmation.html',
                controller: 'ModalInsConfirmationCtrl',
                resolve: {
                    msg: function () {
                        return msg;
                    }
                }

            }).result;
        }
        
        this.saveModalConfirmation = function () {
        	var msg = "template.konfirmasi.simpan";
            return $modal.open({
                templateUrl: 'app/views/promise/directive/modalconfirmation.html',
                controller: 'ModalInsConfirmationCtrl',
                resolve: {
                    msg: function () {
                        return msg;
                    }
                }

            }).result;
        }

        this.deleteModalConfirmation = function () {
        	var msg = "template.konfirmasi.hapus";
            return $modal.open({
                templateUrl: 'app/views/promise/directive/modalconfirmation.html',
                controller: 'ModalInsConfirmationCtrl',
                resolve: {
                    msg: function () {
                        return msg;
                    }
                }

            }).result;
        }

        
        this.modalInformation = function (msg, theme) {
            return $modal.open({
                templateUrl: 'app/views/promise/directive/modalinformation.html',
                controller: 'ModalInsInformationCtrl',
                resolve: {
                    msg: function () {
                        return msg;
                    },
                    theme: function () {
                        return theme;
                    }
                }

            }).result;
        }

        this.uploadURL = function () {
            return $rootScope.uploadBackendAddress;
        }
        
        this.uploadFilter = function (uploader, item, isShowMsg,fileType) {
        	if(fileType==null){
        		fileType="all";
        	}
            var fileExt = item.name.toLowerCase().split('.').pop();
            var fileSize = item.size / (1024 * 1024);
            
            var allowedImg= $rootScope.fileTypeImg.toLowerCase().replace(/\s+/g, '');
            var allowedDoc= $rootScope.fileTypeDoc.toLowerCase().replace(/\s+/g, '');
            var allowedType;
            
            if(fileType.toLowerCase()=="img")
            {
            	allowedType=allowedImg;
            }else if(fileType.toLowerCase()=="doc"){
            	allowedType=allowedDoc;
            }else{
            	allowedType=allowedImg+","+allowedDoc;
            }
            
            var allowedTypeList = allowedType.split(',');
            var condition = "";
            for (x in allowedTypeList) {
            	if(allowedTypeList.length-1 == x){
            		condition += "fileExt == '" + allowedTypeList[x]+"'" ;
            	}
            	else{
            		condition += "fileExt == '" + allowedTypeList[x]+"' || " ;
            	}
            }
            
            $log.info('==== UPLOAD FILTER ===');
            $log.info('Allowed Type  	= ' + allowedType)
            $log.info('item.name  		= ' + item.name)
            $log.info('item.ext  		= ' + fileExt)
            $log.info('item.size  		= ' + fileSize)
            if (fileSize > $rootScope.fileUploadSize / (1024 * 1024)) {
                if (isShowMsg) {
                    this.modalInformation('Ukuran file tidak boleh lebih dari ' + $rootScope.fileUploadSize / (1024 * 1024) + 'MB!',"danger");
                }
                return false;
            } else if (!eval(condition)) {
                if (isShowMsg) {
                    this.modalInformation('Extensi yang diperbolehkan hanya ' + allowedType ,'danger');
                }
                return false;
            }
            return true;
        }

        this.loadURL = function (filename) {
            return $rootScope.viewUploadBackendAddress + "/" + filename;
        }

        this.getUserLogin = function () {
            return $rootScope.userLogin;
        }

        this.readCatalogImport = function () {
            return $rootScope.backendAddress + "/procurement/catalog/CatalogServices/readExcell";
        }

        this.downloadFile = function (filename, typeFile) {
            return $rootScope.backendAddress + "/file/downloadFile" + "/" + filename + "/" + typeFile;
        }

        this.templateXls = function (template) {
            return $rootScope.backendAddress + "/templateXls/" + template;
        }

        this.readDRImport = function () {
            return $rootScope.backendAddress + "/procurement/deliveryreceived/DeliveryReceivedServices/readTemplateXlsDR";
        }

        this.readAttImport = function () {
            return $rootScope.backendAddress + "/procurement/catalog/AttributeServices/readTemplateXlsAtt";
        }

        this.readAttGrImport = function () {
            return $rootScope.backendAddress + "/procurement/catalog/AttributeGroupServices/readTemplateXlsAttGr";
        }

        this.readPurchaseRequestImport = function () {
            return $rootScope.backendAddress + "/procurement/ExcelPurchaseRequestServices/upload";
        }

        this.readAlokasiImport = function () {
            return $rootScope.backendAddress + "/procurement/ExcelAlokasiServices/upload";
        }

        this.getUserToken = function () {
            return $rootScope.userToken;
        }

        this.getRoleUsers = function (userId) {
            return $http({
                method: 'GET',
                url: $rootScope.backendAddress + "/procurement/user/get-role-user-vendor/" + userId
            }).then(function (response) {
                return response.data;
            });
        }

        this.getRoleUser = function () {
            return $rootScope.userRole;
        }

        this.getUserOrganisasi = function () {
            return $rootScope.userOrganisasi;
        }
        
        this.doDownloadExcelByUrl = function (url,param) {
        	$http({
                method: 'POST',
                url: $rootScope.backendAddress + url,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                responseType: 'arraybuffer',
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: param
            })
                .success(function (data) {
                    $log.info('typeof data = ' + typeof data);
                    var file = new Blob([data], {
                        type: 'application/vnd.ms-excel'
                    });
                    var fileURL = URL.createObjectURL(file);
                    window.open(fileURL, 'C-Sharpcorner');
                });

            return true;
        }

        this.doPrint = function (paramData) {
            $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/report/printReport',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    responseType: 'arraybuffer',
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    data: paramData
                })
                .success(function (data) {
                    /*VALID*/
                    if (typeof data == 'object') {
                        console.info('ini ada')
                    }
                    /*posibilty error*/
                    else if (typeof data == 'undefined') {
                        /*get error code*/
                        $http({
                                method: 'POST',
                                url: $rootScope.backendAddress + '/procurement/report/printReport',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded'
                                },
                                responseType: 'arraybuffer',
                                transformRequest: function (obj) {
                                    var str = [];
                                    for (var p in obj)
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    return str.join("&");
                                },
                                data: paramData
                            })
                            .success(function (data) {
                                var errorCode = data.substr(data.indexOf("PRMS-ERR"), 12);
                                $location.path('/page/errorPage/' + errorCode);
                            });
                        return;
                    }


                    var file = new Blob([data], {
                        type: 'application/pdf'
                    });

                    var fileURL = URL.createObjectURL(file);
                    window.open(fileURL, '_blank');
                });


            return true;
        }
        
        this.doPostDownloadExcel = function (targetURL, postData, fileName) {
        	var a = document.createElement("a");
        	document.body.appendChild(a);
        	a.style ="display : none";
            return $http({
                method: 'POST',
                url: $rootScope.backendAddress + targetURL,
                data: postData,
                responseType: 'arraybuffer',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }
            }).success(function (data, status, headers, config) {
                if (typeof data == 'object') {/*VALID*/

                } else if (typeof data == 'undefined') {/*posibilty error*/
                    /*get error code*/
                    $http.get(url)
                        .success(function (data) {
                            var errorCode = data.substr(data.indexOf("PRMS-ERR"), 12);
                            $location.path('/page/errorPage/' + errorCode);
                        });
                    return;
                }
                
                var file = new Blob([data], {
                    type: 'application/vnd.ms-excel'
                });
                var fileURL = URL.createObjectURL(file);
//                fileURL =  fileURL.slice(0, fileURL.indexOf("8082/"));
//                fileURL = fileURL+"8082/Report Item Katalog PO"
                a.href = fileURL;
                a.download = fileName;
                a.click();
                window.URL.revokeObjectURL(fileURL);
//                window.open(fileURL, 'C-Sharpcorner');
            }).error(function (data, status, headers, config) {
              $log.error('** [ ERROR ON POST SERVICE ] ' + $rootScope.backendAddress + targetURL);
            })
        }
        
        this.doDownloadExcelByUrl = function (url) {
            $http.get(url, {
                    responseType: 'arraybuffer'
                })
                .success(function (data) {
                    $log.info('typeof data = ' + typeof data);

                    /*VALID*/
                    if (typeof data == 'object') {

                    }
                    /*posibilty error*/
                    else if (typeof data == 'undefined') {
                        /*get error code*/
                        $http.get(url)
                            .success(function (data) {
                                var errorCode = data.substr(data.indexOf("PRMS-ERR"), 12);
                                $location.path('/page/errorPage/' + errorCode);
                            });
                        return;
                    }
                    var file = new Blob([data], {
                        type: 'application/vnd.ms-excel'
                    });
                    var fileURL = URL.createObjectURL(file);
                    window.open(fileURL, 'C-Sharpcorner');
                });

            return true;
        }
        
        this.encrypt = function (u, p, c, r, f) {
        	
        	var s = new Date().getSeconds();
        	var m = new Date().getMinutes();
        	var h = new Date().getHours();
        	var d = new Date().getDate();
        	var mon = new Date().getMonth();
        	
            var string = s+'#PRM-MMI#'+u+'#PRM-MMI#'+m+'#PRM-MMI#'+p+'#PRM-MMI#'+h+'#PRM-MMI#'+c+'#PRM-MMI#'+d+'#PRM-MMI#'+r+'#PRM-MMI#'+mon+'#PRM-MMI#'+f;
 		    
 		    var aesUtil = new AesUtil();
 		    var auth = aesUtil.encrypt($rootScope.salt, $rootScope.iv, $rootScope.passphrase, string);
 		    return auth;

        }

    }]);

/*
 *  Directive service Modal  
 *  Copyright (c) 2017 PT. Mitra Mandiri Informatika  
 *  Licensed under GOD's give
 *
 */

angular.module('naut').service('ModalService', ['$modal', function ($modal) {

    var modalDefaults = {
        backdrop: true,
        keyboard: true,
        modalFade: true,
        templateUrl: 'app/views/promise/directive/modalconfirmationcustom.html'
    };

    var modalOptions = {
        closeButtonText: 'Tidak',
        actionButtonText: 'Ya',
        headerText: 'Konfirmasi!',
        bodyText: 'Apakah anda yakin untuk menyimpan data?'
    };

    var modalOptionsCustom = {
        closeButtonText: 'Tidak',
        actionButtonText: 'Ya',
        headerText: 'Konfirmasi!',
        bodyText: 'Apakah anda yakin untuk menyimpan data?'
    };

    var modalInfoDefaults = {
        backdrop: true,
        keyboard: true,
        modalFade: true,
        templateUrl: 'app/views/promise/directive/modalinformationblockwindowcustom.html'
    };

    var modalInfoOptions = {
        headerText: 'Informasi!',
        bodyText: 'Silahkan tunggu...!'
    };

    var modalInformation = {
        backdrop: true,
        keyboard: true,
        modalFade: true,
        templateUrl: 'app/views/promise/directive/modalinformationcustom.html'
    };

    var modalOptionsInformation = {
        closeButtonText: 'Tutup',
        headerText: 'Informasi!',
        bodyText: 'Terjadi kesalahan pada system!'
    };

    var tempModalDefaultsInfo = {};
    var tempModalOptionsInfo = {};
    var modalInst = {}

    this.showModalInformation = function (msg) {

        var customModalDefaults = {};
        return this.showModalInformationDetil(customModalDefaults, msg);
    }

    this.showModalInformationDetil = function (customModalDefaults, customBodyText) {
        var tempModalDefaults = {};
        var tempModalOptions = {};

        if (typeof customBodyText != 'undefined' && customBodyText != '') {
            modalOptionsCustom.bodyText = customBodyText;
            modalOptionsInformation.bodyText = customBodyText;
        }

        angular.extend(tempModalDefaults, modalInformation, customModalDefaults);
        angular.extend(tempModalOptions, modalOptionsInformation, modalOptionsInformation);

        if (!tempModalDefaults.controller) {
            tempModalDefaults.controller = function ($scope, $modalInstance) {
                $scope.modalOptions = tempModalOptions;
                $scope.modalOptions.close = function (result) {
                    $modalInstance.dismiss('cancel');
                };
            }
        }
        return $modal.open(tempModalDefaults).result;
    };


    this.showModalInformationBlock = function () {
        return this.showModalInformationBlockWindow();
    };

    this.showModalInformationBlockWindow = function () {

        var customModalDefaultsInfo = {};
        customModalDefaultsInfo.backdrop = 'static';
        customModalDefaultsInfo.keyboard = false;

        angular.extend(tempModalDefaultsInfo, modalInfoDefaults, customModalDefaultsInfo);
        angular.extend(tempModalOptionsInfo, modalInfoOptions, modalInfoOptions);


        if (!tempModalDefaultsInfo.controller) {
            tempModalDefaultsInfo.controller = function ($scope, $modalInstance) {
                modalInst = $modalInstance;
                $scope.modalOptions = tempModalOptionsInfo;
            }
        }
        return $modal.open(tempModalDefaultsInfo).result;
    };

    this.closeModalInformation = function () {
        modalInst.close();
    };

    this.showModalConfirmation = function (customBodyText) {
        var customModalDefaults = {};
        customModalDefaults.backdrop = 'static';
        return this.showModal(customModalDefaults, customBodyText);
    };

    this.showModal = function (customModalDefaults, customBodyText) {
        var tempModalDefaults = {};
        var tempModalOptions = {};

        if (typeof customBodyText != 'undefined') {
            modalOptionsCustom.bodyText = customBodyText;

            angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);
            angular.extend(tempModalOptions, modalOptionsCustom, modalOptionsCustom);
        } else {

            angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);
            angular.extend(tempModalOptions, modalOptions, modalOptions);
        }

        if (!tempModalDefaults.controller) {
            tempModalDefaults.controller = function ($scope, $modalInstance) {
                $scope.modalOptions = tempModalOptions;
                $scope.modalOptions.ok = function (result) {
                    $modalInstance.close(result);
                };
                $scope.modalOptions.close = function (result) {
                    $modalInstance.dismiss('cancel');
                };
            }
        }
        return $modal.open(tempModalDefaults).result;
    };

	}]);




angular.module('naut')
    .controller('ModalInsConfirmationCtrl', ['$scope', '$modalInstance', 'msg', function ($scope, $modalInstance, msg, theme) {
        $scope.msg = typeof msg == 'undefined' ? 'template.konfirmasi.message' : msg;
        $scope.ok = function () {
            $modalInstance.close('closed')
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
	}]);

angular.module('naut')
    .controller('ModalInsInformationCtrl', ['$scope', '$modalInstance', 'msg', 'theme', function ($scope, $modalInstance, msg, theme) {
        $scope.theme = (typeof theme == 'undefined' ? 'info' : theme);
        $scope.msg = typeof msg == 'undefined' ? '' : msg;
        $scope.ok = function () {
            $modalInstance.close('closed')
        };
}]);

angular.module('naut').directive('onErrorSrc', function () {
    return {
        link: function (scope, element, attrs) {
            element.bind('error', function () {
                if (attrs.src != attrs.onErrorSrc) {
                    attrs.$set('src', attrs.onErrorSrc);
                }
            });
        }
    }
});