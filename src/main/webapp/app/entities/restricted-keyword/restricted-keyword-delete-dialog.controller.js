(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('RestrictedKeywordDeleteController',RestrictedKeywordDeleteController);

    RestrictedKeywordDeleteController.$inject = ['$uibModalInstance', 'entity', 'RestrictedKeyword'];

    function RestrictedKeywordDeleteController($uibModalInstance, entity, RestrictedKeyword) {
        var vm = this;

        vm.restrictedKeyword = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RestrictedKeyword.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
