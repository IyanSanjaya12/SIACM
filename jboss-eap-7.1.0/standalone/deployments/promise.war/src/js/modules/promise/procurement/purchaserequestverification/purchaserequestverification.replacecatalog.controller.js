(function() {
	'use strict';

	angular.module('naut').controller('PurchaseRequestReplaceKatalogController', PurchaseRequestReplaceKatalogController);

	function PurchaseRequestReplaceKatalogController($scope, $http, $modal, $rootScope, ngTableParams, $filter, $state, RequestService) {
		$scope.nameSearch = undefined;
		$scope.locationSearch = undefined;

		RequestService.doPOSTJSON('/procurement/catalog/CategoryServices/findAllWithTree', {}).then(function(data) {
			$scope.categoryList = data;
		});

		RequestService.doPOSTJSON('/procurement/catalog/CatalogServices/findCatalogByKontrak', {}).then(function(data) {
			$scope.tableItemList = new ngTableParams({
				page : 1, // show first page
				count : 10, // count per page
				sorting : {
					nama : 'asc' // initial sorting
				}
			}, {
				total : data.length, // length of data
				getData : function($defer, params) {
					// use build-in angular filter
					var orderedData = params.sorting() ? $filter('orderBy')(data, params.orderBy()) : data;

					$defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
				}
			});

			$scope.loading = false;
		});

		$scope.selectedItemList = [];
		$scope.selectItem = function(item) {
			var index = $scope.selectedItemList.indexOf(item);
			// console.log("index ??? "+index);
			if (index == -1) {
				$scope.selectedItemList.push(item);
			} else {
				$scope.selectedItemList.splice(index, 1);
			}
		};

		$scope.btnSelect = function(item) {

			var itemObj = {
				material : item,
				nama : item.namaIND,
				vendorId : item.vendor.id,
				kode : item.kodeProduk,
				deskripsi : item.deskripsiIND,
				vendor : item.vendor.nama,
				unit : item.satuan.nama,
				matauangId : item.mataUang.id,
				matauang : item.mataUang.kode,
				specification : item.deskripsi,
				costcenteritem : $rootScope.nomorDraft
			};

			var itemIsExist = false;
			angular.forEach($rootScope.itemPRList, function(value, index) {
				if ((typeof value.kode !== 'undefined') && (itemIsExist == false)) {
					if (value.kode == itemObj.kode) {
						itemIsExist = true;
						alert('Item Pengadaan "' + value.nama + '" dengan KODE : "' + value.kode + '" sudah dipilih!');
					}
				}
			});
			if (!itemIsExist) {

				angular.forEach($rootScope.itemPRList, function(value, index, values) {
					if (value.kode == $rootScope.prItemSelected.kode) {

						/*
						 * console.log('ASLI = ' + JSON.stringify(values[index]
						 * )); console.log('NEW = ' + JSON.stringify(itemObj));
						 * console.log('values[index].price = ' +
						 * values[index].price);
						 */

						itemObj.quantity = values[index].quantity;
						itemObj.price = values[index].price;
						itemObj.costcenteritem = values[index].costcenteritem;
                        itemObj.isEdited = true;
                        itemObj.prItemId=values[index].prItemId;
                        itemObj.isFromCatalog = true;
						values[index] = itemObj;
					}
				});

				$scope.ok();

			}

		}

	}

	PurchaseRequestReplaceKatalogController.$inject = [ '$scope', '$http', '$modal', '$rootScope', 'ngTableParams', '$filter', '$state', 'RequestService' ];

})();