(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('RestrictedKeywordDialogController', RestrictedKeywordDialogController);

    RestrictedKeywordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RestrictedKeyword'];

    function RestrictedKeywordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RestrictedKeyword) {
        var vm = this;

        vm.restrictedKeyword = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.restrictedKeyword.id !== null) {
                RestrictedKeyword.update(vm.restrictedKeyword, onSaveSuccess, onSaveError);
            } else {
                RestrictedKeyword.save(vm.restrictedKeyword, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('printarchiverApp:restrictedKeywordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
