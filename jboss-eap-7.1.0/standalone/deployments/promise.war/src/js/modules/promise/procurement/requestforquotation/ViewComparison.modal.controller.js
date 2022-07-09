angular
.module('naut')
.controller('PriceComparisonModalInstance', function ($scope, $modalInstance) {
    // Datepicker
    $scope.form = {};

    $scope.itemComparisonList = [{
        nama: 'Komputer Desktop i5 16G',
        supplierList: [{
            nama: 'PT. Global Aksara Informatika',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 10000000,
                description: 'Komputer Programmer'
            }
        }, {
            nama: 'PT. Code Bean',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12000000,
                description: 'Komputer Programmer'
            }
        }]
    }, {
        nama: 'Laptop i5 8G',
        supplierList: [{
            nama: 'PT. Bhineka Indonesia',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12000000,
                description: 'Komputer Programmer'
            }
        }, {
            nama: 'PT. Jakarta Notebook',
            item: {
                qty: 10,
                uom: 'Unit',
                price: 12050000,
                description: 'Komputer Programmer'
            }
        }]
    }];

    $scope.ok = function () {       
        $modalInstance.close('close');
    }
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});