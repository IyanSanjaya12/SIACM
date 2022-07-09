(function () {
	'use strict';

	angular
		.module('naut')
		.controller('ApprovalTahapanViewController', ApprovalTahapanViewController);

	function ApprovalTahapanViewController($scope, $http, $rootScope, $resource, $location, RequestService, $state, $stateParams, $filter, toaster) {
		var vm = this;
		vm.submitted = false;
		vm.organisasiId = null
		
		vm.approvalTahapan = ($stateParams.approvalTahapan != undefined) ? $stateParams.approvalTahapan : {};
		vm.toDo = ($stateParams.toDo != undefined) ? $stateParams.toDo : {};
		vm.thresholdList = [{}];
		vm.approvalTahapan.approvalTahapanDetailList=[];
		$scope.id = null;
		vm.valid = false;
		vm.isValid =[true,true,true,true,true];
		vm.unlimitedCeklis = false;
		vm.errorValidasi1 = '';
		vm.errorValidasi2 = '';
		vm.errorValidasi3 = '';
		vm.errorValidasi4 = '';
		vm.errorValidasi5 = '';
		vm.errorApproval = '';
		vm.isReadOnly = [false,false,false,false,false];
    	vm.isActive = [false,false,false,false,false];
    	vm.isInputReadOnly = [false,false,false,false,false];
		vm.approvalLevel = {endRange1: '', endRange2: '', endRange3: '', endRange4: '', endRange5: ''};
		vm.approvalTahapanDetail1 = {approval: {}, endRange: null};
		vm.approvalTahapanDetail2 = {approval: {}, endRange: null};
		vm.approvalTahapanDetail3 = {approval: {}, endRange: null};
		vm.approvalTahapanDetail4 = {approval: {}, endRange: null};
		vm.approvalTahapanDetail5 = {approval: {}, endRange: null};
		vm.approvalTahapanDetailTemp = [];
		
		
		 
		 $scope.getApprovalListById = function() {
			 
			 RequestService.doPOSTJSON('/procurement/approval/approval-tahapan-detail/get-list', vm.approvalTahapan.id)
				.then(function success(data) {
					if (data != undefined) {
						vm.approvalTahapan.approvalTahapanDetailList = data;
						if(vm.toDo == 'Edit'){
							angular.forEach(vm.approvalTahapan.approvalTahapanDetailList, function(data,index){
								if(index == 0 ){
									vm.approvalLevel.endRange1 = data.endRange;
								}if(index == 1 ){
									vm.approvalLevel.endRange2 = data.endRange;
								}if(index == 2 ){
									vm.approvalLevel.endRange3 = data.endRange;
								}if(index == 3 ){
									vm.approvalLevel.endRange4 = data.endRange;
								}if(index == 4 ){
									vm.approvalLevel.endRange5 = data.endRange;
								}
							});
							if(vm.approvalTahapan.approvalTahapanDetailList.length > 0 ){
								vm.isActive[(vm.approvalTahapan.approvalTahapanDetailList.length) - 1] = true;
								$scope.nonActiveIsChecked((vm.approvalTahapan.approvalTahapanDetailList.length) - 1,vm.isActive[(vm.approvalTahapan.approvalTahapanDetailList.length) - 1])
							}
						}
						$scope.setFormList();
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
			 
		 }
		 
		 $scope.getApprovalList = function () {
	 		 RequestService.doGET('/procurement/approval/get-list-by-organisasi/'+ vm.approvalTahapan.unit.id )
	 		 .then(function success(data) {
	 			 vm.approvalList = data;
	 		 }, function error(response) {
	 			 RequestService.informInternalError();
	 		 });
	 	 }
		 
		 if(vm.toDo == "Edit"){
		 	 $scope.getApprovalListById();
			 $scope.getApprovalList();
		 }
			
		 $scope.getOrganisasiList = function () {
				RequestService.doGET('/procurement/master/organisasi/get-list-parent')
					.then(function success(data) {
						vm.organisasiList = data;
					}, function error(response) {
						RequestService.informInternalError();
				});
			}
			$scope.getOrganisasiList();
			 $scope.setValueOrganisasiId = function () {
				 if (vm.approvalTahapan.organisasi != null){
					 vm.organisasiId = vm.approvalTahapan.organisasi.id;
						
				 }
				 
			}
			
		$scope.getUnitList = function (organisasi) {
			RequestService.doGET('/procurement/master/organisasi/get-all-list-child-by-parent-id-and-self/'+ organisasi.id)
				.then(function successCallback(data) {
					vm.unitList = data; 
				}, function errorCallback(response) {
					RequestService.informInternalError();
			});
			
		}
	
		 $scope.setType = function() {
			 vm.isThreshold = vm.approvalTahapan.tahapan.isThreshold;
		 }
		
		 if(vm.approvalTahapan.tahapan != null){
			 $scope.setType();
		 }
		 
		 $scope.setFormList = function(type) {
			var approval = {
						approvalType: {},
						id:null,
						jenis: null,
						name: null
					}
			if ( vm.isThreshold != null ){
					 RequestService.doPOSTJSON('/procurement/master/threshold/get-list-by-type', type)
					.then(function success(data) {
						if (data != undefined) {
							vm.thresholdList = data;
						}
					}, function error(response) {
						RequestService.informInternalError();
		            	vm.loading = false;
					});
				 
			 } else {
				 vm.thresholdList = [{}];
			 }
		 }
	 
		 $scope.clearUnitList= function(){
			 vm.unitList = [];
			 vm.approvalTahapan.approvalTahapanDetailList = [];
		 }
		 
		 $scope.clearApprovalDetailList= function(){
			 $scope.getApprovalList();
			 vm.approvalTahapan.approvalTahapanDetailList = [];
		 }
		 
		 $scope.reset=function(){
			 vm.approvalTahapan.tahapan=null;
			 vm.approvalTahapan.tipe =null;
			 vm.thresholdList = [{}];
			 $scope.choice = "";
			 vm.isThreshold = null; 
		 }
		 
		 
		 
		$scope.getTahapanList = function (organisasi) {
			RequestService.doPOSTJSON('/procurement/master/tahapan/get-unregistered-list',organisasi )
				.then(function success(data) {
					vm.tahapanList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		
		
		$scope.getApprovalTahapanList = function () {
			RequestService.doGET('/procurement/approval/approval-tahapan/get-list')
				.then(function success(data) {
					vm.approvalTahapanList = data;
				}, function error(response) {
					RequestService.informInternalError();
			});
		}
		$scope.getApprovalTahapanList();
		
		$scope.save = function(isActiveCheck) {
			vm.submitted = true;
				vm.approvalTahapanDetailTemp = [];
				for( var i= 0; i < vm.approvalTahapan.approvalTahapanDetailList.length; i++){
					if( i == 0){
						vm.approvalTahapanDetail1.approval = vm.approvalTahapan.approvalTahapanDetailList[i].approval;
						vm.approvalTahapanDetail1.endRange = vm.approvalLevel.endRange1;
						vm.approvalTahapanDetailTemp.push(vm.approvalTahapanDetail1);
					}if( i == 1){
						vm.approvalTahapanDetail2.approval = vm.approvalTahapan.approvalTahapanDetailList[i].approval;
						vm.approvalTahapanDetail2.endRange = vm.approvalLevel.endRange2;
						vm.approvalTahapanDetailTemp.push(vm.approvalTahapanDetail2);
					}if( i == 2){
						vm.approvalTahapanDetail3.approval = vm.approvalTahapan.approvalTahapanDetailList[i].approval;
						vm.approvalTahapanDetail3.endRange = vm.approvalLevel.endRange3;
						vm.approvalTahapanDetailTemp.push(vm.approvalTahapanDetail3);
					}if( i == 3){
						vm.approvalTahapanDetail4.approval = vm.approvalTahapan.approvalTahapanDetailList[i].approval;
						vm.approvalTahapanDetail4.endRange = vm.approvalLevel.endRange4;
						vm.approvalTahapanDetailTemp.push(vm.approvalTahapanDetail4);
					}if( i == 4){
						vm.approvalTahapanDetail5.approval = vm.approvalTahapan.approvalTahapanDetailList[i].approval;
						vm.approvalTahapanDetail5.endRange = vm.approvalLevel.endRange5;
						vm.approvalTahapanDetailTemp.push(vm.approvalTahapanDetail5);
					}
				}
				
				vm.approvalTahapan.approvalTahapanDetailList = [];
				vm.approvalTahapan.approvalTahapanDetailList = vm.approvalTahapanDetailTemp;
				
				var validTahapan = true;
				angular.forEach(vm.isActive, function(data, index){
					if(data){
						if(vm.approvalTahapan.approvalTahapanDetailList.length != index+1){
							validTahapan = false;
						}
					}
				});
				
			if($scope.validasiKosong(vm.isReadOnly) && $scope.validMaxAmount() && validTahapan && vm.approvalTahapan.approvalTahapanDetailList.length != 0){
				RequestService.modalConfirmation().then(function(result) {
					vm.loading = true;
					$scope.savedata();
				});
        	}
		}
		
        $scope.change=function(approvalTahapanDetail,approval){
        	if(approval != null && approval != undefined){
            	approvalTahapanDetail.approval = approval;
        	}
        }
        /* perubahan KAI 15/12/2020 [20443]*/
        $scope.validasiApprovalLevel = function(level){
        	for(var a=0; a < vm.approvalTahapan.approvalTahapanDetailList.length; a++){
        		if(a != level){
        			if(vm.approvalTahapan.approvalTahapanDetailList[level].approval.id == vm.approvalTahapan.approvalTahapanDetailList[a].approval.id){
        				RequestService.informError('Approval Level tidak boleh sama');
        				vm.approvalTahapan.approvalTahapanDetailList[level].approval = null;
        			}        		        			
        		}
        	}
        }
        
        $scope.nonActiveIsChecked = function(index, value){
        	vm.isReadOnly = [false,false,false,false,false];
        	vm.isActive = [false,false,false,false,false];
        	vm.isInputReadOnly = [false,false,false,false,false];
        	vm.endRange = [];
        	for ( var i=4; i > index; i-- ){
        		vm.isReadOnly[i] = value;
        		var indexRange =  i;
        		if (indexRange == 4){
        			vm.approvalLevel.endRange5 = '';
        			vm.errorValidasi5 = '';
        		}if (indexRange == 3){
        			vm.approvalLevel.endRange4 = '';
        			vm.errorValidasi4 = '';
        		}if (indexRange == 2){
        			vm.approvalLevel.endRange3 = '';
        			vm.errorValidasi3 = '';
        		}if (indexRange == 1){
        			vm.approvalLevel.endRange2 = '';
        			vm.errorValidasi2 = '';
        		}if (indexRange == 0){
        			vm.approvalLevel.endRange1 = '';
        			vm.errorValidasi1 = '';
        		}
        		
        		vm.endRange[i] = null;
        	}
        	
        	for ( var i=4; i > index; i-- ){	
        		var indexApproval =  i;
        		if ( vm.approvalTahapan.approvalTahapanDetailList[indexApproval] != null){
        			vm.approvalTahapan.approvalTahapanDetailList.splice(indexApproval, 1);
        		}
        				
        		vm.isInputReadOnly[i] = value;
        	}
        	
        	vm.isActive[index] = value;
        	
        }
        
        $scope.validasiKosong = function(isReadOnly){
			vm.valid = true;
			if(vm.isThreshold == 1){
				if (vm.approvalLevel.endRange1 == null || typeof vm.approvalLevel.endRange1 == 'null' || vm.approvalLevel.endRange1 == "" || vm.approvalLevel.endRange1 == 0) {
					vm.errorValidasi1 = 'Harus Diisi';
					vm.valid= false;
				}else{
					vm.errorValidasi1 = '';
				}
				if(isReadOnly[1] == false){
					if (vm.approvalLevel.endRange2 == null || typeof vm.approvalLevel.endRange2 == 'null' || vm.approvalLevel.endRange2 == "" || vm.approvalLevel.endRange2 == 0) {
						vm.errorValidasi2 = 'Harus Diisi';
						vm.valid= false;
					}else{
						vm.errorValidasi2 = '';
					}
				}
				if(isReadOnly[2] == false){
					if (vm.approvalLevel.endRange3 == null || typeof vm.approvalLevel.endRange3 == 'null' || vm.approvalLevel.endRange3 == "" || vm.approvalLevel.endRange3 == 0) {
						vm.errorValidasi3 = 'Harus Diisi';
						vm.valid= false;
					}else{
						vm.errorValidasi3 = '';
					}
				}
				if(isReadOnly[3] == false){
					if (vm.approvalLevel.endRange4 == null || typeof vm.approvalLevel.endRange4 == 'null' || vm.approvalLevel.endRange4 == "" || vm.approvalLevel.endRange4 == 0) {
						vm.errorValidasi4 = 'Harus Diisi';
						vm.valid= false;
					}else{
						vm.errorValidasi4 = '';
					}
				}
				if(isReadOnly[4] == false){
					if (vm.approvalLevel.endRange5 == null || typeof vm.approvalLevel.endRange5 == 'null' || vm.approvalLevel.endRange5 == "" || vm.approvalLevel.endRange5 == 0) {
						vm.errorValidasi5 = 'Harus Diisi';
						vm.valid= false;
					}else{
						vm.errorValidasi5 = '';
					}
				}
			}
			
			return vm.valid;
        }
 
        $scope.savedata = function () {
			var url = '';
			if(vm.toDo == "Add"){
				url = '/procurement/approval/approval-tahapan/insert';
            }
			else if(vm.toDo == "Edit"){
            	url = '/procurement/approval/approval-tahapan/update';
            }
			RequestService.doPOSTJSON(url, vm.approvalTahapan)
				.then(function success(data) {
					if (data != undefined) {
						if (data.isValid != undefined) {
							if (data.errorTahapanApproval != undefined) {
								vm.errorMessageApproval = 'promise.procurement.master.approvaltahapan.error.nama_approval_sama';
							}
							vm.loading = false;
						} else {
							RequestService.informSaveSuccess();
							$state.go('app.promise.procurement-master-approvaltahapan');
						}
					}
				}, function error(response) {
					RequestService.informInternalError();
	            	vm.loading = false;
				});
		}
        
        $scope.checkMaxAmount = function(maxAmount,thp){
        	var tampMaxAmount = parseInt(maxAmount);
        	var idx = 0;
        	angular.forEach(vm.isInputReadOnly,function(value){
        		if (value == true){
        			vm.isValid[idx]=true;
        		}
        	});
        	
        	if(thp == 1){
        		if((tampMaxAmount > parseInt(vm.approvalLevel.endRange2)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange3)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange4)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange5)) ){
        			vm.isValid[0] = false;
        		}else{
        			vm.isValid[0] = true;
        		}
        	}else if(thp == 2){
        		if((tampMaxAmount < parseInt(vm.approvalLevel.endRange1)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange3)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange4)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange5)) ){
        			vm.isValid[1] = false;
        		}else{
        			vm.isValid[1] = true;
        		}
        	}else if(thp == 3){
        		if((tampMaxAmount < parseInt(vm.approvalLevel.endRange1)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange2)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange4)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange5)) ){
        			vm.isValid[2] = false;
        		}else{
        			vm.isValid[2] = true;
        		}
        	}else if(thp == 4){
        		if((tampMaxAmount < parseInt(vm.approvalLevel.endRange1)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange2)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange3)) || (tampMaxAmount > parseInt(vm.approvalLevel.endRange5)) ){
        			vm.isValid[3] = false;
        		}else{
        			vm.isValid[3] = true;
        		}
        	}else if(thp == 5){
        		if((tampMaxAmount < parseInt(vm.approvalLevel.endRange1)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange2)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange3)) || (tampMaxAmount < parseInt(vm.approvalLevel.endRange4)) ){
        			vm.isValid[4] = false;
        		}else{
        			vm.isValid[4] = true;
        		}
        	}
        }
        
        $scope.validMaxAmount = function(){
        	var validMaxAmount = true;
			angular.forEach(vm.isValid, function(data){
    			if (data == false) {
    				validMaxAmount = false;
    			}
			});
			if (!validMaxAmount){
				RequestService.modalInformation('Tidak boleh lebih dari Max Amount level diatasnya dan Tidak boleh kurang dari Max Amount level dibawahnya','info');
//				RequestService.modalInformation('Tidak boleh lebih dari Max Amount level diatasnya dan Tidak boleh kurang dari Max Amount level dibawahnya','success');
			}
			return validMaxAmount;
        }
        
        $scope.back = function () {
        	$state.go('app.promise.procurement-master-approvaltahapan');
        }
	}

	ApprovalTahapanViewController.$inject = ['$scope', '$http', '$rootScope', '$resource', '$location', 'RequestService', '$state', '$stateParams', '$filter', 'toaster'];

})();