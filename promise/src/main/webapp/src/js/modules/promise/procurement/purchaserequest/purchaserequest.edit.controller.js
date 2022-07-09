(function () {
    'use strict';
    angular.module('naut').controller('PurchaseRequestEditController', PurchaseRequestEditController);

    function PurchaseRequestEditController($scope, $http, $rootScope, $resource, $location, toaster, ngTableParams, $modal, $filter, RequestService, FileUploader, $stateParams, $q) {
        var form = this;
        $scope.loading = true;
        $scope.message = "";
        $rootScope.jenisAnggaranId = 0;
        $scope.hasUploaded = false;

        if ($stateParams.purchaserequest != undefined) {
            $scope.loading = false;

            var prObject = $stateParams.purchaserequest;

            // Tab 1
            form.prnumber = prObject.prnumber;
            form.daterequired = prObject.daterequired;

            if (prObject.costcenter != null) {

                $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraff/" + prObject.costcenter).then(function (response) {
                    form.costcenter = response.data[0];
                    $rootScope.nomorDraft = form.costcenter;
                    if (form.costcenter != undefined) {
                        $rootScope.jenisAnggaranId = form.costcenter.jenisAnggaran.id;
                    }
                });

            }

            form.description = (((typeof prObject.description !== 'undefined') && (prObject.description !== 'undefined')) ? prObject.description : '');

            form.isJoin = prObject.isJoin;

            // Tab 4
            form.termandcondition = prObject.termandcondition;
        } else {

            $scope.loading = false;
        }

        // Datepicker
        form.clear = function () {
            form.daterequired = null;
        };
        form.disabled = function (date, mode) {
            return false; // ( mode === 'day' && ( date.getDay() === 0 ||
            // date.getDay() === 6 ) );
        };
        form.toggleMin = function () {
            form.minDate = form.minDate ? null : new Date();
        };
        form.toggleMin();
        form.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        form.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
        form.format = form.formats[4];

        form.daterequiredOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.daterequiredOpened = true;
        };

        $scope.department = {};
        $scope.getDepartmentList = function () {
            $scope.departmentList = [];
            $http.get($rootScope.backendAddress + '/procurement/master/organisasi/get-list-by-parent-id/' + $rootScope.userOrganisasi.id) // user

            .then(function (response) {
                $scope.departmentList = response.data;
                // $scope.departmentList.push($rootScope.userOrganisasi);
                angular.forEach($scope.departmentList, function (value, index) {
                    if (value.nama == prObject.department) {
                        form.department = value;
                    }
                });
            })
        }
        $scope.getDepartmentList();

        $scope.findCostcenterList = function (value) {

            if (value.length != 0) {
            	
            	var data = {search:value}

            	RequestService.doPOST("/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraffAndOrganisasi",data)
				.then(function success(response) {
					if (data != undefined) {
						$scope.costCenterList = response.data;
						$scope.loading = false;
					}
				}, function error(response) {
					$log.info("proses gagal");
		        	RequestService.informError("Terjadi Kesalahan Pada System");
				});

            } else {
                $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi").then(function (response) {
                    $scope.costCenterList = response.data;
                    $scope.loading = false;
                });
            }

        }

        /* Uploader */
        form.uploader = new FileUploader({
            url: $rootScope.uploadBackendAddress,
            headers: {
                'Authorization': RequestService.getUserToken()
            }
        });

        form.uploader.filters.push({
            name: 'customFilter',
            fn: function (item, options) {
                return this.queue.length < 50;;
            }
        });

        form.uploader.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);

            fileItem.realFileName = response.fileName;
            fileItem.statusFile = true;
            $scope.hasUploaded = true;

        };
        form.uploader.onAfterAddingFile = function (fileItem) {
            console.info('onAfterAddingFile', fileItem);
            console.log('»> Add File :' + JSON.stringify($rootScope.prasyaratPengadaanList));


            for (var i = 0; i < form.uploader.queue.length - 1; i++) {
                if (form.uploader.queue[form.uploader.queue.length - 1].file.name == form.uploader.queue[i].file.name) {
                    //            		console.log('Nama File : '+form.uploader.queue[form.uploader.queue.length-1].file.name+' sama');
                    form.uploader.queue[i].remove();
                }
            }
            //fileItem.upload() 
        };

        form.uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/ , filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        /* Uploader */

        /* loadDokumen */
        $http.get($rootScope.backendAddress + '/procurement/UploadPurchaseRequestServices/getListByPurchaseRequest/' + prObject.id, {
            ignoreLoadingBar: true
        }).success(function (data, status, headers, config) {
            form.dokPrList = data;
            /* Pindahkan ke list uploader file quee to show */
            for (var y = 0; y < form.dokPrList.length; y++) {
                var file = {
                    name: form.dokPrList[y].uploadPrFileName,
                    size: form.dokPrList[y].uploadPRFileSize
                };
                form.uploader.addToQueue(file, undefined, undefined);
                var item = form.uploader.queue[form.uploader.queue.length - 1];
                item.isUploading = false;
                item.isReady = false;
                item.isSuccess = true;
                item.isUploaded = true;
                item.progress = 100;
                item.statusFile = true;
                /* added new attribute & properties to store realFileName */
                item.realFileName = form.dokPrList[y].uploadPrRealName;
                item.fileName = form.dokPrList[y].uploadPrFileName;;
                item.downloadFile = $rootScope.backendAddress + '/procurement/file/view/' + form.dokPrList[y].realFileName;
            }
            if (data.length > 0) {
                form.uploader.progress = 100;
            } else {
                form.uploader.progress = 0;
            }
            form.loading = false;
        }).error(function (data, status, headers, config) {
            form.loading = false;
        });

        $scope.findCostcenterListItem = function (value) {
            if (value != undefined) {
                if (value.length != 0) {
                    if ($rootScope.jenisAnggaranId != 0) {
                        $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraffAndjenisAnggaran/" + value + "/" + $rootScope.jenisAnggaranId).then(function (response) {
                            $scope.costCenterListItem = response.data;
                            $scope.loading = false;
                        });

                    } else {
                        $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraff/" + value).then(function (response) {
                            $scope.costCenterListItem = response.data;
                            $scope.loading = false;
                        });
                    }
                } else if (value.length == 0 && $rootScope.jenisAnggaranId != 0) {
                    $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi/" + $rootScope.jenisAnggaranId).then(function (response) {
                        $scope.costCenterListItem = response.data;
                        $scope.loading = false;
                    });
                } else {
                    $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi").then(function (response) {
                        $scope.costCenterListItem = response.data;
                        $scope.loading = false;
                    });
                }
            } else {
                $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/getListByUserOrganisasi/" + $rootScope.jenisAnggaranId).then(function (response) {
                    $scope.costCenterListItem = response.data;
                    $scope.loading = false;
                });
            }
        }

        $scope.prSelected = function (value) {
            $rootScope.nomorDraft = value;
            $rootScope.jenisAnggaranId = value.jenisAnggaran.id;

            angular.forEach($rootScope.itemPRList, function (val, index) {
                val.costcenteritem = value;
            });

            $scope.findCostcenterListItem(undefined);
        }

        $scope.prSelectedItem = function (value, index) {

            $rootScope.itemPRList[index].costcenteritem = value;

        }

        $scope.btnKembaliIndex = function () {
            $scope.go('/app/promise/procurement/purchaserequest');
        }

        // generate PR Number
        $scope.generateNumber = function () {
            var date = new Date();
            form.prnumber = "PR/" + date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2) + "/AI/000" + (Math.ceil(Math.random() * 9));

        };

        // Item PR
        $rootScope.itemPRList = [];

        $scope.getItemPR = function () { // data item diload setelah proses load
            // shipping
            return $q(function (resolve, reject) {
                $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/getListByPurchaseRequestId/' + prObject.id).success(function (response) {
                    if (response.length > 0) {
                        // console.log(">> Item : " + JSON.stringify(response));
                        angular.forEach(response, function (data, index) {
                            var objectItem = {};
                            objectItem.prItemId = data.id;
                            objectItem.material = data.item;
                            objectItem.nama = data.itemname;

                            objectItem.quantity = data.quantity;
                            objectItem.price = data.price;
                            objectItem.unit = data.unit;
                            objectItem.kode = data.kode;
                            objectItem.matauang = 'IDR';
                            objectItem.matauangId = 1;
                            objectItem.specification = data.specification;
                            objectItem.pathfile = data.path;
                            objectItem.priceafterjoin = data.priceafterjoin;
                            if (!angular.isUndefined(data.vendor) && data.vendor != null) {
                                objectItem.vendor = data.vendor.nama;
                                objectItem.vendorId = data.vendor.id;
                            }

                            if (data.costcenter != undefined && data.costcenter.length > 0) {
                                $http.get($rootScope.backendAddress + "/procurement/alokasianggaran/AlokasiAnggaranServices/findByAccountNameandByNomorDraff/" + data.costcenter).then(function (response) {
                                    objectItem.costcenteritem = response.data[0];
                                });

                            }

                            $rootScope.itemPRList.push(objectItem);
                        });
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                }).error(function (response) {
                    reject(false);
                });
            });
            console.info($rootScope.itemPRList);
        };

        $rootScope.totalHPSItemPR = 0;
        $scope.btnTambahItemFree = function () {
            $scope.errormessageItem = '';
            var modalInstance = $modal.open({
                templateUrl: '/add_item.html',
                controller: modalInstanceAddItemController

            });
        }
        var modalInstanceAddItemController = function ($scope, $http, $modalInstance, $resource, $rootScope, FileUploader) {
            $scope.form = {};
            $scope.kodeMaterial = {};
            $scope.form.itemname = '';
            $scope.form.quantity = 0;
            $scope.form.price = 0;
            $scope.form.unit = "";

            $scope.refreshkodeMaterialList = function (kodeMaterial) {
                $scope.loading = true;
                if (kodeMaterial.length == 0)
                    kodeMaterial = 0;
                return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial).then(function (response) {
                    $scope.kodeMaterialList = response.data;
                    $scope.loading = false;
                });
            };

            $scope.setKodeMaterialSelected = function() {
				if (typeof $scope.kodeMaterial.selected !== 'undefined') {
					$scope.form.itemname = $scope.kodeMaterial.selected.nama;
					$scope.form.unit = $scope.kodeMaterial.selected.satuanId.nama;
					$scope.form.disabled =true;
				}
			};
            // Satuan List
            $http.get($rootScope.backendAddress + '/procurement/master/satuan/get-list').success(function (data) {
                $scope.satuanList = data;
            });
            // hitung total hps
            $scope.calculateTotalHPS = function () {
                if (typeof $scope.form.quantity !== 'undefined' && typeof $scope.form.price !== 'undefined') {
                    $scope.form.total = $scope.form.quantity * $scope.form.price;
                }
            };

            // fileupload
            var uploader = $scope.uploader = new FileUploader({
                url: $rootScope.uploadBackendAddress,
                method: 'POST'
            });
            // filter
            uploader.filters.push({
                name: 'customFilter',
                fn: function (item /* {File|FileLikeObject} */ , options) {
                    return this.queue.length < 10;
                }
            });

            // simpan
            $scope.btnSimpanItem = function () {
                $scope.loading = true;
                var bolehSimpan = true;
                $scope.errormessageItemname = '';
                $scope.errormessageQuantity = '';
                $scope.errormessagePrice = '';
                $scope.errormessageUnit = '';
                $scope.message = '';
                // cek nama
                if (typeof $scope.kodeMaterial.selected == 'undefined') {
                    $scope.kodeMaterial.selected = {
                        nama: $scope.form.itemname
                    }
                }
                if ($scope.form.itemname == '') {
                    $scope.errormessageItemname = "Nama Item harus diisi!";
                    bolehSimpan = false;
                    return false;
                }

                // cek item exist
                angular.forEach($rootScope.itemPRList, function (value, index) {
                    if (value.nama == $scope.form.itemname) {
                        $scope.errormessageItemname = "Nama Item Sudah Dipilih!";
                        bolehSimpan = false;
                        return false;
                    }
                })

                // quantity
                if ($scope.form.quantity <= 0) {
                    $scope.errormessageQuantity = "Jumlah(Quantity) Item tidak boleh nol!";
                    bolehSimpan = false;
                    return false;
                }
                // satuan
                if ($scope.form.unit == "") {
                    $scope.errormessageUnit = "Satuan Harus dipilih!";
                    bolehSimpan = false;
                    return false;
                }
                // price
                if ($scope.form.price <= 0) {
                    $scope.errormessagePrice = "Harga Item tidak boleh nol!";
                    bolehSimpan = false;
                    return false;
                }

                if (bolehSimpan) {
                    if (uploader.queue.length == 0) {
                        var tot = 0;
                        var objectItem = {
                            material: $scope.kodeMaterial.selected,
                            nama: $scope.form.itemname,
                            kode: $scope.kodeMaterial.selected.kode,
                            vendor: $scope.kodeMaterial.selected.vendor,
                            quantity: $scope.form.quantity,
                            price: $scope.form.price,
                            unit: $scope.form.unit,
                            matauang: 'IDR',
                            matauangId: 1,
                            vendorId: '0',

                            specification: angular.isUndefined($scope.form.specification) ? "" : $scope.form.specification,
                            pathfile: $scope.pathfile,
                            costcenteritem: $rootScope.nomorDraft
                        }
                        $rootScope.itemPRList.push(objectItem);
                        $rootScope.totalHPSItemPR = 0;
                        angular.forEach($rootScope.itemPRList, function (value, index) {
                            $rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
                        });
                        $scope.loading = false;
                        $modalInstance.close('closed');
                    } else {
                        $scope.message = 'Upload File Item';
                        uploader.uploadAll();
                        uploader.onCompleteItem = function (fileItem, response, status, headers) {
                            $scope.pathfile = response.fileName;
                            var tot = 0;
                            var objectItem = {
                                material: $scope.kodeMaterial.selected,
                                nama: $scope.form.itemname,
                                kode: $scope.kodeMaterial.selected.kode,
                                vendor: $scope.kodeMaterial.selected.vendor,
                                quantity: $scope.form.quantity,
                                price: $scope.form.price,
                                unit: $scope.form.unit,
                                matauang: 'IDR',
                                matauangId: 1,
                                vendorId: '0',
                                specification: $scope.form.specification,
                                pathfile: $scope.pathfile,
                                costcenteritem: $rootScope.nomorDraft
                            }
                            $rootScope.itemPRList.push(objectItem);
                            $rootScope.totalHPSItemPR = 0;
                            angular.forEach($rootScope.itemPRList, function (value, index) {
                                $rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
                            });
                            $scope.message = 'Upload File Item Berhasil';
                            $scope.loading = false;
                            $modalInstance.close('closed');
                        };
                    }

                } else {
                    $scope.loading = false;
                }
            }

            $scope.ok = function () {
                $modalInstance.close('closed');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }
        modalInstanceAddItemController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope', 'FileUploader'];
        // on change quantity
        $scope.onChangeQuantity = function () {
            $rootScope.totalHPSItemPR = 0;
            $scope.isTotalValid = true;
            angular.forEach($rootScope.itemPRList, function (value, index) {
                $rootScope.totalHPSItemPR = $rootScope.totalHPSItemPR + (value.quantity * value.price);
                if(isNaN($rootScope.totalHPSItemPR)){
					$scope.isTotalValid = false;
				}
            });
        }

        // on onChangeCostCenter
        $scope.onChangeCostCenter = function (indeks, nomorDraft) {
            // console.log(indeks);
            // console.log(nomorDraft);
            $rootScope.itemPRList[indeks].costcenteritem = nomorDraft

        };

        // Add Item from Katalog
        $scope.addItemKatalog = function () {
            $scope.errormessageItem = '';
            var modalInstance = $modal.open({
                templateUrl: 'app/views/promise/procurement/purchaserequest/purchaserequest.itemadd.html',
                controller: modalInstanceAddItemKatalogController,
                size: 'lg'
            });
        };

        // MODAL ADD CONTROLLER
        var modalInstanceAddItemKatalogController = function ($scope, $http, $modalInstance, $resource) {
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.ok = function () {
                $modalInstance.close('closed');
            }
        };
        modalInstanceAddItemKatalogController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        // remove Item
        // Material Delete
        $rootScope.itemPRListDeleted = [];
        $scope.removeItem = function (obj) {
            $rootScope.removeItemSelected = obj;
            var modalInstance = $modal.open({
                templateUrl: '/remove_item.html',
                controller: modalInstanceRemoveController,
                size: 'md'
            });
            modalInstance.result.then(function () { // ok
                if (typeof obj.prItemId !== 'undefined') {
                    $rootScope.itemPRListDeleted.push(obj.prItemId);
                }
                var index = $rootScope.itemPRList.indexOf(obj);
                $rootScope.itemPRList.splice(index, 1);
                $scope.onChangeQuantity();
            }, function () {});
        };
        var modalInstanceRemoveController = function ($scope, $http, $modalInstance, $resource) {
            $scope.materialName = $rootScope.removeItemSelected.nama;
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            }

            $scope.ok = function () {
                $modalInstance.close('closed');
            }
        }
        modalInstanceRemoveController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];

        // SHIPPING TO
        $scope.shippingList = [];
        $http.get($rootScope.backendAddress + '/procurement/ShippingToPRServices/findByPR/' + prObject.id).success(function (data) {
            if (data.length == 0) {
                var promisePRItem = $scope.getItemPR(); // load pr item
                promisePRItem.then(function (response) {
                    // console.log("load Item PR is " + response);
                });
            } else {
                var promisePRItem = $scope.getItemPR(); // load pr item
                promisePRItem.then(function (response) {
                    // console.log(">> shipping pr : " + JSON.stringify(data));
                    var shippingGroupState = 0;
                    $scope.onChangeQuantity();
                    angular.forEach(data, function (value, index) {
                        var shipTo = {
                            fullName: value.fullName,
                            telephone: value.telephone1,
                            address: value.streetAddress,
                            id: value.id,
                            prItemId: value.purchaseRequestItem,
                            prId: prObject.id,
                            prNumber: value.prNumber,
                            shippingGroup: value.shippingGroup,
                            deliveryTime: value.deliveryTime
                        };
                        if (typeof value.addressBook !== 'undefined') {
                            if (value.addressBook != null) {
                                shipTo.companySelected = value.addressBook.organisasi;
                                shipTo.companyAddressSelected = value.addressBook;
                            } else {
                                shipTo.companySelected = {
                                    companyName: value.addressLabel
                                };
                                shipTo.companyAddressSelected = {
                                    addressLabel: 'New Address',
                                    isNew: true
                                };
                            }
                        } else {
                            shipTo.companySelected = {
                                companyName: value.addressLabel
                            };
                            shipTo.companyAddressSelected = {
                                addressLabel: 'New Address',
                                isNew: true
                            };
                        }
                        var shippingGroupIndex = value.shippingGroup - 1;
                        if (shippingGroupState != value.shippingGroup) {
                            $scope.shippingList.splice(shippingGroupIndex, 1, shipTo);
                            shippingGroupState = value.shippingGroup;

                            $scope.shippingList[shippingGroupIndex].clear = function () {
                                $scope.shippingList[shippingGroupIndex].deliveryTime = null;
                            };
                            $scope.shippingList[shippingGroupIndex].disabled = function (date, mode) {
                                return false;
                            };
                            $scope.shippingList[shippingGroupIndex].toggleMin = function () {
                                $scope.shippingList[shippingGroupIndex].minDate = $scope.shippingList[shippingGroupIndex].minDate ? null : new Date();
                            };
                            $scope.shippingList[shippingGroupIndex].toggleMin();
                            $scope.shippingList[shippingGroupIndex].dateOptions = {
                                formatYear: 'yy',
                                startingDay: 1
                            };
                            $scope.shippingList[shippingGroupIndex].formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
                            $scope.shippingList[shippingGroupIndex].format = $scope.shippingList[shippingGroupIndex].formats[4];

                            $scope.shippingList[shippingGroupIndex].deliveryTimeOpen = function ($event) {
                                $event.preventDefault();
                                $event.stopPropagation();
                                $scope.shippingList[shippingGroupIndex].deliveryTimeOpened = true;
                            };

                        }

                        if ($scope.shippingList != undefined && $scope.shippingList.length > 0) {

                            if (typeof $scope.shippingList[shippingGroupIndex].itemPR === 'undefined') {
                                $scope.shippingList[shippingGroupIndex].itemPR = []
                            }

                        }
                        angular.forEach($rootScope.itemPRList, function (dataPrItem, index) {

                            var objItemPRShiping = {
                                shipQuantity: 0,
                                shipSelected: false,
                                index: index
                            };
                            if (dataPrItem.prItemId == value.purchaseRequestItem) {
                                objItemPRShiping = {
                                    shipQuantity: value.quantity,
                                    shipSelected: true,
                                    index: index
                                };

                                $scope.shippingList[shippingGroupIndex].itemPR.push(objItemPRShiping);

                            }
                        });

                    });
                });
            }

        });

        $scope.btnAddShipping = function () {
            if ($rootScope.itemPRList.length == 0) {
                alert("Item harus diisi terlebih dahulu!");
            } else {
                $scope.statusRemove = false;
                var shipTo = {
                    fullName: null,
                    telephone: null,
                    address: null
                };
                var ctr = $scope.shippingList.push(shipTo);

                // Function for delivery time date picker
                $scope.shippingList[ctr - 1].clear = function () {
                    $scope.shippingList[ctr - 1].deliveryTime = null;
                };
                $scope.shippingList[ctr - 1].disabled = function (date, mode) {
                    return false;
                };
                $scope.shippingList[ctr - 1].toggleMin = function () {
                    $scope.shippingList[ctr - 1].minDate = $scope.shippingList[ctr - 1].minDate ? null : new Date();
                };
                $scope.shippingList[ctr - 1].toggleMin();
                $scope.shippingList[ctr - 1].dateOptions = {
                    formatYear: 'yy',
                    startingDay: 1
                };
                $scope.shippingList[ctr - 1].formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'dd-MM-yyyy', 'yyyy-MM-dd'];
                $scope.shippingList[ctr - 1].format = $scope.shippingList[ctr - 1].formats[4];

                $scope.shippingList[ctr - 1].deliveryTimeOpen = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.shippingList[ctr - 1].deliveryTimeOpened = true;
                };
                // end function

            }
        }

        // companySelected
       
        $scope.findCompany = function () {
        	$scope.companyList = [];
            $scope.loading = true;
            $http.get($rootScope.backendAddress+'/procurement/master/organisasi/get-organisasi-afco-by-token')
                .success(function(data){
                $scope.loading = false;
                $scope.companyList.push(data);
                
            })
            .error(function(err){
                $scope.loading = false;
                console.error("err : "+err);
            });
        };
        $scope.findCompany();

        // get company Address
        $scope.getAddressBookList = function () {
			$rootScope.companyAddresslist = [];			
			RequestService.doGET('/procurement/master/AddressBookServices/getAddressBookListByToken')
			.then(function success(data) {
				$rootScope.companyAddresslist = data;
				var companyAddress = {
						addressLabel : 'Alamat Baru',
						isNew : true
					};
					$rootScope.companyAddresslist
							.push(companyAddress);
				
			}, function error(response) { 
				RequestService.informError("Terjadi Kesalahan Pada System");
		
		});       
	    }
        $scope.getAddressBookList();

        // getAtributteAddress
        $scope.setShippingAddress = function (index, companyAddressSelected) {
            if (companyAddressSelected.isNew != true) {
                $scope.shippingList[index].fullName = companyAddressSelected.fullName;
                $scope.shippingList[index].telephone = companyAddressSelected.telephone1;
                $scope.shippingList[index].address = companyAddressSelected.addressLabel + ', ' + companyAddressSelected.streetAddress + ', ' + companyAddressSelected.city + ', ' + (companyAddressSelected.country!=null ? companyAddressSelected.country : '');
            } else {
                $scope.shippingList[index].fullName = '';
                $scope.shippingList[index].telephone = '';
                $scope.shippingList[index].address = '';
                // console.log('>> new address');
            }
        };

        $scope.statusRemove = true;
        $scope.btnDelShipping = function () {
            $scope.shippingList.splice(-1);
        };

        // Approval
        form.approval = {};
        $scope.approvalList = [];
        $scope.approvalIsChange = false; // jika true, hapus data approval
        // sebelumnya, ganti pilihan baru.
      
        $scope.getApprovalList = function () {
 				$scope.approvalList = [];				
 				RequestService.doGET('/procurement/approval/get-approval-by-tahapan-and-token/CREATE_PR')
 				.then(function success(data) {
 					$scope.approvalList = data;
 					$http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByTahapanAndId/CREATE_PR/' + prObject.id).success(function (data) {
	                    if (data != undefined && data.id != undefined) {
	                        // jika tidak memakai approval template
	                        if (typeof data.approval === 'undefined' || data.approval == null) {
	                            form.approval.selected = 0;
	                            // get approval process
	                            $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findByApprovalProcessType/' + data.id).success(function (data) {
	                                // get user approval
	                                $http.get($rootScope.backendAddress + '/procurement/approvalProcessServices/findRoleUser/' + data[0].user.id).success(function (data) {
	                                    form.approval.newSelected = data[0];
	                                });
	                            }).error(function (data) {
	                                // console.error("Error get Process Approval
	                                // Type");
	                            })

	                        } else {
	                            form.approval.selected = data.approval.id;
	                            form.approval.processTypeId = data.id;
	                            $http.get($rootScope.backendAddress + '/procurement/approval/approvalLevel/' + form.approval.selected).then(function (responseApproval) {
	                                form.approval.levelList = responseApproval.data;
	                                $scope.loadingApproval = false;
	                            });
	                        }
	                    }
	                });
 					
 				}, function error(response) { 
 					RequestService.informError("Terjadi Kesalahan Pada System");
 			
 			});       
         }
        
		
	                
	        
            
        
        $scope.getApprovalList();

        $scope.getApprovalLevel = function () {
            $scope.loadingApproval = true;
            form.approval.levelList = [];
            $http.get($rootScope.backendAddress + '/procurement/approval/approvalLevel/' + form.approval.selected).then(function (response) {
                form.approval.levelList = response.data;
                $scope.loadingApproval = false;
                $scope.approvalIsChange = true;
            })
        };

        $scope.approvalNewList = [];
        $scope.getApprovalNewList = function (name) {
            if (name.length > 0) {
                $scope.loadingApproval = true;
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/approval/role-user-list-organisasi',
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
                        name: name
                    }
                }).success(function (data, status, headers, config) {
                    $scope.loadingApproval = false;
                    $scope.approvalNewList = data;
                });
            }

        };

        // validate
        
     // validate
		$scope.validateForm = function() {
			var isValid = true;
			$scope.errormessagePRNumber = '';
			if (typeof form.prnumber === 'undefined') {
				isValid = false;
				$scope.errormessagePRNumber = 'PR Number harus diisi!';
			}

			$scope.errormessageDaterequired = '';
			if (typeof form.daterequired === 'undefined') {
				isValid = false;
				$scope.errormessageDaterequired = 'Required Date harus diisi!';
			}
			$scope.errormessageDepartment = '';
			if (typeof form.department === 'undefined') {
				isValid = false;
				$scope.errormessageDepartment = 'Department harus dipilih!';
			}
			$scope.errormessageCost = '';
			if (typeof form.costcenter === 'undefined') {
				isValid = false;
				$scope.errormessageCost = 'Cost Center harus dipilih!';
			}
			return isValid;
		}

		$scope.validateForm2 = function() {
			var isValid = true;
			$scope.errormessageItem = '';
			if ($rootScope.itemPRList.length == 0) {
				isValid = false;
				$scope.errormessageItem = 'Item pengadaan harus dipilih!';
			} else {
				for (var i = 0; i < $rootScope.itemPRList.length; i++) {
					if ($rootScope.itemPRList[i].quantity == 0) {
						isValid = false;
						$scope.errormessageItem = 'Item Qty tidak boleh 0!';
						break;
					}
				}
			}
			return isValid;
		}

		// Shipping Validation
		$scope.validateShipping = function(itemPR) {

			$scope.errorShipping = null;
			$scope.errormessageDateDeliveryTimerequired = null;
			$scope.errormessagePerusahaan = null;
			var isValid = true;

			for (var i = 0; i < $scope.shippingList.length; i++) {
				var a = $scope.shippingList[i];
				
				 if(angular.isUndefined(a.companySelected)){
					 isValid = false;
					$scope.errormessagePerusahaan = "Silahkan isi Perusahaan";
					/*return false;*/
					
				}else if (angular.isUndefined(a.deliveryTime)) {
					isValid = false
					$scope.errormessageDateDeliveryTimerequired = "Silahkan isi Delivery Time";
					/*return false;*/
				}
				 
				 angular.forEach(
							$rootScope.itemPRList,
							function(itemPR, indexItemPR) {
								var totalShippingItem = 0;
								angular
										.forEach(
												$scope.shippingList,
												function(shipping) {
													angular
															.forEach(
																	shipping.itemPR,
																	function(
																			shippingItem) {
																		if (indexItemPR == shippingItem.index
																				&& shippingItem.shipSelected == true) {
																			totalShippingItem += shippingItem.shipQuantity;
																		}
																	});
												});
								if (itemPR.quantity != totalShippingItem) {
									isValid = false
									$scope.errorShipping = "Jumlah Shipping tidak sama dengan Jumlah Quota Item PR!";
									/*return false;*/
								}
							});

				for (var x = 0; x < $scope.shippingList.length; x++) {
					if (i != x) {

						var b = $scope.shippingList[x];

						if ((angular.equals(a.companySelected,
								b.companySelected))
								&& (angular.equals(a.companyAddressSelected,
										b.companyAddressSelected))
								&& (angular.equals(a.deliveryTime,
										b.deliveryTime))) {
							isValid = false;
							$scope.errorShipping = "Shipping Address & Delivery Time tidak boleh ada yang sama untuk setiap shipping";
							/*return false;*/
						}
					}
				}
			}

			return isValid;
		}
        /*$scope.validateForm = function () {
            var isValid = true;
            $scope.errormessagePRNumber = '';
            if (typeof form.prnumber === 'undefined') {
                isValid = false;
                $scope.errormessagePRNumber = 'PR Number harus diisi!';
            }

            $scope.errormessageDaterequired = '';
            if (typeof form.daterequired === 'undefined') {
                isValid = false;
                $scope.errormessageDaterequired = 'Required Date harus diisi!';
            }
            $scope.errormessageDepartment = '';
            if (typeof form.department === 'undefined') {
                isValid = false;
                $scope.errormessageDepartment = 'Department harus dipilih!';
            }
            return isValid;
        };
        $scope.validateForm2 = function () {
            var isValid = true;
            $scope.errormessageItem = '';
            if ($rootScope.itemPRList.length == 0) {
                isValid = false;
                $scope.errormessageItem = 'Item pengadaan harus dipilih!';
            } else {
                for (var i = 0; i < $rootScope.itemPRList.length; i++) {
                    if ($rootScope.itemPRList[i].quantity == 0) {
                        isValid = false;
                        $scope.errormessageItem = 'Item Qty tidak boleh 0!';
                        break;
                    }
                }
            }
            return isValid;
        }

        // Shipping Validation
        $scope.validateShipping = function () {

            $scope.errorShipping = null;
            $scope.errormessageDateDeliveryTimerequired = null;

            for (var i = 0; i < $scope.shippingList.length; i++) {

                var a = $scope.shippingList[i];
                if (angular.isUndefined(a.deliveryTime)) {
                    $scope.errormessageDateDeliveryTimerequired = "Silahkan isi Delivery Time";
                    return false;
                }

                for (var x = 0; x < $scope.shippingList.length; x++) {
                    if (i != x) {

                        var b = $scope.shippingList[x];

                        if ((angular.equals(a.companySelected, b.companySelected)) && (angular.equals(a.companyAddressSelected, b.companyAddressSelected)) && (angular.equals(a.deliveryTime, b.deliveryTime))) {
                            $scope.errorShipping = "Shipping Address & Delivery Time tidak boleh ada yang sama untuk setiap shipping";
                            return false;
                        }
                    }
                }
            }

            angular.forEach($rootScope.itemPRList, function (itemPR, indexItemPR) {
                var totalShippingItem = 0;
                angular.forEach($scope.shippingList, function (shipping) {
                    angular.forEach(shipping.itemPR, function (shippingItem) {
                        if (indexItemPR == shippingItem.index && shippingItem.shipSelected == true) {
                            totalShippingItem += shippingItem.shipQuantity;
                        }
                    });
                });
                if (itemPR.quantity != totalShippingItem) {
                    $scope.errorShipping = "Jumlah Shipping tidak sama dengan Jumlah Quota Item PR!";
                    return false;
                }
            });

            return true;
        }*/

        // count total shipping Item
        $scope.countTotalShipingByItem = function (itemIndex) {
            var totalShippingItem = 0;
            angular.forEach($scope.shippingList, function (shipping, shippingIndex) {
                angular.forEach(shipping.itemPR, function (shippingItem, shippingItemIndex) {
                    if (shippingItem.index == itemIndex && shippingItem.shipSelected == true) {
                        totalShippingItem += shippingItem.shipQuantity;
                    }
                });
            });
            return totalShippingItem;
        }

        $scope.validateFormApproval = function () {
            var isValid = true;
            $scope.errormessageApproval = '';
            if (typeof form.approval.selected == 'undefined') {
                isValid = false;
                $scope.errormessageApproval = 'Approval belum disi!';
            } else if (form.approval.selected == 0) {
                if (typeof form.approval.newSelected === 'undefined') {
                    isValid = false;
                    $scope.errormessageApproval = 'Approval belum disi!';
                }
            }
            return isValid;
        }

        $scope.btnSimpanPRStatus = true;
        $scope.btnSimpanPR = function () {
            $scope.btnSimpanPRStatus = false;
            $scope.errorBeforeSave = "";
            //revalidasi Form
            var isValidForSave = true;
            if (!$scope.validateForm()) {
                $scope.errorBeforeSave = "Input Form PR Detil tidak benar!";
                isValidForSave = false;
            }
            if (!$scope.validateForm2()&& $scope.isTotalValid) {
                $scope.errorBeforeSave = "Input Form Item tidak benar!";
                isValidForSave = false;
            }
            if (!$scope.validateShipping()) {
                $scope.errorBeforeSave = "Input Form Shipping tidak benar!";
                isValidForSave = false;
            }
            if (!$scope.validateFormApproval()) {
                $scope.errorBeforeSave = "Input Form Approval tidak benar!";
                isValidForSave = false;
            }
            if (isValidForSave) {
                RequestService.modalConfirmation('Anda yakin ingin menyimpan data Purchase Request?')
                    .then(function (result) {
                        $scope.loading = true;
                        $scope.message = "";
                        form.totalcost = 0;
                        for (var i = 0; i < $rootScope.itemPRList.length; i++) {
                            form.totalcost += $rootScope.itemPRList[i].quantity * $rootScope.itemPRList[i].price;
                        }

                        $scope.fileuploadList = [];
                        angular.forEach(form.uploader.queue, function (item) {
                            var fileUpload = {
                                fileRealName: item.realFileName,
                                fileName: item.file.name,
                                fileSize: item.file.size
                            }
                            $scope.fileuploadList.push(fileUpload);
                        })

                        var formHeaderPR = {
                            id: prObject.id,
                            prnumber: form.prnumber,
                            department: form.department.nama,
                            departmentId: form.department.id, // hardcode sementara
                            costcenter: form.costcenter.nomorDraft,
                            totalcost: form.totalcost,
                            daterequired: $filter('date')(form.daterequired, 'yyyy-MM-dd'),
                            fileuploadList: JSON.stringify($scope.fileuploadList),
                            isJoin: form.isJoin
                        };

                        if (!angular.isUndefined(form.termandcondition)) {
                            formHeaderPR.termandcondition = form.termandcondition;
                        }

                        if (!angular.isUndefined(form.description)) {
                            formHeaderPR.description = form.description;
                        }

                        if (!angular.isUndefined(form.procurementprocess)) {
                            formHeaderPR.procurementprocess = form.procurementprocess;
                        }

                        $scope.appId = '';
                        if (form.approval.selected > 0) {
                            formHeaderPR.approvalId = form.approval.selected;
                            formHeaderPR.approvalIsNew = false;
                            var flow = form.approval.levelList[0];
                            if (flow.group != null) {
                                formHeaderPR.nextapproval = flow.group.nama;
                            } else if (flow.user != null) {
                                formHeaderPR.nextapproval = flow.user.namaPengguna;
                            } else if (flow.role != null) {
                                formHeaderPR.nextapproval = flow.role.nama;
                            }
                        } else {
                            formHeaderPR.approvalUser = form.approval.newSelected.user.id;
                            formHeaderPR.apparovalOrganisasi = form.approval.newSelected.organisasi.id;
                            formHeaderPR.nextapproval = form.approval.newSelected.organisasi.nama + ' - ' + form.approval.newSelected.user.namaPengguna;
                            formHeaderPR.approvalIsNew = true;
                        }
                        formHeaderPR.approvalIsChange = $scope.approvalIsChange;
                        // jika approval sebelumnya data masih kosong
                        if (typeof form.approval.processTypeId === 'undefined') {
                            formHeaderPR.approvalProcessTypeId = 0;
                        } else {
                            formHeaderPR.approvalProcessTypeId = form.approval.processTypeId;
                        }

                        var prosesNow = 0;
                        $http({
                            method: 'POST',
                            url: $rootScope.backendAddress + '/procurement/purchaseRequestServices/update',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                return str.join("&");
                            },
                            data: formHeaderPR
                        }).success(function (data, status, headers, config) {
                            prosesNow++;
                            $scope.message = ('Proses Penyimpanan Purchase Request');

                            if (typeof data !== 'undefined') {
                                var purchaserequestId = data.id;
                                // Remove PR Item
                                $http.get($rootScope.backendAddress + '/procurement/purchaseRequestItemServices/deleteRowByPR/' + prObject.id).success(function (data) {

                                    // Simpan PR ITem
                                    var indexItemPRSave = 0;
                                    angular.forEach($rootScope.itemPRList, function (valueItemPR, indexItemPr) {

                                        var ttl = valueItemPR.quantity * valueItemPR.price;
                                        var formItemPR = {
                                            purchaserequestId: prObject.id,
                                            itemname: valueItemPR.nama,
                                            quantity: valueItemPR.quantity,
                                            unit: valueItemPR.unit,
                                            price: valueItemPR.price,
                                            specification: valueItemPR.specification,


                                            mataUangId: '1', // temporary HC
                                            costcenter: valueItemPR.costcenteritem.nomorDraft,
                                            /*vendorId : valueItemPR.vendorId,*/

                                            kode: valueItemPR.kode,
                                            status: 'Pending',
                                            total: ttl
                                        };


                                        if (typeof valueItemPR.pathfile !== 'undefined') {
                                            formItemPR.pathfile = valueItemPR.pathfile;
                                        }


                                        if ((!angular.isUndefined(valueItemPR.material)) && (valueItemPR.material != null)) {
                                            formItemPR.itemId = valueItemPR.material.id;
                                            // vendorName

                                        }


                                        console.log('angular.isUndefined( valueItemPR.vendor) = ' + angular.isUndefined(valueItemPR.vendor));

                                        if (!angular.isUndefined(valueItemPR.vendor)) {
                                            formItemPR.vendorName = valueItemPR.vendor;
                                            formItemPR.vendorId = valueItemPR.vendorId;

                                        }

                                        var targetURI = '';
                                        if (typeof valueItemPR.kode != 'undefined') {
                                            targetURI = $rootScope.backendAddress + '/procurement/purchaseRequestItemServices/insert';

                                        } else {
                                            targetURI = $rootScope.backendAddress + '/procurement/purchaseRequestItemServices/insertPurchaseItemFreeText';
                                        }

                                        $http({
                                            method: 'POST',
                                            url: targetURI,
                                            headers: {
                                                'Content-Type': 'application/x-www-form-urlencoded'
                                            },
                                            transformRequest: function (obj) {
                                                var str = [];
                                                for (var p in obj)
                                                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                                return str.join("&");
                                            },
                                            data: formItemPR
                                        }).success(function (dataPrItem, status, headers, config) {
                                            $scope.message = ('Proses Penyimpanan Item Purchase Request');
                                            valueItemPR.prItemId = dataPrItem.id;
                                            indexItemPRSave++;
                                            if ($rootScope.itemPRList.length == indexItemPRSave) {

                                                // Save Shipping
                                                if ($scope.shippingList.length == 0) {
                                                    $scope.message = ('Simpan Purchase Request Berhasil');
                                                    $scope.loading = false;
                                                    $scope.btnSimpanPRStatus = false;
                                                } else {
                                                    var indexShippingSave = 0;
                                                    angular.forEach($scope.shippingList, function (valShipping, indexShipping) {
                                                        var addressBookId = 0;
                                                        if (typeof valShipping.companyAddressSelected !== 'undefined') {
                                                            if (typeof valShipping.companyAddressSelected.id !== 'undefined') {
                                                                addressBookId = valShipping.companyAddressSelected.id;
                                                            }
                                                        }
                                                        indexShippingSave++;
                                                        var indexItemShippingSave = 0;
                                                        angular.forEach(valShipping.itemPR, function (valItemShipping, indexItemShipping) {
                                                            if (valItemShipping.shipSelected) { // check
                                                                // item
                                                                // is
                                                                // selected
                                                                var prItemId = 0;
                                                                if (typeof $rootScope.itemPRList[valItemShipping.index].prItemId !== 'undefined') {
                                                                    prItemId = $rootScope.itemPRList[valItemShipping.index].prItemId;
                                                                }
                                                                var shippingObj = {
                                                                    shippingGroup: (indexShipping + 1),
                                                                    addressBookId: addressBookId,
                                                                    prItemId: prItemId,
                                                                    quantity: valItemShipping.shipQuantity,
                                                                    prNumber: form.prnumber,
                                                                    fullName: valShipping.fullName,
                                                                    addressLabel: valShipping.companySelected.companyName,
                                                                    streetAddress: valShipping.address,
                                                                    telephone1: valShipping.telephone,
                                                                    deliveryTime: $filter('date')(valShipping.deliveryTime, 'yyyy-MM-dd')
                                                                };

                                                                $http({
                                                                    method: 'POST',
                                                                    url: $rootScope.backendAddress + '/procurement/ShippingToPRServices/insert',
                                                                    headers: {
                                                                        'Content-Type': 'application/x-www-form-urlencoded'
                                                                    },
                                                                    transformRequest: function (obj) {
                                                                        var str = [];
                                                                        for (var p in obj)
                                                                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                                                        return str.join("&");
                                                                    },
                                                                    data: shippingObj
                                                                }).success(function (data, status, headers, config) {
                                                                    $scope.message = ('Proses Penyimpanan Shipping Purchase Request ');
                                                                    indexItemShippingSave++;
                                                                    if (indexShippingSave == $scope.shippingList.length) {
                                                                        $scope.message = ('Simpan Purchase Request Berhasil');
                                                                        $scope.loading = false;
                                                                        $scope.btnSimpanPRStatus = false;
                                                                    }
                                                                });

                                                            }
                                                        });

                                                    });
                                                }

                                            }
                                        }).error(function (data, status, headers, config) {
                                            // console.log("Error ");
                                            $scope.loading = false;
                                             $scope.btnSimpanPRStatus = true;
                                        });
                                    });

                                });

                            }

                        }).error(function (data, status, headers, config) {
                            $scope.loading = false;
                             $scope.btnSimpanPRStatus = true;
                        });

                    });
            } else {
                console.error("Input form Invalid!");
                $scope.btnSimpanPRStatus = true;
            }

        };

        $scope.go = function (path) {
            $location.path(path);
        };
    }
    PurchaseRequestEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster', 'ngTableParams', '$modal', '$filter', 'RequestService', 'FileUploader', '$stateParams', '$q'];

})();