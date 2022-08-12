(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobDeleteController',PrintJobDeleteController);

    PrintJobDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrintJob'];

    function PrintJobDeleteController($uibModalInstance, entity, PrintJob) {
        var vm = this;

        vm.printJob = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PrintJob.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
