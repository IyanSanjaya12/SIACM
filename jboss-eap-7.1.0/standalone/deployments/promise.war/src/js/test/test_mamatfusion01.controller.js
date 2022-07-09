(function () {
    'use strict';

    angular
        .module('naut')
        .controller('Test01Controller', Test01Controller);

    function Test01Controller($scope) {
        $scope.testHello = "Hello Coy!";
        $scope.attrs = {

            "caption": "Dashboar Pengadaan Barang/Jasa",
            "subCaption": "Mamat Test",
            "numberprefix": "Rp ",
            "xAxisname": "Bulan",
            "yAxisName": "Keuntungan (In IDR)",
            "plotgradientcolor": "",
            "bgcolor": "FFFFFF",
            "showalternatehgridcolor": "0",
            "divlinecolor": "CCCCCC",
            "showvalues": "0",
            "showcanvasborder": "0",
            "canvasborderalpha": "0",
            "canvasbordercolor": "CCCCCC",
            "canvasborderthickness": "1",
            "yaxismaxvalue": "30000",
            "captionpadding": "30",
            "linethickness": "3",
            "yaxisvaluespadding": "15",
            "legendshadow": "0",
            "legendborderalpha": "0",
            "palettecolors": "#f8bd19,#008ee4,#33bdda,#e44a00,#6baa01,#583e78",
            "showborder": "0"
        };

        $scope.attrs2 = {            
            "caption": "Dashboar Pengadaan Barang/Jasa",
            "subCaption": "Mamat Test",
            "xAxisname": "Bulan",
            "yAxisName": "Keuntungan (In IDR)",
            "numberPrefix": "Rp ",
            "plotFillAlpha": "80",
            "paletteColors": "#0075c2,#1aaf5d",
            "baseFontColor": "#333333",
            "baseFont": "Helvetica Neue,Arial",
            "captionFontSize": "14",
            "subcaptionFontSize": "14",
            "subcaptionFontBold": "0",
            "showBorder": "0",
            "bgColor": "#ffffff",
            "showShadow": "0",
            "canvasBgColor": "#ffffff",
            "canvasBorderAlpha": "0",
            "divlineAlpha": "100",
            "divlineColor": "#999999",
            "divlineThickness": "1",
            "divLineIsDashed": "1",
            "divLineDashLen": "1",
            "divLineGapLen": "1",
            "usePlotGradientColor": "0",
            "showplotborder": "0",
            "valueFontColor": "#ffffff",
            "placeValuesInside": "1",
            "showHoverEffect": "1",
            "rotateValues": "1",
            "showXAxisLine": "1",
            "xAxisLineThickness": "1",
            "xAxisLineColor": "#999999",
            "showAlternateHGridColor": "0",
            "legendBgAlpha": "0",
            "legendBorderAlpha": "0",
            "legendShadow": "0",
            "legendItemFontSize": "10",
            "legendItemFontColor": "#666666"
        };

        $scope.categories = [{
            "category": [{
                "label": "Jan"
    }, {
                "label": "Feb"
    }, {
                "label": "Mar"
    }, {
                "label": "Apr"
    }, {
                "label": "May"
    }, {
                "label": "Jun"
    }, {
                "label": "Jul"
    }, {
                "label": "Aug"
    }, {
                "label": "Sep"
    }, {
                "label": "Oct"
    }, {
                "label": "Nov"
    }, {
                "label": "Dec"
    }]
}];

        $scope.dataset = [{
                "seriesname": "Perencanaan",
                "data": [{
                    "value": "22400"
        }, {
                    "value": "24800"
        }, {
                    "value": "21800"
        }, {
                    "value": "21800"
        }, {
                    "value": "24600"
        }, {
                    "value": "27600"
        }, {
                    "value": "26800"
        }, {
                    "value": "27700"
        }, {
                    "value": "23700"
        }, {
                    "value": "25900"
        }, {
                    "value": "26800"
        }, {
                    "value": "24800"
        }]
    },

            {
                "seriesname": "Pengadaan",
                "data": [{
                    "value": "10000"
        }, {
                    "value": "11500"
        }, {
                    "value": "12500"
        }, {
                    "value": "15000"
        }, {
                    "value": "16000"
        }, {
                    "value": "17600"
        }, {
                    "value": "18800"
        }, {
                    "value": "19700"
        }, {
                    "value": "21700"
        }, {
                    "value": "21900"
        }, {
                    "value": "22900"
        }, {
                    "value": "20800"
        }]
    }
];


    }
    Test01Controller.$inject = ['$scope'];
})();