(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobDialogController', PrintJobDialogController);

    PrintJobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrintJob'];

    function PrintJobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrintJob) {
        var vm = this;

        vm.printJob = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.printJob.id !== null) {
                PrintJob.update(vm.printJob, onSaveSuccess, onSaveError);
            } else {
                PrintJob.save(vm.printJob, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('printarchiverApp:printJobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.printDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
