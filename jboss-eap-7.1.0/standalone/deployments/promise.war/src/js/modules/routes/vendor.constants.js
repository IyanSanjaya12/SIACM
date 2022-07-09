/**=========================================================
 * Module: VendorAssetsConstant.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .constant('VENDOR_ASSETS', {
            // jQuery based and standalone scripts
            scripts: {
              'animate':            ['vendor/animate.css/animate.min.css'],
              'icons':              ['vendor/font-awesome/css/font-awesome.min.css',
                                     'vendor/weather-icons/css/weather-icons.min.css',
                                     'vendor/feather/webfont/feather-webfont/feather.css'],
              'sparklines':         ['app/plugins/sparklines/jquery.sparkline.min.js'],
              'slimscroll':         ['vendor/slimscroll/jquery.slimscroll.min.js'],
              'screenfull':         ['vendor/screenfull/dist/screenfull.js'],
              'flot-chart':         ['vendor/flot/jquery.flot.js'],
              'flot-chart-plugins': ['vendor/flot.tooltip/js/jquery.flot.tooltip.min.js',
                                     'vendor/flot/jquery.flot.resize.js',
                                     'vendor/flot/jquery.flot.pie.js',
                                     'vendor/flot/jquery.flot.time.js',
                                     'vendor/flot/jquery.flot.categories.js',
                                     'vendor/flot-spline/js/jquery.flot.spline.min.js'],
              'jquery-ui':          ['vendor/jquery-ui/jquery-ui.min.js',
                                     'vendor/jqueryui-touch-punch/jquery.ui.touch-punch.min.js'],
              'moment' :            ['vendor/moment/min/moment-with-locales.min.js'],
              'gcal':               ['vendor/fullcalendar/dist/gcal.js'],
              'blueimp-gallery':    ['vendor/blueimp-gallery/js/jquery.blueimp-gallery.min.js',
                                     'vendor/blueimp-gallery/css/blueimp-gallery.min.css'],
              'filestyle':          ['vendor/bootstrap-filestyle/src/bootstrap-filestyle.js',         
                                     'vendor/bootstrap-filestyle/src/file-style.directive.js'],         
              
              // ADDITIONAL LIBS
              'TouchSpin':          ['vendor/bootstrap-touchspin/jquery.bootstrap-touchspin.min.js',
                                     'vendor/bootstrap-touchspin/jquery.bootstrap-touchspin.min.css'],
              'daterangepicker':    ['vendor/bootstrap-daterangepicker/daterangepicker.js',
                                     'vendor/bootstrap-daterangepicker/daterangepicker-bs3.css']
            },
            // Angular modules scripts (name is module name to be injected)
            modules: [
              {name: 'toaster',           files: ['vendor/angularjs-toaster/toaster.js',
                                                  'vendor/angularjs-toaster/toaster.css']},
              {name: 'ui.knob',           files: ['vendor/angular-knob/src/angular-knob.js',
                                                  'vendor/jquery-knob/dist/jquery.knob.min.js']},
              {name: 'easypiechart',      files:  ['vendor/jquery.easy-pie-chart/dist/angular.easypiechart.min.js']},
              {name: 'angularFileUpload', files: ['vendor/angular-file-upload/angular-file-upload.min.js']},
              {name: 'ngTable',           files: ['vendor/ng-table/dist/ng-table.min.js',
                                                  'vendor/ng-table/dist/ng-table.min.css']},
              {name: 'ngTableExport',     files: ['vendor/ng-table-export/ng-table-export.js']},
              {name: 'ui.map',            files: ['vendor/angular-ui-map/ui-map.min.js']},
              {name: 'ui.calendar',       files: ['vendor/fullcalendar/dist/fullcalendar.min.js',
                                                  'vendor/fullcalendar/dist/fullcalendar.css',
                                                  'vendor/angular-ui-calendar/src/calendar.js']},
              {name: 'angularBootstrapNavTree',   files: ['vendor/angular-bootstrap-nav-tree/dist/abn_tree_directive.js',
                                                          'vendor/angular-bootstrap-nav-tree/dist/abn_tree.css']},
              {name: 'htmlSortable',              files: ['vendor/html.sortable/dist/html.sortable.js',
                                                          'vendor/html.sortable/dist/html.sortable.angular.js']},
              {name: 'xeditable',                 files: ['vendor/angular-xeditable/dist/js/xeditable.js',
                                                          'vendor/angular-xeditable/dist/css/xeditable.css']},
              {name: 'ngImgCrop',                 files: ['vendor/ng-img-crop/compile/unminified/ng-img-crop.js',
                                                          'vendor/ng-img-crop/compile/unminified/ng-img-crop.css']},
              {name: 'ui.select',                 files: ['vendor/angular-ui-select/dist/select.js',
                                                          'vendor/angular-ui-select/dist/select.css']},
              {name: 'textAngularSetup',          files: ['vendor/textAngular/src/textAngularSetup.js']},
              {name: 'textAngular',               files: ['vendor/textAngular/dist/textAngular-rangy.min.js',
                                                          'vendor/textAngular/src/textAngular.js',
                                                          'vendor/textAngular/src/textAngular.css']},
              {name: 'vr.directives.slider',      files: ['vendor/venturocket-angular-slider/build/angular-slider.min.js']},
              {name: 'datatables',                files: ['vendor/datatables/media/css/jquery.dataTables.min.css',
                                                          'vendor/datatables/media/js/jquery.dataTables.min.js',
                                                          'vendor/angular-datatables/dist/angular-datatables.js'
                                                          ]},
              {name: 'nestable',                  files: ['vendor/nestable/jquery.nestable.js',
                                                          'vendor/nestable/jquery.nestable.css']},
              {name: 'ui.tree',                   files: ['vendor/angular-ui-tree/dist/angular-ui-tree.css',
                                                          'vendor/angular-ui-tree/dist/angular-ui-tree.js']
              },
              //modul promise app
                //Master - Menu Aksi
                {name: 'MenuAksi',      files:['src/js/modules/promise/procurement/master/menuaksi/index.controller.js']},
                {name: 'AddMenuAksi',   files:['src/js/modules/promise/procurement/master/menuaksi/add.controller.js']},
                {name: 'EditMenuAksi',   files:['src/js/modules/promise/procurement/master/menuaksi/edit.controller.js']},
                
              // ADDITIONAL LIBS
              {name: 'ng-fusioncharts',           files: ['vendor/fusioncharts/js/fusioncharts.js',
//                                                          'vendor/fusioncharts/js/themes/fusioncharts.theme.carbon.js',
                                                          'vendor/fusioncharts/angular-fusioncharts.js']},
              {name: 'ez-plus',                   files: ['vendor/angular-elevatezoom/css/jquery.ez-plus.css',
//                                                          'vendor/angular-elevatezoom/css/jquery.fancybox-plus.css',
                                                          'vendor/angular-elevatezoom/js/jquery.ez-plus.js',
//                                                          'vendor/angular-elevatezoom/js/jquery.easing.min.js',
//                                                          'vendor/angular-elevatezoom/js/jquery.mousewheel.js',
//                                                          'vendor/angular-elevatezoom/js/jquery.fancybox-plus.js',
//                                                          'vendor/angular-elevatezoom/js/angular-fancybox-plus.js',
                                                          'vendor/angular-elevatezoom/js/angular-ezplus.js']},
              {name: 'angulargrid',               files: ['vendor/angular-grid/angulargrid.js']},
              {name: 'slimscroll',                files: ['vendor/slimscroll/jquery.slimscroll.min.js']},
              {name: 'tree.dropdown',             files: ['vendor/tree-dropdown/tree-dropdown.js',
                                                          'vendor/tree-dropdown/tree-dropdown.css'
                                                          ]}
            ]

        });

})();

