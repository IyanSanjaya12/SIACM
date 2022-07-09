(function () {
    'use strict';

    angular
        .module('naut')
        .controller('HariLiburController', HariLiburController);

    function HariLiburController($translate, $scope, $http, $rootScope, $resource, $location, $state, RequestService, $stateParams, DTOptionsBuilder, DTColumnBuilder, DTInstances, $compile, $filter) {
        var vm = this;
        
        vm.hariLibur = ($stateParams.hariLibur != undefined) ? $stateParams.hariLibur : {}; 

     	vm.dtInstance = {};
        
        function callback(json) {
		    console.log(json);
		}
        
        vm.reloadData = function () {
			/*vm.nm="<span translate='promise.procurement.master.harilibur.list.NAMA'></span>";*/
		    var resetPaging = false;
		     
		    if(vm.dtInstance != null){
			    vm.dtInstance.reloadData(callback, resetPaging);
		    }
		};  

    	vm.dtOptions = RequestService.requestServerPagging(DTOptionsBuilder.newOptions(), '/procurement/master/hari-libur/get-by-pagination');
    	vm.dtOptions.withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    		$compile(nRow)($scope);
            var start = this.fnSettings()._iDisplayStart;
            var rowNum = start + iDisplayIndex + 1;
            $("td:first", nRow).html('<div class="text-center">' + rowNum + '</div>');
        })
        .withOption('headerCallback',function( thead, data, start, end, display ) {
                $compile(angular.element(thead).contents())($scope);
            });
    	vm.dtColumns = [
    		DTColumnBuilder.newColumn(null).withTitle('<span class="text-center" translate="template.tabel.nomor"></span>').notSortable(),
    		
    		DTColumnBuilder.newColumn('nama').withTitle('<div class="text-center"> <span  translate="promise.procurement.master.harilibur.list.nama"></span></div>').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div class="text-center"> <span  translate="promise.procurement.master.harilibur.list.tanggal"></span></div>').renderWith(function(data){
    			return $filter('date')(data.tanggal, 'dd-MM-yyyy', '');
    		}),
    		DTColumnBuilder.newColumn('deskripsi').withTitle('<div class="text-center"> <span  translate="promise.procurement.master.harilibur.list.deskripsi"></span></div>').renderWith(function(data){
    			return data;
    		}),
    		DTColumnBuilder.newColumn(null).withTitle('<div class="text-center" ><span translate="template.tabel.aksi"></span></div>').notSortable().renderWith(function (data, type, full, meta) {
    		/*document.getElementById("id01").innerHTML = "<span translate='promise.procurement.master.harilibur.list.NAMA'></span>";*/
	    		return ' <div class="text-center"><button ng-class="'+"'bg-'"+' + app.theme.name" class="btn btn-primary btn-sm btn-add" ng-click="edit('+data.id+')"></button> '+
	                	' <button class="btn btn-danger btn-sm btn-delete" ng-click="del('+data.id+')" > '+
	                    ' </button><div> '
    		})
    	];
    			
    	DTInstances.getLast().then(function(instance) {
            vm.dtInstance = instance;
        });
         	
        //cek status include sabtu/minggu 	
        vm.getIsInclude = function () {
        	
        	$http.get($rootScope.backendAddress + '/procurement/master/parameter/get-name/WEEKEND_DISABLE', {
                ignoreLoadingBar: true
            	}).success(function (data, status, headers, config) {
                    vm.isIncludeList = data;
                    vm.isInclude=false;
                    angular.forEach(vm.isIncludeList, function(check) {
                        if (vm.isIncludeList.nilai=='true'){
                        	vm.isInclude=true;
                        }
                    }); 
                    
                    $scope.loading = false;
                }).error(function (data, status, headers, config) {
                	RequestService.informInternalError();
                    $scope.loading = false;
                })
        }
        vm.getIsInclude();
        
        //set include sabtu/minggu
        vm.updateIsInclude=function(){
        	var postForm = {
    			id:vm.isIncludeList.id,
                nilai:vm.isInclude,
                keterangan:vm.isIncludeList.keterangan
            }
            var uri = "/procurement/master/parameter/update"
        	
        	RequestService.doPOST(uri, postForm)
        		.success(function (hariLibur) {
        			
        		})
        	
        }
         	
        $scope.add = function () {
            $state.go('app.promise.procurement-master-harilibur-view', {
				toDo : "add"
			});
        }
            
        $scope.edit = function (hariLiburId) {
        	
        	RequestService.doGET('/procurement/master/hari-libur/get-list-by-id/'+ hariLiburId)
			.then(function success(data) {
				$state.go('app.promise.procurement-master-harilibur-view', {
					hariLibur : data[0],
					toDo : "edit"
				});
			}, function error(response) {
				RequestService.informInternalError();
			});
        	
        }
            
            
        $scope.del = function (hariLiburId) {
        	var url = "/procurement/master/hari-libur/delete" 
        	var data = {};
        	
        	data.id = hariLiburId;
        	RequestService.deleteModalConfirmation()
        		.then(function(result) {
        			vm.loading = true;
        			RequestService.doPOST(url, data)
        				.success(function(data) {
							RequestService.informDeleteSuccess();
							vm.loading = false;
							vm.reloadData();	
						}).error(function(data, status, headers, config) {
							RequestService.informInternalError();
							vm.loading = false;
						})

			});
        };
    }

    HariLiburController.$inject = ['$translate','$scope', '$http', '$rootScope', '$resource', '$location', '$state','RequestService','$stateParams', 'DTOptionsBuilder', 'DTColumnBuilder', 'DTInstances', '$compile', '$filter'];

})();
