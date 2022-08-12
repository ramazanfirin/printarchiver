(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobPageDialogController', PrintJobPageDialogController);

    PrintJobPageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrintJobPage', 'PrintJob'];

    function PrintJobPageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrintJobPage, PrintJob) {
        var vm = this;

        vm.printJobPage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.printjobs = PrintJob.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.printJobPage.id !== null) {
                PrintJobPage.update(vm.printJobPage, onSaveSuccess, onSaveError);
            } else {
                PrintJobPage.save(vm.printJobPage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('printarchiverApp:printJobPageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
