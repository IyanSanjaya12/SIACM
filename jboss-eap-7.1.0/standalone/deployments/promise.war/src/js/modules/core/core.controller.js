/**=========================================================
 * Module: CoreController.js
 =========================================================*/

(function() {
    'use strict';    
    angular
        .module('naut')
        .controller('CoreController', CoreController);

    /* @ngInject */
    function CoreController($rootScope, $location, $scope, RequestService, $state, ModalService) {
    	
	        /******************************
		        BOTTOM SCROLL TOP BUTTON
		     ******************************/
		
		    // declare variable
		    var scrollTop = $(".scrollTop");
		
		    $(window).scroll(function() {
		      // declare variable
		      var topPos = $(this).scrollTop();
		
		      // if user scrolls down - show scroll to top button
		      if (topPos > 100) {
		        $(scrollTop).css("opacity", "1");
		
		      } else {
		        $(scrollTop).css("opacity", "0");
		      }
		
		    }); // scroll END
		
		    //Click event to scroll to top
		    $(scrollTop).click(function() {
		      $('html, body').animate({
		        scrollTop: 0
		      }, 800);
		      return false;
		
		    }); // click() scroll top EMD
	
    	
    	var vm = this;
		$rootScope.showMore = function(){
			$("#kebijakan").css('display', 'block');
			$("#collapse").hide();
		}
		
		
    	
      // Get title for each page
      $rootScope.pageTitle = function() {
        return $rootScope.app.name + ' - ' + $rootScope.app.description;
      };

      // Cancel events from templates
      // ----------------------------------- 
      $rootScope.cancel = function($event){
        $event.preventDefault();
        $event.stopPropagation();
      };
      
      $rootScope.compareList = [];
	  $rootScope.cartList = [];
	  $rootScope.compareVendorList = [];
	  $rootScope.gambarDefault = './app/img/no_image.png';
	  
	  /** ================================================================== COMPARE CATALOG ==================================================== **/
	  
      $rootScope.addCompareCatalog = function(catalog) {
    	  var isExist = false;
		  angular.forEach($rootScope.compareList, function(compare) {
			  if (compare.id == catalog.id) {
				  isExist = true;
			  }
		  });
		  if (isExist == false) {
			  $rootScope.compareList.push(catalog);
			  //alert("Berhasil tambah " + catalog.namaIND + " ke Compare Catalog");
			  RequestService.informSuccess("Berhasil tambah " + catalog.namaIND + " ke Compare Catalog");
		  }
      }
      
      $rootScope.removeCompare = function(compare) {
    	  var indexCompare = $rootScope.compareList.indexOf(compare);
    	  $rootScope.compareList.splice(indexCompare, 1);
      }
      
      $rootScope.removeAllCompare = function() {
    	  $rootScope.compareList = [];
      }
      
      /** checked all select box in compare catalog **/
      $rootScope.selectAllCatalog = function(isAllSelectedCatalog) {
		     var toggleStatus = isAllSelectedCatalog;
		     angular.forEach($rootScope.compareList, function(value){
		    	 value.checked = toggleStatus;
		     });
		  }
      
      $rootScope.showCompare = function() {
    	  var isNext 		= true;
    	  var isNoAttribute = false;
    	  var isProcess 	= true;
    	  
    	  /** get checked only **/
    	  var listCompareCatalog = [];
    	  $rootScope.totalcompareCatalog = $rootScope.compareList.filter(function(value){
    		return value.checked;
    	  }).reduce(function(a,b){
    		  return listCompareCatalog.push(b);
    	  },0);
    	  
    	  angular.forEach(listCompareCatalog,function(value,index){
    		  if(value.attributeGroup === null){
    			  isProcess 	= false;
    			  isNoAttribute = true;
    		  }
    		  if(isProcess){
    			  var attrId 	 = value.attributeGroup.id===null?0:value.attributeGroup.id;
    			  var isProcess2 = true;
    			  angular.forEach(listCompareCatalog,function(value2,index2){
    				  if(value2.attributeGroup === null){
    					  isProcess2 	= false;
    					  isNoAttribute = true;
    				  }
    				  if(isProcess2){
    					  var attrId2 = value2.attributeGroup.id===null?0:value2.attributeGroup.id;
            			  if(attrId !== attrId2){
            				  isNext = false;
            			  }
    				  }
        		  });
    		  }
    	  });
    	  
    	  if(isNoAttribute){
    		  ModalService.showModalInformation('info : Anda tidak bisa melakukan compare karena belum memiliki attribute group');
    		  return false;
    	  }else{
    		  if(isNext){
    			  if(listCompareCatalog.length > 3){
    				  ModalService.showModalInformation('info : Anda tidak bisa melakukan compare melebihi 3 catalog');
            		  return false;
    			  }else if(listCompareCatalog.length < 1){
    				  ModalService.showModalInformation('info : Harap pilih minimal 1 catalog');
            		  return false;
    			  }else{
    				  if($rootScope.catalogFront == 0){
    					  $rootScope.dashboardBreadcrumb = true;
    					  $state.go('app.promise.procurement-ecatalog-compare', {catalogList: listCompareCatalog});
    				  }
    				  else{
    					  $state.go('appportal.procurement-ecatalog-compare', {catalogList: listCompareCatalog});
    				  } 
    				
    			  }
        	  }else{
        		  ModalService.showModalInformation('info : Anda tidak bisa melakukan compare dengan Komoditi, Kategori dan atau Sub Kategori yang berbeda');
        		  return false;
        	  }
    	  }
    	  
      }
      
      /** ================================================================== end COMPARE CATALOG ==================================================== **/
      
      /** ================================================================== COMPARE VENDOR ==================================================== **/
      $rootScope.addCompareVendor = function(vendor) {
    	  var isExist = false;
		  angular.forEach($rootScope.compareVendorList, function(compare) {
			  if (compare.id == vendor.id) {
				  isExist = true;
			  }
		  });
		  if (isExist == false) {
			  $rootScope.compareVendorList.push(vendor);
			  
		  }
      }
      
      $rootScope.removeCompareVendor = function(compareVendor) {
    	  var indexCompareVendor = $rootScope.compareVendorList.indexOf(compareVendor);
    	  
    	  $rootScope.compareVendorList.splice(indexCompareVendor, 1);
      }
      
      $rootScope.removeAllCompareVendor = function() {
    	  $rootScope.compareVendorList = [];
      }
      
      /** checked all select box in compare catalog **/
      $rootScope.selectAllVendor = function(isAllSelectedVendor) {
		     var toggleStatus = isAllSelectedVendor;
		     angular.forEach($rootScope.compareVendorList, function(value){
		    	 value.checked = toggleStatus;
		     });
		  }
      
      
      $rootScope.showCompareVendor = function(){
    	  var listVendor = [];
    	  
    	  /** get checked only **/
    	  $rootScope.totalcompareVendor = $rootScope.compareVendorList.filter(function(value){
    		return value.checked;
    	  }).reduce(function(a,b){
    		  return listVendor.push(b);
    	  },0);
    	  
    	  if(listVendor.length > 3){
    		  ModalService.showModalInformation('info : Anda tidak bisa melakukan compare melebihi 3 vendor');
    		  return false;
    	  }else if(listVendor.length < 1){
    		  ModalService.showModalInformation('info : Harap pilih minimal 1 vendor');
    		  return false;
    	  }else{
    		  $state.go('app.promise.procurement-catalog-vendor-compare', {compareVendorList: listVendor});
    	  }
      }
      /** ================================================================== end COMPARE CATALOG ==================================================== **/
      
      /** ================================================================== COMPARE CART LIST ==================================================== **/
      $rootScope.addCartCatalog = function(catalog) {
    	  $rootScope.cartShow = true;
    	  if(catalog.isAvailable){  	
    		  
	    	  var isExist = false;
	    	  angular.forEach($rootScope.cartList, function(compare, idx) {
				  if (compare.id == catalog.id) {
					  isExist = true;
				  }
			  });
	    	  $scope.vendorId =  null;
	    	  if($rootScope.cartList.length > 0){
	    		  $scope.vendorId = $rootScope.cartList[0].vendor.id;
	    		  RequestService.doPOSTJSON('/procurement/master/item-organisasi/get-list-item-organisasi-for-add-cart', catalog.item.id)
					.then(function success(data) {
						if(data == null){
							ModalService.showModalInformation('Item tidak tersedia!');
						}
						/*else{
							if ($scope.expenseAccount != data.expenseAccount){
								ModalService.showModalInformation('Item yang ditambahkan hanya boleh dari 1 COA yang sama, yaitu ' + $scope.expenseAccount + '|' + data.expenseAccountDescription);
							}*/
							else{
								if (isExist == false) {
									  ModalService
									  .showModalConfirmation(
										  'Apakah Anda yakin ingin menambahkan ' + catalog.namaIND + ' ke keranjang?')
										  .then(function(result) {
											  if( catalog.vendor.id == $scope.vendorId || $scope.vendorId == null ){
												  $rootScope.cartList.push(catalog);
												  RequestService.informSuccess("Berhasil tambah " + catalog.namaIND + " ke Keranjang");
											  }else{
												  RequestService.informError("Hanya bisa memasukan catalog dari Vendor " +$rootScope.cartList[0].vendor.nama);
										  }
									  });
								  }
							}
						/*}*/
					}, function error(response) {				 
						ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
	    	  }
	    	  else{
	    		  RequestService.doPOSTJSON('/procurement/master/item-organisasi/get-list-item-organisasi-for-add-cart', catalog.item.id)
					.then(function success(data) {
						if(data == null){
							ModalService.showModalInformation('Item tidak tersedia!');
						}
						else{
							$scope.expenseAccount = data.expenseAccount;
							if (isExist == false) {
								  ModalService
								  .showModalConfirmation(
									  'Apakah Anda yakin ingin menambahkan ' + catalog.namaIND + ' ke keranjang?')
									  .then(function(result) {
										  if( catalog.vendor.id == $scope.vendorId || $scope.vendorId == null ){
											  $rootScope.cartList.push(catalog);
											  RequestService.informSuccess("Berhasil tambah " + catalog.namaIND + " ke Keranjang");
										  }else{
											  RequestService.informError("Hanya bisa memasukan catalog dari Vendor " +$rootScope.cartList[0].vendor.nama);
										  }
								});
							  }
						}
					}, function error(response) {				 
						ModalService.showModalInformation('Terjadi kesalahan pada system!');
				});
	    	  }
	    	  
    	  }else{
    		  RequestService.informError("Hubungi admin untuk dapat memilih item ini.!");
    	  }
      }
      
      $scope.remove = function() {
    	  if($rootScope.cartList.length == 0) {
    		  $rootScope.cartShow = false;
    	  }
      }
      
      $rootScope.removeCart = function(cart) {
    	  var indexCart = $rootScope.cartList.indexOf(cart);
    	  $rootScope.cartList.splice(indexCart, 1);
    	  $scope.remove();
      }
      
      $rootScope.removeAllCart = function() {
    	  $rootScope.cartList = [];
    	  $scope.remove();
      }
      
      
      
      $rootScope.changeAllCart = function(cartStatus) {
    	  angular.forEach($rootScope.cartList, function(cart){
    		  cart.select = cartStatus;
    	  });
      }
      
      $rootScope.showCart = function() {
    	  $scope.errorNoItem = '';
    	  var cartSelectList = [];
    	  angular.forEach($rootScope.cartList, function(cart){
    		  cartSelectList.push(cart);
    	  });
    	  if(cartSelectList.length == 0) {
    		  //alert('Harap pilih minimal 1 item');
    		  RequestService.informError("Harap pilih minimal 1 item");
    	  } else {
    		  $rootScope.dashboardBreadcrumb = true;
    		  
        	  $state.go('app.promise.procurement-ecatalog-cart', {catalogList:cartSelectList});
    	  }
    	  
      }
      /** ================================================================== end COMPARE CATALOG ==================================================== **/
      
    }
    CoreController.$inject = ['$rootScope', '$location', '$scope', 'RequestService', '$state','ModalService'];

})();

angular.module('naut').filter('cut', function () {
    return function (value, wordwise, max, tail) {
        if (!value) return '';

        max = parseInt(max, 10);
        if (!max) return value;
        if (value.length <= max) return value;

        value = value.substr(0, max);
        if (wordwise) {
            var lastspace = value.lastIndexOf(' ');
            if (lastspace !== -1) {
              //Also remove . and , so its gives a cleaner result.
              if (value.charAt(lastspace-1) === '.' || value.charAt(lastspace-1) === ',') {
                lastspace = lastspace - 1;
              }
              value = value.substr(0, lastspace);
            }
        }

        return value + (tail);
    };
});
