<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %> 
<!DOCTYPE html>
<!--[if IE 9 ]>    <html class="ie9" lang="en" ng-app="naut"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="id" ng-app="naut">
<!--<![endif]-->
<% String appVersion = "prmv015"; %>
<head>
	<title>Centra Auto AC</title>
	<link rel = "icon" href = "app/img/centra auto ac.png" type = "image/x-icon">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="{{ app.description }}">
    <meta name="keywords" content="">
    <!-- <title>Promise | {{ app.description }}</title> -->
    <!--build-css-start-->
    <link rel="stylesheet" href="app/css/bootstrap.css">
    <link href="vendor/bootstrap/dist/css/bootstrap.css" rel="stylesheet" />
    <link rel="stylesheet" href="app/css/styles.css">
    <link rel="stylesheet" href="app/css/app.css">
    <link rel="stylesheet" href="app/css/custom-styles.css">
    <link href="vendor/chosen/chosen.css" rel="stylesheet" />    
    <link href="vendor/owl-carousel/owl.carousel.css" rel="stylesheet" />
    <link rel="stylesheet" href="vendor/bootstrap/dist/css/glyphicons.css">
    <!--build-css-end-->
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
    
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAetkbkD6tPd2LV91pb_N-l6Na_jG4F5FY"></script>
</head>

<body ng-clok="" ng-class="{ 'layout-fixed' : app.layout.isFixed, 'layout-boxed' : app.layout.isBoxed, 'layout-dock' : app.layout.isDocked, 'layout-material': app.layout.isMaterial, 'aside-offscreen' : app.sidebar.isOffscreen, 'footer-hidden': app.footer.hidden, 'in-app': !$state.includes('page')}">
    <div data-ui-view="" data-autoscroll="false" ng-controller="CoreController" class="app-container"></div>
    <!-- Button Scroll Top-->
	<div id="stop" class="scrollTop" title="Back to Top">
	  <span><a href=""><em class="fa fa-angle-double-up"></em></a></span>
	</div>
    <!-- Settings-->
    <div ng-include="'templates/settings.html'" ng-show="showSettings.isHidden" ng-class="{'visible': showSettings}" ng-init="showSettings = false" class="settings-wrapper" ></div>
    <!-- End Settings-->
    <!-- Layer Morph -->
    <div class="layer-morph-overlay">
        <div class="layer-morph-inner bg-info"></div>
    </div>
    <div layer-morph-close="" class="layer-morph-close">
        <em class="icon-cross"></em>
    </div>
    <div class="layer-morph-container bg-info">
        <div class="layer-morph-wrapper">
            <div id="layer-search" ng-include="'templates/layer-search.html'" class="layer-morph"></div>
            <div class="layer-morph-footer">Procurement</div>
        </div>
    </div>
    <div id="footer-bar" ng-include="'templates/footer-bar.html'" class="layer-morph"></div>
    <!-- End Layer Morph -->
    <!-- build:vendor app/js/vendor.js-->
    <!-- End Layer Morph -->
    <!-- build:vendor app/js/vendor.js-->
    <!--build-vendor-start-->
    <script src="vendor/modernizr/modernizr.js"></script>
    <script src="vendor/jquery/dist/jquery.min.js"></script>
    <script src="vendor/jquery-ui/jquery-ui.min.js"></script>
    <script src="vendor/angular/angular.min.js"></script>
    <script src="vendor/angular-route/angular-route.min.js"></script>
    <script src="vendor/angular-cookies/angular-cookies.min.js"></script>
    <script src="vendor/angular-animate/angular-animate.min.js"></script>
    <script src="vendor/angular-touch/angular-touch.min.js"></script>
    <script src="vendor/angular-sanitize/angular-sanitize.min.js"></script>
    <script src="vendor/angular-resource/angular-resource.min.js"></script>
    <script src="vendor/angular-translate/angular-translate.min.js"></script>
    <script src="vendor/ngstorage/ngStorage.min.js"></script>
    <script src="vendor/angular-ui-router/release/angular-ui-router.min.js"></script>
    <script src="vendor/angular-breadcrumb/uiBreadcrumbs.js"></script>
    <script src="vendor/angular-ui-utils/ui-utils.min.js"></script>
    <script src="vendor/angular-translate-loader-url/angular-translate-loader-url.min.js"></script>
    <script src="vendor/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js"></script>
    <script src="vendor/angular-translate-storage-local/angular-translate-storage-local.min.js"></script>
    <script src="vendor/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js"></script>
    <script src="vendor/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script src="vendor/angular-dynamic-locale/dist/tmhDynamicLocale.js"></script>
    <script src="vendor/oclazyload/dist/ocLazyLoad.min.js"></script>
    <script src="vendor/angular-loading-bar/build/loading-bar.min.js"></script>
    <script src="vendor/angular-ui-select/dist/select.js"></script>
    <script src="vendor/chosen/chosen.jquery.min.js"></script>
    <script src="vendor/chosen/angular-chosen.min.js"></script>
    <!-- vendor google chart-->
    <script src="vendor/google-charts/loader.js"></script>
	<!--build-vendor-end-->
    <!-- ------------------------------------------------------------------------------------------------------------------------------------------------- -->
    <script src="src/js/app.module.js"></script>
    <script src="src/js/modules/colors/colors.constant.js"></script>
    <script src="src/js/modules/colors/colors.run.js"></script>
    <script src="src/js/modules/colors/colors.service.js"></script>
    <script src="src/js/modules/common/directives/animate-enabled.directive.js"></script>
    <script src="src/js/modules/common/directives/promise-app.directive.js"></script>
    <script src="src/js/modules/common/directives/chained-animation.directive.js"></script>

    <script src="src/js/modules/common/directives/fullscreen.directive.js"></script>

    <script src="src/js/modules/common/services/browser-detection.service.js"></script>
    <script src="src/js/modules/common/services/support.service.js"></script>
    <script src="src/js/modules/common/services/touch-drag.service.js"></script>
    <script src="src/js/modules/core/config.js"></script>
    <script src="src/js/modules/core/constants.js"></script>
    <script src="src/js/modules/core/core.controller.js"></script>
    <script src="src/js/modules/core/core.run.js"></script>

    <script src="src/js/modules/loadingbar/loadingbar.config.js"></script>
    <script src="src/js/modules/loadingbar/loadingbar.run.js"></script>


    <script src="src/js/modules/routes/routes.config.js"></script>
    <script src="src/js/modules/routes/routes.provider.js"></script>
    <script src="src/js/modules/routes/routes.run.js"></script>
    <script src="src/js/modules/routes/vendor.constants.js"></script>
    <script src="src/js/modules/settings/settings.service.js"></script>

    <script src="src/js/modules/translator/localization.config.js"></script>
    <script src="src/js/modules/translator/localization.controller.js"></script>
    <script src="src/js/modules/translator/translator.config.js"></script>
    <script src="src/js/modules/translator/translator.service.js"></script>
    
    <script src="vendor/owl-carousel/owl.carousel.js"></script>
    <script src="vendor/owl-carousel/owl.navigation.js"></script>
    <script src="vendor/owl-carousel/owl.carousel.directives.js"></script>
    
    
    <!-- ------------------------------------------------------------------------------------------------------------------------------------------ -->
    <!-- endbuild-->

    <!-- inform -->
    <link rel="stylesheet" href="src/js/custom/inform/angular-inform.min.css?v=<%=appVersion%>" />
    <!-- After AngularJS -->
    <script src="src/js/custom/inform/angular-inform.min.js?v=<%=appVersion%>"></script>
    <!-- endbuild-->

    <!-- build:js app/js/application.js-->
    <script src="src/js/app.module.js"></script>
	
    <!-- custom directive -->
    <script src="src/js/modules/promise/directive/promise.directive.address.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.login.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.organisasi.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.peralatan.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.formatTextToNumber.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.viewDataPengadaan.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.currencyBlur.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.alphabetSpecialSymbolOnly.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.alphaNumericLimitedSpecialSymbolOnly.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.alphabetOnly.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.numbersOnly.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.numbersWithComma.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.alphaNumericOnly.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.alphaNumericStripOnly.js"></script>
    <script src="src/js/modules/promise/directive/image-filter-preview.js"></script>
    <script src="src/js/modules/promise/directive/requestservice.directive.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.numbersOnlyCopy.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.StripCommaTitik.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikGarisMiringStripGarisBawahAngkaHuruf.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikStripCommaGarismiringAngka.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikStripCommaGarismiringPetiksatu.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikStripPetiksatu.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikStripCommaPetikAngka.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.onErrorSrc.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.onError.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.TitikStripCommaGarismiringKurung.js"></script>
    <script src="src/js/modules/promise/directive/promise.directive.approval.js"></script>
    
    <!-- custom override -->
    <script src="src/js/modules/promise/override/promise.override.negativeSignForCurrencyFilter.js"></script>

    <!-- Strat Base Modul -->
    <script src="src/js/modules/colors/colors.constant.js"></script>
    <script src="src/js/modules/colors/colors.run.js"></script>
    <script src="src/js/modules/colors/colors.service.js"></script>
    <script src="src/js/modules/common/directives/animate-enabled.directive.js"></script>
    <script src="src/js/modules/common/directives/promise-app.directive.js"></script>
    <script src="src/js/modules/common/directives/chained-animation.directive.js"></script>
    <script src="src/js/modules/common/directives/check-all-table.directive.js"></script>
    <script src="src/js/modules/common/directives/fullscreen.directive.js"></script>
    <script src="src/js/modules/common/directives/reset-key.directive.js"></script>
    <script src="src/js/modules/common/filters/title-case.filter.js"></script>
    <script src="src/js/modules/common/filters/short-number.filter.js"></script>
    <script src="src/js/modules/common/services/browser-detection.service.js"></script>
    <script src="src/js/modules/common/services/support.service.js"></script>
    <script src="src/js/modules/common/services/touch-drag.service.js"></script>
    <script src="src/js/modules/core/config.js"></script>
    <script src="src/js/modules/core/constants.js"></script>
    <script src="src/js/modules/core/core.controller.js"></script>
    <script src="src/js/modules/core/core.run.js"></script>
    <script src="src/js/modules/dashboard/dashboard.controller.js"></script>
    <script src="src/js/modules/dashboard/dashboard-default.controller.js"></script>
    <script src="src/js/modules/dashboard/dashboard.order.controller.js"></script>
    <script src="src/js/modules/dashboard/dashboardp.controller.js"></script>
    <script src="src/js/modules/dashboard/dashboard.anggaran.controller.js"></script>
    <script src="src/js/modules/dashboard/dashboard.perencanaan.controller.js"></script>
    <script src="src/js/modules/layermorph/layer-morph.directive.js"></script>
    <script src="src/js/modules/layermorph/layer-morph.service.js"></script>
    <script src="src/js/modules/translator/localization.config.js"></script>
    <script src="src/js/modules/translator/localization.controller.js"></script>
    <script src="src/js/modules/translator/translator.config.js"></script>
    <script src="src/js/modules/translator/translator.service.js"></script>
    <script src="src/js/modules/ripple/ripple.directive.js"></script>
    <script src="src/js/modules/routes/routes.config.js"></script>
    <script src="src/js/modules/routes/routes.provider.js"></script>
    <script src="src/js/modules/routes/routes.run.js"></script>
    <script src="src/js/modules/routes/vendor.constants.js"></script>
	<!--build-js-start-->
    <script src="src/js/modules/settings/settings.service.js"></script>
    <script src="src/js/modules/layout/header-nav.controller.js"></script>
    <script src="src/js/modules/layout/relogin.controller.js"></script>
    <script src="src/js/modules/layout/ui-sidebar.directive.js"></script>
    <script src="src/js/modules/charts/flot-chart-options.services.js"></script>
    <script src="src/js/modules/charts/flot-chart.controller.js"></script>
    <script src="src/js/modules/charts/flot-chart.directive.js"></script>
    <script src="app/js/templates.js"></script>
    <script src="src/js/modules/loadingbar/loadingbar.config.js"></script>
    <script src="src/js/modules/loadingbar/loadingbar.run.js"></script>
    <script src="vendor/owl-carousel/owl.carousel.js"></script>
    <script src="vendor/owl-carousel/owl.navigation.js"></script>
    <script src="vendor/owl-carousel/owl.autoplay.js"></script>
    <script src="vendor/owl-carousel/owl.carousel.directives.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/product/ecatalog_detail_controller.js"></script>
<!--    <script src="src/js/modules/promise/procurement/catalog/vendor/vendor_compare.controller.js"></script>-->
    <script src="src/js/modules/promise/procurement/catalog/product/ecatalog_cart_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/product/my-controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/product/product_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/product/my-controller.js"></script>
    <script src="src/js/modules/apps/mailbox.controller.js"></script>
    <!-- End Base Modul -->
	
    <!-- Promise Controller -->
    <script src="src/js/modules/promise/procurement/login/login.controller.js"></script>
    <script src="src/js/modules/promise/procurement/login/recover.controller.js"></script>
    <script src="src/js/modules/promise/procurement/login/recover.execute.controller.js"></script>
    <script src="src/js/modules/promise/procurement/registrasi/registrasi.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/registrasi.vendor.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/regvendor.footer.controller.js"></script>
    <!-- Inisialisasi -->
    <script src="src/js/modules/promise/procurement/inisialisasi/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/upload.controller.js"></script>
    <!-- Inisialisasi BUMN-->
    <script src="src/js/modules/promise/procurement/inisialisasi/index.bumn.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/edit.bumn.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.kebutuhanmaterialtotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.kebutuhanjasatotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.kebutuhanjasasatuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.bidang.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.bumn.vendor.controller.js"></script>
    <!-- Inisialisasi SMB-->
    <script src="src/js/modules/promise/procurement/inisialisasi/index.smb.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.kebutuhanmaterialtotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.kebutuhanmaterialsatuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.kebutuhanjasatotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.kebutuhanjasasatuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.bidang.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.smb.vendor.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/edit.smb.controller.js"></script>
    <!-- Inisiasliasi 04 -->
    <script src="src/js/modules/promise/procurement/inisialisasi/index.04.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.04.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.04.kebutuhanmaterialtotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.04.bidang.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/add.04.vendor.controller.js"></script>
    <script src="src/js/modules/promise/procurement/inisialisasi/edit.04.controller.js"></script>

    <!-- Rekap Pengadaan -->
    <script src="src/js/modules/promise/procurement/rekappengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/rekappengadaan/detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/rekappengadaan/view.controller.js"></script>

    <!-- Seleksi Pendaftaran -->
    <script src="src/js/modules/promise/procurement/seleksipendaftaran/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/seleksipendaftaran/view.controller.js"></script>
	
   <!--Blacklist Vendor-->
    <script src="src/js/modules/promise/procurement/blacklistvendor/blacklistvendor.add.controller.js"></script>
	<script src="src/js/modules/promise/procurement/blacklistvendor/blacklistvendor.index.controller.js"></script>
	<script src="src/js/modules/promise/procurement/blacklistvendor/blacklistvendor.view.controller.js"></script>
	
    <!--Master Alasan Blacklist-->
    <script src="src/js/modules/promise/procurement/master/alasanblacklist/alasanblacklist.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/alasanblacklist/alasanblacklist.view.controller.js"></script>

    <!--Master Aksi-->
    <script src="src/js/modules/promise/procurement/master/aksi/aksi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/aksi/aksi.view.controller.js"></script>
    
    <!--Role Menu-->
    <script src="src/js/modules/promise/procurement/master/rolemenu/rolemenu.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/rolemenu/rolemenu.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/rolemenu/rolemenu.view.controller.js"></script>
    
    <!--Master Role-->
    <script src="src/js/modules/promise/procurement/master/role/role.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/role/role.view.controller.js"></script>
    
   	<!--Menu-->
    <script src="src/js/modules/promise/procurement/master/menu/menu.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/menu/menu.view.controller.js"></script>
    
    <!-- Menu Aksi -->
   	<script src="src/js/modules/promise/procurement/master/menuaksi/menuaksi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/menuaksi/menuaksi.view.controller.js"></script>
    
    <!--Email notification-->
    <script src="src/js/modules/promise/procurement/master/emailnotification/emailnotification.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/emailnotification/emailnotification.view.controller.js"></script>
    
    <!--Email notification Log-->
    <!--penambahan modul KAI 22/12/2020 [21152]-->
    <script src="src/js/modules/promise/procurement/master/emailnotificationlog/emailnotificationlog.index.controller.js"></script>
    
    <!-- Email STMP-->
    <script src="src/js/modules/promise/procurement/master/emailsmtp/emailsmtp.index.controller.js"></script>
    
    <!-- Item Group -->
    <script src="src/js/modules/promise/procurement/master/itemgroup/itemgroup.index.controller.js"></script>
    
    <!-- Bidang Usaha -->
    <script src="src/js/modules/promise/procurement/master/bidangusaha/bidangusaha.index.controller.js"></script>

    <script src="src/js/modules/promise/procurement/master/sla/mastersla.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminsla/adminsla.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminsla/adminsla.view.controller.js"></script>

    <!-- Purchase Request -->
    <script src="src/js/modules/promise/procurement/purchaserequest/purchaserequest.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequest/purchaserequest.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequest/purchaserequest.edit.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequest/purchaserequest.item-add.controller.js"></script>

    <!-- Purchase Request Consolidation -->
    <script src="src/js/modules/promise/procurement/purchaserequestconsolidation/purchaserequestconsolidation.index.controller.js"></script>


    <!-- Purchase Request Verification -->
    <script src="src/js/modules/promise/procurement/purchaserequestverification/purchaserequestverification.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequestverification/purchaserequestverification.verify.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequestverification/purchaserequestverification.replacecatalog.controller.js"></script>

    <!-- Purchase Order -->
    <script src="src/js/modules/promise/procurement/purchaseorder/purchaseorder.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaseorder/purchaseorder.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaseorder/purchaseorder.vendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaseorder/purchaseorder.vendor.detail.controller.js"></script>
	
	<!-- Delivery Received -->
	<script src="src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.index.controller.js"></script>
	
    <!-- Request for Quotation -->
    <script src="src/js/modules/promise/procurement/requestforquotation/requestforquotation.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/requestforquotation/requestforquotation.add.controller.js"></script>

    
    <!-- Pelaksana Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.viewteam.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/pelaksanapengadaan/pelaksanapengadaan.viewpejabat.controller.js"></script>
    
	
    
	
    <!-- User -->
    <script src="src/js/modules/promise/procurement/master/user/user.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/user/user.view.controller.js"></script>

    <!-- Hari Libur -->
    <script src="src/js/modules/promise/procurement/master/harilibur/harilibur.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/harilibur/harilibur.view.controller.js"></script>

    <!-- Bank -->
    <script src="src/js/modules/promise/procurement/master/bank/bank.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/bank/bank.view.controller.js"></script>
	
   <!-- Pembuat Keputusan -->
    <script src="src/js/modules/promise/procurement/master/pembuatkeputusan/pembuatkeputusan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/pembuatkeputusan/pembuatkeputusan.view.controller.js"></script>
    <!-- <script src="src/js/modules/promise/procurement/master/pembuatkeputusan/edit.controller.js"></script> -->

    <!-- Kondisi Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/kondisipengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kondisipengadaan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kondisipengadaan/edit.controller.js"></script>

    <!-- Registrasi Jadwal Tahapan Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/registrasijadwaltahapanpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/registrasijadwaltahapanpengadaan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/registrasijadwaltahapanpengadaan/edit.controller.js"></script>

    <!-- Conditional Price -->
    <script src="src/js/modules/promise/procurement/master/conditionalprize/conditionalprice.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/conditionalprize/conditionalprice.view.controller.js"></script>

    <!-- Jenis Pajak -->
    <script src="src/js/modules/promise/procurement/master/jenispajak/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/jenispajak/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/jenispajak/edit.controller.js"></script>

    <!-- Kategori Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/kategoripengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kategoripengadaan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kategoripengadaan/edit.controller.js"></script>

    <!-- Kualifikasi Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/kualifikasipengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kualifikasipengadaan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kualifikasipengadaan/edit.controller.js"></script>

    <!-- Kualifikasi Vendor -->
    <script src="src/js/modules/promise/procurement/master/kualifikasivendor/kualifikasivendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kualifikasivendor/kualifikasivendor.view.controller.js"></script>

    <!-- Mata Uang -->
    <script src="src/js/modules/promise/procurement/master/matauang/matauang.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/matauang/matauang.view.controller.js"></script>

    <!-- Termin Kontrak -->
    <script src="src/js/modules/promise/procurement/master/terminkontrak/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/terminkontrak/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/terminkontrak/edit.controller.js"></script>

    <!-- Vendor Requirement -->
    <script src="src/js/modules/promise/procurement/master/vendorrequirement/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/vendorrequirement/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/vendorrequirement/edit.controller.js"></script>

    <!-- Metode Penawaran Harga -->
  	<script src="src/js/modules/promise/procurement/master/metodepenawaranharga/metodepenawaranharga.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/metodepenawaranharga/metodepenawaranharga.view.controller.js"></script>
    
    <!-- Sistem Evaluasi Penawaran -->
    <script src="src/js/modules/promise/procurement/master/sistemevaluasipenawaran/sistemevaluasipenawaran.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/sistemevaluasipenawaran/sistemevaluasipenawaran.view.controller.js"></script>

    <!-- Sub Bidang Usaha -->
    <script src="src/js/modules/promise/procurement/master/subbidangusaha/subbidangusaha.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/subbidangusaha/subbidangusaha.view.controller.js"></script>

    <!-- Tahapan -->
    <script src="src/js/modules/promise/procurement/master/tahapan/tahapan.index.controller.js"></script>
    <!-- <script src="src/js/modules/promise/procurement/master/tahapan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/tahapan/edit.controller.js"></script> -->
	<script src="src/js/modules/promise/procurement/master/tahapan/tahapan.view.controller.js"></script>
	
    <!-- Jenis Penawaran -->
    <script src="src/js/modules/promise/procurement/master/jenispenawaran/jenispenawaran.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/jenispenawaran/jenispenawaran.view.controller.js"></script>

   <!-- Item -->
    <script src="src/js/modules/promise/procurement/master/item/item.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/item/item.view.controller.js"></script>
    <!-- <script src="src/js/modules/promise/procurement/master/item/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/item/edit.controller.js"></script> -->

    <!-- Afco -->
    <script src="src/js/modules/promise/procurement/master/afco/afco.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/afco/afco.view.controller.js"></script>

   	<!-- ItemType -->
    <script src="src/js/modules/promise/procurement/master/itemtype/itemtype.index.controller.js"></script>
  	<script src="src/js/modules/promise/procurement/master/itemtype/itemtype.view.controller.js"></script>

    <!-- Jenis Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/jenispengadaan/jenispengadaan.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/jenispengadaan/jenispengadaan.index.controller.js"></script>
    
	<!-- Metode Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/metodepengadaan/metodepengadaan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/metodepengadaan/metodepengadaan.view.controller.js"></script>

    <!-- Metode Penyampaian Dokumen Pengadaan -->
    <script src="src/js/modules/promise/procurement/master/metodepenyampaiandokumenpengadaan/metodepenyampaiandokumenpengadaan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/metodepenyampaiandokumenpengadaan/metodepenyampaiandokumenpengadaan.view.controller.js"></script>
    
    <!-- Jabatan -->
    <script src="src/js/modules/promise/procurement/master/jabatan/jabatan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/jabatan/jabatan.view.controller.js"></script>

    <!-- Satuan -->
    <script src="src/js/modules/promise/procurement/master/satuan/satuan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/satuan/satuan.view.controller.js"></script>

    <!-- Kriteria Administrasi -->
    <script src="src/js/modules/promise/procurement/master/kriteriaadministrasi/kriteriaadministrasi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kriteriaadministrasi/kriteriaadministrasi.view.controller.js"></script>

    <!-- Kriteria Kualifikasi Administrasi -->
    <script src="src/js/modules/promise/procurement/master/kriteriakualifikasiadministrasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kriteriakualifikasiadministrasi/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kriteriakualifikasiadministrasi/edit.controller.js"></script>
    
    <!-- Parameter -->
    <script src="src/js/modules/promise/procurement/master/parameter/parameter.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/parameter/parameter.view.controller.js"></script>

    <!-- Formula Penilaian -->
    <script src="src/js/modules/promise/procurement/master/formulapenilaian/formulapenilaian.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/formulapenilaian/formulapenilaian.view.controller.js"></script>

    <!-- Admin SLA -->
    <script src="src/js/modules/promise/procurement/master/adminslavendor/adminslavendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/adminslavendor/adminslavendor.view.controller.js"></script>

    <!-- Dokumen Expired -->
    <script src="src/js/modules/promise/procurement/vendordokumenexpired/vendordokumenexpired.index.controller.js"></script>

    <!-- Indikator Penilaian -->
    <script src="src/js/modules/promise/procurement/master/indikatorpenilaian/indikatorpenilaian.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/indikatorpenilaian/indikatorpenilaian.view.controller.js"></script>


   	<!-- Kriteria Teknis -->
    <script src="src/js/modules/promise/procurement/master/kriteriateknis/kriteriateknis.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/kriteriateknis/kriteriateknis.view.controller.js"></script>
    
    <!-- Organisasi -->
    <script src="src/js/modules/promise/procurement/master/organisasi/organisasi.index.controller.js"></script>

    <!-- Approval Tahapan -->
    <script src="src/js/modules/promise/procurement/master/approvaltahapan/approvaltahapan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/approvaltahapan/approvaltahapan.view.controller.js"></script>

    <!-- Negara -->
    <script src="src/js/modules/promise/procurement/master/negara/negara.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/negara/negara.view.controller.js"></script>

    <!-- Application Generate PR Number -->
    <script src="src/js/modules/promise/procurement/application/generateprnumber/generateprnumber.index.controller.js"></script>
    <!--
    <script src="src/js/modules/promise/procurement/application/generateprnumber/generatenumber.add.controller.js"></script>
    -->

    <!-- Panitia - Jadwal Pengadaan -->
    <script src="src/js/modules/promise/procurement/jadwalpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/jadwalpengadaan/detil.controller.js"></script>
    <script src="src/js/modules/promise/procurement/jadwalpengadaan/detil2.controller.js"></script>

    <!-- Panitia --Pemasukan Penawaran -->
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/view.satuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/view.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/detail.satuan.controller.js"></script>

    <!-- Panitia --Pembukaan Penawaran -->
    <script src="src/js/modules/promise/procurement/pembukaanpenawaran/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembukaanpenawaran/detil.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembukaanpenawaran/detil.satuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembukaanpenawaran/detil.total.cetak.controller.js"></script>

    <!-- Panitia --Pembukaan Penawaran Dua Tahap -->
    <!-- Tahap Satu -->
    <script src="src/js/modules/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/detil.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembukaanpenawaranduatahap/tahapsatu/detil.satuan.controller.js"></script>

    <!-- Panitia --Kriteria Evaluasi -->
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.satuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.merit.total.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.satuandetail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.satuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/kriteriaevaluasi/kriteria.evaluasi.add.merit.satuan.controller.js"></script>

    <!-- Panitia --Evaluasi Teknis -->
    <script src="src/js/modules/promise/procurement/evaluasiteknis/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiteknis/total/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiteknis/total/cetak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiteknis/total/sistemgugur/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiteknis/satuan/index.controller.js"></script>

    <!-- Panitia --Evaluasi Administrasi-->
    <script src="src/js/modules/promise/procurement/evaluasiadministrasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiadministrasi/edit.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiadministrasi/cetak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiadministrasi/edit.satuan.controller.js"></script>

    <!-- Panitia --Penjelasan -->
    <script src="src/js/modules/promise/procurement/penjelasan/penjelasan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penjelasan/penjelasan.detail.controller.js"></script>

    <!-- Vendor --Approval -->
    <script src="src/js/modules/promise/procurement/vendor/approval/vendor.approval.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/approval/vendor.approval.detail.controller.js"></script>

    <script src="src/js/modules/promise/procurement/vendor/approval/registrasivendor.approval.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/approval/perpanjangantdr.approval.detail.controller.js"></script>

    <!--Daftar Vendor--->
    <script src="src/js/modules/promise/procurement/vendor/daftarvendor/vendor.daftarvendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/daftarvendor/vendor.daftarvendor.detail.controller.js"></script>
  
    <!--  Portal -->
    <script src="src/js/modules/promise/procurement/portal/portal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/portal/portal.carousel.controller.js"></script>
    <script src="src/js/modules/promise/procurement/portal/portal.map.controller.js"></script>
	<script src="src/js/modules/promise/procurement/portal/ecatalog_portal_compare_controller.js"></script>
	<script src="src/js/modules/promise/procurement/portal/evendor_detail_portal_controller.js"></script>
	
    <!-- Sidebar -->
    <script src="src/js/modules/promise/dashboard/sidebar.menu.controller.js"></script>

    <!-- error page -->
    <script src="src/js/modules/promise/errorPage/errorPage.controller.js"></script>

    <!-- Negosiasi -->
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.index.controller.js"></script>

    <!-- Negosiasi Total - Panitia -->
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.total.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.total.detail.more.controller.js"></script>
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.total.detail.history.controller.js"></script>
        <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.total.detail.cetak.controller.js"></script>

    <!-- Negosiasi Total - Vendor -->
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.vendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.vendor.total.detail.controller.js"></script>

    <!-- Negosiasi Satuan - Panitia -->
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.satuan.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.satuan.detail.penawaran.controller.js"></script>
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.satuan.detail.penawaran.more.controller.js"></script>

    <!-- Negosiasi Satuan - Vendor -->
    <script src="src/js/modules/promise/procurement/negosiasi/negosiasi.vendor.satuan.detail.controller.js"></script>

    <!-- Sanggahan - Vendor -->
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.vendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.vendor.total.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.vendor.satuan.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.vendor.satuan.detail.more.controller.js"></script>

    <!-- Sanggahan - Panitia -->
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.total.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.satuan.listitem.controller.js"></script>
    <script src="src/js/modules/promise/procurement/sanggahan/sanggahan.satuan.detail.controller.js"></script>

    <!-- Vendor - Data Registrasi -->
    <script src="src/js/modules/promise/procurement/vendor/datauser/registrasi.datauser.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/dataperusahaan/registrasi.dataperusahaan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/databank/registrasi.databank.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/databank/registrasi.databank.view.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/vendor/datasegmentasi/registrasi.datasegmentasi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/datasegmentasi/registrasi.datasegmentasi.view.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/vendor/datakeuangan/registrasi.datakeuangan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/datakeuangan/registrasi.datakeuangan.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/datadokumen/registrasi.datadokumen.controller.js"></script>
    
     <!-- Vendor - Registrasi Data Peralatan -->
    <script src="src/js/modules/promise/procurement/vendor/dataperalatan/registrasi.dataperalatan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/dataperalatan/registrasi.dataperalatan.view.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/vendor/datapengalaman/registrasi.datapengalaman.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/datapengalaman/registrasi.datapengalaman.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/vendor/datasertifikat/datasertifikat.controller.js"></script>

    <!-- Vendor - Undangan Vendor -->
    <script src="src/js/modules/promise/procurement/undanganpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/undanganpengadaan/detil.controller.js"></script>

    <!-- Vendor - Hasil Pengadaan -->
    <script src="src/js/modules/promise/procurement/hasilpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/hasilpengadaan/detil.controller.js"></script>

    <!-- Panitia --Pendaftaran Pengadaan -->
    <script src="src/js/modules/promise/procurement/pengumumanpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengumumanpengadaan/view/index.controller.js"></script>

    <!-- Vendor --Pemasukan Penawaran -->
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/data.penawaranvendorsatusampul.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/data.penawaranvendorduasampul.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/data.penawaranvendortahapsatu.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/datapenawarantahapdua.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/datapenawaran/data.penawaranvendortahapdua.controller.js"></script>

    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaran.vendorhargatotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaran.vendorhargasatuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaran.vendorhargadetailmaterial.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranharga/penawaran.vendorhargadetailjasa.controller.js"></script>

    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/hargatotal.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/hargasatuan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/hargasatuanmaterial.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pemasukanpenawaran/vendor/penawaranhargaindependence/hargasatuanjasa.controller.js"></script>

    <!-- Vendor - Undangan Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/undangankualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/undangankualifikasi/detil.controller.js"></script>

    <!-- vendor --Pengumuman Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/pengumumankualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/pengumumankualifikasi/detil.controller.js"></script>

    <!-- vendor --Data Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/datakualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/datakualifikasi/detil.controller.js"></script>

    <!-- Panitia --Klarifikasi Harga -->
    <script src="src/js/modules/promise/procurement/klarifikasiharga/klarifikasiharga.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/klarifikasiharga/satuan/klarifikasiharga.satuan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/klarifikasiharga/satuan/klarifikasiharga.satuan.detil.controller.js"></script>
    <script src="src/js/modules/promise/procurement/klarifikasiharga/total/klarifikasiharga.total.index.controller.js"></script>

    <!-- Panitia --Evaluasi Harga Simple -->
    <!---- Auction ---->
    <script src="src/js/modules/promise/procurement/evaluasiharga/evaluasihargasimple.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasihargasimple.total.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasihargasimple.sesiauction.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasihargasimple.historyauction.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasihargasimple.diskualifikasi.view.controller.js"></script>
    <!-- Panitia --Evaluasi Harga -->
    <!---- Auction ---->
    <script src="src/js/modules/promise/procurement/evaluasiharga/evaluasiharga.index.controller.js"></script>
    <!----- Total ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.total.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.sesiauction.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.historyauction.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.diskualifikasi.view.controller.js"></script>
     <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.total.cetak.controller.js"></script>
    <!---- Satuan ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.add.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.sesiauction.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.historyauction.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.diskualifikasi.view.controller.js"></script>

    <!---- Bidding ---->
    <script src="src/js/modules/promise/procurement/evaluasiharga/evaluasiharga.bidding.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/evaluasihargasimple.bidding.index.controller.js"></script>
    <!----- Total ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasiharga.total.bidding.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/total/evaluasihargasimple.total.bidding.view.controller.js"></script>
    <!---- Satuan ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/satuan/evaluasiharga.satuan.bidding.view.controller.js"></script>

    <!-- Vendor --Evaluasi Harga --Penawaran Harga -->
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/penawaranharga.total.index.controller.js"></script>
    <!----- Total ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.auction.terbuka.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.pemasukanpenawaran.terbuka.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.auction.tertutup.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/total/penawaranharga.total.pemasukanpenawaran.tertutup.controller.js"></script>
    <!----- Satuan ----->
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.terbuka.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.detail.terbuka.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.pemasukanpenawaran.terbuka.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.tertutup.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.auction.detail.tertutup.controller.js"></script>
    <script src="src/js/modules/promise/procurement/evaluasiharga/vendor/satuan/penawaranharga.satuan.pemasukanpenawaran.tertutup.controller.js"></script>

    <!-- Panitia --Penetapan Pemenang -->
    <script src="src/js/modules/promise/procurement/penetapanpemenang/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/total.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/total.sistemgugur.cetak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/total.meritpoint.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/total.meritpoint.cetak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/satuan.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/satuan.meritpoint.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penetapanpemenang/penetapanpemenang.multiwinner.controller.js"></script>

    <!-- Panitia --Penunjukan Pemenang -->
    <script src="src/js/modules/promise/procurement/penunjukanpemenang/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penunjukanpemenang/total.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penunjukanpemenang/total.meritpoint.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penunjukanpemenang/satuan.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/penunjukanpemenang/satuan.meritpoint.controller.js"></script>

    <!-- Panitia --Usulan Calon Pemenang -->
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/usulancalonpemenang.total.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/usulancalonpemenang.total.meritpoint.controller.js"></script>
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/usulancalonpemenang.total.cetak.meritpoint.controller.js"></script>
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/usulancalonpemenang.satuan.sistemgugur.controller.js"></script>
    <script src="src/js/modules/promise/procurement/usulancalonpemenang/usulancalonpemenang.satuan.meritpoint.controller.js"></script>

    <!-- Panitia --SPK -->
    <script src="src/js/modules/promise/procurement/spk/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/spk/view/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/spk/index-spk.controller.js"></script>

    <!-- Panitia --Pengambilan Dokumen -->
    <script src="src/js/modules/promise/procurement/pengambilandokumen/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengambilandokumen/view/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengambilandokumen/view/pengambilandokumencetak.controller.js"></script>

    <!-- Panitia --Alokasi Anggaran -->
    <script src="src/js/modules/promise/procurement/alokasianggaran/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.gabung.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.gabung.list.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.gabung.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.index.02.controller.js"></script>

    <!-- Perencanaan -->
    <script src="src/js/modules/promise/procurement/perencanaan/perencanaan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/perencanaan/perencanaan.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/perencanaan/perencanaan.detail.add.controller.js"></script>

    <!-- Panitia --History Pengadaan -->
    <script src="src/js/modules/promise/procurement/historypengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/historypengadaan/historypengadaan.view.controller.js"></script>

    <!-- Manajemen Kontrak -->
    <script src="src/js/modules/promise/procurement/manajemenkontrak/index.viewpengadaan.controller.js"></script>
    <script src="src/js/modules/promise/procurement/manajemenkontrak/create.kontrak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/manajemenkontrak/index.viewkontrak.controller.js"></script>
    <script src="src/js/modules/promise/procurement/manajemenkontrak/edit.kontrak.controller.js"></script>

    <!-- Jadwal Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/jadwalkualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/jadwalkualifikasi/detil2.controller.js"></script>

    <!-- Pengambilan Dokumen Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/pengambilandokumenkualifikasi/view/index.controller.js"></script>

    <!-- Pemasukan Dokumen Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/pemasukandokumenkualifikasi/view/index.controller.js"></script>

    <!-- Kriteria Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/kriteriakualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/kriteriakualifikasi/view/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/kriteriakualifikasi/viewjasa/index.controller.js"></script>

    <!-- Panitia -- Delegasi Pengadaan -->
    <script src="src/js/modules/promise/procurement/delegasipengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/delegasipengadaan/detil.controller.js"></script>

    <!-- Seleksi Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiadministrasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksidukunganbank/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikemampuandasar/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksikeuangan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksiteknis/index.controller.js"></script>
    <!--<script src="src/js/modules/promise/procurement/prakualifikasi/seleksikualifikasi/view/seleksinilaikemampuanpaket/index.controller.js"></script>-->
    <script src="src/js/modules/promise/procurement/prakualifikasi/sanggahanprakualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/sanggahanprakualifikasi/detil.controller.js"></script>
    <!-- Panitia -- Pembuktian Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/persetujuankualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/persetujuankualifikasi/detil.controller.js"></script>

    <!-- Panitia -- Pembuktian Kualifikasi -->
    <script src="src/js/modules/promise/procurement/pembuktiankualifikasi/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pembuktiankualifikasi/detil.controller.js"></script>
    <!-- Panitia - Persetujuan Pengadaan -->
    <script src="src/js/modules/promise/procurement/persetujuanpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/persetujuanpengadaan/detail.controller.js"></script>
    <!-- Panitia - Delegasi Persetujuan Pengdaan -->
    <script src="src/js/modules/promise/procurement/delegasipersetujuanpengadaan/index.controller.js"></script>
    <!-- Panitia - Pengumuman Evaluasi Admin Teknis -->
    <script src="src/js/modules/promise/procurement/pengumumanevaluasiadminteknis/pengumumanevaluasiadminteknis.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengumumanevaluasiadminteknis/pengumumanevaluasiadminteknis.detil.controller.js"></script>
    <!-- Panitia - Hasil Kualifikasi -->
    <script src="src/js/modules/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.detil.controller.js"></script>
    <script src="src/js/modules/promise/procurement/prakualifikasi/hasilkualifikasi/hasilkualifikasi.index.controller.js"></script>
    <!-- Approval - Master Setting -->
    <script src="src/js/modules/promise/procurement/master/approval/approval.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/approval/approval.view.controller.js"></script>

    <!-- Approval Process -->
    <script src="src/js/modules/promise/procurement/approval/approval.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.pr.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.bo.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.updatevendor.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.po.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.blacklist.vendor.controller.js"></script>
    <script src="src/js/modules/promise/procurement/approval/approval.view.unblacklist.vendor.controller.js"></script>

    <!--KPI Monitoring-->
    <script src="src/js/modules/promise/procurement/kpimonitoring/kpimonitoring.index.controller.js"></script>

    <!-- Manajemen Vendor -->
    <script src="src/js/modules/promise/procurement/manajemenvendor/manajemenvendor.penilaian.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/rekaphistoryvendor/rekaphistoryvendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/rekaphistoryvendor/rekaphistoryvendor.view.controller.js"></script>
    
     <!-- Data Sertifikat Vendor -->
    <script src="src/js/modules/promise/procurement/datasertifikat/datasertifikat.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/datasertifikat/datasertifikat.view.controller.js"></script>
    
    <!-- Pengadaan Gagal -->
    <script src="src/js/modules/promise/procurement/pengadaangagal/pengadaangagal.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengadaangagal/pengadaangagal.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengadaangagal/pengadaangagal.edit.controller.js"></script>
    <script src="src/js/modules/promise/procurement/pengadaangagal/pengadaangagal.view.controller.js"></script>

    <!-- Pengadaan Gagal -->
    <script src="src/js/modules/promise/procurement/pengadaanulang/pengadaanulang.index.controller.js"></script>

    <!-- Manajemen Vendor -->
    <script src="src/js/modules/promise/procurement/adminslavendor/registrasi/adminslavendor.registrasi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminslavendor/registrasi/adminslavendor.registrasi.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminslavendor/perpanjangan/adminslavendor.perpanjangan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminslavendor/blacklist/adminslavendor.blacklist.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/adminslavendor/unblacklist/adminslavendor.unblacklist.index.controller.js"></script>

    <!-- Master -->
    <script src="src/js/modules/promise/procurement/master/webservicemanager/webservicemanager.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/webservicemanager/webservicemanager.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/webservicemanager/webservicemanager.edit.controller.js"></script>
 
	<!-- Sales Order Vendor -->
	<script src="src/js/modules/promise/procurement/vendor/sales_order/sales.order.vendor.index.controller.js"></script>

    <!-- TEST TABEL 
    <script src="src/js/modules/promise/procurement/test/testtabel/index-testtabel.controller.js"></script>-->
    <script src="src/js/test/test_mamatfusion01.controller.js"></script>
    <script src="src/js/test/test_tables.js"></script>
    
    <script src="src/js/modules/promise/procurement/catalog/product/catalog_controller.js"></script>
    
    <!-- Master Category -->
    <script src="src/js/modules/promise/procurement/catalog/category/mastercategory.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/category/mastercategory.view.controller.js"></script>
    
    <!-- Panitia - AddressBook -->
  	<script src="src/js/modules/promise/procurement/master/addressbook/addressbook.index.controller.js"></script>
  	<script src="src/js/modules/promise/procurement/master/addressbook/addressbook.view.controller.js"></script>
  	
  	<!-- Booking Order -->
  	<script src="src/js/modules/promise/procurement/bookingorder/bookingorder.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/bookingorder/bookingorder.view.controller.js"></script>
    <script src="src/js/modules/promise/procurement/bookingorder/bookingorder.detail.controller.js"></script>
    
    <!--Role Jabatan-->
    <script src="src/js/modules/promise/procurement/master/rolejabatan/rolejabatan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/rolejabatan/rolejabatan.view.controller.js"></script>
    
    <!-- MonitoringWorklist -->
    <script src="src/js/modules/promise/procurement/monitoringworklist/monitoringworklist.index.controller.js"></script>
    
    <!-- Laporan Catalog s\d Laporan anggaran -->
    <script src="src/js/modules/promise/procurement/laporan/laporancatalog.index.controller.js"></script>    
    <script src="src/js/modules/promise/procurement/laporan/laporanvendor.index.controller.js"></script>  	 	  
    <script src="src/js/modules/promise/procurement/laporan/laporanpurchaseorder.index.controller.js"></script>    
    <script src="src/js/modules/promise/procurement/laporan/laporancatalogcontrack.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/laporan/laporanbudgetdanrealisasi.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/laporan/laporanevaluasikinerjavendor.index.controller.js"></script> 
    <script src="src/js/modules/promise/procurement/laporan/laporanevaluasikinerjavendor.detail.controller.js"></script>
    <script src="src/js/modules/promise/procurement/laporan/laporanpembelian.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/laporan/laporanitemkatalogpo.index.controller.js"></script>
    
     <!-- Rating -->
    <script src="src/js/modules/promise/procurement/rating/ratingdeliveryreceipt.index.controller.js"></script>  
    
     <!-- UserAdditional -->
    <script src="src/js/modules/promise/procurement/master/useradditional/useradditional.index.controller.js"></script>  
    <script src="src/js/modules/promise/procurement/master/useradditional/useradditional.view.controller.js"></script> 
     
    <!--Org ApprovalPath-->
    <script src="src/js/modules/promise/procurement/master/approvalpath/approvalpath.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/approvalpath/approvalpath.view.controller.js"></script>
    
    <!--Manage Catalog-->
    <script src="src/js/modules/promise/procurement/catalog/catalog_manage_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/catalog_import_controller.js"></script>
    
    <!--View Catalog-->
    <script src="src/js/modules/promise/procurement/catalog/product/ecatalog_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/product/ecatalog_compare_controller.js"></script>
    
    <!-- Master Attribute -->
    <script src="src/js/modules/promise/procurement/catalog/attribute/attribute.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attribute/attribute.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attribute/attribute.edit.controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attribute/attribute.import.controller.js"></script>
    
    <!-- Import Master Category -->
    <script src="src/js/modules/promise/procurement/catalog/category/master_category_import_controller.js"></script>
    
    <!-- Attribute Group -->
    <script src="src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_add_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_edit_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/attributegroup/master_attribute_set_import_controller.js"></script>
    
    <!-- Catalog Vendor -->
    <script src="src/js/modules/promise/procurement/catalog/vendor/evendor_controller.js"></script>
    <script src="src/js/modules/promise/procurement/catalog/vendor/evendor_detail_controller.js"></script>
    
    <!-- Report Monitoring Pengadaan -->
    <script src="src/js/modules/promise/procurement/report/monitoringpengadaan.index.controller.js"></script>
    
    <!-- Report Monitoring Vendor -->
    <script src="src/js/modules/promise/procurement/report/monitoringvendor.index.controller.js"></script>
    
    <!-- Tambahan Terakhir -->
    <script src="src/js/modules/promise/procurement/portal/home_controller.js"></script>    
    <script src="src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.vendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/alokasianggaran/alokasianggaran.index.02.import.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequest/purchaserequest_import_controller.js"></script>
    <script src="src/js/modules/promise/procurement/requestforquotation/requestforquotation.vendor.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.import.controller.js"></script>
    <script src="src/js/modules/promise/procurement/invoicematching/invoicematch.index.controller.js"></script>
    <script src="src/js/custom/fe-controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequestjoin/purchaserequestjoin.add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/purchaserequestjoin/purchaserequestjoin.index.controller.js"></script>
    <!-- Table Log KAI 22042021 [23186]-->
    <script src="src/js/modules/promise/procurement/modules/tablelog/tablelog.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/modules/tablelog/tablelog.view.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/master/barang/barang.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/barang/barang.add.edit.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/transaction/penjualan/penjualan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/transaction/penjualan/penjualan.detail.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/master/karyawan/karyawan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/karyawan/karyawan.add.edit.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/master/position/position.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/position/position.add.edit.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/master/mobil/mobil.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/mobil/mobil.add.edit.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/master/pelanggan/pelanggan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/pelanggan/pelanggan.add.edit.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/bonus/bonus.index.controller.js"></script>
    
    <script src="src/js/modules/promise/procurement/transaction/laporanpenjualan/laporanpenjualan.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/transaction/laporanpenjualan/laporanpenjualan.detail.controller.js"></script>
    
    <!--build-js-end-->
    
    <!-- Delivery Received -->
	<script src="src/js/modules/promise/procurement/deliveryreceived/deliveryreceived.detail.controller.js"></script>
    
	<!-- <script src="src/js/modules/settings/settings.controller.js"></script> -->
	<script src="/promisews/procurement/generatorservices/js/settingController/<%=appVersion%>"></script>
	
    <!-- Alur Pengadaan -->
    <!-- <script src="src/js/modules/promise/procurement/master/alurpengadaan/index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/alurpengadaan/add.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/alurpengadaan/edit.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/alurpengadaan/alurpengadaan.02.controller.js"></script>
    <script src="src/js/modules/promise/procurement/master/alurpengadaan/alurpengadaan.02.tambahtahapan.controller.js"></script> -->
	
    <!-- Security -->
    <script type="text/javascript" src="src/js/modules/security/AesUtil.js"></script>
    
    <!-- Table Log KAI 22042021 [23186]-->
    <script src="src/js/modules/promise/procurement/modules/tablelog/tablelog.index.controller.js"></script>
    <script src="src/js/modules/promise/procurement/modules/tablelog/tablelog.view.controller.js"></script>
    
  	<script src="vendor/bootstrap/dist/js/bootstrap.min.js"></script>
    
</body>

</html>