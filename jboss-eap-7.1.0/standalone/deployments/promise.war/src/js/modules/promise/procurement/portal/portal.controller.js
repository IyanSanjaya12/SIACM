/**
 * ========================================================= Module:
 * PortalController Author: Reinhard
 * =========================================================
 */
/*(function() {*/
	'use strict';
    
	angular.module('naut')

	.controller('PortalController', [ '$scope', '$http', 'colors', '$rootScope','$resource', '$location', 'DTOptionsBuilder', 'DTColumnDefBuilder', '$sce', 'RequestService', '$state',

	function ($scope, $http, colors, $rootScope, $resource, $location, DTOptionsBuilder, DTColumnDefBuilder, $sce, RequestService, $state) {
		var form = this;
		
		//form.interfacing = true;
    	if($rootScope.interfacing.replace(" ", "").toUpperCase()=="FALSE"){
    		$location.path('/page/portal');
    		form.interfacing = false;
    	}
    	else{      		
    		window.location = $rootScope.pmAppLink;
    	}
		
		/*form.appInfo = '';
		
		$http.get($rootScope.backendAddress + '/procurement/interfacing/testing-service/getAppInfo')
        .success(function (data, status, headers, config) {
        	$rootScope.appInfoPelindoBuildId = data.buildId;
        	$rootScope.appInfoPelindoBuildDate = data.buildDate;
        })
        .error(function (dataTemp, status, headers, config) {
            console.error('Connection Failed');
            
        });*/
		
		$rootScope.cartShow = false;
		/*form.loading = true;
        form.currentPage = "home";
    	
        form.transInCss = "pt-page-current pt-page-flipInBottom pt-page-delay300";
        form.transOutCss = "pt-page-current pt-page-flipOutTop";
        
		// Datepicker
		form.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy','shortDate', 'dd-MM-yyyy' ];
		form.format = form.formats[4];
		form.dtOptions = DTOptionsBuilder.newOptions().withOption('bFilter',false).withOption('bLengthChange', false);
		form.dtBeritaOptions = DTOptionsBuilder.newOptions().withOption('bFilter',false).withOption('bLengthChange', false);

		
		// Pengumuman list
		form.pengumumanList = [ {}, {}, {}, {} ];

		// Pengadaan Form
		form.pengadaanKeyword = null;
		form.pengadaanTanggalFrom = null;
		form.pengadaanTanggalTo = null;
		form.pengadaanOrganisasi = {"id":0};

		form.pengadaanTanggalFromOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.pengadaanTanggalFromOpened = true;
			form.pengadaanTanggalToOpened = false;
			form.beritaTanggalToOpened = false;
		};

		form.pengadaanTanggalToOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.pengadaanTanggalToOpened = true;
			form.pengadaanTanggalFromOpened = false;
			form.beritaTanggalToOpened = false;
		};

		$scope.pengadaanSearch = function() {
			search(1, form.pengadaanOrganisasi, form.pengadaanTanggalFrom, form.pengadaanTanggalTo, form.pengadaanKeyword);
		}

		// Hasil Pengadaan Form
		form.hasilPengadaanKeyword = null;
		form.hasilPengadaanTanggalFrom = null;
		form.hasilPengadaanTanggalTo = null;
		form.hasilPengadaanOrganisasi = {"id":0};

		form.hasilPengadaanTanggalFromOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.hasilPengadaanTanggalFromOpened = true;
		};

		form.hasilPengadaanTanggalToOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.hasilPengadaanTanggalToOpened = true;
		};
		
		$scope.hasilPengadaanSearch = function() {
			search(2, form.hasilPengadaanOrganisasi, form.hasilPengadaanTanggalFrom, form.hasilPengadaanTanggalTo, form.hasilPengadaanKeyword);
		}
		
		// Berita Form
		form.beritaKeyword = null;
		form.beritaTanggalFrom = null;
		form.beritaTanggalTo = null;
		form.beritaOrganisasi = {"id":0};

		form.beritaTanggalFromOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.beritaTanggalFromOpened = true;
			form.beritaTanggalToOpened = false;
			form.pengadaanTanggalFromOpened = false;
			form.pengadaanTanggalToOpened = false;
		};

		form.beritaTanggalToOpen = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			form.beritaTanggalToOpened = true;
			form.pengadaanTanggalFromOpened = false;
			form.pengadaanTanggalToOpened = false;
			form.beritaTanggalFromOpened = false;
		};
	
		$scope.beritaSearch = function() {
			search(3, form.beritaOrganisasi, form.beritaTanggalFrom, form.beritaTanggalTo, form.beritaKeyword);
		}*/
		
		//------------------------------------- Catalog Portal ------------------
		
		RequestService.doPOSTJSON('/catalog/CategoryPortalServices/findAllWithTree', {})
        .then(function (data) {
            $scope.categoryList = data;
            $scope.category0 = data[0];
            $scope.category1 = data[1];
        });
		
		$scope.changeCategoryPortal = function(sub1category){
        	$rootScope.subcategory = sub1category;
            $state.go('appportal.catalog');
             
        }	
		
		//------------------------------------- ACTION BUTTON ------------------
		
		$scope.gotoHome = function() {
			if (form.currentPage != "home")
				{
				    form.homeCss = form.transInCss;
					if (form.currentPage == "about")
						{
							form.aboutCss = form.transOutCss;
						}
					else if (form.currentPage == "contact")
						{
						
							form.contactCss = form.transOutCss;
						}
					form.currentPage = "home";
				}
			
		}
		
		$scope.gotoAbout = function() {
			if (form.currentPage != "about")
			{
			    form.aboutCss = form.transInCss;
				if (form.currentPage == "home")
					{
						form.homeCss = form.transOutCss;
					}
				else if (form.currentPage == "contact")
					{
					
						form.contactCss = form.transOutCss;
					}
				form.currentPage = "about";
			}
		}
		
		$scope.gotoContact = function() {
			if (form.currentPage != "contact")
			{
			    form.contactCss = form.transInCss;
				if (form.currentPage == "home")
					{
						form.homeCss = form.transOutCss;
					}
				else if (form.currentPage == "about")
					{
					
						form.aboutCss = form.transOutCss;
					}
				form.currentPage = "contact";
			}
		}
		
		$scope.gotoLogin = function() {
			  $location.path('/page/promiselogin');
		}
		
		$scope.detilPengadaan = function(obj) {
			
			showDetil(1, obj);
		}
		
		$scope.detilHasil = function(obj) {
			showDetil(2, obj);
		}
		
		$scope.detilBerita = function(obj) {
			
			showDetil(3, obj);
		}
		
		$scope.closeDetil = function() {
			
			form.showDetil = false;
		}
		
		function showDetil(tipe, obj)
		{
			
			form.detil = obj;
			form.detilTipe = tipe;
			
		   	if (tipe == 1)
		   		{
		   		  form.detilTitle="Pengumuman Pengadaan";
		   		}
		   	else if ( tipe == 2 )
		   		{
		   		  form.detilTitle="Pengumuman Hasil Pengadaan";
		   		}
		   	else if ( tipe == 3 )
	   		{
		   		form.htmlBerita =  $sce.trustAsHtml(form.detil.isiPengumuman);
		   		form.detilTitle="Berita";
	   		}
	   	
		   	form.showDetil = true;
		}
	
		
		// --------------------------------------------- BEGIN --------------------------------------------- 
		
		/*$http.get($rootScope.backendAddress+ '/procurement/master/organisasi/get-list-by-parent-id/0',
						{
							ignoreLoadingBar : true
						}).success(
						function(data, status, headers, config) {
							form.organisasiList = [{"id":0, "nama": "Semua Unit"}]
							form.organisasiList = form.organisasiList.concat(data);
							
			            	search(1, form.hasilPengadaanOrganisasi, form.hasilPengadaanTanggalFrom, form.hasilPengadaanTanggalTo, form.hasilPengadaanKeyword);
			            	search(2, form.hasilPengadaanOrganisasi, form.hasilPengadaanTanggalFrom, form.hasilPengadaanTanggalTo, form.hasilPengadaanKeyword);
			            	search(3, form.hasilPengadaanOrganisasi, form.hasilPengadaanTanggalFrom, form.hasilPengadaanTanggalTo, form.hasilPengadaanKeyword);
						
						}).error(function(data, status, headers, config) {

				});*/
		
		// --------------------------------------------- END --------------------------------------------- 

		
		
		
		/*function search(tipeId, organisasi, tglMulai, tglSelesai, keyword) {

			var postData = {
				"tipeId" : tipeId,
			}

			if (organisasi != null && organisasi.id != 0 ) {
				postData.organisasiId = organisasi.id;
			}

			if (tglMulai != null) {
				var d = new Date(tglMulai);
				var year = d.getFullYear();
				var month = d.getMonth() + 1; // beware: January = 0; February
				// = 1, etc.
				var day = d.getDate();
				postData.tglMulai = day + '-' + month + '-' + year;
			}

			if (tglSelesai != null) {
				var d = new Date(tglSelesai);
				var year = d.getFullYear();
				var month = d.getMonth() + 1; // beware: January = 0; February
				// = 1, etc.
				var day = d.getDate();
				postData.tglSelesai = day + '-' + month + '-' + year;
			}

			if (keyword != null) {
				postData.keyword = keyword.trim();
			}

			$http( {
						method : 'POST',
						url : $rootScope.backendAddress	+ '/procurement/inisialisasi/PengumumanPengadaanServices/getPengumumanPengadaanListForSearch',
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						},
						transformRequest : function(obj) {
							var str = [];
							for ( var p in obj)
								str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
							return str.join("&");
						},
						data : postData
					}).success(function(data, status, headers, config) {

				form.pengumumanList[tipeId] = data;
				form.loading = false;
			})
		//}
		
	}*/

	//PortalController.$inject = [ '$scope', '$http', 'colors', '$rootScope','$resource', '$location', 'DTOptionsBuilder', 'DTColumnDefBuilder', '$sce'];

//})();
}]);