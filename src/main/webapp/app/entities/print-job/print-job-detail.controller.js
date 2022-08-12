(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobDetailController', PrintJobDetailController);

    PrintJobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrintJob'];

    function PrintJobDetailController($scope, $rootScope, $stateParams, previousState, entity, PrintJob) {
        var vm = this;

        vm.printJob = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('printarchiverApp:printJobUpdate', function(event, result) {
            vm.printJob = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
