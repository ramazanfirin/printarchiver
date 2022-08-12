(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobPageDeleteController',PrintJobPageDeleteController);

    PrintJobPageDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrintJobPage'];

    function PrintJobPageDeleteController($uibModalInstance, entity, PrintJobPage) {
        var vm = this;

        vm.printJobPage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PrintJobPage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
