(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobDetailController', PrintJobDetailController);

    PrintJobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrintJob','PrintJobPage','AlertService','$sce'];

    function PrintJobDetailController($scope, $rootScope, $stateParams, previousState, entity, PrintJob,PrintJobPage,AlertService,$sce ) {
        var vm = this;

        vm.printJob = entity;
        vm.previousState = previousState.name;
        vm.printJobPages = [];
			
		findByJob();
		
		vm.selectedPrintJobPage={};
		vm.context={};
	    vm.previous= previous;
		vm.next =next;
		vm.pageIndex;
		vm.showOnlyUnsafePage=true;
		vm.zoom = zoom;
		 
		function findByJob() {
            PrintJobPage.findByJobId({
                id: vm.printJob.id
            }, onfindJobRouteSuccess, onfindByJobError);
        }

		
		function onfindJobRouteSuccess(data, headers) {
			vm.printJobPages = data;
			if(vm.printJobPages.length>0){
				vm.pageIndex = 0;
				getContentOfPage();
				
			}	
		}
		
		function onfindByJobError(error) {
			 AlertService.error(error.data.message);
		}
////////////////////////////////////////////////////////
		function findContext() {
            PrintJobPage.findContext({
                id: vm.selectedPrintJobPage.id
            }, onfindContextSuccess, onfindContextError);
        }

		function onfindContextSuccess(data, headers) {
			vm.context= data.value;
			vm.context = $sce.trustAsHtml(vm.context);
			console.log("bitti");
		}

		function onfindContextError(error) {
			 AlertService.error(error);
		}

		function previous(){
			if(vm.showOnlyUnsafePage){
				findFirstPreviousUnsafePage();
			}
			else if(vm.pageIndex!=0){
				vm.pageIndex = vm.pageIndex -1;
			}	
			getContentOfPage();
		}
		
		function next(){
			if(vm.showOnlyUnsafePage){
				findFirstNextUnsafePage();	
			}
			else if(vm.pageIndex != vm.printJob.pageCount){
				vm.pageIndex = vm.pageIndex + 1;
			}
			getContentOfPage();
			
		}
		
		function findFirstNextUnsafePage(){
			for (let i = vm.pageIndex+1; i < vm.printJobPages.length; i++) {
 				 if(vm.printJobPages[i].resultStatus == 'NOT_SAFETY'){
 				    vm.pageIndex = i;
 				    break; 
				}
			}
		}
		
		function findFirstPreviousUnsafePage(){
			for (let i = vm.pageIndex-1; i >= 0; i--) {
 				 if(vm.printJobPages[i].resultStatus == 'NOT_SAFETY'){
 				    vm.pageIndex = i;
 				    break; 
				}
			}
		}

		function getContentOfPage(){
			vm.selectedPrintJobPage = vm.printJobPages[vm.pageIndex];
		    findContext(vm.selectedPrintJobPage.id);	
		}


		function zoom() {
			var imageId = document.getElementById('view');
			if (imageId.style.width == "300px") {
				imageId.style.width = "600px";
				imageId.style.height = "800px";
			} else {
				imageId.style.width = "300px";
				imageId.style.height = "400px";
			}
		}

        var unsubscribe = $rootScope.$on('printarchiverApp:printJobUpdate', function(event, result) {
            vm.printJob = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
