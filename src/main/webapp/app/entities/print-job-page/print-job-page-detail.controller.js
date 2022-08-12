(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .controller('PrintJobPageDetailController', PrintJobPageDetailController);

    PrintJobPageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrintJobPage', 'PrintJob'];

    function PrintJobPageDetailController($scope, $rootScope, $stateParams, previousState, entity, PrintJobPage, PrintJob) {
        var vm = this;

        vm.printJobPage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('printarchiverApp:printJobPageUpdate', function(event, result) {
            vm.printJobPage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
