/**=========================================================
 * Module: PurchaseRequestEditController.js
 * Author: NK
 =========================================================*/

(function () {
    'use strict';
    angular.module('naut').controller('PurchaseRequestEditController', PurchaseRequestEditController);

    function PurchaseRequestEditController($scope, $http, $rootScope, $resource, $location, toaster, ngTableParams, $modal, $filter, $stateParams) {
    	var form = this;
    	form.dataPurchaseRequest = $stateParams.purchaserequest;
    	$rootScope.listItem;
    	$rootScope.materialObj2;
    	
    	$scope.btnUpdatePrStatus = true;
        $scope.btnKembaliIndexStatus = true;
    	
        $scope.dokumenTanggalOpen = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            form.dokumenTanggalOpened = true;
        };
        
        $scope.procurementProcessList = [{id: 1,nama: "Direct Purchase"}, 
                                         {id: 2,nama: "Simple Procurement"}, 
                                         {id: 3,nama: "Advance Procurement"}
                                        ];
        
        var idpp;
        if ( form.dataPurchaseRequest.procurementprocess == "Direct Purchase" ){
        	idpp="0"
        }else if ( form.dataPurchaseRequest.procurementprocess == "Simple Procurement") {  	
        	idpp="1"
        }else{       	
        	idpp="2"
        } 
        form.dataPurchaseRequest.procurementprocess = $scope.procurementProcessList[idpp];
        
        
        
        $scope.getListPrItem = function (purchaserequestId) {
        	return $http.get($rootScope.backendAddress + '/procurement/order/purchaseRequestItemServices/getListByPurchaseRequestId/'+purchaserequestId)
            .then(function (response) {
            	$scope.loading = false;
            	return response.data;
            });
        }
        
        if (typeof form.dataPurchaseRequest.orderPurchaseRequestItemList === 'undefined' ) {
        	$scope.getListPrItem(form.dataPurchaseRequest.id).then(function(data){
        		$rootScope.materialList=data;
        	});
    	}
        
        $scope.btnKembali = function () {
            $location.path('/app/promise/procurement/order/purchaserequest');
        }
        
        $scope.loading = false;
        
        
        $scope.btnSimpanPr = function () {
            $scope.simpanPr();
        }
        
        
        $scope.modalsTambah = function () {
            var modalInstance = $modal.open({
                templateUrl: '/tambah.html',
                controller: modalInstanceController
                
            });
        }
        
      //Material
        $scope.autoLoadMaterial = function () {
            if (typeof $rootScope.materialList === 'undefined') {
                $rootScope.materialList = [];
                form.materialList = [];
            } else {
                form.materialList = $rootScope.materialList;
                //List Material		
                form.tableMaterialList = new ngTableParams({
                    page: 1, // show first page
                    count: 5 // count per page
                }, {
                    total: form.materialList.length, // length of data4
                    getData: function ($defer, params) {
                        $defer.resolve(form.materialList.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            }
        };
        $scope.autoLoadMaterial();
        
        
       /* $scope.addItem = function () 
        {
            // ADD A NEW ELEMENT.
        	var totItm=0;
        	totItm=form.quantity*form.price;
        	
        	$rootScope.listItem.push({ 
            	itemname: form.itemname,
            	vendorname: form.vendorname,
            	quantity: form.quantity,
            	price: form.price,
            	unit: form.unit,
            	costcenter: form.costcenteritem,
            	specification: form.specification,
            	pathfile: form.pathfile,
            	total: totItm
            });
        	
        	$scope.loading = false;
            form.itemname = '';
            form.vendorname= '';
            form.quantity = '';
            form.unit = '';
            form.price = '';
            form.specification= '';
            form.costcenteritem='';
            form.pathfile='';
            
        }
        */
        $scope.autoLoadListItem = function () {
            if (typeof $rootScope.listItem === 'undefined') {
                $rootScope.listItem = [];
            } else {
                form.listItem = $rootScope.listItem;
            }
        }
        
        $scope.autoLoadListItem();
        
        $scope.insertItemPr = function (purchaserequestId) {
            $scope.message = ('Proses penyimpanan item PR...');
            var ttl=0;
            var itId="";
            if (typeof $rootScope.materialList.length !== 'undefined') {
            	for (var i = 0; i < $rootScope.materialList.length; i++) {
            		ttl=$rootScope.materialList[i].quantity * $rootScope.materialList[i].price;
            		
            	console.log("ITEMPR : "+ JSON.stringify($rootScope.materialList[i]))
            	if (typeof $rootScope.materialList[i].item.id == 'undefined' ) {
            		itId=" ";
            	}else{
            		itId=$rootScope.materialList[i].item.id;
            	}
            	
            	var formItemPR = {
            		    id:$rootScope.materialList[i].id,
                		purchaserequestId: purchaserequestId,
                		itemname: $rootScope.materialList[i].itemname,
                		quantity: $rootScope.materialList[i].quantity,
                		unit: $rootScope.materialList[i].unit,
                		price: $rootScope.materialList[i].price,
                		specification: $rootScope.materialList[i].specification,
                		pathfile:$rootScope.materialList[i].pathfile,
                		itemId :itId,
                		mataUangId :'1',//temporary HC
                		costcenter :form.costcenteritem,
                		vendorId :'0',
                		kode :$rootScope.materialList[i].item.kode,
                		status:'Pending',//temporary HC
                		total: ttl
                };
                var targetURI = $rootScope.backendAddress + '/procurement/order/purchaseRequestItemServices/update';
                
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
                }).success(function (data, status, headers, config) {
                    $scope.message = ('Proses penyimpanan item PR... ' + (((i + 1) / 100) * 100) + '%');
                });
            	}
            $scope.message = ('Proses penyimpanan item PR free text	... BERHASIL!');
            }
            
        }
        
        //MODAL CONTROLLER
        var modalInstanceController = function ($scope, $http, $modalInstance, $resource) {
        	$scope.form ={};
            $scope.kodeMaterial = {};
            
            $scope.refreshkodeMaterialList = function (kodeMaterial) {
            $scope.loading = true;
             if (kodeMaterial.length == 0)
                 kodeMaterial = 0;
             	 return $http.get($rootScope.backendAddress + '/procurement/master/item/get-list-by-kode/' + kodeMaterial)
                 .then(function (response) {
                     $scope.kodeMaterialList = response.data;
                     $scope.loading = false;
                 });
            };
            $scope.setKodeMaterialSelected = function () {
             if (typeof $scope.kodeMaterial.selected !== 'undefined') {
            	$scope.form.itemname = $scope.kodeMaterial.selected.nama;
            	$scope.form.unit = $scope.kodeMaterial.selected.satuanId.nama;
                }
            };
         
         //HPS
         $scope.calculateTotalHPS = function () {
                 if (typeof $scope.form.quantity !== 'undefined' && typeof $scope.form.price !== 'undefined') {
                	 $scope.form.total = $scope.form.quantity * $scope.form.price;
                  }
             };
       
        //Simpan Material
         $scope.btnSimpanMaterial = function () {
        	 
                 $scope.kodeMaterialError = false;
                 $scope.kuantitasError = false;
                 $scope.kuantitasError01 = false;
                 $scope.hpsError = false;
                 $scope.hpsError01 = false;
                 var bolehSimpan = true;
                 
                 if (typeof $scope.form.quantity === 'undefined') {
                     $scope.kuantitasError = true;
                     bolehSimpan = false;
                 }
                 if ($scope.form.quantity <= 0) {
                     $scope.kuantitasError01 = true;
                     bolehSimpan = false;
                 }
                 if (typeof $scope.form.price === 'undefined') {
                     $scope.hpsError = true;
                     bolehSimpan = false;
                 }
                 if ($scope.form.price <= 0) {
                     $scope.hpsError01 = true;
                     bolehSimpan = false;
                 }
                 
                 if (bolehSimpan) {
                	 if (typeof $scope.kodeMaterial.selected == 'undefined') {
                		 $scope.kodeMaterial.selected={nama:$scope.form.itemname}
                	 }
                	 var tot =0;
                 	tot+=$scope.form.quantity*$scope.form.price;
                  	
                 	$rootScope.materialObj2 = { 
                         //material: $scope.kodeMaterial.selected,
                     	 itemname :$scope.kodeMaterial.selected.nama,
                 		 quantity: $scope.form.quantity,
                         price: $scope.form.price,
                         unit: $scope.form.unit,
                         mataUang: '1',
                         specification: $scope.form.specification,
                         pathfile: $scope.form.pathfile,
                         ttlItem: tot
                     }
                     
                 if (typeof $rootScope.materialEditId !== 'undefined') {
                         $rootScope.materialList[$rootScope.materialEditId] = $rootScope.materialObj2;
                 }else{
                	 	 $rootScope.materialList.push($rootScope.materialObj2);
                	 	if (typeof $rootScope.materialList.length !== 'undefined') {
                       	 var subttl=0;
                       	 for (var i = 0; i < $rootScope.materialList.length; i++) {
                        		subttl+=$rootScope.materialList[i].ttlItem;
                       	 }
                       	 form.subtotal=subttl;
                        }
                 }
                 
                 $modalInstance.close('closed');
              }
         
         }
              
        $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
         }
        
        $scope.ok = function () {
            $modalInstance.close('closed');
        }
        
        }
        modalInstanceController.$inject = ['$scope', '$http', '$modalInstance', '$resource', '$rootScope'];
        //----END MODAL
        
        //Edit
        $scope.setEditMaterial = function () {
        if (typeof $rootScope.materialEditId !== 'undefined') {
            $scope.materialObj = $rootScope.materialList[$rootScope.materialEditId];
            $scope.kodeMaterial.selected = $scope.materialObj.material;
            	form.quantity = $scope.materialObj.quantity;
            	form.price = $scope.materialObj.price;
            	form.specification = $scope.materialObj.specification;
                $scope.calculateTotalHPS();
            	form.itemname = $scope.kodeMaterial.selected.nama;
            	form.unit = $scope.kodeMaterial.selected.satuanId.nama;
            	form.pathfile = $scope.materialObj.pathfile;
            	
        } else {
            $scope.materialObj = {};
            
        }
    };
    
    $scope.setEditMaterial();
    
        
        $scope.editMaterial = function () {
        	var modalInstance = $modal.open({
                templateUrl: '/tambah.html',
                controller: modalInstanceController
                
            });
        }
        
      //Material Delete
        $scope.removeMaterial = function (obj, size) {
            var modalInstance = $modal.open({
                templateUrl: '/alertTable2.html',
                controller: modalInstanceController,
                size: size,
                resolve: {
                	
                	materialName: function () {
                		return obj.itemname;
                    }
                }
            });
            modalInstance.result.then(function () { //ok
                var index = $rootScope.materialList.indexOf(obj);
                form.subtotal=form.subtotal-obj.ttlItem;
                $rootScope.materialList.splice(index, 1);
                $scope.autoLoadMaterial();
               }, function () {});
        };
        
        $scope.btnUpdatePurchaseRequest = function () {
        	
        	if (typeof form.dataPurchaseRequest.department === 'undefined' || form.dataPurchaseRequest.department == '') {
                alert("Department must fill");
            } else if (typeof form.dataPurchaseRequest.daterequired === 'undefined' || form.dataPurchaseRequest.daterequired == '') {
                alert("Date Required must fill");
            } else if(typeof $rootScope.materialList.length == 'undefined' || $rootScope.materialList.length== ''){
            	alert("Item Must Fill");
            } 
        	
            else {
                $scope.loading = true;
                form.totalcost=0;
                for (var i = 0; i < $rootScope.materialList.length; i++) {
                	form.totalcost+=$rootScope.materialList[i].quantity * $rootScope.materialList[i].price;
                }
                //form.dataPurchaseRequest.procurementprocess=form.procurementProcessList.nama;
                form.dataPurchaseRequest.daterequired = $filter('date')(form.daterequired, 'yyyy-MM-dd');
               
                console.log(">> HEADER :" + JSON.stringify(form))
                
                var formHeaderPR = {
                		id: form.dataPurchaseRequest.id,
                		prnumber: form.dataPurchaseRequest.prnumber,
                		department: form.dataPurchaseRequest.department,
                		costcenter: form.dataPurchaseRequest.costcenter,
                		totalcost: form.dataPurchaseRequest.totalcost,
                		daterequired: form.dataPurchaseRequest.daterequired,
                		nextapproval: form.dataPurchaseRequest.nextapproval,
                		procurementprocess: form.dataPurchaseRequest.procurementprocess.nama,
                		termandconditionId: form.dataPurchaseRequest.termandcondition.id,
                		description: form.dataPurchaseRequest.description
                };
                
                console.log(">> HEADER :" + JSON.stringify(formHeaderPR))
                
                
                $http({
                    method: 'POST',
                    url: $rootScope.backendAddress + '/procurement/order/purchaseRequestServices/update',
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
                    if (typeof data !== 'undefined') {
                    	var purchaserequestId = data.id;
                        $scope.message = ('Proses penyimpanan header PR... BERHASIL');
                        $scope.insertItemPr(purchaserequestId);
                    	toaster.pop('success', 'PurchaseRequest', 'Save ' + data.nama + ', success.');
                    	$location.path('/app/promise/procurement/order/purchaserequest');
                    }
                    $scope.loading = false;
                });
                
            };
        };
      }
    PurchaseRequestEditController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'toaster','ngTableParams','$modal','$filter','$stateParams'];

})();
