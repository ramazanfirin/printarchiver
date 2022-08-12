(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('RestrictedKeywordDetailController', RestrictedKeywordDetailController);

    RestrictedKeywordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RestrictedKeyword'];

    function RestrictedKeywordDetailController($scope, $rootScope, $stateParams, previousState, entity, RestrictedKeyword) {
        var vm = this;

        vm.restrictedKeyword = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('printarchiverApp:restrictedKeywordUpdate', function(event, result) {
            vm.restrictedKeyword = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
